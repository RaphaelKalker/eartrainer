package com.music.eartrainr.fragment;

import android.text.TextUtils;
import android.widget.Spinner;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.music.eartrainr.GameManager;
import com.music.eartrainr.R;

import static com.music.eartrainr.GameManager.GAMES.GAME_STEP_NR;

public abstract class GameFragment<G> extends AbstractStep {

  public static final boolean OVERRIDE_NEXT = true;


  @Override public boolean nextIf() {
    G gameData = (G) GameManager.getInstance().getGameData(getArguments().getInt(GAME_STEP_NR));

    if (gameData == null) {
      return true;
    } else {
      boolean inputValid = validateInput(gameData);
      return inputValid || OVERRIDE_NEXT;
    }
  }

  public String getValidationErrorString(VALIDATION validation) {
    if (validation == VALIDATION.CORRECT) {
      return "Please try again";
    }

    return validation ==
        VALIDATION.INCOMPLETE_INPUT ?
        getString(R.string.error_no_input) :
        getString(R.string.error_wrong_answer);
  }

  public enum VALIDATION {
    INCOMPLETE_INPUT,
    CORRECT,
    INCORRECT
  }

  abstract boolean validateInput(final G answer);

  protected VALIDATION verifyUserSelection(
      final String[] answers,
      final Spinner... spinners) {

    for (int i = 0; i < spinners.length; i++) {

      if (spinners[i].getSelectedItemPosition() == 0) {
        return VALIDATION.INCOMPLETE_INPUT;
      }

      final String input = spinners[i].getSelectedItem().toString();

      if (!TextUtils.equals(input, answers[i])) {
        return VALIDATION.INCORRECT;
      }
    }

    return VALIDATION.CORRECT;

  }

  @Override public void recordAnswer() {
    //OVERRIDE IF NEEDED
  }

  @Override public void updateCurrent() {
    //OVERRIDE IF NEEDED
  }
}
