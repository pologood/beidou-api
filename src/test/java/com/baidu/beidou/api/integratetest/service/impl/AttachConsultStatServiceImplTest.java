package com.baidu.beidou.api.integratetest.service.impl;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.AttachConsultStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.AttachConsultStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.util.DateUtils;

/**
 * ClassName: AttachConsultStatServiceImplTest <br>
 * Function: 附加信息-咨询  statService test
 *
 * @author wangxujin
 */
public class AttachConsultStatServiceImplTest {

    private String beanName = "attachConsultStatService";

    private AttachConsultStatServiceImpl attachConsultStatService;

    private ApiReportQueryParameter request = new ApiReportQueryParameter();

    @Before
    public void setUp() throws Exception {
        attachConsultStatService = (AttachConsultStatServiceImpl) ServiceLocator.getInstance().getBeanByName(beanName);

        Date startDate = DateUtils.strToDate("20150701");
        Date endDate = DateUtils.strToDate("20150715");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setIdOnly(false);
    }

    @Test
    public void testQueryStat() throws Exception {

        List<AttachConsultStatViewItem> statViewItems = attachConsultStatService.queryStat(request);
        for (AttachConsultStatViewItem item : statViewItems) {
            System.out.println(item);
        }

    }
}