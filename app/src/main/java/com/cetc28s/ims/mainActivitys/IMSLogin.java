package com.cetc28s.ims.mainActivitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cetc28s.ims.R;
import com.cetc28s.ims.fragments.FragmentFirst;
import com.cetc28s.ims.utils.BaseActivity;
import com.cetc28s.ims.utils.Common;
import com.cetc28s.ims.utils.ConstUtil;
import com.cetc28s.ims.utils.HttpUtils;
import com.cetc28s.ims.utils.JsonUtil;
import com.cetc28s.ims.utils.Logger;
import com.cetc28s.ims.utils.MyApplication;
import com.cetc28s.ims.utils.NetInfo;
import com.cetc28s.ims.utils.Toaster;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/2/26.
 * Version 1.0
 */

public class IMSLogin extends BaseActivity {
    @BindView(R.id.edit_text_name)
    EditText name;
    @BindView(R.id.edit_text_pwd)
    EditText password;
    @BindView(R.id.edit_url_linear)
    LinearLayout editLinear;
    @BindView(R.id.edit_url_btn_linear)
    LinearLayout editBtnLinear;
    @BindView(R.id.edit_url)
    EditText url;
    @BindView(R.id.remember_name_psd)
    CheckBox isRememberNamePsd;
    private String responseData;
    private LoginBean loginBean;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    public static final String filePath = "/data/data/com.cetc28s.ims/shared_prefs/userInfo.xml";
    public static final String loginFilePath = "/data/data/com.cetc28s.ims/shared_prefs/loginInfo.xml";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏,全屏幕显示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_imslogin);
        ButterKnife.bind(this);
        NetInfo.netWorkState(this);
        editLinear.setVisibility(View.GONE);
        editBtnLinear.setVisibility(View.GONE);
        getUrl();
        getLoginInfo(name,password,isRememberNamePsd);
    }

    /**
    *  @Description:  登陆事件
    *  @Author:  chendi
    *  @Time:  2018/2/27 21:46
    */
    public void btLogin(View view){
        final String userName = name.getText().toString();
        final String userPsd = password.getText().toString();
        Pattern pattern = Pattern.compile("(?<!\\d)(?:(?:[a-zA-Z]*://[^\\s|^\\u4e00-\\u9fa5]*))(?!\\d)");
        String urlPattern = MyApplication.commonUrl.substring(0,MyApplication.commonUrl.length()-1);
        Logger.d("-------------->",urlPattern);
        Matcher matcher = pattern.matcher(urlPattern);
        boolean isPattern = matcher.find();
        Logger.d("-------------->",isPattern+"");
        if (isPattern) {
            String url = ConstUtil.LOGIN_URL + "?jobNumber="+userName+"&password="+userPsd;
            if(userName.equals("") || userPsd.equals(""))
            {
                Toaster.toast(this,"用户名和密码不能为空");
            }else{
                HttpUtils.sendGetRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toaster.toast(IMSLogin.this,"登录失败");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        responseData = response.body().string();
                        Logger.d("--------login",responseData);
                        //loginBean = new JsonUtil<LoginBean>().parseJson(responseData);
                        loginBean = JsonUtil.getGsonInstance().fromJson(responseData,new TypeToken<LoginBean>(){}.getType());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (loginBean.getStatus() == 0) {
                                    rememberNamePsd(userName,userPsd);
                                    MyApplication.appName = loginBean.getData().getName();
                                    MyApplication.appJobNum = loginBean.getData().getJobNumber();
                                    Toaster.toast(IMSLogin.this, "登陆成功");
                                    Intent intent = new Intent(IMSLogin.this, IMSMain.class);
                                    startActivity(intent);
                                    IMSLogin.this.finish();
                                }else{
                                    Toaster.toast(IMSLogin.this,"用户名或密码错误");
                                }
                            }
                        });
                    }
                });
            }
        }else{
            Toaster.toast(this,"请配置地址");
        }

        /*Intent intent = new Intent(IMSLogin.this, IMSMain.class);
        MyApplication.appName = "chendi";
        MyApplication.appJobNum = "015831";
        startActivity(intent);
        IMSLogin.this.finish();*/
    }

    /**
    *  @Description:  保存用户名、密码和勾选信息
    *  @Author:  chendi
    *  @Time:  2018/5/1 19:20
    */
    public void rememberNamePsd(String name , String psd){
        shared = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        editor = shared.edit();
        if(isRememberNamePsd.isChecked()){
            editor.putString("name",name);
            editor.putString("psd",psd);
            editor.putBoolean("flag",true);
            editor.commit();
        }else{
            editor.putString("name","");
            editor.putString("psd","");
            editor.putBoolean("flag",false);
            editor.commit();
        }
    }

    /**
    *  @Description:  获取用户名和密码信息
    *  @Author:  chendi
    *  @Time:  2018/5/1 19:17
    */
    public void getLoginInfo(EditText editText1,EditText editText2,CheckBox checkBox){
        File file = new File(loginFilePath);
        if (file.exists()) {
            shared = getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
            editText1.setText(shared.getString("name",""));
            editText2.setText(shared.getString("psd",""));
            checkBox.setChecked(shared.getBoolean("flag",false));
        }
    }
    /**
    *  @Description:  点击显示编辑框
    *  @Author:  chendi
    *  @Time:  2018/4/27 19:58
    */
    public void editUrl(View view){
        editLinear.setVisibility(View.VISIBLE);
        editBtnLinear.setVisibility(View.VISIBLE);
    }

    /**
    *  @Description:  取消事件
    *  @Author:  chendi
    *  @Time:  2018/4/27 20:00
    */
    public void editUrlCancel(View view){
        editLinear.setVisibility(View.GONE);
        editBtnLinear.setVisibility(View.GONE);
    }

    /**
    *  @Description:  保存事件
    *  @Author:  chendi
    *  @Time:  2018/4/27 20:02
    */
    public void editUrlSave(View view){
        boolean isPattern = Common.getPattern(url.getText().toString());
        Logger.d("------------->",isPattern+"");
        if (isPattern) {
            shared = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            editor = shared.edit();
            editor.putString("url",url.getText().toString());
            editor.commit();
            MyApplication.commonUrl = url.getText().toString()+"/";
            editLinear.setVisibility(View.GONE);
            editBtnLinear.setVisibility(View.GONE);
        }else{
            Toaster.toast(this,"url格式错误");
        }

    }

    /**
    *  @Description:  获取url
    *  @Author:  chendi
    *  @Time:  2018/4/27 20:04
    */
    public void getUrl(){
        MyApplication.commonUrl = "heihei";
        File file = new File(filePath);
        if (file.exists()) {
            shared = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
            MyApplication.commonUrl = shared.getString("url",null)+"/";
        }else{
            Toaster.toast(this,"请配置地址");
        }
    }
}
