package com.baidu.beidou.api.internal.audit.constant;

public class QueryConstant {

	public final static class QueryUnitAudit {
		public final static int planName = 1;		// 推广计划名称
		public final static int groupName = 2;		// 推广组名称
		public final static int unitId = 3;			// 创意ID
		public final static int unitContent = 4;	// 创意内容（标题和描述）
		public final static int targetUrl = 5;		// 点击URL
		public final static int showUrl = 6;		// 显示URL
	}
	
	public final static class QueryUnitType {
        public static final int all = -1;   // 全部
		public final static int text = 1;		// 文字
		public final static int image = 2;		// 图片
		public final static int icon = 5;		// 图文
        public static final int smart = 9; // 智能创意
        public static final int allNotSmart = 10; // 除智能创意以外的所有
	}
	
	
	public final static class QueryUserReaudit {
		public final static int allUser = 0;		// 全部
		public final static int userName = 1;		// 用户名查询
		public final static int userId = 2;			// 用户ID查询
	}
	
	public final static class QueryUserReauditByUnitIds {
		public final static int unitIds = 1;		// 创意ID查询
	}
	
	public final static int TRADE_MODIFIED = 1;	// 人工修改标记
	
	public final static class QueryModifyTag {
		public static final int confidenceLevel = 1;  // 1：准确度
		public static final int beautyLevel = 2;	  // 2：美观度
		public static final int cheatLevel= 3;        // 3：欺诈
        public static final int vulgarLevel = 4;      // 4：低俗
        public static final int dangerLevel = 5;      // 5：高危
		
		public static final int levelMinValue = 0;		// 0：未评定，最小值
		
		public static final int confidenceLevelMaxValue = 3;	// 1：准确度最大值为3
		public static final int beautyLevelMaxValue = 3;		// 2：美观度最大值为3
		public static final int cheatLevelMaxValue = 2;           // 3：欺诈最大值为2
        public static final int vulgarLevelMaxValue = 3;        // 4：低俗最大值为3
        public static final int dangerLevelMaxValue = 2;        // 5：高危最大值为2
    }
    public static final int eachTagLength = 3;
    public static final int tagNum = 5; // 标记为的个数
	
	public static final int GET_AUDITING_UNIT_MAX = 1000;
}
