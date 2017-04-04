package com.baidu.beidou.api.external.report.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.GenderStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.GenderViewItem;

/**
 * 
 * ClassName: GenderStatServiceImpl <br>
 * Function: 性别报告查询，以实际查询出doris的数据为准
 * 
 * @author zhangxu
 * @date Sep 25, 2012
 */
public class GenderStatServiceImpl extends GenericStatServiceImpl<GenderStatViewItem> implements
        GenericStatService<GenderStatViewItem> {
    
    @Resource
    private ReportLiteService reportLiteService;

    /**
     * 获取性别维度报告数据
     * 
     * @param request 查询参数
     * @return List<GenderStatViewItem> 性别维度报告数据列表
     */
    public List<GenderStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        List<GenderViewItem> allData = reportLiteService.queryGenderLiteReport(buildQueryParam(request));
        logger.info("GenderStatServiceImpl ：request=[{}] got {} result from olap", request, allData.size());

        List<GenderStatViewItem> result = new ArrayList<GenderStatViewItem>();

        // 构造显示VO列表
        if (!CollectionUtils.isEmpty(allData)) {// 如果有统计数据

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

            // Step4. 存放gender的map
            Map<String, GenderViewItem> genderMapping = new HashMap<String, GenderViewItem>();
            for (GenderViewItem record : allData) {
                Integer groupId = record.getGroupId();
                Integer genderId = record.getGenderId();
                String key = groupId + "_" + genderId;
                genderMapping.put(key, record);
            }

            // Step5. 构造volist
            Map<String, GenderStatViewItem> dtViewItemMapping = new HashMap<String, GenderStatViewItem>();
            for (String key : genderMapping.keySet()) {
                GenderViewItem record = genderMapping.get(key);
                if (record != null) {
                    Integer groupId = record.getGroupId();
                    Integer genderId = record.getGenderId();
                    GenderStatViewItem genderItem = new GenderStatViewItem();
                    genderItem.setGenderid(genderId);
                    genderItem.setGroupid(groupId);
                    Date date = DateUtils.olapStrToDate(record.getShowDate());
                    genderItem.setDay(dateFormat.format(date));
                    if (genderItem.getGenderid() == ReportWebConstants.DT_GENDER_MALE) {
                        genderItem.setGendername(ReportWebConstants.DT_GENDER_MALE_STRING);
                    } else if (genderItem.getGenderid() == ReportWebConstants.DT_GENDER_FEMALE) {
                        genderItem.setGendername(ReportWebConstants.DT_GENDER_FEMALE_STRING);
                    } else {
                        genderItem.setGendername(ReportWebConstants.DT_GENDER_NOTKNOWN_STRING);
                    }
                    genderItem.fillStatRecord(record);
                    dtViewItemMapping.put(key, genderItem);
                }
            }

            // Step6. 合并数据并过滤
            for (GenderStatViewItem genderItem : dtViewItemMapping.values()) {
                Integer genderId = genderItem.getGenderid();
                Integer groupId = genderItem.getGroupid();

                genderItem.setUserid(userId);
                genderItem.setUsername(username);

                // 合并未传入（9）和未识别（0）数据项
                if (!genderItem.isDeleted()) {
                    if ((genderItem.getGenderid() == ReportWebConstants.DT_GENDER_NOTINPUT || genderItem.getGenderid() == ReportWebConstants.DT_GENDER_NOTKNOWN)) {
                        Integer otherId =
                                (genderId == ReportWebConstants.DT_GENDER_NOTKNOWN) ? ReportWebConstants.DT_GENDER_NOTINPUT
                                        : ReportWebConstants.DT_GENDER_NOTKNOWN;
                        String otherKey = groupId + "_" + otherId;
                        GenderStatViewItem otherGenderItem = dtViewItemMapping.get(otherKey);
                        if (otherGenderItem != null) {
                            if (!otherGenderItem.isDeleted()) {
                                genderItem.setSrchs(genderItem.getSrchs() + otherGenderItem.getSrchs());
                                genderItem.setSrchuv(genderItem.getSrchuv() + otherGenderItem.getSrchuv());
                                genderItem.setClks(genderItem.getClks() + otherGenderItem.getClks());
                                genderItem.setClkuv(genderItem.getClkuv() + otherGenderItem.getClkuv());
                                genderItem.setCost(genderItem.getCost() + otherGenderItem.getCost());
                                genderItem.setHolmesClks(genderItem.getHolmesClks() + otherGenderItem.getHolmesClks());
                                genderItem.setEffectArrCnt(genderItem.getEffectArrCnt()
                                        + otherGenderItem.getEffectArrCnt());
                                genderItem.setArrivalCnt(genderItem.getArrivalCnt() + otherGenderItem.getArrivalCnt());
                                genderItem.setHopCnt(genderItem.getHopCnt() + otherGenderItem.getHopCnt());
                                genderItem.setResTime(genderItem.getResTime() + otherGenderItem.getResTime());
                                genderItem.setDirectTrans(genderItem.getDirectTrans()
                                        + otherGenderItem.getDirectTrans());
                                genderItem.setIndirectTrans(genderItem.getIndirectTrans()
                                        + otherGenderItem.getIndirectTrans());
                                genderItem.generateExtentionFields();
                                otherGenderItem.setDeleted(true);
                            }
                        }

                        // 填充用户、推广组、推广计划信息
                        CproGroup groupViewItem = groupInfoData.get(groupId);
                        genderItem.setGroupid(groupId);
                        if (groupViewItem != null) {
                            genderItem.setGroupname(groupViewItem.getGroupName());
                            Integer planId = groupViewItem.getPlanId();
                            if (planId != null) {
                                CproPlan planViewItem = planInfoData.get(planId);
                                if (planViewItem != null) {
                                    genderItem.setPlanid(planId);
                                    genderItem.setPlanname(planViewItem.getPlanName());
                                }
                            }
                        }

                        result.add(genderItem);

                    } else {

                        // 填充用户、推广组、推广计划信息
                        CproGroup groupViewItem = groupInfoData.get(groupId);
                        genderItem.setGroupid(groupId);
                        if (groupViewItem != null) {
                            genderItem.setGroupname(groupViewItem.getGroupName());
                            Integer planId = groupViewItem.getPlanId();
                            if (planId != null) {
                                CproPlan planViewItem = planInfoData.get(planId);
                                if (planViewItem != null) {
                                    genderItem.setPlanid(planId);
                                    genderItem.setPlanname(planViewItem.getPlanName());
                                }
                            }
                        }

                        result.add(genderItem);

                    }
                }
            }

        }

        return result;

    }

    /**
     * main
     */
    public static void main(String[] args) throws Exception {

        GenderStatServiceImpl service;
        String beanName = "genderStatService";
        service = (GenderStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20120811");
        Date endDate = DateUtils.strToDate("20120831");
        request.setUserid(499);
        request.setStartDate(startDate);
        request.setEndDate(endDate);

        List<Integer> planIds = new ArrayList<Integer>();
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

        List<GenderStatViewItem> result = service.queryStat(request);
        for (GenderStatViewItem item : result) {
            System.out.println(item);
        }
    }

}
