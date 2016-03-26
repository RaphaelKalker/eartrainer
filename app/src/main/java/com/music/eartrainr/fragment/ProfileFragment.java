package com.music.eartrainr.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.FirebaseError;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.music.eartrainr.Bus;
import com.music.eartrainr.Database;
import com.music.eartrainr.ModuleUri;
import com.music.eartrainr.Wtf;
import com.music.eartrainr.adapter.ProfileFriendsAdapter;
import com.music.eartrainr.R;
import com.music.eartrainr.adapter.RecyclerViewBaseAdapter.VisibilitySettings;
import com.music.eartrainr.event.FriendItemGetEvent;
import com.music.eartrainr.model.User;
import com.music.eartrainr.utils.DividerItemDecoration;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileFragment extends BaseFragment implements ProfileFriendsAdapter.OnRowItemClick {

  public static final String TAG = ProfileFragment.class.getSimpleName();

  //region BUNDLE KEYS
  private static final String USER_KEY = "user";
  //endregion

  //region BUTTERKNIFE

  @Bind(R.id.profile_name) TextView mProfileName;
  @Bind(R.id.profile_username) TextView mUserName;
  @Bind(R.id.profile_email) TextView mEmail;
  @Bind(R.id.profile_rank) TextView mRank;
  @Bind(R.id.profile_image) MaterialLetterIcon mProfileImage;
  @Bind(R.id.profile_friends) RecyclerView mFriendsList;
  @Bind(R.id.profile_add_friend) FloatingActionButton mAddFriendBtn;

  //endregion

  private ProfileFriendsAdapter mFriendsListAdapter;
  private Object mProfile;
  private List<User> mFriends;
  private String mCurrentUser;

  //region START STUFF
  public ProfileFragment() {
    // Required empty public constructor
  }

  public static ProfileFragment newInstance(Uri uri) {
    ProfileFragment fragment = new ProfileFragment();
    Bundle args = new Bundle();
    args.putParcelable(ActivityNavigation.KEY_FRAGMENT_URI_ARG, uri);
    fragment.setArguments(args);
    return fragment;
  }

  //endregion

  //region LIFECYCLE
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

    if (savedInstanceState != null) {
      restoreState(savedInstanceState);
    } else {
      mCurrentUser = ModuleUri.parseUri(getContext(), getUri()).getUser();
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_profile, container, false);
    return view;
  }

  @Override public void onViewCreated(
      final View view,
      @Nullable final Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);



    initView();
    setupFriendList();

    Database.getSingleton().getFriends(mCurrentUser);
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

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
    mFriendsListAdapter.removeListener();
  }


  @Override public void onSaveInstanceState(final Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(USER_KEY, mCurrentUser);
  }

  //endregion

  //region UI HELPERS

  public void initView() {
    mProfileName.setText(mCurrentUser);
    mProfileImage.setLetter(mCurrentUser);
    mAddFriendBtn.setVisibility(getVisiblitySettings() == VisibilitySettings.OWNER ? View.VISIBLE : View.INVISIBLE);
  }

  private void setupFriendList() {
    mFriendsList.setLayoutManager(new LinearLayoutManager(getActivity()));
    mFriendsList
        .addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    mFriendsListAdapter = new ProfileFriendsAdapter(getActivity(), R.layout.list_profile_friends_item, getVisiblitySettings());
    mFriendsList.setAdapter(mFriendsListAdapter);
    mFriendsListAdapter.setOnRowItemClickListener(this);
  }

  public VisibilitySettings getVisiblitySettings() {
    if (TextUtils.equals(Database.getSingleton().getUserName(), mCurrentUser)) {
      return VisibilitySettings.OWNER;
    } else {
      return VisibilitySettings.OTHER;
    }
  }

  @Override public void restoreState(final Bundle savedState) {
    mCurrentUser = savedState.getString(USER_KEY);
  }

  @Override public String getTitle() {
    return getString(R.string.fragment_title_profile);
  }
  //endregion

  //region EVENTS

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

    if (event.mEventID == FriendItemGetEvent.EVENT.DELETE_REQUEST) {
      Wtf.log("Delete request");
      startLoading("Deleting friend...");
      final User deleteUser = (User) event.mData;
      final String currentUser = Database.getSingleton().getUserName();
      Database.getSingleton().removeFriend(deleteUser.getUserName(), currentUser, true);
      return;
    }

    if (event.mEventID == FriendItemGetEvent.EVENT.DELETE_SUCCESS) {
      stopLoading();
      displaySuccess(getView(), getString(R.string.friend_add_friend_deleted));
      mFriendsListAdapter.processDeletion();
      return;
    }

  }

  //endregion

  //region CLICK LISTENERS

  @OnClick(R.id.profile_add_friend)
  public void onAddFriendClicked() {

    FriendAddFragment.Parameters params = new FriendAddFragment.Parameters.Builder()
        .title(getResources().getString(R.string.fragment_title_friendadd))
        .build();

    //open view on top
    final Uri uri = new ModuleUri.BBuilder()
        .fragment(FriendAddFragment.TAG)
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
      final Uri uri = new ModuleUri.BBuilder().navToProfile(user.getUserName());
      mNavigationCallback.onFragmentInteraction(uri);
    } else {
      displayError(getView(), getString(R.string.fail_navigation));
    }
  }

  //endregion
}
