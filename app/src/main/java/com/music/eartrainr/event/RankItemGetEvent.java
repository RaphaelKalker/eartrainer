package com.music.eartrainr.event;

import com.music.eartrainr.model.Rank;
import com.music.eartrainr.utils.Auth;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankItemGetEvent extends EventBusEvent<List<Rank>> {

  public static final String TAG = RankItemGetEvent.class.getSimpleName();

  public interface EVENT {
    int RANK_GET_SUCCESS = Auth.generateEventToken(TAG, "rank_get_success");
    int RANK_GET_FAIL = Auth.generateEventToken(TAG, "rank_get_fail");
  }

  public RankItemGetEvent success(final List<Rank> rankList) {
    mEventID = EVENT.RANK_GET_SUCCESS;
    mData = rankList;
    Collections.sort(mData, new Comparator<Rank>() {
      @Override
      public int compare(Rank rank1, Rank rank2) {
        return rank2.getTotalScore() - rank1.getTotalScore();
      }
    });
    return this;
  }
}
