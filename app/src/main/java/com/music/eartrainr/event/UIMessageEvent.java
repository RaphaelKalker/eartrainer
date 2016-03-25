package com.music.eartrainr.event;

import com.music.eartrainr.utils.Auth;


public class UIMessageEvent extends EventBusEvent<String>{

  public static final String TAG = UIMessageEvent.class.getSimpleName();

//  public interface EVENT {
//    int RANK_GET_SUCCESS = Auth.generateEventToken(TAG, "rank_get_success");
//    int RANK_GET_FAIL = Auth.generateEventToken(TAG, "rank_get_fail");
//  }

  public UIMessageEvent message(final String message) {
//    mEventID = EVENT.RANK_GET_SUCCESS;
    mData = message;
    return this;
  }
}
