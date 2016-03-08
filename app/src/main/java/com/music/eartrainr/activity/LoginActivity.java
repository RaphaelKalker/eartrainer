package com.music.eartrainr.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.R;
import com.music.eartrainr.fragment.LoginFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginActivity extends BaseActivity {

  private static final String TAG = LoginActivity.class.getSimpleName();
  @Bind(R.id.toolbar) Toolbar mToolbar;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);

    getSupportActionBar().setTitle("Login Page");

    Uri uri = new ModuleUri.BBuilder()
        .type(ModuleUri.Type.ACTIVITY)
        .activity(LoginActivity.TAG)
        .fragment(LoginFragment.TAG)
        .build();

    onFragmentInteraction(uri);
  }

  public LoginActivity() {
  }
}
