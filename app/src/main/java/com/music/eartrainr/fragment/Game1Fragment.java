package com.music.eartrainr.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.joanzapata.iconify.widget.IconTextView;
import com.music.eartrainr.R;
import com.music.eartrainr.Wtf;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;


public class Game1Fragment extends GameFragment {

  public static final String TAG = Game1Fragment.class.getSimpleName();

//  public static final int MIDI_EXAMPLE = R.raw.moonlight_movement1;
  private MediaPlayer mMediaPlayer;
  private State mState;

  @Bind(R.id.game1_replay_btn) Button mReplayBtn;
  @Bind(R.id.game1_skip_btn) Button mSkipBtn;
  @Bind(R.id.game1_end_btn) Button mEndBtn;
  @Bind(R.id.game1_submit_btn) Button mSubmitBtn;
  @Bind(R.id.game1_class_spinner) Spinner mClassDropdown;
  @Bind(R.id.game1_size_spinner) Spinner mSizeDropdown;
  @Bind(R.id.game1_musical_note) IconTextView mMusicNote;


  public static Game1Fragment newInstance(final Uri uri) {
    final Game1Fragment game1Fragment = new Game1Fragment();
    game1Fragment.setArguments(getDefaultArgs(uri));
    return game1Fragment;
  }

  public Game1Fragment() {
    // Required empty public constructor
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
    return inflater.inflate(R.layout.fragment_game1, container, false);
  }

  @Override public void onViewCreated(
      final View view,
      @Nullable final Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ActivityNavigation) {
      mNavigationCallback = (ActivityNavigation) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mNavigationCallback = null;

    if (mMediaPlayer != null) {
      mMediaPlayer.setOnCompletionListener(null);
    }
  }

  @Override public void onResume() {
    super.onResume();

    if (mMediaPlayer == null) {
//      initMediaPlayer();
    }

    initView();

  }

  @Override public void onPause() {
    super.onPause();

    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
      mMediaPlayer.pause();
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void restoreState(final Bundle savedState) {

  }

  @Override public String getTitle() {
    return getString(R.string.fragment_title_game1);
  }

  /*
  * VIEW STUFF
  * */

  private void initView() {
    ArrayAdapter classAdapter = ArrayAdapter.createFromResource(
        getContext(),
        R.array.game_classes,
        android.R.layout.simple_spinner_item
    );

    classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mClassDropdown.setAdapter(classAdapter);

    ArrayAdapter sizeAdapter = ArrayAdapter.createFromResource(
        getContext(),
        R.array.game_size,
        android.R.layout.simple_spinner_item
    );

    sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSizeDropdown.setAdapter(sizeAdapter);
  }

  private void initMediaPlayer(final int rawMidiFile) {
    if (mMediaPlayer == null) {
      mMediaPlayer = MediaPlayer.create(getContext(), rawMidiFile);
      mMediaPlayer.setScreenOnWhilePlaying(true);
      mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override public void onCompletion(final MediaPlayer mp) {
          mMusicNote.setTextColor(ContextCompat.getColor(getContext(), R.color.md_blue_grey_200));
        }
      });
      mMediaPlayer.start();
      mMusicNote.setTextColor(ContextCompat.getColor(getContext(), R.color.md_green_400));
    }
  }

    private enum State {
      PLAYING,
      DONE,

    }

  public void setReplayState() {
    if (mState == State.PLAYING) {
      mReplayBtn.setVisibility(View.GONE);
    }

    if (mState == State.DONE) {
      mReplayBtn.setVisibility(View.VISIBLE);
    }
  }

  /*
  * ON CLICKS
  * */

  @OnClick(R.id.game1_replay_btn)
  public void onReplayClick() {
    if (mMediaPlayer != null) {
      mMediaPlayer.pause();
      mMediaPlayer.seekTo(0);
      mMediaPlayer.start();
    }
  }

  @OnClick(R.id.game1_skip_btn)
  public void onSkipClick() {
    startLoading(getString(R.string.game_next_game));
    cleanupView();


    //Start Loading
  }

  private void cleanupView() {

//    Game game = getNextGame()

    if (mMediaPlayer != null) {
      mMediaPlayer.reset();
      mMediaPlayer = null;
//      initMediaPlayer();
    }
  }



  @OnItemSelected(R.id.game1_class_spinner)
  public void onClassItemSelected() {
    Wtf.log();
  }


}
