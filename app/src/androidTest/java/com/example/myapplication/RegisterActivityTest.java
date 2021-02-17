package com.example.myapplication;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import androidx.test.rule.ActivityTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.example.myapplication.TestSetup.signUserOut;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import static org.junit.Assert.*;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityTestRule = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);
    private RegisterActivity mActivity = null;

    public ActivityTestRule<UserProfileActivity> mUserProfileActivityRule = new ActivityTestRule<UserProfileActivity>(UserProfileActivity.class);
    private UserProfileActivity mUserProfileActivity = null;

    Instrumentation.ActivityMonitor monitorSettings = InstrumentationRegistry.getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);


    Instrumentation.ActivityMonitor monitor = InstrumentationRegistry.getInstrumentation().addMonitor(RegisterActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        mUserProfileActivity = mUserProfileActivityRule.getActivity();
    }



    public static void emailAddress(){

        Random rand = new Random();
        int rand_int1 = rand.nextInt(5000);

        String value = Integer.toString(rand_int1);
        String email = "example" + value + "@example.com";
        onView(withId(R.id.register_email)).perform(typeText(email));
    }



    @Test
    public void checkRegisterView(){
        View view = mActivity.findViewById(R.id.register_email);
        assertNotNull(view);
    }

    @Test
    public void checkLoginEmailHint(){
        onView(withId(R.id.register_email)).check(matches(withHint("Email...")));
    }
    @Test
    public void checkLoginEmailText(){
        onView(withId(R.id.register_email)).perform(typeText("example@example.com"));
    }

    @Test
    public void checkLoginPasswordHint(){
        onView(withId(R.id.register_password)).check(matches(withHint("Password...")));
    }
    @Test
    public void checkLoginPasswordText(){
        onView(withId(R.id.register_password)).perform(typeText("Example1"));
    }

    @Test
    public void checkExistingAccountLink(){
        assertNotNull(mActivity.findViewById(R.id.existing_account));
        onView(withId(R.id.existing_account)).perform(click());
        Activity login = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        assertNotNull(login);
        login.finish();
    }

    @Test
    public void RegisterWithNoEmailOrPassword(){
        onView(withId(R.id.register_email)).perform(typeText(""));
        onView(withId(R.id.register_password)).perform(typeText(""));
        onView(withId(R.id.register_button)).perform(click());
        onView(withText("Please enter email..."))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity()
                .getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void WrongPasswordLength() throws InterruptedException {
        onView(withId(R.id.register_email)).perform(typeText("example@example.com"));
        onView(withId(R.id.register_password)).perform(typeText("exm"));
        onView(withId(R.id.register_button)).perform(click());
        sleep(1000);
        onView(withText("Password Not Long Enough"))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity()
                .getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void RegisterAccount() throws InterruptedException {
        //onView(withId(R.id.register_email)).perform(typeText("example155@example.com"));
        emailAddress();
        onView(withId(R.id.register_password)).perform(typeText("Example01"));
        onView(withId(R.id.register_button)).perform(click());
        sleep(1000);
        Activity profile = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorSettings, 5000);
        assertNotNull(profile);
        sleep(1000);
        onView(withId(R.id.set_username)).perform(typeText("ExampleUser0"));
        onView(withId(R.id.set_profile_status)).perform(typeText("I'm an example0"));
        onView(withId(R.id.update_profile_button)).perform(click());
        sleep(1000);
        profile.finish();
        signUserOut();
    }

    @Test
    public void useSameEmail() throws InterruptedException {
        onView(withId(R.id.register_email)).perform(typeText("example01@example01.com"));
        onView(withId(R.id.register_password)).perform(typeText("Example01"));
        onView(withId(R.id.register_button)).perform(click());
        Activity waitForMonitorWithTimeout = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        sleep(1000);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}