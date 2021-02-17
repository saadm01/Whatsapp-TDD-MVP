package com.example.myapplication;

import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.NonNull;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

 class LoginPresenter {

    private final LoginView loginView;
     private FirebaseAuth firebaseAuth;
     private DatabaseReference usersRef;


    LoginPresenter(LoginView loginView) {
        //Initialise login view
        this.loginView = loginView;
    }


    void loginUser(String email, String password) {
        //Login for user
        if(email.isEmpty()){
            loginView.showEnterEmail();
            return;
        }
        if(password.isEmpty()){
            loginView.showEnterPassword();
        }
        else {
            firebaseAuth = FirebaseAuth.getInstance();
            usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
            loginView.progressDialog();
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Get userId
                                String userId = firebaseAuth.getCurrentUser().getUid();
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                usersRef.child(userId).child("token").setValue(deviceToken)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    loginView.showSucess();
                                                    loginView.SendUserToMainPage();
                                                    loginView.progressDialogDismiss();
                                                }
                                            }
                                        });
                            }
                            else {
                                loginView.showErrorMsg();
                            }
                        }
                    });
        }
    }

    void sendUserToRegisterPage(){
        loginView.SendUserToRegisterPage();
     }

     void sendUserToPhoneLoginPage(){
        loginView.SendUserToPhoneLoginPage();
     }

}
