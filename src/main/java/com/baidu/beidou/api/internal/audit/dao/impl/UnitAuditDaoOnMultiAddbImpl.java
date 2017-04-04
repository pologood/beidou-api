package com.baidu.beidou.api.internal.audit.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.internal.audit.dao.UnitAuditDaoOnMultiAddb;
import com.baidu.beidou.api.internal.audit.util.UnitAuditFilter;
import com.baidu.beidou.api.internal.audit.util.UnitAuditSql;
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.util.StringUtils;
import com.baidu.beidou.util.dao.GenericRowMapping;
import com.baidu.beidou.util.dao.MultiDataSourceDaoImpl;
import com.baidu.beidou.util.partition.PartID;
import com.baidu.beidou.util.partition.strategy.PartitionStrategy;

public class UnitAuditDaoOnMultiAddbImpl extends MultiDataSourceDaoImpl<Integer> implements UnitAuditDaoOnMultiAddb {
	
	/**
	 * 创意的分表策略
	 */
	private PartitionStrategy strategy = null;

	public Map<Integer, Integer> findAllUnitCnt(List<Integer> pagedUserIds) {
		Map<Integer, Integer> unitMap = new HashMap<Integer, Integer>();
		if (CollectionUtils.isEmpty(pagedUserIds)) {
			return unitMap;
		}

		List<PartID> parts = strategy.getAllPartitions();
		for (PartID part : parts) {
			findAllUnitCntOneTable(part.getTablename(), pagedUserIds,	unitMap);
		}
		
		return unitMap;
	}
	
	private void findAllUnitCntOneTable(String poname,
			List<Integer> pagedUserIds, final Map<Integer, Integer> unitMap) {
		if (unitMap == null || CollectionUtils.isEmpty(pagedUserIds)) {
			return;
		}
		// 加入状态为待审核指定审核员的条件
		StringBuilder sql = new StringBuilder("select s.uid, count(id) from "	+ poname + " s where ");
		int index = 0;

		// 设置hql的参数
		List<Object> params = new ArrayList<Object>();
		int[] types = new int[2];

		sql.append(" s.state != ? and s.state != ? ");
		params.add(index, Integer.valueOf(CproUnitConstant.UNIT_STATE_AUDITING));
		types[index] = Types.INTEGER;
		index++;
		params.add(index, Integer.valueOf(CproUnitConstant.UNIT_STATE_DELETE));
		types[index] = Types.INTEGER;
		index++;

		sql.append(" and s.uid in(").append(StringUtils.makeStrCollection(pagedUserIds)).append(')');
		this.appendUserIdRouting(sql, "s.uid");

		sql.append(" group by s.uid");

		super.findBySql(new GenericRowMapping<Integer>() {
			
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				int userid = rs.getInt(1);
				int cnt = rs.getInt(2);
				Integer curCnt = unitMap.get(userid);
				if (curCnt == null) {
					curCnt = cnt;
				} else {
					curCnt += cnt;
				}
				unitMap.put(userid, cnt);
				return null;
			}

		}, sql.toString(), params.toArray(), types);
	}
	
	public List<Unit> findReauditUnitByUnitIds(String unitIds) {
		List<Unit> result = new ArrayList<Unit>();
		List<PartID> parts = strategy.getAllPartitions();
		for (PartID part : parts) {
			result.addAll(findReauditUnitByUnitIdsOneTable(part, unitIds));
		}
		
		return result;
	}
	
	private List<Unit> findReauditUnitByUnitIdsOneTable(PartID part, String unitIds) {
		String unit = "unit";
		StringBuilder hql = new StringBuilder();
		// 设置hql的参数
		List<Object> params = null;
		params = new ArrayList<Object>();

		// 加入状态为待审核指定审核员的条件
		hql.append("select ").append(unit).append(" from ").append(
				part.getPoname()).append(" ").append(unit).append(" left join " + unit + ".auditing")
				.append(" where ");
		int index = 0;
		hql.append(unit + ".state != ? AND " + unit + ".state != ? ");
		params.add(index, Integer.valueOf(CproUnitConstant.UNIT_STATE_AUDITING));
		index++;
		params.add(index, Integer.valueOf(CproUnitConstant.UNIT_STATE_DELETE));
		index++;
		
		UnitAuditSql filter = UnitAuditFilter.getUnitReauditFilter(unit, unitIds);
		hql.append(filter.getSql());
		params.addAll(filter.getParams());
		
		hql.append(" order by " + unit + ".auditTime desc");
		List<Unit> result = super.findByHql(Unit.class, hql.toString(), params.toArray());
		return result;
	}

	public PartitionStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(PartitionStrategy strategy) {
		this.strategy = strategy;
	}
}
