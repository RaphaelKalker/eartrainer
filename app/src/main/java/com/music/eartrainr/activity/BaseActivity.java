package com.music.eartrainr.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.music.eartrainr.Bus;
import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.event.UIMessageEvent;
import com.music.eartrainr.fragment.BaseFragment;
import com.music.eartrainr.fragment.ActivityNavigation;
import com.music.eartrainr.utils.UiUtils;

import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.music.eartrainr.ModuleUri.Action.EXIT;


public abstract class BaseActivity
    extends AppCompatActivity
    implements ActivityNavigation {

  public static final boolean LOGGING = true;
  private static final String URI = "uri";
  private Uri mUri;


  public static void startActivity(final BaseActivity current, final Class<?> next, final Uri uri) {
    final Intent intent =  new Intent(current, next);
    intent.putExtra(URI, uri);
    current.startActivity(intent);
  }

  public static void startActivity(final BaseActivity current, final Class<?> next, final Uri uri, final int resultCode) {
    final Intent intent =  new Intent(current, next);
    intent.putExtra(URI, uri);
    current.startActivityForResult(intent, resultCode);
  }



  public void startActivity(final Uri uri) {
    final ModuleUri moduleUri = ModuleUri.parseUri(getApplicationContext(), uri);
//    final String clazzPath = moduleUri.getActivityPath();
  }

  @Override protected void onStart() {
    super.onStart();
    Bus.register(this);
  }

  @Override protected void onStop() {
    super.onStop();
    Bus.unregister(this);
  }

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();

    if (intent != null) {
      mUri = intent.getParcelableExtra(URI);
    }
  }

  @Override public void onFragmentInteraction(
      final Uri uri,
      final Bundle savedInstance) {

    if (LOGGING) Wtf.log("Opening... " + uri.toString());

    final ModuleUri moduleUri = ModuleUri.parseUri(getApplicationContext(), uri);
    final int action = moduleUri.getAction();
    final String user = moduleUri.getUser();
    final String fragmentTag = moduleUri.getFragmentTag();

    /*
    * Requesting close
    * */
    if (action == EXIT) {
      Wtf.log("Closing Fragment");
      getSupportFragmentManager().popBackStack();
      return;
    }

    /*
    * Warn if no user specified
    * */
    if (TextUtils.isEmpty(user)) {
      Wtf.warn("User name was not specified");
    }

    final String clazzPath = ModuleUri.getFragmentString(uri);
    final int fragmentType = moduleUri.getFragmentType(uri);

    try {

        /*
      * Launch a new activity if the current one is not correct.
      * Note: We launch the fragment on the second pass
      * */

      if (launchActivityIfNecessary(moduleUri)) {
        return;
      }


      final Class<?> clazz = Class.forName(clazzPath);
      final Class [] args = new Class[1];
      args[0] = Uri.class;
      final Method method = clazz.getMethod("newInstance", args);




      /*
      * Launch a new fragment
      * */
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();



      /*
      * Do not .commit() a dialog - .show() takes care of it
      * Remove any current dialog fragment on the screen
      * */
      if (moduleUri.isDialogFragment()) {
        DialogFragment dialog = (DialogFragment) method.invoke(null, uri);


        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");

        if (prev != null) {
          ft.remove(prev);
        }

        ft.addToBackStack(null);


        dialog.show(ft, "dialog");

      } else {
        BaseFragment fragment = (BaseFragment) method.invoke(null, uri);
        BaseFragment prev = (BaseFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);

        if (prev == null || true) {
          ft.replace(R.id.fragment_container, fragment, fragmentTag)
            .addToBackStack(null)
            .commit();
        }
      }







    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (ClassCastException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      Wtf.log("The method newInstance(Uri uri) could not be found");
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }



  }


  private boolean launchActivityIfNecessary(ModuleUri moduleUri) throws ClassNotFoundException {

    boolean handled = false;

    final String currentActivity = this.getClass().getSimpleName();

    if (!TextUtils.equals(currentActivity, moduleUri.getActivity()) &&
        !TextUtils.isEmpty(moduleUri.getActivityPath())) {
      final Class<?> activityClazz = Class.forName(moduleUri.getActivityPath());
      Wtf.log("Starting Activity: " + moduleUri.getActivity());
      BaseActivity.startActivity(this, activityClazz, moduleUri.getUri());
      handled = true;
    }

    return handled;
  }

  @Override public void onFragmentInteraction(final Uri uri) {
    onFragmentInteraction(uri, null);
  }

  @Override public Uri getUri() {
    return mUri;
  }

  @Override public void setTitle(final String title) {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(title);
    }
  }

  //region EVENTS
  @Subscribe
  public void onNewMessage(final UIMessageEvent event) {
    final View rootView = findViewById(R.id.nav_drawerlayout);

    if (rootView != null) {
      UiUtils.displaySuccess(rootView, event.mData);
    }
  }
  //endregion
}
