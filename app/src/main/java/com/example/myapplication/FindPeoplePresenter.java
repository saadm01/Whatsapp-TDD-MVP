package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class FindPeoplePresenter {
    private final FindPeopleView findPeopleView;
    private DatabaseReference usersRef;


    FindPeoplePresenter(FindPeopleView findPeopleView) {
        this.findPeopleView = findPeopleView;
    }

    void start(){
        setup();
        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(usersRef, Contacts.class).build();
        FirebaseRecyclerAdapter<Contacts, FindPeopleActivity.ViewFindFriends> adapter = new FirebaseRecyclerAdapter<Contacts, FindPeopleActivity.ViewFindFriends>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindPeopleActivity.ViewFindFriends holder, final int position, @NonNull Contacts model) {
                //Set user details
                //Set name of user
                holder.nameOfUser.setText(model.getName());
                //Set status of user
                holder.statusOfUser.setText(model.getStatus());
                //Set profile picture of user
                Picasso.get().load(model.getPicture()).placeholder(R.drawable.profile_image).into(holder.pictureOfUser);

                findPeopleView.showSuccess();
                //Select user
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Find clicked on user
                        String clickedUser = getRef(position).getKey();
                        findPeopleView.SendUserToClickedOnUsersProfile(clickedUser);
                    }
                });
            }
            @NonNull
            @Override
            public FindPeopleActivity.ViewFindFriends onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //Display the list of possible friends
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_friends_layout, parent, false );
                FindPeopleActivity.ViewFindFriends viewFindFriendsHolder = new FindPeopleActivity.ViewFindFriends(view);
                findPeopleView.showSuccess();
                return  viewFindFriendsHolder;
            }
        };
        //Always listening for any incoming changes
        findPeopleView.recyclerFriendsView(adapter);
    }

    private void setup() {
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }


}
