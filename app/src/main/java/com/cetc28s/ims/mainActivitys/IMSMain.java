package com.cetc28s.ims.mainActivitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.cetc28s.ims.fragments.FragmentAdapter;
import com.cetc28s.ims.fragments.FragmentFirst;
import com.cetc28s.ims.fragments.FragmentSecond;
import com.cetc28s.ims.leave.beans.LeaveInfo;
import com.cetc28s.ims.outWork.beans.OutInfoBean;
import com.cetc28s.ims.services.MyService;
import com.cetc28s.ims.utils.BaseActivity;
import com.cetc28s.ims.utils.Common;
import com.cetc28s.ims.utils.ConstUtil;
import com.cetc28s.ims.utils.HttpUtils;
import com.cetc28s.ims.utils.JsonUtil;
import com.cetc28s.ims.utils.Logger;
import com.cetc28s.ims.utils.StatueBarUtil;
import com.cetc28s.ims.R;
import com.cetc28s.ims.utils.Toaster;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.cetc28s.ims.utils.MyApplication.okPersonInfo;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/2/27.
 * Version 1.0
 */

public class IMSMain extends BaseActivity implements ViewPager.OnPageChangeListener
        ,FragmentFirst.FragmentOutCallBack,FragmentSecond.FragmentLeaveCallBack{

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout_elem)
    TabLayout mTabLayout;
    private TextView tag;
    private int count = 2;
    String[] title=new String[]{"出差","请假","日志","我的"};
    int[] drawable=new int[]{R.drawable.tab1_seleced,R.drawable.tab2, R.drawable.tab3,R.drawable.tab4};
    int[] drawable1=new int[]{R.drawable.tab1,R.drawable.tab2_seleced, R.drawable.tab3,R.drawable.tab4};

    private String outFillTime;
    private String leaveFillTime;
    private OutInfoBean mOutInfo;
    private LeaveInfo mLeaveInfo;
    private int outCount = 0;
    private int leaveCount = 0;
    private MyReceiver1 mMyReceiver1;
    private MyReceiver2 mMyReceiver2;
    private Intent intent;
    private boolean flag = false;
    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);//绑定控件
        StatueBarUtil.StatusBarLightMode(this); //设置状态栏白底黑字

        mMyReceiver1 = new MyReceiver1();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("com.cetc28s.ims.receiver1");
        registerReceiver(mMyReceiver1,filter1);

        mMyReceiver2 = new MyReceiver2();
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("com.cetc28s.ims.receiver2");
        registerReceiver(mMyReceiver2,filter2);



       // getTabTagInfo();
        initFragment();
        getOkPerson();


        intent = new Intent(this, MyService.class);
       /* intent.putExtra("outFillTime",outFillTime);
        intent.putExtra("leaveFillTime",leaveFillTime);*/
        startService(intent);
    }


    /**
    *  @Description:  获取批准人信息
    *  @Author:  chendi
    *  @Time:  2018/5/3 22:41
    */
    public void getOkPerson(){
        okPersonInfo = "";
        HttpUtils.sendGetRequest(ConstUtil.LOGIN_ALL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                okPersonInfo = "";
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                okPersonInfo = responseData;
                Logger.d("--------user", okPersonInfo);
            }
        });
    }

    @Override
    public void getOutData(String data) {
        if(data.equals("true")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = mTabLayout.getTabAt(0).getCustomView();
                    ViewParent parent = view.getParent();
                    ((ViewGroup)parent).removeView(view);
                    mTabLayout.getTabAt(0).setCustomView(getTabView(0));
                    imageView.setImageResource(drawable[0]);
                    textView.setText(title[0]);
                    setTag(tag,false,0);
                }
            });
        }
    }

    @Override
    public void getLeaveData(String data) {
        if(data.equals("true")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = mTabLayout.getTabAt(1).getCustomView();
                    ViewParent parent = view.getParent();
                    ((ViewGroup)parent).removeView(view);
                    mTabLayout.getTabAt(1).setCustomView(getTabView(1));
                    imageView.setImageResource(drawable1[1]);
                    textView.setText(title[1]);
                    setTag(tag,false,0);
                }
            });
        }
    }

    /**
    *  @Description:  初始化Fragment
    *  @Author:  chendi
    *  @Time:  2018/2/27 22:59
    */
    public void initFragment(){

        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
//        左右各缓存3个fragment页面
        mViewPager.setOffscreenPageLimit(3);
//        mTabLayout和viewpager绑定
        mTabLayout.setupWithViewPager(mViewPager);
//        加载tab视图
        for (int i = 0 ; i < 4 ; i++) {
            mTabLayout.getTabAt(i).setCustomView(getTabView(i));
            imageView.setImageResource(drawable[i]);
            textView.setText(title[i]);
        }
//        为tab设置监听事件
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTab(tab,true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTab(tab,false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    /**
    *  @Description:  获取tab视图
    *  @Author:  chendi
    *  @Time:  2018/2/27 23:19
    */
    public View getTabView(final int position)
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_main_tab,null);
        imageView = (ImageView)view.findViewById(R.id.tab_image);
        textView = (TextView)view.findViewById(R.id.tab_text);
        tag = (TextView)view.findViewById(R.id.tab_tag);
        if(0 == position){
            textView.setTextColor(ContextCompat.getColor(this,R.color.tabTextColor));
            if(outCount > 0){
                setTag(tag,true,outCount); 
            }
        }else if(1 == position){
            textView.setTextColor(ContextCompat.getColor(this,R.color.unTabTextColor));
            if(leaveCount > 0){
                setTag(tag,true,leaveCount);
            }
        } else{
            textView.setTextColor(ContextCompat.getColor(this,R.color.unTabTextColor));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(position);
            }
        });
        return view;
    }

    /**
    *  @Description:  设置提醒
    *  @Author:  chendi
    *  @Time:  2018/5/3 23:07
    */
    public void setTag(TextView tag,boolean flag,int count){
        if(flag){
            tag.setVisibility(View.VISIBLE);
            tag.setBackgroundResource(R.drawable.tab_tag);
            tag.setText(" "+count+" ");
            tag.setTextColor(Color.WHITE);
        }else{
            tag.setVisibility(View.GONE);
        }
    }

    /**
    *  @Description:  切换视图
    *  @Author:  chendi
    *  @Time:  2018/2/27 23:24
    */
    public void changeTab(TabLayout.Tab tab,boolean selected)
    {
        View view = tab.getCustomView();
        ImageView imageView = (ImageView)view.findViewById(R.id.tab_image);
        TextView textView = (TextView)view.findViewById(R.id.tab_text);
        tag = (TextView)view.findViewById(R.id.tab_tag);
        if(selected){
            textView.setTextColor(ContextCompat.getColor(this,R.color.tabTextColor));
            if(tab == mTabLayout.getTabAt(0)){
                imageView.setImageResource(R.drawable.tab1_seleced);
                setTag(tag,false,0);
            }
            else if(tab == mTabLayout.getTabAt(1))
            {
                imageView.setImageResource(R.drawable.tab2_seleced);
                setTag(tag,false,0);
            }
            else if(tab == mTabLayout.getTabAt(2))
            {
                imageView.setImageResource(R.drawable.tab3_selected);
            }
            else if(tab == mTabLayout.getTabAt(3))
            {
                imageView.setImageResource(R.drawable.tab4_selected);
            }
           /* else if(tab == mTabLayout.getTabAt(4))
            {
                imageView.setImageResource(R.drawable.tab4_selected);
            }*/
        }
        else{
            textView.setTextColor(ContextCompat.getColor(this,R.color.unTabTextColor));
            if(tab == mTabLayout.getTabAt(0)){
                imageView.setImageResource(R.drawable.tab1);
                setTag(tag,false,0);
            }
            else if(tab == mTabLayout.getTabAt(1))
            {
                imageView.setImageResource(R.drawable.tab2);
                setTag(tag,false,0);
            }
            else if(tab == mTabLayout.getTabAt(2))
            {
                imageView.setImageResource(R.drawable.tab3);
            }
            else if(tab == mTabLayout.getTabAt(3))
            {
                imageView.setImageResource(R.drawable.tab4);
            }
            /*else if(tab == mTabLayout.getTabAt(4))
            {
                imageView.setImageResource(R.drawable.tab4);
            }*/

        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        if(2 == count) {
            Toaster.toast(this,"再按一次退出");
            count--;
        }
        else{
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMyReceiver1);
        unregisterReceiver(mMyReceiver2);
        stopService(intent);
    }

    public class MyReceiver1 extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d("-----------receive1>",intent.getIntExtra("key1",0)+"");
            outCount = intent.getIntExtra("key1",0);
            if(outCount != 0){
                View view = mTabLayout.getTabAt(0).getCustomView();
                if(view != null){
                    ViewParent parent = view.getParent();
                    if(parent != null){
                        ((ViewGroup)parent).removeView(view);
                    }
                }
                Logger.d("------isSelected->",mTabLayout.getTabAt(0).isSelected()+"");
                mTabLayout.getTabAt(0).setCustomView(getTabView(0));
                if( mTabLayout.getTabAt(0).isSelected()){
                    setRefreshTab(drawable[0],title[0],R.color.tabTextColor);
                }else{
                    setRefreshTab(drawable1[0],title[0],R.color.unTabTextColor);
                }
            }
        }
    }

    public class MyReceiver2 extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d("-----------receive2>",intent.getIntExtra("key2",0)+"");
            leaveCount = intent.getIntExtra("key2",0);

            if(leaveCount != 0){
                View view = mTabLayout.getTabAt(1).getCustomView();
                if(view != null){
                    ViewParent parent = view.getParent();
                    if(parent != null){
                        ((ViewGroup)parent).removeView(view);
                    }
                }

                Logger.d("------isSelected->",mTabLayout.getTabAt(1).isSelected()+"");
                mTabLayout.getTabAt(1).setCustomView(getTabView(1));

                if( mTabLayout.getTabAt(1).isSelected()){
                    setRefreshTab(drawable1[1],title[1],R.color.tabTextColor);
                }else{
                    setRefreshTab(drawable[1],title[1],R.color.unTabTextColor);
                }
            }
        }
    }
    /**
    *  @Description:  刷新tab
    *  @Author:  chendi
    *  @Time:  2018/5/5 0:47
    */
    public void setRefreshTab(int drawable,String title,int color){
        imageView.setImageResource(drawable);
        textView.setText(title);
        textView.setTextColor(ContextCompat.getColor(IMSMain.this,color));
    }

}
