package com.baidu.beidou.api.integratetest.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.GroupStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.GroupStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.util.DateUtils;

/**
 * 推广组报告服务单测
 * 
 * @author work
 * 
 */
public class GroupStatServiceImplTest {

    /**
     * queryStat
     * 
     * @throws Exception Exception
     */
    @Test
    public void testQueryStat() throws Exception {
        CproGroupConstantMgr cproGroupConstantMgr =
                (CproGroupConstantMgr) ServiceLocator.getInstance().getBeanByName("cproGroupConstantMgr");
        cproGroupConstantMgr.loadSystemConf();
        GroupStatServiceImpl groupStatService;
        String beanName = "groupStatService";
        groupStatService = (GroupStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));

        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20131230");
        Date endDate = DateUtils.strToDate("20131230");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setNeedUv(true);

        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(2308440);
        request.setPlanIds(planIds);

        List<Integer> groupIds = new ArrayList<Integer>();
        groupIds.add(8006496);
        request.setGroupIds(groupIds);

        List<GroupStatViewItem> result = groupStatService.queryStat(request);
        for (GroupStatViewItem item : result) {
            System.out.println(item);
        }
    }

}
