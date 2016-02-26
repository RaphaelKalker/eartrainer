package com.music.eartrainr.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.music.eartrainr.Auth;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;


public class FriendAddFragment extends BaseDialogFragment {

  public static final String TAG = FriendAddFragment.class.getSimpleName();

  public FriendAddFragment() {
    // Required empty public constructor
  }

  public static FriendAddFragment newInstance(final Uri uri){
    FriendAddFragment fragment = new FriendAddFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }

    setStyle(DialogFragment.STYLE_NORMAL, 0);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_friend_add, container, false);
  }

  @Override public void onViewCreated(
      final View view,
      final Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (true) {
      final Uri uri = getUri();
      Wtf.log(uri.toString());
    }
  }


  public static class Parameters {
    public static final String TAG = Parameters.class.getCanonicalName();
    private static final String KEY_TITLE = Auth.createKey(TAG, "title");

    public String mTitle;

    private Parameters() {
    }

    private Parameters(final Bundle in) {
      mTitle = in.getString(KEY_TITLE);
    }

    public static Parameters obtain(final Bundle bundle) {
      Parameters parameters;

      if (bundle == Bundle.EMPTY) {
        parameters = new Parameters.Builder().build();
      } else {
        parameters = new Parameters(bundle);
      }
      return parameters;
    }

    public Bundle bundle() {
      final Bundle bundle = new Bundle();
      bundle.putString(KEY_TITLE, mTitle);
      return bundle;
    }

    public static class Builder {

      private final Parameters mParameters;

      public Builder() {
        mParameters = new Parameters();
      }

      public Parameters build() {
        return mParameters;
      }

      public Builder title(final String title) {
        mParameters.mTitle = title;
        return this;
      }
    }
  }
}
