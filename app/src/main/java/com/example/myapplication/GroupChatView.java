package com.example.myapplication;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public interface GroupChatView {
    void enterText();
    void appendMsg(String chatName, String chatMessage, String chatTime, String chatDate);
}
