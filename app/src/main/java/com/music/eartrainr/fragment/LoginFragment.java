package com.music.eartrainr.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.FirebaseError;
import com.firebase.client.annotations.NotNull;
import com.music.eartrainr.Bus;
import com.music.eartrainr.Database;
import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.R;
import com.music.eartrainr.TextUtility;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.activity.MainActivity;
import com.music.eartrainr.event.SignInEvent;
import com.music.eartrainr.event.SignUpEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginFragment extends BaseFragment implements FragmentNavigation {

  public static final String TAG = LoginFragment.class.getSimpleName();

  @Bind(R.id.login_signin_btn) Button mSignInBtn;
  @Bind(R.id.login_signup_btn) Button mSignUpBtn;
  @Bind(R.id.login_email) EditText mEmail;
  @Bind(R.id.login_password) EditText mPassword;
  @Bind(R.id.login_toggle_signup_btn) TextView mToggleTxt;
  @Bind(R.id.login_progress) ProgressBar mProgressBar;

  private int mLoginState = SIGN_UP;

  public static final String BUNDLE_EMAIL_KEY = "email";
  public static final String BUNDLE_PASSWORD_KEY = "password";
  public static final String BUNDLE_TOGGLE_STATE = "toggle_state";

  public static final int SIGN_IN = 0;
  public static final int SIGN_UP = 1;

  public static LoginFragment newInstance(final Uri uri) {
    LoginFragment fragment = new LoginFragment();
    Bundle args = new Bundle();
    args.putParcelable(KEY_FRAGMENT_URI_ARG, uri);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onStart() {
    super.onStart();
    Bus.register(this);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login, container, false);

    ButterKnife.bind(this, view);

    restoreState(savedInstanceState);

    mEmail.setText("raphael@gmail.com");
    mPassword.setText("password");

    return view;
  }

  @Override
  public void restoreState(final Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      mLoginState = savedInstanceState.getInt(BUNDLE_TOGGLE_STATE, SIGN_IN);
      mEmail.setText(savedInstanceState.getString(BUNDLE_EMAIL_KEY));
      mPassword.setText(savedInstanceState.getString(BUNDLE_PASSWORD_KEY));

    } else {
    }

    initView();
  }

  private void initView() {
    mSignUpBtn.setVisibility(mLoginState == SIGN_UP ? View.VISIBLE : View.GONE);
    mSignInBtn.setVisibility(mLoginState == SIGN_IN ? View.VISIBLE : View.GONE);

    mToggleTxt.setText(
        mLoginState == SIGN_IN ?
        getResources().getText(R.string.login_toggle_signup) :
        getResources().getText(R.string.login_toggle_signin));

  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onStop() {
    super.onStop();
    Bus.unregister(this);
  }

  @OnClick(R.id.login_signin_btn)
  public void onSignInBtnClick() {
    Wtf.log();
    mProgressBar.setVisibility(View.VISIBLE);

    final String email = mEmail.getText().toString();
    final String password = mPassword.getText().toString();

    if (!TextUtility.isValidEmail(email)) {
      Wtf.log();
      mEmail.setError(getResources().getString(R.string.error_invalid_email));
      return;
    }

    if (TextUtils.isEmpty(password)) {
      mPassword.setError(getResources().getString(R.string.error_invalid_password));
      return;
    }

    Database.getSingleton().authorize(email, password, null);







    final boolean success = false;

    //if the user hasn't added the right details
    if (!success) {

    } else {


    }
  }

  @OnClick(R.id.login_signup_btn)
  public void onSignupClick() {
    Wtf.log("sign up click");


    final String email = mEmail.getText().toString();
    final String password = mPassword.getText().toString();

    if (mLoginState == SIGN_UP) {

      if (!TextUtility.isValidEmail(email)) {
        Wtf.log();
        mEmail.setError(getResources().getString(R.string.error_invalid_email));
        return;
      }

      if (TextUtils.isEmpty(password)) {
        mPassword.setError(getResources().getString(R.string.error_invalid_password));
        return;
      }

      mProgressBar.setVisibility(View.VISIBLE);
      Database.getSingleton().createUser(email, password);

    } else {
      throw new IllegalStateException("The sign in view is visible, but the sign UP view should be visible.");
    }
  }

  @OnClick(R.id.login_toggle_signup_btn)
  public void onToggleClick() {
    mLoginState = mLoginState == SIGN_IN ? SIGN_UP : SIGN_IN;
    initView();

  }



  @Subscribe
  public void onSignUpComplete(@NotNull final SignUpEvent event) {

    Wtf.log("Caught Event: " + event.mEventID);

    if (event.mEventID == Database.EventToken.NEW_USER) {
      mProgressBar.setVisibility(View.GONE);

      if (event.mError != null) {
        displayError(getView(), ((FirebaseError) event.mError).getMessage());
        return;
      }

      if (event.mData != null) {

        final String uid = (String) event.mData;

        if (TextUtils.isEmpty(uid)) {
          displayError(getView(), getString(R.string.error_login_generic));
          return;
        }

        //show creating profile dialog

        //Sanity check
        if (!TextUtils.equals(Database.getSingleton().getUserId(), uid)) {
//          throw new SecurityException("User ID does not match");
          //TODO: figure out why the ids are not the same
          Wtf.log("SECURITY EXCEPTION");
        }

        Uri uri = ModuleUri.Builder(getActivity().getApplicationContext())
                           .to(ProfileFragment.TAG)
                           .user(Database.getSingleton().getUserId())
                           .build();

        mNavigationCallback.onFragmentInteraction(uri);


      }
    }
  }


  @Subscribe
  public void onAuthorizationComplete(@NotNull final SignInEvent event) {

    Wtf.log("onAuthorizationComplete");

    if (event.mEventID == Database.EventToken.AUTHORIZATION) {
      mProgressBar.setVisibility(View.GONE);
      Wtf.log();
      final AuthData authData;
      final FirebaseError error;
      if ((authData = (AuthData)event.mData) != null) {
        displaySuccess(getView(), authData.getToken());
        authData.getAuth();
        authData.getToken();

        //TODO: launch new activity

        Uri uri = ModuleUri.Builder(getActivity().getApplicationContext())
                           .activity(MainActivity.TAG)
                           .to(ProfileFragment.TAG)
                           .user(Database.getSingleton().getEmail())
                           .build();


        //it was the correct information
        mNavigationCallback.onFragmentInteraction(uri);


      } else if ((error = (FirebaseError) event.mError) != null){
        displayError(getView(), error.getMessage());

      }
    }
  }


  @Override public void onFragmentInteraction(final Uri uri) {

  }

  @Override public Uri getUri() {
    return null;
  }

  @Override public String getTitle() {
    return getString(R.string.fragment_title_login);
  }
}
