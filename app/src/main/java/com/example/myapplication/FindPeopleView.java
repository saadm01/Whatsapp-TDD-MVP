package com.example.myapplication;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

interface FindPeopleView {
    void SendUserToClickedOnUsersProfile(String clickedUser);
    void recyclerFriendsView(FirebaseRecyclerAdapter adapter);
    void showSuccess();
}
