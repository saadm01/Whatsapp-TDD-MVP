package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class FriendRequestsPresenterTest {

    @Test
    public void showListOfRequets()
    {
        FriendRequestsView friendRequestsView = mock(FriendRequestsView.class);
        FriendRequestsPresenter friendRequestsPresenter = new FriendRequestsPresenter(friendRequestsView);
        friendRequestsPresenter.start();
    }


    @Test
    public void acceptRequest()
    {
        FriendRequestsView friendRequestsView = mock(FriendRequestsView.class);
        FriendRequestsPresenter friendRequestsPresenter = new FriendRequestsPresenter(friendRequestsView);
        friendRequestsPresenter.start();
        verify(friendRequestsView).added();

    }

    @Test
    public void declineRequest()
    {
        FriendRequestsView friendRequestsView = mock(FriendRequestsView.class);
        FriendRequestsPresenter friendRequestsPresenter = new FriendRequestsPresenter(friendRequestsView);
        friendRequestsPresenter.start();
        verify(friendRequestsView).declined();
    }

}