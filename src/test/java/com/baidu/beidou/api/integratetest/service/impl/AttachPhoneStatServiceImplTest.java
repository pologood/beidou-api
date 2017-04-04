package com.baidu.beidou.api.integratetest.service.impl;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.AttachPhoneStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.AttachPhoneStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.util.DateUtils;

/**
 * ClassName: AttachPhoneStatServiceImplTest <br>
 * Function: 附加信息-电话 StatService test
 *
 * @author wangxujin
 */
public class AttachPhoneStatServiceImplTest {

    private String beanName = "attachPhoneStatService";

    private AttachPhoneStatServiceImpl attachPhoneStatService;

    private ApiReportQueryParameter request = new ApiReportQueryParameter();

    @Before
    public void setUp() throws Exception {
        attachPhoneStatService = (AttachPhoneStatServiceImpl) ServiceLocator.getInstance().getBeanByName(beanName);

        Date startDate = DateUtils.strToDate("20150701");
        Date endDate = DateUtils.strToDate("20150715");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setIdOnly(false);
    }

    @Test
    public void testQueryStat() throws Exception {

        List<AttachPhoneStatViewItem> statViewItems = attachPhoneStatService.queryStat(request);
        for (AttachPhoneStatViewItem item : statViewItems) {
            System.out.println(item);
        }

    }
}