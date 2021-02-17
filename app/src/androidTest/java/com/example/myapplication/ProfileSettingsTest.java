package com.example.myapplication;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.os.SystemClock;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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
import static com.example.myapplication.TestSetup.signUserOut;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class ProfileSettingsTest {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    private LoginActivity mLoginActivity = null;

    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mMainActivity = null;

    Instrumentation.ActivityMonitor monitorMain = InstrumentationRegistry.getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorLogin = InstrumentationRegistry.getInstrumentation().addMonitor(LoginActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorSettings = InstrumentationRegistry.getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);




    @Before
    public void setUp() throws Exception {
        mLoginActivity = mLoginActivityTestRule.getActivity();
        mMainActivity = mMainActivityTestRule.getActivity();
    }

    @Test
    public void seeProfile() throws InterruptedException {
        login();
        sleep(1000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Settings")).perform(click());
        onView(withId(R.id.set_username)).check(matches(withHint("username")));
        onView(withId(R.id.update_profile_button)).perform(click());
        Activity main = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorMain, 5000);
        sleep(1000);
        signUserOut();
    }

    @Test
    public void updateUsername() throws InterruptedException {
        login();
        sleep(1000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Settings")).perform(click());
        Activity settings = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorSettings, 5000);
        assertNotNull(settings);
        onView(withId(R.id.set_username)).perform(clearText());
        onView(withId(R.id.set_username)).perform(typeText("egUser"));
        onView(withId(R.id.update_profile_button)).perform(click());
        Activity main = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorMain, 5000);
        settings.finish();
        signUserOut();
    }


    @Test
    public void updateStatus() throws InterruptedException {
        login();
        sleep(1000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Settings")).perform(click());
        Activity settings = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorSettings, 5000);
        assertNotNull(settings);
        onView(withId(R.id.set_profile_status)).perform(clearText());
        onView(withId(R.id.set_profile_status)).perform(typeText("egStatus"));
        sleep(1000);
        onView(withId(R.id.update_profile_button)).perform(click());
        Activity main = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorMain, 5000);
        settings.finish();
        signUserOut();
    }



    @Test
    public void updateUsernameAndStatus() throws InterruptedException {
        login();
        sleep(1000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Settings")).perform(click());
        Activity settings = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorSettings, 5000);
        assertNotNull(settings);
        onView(withId(R.id.set_username)).perform(clearText());
        onView(withId(R.id.set_profile_status)).perform(clearText());
        onView(withId(R.id.set_username)).perform(typeText("exampleOfUser"));
        onView(withId(R.id.set_profile_status)).perform(typeText("I'm a status"));
        onView(withId(R.id.update_profile_button)).perform(click());
        Activity main = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorMain, 5000);
        settings.finish();
        signUserOut();
    }

    @After
    public void tearDown() throws Exception {
        mLoginActivity = null;
        mMainActivity = null;
    }
}
