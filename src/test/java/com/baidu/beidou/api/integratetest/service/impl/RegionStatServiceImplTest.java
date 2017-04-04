/**
 * 
 */
package com.baidu.beidou.api.integratetest.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.RegionStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.RegionStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.util.DateUtils;
import com.baidu.unbiz.common.Assert;
import com.baidu.unbiz.common.CollectionUtil;

/**
 * RegionStatServiceImplTest
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年8月12日 下午6:27:35
 */
public class RegionStatServiceImplTest {

    /**
     * queryStat
     * 
     * @throws Exception Exception
     */
    @Test
    public void queryStat() throws Exception {

        RegionStatServiceImpl service;
        String beanName = "regionStatService";
        service = (RegionStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        // 初始化推广组配置
        beanName = "cproGroupConstantMgr";
        CproGroupConstantMgr cproGroupConstantMgr =
                (CproGroupConstantMgr) (ServiceLocator.getInstance().getBeanByName(beanName));
        cproGroupConstantMgr.loadSystemConf();

        // ApiReportQueryParameter request = new ApiReportQueryParameter();
        // Date startDate = DateUtils.strToDate("20120811");
        // Date endDate = DateUtils.strToDate("20120831");
        // request.setUserid(499);
        // request.setStartDate(startDate);
        // request.setEndDate(endDate);
        //
        // List<Integer> planIds = CollectionUtil.createArrayList();
        // planIds.add(1351);
        // planIds.add(108);
        // planIds.add(3281);// not me
        // request.setPlanIds(planIds);
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20111231");
        Date endDate = DateUtils.strToDate("20150104");
        request.setUserid(8);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setIdOnly(false);

        List<Integer> planIds = new ArrayList<Integer>();
        // planIds.add(757446);
        planIds.add(1987443);
        // planIds.add(740918);// not me
        request.setPlanIds(planIds);
        // List<Integer> groupIds = new ArrayList<Integer>();
        // groupIds.add(2166145);
        // groupIds.add(2166147);
        // groupIds.add(2166148);
        // groupIds.add(970599);// not me
        // request.setGroupIds(groupIds);

        List<RegionStatViewItem> result = service.queryStat(request);
        Assert.assertTrue(CollectionUtil.isNotEmpty(result));
        for (RegionStatViewItem item : result) {
            System.out.println(item);
        }
    }

}
