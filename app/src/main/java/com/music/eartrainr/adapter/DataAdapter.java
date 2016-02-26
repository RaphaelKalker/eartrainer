package com.music.eartrainr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.eartrainr.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DataAdapter
    extends RecyclerViewBaseAdapter<String, DataAdapter.DataAdapterViewHolder> {


  private OnRowItemClick<String> mOnClickListener;

  public DataAdapter(List<String> data, final int listItemLayout) {
    setDataSource(data);
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

    holder.name.setText(getItem(position));
  }

  @Override public void setOnRowItemClickListener(final OnRowItemClick<String> listener) {
    mOnClickListener = listener;
  }

  @Override public void removeListener() {
    mOnClickListener = null;
  }

  protected class DataAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.friend_name) TextView name;

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
