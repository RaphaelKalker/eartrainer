package com.music.eartrainr.event;


import com.music.eartrainr.Auth;


public class FriendItemGetEvent<E, U> extends EventBusEvent {
  public static final String TAG = FriendItemGetEvent.class.getSimpleName();


  public interface EVENT {
    int FRIEND_RETRIEVED = Auth.generateEventToken(TAG, "new_friend");
  }

  public FriendItemGetEvent() {
  }

  public FriendItemGetEvent error(
      final int id,
      final E error) {
    mEventID = id;
    mError = error;
    return this;
  }

  public FriendItemGetEvent itemGet(final U item) {
    mEventID = EVENT.FRIEND_RETRIEVED;
    mData = item;
    return this;
  }
}

