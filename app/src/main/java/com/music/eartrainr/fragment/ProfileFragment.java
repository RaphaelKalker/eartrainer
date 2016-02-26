package com.music.eartrainr.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.music.eartrainr.Database;
import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.adapter.DataAdapter;
import com.music.eartrainr.R;
import com.music.eartrainr.Test;
import com.music.eartrainr.model.User;
import com.music.eartrainr.retrofit.FirebaseService;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;


public class ProfileFragment extends BaseFragment  {

  public static final String TAG = ProfileFragment.class.getSimpleName();

  @Bind(R.id.profile_name) TextView mProfileName;
  @Bind(R.id.profile_username) TextView mUserName;
  @Bind(R.id.profile_email) TextView mEmail;
  @Bind(R.id.profile_rank) TextView mRank;
  @Bind(R.id.profile_image) ImageView mProfileImage;
  @Bind(R.id.profile_friends) RecyclerView mFriendsList;
  @Bind(R.id.profile_add_friend) FloatingActionButton mAddFriendBtn;
  private DataAdapter mFriendsListAdapter;
  private Object mProfile;


  public ProfileFragment() {
    // Required empty public constructor
  }

  public static ProfileFragment newInstance(Uri uri) {
    ProfileFragment fragment = new ProfileFragment();
    Bundle args = new Bundle();
    args.putParcelable(FragmentNavigation.KEY_FRAGMENT_URI_ARG, uri);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }



    //Observales!
    Observable bservable = Observable.create(new Observable.OnSubscribe<String>(){

      @Override public void call(final Subscriber<? super String> subscriber) {
        subscriber.onNext("HELLO");
        subscriber.onNext(longRunningOperationObservableEmitter());
        subscriber.onCompleted();

      }

    });

    Action1<String> onNextActionWithoutCompletedOrError = new Action1<String>() {
      @Override public void call(final String s) {
        Wtf.log("onNextActionWithoutCompletedOrError");
      }
    };

    bservable.subscribe(new Subscriber<String>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(final Throwable e) {
        Wtf.log("We good");
      }

      @Override public void onNext(final String s) {
        Wtf.log(s);
      }
    });

//    bservable.subscribe(onNextActionWithoutCompletedOrError);



    //RETROFIT

    // Create a very simple REST adapter which points the GitHub API.
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(FirebaseService.API_URL_GIT)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    // Create an instance of our GitHub API interface.
    FirebaseService.GitHub github = retrofit.create(FirebaseService.GitHub.class);

    // Create a call instance for looking up Retrofit contributors.
//    Call<String> call = github.contributors().("raphael");
    Call<List<FirebaseService.Contributor>> call = github.contributors("square", "retrofit");

    call.enqueue(new Callback<List<FirebaseService.Contributor>>() {
      @Override public void onResponse(
          final Call<List<FirebaseService.Contributor>> call,
          final Response<List<FirebaseService.Contributor>> response) {
        Wtf.log();
      }

      @Override public void onFailure(
          final Call<List<FirebaseService.Contributor>> call,
          final Throwable t) {
        Wtf.log();
      }
    });


    Database.getSingleton().getProfile(Database.getSingleton().getUserId(), new Database.FirebaseGET<User>() {
      @Override public void onSuccess(final User user) {
        Wtf.log();
        mProfileName.setText(user.getUserName());
        mRank.setText(user.getRank());
        mEmail.setText(user.getEmail());
        mUserName.setText(user.getUserName());
      }

      @Override public void onError(final FirebaseError error) {
        Wtf.log();
      }
    });




  }

  @Override public void onViewCreated(
      final View view,
      @Nullable final Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ModuleUri moduleUri = ModuleUri.parseUri(getActivity(), getUri());
    mProfileName.setText(moduleUri.getUserId());
  }

  private String longRunningOperationObservableEmitter() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    int shit  = 1/0;
    return "DONE";
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    final View view = inflater.inflate(R.layout.fragment_profile, container, false);
    ButterKnife.bind(this, view);

    Glide
        .with(this)
        .load(R.drawable.raph)
        .centerCrop()
        .crossFade()
        .into(mProfileImage);



    mFriendsList.setLayoutManager(new LinearLayoutManager(getActivity()));
    mFriendsListAdapter = new DataAdapter(Test.getFriendList(), R.layout.list_profile_friends_item);
    mFriendsList.setAdapter(mFriendsListAdapter);


    return view;
  }

  @Override public void restoreState(final Bundle savedState) {

  }

  @Override public String getTitle() {
    return getString(R.string.fragment_title_profile);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }



  /*
  * ONCLICK LISTENERS
  * */

  @OnClick(R.id.profile_add_friend)
  public void onAddFriendClicked() {


    FriendAddFragment.Parameters params = new FriendAddFragment.Parameters.Builder()
        .title(getResources().getString(R.string.fragment_title_friendadd))
        .build();


    //open view on top
    final Uri uri = ModuleUri.Builder(getContext().getApplicationContext())
        .to(FriendAddFragment.TAG)
        .type(ModuleUri.Type.DIALOG)
        .bundle(params.bundle())
        .build();

    mNavigationCallback.onFragmentInteraction(uri);

  }



}
