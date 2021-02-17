package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.Mock;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class UserProfilePresenterTest {


    @Test
    public void showUserDetails() {
        UserProfileView userProfileView = mock(UserProfileView.class);
        UserProfilePresenter userProfilePresenter = new UserProfilePresenter(userProfileView);
        userProfilePresenter.getUserDetails("userId");
        verify(userProfilePresenter).display("name", "status", "picture");
    }

    @Test
    public void showSomeUserDetails() {
        UserProfileView userProfileView = mock(UserProfileView.class);
        UserProfilePresenter userProfilePresenter = new UserProfilePresenter(userProfileView);
        userProfilePresenter.getUserDetails("userId");
        verify(userProfilePresenter).displayJustNameAndStatus("name", "status");
    }

    @Test
    public void cacnelMessgae() {
        UserProfileView userProfileView = mock(UserProfileView.class);
        UserProfilePresenter userProfilePresenter = new UserProfilePresenter(userProfileView);
        userProfilePresenter.cancelMessage();
        verify(userProfileView).changeButtonTextToSend();
    }

    @Test
    public void sendMessage() {
        UserProfileView userProfileView = mock(UserProfileView.class);
        UserProfilePresenter userProfilePresenter = new UserProfilePresenter(userProfileView);
        userProfilePresenter.sendMessage();
        verify(userProfileView).changeButtonTextToCancel();
    }

    @Test
    public void acceptMessage() {
        UserProfileView userProfileView = mock(UserProfileView.class);
        UserProfilePresenter userProfilePresenter = new UserProfilePresenter(userProfileView);
        userProfilePresenter.acceptMessage();
        verify(userProfileView).changeButtonToRemoveFriend();
    }

    @Test
    public void removerMessage() {
        UserProfileView userProfileView = mock(UserProfileView.class);
        UserProfilePresenter userProfilePresenter = new UserProfilePresenter(userProfileView);
        userProfilePresenter.removerMessage();
        verify(userProfileView).changeButtonTextToSend();
    }








}