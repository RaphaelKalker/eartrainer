package com.music.eartrainr.activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.music.eartrainr.Database;
import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.fragment.Game1Fragment;
import com.music.eartrainr.fragment.ProfileFragment;

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

    onFragmentInteraction(getUri());
  }

  private NavigationView.OnNavigationItemSelectedListener mNavigationItemListener = new NavigationView.OnNavigationItemSelectedListener() {
    @Override public boolean onNavigationItemSelected(final MenuItem item) {
      String tag = "";

      switch (item.getItemId()) {

        case R.id.nav_home:
          tag = ProfileFragment.TAG;
          break;

        case R.id.nav_logout:
          Database.getSingleton().logout();
          finish();
          break;

        case R.id.nav_game1:
          tag = Game1Fragment.TAG;
          break;
        case R.id.nav_game2:
        case R.id.nav_game3:
        case R.id.nav_game4:
          break;
      }

      mDrawerLayout.closeDrawers();

      onFragmentInteraction(
          new ModuleUri.BBuilder().fragment(tag).build()
      );

      return false;
    }
  };
  
}
