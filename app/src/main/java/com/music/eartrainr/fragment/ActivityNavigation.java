package com.music.eartrainr.fragment;

import android.net.Uri;


/**
 * Created by Raphael on 16-02-17.
 */
public interface ActivityNavigation {

  String KEY_FRAGMENT_URI_ARG = "fragment_uri";

  void onFragmentInteraction(Uri uri);
  Uri getUri();
  void setTitle(final String title);

}