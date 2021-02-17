/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

package com.example.myapplication;

public interface LoginView {
    void showEnterEmail();
    void showEnterPassword();
    void progressDialog();
    void showSucess();
    void SendUserToMainPage();
    void progressDialogDismiss();
    void showErrorMsg();
    void SendUserToRegisterPage();
    void SendUserToPhoneLoginPage();

}
