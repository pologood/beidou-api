/**
 * 
 */
package com.baidu.beidou.api.integratetest.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.InterestStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.InterestStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.util.DateUtils;
import com.baidu.unbiz.common.Assert;
import com.baidu.unbiz.common.CollectionUtil;

/**
 * InterestStatServiceImplTest
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年8月13日 下午9:06:44
 */
@Ignore
public class InterestStatServiceImplTest {

    /**
     * queryStat
     * 
     * @throws Exception Exception
     */
    @Test
    public void queryData() throws Exception {

        InterestStatServiceImpl service = null;
        String beanName = "interestStatService";
        service = (InterestStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        // ApiReportQueryParameter request = new ApiReportQueryParameter();
        // Date startDate = DateUtils.strToDate("20130106");
        // Date endDate = DateUtils.strToDate("20130110");
        // request.setUserid(18);
        // List<Integer> groupIds = CollectionUtil.createArrayList();
        // groupIds.add(4302878);
        // request.setGroupIds(groupIds);
        //
        // request.setReportType(ReportWebConstants.REPORT_TYPE.INTEREST_SHOWN);
        // request.setStatRange(ReportWebConstants.REPORT_RANGE.GROUP);
        // request.setStartDate(startDate);
        // request.setEndDate(endDate);
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20111231");
        Date endDate = DateUtils.strToDate("20150104");
        request.setUserid(8);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setIdOnly(false);

        List<Integer> planIds = new ArrayList<Integer>();
        // planIds.add(757446);
        planIds.add(1987417);
        // planIds.add(740918);// not me
        request.setPlanIds(planIds);

        List<InterestStatViewItem> result = service.queryStat(request);
        Assert.assertTrue(CollectionUtil.isNotEmpty(result));
        for (InterestStatViewItem item : result) {
            System.out.println(item);
        }
        System.out.println("===========");

    }

}
