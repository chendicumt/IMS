package com.cetc28s.ims.worklog.beans;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/4/8.
 * Version 1.0
 */

public class LogRecyInfoBean {

    private String name;
    private String time;
    private String workContent;

    public LogRecyInfoBean(String name, String time, String workContent) {
        this.name = name;
        this.time = time;
        this.workContent = workContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }
}
