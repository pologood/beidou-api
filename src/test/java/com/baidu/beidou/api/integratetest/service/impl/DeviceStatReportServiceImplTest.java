package com.baidu.beidou.api.integratetest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.DeviceStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.DeviceStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.util.DateUtils;

/**
 * 设备报告服务单测
 * 
 * @author work
 * 
 */
public class DeviceStatReportServiceImplTest {

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

        DeviceStatServiceImpl deviceStatService;
        String beanName = "deviceStatService";
        deviceStatService = (DeviceStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        String start = "20140615";
        String end = "20140615";
        request.setUserid(userId);
        request.setStartDate(DateUtils.strToDate(start));
        request.setEndDate(DateUtils.strToDate(end));
        request.setIdOnly(false);
        request.setNeedUv(true);

        List<Integer> planIds = new ArrayList<Integer>();
        planIds.add(1376830);
        request.setPlanIds(planIds);

        List<DeviceStatViewItem> result = deviceStatService.queryStat(request);

        System.out.println(result.size());
        for (DeviceStatViewItem item : result) {
            System.out.println(item);
        }
    }
}
