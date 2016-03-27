package com.music.eartrainr.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.music.eartrainr.Database;
import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.R;
import com.music.eartrainr.api.MultiplayerService;
import com.music.eartrainr.event.MultiPlayerEvent;
import com.music.eartrainr.fragment.ProfileFragment;
import com.music.eartrainr.interfaces.ResultCodes;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.gcm.QuickstartPreferences;
import com.music.eartrainr.gcm.RegistrationIntentService;
import com.music.eartrainr.model.FirebaseRank;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

  public static final String TAG = MainActivity.class.getSimpleName();

  private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

  private BroadcastReceiver mRegistrationBroadcastReceiver;
  private boolean isReceiverRegistered;

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.nav_view) NavigationView mNavigationView;
  @Bind(R.id.nav_drawerlayout) DrawerLayout mDrawerLayout;
  private TextView mUserName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(mToolbar);

    initView();

    //Resumed state
    if (savedInstanceState != null) {
//      onFragmentInteraction(getUri(), savedInstanceState);
    } else {
      onFragmentInteraction(getUri());
    }

    mRegistrationBroadcastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        boolean sentToken = sharedPreferences
                .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
        if (sentToken) {
          Wtf.log(getString(R.string.gcm_send_message));
      } else {
        Wtf.log(getString(R.string.token_error_message));
      }
      }
    };

    // Registering BroadcastReceiver
    registerReceiver();

    if (checkPlayServices()) {
      // Start IntentService to register this application with GCM.
      Intent intent = new Intent(this, RegistrationIntentService.class);
      startService(intent);
    }
  }

  private void initView() {
    if (mNavigationView != null) {
      mNavigationView.setNavigationItemSelectedListener(mNavigationItemListener);
      View header = mNavigationView.getHeaderView(0);
      mUserName = ButterKnife.findById(header, R.id.drawer_username);
      mUserName.setText(Database.getSingleton().getUserName());
    }
  }

  @Override
  public void onBackPressed() {
    if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
      setResult(ResultCodes.MAIN);
      this.finish();
    } else {
      getSupportFragmentManager().popBackStack();
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
          uriBuilder.activity(LoginActivity.TAG);
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

  private void registerReceiver(){
    if(!isReceiverRegistered) {
      LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
              new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
      isReceiverRegistered = true;
    }
  }

  /**
   * Check the device to make sure it has the Google Play Services APK. If
   * it doesn't, display a dialog that allows users to download the APK from
   * the Google Play Store or enable it in the device's system settings.
   */
  private boolean checkPlayServices() {
    Wtf.log("Checking play services");
    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
    int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
    if (resultCode != ConnectionResult.SUCCESS) {
      if (apiAvailability.isUserResolvableError(resultCode)) {
        apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                .show();
      } else {
        Wtf.log("This device is not supported");
        finish();
      }
      return false;
    }
    return true;
  }
}
