package com.music.eartrainr.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.music.eartrainr.Database;
import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.R;
import com.music.eartrainr.fragment.ProfileFragment;
import com.music.eartrainr.model.FirebaseRank;
import com.music.eartrainr.test.SetupFB;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

  public static final String TAG = MainActivity.class.getSimpleName();

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.nav_view) NavigationView mNavigationView;
  @Bind(R.id.nav_drawerlayout) DrawerLayout mDrawerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(mToolbar);

    mNavigationView.setNavigationItemSelectedListener(mNavigationItemListener);

    //Resumed state
    if (savedInstanceState != null) {
//      onFragmentInteraction(getUri(), savedInstanceState);
    } else {
      onFragmentInteraction(getUri());
    }
  }

  private NavigationView.OnNavigationItemSelectedListener mNavigationItemListener = new NavigationView.OnNavigationItemSelectedListener() {
    @Override public boolean onNavigationItemSelected(final MenuItem item) {
      String tag = "";

      final ModuleUri.BBuilder uriBuilder = new ModuleUri.BBuilder();
      final boolean exit = false;

      switch (item.getItemId()) {

        case R.id.nav_home:
          uriBuilder.activity(MainActivity.TAG).fragment(ProfileFragment.TAG);
          break;

        case R.id.nav_leader_board:
          uriBuilder.activity(LeaderBoardActivity.TAG);
          break;

        case R.id.nav_logout:
          Database.getSingleton().logout();
          finish();
          break;

        case R.id.nav_game1:
          uriBuilder.activity(IntervalDetectionGameActivity.TAG);
          break;

        case R.id.btn_temp:
          FirebaseRank.initDB();
          break;
        case R.id.btn_db_answer_init:
//          SetupFB.intervalDetectionAnswer(getApplicationContext());
          break;
      }

      mDrawerLayout.closeDrawers();

      if (uriBuilder.isNavigationDefined()) {
        final String user = Database.getSingleton().getUserName();

        onFragmentInteraction(
            uriBuilder.user(user).build()
        );
      }


      return false;
    }
  };
  
}
