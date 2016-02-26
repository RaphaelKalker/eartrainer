package com.music.eartrainr.activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.Toolbar;

import com.music.eartrainr.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

  public static final String TAG = MainActivity.class.getSimpleName();

  @Bind(R.id.toolbar) Toolbar mToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);

    getSupportActionBar().setTitle("Main Activity");

    onFragmentInteraction(getUri());


  }



}
