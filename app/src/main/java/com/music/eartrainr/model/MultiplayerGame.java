package com.music.eartrainr.model;

public class MultiplayerGame {
  public final String username;
  public final String competitor;
  public final int gameID;


  public MultiplayerGame(
      String username,
      String competitor,
      int gameID) {
    this.username = username;
    this.competitor = competitor;
    this.gameID = gameID;
  }
}
