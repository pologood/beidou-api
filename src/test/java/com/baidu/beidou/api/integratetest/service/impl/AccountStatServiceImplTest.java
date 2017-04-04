package com.baidu.beidou.api.integratetest.service.impl;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.AccountStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.AccountStatViewItem;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.cprogroup.service.CproGroupConstantMgr;
import com.baidu.beidou.util.DateUtils;

/**
 * 账户报告服务单测
 * 
 * @author work
 * 
 */
public class AccountStatServiceImplTest {


    /**
     * testQueryStat
     * 
     * @throws Exception Exception
     */
    @Test
    public void testQueryStat() throws Exception {
        CproGroupConstantMgr cproGroupConstantMgr =
                (CproGroupConstantMgr) ServiceLocator.getInstance().getBeanByName("cproGroupConstantMgr");
        cproGroupConstantMgr.loadSystemConf();

        AccountStatServiceImpl accountStatService;
        String beanName = "accountStatService";
        accountStatService = (AccountStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));

        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20131229");
        Date endDate = DateUtils.strToDate("20140103");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setNeedUv(true);

        List<AccountStatViewItem> result = null;
        try {
            result = accountStatService.queryStat(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (AccountStatViewItem item : result) {
            System.out.println(item);
        }
    }

}
