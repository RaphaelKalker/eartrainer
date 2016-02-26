package com.music.eartrainr.event;


public class SignInEvent<T> extends EventBusEvent {

  public SignInEvent() {
  }

  public SignInEvent success(final int id, T data) {
    mEventID = id;
    mData = data;
    return this;
  }

  public SignInEvent error(
      final int id,
      final T error) {
    mEventID = id;
    mError = error;
    return this;
  }
}
