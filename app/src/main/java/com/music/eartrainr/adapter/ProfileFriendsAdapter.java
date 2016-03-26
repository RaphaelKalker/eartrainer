package com.music.eartrainr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.music.eartrainr.Bus;
import com.music.eartrainr.Database;
import com.music.eartrainr.R;
import com.music.eartrainr.api.MultiplayerService;
import com.music.eartrainr.event.FriendItemGetEvent;
import com.music.eartrainr.model.User;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileFriendsAdapter
    extends RecyclerViewBaseAdapter<User, ProfileFriendsAdapter.DataAdapterViewHolder> {

  private static final Random RANDOM = new Random();

  private final int[] mColors;
  private OnRowItemClick<User> mOnClickListener;

  public ProfileFriendsAdapter(final Context context, final int listItemLayout, final VisibilitySettings visibilitySettings) {
    super.setDataSource(null);
    super.setView(listItemLayout);
    super.applyVisibilitySettings(visibilitySettings);
    mColors = context.getResources().getIntArray(R.array.letterImageViewColours);
  }

  @Override public DataAdapterViewHolder onCreateViewHolder(
      final ViewGroup parent,
      final int viewType) {
    final View view = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
    return new DataAdapterViewHolder(view);
  }

  @Override public void onBindViewHolder(
      final DataAdapterViewHolder holder,
      final int position) {

    final String username = getItem(position).getUserName();
    holder.deleteBtn.setVisibility(mVisiblitySetting == VisibilitySettings.OWNER ? View.VISIBLE : View.GONE);
    holder.name.setText(username);
    holder.pic.setLetter(username);
    holder.pic.setShapeColor(mColors[RANDOM.nextInt(mColors.length -1)]);

  }

  @Override public void setOnRowItemClickListener(final OnRowItemClick<User> listener) {
    mOnClickListener = listener;
  }

  @Override public void removeListener() {
    mOnClickListener = null;
  }

  protected class DataAdapterViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.friend_item_pic) MaterialLetterIcon pic;
    @Bind(R.id.friend_item_name) TextView name;
    @Bind(R.id.friend_item_delete) Button deleteBtn;
    @Bind(R.id.status_message) TextView statusMessage;

    public DataAdapterViewHolder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.friend_item_delete)
    public void onFriendDeleteClick() {
      statusMessage.setText("Deleting...");
      final User userToDelete = getItem(getLayoutPosition());
      queueForDeletion(userToDelete);
      Bus.post(new FriendItemGetEvent().itemDeleteRequest(userToDelete));
    }

    @OnClick(R.id.friend_item_challenge)
    public void onFriendChallengeClick() {
      //statusMessage.setText("Challenging...");
      final User userToChallenge = getItem(getLayoutPosition());
      MultiplayerService.requestMatch(Database.getSingleton().getUserName(), userToChallenge.getUserName());
    }

    @OnClick(R.id.friend_item_container)
    public void onRequestProfileClicked(){
      final User requestedUser = getItem(getLayoutPosition());
      mOnClickListener.onRowItemClick(requestedUser, getLayoutPosition());
    }
  }

  @Override public void applyVisibilitySettings(VisibilitySettings settings) {


  }
}
