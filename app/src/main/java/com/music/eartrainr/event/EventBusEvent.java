package com.music.eartrainr.event;

public class EventBusEvent<T> {
  public int mEventID;
  public T mData;
  public T mError;
}
