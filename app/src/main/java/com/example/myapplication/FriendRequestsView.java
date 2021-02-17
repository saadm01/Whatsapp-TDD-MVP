package com.example.myapplication;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

import com.firebase.ui.database.FirebaseRecyclerAdapter;

public interface FriendRequestsView {
    void sendAdaptar(FirebaseRecyclerAdapter firebaseRecyclerAdapter);
    void added();
    void declined();
}
