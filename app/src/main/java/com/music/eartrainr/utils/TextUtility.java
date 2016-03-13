package com.music.eartrainr.utils;

import android.text.TextUtils;


public class TextUtility {

  public final static boolean isValidEmail(CharSequence target) {
    return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
  }

  public static String getUserNameFromEmail(final String email) {
    return email.split("\\@")[0];
  }
}
