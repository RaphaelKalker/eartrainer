package com.music.eartrainr.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.github.fcannizzaro.materialstepper.style.TabStepper;
import com.music.eartrainr.Bus;
import com.music.eartrainr.GameManager;
import com.music.eartrainr.event.GameManagerEvent;

import org.greenrobot.eventbus.Subscribe;


public abstract class BaseGameActivity<GAME> extends TabStepper {


  private ProgressDialog mProgressDialog;

  //region LIFE CYCLE
  @Override protected void onCreate(final Bundle savedInstanceState) {
    initGameTabs();
    initGameManager(getGameClazz());
    mProgressDialog = ProgressDialog.show(this, "Game Manager", "Initializing Game...", false);


    //RUN LAST
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

  @Override protected void onDestroy() {
    super.onDestroy();
    GameManager.destroy();
    if (mProgressDialog != null) {
      mProgressDialog.dismiss();
    }
  }

  //endregion

  public void initGameManager(Class<GAME> modelClazz) {
    GameManager.getInstance().doStuff(getGameIdentifier(), getGameClazz());
  }

  //region SUBSCRIBERS
  @Subscribe
  public void onGameManagerInitialized(final GameManagerEvent event) {
    if (event.mEventID == GameManagerEvent.EVENT.GAMES_READY) {
      if (mProgressDialog != null) {
        mProgressDialog.setMessage("Done");
        mProgressDialog.hide();
      }
    }
  }

  //endregion

  //region ABSTRACT METHODS
  abstract void initGameTabs();
  abstract Class<GAME> getGameClazz();
  abstract String getGameIdentifier();

  //endregion
}
