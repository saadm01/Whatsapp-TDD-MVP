package com.example.myapplication;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class SettingsPresenterTest {


    @Test
    public void updateNameAndStatus()
    {
        SettingsView settingsView= mock(SettingsView.class);
        SettingsPresenter settingsPresenter = new SettingsPresenter(settingsView);
        settingsPresenter.updateAccount("username","userstatus");
        verify(settingsView).succesMsg();
    }




    @Test
    public void missingName() {
        SettingsView settingsView= mock(SettingsView.class);
        SettingsPresenter settingsPresenter = new SettingsPresenter(settingsView);
        settingsPresenter.checkValues("","status");
        verify(settingsView).enterName();
    }

    @Test
    public void missingStatus() {
        SettingsView settingsView= mock(SettingsView.class);
        SettingsPresenter settingsPresenter = new SettingsPresenter(settingsView);
        settingsPresenter.checkValues("name","");
        verify(settingsView).enterStatus();
    }

    @Test
    public void sendUserToMainPage()
    {
        SettingsView settingsView= mock(SettingsView.class);
        SettingsPresenter settingsPresenter = new SettingsPresenter(settingsView);
        settingsPresenter.sendUserToMainPage();
        verify(settingsView).SendUserToMainPage();
    }

    @Test
    public void successMsg()
    {
        SettingsView settingsView= mock(SettingsView.class);
        SettingsPresenter settingsPresenter = new SettingsPresenter(settingsView);
        settingsPresenter.sucess();
        verify(settingsView).succesMsg();
    }

    @Test
    public void updateMsg()
    {
        SettingsView settingsView= mock(SettingsView.class);
        SettingsPresenter settingsPresenter = new SettingsPresenter(settingsView);
        settingsPresenter.updateMsg();
        verify(settingsView).updateInfo();
    }
}