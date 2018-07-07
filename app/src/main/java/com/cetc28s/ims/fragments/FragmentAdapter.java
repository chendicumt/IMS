package com.cetc28s.ims.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/2/27.
 * Version 1.0
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new FragmentSecond();
        } else if (position == 2) {
            return new FragmentThird();
        }else if (position == 3){
            return new FragmentForth();
        }
        return new FragmentFirst();
    }

    @Override
    public int getCount() {
        return 4;
    }
}
