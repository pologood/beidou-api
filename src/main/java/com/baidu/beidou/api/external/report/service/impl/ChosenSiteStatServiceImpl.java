package com.baidu.beidou.api.external.report.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.axis.utils.StringUtils;

import com.baidu.beidou.api.external.report.comparator.AbstractViewItemComparator;
import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.ShownSiteStatViewItem;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.CproGroupInfo;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.vo.BDSiteInfo;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.stat.driver.exception.StorageServiceException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.SiteViewItem;

public class ChosenSiteStatServiceImpl extends GenericStatServiceImpl<ShownSiteStatViewItem> implements
        GenericStatService<ShownSiteStatViewItem> {

    @Resource
    private ReportLiteService reportLiteService;
    
    /**
     * 获取有展现网站报告数据
     * 
     * @param request 查询参数
     * @return List<ShownSiteStatServiceImpl> 有展现网站报告数据列表
     */
    public List<ShownSiteStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        int limitCount = reportLiteService.countGroupMainSiteInOlap(buildQueryParam(request));
        if (limitCount > size) {
            logger.info("ChosenSiteStatServiceImpl : request data size too large size is :" + limitCount
                    + " olap size :" + limitCount);
            throw new StorageServiceException();
        }
        
        List<SiteViewItem> allData = reportLiteService.querySiteLiteReport(buildQueryParam(request));
        logger.info("ChosenSiteStatServiceImpl ：request=[{}] got {}  result from OLAP", request, allData.size());

        if (allData.size() > size) {
            String errorMsg = "request data size too large";
            logger.info(errorMsg);
            throw new StorageServiceException(errorMsg, null);
        }

        List<ShownSiteStatViewItem> result = CollectionUtil.createArrayList();
        if (CollectionUtil.isEmpty(allData)) {
            return result;
        }
        // 获取所有推广组id
        this.groupIds = findGroupIds(userId, planIds, groupIds);
        // 获取推广组下所有的自选网站(不包括已删除的)
        List<CproGroupInfo> groupInfoList = cproGroupMgr.findCproGroupInfoByKeys(groupIds, userId);
        if (CollectionUtil.isEmpty(groupInfoList)) {
            return result;
        }
        // 自选网站的map，key为groupid和siteUrl,从doris中过滤自选网站时使用
        Map<String, CproGroupInfo> groupIdSiteUrlKey = CollectionUtil.createHashMap();
        for (CproGroupInfo groupInfo : groupInfoList) {
            if (groupInfo.getIsAllSite() == CproGroupConstant.GROUP_ALLSITE
                    || StringUtils.isEmpty(groupInfo.getSiteListStr())) {
                // 没有自选网站的不需要关心
                continue;
            }

            String[] siteIds = groupInfo.getSiteListStr().split("\\|");
            if (siteIds != null) {
                List<BDSiteInfo> infos = UnionSiteCache.siteInfoCache.getSiteInfoList();
                Map<Integer, Integer> siteIndexMap = UnionSiteCache.siteInfoCache.getReverseIndexBySiteId();
                for (int k = 0; k < siteIds.length; k++) {
                    // 为了避免出现""，因此加入判断
                    if (StringUtils.isEmpty(siteIds[k])) {
                        continue;
                    }
                    Integer index = siteIndexMap.get(Integer.valueOf(siteIds[k]));
                    if (index != null) {
                        String key = groupInfo.getGroupId() + infos.get(index).getSiteurl();
                        groupIdSiteUrlKey.put(key, groupInfo);
                    }
                }
            }
        }
        if (groupIdSiteUrlKey.isEmpty()) {
            // 无自选网站
            return result;
        }
        for (SiteViewItem item : allData) {
            Integer groupid = item.getGroupId();
            String siteurl = item.getSiteUrl();
            // 过滤自选网站
            String key = groupid + siteurl;
            if (!groupIdSiteUrlKey.containsKey(key)) {
                continue;
            }
            ShownSiteStatViewItem stat = new ShownSiteStatViewItem();
            Date date = DateUtils.olapStrToDate(item.getShowDate());
            stat.setDay(dateFormat.format(date));
            stat.setUserId(userId);
            stat.setPlanId(item.getPlanId());
            stat.setGroupId(groupid);
            stat.setSiteUrl(siteurl);
            stat.fillStatRecord(item);
            result.add(stat);
        }


        // 按照时间排序
        Collections.sort(result, new AbstractViewItemComparator());

        if (!idOnly) {
            result = getEmptyViewItem(result);
        }

        return result;
    }

    /**
     * 获取未填充展现、点击、消费、ctr、cpm、acp数据的有展现网站信息 <br>
     * Note：非id only
     * 
     * @param request 查询参数
     * @return List<ShownSiteStatViewItem> 有展现网站数据列表
     */
    public List<ShownSiteStatViewItem> getEmptyViewItem(List<ShownSiteStatViewItem> result) throws Exception {
        // Step1.
        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            logger.error("GroupStatServiceImpl got empty user info result from db");
            return result;
        }
        String username = accountInfoData.get(userId).getUsername();

        // Step2.
        Map<Integer, CproGroup> groupInfoData = findGroupInfo(userId, planIds, groupIds);
        if (CollectionUtil.isEmpty(groupIds)) {
            groupIds = CollectionUtil.createArrayList();
            groupIds.addAll(groupInfoData.keySet());
        }

        // Step3.
        Map<Integer, CproPlan> planInfoData = findPlanInfo(userId, groupInfoData);

        for (ShownSiteStatViewItem stat : result) {
            try {
                stat.setUserName(username);
                if (planInfoData.get(stat.getPlanId()) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get plan id=["
                            + stat.getPlanId() + "]");
                }
                stat.setPlanName(planInfoData.get(stat.getPlanId()).getPlanName());
                if (groupInfoData.get(stat.getGroupId()) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get group id=["
                            + stat.getGroupId() + "]");
                }
                stat.setGroupName(groupInfoData.get(stat.getGroupId()).getGroupName());
            } catch (Exception e) {
                logger.error("error occured when contructing view item", e);
                continue;
            }
        }
        return result;
    }

}
