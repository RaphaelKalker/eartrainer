package com.music.eartrainr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.music.eartrainr.R;
import com.music.eartrainr.model.Rank;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LeaderAdapter extends RecyclerViewBaseAdapter<Rank, LeaderAdapter.LeaderBoardViewholder>{

  private static final Random RANDOM = new Random();
  private final int[] mColours;

  public LeaderAdapter(final Context context, final int listItemLayout) {
    super.setDataSource(null);
    super.setView(listItemLayout);
    mColours = context.getResources().getIntArray(R.array.letterImageViewColours);
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

    final Rank rank = getItem(position);

    holder.userName.setText(rank.getUsername());
    holder.totalScore.setText(String.valueOf(rank.getTotalScore()));
    holder.avatar.setLetter(rank.getUsername());
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
