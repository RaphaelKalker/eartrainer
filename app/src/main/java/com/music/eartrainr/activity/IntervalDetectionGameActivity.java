package com.music.eartrainr.activity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.IdRes;

import com.music.eartrainr.Bus;
import com.music.eartrainr.Database;
import com.music.eartrainr.GameManager;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.api.MultiplayerService;
import com.music.eartrainr.event.GameManagerEvent;
import com.music.eartrainr.event.MultiPlayerEvent;
import com.music.eartrainr.fragment.BaseFragment;
import com.music.eartrainr.fragment.GameStepSummaryFragment;
import com.music.eartrainr.fragment.IntervalDetectionStepFragment;
import com.music.eartrainr.interfaces.GameHelper;
import com.music.eartrainr.model.IntervalDetection;

import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class IntervalDetectionGameActivity extends BaseGameActivity<IntervalDetection> implements GameHelper {

  public static final String TAG = IntervalDetectionGameActivity.class.getSimpleName();
  private MediaPlayer mMediaPlayer;
  private ProgressDialog mDialog;

  //region LIFECYCLE

  @Override
  protected void onCreate(Bundle savedInstanceState) {
//    mDialog = ProgressDialog.show(this, "Game Manager", "Initializing Game...", false);

    boolean linear = getIntent().getBooleanExtra("linear", false);

    handleMultiplayer();

    setErrorTimeout(1500);
    setLinear(linear);
    setTitle("Tab Stepper <small>(" + (linear ? "" : "Non ") + "Linear)</small>");
    setAlternativeTab(false);

    //THIS MUST BE AT THE END
    super.onCreate(savedInstanceState);
  }

  @Override protected void onStart() {
    super.onStart();
    Bus.register(this);
  }

  @Override protected void onStop() {
    super.onStop();
    Bus.unregister(this);
  }





  private void handleMultiplayer() {
    Intent intent = getIntent();

    if (intent != null) {
      Bundle args = intent.getExtras();

      final boolean multiplayer = args.getBoolean(GameManager.GAMES.MULTIPLAYER, false);
      final String opponent = args.getString(GameManager.GAMES.OPPONENT, "");
      final int id = args.getInt(GameManager.GAMES.GAME_ID, 1);
      final String message = args.getString(GameManager.GAMES.MESSAGE, "");

      if (multiplayer) {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(message);
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

  private void updateDialogTimer(final String seconds) {
    if (mDialog != null) {
      runOnUiThread(new Runnable() {
        @Override public void run() {
          mDialog.setMessage(seconds);
        }
      });
    }
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

  public static Intent makeMultiplayerIntent(Context context, boolean notification, Bundle args) {
    Intent intent = new Intent(context, IntervalDetectionGameActivity.class);
    if (notification) {
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    intent.putExtras(args);
    return intent;
  }

  //endregion


  //region SUBSCRIBERS
  @Subscribe
  public void onGameManagerInitialized(final GameManagerEvent event) {
    if (event.mEventID == GameManagerEvent.EVENT.GAMES_READY) {
      if (mDialog != null && false) {
        mDialog.setMessage("Done");
        mDialog.hide();
      }
    }
  }

  @Subscribe
  public void onPrepareGame(final MultiPlayerEvent event) {
    if (event.mEventID == MultiPlayerEvent.EVENT.MATCH_PREPARE_PENDING) {
      if (mDialog != null && mDialog.isShowing()) {
        //TODO: parse js date and find difference
        String startTime = (String) event.mData;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      }
    }
  }


  //endregion
}
