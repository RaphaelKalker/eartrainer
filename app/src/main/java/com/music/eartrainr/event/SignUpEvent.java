package com.music.eartrainr.event;


public class SignUpEvent<T> extends EventBusEvent {

  public SignUpEvent() {
  }

  public SignUpEvent success(final int id, final String userUID) {
    mEventID = id;
    mData = userUID;
    return this;
  }

  public SignUpEvent error(
      final int id,
      final T error) {
    mEventID = id;
    mError = error;
    return this;
  }
}
