package com.music.eartrainr.event;

import com.music.eartrainr.api.MultiplayerService;
import com.music.eartrainr.model.MultiplayerGame;
import com.music.eartrainr.utils.Auth;


public class MultiPlayerEvent extends EventBusEvent {

  public static final String TAG = MultiPlayerEvent.class.getSimpleName();

  public interface EVENT {
    int MATCH_REQUEST_SUCCESS = Auth.generateEventToken(TAG, "match_request_success");
    int MATCH_PREPARE_PENDING = Auth.generateEventToken(TAG, "match_prepare_pending");
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

  public MultiPlayerEvent prepareStart(final String message) {
    mEventID = EVENT.MATCH_PREPARE_PENDING;
    mData = message;
    return this;
  }


}
