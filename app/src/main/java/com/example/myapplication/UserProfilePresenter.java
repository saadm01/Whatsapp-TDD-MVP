package com.example.myapplication;

import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class UserProfilePresenter {
    private final UserProfileView userProfileView;

    private DatabaseReference userRef;
    private DatabaseReference messageRequestRef;
    private DatabaseReference contactsRef;
    private FirebaseAuth firebaseAuth;

    private String userID;
    private String reciverIdOfUser;
    private String currentState;

    UserProfilePresenter(UserProfileView userProfileView) {
        this.userProfileView = userProfileView;
    }


    void setup(String ReciverIdOfUser){
        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        messageRequestRef = FirebaseDatabase.getInstance().getReference().child("Message Requests");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        reciverIdOfUser = ReciverIdOfUser;
        currentState = "new";
        userID = firebaseAuth.getCurrentUser().getUid();
    }

    void getUserDetails(String ReciverIdOfUser){
        setup(ReciverIdOfUser);
        userRef.child(reciverIdOfUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("picture"))){
                    //Get current user username from database
                    String getUsername = dataSnapshot.child("name").getValue().toString();
                    //Get current user status from database
                    String getStatus = dataSnapshot.child("status").getValue().toString();
                    //Get current user profile picture from database
                    String getProfilePic = dataSnapshot.child("picture").getValue().toString();

                    //Display current user username
                    //Display current user status
                    //Display current user profile picture
                    display(getUsername, getStatus, getProfilePic);

                    manageChatRequests();
                    //userProfileView.IncomingMessageRequests();
                }
                else {
                    //Get current user username from database
                    String getUsername = dataSnapshot.child("name").getValue().toString();
                    //Get current user status from database
                    String getStatus = dataSnapshot.child("status").getValue().toString();

                    //Display current user username
                    //Display current user status
                    displayJustNameAndStatus(getUsername, getStatus);

                    manageChatRequests();

                    //userProfileView.IncomingMessageRequests();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void manageChatRequests() {
        setup(reciverIdOfUser);
        messageRequestRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(reciverIdOfUser)){
                    //Check to see if there has been any message requests
                    String confirmation = dataSnapshot.child(reciverIdOfUser).child("confirm").getValue().toString();
                    if(confirmation.equals("sent")){
                        //Change the current state of the request
                        currentState = "message request sent";
                        ///Allow user to go back on and cancel their message request
                        userProfileView.changeButtonTextToCancel();
                    }
                    //Receiver
                    else if(confirmation.equals("received")){
                        currentState = "message request received";
                        //Change button text to allow user to accept incoming message request
                        userProfileView.changeButtonToAccept();

                    }

                }
                else{
                    contactsRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(reciverIdOfUser)){
                                //Set state to friends
                                currentState = "friends";
                                //Set button to user to remove their friend
                                userProfileView.changeButtonToRemoveFriend();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        if (!userID.equals(reciverIdOfUser))
        {
            userProfileView.buttonClick();
        }
        else {
            userProfileView.makeButtonInvisible();
        }

    }

    void changeMessage(){
        userProfileView.changeButtonStateToFalse();

        //Communicating users are new to each other
        if(currentState.equals("new")) {
            sendMessage();
        }
        if(currentState.equals("message request sent")){

            cancelMessage();
        }
        if(currentState.equals("message request received")){
            acceptMessage();
        }
        if(currentState.equals("friends")) {
            removerMessage();
        }

    }

    public void removerMessage() {
        contactsRef.child(userID).child(reciverIdOfUser).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                //Receiver
                                contactsRef.child(reciverIdOfUser).child(userID).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    //Set state back to new as there is no longer any communication
                                                    currentState = "new";
                                                    //Set button back for messaging
                                                    userProfileView.changeButtonTextToSend();
                                                    //Remove the decline message button
                                                    userProfileView.hideDecline();

                                                }
                                            }
                                        });
                            }
                        }
                    });
    }

    public void acceptMessage() {
        //Accept message and contact request from sent user
        contactsRef.child(userID).child(reciverIdOfUser).child("Contacts").setValue("added")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Accept message and contact request from receiving user
                            contactsRef.child(reciverIdOfUser).child(userID).child("Contacts").setValue("added")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                //Remove message request from sender side as request has been accepted
                                                messageRequestRef.child(userID).child(reciverIdOfUser).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                //Remove message request from receiver side as request has been accepted
                                                                messageRequestRef.child(reciverIdOfUser).child(userID).removeValue()
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                //Set state to friends
                                                                                currentState = "friends";
                                                                                //Set button to user to remove their friend
                                                                                userProfileView.changeButtonToRemoveFriend();
                                                                                //Remove the decline message button
                                                                                userProfileView.hideDecline();

                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void cancelMessage() {
        messageRequestRef.child(userID).child(reciverIdOfUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Cancel message request from receiving user
                    messageRequestRef.child(reciverIdOfUser).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //Set state back to new as there is no longer any communication
                                currentState = "new";
                                //Set button back for messaging
                                userProfileView.changeButtonTextToSend();
                                //Remove the decline message button
                                userProfileView.hideDecline();
                            }
                        }
                    });
                }
            }
        });
    }

    public void sendMessage() {
        messageRequestRef.child(userID).child(reciverIdOfUser).child("confirm").setValue("sent")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //Receiver
                                messageRequestRef.child(reciverIdOfUser).child(userID).child("confirm").setValue("received")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    //Allow button to be clickable by user
                                                    userProfileView.changeButtonStateToTrue();
                                                    currentState = "message request sent";
                                                    //Allow user to cancel
                                                    userProfileView.changeButtonTextToCancel();
                                                }
                                            }
                                        });
                            }
                        }
                    });
    }




    void displayJustNameAndStatus(String username, String status) {
        userProfileView.displayHalfProfile(username, status);
    }

    void display(String username, String status, String profilePic) {
        userProfileView.displayProfile(username, status, profilePic);
    }
}
