package com.baidu.beidou.api.external.accountfile.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.baidu.beidou.api.external.accountfile.bo.ApiAccountFileTask;
import com.baidu.beidou.api.external.accountfile.dao.ApiAccountFileTaskDao;
import com.baidu.beidou.util.dao.GenericDaoImpl;

/**
 * 
 * ClassName: ApiAccountFileTaskDaoImpl  <br>
 * Function: API用的accountfile表读取DAO实现
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 26, 2012
 */
public class ApiAccountFileTaskDaoImpl extends GenericDaoImpl<ApiAccountFileTask, Long> implements ApiAccountFileTaskDao {

	public List<ApiAccountFileTask> findByUserId(Integer userId){
		if(userId == null){
			return new ArrayList<ApiAccountFileTask>(0);
		}
		return super.findByCriteria(Order.desc("userid"), 
				Restrictions.eq("userid", userId));
	}
	
	public List<ApiAccountFileTask> findByFileId(String fileId){
		if(fileId == null){
			return new ArrayList<ApiAccountFileTask>(0);
		}
		return super.findByCriteria(Order.desc("fileid"), 
				Restrictions.eq("fileid", fileId));
	}
	
	public List<ApiAccountFileTask> findByUserIdAndFileId(Integer userId, String fileId){
		if(fileId == null){
			return new ArrayList<ApiAccountFileTask>(0);
		}
		return super.findByCriteria(Order.desc("fileid"), 
				Restrictions.eq("userid", userId),
				Restrictions.eq("fileid", fileId));
	}
	
	public List<ApiAccountFileTask> findByUserIdAndStatus(Integer userId, List<Integer> status){
		if(userId == null || status == null ){
			return new ArrayList<ApiAccountFileTask>(0);
		}
		return super.findByCriteria(Order.desc("fileid"), 
				Restrictions.eq("userid", userId),
				Restrictions.in("status", status));
	}
	
	public List<ApiAccountFileTask> findByStatusAndMachineId(Integer status, String machineId){
		if(status == null || machineId == null){
			return new ArrayList<ApiAccountFileTask>(0);
		}
		return super.findByCriteria(Order.desc("fileid"), 
				Restrictions.eq("status", status),
				Restrictions.eq("machineid", machineId));
	}
	
	public List<ApiAccountFileTask> findByStatusAndMachineIdAndModtime(Integer status, String machineId, Date modtime){
		if(status == null || machineId == null || modtime == null){
			return new ArrayList<ApiAccountFileTask>(0);
		}
		return super.findByCriteria(Order.desc("fileid"), 
				Restrictions.eq("status", status),
				Restrictions.eq("machineid", machineId),
				Restrictions.lt("modtime", modtime));
	}
	
	public Integer updateStatus(Integer userId, String fileId, Integer status){
		String hql = "update ApiAccountFileTask t set t.status = ?,  t.modtime = now() where t.userid = ? and t.fileid = ?";
		Object[] args = new Object[3];
		args[0] = status;
		args[1] = userId;
		args[2] = fileId;
		return super.execute(hql, args);
	}
	
	public Integer updateStatusAndMd5(Integer userId, String fileId, Integer status, String md5){
		String hql = "update ApiAccountFileTask t set t.status = ?,  t.md5 = ?, t.modtime = now() where t.userid = ? and t.fileid = ?";
		Object[] args = new Object[4];
		args[0] = status;
		args[1] = md5;
		args[2] = userId;
		args[3] = fileId;
		return super.execute(hql, args);
	}

//	@Override
//	public ApiAccountFileTask addAccountFileTask(ApiAccountFileTask task) {
//		String sql = "insert into beidoureport.api_accountfile(userid,planids,status,fileid,md5,machineid,format,opuser,addtime,modtime,retry,handletime) values(?,?,?,?,?,?,?,?,now(),now(),?,now())";
//		Object[] args = new Object[9];
//		args[0] = task.getUserid();
//		args[1] = task.getPlanids();
//		args[2] = task.getStatus();
//		args[3] = task.getFileid();
//		args[4] = task.getMd5();
//		args[5] = task.getMachineid();
//		args[6] = task.getFormat();
//		args[7] = task.getOpuser();
//		args[8] = task.getRetry();
//		
//		super.executeBySql(sql, args);
//		return task;
//	}
	
	public Integer updateStatusAndHandleTime(Integer userId, String fileId, Integer status) {
		String hql = "update ApiAccountFileTask t set t.status = ?,t.handletime = now(), t.modtime = now() where t.userid = ? and t.fileid = ?";
		Object[] args = new Object[3];
		args[0] = status;
		args[1] = userId;
		args[2] = fileId;
		return super.execute(hql, args);
	}
}
