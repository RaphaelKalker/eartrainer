package com.music.eartrainr.adapter;

import android.support.v7.widget.RecyclerView;

import android.view.View;

import java.util.List;


public abstract class RecyclerViewBaseAdapter<T, VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {

  private static final String TAG = RecyclerViewBaseAdapter.class.getSimpleName();
  private List<T> mDataSource;
  private int mSize;

  public void setView(final int listItemLayout) {
    mLayout = listItemLayout;
  }

  public int mLayout;

  public void setDataSource(List<T> dataSource) {
    mDataSource = dataSource;
    mSize = dataSource == null ? 0 : dataSource.size();
    notifyDataSetChanged();
  }

  public List<T> getDataSource() {
    return mDataSource;
  }

  @Override public int getItemCount() {
    return mSize;
  }

  public T getItem(final int position) {
    return position < mSize && position > -1
        ? getDataSource().get(position)
        : null;
  }

  @Override public long getItemId(final int position) {
    return super.getItemId(position);
  }

  public interface OnRowItemClick<T> {
    void onRowItemClick(final View view, final int position);
  }

  public abstract void setOnRowItemClickListener(OnRowItemClick<T> listener);

  public abstract void removeListener();
}

