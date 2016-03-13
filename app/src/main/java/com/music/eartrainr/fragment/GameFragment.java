package com.music.eartrainr.fragment;

import android.util.SparseArray;


public abstract class GameFragment extends BaseFragment {

  SparseArray<Game> mGames = new SparseArray();

  class Game {


    private int midiFile;
    private int task;


    public int getMidiFile() {
      return midiFile;
    }

    public void setMidiFile(final int midiFile) {
      this.midiFile = midiFile;
    }

  }


  private Game getNextGame(final int gameSessionID) {
    return mGames.get(gameSessionID);
  }
}
