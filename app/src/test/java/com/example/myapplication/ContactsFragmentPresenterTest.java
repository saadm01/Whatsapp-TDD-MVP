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

public class ContactsFragmentPresenterTest {

    @Test
    public void showListOfContacts()
    {
        ContactsFragmentView contactsFragmentView = mock(ContactsFragmentView.class);
        ContactsFragmentPresenter contactsFragmentPresenter = new ContactsFragmentPresenter(contactsFragmentView);
        contactsFragmentPresenter.start();
    }

}