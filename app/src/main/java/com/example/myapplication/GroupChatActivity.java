package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import androidx.appcompat.widget.Toolbar;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class GroupChatActivity extends AppCompatActivity implements GroupChatView{

    private GroupChatPresenter groupChatPresenter;

    private ScrollView scrollView;
    private TextView messages;
    private Toolbar toolbar;
    private ImageButton sendMessage;
    private EditText textMessage;

    private String groupName;
    private String data;
    private String time;
    private String userId;
    private String username;

    private DatabaseReference nameOfGroupRef;
    private DatabaseReference keyOfGroupMessageRef;
    private DatabaseReference usersRef;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        initialisePresenter();


        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        groupName = getIntent().getExtras().get("groupChatName").toString();
        Toast.makeText(GroupChatActivity.this, groupName, Toast.LENGTH_SHORT).show();

        nameOfGroupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName);

        InitialiseFields();

        groupChatPresenter.passGroupName(groupName);
        groupChatPresenter.getUserDetails();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send message
                String newTextMessage = textMessage.getText().toString();
                groupChatPresenter.storeMessageInDB(newTextMessage);
                textMessage.setText("");
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });
    }

    private void initialisePresenter() {
        groupChatPresenter = new GroupChatPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        nameOfGroupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    groupChatPresenter.showAnyExistingMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    groupChatPresenter.showAnyExistingMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void InitialiseFields() {
        scrollView = (ScrollView) findViewById(R.id.scrollViewGroups);
        messages = (TextView) findViewById(R.id.group_chat_text);
        toolbar = (Toolbar) findViewById(R.id.group_chat_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(groupName);
        sendMessage = (ImageButton) findViewById(R.id.send_group_message_button);
        textMessage = (EditText) findViewById(R.id.group_message);
    }


    @Override
    public void enterText(){
        Toast.makeText(this, "Type message..", Toast.LENGTH_SHORT).show();
    }
    public void appendMsg(String chatName, String chatMessage, String chatTime, String chatDate){
        messages.append(chatName + ":\n" + chatMessage + "\n" + chatTime + "     " + chatDate + "\n\n\n");
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }
}
