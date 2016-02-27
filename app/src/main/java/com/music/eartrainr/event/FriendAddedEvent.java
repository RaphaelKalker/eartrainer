package com.music.eartrainr.event;


public class FriendAddedEvent<T> extends EventBusEvent {

  private boolean mFriendExists;

  public FriendAddedEvent() {
  }

  public FriendAddedEvent success(final int id, T data) {
    mEventID = id;
    mData = data;
    return this;
  }

  public FriendAddedEvent error(
      final int id,
      final T error) {
    mEventID = id;
    mError = error;
    return this;
  }

  public boolean isFriendExists() {
    return mFriendExists;
  }

  public void setFriendExists(final boolean friendExists) {
    mFriendExists = friendExists;
  }
}
