package com.cetc28s.ims.worklog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cetc28s.ims.R;
import com.cetc28s.ims.utils.BaseActivity;
import com.cetc28s.ims.utils.Common;
import com.cetc28s.ims.utils.ConstUtil;
import com.cetc28s.ims.utils.JsonUtil;
import com.cetc28s.ims.utils.Logger;
import com.cetc28s.ims.utils.MyApplication;
import com.cetc28s.ims.utils.NetInfo;
import com.cetc28s.ims.utils.StatueBarUtil;
import com.cetc28s.ims.utils.Toaster;
import com.cetc28s.ims.worklog.beans.LogInfo;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/18.
 * Version 1.0
 */

public class WorkLogDetail extends BaseActivity {

    @BindView(R.id.fragment_third_log_detail_toolBar)
    Toolbar mWorkLogToolBar;
    @BindView(R.id.log_detail_name)
    EditText logName;
    @BindView(R.id.log_detail_date)
    EditText logDate;
    @BindView(R.id.log_detail_todayComplWork)
    EditText logComlWork;
    @BindView(R.id.log_detail_todayPlanWork)
    EditText logPlanWork;
    @BindView(R.id.log_detail_changeWork)
    EditText logChangeWork;
    @BindView(R.id.log_detail_info)
    EditText logDetailInfo;
    @BindView(R.id.log_detail_saveBtn)
    Button logSaveBtn;
    @BindView(R.id.log_detail_editBtn)
    Button logEditBtn;
    @BindView(R.id.third_btn_linear_layout)
    LinearLayout mLinearLayout;
    private boolean isAdd = true;
    private int position;
    private int addedit = 0;
    private String data;
    private LogInfo logInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_third_worklog_detail);

        ButterKnife.bind(this);

        StatueBarUtil.StatusBarLightMode(this);

        NetInfo.netWorkState(this);

        setSupportActionBar(mWorkLogToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        Intent intent = getIntent();
        isAdd = intent.getBooleanExtra("isAdd",false);
        position = intent.getIntExtra("pos",1);
        //size = intent.getIntExtra("dataLength",1);
        data = intent.getStringExtra("data");

        changeAddEditStatue(isAdd);
        if (isAdd) {
            Date date =new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String today = dateFormat.format(date);
            logDate.setText(today);
        }

    }

    /**
    *  @Description:  改变控件状态
    *  @Author:  chendi
    *  @Time:  2018/4/13 14:33
    */
    public void changeAddEditStatue(boolean flag){
        logName.setFocusable(flag);
        logName.setFocusableInTouchMode(flag);
        logDate.setFocusable(flag);
        logDate.setEnabled(flag);
        logDate.setTextColor(Color.BLACK);
        logComlWork.setFocusable(flag);
        logComlWork.setFocusableInTouchMode(flag);
        logPlanWork.setFocusable(flag);
        logPlanWork.setFocusableInTouchMode(flag);
        logChangeWork.setFocusable(flag);
        logChangeWork.setFocusableInTouchMode(flag);
        logChangeWork.setTextColor(Color.BLACK);
        logDetailInfo.setFocusable(flag);
        logDetailInfo.setFocusableInTouchMode(flag);
        if(!flag){
            if (MyApplication.flag == 0) {
                mLinearLayout.setVisibility(View.GONE);
            }else{
                logSaveBtn.setVisibility(View.GONE);
                logEditBtn.setVisibility(View.VISIBLE);
            }
            logInfo = JsonUtil.getGsonInstance().fromJson(data,new TypeToken<LogInfo>(){}.getType());
            logName.setText(logInfo.getData().get(position).getName());
            logDate.setText(logInfo.getData().get(position).getStartTime().substring(0,10));
            logComlWork.setText(logInfo.getData().get(position).getWorkContent());
            logPlanWork.setText(logInfo.getData().get(position).getWorkPlan());
            logChangeWork.setText(logInfo.getData().get(position).getCoordinate());
            logDetailInfo.setText(logInfo.getData().get(position).getRemark());
        }else{
            mLinearLayout.setVisibility(View.VISIBLE);
            logName.setFocusable(false);
            logName.setFocusableInTouchMode(false);
            logName.setText(MyApplication.appName);
            logEditBtn.setVisibility(View.GONE);
            logDate.setFocusable(false);
            logDate.setEnabled(false);
        }
    }

    /**
    *  @Description:  保存事件
    *  @Author:  chendi
    *  @Time:  2018/4/11 19:24
    */
    public void logSaveData(View view){
        if(logName.getText().toString().equals("") || logComlWork.getText().toString().equals("")||
                logPlanWork.getText().toString().equals("") || logChangeWork.getText().toString().equals("") ||
                logDetailInfo.getText().toString().equals("")){
            Toaster.toast(this,"内容不能为空");
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestbody;
                    if (MyApplication.flag == 1 && addedit == 1) {
                        requestbody = new FormBody.Builder()
                                .add("id",logInfo.getData().get(position).getId())
                                .add("jobNumber",MyApplication.appJobNum)
                                .add("fillingTime", Common.getTodayTime())
                                .add("startTime",logDate.getText().toString())
                                .add("endTime",logDate.getText().toString())
                                .add("workContent",logComlWork.getText().toString())
                                .add("workPlan",logPlanWork.getText().toString())
                                .add("remark",logDetailInfo.getText().toString())
                                .add("coordinate",logChangeWork.getText().toString())
                                .build();
                    }else{
                        requestbody = new FormBody.Builder()
                                .add("jobNumber",MyApplication.appJobNum)
                                .add("fillingTime",Common.getTodayTime())
                                .add("startTime",logDate.getText().toString())
                                .add("endTime",logDate.getText().toString())
                                .add("workContent",logComlWork.getText().toString())
                                .add("workPlan",logPlanWork.getText().toString())
                                .add("remark",logDetailInfo.getText().toString())
                                .add("coordinate",logChangeWork.getText().toString())
                                .build();
                    }
                    Request request = new Request.Builder()
                            .url(ConstUtil.LOG_INFO_ADD)
                            .post(requestbody)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        String ss = response.body().string();
                        Logger.d("-------add",ss);
                        if (response.isSuccessful()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toaster.toast(WorkLogDetail.this,"保存成功");
                                    MyApplication.autoRefresh = 1;
                                    WorkLogDetail.this.finish();
                                }
                            });

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    /**
    *  @Description:  修改事件
    *  @Author:  chendi
    *  @Time:  2018/4/13 16:44
    */
    public void logEditBtn(View view){
        logEditBtn.setVisibility(View.GONE);
        logSaveBtn.setVisibility(View.VISIBLE);
        logComlWork.setFocusable(true);
        logComlWork.setFocusableInTouchMode(true);
        logPlanWork.setFocusable(true);
        logPlanWork.setFocusableInTouchMode(true);
        logDetailInfo.setFocusable(true);
        logDetailInfo.setFocusableInTouchMode(true);
        logChangeWork.setFocusable(true);
        logChangeWork.setFocusableInTouchMode(true);
        addedit = 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        addedit = 0;
    }
}
