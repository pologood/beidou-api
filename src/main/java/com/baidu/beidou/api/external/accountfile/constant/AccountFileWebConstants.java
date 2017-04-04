package com.baidu.beidou.api.external.accountfile.constant;

/**
 * 
 * ClassName: AccountFileWebConstants  <br>
 * Function: 常量设置
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 27, 2012
 */
public class AccountFileWebConstants {

	// 下载文件对外的url，getAccountFileUrl返回的是这个url加上文件名，注意要有最后到斜杠“/”!!
	public static String DOWNLOAD_FILE_URL = "http://localhost:8888/apiaccountfile/";
	
	// 下载账户数据文件保存的本地地址，一般为MFS映射的网盘,可以不带"/"
	public static String DOWNLOAD_FILE_SAVE_PATH = "e:/api_accountfile";
	
	// 下载数据文件保存的本地地址，保存压缩打包前的数据
	public static String TMP_SAVE_PATH = "e:/tmp";
	
	// 保存在数据库中的planId分隔符
	public static final char ACCOUNTFILE_PLANID_SEPERATOR = ',';
	
	// 保存在数据库cprogroupinfo中的site,trade分隔符
	public static final char GROUPINFO_SITE_TRADE_SEPERATOR = '|';
	
	// 保存在文件中的数据分隔符
	public static final char ACCOUNTFILE_DATA_SEPERATOR = '|';
	public static final String ACCOUNTFILE_DATA_SEPERATOR_STR = "|";
	
	// 哈希缓存中key的分隔符
	public static final char ACCOUNTFILE_HASH_CACHE_KEY_SEPERATOR = '-';
	
	// 压缩文件的设置
	public static final char ACCOUNTFILE_CSV_SEPERATOR = '\t';
	public static final String ACCOUNTFILE_CSV_ENCODING = "utf-8";
	public static final String ACCOUNTFILE_ZIP_SUFFIX = ".zip";
	public static final String ACCOUNTFILE_GZIP_SUFFIX = ".tar.gz";
	public static final String ACCOUNTFILE_TAR_SUFFIX = ".tar";
	
	//下载文件格式
	public static final int FORMAT_ZIP = 0;
	public static final int FORMAT_GZIP = 1;
	
	//md5串长度
	public static final int MD5_STRING_LENGTH = 32;
	
	//campaignIds最长限制
	public static int MAX_PLANIDS = 100;
	
	//认为已经处理超时的时间分钟数，任务状态是“处理中”，如果now-optime大于这个分钟数，则认为是超时任务
	public static int TASK_TIMEOUT_MINUTES = 5;
	
	//是否开启节流阀，用来作负载控制
	//由LoadControlInterceptor调用，如果为false则不限制用户并发请求
	public static boolean ENABLE_THROTTLE = false;
	
	//1个用户最多可以同时处理的任务数量，用来作负载控制
	//由LoadControlInterceptor调用，如果1个用户“处理中”状态的任务超过这个阈值就禁止其再插入JMS任务
	public static int CONCURRENT_TASKS_PER_USER = 5;
	
	//是否开启哈希锁，开启哈希缓存会将userId+planIds+format作为一个key，对应到value是
	//fileId，保存在memcache中缓存，如果相同到query过来，就直接取出fileId返回，不放入JMS队列中处理
	//如果为false，则不保存在memcache中
	public static boolean ENABLE_HASH_LOCK = false;
	
	//此参数只有在开启了哈希锁时才有效，该参数单位为秒，
	//设置保存在memcache中缓存的失效时间
	public static int EXPIRE_TIME_OF_HASH_LOCK = 600;
	
	//是否在服务器启动时处理所有状态为1（未处理）的任务，做到原子性
	//由InitUndoneTasks使用
	public static boolean ENABLE_INIT_ATOMICITY = true;
	
	//配置文件中输出文件的头的分隔符
	public static final String HEADERS_SEPERATOR = ",";

	public String getDOWNLOAD_FILE_URL() {
		return DOWNLOAD_FILE_URL;
	}

	public void setDOWNLOAD_FILE_URL(String download_file_url) {
		DOWNLOAD_FILE_URL = download_file_url;
	}

