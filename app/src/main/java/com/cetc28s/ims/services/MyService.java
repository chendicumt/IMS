package com.cetc28s.ims.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.cetc28s.ims.leave.beans.LeaveInfo;
import com.cetc28s.ims.outWork.beans.OutInfoBean;
import com.cetc28s.ims.utils.Common;
import com.cetc28s.ims.utils.ConstUtil;
import com.cetc28s.ims.utils.HttpUtils;
import com.cetc28s.ims.utils.JsonUtil;
import com.cetc28s.ims.utils.Logger;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyService extends Service {

    public static  Intent intent1 = new Intent("com.cetc28s.ims.receiver1");
    public static  Intent intent2 = new Intent("com.cetc28s.ims.receiver2");
    public static final String OUTURL = ConstUtil.OUT_INFO_SEARCH + "?startTime=" + Common.getStartDate() + " 00:00:00&endTime=" + Common.getEndDate()+" 00:00:00";
    public static final String LEAVEURL = ConstUtil.LEAVE_INFO_SEARCH + "?startTime=" + Common.getStartDate() + " 00:00:00&endTime=" + Common.getEndDate()+" 00:00:00";
    private String responseData1 = "";
    private String responseData2 = "";
    private OutInfoBean mOutInfo;
    private LeaveInfo mLeaveInfo;
    private String outFillTime;
    private String leaveFillTime;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("------------>","create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*outFillTime = intent.getStringExtra("outFillTime");
        leaveFillTime = intent.getStringExtra("leaveFillTime");*/
        getInfo();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getInfo(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getTabTagInfo();
                Logger.d("--------------->intent",outFillTime+leaveFillTime);
                final int[] outCount = {0};
                final int[] leaveCount = {0};
                HttpUtils.sendGetRequest(OUTURL, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        responseData1 = response.body().string();
                        Logger.d("-------------->out",responseData1);
                        mOutInfo = JsonUtil.getGsonInstance().fromJson(responseData1,new TypeToken<OutInfoBean>(){}.getType());
                        int size;
                        if(mOutInfo.getData() != null){
                            size = mOutInfo.getData().size()-1;
                            for(int i = size ; i >= 0 ; i--){
                                String s = mOutInfo.getData().get(i).getFillingTime();
                                if(Common.getDate(s) > Common.getDate(outFillTime)){
                                    outCount[0]++;
                                }else{
                                    outFillTime = mOutInfo.getData().get(size).getFillingTime();
                                    saveFillTime(outFillTime,"out");
                                    intent1.putExtra("key1", outCount[0]);
                                    sendBroadcast(intent1);
                                    break;
                                }
                            }
                        }
                    }
                });

                HttpUtils.sendGetRequest(LEAVEURL, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        responseData2 = response.body().string();
                        Logger.d("-------------->leave",responseData2);
                        mLeaveInfo = JsonUtil.getGsonInstance().fromJson(responseData2,new TypeToken<LeaveInfo>(){}.getType());
                        int size;
                        if (mLeaveInfo.getData() != null){
                            size = mLeaveInfo.getData().size()-1;
                            for(int i = size ; i >= 0 ; i--){
                                String s = mLeaveInfo.getData().get(i).getFillingTime();
                                if(Common.getDate(s) > Common.getDate(leaveFillTime)){
                                    leaveCount[0]++;
                                }else{
                                    leaveFillTime = mLeaveInfo.getData().get(size).getFillingTime();
                                    saveFillTime(leaveFillTime,"leave");
                                    intent2.putExtra("key2", leaveCount[0]);
                                    sendBroadcast(intent2);
                                    break;
                                    }
                                }
                            }
                        }
                });
            }
        },0,20000);
    }


    /**
     *  @Description:  获取最近一次填写时间
     *  @Author:  chendi
     *  @Time:  2018/5/3 23:07
     */
    public void getTabTagInfo(){
        outFillTime = Common.getTodayTime();
        leaveFillTime = Common.getTodayTime();
        File file = new File(ConstUtil.tabFilePath);
        if (file.exists()) {
            SharedPreferences shared = getSharedPreferences("timeInfo", Context.MODE_PRIVATE);
            Logger.d("------------outtab", shared.getString("out",""));
            Logger.d("------------leavetab", shared.getString("leave",""));
            outFillTime = shared.getString("out","");
            leaveFillTime = shared.getString("leave","");
        }
    }

    public void saveFillTime(String fillTime, String info){
        SharedPreferences sharedPreferences = getSharedPreferences("timeInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(info,fillTime);
        editor.commit();
    }


}
