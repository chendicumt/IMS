package com.cetc28s.ims.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cetc28s.ims.R;
import com.cetc28s.ims.myInfo.MyInfoActivity;
import com.cetc28s.ims.utils.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/2/27.
 * Version 1.0
 */

public class FragmentForth extends Fragment {
    @BindView(R.id.fragment_forth_toolBar)
    Toolbar mToolbar;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_jobNum)
    TextView userJobNum;
    @BindView(R.id.my_out_info)
    LinearLayout myOutInfo;
    @BindView(R.id.my_leave_info)
    LinearLayout myLeaveInfo;
    @BindView(R.id.my_log_info)
    LinearLayout myLogInfo;
    @BindView(R.id.my_team)
    LinearLayout myTeam;
    /*@BindView(R.id.my_edit)
    TextView myEdit;
    @BindView(R.id.my_setting)
    TextView mySetting;
    @BindView(R.id.my_about)
    TextView myAbout;*/
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forth,container,false);
        ButterKnife.bind(this,view);
        mToolbar.setTitle("我  的");
        mToolbar.setTitleTextColor(Color.BLACK);

        userName.setText(MyApplication.appName);
        userJobNum.setText(MyApplication.appJobNum);

        myOutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyInfoActivity.class);
                intent.putExtra("type","out");
                startActivity(intent);
            }
        });
        myLeaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyInfoActivity.class);
                intent.putExtra("type","leave");
                startActivity(intent);
            }
        });
        myLogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyInfoActivity.class);
                intent.putExtra("type","log");
                startActivity(intent);
            }
        });
        myTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyInfoActivity.class);
                intent.putExtra("type","team");
                startActivity(intent);
            }
        });
        return view;
    }


}
