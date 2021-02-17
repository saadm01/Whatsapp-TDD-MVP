package com.example.myapplication;

import android.content.Intent;
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

public class ChatsFragmentPresenter {

    private final ChatsFragmentView chatsFragmentView;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference chatsRef;
    private DatabaseReference usersRef;
    private String userId;


    ChatsFragmentPresenter(ChatsFragmentView chatsFragmentView) {
        this.chatsFragmentView = chatsFragmentView;
    }

    void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        chatsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(userId);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    void start(){
        setup();
        FirebaseRecyclerOptions<Contacts> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(chatsRef, Contacts.class).build();

        FirebaseRecyclerAdapter<Contacts, ChatsFragment.viewOfActiveChats> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacts, ChatsFragment.viewOfActiveChats>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatsFragment.viewOfActiveChats viewOfActiveChats, int position, @NonNull Contacts contacts) {
                final String idOfUsers = getRef(position).getKey();
                final String[] getUserChatProfilePic = {"default_image"};
                usersRef.child(idOfUsers).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild("picture")) {
                                //Get all information about the user who has active chat
                                //Get chat user profile picture from database
                                getUserChatProfilePic[0] = dataSnapshot.child("picture").getValue().toString();

                                //Display details about the  user who has active chat
                                //Display profile picture of the user who has active chat
                                Picasso.get().load(getUserChatProfilePic[0]).into(viewOfActiveChats.pictureOfUser);
                            }

                            //Get active chat user username from database
                            final String getUserChatName = dataSnapshot.child("name").getValue().toString();
                            //Get active chat user status from database
                            final String getUserChatStatus = dataSnapshot.child("status").getValue().toString();

                            //Display user name of active chat
                            viewOfActiveChats.nameOfUser.setText(getUserChatName);


                            //Check if user is online
                            if(dataSnapshot.child("active").hasChild("online")) {
                                //Receive information about user being active and online
                                String online = dataSnapshot.child("active").child("online").getValue().toString();
                                String time = dataSnapshot.child("active").child("time").getValue().toString();
                                String date = dataSnapshot.child("active").child("date").getValue().toString();

                                if(online.equals("yes")){
                                    //Display last seen time for user of active chat
                                    viewOfActiveChats.statusOfUser.setText("User Is Online");
                                }
                                else if(online.equals("no")){
                                    //Display last seen time for user of active chat
                                    viewOfActiveChats.statusOfUser.setText("Last Seen: "  + date + " " + time);
                                }
                            }
                            else{
                                //If user has not updated aka old account then will show this msg
                                viewOfActiveChats.statusOfUser.setText("Not Updated");
                            }


                            viewOfActiveChats.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Send user to chat with select user
                                    //Will get user id and username and profile picture
                                    chatsFragmentView.userChatInfo(idOfUsers, getUserChatProfilePic, getUserChatName);
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @NonNull
            @Override
            public ChatsFragment.viewOfActiveChats onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //Display list of active chats
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_friends_layout, parent, false);
                return new ChatsFragment.viewOfActiveChats(view);
            }
        };

        //Always listening for any incoming changes
        chatsFragmentView.sendAdapter(firebaseRecyclerAdapter);
    }
}
