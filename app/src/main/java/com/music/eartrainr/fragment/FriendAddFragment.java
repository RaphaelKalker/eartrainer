package com.music.eartrainr.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.eartrainr.Auth;
import com.music.eartrainr.Bus;
import com.music.eartrainr.Database;
import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.event.FriendAddedEvent;
import com.music.eartrainr.model.User;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FriendAddFragment extends BaseDialogFragment {

  public static final String TAG = FriendAddFragment.class.getSimpleName();

  @Bind(R.id.friend_add_username) TextView mUserName;
  @Bind(R.id.status_message) TextView mStatusMessage;


  public FriendAddFragment() {
    // Required empty public constructor
  }

  public static FriendAddFragment newInstance(final Uri uri){
    FriendAddFragment fragment = new FriendAddFragment();
    return fragment;
  }

  @Override public void onStart() {
    super.onStart();
    Bus.register(this);
  }

  @Override public void onStop() {
    super.onStart();
    Bus.unregister(this);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }

    setStyle(DialogFragment.STYLE_NORMAL, 0);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_friend_add, container, false);
  }

  @Override public void onViewCreated(
      final View view,
      final Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);

    if (true) {
      final Uri uri = getUri();
      Wtf.log("Is this uri valid:" + uri.toString());
    }
  }

  @OnClick(R.id.friend_add_btn)
  public void onFriendAddClick() {

    showProgress();
    mStatusMessage.setText(getString(R.string.friend_add_searching));

    final String username = mUserName.getText().toString();

    Wtf.log("Trying to add friend: " + username);

    //TODO: check if the username exists
    Database.getSingleton().findUser(username);
  }

  @Subscribe(priority = 2)
  public void onFriendStatusUpdate(final FriendAddedEvent event) {

    Wtf.logEvent(event.mEventID);

    //Successfully added friend
    if (event.mEventID == FriendAddedEvent.EVENT.FRIEND_ADDED &&
        event.mData != null) {

      finishSearch(R.string.friend_add_successful);

      final User user = (User) event.mData;
      Wtf.log("TODO: time to navigate back to profile view");

      mNavigationCallback.onFragmentInteraction(
          ModuleUri.exit(getActivity().getApplicationContext())
      );

      return;
    }

    //Found User, now adding as friend.
    if (event.mEventID == FriendAddedEvent.EVENT.USER_FOUND){
      mStatusMessage.setText(getString(R.string.friend_add_friend));
      Database.getSingleton().addFriend((User) event.mData);
      return;
    }

    if (event.mEventID == FriendAddedEvent.EVENT.USER_UNKNOWN) {
      finishSearch(R.string.friend_add_not_found);
      return;
    }
  }

  private void finishSearch(final int stringId) {
    mProgressBar.setVisibility(View.GONE);
    mStatusMessage.setText(getString(stringId));
  }


  public static class Parameters {
    public static final String TAG = Parameters.class.getCanonicalName();
    private static final String KEY_TITLE = Auth.createKey(TAG, "title");
    private static final String KEY_FRIEND = Auth.createKey(TAG, "friend");


    public String mTitle;
    public String mUserName;

    private Parameters() {
    }

    private Parameters(final Bundle in) {
      mTitle = in.getString(KEY_TITLE);
    }

    public static Parameters obtain(final Bundle bundle) {
      Parameters parameters;

      if (bundle == Bundle.EMPTY) {
        parameters = new Parameters.Builder().build();
      } else {
        parameters = new Parameters(bundle);
      }
      return parameters;
    }

    public Bundle bundle() {
      final Bundle bundle = new Bundle();
      bundle.putString(KEY_TITLE, mTitle);
      bundle.putString(KEY_FRIEND, mUserName);
      return bundle;
    }

    public static class Builder {

      private final Parameters mParameters;
      private final Bundle mBundle;

      public Builder() {
        mParameters = new Parameters();
        mBundle = new Bundle();
      }

      public Parameters build() {
        return mParameters;
//        return mBundle;
      }

      public Builder title(final String title) {
        mParameters.mTitle = title;
//        mBundle.putString(KEY_TITLE, title);
        return this;
      }

      public Builder friend(final User user) {
//        mBundle.putString(KEY_FRIEND, friendName);
        mParameters.mUserName = user.getUserName();
        return this;
      }
    }
  }
}
