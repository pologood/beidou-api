package com.baidu.beidou.api.integratetest.service.impl;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.AttachMessageStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.AttachMessageStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.util.DateUtils;

/**
 * ClassName: AttachMessageStatServiceImplTest <br>
 * Function: 附加信息-短信  statService test
 *
 * @author wangxujin
 */
public class AttachMessageStatServiceImplTest {

    private String beanName = "attachMessageStatService";

    private AttachMessageStatServiceImpl attachMessageStatService;

    private ApiReportQueryParameter request = new ApiReportQueryParameter();

    @Before
    public void setUp() throws Exception {
        attachMessageStatService = (AttachMessageStatServiceImpl) ServiceLocator.getInstance().getBeanByName(beanName);

        Date startDate = DateUtils.strToDate("20150701");
        Date endDate = DateUtils.strToDate("20150715");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setIdOnly(false);
    }

    @Test
    public void testQueryStat() throws Exception {

        List<AttachMessageStatViewItem> statViewItems = attachMessageStatService.queryStat(request);
        for (AttachMessageStatViewItem item : statViewItems) {
            System.out.println(item);
        }

    }
}