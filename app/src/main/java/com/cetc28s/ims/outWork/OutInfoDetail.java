package com.cetc28s.ims.outWork;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.cetc28s.ims.R;
import com.cetc28s.ims.leave.LeaveInfoDetail;
import com.cetc28s.ims.mainActivitys.UserBean;
import com.cetc28s.ims.outWork.beans.OutInfoBean;
import com.cetc28s.ims.utils.AllUserUtil;
import com.cetc28s.ims.utils.BaseActivity;
import com.cetc28s.ims.utils.Common;
import com.cetc28s.ims.utils.ConstUtil;
import com.cetc28s.ims.utils.HttpUtils;
import com.cetc28s.ims.utils.JsonUtil;
import com.cetc28s.ims.utils.Logger;
import com.cetc28s.ims.utils.MyApplication;
import com.cetc28s.ims.utils.NetInfo;
import com.cetc28s.ims.utils.SpinnerListener;
import com.cetc28s.ims.utils.StatueBarUtil;
import com.cetc28s.ims.utils.Toaster;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/5.
 * Version 1.0
 */

public class OutInfoDetail extends BaseActivity implements SpinnerListener{

    @BindView(R.id.out_detail_name)
    EditText name;
    @BindView(R.id.out_detail_address)
    EditText address;
    @BindView(R.id.fragment_first_out_detail_toolBar)
    Toolbar mToolbar;
    @BindView(R.id.out_detail_startTime)
    EditText startTime;
    @BindView(R.id.out_plan_endTime)
    EditText planEndTime;
    @BindView(R.id.out_real_endTime)
    EditText realEndTime;
    @BindView(R.id.out_detail_okPerson)
    EditText okPerson;
    @BindView(R.id.out_detail_okPerson_spinner)
    Spinner spinner;
    @BindView(R.id.out_detail_task)
    EditText task;
    @BindView(R.id.out_detail_editBtn)
    Button edit;
    @BindView(R.id.out_detail_saveBtn)
    Button save;
    @BindView(R.id.first_btn_linear_layout)
    LinearLayout mLinearLayout;
    private Calendar mCalendar1 = Calendar.getInstance();
    private Calendar mCalendar2 = Calendar.getInstance();
    private Calendar mCalendar3 = Calendar.getInstance();
    private boolean isAdd = true;
    private int isStartTime = 1;
    private int position;
    private int size;
    private String data;
    private OutInfoBean outInfoBean;
    private int addedit = 0;
    private String JobNum = "";
    private List<String> list = new ArrayList<>();
    private UserBean userBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_first_outinfo_detail);

        ButterKnife.bind(this);

        StatueBarUtil.StatusBarLightMode(this); //设置状态栏白底黑字

        NetInfo.netWorkState(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        Intent intent = this.getIntent();
        isAdd = intent.getBooleanExtra("isAdd",false);

        position = intent.getIntExtra("pos",1);
        size = intent.getIntExtra("dataLength",1);
        data = intent.getStringExtra("data");

        changeAddEditStatue(isAdd);

        if (planEndTime.isEnabled() == true && realEndTime.isEnabled() == true) {
            planEndTime.setFocusable(false);
            planEndTime.setEnabled(false);
            realEndTime.setFocusable(false);
            realEndTime.setEnabled(false);
        }

        if(isAdd){
            getOkPerson();
        }
    }


    /**
     *@Description: 获取所有人员信息
     *@Author: chendi
     *@Time: 2018/4/17 19:04
     */
    public void getOkPerson(){
        list.clear();
        if(!MyApplication.okPersonInfo.equals("")){
            userBean = JsonUtil.getGsonInstance().fromJson(MyApplication.okPersonInfo, new TypeToken<UserBean>() {}.getType());
            if(userBean.getData() != null){
                for (int i = 0; i < userBean.getData().size(); i++) {
                    list.add(userBean.getData().get(i).getName()+" "+userBean.getData().get(i).getJobNumber());
                }
                new AllUserUtil(this).initSpinner(spinner, okPerson, list, this);
            }
        }
    }

    /**
    *  @Description:  获取批准人工号
    *  @Author:  chendi
    *  @Time:  2018/5/18 0:11
    */
    public String getOkPersonNum(String okPerson){
        Map<String,String> map = new HashMap<>();
        if(!MyApplication.okPersonInfo.equals("")){
            userBean = JsonUtil.getGsonInstance().fromJson(MyApplication.okPersonInfo, new TypeToken<UserBean>() {}.getType());
            if(userBean.getData() != null){
                for (int i = 0; i < userBean.getData().size(); i++) {
                    map.put(userBean.getData().get(i).getName(),userBean.getData().get(i).getJobNumber());
                }
            }
        }
        return map.get(okPerson);
    }

    /**
    *@Description:spinner的回调
    *@Author: chendi
    *@Time: 2018/4/21 9:31
    */
    @Override
    public void getSpinnerData(String spinnerData) {
        JobNum = spinnerData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(android.R.id.home == id){
            this.finish();
        }
        return true;
    }

    /**
    *  @Description:  开始日期事件
    *  @Author:  chendi
    *  @Time:  2018/3/7 19:23
    */
    public void outDetaiStartTimeBtn(View view) {
        mCalendar1 = getDate(mCalendar1,startTime);
    }

    /**
     *  @Description:  计划结束日期事件
     *  @Author:  chendi
     *  @Time:  2018/3/7 19:24
     */
    public void outDetailPlanEndTimeBtn(View view){
        isStartTime = 2;
        mCalendar2 = getDate(mCalendar2,planEndTime);
    }

    /**
    *  @Description:  实际结束日期事件
    *  @Author:  chendi
    *  @Time:  2018/3/7 19:23
    */
    public void outDetailRealEndTimeBtn(View view){
        isStartTime = 3;
        mCalendar3 = getDate(mCalendar3,realEndTime);
    }



    /**
     *  @Description:  获取日期
     *  @Author:  chendi
     *  @Time:  2018/3/7 19:24
     */

    public Calendar getDate(final Calendar mCalendar, final EditText editText)
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mCalendar.set(i,i1,i2);
                if(isStartTime == 1){
                    editText.setText(DateFormat.format("yyy-MM-dd",mCalendar));
                    if (planEndTime.isEnabled() == false && realEndTime.isEnabled() == false) {
                        planEndTime.setFocusable(true);
                        planEndTime.setEnabled(true);
                    }
                }else if(isStartTime == 2){
                    if (mCalendar2.getTime().getTime() >= mCalendar1.getTime().getTime()) {
                        editText.setText(DateFormat.format("yyy-MM-dd", mCalendar));
                    } else {
                        Toaster.toast(OutInfoDetail.this, "结束时间不能小于开始时间");
                    }
                }else {
                    if (mCalendar3.getTime().getTime() >= mCalendar1.getTime().getTime()) {
                        editText.setText(DateFormat.format("yyy-MM-dd", mCalendar));
                    } else {
                        Toaster.toast(OutInfoDetail.this, "结束时间不能小于开始时间");
                    }
                }
            }
        },mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        return mCalendar;
    }

    /**
    *  @Description:  保存修改是的控件状态
    *  @Author:  chendi
    *  @Time:  2018/3/10 16:15
    */
    public void changeAddEditStatue(boolean flag){
            name.setFocusable(flag);
            name.setFocusableInTouchMode(flag);
            address.setFocusable(flag);
            address.setFocusableInTouchMode(flag);
            startTime.setFocusable(flag);
            startTime.setEnabled(flag);
            planEndTime.setFocusable(flag);
            planEndTime.setEnabled(flag);
            realEndTime.setFocusable(flag);
            realEndTime.setEnabled(flag);
            okPerson.setFocusable(flag);
            okPerson.setFocusableInTouchMode(flag);
            spinner.setFocusable(flag);
//            spinner.setFocusableInTouchMode(flag);
            task.setFocusable(flag);
            task.setFocusableInTouchMode(flag);
        if(!flag){
            outInfoBean = JsonUtil.getGsonInstance().fromJson(data,new TypeToken<OutInfoBean>(){}.getType());
            if (MyApplication.flag == 0) {
                mLinearLayout.setVisibility(View.GONE);
            }else{
                save.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                mCalendar1 = Common.getCalendarDate(outInfoBean.getData().get(position).getStartTime());
            }

            name.setText(outInfoBean.getData().get(position).getName());
            address.setText(outInfoBean.getData().get(position).getDestination());
            startTime.setText(outInfoBean.getData().get(position).getStartTime().substring(0,10));
            startTime.setTextColor(Color.BLACK);
            planEndTime.setText(outInfoBean.getData().get(position).getPlanEndTime().substring(0,10));
            planEndTime.setTextColor(Color.BLACK);
            if(outInfoBean.getData().get(position).getRealEndTime().substring(0,10).equals("2018-01-01") ||
                    outInfoBean.getData().get(position).getRealEndTime().substring(0,10).equals("1970-01-01")){
                realEndTime.setText("");
            }else{
                realEndTime.setText(outInfoBean.getData().get(position).getRealEndTime().substring(0,10));
            }
            realEndTime.setTextColor(Color.BLACK);
            okPerson.setText(outInfoBean.getData().get(position).getApprovalName());
            task.setText(outInfoBean.getData().get(position).getMission());
        }else{
            mLinearLayout.setVisibility(View.VISIBLE);
//            name.setFocusable(false);
            name.setFocusableInTouchMode(false);
            name.setText(MyApplication.appName);
            edit.setVisibility(View.GONE);
            okPerson.setFocusable(false);
//            okPerson.setFocusableInTouchMode(false);
        }
    }

    /**
    *  @Description:  保存数据事件
    *  @Author:  chendi
    *  @Time:  2018/3/12 22:34
    */

    public void saveData(View view){
        //do something
        if(name.getText().toString().equals("") || task.getText().toString().equals("")||
        address.getText().toString().equals("") || startTime.getText().toString().equals("") ||
                planEndTime.getText().toString().equals("") || okPerson.getText().toString().equals("")){
            Toaster.toast(this,"内容不能为空");
        }
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formBody;
                    String realTime = "";
                    if(MyApplication.flag == 1 && addedit == 1){
                        if (!realEndTime.getText().toString().equals("")) {
                            realTime = realEndTime.getText().toString()+" "+"00:00:00";
                        }else{
                            realTime = realEndTime.getText().toString();
                        }

                        formBody = new FormBody.Builder()
                                .add("id",outInfoBean.getData().get(position).getId())
                                .add("jobNumber", MyApplication.appJobNum)
                                .add("approvalJobNumber",getOkPersonNum(okPerson.getText().toString()))
                                .add("destination", address.getText().toString())
                                .add("fillingTime", Common.getTodayTime())
                                .add("lastChangeTime","2018-01-01 00:00:00")
                                .add("mission",task.getText().toString())
                                .add("planEndTime",planEndTime.getText().toString()+" "+"00:00:00")
                                .add("realEndTime",realTime)
                                .add("startTime",startTime.getText().toString()+" "+"00:00:00")
                                .build();
                    }else{
                        formBody = new FormBody.Builder()
                                .add("jobNumber", MyApplication.appJobNum)
                                .add("approvalJobNumber",JobNum)
                                .add("destination", address.getText().toString())
                                .add("fillingTime",Common.getTodayTime())
                                .add("lastChangeTime","2018-01-01 00:00:00")
                                .add("mission",task.getText().toString())
                                .add("planEndTime",planEndTime.getText().toString()+" "+"00:00:00")
                                .add("realEndTime",realEndTime.getText().toString())
                                .add("startTime",startTime.getText().toString()+" "+"00:00:00")
                                .build();
                    }
                    Request request = new Request.Builder()
                            .url(ConstUtil.OUT_INFO_ADD)
                            .post(formBody)
                            .build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        String ss = response.body().string();
                        Logger.d("-------add",ss);
                        if (response.isSuccessful()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toaster.toast(OutInfoDetail.this,"保存成功");
                                    MyApplication.autoRefresh = 1;
                                    OutInfoDetail.this.finish();
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
    *  @Description:  修改数据事件
    *  @Author:  chendi
    *  @Time:  2018/3/12 22:34
    */
    public void editData(View view){
        edit.setVisibility(View.GONE);
        save.setVisibility(View.VISIBLE);
//        realEndTime.setFocusable(true);
        realEndTime.setEnabled(true);
//        task.setFocusable(true);
        task.setFocusableInTouchMode(true);
        addedit = 1;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addedit = 0;
    }
}
