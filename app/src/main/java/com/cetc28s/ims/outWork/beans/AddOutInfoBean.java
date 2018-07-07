package com.cetc28s.ims.outWork.beans;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/28.
 * Version 1.0
 */

public class AddOutInfoBean {
    private String jobNumber;
    private String fillingTime;
    private String lastChangeTime;
    private String mission;
    private String destination;
    private String  startTime;
    private String planEndTime;
    private String realEndTime;
    private String approvalJobNumber;

   public AddOutInfoBean(String jobNumber,String fillingTime, String lastChangeTime,String mission,
            String destination,String startTime,String planEndTime,String realEndTime,String approvalJobNumber){
       this.jobNumber = jobNumber;
       this.fillingTime = fillingTime;
       this.lastChangeTime = lastChangeTime;
       this.mission = mission;
       this.destination = destination;
       this.startTime = startTime;
       this.planEndTime = planEndTime;
       this.realEndTime = realEndTime;
       this.approvalJobNumber = approvalJobNumber;
   }
}
