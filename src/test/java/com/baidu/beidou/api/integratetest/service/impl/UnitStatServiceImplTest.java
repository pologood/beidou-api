package com.baidu.beidou.api.integratetest.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.UnitStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.UnitStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.test.common.AbstractShardAddbTestCaseLegacy;
import com.baidu.beidou.util.DateUtils;

/**
 * 创意报告服务单测
 * 
 * @author work
 * 
 */
public class UnitStatServiceImplTest extends AbstractShardAddbTestCaseLegacy {

    @Resource
    private CproGroupConstantMgr cproGroupConstantMgr;

    /**
     * queryStat
     */
    @Test
    public void testQueryStat() {
        // CproGroupConstantMgr cproGroupConstantMgr =
        // (CproGroupConstantMgr) ServiceLocator.getInstance().getBeanByName("cproGroupConstantMgr");
        cproGroupConstantMgr.loadSystemConf();
        UnitStatServiceImpl unitStatService;
        String beanName = "unitStatService";
        unitStatService = (UnitStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
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

            List<Integer> groupIds = new ArrayList<Integer>();
            groupIds.add(1);
            request.setGroupIds(groupIds);

            List<Long> unitIds = new ArrayList<Long>();
            unitIds.add((long) 1);
            request.setUnitIds(unitIds);

            List<UnitStatViewItem> result = unitStatService.queryStat(request);
            for (UnitStatViewItem item : result) {
                System.out.println(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getShard() {
        // TODO Auto-generated method stub
        return 0;
    }
}
