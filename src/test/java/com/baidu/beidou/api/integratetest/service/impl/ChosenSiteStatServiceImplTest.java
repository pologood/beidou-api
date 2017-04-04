/**
 * 
 */
package com.baidu.beidou.api.integratetest.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.ChosenSiteStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.ShownSiteStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.util.DateUtils;

/**
 * ChosenSiteStatServiceImplTest
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年8月11日 下午3:31:05
 */
public class ChosenSiteStatServiceImplTest {

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

        ChosenSiteStatServiceImpl service;
        String beanName = "chosenSiteStatService";
        service = (ChosenSiteStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
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

        // List<Integer> groupIds = new ArrayList<Integer>();
        // groupIds.add(2166145);
        // groupIds.add(2166147);
        // groupIds.add(2166148);
        // groupIds.add(970599);// not me
        // request.setGroupIds(groupIds);

        List<ShownSiteStatViewItem> result = service.queryStat(request);
        for (ShownSiteStatViewItem item : result) {
            System.out.println(item);
        }
    }

}
