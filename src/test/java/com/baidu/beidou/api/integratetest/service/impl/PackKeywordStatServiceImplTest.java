/**
 * 
 */
package com.baidu.beidou.api.integratetest.service.impl;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.report.service.impl.PackKeywordStatServiceImpl;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.report.vo.KeywordStatViewItem;
import com.baidu.beidou.api.util.ServiceLocator;
import com.baidu.beidou.util.DateUtils;
import com.baidu.unbiz.common.Assert;
import com.baidu.unbiz.common.CollectionUtil;

/**
 * PackKeywordStatServiceImplTest
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年8月19日 上午1:31:05
 */
@Ignore
public class PackKeywordStatServiceImplTest {

    /**
     * queryStat
     * 
     * @throws Exception Exception
     */
    @Test
    public void queryData() throws Exception {

        PackKeywordStatServiceImpl service;
        String beanName = "packKeywordStatService";
        service = (PackKeywordStatServiceImpl) (ServiceLocator.getInstance().getBeanByName(beanName));
        ApiReportQueryParameter request = new ApiReportQueryParameter();
        Date startDate = DateUtils.strToDate("20140625");
        Date endDate = DateUtils.strToDate("20140630");
        request.setUserid(5424427);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        // List<Integer> planIds = new ArrayList<Integer>();
        // planIds.add(771017);
        // planIds.add(771014);
        // planIds.add(771001);
        // planIds.add(771005);
        // planIds.add(328475);
        // request.setPlanIds(planIds);
        List<Integer> planIds = CollectionUtil.createArrayList();
        planIds.add(1813524);
        // planIds.add(1974692);
        request.setPlanIds(planIds);
        // planIds.add(1987418);8980404
        List<Integer> groupIds = CollectionUtil.createArrayList();
        groupIds.add(6384348);
        // groupIds.add(8604652);
        // planIds.add(740918);// not me
        request.setGroupIds(groupIds);

        request.setIdOnly(false);
        request.setNeedTransHolmes(true);
        request.setNeedUv(true);

        List<KeywordStatViewItem> result = service.queryStat(request);
        Assert.assertTrue(CollectionUtil.isNotEmpty(result));
        for (KeywordStatViewItem item : result) {
            // if (item.getGpId() == 156180) {
            System.out.println(item);
            // }
        }

    }

}
