package com.music.eartrainr;

import android.app.Application;

import com.firebase.client.Firebase;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import butterknife.ButterKnife;


public class EarTrainerApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    initFirebase();
    ButterKnife.setDebug(false);
    Iconify.with(new FontAwesomeModule());

  }

  private void initFirebase() {
    Wtf.log("FIRST");
    Firebase.setAndroidContext(getApplicationContext());
  }
}
