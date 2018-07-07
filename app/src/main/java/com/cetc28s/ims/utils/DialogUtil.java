package com.cetc28s.ims.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.cetc28s.ims.R;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/16.
 * Version 1.0
 */

public class DialogUtil {


    private String searchData = "";
    private String responseData = "";
    /**
    *  @Description:  搜索框
    *  @Author:  chendi
    *  @Time:  2018/3/16 22:43
    */
    public  String initDialog(final String section, String url, int layoutRes, int res1, int res2, int res3, int res4, int res5, final Context context, final DialogCallBack callBack) {

        final String[] url1 = {url};
        final int[] isStartTime = {1};
        final LayoutInflater[] inflater = {LayoutInflater.from(context)};
        View view = inflater[0].inflate(layoutRes, null);
        final EditText editText1 = (EditText) view.findViewById(res1);
        final EditText editText2 = (EditText) view.findViewById(res2);
        final EditText startTime = (EditText) view.findViewById(res3);
        final EditText endTime = (EditText) view.findViewById(res4);
        final EditText editText5 = (EditText) view.findViewById(res5);
        endTime.setFocusable(false);
        endTime.setEnabled(false);
        final Calendar mCalendar1 = Calendar.getInstance();
        final Calendar mCalendar2 = Calendar.getInstance();
        final AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();
        dialog.setTitle("查    询");
        dialog.setIcon(R.drawable.search);
        if (MyApplication.flag == 1) {
            editText1.setVisibility(View.GONE);
        }
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "查找", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(url1[0]+"?");
                    if(!editText1.getText().toString().equals("")){
                        sb.append("name="+editText1.getText().toString()+"&");
                    }
                    if (section.equals("out") || section.equals("leave")) {
                        if (!editText2.getText().toString().equals("")) {
                            if (section.equals("out")) {
                                sb.append("destination="+editText2.getText().toString()+"&");
                            }else{
                                sb.append("type="+editText2.getText().toString()+"&");
                            }
                        }
                        if (!editText5.getText().toString().equals("")) {
                            sb.append("approvaName="+editText5.getText().toString()+"&");
                        }
                    }

                    if (!startTime.getText().toString().equals("")) {
                        if(section.equals("log")){
                            sb.append("startTime="+startTime.getText().toString()+"&");
                        }else{
                            sb.append("startTime="+startTime.getText().toString()+" 00:00:00"+"&");
                        }
                    }
                    if (!endTime.getText().toString().equals("")) {
                        if(section.equals("log")){
                            sb.append("endTime="+endTime.getText().toString()+"&");
                        }else{
                            sb.append("endTime="+endTime.getText().toString()+" 00:00:00"+"&");
                        }
                    }

                    if(MyApplication.flag == 1){
                        url1[0] = sb.toString()+"name="+MyApplication.appName;
                    }else{
                        url1[0] = sb.toString().substring(0,sb.toString().length()-1);
                        Logger.d("----------->",url1[0]);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpUtils.sendGetRequest(url1[0], new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    callBack.getData(responseData);
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    responseData = response.body().string();
                                    callBack.getData(responseData);
                                }
                            });
                        }
                    }).start();
                }
//            }

        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        mCalendar1.set(i, i1, i2);
                        if(isStartTime[0] == 1){
                            startTime.setText(DateFormat.format("yyy-MM-dd", mCalendar1));
                        }else {
                            if (mCalendar2.getTime().getTime() >= mCalendar1.getTime().getTime()) {
                                startTime.setText(DateFormat.format("yyy-MM-dd", mCalendar1));
                            } else {
                                Toaster.toast(context, "结束时间不能小于开始时间");
                            }

                        }
                        if(!endTime.isEnabled()){
                            endTime.setFocusable(true);
                            endTime.setEnabled(true);
                        }
                    }
                }, mCalendar1.get(Calendar.YEAR), mCalendar1.get(Calendar.MONTH), mCalendar1.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartTime[0] = 2;
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        mCalendar2.set(i, i1, i2);
                        if (mCalendar2.getTime().getTime() >= mCalendar1.getTime().getTime()) {
                            endTime.setText(DateFormat.format("yyy-MM-dd", mCalendar2));
                        } else {
                            Toaster.toast(context, "结束时间不能小于开始时间");
                        }
                    }
                }, mCalendar2.get(Calendar.YEAR), mCalendar2.get(Calendar.MONTH), mCalendar2.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });
        return searchData;
    }
}


