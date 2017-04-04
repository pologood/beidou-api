package com.baidu.beidou.api.external.report.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baidu.beidou.api.external.report.comparator.AbstractViewItemComparator;
import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.ShownSiteStatViewItem;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.stat.driver.exception.StorageServiceException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.SiteViewItem;

/**
 * ClassName: ShownSiteStatServiceImpl Function: 有展现网站报告查询，以doris查询出到数据为基准 `
 * 
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 30, 2011
 */
public class ShownSiteStatServiceImpl extends GenericStatServiceImpl<ShownSiteStatViewItem> implements
        GenericStatService<ShownSiteStatViewItem> {

    @Resource
    private ReportLiteService reportLiteService;

    /**
     * 获取有展现网站报告数据
     * 
     * @param request
     *            查询参数
     * @return List<ShownSiteStatServiceImpl> 有展现网站报告数据列表
     */
    public List<ShownSiteStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        int limitCount = reportLiteService.countGroupMainSiteInOlap(buildQueryParam(request));
        if (limitCount > size) {
            logger.info("ShownSiteStatServiceImpl : request data size too large size is :" + limitCount + " olap size :"
                    + limitCount);
            throw new StorageServiceException();
        }

        List<SiteViewItem> allData = reportLiteService.querySiteLiteReport(buildQueryParam(request));
        logger.info("ShownSiteStatServiceImpl ：request=[{}] got {}" + " result from OLAP ", request, allData.size());

        List<ShownSiteStatViewItem> result = CollectionUtil.createArrayList();
        if (CollectionUtil.isEmpty(allData)) {
            return result;
        }

        for (SiteViewItem item : allData) {
            String siteurl = item.getSiteUrl();
            ShownSiteStatViewItem stat = new ShownSiteStatViewItem();
            // FIXME
            Date date = DateUtils.olapStrToDate(item.getShowDate());
            stat.setDay(dateFormat.format(date));
            stat.setUserId(userId);
            stat.setPlanId(item.getPlanId());
            stat.setGroupId(item.getGroupId());
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
     * @param request
     *            查询参数
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
        if (CollectionUtil.isNotEmpty(groupIds)) {
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