package com.baidu.beidou.api.external.report.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.beidou.api.external.report.constant.ReportWebConstants.REPORT_TYPE;

/**
 * 
 * ClassName: ApiReportTaskConstant
 * Function: API报告表DAO内status状态常量
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public class ApiReportTaskConstant {

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
	public static int TASK_STATUS_FAIL = 400;
	
	/**
	 * 任务状态：服务器内部错误强制失败
	 */
	public static int TASK_STATUS_INTERNAL_ERROR_FAIL = 401;
	
	/**
	 * 任务状态：查询范围过大失败
	 */
	public static int TASK_STATUS_QUERY_TOO_LARGE_FAIL = 402;
	
	/**
	 * 任务状态：处理异常重试中
	 */
	public static int TASK_STATUS_FAIL_WITH_RETRY = 403;
	
	/**
	 * 任务状态：处理超时重试中
	 */
	public static int TASK_STATUS_TIMEOUT_WITH_RETRY = 404;
	
	//////////////////////////////////////////////////
	/**
	 * 任务状态：处理超时重试中
	 */
	public static int FRONT_TASK_STATUS_NEW = 1;
	
	public static int FRONT_TASK_STATUS_DOING = 2;
	
	public static int FRONT_TASK_STATUS_OK = 3;
	
	public static int FRONT_TASK_STATUS_FAIL = 4;
	
	public static int FRONT_TASK_STATUS_FAIL_QUERY_TOO_LARGE = 5;
	
	// report status 前后端转换
	public static final Map<Integer, Integer> REPORT_STATUS_FRONT_END_MAP = new HashMap<Integer, Integer>();
	
	static {
		REPORT_STATUS_FRONT_END_MAP.put(TASK_STATUS_NEW, FRONT_TASK_STATUS_NEW);
		REPORT_STATUS_FRONT_END_MAP.put(TASK_STATUS_DOING, FRONT_TASK_STATUS_DOING);
		REPORT_STATUS_FRONT_END_MAP.put(TASK_STATUS_OK, FRONT_TASK_STATUS_OK);
		
		REPORT_STATUS_FRONT_END_MAP.put(TASK_STATUS_FAIL, FRONT_TASK_STATUS_FAIL);
		REPORT_STATUS_FRONT_END_MAP.put(TASK_STATUS_INTERNAL_ERROR_FAIL, FRONT_TASK_STATUS_FAIL);
		REPORT_STATUS_FRONT_END_MAP.put(TASK_STATUS_FAIL_WITH_RETRY, FRONT_TASK_STATUS_FAIL);
		REPORT_STATUS_FRONT_END_MAP.put(TASK_STATUS_TIMEOUT_WITH_RETRY, FRONT_TASK_STATUS_FAIL);
		
		REPORT_STATUS_FRONT_END_MAP.put(TASK_STATUS_QUERY_TOO_LARGE_FAIL, FRONT_TASK_STATUS_FAIL_QUERY_TOO_LARGE);
	}
	
	
}
