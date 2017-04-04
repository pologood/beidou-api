package com.baidu.beidou.api.internal.audit.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.internal.audit.service.ProductInfoService;
import com.baidu.beidou.api.internal.audit.vo.ProductView;
import com.baidu.beidou.api.internal.audit.vo.request.QueryProduct;
import com.baidu.beidou.cprogroup.bo.SmartIdeaProductFilterVo;
import com.baidu.beidou.cprogroup.service.SmartIdeaProductFilterMgr;
import com.baidu.beidou.cprounit.bo.SmartIdeaTemplateElementConfVo;
import com.baidu.beidou.cprounit.bo.Unit;
import com.baidu.beidou.cprounit.bo.UnitMater;
import com.baidu.beidou.cprounit.constant.CproUnitConstant;
import com.baidu.beidou.cprounit.dao.UnitDao;
import com.baidu.beidou.cprounit.service.CproUnitMgr;
import com.baidu.beidou.cprounit.service.SmartIdeaTemplateConfMgr;
import com.baidu.beidou.util.sidriver.bo.SiProductBiz.IdeaPreviewItem;
import com.baidu.beidou.util.sidriver.service.SmartIdeaService;
import com.baidu.unbiz.biz.framework.sterotype.BizResult;
import com.baidu.unbiz.soma.biz.siconf.api.SiConfService;
import com.baidu.unbiz.soma.biz.siconf.bo.preview.AuditPreviewRequest;
import com.baidu.unbiz.soma.biz.siconf.bo.preview.AuditPreviewResponse;

public class ProductInfoServiceImpl implements ProductInfoService {

    private static final Integer MAX_TEMPLATE_ID = 10000;

    private static final Log LOG = LogFactory.getLog(ProductInfoServiceImpl.class);

    private SmartIdeaProductFilterMgr smartIdeaProductFilterMgr;

    private SmartIdeaTemplateConfMgr smartIdeaTemplateConfMgr;

    private SmartIdeaService smartIdeaService;

    private UnitDao unitDao;

    private CproUnitMgr unitMgr;

    /**
     * <tt>smartidea-conf</tt>服务接口，由其SDK生成
     */
    private SiConfService siConfService;

    /**
     * getProductViewList: 根据unitId等信息，获取该创意的抽样预览结果
     * 由于目前两套智能创意渲染逻辑并存，使用templateId>10000作为判断条件
     * templataId<10000，走biz-servier渲染
     * templateId>=10000, 走新框架预览逻辑
     *
     * @param query
     *
     * @return
     */
    public List<ProductView> getProductViewList(QueryProduct query) {
        Integer templateId = query.getTemplateId();
        if (templateId < MAX_TEMPLATE_ID) {
            return this.getProductViewListFromBizServer(query);
        } else {
            return this.getProdcutViewListFromSiConfServer(query);
        }
    }

    /**
     * 新框架的创意抽样预览
     * 目前由si-conf server提供服务
     *
     * @param query
     *
     * @return
     */
    private List<ProductView> getProdcutViewListFromSiConfServer(QueryProduct query) {
        List<ProductView> result = new ArrayList<ProductView>();

        Long unitId = query.getUnitId();
        Integer groupId = query.getGroupId();
        Integer planId = query.getPlanId();
        Integer userId = query.getUserId();
        Integer templateId = query.getTemplateId();
        Integer width = query.getWidth();
        Integer height = query.getHeight();

        Unit unit = unitDao.findById(userId, unitId);
        if (unit == null) {
            LOG.error("get unit info failed, unitId=" + unitId + ", userId=" + userId);
            return result;
        }
        UnitMater mater = unit.getMaterial();
        Long tplConfId = mater.getTplConfId();
        Integer wlType = mater.getWuliaoType();

        AuditPreviewRequest request = new AuditPreviewRequest();
        request.setUnitId(unitId);
        request.setGroupId(groupId);
        request.setPlanId(planId);
        request.setUserId(userId);
        request.setTemplateId(templateId);
        request.setHeight(height);
        request.setWidth(width);
        request.setWlType(wlType);
        request.setTplConfId(tplConfId);
        request.setTargetUrl(mater.getTargetUrl());
        request.setWirelessTargetUrl(mater.getWirelessTargetUrl());
        request.setShowUrl(mater.getShowUrl());
        request.setWirelessShowUrl(mater.getWirelessShowUrl());

        BizResult<AuditPreviewResponse> bizResult = null;
        try {
            bizResult = siConfService.auditPreviewSmartIdea(request);
            if (!bizResult.isSuccess()) {
                LOG.error(bizResult.getBizErrors());
                return result;
            }
        } catch (Exception e) {
            LOG.error("auditPreviewSmartIdea from siConfService error:" + e.getMessage());
            return result;
        }

        if (bizResult.getDataList().isEmpty()) {
            LOG.error("auditPreviewSmartIdea dataList is empty");
            return result;
        } else {
            for (AuditPreviewResponse ar : bizResult.getDataList()) {
                result.add(this.getProductView(ar));
            }
        }
        return result;
    }

