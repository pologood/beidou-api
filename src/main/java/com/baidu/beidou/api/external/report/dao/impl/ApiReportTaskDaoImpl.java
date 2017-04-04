package com.baidu.beidou.api.external.report.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baidu.beidou.util.StringUtils;
import com.baidu.beidou.api.external.report.bo.ApiReportTask;
import com.baidu.beidou.api.external.report.dao.ApiReportTaskDao;
import com.baidu.beidou.util.dao.BaseDaoSupport;
import com.baidu.beidou.util.dao.GenericRowMapping;

/**
 * 
 * ClassName: ApiReportDaoImpl
 * Function: API报告表DAO接口实现
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 4, 2012
 */
public class ApiReportTaskDaoImpl extends BaseDaoSupport implements ApiReportTaskDao{
	
	/**
	 * table name constant
	 */
	private static final String TABLE_NAME_APIREPORT = "beidoureport.api_report";

	/**
	 * TaskRowMapping
	 */
	private class TaskRowMapping implements GenericRowMapping<ApiReportTask> {
		public ApiReportTask mapRow(ResultSet rs, int rowNum) throws SQLException {
			ApiReportTask result = new ApiReportTask();
			result.setId(rs.getLong(1));
			result.setQueryparam(rs.getString(2));
			result.setStatus(rs.getInt(3));
			result.setUserid(rs.getInt(4));
			result.setReportid(rs.getString(5));
			result.setMachineid(rs.getString(6));
			result.setPerformancedata(rs.getInt(7));
			result.setIszip(rs.getInt(8));
			result.setOpuser(rs.getInt(9));
			result.setAddtime(rs.getDate(10));
			result.setModtime(rs.getDate(11));
			result.setRetry(rs.getInt(12));
			return result;
		}
	}
	
