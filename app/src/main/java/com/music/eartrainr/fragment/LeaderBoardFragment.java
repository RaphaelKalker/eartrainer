package com.music.eartrainr.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.music.eartrainr.Bus;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.adapter.LeaderAdapter;
import com.music.eartrainr.event.RankItemGetEvent;
import com.music.eartrainr.model.FirebaseRank;
import com.music.eartrainr.utils.DividerItemDecoration;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LeaderBoardFragment extends BaseFragment {

  public static final String TAG = LeaderBoardFragment.class.getSimpleName();

  @Bind(R.id.leaderboard_list) RecyclerView mLeaderList;
  @Bind(R.id.loading) ProgressBar mProgressBar;

  private LeaderAdapter mLeaderAdapter;

  public static LeaderBoardFragment newInstance(final Uri uri, int tab) {
    LeaderBoardFragment fragment = new LeaderBoardFragment();
    Bundle args = new Bundle();
    args.putInt("tab", tab);
    fragment.setArguments(args);
    return fragment;
  }

  //region LIFECYCLE

  @Override public void onStart() {
    super.onStart();
    Bus.register(this);
  }

  @Override public void onStop() {
    super.onStop();
    Bus.unregister(this);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    startLoading("Retrieval rank items");
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_leader_board, container, false);
  }

  @Override public void onViewCreated(
      final View view,
      @Nullable final Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    Wtf.log("Starting to load items");
    setLoadingState(true);
    setupListView();
    FirebaseRank.get();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ActivityNavigation) {
      mNavigationCallback = (ActivityNavigation) context;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mNavigationCallback = null;
  }

  //endregion

  //region UI HELPERS
  private void setLoadingState(final boolean isLoading) {
    if (mProgressBar != null && false) {
      mProgressBar.setIndeterminate(true);
      mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }
  }

  private void setupListView() {
    mLeaderAdapter = new LeaderAdapter(getActivity(), R.layout.list_leaderboard_item, this.getArguments().getInt("tab"));
    mLeaderList.setAdapter(mLeaderAdapter);
    mLeaderList.setLayoutManager(new LinearLayoutManager(getActivity()));
    mLeaderList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
  }

  //endregion

  //region SUBSCRIBERS

  @Subscribe
  public void onGetRequest(final RankItemGetEvent event) {

    setLoadingState(false);

    if (event.mEventID == RankItemGetEvent.EVENT.RANK_GET_SUCCESS) {
      mLeaderAdapter.setDataSource(event.mData);
      return;
    }

    if (event.mEventID == RankItemGetEvent.EVENT.RANK_GET_FAIL) {
      displayError(getView(), getString(R.string.fail_retrieve_items));
      return;
    }

  }

  //endregion

  //region ONCLICKS

  //endregion

  @Override public String getTitle() {
    return getString(R.string.fragment_title_leaderboard);
  }

  @Override public void restoreState(final Bundle savedState) {

  }
}
