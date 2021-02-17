package com.example.myapplication;

import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class MessageChatActivityPresenter {
    private final MessageChatActivityView messageChatActivityView;

    MessageChatActivityPresenter(MessageChatActivityView messageChatActivityView) {
        this.messageChatActivityView = messageChatActivityView;
    }

    void ShowReceiverDetails(String usernameOfReceiver, String pictureOfReceiver){
        messageChatActivityView.showReceiverDetails(usernameOfReceiver, pictureOfReceiver);
    }

    void lastSeen(DataSnapshot dataSnapshot){
        if(dataSnapshot.child("active").hasChild("online")) {
            //Receive information about user being active and online
            String online = dataSnapshot.child("active").child("online").getValue().toString();
            String time = dataSnapshot.child("active").child("time").getValue().toString();
            String date = dataSnapshot.child("active").child("date").getValue().toString();

            if(online.equals("yes")){
                //Display last seen time for user of active chat
                messageChatActivityView.userIsOnline();
            }
            else if(online.equals("no")){
                //Display last seen time for user of active chat
                messageChatActivityView.userIsOffline(date, time);
            }
        }
        else{
            //If user has not updated aka old account then will show this msg
            messageChatActivityView.updateApp();
        }
    }

    void sendMessageToUser(String textOfMessage){
        if(textOfMessage.isEmpty()){
            messageChatActivityView.enterText();
        }

    }
}
