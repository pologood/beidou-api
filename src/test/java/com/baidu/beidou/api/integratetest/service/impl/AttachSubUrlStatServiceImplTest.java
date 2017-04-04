package com.baidu.beidou.api.integratetest.service.impl;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.AttachSubUrlStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.AttachSubUrlStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.util.DateUtils;

/**
 * ClassName: AttachSubUrlStatServiceImplTest <br>
 * Function: 附加信息-子链  statService test
 *
 * @author wangxujin
 */
public class AttachSubUrlStatServiceImplTest {

    private String beanName = "attachSubUrlStatService";

    private AttachSubUrlStatServiceImpl attachSubUrlStatService;

    private ApiReportQueryParameter request = new ApiReportQueryParameter();

    @Before
    public void setUp() throws Exception {
        attachSubUrlStatService = (AttachSubUrlStatServiceImpl) ServiceLocator.getInstance().getBeanByName(beanName);

        Date startDate = DateUtils.strToDate("20150701");
        Date endDate = DateUtils.strToDate("20150715");
        request.setUserid(19);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setIdOnly(false);
    }

    @Test
    public void testQueryStat() throws Exception {

        List<AttachSubUrlStatViewItem> statViewItems = attachSubUrlStatService.queryStat(request);
        for (AttachSubUrlStatViewItem item : statViewItems) {
            System.out.println(item);
        }

    }
}