package com.example.myapplication;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;


import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.example.myapplication.TestSetup.login;
import static com.example.myapplication.TestSetup.pressBack;
import static com.example.myapplication.TestSetup.signUserOut;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class FindFriendsTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    private LoginActivity mActivity = null;


    public ActivityTestRule<MainActivity> mActivityTestRuleMain = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mMainActivity = null;


    Instrumentation.ActivityMonitor monitorMain = InstrumentationRegistry.getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);



    @Test
    public void clickFindFriends() throws InterruptedException{
        login();
        Activity main = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorMain, 5000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Find Friends")).perform(click());
        sleep(1000);
        onView(withText("Find New Friends")).check(matches(isDisplayed()));
        sleep(1000);
        pressBack();
        signUserOut();
    }

    @Test
    public void findFriendsView() throws InterruptedException{
        login();
        Activity main = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorMain, 5000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Find Friends")).perform(click());
        sleep(2000);
        onView(withText("Find New Friends")).check(matches(isDisplayed()));
        sleep(2000);
        onView(withText("exampleOrginal")).check(matches(isDisplayed()));
        pressBack();
        signUserOut();
    }

    @Test
    public void clickOnOwnProfile() throws InterruptedException{
        login();
        Activity main = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorMain, 5000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Find Friends")).perform(click());
        sleep(1000);
        onView(withText("Find New Friends")).check(matches(isDisplayed()));
        sleep(2000);
        onView(withText("egUser")).perform(click());
        sleep(2000);
        onView(withText("User Profile")).check(matches(isDisplayed()));
        pressBack();
        pressBack();
        signUserOut();
    }

    @Test
    public void sendMsgRequest() throws InterruptedException{
        login();
        Activity main = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorMain, 5000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Find Friends")).perform(click());
        sleep(1000);
        onView(withText("Find New Friends")).check(matches(isDisplayed()));
        sleep(2000);
        onView(withText("saaduser")).perform(click());
        sleep(2000);
        onView(withText("Send Message")).perform(click());
        onView(withText("Cancel Message Request")).check(matches(isDisplayed()));
        pressBack();
        pressBack();
        signUserOut();
    }



    @Test
    public void zcancelMsgRequest() throws InterruptedException{
        login();
        Activity main = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorMain, 5000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Find Friends")).perform(click());
        sleep(1000);
        onView(withText("Find New Friends")).check(matches(isDisplayed()));
        sleep(2000);
        onView(withText("saaduser")).perform(click());
        sleep(2000);
        onView(withText("Cancel Message Request")).perform(click());
        sleep(1000);
        onView(withText("Send Message")).check(matches(isDisplayed()));
        pressBack();
        pressBack();
        signUserOut();
    }



}



