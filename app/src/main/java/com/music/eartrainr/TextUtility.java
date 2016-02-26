package com.music.eartrainr;

import android.text.TextUtils;


public class TextUtility {

  public final static boolean isValidEmail(CharSequence target) {
    return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
  }

}