    /**
     * getProductViewList: 根据unitId等信息，获取该创意可拼接生成的产品预览列表，抽查
     *
     * @version cpweb-699
     * @author genglei01
     * @date Mar 5, 2014
     */
    private List<ProductView> getProductViewListFromBizServer(QueryProduct query) {
        List<ProductView> result = new ArrayList<ProductView>();

        Long unitId = query.getUnitId();
        Integer groupId = query.getGroupId();
        Integer planId = query.getPlanId();
        Integer userId = query.getUserId();
        Integer templateId = query.getTemplateId();
        Integer width = query.getWidth();
        Integer height = query.getHeight();

        // 获取推广组过滤条件
        SmartIdeaProductFilterVo smartIdeaProductFilter = smartIdeaProductFilterMgr.findVoByGroupId(groupId, userId);
        if (smartIdeaProductFilter == null) {
            LOG.error("get product filter for the targeted group failed, groupId=" + groupId + ", userId=" + userId);
            return result;
        }

        SmartIdeaTemplateElementConfVo templateConf = smartIdeaTemplateConfMgr.findByGroupId(groupId, userId);
        if (templateConf == null) {
            LOG.error("get template element conf for the targeted group failed, groupId=" + groupId + ", userId="
                    + userId);
            return result;
        }

        Unit unit = unitDao.findById(userId, unitId);
        if (unit == null) {
            LOG.error("get unit info failed, unitId=" + unitId + ", userId=" + userId);
            return result;
        }
        UnitMater mater = unit.getMaterial();

        Integer wuliaoType = mater.getWuliaoType();

        // 计时器
        StopWatch sw = new StopWatch();
        sw.start();

        String iconUrl = null;
        if (unit.getMaterial().getWuliaoType() == CproUnitConstant.MATERIAL_TYPE_LITERAL_WITH_ICON) {
            iconUrl = unitMgr.getTmpUrl(unit.getMaterial().getMcId(), unit.getMaterial().getMcVersionId());
        }

        List<IdeaPreviewItem> list = smartIdeaService.getAuditInfos(unitId, groupId,
                planId, userId, templateId, width, height, smartIdeaProductFilter,
                templateConf, wuliaoType, mater.getTargetUrl(), mater.getShowUrl(),
                mater.getWirelessTargetUrl(), mater.getWirelessShowUrl(), iconUrl);

        sw.stop();
        LOG.info("get product preview list using " + sw.getTime() + " ms, unitId=" + unitId
                + ", groupId=" + groupId + ", userId=" + userId + ", templateId=" + templateId
                + ", width=" + width + ", height=" + height);

        if (CollectionUtils.isEmpty(list)) {
            LOG.warn("get product preview list for the targeted is empty, unitId=" + unitId
                    + ", groupId=" + groupId + ", userId=" + userId + ", templateId=" + templateId
                    + ", width=" + width + ", height=" + height);
            return result;
        }

        for (IdeaPreviewItem item : list) {
            result.add(this.getProductView(item));
        }

        return result;
    }

    private ProductView getProductView(IdeaPreviewItem item) {
        ProductView result = new ProductView();
        result.setUnitId(item.getAdId());
        result.setUserId(item.getUserId());
        result.setProductId(item.getProductId());
        result.setUrl(item.getProductUrl());
        result.setHtmlSnippet(item.getHtmlSnippet().toStringUtf8());
        result.setWidth(item.getWidth());
        result.setHeight(item.getHeight());

        // 设置智能文本或者图文创意的标题和描述
        result.setTitle(item.getPreviewText().getTitle());
        result.setDesc1(item.getPreviewText().getDesc1());
        result.setDesc2(item.getPreviewText().getDesc2());

        // 设置智能图文创意的icon链接
        result.setIconUrl(item.getPreviewText().getIcon().toStringUtf8());

        return result;
    }

    private ProductView getProductView(AuditPreviewResponse item) {
        ProductView result = new ProductView();
        result.setUnitId(item.getUnitId());
        result.setUserId(item.getUserId());
        result.setProductId(item.getProductId());
        result.setUrl(item.getProductUrl());
        result.setHtmlSnippet(item.getHtmlSnippet());
        result.setWidth(item.getWidth());
        result.setHeight(item.getHeight());
        return result;
    }

    /**
     * refuseProduct: 拒绝产品
     *
     * @version cpweb-699
     * @author genglei01
     * @date Mar 5, 2014
     */
    public boolean refuseProduct(List<Long> productIds, Integer userId) {
        if (CollectionUtils.isEmpty(productIds)) {
            return true;
        }

        // 计时器
        StopWatch sw = new StopWatch();
        sw.start();

        boolean result = smartIdeaService.refuseProduct(productIds, userId);

        sw.stop();
        LOG.info("refuse productIds using " + sw.getTime() + " ms, product size is " + productIds.size());

        return result;
    }

    public SmartIdeaProductFilterMgr getSmartIdeaProductFilterMgr() {
        return smartIdeaProductFilterMgr;
    }

    public void setSmartIdeaProductFilterMgr(
            SmartIdeaProductFilterMgr smartIdeaProductFilterMgr) {
        this.smartIdeaProductFilterMgr = smartIdeaProductFilterMgr;
    }

    public SmartIdeaService getSmartIdeaService() {
        return smartIdeaService;
    }

    public void setSmartIdeaService(SmartIdeaService smartIdeaService) {
        this.smartIdeaService = smartIdeaService;
    }

    public SmartIdeaTemplateConfMgr getSmartIdeaTemplateConfMgr() {
        return smartIdeaTemplateConfMgr;
    }

    public void setSmartIdeaTemplateConfMgr(
            SmartIdeaTemplateConfMgr smartIdeaTemplateConfMgr) {
        this.smartIdeaTemplateConfMgr = smartIdeaTemplateConfMgr;
    }

    public UnitDao getUnitDao() {
        return unitDao;
    }

    public void setUnitDao(UnitDao unitDao) {
        this.unitDao = unitDao;
    }

    public CproUnitMgr getUnitMgr() {
        return unitMgr;
    }

    public void setUnitMgr(CproUnitMgr unitMgr) {
        this.unitMgr = unitMgr;
    }

    public SiConfService getSiConfService() {
        return siConfService;
    }

    public void setSiConfService(SiConfService siConfService) {
        this.siConfService = siConfService;
    }
}
