package com.music.eartrainr;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.music.eartrainr.activity.LoginActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class FriendsHandlingTest
{

  private static String friendToAdd = "n_simone";
  @Rule
  public ActivityTestRule<LoginActivity> mActivityRule =
      new ActivityTestRule<>(LoginActivity.class);

  //Login into the application before running tests on profile view
  @Before
  public void login() {
    String email = "jacinta@gmail.com";
    String password = "password";

    onView(withId(R.id.login_email))
        .perform(typeText(email), closeSoftKeyboard());
    onView(withId(R.id.login_password))
        .perform(typeText(password), closeSoftKeyboard());

    onView(withId(R.id.login_signin_btn)).perform(click());
  }

  //test attempts to add friends who either do not exist or failing to enter a friend to add
  @Test
  public void testAddFriendInvalid()
  {
    onView(withId(R.id.profile_add_friend)).perform(click());
    onView(withId(R.id.friend_add_btn)).perform(click());

    //if you enter nothing, container remains open
    onView(withId(R.id.friend_add_username_container))
        .check(matches(isDisplayed()));

    onView(withId(R.id.friend_add_username)).perform(typeText("1"));
    onView(withId(R.id.friend_add_btn)).perform(click());

    //if you enter username that doesn't exist, container remains open
    onView(withId(R.id.friend_add_username_container))
        .check(matches(isDisplayed()));

  }


  //Test adding an actual friend
  @Test
  public void testAddFriendValid()
  {
    onView(withId(R.id.profile_add_friend)).perform(click());
    onView(withId(R.id.friend_add_username)).perform(typeText(friendToAdd));

    onView(withId(R.id.friend_add_btn)).perform(click());

    try {
      Thread.sleep(5000);
    } catch (Exception e) {

    }

    onView(withId(R.id.friend_add_btn))
        .check(doesNotExist());
  }
}