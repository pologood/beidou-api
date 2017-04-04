package com.baidu.beidou.api.external.report.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * ClassName: ReportWebConstants  <br>
 * Function: 常量定义类
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 9, 2012
 */
public class ReportWebConstants {
	
	// 下载文件对外的url，getReportFileUrl返回的是这个url加上文件名，注意要有最后到斜杠“/”
	public static String DOWNLOAD_FILE_URL = "http://localhost:8888/apireport/";
	
	// 下载报告文件保存的本地地址，一般为MFS映射的网盘
	public static String DOWNLOAD_FILE_SAVE_PATH = "e:/123456";
	
	// csv文件的设置
	public static final char REPORT_CSV_SEPERATOR = '\t';
	public static final String REPORT_CSV_ENCODING = "utf-8";
	public static final String REPORT_ZIP_SUFFIX = ".zip";
	
	//绩效常量，展现、点击、消费、CTR、CPM、ACP
	public static final String PERFORMANCE_DATE_SRCH = "srch";
	public static final String PERFORMANCE_DATE_CLICK = "click";
	public static final String PERFORMANCE_DATE_COST = "cost";
	public static final String PERFORMANCE_DATE_CTR = "ctr";
	public static final String PERFORMANCE_DATE_CPM = "cpm";
	public static final String PERFORMANCE_DATE_ACP = "acp";
	// UV
	public static final String PERFORMANCE_DATE_SRCHUV = "srchuv";  //展现独立访客
	public static final String PERFORMANCE_DATE_CLICKUV = "clickuv"; //点击独立访客
	public static final String PERFORMANCE_DATE_SRSUR = "srsur"; //展现频次:展现次数/展现受众
	public static final String PERFORMANCE_DATE_CUSUR = "cusur"; //独立访客点击率：点击受众/展现受众
	public static final String PERFORMANCE_DATE_COCUR = "cocur"; //平均独立访客点击价格：消费/点击受众
	// Holmes
	public static final String PERFORMANCE_DATE_ARRIVAL_RATE = "arrivalRate";  //到达率
	public static final String PERFORMANCE_DATE_HOP_RATE = "hopRate"; //二跳率
	public static final String PERFORMANCE_DATE_RES_TIME = "avgResTime"; //平均访问时间
	public static final String PERFORMANCE_DATE_DIRECT_TRANS_CNT = "directTrans";		//直接转化
	public static final String PERFORMANCE_DATE_INDIRECT_TRANS_CNT = "indirectTrans";	//间接转化
	
	//下载文件格式
	public static final int FORMAT_ZIP = 0;
	public static final int FORMAT_CSV = 1;
	
	//自有流量Domain集合(需要显示二级域的一级域集合) 
	public static List<String> showSecondDomainSiteList = new ArrayList<String>();
	
	//md5串长度
	public static final int MD5_STRING_LENGTH = 32;
	
	// CT、QT词如果在北斗库中找不到则在keyword name后追加这个字符串
	// 兴趣、受众组合也复用该值
	public static final String HAS_BEEN_DELETE_SUFFIX = "[已删除]";
	
	public static final String HAS_BEEN_MODIFIED_SUFFIX = "[已优化]";
	
	//statIds最长限制
	public static int MAX_STATIDS = 100;
	
	//重试的最大次数，失败的任务和超时的任务最大的重试次数
	public static int MAX_RETRY = 1;
	
	//认为已经处理超时的时间分钟数，任务状态是“处理中”，如果now-optime大于这个分钟数，则认为是超时任务
	public static int TASK_TIMEOUT_MINUTES = 5;
	
	
	//是否开启节流阀，用来作负载控制
	//由LoadControlInterceptor调用，如果为false则不限制用户并发请求
	public static boolean ENABLE_THROTTLE = false;
	
	//1个用户最多可以同时处理的任务数量，用来作负载控制
	//由LoadControlInterceptor调用，如果1个用户“未处理”+“处理中”状态的任务超过这个阈值就禁止其再插入JMS任务
	public static int CONCURRENT_TASKS_PER_USER = 5;
	
	//是否开启哈希缓存，开启哈希缓存会将成功的queryparam做md5哈希成一个key，对应到value是
	//reportId，保存在memcache中缓存，如果相同到query过来，就直接取出reportId返回，不放入JMS队列中处理
	//如果为false，则不保存在memcache中
	public static boolean ENABLE_HASH_CACHE = false;
	
	//此参数只有在开启了哈希缓存时才有效，该参数单位为秒，
	//设置保存在memcache中缓存的实效时间
	public static int EXPIRE_TIME_OF_HASH_CACHE = 5;
	
