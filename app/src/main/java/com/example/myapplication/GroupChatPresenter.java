package com.example.myapplication;

import android.text.TextUtils;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import androidx.annotation.NonNull;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class GroupChatPresenter {
    private final GroupChatView groupChatView;


   private String groupName;
    private String data;
    private String time;
    private String userId;
    private String username;

    private DatabaseReference nameOfGroupRef;
    private DatabaseReference keyOfGroupMessageRef;
    private DatabaseReference usersRef;
    private FirebaseAuth firebaseAuth;

    GroupChatPresenter(GroupChatView groupChatView) {
        this.groupChatView = groupChatView;
    }


    void passGroupName(String nameOfGroup){
        groupName = nameOfGroup;
    }

    void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        nameOfGroupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName);
    }

    void getUserDetails(){
        //Get sender's details
        setup();
        usersRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    username = dataSnapshot.child("name").getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    void storeMessageInDB(String newTextMessage){
        //Store sent message
        setup();
        String messgaeGroupKey = nameOfGroupRef.push().getKey();
        if(newTextMessage.isEmpty()){
            groupChatView.enterText();
        }
        else {
            Calendar calendarTime = Calendar.getInstance();
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
            time = simpleTimeFormat.format(calendarTime.getTime());

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyy");
            data = simpleDateFormat.format(calendar.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            nameOfGroupRef.updateChildren(groupMessageKey);

            keyOfGroupMessageRef = nameOfGroupRef.child(messgaeGroupKey);

            HashMap<String, Object> messageDetailsMap = new HashMap<>();
            messageDetailsMap.put("name", username);
            messageDetailsMap.put("message", newTextMessage);
            messageDetailsMap.put("date", data);
            messageDetailsMap.put("time", time);
            keyOfGroupMessageRef.updateChildren(messageDetailsMap);

        }
    }

    void showAnyExistingMessages(DataSnapshot dataSnapshot){
        //Show all messages
        setup();
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()){
            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            groupChatView.appendMsg(chatName, chatMessage, chatTime, chatDate);

        }
    }
}
