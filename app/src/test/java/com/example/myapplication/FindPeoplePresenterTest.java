package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.junit.Assert;
import org.junit.Test;

import androidx.annotation.NonNull;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class FindPeoplePresenterTest {

    private  FindPeopleView findPeopleView;
    private DatabaseReference usersRef;

    void getUserRef(){
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }



    FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(usersRef, Contacts.class).build();

    FirebaseRecyclerAdapter<Contacts, FindPeopleActivity.ViewFindFriends> adapter = new FirebaseRecyclerAdapter<Contacts, FindPeopleActivity.ViewFindFriends>(options) {
        @Override
        protected void onBindViewHolder(@NonNull FindPeopleActivity.ViewFindFriends holder, final int position, @NonNull Contacts model) {


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



    @Test
    public void seeScuessForFullLsit()
    {
        FindPeopleView findPeopleView= mock(FindPeopleView.class);
        FindPeoplePresenter findPeoplePresenter = new FindPeoplePresenter(findPeopleView);
        findPeoplePresenter.start();
        verify(findPeopleView).showSuccess();
    }

    @Test
    public void seePeople()
    {
        FindPeopleView findPeopleView= mock(FindPeopleView.class);
        FindPeoplePresenter findPeoplePresenter = new FindPeoplePresenter(findPeopleView);
        findPeoplePresenter.start();
        verify(findPeopleView).recyclerFriendsView(adapter);
    }

    @Test
    public void clickOnOwnProfile()
    {
        FindPeopleView findPeopleView = mock(FindPeopleView.class);
        FindPeoplePresenter findPeoplePresenter = new FindPeoplePresenter(findPeopleView);
        findPeopleView.SendUserToClickedOnUsersProfile("currentUser");
        verify(findPeopleView).SendUserToClickedOnUsersProfile("currentUser");

    }

    @Test
    public void clickOnUserProfile()
    {
        FindPeopleView findPeopleView = mock(FindPeopleView.class);
        FindPeoplePresenter findPeoplePresenter = new FindPeoplePresenter(findPeopleView);
        findPeopleView.SendUserToClickedOnUsersProfile("currentUser");
        verify(findPeopleView).SendUserToClickedOnUsersProfile("currentUser");

    }

}