package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class SettingsActivity extends AppCompatActivity implements SettingsView {

    private SettingsPresenter settingsPresenter;

    private Button updateProfile;
    private EditText username, status;
    private String currentUserID;
    private CircleImageView profilePic;
    private ProgressDialog progressDialog;


    //User may only have one profile picture at one time
    private static final int pic =1;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef;
    private StorageReference profilePicOfUsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialisePresenter();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        profilePicOfUsersRef = FirebaseStorage.getInstance().getReference().child("Profile Pics");


        InitialiseFields();

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsPresenter.updateAccount(username.getText().toString(), status.getText().toString());
            }
        });

        //Get user details of currently logged in user
        settingsPresenter.getUserInfo();

        //User wants to change profile picture
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePic();
            }
        });

    }

    private void initialisePresenter() {
        settingsPresenter = new SettingsPresenter(this);
    }


    public void InitialiseFields(){
        updateProfile = (Button) findViewById(R.id.update_profile_button);
        username = (EditText) findViewById(R.id.set_username);
        status = (EditText) findViewById(R.id.set_profile_status);
        profilePic = (CircleImageView)findViewById(R.id.set_profile_image);
        progressDialog = new ProgressDialog(this);
    }



    private void changePic() {
        //Start new intent for changing user profile picture
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //Open new intent for getting profile picture once user has clicked on the profile picture
        startActivityForResult(intent,pic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Crop api has loaded
        if(requestCode==pic && resultCode==RESULT_OK && data!=null){
            //Get image from internal phone storage
            Uri imgUri = data.getData();

            //Start picker to get image for cropping and then use the image in cropping activity
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){

                progressDialog.setTitle("Profile Picture");
                progressDialog.setMessage("Profile picture is loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                //UriResult contains the cropped image
                Uri uriResult = result.getUri();

                //Store file path of image for the current user
                final StorageReference filepath = profilePicOfUsersRef.child(currentUserID + ".jpg");
                filepath.putFile(uriResult).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String urlOfStoredPic = uri.toString();
                                rootRef.child("Users").child(currentUserID).child("picture").setValue(urlOfStoredPic)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                //Profile picture has saved in the database successfully
                                                //Display success message
                                                Toast.makeText(SettingsActivity.this, "Profile Picture Stored Successfully..." , Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                            else {
                                                //Display error message
                                                String errorMessage = task.getException().toString();
                                                Toast.makeText(SettingsActivity.this, "Error: " + errorMessage , Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public void SendUserToMainPage() {
        //Send user to the main page of the application
        Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void enterName() {
        Toast.makeText(this, "Enter visible username for others to see...", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void enterStatus() {
        Toast.makeText(this, "Write a status be creative and have fun...", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void succesMsg() {
        Toast.makeText(SettingsActivity.this, "Profile and Account Updated Successfully...", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void updateInfo() {
        Toast.makeText(SettingsActivity.this, "Update Information" , Toast.LENGTH_SHORT).show();
    }
    @Override
    public void errorMsg() {
        Toast.makeText(SettingsActivity.this, "Error" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayProfile(String getUsername, String getStatus, String getProfilePic) {
        username.setText(getUsername);
        //Display current user status
        status.setText(getStatus);
        //Display current user profile picture
        Picasso.get().load(getProfilePic).placeholder(R.drawable.profile_image).into(profilePic);
    }

    @Override
    public void displayHalfProfile(String getUsername, String getStatus) {
        username.setText(getUsername);
        //Display current user status
        status.setText(getStatus);

    }
}