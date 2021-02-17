package com.example.myapplication;

import org.junit.Test;
import org.mockito.Mock;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.*;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class LoginPresenterTest {



    @Test
    public void correctUsernameAndPassword()
    {
        LoginView loginView= mock(LoginView.class);
        LoginPresenter loginPresenter = new LoginPresenter(loginView);
        loginPresenter.loginUser("user@user.com","password");
        verify(loginView).showSucess();
    }

    @Test
    public void sendToMainPage()
    {
        LoginView loginView= mock(LoginView.class);
        LoginPresenter loginPresenter = new LoginPresenter(loginView);
        loginPresenter.loginUser("user@user.com","password");
        verify(loginView).SendUserToMainPage();
    }

    @Test
    public void showDialog()
    {
        LoginView loginView= mock(LoginView.class);
        LoginPresenter loginPresenter = new LoginPresenter(loginView);
        loginPresenter.loginUser("user@user.com","password");
        verify(loginView).progressDialog();
    }


    @Test
    public void incorrectUsernameAndPassword()
    {
        LoginView loginView= mock(LoginView.class);
        LoginPresenter loginPresenter = new LoginPresenter(loginView);
        loginPresenter.loginUser("user@.com","password");
        verify(loginView).showSucess();
    }


    @Test
    public void missingPassword()
    {
        LoginView loginView= mock(LoginView.class);
        LoginPresenter loginPresenter = new LoginPresenter(loginView);
        loginPresenter.loginUser("xyz","");
        verify(loginView).showEnterPassword();
    }

    @Test
    public void missingEmail()
    {
        LoginView loginView= mock(LoginView.class);
        LoginPresenter loginPresenter = new LoginPresenter(loginView);
        loginPresenter.loginUser("","password");
        verify(loginView).showEnterEmail();
    }

    @Test
    public void sendToRegisterPage()
    {
        LoginView loginView= mock(LoginView.class);
        LoginPresenter loginPresenter = new LoginPresenter(loginView);
        loginPresenter.sendUserToRegisterPage();
        verify(loginView).SendUserToRegisterPage();
    }

    @Test
    public void sendToPhoneRegisterPage()
    {
        LoginView loginView= mock(LoginView.class);
        LoginPresenter loginPresenter = new LoginPresenter(loginView);
        loginPresenter.sendUserToPhoneLoginPage();
        verify(loginView).SendUserToPhoneLoginPage();
    }
}