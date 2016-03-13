package com.music.eartrainr.event;


import com.music.eartrainr.utils.Auth;
import com.music.eartrainr.model.User;


public class FriendAddedEvent<T> extends EventBusEvent {
  public static final String TAG = FriendAddedEvent.class.getSimpleName();




  public interface EVENT {
    int FRIEND_ADDED = Auth.generateEventToken(TAG, "new_friend");
//    int FRIEND_FOUND = Auth.generateEventToken(TAG, "friend_found");
    int USER_FOUND = Auth.generateEventToken(TAG, "user_found");
    int USER_UNKNOWN = Auth.generateEventToken(TAG, "user_not_found");
  }


  public FriendAddedEvent() {
  }


  public FriendAddedEvent error(
      final int id,
      final T error) {
    mEventID = id;
    mError = error;
    return this;
  }

  public EventBusEvent found(final User user) {
    mEventID = EVENT.USER_FOUND;
    mData = user;
    return this;
  }

  public EventBusEvent friendAdded(final User user) {
    mEventID = EVENT.FRIEND_ADDED;
    mData = user;
    return this;
  }

  public FriendAddedEvent notFound(final String userName) {
    mEventID = EVENT.USER_UNKNOWN;
    mData = userName;
    return this;
  }
}
