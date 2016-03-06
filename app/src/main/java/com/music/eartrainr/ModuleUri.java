package com.music.eartrainr;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.music.eartrainr.activity.MainActivity;
import com.music.eartrainr.fragment.ProfileFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.music.eartrainr.ModuleUri.Args.ACTION;


public class ModuleUri {

  private static final String SCHEME = "com.music.eartrainr";
  private static final String FRAGMENT_PATH = SCHEME + ".fragment.%s";
  private static final String ACTIVITY_PATH = SCHEME + ".activity.%s";
  private static final String FRAGMENT = "fragment";
  private static final String NAVIGATION = "navigation";
  private static final String USER = "user";
  private static final String TYPE = "type";
  private static final String BUNDLE = "bundle";
  private static final String ACTIVITY = "activity";

  private Context mContext;
  private Map<String, Object> mUriArgs;
  private Uri mUri;
  private List<String> mSegments;


  private ModuleUri(final Context applicationContext) {
    mUriArgs = new HashMap<>();
    mContext = applicationContext;
  }

  public String getUserId() {
    return (String) mUriArgs.get(USER);
  }

  public String getActivity() {
    return (String) mUriArgs.get(ACTIVITY);
  }

  public int getAction() {
    return mUriArgs.get(ACTION) != null ? Integer.valueOf((String) mUriArgs.get(ACTION)): Action.NONE;
  }

//  public String getActivityPath() {
//    return mUriArgs.get(ACTIVITY);
//    return mActivityPath;
//  }

  public static class BBuilder{
    //TODO move all builder stuff to inner class
  }

  public static Uri exit(Context context) {
    final ModuleUri uri = new ModuleUri(context);
    uri.mUriArgs.put(ACTION, Action.EXIT);
    return uri.build();
  }

  public static ModuleUri Builder(final Context applicationContext) {
    return new ModuleUri(applicationContext);
  }

  public ModuleUri to(final String destination) {
    mUriArgs.put(FRAGMENT, destination);
    return this;
  }

  public ModuleUri activity(final String activity) {
    mUriArgs.put(ACTIVITY, activity);
    return this;
  }

  public ModuleUri user(final String user) {
    mUriArgs.put(USER, user);
    return this;
  }

  public ModuleUri type(final int type) {
    mUriArgs.put(TYPE, String.valueOf(type));
    return this;
  }

//  public ModuleUri action(final Action action) {
//    mUriArgs.put(ACTION, action);
//    return this;
//  }

  public ModuleUri exitCurrent() {
    mUriArgs.put(ACTION, Action.EXIT);
    return this;
  }

  /*
  * CONVENIENCE NAVIGATION METHODS
  * */
  public ModuleUri navToProfile(final String username) {
    this.to(ProfileFragment.TAG);
    this.user(username);
    this.activity(MainActivity.TAG);
    return this;

  }

  public ModuleUri bundle(final Bundle bundle) {
    mUriArgs.put(BUNDLE, bundle);
    return this;
  }

  public Uri build() {
    final String app = mContext.getResources().getString(R.string.app_name);

    Uri.Builder builder = new Uri.Builder();
    builder.scheme(SCHEME);
    builder.appendPath(NAVIGATION);
//    builder.appendQueryParameter(FRAGMENT, mDestination);

    for (Map.Entry<String, Object> entry : mUriArgs.entrySet()) {
      Object val = entry.getValue();

      if (val == null) {
        continue;
      }

      if (val instanceof Bundle) {
        Wtf.log("GOTTA DO SOME BUNDLE SHIT");
      } else {
        builder.appendQueryParameter(entry.getKey(), val.toString());

      }
    }

    return builder.build();
  }

  public static String getFragmentString(@NonNull Uri uri) {

    List<String> segments = uri.getPathSegments();

    final int segmentSize = segments.size();

    final String instruction = segments.get(0);
    final String fragment = uri.getQueryParameter(FRAGMENT);
    final String type = uri.getQueryParameter(TYPE);

//    if (TextUtils.equals(instruction, FRAGMENT)) {
//      if (TextUtils.isEmpty(fragment)) {
//        throw new IllegalStateException("Instruction was Fragment, but no fragment was specified");
//      }
//    }

    return String.format(FRAGMENT_PATH, fragment);

  }


  //Type is either FRAGMENT / ACTIVITY
  public String getPath(@NonNull final String type ) {

    List<String> segments = mUri.getPathSegments();

    final int segmentSize = segments.size();

    final String instruction = segments.get(0);
    final String fragmentOrActivity = mUri.getQueryParameter(type);

    if (TextUtils.equals(instruction, type)) {
      if (TextUtils.isEmpty(fragmentOrActivity)) {
        throw new IllegalStateException("Instruction was Fragment, but no fragmentOrActivity was specified");
      }
    }

    return String.format(ACTIVITY_PATH, fragmentOrActivity);
  }

  public String getActivityPath() {
    String activityPath = "";
    final String activity = (String) mUriArgs.get(ACTIVITY);

    if (!TextUtils.isEmpty(activity)) {
       activityPath = String.format(ACTIVITY_PATH, activity);
    }
    return activityPath;
  }

  public static ModuleUri parseUri(final Context context, final Uri uri) {
    final ModuleUri moduleUri = new ModuleUri(context);
    moduleUri.mUri = uri;
    moduleUri.mSegments = uri.getPathSegments();

    Set<String> queryNames = uri.getQueryParameterNames();

    //Parses the arguments like fragment=FragmentName
    for (String queryKey : queryNames) {
      moduleUri.mUriArgs.put(queryKey, uri.getQueryParameter(queryKey));
    }

    return moduleUri;
  }

  public int getFragmentType(final Uri uri) {
//    return (int) mUriArgs.get(TYPE);
    return 0;
  }

  public boolean isDialogFragment() {
    return mUriArgs.get(TYPE) != null && Integer.valueOf((String) mUriArgs.get(TYPE)) == Type.DIALOG;
  }

  public boolean closeCurrentFragment() {
    return mUriArgs.get(ACTION) != null && mUriArgs.get(ACTION) == Action.EXIT;
  }

  public String getFragmentTag() {
    return mUriArgs != null ? (String) mUriArgs.get(FRAGMENT) : "";
  }


  public interface FragmentModule {
    Uri getUri();
    String getTitle();
    void restoreState(final Bundle savedState);

  }


  public interface Type {
    int DIALOG = 0;
    int OVERLAY = 1;
    int ACTIVITY = 2;
  }

  public interface Action {
    int EXIT = 0;
    int OPEN = 1;
    int NONE = -1;
  }

  public interface Args {
    String ACTION = "action";
  }
}
