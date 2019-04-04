package com.jin10.chatnow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private EditText chat;
    private ImageButton send;
    DatabaseReference reference;

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Chat> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String _username=getIntent().getStringExtra("username");
        final String _id=getIntent().getStringExtra("userid");
        setTitle(_username);
        chat=(EditText)findViewById(R.id.chat);
        send=(ImageButton) findViewById(R.id.send);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        recyclerView= findViewById(R.id.rec_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = chat.getText().toString();
                if(str.equals("")){
                    Toast.makeText(ChatActivity.this,"Type Something",Toast.LENGTH_SHORT).show();

                }else{
                    sendMessage(firebaseUser.getUid(),_id,str);
                    //Toast.makeText(ChatActivity.this,firebaseUser.getUid()+" "+_id,Toast.LENGTH_SHORT).show();

                }

                chat.setText("");
            }
        });


        reference =FirebaseDatabase.getInstance().getReference("User").child(_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessage(firebaseUser.getUid(),_id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void sendMessage(String sender,String receiver,String message){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        ref.child("Chats").push().setValue(hashMap);

    }

    private void readMessage(final String myId,final String userId){

        chats= new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chatObj=snapshot.getValue(Chat.class);



                    //Toast.makeText(ChatActivity.this,snapshot.toString(),Toast.LENGTH_LONG).show();
                    if((chatObj.getReceiver().equals(myId) && chatObj.getSender().equals(userId)) ||
                            (chatObj.getReceiver().equals(userId) && chatObj.getSender().equals(myId)) ){
                        chats.add(chatObj);
                    }

                    messageAdapter = new MessageAdapter(ChatActivity.this,chats);
                    recyclerView.setAdapter(messageAdapter);

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




}
