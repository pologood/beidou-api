package com.baidu.beidou.api.external.kr.exporter.rpc;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.kr.exporter.KrService;
import com.baidu.beidou.api.external.kr.vo.KRResponse;
import com.baidu.beidou.api.external.kr.vo.KRResultType;
import com.baidu.beidou.api.external.kr.vo.KRSuggestResultType;
import com.baidu.beidou.api.external.kr.vo.request.GetKRBySeedRequest;
import com.baidu.beidou.api.external.kr.vo.request.GetKRSuggestBySeedRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.ApiResult;
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
public class KrServiceTest extends ApiBaseRPCTest<KrService> {

	@Test
	public void testGetKRBySeed() throws Exception{
		KrService exporter = getServiceProxy(KrService.class, ApiExternalConstant.KR_SERVICE_URL);
		GetKRBySeedRequest request = new GetKRBySeedRequest();
		request.setSeed("123");
		request.setGroupId(3474310l);
		request.setAliveDays(15);
		request.setTargetType(7);
		long start = System.currentTimeMillis();
		BaseResponse<KRResultType> result = exporter.getKRBySeed(dataUser2, request, apiOption2);
		long end = System.currentTimeMillis();
		System.out.println("Using " + (end - start) + "ms");
		System.out.println(result);
	}
	
	@Test
	public void testGetKRBySeed2() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);
		
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499,499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();
		
		params.setServiceName("KrService");
		params.setMethodName("getKRBySeed");
		GetKRBySeedRequest request = new GetKRBySeedRequest();
		request.setSeed("汽车");
		request.setGroupId(228);
		request.setRegionList(new int[]{1,2,3});
		request.setAliveDays(15);
		request.setTargetType(7);
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String paramStringJson = mapper.writeValueAsString(request);
		System.out.println(paramStringJson);
		params.setParameterJSON(paramStringJson);
		
		long start = System.currentTimeMillis();
		BaseResponse<CommonResponse> result = drAPIMountAPI.invoke(user, params, options);
		long end = System.currentTimeMillis();
		System.out.println("Using " + (end - start) + "ms");
		
		System.out.println(result);
		System.out.println(result.getData()[0].getResponseData());
		
		Gson gson = new Gson();
		KRResultType[] result1 = gson.fromJson(result.getData()[0].getResponseData(), KRResultType[].class);
		
		System.out.println(result1[0]);
	}
	
	@Test
	public void testGetKRSuggestBySeed() throws Exception{
		KrService exporter = getServiceProxy(KrService.class, ApiExternalConstant.KR_SERVICE_URL);
		GetKRSuggestBySeedRequest request = new GetKRSuggestBySeedRequest();
		request.setSeed("123");
		request.setGroupId(3474310l);
		request.setAliveDays(15);
		request.setTargetType(7);
		long start = System.currentTimeMillis();
		ApiResult<KRSuggestResultType>  result = exporter.getKRSuggestBySeed(dataUser, request, apiOption);
		long end = System.currentTimeMillis();
		System.out.println("Using " + (end - start) + "ms");
		System.out.println(result);
	}
	
	@Test
	public void testGetKRAndSuggestBySeed() throws Exception{
		KrService exporter = getServiceProxy(KrService.class, ApiExternalConstant.KR_SERVICE_URL);
		GetKRBySeedRequest request = new GetKRBySeedRequest();
		request.setSeed("醋");
		request.setGroupId(3474310l);
		request.setAliveDays(15);
		request.setTargetType(7);
		long start = System.currentTimeMillis();
		ApiResult<KRResponse>  result = exporter.getKRAndSuggestBySeed(dataUser, request, apiOption);
		long end = System.currentTimeMillis();
		System.out.println("Using " + (end - start) + "ms");
		System.out.println(result);
	}
}
