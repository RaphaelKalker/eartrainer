package com.music.eartrainr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Rank {

  public Rank() {
  }

  private long game_1_won;
  private long game_1_lost;
  private int game_1_score;
  private String username;

  public long getGame_1_won() {
    return game_1_won;
  }

  public void setGame_1_won(final long game_1_won) {
    this.game_1_won = game_1_won;
  }

  public long getGame_1_lost() {
    return game_1_lost;
  }

  public void setGame_1_lost(final long game_1_lost) {
    this.game_1_lost = game_1_lost;
  }

  public int getGame_1_score() { return game_1_score; }

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
