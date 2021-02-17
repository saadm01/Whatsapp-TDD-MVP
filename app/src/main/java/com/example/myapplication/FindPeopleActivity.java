package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class FindPeopleActivity extends AppCompatActivity implements FindPeopleView{

    private DatabaseReference usersRef;

    private RecyclerView recyclerFriendsView;
    private Toolbar toolbar;

    private FindPeoplePresenter findPeoplePresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);

        initialisePresenter();

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        InitialiseFields();
    }

    private void initialisePresenter() {
        findPeoplePresenter = new FindPeoplePresenter(this);
    }

    private void InitialiseFields() {

        toolbar = (Toolbar) findViewById(R.id.find_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find New Friends");

        recyclerFriendsView = (RecyclerView) findViewById(R.id.friends_list);
        recyclerFriendsView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        findPeoplePresenter.start();
    }

    public static class ViewFindFriends extends RecyclerView.ViewHolder{

        TextView nameOfUser;
        TextView statusOfUser;
        CircleImageView pictureOfUser;


        public ViewFindFriends(@NonNull View itemView) {
            super(itemView);

            //Get user details
            //Get name of user
            nameOfUser = itemView.findViewById(R.id.name_of_user);
            //Get status of user
            statusOfUser = itemView.findViewById(R.id.status_of_user);
            //Get profile picture of user
            pictureOfUser = itemView.findViewById(R.id.profile_pics_of_users);
        }
    }

    @Override
    public void SendUserToClickedOnUsersProfile(String clickedUser) {
        Intent profileIntent = new Intent(FindPeopleActivity.this, UserProfileActivity.class);
        profileIntent.putExtra("clickedUser", clickedUser);
        startActivity(profileIntent);
    }

    @Override
    public void recyclerFriendsView(FirebaseRecyclerAdapter adapter) {
        recyclerFriendsView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void showSuccess() {
        //Toast.makeText(this, "List Of Possible Friends", Toast.LENGTH_SHORT).show();
    }


}
