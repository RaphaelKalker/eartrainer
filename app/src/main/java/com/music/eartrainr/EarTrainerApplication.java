package com.music.eartrainr;

import android.app.Application;

import com.firebase.client.Firebase;

import butterknife.ButterKnife;


public class EarTrainerApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    initFirebase();
    ButterKnife.setDebug(false);

  }

  private void initFirebase() {
    Wtf.log("FIRST");
    Firebase.setAndroidContext(getApplicationContext());
  }
}
