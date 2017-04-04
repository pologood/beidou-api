package com.baidu.beidou.api.internal.audit.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.internal.audit.constant.QueryConstant;
import com.baidu.beidou.api.internal.audit.dao.UnitAuditDao;
import com.baidu.beidou.api.internal.audit.util.UnitAuditFilter;
import com.baidu.beidou.api.internal.audit.util.UnitAuditSql;
import com.baidu.beidou.api.internal.audit.vo.UnitMaterialInfo;
import com.baidu.beidou.api.internal.audit.vo.request.QueryBase;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitAudit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitReaudit;
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.cprounit.bo.UnitPreMater;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.user.constant.UserConstant;
import com.baidu.beidou.util.dao.GenericDaoImpl;
import com.baidu.beidou.util.dao.GenericRowMapping;
import com.baidu.beidou.util.partition.PartID;
import com.baidu.beidou.util.partition.impl.PartKeyBDidImpl;
import com.baidu.beidou.util.partition.strategy.PartitionStrategy;

public class UnitAuditDaoImpl extends GenericDaoImpl<Integer, Long> implements UnitAuditDao {
	
	private static final Log LOG = LogFactory.getLog(UnitAuditDaoImpl.class);
	
	/**
	 * 创意的分表策略
	 */
	private PartitionStrategy strategy = null;
	

	// 复审SQL拼接
	private static final String COND_USTATE = "u.ustate=";
	private static final String COND_USERSHIFENSTATE = "u.ushifenstatid!=";
	private static final String COND_USERID = "u.userid=";
	private static final String COND_USERNAME = "u.username = ";
	// 复审过滤条件
	private static final List<Integer> excludeReauditShifenState = new ArrayList<Integer>();
	static {
		excludeReauditShifenState.add(UserConstant.SHIFEN_STATE_CLOSE);
		excludeReauditShifenState.add(UserConstant.SHIFEN_STATE_DISABLE);
		excludeReauditShifenState.add(UserConstant.SHIFEN_STATE_AUDITING);
	}
		
