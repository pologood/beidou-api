package com.baidu.beidou.api.external.accountfile.service;

import java.util.Date;
import java.util.List;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;

/**
 * 
 * InterfaceName: ApiAccountFileTaskMgr  <br>
 * Function: API用的accountfile服务接口定义
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public interface ApiAccountFileTaskMgr {

	public List<ApiAccountFileTask> findByUserId(Integer userId);
	
	public List<ApiAccountFileTask> findByFileId(String fileId);
	
	public List<ApiAccountFileTask> findByUserIdAndFileId(Integer userId, String fileId);
	
	public List<ApiAccountFileTask> findByUserIdAndStatus(Integer userId, List<Integer> status);
	
	public List<ApiAccountFileTask> findByStatusAndMachineId(Integer status, String machineId);
	
	public List<ApiAccountFileTask> findByStatusAndMachineIdAndModtime(Integer status, String machineId, Date modtime);
	
	public Integer updateStatus(Integer userId, String fileId, Integer status);
	
	public Integer updateStatusAndMd5(Integer userId, String fileId, Integer status, String md5);
	
	public ApiAccountFileTask addAccountFileTask(ApiAccountFileTask task);
	
	public Integer updateStatusAndHandleTime(Integer userId, String fileId, Integer status);
	
}
