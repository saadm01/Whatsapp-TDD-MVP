package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class ContactsFragmentPresenter {
    private final ContactsFragmentView contactsFragmentView;

    private String userId;

    private DatabaseReference contactsRef;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;

    ContactsFragmentPresenter(ContactsFragmentView contactsFragmentView) {
        this.contactsFragmentView = contactsFragmentView;
    }

    void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(userId);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    void start(){
        setup();
        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(contactsRef, Contacts.class).build();

        FirebaseRecyclerAdapter<Contacts, ContactsFragment.ContactsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacts, ContactsFragment.ContactsViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsFragment.ContactsViewHolder contactsView, int position, @NonNull Contacts contacts) {
                //Get contact
                String idOfUsers = getRef(position).getKey();
                usersRef.child(idOfUsers).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //If contact has profile picture set
                        if(dataSnapshot.hasChild("picture")){
                            //Get all information about the contact
                            //Get contact user username from database
                            String getContactName = dataSnapshot.child("name").getValue().toString();
                            //Get contact user status from database
                            String getContactStatus = dataSnapshot.child("status").getValue().toString();
                            //Get contact user profile picture from database
                            String getContactProfilePic = dataSnapshot.child("picture").getValue().toString();

                            //Display contact details
                            //Display name of contact
                            contactsView.nameOfUser.setText(getContactName);
                            //Display status of contact
                            contactsView.statusOfUser.setText(getContactStatus);
                            //Display profile picture of contact
                            Picasso.get().load(getContactProfilePic).into(contactsView.pictureOfUser);
                        }
                        //If contact has no profile picture
                        else {
                            //Get contact user username from database
                            String getContactName = dataSnapshot.child("name").getValue().toString();
                            //Get contact user status from database
                            String getContactStatus = dataSnapshot.child("status").getValue().toString();

                            //Display contact details
                            //Display name of contact
                            contactsView.nameOfUser.setText(getContactName);
                            //Display status of contact
                            contactsView.statusOfUser.setText(getContactStatus);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @NonNull
            @Override
            public ContactsFragment.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //Display the list of contacts
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_friends_layout, parent, false );
                ContactsFragment.ContactsViewHolder contactsViewHolder = new ContactsFragment.ContactsViewHolder(view);
                return  contactsViewHolder;
            }
        };

        //Always listening for any incoming changes
        contactsFragmentView.sendAdapter(firebaseRecyclerAdapter);

    }

}
