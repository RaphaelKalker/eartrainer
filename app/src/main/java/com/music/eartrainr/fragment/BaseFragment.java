package com.music.eartrainr.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.Wtf;


public abstract class BaseFragment<T>
    extends Fragment
    implements ModuleUri.FragmentModule {

  ActivityNavigation mNavigationCallback;
  private ProgressDialog mProgressDialog;


  @Override public void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mProgressDialog = new ProgressDialog(getContext(), 0);
    mProgressDialog.setTitle("Doing Stuff");
    mProgressDialog.setCancelable(true);
    mProgressDialog.setIndeterminate(true);
  }

  @Override public void onAttach(final Context context) {
    super.onAttach(context);

    if (context instanceof ActivityNavigation) {
      mNavigationCallback = (ActivityNavigation) context;
      mNavigationCallback.setTitle(getTitle());
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    mNavigationCallback = null;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public static void displayError(
      final View view,
      final String message) {
    if (view != null) {
      Wtf.log("Displaying Error: " + message);
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

  protected void startLoading(final String message) {
    if (getView() != null && mProgressDialog != null) {
      mProgressDialog.setMessage(message);
      mProgressDialog.show();
    }
  }

  protected void stopLoading() {
    if (mProgressDialog != null) {
//      mProgressDialog.setIndeterminate(false);
      mProgressDialog.hide();
    }
  }

  @Override
  public Uri getUri() {
    return getArguments() != null ? (Uri) getArguments()
        .getParcelable(ActivityNavigation.KEY_FRAGMENT_URI_ARG) : Uri.EMPTY;
  }

  public static Bundle getDefaultArgs(final Uri uri) {
    final Bundle bundle = new Bundle();
    bundle.putParcelable(ActivityNavigation.KEY_FRAGMENT_URI_ARG, uri);
    return bundle;
  }


}
