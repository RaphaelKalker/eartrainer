package com.music.eartrainr;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.music.eartrainr.event.EventBusEvent;
import com.music.eartrainr.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;


public class Bus {

  public static void post(final EventBusEvent event) {
    Wtf.log("Posting: " + event.mEventID);
    EventBus.getDefault().post(event);
  }

  public static void register(final Object subscriber) {
    EventBus.getDefault().register(subscriber);
  }

  public static void unregister(final Object subscriber) {
    EventBus.getDefault().unregister(subscriber);
  }
}
