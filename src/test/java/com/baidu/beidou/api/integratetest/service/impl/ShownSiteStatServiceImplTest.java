/**
 * 
 */
package com.baidu.beidou.api.integratetest.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.ShownSiteStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.ShownSiteStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.util.DateUtils;
import com.baidu.unbiz.common.Assert;
import com.baidu.unbiz.common.CollectionUtil;

/**
 * ShownSiteStatServiceImplTest
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年8月11日 下午6:28:46
 */
public class ShownSiteStatServiceImplTest {

    /**
     * queryStat
     * 
     * @throws Exception Exception
     */
    @Test
    public void queryStat() throws Exception {

        ShownSiteStatServiceImpl service;
        String beanName = "shownSiteStatService";
        service = (ShownSiteStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20111231");
        Date endDate = DateUtils.strToDate("20150104");
        request.setUserid(8);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setIdOnly(false);

        List<Integer> planIds = new ArrayList<Integer>();
        // planIds.add(757446);
        planIds.add(1987347);
        // planIds.add(740918);// not me
        request.setPlanIds(planIds);

        // List<Integer> groupIds = new ArrayList<Integer>();
        // groupIds.add(2166145);
        // groupIds.add(2166147);
        // groupIds.add(2166148);
        // groupIds.add(970599);// not me
        // request.setGroupIds(groupIds);

        List<ShownSiteStatViewItem> result = service.queryStat(request);
        Assert.assertTrue(CollectionUtil.isNotEmpty(result));
        for (ShownSiteStatViewItem item : result) {
            System.out.println(item);
        }
    }
}
