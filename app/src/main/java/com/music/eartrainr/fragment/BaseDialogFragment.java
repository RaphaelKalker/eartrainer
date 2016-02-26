package com.music.eartrainr.fragment;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.app.DialogFragment;


public class BaseDialogFragment extends DialogFragment implements FragmentNavigation {
  public static final String KEY_FRAGMENT_URI_ARG = "fragment_uri";
  private FragmentNavigation mNavigationCallback;


  @Override public void onAttach(final Activity activity) {
    super.onAttach(activity);
    if (activity instanceof FragmentNavigation) {
      mNavigationCallback = (FragmentNavigation) activity;
    } else {
      throw new RuntimeException(activity.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    mNavigationCallback = null;
  }

  @Override public void onFragmentInteraction(final Uri uri) {

  }

  @Override public Uri getUri() {
    return getArguments() != null ? (Uri) getArguments()
        .getParcelable(KEY_FRAGMENT_URI_ARG) : Uri.EMPTY;
  }
}
