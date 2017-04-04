package com.baidu.beidou.api.integratetest.exporter.impl;

import static org.hamcrest.Matchers.is;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.report.exporter.ApiReportService;
import com.baidu.beidou.api.external.report.vo.ReportRequestType;
import com.baidu.beidou.api.external.report.vo.request.GetReportFileUrlRequest;
import com.baidu.beidou.api.external.report.vo.request.GetReportIdRequest;
import com.baidu.beidou.api.external.report.vo.request.GetReportStateRequest;
import com.baidu.beidou.api.external.report.vo.response.GetReportIdResponse;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.test.common.AbstractShardXdbTestCaseLegacy;
import com.baidu.beidou.util.DateUtils;

/**
 * 报告服务单测
 * 
 * @author work
 * 
 */
@Ignore
public class ApiReportServiceImplTest extends AbstractShardXdbTestCaseLegacy {

    private static int userId = 19;

    @Resource
    private ApiReportService apiReportService;

    @SuppressWarnings("rawtypes")
    @Test
    @Rollback(true)
    public void testGetReportId() throws Exception {
        DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
        ApiOption apiOption = DarwinApiHelper.getApiOptions();

        GetReportIdRequest request = new GetReportIdRequest();
        ReportRequestType type = new ReportRequestType();
        // type.setPerformanceData(new String[]{"SrCh","acp","srch","ctr","acp","cost"});
        type.setPerformanceData(new String[] {});
        String start = "20120102";
        String end = "20120102";
        type.setStartDate(DateUtils.strToDate(start));
        type.setEndDate(DateUtils.strToDate(end));
        type.setFormat(0);
        type.setIdOnly(false);
        type.setReportType(3);
        type.setStatRange(1);
        // type.setStatIds(new long[]{757447l, 757448l});
        request.setReportRequestType(type);
        ApiResult result = apiReportService.getReportId(dataUser, request, apiOption);

        // 打印返回
        System.out.println(result);
        Thread.sleep(10000);

        // 成功后没有error，并且返回该用户的信息
        assertThat(result.getErrors(), nullValue());
        assertThat(result.getData().size(), is(1));
        assertThat(result.getPayment().getTotal(), is(1));
        assertThat(result.getPayment().getSuccess(), is(1));

        // 验证结果
        GetReportIdResponse reportId = (GetReportIdResponse) result.getData().get(0);
        assertThat(reportId.getReportId().length(), is(32));
        // assertThat(accountInfo.getUsername(), is("cprotest1"));
        // assertThat(accountInfo.getBalance(), is(balance.floatValue()));

    }

    @SuppressWarnings("rawtypes")
    @Test
    @Rollback(true)
    public void testGetReportState() throws Exception {
        DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
        ApiOption apiOption = DarwinApiHelper.getApiOptions();

        String reportId = "648d5dc3ca33087f13a1ff7e3cdbd876";
        GetReportStateRequest request = new GetReportStateRequest();
        request.setReportId(reportId);
        ApiResult result = apiReportService.getReportState(dataUser, request, apiOption);

        // 打印返回
        System.out.println(result);

        // // 成功后没有error，并且返回该用户的信息
        // assertThat(result.getErrors(), nullValue());
        // assertThat(result.getData().size(), is(1));
        // assertThat(result.getPayment().getTotal(), is(1));
        // assertThat(result.getPayment().getSuccess(), is(1));
        //
        // // 验证结果
        // AccountInfoType accountInfo = (AccountInfoType)result.getData().get(0);
        // assertThat(accountInfo.getUserid(), is(userId));
        // assertThat(accountInfo.getUsername(), is("cprotest1"));
        // assertThat(accountInfo.getBalance(), is(balance.floatValue()));

    }

    @SuppressWarnings("rawtypes")
    @Test
    @Rollback(true)
    public void testGetReportFileUrl() throws Exception {
        DataPrivilege dataUser = DarwinApiHelper.getDataPrivilege(userId, userId);
        ApiOption apiOption = DarwinApiHelper.getApiOptions();

        String reportId = "648d5dc3ca33087f13a1ff7e3cdbd876";
        GetReportFileUrlRequest request = new GetReportFileUrlRequest();
        request.setReportId(reportId);
        ApiResult result = apiReportService.getReportFileUrl(dataUser, request, apiOption);

        // 打印返回
        System.out.println(result);

        // // 成功后没有error，并且返回该用户的信息
        // assertThat(result.getErrors(), nullValue());
        // assertThat(result.getData().size(), is(1));
        // assertThat(result.getPayment().getTotal(), is(1));
        // assertThat(result.getPayment().getSuccess(), is(1));
        //
        // // 验证结果
        // AccountInfoType accountInfo = (AccountInfoType)result.getData().get(0);
        // assertThat(accountInfo.getUserid(), is(userId));
        // assertThat(accountInfo.getUsername(), is("cprotest1"));
        // assertThat(accountInfo.getBalance(), is(balance.floatValue()));

    }

}
