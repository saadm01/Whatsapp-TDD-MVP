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

public class NumberLoginPresenterTest {

    @Test
    public void correctNumber()
    {
        NumberLoginView numberLoginView= mock(NumberLoginView.class);
        NumberLoginPresenter numberLoginPresenter = new NumberLoginPresenter(numberLoginView);
        numberLoginPresenter.sendVerificationNumber("++xxxxxxxxxxxx");
        verify(numberLoginView).showProgress();
    }

    @Test
    public void missingNumber()
    {
        NumberLoginView numberLoginView= mock(NumberLoginView.class);
        NumberLoginPresenter numberLoginPresenter = new NumberLoginPresenter(numberLoginView);
        numberLoginPresenter.sendVerificationNumber("");
        verify(numberLoginView).showEnter();
    }

    @Test
    public void missingCode()
    {
        NumberLoginView numberLoginView= mock(NumberLoginView.class);
        NumberLoginPresenter numberLoginPresenter = new NumberLoginPresenter(numberLoginView);
        numberLoginPresenter.sendVerificationCode("");
        verify(numberLoginView).showEnterCode();
    }

    @Test
    public void correctCode()
    {
        NumberLoginView numberLoginView= mock(NumberLoginView.class);
        NumberLoginPresenter numberLoginPresenter = new NumberLoginPresenter(numberLoginView);
        numberLoginPresenter.sendVerificationCode("");
        verify(numberLoginView).showDialogForCode();
    }
}