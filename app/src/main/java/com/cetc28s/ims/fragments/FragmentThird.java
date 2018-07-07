package com.cetc28s.ims.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cetc28s.ims.R;
import com.cetc28s.ims.leave.beans.LeaveInfo;
import com.cetc28s.ims.utils.Common;
import com.cetc28s.ims.utils.ConstUtil;
import com.cetc28s.ims.utils.DialogCallBack;
import com.cetc28s.ims.utils.DialogUtil;
import com.cetc28s.ims.utils.HttpUtils;
import com.cetc28s.ims.utils.JsonUtil;
import com.cetc28s.ims.utils.Logger;
import com.cetc28s.ims.utils.MyApplication;
import com.cetc28s.ims.utils.NetInfo;
import com.cetc28s.ims.utils.OnItemClickListener;
import com.cetc28s.ims.utils.Toaster;
import com.cetc28s.ims.worklog.LogRecyclerAdapter;
import com.cetc28s.ims.worklog.WorkLogDetail;
import com.cetc28s.ims.worklog.beans.LogInfo;
import com.cetc28s.ims.worklog.beans.LogRecyInfoBean;
import com.google.gson.reflect.TypeToken;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

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
 * Created by chendi on 2018/2/27.
 * Version 1.0
 */

public class FragmentThird extends Fragment {

    @BindView(R.id.fragment_third_toolBar)
    Toolbar mThirToolbar;
    @BindView(R.id.fragment_third_recycler)
    PullLoadMoreRecyclerView mRecyclerView;
    @BindView(R.id.tool_bar_image)
    ImageView titleImage;
    @BindView(R.id.tool_bar_title)
    TextView title;
    private View view;
    private LogRecyclerAdapter mLogRecyclerAdapter;
    private List<LogRecyInfoBean> list = new ArrayList<>();
    private String responseData;
    private LogInfo mLogInfo;
    private String url;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_third, container, false);
        ButterKnife.bind(this, view);

        MyApplication.autoRefresh = 0;
        titleImage.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        mThirToolbar.inflateMenu(R.menu.third_fragment_menu);
        if(MyApplication.flag == 1){
            titleImage.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            titleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.flag = 0;
                    getActivity().finish();
                }
            });
        }else{
            mThirToolbar.setTitle("日  志");
        }
        mThirToolbar.setTitleTextColor(Color.BLACK);
        mThirToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.log_search) {
                   // initDialog();
                    new DialogUtil().initDialog("log",ConstUtil.LOG_INFO_SEARCH, R.layout.fragment_third_search_dialog
                            , R.id.fragment_third_search_name, 0, R.id.fragment_third_search_startTime
                            , R.id.fragment_third_search_endTime, 0, getContext(), new DialogCallBack() {
                                @Override
                                public void getData(String response) {
                                    responseData = response;
                                    if (responseData.equals("")) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toaster.toast(getContext(),"获取信息失败");
                                            }
                                        });
                                    }else{
                                        mLogInfo = JsonUtil.getGsonInstance().fromJson(responseData, new TypeToken<LogInfo>(){}.getType());
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (mLogInfo.getStatus() == 0) {
                                                    setList();
                                                    initRecyclerView();
//                                                    mLogRecyclerAdapter.notifyDataSetChanged();
                                                } else {
                                                    Toaster.toast(getContext(), "无相关信息");
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
                if (id == R.id.log_add) {
                    Intent intent = new Intent(getContext(), WorkLogDetail.class);
                    intent.putExtra("isAdd", true);
                    startActivity(intent);
                }
                return true;
            }
        });
        initData();
        return view;
    }

    /**
     * @Description: 初始化recyclerview
     * @Author: chendi
     * @Time: 2018/4/9 0:11
     */
    private void initData() {
        if(MyApplication.flag == 1){
            url = ConstUtil.LOG_INFO_SEARCH + "?name="+MyApplication.appName +"&startTime=" + Common.getStartDate() + "&endTime=" + Common.getEndDate();
        }else{
            url = ConstUtil.LOG_INFO_SEARCH + "?startTime=" + Common.getToday() + "&endTime=" + Common.getToday();
        }
        Logger.d("-----------log>",url);
        if(NetInfo.netWorkState(getContext())){
            HttpUtils.sendGetRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toaster.toast(getContext(), "获取信息失败");
                            mRecyclerView.setPullRefreshEnable(false);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    responseData = response.body().string();
                    Logger.d("-----------log>",responseData);
                    mLogInfo = JsonUtil.getGsonInstance().fromJson(responseData, new TypeToken<LogInfo>() {
                    }.getType());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mLogInfo.getData() != null) {
                                setList();
                                initRecyclerView();
                            } else {
                                // Toaster.toast(getContext(), "无相关信息");
                                mRecyclerView.setPullRefreshEnable(false);
                            }
                        }
                    });
                }
            });
        }
    }


    /**
     * @Description: 装在数据
     * @Author: chendi
     * @Time: 2018/3/18 22:27
     */
    public void setList() {
        list.clear();
        for (int i = mLogInfo.getData().size() - 1; i >= 0; i--) {
            LogRecyInfoBean logRecyInfoBean = new LogRecyInfoBean(mLogInfo.getData().get(i).getName(),
                    mLogInfo.getData().get(i).getStartTime(), mLogInfo.getData().get(i).getWorkContent());
            list.add(logRecyInfoBean);
        }
    }

    /**
     * @Description: 初始化recyclerview
     * @Author: chendi
     * @Time: 2018/3/18 22:26
     */
    public void initRecyclerView() {
        mLogRecyclerAdapter = new LogRecyclerAdapter(list);
        setOnItemClickListener();
        mRecyclerView.setAdapter(mLogRecyclerAdapter);
        mRecyclerView.setLinearLayout();
        mRecyclerView.setPushRefreshEnable(false);
        mRecyclerView.setPullRefreshEnable(true);
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       /* if(MyApplication.flag == 1){
                            url = ConstUtil.LOG_INFO_SEARCH+"?name="+MyApplication.appName;
                        }else{
                            url = ConstUtil.LOG_INFO_ALL;
                        }*/
                        HttpUtils.sendGetRequest(url, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toaster.toast(getContext(), "获取信息失败");
                                        mRecyclerView.setPullLoadMoreCompleted();
                                    }
                                });
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                responseData = response.body().string();
                                Logger.d("-----------re",responseData);
                                mLogInfo = JsonUtil.getGsonInstance().fromJson(responseData, new TypeToken<LogInfo>() {
                                }.getType());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mLogInfo.getStatus() == 0) {
                                            setList();
                                            mLogRecyclerAdapter.notifyDataSetChanged();
                                        } else {
//                                            Toaster.toast(getContext(), "无相关信息");
                                        }
                                        mRecyclerView.setPullLoadMoreCompleted();
                                    }
                                });
                            }
                        });
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    /**
     * @Description: recyclerview的item点击事件
     * @Author: chendi
     * @Time: 2018/3/18 22:26
     */

    public void setOnItemClickListener() {
        mLogRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), WorkLogDetail.class);
                intent.putExtra("isAdd", false);
                intent.putExtra("pos", mLogInfo.getData().size() - 1 - position);
                intent.putExtra("dataLength", mLogInfo.getData().size());
                intent.putExtra("data", responseData);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (MyApplication.autoRefresh == 1) {
            initData();
        }
    }
}
