
package com.cetc28s.ims.mainActivitys;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import com.cetc28s.ims.R;
import com.cetc28s.ims.leave.LeaveInfoDetail;
import com.cetc28s.ims.utils.AllUserUtil;
import com.cetc28s.ims.utils.BaseActivity;
import com.cetc28s.ims.utils.ConstUtil;
import com.cetc28s.ims.utils.HttpUtils;
import com.cetc28s.ims.utils.JsonUtil;
import com.cetc28s.ims.utils.Logger;
import com.cetc28s.ims.utils.NetInfo;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.cetc28s.ims.utils.MyApplication.okPersonInfo;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/2/26.
 * Version 1.0
 */

public class IMSSplash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        隐藏状态栏,全屏幕显示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_imssplash);

        NetInfo.netWorkState(this);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IMSSplash.this,IMSLogin.class);
                startActivity(intent);
                IMSSplash.this.finish();
            }
        },4000);
    }



}
