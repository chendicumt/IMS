package com.cetc28s.ims.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/27.
 * Version 1.0
 */

public class HttpUtils {


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    /**
     * Get的方式发送网络请求
     *
     * @param address
     * @param callback
     */
    public static void sendGetRequest(String address,okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
        Log.d("----->","网络请求");
    }

    /**
     * Post方式发送json
     * @param address
     * @param json
     * @param callback
     */
    public static void sendJsonPostRequest(String address,String json,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        //把请求的内容字符串转换为json
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
        Log.d("----->","网络请求2");
    }

    /**
     * Post方式发送字符串
     * @param address
     * @param string
     * @param callback
     */
    public static void sendPostRequest(String address,String string,okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody=RequestBody.create(MEDIA_TYPE_MARKDOWN,string);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

}
