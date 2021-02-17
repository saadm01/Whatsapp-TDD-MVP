package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class UserProfileActivity extends AppCompatActivity implements UserProfileView {

    private UserProfilePresenter userProfilePresenter;

    private Button sendMessageButton;
    private Button declineMessageButton;
    private CircleImageView profilePicture;
    private TextView username;
    private TextView userStatus;

    private DatabaseReference userRef;
    private DatabaseReference messageRequestRef;
    private DatabaseReference contactsRef;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;

    private String userID;
    private String reciverIdOfUser;
    private String currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initialisePresenter();

        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        messageRequestRef = FirebaseDatabase.getInstance().getReference().child("Message Requests");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        reciverIdOfUser = getIntent().getExtras().get("clickedUser").toString();
        currentState = "new";
        userID = firebaseAuth.getCurrentUser().getUid();

        InitialiseFields();

        userProfilePresenter.getUserDetails(reciverIdOfUser);
    }

    private void initialisePresenter() {
        userProfilePresenter = new UserProfilePresenter(this);
    }

    private void InitialiseFields() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("User Profile");

        username = (TextView) findViewById(R.id.see_user_profile_name);
        userStatus = (TextView) findViewById(R.id.see_user_profile_status);
        sendMessageButton = (Button) findViewById(R.id.send_message_to_seen_user);
        declineMessageButton = (Button) findViewById(R.id.decline_message_request);
        profilePicture = (CircleImageView) findViewById(R.id.see_user_profile_picture);
    }

    @Override
    public void displayProfile(String getUsername, String getStatus, String getProfilePic) {
        username.setText(getUsername);
        //Display current user status
        userStatus.setText(getStatus);
        //Display current user profile picture
        Picasso.get().load(getProfilePic).placeholder(R.drawable.profile_image).into(profilePicture);
    }

    @Override
    public void displayHalfProfile(String getUsername, String getStatus) {
        username.setText(getUsername);
        //Display current user status
        userStatus.setText(getStatus);

    }
    @Override
    public void changeButtonTextToCancel() {
        sendMessageButton.setText("Cancel Message Request");
    }

    @Override
    public void buttonClick() {
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfilePresenter.changeMessage();
            }
        });
    }
    @Override
    public void changeButtonToAccept() {
        sendMessageButton.setText("Accept Message");
        //Show user the decline button
        declineMessageButton.setEnabled(true);
        declineMessageButton.setVisibility(View.VISIBLE);

    }
    @Override
    public void changeButtonToRemoveFriend() {
        sendMessageButton.setEnabled(true);
        sendMessageButton.setText("Remove Friend");
    }

    @Override
    public void changeButtonStateToFalse() {
        sendMessageButton.setEnabled(false);
    }
    @Override
    public void changeButtonStateToTrue() {
        sendMessageButton.setEnabled(true);

    }
    @Override
    public void changeButtonTextToSend() {
        sendMessageButton.setText("Send Message");
        sendMessageButton.setEnabled(true);
    }
    @Override
    public void hideDecline() {
        declineMessageButton.setEnabled(false);
        declineMessageButton.setVisibility(View.INVISIBLE);
    }
    @Override
    public void makeButtonInvisible() {

        sendMessageButton.setVisibility(View.INVISIBLE);
    }
}
