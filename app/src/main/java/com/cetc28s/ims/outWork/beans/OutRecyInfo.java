package com.cetc28s.ims.outWork.beans;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/28.
 * Version 1.0
 */

public class OutRecyInfo {
    private String name;
    private String address;
    private String startTime;
    private String planEndTime;
    private String realEndTime;

    public OutRecyInfo(String name, String address, String startTime, String planEndTime, String realENdTime){
        this.name = name;
        this.address = address;
        this.startTime = startTime;
        this.planEndTime = planEndTime;
        this.realEndTime = realENdTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(String realEndTime) {
        this.realEndTime = realEndTime;
    }
}
