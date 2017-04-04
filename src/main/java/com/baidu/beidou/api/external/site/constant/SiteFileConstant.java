package com.baidu.beidou.api.external.site.constant;

public class SiteFileConstant {

	public static String DOWNLOAD_FILE_URL = "http://localhost:8888/apisitefile/";
	public static String DOWNLOAD_FILE_SAVEPATH = "/home/work/";
	
	public static String SITE_FILE_ID = "";
	
	public static String SITE_FILE_MD5 = "";
	
	public static boolean IS_GENERATE_GOING = false;
	
	public interface POSITION_GET_SITEFILE_ID{
	}
	
	public interface POSITION_GET_SITEFILE_STATE{
		public static final String POSITION_SITEFILEID = "fileId";
	}
	
	public interface POSITION_GET_SITEFILE_FILEURL{
		public static final String POSITION_SITEFILEID = "fileId";
	}
	
	public static final String SITEFILE_ZIP_SUFFIX = ".zip";
	
	//md5串长度
	public static final int MD5_STRING_LENGTH = 32;
	
	public static final String[] SITE_FILE_HEADER = {"siteurl","sitename","firstTradeId","secondTradeId","wuliaoType",
		"displayType","isMain","adThruput","srchPv","uv","siteSource"};

	public static final int SITE_SRCH_GT_THRESHOLD = 1;
	public static final int SITE_SRCH_LE_THRESHOLD = 0;
	
	/////////////////////////////////////////////////
	/**
	* 任务状态：未处理
	*/
	public static int TASK_STATUS_NEW = 1;

	/**
	* 任务状态：处理中
	*/
	public static int TASK_STATUS_DOING = 2;

	/**
	* 任务状态：完成
	*/
	public static int TASK_STATUS_OK = 3;

	/**
	* 任务状态：失败
	*/
	public static int TASK_STATUS_FAIL = 4;

	//////////////////////////////////////////////////

	public String getDOWNLOAD_FILE_URL() {
		return DOWNLOAD_FILE_URL;
	}

	public void setDOWNLOAD_FILE_URL(String dOWNLOAD_FILE_URL) {
		DOWNLOAD_FILE_URL = dOWNLOAD_FILE_URL;
	}

	public String getDOWNLOAD_FILE_SAVEPATH() {
		return DOWNLOAD_FILE_SAVEPATH;
	}

	public void setDOWNLOAD_FILE_SAVEPATH(String dOWNLOAD_FILE_SAVEPATH) {
		DOWNLOAD_FILE_SAVEPATH = dOWNLOAD_FILE_SAVEPATH;
	}
	
}
