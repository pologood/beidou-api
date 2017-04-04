package com.baidu.beidou.api.external.accountfile.service.impl;

import java.util.Date;
import java.util.List;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;
import com.baidu.beidou.api.external.accountfile.dao.ApiAccountFileTaskDao;
import com.baidu.beidou.api.external.accountfile.service.ApiAccountFileTaskMgr;

/**
 * 
 * ClassName: ApiAccountFileTaskMgrImpl  <br>
 * Function: API用的accountfile服务类
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class ApiAccountFileTaskMgrImpl implements ApiAccountFileTaskMgr{
	
	private ApiAccountFileTaskDao apiAccountFileTaskDao;
	
	public List<ApiAccountFileTask> findByUserId(Integer userId){
		return apiAccountFileTaskDao.findByUserId(userId);
	}
	
	public List<ApiAccountFileTask> findByFileId(String fileId){
		return apiAccountFileTaskDao.findByFileId(fileId);
	}
	
	public List<ApiAccountFileTask> findByUserIdAndFileId(Integer userId, String fileId){
		return apiAccountFileTaskDao.findByUserIdAndFileId(userId, fileId);
	}
	
	public List<ApiAccountFileTask> findByUserIdAndStatus(Integer userId, List<Integer> status){
		return apiAccountFileTaskDao.findByUserIdAndStatus(userId, status);
	}
	
	public List<ApiAccountFileTask> findByStatusAndMachineId(Integer status, String machineId){
		return apiAccountFileTaskDao.findByStatusAndMachineId(status, machineId);
	}
	
	public List<ApiAccountFileTask> findByStatusAndMachineIdAndModtime(Integer status, String machineId, Date modtime){
		return apiAccountFileTaskDao.findByStatusAndMachineIdAndModtime(status, machineId, modtime);
	}
	
	public Integer updateStatus(Integer userId, String fileId, Integer status){
		return apiAccountFileTaskDao.updateStatus(userId, fileId, status);
	}
	
	public Integer updateStatusAndMd5(Integer userId, String fileId, Integer status, String md5){
		return apiAccountFileTaskDao.updateStatusAndMd5(userId, fileId, status, md5);
	}
	
	public ApiAccountFileTask addAccountFileTask(ApiAccountFileTask task){
		ApiAccountFileTask persistentTask = apiAccountFileTaskDao.makePersistent(task);
		return persistentTask;
	}
	
	@Override
	public Integer updateStatusAndHandleTime(Integer userId, String fileId,
			Integer status) {
		return apiAccountFileTaskDao.updateStatusAndHandleTime(userId, fileId, status);
	}

	public ApiAccountFileTaskDao getApiAccountFileTaskDao() {
		return apiAccountFileTaskDao;
	}

	public void setApiAccountFileTaskDao(ApiAccountFileTaskDao apiAccountFileTaskDao) {
		this.apiAccountFileTaskDao = apiAccountFileTaskDao;
	}


}
