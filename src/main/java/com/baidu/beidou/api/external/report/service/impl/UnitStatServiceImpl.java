package com.baidu.beidou.api.external.report.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.UnitStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.stat.driver.exception.StorageServiceException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.UnitViewItem;

/**
 * 
 * ClassName: UnitStatServiceImpl Function: 创意报告查询，以DB查询出到数据为基准
 * 
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 30, 2011
 */
public class UnitStatServiceImpl extends GenericStatServiceImpl<UnitStatViewItem> implements
        GenericStatService<UnitStatViewItem> {

    @Resource
    private ReportLiteService reportLiteService;

    /**
     * 获取创意报告数据
     * 
     * @param request 查询参数
     * @return List<UnitStatViewItem> 创意报告数据列表
     */
    public List<UnitStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        try {
            this.initParameters(request);
            List<UnitViewItem> allData = reportLiteService.queryUnitLiteReport(buildQueryParam(request));
            logger.info("UnitStatServiceImpl ：request=[{}] got {} result from olap", request, allData.size());
            
            if (allData.size() > size) {
                logger.info("UnitStatServiceImpl : request data size too large size is : {}", allData.size());
                throw new StorageServiceException();
            }

            // 生成空数据 把merge后的数据存在的unitid 传进去 减少不必要的item 如果请求中传入了unitid(最多100个)
            // 则使用请求中的来造空view add by caichao
            List<Long> units = new ArrayList<Long>(allData.size());
            for (UnitViewItem unitViewItem : allData) {
                Long unitId = unitViewItem.getUnitId();
                if (!units.contains(unitId)) {
                    units.add(unitId);
                }
            }
            // 为了防止请求时间跨度太大导致数据量太大 判断如果最终总数据量大于10w,抛出异常
            Integer size = CollectionUtils.isEmpty(unitIds) ? units.size() : unitIds.size();

            if (!isSizeOk(from, to, size)) {
                logger.warn("UnitStatServiceImpl : request data size too large");
                throw new StorageServiceException();
            }

            // 对doris数据按天分拨 完全去除空viewItem
            Map<String, List<UnitViewItem>> dayMap = new HashMap<String, List<UnitViewItem>>();
            for (UnitViewItem unitViewItem : allData) {
                Date date = DateUtils.olapStrToDate(unitViewItem.getShowDate());
                String key = dateFormat.format(date);
                List<UnitViewItem> dayList = dayMap.get(key);
                if (dayList == null) {
                    dayList = new ArrayList<UnitViewItem>();
                    dayMap.put(key, dayList);
                }
                dayList.add(unitViewItem);

            }

            List<UnitStatViewItem> result = getEmptyViewItem(units, dayMap);
            logger.info("UnitStatServiceImpl ：request=[{}] contruct {} info data from db", request, result.size());
            
            return result;
        } catch (Exception e) {
            logger.info("Error occurred when querying unit report, reason=" + e.getMessage(), e);
        }
        
        return new ArrayList<UnitStatViewItem>(0);
    }

    /**
     * 获取未填充展现、点击、消费、ctr、cpm、acp数据创意信息
     * 
     * @param request 查询参数
     * @return List<UnitStatViewItem> 空绩效数据的创意报告数据列表
     */
    public List<UnitStatViewItem> getEmptyViewItem(List<Long> units, Map<String, List<UnitViewItem>> dayMap)
            throws Exception {
        if (CollectionUtils.isEmpty(unitIds)) {
            unitIds = units;
        }
        List<UnitStatViewItem> result = new ArrayList<UnitStatViewItem>();

        // Step1.
        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            logger.error("UnitStatServiceImpl got empty user info result from db");
            return result;
        }
        String username = accountInfoData.get(userId).getUsername();

        // Step2.
        Map<Long, UnitInfoView> unitInfoData = findUnitInfo(userId, planIds, groupIds, unitIds);
        if (CollectionUtils.isEmpty(unitIds)) {
            unitIds = new ArrayList<Long>();
            unitIds.addAll(unitInfoData.keySet());
        }

        // Step3.
        Map<Integer, CproGroup> groupInfoData = findGroupInfo(userId, unitInfoData);
        Map<Integer, CproPlan> planInfoData = findPlanInfo(userId, groupInfoData);

        // Step4.
        // Date start = (Date)(from.clone());
        // for(;start.getTime() <= to.getTime();start = DateUtils.getNextDay(start)){
        // for(Long unitId: unitIds){
        // UnitStatViewItem stat = new UnitStatViewItem();
        // //stat.fillZeroStat();
        // stat.setDay(dateFormat.format(start));
        // stat.setUserid(userId);
        // stat.setUsername(username);
        //
        // UnitInfoView dbVo = unitInfoData.get(unitId);
        // if(dbVo == null){
        // throw new InternalException("Error occurred when contructing view item, can not get unit id=[" + unitId +
        // "]");
        // }
        //
        // Integer planId = Long.valueOf(dbVo.getPlanid()).intValue();
        // stat.setPlanid(planId);
        // if(planInfoData.get(planId) == null){
        // throw new InternalException("Error occurred when contructing view item, can not get plan id=[" + planId +
        // "]");
        // }
        // String planName = planInfoData.get(planId).getPlanName();
        // stat.setPlanname(planName);
        //
        // Integer groupId = Long.valueOf(dbVo.getGroupid()).intValue();
        // stat.setGroupid(groupId);
        // if(groupInfoData.get(groupId) == null){
        // throw new InternalException("Error occurred when contructing view item, can not get group id=[" + groupId +
        // "]");
        // }
        // String groupName = groupInfoData.get(groupId).getGroupName();
        // stat.setGroupname(groupName);
        //
        // stat.setUnitid(unitId);
        // String title = dbVo.getTitle();
        // stat.setTitle(title);
        // int wuliaotype = dbVo.getWuliaoType();
        // stat.setWuliaotype(wuliaotype);
        //
        // result.add(stat);
        // }
        // }

        for (Map.Entry<String, List<UnitViewItem>> entry : dayMap.entrySet()) {
            String day = entry.getKey();
            for (UnitViewItem unitViewItem : entry.getValue()) {
                UnitStatViewItem stat = new UnitStatViewItem();

                Long unitId = unitViewItem.getUnitId();
                stat.setDay(day);
                stat.setUserid(userId);
                stat.setUsername(username);

                UnitInfoView dbVo = unitInfoData.get(unitId);
                if (dbVo == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get unit id=["
                            + unitId + "]");
                }

                Integer planId = Long.valueOf(dbVo.getPlanid()).intValue();
                stat.setPlanid(planId);
                if (planInfoData.get(planId) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get plan id=["
                            + planId + "]");
                }
                String planName = planInfoData.get(planId).getPlanName();
                stat.setPlanname(planName);

                Integer groupId = Long.valueOf(dbVo.getGroupid()).intValue();
                stat.setGroupid(groupId);
                if (groupInfoData.get(groupId) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get group id=["
                            + groupId + "]");
                }
                String groupName = groupInfoData.get(groupId).getGroupName();
                stat.setGroupname(groupName);

                stat.setUnitid(unitId);
                String title = dbVo.getTitle();
                stat.setTitle(title);
                int wuliaotype = dbVo.getWuliaoType();
                stat.setWuliaotype(wuliaotype);

                stat.fillStatRecord(unitViewItem);
                result.add(stat);
            }
        }

        return result;
    }

    /**
     * main
     */
    public static void main(String[] args) throws Exception {

        UnitStatServiceImpl service;
        String beanName = "unitStatService";
        service = (UnitStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20111230");
        Date endDate = DateUtils.strToDate("20120102");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);

        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(757446);
        planIds.add(757447);
        planIds.add(757448);// not me
        // request.setPlanIds(planIds);

        List<Integer> groupIds = new ArrayList<Integer>();
        groupIds.add(2166145);
        groupIds.add(2166147);
        groupIds.add(2166148);
        groupIds.add(2166149);// not me
        // request.setGroupIds(groupIds);

        List<Long> unitIds = new ArrayList<Long>();
        unitIds.add(9283621l);
        unitIds.add(9283625l);
        unitIds.add(9283629l);// not me
        request.setUnitIds(unitIds);

        List<UnitStatViewItem> result = service.queryStat(request);
        for (UnitStatViewItem item : result) {
            System.out.println(item);
        }
    }

}
