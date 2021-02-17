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

public class RegisterPresenterTest {

    @Test
    public void missingPassword()
    {
        RegisterView registerView= mock(RegisterView.class);
        RegisterPresenter registerPresenter = new RegisterPresenter(registerView);
        registerPresenter.CreateNewAccount("xyz","");
        verify(registerView).showEnterPassword();
    }

    @Test
    public void passwordLenght()
    {
        RegisterView registerView= mock(RegisterView.class);
        RegisterPresenter registerPresenter = new RegisterPresenter(registerView);
        registerPresenter.CreateNewAccount("xyz","abc");
        verify(registerView).ShowPasswrodNotLong();
    }


}