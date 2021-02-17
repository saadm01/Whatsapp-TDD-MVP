package com.example.myapplication;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

interface UserProfileView {
    void displayProfile(String username, String status, String profilePic);
    void displayHalfProfile(String username, String status);
    void changeButtonTextToCancel();
    void changeButtonToAccept();
    void changeButtonToRemoveFriend();
    void changeButtonStateToFalse();
    void changeButtonStateToTrue();
    void changeButtonTextToSend();
    void hideDecline();
    void makeButtonInvisible();
    void buttonClick();

}
