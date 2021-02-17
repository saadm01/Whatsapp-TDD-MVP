package com.example.myapplication;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import androidx.annotation.NonNull;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

class SettingsPresenter {

    private final SettingsView settingsView;
    //User may only have one profile picture at one time
    private static final int pic =1;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef;
    private StorageReference profilePicOfUsersRef;
    private EditText username, status;
    private String currentUserID;
    private CircleImageView profilePic;

    SettingsPresenter(SettingsView settingsView) {
        this.settingsView = settingsView;
    }

    void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        profilePicOfUsersRef = FirebaseStorage.getInstance().getReference().child("Profile Pics");
    }


    void updateAccount(String setUsername, String setStatus){
        setup();
        checkValues(setUsername, setStatus);

        HashMap<String, Object> profileMap = new HashMap<>();
        profileMap.put("uid", currentUserID);
        profileMap.put("name", setUsername);
        profileMap.put("status", setStatus);
        rootRef.child("Users").child(currentUserID).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Send user to the main page of the application and display success message
                    sendUserToMainPage();
                    sucess();
                }
                else {
                    //Display error message
                    showErrorMsg();
                }
            }
        });
    }


    void getUserInfo(){
        setup();
        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Get details of current active user
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("picture")))){
                    //Get current user username from database
                    String getUsername = dataSnapshot.child("name").getValue().toString();
                    //Get current user status from database
                    String getStatus = dataSnapshot.child("status").getValue().toString();
                    //Get current user profile picture from database
                    String getProfilePic = dataSnapshot.child("picture").getValue().toString();

                    //Display current user username, current user status, current user profile picture
                    display(getUsername, getStatus, getProfilePic);

                }
                else if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){
                    //Get current user username from database
                    String getUsername = dataSnapshot.child("name").getValue().toString();
                    //Get current user status from database
                    String getStatus = dataSnapshot.child("status").getValue().toString();

                    //Display current user username, current user status
                    displayJustNameAndStatus(getUsername, getStatus);
                }
                else {
                    //Display information message
                    updateMsg();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    void showErrorMsg() {
        settingsView.errorMsg();
    }


    void checkValues(String name, String status){
        if(name.isEmpty()){
            settingsView.enterName();
        }
        if(status.isEmpty()){
            settingsView.enterStatus();
        }
    }
    void sendUserToMainPage(){
        settingsView.SendUserToMainPage();
    }

    void sucess(){
        settingsView.succesMsg();
    }

    void updateMsg(){
        settingsView.updateInfo();
    }
    void display(String username, String status, String profilePic){
        settingsView.displayProfile(username, status, profilePic);
    }
    void displayJustNameAndStatus(String username, String status){
        settingsView.displayHalfProfile(username, status);
    }


}
