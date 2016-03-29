package com.music.eartrainr;

import android.os.Handler;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.music.eartrainr.activity.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.doubleClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;



@RunWith(AndroidJUnit4.class)
public class LoginTest
{
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);


    //Test login attempts with invalid email/password
    @Test
    public void testLoginInvalidEmailPassword()
    {
        String email = "fake_email";
        String password = "fake_password";

        onView(withId(R.id.login_email))
                .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.login_signin_btn)).perform(click());

        onView(withId(R.id.profile_image))
                .check(doesNotExist());
    }
    //Test login attempts with invalid password, but existing email
    @Test
    public void testLoginInvalidPassword()
    {
        String email = "jacinta@gmail.com";
        String password = "fake_password";

        onView(withId(R.id.login_email))
            .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.login_password))
            .perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.login_signin_btn)).perform(click());

        onView(withId(R.id.profile_image))
            .check(doesNotExist());
    }

    //Test valid login
    @Test
    public void testLoginValidEmailPassword()
    {
        String email = "jacinta@gmail.com";
        String password = "password";

        onView(withId(R.id.login_email))
            .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.login_password))
            .perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.login_signin_btn)).perform(click());
//        try {
//            Thread.sleep(5000);
//        } catch (Exception e) {
//
//        }
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()));
    }
}