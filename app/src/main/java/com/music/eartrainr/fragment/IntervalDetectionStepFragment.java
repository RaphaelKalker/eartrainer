package com.music.eartrainr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.activity.IntervalDetectionGameActivity;
import com.music.eartrainr.model.IntervalDetection;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.music.eartrainr.GameManager.GAMES.GAME_LAYOUT;
import static com.music.eartrainr.GameManager.GAMES.GAME_STEP_NR;
import static com.music.eartrainr.GameManager.GAMES.STEP_MIDI_FILE;


public class IntervalDetectionStepFragment extends GameFragment<IntervalDetection> {

  public static final String TAG = IntervalDetectionStepFragment.class.getSimpleName();

  private VALIDATION mValidationState;

  private enum State {
    PLAYING,
    DONE,
  }

  //region VIEWS

  @Bind(R.id.game1_replay_btn) Button mReplayBtn;
  @Bind(R.id.game1_class_spinner) Spinner mClassDropdown;
  @Bind(R.id.game1_size_spinner) Spinner mSizeDropdown;
  @Bind(R.id.game1_musical_note) IconTextView mMusicNote;
  @Bind(R.id.game1_successrate_val) TextView mSuccessRate;

  //endregion

  //region INSTANTIATION

  public static IntervalDetectionStepFragment newInstance(final Bundle args) {
    final IntervalDetectionStepFragment gameStepFragment = new IntervalDetectionStepFragment();
    gameStepFragment.setArguments(args);
    return gameStepFragment;
  }

  public IntervalDetectionStepFragment() {
    // Required empty public constructor
  }

  //endregion

  //region VIEWCYCLE

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    final int gameLayout = getArguments().getInt(GAME_LAYOUT);
    return inflater.inflate(gameLayout, container, false);
  }

  @Override public void onViewCreated(
      final View view,
      @Nullable final Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
  }

  @Override public void onResume() {
    super.onResume();
    initView();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  //endregion

  //region VIEW SETUP

  @Override public String name() {
    return "Q" + String.valueOf(getArguments().getInt(GAME_STEP_NR) + 1);
  }

  @Override public String error() {
    return super.getValidationErrorString(mValidationState);
  }

  @Override public boolean validateInput(final IntervalDetection gameData) {

    mValidationState = verifyUserSelection(gameData.parseAnswers(), mClassDropdown, mSizeDropdown);

    switch (mValidationState) {
      case CORRECT:
        return true;
      case INCOMPLETE_INPUT:
        return false;
      case INCORRECT:
        return false;
    }
    return false;
  }

  private void initView() {
    ArrayAdapter classAdapter = ArrayAdapter.createFromResource(
        getContext(),
        R.array.game_classes,
        android.R.layout.simple_spinner_item
    );

    classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mClassDropdown.setAdapter(classAdapter);
    mClassDropdown.setTag("");

    ArrayAdapter sizeAdapter = ArrayAdapter.createFromResource(
        getContext(),
        R.array.game_size,
        android.R.layout.simple_spinner_item
    );

    sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSizeDropdown.setAdapter(sizeAdapter);
  }

  //endregion

  //region CLICK LISTENER

  @OnClick(R.id.game1_replay_btn)
  public void onPlayClicked() {
    ((IntervalDetectionGameActivity)getActivity()).requestPlay(getArguments().getInt(STEP_MIDI_FILE));
  }

  //endregion
}
