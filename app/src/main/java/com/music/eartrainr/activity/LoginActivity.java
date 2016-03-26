package com.music.eartrainr.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.R;
import com.music.eartrainr.fragment.LoginFragment;
import com.music.eartrainr.interfaces.ResultCodes;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.music.eartrainr.interfaces.ResultCodes.*;


public class LoginActivity extends BaseActivity {

  public static final String TAG = LoginActivity.class.getSimpleName();
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

  @Override
  public void onBackPressed() {
    setResult(LOGIN);
    this.finish();
  }

//  @Override protected void onActivityResult(
//      final int requestCode,
//      final int resultCode,
//      final Intent data) {
//    super.onActivityResult(requestCode, resultCode, data);
//
//    if (resultCode == ResultCodes.MAIN) {
//      setResult(Res);
//      finish();
//    }
//  }
}
