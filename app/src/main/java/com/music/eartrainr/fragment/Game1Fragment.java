package com.music.eartrainr.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.music.eartrainr.R;


public class Game1Fragment extends GameFragment {

  public static final String TAG = Game1Fragment.class.getSimpleName();

  public static Game1Fragment newInstance(final Uri uri) {
    final Game1Fragment game1Fragment = new Game1Fragment();
    game1Fragment.setArguments(getDefaultArgs(uri));
    return game1Fragment;
  }

  public Game1Fragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_game1, container, false);
  }


  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof FragmentNavigation) {
      mNavigationCallback = (FragmentNavigation) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mNavigationCallback = null;
  }

  @Override public void restoreState(final Bundle savedState) {

  }

  @Override public String getTitle() {
    return getString(R.string.fragment_title_game1) ;
  }
}
