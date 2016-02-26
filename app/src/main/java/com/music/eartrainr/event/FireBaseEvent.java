package com.music.eartrainr.event;

public class FireBaseEvent extends EventBusEvent{

  public static EventBusEvent newEvent() {
    final EventBusEvent event = new EventBusEvent();
    return event;
  }

  public static EventBusEvent errorEvent() {
    final EventBusEvent event = new EventBusEvent();
    return event;
  }


}
