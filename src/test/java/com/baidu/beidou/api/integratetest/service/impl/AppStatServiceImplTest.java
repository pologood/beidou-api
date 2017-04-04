package com.baidu.beidou.api.integratetest.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.AppStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.AppStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.util.DateUtils;
import com.baidu.unbiz.common.Assert;
import com.baidu.unbiz.common.CollectionUtil;

/**
 * app应用报告数据
 * 
 * @author caichao
 * @since 2013-11-20
 */
public class AppStatServiceImplTest {

    /**
     * queryStat
     * 
     * @throws Exception Exception
     */
    @Test
    public void queryStat() throws Exception {
        CproGroupConstantMgr cproGroupConstantMgr =
                (CproGroupConstantMgr) ServiceLocator.getInstance().getBeanByName("cproGroupConstantMgr");
        cproGroupConstantMgr.loadSystemConf();

        AppStatServiceImpl service;
        String beanName = "appStatService";
        service = (AppStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20111231");
        Date endDate = DateUtils.strToDate("20150104");
        request.setUserid(8);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setIdOnly(false);
        // request.setNeedUv(true);

        List<Integer> planIds = new ArrayList<Integer>();
        // planIds.add(757446);
        planIds.add(1987347);
        // planIds.add(740918);// not me
        request.setPlanIds(planIds);

        List<AppStatViewItem> result = service.queryStat(request);
        Assert.assertTrue(CollectionUtil.isNotEmpty(result));
        for (AppStatViewItem item : result) {
            System.out.println(item);
        }
    }

}
