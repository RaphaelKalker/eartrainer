package com.github.fcannizzaro.materialstepper.style;

import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.R;
import com.github.fcannizzaro.materialstepper.adapter.PageAdapter;
import com.github.fcannizzaro.materialstepper.adapter.PageChangeAdapter;

import java.util.List;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
abstract class BasePager extends BaseStyle {

    // view
    protected ViewPager mPager;

    // adapters
    protected PageAdapter mPagerAdapter;

    protected void init() {
        mPager = (ViewPager) findViewById(R.id.stepPager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        mPager.addOnPageChangeListener(new PageChangeAdapter() {
            @Override
            public void onPageSelected(int position) {
                mSteps.get(position).onStepVisible();
                onPageChanged(position);
            }
        });
    }

    private void initAdapter() {
        if (mPagerAdapter == null)
            mPagerAdapter = new PageAdapter(getSupportFragmentManager());
    }

    @Override
    public void addStep(AbstractStep step) {
        super.addStep(step);
        initAdapter();
        mPagerAdapter.add(step);
    }

    @Override
    public void addSteps(List<AbstractStep> steps) {
        super.addSteps(steps);
        initAdapter();
        mPagerAdapter.set(mSteps.getSteps());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        mPager.setCurrentItem(mSteps.current());
    }

    public abstract void onPageChanged(final int position);

}