	public List<Integer> findAllUserIds(QueryBase query) {
		StringBuilder sql = new StringBuilder("select u.userid from beidoucap.useraccount u");
		List<Integer> result = new LinkedList<Integer>();
		
		sql.append(" where ").append(COND_USTATE).append(UserConstant.USER_STATE_NORMAL);
		for (Integer state : excludeReauditShifenState) {
			sql.append(" and ").append(COND_USERSHIFENSTATE).append(state);
		}
		try {
			int queryType = query.getQueryType();
			if (queryType == QueryConstant.QueryUserReaudit.userId) {
				Integer userId = Integer.parseInt(query.getQuery());
				sql.append(" and ").append(COND_USERID).append(userId);
			} else if (queryType == QueryConstant.QueryUserReaudit.userName) {
                sql.append(" and ").append(COND_USERNAME).append("\'").append(query.getQuery()).append("\'");
			}
			
			final List<Integer> response = new LinkedList<Integer>();
			super.findBySql(new GenericRowMapping<Integer>() {
				public Integer mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					response.add(rs.getInt(1));
					return null;
				}
			}, sql.toString(), null, null);
			result.addAll(response);
			
		} catch (Exception e) {
			LOG.error("Param error: queryType=" + query.getQueryType() + ", query=" + query.getQuery(), e);
		}
		return result;
	}
	
	public long countAuditUnit(int userId, QueryUnitAudit query) {
		long count = 0;
		String unit = "unit";
		// 设置hql的参数
		List<Object> params = null;
		PartID part = strategy.getPartitions(new PartKeyBDidImpl(userId));
		
		List<Long> unitIds = query.getUnitIds();
		
		List<List<Long>> pageList = doPage(unitIds, 20000);
		for (List<Long> page : pageList) {
			query.setUnitIds(page);
			
			StringBuilder hql = new StringBuilder();
			
			params = new ArrayList<Object>();
			
			// 加入状态为待审核指定审核员的条件
			hql.append("select count(").append(unit).append(") from ").append(
					part.getPoname()).append(" ").append(unit).append(" where ");
			int index = 0;
			hql.append(unit + ".state = ? ");
			params.add(index, Integer.valueOf(CproUnitConstant.UNIT_STATE_AUDITING));
			index++;
	
			hql.append(" and " + unit + ".material.newAdtradeid != ?");
			params.add(index, Integer.valueOf(CproUnitConstant.UNIT_TRADE_INVALID));
			index++;
	
			UnitAuditSql filter = UnitAuditFilter.getUnitAuditFilter(unit, query);
			hql.append(filter.getSql());
			params.addAll(filter.getParams());
			
			Long pageCount = super.countByCondition(hql.toString(), params.toArray());
			count += pageCount;
		}
		//count 执行完成之后需重新set下，否则后面的findAuditUnit方法unitIds会有问题
		query.setUnitIds(unitIds);
		return count;
	}
	
	public List<Unit> findAuditUnit(int userId, QueryUnitAudit query) {
		String unit = "unit";
		List<Unit> result = new ArrayList<Unit>();
		
		List<Object> params = null;
		PartID part = strategy.getPartitions(new PartKeyBDidImpl(userId));
		
		List<Long> unitIds = query.getUnitIds();
		List<List<Long>> pageList = doPage(unitIds, 20000);
		
		for (List<Long> page : pageList) {
			query.setUnitIds(page);
			
			StringBuilder hql = new StringBuilder();
			
			// 设置hql的参数
			params = new ArrayList<Object>();
	
			
			// 加入状态为待审核指定审核员的条件
			hql.append("select ").append(unit).append(" from ").append(part.getPoname()).append(" ").append(unit)
					.append(" left join "+unit+".group").append(" where ");
			int index = 0;
			hql.append(unit + ".state = ? ");
			params.add(index, Integer.valueOf(CproUnitConstant.UNIT_STATE_AUDITING));
			index++;
	
			hql.append(" and " + unit + ".material.newAdtradeid != ?");
			params.add(index, Integer.valueOf(CproUnitConstant.UNIT_TRADE_INVALID));
			index++;
	
			UnitAuditSql filter = UnitAuditFilter.getUnitAuditFilter(unit, query);
			hql.append(filter.getSql());
			params.addAll(filter.getParams());
			
			hql.append(" order by " + unit + ".group.groupId asc, ");
			hql.append(unit + ".chaTime asc, ");
			hql.append(unit + ".material.wuliaoType asc");
			List<Unit> retList = super.findByHql(Unit.class, query.getPage(), query.getPageSize() / pageList.size(), 
					hql.toString(), params.toArray());
			result.addAll(retList);
		}
		return result;
	}
	
	
	public UnitPreMater findUnitPreMater(int userId, long unitId) {
		PartID part = strategy.getPartitions(new PartKeyBDidImpl(userId));
		
		String prematerTable = "com.baidu.beidou.cprounit.bo.UnitPreMater" + part.getId();
		StringBuilder prematerHql = new StringBuilder(); 
		prematerHql.append("select pre from ").append(prematerTable).append(" pre where id=")
				.append(unitId);
		List<UnitPreMater> prematerList = super.findByHql(UnitPreMater.class, prematerHql.toString());
		
		if (CollectionUtils.isNotEmpty(prematerList)) {
			return prematerList.get(0);
		}
		
		return null;
	}
	
	public long countReauditUnit(int userId, QueryUnitReaudit query) {
		String unit = "unit";
		StringBuilder hql = new StringBuilder();
		// 设置hql的参数
		List<Object> params = null;
		params = new ArrayList<Object>();

		PartID part = strategy.getPartitions(new PartKeyBDidImpl(userId));
		// 加入状态为待审核指定审核员的条件
		hql.append("select count(").append(unit).append(") from ").append(
				part.getPoname()).append(" ").append(unit)
				.append(" left join " + unit + ".auditing").append(" where ");
		int index = 0;
		hql.append(unit + ".state != ? AND " + unit + ".state != ? ");
		params.add(index, Integer.valueOf(CproUnitConstant.UNIT_STATE_AUDITING));
		index++;
		params.add(index, Integer.valueOf(CproUnitConstant.UNIT_STATE_DELETE));
		index++;
		
		UnitAuditSql filter = UnitAuditFilter.getUnitReauditFilter(unit, query);
		hql.append(filter.getSql());
		params.addAll(filter.getParams());
		
		return super.countByCondition(hql.toString(), params.toArray());
	}
	
	public List<Unit> findReauditUnit(int userId, QueryUnitReaudit query) {
		String unit = "unit";
		StringBuilder hql = new StringBuilder();
		// 设置hql的参数
		List<Object> params = null;
		params = new ArrayList<Object>();

		PartID part = strategy.getPartitions(new PartKeyBDidImpl(userId));
		// 加入状态为待审核指定审核员的条件
		hql.append("select ").append(unit).append(" from ").append(
				part.getPoname()).append(" ").append(unit)
				.append(" left join " + unit + ".auditing").append(" where ");
		int index = 0;
		hql.append(unit + ".state != ? AND " + unit + ".state != ? ");
		params.add(index, Integer.valueOf(CproUnitConstant.UNIT_STATE_AUDITING));
		index++;
		params.add(index, Integer.valueOf(CproUnitConstant.UNIT_STATE_DELETE));
		index++;
		
		UnitAuditSql filter = UnitAuditFilter.getUnitReauditFilter(unit, query);
		hql.append(filter.getSql());
		params.addAll(filter.getParams());
		
		hql.append(" order by " + unit + ".auditTime desc");
		List<Unit> result = super.findByHql(Unit.class, query.getPage(), query.getPageSize(), 
				hql.toString(), params.toArray());
		return result;
	}

	private <T extends Object> List<List<T>> doPage(List<T> list, int pageSize) {
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}

		int pageNum = (list.size() / pageSize) + 1;
		if ((list.size() % pageSize) == 0) {
			pageNum -= 1;
		}
		List<List<T>> result = new ArrayList<List<T>>(pageNum);
		for (int i = 0; i < pageNum; i++) {
			int from = i * pageSize;
			int to = (i + 1) * pageSize;
			if (to > list.size()) {
				to = list.size();
			}
			List<T> item = list.subList(from, to);
			result.add(item);
		}

		return result;
	}

    /**
     * Function: 获取创意物料信息，包含创意的mcId和mcVersionId
     * 
     * @author genglei01
     * @param userId userId
     * @param unitId unitId
     * @return UnitMaterialInfo
     */
    public UnitMaterialInfo findUnitMaterialInfo(int userId, Long unitId) {
        PartID partId = strategy.getPartitions(new PartKeyBDidImpl(userId));
        String sql = "SELECT id, userid, mcId, mcVersionId, wuliaoType, targetUrl, file_src_md5 "
                + "FROM beidou.cprounitmater" + partId.getId() + " WHERE id=?";
        List<Map<String, Object>> result = super.findBySql(sql, new Object[] { unitId }, new int[] { Types.BIGINT });
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        UnitMaterialInfo info = new UnitMaterialInfo();
        info.setUnitId((Long) result.get(0).get("id"));
        info.setUserId((Integer) result.get(0).get("userid"));
        info.setMcId((Long) result.get(0).get("mcId"));
        info.setVersionId((Integer) result.get(0).get("mcVersionId"));
        info.setTargetUrl((String) result.get(0).get("targetUrl"));
        info.setFileSrcMd5((String) result.get(0).get("file_src_md5"));
        info.setWuliaoType((Integer) result.get(0).get("wuliaoType"));
        return info;
    }

	public PartitionStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(PartitionStrategy strategy) {
		this.strategy = strategy;
	}
	
}
