package com.music.eartrainr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.music.eartrainr.Bus;
import com.music.eartrainr.R;
import com.music.eartrainr.event.FriendItemGetEvent;
import com.music.eartrainr.model.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileFriendsAdapter
    extends RecyclerViewBaseAdapter<User, ProfileFriendsAdapter.DataAdapterViewHolder> {


  private OnRowItemClick<User> mOnClickListener;

  public ProfileFriendsAdapter(final int listItemLayout, final VisibilitySettings visibilitySettings) {
    super.setDataSource(null);
    super.setView(listItemLayout);
    super.applyVisibilitySettings(visibilitySettings);
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
    holder.deleteBtn.setVisibility(mVisiblitySetting == VisibilitySettings.OWNER ? View.VISIBLE : View.GONE);
    holder.name.setText( getItem(position).getUserName());
  }

  @Override public void setOnRowItemClickListener(final OnRowItemClick<User> listener) {
    mOnClickListener = listener;
  }

  @Override public void removeListener() {
    mOnClickListener = null;
  }

  protected class DataAdapterViewHolder extends RecyclerView.ViewHolder {
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

    @OnClick(R.id.friend_item_container)
    public void onRequestProfileClicked(){
      final User requestedUser = getItem(getLayoutPosition());
      mOnClickListener.onRowItemClick(requestedUser, getLayoutPosition());
    }
  }

  @Override public void applyVisibilitySettings(VisibilitySettings settings) {


  }
}
