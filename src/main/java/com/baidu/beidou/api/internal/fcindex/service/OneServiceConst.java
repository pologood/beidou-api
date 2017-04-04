package com.baidu.beidou.api.internal.fcindex.service;

public final class OneServiceConst {

	public static final int MAX_USERIDS_NUM = 20;
	
    public static final class PromoteType {
        // promotion
        public static final int PRO = 0;

        // ad
        public static final int AD = 1;
    }

    public static final class ByDay {
        // by day
        public static final int YES = 1;

        public static final int NO = 0;
    }

    public static final class SysCode {
        // cpro
        public static final String CPRO = "5_0";

        // Wang Meng
        public static final String WM = "5";
    }

    public static final class ErrorCode {
        public static final String NO_RESULT = "NO_RESULT";

        public static final String DATE_FORMAT = "WRONG_DATE_FORMAT";

        public static final String DATE_RANGE_SINGLE = "WRONG_DATE_RANGE_92";

        public static final String DATE_RANGE_BATCH = "WRONG_DATE_RANGE_31";

        public static final String DATE_RANGE_MIX = "START_AFTER_END";

        public static final String DATE_NO_TODAY = "NO_TODAY";

        public static final String BY_DAY = "MIS_BY_DAY_0_1";
        
        public static final String USERIDS_EXCEED = "USERIDS_EXCEED";
        
        public static final String USERIDS_EMPTY = "USERIDS_EMPTY";
    }

    public static final class StatusCode {
        public static final int SUCCESSFUL = 0;

        public static final int FAILED = 1;
    }

    public static final class TimeRange {
        public static final int SINGLE = 92;

        public static final int BATCH = 31;
    }
}
