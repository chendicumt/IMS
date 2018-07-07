package com.cetc28s.ims.leave.beans;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/4/7.
 * Version 1.0
 */

public class LeaveRecyInfo {

    private String name;
    private String leaveType;
    private String startTime;
    private String endTime;
    private String dayLength;

    public LeaveRecyInfo(String name, String leaveType, String startTime, String endTime, String dayLength) {
        this.name = name;
        this.leaveType = leaveType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayLength = dayLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDayLength() {
        return dayLength;
    }

    public void setDayLength(String dayLength) {
        this.dayLength = dayLength;
    }
}
