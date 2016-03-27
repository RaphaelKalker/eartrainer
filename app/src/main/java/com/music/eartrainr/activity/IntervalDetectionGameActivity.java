package com.music.eartrainr.activity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;

import com.music.eartrainr.Database;
import com.music.eartrainr.GameManager;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.api.MultiplayerService;
import com.music.eartrainr.fragment.BaseFragment;
import com.music.eartrainr.fragment.GameStepSummaryFragment;
import com.music.eartrainr.fragment.IntervalDetectionStepFragment;
import com.music.eartrainr.interfaces.GameHelper;
import com.music.eartrainr.model.IntervalDetection;


public class IntervalDetectionGameActivity extends BaseGameActivity<IntervalDetection> implements GameHelper {

  public static final String TAG = IntervalDetectionGameActivity.class.getSimpleName();
  private MediaPlayer mMediaPlayer;
  private ProgressDialog mDialog;

  //region LIFECYCLE

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    boolean linear = getIntent().getBooleanExtra("linear", false);

    handleMultiplayer();

    setErrorTimeout(1500);
    setLinear(linear);
    setTitle("Tab Stepper <small>(" + (linear ? "" : "Non ") + "Linear)</small>");
    setAlternativeTab(false);

    //THIS MUST BE AT THE END
    super.onCreate(savedInstanceState);
  }



  private void handleMultiplayer() {
    Intent intent = getIntent();

    if (intent != null) {
      Bundle args = intent.getExtras();

      final boolean multiplayer = args.getBoolean(GameManager.GAMES.MULTIPLAYER, false);
      final String opponent = args.getString(GameManager.GAMES.OPPONENT, "");
      final int id = args.getInt(GameManager.GAMES.GAME_ID, 1);

      if (multiplayer) {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(String.format("Waiting on %s to accept", opponent));
        mDialog.setTitle("Multi Player Mode");
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
          @Override public void onClick(
              final DialogInterface dialog,
              final int which) {
            //TODO send to service
            MultiplayerService.getInstance().cancelRequest(Database.getSingleton().getUserName(), id);

            mDialog.setMessage("Canceling Request...");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
              @Override public void run() {
                finish();
              }
            }, 1000);
          }
        });
        mDialog.setCancelable(false);

        mDialog.setIndeterminate(true);
        mDialog.show();



      }
    }
  }

  @Override protected void onPause() {
    requestPause();
    super.onPause();

    if (mDialog != null) {
      mDialog.dismiss();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    GameManager.getInstance().destroy();
  }

  //endregion

  //region VIEW SETUP
  @Override public void onPageChanged(final int position) {
    if (mPager.getAdapter().getCount() - 1 == position) {
      //update with the latest stats
      mSteps.getCurrent().updateCurrent();
    }
    reset();
  }

  @Override
  public void initGameTabs() {

    IntervalDetectionStepFragment fragment;

    int [] midiFiles = GameManager.getMidiArray();

    final int gameSteps = midiFiles.length;

    for (int i = 0; i < gameSteps; i++) {
      Bundle args = new GameManager.StepBuilder()
          .midiFile(midiFiles[i])
          .layout(R.layout.fragment_game1)
          .stepNr(i)
          .build();

      fragment = IntervalDetectionStepFragment.newInstance(args);
      addStep(fragment);
    }

    Bundle args = new GameManager.StepBuilder()
        .layout(R.layout.fragment_game_summary)
        .stepNr(gameSteps)
        .build();

    addStep(GameStepSummaryFragment.newInstance(args));
  }

  @Override
  public Class<IntervalDetection> getGameClazz() {
    return IntervalDetection.class;
  }

  @Override String getGameIdentifier() {
    return GameManager.GAMES.INTERVAL_DETECTION;
  }

  //endregion



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

  @Override public String getAnswer(final int stepNr) {
    IntervalDetection step = (IntervalDetection) GameManager.getInstance().getGameData(stepNr);
    return step.getAnswer();
  }

  @Override public void addAnswerAttempt(final int step, final boolean success) {
    GameManager.getInstance().addAnswer(success);
  }

  @Override public void onNext() {
    super.onNext();
  }

  public static Intent makeIntent(Context context, boolean notification) {
    Intent intent = new Intent(context, IntervalDetectionGameActivity.class);
    if (notification) {
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    //TODO add arguments about multiplayer crap
    return intent;
  }

  //endregion
}
