package com.cetc28s.ims.myInfo;


import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cetc28s.ims.R;
import com.cetc28s.ims.fragments.FragmentFirst;
import com.cetc28s.ims.fragments.FragmentSecond;
import com.cetc28s.ims.fragments.FragmentThird;
import com.cetc28s.ims.utils.BaseActivity;
import com.cetc28s.ims.utils.Logger;
import com.cetc28s.ims.utils.MyApplication;
import com.cetc28s.ims.utils.NetInfo;
import com.cetc28s.ims.utils.StatueBarUtil;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/4/12.
 * Version 1.0
 */

public class MyInfoActivity extends BaseActivity {

    private String type;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_my_info);
        StatueBarUtil.StatusBarLightMode(this);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        Logger.d("----------------",type);
        initFragment(type);
        NetInfo.netWorkState(this);
    }


    public void initFragment(String type){
        MyApplication.flag = 1;
        if(type.equals("out")){
            getSupportFragmentManager().beginTransaction().add(R.id.base_layout,new FragmentFirst(),"first").commit();
        } else if(type.equals("leave")){
            getSupportFragmentManager().beginTransaction().add(R.id.base_layout,new FragmentSecond(),"second").commit();
        } else if(type.equals("log")){
            getSupportFragmentManager().beginTransaction().add(R.id.base_layout,new FragmentThird(),"third").commit();
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.base_layout,new MyTeamFragment(),"team").commit();
        }

    }

    @Override
    public void onBackPressed() {
        MyApplication.flag = 0;
        this.finish();
    }
}
