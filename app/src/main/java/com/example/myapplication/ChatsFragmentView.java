package com.example.myapplication;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

import com.firebase.ui.database.FirebaseRecyclerAdapter;

interface ChatsFragmentView {
    void userChatInfo(String idOfUsers, String[] getUserChatProfilePic, String getUserChatName);
    void sendAdapter(FirebaseRecyclerAdapter firebaseRecyclerAdapter);

}
