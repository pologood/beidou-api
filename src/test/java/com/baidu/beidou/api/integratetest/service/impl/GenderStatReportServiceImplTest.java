package com.baidu.beidou.api.integratetest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.GenderStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.GenderStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.util.DateUtils;

/**
 * 性别报告服务单测
 * 
 * @author work
 * 
 */
public class GenderStatReportServiceImplTest {
    private static int userId = 19;

    /**
     * queryStat
     * 
     * @throws Exception Exception
     */
    @Test
    public void testGetReportId() throws Exception {
        CproGroupConstantMgr cproGroupConstantMgr =
                (CproGroupConstantMgr) ServiceLocator.getInstance().getBeanByName("cproGroupConstantMgr");
        cproGroupConstantMgr.loadSystemConf();

        GenderStatServiceImpl genderStatService;
        String beanName = "genderStatService";
        genderStatService = (GenderStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        String start = "20131229";
        String end = "20140103";
        request.setUserid(userId);
        request.setStartDate(DateUtils.strToDate(start));
        request.setEndDate(DateUtils.strToDate(end));
        request.setIdOnly(false);
        request.setNeedUv(true);

        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(1376830);
        request.setPlanIds(planIds);
        List<Integer> groupIds = new ArrayList<Integer>();
        groupIds.add(4347920);
        request.setGroupIds(groupIds);

        List<GenderStatViewItem> result = genderStatService.queryStat(request);

        System.out.println(result.size());
        for (GenderStatViewItem item : result) {
            System.out.println(item);
        }
    }
}
