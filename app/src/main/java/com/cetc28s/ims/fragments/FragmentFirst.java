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
import com.cetc28s.ims.outWork.OutInfoDetail;
import com.cetc28s.ims.outWork.OutRecyclerAdapter;
import com.cetc28s.ims.outWork.beans.OutInfoBean;
import com.cetc28s.ims.outWork.beans.OutRecyInfo;
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

public class FragmentFirst extends Fragment{

    @BindView(R.id.toolBar_first)
    Toolbar mFirToolbar;

    @BindView(R.id.recycler_first)
    PullLoadMoreRecyclerView mRecyclerView;

    @BindView(R.id.tool_bar_image)
    ImageView titleImage;
    @BindView(R.id.tool_bar_title)
    TextView title;

    private View view;
    private OutRecyclerAdapter mOutRecyclerAdapter;
    private List<OutRecyInfo> mOutInfoList = new ArrayList<>();
    private String responseData;
    private OutInfoBean mOutInfo;
    private String url;
    private boolean isSaveFillTime = false;

    public interface FragmentOutCallBack{
        void getOutData(String data);
    }

    private FragmentOutCallBack mOutCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(MyApplication.flag != 1){
            mOutCallBack = (FragmentOutCallBack)getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first,container,false);
        ButterKnife.bind(this,view);

