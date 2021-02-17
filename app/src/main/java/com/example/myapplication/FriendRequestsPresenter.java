package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class FriendRequestsPresenter {
    private final FriendRequestsView friendRequestsView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference chatRef;
    private DatabaseReference usersRef;
    private DatabaseReference contactsRef;

    private String userID;

    FriendRequestsPresenter(FriendRequestsView friendRequestsView) {
        this.friendRequestsView = friendRequestsView;
    }

    void setup(){

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        chatRef = FirebaseDatabase.getInstance().getReference().child("Message Requests");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
    }


    void start(){
        setup();
        FirebaseRecyclerOptions<Contacts> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(chatRef.child(userID), Contacts.class).build();

        FirebaseRecyclerAdapter<Contacts, FriendRequestsFragment.Requests> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacts, FriendRequestsFragment.Requests>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final FriendRequestsFragment.Requests requests, int position, @NonNull Contacts contacts) {
                //Show the accept and decline button to the user
                requests.itemView.findViewById(R.id.acceptRequest).setVisibility(View.VISIBLE);
                requests.itemView.findViewById(R.id.declineRequest).setVisibility(View.VISIBLE);

                final String idOfUsers = getRef(position).getKey();
                //Get received requested
                DatabaseReference requestTypeRef = getRef(position).child("confirm").getRef();

                requestTypeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()) {

                            String statusOfRequestType = dataSnapshot.getValue().toString();
                            //Check to see if the requests have been received
                            if (statusOfRequestType.equals("received")) {
                                usersRef.child(idOfUsers).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild("picture")) {
                                            //Get all information about the user who has sent the request
                                            //Get request user profile picture from database
                                            String getRequestProfilePic = dataSnapshot.child("picture").getValue().toString();

                                            //Display details about the user who has sent the request
                                            //Display profile picture of the user from request
                                            Picasso.get().load(getRequestProfilePic).into(requests.pictureOfUser);

                                        }
                                        //Get request user username from database
                                        final String getRequestName = dataSnapshot.child("name").getValue().toString();
                                        //Get request user status from database
                                        final String getRequestStatus = dataSnapshot.child("status").getValue().toString();

                                        //Display  user name of request
                                        requests.nameOfUser.setText(getRequestName);
                                        //Display status of the user from request
                                        requests.statusOfUser.setText(getRequestStatus);

                                        //User clicks accept
                                        requests.itemView.findViewById(R.id.acceptRequest).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //Update sender's contact list
                                                contactsRef.child(userID).child(idOfUsers).child("ContactStatus")
                                                        .setValue("added").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //Update contact list for request receiver user
                                                            contactsRef.child(idOfUsers).child(userID).child("ContactStatus")
                                                                    .setValue("added").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        //Update list of requests for sender
                                                                        chatRef.child(userID).child(idOfUsers)
                                                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    //Update list of requests for request receiver user
                                                                                    chatRef.child(idOfUsers).child(userID)
                                                                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                friendRequestsView.added();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        });

                                        //User clicks decline
                                        requests.itemView.findViewById(R.id.declineRequest).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //Update list of requests for sender
                                                //Will remove the request
                                                chatRef.child(userID).child(idOfUsers)
                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //Update list of requests for request receiver user
                                                            //Will remove the request
                                                            chatRef.child(idOfUsers).child(userID)
                                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        friendRequestsView.declined();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });

                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @NonNull
            @Override
            public FriendRequestsFragment.Requests onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //Display list of requests
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_friends_layout, parent, false);
                FriendRequestsFragment.Requests requests = new FriendRequestsFragment.Requests(view);
                return requests;
            }
        };

        friendRequestsView.sendAdaptar(firebaseRecyclerAdapter);

    }


}
