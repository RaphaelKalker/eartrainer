package com.music.eartrainr.event;

import com.music.eartrainr.utils.Auth;

import static com.music.eartrainr.event.NavigationEvent.EVENT.NAVIGATION_FAILED;


public class NavigationEvent extends EventBusEvent {

  public static final String TAG = NavigationEvent.class.getSimpleName();


  public interface EVENT {
    int NAVIGATION_FAILED = Auth.generateEventToken(TAG, "navigation_failed");
  }


  public NavigationEvent error(final String target, final String error) {
    mEventID = NAVIGATION_FAILED;
    mData = target;
    mError = error;
    return this;
  }
}
