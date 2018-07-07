package com.cetc28s.ims.utils;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/4/18.
 * Version 1.0
 */
public class AllUserUtil {

//     public static String data = "";
    private SpinnerListener spinnerListener = null;
    public AllUserUtil(SpinnerListener spinnerListener){
        this.spinnerListener = spinnerListener;
    }
    /**
     *@Description: 初始化spinner
     *@Author: chendi
     *@Time: 2018/4/17 19:03
     */
    public void initSpinner(Spinner spinner, final EditText editText, final List<String> list, Context context){

        /*Spinner spinner = (Spinner)findViewById(res1);
        final EditText editText = (EditText)findViewById(res2);*/
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] ss = parent.getSelectedItem().toString().split(" ");
                editText.setText(ss[0]);
                spinnerListener.getSpinnerData(ss[1]);
                Logger.d("--------user1", ss[1]+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
