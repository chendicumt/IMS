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
import com.cetc28s.ims.leave.LeaveInfoDetail;
import com.cetc28s.ims.leave.LeaveRecyclerAdapter;
import com.cetc28s.ims.leave.beans.LeaveInfo;
import com.cetc28s.ims.leave.beans.LeaveRecyInfo;
import com.cetc28s.ims.outWork.beans.OutInfoBean;
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

public class FragmentSecond extends Fragment {

    @BindView(R.id.fragment_second_toolBar)
    Toolbar mSecToolbar;
    @BindView(R.id.fragment_second_recycler)
    PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    @BindView(R.id.tool_bar_image)
    ImageView titleImage;
    @BindView(R.id.tool_bar_title)
    TextView title;
    private View view;
    private LeaveRecyclerAdapter mRecyclerAdapter;
    private List<LeaveRecyInfo> list = new ArrayList<>();
    private String responseData;
    private LeaveInfo mLeaveInfo;
    private String url;
    private boolean isSaveFillTime = false;

    public interface FragmentLeaveCallBack{
        void getLeaveData(String data);
    }

    private FragmentLeaveCallBack mLeaveCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(MyApplication.flag != 1){
            mLeaveCallBack = (FragmentLeaveCallBack)getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second,container,false);
        ButterKnife.bind(this,view);