	//是否在服务器启动时处理所有状态为1和404的任务，做到原子性
	//由InitUndoneTasks使用
	public static boolean ENABLE_INIT_ATOMICITY = true;
	
	public static final String ERR_DORIS = "查询数据量过大，建议您缩小查询范围。";
	
	/**
	 * 人群属性性别常量
	 * 7:男
	 * 8:女
	 * 0:未知性别
	 * 9:未传入
	 */
	public static final int DT_GENDER_NOTKNOWN = 0;
	public static final int DT_GENDER_MALE = 7;
	public static final int DT_GENDER_FEMALE = 8;
	public static final int DT_GENDER_NOTINPUT = 9;
	public static final String DT_GENDER_MALE_STRING = "男";
	public static final String DT_GENDER_FEMALE_STRING = "女";
	public static final String DT_GENDER_NOTKNOWN_STRING = "未知";
	
	/**
	 * 地域报表相关常量定义
	 * 1:一级地域
	 * 2:二级地域
	 */
	public static final int REGION_TYPE_FIRST = 1;
	public static final int REGION_TYPE_SECOND = 2;
	public static final String REGION_OTHER = "其他";
	public static final String REGION_NOTKNOWN = "未知";
	public static final String REGION_COLUMN_REGIONNAME = "regionName";
	
	/**
	 * 兴趣报告相关常量定义
	 * 1：一级兴趣点或者抽象人群属性
	 * 2：二级兴趣点
	 * 3：兴趣组合
	 */
	public static final int INTEREST_TYPE_FIRST = 1;
	public static final int INTEREST_TYPE_SECOND = 2;
	public static final int INTEREST_TYPE_CUSTOM = 3;
	public static final String INTEREST_NO_PACKNAME = "-";
	
	//报告类型
	public static interface REPORT_TYPE {
		public static final int ACCOUNT = 1; // 账户报告
		public static final int PLAN = 2; // 推广计划报告
		public static final int GROUP = 3; // 推广组报告
		public static final int UNIT = 4; // 创意报告
		public static final int KEYWORD_NORMAL = 5; // 关键词报告
		public static final int KEYWORD_NORMAL_COMPATIBLE = 6;  // 关键词报告(兼容之前的分为主题词和关键词报告，现和5的意义相同，做兼容使用)
		public static final int SITE_SHOWN = 7;   // 有展现网站报告
		public static final int INTEREST_SHOWN = 8;  // 有展现兴趣报告
		public static final int PACK = 9;  // 受众组合报告
		public static final int KEYWORD_PACK = 10; // 关键词组合报告
		public static final int REGION = 11;  // 地域报告
		public static final int GENDER = 12;  // 性别报告
		public static final int INTEREST_CHOSEN = 13;  // 自选兴趣报告
		public static final int SITE_CHOSEN = 14;  // 自选网站报告
		public static final int TRADE_CHOSEN = 15;  // 自选行业报告
		public static final int APP = 16;
		public static final int DEVICE = 17;
		public static final int ATTACH_PHONE = 18; // 附加信息—电话
		public static final int ATTACH_MESSAGE = 19; // 附加信息-短信
		public static final int ATTACH_CONSULT = 20; // 附加信息-咨询
		public static final int ATTACH_SUB_URL = 21; // 附加信息-子链
		//public static final int PACK_INTEREST = 13;  // 受众组合报告详情的兴趣报告 
		//public static final int PACK_KEYWORD = 14;  // 受众组合报告详情的关键词报告
	}
	
	//报告范围
	public static interface REPORT_RANGE {
		public static final int ACCOUNT = 1;
		public static final int PLAN = 2;
		public static final int GROUP = 3;
		public static final int UNIT = 4;
	}
	
	// 前后端参数名映射转换：报告类型与报告范围转换
	public static final Map<Integer, List<Integer>> REPORT_RANGE_ALLOWED_TYPES_MAP = new HashMap<Integer, List<Integer>>();
	
	// id only的csv header头
	public static final Map<Integer, String[]> REPORT_HEADER_IDONLY = new HashMap<Integer, String[]>();
	
	// id not only的csv header头
	public static final Map<Integer, String[]> REPORT_HEADER_NOT_IDONLY = new HashMap<Integer, String[]>();
	
