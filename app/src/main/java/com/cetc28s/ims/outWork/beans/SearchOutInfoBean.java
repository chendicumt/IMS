package com.cetc28s.ims.outWork.beans;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/29.
 * Version 1.0
 */

public class SearchOutInfoBean {

    private String jobNumber;
    private String address;
    private String startTime;
    private String endTime;
    private String task;

    public SearchOutInfoBean( String jobNumber,String address,String startTime,String endTime,String task){
        this.jobNumber = jobNumber;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.task = task;

    }
}
