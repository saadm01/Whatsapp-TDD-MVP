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

class RegisterPresenter {

    private final RegisterView registerView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef;


    RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
    }

    void CreateNewAccount(String email, String password) {


        if(email.isEmpty()){
            registerView.showEnterEmail();
        }
        if(password.isEmpty()){
            registerView.showEnterPassword();
        }
        if(password.length() < 8){
            registerView.ShowPasswrodNotLong();
        }
        else {

            firebaseAuth = FirebaseAuth.getInstance();
            rootRef = FirebaseDatabase.getInstance().getReference();

            registerView.loadingBarShow();

            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String currentUserID = firebaseAuth.getCurrentUser().getUid();
                                rootRef.child("Users").child(currentUserID).setValue("");

                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                rootRef.child("Users").child(currentUserID).child("token").setValue(deviceToken);

                                registerView.SendUserToMainPage();
                                registerView.showSucessMsg();
                                registerView.dismissLoadingBar();

                            }

                        }
                    });
        }

    }

}
