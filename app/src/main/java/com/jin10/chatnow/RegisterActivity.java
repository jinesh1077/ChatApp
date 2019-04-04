package com.jin10.chatnow;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText username,email,password;
    private Button btn_register;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=(EditText)findViewById(R.id.username);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        btn_register=(Button)findViewById(R.id.btn_register);
        auth= FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _username = username.getText().toString();
                String _email = email.getText().toString();
                String _password = password.getText().toString();

                if(TextUtils.isEmpty(_username)||TextUtils.isEmpty(_email)||TextUtils.isEmpty(_password)){
                    Toast.makeText(RegisterActivity.this,"ALL FIELDS ARE REQUIERD",Toast.LENGTH_SHORT).show();
                }else if(_password.length()<4){
                    Toast.makeText(RegisterActivity.this,"Password Length Be atleast 4",Toast.LENGTH_SHORT).show();
                }else{
                    register(_username,_email,_password);
                }



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
                            hashMap.put("imageURL","default");

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



}
