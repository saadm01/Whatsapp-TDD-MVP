package com.example.myapplication;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
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

public class NumberLoginActivityTest {

    @Rule
    public ActivityTestRule<NumberLoginActivity> mActivityTestRule = new ActivityTestRule<NumberLoginActivity>(NumberLoginActivity.class);
    private NumberLoginActivity mActivity = null;

    Instrumentation.ActivityMonitor monitor = InstrumentationRegistry.getInstrumentation().addMonitor(NumberLoginActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorSettings = InstrumentationRegistry.getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void checkView(){
        View view = mActivity.findViewById(R.id.verification_button);
        assertNotNull(view);
    }

    @Test
    public void checkUserNumberHint(){
        onView(withId(R.id.user_number)).check(matches(withHint("Enter your phone number...")));
    }
    @Test
    public void verifyCodeHint(){
        onView(withId(R.id.verification_code)).check(matches(withHint("Enter your received verification code...")));
    }

    @Test
    public void invalidPhoneNumber() throws InterruptedException {
        onView(withId(R.id.user_number)).perform(typeText("123"));
        onView(withId(R.id.verification_button)).perform(click());
        onView(withText("Phone Verification")).check(matches(isDisplayed()));
        onView(withText("Please wait, while we are authenticating using your phone...")).check(matches(isDisplayed()));
        sleep(1000);
        onView(withText("Invalid Phone Number, Please enter correct phone number with your country code..."))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity()
                .getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void validPhoneNumberOnSamePhone() throws InterruptedException {
        onView(withId(R.id.user_number)).perform(typeText("+447577696655"));
        onView(withId(R.id.verification_button)).perform(click());
        onView(withText("Phone Verification")).check(matches(isDisplayed()));
        onView(withText("Please wait, while we are authenticating using your phone...")).check(matches(isDisplayed()));
        Activity settings = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorSettings, 5000);
        assertNotNull(settings);
        settings.finish();
    }

    @Test
    public void invalidVerificationCode() throws InterruptedException {
        onView(withId(R.id.user_number)).perform(typeText("+447518310488"));
        onView(withId(R.id.verification_button)).perform(click());
        onView(withText("Phone Verification")).check(matches(isDisplayed()));
        onView(withText("Please wait, while we are authenticating using your phone...")).check(matches(isDisplayed()));
        onView(withText("Code has been sent, please check and verify...")).check(matches(isDisplayed()));
        sleep(1000);
        onView(withId(R.id.verification_code)).perform(typeText("123"));
        onView(withId(R.id.verifiy_number_button)).perform(click());
        onView(withText("Verification Code")).check(matches(isDisplayed()));
        onView(withText("Please wait, while we are verifying verification code...")).check(matches(isDisplayed()));
        Activity waitForMonitorWithTimeout = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
    }

    @Test
    public void validPhoneNumberWithDifferentPhoneNumber() throws InterruptedException {
        onView(withId(R.id.user_number)).perform(typeText("+447518310488"));
        onView(withId(R.id.verification_button)).perform(click());
        onView(withText("Phone Verification")).check(matches(isDisplayed()));
        onView(withText("Please wait, while we are authenticating using your phone...")).check(matches(isDisplayed()));
        onView(withText("Code has been sent, please check and verify...")).check(matches(isDisplayed()));
        sleep(1000);
        onView(withId(R.id.verification_code)).perform(typeText("123456"));//Code is stand in
        onView(withId(R.id.verifiy_number_button)).perform(click());
        onView(withText("Verification Code")).check(matches(isDisplayed()));
        onView(withText("Please wait, while we are verifying verification code...")).check(matches(isDisplayed()));
        Activity settings = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitorSettings, 5000);
        assertNotNull(settings);
        settings.finish();
    }



    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}