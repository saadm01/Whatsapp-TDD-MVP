package com.example.myapplication;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class MainPresenter {
    private final MainView mainView;

    MainPresenter(MainView mainView) {
        this.mainView = mainView;
    }

    void SendUserToFindFriends(){
        mainView.sendUserToFindFriends();
    }

    void newGroup(){
        mainView.createNewGroup();
    }

    void sendUserToSettingsPage(){
        mainView.SendUserToSettingsPage();
    }



}