	static {

		List<Integer> accountRangeTypes = new ArrayList<Integer>();
		accountRangeTypes.add(REPORT_TYPE.ACCOUNT);
		accountRangeTypes.add(REPORT_TYPE.PLAN);
		accountRangeTypes.add(REPORT_TYPE.GROUP);
		accountRangeTypes.add(REPORT_TYPE.UNIT);
		accountRangeTypes.add(REPORT_TYPE.KEYWORD_NORMAL);
		accountRangeTypes.add(REPORT_TYPE.KEYWORD_NORMAL_COMPATIBLE);
		accountRangeTypes.add(REPORT_TYPE.SITE_SHOWN);
		accountRangeTypes.add(REPORT_TYPE.INTEREST_SHOWN);
		accountRangeTypes.add(REPORT_TYPE.PACK);
		accountRangeTypes.add(REPORT_TYPE.KEYWORD_PACK);
		accountRangeTypes.add(REPORT_TYPE.REGION);
		accountRangeTypes.add(REPORT_TYPE.GENDER);
		accountRangeTypes.add(REPORT_TYPE.INTEREST_CHOSEN);
		accountRangeTypes.add(REPORT_TYPE.SITE_CHOSEN);
		accountRangeTypes.add(REPORT_TYPE.TRADE_CHOSEN);
		accountRangeTypes.add(REPORT_TYPE.APP);
		accountRangeTypes.add(REPORT_TYPE.DEVICE);
		accountRangeTypes.add(REPORT_TYPE.ATTACH_PHONE);
		accountRangeTypes.add(REPORT_TYPE.ATTACH_MESSAGE);
		accountRangeTypes.add(REPORT_TYPE.ATTACH_CONSULT);
		accountRangeTypes.add(REPORT_TYPE.ATTACH_SUB_URL);
		REPORT_RANGE_ALLOWED_TYPES_MAP.put(REPORT_RANGE.ACCOUNT, accountRangeTypes);
		
		List<Integer> planRangeTypes = new ArrayList<Integer>();
		planRangeTypes.add(REPORT_TYPE.PLAN);
		planRangeTypes.add(REPORT_TYPE.GROUP);
		planRangeTypes.add(REPORT_TYPE.UNIT);
		planRangeTypes.add(REPORT_TYPE.KEYWORD_NORMAL);
		planRangeTypes.add(REPORT_TYPE.KEYWORD_NORMAL_COMPATIBLE);
		planRangeTypes.add(REPORT_TYPE.SITE_SHOWN);
		planRangeTypes.add(REPORT_TYPE.INTEREST_SHOWN);
		planRangeTypes.add(REPORT_TYPE.PACK);
		planRangeTypes.add(REPORT_TYPE.KEYWORD_PACK);
		planRangeTypes.add(REPORT_TYPE.REGION);
		planRangeTypes.add(REPORT_TYPE.GENDER);
		planRangeTypes.add(REPORT_TYPE.INTEREST_CHOSEN);
		planRangeTypes.add(REPORT_TYPE.SITE_CHOSEN);
		planRangeTypes.add(REPORT_TYPE.TRADE_CHOSEN);
		planRangeTypes.add(REPORT_TYPE.APP);
		planRangeTypes.add(REPORT_TYPE.DEVICE);
		planRangeTypes.add(REPORT_TYPE.ATTACH_PHONE);
		planRangeTypes.add(REPORT_TYPE.ATTACH_MESSAGE);
		planRangeTypes.add(REPORT_TYPE.ATTACH_CONSULT);
		planRangeTypes.add(REPORT_TYPE.ATTACH_SUB_URL);
		REPORT_RANGE_ALLOWED_TYPES_MAP.put(REPORT_RANGE.PLAN, planRangeTypes);
		
		List<Integer> groupRangeTypes = new ArrayList<Integer>();
		groupRangeTypes.add(REPORT_TYPE.GROUP);
		groupRangeTypes.add(REPORT_TYPE.UNIT);
		groupRangeTypes.add(REPORT_TYPE.KEYWORD_NORMAL);
		groupRangeTypes.add(REPORT_TYPE.KEYWORD_NORMAL_COMPATIBLE);
		groupRangeTypes.add(REPORT_TYPE.SITE_SHOWN);
		groupRangeTypes.add(REPORT_TYPE.INTEREST_SHOWN);
		groupRangeTypes.add(REPORT_TYPE.PACK);
		groupRangeTypes.add(REPORT_TYPE.KEYWORD_PACK);
		groupRangeTypes.add(REPORT_TYPE.REGION);
		groupRangeTypes.add(REPORT_TYPE.GENDER);
		groupRangeTypes.add(REPORT_TYPE.INTEREST_CHOSEN);
		groupRangeTypes.add(REPORT_TYPE.SITE_CHOSEN);
		groupRangeTypes.add(REPORT_TYPE.TRADE_CHOSEN);
		groupRangeTypes.add(REPORT_TYPE.APP);
		groupRangeTypes.add(REPORT_TYPE.DEVICE);
		groupRangeTypes.add(REPORT_TYPE.ATTACH_PHONE);
		groupRangeTypes.add(REPORT_TYPE.ATTACH_MESSAGE);
		groupRangeTypes.add(REPORT_TYPE.ATTACH_CONSULT);
		groupRangeTypes.add(REPORT_TYPE.ATTACH_SUB_URL);
		REPORT_RANGE_ALLOWED_TYPES_MAP.put(REPORT_RANGE.GROUP, groupRangeTypes);
		
		List<Integer> unitRangeTypes = new ArrayList<Integer>();
		unitRangeTypes.add(REPORT_TYPE.UNIT);
		REPORT_RANGE_ALLOWED_TYPES_MAP.put(REPORT_RANGE.UNIT, unitRangeTypes);
		
		//日期	账户ID	账户	srchs	click	cost	ctr	acp	cpm
		REPORT_HEADER_IDONLY.put(1, new String[]{"日期" ,"账户ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(2, new String[]{"日期" ,"账户ID", "推广计划ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(3, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(4, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "创意ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(5, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "关键词ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(6, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "关键词ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(7, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "网站", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(8, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "推广组-受众组合关联关系ID", "兴趣ID", "类型", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(9, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "推广组-受众组合关联关系ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(10, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "推广组-受众组合关联关系ID", "关键词ID", "类型", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(11, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "地域ID", "地域类型", "一级地域ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(12, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "性别ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(13, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "推广组-受众组合关联关系ID", "兴趣ID", "类型", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(14, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "网站", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(15, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "行业", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(16, new String[]{"日期" ,"账户ID", "推广计划ID", "推广组ID", "应用ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(17, new String[]{"日期" ,"账户ID", "推广计划ID","设备ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_IDONLY.put(18, new String[] {"日期", "账户ID", "推广计划ID", "推广组ID", "服务电话ID", "展现", "点击", "消费",
				"CTR", "CPM", "ACP", "展现独立访客", "点击独立访客", "展现频次", "独立访客点击率", "平均独立访客点击价格", "到达率", "二跳率", "平均访问时间",
				"直接转化", "间接转化"});
		REPORT_HEADER_IDONLY.put(19, new String[] {"日期", "账户ID", "推广计划ID", "推广组ID", "短信号码ID", "展现", "点击", "消费",
				"CTR", "CPM", "ACP", "展现独立访客", "点击独立访客", "展现频次", "独立访客点击率", "平均独立访客点击价格", "到达率", "二跳率", "平均访问时间",
				"直接转化", "间接转化"});
		REPORT_HEADER_IDONLY.put(20, new String[] {"日期", "账户ID", "推广计划ID", "推广组ID", "展现", "点击", "消费", "CTR", "CPM",
				"ACP", "展现独立访客", "点击独立访客", "展现频次", "独立访客点击率", "平均独立访客点击价格", "到达率", "二跳率", "平均访问时间", "直接转化", "间接转化"});
		REPORT_HEADER_IDONLY.put(21, new String[] {"日期", "账户ID", "推广计划ID", "推广组ID", "子链", "展现", "点击", "消费", "CTR",
				"CPM", "ACP", "展现独立访客", "点击独立访客", "展现频次", "独立访客点击率", "平均独立访客点击价格", "到达率", "二跳率", "平均访问时间", "直接转化",
				"间接转化"});


		REPORT_HEADER_NOT_IDONLY.put(1, new String[]{"日期" ,"账户ID", "账户", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(2, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(3, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(4, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "创意ID", "创意标题", "创意类型", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(5, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "主题词ID", "主题词", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(6, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "关键词ID", "关键词", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(7, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "网站", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(8, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "推广组-受众组合关联关系ID", "受众组合", "兴趣ID", "兴趣", "类型",  "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(9, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "推广组-受众组合关联关系ID", "受众组合", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(10, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "推广组-受众组合关联关系ID", "受众组合", "关键词ID", "关键词", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(11, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "地域ID", "地域", "地域类型", "一级地域ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(12, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "性别ID", "性别", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(13, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "推广组-受众组合关联关系ID", "受众组合", "兴趣ID", "兴趣", "类型",  "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(14, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "网站", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(15, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "行业", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(16, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "应用ID","应用名称" ,"展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(17, new String[]{"日期" ,"账户ID", "账户", "推广计划ID", "推广计划", "设备ID", "展现" , "点击", "消费" , "CTR", "CPM", "ACP","展现独立访客","点击独立访客","展现频次","独立访客点击率","平均独立访客点击价格","到达率","二跳率","平均访问时间","直接转化","间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(18, new String[] {"日期", "账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组",
				"服务电话ID", "服务电话", "展现", "点击", "消费", "CTR", "CPM", "ACP", "展现独立访客", "点击独立访客", "展现频次", "独立访客点击率",
				"平均独立访客点击价格", "到达率", "二跳率", "平均访问时间", "直接转化", "间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(19, new String[] {"日期", "账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组",
				"短信号码ID", "短信号码", "展现", "点击", "消费", "CTR", "CPM", "ACP", "展现独立访客", "点击独立访客", "展现频次", "独立访客点击率",
				"平均独立访客点击价格", "到达率", "二跳率", "平均访问时间", "直接转化", "间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(20, new String[] {"日期", "账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "展现",
				"点击", "消费", "CTR", "CPM", "ACP", "展现独立访客", "点击独立访客", "展现频次", "独立访客点击率", "平均独立访客点击价格", "到达率", "二跳率",
				"平均访问时间", "直接转化", "间接转化"});
		REPORT_HEADER_NOT_IDONLY.put(21, new String[] {"日期", "账户ID", "账户", "推广计划ID", "推广计划", "推广组ID", "推广组", "子链",
				"展现", "点击", "消费", "CTR", "CPM", "ACP", "展现独立访客", "点击独立访客", "展现频次", "独立访客点击率", "平均独立访客点击价格", "到达率",
				"二跳率", "平均访问时间", "直接转化", "间接转化"});
	}
	
	public void setShowSecondDomainSiteList(
			String showSecondDomainSiteList) {
		if (!StringUtils.isEmpty(showSecondDomainSiteList)) {
			if (ReportWebConstants.showSecondDomainSiteList == null) {
				ReportWebConstants.showSecondDomainSiteList = new ArrayList<String>();
			}
			for (String domain : showSecondDomainSiteList.split(",")) {
				ReportWebConstants.showSecondDomainSiteList.add(domain);
			}
		}
	}

	public static String getDOWNLOAD_FILE_URL() {
		return DOWNLOAD_FILE_URL;
	}

	public void setDOWNLOAD_FILE_URL(String download_file_url) {
		DOWNLOAD_FILE_URL = download_file_url;
	}

	public static String getDOWNLOAD_FILE_SAVE_PATH() {
		return DOWNLOAD_FILE_SAVE_PATH;
	}

	public void setDOWNLOAD_FILE_SAVE_PATH(String download_file_save_path) {
		DOWNLOAD_FILE_SAVE_PATH = download_file_save_path;
	}

	public int getMAX_RETRY() {
		return MAX_RETRY;
	}

	public void setMAX_RETRY(int max_retry) {
		MAX_RETRY = max_retry;
	}

	public int getMAX_STATIDS() {
		return MAX_STATIDS;
	}
	
	public void setMAX_STATIDS(int max_statids) {
		MAX_STATIDS = max_statids;
	}

	public int getTASK_TIMEOUT_MINUTES() {
		return TASK_TIMEOUT_MINUTES;
	}

	public void setTASK_TIMEOUT_MINUTES(int task_timeout_minutes) {
		TASK_TIMEOUT_MINUTES = task_timeout_minutes;
	}

	public int getCONCURRENT_TASKS_PER_USER() {
		return CONCURRENT_TASKS_PER_USER;
	}

	public void setCONCURRENT_TASKS_PER_USER(int concurrent_tasks_per_user) {
		CONCURRENT_TASKS_PER_USER = concurrent_tasks_per_user;
	}

	public boolean isENABLE_HASH_CACHE() {
		return ENABLE_HASH_CACHE;
	}

	public void setENABLE_HASH_CACHE(boolean enable_hash_cache) {
		ENABLE_HASH_CACHE = enable_hash_cache;
	}

	public int getEXPIRE_TIME_OF_HASH_CACHE() {
		return EXPIRE_TIME_OF_HASH_CACHE;
	}

	public void setEXPIRE_TIME_OF_HASH_CACHE(int expire_time_of_hash_cache) {
		EXPIRE_TIME_OF_HASH_CACHE = expire_time_of_hash_cache;
	}

	public boolean isENABLE_THROTTLE() {
		return ENABLE_THROTTLE;
	}

	public void setENABLE_THROTTLE(boolean enable_throttle) {
		ENABLE_THROTTLE = enable_throttle;
	}

	public boolean isENABLE_INIT_ATOMICITY() {
		return ENABLE_INIT_ATOMICITY;
	}

	public void setENABLE_INIT_ATOMICITY(boolean enable_init_atomicity) {
		ENABLE_INIT_ATOMICITY = enable_init_atomicity;
	}


}