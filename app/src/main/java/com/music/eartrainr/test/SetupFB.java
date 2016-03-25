package com.music.eartrainr.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.music.eartrainr.Bus;
import com.music.eartrainr.Database;
import com.music.eartrainr.R;
import com.music.eartrainr.event.UIMessageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.music.eartrainr.FirebaseKeys.ANSWER;
import static com.music.eartrainr.FirebaseKeys.GAMES;
import static com.music.eartrainr.FirebaseKeys.INTERVAL_DETECTION;
import static com.music.eartrainr.FirebaseKeys.MIDI;


public class SetupFB {

  private static final int MAX_STEPS = 10;

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static void intervalDetectionAnswer(final Context context) {
    Firebase fb = Database.getSingleton().getFirebase();

    final List<HashMap> gameSteps = new ArrayList<>(MAX_STEPS);

    //First item is the default text, skip it!
    final String [] gameSizes = context.getResources().getStringArray(R.array.game_size);
    final String [] gameClasses = context.getResources().getStringArray(R.array.game_classes);

    for (int i = 0; i < MAX_STEPS; i++) {

      final HashMap map = new HashMap()
      {{
        put(ANSWER,constructAnswer(
            gameSizes[ThreadLocalRandom.current().nextInt(1,gameSizes.length - 1)],
            gameClasses[ThreadLocalRandom.current().nextInt(1,gameClasses.length - 1)])
        );
        put(MIDI, 999);
      }};

      gameSteps.add(map);
    }

    fb.child(GAMES).child(INTERVAL_DETECTION).setValue(gameSteps, new Firebase.CompletionListener() {
      @Override public void onComplete(
          final FirebaseError firebaseError,
          final Firebase firebase) {

        if (firebaseError != null) {
          Bus.post(new UIMessageEvent().message("Unable to setup interval Answers"));
        } else {
          Bus.post(new UIMessageEvent().message("Successfully setup interval Answers"));
        }
      }
    });
  }

  private static String constructAnswer(final String str1, final String str2) {
    return str1 + "," + str2;
  }
}
