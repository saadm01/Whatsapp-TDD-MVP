package com.example.myapplication;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public interface RegisterView {
    void showEnterEmail();
    void showEnterPassword();
    void ShowPasswrodNotLong();
    void loadingBarShow();
    void SendUserToMainPage();
    void showSucessMsg();
    void dismissLoadingBar();

}
