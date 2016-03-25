package com.music.eartrainr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.music.eartrainr.GameManager;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IntervalDetection extends BaseGame {


  public String getAnswer() {
    return answer;
  }

  public void setAnswer(final String answer) {
    this.answer = answer;
  }

  String answer;

  public long getMidi() {
    return midi;
  }

  public void setMidi(final int midi) {
    this.midi = midi;
  }

  long midi;

  public String[] parseAnswers() {
    return getAnswer().split(",");
  }


}
