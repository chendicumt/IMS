package com.cetc28s.ims.myInfo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cetc28s.ims.R;
import com.cetc28s.ims.utils.ConstUtil;
import com.cetc28s.ims.utils.HttpUtils;
import com.cetc28s.ims.utils.JsonUtil;
import com.cetc28s.ims.utils.MyApplication;
import com.cetc28s.ims.utils.NetInfo;
import com.cetc28s.ims.utils.Toaster;
import com.google.gson.reflect.TypeToken;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/5/3.
 * Version 1.0
 */

public class MyTeamFragment extends Fragment {

    @BindView(R.id.recycler_team)
    PullLoadMoreRecyclerView teamRecycler;
    @BindView(R.id.tool_bar_image)
    ImageView back;
    @BindView(R.id.toolBar_team)
    Toolbar mToolbar;
    private View view;
    private String responseData;
    private TeamInfoBean teamInfo;
    private TeamRecyclerAdapter adapter;
    private List<TeamRecyInfo> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_my_team,container,false);
        ButterKnife.bind(this,view);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.flag = 0;
                getActivity().finish();
            }
        });
        mToolbar.setTitleTextColor(Color.BLACK);

        initData();
        return view;
    }
    
    /**
    *  @Description:  初始化数据
    *  @Author:  chendi
    *  @Time:  2018/5/4 0:17
    */
    public void initData(){
        if(NetInfo.netWorkState(getContext())){
            HttpUtils.sendGetRequest(ConstUtil.USER_STATE, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toaster.toast(getContext(),"获取信息失败");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    responseData = response.body().string();
                    teamInfo = JsonUtil.getGsonInstance().fromJson(responseData,new TypeToken<TeamInfoBean>(){}.getType());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(teamInfo.getData() != null){
                                setList();
                                initRecyclerView();
                            }else{
                                Toaster.toast(getContext(),"无相关信息");
                            }
                        }
                    });
                }
            });
        }
    }

    public void setList(){
        list.clear();
        String  state;
        String startTime;
        String endTime;
        for (int i = 0 ; i < teamInfo.getData().size() ; i++) {
            state = teamInfo.getData().get(i).getState();
            if(teamInfo.getData().get(i).getState().equals("")){
                state = "在所";
            }
            startTime = teamInfo.getData().get(i).getStartTime().split(" ")[0];
            if(teamInfo.getData().get(i).getStartTime().equals("1970-01-01 00:00:00")){
                startTime = "";
            }
            endTime = teamInfo.getData().get(i).getEndTime().split(" ")[0];
            if(teamInfo.getData().get(i).getEndTime().equals("1970-01-01 00:00:00")){
                endTime = "";
            }
            TeamRecyInfo info = new TeamRecyInfo(teamInfo.getData().get(i).getName(),
                    state,teamInfo.getData().get(i).getMessage(),
                    startTime,endTime);
            list.add(info);
        }
    }

    public void initRecyclerView(){
        adapter = new TeamRecyclerAdapter(list);
        teamRecycler.setAdapter(adapter);
        teamRecycler.setLinearLayout();
        teamRecycler.setPushRefreshEnable(false);
        teamRecycler.setPullRefreshEnable(false);
    }
}
