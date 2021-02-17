package com.example.myapplication;

import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class NumberLoginPresenter {
    private final NumberLoginView numberLoginView;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private FirebaseAuth firebaseAuth;

    NumberLoginPresenter(NumberLoginView numberLoginView) {
        this.numberLoginView = numberLoginView;
    }

    void sendVerificationNumber(String phoneNumber){
        if (phoneNumber.isEmpty())
        {
            numberLoginView.showEnter();
        }
        else
        {
            numberLoginView.showProgress();

            numberLoginView.sendNumber();
        }
    }

    void sendVerificationCode(String verificationCode){
        if (verificationCode.isEmpty())
        {
            numberLoginView.showEnterCode();
        }
        else
        {
            numberLoginView.showDialogForCode();
        }
    }



}
