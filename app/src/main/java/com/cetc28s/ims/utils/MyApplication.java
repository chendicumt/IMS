package com.cetc28s.ims.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by PC on 2017/10/13.
 */

public class MyApplication extends Application {

    private static Context context;
    public static String appName;
    public static String appJobNum;
    public static String okPersonInfo;
    public static String commonUrl;
    /**
     * 是否是我的标志
     */
    public static int flag ;
    /**
     * 是否自动刷新标志
     */
    public static int autoRefresh;

    @Override
    public void onCreate() {
        context=getApplicationContext();
    }
    public static Context getContext()
    {
        return context;
    }
}
