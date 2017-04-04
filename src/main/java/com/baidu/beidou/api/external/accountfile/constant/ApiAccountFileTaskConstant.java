package com.baidu.beidou.api.external.accountfile.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * ClassName: ApiAccountFileTaskConstant  <br>
 * Function: API账户数据表DAO内status状态常量
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class ApiAccountFileTaskConstant {

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

	public static int FRONT_TASK_STATUS_NEW = 1;
	
	public static int FRONT_TASK_STATUS_DOING = 2;
	
	public static int FRONT_TASK_STATUS_OK = 3;
	
	public static int FRONT_TASK_STATUS_FAIL = 4;
	
	// report status 前后端转换
	public static final Map<Integer, Integer> ACCOUNTFILE_STATUS_FRONT_END_MAP = new HashMap<Integer, Integer>();
	
	static {
		ACCOUNTFILE_STATUS_FRONT_END_MAP.put(TASK_STATUS_NEW, FRONT_TASK_STATUS_NEW);
		ACCOUNTFILE_STATUS_FRONT_END_MAP.put(TASK_STATUS_DOING, FRONT_TASK_STATUS_DOING);
		ACCOUNTFILE_STATUS_FRONT_END_MAP.put(TASK_STATUS_OK, FRONT_TASK_STATUS_OK);
		ACCOUNTFILE_STATUS_FRONT_END_MAP.put(TASK_STATUS_FAIL, FRONT_TASK_STATUS_FAIL);
	}
	
}
