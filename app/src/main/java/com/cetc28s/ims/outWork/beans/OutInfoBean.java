package com.cetc28s.ims.outWork.beans;

import java.util.List;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/4.
 * Version 1.0
 */

public class OutInfoBean {

    private List<Data> data;
    public static class Data{
        private String id;
        private String jobNumber;
        private String fillingTime;
        private String lastChangeTime;
        private String mission;
        private String destination;
        private String startTime;
        private String planEndTime;
        private String realEndTime;
        private String approvalJobNumber;
        private String name;
        private String approvalName;

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

        public String getFillingTime() {
            return fillingTime;
        }

        public void setFillingTime(String fillingTime) {
            this.fillingTime = fillingTime;
        }

        public String getLastChangeTime() {
            return lastChangeTime;
        }

        public void setLastChangeTime(String lastChangeTime) {
            this.lastChangeTime = lastChangeTime;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getMission() {
            return mission;
        }

        public void setMission(String mission) {
            this.mission = mission;
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

        public String getApprovalJobNumber() {
            return approvalJobNumber;
        }

        public void setApprovalJobNumber(String approvalJobNumber) {
            this.approvalJobNumber = approvalJobNumber;
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
