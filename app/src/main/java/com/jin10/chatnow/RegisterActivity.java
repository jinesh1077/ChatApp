package com.jin10.chatnow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText username,email,password;
    private Button btn_register,imgSel;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    private Button btnChoose, btnUpload;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 10;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    String strp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=(EditText)findViewById(R.id.username);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        btn_register=(Button)findViewById(R.id.btn_register);
        imgSel=(Button)findViewById(R.id.imgSel);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        auth= FirebaseAuth.getInstance();
        strp="https://firebasestorage.googleapis.com/v0/b/chatnow-3f81f.appspot.com/o/imgh.jpg?alt=media&token=37f10b4a-d4f6-4bda-8b6d-346581e3e4b0";

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _username = username.getText().toString();
                String _email = email.getText().toString();
                String _password = password.getText().toString();


                if (TextUtils.isEmpty(_username) || TextUtils.isEmpty(_email) || TextUtils.isEmpty(_password)) {
                    Toast.makeText(RegisterActivity.this, "ALL FIELDS ARE REQUIERD", Toast.LENGTH_SHORT).show();
                } else if (_password.length() < 4) {
                    Toast.makeText(RegisterActivity.this, "Password Length Be atleast 4", Toast.LENGTH_SHORT).show();
                } else{
                    register(_username,_email,_password);
                }



            }
        });
        imgSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });


    }

    private void register(final String username, String email, String password){

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            String userId = firebaseUser.getUid();

                            reference= FirebaseDatabase.getInstance().getReference("User").child(userId);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("username",username);
                            hashMap.put("imageURL",strp);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){


                                        Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();

                                    }else{
                                        Toast.makeText(RegisterActivity.this,"Auth Error",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });


                        }else{
                            Toast.makeText(RegisterActivity.this,"Already Registered",Toast.LENGTH_SHORT).show();

                        }


                    }
                });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            uploadImage();
            //Toast.makeText(RegisterActivity.this,"ok",Toast.LENGTH_SHORT).show();


        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }



    private void imgLoad(){
        String _uid = email.getText().toString();

        StorageReference storageRef = storageReference.child(_uid);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                strp= String.valueOf(uri);
                //Toast.makeText(RegisterActivity.this,uri+"",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(RegisterActivity.this,"error",Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void uploadImage() {

        if(filePath != null)
        {
            String _uid = email.getText().toString();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(_uid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            imgLoad();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }






    }
