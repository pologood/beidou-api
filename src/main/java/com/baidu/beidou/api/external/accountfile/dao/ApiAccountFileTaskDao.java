package com.baidu.beidou.api.external.accountfile.dao;

import java.util.Date;
import java.util.List;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;
import com.baidu.beidou.util.dao.GenericDao;

/**
 * 
 * InterfaceName: ApiAccountFileTaskDao  <br>
 * Function: API用的accountfile表读取DAO接口定义
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public interface ApiAccountFileTaskDao extends GenericDao<ApiAccountFileTask, Long> {

	public List<ApiAccountFileTask> findByUserId(Integer userId);
	
	public List<ApiAccountFileTask> findByFileId(String fileId);
	
	public List<ApiAccountFileTask> findByUserIdAndFileId(Integer userId, String fileId);
	
	public List<ApiAccountFileTask> findByUserIdAndStatus(Integer userId, List<Integer> status);
	
	public List<ApiAccountFileTask> findByStatusAndMachineId(Integer status, String machineId);
	
	public List<ApiAccountFileTask> findByStatusAndMachineIdAndModtime(Integer status, String machineId, Date modtime);
	
	public Integer updateStatus(Integer userId, String fileId, Integer status);
	
	public Integer updateStatusAndMd5(Integer userId, String fileId, Integer status, String md5);
	
	public Integer updateStatusAndHandleTime(Integer userId, String fileId, Integer status);
	
}
