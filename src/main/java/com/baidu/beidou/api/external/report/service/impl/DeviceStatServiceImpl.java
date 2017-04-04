package com.baidu.beidou.api.external.report.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.FastDateFormat;

import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.DeviceStatViewItem;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.exception.InternalException;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.util.DateUtils;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportLiteService;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.DeviceViewItem;

/**
 * @author caichao
 * @since 2013-11-20
 */
public class DeviceStatServiceImpl extends GenericStatServiceImpl<DeviceStatViewItem> implements
        GenericStatService<DeviceStatViewItem> {
    
    protected FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMdd");
    
    @Resource
    private ReportLiteService reportLiteService;

    @Override
    public List<DeviceStatViewItem> queryStat(ApiReportQueryParameter request) throws Exception {
        this.initParameters(request);
        List<DeviceViewItem> allData = reportLiteService.queryDeviceLiteReport(buildQueryParam(request));

        logger.info("DeviceStatServiceImpl ：request=[{}] got {} result from olap", request, allData.size());
        // System.out.println(JSONUtil.serialize(statService2Result));

        List<DeviceStatViewItem> result = new ArrayList<DeviceStatViewItem>();
        for (DeviceViewItem deviceViewItem : allData) {
            DeviceStatViewItem statItem = new DeviceStatViewItem();
            Date date = DateUtils.olapStrToDate(deviceViewItem.getShowDate());
            statItem.setDay(dateFormat.format(date));
            statItem.setUserid(userId);
            statItem.setPlanid(deviceViewItem.getPlanId());
            statItem.setDeviceid(deviceViewItem.getDeviceId());
            statItem.fillStatRecord(deviceViewItem);
            result.add(statItem);
        }

        if (!idOnly) {
            result = getEmptyViewItem(result);
        }
        logger.info("DeviceStatServiceImpl ：request=[{}] contruct {} info data from db", request, result.size());

        return result;
    }

    /**
     * 获取未填充展现、点击、消费、ctr、cpm、acp数据的推广计划信息
     * 
     * @param result result
     * @return List<DeviceStatViewItem> 空绩效数据的推广计划报告数据列表
     * @throws Exception Exception
     */
    public List<DeviceStatViewItem> getEmptyViewItem(List<DeviceStatViewItem> result) throws Exception {
        Map<Integer, User> accountInfoData = findAccountInfo(userId);
        if (accountInfoData.get(userId) == null) {
            logger.error("DeviceStatServiceImpl got empty user info result from db");
            return result;
        }
        String username = accountInfoData.get(userId).getUsername();

        Map<Integer, CproPlan> planInfoData = findPlanInfo(userId, planIds);
        if (CollectionUtils.isEmpty(planIds)) {
            planIds = new ArrayList<Integer>();
            planIds.addAll(planInfoData.keySet());
        }

        for (DeviceStatViewItem deviceViewItem : result) {
            deviceViewItem.setUsername(username);

            int planId = deviceViewItem.getPlanid();

            if (planInfoData.get(planId) == null) {
                throw new InternalException("Error occurred when contructing view item, can not get plan id=[" + planId
                        + "]");
            }
            String planName = planInfoData.get(planId).getPlanName();
            deviceViewItem.setPlanname(planName);
        }

        return result;
    }

}