        MyApplication.autoRefresh = 0;
        //添加toolbar
        titleImage.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        mSecToolbar.inflateMenu(R.menu.second_fragment_menu);
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
            mSecToolbar.setTitle("请  假");
        }
        mSecToolbar.setTitleTextColor(Color.BLACK);
        mSecToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
               /* if (id == R.id.leave_statistics) {
                    Intent intent = new Intent(getContext(), LeaveStatistics.class);
                    startActivity(intent);
                }*/
                if (id == R.id.leave_add) {
                    Intent intent = new Intent(getContext(), LeaveInfoDetail.class);
                    intent.putExtra("isAdd",true);
                    startActivity(intent);
                }
                if (id == R.id.leave_search) {
                    //initDialog();
                    new DialogUtil().initDialog("leave",ConstUtil.LEAVE_INFO_SEARCH, R.layout.fragment_second_search_dialog
                            , R.id.fragment_second_search_name, R.id.fragment_second_search_type, R.id.fragment_second_search_startTime
                            , R.id.fragment_second_search_endTime, R.id.fragment_second_search_okPerson, getContext(), new DialogCallBack() {
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
                                        mLeaveInfo = JsonUtil.getGsonInstance().fromJson(responseData,new TypeToken<LeaveInfo>(){}.getType());
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(mLeaveInfo.getData() != null) {
                                                    setList();
                                                    initRecycler();
//                                                    mRecyclerAdapter.notifyDataSetChanged();
                                                }else{
                                                    Toaster.toast(getContext(),"无相关信息");
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
                return true;
            }
        });
        initData();

        return view;
    }


    /**
    *  @Description:  初始化recyclerview
    *  @Author:  chendi
    *  @Time:  2018/4/8 23:58
    */
    public void initData(){

        if(MyApplication.flag == 1){
            url = ConstUtil.LEAVE_INFO_SEARCH + "?name=" + MyApplication.appName;
        }else{
            url = ConstUtil.LEAVE_INFO_SEARCH + "?startTime=" + Common.getStartDate() + " 00:00:00&endTime=" + Common.getEndDate()+" 00:00:00";
            isSaveFillTime = true;
        }
        if(NetInfo.netWorkState(getContext())){
            HttpUtils.sendGetRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toaster.toast(getContext(),"获取信息失败");
                            mPullLoadMoreRecyclerView.setPullRefreshEnable(false);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    responseData = response.body().string();
                    Logger.d("----------->",responseData);
                    mLeaveInfo = JsonUtil.getGsonInstance().fromJson(responseData,new TypeToken<LeaveInfo>(){}.getType());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mLeaveInfo.getData() != null){
                                setList();
                                initRecycler();
                                if(isSaveFillTime){
                                    saveFillTime(getActivity(),mLeaveInfo,"leave");
                                }
                            }else{
                                // Toaster.toast(getContext(),"无相关信息");
                                mPullLoadMoreRecyclerView.setPullRefreshEnable(false);
                            }

                        }
                    });
                }
            });
        }
    }


    public void saveFillTime(Activity activity, LeaveInfo leaveInfo, String info){
        int size = leaveInfo.getData().size()-1;
        SharedPreferences sharedPreferences = activity.getSharedPreferences("timeInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String fillTime = leaveInfo.getData().get(size).getFillingTime();
        editor.putString(info,fillTime);
        editor.commit();
    }

    /**
     *  @Description:  装载数据
     *  @Author:  chendi
     *  @Time:  2018/3/16 23:52
     */
    public void setList(){
        list.clear();
        String timeLength;
        double time;
        for(int i = mLeaveInfo.getData().size()-1 ; i>= 0 ; i--){
            time = mLeaveInfo.getData().get(i).getVocationDay();
            if (Common.getDecimalPoint(time) == 2) {
                timeLength =((int)(time*100))/100+" 天 "
                        +((int)(time*100))%100+" 小时";
            }else{
                timeLength =((int)(time*10))/10+" 天 "
                        +((int)(time*10))%10+" 小时";
            }
            LeaveRecyInfo leaveRecyInfo = new LeaveRecyInfo(mLeaveInfo.getData().get(i).getName(),
                    mLeaveInfo.getData().get(i).getType(),mLeaveInfo.getData().get(i).getStartTime().substring(0,10),
                    mLeaveInfo.getData().get(i).getEndTime().substring(0,10),timeLength);
            list.add(leaveRecyInfo);
        }
    }
    /**
    *  @Description:  初始化recyclerview
    *  @Author:  chendi
    *  @Time:  2018/3/17 0:04
    */
    public void initRecycler(){
//        final PullLoadMoreRecyclerView mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView)view.findViewById(R.id.fragment_second_recycler);
        mRecyclerAdapter = new LeaveRecyclerAdapter(list);
        setOnRecyclerViewItemClick();
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerAdapter);
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setPullRefreshEnable(true);
        mPullLoadMoreRecyclerView.setPushRefreshEnable(false);
        //mRecyclerView.setFooterViewText("正在加载...");
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /*if(MyApplication.flag == 1){
                                url = ConstUtil.LEAVE_INFO_SEARCH+"?name="+MyApplication.appName;
                            }else{
                                url = ConstUtil.LEAVE_INFO_SEARCH + "?startTime=" + Common.getStartDate() + " 00:00:00&endTime=" + Common.getEndDate()+" 00:00:00";
                            }*/
                            Logger.d("----------->order",url);
                            HttpUtils.sendGetRequest(url, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toaster.toast(getContext(),"获取信息失败");
                                            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    responseData = response.body().string();
                                    Logger.d("----------->",responseData);
                                    mLeaveInfo = JsonUtil.getGsonInstance().fromJson(responseData,new TypeToken<LeaveInfo>(){}.getType());
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(mLeaveInfo.getData() != null){
                                                setList();
                                                mRecyclerAdapter.notifyDataSetChanged();
                                                if(isSaveFillTime){
                                                    saveFillTime(getActivity(),mLeaveInfo,"leave");
                                                }
                                                if(MyApplication.flag != 1){
                                                    mLeaveCallBack.getLeaveData("true");
                                                }
                                            }else{
//                                                Toaster.toast(getContext(),"无相关信息");
                                            }
                                            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                                        }
                                    });

                                }
                            });

                        }
                    },2000);
                }

                @Override
                public void onLoadMore() {

                }
            });

    }


/**
*  @Description:  添加item事件
*  @Author:  chendi
*  @Time:  2018/4/8 23:57
*/
    public void setOnRecyclerViewItemClick(){
        mRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(getContext(),LeaveInfoDetail.class);
                intent.putExtra("isAdd",false);
                intent.putExtra("pos",mLeaveInfo.getData().size()-position-1);
                intent.putExtra("dataLength",mLeaveInfo.getData().size());
                intent.putExtra("data",responseData);
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
