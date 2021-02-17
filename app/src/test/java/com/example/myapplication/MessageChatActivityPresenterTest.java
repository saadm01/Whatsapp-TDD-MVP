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

public class MessageChatActivityPresenterTest {

    @Test
    public void showChatWithUser() {
        MessageChatActivityView messageChatActivityView = mock(MessageChatActivityView.class);
        MessageChatActivityPresenter messageChatActivityPresenter = new MessageChatActivityPresenter(messageChatActivityView);
        messageChatActivityPresenter.ShowReceiverDetails("userReceiver", "userPicOfReceiver");
    }

    @Test
    public void showMsg() {
        MessageChatActivityView messageChatActivityView = mock(MessageChatActivityView.class);
        MessageChatActivityPresenter messageChatActivityPresenter = new MessageChatActivityPresenter(messageChatActivityView);
        messageChatActivityPresenter.sendMessageToUser("Hello");
    }

    @Test
    public void noText() {
        MessageChatActivityView messageChatActivityView = mock(MessageChatActivityView.class);
        MessageChatActivityPresenter messageChatActivityPresenter = new MessageChatActivityPresenter(messageChatActivityView);
        messageChatActivityPresenter.sendMessageToUser("");
        verify(messageChatActivityView).enterText();
    }





}