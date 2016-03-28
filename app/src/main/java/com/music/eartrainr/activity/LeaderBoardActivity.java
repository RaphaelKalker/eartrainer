package com.music.eartrainr.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.music.eartrainr.Bus;
import com.music.eartrainr.R;
import com.music.eartrainr.event.RankItemGetEvent;
import com.music.eartrainr.fragment.LeaderBoardFragment;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LeaderBoardActivity extends BaseActivity {

  public static final String TAG = LeaderBoardActivity.class.getSimpleName();

  @Bind(R.id.leaderboard_viewpager) ViewPager mViewPager;
  @Bind(R.id.leaderboard_tabs) TabLayout mTabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_leader_board);
    ButterKnife.bind(this);

    mViewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), this));
    mTabLayout.setupWithViewPager(mViewPager);
  }

  //endregion

  //region ADAPTER

  public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final static int PAGE_COUNT = 1;
    private String tabTitles[];
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
      super(fm);
      this.context = context;
      tabTitles = context.getResources().getStringArray(R.array.games);
    }

    @Override
    public int getCount() {
      return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
      return LeaderBoardFragment.newInstance(null);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return tabTitles[position];
    }
  }

  //endregion
}

