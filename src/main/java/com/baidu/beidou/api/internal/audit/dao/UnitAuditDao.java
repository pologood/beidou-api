package com.baidu.beidou.api.internal.audit.dao;

import java.util.List;

import com.baidu.beidou.api.internal.audit.vo.UnitMaterialInfo;
import com.baidu.beidou.api.internal.audit.vo.request.QueryBase;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitAudit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitReaudit;
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.cprounit.bo.UnitPreMater;

public interface UnitAuditDao {
	
	public List<Integer> findAllUserIds(QueryBase query);
	
	public long countAuditUnit(int userId, QueryUnitAudit query);
	
	public List<Unit> findAuditUnit(int userId, QueryUnitAudit query);
	
	public UnitPreMater findUnitPreMater(int userId, long unitId);
	
	public long countReauditUnit(int userId, QueryUnitReaudit query);
	
	public List<Unit> findReauditUnit(int userId, QueryUnitReaudit query);
	
    /**
     * Function: 获取创意物料信息，包含创意的mcId和mcVersionId
     * 
     * @author genglei01
     * @param userId userId
     * @param unitId unitId
     * @return UnitMaterialInfo
     */
    public UnitMaterialInfo findUnitMaterialInfo(int userId, Long unitId);

}
