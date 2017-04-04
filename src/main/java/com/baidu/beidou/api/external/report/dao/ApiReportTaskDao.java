package com.baidu.beidou.api.external.report.dao;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import com.baidu.beidou.api.external.report.bo.ApiReportTask;

/**
 * 
 * InterfaceName: ApiReportDao
 * Function: beidou.api_report表DAO
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public interface ApiReportTaskDao {

	/**
	 * 根据id查找ApiReportTask
	 * @param id 
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findApiReportTasksById(long id);
	
	/**
	 * 根据report id查找ApiReportTask
	 * @param reportid 
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findApiReportTasksByReportId(String reportid);
	
	/**
	 * 根据状态查找ApiReportTask
	 * @param status 
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findApiReportTasksByStatus(int status);
	
	/**
	 * 根据状态和机器名查找ApiReportTask
	 * @param status 
	 * @param machineid 
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findApiReportTasksByStatusAndMachineid(List<Integer> status, String machineid);
	
	/**
	 * 根据用户、状态和机器名查找ApiReportTask
	 * @param userid
	 * @param status 
	 * @param machineid 
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findApiReportTasksByUseridAndStatusAndMachineid(int userid, List<Integer> status, String machineid);
	
	/**
	 * 查询某机器下某状态下超时的任务
	 * @param minTime optime在这个时间之前
	 * @param status  任务状态
	 * @param machineid 机器id
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findApiReportTimeoutTasksByMachineid(Date minTime, int status, String machineid);
	
	/**
	 * 插入新的任务
	 * @param queryparam
	 * @param status
	 * @param userid
	 * @param reportid
	 * @param machineid
	 * @param iszip
	 * @param opuser
	 * @param addtime
	 * @param modtime
	 * @return long 主键id
	 */
	public long addApiReportTask(String queryparam, int status, int userid, String reportid, String machineid, int performancedata, int iszip, 
			int opuser, Date addtime, Date modtime);
	
	/**
	 * 根据id修改任务状态
	 * @param oldStatus
	 * @param newStatus
	 * @param modtime
	 * @param id
	 * @return int
	 */
	public int updateApiReportTaskStatus(int oldStatus, int newStatus, Date modtime, long id);
	
	/**
	 * 根据id修改任务状态，并且递增retry次数+1与修改时间
	 * @param oldStatus
	 * @param newStatus
	 * @param modtime
	 * @param id
	 * @return int
	 */
	public int updateApiReportTaskStatusIncrRetry(int oldStatus, int newStatus, Date modtime, long id);
	
}
