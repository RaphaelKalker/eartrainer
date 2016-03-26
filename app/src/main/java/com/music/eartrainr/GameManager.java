package com.music.eartrainr;


import android.os.Bundle;
import android.os.Handler;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.music.eartrainr.event.GameManagerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GameManager<STEP> {

  private static volatile GameManager INSTANCE;
  private volatile List<STEP> mGameSteps;
  private volatile int wrongGuesses;
  private volatile int correctGuesses;

  //region INSTANTIATION
  private GameManager() {
  }

  public static GameManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new GameManager();
      INSTANCE.mGameSteps = new ArrayList();
    }
    return INSTANCE;
  }

  public void destroy() {
    INSTANCE = null;
  }

  //endregion

  public static int[] getMidiArray() {
    return new int[] {
        R.raw.maj2ac4,
        R.raw.maj3ac4,
        R.raw.min3ac4,
        R.raw.per4ac4,
        R.raw.per5ac4,
        R.raw.maj6ac4,
        R.raw.min6ac4,
        R.raw.maj7ac4,
        R.raw.min7ac4,
        R.raw.per8ac4
    };
  }

//  public String getAnswer(final String game) {
//    doStuff(game, mModelClazz);
//    return "";
//  }

  public STEP getGameData(final int stepPosition) {
    if (mGameSteps == null || mGameSteps.size() == 0) {
      throw new IllegalStateException("There are no game steps. You called this too early!");
    }

    return mGameSteps.get(stepPosition);
  }

  public void doStuff(final String gameIdentifier, final Class<?> clazz) {
    Firebase fb = Database.getSingleton().getFirebase();


    fb.child("games").child(gameIdentifier)
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override public void onDataChange(final DataSnapshot dataSnapshot) {

            final List<STEP> gameSteps = new ArrayList<STEP>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              STEP gameStep = (STEP) snapshot.getValue(clazz);
              mGameSteps.add(gameStep);
            }

            Bus.post(new GameManagerEvent().gamesReady());

//            new Handler().postDelayed(new Runnable(){
//              public void run() {
//
//                // do something
//              }}, 5000);

          }

          @Override public void onCancelled(final FirebaseError firebaseError) {
            //do nothing
          }
        });
  }

  public void addAnswer(final boolean success) {
    if (success) {
      correctGuesses += 1;
    } else {
      wrongGuesses += 1;
    }
  }

  public HashMap getSummaryStats() {
    final HashMap map = new HashMap()
    {{
      put("correct", String.valueOf(correctGuesses));
      put("incorrect", String.valueOf(wrongGuesses));
    }};

    return map;
  }


  public interface GAMES {
    String GAME_LAYOUT = "game_layout";
    String STEP_MIDI_FILE = "step_midi_file";
    String GAME_NAME = "game_name";
    String GAME_STEP_NR = "inner_game_nr";
    String INTERVAL_DETECTION = "interval_detection";
  }

//  public static class GameBuilder {
//
//    private Game mGame;
//    private int mLayout;
//    private String mName;
//
//    public GameBuilder() {
//    }
//
//
//
//    public GameBuilder name(final String name) {
//      mName = name;
//      return this;
//    }
//
//    public Game build() {
//      final Game game = new Game();
//      game.setLayout(mLayout);
//      game.setName(mName);
//      return game;
//    }
//  }


//  public static class Game {
//    private int layout;
//    private String name;
//    private int innerGameNr;
//
//    public int getLayout() {
//      return layout;
//    }
//
//    public void setLayout(final int layout) {
//      this.layout = layout;
//    }
//
//    public String getName() {
//      return name;
//    }
//
//    public void setName(final String name) {
//      this.name = name;
//    }
//
//    public int getInnerGameNr() {
//      return innerGameNr;
//    }
//
//    public void setInnerGameNr(final int innerGameNr) {
//      this.innerGameNr = innerGameNr;
//    }
//  }



  public static class StepBuilder {
    private Bundle mArgs;

    public StepBuilder() {
      mArgs = new Bundle();
    }

    public StepBuilder midiFile(final int midiFile) {
      mArgs.putInt(GAMES.STEP_MIDI_FILE, midiFile);
      return this;
    }

    public StepBuilder layout(final int layout) {
      mArgs.putInt(GAMES.GAME_LAYOUT, layout);
      return this;
    }

    public StepBuilder stepNr(final int nr) {
      mArgs.putInt(GAMES.GAME_STEP_NR, nr);
      return this;
    }

    public Bundle build() {
      return mArgs;
    }
  }

//  public static class Step {
//    private int midifile;
//
//    public int getMidifile() {
//      return midifile;
//    }
//
//    public void setMidifile(final int midifile) {
//      this.midifile = midifile;
//    }
//  }

}
