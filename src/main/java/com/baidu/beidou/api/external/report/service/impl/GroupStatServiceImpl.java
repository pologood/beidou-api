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
import com.baidu.beidou.api.external.report.vo.GroupStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.stat.driver.exception.StorageServiceException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.GroupViewItem;

/**
 * 
 * ClassName: GroupStatServiceImpl Function: 推广组报告查询，以DB查询出到数据为基准
 * 
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 30, 2011
 */
public class GroupStatServiceImpl extends GenericStatServiceImpl<GroupStatViewItem> implements
        GenericStatService<GroupStatViewItem> {
    
    @Resource
    private ReportLiteService reportLiteService;

    /**
     * 获取推广组报告数据
     * 
     * @param request 查询参数
     * @return List<GroupStatViewItem> 推广组报告数据列表
     */
    public List<GroupStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        List<GroupViewItem> allData = reportLiteService.queryGroupLiteReport(buildQueryParam(request));
        logger.info("GroupStatServiceImpl ：request=[{}] got {} result from olap", request, allData.size());

        // 生成空数据 把merge后的数据中存在的groupid 传进去 减少不必要的item 如果请求中传入了groupid(最多100个)
        // 则使用请求中的来造空view add by caichao
        List<Integer> groups = new ArrayList<Integer>(allData.size());
        for (GroupViewItem groupViewItem : allData) {
            Integer groupid = groupViewItem.getGroupId();
            if (!groups.contains(groupid)) {
                groups.add(groupid);
            }

        }
        // 为了防止请求时间跨度太大导致数据量太大 判断如果最终总数据量大于10w,抛出异常
        Integer size = CollectionUtils.isEmpty(groupIds) ? groups.size() : groupIds.size();

        if (!isSizeOk(from, to, size)) {
            logger.warn("GroupStatServiceImpl : request data size too large");
            throw new StorageServiceException();
        }

        // 对doris数据按天分拨 完全去除空viewItem
        Map<String, List<GroupViewItem>> dayMap = new HashMap<String, List<GroupViewItem>>();
        for (GroupViewItem groupViewItem : allData) {
            Date date = DateUtils.olapStrToDate(groupViewItem.getShowDate());
            String key = dateFormat.format(date);
            List<GroupViewItem> dayList = dayMap.get(key);
            if (dayList == null) {
                dayList = new ArrayList<GroupViewItem>();
                dayMap.put(key, dayList);
            }
            dayList.add(groupViewItem);

        }
        // 把doris中数据和DB中数据写入viewItem
        List<GroupStatViewItem> result = getEmptyViewItem(groups, dayMap);
        logger.info("GroupStatServiceImpl ：request=[{}] contruct {} info data from db", request, result.size());

        // 合并从数据库中获取到数据与doris查询出来到数据
        // int cnt = 0;
        // begin:
        // for(GroupStatViewItem item: result){
        // for(Map<String, Object> res: allData){
        // Date date = (Date) res.get(ReportConstants.FROMDATE);
        // Integer planid = (Integer) res.get(ReportConstants.PLAN);
        // Integer groupid = (Integer) res.get(ReportConstants.GROUP);
        // // FROMDATE、PLAN、GROUP 三者一致，才进行合并
        // if(date.equals(DateUtils.strToDate(item.getDay()))
        // && item.getGroupid() == groupid
        // && item.getPlanid() == planid){
        // item.fillStatRecordOnly(res);
        // cnt++;
        // continue begin;
        // }
        // }
        // }
        // log.info("GroupStatServiceImpl ：request=[" + request + "] merge " + cnt + " item with doris and db");
        // if(allData.size() != cnt){
        // log.error("GroupStatServiceImpl : db info data and doris data are not completely match" + request);
        // }
        // 按照时间排序
        Collections.sort(result, new AbstractViewItemComparator());
        return result;
    }

    /**
     * 获取未填充展现、点击、消费、ctr、cpm、acp数据的推广组信息
     * 
     * @param request 查询参数
     * @return List<GroupStatViewItem> 空绩效数据的推广计划组数据列表
     */
    public List<GroupStatViewItem> getEmptyViewItem(List<Integer> groups, Map<String, List<GroupViewItem>> dayMap)
            throws Exception {
        if (CollectionUtils.isEmpty(groupIds)) {
            groupIds = groups;
        }
        List<GroupStatViewItem> result = new ArrayList<GroupStatViewItem>();

        // Step1.
        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            logger.error("GroupStatServiceImpl got empty user info result from db");
            return result;
        }
        String username = accountInfoData.get(userId).getUsername();

        // Step2.
        Map<Integer, CproGroup> groupInfoData = findGroupInfo(userId, planIds, groupIds);
        if (CollectionUtils.isEmpty(groupIds)) {
            groupIds = new ArrayList<Integer>();
            groupIds.addAll(groupInfoData.keySet());
        }

        // Step3.
        Map<Integer, CproPlan> planInfoData = findPlanInfo(userId, groupInfoData);

        // Step4.
        // Date start = (Date)(from.clone());
        // for(;start.getTime() <= to.getTime();start = DateUtils.getNextDay(start)){
        // for(Integer groupId: groupIds){
        // GroupStatViewItem stat = new GroupStatViewItem();
        // //stat.fillZeroStat();
        // stat.setDay(dateFormat.format(start));
        // stat.setUserid(userId);
        // stat.setUsername(username);
        //
        // if(groupInfoData.get(groupId) == null){
        // throw new InternalException("Error occurred when contructing view item, can not get group id=[" + groupId +
        // "]");
        // }
        // Integer planId = groupInfoData.get(groupId).getPlanId();
        // stat.setPlanid(planId);
        //
        // if(planInfoData.get(planId) == null){
        // throw new InternalException("Error occurred when contructing view item, can not get plan id=[" + planId +
        // "]");
        // }
        // String planName = planInfoData.get(planId).getPlanName();
        // stat.setPlanname(planName);
        //
        // stat.setGroupid(groupId);
        // String groupName = groupInfoData.get(groupId).getGroupName();
        // stat.setGroupname(groupName);
        //
        // result.add(stat);
        // }
        // }

        for (Map.Entry<String, List<GroupViewItem>> entry : dayMap.entrySet()) {
            String day = entry.getKey();
            for (GroupViewItem groupViewItem : entry.getValue()) {
                GroupStatViewItem stat = new GroupStatViewItem();
                stat.setDay(day);
                stat.setUserid(userId);
                stat.setUsername(username);
                Integer groupId = groupViewItem.getGroupId();

                if (groupInfoData.get(groupId) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get group id=["
                            + groupId + "]");
                }
                Integer planId = groupInfoData.get(groupId).getPlanId();
                stat.setPlanid(planId);

                if (planInfoData.get(planId) == null) {
                    throw new InternalException("Error occurred when contructing view item, can not get plan id=["
                            + planId + "]");
                }
                String planName = planInfoData.get(planId).getPlanName();
                stat.setPlanname(planName);

                stat.setGroupid(groupId);
                String groupName = groupInfoData.get(groupId).getGroupName();
                stat.setGroupname(groupName);
                stat.fillStatRecord(groupViewItem);
                result.add(stat);
            }
        }

        return result;
    }

    /**
     * main
     */
    public static void main(String[] args) throws Exception {

        GroupStatServiceImpl service;
        String beanName = "groupStatService";
        service = (GroupStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20111230");
        Date endDate = DateUtils.strToDate("20120102");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);

        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(757446);
        planIds.add(757447);
        planIds.add(740918);// not me
        // request.setPlanIds(planIds);

        List<Integer> groupIds = new ArrayList<Integer>();
        groupIds.add(2166145);
        groupIds.add(2166147);
        groupIds.add(2166148);
        groupIds.add(970599);// not me
        request.setGroupIds(groupIds);

        List<GroupStatViewItem> result = service.queryStat(request);
        for (GroupStatViewItem item : result) {
            System.out.println(item);
        }
    }

}
