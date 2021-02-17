package com.example.myapplication;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public interface SettingsView {
    void enterName();
    void enterStatus();
    void SendUserToMainPage();
    void succesMsg();
    void updateInfo();
    void errorMsg();
    void displayProfile(String username, String status, String profilePic);
    void displayHalfProfile(String username, String status);

}
