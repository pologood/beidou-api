package com.baidu.beidou.api.external.report.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.AppStatViewItem;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.AppViewItem;

/**
 * FIXME 这代码本身就有问题，我勒个去
 * 
 * @author caichao
 * @since 2013-11-20
 */
public class AppStatServiceImpl extends GenericStatServiceImpl<AppStatViewItem> implements
        GenericStatService<AppStatViewItem> {

    @Resource
    private ReportLiteService reportLiteService;
    
    @Override
    public List<AppStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        List<AppViewItem> allData = reportLiteService.queryAppLiteReport(buildQueryParam(request));
        logger.info("AppStatServiceImpl ：request=[{}] got {} result from Olap", request, allData.size());

        List<AppStatViewItem> result = CollectionUtil.createArrayList();
        // 合并从数据库中获取到数据与Olap查询出来到数据
        for (AppViewItem res : allData) {
            AppStatViewItem appitem = new AppStatViewItem();
            Date date = DateUtils.olapStrToDate(res.getShowDate());
            appitem.setDay(dateFormat.format(date));
            appitem.setUserId(userId);
            appitem.setPlanId(res.getPlanId());
            appitem.setGroupId(res.getGroupId());
            // appitem.fillStatRecordOnly(res);
            appitem.fillStatRecord(res);
            Long appid = res.getAppId();
            appitem.setAppId(appid);
            appitem.setAppName(UnionSiteCache.appCache.getAppNameById(appid));
            result.add(appitem);
            // continue begin;

        }
        if (!idOnly) {
            result = getEmptyViewItem(result);
        }
        return result;
    }

    /**
     * 获取未填充展现、点击、消费、ctr、cpm、acp数据的推广组信息
     * 
     * @param request 查询参数
     * @return List<GroupStatViewItem> 空绩效数据的推广计划组数据列表
     */
    public List<AppStatViewItem> getEmptyViewItem(List<AppStatViewItem> result) throws Exception {

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

        for (AppStatViewItem stat : result) {
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
