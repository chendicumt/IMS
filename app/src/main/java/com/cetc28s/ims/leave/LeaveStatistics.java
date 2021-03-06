package com.cetc28s.ims.leave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cetc28s.ims.R;
import com.cetc28s.ims.utils.StatueBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/13.
 * Version 1.0
 */

public class LeaveStatistics extends AppCompatActivity {

    @BindView(R.id.fragment_second_leave_statistics_toolBar)
    Toolbar mStatisToolBat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_second_leave_statistics);
        ButterKnife.bind(this);
        StatueBarUtil.StatusBarLightMode(this);
        setSupportActionBar(mStatisToolBat);
        ActionBar actionBar =getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return true;
    }
}
