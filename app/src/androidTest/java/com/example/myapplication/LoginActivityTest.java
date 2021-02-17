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
import static com.example.myapplication.TestSetup.signUserOut;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    private LoginActivity mActivity = null;

    public ActivityTestRule<UserProfileActivity> mUserProfileActivityRule = new ActivityTestRule<UserProfileActivity>(UserProfileActivity.class);
    private UserProfileActivity mUserProfileActivity = null;

    Instrumentation.ActivityMonitor monitorSettings = InstrumentationRegistry.getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);

    Instrumentation.ActivityMonitor monitorRegister = InstrumentationRegistry.getInstrumentation().addMonitor(RegisterActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorMain = InstrumentationRegistry.getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorPhone = InstrumentationRegistry.getInstrumentation().addMonitor(LoginActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorLogin = InstrumentationRegistry.getInstrumentation().addMonitor(LoginActivity.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {

        mActivity = mActivityTestRule.getActivity();
    }



    @Test
    public void checkLoginView(){
        View view = mActivity.findViewById(R.id.login_email);
        assertNotNull(view);
    }

    @Test
    public void checkLoginEmailHint(){
        onView(withId(R.id.login_email)).check(matches(withHint("Email...")));
    }
    @Test
    public void checkLoginEmailText(){
        onView(withId(R.id.login_email)).perform(typeText("exmaple@example.com"));
    }

    @Test
    public void checkLoginPasswordHint(){
        onView(withId(R.id.login_password)).check(matches(withHint("Password...")));
    }
    @Test
    public void checkLoginPasswordText(){
        onView(withId(R.id.login_password)).perform(typeText("Exmaple1"));
    }


    @Test
    public void checkNewAccountLink(){
        assertNotNull(mActivity.findViewById(R.id.new_account));
        onView(withId(R.id.new_account)).perform(click());
        Activity register = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorRegister, 5000);
        assertNotNull(register);
        register.finish();
    }

    @Test
    public void phoneRegisterPage(){
        assertNotNull(mActivity.findViewById(R.id.number_login_button));
        onView(withId(R.id.number_login_button)).perform(click());
        Activity registerPhone = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorPhone, 5000);
        assertNotNull(registerPhone);
        registerPhone.finish();
    }


    @Test
    public void signIn(){
        onView(withId(R.id.login_email)).perform(typeText("example@example.com"));
        onView(withId(R.id.login_password)).perform(typeText("Example1"));
        onView(withId(R.id.login_button)).perform(click());
        onView(withText("Sign In"))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity()
                .getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        Activity main = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorMain, 5000);
        signUserOut();
        main.finish();
    }



    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}