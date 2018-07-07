package com.cetc28s.ims.leave.beans;

import java.util.List;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/16.
 * Version 1.0
 */

public class LeaveInfo {

    private List<Data> data;
    public static class Data{
        private String id;
        private String jobNumber;
        private String reason;
        private String approvalJobNumber;
        private String startTime;
        private String endTime;
        private double vocationDay;
        private String type;
        private String name;
        private String approvalName;
        private String fillingTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJobNumber() {
            return jobNumber;
        }

        public void setJobNumber(String jobNumber) {
            this.jobNumber = jobNumber;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getApprovalJobNumber() {
            return approvalJobNumber;
        }

        public void setApprovalJobNumber(String approvalJobNumber) {
            this.approvalJobNumber = approvalJobNumber;
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

        public double getVocationDay() {
            return vocationDay;
        }

        public void setVocationDay(double vocationDay) {
            this.vocationDay = vocationDay;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getApprovalName() {
            return approvalName;
        }

        public void setApprovalName(String approvalName) {
            this.approvalName = approvalName;
        }

        public String getFillingTime() {
            return fillingTime;
        }

        public void setFillingTime(String fillingTime) {
            this.fillingTime = fillingTime;
        }
    }
    private String message;
    private int status;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
