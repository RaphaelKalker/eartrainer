package com.music.eartrainr.event;

import com.music.eartrainr.api.MultiplayerService;
import com.music.eartrainr.model.MultiplayerGame;
import com.music.eartrainr.utils.Auth;


public class MultiPlayerEvent extends EventBusEvent {

  public static final String TAG = MultiPlayerEvent.class.getSimpleName();

  public interface EVENT {
    int MATCH_REQUEST_SUCCESS = Auth.generateEventToken(TAG, "match_request_success");
    int MATCH_PREPARE_PENDING = Auth.generateEventToken(TAG, "match_prepare_pending");
    int MATCH_DECLINED_SUCCESS = Auth.generateEventToken(TAG, "match_declined_success");
    int MATCH_DECLINE = Auth.generateEventToken(TAG, "match_decline");
    int MATCH_ACCEPTED_SUCCESS = Auth.generateEventToken(TAG, "match_accepted_success");
    int MATCH_CANCELED_SUCCESS = Auth.generateEventToken(TAG, "match_cancelled_success");
    int MATCH_CANCEL = Auth.generateEventToken(TAG, "match_cancel");
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

  public MultiPlayerEvent declineSuccess() {
    mEventID = EVENT.MATCH_DECLINED_SUCCESS;
    return this;
  }

  public MultiPlayerEvent acceptSuccess() {
    mEventID = EVENT.MATCH_ACCEPTED_SUCCESS;
    return this;
  }

  public MultiPlayerEvent cancelSuccess() {
    mEventID = EVENT.MATCH_CANCELED_SUCCESS;
    return this;
  }

  public MultiPlayerEvent cancelGame() {
    mEventID = EVENT.MATCH_CANCEL;
    return this;
  }

  public MultiPlayerEvent declineGame() {
    mEventID = EVENT.MATCH_DECLINE;
    return this;
  }
}