        MyApplication.autoRefresh = 0;
        //添加toolbar
        titleImage.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        mFirToolbar.inflateMenu(R.menu.first_fragment_menu);

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
            mFirToolbar.setTitle("出  差");
        }
        mFirToolbar.setTitleTextColor(Color.BLACK);
        mFirToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.out_add)
                {
                    Intent intent = new Intent(getContext(), OutInfoDetail.class);
                    intent.putExtra("isAdd",true);
                    startActivity(intent);
                }
               /* if(id == R.id.out_statistics){
                    Intent intent = new Intent(getContext(), OutInfoStatistics.class);
                    startActivity(intent);
                }*/
                if (id == R.id.out_search) {
                    //initDialog();
                    new DialogUtil().initDialog("out",ConstUtil.OUT_INFO_SEARCH, R.layout.fragment_first_search_dialog
                            , R.id.fragment_first_search_name, R.id.fragment_first_search_address, R.id.fragment_first_search_startTime
                            , R.id.fragment_first_search_endTime, R.id.fragment_first_search_okPersion, getContext(), new DialogCallBack() {
                                @Override
                                public void getData(String response) {
                                    responseData = response;
                                   if(responseData.equals("")){
                                       getActivity().runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {
                                               Toaster.toast(getContext(),"获取信息失败");
                                           }
                                       });
                                    }else{
                                       mOutInfo = JsonUtil.getGsonInstance().fromJson(responseData,new TypeToken<OutInfoBean>(){}.getType());
                                       getActivity().runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {
                                               if(mOutInfo.getData() != null) {
                                                   setList();
                                                   initRecyclerView();
//                                                   mOutRecyclerAdapter.notifyDataSetChanged();
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
    *  @Description:  初始化数据
    *  @Author:  chendi
    *  @Time:  2018/4/13 11:26
    */
    public void initData(){
        if(MyApplication.flag == 1){
            url = ConstUtil.OUT_INFO_SEARCH+"?name="+MyApplication.appName;
        }else{
            url = ConstUtil.OUT_INFO_SEARCH + "?startTime=" + Common.getStartDate() + " 00:00:00&endTime=" + Common.getEndDate()+" 00:00:00";
            isSaveFillTime = true;
        }
        if( NetInfo.netWorkState(getContext())){
            HttpUtils.sendGetRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toaster.toast(getContext(),"获取信息失败");
                            mRecyclerView.setPullRefreshEnable(false);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    responseData = response.body().string();
                    Logger.d("----------->",responseData);
                    mOutInfo = JsonUtil.getGsonInstance().fromJson(responseData,new TypeToken<OutInfoBean>(){}.getType());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mOutInfo.getData()!= null){
                                setList();
                                initRecyclerView();
                                if(isSaveFillTime){
                                    saveFillTime(getActivity(),mOutInfo,"out");
                                }
                            }else{
                                // Toaster.toast(getContext(),"无相关信息");
                                mRecyclerView.setPullRefreshEnable(false);
                            }
                        }
                    });
                }
            });
        }

    }

    public void saveFillTime(Activity activity, OutInfoBean mOutInfo, String info){
        int size = mOutInfo.getData().size()-1;
        SharedPreferences sharedPreferences = activity.getSharedPreferences("timeInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String fillTime = mOutInfo.getData().get(size).getFillingTime();
        editor.putString(info,fillTime);
        editor.commit();
    }

    /**
    *  @Description:  装配数据
    *  @Author:  chendi
    *  @Time:  2018/3/4 23:14
    */
    public void setList()
    {
        String realDate = "";
        mOutInfoList.clear();
        for (int i = mOutInfo.getData().size()-1 ; i >= 0 ; i--) {
            realDate = mOutInfo.getData().get(i).getRealEndTime().substring(0,10);
            if(mOutInfo.getData().get(i).getRealEndTime().substring(0,10).equals("2018-01-01")||
                    mOutInfo.getData().get(i).getRealEndTime().substring(0,10).equals("1970-01-01")){
                realDate = "";
            }
            OutRecyInfo outRecyInfo = new OutRecyInfo(mOutInfo.getData().get(i).getName(),
                    mOutInfo.getData().get(i).getDestination(),
                    mOutInfo.getData().get(i).getStartTime().substring(0,10),
                    mOutInfo.getData().get(i).getPlanEndTime().substring(0,10),
                    realDate);
            mOutInfoList.add(outRecyInfo);
        }

    }

    /**
     *  @Description:  初始化recyclerview
     *  @Author:  chendi
     *  @Time:  2018/3/4 23:07
     */
    public void initRecyclerView()
    {
        mOutRecyclerAdapter = new OutRecyclerAdapter(mOutInfoList);
        setRecyclerItemViewOnClick();
        mRecyclerView.setAdapter(mOutRecyclerAdapter);
        mRecyclerView.setLinearLayout();
        mRecyclerView.setPullRefreshEnable(true);
        mRecyclerView.setPushRefreshEnable(false);
        //mRecyclerView.setFooterViewText("正在加载...");
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                        /*if(MyApplication.flag == 1){
                            url = ConstUtil.OUT_INFO_SEARCH+"?name="+MyApplication.appName;
                        }else{
                            url = ConstUtil.OUT_INFO_ALL;
                        }*/
                                    Logger.d("----------->order",url);
                                    HttpUtils.sendGetRequest(url, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toaster.toast(getContext(),"获取信息失败");
                                                    mRecyclerView.setPullLoadMoreCompleted();
                                                }
                                            });
                                        }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                responseData = response.body().string();
                                Logger.d("----------->",responseData);
                                mOutInfo = JsonUtil.getGsonInstance().fromJson(responseData,new TypeToken<OutInfoBean>(){}.getType());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(mOutInfo.getData() != null) {
                                            setList();
                                            mOutRecyclerAdapter.notifyDataSetChanged();
                                            if(isSaveFillTime){
                                                saveFillTime(getActivity(),mOutInfo,"out");
                                            }
                                            if(MyApplication.flag != 1){
                                                mOutCallBack.getOutData("true");
                                            }
                                        }else{
//                                            Toaster.toast(getContext(),"无相关信息");
                                        }
                                        mRecyclerView.setPullLoadMoreCompleted();
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
    *  @Description:  设置recyclerview中每一个条目的监听事件，回调
    *  @Author:  chendi
    *  @Time:  2018/3/8 23:28
    */
    public void setRecyclerItemViewOnClick(){
        mOutRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), OutInfoDetail.class);
                intent.putExtra("isAdd",false);
                intent.putExtra("pos",mOutInfo.getData().size()-position-1);
                intent.putExtra("dataLength",mOutInfo.getData().size());
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
