package com.music.eartrainr.fragment;

import android.content.Context;
import android.media.MediaCodec;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.eartrainr.GameManager;
import com.music.eartrainr.R;
import com.music.eartrainr.utils.UiUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;


public class GameStepSummaryFragment extends GameFragment {

  //region
  @Bind(R.id.game_summary_correct_guesses_value) TextView mCorrectGuesses;
  @Bind(R.id.game_summary_incorrect_guesses_value) TextView mWrongGuesses;

  //endregion

  public static GameStepSummaryFragment newInstance(Bundle args) {
    final GameStepSummaryFragment fragment = new GameStepSummaryFragment();
    fragment.setArguments(args);
    return fragment;
  }

  //region VIEW CYCLE
  @Nullable @Override public View onCreateView(
      final LayoutInflater inflater,
      @Nullable final ViewGroup container,
      @Nullable final Bundle savedInstanceState) {
    return UiUtils.inflate(getActivity(), container, R.layout.fragment_game_summary);
  }

  @Override public void onViewCreated(
      final View view,
      @Nullable final Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    initView();
  }


  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }


  private void initView() {
    HashMap<String, String> stats = GameManager.getInstance().getSummaryStats();
    mCorrectGuesses.setText(stats.get("correct"));
    mWrongGuesses.setText(stats.get("incorrect"));
  }

  //endregion

  @Override boolean validateInput(final Object answer) {
    return true;
  }

  @Override public String name() {
    return "Summary";
  }

  @Override public void updateCurrent() {
    initView();
  }
}
