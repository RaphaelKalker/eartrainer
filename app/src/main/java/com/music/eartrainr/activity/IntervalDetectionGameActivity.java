package com.music.eartrainr.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.IdRes;

import com.music.eartrainr.GameManager;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.fragment.BaseFragment;
import com.music.eartrainr.fragment.IntervalDetectionStepFragment;
import com.music.eartrainr.model.IntervalDetection;


public class IntervalDetectionGameActivity extends BaseGameActivity<IntervalDetection> {

  public static final String TAG = IntervalDetectionGameActivity.class.getSimpleName();
  private MediaPlayer mMediaPlayer;

  //region LIFECYCLE

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    boolean linear = getIntent().getBooleanExtra("linear", false);

    setErrorTimeout(1500);
    setLinear(linear);
    setTitle("Tab Stepper <small>(" + (linear ? "" : "Non ") + "Linear)</small>");
    setAlternativeTab(false);

    //THIS MUST BE AT THE END
    super.onCreate(savedInstanceState);
  }

  @Override protected void onPause() {
    requestPause();
    super.onPause();
  }

  //endregion

  //region VIEW SETUP
  @Override
  public void initGameTabs() {

    IntervalDetectionStepFragment fragment;

    int [] midiFiles = GameManager.getMidiArray();

    for (int i = 0; i < midiFiles.length; i++) {
      Bundle args = new GameManager.StepBuilder()
          .midiFile(midiFiles[i])
          .layout(R.layout.fragment_game1)
          .stepNr(i)
          .build();

      fragment = IntervalDetectionStepFragment.newInstance(args);
      addStep(fragment);
    }
  }

  @Override
  public Class<IntervalDetection> getGameClazz() {
    return IntervalDetection.class;
  }

  @Override String getGameIdentifier() {
    return GameManager.GAMES.INTERVAL_DETECTION;
  }

  //endregion



//  @Override public void onUpdate() {
//    super.onUpdate();
//  }
//
//  @Override public void onNext() {
//    super.onNext();
//  }
//
//  @Override public void onPrevious() {
//    super.onPrevious();
//  }




  @Override public void onPageChanged(final int position) {
    Wtf.log("Position Changed: " + position);
    reset();
  }

//  @Override public void onNext() {
//    super.onNext();
//  }

  //region MEDIA PLAYER
  public void requestPlay(@IdRes final int song) {
    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
      BaseFragment.displaySuccess(findViewById(R.id.game_tabs_container), "Already playing");
      return;
    }

    mMediaPlayer = MediaPlayer.create(this, song);
    mMediaPlayer.start();
  }

  public void requestPause() {
    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
      mMediaPlayer.pause();
    }
  }

  public void reset() {
    if (mMediaPlayer != null) {
      mMediaPlayer.reset();
      mMediaPlayer.release();
      mMediaPlayer = null;
    }
  }

  //endregion
}
