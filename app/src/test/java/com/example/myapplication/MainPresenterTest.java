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

public class MainPresenterTest {

    @Test
    public void findFriends()
    {
        MainView mainView= mock(MainView.class);
        MainPresenter mainPresenter = new MainPresenter(mainView);
        mainPresenter.SendUserToFindFriends();
        verify(mainView).sendUserToFindFriends();
    }

    @Test
    public void addNewGroup()
    {
        MainView mainView= mock(MainView.class);
        MainPresenter mainPresenter = new MainPresenter(mainView);
        mainPresenter.newGroup();
        verify(mainView).createNewGroup();
    }

    @Test
    public void settings()
    {
        MainView mainView= mock(MainView.class);
        MainPresenter mainPresenter = new MainPresenter(mainView);
        mainPresenter.sendUserToSettingsPage();
        verify(mainView).SendUserToSettingsPage();
    }

}