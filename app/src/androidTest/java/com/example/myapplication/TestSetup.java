package com.example.myapplication;

import androidx.test.espresso.action.ViewActions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class TestSetup {


    public static void login(){
        onView(withId(R.id.login_email)).perform(typeText("example@example.com"));
        onView(withId(R.id.login_password)).perform(typeText("Example1"));
        onView(withId(R.id.login_button)).perform(click());
    }

    public static void login1(){
        onView(withId(R.id.login_email)).perform(typeText("example@example.com"));
        onView(withId(R.id.login_password)).perform(typeText("Example1"));
        onView(withId(R.id.login_button)).perform(click());
    }

    public static void login2(){
        onView(withId(R.id.login_email)).perform(typeText("saad@saad.com"));
        onView(withId(R.id.login_password)).perform(typeText("saadsaad"));
        onView(withId(R.id.login_button)).perform(click());
    }

    public static void signUserOut(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Logout")).perform(click());
    }

    public static void pressBack() {
        onView(isRoot()).perform(ViewActions.pressBack());
    }
}
