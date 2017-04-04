/**
 * 
 */
package com.baidu.beidou.api.integratetest.service.impl;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.service.impl.PackageStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.PackStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.util.DateUtils;

/**
 * PackageStatServiceImplTest
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年8月19日 上午12:47:38
 */
@Ignore
public class PackageStatServiceImplTest {

    /**
     * queryStat
     * 
     * @throws Exception Exception
     */
    @Test
    public void queryData() throws Exception {

        PackageStatServiceImpl service = null;
        String beanName = "packageStatService";
        service = (PackageStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20111231");
        Date endDate = DateUtils.strToDate("20150104");
        request.setUserid(8); // planId 1987417
        request.setReportType(ReportWebConstants.REPORT_TYPE.PACK);
        request.setStatRange(ReportWebConstants.REPORT_RANGE.ACCOUNT);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        List<PackStatViewItem> result = service.queryStat(request);
        for (PackStatViewItem item : result) {
            System.out.println(item);
        }

    }

}
