package com.music.eartrainr.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ProgressBar;

import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.R;

import butterknife.Bind;


public class BaseDialogFragment extends DialogFragment implements ModuleUri.FragmentModule, FragmentProgress {
  public static final String KEY_FRAGMENT_URI_ARG = "fragment_uri";
  public ActivityNavigation mNavigationCallback;

  @Bind(R.id.progress_spinner) ProgressBar mProgressBar;


  @Override public void onAttach(final Activity activity) {
    super.onAttach(activity);
    if (activity instanceof ActivityNavigation) {
      mNavigationCallback = (ActivityNavigation) activity;
    } else {
      throw new RuntimeException(activity.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    mNavigationCallback = null;
  }

  @Override
  public Uri getUri() {
    return getArguments() != null ? (Uri) getArguments()
        .getParcelable(KEY_FRAGMENT_URI_ARG) : Uri.EMPTY;
  }

  @Override public String getTitle() {
    return "asdf";
  }

  @Override public void restoreState(final Bundle savedState) {

  }

  @Override public void showProgress() {
    if (mProgressBar != null) {
      mProgressBar.setVisibility(View.VISIBLE);
    }
  }

  @Override public void hideProgress() {
    if (mProgressBar != null) {
      mProgressBar.setVisibility(View.GONE);
    }
  }
}
