package com.music.eartrainr.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.github.fcannizzaro.materialstepper.style.TabStepper;
import com.music.eartrainr.Bus;
import com.music.eartrainr.GameManager;
import com.music.eartrainr.event.GameManagerEvent;
import com.music.eartrainr.event.MultiPlayerEvent;

import org.greenrobot.eventbus.Subscribe;


public abstract class BaseGameActivity<GAME> extends TabStepper {

  private ProgressDialog mProgressDialog;

  //region LIFE CYCLE
  @Override protected void onCreate(final Bundle savedInstanceState) {
    initGameTabs();
    initGameManager(getGameClazz());


    //RUN LAST
    super.onCreate(savedInstanceState);
  }



  //endregion

  public void initGameManager(Class<GAME> modelClazz) {
    GameManager.getInstance().doStuff(getGameIdentifier(), getGameClazz());
  }



  //region ABSTRACT METHODS
  abstract void initGameTabs();
  abstract Class<GAME> getGameClazz();
  abstract String getGameIdentifier();

  //endregion
}
