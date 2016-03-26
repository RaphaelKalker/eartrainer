package com.music.eartrainr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.music.eartrainr.GameManager;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IntervalDetection extends BaseGame {

  public static IntervalDetection newStep(final String answer) {
    final IntervalDetection step = new IntervalDetection();
    step.setAnswer(answer);
    return step;
  }


  public String getAnswer() {
    return answer;
  }

  public void setAnswer(final String answer) {
    this.answer = answer;
  }

  String answer;

  public String getMidi() {
    return midi;
  }

  public void setMidi(final String midi) {
    this.midi = midi;
  }

  String midi;

  public String[] parseAnswers() {
    return getAnswer().split(",");
  }


}
