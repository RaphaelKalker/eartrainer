package com.music.eartrainr.activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.music.eartrainr.Database;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

  public static final String TAG = MainActivity.class.getSimpleName();

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.nav_view) NavigationView mNavigationView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);

    getSupportActionBar().setTitle("Main Activity");

    onFragmentInteraction(getUri());
    mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override public boolean onNavigationItemSelected(final MenuItem item) {
        Wtf.log("NAVIGATION SELECTED");
        return false;
      }
    });

  }

  private NavigationView.OnNavigationItemSelectedListener mNavigationItemListener = new NavigationView.OnNavigationItemSelectedListener() {
    @Override public boolean onNavigationItemSelected(final MenuItem item) {
      Wtf.log();

      switch (item.getItemId()) {
        case R.id.nav_logout:
          Wtf.log("Logout");
          //TODO: show some dialog

          //TODO: go back to login activity with login fragment
          Database.getSingleton().logout();
          finishActivity(0);
          break;
      }
      return false;
    }
  };



}
