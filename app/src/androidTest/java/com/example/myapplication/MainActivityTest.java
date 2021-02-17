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

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRuleMain = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mMainActivity = null;


    public ActivityTestRule<UserProfileActivity> mUserProfileActivityRule = new ActivityTestRule<UserProfileActivity>(UserProfileActivity.class);
    private UserProfileActivity mUserProfileActivity = null;

    Instrumentation.ActivityMonitor monitorSettings = InstrumentationRegistry.getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorLogout = InstrumentationRegistry.getInstrumentation().addMonitor(LoginActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorFriends = InstrumentationRegistry.getInstrumentation().addMonitor(UserProfileActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorGroupsChat = InstrumentationRegistry.getInstrumentation().addMonitor(GroupChatActivity.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {
        mMainActivity = mActivityTestRuleMain.getActivity();
        mUserProfileActivity = mUserProfileActivityRule.getActivity();
    }




    @Test
    public void moveToChatFragment() throws InterruptedException {
        login();
        sleep(1000);
        onView(withText("Chats")).perform(click());
        onView(withText("Chats")).check(matches(isDisplayed()));
        signUserOut();
    }


    @Test
    public void moveToGroupsFragment() throws InterruptedException {
        login();
        sleep(1000);
        onView(withText("GROUPS")).perform(click());
        onView(withText("Groups")).check(matches(isDisplayed()));
        signUserOut();
    }

    @Test
    public void moveToContactsFragment() throws InterruptedException {
        login();
        sleep(1000);
        onView(withText("CONTACTS")).perform(click());
        signUserOut();
    }


    @Test
    public void moveToRequestsFragment() throws InterruptedException {
        login();
        sleep(1000);
        onView(withText("CONTACTS")).perform(click());
        signUserOut();
    }


    @Test
    public void clickSignOut() throws InterruptedException {
        login();
        sleep(1000);
        signUserOut();
    }


    @Test
    public void clickMenuOptionsSettings() throws InterruptedException {
        login();
        sleep(1000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Settings")).perform(click());
        onView(withId(R.id.set_username)).check(matches(withHint("username")));
        Activity settings = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorSettings, 5000);
        assertNotNull(settings);
        settings.finish();
        signUserOut();
    }


    @Test
    public void findFriends() throws InterruptedException {
        login();
        sleep(1000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Find Friends")).perform(click());
        sleep(1000);
        onView(withText("Find New Friends")).check(matches(isDisplayed()));
        pressBack();
        signUserOut();
    }



    @After
    public void tearDown() throws Exception {
        mMainActivity = null;
        mUserProfileActivity = null;
    }
}