package com.music.eartrainr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.music.eartrainr.R;
import com.music.eartrainr.RankManager;
import com.music.eartrainr.activity.LeaderBoardActivity;
import com.music.eartrainr.model.Rank;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LeaderAdapter extends RecyclerViewBaseAdapter<Rank, LeaderAdapter.LeaderBoardViewholder>{

  private static final Random RANDOM = new Random();
  private final int[] mColours;
  private final int mTab;

  public LeaderAdapter(final Context context, final int listItemLayout, int tab) {
    super.setDataSource(null);
    super.setView(listItemLayout);
    mColours = context.getResources().getIntArray(R.array.letterImageViewColours);
    mTab = tab;
  }

  //region VIEWHOLDER

  @Override public LeaderBoardViewholder onCreateViewHolder(
      final ViewGroup parent,
      final int viewType) {
    final View view = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
    return new LeaderBoardViewholder(view);
  }

  @Override public void onBindViewHolder(
      final LeaderBoardViewholder holder,
      final int position) {

    RankManager rankManager = RankManager.getInstance();
    rankManager.updateRank(getItem(position));
    int score = mTab == 0 ? rankManager.calculateTotalRank() : rankManager.calculateGameRank(mTab);

    holder.userName.setText(rankManager.getUsername());
    holder.totalScore.setText(String.valueOf(score));
    holder.avatar.setLetter(rankManager.getUsername());
    holder.avatar.setShapeColor(mColours[RANDOM.nextInt(mColours.length -1)]);
  }

  public static class LeaderBoardViewholder extends RecyclerView.ViewHolder {

    @Bind(R.id.leaderboard_total_score) TextView totalScore;
    @Bind(R.id.leaderboard_username) TextView userName;
    @Bind(R.id.leaderboard_avatar) MaterialLetterIcon avatar;
    public LeaderBoardViewholder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  //endregion


  @Override public void setOnRowItemClickListener(final OnRowItemClick<Rank> listener) {

  }

  @Override public void removeListener() {

  }
}
