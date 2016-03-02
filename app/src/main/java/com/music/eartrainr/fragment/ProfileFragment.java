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
import com.firebase.client.annotations.NotNull;
import com.music.eartrainr.Bus;
import com.music.eartrainr.Database;
import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.adapter.DataAdapter;
import com.music.eartrainr.R;
import com.music.eartrainr.Test;
import com.music.eartrainr.event.FireBaseEvent;
import com.music.eartrainr.event.FriendAddedEvent;
import com.music.eartrainr.event.FriendItemGetEvent;
import com.music.eartrainr.model.User;
import com.music.eartrainr.retrofit.FirebaseService;

import org.greenrobot.eventbus.Subscribe;

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


public class ProfileFragment extends BaseFragment implements DataAdapter.OnRowItemClick  {

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
  private List<User> mFriends;
  private String mCurrentUser;


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

  @Override public void onStart() {
    super.onStart();
    Bus.register(this);
  }

  @Override public void onStop() {
    super.onStop();
    Bus.unregister(this);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }

    mCurrentUser = ModuleUri.parseUri(getContext(), getUri()).getUserId();

    Database.getSingleton().getProfile(mCurrentUser, new Database.FirebaseGET<User>() {
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

  @Subscribe(priority = 1)
  public void onFriendStatusUpdate(final FriendAddedEvent event) {
    if (event.mEventID == FriendAddedEvent.EVENT.FRIEND_ADDED) {
      final User user = (User) event.mData;
      mFriends = mFriendsListAdapter.getDataSource();
      mFriends.add(user);
      mFriendsListAdapter.setDataSource(mFriends);
      Wtf.log("Got user in profile view: " + user.getUserName());
    }
  }

  @Subscribe
  public void onFriendListItemGet(final FriendItemGetEvent<FirebaseError, User> event) {
    Wtf.logEvent(event.mEventID);

    if (event.mEventID == FriendItemGetEvent.EVENT.FRIEND_RETRIEVED) {
      Wtf.log("adding user!");
      mFriendsListAdapter.addItem((User) event.mData);
      return;
    }
  }

  @Subscribe
  public void onFriendListItemDelete(final FriendItemGetEvent event) {

    if (event.mEventID == FriendItemGetEvent.EVENT.DELETE_REQUEST){
      Wtf.log("Delete request");
      final User deleteUser = (User) event.mData;
      final String currentUser = Database.getSingleton().getUserName();
      Database.getSingleton().removeFriend(deleteUser.getUserName(), currentUser, true);
      return;
    }

    if (event.mEventID == FriendItemGetEvent.EVENT.DELETE_SUCCESS){
      Wtf.log("Delete success");
      displaySuccess(getView(), getString(R.string.friend_add_friend_deleted));
      mFriendsListAdapter.processDeletion();
      return;
    }

  }


  @Override public void onViewCreated(
      final View view,
      @Nullable final Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ModuleUri moduleUri = ModuleUri.parseUri(getActivity(), getUri());
    mProfileName.setText(mCurrentUser);
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

    mFriendsList.setLayoutManager(new LinearLayoutManager(getActivity()));
    mFriendsListAdapter = new DataAdapter(R.layout.list_profile_friends_item);
    mFriendsList.setAdapter(mFriendsListAdapter);
    mFriendsListAdapter.setOnRowItemClickListener(this);

    Database.getSingleton().getFriends(mCurrentUser);

    Glide
        .with(this)
        .load(R.drawable.raph)
        .centerCrop()
        .crossFade()
        .into(mProfileImage);






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
    mFriendsListAdapter.removeListener();
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


  @Override public void onRowItemClick(
      final Object item,
      final int position) {
    final User user = (User) item;

    if (user != null) {
      Wtf.log("Clicked on user: " + user.getUserName());
      final Uri uri = ModuleUri.Builder(getActivity().getApplicationContext()).navToProfile(user.getUserName()).build();
      mNavigationCallback.onFragmentInteraction(uri);
    } else {
      displayError(getView(), getString(R.string.fail_navigation));
    }
  }
}
