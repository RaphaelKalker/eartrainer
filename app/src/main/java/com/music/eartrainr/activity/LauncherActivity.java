package com.music.eartrainr.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.music.eartrainr.Database;
import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.R;
import com.music.eartrainr.fragment.LoginFragment;
import com.music.eartrainr.interfaces.ResultCodes;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LauncherActivity extends BaseActivity {

//  @Bind(R.id.nav_view) NavigationView mNavigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launcher);
    ButterKnife.bind(this);


    //if login data present
    final SharedPreferences prefs = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE);
    final String userSessionKey = prefs.getString("user_session_key", "");

    if (TextUtils.isEmpty(userSessionKey)) {
      startActivity(this, LoginActivity.class, getUri(), ResultCodes.LOGIN);
    } else {
      startActivity(this, MainActivity.class, getUri(), ResultCodes.MAIN);
    }





//    final Uri uri = ModuleUri.Builder(getApplicationContext()).to(LoginFragment.TAG).build();
//
//    onFragmentInteraction(uri);
//
//    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
  //    setSupportActionBar(toolbar);
//
//    mNavigationView.setNavigationItemSelectedListener(mNavigationItemListener);

  }

  @Override protected void onActivityResult(
      final int requestCode,
      final int resultCode,
      final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (resultCode) {
      case ResultCodes.LOGIN:
        finish();
        break;
      case ResultCodes.MAIN:
        finish();
        break;
    }
  }
}
