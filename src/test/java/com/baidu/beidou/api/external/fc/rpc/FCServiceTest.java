package com.baidu.beidou.api.external.fc.rpc;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.fc.exporter.FCService;
import com.baidu.beidou.api.external.fc.vo.FCCampaignUnitIdType;
import com.baidu.beidou.api.external.fc.vo.FCCampaignUnitType;
import com.baidu.beidou.api.external.fc.vo.request.GetFCUnitByFCCampaignIdsRequest;
import com.baidu.beidou.api.external.fc.vo.request.GetFCUnitIdByFCCampaignIdsRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.tools.api.common.CommonRequest;
import com.baidu.fengchao.tools.api.common.CommonResponse;
import com.baidu.fengchao.tools.api.common.DRAPIMountAPI;
import com.baidu.gson.Gson;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
@Ignore
public class FCServiceTest extends ApiBaseRPCTest<FCService> {

	@Test
	public void testGetFCUnitByFCCampaignIds() throws Exception {
		FCService exporter = getServiceProxy(FCService.class, ApiExternalConstant.FC_SERVICE_URL);

		GetFCUnitByFCCampaignIdsRequest request = new GetFCUnitByFCCampaignIdsRequest();
		request.setCampaignIds(new long[] { 67201314l, 67147881l, 22077418l });
		BaseResponse<FCCampaignUnitType> result = exporter.getFCUnitByFCCampaignIds(dataUser2, request, apiOption2);
		System.out.println(result);
	}

	@Test
	public void testGetFCUnitByFCCampaignIds2() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499, 499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();

		params.setServiceName("FCService");
		params.setMethodName("getFCUnitByFCCampaignIds");
		GetFCUnitByFCCampaignIdsRequest request = new GetFCUnitByFCCampaignIdsRequest();
		request.setCampaignIds(new long[] { 67201314l, 67147881l, 22077418l });
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String paramStringJson = mapper.writeValueAsString(request);
		System.out.println(paramStringJson);
		params.setParameterJSON(paramStringJson);

		BaseResponse<CommonResponse> result = drAPIMountAPI.invoke(user, params, options);

		System.out.println(result);
		System.out.println(result.getData()[0].getResponseData());

		Gson gson = new Gson();
		FCCampaignUnitType[] result1 = gson.fromJson(result.getData()[0].getResponseData(), FCCampaignUnitType[].class);

		System.out.println(result1[0]);
	}

	@Test
	public void testGetFCUnitIdByFCCampaignIds() throws Exception {
		FCService exporter = getServiceProxy(FCService.class, ApiExternalConstant.FC_SERVICE_URL);

		GetFCUnitIdByFCCampaignIdsRequest request = new GetFCUnitIdByFCCampaignIdsRequest();
		request.setCampaignIds(new long[] { 67201314l, 67147881l, 22077418l });
		BaseResponse<FCCampaignUnitIdType> result = exporter.getFCUnitIdByFCCampaignIds(dataUser2, request, apiOption2);
		System.out.println(result);
	}

	@Test
	public void testGetFCUnitIdByFCCampaignIds2() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499, 499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();

		params.setServiceName("FCService");
		params.setMethodName("getFCUnitIdByFCCampaignIds");
		GetFCUnitIdByFCCampaignIdsRequest request = new GetFCUnitIdByFCCampaignIdsRequest();
		request.setCampaignIds(new long[] { 67201314l, 67147881l, 22077418l });
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String paramStringJson = mapper.writeValueAsString(request);
		System.out.println(paramStringJson);
		params.setParameterJSON(paramStringJson);

		BaseResponse<CommonResponse> result = drAPIMountAPI.invoke(user, params, options);

		System.out.println(result);
		System.out.println(result.getData()[0].getResponseData());

		Gson gson = new Gson();
		FCCampaignUnitIdType[] result1 = gson.fromJson(result.getData()[0].getResponseData(), FCCampaignUnitIdType[].class);

		System.out.println(result1[0]);
	}

}
