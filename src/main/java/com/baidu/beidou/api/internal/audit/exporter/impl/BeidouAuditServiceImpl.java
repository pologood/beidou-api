package com.baidu.beidou.api.internal.audit.exporter.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.internal.audit.constant.AuditConstant;
import com.baidu.beidou.api.internal.audit.constant.QueryConstant;
import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;
import com.baidu.beidou.api.internal.audit.exporter.BeidouAuditService;
import com.baidu.beidou.api.internal.audit.service.AuditOptService;
import com.baidu.beidou.api.internal.audit.service.ProductInfoService;
import com.baidu.beidou.api.internal.audit.service.ReauditInfoService;
import com.baidu.beidou.api.internal.audit.service.UnitAuditInfoService;
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
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.service.UbmcService;
import com.baidu.beidou.cprounit.ubmcdriver.material.request.RequestBaseMaterial;
import com.baidu.beidou.cprounit.ubmcdriver.material.request.RequestLite;
import com.baidu.beidou.cprounit.vo.MaterialElementUrl;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.beidou.util.cache.TradeCache;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class BeidouAuditServiceImpl implements BeidouAuditService {

    private static final Log LOG = LogFactory.getLog(BeidouAuditServiceImpl.class);

    private AuditOptService auditOptService;

    private UnitAuditInfoService unitAuditInfoService;

    private ProductInfoService productInfoService;

    private UserMgr userMgr;

    private TradeCache tradeCache;
    
    private CproUnitMgr unitMgr;
    
    private UbmcService ubmcService;
    
    private ReauditInfoService reauditInfoService;
    
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
            List<Integer> refuseReasonIds, Integer auditorId, Integer dataStreamType) {
        AuditResult<Object> result = new AuditResult<Object>();
        if (!this.validateParams(userId, unitId, mcId, versionId, newTradeId, 
                unitTag, auditType, auditResult, refuseReasonIds)) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            return result;
        }
        
        // 添加计时器，记录各步骤执行时间
        StopWatch sw = new StopWatch();
        sw.start();
        
        // 历史操作记录保存对象
        List<OptContent> optContents = Lists.newArrayList();
        
        Unit unit = auditOptService.auditResult(userId, unitId, mcId, versionId, newTradeId, tradeModified, 
                unitTag, unitTagModified, auditType, auditResult, auditResultModified, refuseReasonIds, optContents);

        if (unit == null) {
            result.setStatus(ResponseStatus.ERROR);
            return result;
        }
        
        sw.stop();
        LOG.info("Step1(audit/reaudit pass) using " + sw.getTime() + " ms, for auditorId=" + auditorId);
        
        // 如果为增量审核，记录审核历史创意版本，并将审核结果传给风控团队
        if (dataStreamType == AuditConstant.DATA_STREAM_TYPE_INCR) {
            sw.reset();
            sw.start();
            boolean res = auditOptService.pushAuditLog(unit, auditResult, refuseReasonIds, auditorId);
            if (!res) {
                LOG.error("push audit log to riskmgr failed for unitid=" + unitId + ", userId=" + userId
                        + ", mcId=" + mcId + ", versionId=" + versionId);
            }
            sw.stop();
            LOG.info("Step2(record_audit_opt) using " + sw.getTime() + " ms, for auditorId=" + auditorId);
        }
        
        // 加入session中，后续有拦截器处理
        SessionHolder.getSession().put(ApiConstant.KEY_OPTHISTORY_CONTENT, optContents); 
        
        return result;
    }
    
    /**
     * Function: 校验参数
     * 
     * @author genglei01
     * @param userId    userId
     * @param unitId    unitId
     * @param mcId    mcId
     * @param versionId    versionId
     * @param newTradeId    三级行业ID
     * @param unitTag   创意标注，目前支持5个标注
     * @param auditType 审核操作类型，1代表一审，2代表复审
     * @param auditResult   审核操作结果，0代表有效，4代表拒绝
     * @param refuseReasonIds   拒绝理由ID列表
     * @return boolean true表示参数无问题；falsh表示参数有问题
     */
    private boolean validateParams(Integer userId, Long unitId, Long mcId, Integer versionId, Integer newTradeId, 
            Integer unitTag, Integer auditType, Integer auditResult, List<Integer> refuseReasonIds) {
        if (userId == null || userId <= 0 || unitId == null || unitId <= 0L
                || mcId == null || mcId <= 0L || versionId == null || versionId <= 0
                || newTradeId == null || newTradeId <= 0 || unitTag == null || unitTag < 0
                || (auditType != AuditConstant.BEIDOU_FIRST_AUDIT && auditType != AuditConstant.BEIDOU_SECOND_AUDIT)
                || (auditResult != AuditConstant.BEIDOU_AUDIT_PASS && auditResult != AuditConstant.BEIDOU_AUDIT_REFUSE)) {
            return false;
        }
                
        if (auditResult == AuditConstant.BEIDOU_AUDIT_REFUSE && CollectionUtils.isEmpty(refuseReasonIds)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Function: 获取物料URL
     * 
     * @author genglei01
     * @param requestList  请求ubmc的相关ID
     * @return AuditResult<MaterialUrlResponse>
     */
    public AuditResult<MaterialUrlResponse> getMaterialUrl(List<MaterialUrlRequest> requestList) {
        AuditResult<MaterialUrlResponse> result = new AuditResult<MaterialUrlResponse>();
        if (CollectionUtils.isEmpty(requestList)) {
            return result;
        }
        
        List<RequestBaseMaterial> units = Lists.transform(requestList, new Function<MaterialUrlRequest, RequestBaseMaterial>() {
            @Override
            public RequestBaseMaterial apply(MaterialUrlRequest request) {
                return new RequestLite(request.getMcId(), request.getVersionId());
            }
        });
        
        // 设置空的userId，防止报错
        ThreadContext.putUserId(1);
        Map<RequestBaseMaterial, String> responseMap = unitMgr.generateMaterUrl(units);
        if (responseMap != null && !responseMap.isEmpty()) {
            for (Map.Entry<RequestBaseMaterial, String> entry : responseMap.entrySet()) {
                if (entry != null && StringUtils.isNotEmpty(entry.getValue())) {
                    MaterialUrlResponse response = new MaterialUrlResponse(entry.getKey().getMcId(), 
                            entry.getKey().getVersionId(), entry.getValue());
                    result.addResult(response);
                }
            }
            if (CollectionUtils.isNotEmpty(result.getData())) {
                result.setTotalNum(result.getData().size());
            }
        } else {
            result.setTotalNum(0);
        }
        result.setTotalPage(1);
        
        return result;
    }
    
    /**
     * Function: 获取创意url以及元素url
     * 
     * @author genglei01
     * @param requestList  请求ubmc的相关ID
     * @return AuditResult<MaterialUrlResponse>
     */
    public AuditResult<MaterialElementUrl> getFlashAndElementUrl(List<MaterialUrlRequest> requestList) {
        AuditResult<MaterialElementUrl> result = new AuditResult<MaterialElementUrl>();
        if (CollectionUtils.isEmpty(requestList)) {
            return result;
        }
        
        List<RequestBaseMaterial> units = Lists.transform(requestList, 
                new Function<MaterialUrlRequest, RequestBaseMaterial>() {
                    @Override
                    public RequestBaseMaterial apply(MaterialUrlRequest request) {
                        return new RequestLite(request.getMcId(), request.getVersionId());
                    }
                });
        
        List<MaterialElementUrl> response = ubmcService.generateMaterialElementUrl(units);
        if (CollectionUtils.isNotEmpty(response)) {
            for (MaterialElementUrl url : response) {
                if (url != null) {
                    result.addResult(url);
                }
            }
            if (CollectionUtils.isNotEmpty(result.getData())) {
                result.setTotalNum(result.getData().size());
            }
        } else {
            result.setTotalNum(0);
        }
        result.setTotalPage(1);
        
        return result;
    }

    /**
     * Function: 获取复审用户列表
     * 
     * @author genglei01
     * @param queryType 请求类型，1-用户名查询，2-用户ID查询
     * @param query 请求内容，内容为空时全文搜索
     * @param page  页码
     * @param pageSize  每页大小
     * @return AuditResult<ReauditUserInfo>
     */
    public AuditResult<ReauditUserInfo> getReauditUserList(QueryBase query) {
        AuditResult<ReauditUserInfo> result = new AuditResult<ReauditUserInfo>();
        if (query == null || query.getQueryType() == null
                || query.getPage() < 0 || query.getPageSize() <= 0) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            return result;
        }
        
        if (query.getQueryType() != QueryConstant.QueryUserReaudit.allUser
                && query.getQueryType() != QueryConstant.QueryUserReaudit.userName
                && query.getQueryType() != QueryConstant.QueryUserReaudit.userId) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            return result;
        }
        
        List<ReauditUserInfo> list = Lists.newArrayList();
        int totalNum = reauditInfoService.findReauditUserList(query, list);
        result.addResults(list);
        
        int totalPage = (totalNum - 1) / query.getPageSize() + 1;
        result.setTotalNum(totalNum);
        result.setTotalPage(totalPage);
        
        return result;
    }
    
    /**
     * Function: 获取复审创意列表
     * 
     * @author genglei01
     * @param userId    用户ID
     * @param query 查询参数
     * @return AuditResult<UnitResponse>
     */
    public AuditResult<UnitResponse> getReauditUnitList(Integer userId, QueryUnitReaudit queryUnit) {
        AuditResult<UnitResponse> result = new AuditResult<UnitResponse>();
        if (userId == null || userId <= 0) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            return result;
        }
        
        queryUnit.setUserId(userId);
        int totalNum = (int) unitAuditInfoService.countReauditUnit(userId, queryUnit);
        int totalPage = (totalNum - 1) / queryUnit.getPageSize() + 1;
        
        List<UnitResponse> list = unitAuditInfoService.getReauditUnitList(userId, queryUnit);
        result.addResults(list);
        
        result.setTotalNum(totalNum);
        result.setTotalPage(totalPage);
        
        return result;
    }
    
    /**
     * Function: 通过unitIds获取复审创意列表
     * 
     * @author genglei01
     * @param unitIds    创意ID列表，以逗号分隔连接
     * @return AuditResult<UnitResponse>
     */
    public AuditResult<UnitResponse> getReauditUnitListByUnitIds(String unitIds) {
        AuditResult<UnitResponse> result = new AuditResult<UnitResponse>();
        if (StringUtils.isEmpty(unitIds) && unitIds.length() <= AuditConstant.REAUDIT_UNITID_LENGTH) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            return result;
        }
        
        List<UnitResponse> list = unitAuditInfoService.getReauditUnitListByUnitIds(unitIds);
        if (CollectionUtils.isNotEmpty(list)) {
            result.addResults(list);
            result.setTotalNum(list.size());
        }
        result.setTotalPage(1);
        
        return result;
    }
    
    /**
     * Function: 获取产品列表
     * 
     * @author genglei01
     * @param userId    用户ID
     * @param query 请求参数
     * @return AuditResult<ProductView>
     */
    public AuditResult<ProductView> getProductViewList(Integer userId, QueryProduct query) {
        AuditResult<ProductView> result = new AuditResult<ProductView>();
        
        List<ProductView> list = productInfoService.getProductViewList(query);
        if (CollectionUtils.isNotEmpty(list)) {
            result.addResults(list);
            result.setTotalNum(list.size());
        } else {
            result.setTotalNum(0);
        }
        
        result.setTotalPage(1);
        
        return result;
    }

    /**
     * Function: 拒绝产品
     * 
     * @author genglei01
     * @param userId    用户ID
     * @param productIds    产品ID列表
     * @return AuditResult<Object>
     */
    public AuditResult<Object> refuseProduct(Integer userId, List<Long> productIds) {
        AuditResult<Object> result = new AuditResult<Object>();
        
        if (userId == null || CollectionUtils.isEmpty(productIds)) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            return result;
        }
        
        boolean flag = productInfoService.refuseProduct(productIds, userId);
        if (flag == false) {
            result.setStatus(ResponseStatus.ERROR);
        }
        
        return result;
    }
    
    /**
     * Function: 获取智能创意子链信息
     * 
     * @author genglei01
     * @param userId    userId
     * @param groupId   groupId
     * @param unitId    unitId
     * @return AuditResult<TemplateElementUrlVo>
     */
    public AuditResult<TemplateElementUrlVo> getElementUrlList(int userId, int groupId, long unitId) {
        AuditResult<TemplateElementUrlVo> result = new AuditResult<TemplateElementUrlVo>();
        
        if (userId <= 0 || groupId <= 0 ||  unitId <= 0L) {
            result.setStatus(ResponseStatus.PARAM_ERROR);
            return result;
        }
        
        List<TemplateElementUrlVo> list = unitAuditInfoService.getElementUrlList(userId, groupId);
        
        if (CollectionUtils.isNotEmpty(list)) {
            list = this.filterNullUrl(list);    // 过滤过滤url为null的item
            result.addResults(list);
            result.setTotalNum(list.size());
        } else {
            result.setTotalNum(0);
        }
        result.setTotalPage(1);
        
        return result;
    }
    
    /**
     * Function: 过滤url为null的item
     * 
     * @author genglei01
     * @param list 输入队列
     * @return List<TemplateElementUrlVo>
     */
    private List<TemplateElementUrlVo> filterNullUrl (List<TemplateElementUrlVo> list) {
        List<TemplateElementUrlVo> result = Lists.newArrayList();
        for (TemplateElementUrlVo urlItem : list) {
            if (StringUtils.isNotEmpty(urlItem.getTargetUrl())
                    && StringUtils.isNotEmpty(urlItem.getWirelessTargetUrl())) {
                result.add(urlItem);
            }
        }
        
        return result;
    }

    public AuditOptService getAuditOptService() {
        return auditOptService;
    }

    public void setAuditOptService(AuditOptService auditOptService) {
        this.auditOptService = auditOptService;
    }

    public UnitAuditInfoService getUnitAuditInfoService() {
        return unitAuditInfoService;
    }

    public void setUnitAuditInfoService(UnitAuditInfoService unitAuditInfoService) {
        this.unitAuditInfoService = unitAuditInfoService;
    }

    public UserMgr getUserMgr() {
        return userMgr;
    }

    public void setUserMgr(UserMgr userMgr) {
        this.userMgr = userMgr;
    }

    public TradeCache getTradeCache() {
        return tradeCache;
    }

    public void setTradeCache(TradeCache tradeCache) {
        this.tradeCache = tradeCache;
    }

    public ProductInfoService getProductInfoService() {
        return productInfoService;
    }

    public void setProductInfoService(ProductInfoService productInfoService) {
        this.productInfoService = productInfoService;
    }

    public CproUnitMgr getUnitMgr() {
        return unitMgr;
    }

    public void setUnitMgr(CproUnitMgr unitMgr) {
        this.unitMgr = unitMgr;
    }

    public ReauditInfoService getReauditInfoService() {
        return reauditInfoService;
    }

    public void setReauditInfoService(ReauditInfoService reauditInfoService) {
        this.reauditInfoService = reauditInfoService;
    }
    
    public UbmcService getUbmcService() {
        return ubmcService;
    }

    public void setUbmcService(UbmcService ubmcService) {
        this.ubmcService = ubmcService;
    }
}
