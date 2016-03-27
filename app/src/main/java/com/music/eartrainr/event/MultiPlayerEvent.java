package com.music.eartrainr.event;

import com.music.eartrainr.model.MultiplayerGame;
import com.music.eartrainr.utils.Auth;


public class MultiPlayerEvent extends EventBusEvent<MultiplayerGame> {

  public static final String TAG = MultiPlayerEvent.class.getSimpleName();

  public interface EVENT {
    int MATCH_REQUEST_SUCCESS = Auth.generateEventToken(TAG, "match_request_success");
  }

  public MultiPlayerEvent success(final MultiplayerGame multiplayerGame) {
    mEventID = EVENT.MATCH_REQUEST_SUCCESS;
    mData = multiplayerGame;
    return this;
  }

  public MultiPlayerEvent error(final MultiplayerGame multiplayerGame) {
    mEventID = EVENT.MATCH_REQUEST_SUCCESS;
    mData = multiplayerGame;
    return this;
  }


}
