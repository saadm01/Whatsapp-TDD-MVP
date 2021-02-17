package com.example.myapplication;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

interface NumberLoginView {
    void showEnter();
    void showProgress();
    void sendNumber();
    void showEnterCode();
    void showDialogForCode();
    void dismiss();
}
