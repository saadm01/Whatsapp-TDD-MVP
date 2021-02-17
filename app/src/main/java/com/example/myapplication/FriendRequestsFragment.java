package com.example.myapplication;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendRequestsFragment extends Fragment implements FriendRequestsView{

    private FriendRequestsPresenter friendRequestsPresenter;


    private View requestsView;
    private RecyclerView requestsList;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference chatRef;
    private DatabaseReference usersRef;
    private DatabaseReference contactsRef;

    private String userID;

    public FriendRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initialisePresenter();

        // Inflate the layout for this fragment
        requestsView = inflater.inflate(R.layout.fragment_friend_requests, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        chatRef = FirebaseDatabase.getInstance().getReference().child("Message Requests");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");

        InitialiseFields();

        return requestsView;
    }

    private void initialisePresenter() {
        friendRequestsPresenter = new FriendRequestsPresenter(this);
    }

    private void InitialiseFields() {

        requestsList = (RecyclerView) requestsView.findViewById(R.id.requests);
        requestsList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        friendRequestsPresenter.start();
    }



    public static class Requests extends RecyclerView.ViewHolder{

        TextView nameOfUser;
        TextView statusOfUser;
        CircleImageView pictureOfUser;
        Button acceptRequest;
        Button declineRequest;

        public Requests(@NonNull View itemView) {
            super(itemView);

            //Get user details
            //Get name of user
            nameOfUser = itemView.findViewById(R.id.name_of_user);
            //Get status of user
            statusOfUser = itemView.findViewById(R.id.status_of_user);
            //Get profile picture of user
            pictureOfUser = itemView.findViewById(R.id.profile_pics_of_users);

            acceptRequest = itemView.findViewById(R.id.acceptRequest);
            declineRequest = itemView.findViewById(R.id.declineRequest);

        }
    }

    public void sendAdaptar( FirebaseRecyclerAdapter firebaseRecyclerAdapter){
        requestsList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
    public void added(){
        Toast.makeText(getContext(), " New contact successfully added", Toast.LENGTH_SHORT).show();
    }
    public void declined(){
        Toast.makeText(getContext(), " Request Declined", Toast.LENGTH_SHORT).show();
    }
}
