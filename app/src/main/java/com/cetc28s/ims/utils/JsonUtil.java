package com.cetc28s.ims.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/27.
 * Version 1.0
 */

public class JsonUtil<T> {

    private static Gson gson;
    public static Gson getGsonInstance(){
        if(gson == null){
            gson = new Gson();
        }
        return gson;
    }
    /**
    *  @Description:  生成添加出差信息的json
    *  @Author:  chendi
    *  @Time:  2018/3/28 23:54
    */
    public String createJson(T t){
        Gson gson=new GsonBuilder().serializeNulls().create();
        return gson.toJson(t);
    }
}
