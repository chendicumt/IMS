package com.cetc28s.ims.leave;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.TimePicker;

import com.cetc28s.ims.R;
import com.cetc28s.ims.leave.beans.LeaveInfo;
import com.cetc28s.ims.mainActivitys.UserBean;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * Created by chendi on 2018/3/13.
 * Version 1.0
 */

public class LeaveInfoDetail extends BaseActivity implements SpinnerListener{

    @BindView(R.id.fragment_second_leave_detail_toolBar)
    Toolbar mLeaveToolBar;
    @BindView(R.id.leave_detail_name)
    EditText leaveName;
    @BindView(R.id.leave_detail_type)
    EditText leaveType;
    @BindView(R.id.leave_detail_startDate)
    EditText leaveStartDate;
    @BindView(R.id.leave_detail_endDate)
    EditText leaveEndDate;
    @BindView(R.id.leave_detail_startTime)
    EditText leaveStartTime;
    @BindView(R.id.leave_detail_endTime)
    EditText leaveEndTime;
    @BindView(R.id.leave_detail_timeLength)
    EditText leaveTimeLength;
    @BindView(R.id.leave_detail_okPerson)
    EditText leaveOkPerson;
    @BindView(R.id.leave_detail_okPerson_spinner)
    Spinner leaveOkPersonSpinner;
    @BindView(R.id.leave_detail_reason)
    EditText leaveReason;
    @BindView(R.id.leave_detail_saveBtn)
    Button leaveSaveBtn;
    @BindView(R.id.leave_detail_editBtn)
    Button leaveEditBtn;
    @BindView(R.id.second_btn_linear_layout)
    LinearLayout mLinearLayout;
    private AlertDialog.Builder builder;
    private AlertDialog dialog = null;
    private int leaveTypeIndex=-1;
    private Calendar mCalendar1 = Calendar.getInstance();
    private Calendar mCalendar2 = Calendar.getInstance();
    private Calendar mCalendar3 = Calendar.getInstance();
    private Calendar mCalendar4 = Calendar.getInstance();
    private String[] leaveTypeArr = {"事假","病假","年假","调休","婚假","产假","陪产假"};
    private boolean isStartDate = true;
    private boolean isStartTime = true;
    private boolean isAdd = true;
    private int position;
    private int size;
    private String data;
    private LeaveInfo leaveInfo;
    private int addedit = 0;
    private String JobNum = "";
    private List<String> list = new ArrayList<>();
    private UserBean userBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_second_leave_detail);

        ButterKnife.bind(this);

        StatueBarUtil.StatusBarLightMode(this);

        NetInfo.netWorkState(this);

        setSupportActionBar(mLeaveToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        Intent intent = getIntent();
        isAdd = intent.getBooleanExtra("isAdd",false);
        position = intent.getIntExtra("pos",1);
        size = intent.getIntExtra("dataLength",1);
        data = intent.getStringExtra("data");

        changeAddEditStatue(isAdd);
        if(leaveEndDate.isFocusable() == true) {
            leaveEndDate.setFocusable(false);
            leaveEndDate.setEnabled(false);
            leaveEndTime.setFocusable(false);
            leaveEndTime.setEnabled(false);
        }

        if(isAdd){
            getOkPerson();
        }
    }

    /**
    *  @Description:  选择请假类型
    *  @Author:  chendi
    *  @Time:  2018/3/14 20:06
    */
    public void leaveTypeBtn(View view){
        builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(leaveTypeArr, leaveTypeIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                leaveType.setText(leaveTypeArr[i]);
                leaveTypeIndex = i;
                dialog.dismiss();
            }
        });
        dialog = builder .create();
        dialog.show();

    }

    /**
    *  @Description:  请假开始日期事件
    *  @Author:  chendi
    *  @Time:  2018/3/14 20:14
    */

    public void leaveDetailStartDateBtn(View view){
        mCalendar1 = getDate(mCalendar1,leaveStartDate);
    }

    /**
    *  @Description:  请假结束日期事件
    *  @Author:  chendi
    *  @Time:  2018/3/14 20:15
    */
    public void leaveDetailEndDateBtn(View view){
        isStartDate = false;
        mCalendar2 = getDate(mCalendar2,leaveEndDate);
    }

    /**
    *  @Description:  请假开始时间事件
    *  @Author:  chendi
    *  @Time:  2018/4/13 10:54
    */
    public void leaveDetailStartTimeBtn(View view){
        mCalendar3 = getTime(mCalendar3,leaveStartTime);

    }

    /**
    *  @Description:  请假结束时间事件
    *  @Author:  chendi
    *  @Time:  2018/4/13 10:54
    */
    public void leaveDetailEndTimeBtn(View view){
        isStartTime = false;
        mCalendar4 = getTime(mCalendar4,leaveEndTime);
    }

    /**
    *  @Description:  获取时间
    *  @Author:  chendi
    *  @Time:  2018/4/13 10:52
    */
    public Calendar getTime(final Calendar calendar, final EditText editText){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY,i);
                calendar.set(Calendar.MINUTE,i1);
                if(isStartTime){
                    editText.setText(DateFormat.format("HH:mm",calendar));
                    if(leaveEndTime.isFocusable() == false){
                        leaveEndTime.setFocusable(true);
                        leaveEndTime.setEnabled(true);
                    }
                }else{
                    if(mCalendar2.getTime().getTime() == mCalendar1.getTime().getTime()){
                        if(mCalendar4.getTime().getTime() >= mCalendar3.getTime().getTime()){
                            editText.setText(DateFormat.format("HH:mm",calendar));
                        }else{
                            Toaster.toast(LeaveInfoDetail.this, "结束时间不能小于开始时间");
                        }
                    }else{
                        editText.setText(DateFormat.format("HH:mm",calendar));
                    }
                    String d1 =DateFormat.format("yyy-MM-dd",mCalendar1).toString()+" "+DateFormat.format("HH:mm",mCalendar3).toString()+":00";
                    String d2 =DateFormat.format("yyy-MM-dd",mCalendar2).toString()+" "+DateFormat.format("HH:mm",mCalendar4).toString()+":00";
                    SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = format.parse(d1);
                        date2 = format.parse(d2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long diff = date2.getTime() - date1.getTime();
                   /* long da1 = date2.getTime()/ (24 * 60 * 60 * 1000);
                    long da2 = date1.getTime()/ (24 * 60 * 60 * 1000);*/

                    long day = diff / (24 * 60 * 60 * 1000);
                    long hours = (diff - (day * 24 * 60 * 60 * 1000)) / (60 * 60 * 1000);

                    if ((mCalendar2.getTime().getTime() == mCalendar1.getTime().getTime())
                            && (mCalendar4.getTime().getTime() >= mCalendar3.getTime().getTime())) {
                        leaveTimeLength.setText(day + " 天 " + hours + " 小时");
                    } else if (mCalendar2.getTime().getTime() > mCalendar1.getTime().getTime()) {
                        leaveTimeLength.setText(day + " 天 " + hours + " 小时");
                    }else{

                    }
            }
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();
        return calendar;
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
                if(isStartDate){
                    editText.setText(DateFormat.format("yyy-MM-dd",mCalendar));
                    if(leaveEndDate.isFocusable() == false){
                        leaveEndDate.setFocusable(true);
                        leaveEndDate.setEnabled(true);
                    }
                }else {
                    if (mCalendar2.getTime().getTime() >= mCalendar1.getTime().getTime()) {
                        editText.setText(DateFormat.format("yyy-MM-dd", mCalendar));
                    } else {
                        Toaster.toast(LeaveInfoDetail.this, "结束日期不能小于开始日期");
                    }
                }
            }
        },mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        return mCalendar;
    }

    /**
    *@Description: spinner数据的回掉
    *@Author: chendi
    *@Time: 2018/4/20 11:18
    */
    @Override
    public void getSpinnerData(String spinnerData) {

        Logger.d("--------user", spinnerData);
        JobNum = spinnerData;
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
                Logger.d("--------user", list.size()+"");
                new AllUserUtil(LeaveInfoDetail.this).initSpinner(leaveOkPersonSpinner, leaveOkPerson, list, LeaveInfoDetail.this);
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
    *  @Description:  改变控件状态
    *  @Author:  chendi
    *  @Time:  2018/4/13 10:53
    */
    public void changeAddEditStatue(boolean flag){
        double time;
        String timeLength;
        leaveName.setFocusable(flag);
        leaveName.setFocusableInTouchMode(flag);
        leaveType.setFocusable(flag);
        leaveType.setEnabled(flag);
        leaveStartDate.setFocusable(flag);
        leaveStartDate.setEnabled(flag);
        leaveEndDate.setFocusable(flag);
        leaveEndDate.setEnabled(flag);
        leaveTimeLength.setFocusable(flag);
        leaveTimeLength.setEnabled(flag);
        leaveOkPerson.setFocusable(flag);
        leaveOkPerson.setFocusableInTouchMode(flag);
        leaveOkPersonSpinner.setFocusable(flag);
//        leaveOkPersonSpinner.setFocusableInTouchMode(flag);
        leaveReason.setFocusable(flag);
        leaveReason.setFocusableInTouchMode(flag);
        if(!flag){
            if (MyApplication.flag == 0) {
                mLinearLayout.setVisibility(View.GONE);
            }else{
                leaveSaveBtn.setVisibility(View.GONE);
                leaveEditBtn.setVisibility(View.VISIBLE);
            }
            leaveInfo = JsonUtil.getGsonInstance().fromJson(data,new TypeToken<LeaveInfo>(){}.getType());
            leaveName.setText(leaveInfo.getData().get(position).getName());
            leaveType.setText(leaveInfo.getData().get(position).getType());
            leaveType.setTextColor(Color.BLACK);
            leaveStartDate.setText(leaveInfo.getData().get(position).getStartTime().substring(0,10));
            leaveStartDate.setTextColor(Color.BLACK);
            leaveEndDate.setText(leaveInfo.getData().get(position).getEndTime().substring(0,10));
            leaveEndDate.setTextColor(Color.BLACK);
            leaveStartTime.setText(leaveInfo.getData().get(position).getStartTime().split(" ")[1].substring(0,5));
            leaveStartTime.setTextColor(Color.BLACK);
            leaveEndTime.setText(leaveInfo.getData().get(position).getEndTime().split(" ")[1].substring(0,5));
            leaveEndTime.setTextColor(Color.BLACK);
            time = leaveInfo.getData().get(position).getVocationDay();
            if (Common.getDecimalPoint(time) == 2) {
                timeLength =((int)(time*100))/100+" 天 "
                        +((int)(time*100))%100+" 小时";
            }else{
                timeLength =((int)(time*10))/10+" 天 "
                        +((int)(time*10))%10+" 小时";
            }
            leaveTimeLength.setText(timeLength);
            leaveTimeLength.setTextColor(Color.BLACK);
            leaveOkPerson.setText(leaveInfo.getData().get(position).getApprovalName());
            leaveReason.setText(leaveInfo.getData().get(position).getReason());
        }else{
            mLinearLayout.setVisibility(View.VISIBLE);
            leaveName.setFocusable(false);
            leaveName.setFocusableInTouchMode(false);
            leaveName.setText(MyApplication.appName);
            leaveEditBtn.setVisibility(View.GONE);
            leaveOkPerson.setFocusable(false);
            leaveOkPerson.setFocusableInTouchMode(false);
        }
    }


    /**
    *  @Description:  保存按钮
    *  @Author:  chendi
    *  @Time:  2018/3/14 23:16
    */
    public void leaveSaveData(View view){

        if(leaveName.getText().toString().equals("") || leaveType.getText().toString().equals("")||
                leaveStartDate.getText().toString().equals("") || leaveEndDate.getText().toString().equals("") ||
                /*leaveStartTime.getText().toString().equals("") || leaveEndTime.getText().toString().equals("") ||*/
                leaveOkPerson.getText().toString().equals("")){
            Toaster.toast(this,"内容不能为空");
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody;
                    if(MyApplication.flag == 1 && addedit == 1){
                        requestBody = new FormBody.Builder()
                                .add("id",leaveInfo.getData().get(position).getId())
                                .add("jobNumber",MyApplication.appJobNum)
                                .add("type",leaveType.getText().toString())
                                .add("startTime",leaveStartDate.getText().toString()+" "+ leaveStartTime.getText().toString()+":00")
                                .add("endTime",leaveEndDate.getText().toString()+" "+ leaveEndTime.getText().toString()+":00")
                                .add("fillingTime",Common.getTodayTime())
                                .add("vocationDay",leaveTimeLength.getText().toString().split(" ")[0]+"."+leaveTimeLength.getText().toString().split(" ")[2])
                                .add("approvalJobNumber",getOkPersonNum(leaveOkPerson.getText().toString()))
                                .add("reason",leaveReason.getText().toString())
                                .build();
                    }else{
                        requestBody = new FormBody.Builder()
                                .add("jobNumber",MyApplication.appJobNum)
                                .add("type",leaveType.getText().toString())
                                .add("startTime",leaveStartDate.getText().toString()+" "+ leaveStartTime.getText().toString()+":00")
                                .add("endTime",leaveEndDate.getText().toString()+" "+ leaveEndTime.getText().toString()+":00")
                                .add("fillingTime",Common.getTodayTime())
                                .add("vocationDay",leaveTimeLength.getText().toString().split(" ")[0]+"."+leaveTimeLength.getText().toString().split(" ")[2])
                                .add("approvalJobNumber",JobNum)
                                .add("reason",leaveReason.getText().toString())
                                .build();
                    }
                    Request request = new Request.Builder()
                            .url(ConstUtil.LEAVE_INFO_ADD)
                            .post(requestBody)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        String ss = response.body().string();
                        Logger.d("-------add",ss);
                        if (response.isSuccessful()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toaster.toast(LeaveInfoDetail.this,"保存成功");
                                    MyApplication.autoRefresh = 1;
                                    LeaveInfoDetail.this.finish();
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
    *  @Time:  2018/4/13 15:20
    */
    public void leaveEditData(View view){
        leaveEditBtn.setVisibility(View.GONE);
        leaveSaveBtn.setVisibility(View.VISIBLE);
        leaveReason.setFocusable(true);
        leaveReason.setFocusableInTouchMode(true);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addedit = 0;
    }
}
