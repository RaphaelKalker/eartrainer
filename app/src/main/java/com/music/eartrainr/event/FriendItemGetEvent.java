package com.music.eartrainr.event;


import com.music.eartrainr.Auth;

import static com.music.eartrainr.event.FriendItemGetEvent.EVENT.DELETE_REQUEST;
import static com.music.eartrainr.event.FriendItemGetEvent.EVENT.DELETE_SUCCESS;
import static com.music.eartrainr.event.FriendItemGetEvent.EVENT.FRIEND_RETRIEVED;


public class FriendItemGetEvent<E, U> extends EventBusEvent {
  public static final String TAG = FriendItemGetEvent.class.getSimpleName();


  public interface EVENT {
    int FRIEND_RETRIEVED = Auth.generateEventToken(TAG, "new_friend");
    int DELETE_REQUEST = Auth.generateEventToken(TAG, "friend_delete_request");
    int DELETE_SUCCESS = Auth.generateEventToken(TAG, "friend_delete_success");
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
    mEventID = FRIEND_RETRIEVED;
    mData = item;
    return this;
  }

  public FriendItemGetEvent itemDeleteSuccess() {
    mEventID = DELETE_SUCCESS;
    return this;
  }

  public FriendItemGetEvent itemDeleteRequest(final U item) {
    mEventID = DELETE_REQUEST;
    mData = item;
    return this;
  }
}

