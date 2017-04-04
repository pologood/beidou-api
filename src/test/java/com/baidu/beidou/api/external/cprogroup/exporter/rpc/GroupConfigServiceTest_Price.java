package com.baidu.beidou.api.external.cprogroup.exporter.rpc;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.vo.PriceType;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetPriceRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetPriceRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
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

/**
 * GroupConfigServiceTest_Price
 * 
 * @author work
 * 
 */
@Ignore
public class GroupConfigServiceTest_Price extends ApiBaseRPCTest<GroupConfigService> {

	@Test
	public void testGetPrice() throws Exception {
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		GetPriceRequest request = new GetPriceRequest();
		request.setGroupIds(new long[] { 1l });
		long start = System.currentTimeMillis();
		BaseResponse<PriceType> result = exporter.getPrice(dataUser2, request, apiOption2);
		long end = System.currentTimeMillis();
		System.out.println("Using " + (end - start) + "ms");
		System.out.println(result);
	}

	@Test
	public void testGetPrice2() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499, 499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();

		params.setServiceName("GroupConfigService");
		params.setMethodName("getPrice");
		GetPriceRequest request = new GetPriceRequest();
		request.setGroupIds(new long[] { 111l });
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
		PriceType[] result1 = gson.fromJson(result.getData()[0].getResponseData(), PriceType[].class);

		System.out.println(result1[0]);
	}

	@Test
	public void testSetPrice() throws Exception {
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetPriceRequest request = new SetPriceRequest();
		List<PriceType> prices = new ArrayList<PriceType>();

		PriceType type1 = new PriceType();
		type1.setGroupId(228);
		type1.setId1(112);
		type1.setPrice(300);
		type1.setType(1);
		prices.add(type1);

//		PriceType type2 = new PriceType();
//		type2.setGroupId(228l);
//		type2.setId1(1);
//		type2.setPrice(100);
//		type2.setType(1);
//		prices.add(type2);

		request.setPrices(prices);

		BaseResponse<PlaceHolderResult> result = exporter.setPrice(dataUser2, request, apiOption2);
		System.out.println(result);
	}

	@Test
	public void testSetPrice2() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499, 499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();

		params.setServiceName("GroupConfigService");
		params.setMethodName("setPrice");

		SetPriceRequest request = new SetPriceRequest();
		List<PriceType> prices = new ArrayList<PriceType>();

		PriceType type1 = new PriceType();
		type1.setGroupId(228l);
		type1.setId1(1);
		type1.setPrice(100);
		type1.setType(1);
		prices.add(type1);

		PriceType type2 = new PriceType();
		type2.setGroupId(228l);
		type2.setId1(1);
		type2.setPrice(100);
		type2.setType(1);
		prices.add(type2);

		request.setPrices(prices);

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

	}

}
