package com.baidu.beidou.api.external.report.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.RegionAssistant;
import com.baidu.beidou.api.external.report.vo.RegionStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.report.ReportConstants;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.RegionViewItem;

/**
 * 
 * ClassName: RegionStatServiceImpl <br>
 * Function: 地域报告查询，以实际查询出doris的数据为准
 * 
 * @author zhangxu
 * @date Sep 25, 2012
 */
public class RegionStatServiceImpl extends GenericStatServiceImpl<RegionStatViewItem> implements
        GenericStatService<RegionStatViewItem> {
    
    @Resource
    private ReportLiteService reportLiteService;

    /**
     * 获取推广组报告数据 <br>
     * 
     * Note: bd_991_reg_group_uv 这个里面必须有999999这个汇总一级地域的才可以 而其他的reg_stat_uv,holmes和trans是程序计算的
     * 
     * @param request 查询参数
     * @return List<GroupStatViewItem> 推广组报告数据列表
     */
    public List<RegionStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        List<RegionViewItem> allData = reportLiteService.queryRegionLiteReport(buildQueryParam(request));
        logger.info("RegionStatServiceImpl ：request=[{}] got {} result from olap", request, allData.size());

        // 返回的结果
        List<RegionStatViewItem> result = CollectionUtil.createArrayList();
        if (CollectionUtil.isEmpty(allData)) {
            return result;
        }

        // 将doris数据按天切割
        Map<String, List<RegionViewItem>> allDataByDate = CollectionUtil.createHashMap();
        if (CollectionUtil.isNotEmpty(allData)) {
            for (RegionViewItem item : allData) {
                Date date = DateUtils.olapStrToDate(item.getShowDate());
                String key = dateFormat.format(date);
                List<RegionViewItem> temp = allDataByDate.get(key);
                if (temp == null) {
                    temp = CollectionUtil.createArrayList();
                }
                temp.add(item);
                allDataByDate.put(key, temp);
            }
        }

        // Step1.
        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            logger.error("RegionStatServiceImpl got empty user info result from db");
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

        for (Map.Entry<String, List<RegionViewItem>> entry : allDataByDate.entrySet()) {
            String day = entry.getKey();
            List<RegionViewItem> data = entry.getValue();
            // 保存查询的绩效数据与地域字面
            Map<Integer, Map<Integer, RegionAssistant>> regionGroupMapping = CollectionUtil.createHashMap();
            // 构造的查询地域字面数据结构
            Map<Integer, List<String>> groupRegionMap = CollectionUtil.createHashMap();

            // 9、将地域放入map中，供过滤使用,key为复合key，即推广组id+地域id
            Map<String, RegionViewItem> regionDataMap = CollectionUtil.createHashMap();

            // Step4. 遍历所有推广组构造地域统计数据
            for (RegionViewItem item : data) {
                Integer groupid = item.getGroupId();
                Integer provid = item.getFirstRegionId();
                Integer cityid = item.getSecondRegionId();

                // 构造RegCache查询参数
                List<String> regionIds = null;
                if (groupid != null) {
                    regionIds = groupRegionMap.get(groupid);
                    if (regionIds == null) {
                        regionIds = CollectionUtil.createArrayList();
                    }
                    String key = provid + "_" + cityid;
                    regionIds.add(key);
                }
                groupRegionMap.put(groupid, regionIds);

                // 存储数据,用于以后填充数据
                if ((provid != null) && (cityid != null) && (groupid != null)) {
                    String key = groupid + "_" + provid + "_" + cityid;
                    regionDataMap.put(key, item);
                }
            }
            generateRegionVo(groupRegionMap, regionGroupMapping);
            // 构造report显示VOList并过滤
            if (!regionGroupMapping.isEmpty()) {
                RegionViewItem item;

                // 遍历每个推广组
                for (Integer groupid : regionGroupMapping.keySet()) {
                    Map<Integer, RegionAssistant> regionVoMap = regionGroupMapping.get(groupid);
                    for (Integer firstRegionId : regionVoMap.keySet()) {

                        // 构造父地域
                        RegionAssistant regionAssistant = new RegionAssistant(firstRegionId);
                        RegionAssistant firstRegion = regionVoMap.get(firstRegionId);
                        RegionStatViewItem viewItem = new RegionStatViewItem();
                        viewItem.setRegionId(firstRegionId);
                        String key = "";
                        if (firstRegionId == 0) {
                            key = groupid + "_" + firstRegionId + "_" + 0;
                            item = regionDataMap.get(key);
                        } else {
                            key = groupid + "_" + firstRegionId + "_" + ReportConstants.sumFlagInDoris;
                            item = regionDataMap.get(key);
                        }
                        viewItem.setUserId(userId);
                        viewItem.setUserName(username);
                        // viewItem.fillStatRecordOnly(data);
                        viewItem.fillStatRecord(item);
                        viewItem.setRegionName(firstRegion.getSelfRegion().getRegionName());
                        viewItem.setRegionId(firstRegion.getSelfRegion().getRegionId());
                        viewItem.setType(ReportWebConstants.REGION_TYPE_FIRST);

                        // 填充用户、推广组、推广计划信息
                        CproGroup groupViewItem = groupInfoData.get(groupid);
                        viewItem.setGroupId(groupid);
                        if (groupViewItem != null) {
                            viewItem.setGroupName(groupViewItem.getGroupName());
                            Integer planId = groupViewItem.getPlanId();
                            if (planId != null) {
                                CproPlan planViewItem = planInfoData.get(planId);
                                if (planViewItem != null) {
                                    viewItem.setPlanId(planId);
                                    viewItem.setPlanName(planViewItem.getPlanName());
                                }
                            }
                        }

                        regionAssistant.setSelfRegion(viewItem);
                        result.add(viewItem);

                        // 构造子地域
                        for (RegionStatViewItem childRegion : firstRegion.getChildRegions()) {
                            RegionStatViewItem childViewItem = new RegionStatViewItem();
                            key = groupid + "_" + firstRegionId + "_" + childRegion.getRegionId();
                            item = regionDataMap.get(key);
                            childViewItem.setUserId(userId);
                            childViewItem.setUserName(username);
                            viewItem.setDay(day);
                            childViewItem.setDay(day);
                            childViewItem.fillStatRecord(item);
                            childViewItem.setRegionId(childRegion.getRegionId());
                            if (childRegion.getRegionId() == 0) {
                                childRegion.setRegionId(Integer.MAX_VALUE);
                            }
                            childViewItem.setRegionName(childRegion.getRegionName());
                            childViewItem.setType(ReportWebConstants.REGION_TYPE_SECOND);

                            // 填充用户、推广组、推广计划信息
                            groupViewItem = groupInfoData.get(groupid);
                            childViewItem.setGroupId(groupid);
                            if (groupViewItem != null) {
                                childViewItem.setGroupName(groupViewItem.getGroupName());
                                Integer planId = groupViewItem.getPlanId();
                                if (planId != null) {
                                    CproPlan planViewItem = planInfoData.get(planId);
                                    if (planViewItem != null) {
                                        childViewItem.setPlanId(planId);
                                        childViewItem.setPlanName(planViewItem.getPlanName());
                                    }
                                }
                            }

                            childViewItem.setFirstRegionId(firstRegionId);
                            regionAssistant.addSecondRegion(childViewItem);
                            result.add(childViewItem);
                        }

                        // 计算扩展列
                        regionAssistant.ensureStatFields();

                    }
                }
            }
        }

        return result;

    }

    /**
     * 通过RegCache相关接口构造地域的父子关系树
     * 
     * @param groupRegionMap
     */
    private void generateRegionVo(Map<Integer, List<String>> groupRegionMap,
            Map<Integer, Map<Integer, RegionAssistant>> regionGroupMapping) {
        // 1、遍历所有推广组
        for (Integer groupId : groupRegionMap.keySet()) {

            // 2、判断当前推广组地域列表是否存在，若存在取出，若不存在则新建
            Map<Integer, RegionAssistant> groupRegions = regionGroupMapping.get(groupId);
            if (groupRegions == null) {
                groupRegions = CollectionUtil.createHashMap();
            }
            regionGroupMapping.put(groupId, groupRegions);

            // 3、遍历推广组地域id列表
            List<String> regionList = groupRegionMap.get(groupId);
            for (String regionKey : regionList) {
                String[] regionIds = regionKey.split("_");
                Integer firstRegionId = Integer.parseInt(regionIds[0]);
                if (firstRegionId == 0) {// 如果一级地域是其他或者未识别，则没有二级地域，因此不做二级地域的处理
                    RegionAssistant assistant = groupRegions.get(firstRegionId);
                    if (assistant == null) {
                        assistant = new RegionAssistant(firstRegionId);
                        RegionStatViewItem firstRegionItem = new RegionStatViewItem();
                        firstRegionItem.setRegionId(firstRegionId);
                        firstRegionItem.setRegionName(ReportWebConstants.REGION_OTHER);// 设置地域名为“其他”
                        assistant.setSelfRegion(firstRegionItem);
                        groupRegions.put(firstRegionId, assistant);
                    }
                } else {
                    Integer secondRegionId = Integer.parseInt(regionIds[1]);

                    // 处理一级地域
                    RegionAssistant assistant = groupRegions.get(firstRegionId);
                    if (assistant == null) {
                        assistant = new RegionAssistant(firstRegionId);
                        RegionStatViewItem firstRegionItem = new RegionStatViewItem();
                        firstRegionItem.setRegionId(firstRegionId);
                        String regionName = UnionSiteCache.regCache.getRegNameList().get(firstRegionId);
                        firstRegionItem.setRegionName(regionName);
                        assistant.setSelfRegion(firstRegionItem);
                        groupRegions.put(firstRegionId, assistant);
                    }

                    // 处理二级地域
                    if (secondRegionId != ReportConstants.sumFlagInDoris) {
                        RegionStatViewItem secondRegionItem = new RegionStatViewItem();
                        secondRegionItem.setRegionId(secondRegionId);
                        if (secondRegionId != 0) {
                            String regionName = UnionSiteCache.regCache.getRegNameList().get(secondRegionId);
                            secondRegionItem.setRegionName(regionName);
                        } else {
                            secondRegionItem.setRegionName(ReportWebConstants.REGION_NOTKNOWN);// 设置地域名为“未知”
                        }
                        assistant.addSecondRegion(secondRegionItem);
                    }
                }
            }
        }
    }

    /**
     * main
     */
    public static void main(String[] args) throws Exception {

        RegionStatServiceImpl service;
        String beanName = "regionStatService";
        service = (RegionStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        // 初始化推广组配置
        beanName = "cproGroupConstantMgr";
        CproGroupConstantMgr cproGroupConstantMgr =
                (CproGroupConstantMgr) (ServiceLocator.getInstance().getBeanByName(beanName));
        cproGroupConstantMgr.loadSystemConf();

        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20120811");
        Date endDate = DateUtils.strToDate("20120831");
        request.setUserid(499);
        request.setStartDate(startDate);
        request.setEndDate(endDate);

        List<Integer> planIds = CollectionUtil.createArrayList();
        planIds.add(1351);
        planIds.add(108);
        planIds.add(3281);// not me
        request.setPlanIds(planIds);

        // List<Integer> groupIds = new ArrayList<Integer>();
        // groupIds.add(2166145);
        // groupIds.add(2166147);
        // groupIds.add(2166148);
        // groupIds.add(970599);// not me
        // request.setGroupIds(groupIds);

        List<RegionStatViewItem> result = service.queryStat(request);
        for (RegionStatViewItem item : result) {
            System.out.println(item);
        }
    }

}
