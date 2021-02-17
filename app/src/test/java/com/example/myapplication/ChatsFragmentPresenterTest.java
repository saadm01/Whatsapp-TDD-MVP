package com.example.myapplication;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class ChatsFragmentPresenterTest {

    @Test
    public void showListOfChats() {
        ChatsFragmentView chatsFragmentView = mock(ChatsFragmentView.class);
        ChatsFragmentPresenter chatsFragmentPresenter = new ChatsFragmentPresenter(chatsFragmentView);
        chatsFragmentPresenter.start();
    }




}