package com.example.myapplication;


import android.content.Intent;
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
public class ChatsFragment extends Fragment implements ChatsFragmentView{

    private ChatsFragmentPresenter chatsFragmentPresenter;


    private String userId;
    private View activeChatsView;
    private RecyclerView listOfActiveChats;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference chatsRef;
    private DatabaseReference usersRef;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initialisePresenter();


        // Inflate the layout for this fragment
        activeChatsView = inflater.inflate(R.layout.fragment_chats, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        chatsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(userId);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        InitialiseFields();

        return activeChatsView;
    }

    private void initialisePresenter() {
        chatsFragmentPresenter = new ChatsFragmentPresenter(this);
    }

    private void InitialiseFields() {

        listOfActiveChats = (RecyclerView) activeChatsView.findViewById(R.id.activeChats);
        listOfActiveChats.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        chatsFragmentPresenter.start();
    }

    public void userChatInfo(String idOfUsers, String[] getUserChatProfilePic, String getUserChatName){
        Intent privateChatIntent = new Intent(getContext(), MessageChatActivity.class);
        privateChatIntent.putExtra("chatUserId", idOfUsers);
        privateChatIntent.putExtra("chatPicture", getUserChatProfilePic[0]);
        privateChatIntent.putExtra("chatUsername", getUserChatName);
        startActivity(privateChatIntent);
    }

    public void sendAdapter( FirebaseRecyclerAdapter firebaseRecyclerAdapter){
        listOfActiveChats.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }





    public static class viewOfActiveChats extends RecyclerView.ViewHolder{

        TextView nameOfUser;
        TextView statusOfUser;
        CircleImageView pictureOfUser;

        public viewOfActiveChats(@NonNull View itemView) {
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

}
