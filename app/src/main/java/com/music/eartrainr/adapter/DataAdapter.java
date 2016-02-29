package com.music.eartrainr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.eartrainr.R;
import com.music.eartrainr.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DataAdapter
    extends RecyclerViewBaseAdapter<User, DataAdapter.DataAdapterViewHolder> {


  private OnRowItemClick<User> mOnClickListener;

  public DataAdapter(final int listItemLayout) {
    setDataSource(null);
    setView(listItemLayout);
  }

  @Override public DataAdapterViewHolder onCreateViewHolder(
      final ViewGroup parent,
      final int viewType) {
    //BAD why should we have to inflate again?
    final View view = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
    return new DataAdapterViewHolder(view);
  }

  @Override public void onBindViewHolder(
      final DataAdapterViewHolder holder,
      final int position) {

    holder.name.setText( getItem(position).getUserName());
  }

  @Override public void setOnRowItemClickListener(final OnRowItemClick<User> listener) {
    mOnClickListener = listener;
  }

  @Override public void removeListener() {
    mOnClickListener = null;
  }

  protected class DataAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.friend_item_name) TextView name;

    public DataAdapterViewHolder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(final View v) {
      if (mOnClickListener != null) {
        mOnClickListener.onRowItemClick(v, getLayoutPosition());
      }
    }
  }


}
