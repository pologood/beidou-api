package com.baidu.beidou.api.integratetest.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.PlanStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.PlanStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.util.DateUtils;

/**
 * 计划报告服务单测
 * 
 * @author work
 * 
 */
public class PlanStatServiceImplTest {

    /**
     * queryStat
     * 
     */
    @Test
    public void testQueryStat() {
        CproGroupConstantMgr cproGroupConstantMgr =
                (CproGroupConstantMgr) ServiceLocator.getInstance().getBeanByName("cproGroupConstantMgr");
        cproGroupConstantMgr.loadSystemConf();
        PlanStatServiceImpl planStatService;
        String beanName = "planStatService";
        planStatService = (PlanStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        try {
            ApiReportQueryParameter request = new ApiReportQueryParameter();
            Date startDate = DateUtils.strToDate("20140722");
            Date endDate = DateUtils.strToDate("20140722");
            request.setUserid(5);
            request.setStartDate(startDate);
            request.setEndDate(endDate);

            List<Integer> planIds = new ArrayList<Integer>();
            planIds.add(1);
            request.setPlanIds(planIds);

            List<PlanStatViewItem> result = null;
            try {
                result = planStatService.queryStat(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (PlanStatViewItem item : result) {
                System.out.println(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
