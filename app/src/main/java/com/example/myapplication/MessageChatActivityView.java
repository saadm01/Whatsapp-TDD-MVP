package com.example.myapplication;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

interface MessageChatActivityView {
    void showReceiverDetails(String usernameOfReceiver, String pictureOfReceiver);
    void userIsOnline();
    void userIsOffline(String date, String time);
    void updateApp();
    void enterText();

}
