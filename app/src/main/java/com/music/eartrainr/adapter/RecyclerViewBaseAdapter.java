package com.music.eartrainr.adapter;

import android.support.v7.widget.RecyclerView;

import android.view.View;

import com.music.eartrainr.Wtf;
import com.music.eartrainr.model.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public abstract class RecyclerViewBaseAdapter<T, VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {

  private static final String TAG = RecyclerViewBaseAdapter.class.getSimpleName();
  private List<T> mDataSource;
  private Queue<T> mDeletionQueue;

  public void setView(final int listItemLayout) {
    mLayout = listItemLayout;
  }

  public int mLayout;

  public void setDataSource(List<T> dataSource) {
    mDataSource = dataSource != null ? dataSource : new ArrayList<T>();
    mDeletionQueue = new LinkedList<T>();
    notifyDataSetChanged();
  }

  public void addItem(T item) {
    mDataSource.add(item);
    notifyItemInserted(getItemCount() -1);
  }

  public List<T> getDataSource() {
    return mDataSource;
  }

  @Override public int getItemCount() {
    return mDataSource != null ? mDataSource.size() : 0;
  }

  public T getItem(final int position) {
    return position < getItemCount() && position > -1
        ? getDataSource().get(position)
        : null;
  }

  @Override public long getItemId(final int position) {
    return super.getItemId(position);
  }

  public void queueForDeletion(final T itemToDelete) {
    mDeletionQueue.add(itemToDelete);
  }

  public void processDeletion() {
    Wtf.log(String.format("Processing queue with %d element(s) for a deletion", mDeletionQueue.size()));
    final T itemToDelete = mDeletionQueue.poll();
    if (getDataSource().contains(itemToDelete)) {
      final int position = getDataSource().indexOf(itemToDelete);
      getDataSource().remove(itemToDelete);
      notifyItemRemoved(position);
    }
  }

  public interface OnRowItemClick<T> {
    void onRowItemClick(final View view, final int position);
  }

  public abstract void setOnRowItemClickListener(OnRowItemClick<T> listener);

  public abstract void removeListener();
}

