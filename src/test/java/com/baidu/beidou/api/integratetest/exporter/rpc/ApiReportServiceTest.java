package com.baidu.beidou.api.integratetest.exporter.rpc;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.report.exporter.ApiReportService;
import com.baidu.beidou.api.external.report.vo.ReportRequestType;
import com.baidu.beidou.api.external.report.vo.request.GetReportFileUrlRequest;
import com.baidu.beidou.api.external.report.vo.request.GetReportIdRequest;
import com.baidu.beidou.api.external.report.vo.request.GetReportStateRequest;
import com.baidu.beidou.api.external.report.vo.response.GetReportFileUrlResponse;
import com.baidu.beidou.api.external.report.vo.response.GetReportIdResponse;
import com.baidu.beidou.api.external.report.vo.response.GetReportStateResponse;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.util.DateUtils;

/**
 * 报告服务rpc单测
 * 
 * @author work
 * 
 */
@Ignore
public class ApiReportServiceTest extends ApiBaseRPCTest<ApiReportService> {

    @Test
    public void testGetReportId() throws Exception {
        ApiReportService exporter = getServiceProxy(ApiReportService.class, ApiExternalConstant.APIREPORT_SERVICE_URL);

        GetReportIdRequest request = new GetReportIdRequest();
        ReportRequestType type = new ReportRequestType();
        // type.setPerformanceData(new String[]{"SrCh","acp","srch","ctr","acp","cost"});
        type.setPerformanceData(new String[] { "srch", "click", "cost", "cpm", "ctr", "acp", "srchuv", "clickuv",
                "srsur", "cusur", "cocur", "arrivalRate", "hoprate", "avgResTime", "directTrans", "indirectTrans" });
        // type.setPerformanceData(new String[]{});
        String start = "20140501";
        String end = "2014502";
        type.setStartDate(DateUtils.strToDate(start));
        type.setEndDate(DateUtils.strToDate(end));
        type.setFormat(1);
        type.setIdOnly(false);
        type.setReportType(2);
        type.setStatRange(1);
        // type.setStatIds(new long[] {757447});
        // type.setStatIds(new long[]{1975824, 1201794});
        // type.setStatIds(new long[]{9283622l, 9283621l, 9283628l, 9283629l});
        request.setReportRequestType(type);
        ApiResult<GetReportIdResponse> result = exporter.getReportId(dataUser, request, apiOption);
        System.out.println(result);

    }

    @Test
    public void testGetReportState() throws Exception {
        ApiReportService exporter = getServiceProxy(ApiReportService.class, ApiExternalConstant.APIREPORT_SERVICE_URL);

        String reportId = "eba797fe33225785c77136f844db2a9c";
        GetReportStateRequest request = new GetReportStateRequest();
        request.setReportId(reportId);
        ApiResult<GetReportStateResponse> result = exporter.getReportState(dataUser, request, apiOption);

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

    @Test
    public void testGetReportFileUrl() throws Exception {
        ApiReportService exporter = getServiceProxy(ApiReportService.class, ApiExternalConstant.APIREPORT_SERVICE_URL);

        String reportId = "7f943fc6f4321402fff1a56173e7f1bb";
        GetReportFileUrlRequest request = new GetReportFileUrlRequest();
        request.setReportId(reportId);
        ApiResult<GetReportFileUrlResponse> result = exporter.getReportFileUrl(dataUser, request, apiOption);

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
