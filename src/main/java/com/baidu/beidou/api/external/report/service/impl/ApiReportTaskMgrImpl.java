package com.baidu.beidou.api.external.report.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.report.bo.ApiReportTask;
import com.baidu.beidou.api.external.report.constant.ApiReportTaskConstant;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.dao.ApiReportTaskDao;
import com.baidu.beidou.api.external.report.service.ApiReportTaskMgr;
import com.baidu.beidou.exception.InternalException;


/**
 * 
 * ClassName: ApiReportTaskMgrImpl
 * Function: 报告任务表api_report服务实现
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public class ApiReportTaskMgrImpl implements ApiReportTaskMgr{
	
	private static final Log log = LogFactory.getLog(ApiReportTaskMgrImpl.class);
	
	private ApiReportTaskDao apiReportTaskDao;

	/**
	 * 根据id查找ApiReportTask
	 * @param id 
	 * @return ApiReportTask
	 */
	public ApiReportTask findTasksById(long id) {
		List<ApiReportTask> result = apiReportTaskDao.findApiReportTasksById(id);
		if(CollectionUtils.isEmpty(result)){
			log.warn("do not find task id=[" + id + "]");
			return null;
		}
		if(result.size() > 1){
			log.error("find multi task id=[" + id + "]");
			return null;
		}
		return result.get(0);
	}
	
	/**
	 * 根据report id查找ApiReportTask
	 * @param reportid 
	 * @return ApiReportTask
	 */
	public ApiReportTask findTasksByReportId(String reportid) {
		List<ApiReportTask> result = apiReportTaskDao.findApiReportTasksByReportId(reportid);
		if(CollectionUtils.isEmpty(result)){
			log.warn("do not find task reportid=[" + reportid + "]");
			return null;
		}
		if(result.size() > 1){
			log.error("find multi task reportid=[" + reportid + "]");
			return null;
		}
		return result.get(0);
	}
	
	/**
	 * 根据report id查找本机处理的超时ApiReportTask列表
	 * @param reportid 
	 * @param machineid
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findTimeoutTasksByMachineid(Date minTime, String machineid) {
		List<ApiReportTask> resultDoing = apiReportTaskDao.findApiReportTimeoutTasksByMachineid(minTime, ApiReportTaskConstant.TASK_STATUS_DOING, machineid);
		List<ApiReportTask> resultNew = apiReportTaskDao.findApiReportTimeoutTasksByMachineid(minTime, ApiReportTaskConstant.TASK_STATUS_NEW, machineid);
		List<ApiReportTask> result = new ArrayList<ApiReportTask>();
		result.addAll(resultDoing);
		result.addAll(resultNew);
		if(CollectionUtils.isEmpty(result)){
			return new ArrayList<ApiReportTask>();
		}
		return result;
	}
	
	/**
	 * 查找本机失败重试的任务
	 * @param machineid
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findFailTasksByMachieid(String machineid) {
		List<Integer> status = new ArrayList<Integer>();
		status.add(ApiReportTaskConstant.TASK_STATUS_FAIL_WITH_RETRY);
		List<ApiReportTask> result = apiReportTaskDao.findApiReportTasksByStatusAndMachineid(status, machineid);
		if(CollectionUtils.isEmpty(result)){
			return new ArrayList<ApiReportTask>();
		}
		return result;
	}
	
	/**
	 * 根据机器id和状态查找任务
	 * @param status
	 * @param machineid
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findTasksByStatusAndMachieid(List<Integer> status, String machineid) {
		List<ApiReportTask> result = apiReportTaskDao.findApiReportTasksByStatusAndMachineid(status, machineid);
		if(CollectionUtils.isEmpty(result)){
			return new ArrayList<ApiReportTask>();
		}
		return result;
	}
	
	/**
	 * 根据用户、机器id和状态查找任务
	 * @param userid
	 * @param status
	 * @param machineid
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findTasksByUseridAndStatusAndMachieid(int userid, List<Integer> status, String machineid) {
		List<ApiReportTask> result = apiReportTaskDao.findApiReportTasksByUseridAndStatusAndMachineid(userid, status, machineid);
		if(CollectionUtils.isEmpty(result)){
			return new ArrayList<ApiReportTask>();
		}
		return result;
	}
	
	/**
	 * 插入新的任务
	 * @param task
	 * @return long 主键id
	 */
	public long addTask(ApiReportTask task) {
		Date now = new Date();
		return apiReportTaskDao.addApiReportTask(task.getQueryparam(), 
				ApiReportTaskConstant.TASK_STATUS_NEW, 
				task.getUserid(), 
				task.getReportid(), 
				task.getMachineid(), 
				task.getPerformancedata(),
				task.getIszip(), 
				task.getOpuser(), 
				now, 
				now);
	}
	
	/**
	 * 修改任务状态为处理中
	 * @param task
	 * @return int
	 */
	public int updateTaskStatusDoing(ApiReportTask task) throws InternalException{
		Date modtime = new Date();
		int res = apiReportTaskDao.updateApiReportTaskStatus(task.getStatus(), ApiReportTaskConstant.TASK_STATUS_DOING, modtime, task.getId());
		if(res == 0){
			log.error("updateTaskStatusDoing fail task=[" + task + "]");
			throw new InternalException("update task status doing fail");
		} else {
			task.setStatus(ApiReportTaskConstant.TASK_STATUS_DOING);
			task.setModtime(modtime);
		}
		return res;
	}
	
	/**
	 * 修改任务状态为成功
	 * @param task
	 * @return int
	 */
	public int updateTaskStatusOk(ApiReportTask task) throws InternalException{
		Date modtime = new Date();
		int res = apiReportTaskDao.updateApiReportTaskStatus(task.getStatus(), ApiReportTaskConstant.TASK_STATUS_OK, modtime, task.getId());
		if(res == 0){
			log.error("updateTaskStatusOk fail task=[" + task + "]");
			throw new InternalException("update task status ok fail");
		} else {
			task.setStatus(ApiReportTaskConstant.TASK_STATUS_OK);
			task.setModtime(modtime);
		}
		return res;
	}
	
	/**
	 * 修改任务状态为失败
	 * @param task
	 * @return int
	 */
	public int updateTaskStatusFail(ApiReportTask task) throws InternalException{
		Date modtime = new Date();
		int res = apiReportTaskDao.updateApiReportTaskStatus(task.getStatus(), ApiReportTaskConstant.TASK_STATUS_FAIL, modtime, task.getId());
		if(res == 0){
			log.error("updateTaskStatusFail fail task=[" + task + "]");
			throw new InternalException("update task status fail fail");
		} else {
			task.setStatus(ApiReportTaskConstant.TASK_STATUS_FAIL);
			task.setModtime(modtime);
		}
		return res;
	}
	
	/**
	 * 修改任务状态为查询范围过大失败
	 * @param task
	 * @return int
	 */
	public int updateTaskStatusQueryTooLargeFail(ApiReportTask task) throws InternalException {
		Date modtime = new Date();
		int res = apiReportTaskDao.updateApiReportTaskStatus(task.getStatus(), ApiReportTaskConstant.TASK_STATUS_QUERY_TOO_LARGE_FAIL, modtime, task.getId());
		if(res == 0){
			log.error("updateTaskStatusQueryTooLargeFail fail task=[" + task + "]");
			throw new InternalException("update task status query too large fail fail");
		} else {
			task.setStatus(ApiReportTaskConstant.TASK_STATUS_QUERY_TOO_LARGE_FAIL);
			task.setModtime(modtime);
		}
		return res;
	}
	
	/**
	 * 修改任务状态为失败重试中，如果超过重试次数，直接置为失败
	 * @param task
	 * @return int
	 */
	public int updateTaskStatusFailIncrementRetry(ApiReportTask task) throws InternalException{
		if(task.getRetry() >= ReportWebConstants.MAX_RETRY){
			return updateTaskStatusFail(task);
		}
		Date modtime = new Date();
		int res = apiReportTaskDao.updateApiReportTaskStatusIncrRetry(task.getStatus(), ApiReportTaskConstant.TASK_STATUS_FAIL_WITH_RETRY, modtime, task.getId());
		if(res == 0){
			log.error("updateTaskStatusFailIncrementRetry fail task=[" + task + "]");
			throw new InternalException("update task status fail to incr retry fail");
		} else {
			task.setStatus(ApiReportTaskConstant.TASK_STATUS_FAIL_WITH_RETRY);
			task.setModtime(modtime);
			task.setRetry(task.getRetry()+1);
		}
		return res;
	}
	
	/**
	 * 修改任务状态为超时重试中，如果超过重试次数，直接置为失败
	 * @param task
	 * @return int
	 */
	public int updateTaskStatusTimeoutIncrementRetry(ApiReportTask task) throws InternalException{
		if(task.getRetry() >= ReportWebConstants.MAX_RETRY){
			return updateTaskStatusFail(task);
		}
		Date modtime = new Date();
		int res = apiReportTaskDao.updateApiReportTaskStatusIncrRetry(task.getStatus(), ApiReportTaskConstant.TASK_STATUS_TIMEOUT_WITH_RETRY, modtime, task.getId());
		if(res == 0){
			log.error("updateTaskStatusTimeoutIncrementRetry fail task=[" + task + "]");
			throw new InternalException("update task status timeout to incr retry fail");
		} else {
			task.setStatus(ApiReportTaskConstant.TASK_STATUS_TIMEOUT_WITH_RETRY);
			task.setModtime(modtime);
			task.setRetry(task.getRetry()+1);
		}
		return res;
	}

	public ApiReportTaskDao getApiReportTaskDao() {
		return apiReportTaskDao;
	}

	public void setApiReportTaskDao(ApiReportTaskDao apiReportTaskDao) {
		this.apiReportTaskDao = apiReportTaskDao;
	}
	
}
