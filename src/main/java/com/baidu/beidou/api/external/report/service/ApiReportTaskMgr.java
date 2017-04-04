package com.baidu.beidou.api.external.report.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.report.bo.ApiReportTask;
import com.baidu.beidou.api.external.report.constant.ApiReportTaskConstant;
import com.baidu.beidou.exception.InternalException;

/**
 * 
 * InterfaceName: ApiReportTaskMgr
 * Function: 报告任务表api_report服务接口
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public interface ApiReportTaskMgr {

	/**
	 * 根据id查找ApiReportTask
	 * @param id 
	 * @return ApiReportTask
	 */
	public ApiReportTask findTasksById(long id);
	
	/**
	 * 根据report id查找ApiReportTask
	 * @param reportid 
	 * @return ApiReportTask
	 */
	public ApiReportTask findTasksByReportId(String reportid);
	
	/**
	 * 根据report id查找本机处理的超时ApiReportTask列表
	 * @param reportid 
	 * @param machineid
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findTimeoutTasksByMachineid(Date minTime, String machineid);
	
	/**
	 * 查找本机失败重试的任务
	 * @param machineid
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findFailTasksByMachieid(String machineid);
	
	/**
	 * 根据机器id和状态查找任务
	 * @param status
	 * @param machineid
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findTasksByStatusAndMachieid(List<Integer> status, String machineid);
	
	/**
	 * 根据用户、机器id和状态查找任务
	 * @param userid
	 * @param status
	 * @param machineid
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findTasksByUseridAndStatusAndMachieid(int userid, List<Integer> status, String machineid);
	
	/**
	 * 插入新的任务
	 * @param task
	 * @return long 主键id
	 */
	public long addTask(ApiReportTask task);
	
	/**
	 * 修改任务状态为处理中
	 * @param task
	 * @return int
	 */
	public int updateTaskStatusDoing(ApiReportTask task) throws InternalException;
	
	/**
	 * 修改任务状态为成功
	 * @param task
	 * @return int
	 */
	public int updateTaskStatusOk(ApiReportTask task) throws InternalException;
	
	/**
	 * 修改任务状态为失败
	 * @param task
	 * @return int
	 */
	public int updateTaskStatusFail(ApiReportTask task) throws InternalException;
	
	/**
	 * 修改任务状态为查询范围过大失败
	 * @param task
	 * @return int
	 */
	public int updateTaskStatusQueryTooLargeFail(ApiReportTask task) throws InternalException;
	
	/**
	 * 修改任务状态为失败重试中，如果超过重试次数，直接置为失败
	 * @param task
	 * @return int
	 */
	public int updateTaskStatusFailIncrementRetry(ApiReportTask task) throws InternalException;
	
	/**
	 * 修改任务状态为超时重试中，如果超过重试次数，直接置为失败
	 * @param task
	 * @return int
	 */
	public int updateTaskStatusTimeoutIncrementRetry(ApiReportTask task) throws InternalException;
	
}
