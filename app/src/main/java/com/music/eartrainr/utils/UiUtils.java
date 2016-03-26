package com.music.eartrainr.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;


public class UiUtils {

  public static void displayError(
      final View view,
      final String message) {
    if (view != null) {
      Snackbar sb = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
      sb.getView().setBackgroundColor(Color.RED);
      sb.show();
    }
  }

  public static void displaySuccess(
      final View view,
      final String message) {
    if (view != null) {
      Snackbar sb = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
      sb.getView().setBackgroundColor(Color.GREEN);
      sb.show();
    }
  }

  public static void displayValidationError(final View view, final String message) {
    Snackbar sb = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
    sb.getView().setBackgroundColor(Color.WHITE);
    sb.setActionTextColor(Color.BLACK);
    sb.show();
  }

  public static View inflate(final Context context, final ViewGroup root, final @LayoutRes int layoutId) {
    final Context themeWrapper = new ContextThemeWrapper(context, R.style.AppTheme);
    LayoutInflater lf = LayoutInflater.from(context).cloneInContext(themeWrapper);
    return lf.inflate(layoutId, root, false);
  }
}
