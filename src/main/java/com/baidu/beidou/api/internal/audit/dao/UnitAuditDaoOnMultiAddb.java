package com.baidu.beidou.api.internal.audit.dao;

import java.util.List;
import java.util.Map;

import com.baidu.beidou.cprounit.bo.Unit;

public interface UnitAuditDaoOnMultiAddb {
	
	public Map<Integer, Integer> findAllUnitCnt(List<Integer> pagedUserIds);
	
	public List<Unit> findReauditUnitByUnitIds(String unitIds);

}
