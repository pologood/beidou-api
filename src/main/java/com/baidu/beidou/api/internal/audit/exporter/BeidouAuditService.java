package com.baidu.beidou.api.internal.audit.exporter;

import java.util.List;

import com.baidu.beidou.api.internal.audit.vo.AuditResult;
import com.baidu.beidou.api.internal.audit.vo.MaterialUrlRequest;
import com.baidu.beidou.api.internal.audit.vo.MaterialUrlResponse;
import com.baidu.beidou.api.internal.audit.vo.ProductView;
import com.baidu.beidou.api.internal.audit.vo.ReauditUserInfo;
import com.baidu.beidou.api.internal.audit.vo.TemplateElementUrlVo;
import com.baidu.beidou.api.internal.audit.vo.UnitResponse;
import com.baidu.beidou.api.internal.audit.vo.request.QueryBase;
import com.baidu.beidou.api.internal.audit.vo.request.QueryProduct;
import com.baidu.beidou.api.internal.audit.vo.request.QueryUnitReaudit;
import com.baidu.beidou.cprounit.vo.MaterialElementUrl;

/**
 * Function: 北斗内部API，创意审核相关
 * 
 * @ClassName: BeidouAuditService
 * @author genglei01
 * @date Mar 17, 2015 11:57:33 AM
 */
public interface BeidouAuditService {

    /**
     * Function: 审核操作接口
     * 
     * @author genglei01
     * @param userId    userId
     * @param unitId    unitId
     * @param mcId    mcId
     * @param versionId    versionId
     * @param newTradeId    三级行业ID
     * @param tradeModified 行业是否被修改
     * @param unitTag   创意标注，目前支持5个标注
     * @param unitTagModified   创意标注是否修改
     * @param auditType 审核操作类型，1代表一审，2代表复审
     * @param auditResult   审核操作结果，0代表有效，4代表拒绝
     * @param auditResultModified   审核操作结果是否修改
     * @param refuseReasonIds   拒绝理由ID列表
     * @param auditorId auditorId   审核员ID
     * @param dataStreamType 数据输入流类型，0：增量数据流；1：全量数据流
     * @return AuditResult
     */
    public AuditResult<Object> auditResult(Integer userId, Long unitId, Long mcId, Integer versionId, 
            Integer newTradeId, boolean tradeModified, Integer unitTag, boolean unitTagModified, 
            Integer auditType, Integer auditResult, boolean auditResultModified, 
            List<Integer> refuseReasonIds, Integer auditorId, Integer dataStreamType);
    
    /**
     * Function: 获取物料URL
     * 
     * @author genglei01
     * @param requestList  请求ubmc的相关ID
     * @return AuditResult<MaterialUrlResponse>
     */
    public AuditResult<MaterialUrlResponse> getMaterialUrl(List<MaterialUrlRequest> requestList);
    
    /**
     * Function: 获取创意url以及元素url
     * 
     * @author genglei01
     * @param requestList  请求ubmc的相关ID
     * @return AuditResult<MaterialUrlResponse>
     */
    public AuditResult<MaterialElementUrl> getFlashAndElementUrl(List<MaterialUrlRequest> requestList);
    
    /**
     * Function: 获取复审用户列表
     * 
     * @author genglei01
     * @param query 请求参数
     * @return AuditResult<ReauditUserInfo>
     */
    public AuditResult<ReauditUserInfo> getReauditUserList(QueryBase query);
    
    /**
     * Function: 获取复审创意列表
     * 
     * @author genglei01
     * @param userId    用户ID
     * @param queryUnit 查询参数
     * @return AuditResult<UnitResponse>
     */
    public AuditResult<UnitResponse> getReauditUnitList(Integer userId, QueryUnitReaudit queryUnit);
    
    /**
     * Function: 通过unitIds获取复审创意列表
     * 
     * @author genglei01
     * @param unitIds    创意ID列表，以逗号分隔连接
     * @return AuditResult<UnitResponse>
     */
    public AuditResult<UnitResponse> getReauditUnitListByUnitIds(String unitIds);
    
    /**
     * Function: 获取产品列表
     * 
     * @author genglei01
     * @param userId    用户ID
     * @param query 请求参数
     * @return AuditResult<ProductView>
     */
    public AuditResult<ProductView> getProductViewList(Integer userId, QueryProduct query);
    
    /**
     * Function: 拒绝产品
     * 
     * @author genglei01
     * @param userId    用户ID
     * @param productIds    产品ID列表
     * @return AuditResult<Object>
     */
    public AuditResult<Object> refuseProduct(Integer userId, List<Long> productIds);
    
    /**
     * Function: 获取智能创意子链信息
     * 
     * @author genglei01
     * @param userId    userId
     * @param groupId   groupId
     * @param unitId    unitId
     * @return AuditResult<TemplateElementUrlVo>
     */
    public AuditResult<TemplateElementUrlVo> getElementUrlList(int userId, int groupId, long unitId);

}
