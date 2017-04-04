package com.baidu.beidou.api.external.report.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.report.comparator.AbstractViewItemComparator;
import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.PlanStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.stat.driver.exception.StorageServiceException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.PlanViewItem;

/**
 * 
 * ClassName: PlanStatServiceImpl Function: 推广计划报告查询，以DB查询出到数据为基准
 * 
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 30, 2011
 */
public class PlanStatServiceImpl extends GenericStatServiceImpl<PlanStatViewItem> implements
        GenericStatService<PlanStatViewItem> {
    
    @Resource
    private ReportLiteService reportLiteService;

    /**
     * 获取推广计划报告数据
     * 
     * @param request 查询参数
     * @return List<PlanStatViewItem> 推广计划报告数据列表
     */
    public List<PlanStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        List<PlanViewItem> allData = reportLiteService.queryPlanLiteReport(buildQueryParam(request));
        logger.info("PlanStatServiceImpl ：request=[{}] got {} result from olap", request, allData.size());
        // System.out.println(JSONUtil.serialize(statService2Result));
        List<Integer> plans = new ArrayList<Integer>(allData.size());
        for (PlanViewItem planViewItem : allData) {
            Integer planId = planViewItem.getPlanId();
            if (!plans.contains(planId)) {
                plans.add(planId);
            }
        }

        // 为了防止请求时间跨度太大导致数据量太大 判断如果最终总数据量大于10w,抛出异常
        Integer size = CollectionUtils.isEmpty(planIds) ? plans.size() : planIds.size();

        if (!isSizeOk(from, to, size)) {
            logger.warn("PlanStatServiceImpl : request data size too large");
            throw new StorageServiceException();
        }

        // 对doris数据按天分拨 完全去除空viewItem
        Map<String, List<PlanViewItem>> dayMap = new HashMap<String, List<PlanViewItem>>();
        for (PlanViewItem planViewItem : allData) {
            Date date = DateUtils.olapStrToDate(planViewItem.getShowDate());
            String key = dateFormat.format(date);
            List<PlanViewItem> dayList = dayMap.get(key);
            if (dayList == null) {
                dayList = new ArrayList<PlanViewItem>();
                dayMap.put(key, dayList);
            }
            dayList.add(planViewItem);

        }

        List<PlanStatViewItem> result = getEmptyViewItem(plans, dayMap);
        logger.info("PlanStatServiceImpl ：request=[{}] contruct {} info data from db", request, result.size());

        // int cnt = 0;
        // begin:
        // for(PlanStatViewItem item: result){
        // for(Map<String, Object> res: allData){
        // Date date = (Date) res.get(ReportConstants.FROMDATE);
        // Integer planid = (Integer) res.get(ReportConstants.PLAN);
        // //log.info(item.getPlanid() + "|" + planid);
        // //log.info(item.getDay() + "|" + date);
        // //log.info(item.getDay().equals(DateUtils.getDateStr(date)));
        // //log.info(item.getPlanid() == planid);
        // // FROMDATE、PLAN 两者一致，才进行合并
        // if(date.equals(DateUtils.strToDate(item.getDay()))
        // && item.getPlanid() == planid){
        // item.fillStatRecordOnly(res);
        // cnt++;
        // continue begin;
        // }
        // }
        // }
        // log.info("PlanStatServiceImpl ：request=[" + request + "] merge " + cnt + " item with doris and db");
        // if(allData.size() != cnt){
        // log.error("PlanStatServiceImpl : db info data and doris data are not completely match" + request);
        // }

        return result;
    }

    /**
     * 获取未填充展现、点击、消费、ctr、cpm、acp数据的推广计划信息
     * 
     * @param request 查询参数
     * @return List<PlanStatViewItem> 空绩效数据的推广计划报告数据列表
     */
    public List<PlanStatViewItem> getEmptyViewItem(List<Integer> plans, Map<String, List<PlanViewItem>> dayMap)
            throws Exception {
        if (CollectionUtils.isEmpty(planIds)) {
            planIds = plans;
        }
        List<PlanStatViewItem> result = new ArrayList<PlanStatViewItem>();

        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            logger.error("PlanStatServiceImpl got empty user info result from db");
            return result;
        }
        String username = accountInfoData.get(userId).getUsername();

        Map<Integer, CproPlan> planInfoData = findPlanInfo(userId, planIds);
        if (CollectionUtils.isEmpty(planIds)) {
            planIds = new ArrayList<Integer>();
            planIds.addAll(planInfoData.keySet());
        }

        // Date start = (Date)(from.clone());
        // for(;start.getTime() <= to.getTime();start = DateUtils.getNextDay(start)){
        // for(Integer planId: planIds){
        // PlanStatViewItem stat = new PlanStatViewItem();
        // //stat.fillZeroStat();
        // stat.setDay(dateFormat.format(start));
        // stat.setUserid(userId);
        // stat.setUsername(username);
        // stat.setPlanid(planId);
        //
        // if(planInfoData.get(planId) == null){
        // throw new InternalException("Error occurred when contructing view item, can not get plan id=[" + planId +
        // "]");
        // }
        // stat.setPlanname(planInfoData.get(planId).getPlanName());
        // result.add(stat);
        // }
        // }

        for (Map.Entry<String, List<PlanViewItem>> entry : dayMap.entrySet()) {
            String day = entry.getKey();
            for (PlanViewItem planViewItem : entry.getValue()) {
                PlanStatViewItem stat = new PlanStatViewItem();
                Integer planId = planViewItem.getPlanId();
                stat.setDay(day);
                stat.setUserid(userId);
                stat.setUsername(username);
                stat.setPlanid(planId);

                if (planInfoData.get(planId) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get plan id=["
                            + planId + "]");
                }
                stat.setPlanname(planInfoData.get(planId).getPlanName());
                stat.fillStatRecord(planViewItem);
                result.add(stat);
            }
        }

        Collections.sort(result, new AbstractViewItemComparator());
        return result;
    }

    /**
     * main
     */
    public static void main(String[] args) throws Exception {

        PlanStatServiceImpl service;
        String beanName = "planStatService";
        service = (PlanStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20111230");
        Date endDate = DateUtils.strToDate("20120103");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        // List<Integer> planIds = new ArrayList<Integer>();
        // planIds.add(771017);
        // planIds.add(771014);
        // planIds.add(771001);
        // planIds.add(771005);
        // planIds.add(328475);
        // request.setPlanIds(planIds);
        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(757446);
        planIds.add(757447);
        planIds.add(740918);// not me
        request.setPlanIds(planIds);
        List<PlanStatViewItem> result = service.queryStat(request);
        for (PlanStatViewItem item : result) {
            System.out.println(item);
        }
    }

}