	public String getDOWNLOAD_FILE_SAVE_PATH() {
		return DOWNLOAD_FILE_SAVE_PATH;
	}

	public void setDOWNLOAD_FILE_SAVE_PATH(String download_file_save_path) {
		DOWNLOAD_FILE_SAVE_PATH = download_file_save_path;
	}

	public String getTMP_SAVE_PATH() {
		return TMP_SAVE_PATH;
	}

	public void setTMP_SAVE_PATH(String tmp_save_path) {
		TMP_SAVE_PATH = tmp_save_path;
	}

	public int getMAX_PLANIDS() {
		return MAX_PLANIDS;
	}

	public void setMAX_PLANIDS(int max_planids) {
		MAX_PLANIDS = max_planids;
	}

	public int getTASK_TIMEOUT_MINUTES() {
		return TASK_TIMEOUT_MINUTES;
	}

	public void setTASK_TIMEOUT_MINUTES(int task_timeout_minutes) {
		TASK_TIMEOUT_MINUTES = task_timeout_minutes;
	}

	public boolean isENABLE_THROTTLE() {
		return ENABLE_THROTTLE;
	}

	public void setENABLE_THROTTLE(boolean enable_throttle) {
		ENABLE_THROTTLE = enable_throttle;
	}

	public int getCONCURRENT_TASKS_PER_USER() {
		return CONCURRENT_TASKS_PER_USER;
	}

	public void setCONCURRENT_TASKS_PER_USER(int concurrent_tasks_per_user) {
		CONCURRENT_TASKS_PER_USER = concurrent_tasks_per_user;
	}

	public boolean isENABLE_HASH_LOCK() {
		return ENABLE_HASH_LOCK;
	}

	public void setENABLE_HASH_LOCK(boolean enable_hash_lock) {
		ENABLE_HASH_LOCK = enable_hash_lock;
	}

	public int getEXPIRE_TIME_OF_HASH_LOCK() {
		return EXPIRE_TIME_OF_HASH_LOCK;
	}

	public  void setEXPIRE_TIME_OF_HASH_LOCK(int expire_time_of_hash_lock) {
		EXPIRE_TIME_OF_HASH_LOCK = expire_time_of_hash_lock;
	}

	public char getACCOUNTFILE_HASH_CACHE_KEY_SEPERATOR() {
		return ACCOUNTFILE_HASH_CACHE_KEY_SEPERATOR;
	}

	public boolean isENABLE_INIT_ATOMICITY() {
		return ENABLE_INIT_ATOMICITY;
	}

	public void setENABLE_INIT_ATOMICITY(boolean enable_init_atomicity) {
		ENABLE_INIT_ATOMICITY = enable_init_atomicity;
	}

	public char getACCOUNTFILE_PLANID_SEPERATOR() {
		return ACCOUNTFILE_PLANID_SEPERATOR;
	}

	public char getACCOUNTFILE_DATA_SEPERATOR() {
		return ACCOUNTFILE_DATA_SEPERATOR;
	}

	public char getACCOUNTFILE_CSV_SEPERATOR() {
		return ACCOUNTFILE_CSV_SEPERATOR;
	}

	public String getACCOUNTFILE_CSV_ENCODING() {
		return ACCOUNTFILE_CSV_ENCODING;
	}

	public String getACCOUNTFILE_ZIP_SUFFIX() {
		return ACCOUNTFILE_ZIP_SUFFIX;
	}

	public String getACCOUNTFILE_GZIP_SUFFIX() {
		return ACCOUNTFILE_GZIP_SUFFIX;
	}

	public String getACCOUNTFILE_TAR_SUFFIX() {
		return ACCOUNTFILE_TAR_SUFFIX;
	}

	public int getFORMAT_ZIP() {
		return FORMAT_ZIP;
	}

	public int getFORMAT_GZIP() {
		return FORMAT_GZIP;
	}

	public int getMD5_STRING_LENGTH() {
		return MD5_STRING_LENGTH;
	}

	public String getHEADERS_SEPERATOR() {
		return HEADERS_SEPERATOR;
	}
	
	
}