	/**
	 * 根据id查找ApiReportTask
	 * @param id 
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findApiReportTasksById(long id) {
		return super.findBySql(
						new TaskRowMapping(),
						"select id, queryparam, status, userid, reportid, machineid, performancedata, iszip, opuser, addtime, modtime, retry from " + 
						TABLE_NAME_APIREPORT +
						" where id=? ",
						new Object[] { id },
						new int[] { Types.BIGINT });
	}
	
	/**
	 * 根据report id查找ApiReportTask
	 * @param reportid 
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findApiReportTasksByReportId(String reportid) {
		return super.findBySql(
						new TaskRowMapping(),
						"select id, queryparam, status, userid, reportid, machineid, performancedata, iszip, opuser, addtime, modtime, retry from " + 
						TABLE_NAME_APIREPORT +
						" where reportid=? ",
						new Object[] { reportid },
						new int[] { Types.VARCHAR });
	}
	
	/**
	 * 根据状态查找ApiReportTask
	 * @param status 
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findApiReportTasksByStatus(int status) {
		return super.findBySql(
						new TaskRowMapping(),
						"select id, queryparam, status, userid, reportid, machineid, performancedata, iszip, opuser, addtime, modtime, retry from " + 
						TABLE_NAME_APIREPORT +
						" where status=? ",
						new Object[] { status },
						new int[] { Types.INTEGER });
	}
	
	/**
	 * 根据状态和机器名查找ApiReportTask
	 * @param status 
	 * @param machineid 
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findApiReportTasksByStatusAndMachineid(List<Integer> status, String machineid){
		String statusList = StringUtils.makeStrFromCollection(status, ",");
		return super.findBySql(
						new TaskRowMapping(),
						"select id, queryparam, status, userid, reportid, machineid, performancedata, iszip, opuser, addtime, modtime, retry from " + 
						TABLE_NAME_APIREPORT +
						" where status in (" + statusList + ") and machineid=?",
						new Object[] { machineid },
						new int[] { Types.VARCHAR });
	}
	
	/**
	 * 根据用户、状态和机器名查找ApiReportTask
	 * @param userid
	 * @param status 
	 * @param machineid 
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findApiReportTasksByUseridAndStatusAndMachineid(int userid, List<Integer> status, String machineid){
		String statusList = StringUtils.makeStrFromCollection(status, ",");
		return super.findBySql(
						new TaskRowMapping(),
						"select id, queryparam, status, userid, reportid, machineid, performancedata, iszip, opuser, addtime, modtime, retry from " + 
						TABLE_NAME_APIREPORT +
						" where userid= ? and status in (" + statusList + ") and machineid=?",
						new Object[] { userid, machineid },
						new int[] { Types.INTEGER, Types.VARCHAR });
	}
	
	/**
	 * 查询某机器下某状态下超时的任务
	 * @param minTime optime在这个时间之前
	 * @param status  任务状态
	 * @param machineid 机器id
	 * @return List<ApiReportTask>
	 */
	public List<ApiReportTask> findApiReportTimeoutTasksByMachineid(Date minTime, int status, String machineid) {
		return super.findBySql(
						new TaskRowMapping(),
						"select id, queryparam, status, userid, reportid, machineid, performancedata, iszip, opuser, addtime, modtime, retry from " + 
						TABLE_NAME_APIREPORT +
						" where modtime<? and status=? and machineid=?",
						new Object[] { minTime, status, machineid},
						new int[] { Types.TIMESTAMP, Types.INTEGER, Types.VARCHAR});
	}
	
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
			int opuser, Date addtime, Date modtime) {
		Number id = super.insertBySql(
				"insert into " + TABLE_NAME_APIREPORT + "(queryparam, status, userid, reportid, machineid, performancedata, iszip, opuser, addtime, modtime, retry) values(?,?,?,?,?,?,?,?,?,?,?)", 
						new Object[] { queryparam, status, userid, reportid, machineid, performancedata, iszip, opuser, addtime, modtime, 0},
				new int[] { Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.TIMESTAMP,Types.TIMESTAMP, Types.INTEGER});
		return id.longValue();
	}
	
	/**
	 * 根据id修改任务状态
	 * @param oldStatus
	 * @param newStatus
	 * @param modtime
	 * @param id
	 * @return int
	 */
	public int updateApiReportTaskStatus(int oldStatus, int newStatus, Date modtime, long id) {
//		List<Map<String, Object>> result = super.findBySql(
//				"select id from " + TABLE_NAME_APIREPORT + " where id=? and status=? for update",
//				new Object[] { id, oldStatus },
//				new int[] { Types.INTEGER, Types.INTEGER });
//		if (result == null || result.isEmpty()) {
//			return 0;
//		} else {
			return super.executeBySql(
					"update " + TABLE_NAME_APIREPORT + "  set status=?, modtime=? where id=?",
					new Object[] { newStatus, modtime, id },
					new int[] { Types.INTEGER, Types.TIMESTAMP, Types.BIGINT });
//		}
	}
	
	/**
	 * 根据id修改任务状态，并且递增retry次数+1与修改时间
	 * @param oldStatus
	 * @param newStatus
	 * @param modtime
	 * @param id
	 * @return int
	 */
	public int updateApiReportTaskStatusIncrRetry(int oldStatus, int newStatus, Date modtime, long id) {
		List<Map<String, Object>> result = super.findBySql(
				"select id from " + TABLE_NAME_APIREPORT + " where id=? and status=? for update",
				new Object[] { id, oldStatus },
				new int[] { Types.INTEGER, Types.INTEGER });
		if (result == null || result.isEmpty()) {
			return 0;
		} else {
			return super.executeBySql(
					"update " + TABLE_NAME_APIREPORT + "  set status=?, modtime=?, retry=retry+1 where id=?",
					new Object[] { newStatus, modtime, id },
					new int[] { Types.INTEGER, Types.TIMESTAMP, Types.BIGINT });
		}
	}
	
}
