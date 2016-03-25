package com.music.eartrainr.event;

import com.music.eartrainr.utils.Auth;

import java.util.List;

import static com.music.eartrainr.event.GameManagerEvent.EVENT.GAMES_READY;
import static com.music.eartrainr.event.GameManagerEvent.EVENT.LIST_GET;


public class GameManagerEvent<G> extends EventBusEvent {

  public static final String TAG = FriendItemGetEvent.class.getSimpleName();




  public interface EVENT {
    int LIST_GET = Auth.generateEventToken(TAG, "new_friend");
    int GAMES_READY = Auth.generateEventToken(TAG, "games_ready");
  }

  public GameManagerEvent() {
  }

//  public GameManagerEvent error(
//      final int id,
//      final E error) {
//    mEventID = id;
//    mError = error;
//    return this;
//  }

  public GameManagerEvent listGet(final List<G> item) {
    mEventID = LIST_GET;
    mData = item;
    return this;
  }

  public <G> EventBusEvent gamesReady() {
    mEventID = GAMES_READY;
    return this;
  }

}
