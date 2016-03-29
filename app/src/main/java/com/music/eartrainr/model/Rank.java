package com.music.eartrainr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Rank {

  public Rank() { }

  private int game_1_score;
  private int game_2_score;
  private int game_3_score;
  private String username;

  public int getGame_1_score() { return game_1_score; }

  public int getGame_2_score() { return game_2_score; }

  public int getGame_3_score() { return game_3_score; }

  public int getTotalScore() {
    return (int) (getGame_1_score());
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }
}
