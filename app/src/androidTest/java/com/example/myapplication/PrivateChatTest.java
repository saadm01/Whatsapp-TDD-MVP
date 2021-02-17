package com.example.myapplication;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.example.myapplication.TestSetup.login1;
import static com.example.myapplication.TestSetup.login2;
import static com.example.myapplication.TestSetup.pressBack;
import static com.example.myapplication.TestSetup.signUserOut;
import static java.lang.Thread.sleep;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class PrivateChatTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRuleMain = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mMainActivity = null;



    @Test
    public void seeChatsFragment() throws InterruptedException {
        login1();
        sleep(1000);
        onView(withText("Chats")).perform(click());
        onView(withText("Chats")).check(matches(isDisplayed()));
        signUserOut();
    }

    @Test
    public void seeChatOfContact() throws InterruptedException {
        login1();
        sleep(1000);
        onView(withText("Chats")).perform(click());
        onView(withText("saadsaad")).check(matches(isDisplayed()));
        signUserOut();
    }

    @Test
    public void seeChatOfContactOtherView() throws InterruptedException {
        login2();
        sleep(1000);
        onView(withText("Chats")).perform(click());
        onView(withText("egUser")).check(matches(isDisplayed()));
        signUserOut();
    }

    @Test
    public void clickOnChatOfContact() throws InterruptedException {
        login1();
        sleep(1000);
        onView(withText("Chats")).perform(click());
        onView(withText("saadsaad")).perform(click());
        sleep(1000);
        onView(withText("saadsaad")).check(matches(isDisplayed()));
        pressBack();
        signUserOut();
    }

    @Test
    public void clickOnChatOfContactOtherView() throws InterruptedException {
        login1();
        sleep(1000);
        onView(withText("Chats")).perform(click());
        onView(withText("egUser")).perform(click());
        sleep(1000);
        onView(withText("egUser")).check(matches(isDisplayed()));
        pressBack();
        signUserOut();
    }


    @Test
    public void msgChat() throws InterruptedException {
        login1();
        sleep(1000);
        onView(withText("Chats")).perform(click());
        onView(withText("saadsaad")).perform(click());
        sleep(1000);
        onView(withText("saadsaad")).check(matches(isDisplayed()));
        onView(withId(R.id.typeMessage)).perform(typeText("Example Hello"));
        onView(withId(R.id.send_message_button)).perform(click());
        sleep(1000);
        pressBack();
        pressBack();
        signUserOut();
    }




}
