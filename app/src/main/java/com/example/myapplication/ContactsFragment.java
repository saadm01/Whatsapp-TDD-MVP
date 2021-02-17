package com.example.myapplication;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
public class ContactsFragment extends Fragment implements ContactsFragmentView{

    private ContactsFragmentPresenter contactsFragmentPresenter;


    private RecyclerView contacts;
    private View ContactsView;

    private String userId;

    private DatabaseReference contactsRef;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initialisePresenter();
        // Inflate the layout for this fragment
        ContactsView = inflater.inflate(R.layout.fragment_contants, container, false);

        InitialiseFields();

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(userId);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        
        return ContactsView;
    }

    private void initialisePresenter() {
        contactsFragmentPresenter = new ContactsFragmentPresenter(this);
    }

    private void InitialiseFields() {
        contacts = (RecyclerView) ContactsView.findViewById(R.id.listOfContacts);
        contacts.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        contactsFragmentPresenter.start();
    }


    public static class ContactsViewHolder extends RecyclerView.ViewHolder{

        TextView nameOfUser;
        TextView statusOfUser;
        CircleImageView pictureOfUser;

        public ContactsViewHolder(@NonNull View itemView) {
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

    public void sendAdapter( FirebaseRecyclerAdapter firebaseRecyclerAdapter){
        contacts.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
}
