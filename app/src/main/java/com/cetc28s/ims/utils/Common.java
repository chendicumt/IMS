package com.cetc28s.ims.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.cetc28s.ims.outWork.beans.OutInfoBean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/4/23.
 * Version 1.0
 */
public class Common {

    /**
    *@Description: 获取小数点后位数
    *@Author: chendi
    *@Time: 2018/4/23 19:09
    */
    public static int getDecimalPoint(double s){
        int count;
        String sss = s+"";
        String[] ss = sss.split("\\.");
        count = ss[1].length();
        return count;
    }

    /**
    *@Description: 获得当前月的起始日期
    *@Author: chendi
    *@Time: 2018/4/23 19:09
    */
    public static String getStartDate(){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String firstDay;
        calendar.add(Calendar.MONTH,0);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        firstDay = format.format(calendar.getTime());
        return firstDay;
    }

    /**
    *@Description: 获得当前月的最后一天
    *@Author: chendi
    *@Time: 2018/4/23 19:25
    */
    public static String getEndDate(){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String endDay;
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        endDay = format.format(calendar.getTime());
        return endDay;
    }

    /**
    *@Description: 获取当前天
    *@Author: chendi
    *@Time: 2018/4/24 20:00
    */
    public static String getToday(){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String today;
        today = format.format(calendar.getTime());
        return today;
    }

    /**
    *  @Description:  获取当前时间
    *  @Author:  chendi
    *  @Time:  2018/5/2 23:09
    */
    public static String getTodayTime(){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String today;
        today = format.format(calendar.getTime());
        return today;
    }

    /**
    *  @Description:  匹配网址
    *  @Author:  chendi
    *  @Time:  2018/5/2 23:10
    */
    public static boolean getPattern(String url){
        Pattern pattern = Pattern.compile("(?<!\\d)(?:(?:[a-zA-Z]*://[^\\s|^\\u4e00-\\u9fa5]*))(?!\\d)");
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    public static void saveFillTime(Activity activity,OutInfoBean mOutInfo,String info){
        int size = mOutInfo.getData().size()-1;
        SharedPreferences sharedPreferences = activity.getSharedPreferences("timeInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String fillTime = mOutInfo.getData().get(size).getFillingTime();
        editor.putString(info,fillTime);
        editor.commit();
    }


    /**
    *  @Description:  将字符串转换为Date
    *  @Author:  chendi
    *  @Time:  2018/5/18 21:41
    */
    public static long getDate(String data){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     *  @Description:  将字符串转换为Calendar
     *  @Author:  chendi
     *  @Time:  2018/5/18 21:41
     */
    public static Calendar getCalendarDate(String data){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca = Calendar.getInstance();
        Date date;
        try {
            date = format.parse(data);
            ca.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ca;
    }
}
