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
import com.baidu.beidou.api.external.cprogroup.vo.ExcludePeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludePeopleType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludePeopleRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludePeopleRequest;
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
 * 排除人群
 * 
 * @author work
 * 
 */
@Ignore
public class GroupConfigServiceTest_ExcludePeople extends ApiBaseRPCTest<GroupConfigService> {

	@Test
	public void testGetExcludePeople() throws Exception {
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		GetExcludePeopleRequest request = new GetExcludePeopleRequest();
		request.setGroupIds(new long[] { 1l });
		long start = System.currentTimeMillis();
		BaseResponse<ExcludePeopleType> result = exporter.getExcludePeople(dataUser2, request, apiOption2);
		long end = System.currentTimeMillis();
		System.out.println("Using " + (end - start) + "ms");
		System.out.println(result);
	}

	@Test
	public void testGetExcludePeople2() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499, 499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();

		params.setServiceName("GroupConfigService");
		params.setMethodName("getExcludePeople");
		GetExcludePeopleRequest request = new GetExcludePeopleRequest();
		request.setGroupIds(new long[] { 228l });
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
		ExcludePeopleType[] result1 = gson.fromJson(result.getData()[0].getResponseData(), ExcludePeopleType[].class);

		System.out.println(result1[0]);
	}

	@Test
	public void testSetExcludePeople() throws Exception {
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetExcludePeopleRequest request = new SetExcludePeopleRequest();
		ExcludePeopleType type = new ExcludePeopleType();
		type.setGroupId(228l);

		List<Long> ids = new ArrayList<Long>();
		ids.add(1l);
		ids.add(2l);
		ids.add(3l);
		type.setExcludePeopleIds(ids);
		request.setExcludePeople(type);

		BaseResponse<PlaceHolderResult> result = exporter.setExcludePeople(dataUser2, request, apiOption2);
		System.out.println(result);
	}

	@Test
	public void testSetExcludePeople2() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499, 499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();

		params.setServiceName("GroupConfigService");
		params.setMethodName("setExcludePeople");

		SetExcludePeopleRequest request = new SetExcludePeopleRequest();
		ExcludePeopleType type = new ExcludePeopleType();
		type.setGroupId(228l);
		List<Long> ids = new ArrayList<Long>();
		ids.add(1l);
		ids.add(2l);
		ids.add(3l);
		type.setExcludePeopleIds(ids);
		request.setExcludePeople(type);

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

	@Test
	public void testAddExcludePeople() throws Exception {
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);

		AddExcludePeopleRequest request = new AddExcludePeopleRequest();
		List<GroupExcludePeopleType> excludePeoples = new ArrayList<GroupExcludePeopleType>();

		GroupExcludePeopleType type1 = new GroupExcludePeopleType();
		type1.setGroupId(244540l);
		type1.setExcludePeopleId(1l);

		GroupExcludePeopleType type2 = new GroupExcludePeopleType();
		type2.setGroupId(244540l);
		type2.setExcludePeopleId(1l);

		GroupExcludePeopleType type3 = new GroupExcludePeopleType();
		type3.setGroupId(244540l);
		type3.setExcludePeopleId(1l);

		GroupExcludePeopleType type4 = new GroupExcludePeopleType();
		type4.setGroupId(244540l);
		type4.setExcludePeopleId(1l);

		GroupExcludePeopleType type5 = new GroupExcludePeopleType();
		type5.setGroupId(244540l);
		type5.setExcludePeopleId(1l);

		excludePeoples.add(type1);
		excludePeoples.add(type2);
		excludePeoples.add(type3);
		excludePeoples.add(type4);
		excludePeoples.add(type5);
		request.setExcludePeoples(excludePeoples);

		BaseResponse<PlaceHolderResult> result = exporter.addExcludePeople(dataUser2, request, apiOption2);
		System.out.println(result);
	}

	@Test
	public void testAddExcludePeople2() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499, 499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();

		params.setServiceName("GroupConfigService");
		params.setMethodName("addExcludePeople");

		AddExcludePeopleRequest request = new AddExcludePeopleRequest();
		List<GroupExcludePeopleType> excludePeoples = new ArrayList<GroupExcludePeopleType>();

		GroupExcludePeopleType type1 = new GroupExcludePeopleType();
		type1.setGroupId(244540l);
		type1.setExcludePeopleId(1l);

		GroupExcludePeopleType type2 = new GroupExcludePeopleType();
		type2.setGroupId(244540l);
		type2.setExcludePeopleId(1l);

		GroupExcludePeopleType type3 = new GroupExcludePeopleType();
		type3.setGroupId(244540l);
		type3.setExcludePeopleId(1l);

		GroupExcludePeopleType type4 = new GroupExcludePeopleType();
		type4.setGroupId(244540l);
		type4.setExcludePeopleId(1l);

		GroupExcludePeopleType type5 = new GroupExcludePeopleType();
		type5.setGroupId(244540l);
		type5.setExcludePeopleId(1l);

		excludePeoples.add(type1);
		excludePeoples.add(type2);
		excludePeoples.add(type3);
		excludePeoples.add(type4);
		excludePeoples.add(type5);
		request.setExcludePeoples(excludePeoples);

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

	}

	@Test
	public void testDeleteExcludePeople() throws Exception {
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);

		DeleteExcludePeopleRequest request = new DeleteExcludePeopleRequest();
		List<GroupExcludePeopleType> excludePeoples = new ArrayList<GroupExcludePeopleType>();

		GroupExcludePeopleType type1 = new GroupExcludePeopleType();
		type1.setGroupId(244540l);
		type1.setExcludePeopleId(1l);

		GroupExcludePeopleType type2 = new GroupExcludePeopleType();
		type2.setGroupId(244540l);
		type2.setExcludePeopleId(1l);

		GroupExcludePeopleType type3 = new GroupExcludePeopleType();
		type3.setGroupId(244540l);
		type3.setExcludePeopleId(1l);

		GroupExcludePeopleType type4 = new GroupExcludePeopleType();
		type4.setGroupId(244540l);
		type4.setExcludePeopleId(1l);

		GroupExcludePeopleType type5 = new GroupExcludePeopleType();
		type5.setGroupId(244540l);
		type5.setExcludePeopleId(1l);

		excludePeoples.add(type1);
		excludePeoples.add(type2);
		excludePeoples.add(type3);
		excludePeoples.add(type4);
		excludePeoples.add(type5);
		request.setExcludePeoples(excludePeoples);

		BaseResponse<PlaceHolderResult> result = exporter.deleteExcludePeople(dataUser2, request, apiOption2);
		System.out.println(result);
	}

	@Test
	public void testDeleteExcludePeople2() throws Exception {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);

		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499, 499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();

		params.setServiceName("GroupConfigService");
		params.setMethodName("deleteExcludePeople");

		DeleteExcludePeopleRequest request = new DeleteExcludePeopleRequest();
		List<GroupExcludePeopleType> excludePeoples = new ArrayList<GroupExcludePeopleType>();

		GroupExcludePeopleType type1 = new GroupExcludePeopleType();
		type1.setGroupId(244540l);
		type1.setExcludePeopleId(1l);

		GroupExcludePeopleType type2 = new GroupExcludePeopleType();
		type2.setGroupId(244540l);
		type2.setExcludePeopleId(1l);

		GroupExcludePeopleType type3 = new GroupExcludePeopleType();
		type3.setGroupId(244540l);
		type3.setExcludePeopleId(1l);

		GroupExcludePeopleType type4 = new GroupExcludePeopleType();
		type4.setGroupId(244540l);
		type4.setExcludePeopleId(1l);

		GroupExcludePeopleType type5 = new GroupExcludePeopleType();
		type5.setGroupId(244540l);
		type5.setExcludePeopleId(1l);

		excludePeoples.add(type1);
		excludePeoples.add(type2);
		excludePeoples.add(type3);
		excludePeoples.add(type4);
		excludePeoples.add(type5);
		request.setExcludePeoples(excludePeoples);

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

	}

}
