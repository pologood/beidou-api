package com.baidu.beidou.api.external.tool.exporter.rpc;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.tool.exporter.ToolService;
import com.baidu.beidou.api.external.tool.vo.AdInfo;
import com.baidu.beidou.api.external.tool.vo.FCKeyword;
import com.baidu.beidou.api.external.tool.vo.OneReportRequestType;
import com.baidu.beidou.api.external.tool.vo.PackType;
import com.baidu.beidou.api.external.tool.vo.SiteInfo;
import com.baidu.beidou.api.external.tool.vo.request.GetAdInfoRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetFCKeywordByUnitIdsRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetLastHistoryRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetOneReportRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetPackByGroupPackIdRequest;
import com.baidu.beidou.api.external.tool.vo.request.GetSiteInfoRequest;
import com.baidu.beidou.api.external.tool.vo.response.GetLastHistoryResponse;
import com.baidu.beidou.api.external.tool.vo.response.GetOneReportResponse;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.util.DateUtils;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.tools.api.common.CommonRequest;
import com.baidu.fengchao.tools.api.common.CommonResponse;
import com.baidu.fengchao.tools.api.common.DRAPIMountAPI;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
@Ignore
public class ToolServiceTest extends ApiBaseRPCTest<ToolService> {

	@Test
	public void testGetAdInfo() throws Exception {
		ToolService exporter = getServiceProxy(ToolService.class, ApiExternalConstant.TOOL_SERVICE_URL);
		GetAdInfoRequest request = new GetAdInfoRequest();
		//request.setAdIds(new long[]{});
		request.setAdIds(new long[] { 1589576l, 1587994l, 1589573l });
		BaseResponse<AdInfo> result = exporter.getAdInfo(dataUser2, request, apiOption2);
		System.out.println(result);
	}

	@Test
	public void testGetAdInfo2() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);

		BaseRequestUser user = new BaseRequestUser(499l, 499l);
		CommonRequest params = new CommonRequest();

		params.setServiceName("ToolService");
		params.setMethodName("getAdInfo");
		GetAdInfoRequest request = new GetAdInfoRequest();
		request.setAdIds(new long[] { 1l, 2l });
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String paramStringJson = mapper.writeValueAsString(request);
		System.out.println(paramStringJson);
		params.setParameterJSON(paramStringJson);

		//BaseRequestOptions options=new BaseRequestOptions(1,1);
		BaseResponse<CommonResponse> result = drAPIMountAPI.invoke(user, params, null);

		System.out.println(result);
	}

	@Test
	public void testGetSiteInfo() throws Exception {
		ToolService exporter = getServiceProxy(ToolService.class, ApiExternalConstant.TOOL_SERVICE_URL);
		GetSiteInfoRequest request = new GetSiteInfoRequest();
		//request.setAdIds(new long[]{});
		request.setSites(new String[] { "sina.com.cn", "tieba.baidu.com", "nonono.cn" });
		//request.setAdIds(new long[]{13375l,1587994l,1589573l});
		BaseResponse<SiteInfo> result = exporter.getSiteInfo(dataUser2, request, apiOption2);
		System.out.println(result);
	}

	@Test
	public void testGetSiteInfo2() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);

		BaseRequestUser user = new BaseRequestUser(499l, 499l);
		CommonRequest params = new CommonRequest();

		params.setServiceName("ToolService");
		params.setMethodName("getSiteInfo");
		GetSiteInfoRequest request = new GetSiteInfoRequest();
		request.setSites(new String[] { "sina.com.cn", "tieba.baidu.com", "nonono.cn" });
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String paramStringJson = mapper.writeValueAsString(request);
		System.out.println(paramStringJson);
		params.setParameterJSON(paramStringJson);

		//BaseRequestOptions options=new BaseRequestOptions(1,1);
		BaseResponse<CommonResponse> result = drAPIMountAPI.invoke(user, params, null);

		System.out.println(result);
	}

	@Test
	public void testGetFCKeywordByUnitIds() throws Exception {
		ToolService exporter = getServiceProxy(ToolService.class, ApiExternalConstant.TOOL_SERVICE_URL);
		GetFCKeywordByUnitIdsRequest request = new GetFCKeywordByUnitIdsRequest();
		request.setUnitIds(new long[] {});
		BaseResponse<FCKeyword> result = exporter.getFCKeywordByUnitIds(dataUser2, request, apiOption2);
		System.out.println(result);
	}

	@Test
	public void testGetFCKeywordByUnitIds2() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);

		BaseRequestUser user = new BaseRequestUser(499l, 499l);
		CommonRequest params = new CommonRequest();

		params.setServiceName("ToolService");
		params.setMethodName("getFCKeywordByUnitIds");
		GetFCKeywordByUnitIdsRequest request = new GetFCKeywordByUnitIdsRequest();
		request.setUnitIds(new long[] { 95648014l, 2l });
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String paramStringJson = mapper.writeValueAsString(request);
		System.out.println(paramStringJson);
		params.setParameterJSON(paramStringJson);

		//BaseRequestOptions options=new BaseRequestOptions(1,1);
		BaseResponse<CommonResponse> result = drAPIMountAPI.invoke(user, params, null);

		System.out.println(result);
	}

	@Test
	public void testGetPackByGroupPackId() throws Exception {
		ToolService exporter = getServiceProxy(ToolService.class, ApiExternalConstant.TOOL_SERVICE_URL);
		GetPackByGroupPackIdRequest request = new GetPackByGroupPackIdRequest();
		request.setGpids(new long[] { 805l, 806l, 3l });
		BaseResponse<PackType> result = exporter.getPackByGroupPackId(dataUser2, request, apiOption2);
		System.out.println(result);
	}
	
	@Test
	public void testGetOneReport()throws Exception{
		ToolService exporter = getServiceProxy(ToolService.class, ApiExternalConstant.TOOL_SERVICE_URL);
		GetOneReportRequest request = new GetOneReportRequest();
		OneReportRequestType requestType = new OneReportRequestType();
		String startDate = "20120606";
		String endDate = "20120610";
		requestType.setStartDate(DateUtils.strToDate(startDate));
		requestType.setEndDate(DateUtils.strToDate(endDate));
		requestType.setReportType(2);
		requestType.setStatRange(2);
		requestType.setStatIds(new long[]{1857880,1775128});
		
		request.setOneReportRequestType(requestType);
		BaseResponse<GetOneReportResponse> result = exporter.getOneReport(dataUser2, request, apiOption2);
		System.out.println(result);
	}
	
	@Test
	public void testGetLastHistory()throws Exception{
		ToolService exporter = getServiceProxy(ToolService.class, ApiExternalConstant.TOOL_SERVICE_URL);
		GetLastHistoryRequest request = new GetLastHistoryRequest();
		BaseResponse<GetLastHistoryResponse> result = exporter.getLastHistory(dataUser2, request, apiOption2);
		System.out.println(result);
	}
}
