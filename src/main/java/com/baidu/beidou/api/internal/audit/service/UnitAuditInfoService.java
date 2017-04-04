package com.baidu.beidou.api.internal.audit.service;

import java.util.List;

import com.baidu.beidou.api.internal.audit.vo.TemplateElementUrlVo;
import com.baidu.beidou.api.internal.audit.vo.UnitAuditInfo;
import com.baidu.beidou.api.internal.audit.vo.UnitMaterialInfo;
import com.baidu.beidou.api.internal.audit.vo.UnitReauditInfo;
import com.baidu.beidou.api.internal.audit.vo.UnitResponse;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitAudit;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitReaudit;


public interface UnitAuditInfoService {
	
	public long countAuditUnit(Integer userId, QueryUnitAudit query);
	
	public List<UnitAuditInfo> updateUnitStateAndGetUnitAuditList(Integer userId, QueryUnitAudit query);
	
	public long countReauditUnit(Integer userId, QueryUnitReaudit query);
	
	public List<UnitReauditInfo> getUnitReauditList(Integer userId, QueryUnitReaudit query);
	
	public List<UnitReauditInfo> findReauditUnitByUnitIds(String unitIds);
	
	public List<Long> findAuditingUnitIds(Integer userId, List<Long> unitIds);

    /**
     * Function: 获取创意物料信息，包含图片或者flash的二进制文件
     * 
     * @author genglei01
     * @param userId userId
     * @param unitId unitId
     * @return UnitMaterialInfo
     */
    public UnitMaterialInfo getUnitMaterailInfo(Integer userId, Long unitId);
    
    /**
     * Function: 获取复审创意列表，专门为风控团队
     * 
     * @author genglei01
     * @param userId    用户名
     * @param query 请求
     * @return  List<UnitResponse>
     */
    public List<UnitResponse> getReauditUnitList(Integer userId, QueryUnitReaudit query);
    
    /**
     * Function: 通过unitIds获取复审创意列表
     * 
     * @author genglei01
     * @param unitIds 创意ID列表，以逗号分隔连接
     * @return List<UnitResponse>
     */
    public List<UnitResponse> getReauditUnitListByUnitIds(String unitIds);
    
    /**
     * Function: 获取智能创意子链信息
     * 
     * @author genglei01
     * @param userId    userId
     * @param groupId    groupId
     * @return List<TemplateElementUrlVo>
     */
    public List<TemplateElementUrlVo> getElementUrlList(int userId, int groupId);

}
