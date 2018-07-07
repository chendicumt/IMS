package com.cetc28s.ims.utils;

/**
 * ----------------------------------------------------------
 * Copyright (C) 2018-, by cetc28s1b112s, All rights reserved.
 * ----------------------------------------------------------
 * Created by chendi on 2018/3/27.
 * Version 1.0
 */

public class ConstUtil {

    /**
     * 基本URL  41668   41295  http://17563y833z.imwork.net:41295/
     */
    public static final String BASE_URL = MyApplication.commonUrl;
    /**
     * 登陆地址
     */
    public static final String LOGIN_URL = BASE_URL+"employee/login";
    /**
     * 获取所有人员信息
     */
    public static final String LOGIN_ALL = BASE_URL+"employee/getApprovalList";
    /**
     * 获取所有出差信息
     */
    public static final String OUT_INFO_ALL = BASE_URL+"businessTrip/all";
    /**
     * 添加出差信息
     */
    public static final String OUT_INFO_ADD = BASE_URL+"businessTrip/add";

    /**
     * 查找出差信息
     */
    public static final String OUT_INFO_SEARCH = BASE_URL+"businessTrip/search";
    /**
     * 所有请假信息
     */
    public static final String LEAVE_INFO_ALL = BASE_URL+"vocation/all";
    /**
     * 查询请假信息
     */
    public static final String LEAVE_INFO_SEARCH = BASE_URL+"vocation/search";
    /**
     * 增加请假信息
     */
    public static final String LEAVE_INFO_ADD = BASE_URL+"vocation/add";
    /**
     * 所有日志信息
     */
    public static final String LOG_INFO_ALL = BASE_URL+"workLog/all";

    /**
     *查询日志
     */
    public static final String LOG_INFO_SEARCH = BASE_URL+"workLog/search";
    /**
     * 增加日志信息
     */
    public static final String LOG_INFO_ADD = BASE_URL+"workLog/add";

    /**
     * 查看人员状态
     */
    public static final String USER_STATE = BASE_URL+"employee/state";

    /**
     * tab提示信息保存路径
     */
    public static final String tabFilePath = "/data/data/com.cetc28s.ims/shared_prefs/timeInfo.xml";
}
