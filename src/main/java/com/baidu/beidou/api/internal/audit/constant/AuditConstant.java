package com.baidu.beidou.api.internal.audit.constant;

import com.baidu.beidou.cprounit.constant.CproUnitConstant;

public class AuditConstant {

    // 审核结果类型
    public static final int AUDIT_RESULT_ALL = -1;
    public static final int AUDIT_RESULT_PASS = 0;
    public static final int AUDIT_RESULT_REFUSE = 1;
    public static final int AUDIT_RESULT_PAUSE = 2;

    public static final int CONFIDENCE_LEVEL_NO = 0;
    public static final int CONFIDENCE_LEVEL_LOW = 1;
    public static final int CONFIDENCE_LEVEL_MID = 2;
    public static final int CONFIDENCE_LEVEL_HIGH = 3;

    public static final String[] USER_VIEW_STATE_NAME = { "账户开通", "账户关闭" };

    /**
     * 对接风控的北斗审核接口
     */
    public static final int BEIDOU_FIRST_AUDIT = 1;  // 北斗一审
    public static final int BEIDOU_SECOND_AUDIT = 2; // 北斗复审
    
    public static final int BEIDOU_AUDIT_PASS = CproUnitConstant.UNIT_STATE_NORMAL;
    public static final int BEIDOU_AUDIT_REFUSE = CproUnitConstant.UNIT_STATE_REFUSE;
    
    public static final int REAUDIT_UNITID_LENGTH = 1000;
    
    public static final int APP_ID_BEIDOU_NORMAL_UNIT = 301;
    public static final int APP_ID_BEIDOU_SMART_UNIT = 302;
    
    public static final int DATA_STREAM_TYPE_INCR = 0;  // 增量数据流
    public static final int DATA_STREAM_TYPE_TOTAL = 1; // 全量数据流
    
}
