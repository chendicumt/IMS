package com.cetc28s.ims.leave.beans;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/4/7.
 * Version 1.0
 */

public class AddLeaveInfoBean {

    private String jobNumber;
    private String reason;
    private String approvalJobNumber;
    private String startTime;
    private String endTime;
    private String fillingTime;
    private int vocationDay;
    private String type;

    public AddLeaveInfoBean(String jobNumber, String reason, String approvalJobNumber, String startTime, String endTime, String fillingTime, int vocationDay, String type) {
        this.jobNumber = jobNumber;
        this.reason = reason;
        this.approvalJobNumber = approvalJobNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.fillingTime = fillingTime;
        this.vocationDay = vocationDay;
        this.type = type;
    }
}
