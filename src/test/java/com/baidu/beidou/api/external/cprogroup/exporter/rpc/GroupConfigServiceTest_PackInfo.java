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
import com.baidu.beidou.api.external.cprogroup.vo.GroupPackItemType;
import com.baidu.beidou.api.external.cprogroup.vo.PackInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.PackItemType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddPackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeletePackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetPackInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetPackInfoRequest;
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
 * GroupConfigServiceTest_PackInfo
 * 
 * @author work
 * 
 */
@Ignore
public class GroupConfigServiceTest_PackInfo extends ApiBaseRPCTest<GroupConfigService> {

	@Test
	public void testGetPackInfo() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		GetPackInfoRequest request = new GetPackInfoRequest();
		request.setGroupIds(new long[]{1l});
		long start = System.currentTimeMillis();
		BaseResponse<PackInfoType> result = exporter.getPackInfo(dataUser2, request, apiOption2);
		long end = System.currentTimeMillis();
		System.out.println("Using " + (end - start) + "ms");
		System.out.println(result);
	}
	
	@Test
	public void testGetPackInfo2() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);
		
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499,499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();
		
		params.setServiceName("GroupConfigService");
		params.setMethodName("getPackInfo");
		GetPackInfoRequest request = new GetPackInfoRequest();
		request.setGroupIds(new long[]{111l});
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
		PackInfoType[] result1 = gson.fromJson(result.getData()[0].getResponseData(), PackInfoType[].class);
		
		System.out.println(result1[0]);
	}
	
	@Test
	public void testSetPackInfo() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetPackInfoRequest request = new SetPackInfoRequest();
		PackInfoType type = new PackInfoType();
		type.setGroupId(12000150L);
		List<PackItemType> packItems = new ArrayList<PackItemType>();
		
		PackItemType type1 = new PackItemType();
		type1.setType(3);
		type1.setPackId(2);
		
//		PackItemType type2 = new PackItemType();
//		type2.setType(0);
//		type2.setPackId(2);
		
		packItems.add(type1);
//		packItems.add(type2);
		
		type.setPackItems(packItems);
		
		request.setPackInfo(type);
		
		BaseResponse<PlaceHolderResult> result = exporter.setPackInfo(dataUser2, request, apiOption2);
		System.out.println(result);
	}
	
	@Test
	public void testSetPackInfo2() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);
		
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499,499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();
		
		params.setServiceName("GroupConfigService");
		params.setMethodName("setPackInfo");
		
		SetPackInfoRequest request = new SetPackInfoRequest();
		PackInfoType type = new PackInfoType();
		type.setGroupId(228l);
		List<PackItemType> packItems = new ArrayList<PackItemType>();
		
		PackItemType type1 = new PackItemType();
		type1.setType(0);
		type1.setPackId(2);
		
		PackItemType type2 = new PackItemType();
		type2.setType(0);
		type2.setPackId(2);
		
		packItems.add(type1);
		packItems.add(type2);
		
		type.setPackItems(packItems);
		
		request.setPackInfo(type);
		
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
	public void testAddPackInfo() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		
		AddPackInfoRequest request = new AddPackInfoRequest();
		List<GroupPackItemType> packs = new ArrayList<GroupPackItemType>();
		
		GroupPackItemType type1 = new GroupPackItemType();
		type1.setGroupId(12000150L);
		type1.setType(3);
		type1.setPackId(2);
		
//		GroupPackItemType type2 = new GroupPackItemType();
//		type2.setType(1);
//		type2.setPackId(1);
//		
//		GroupPackItemType type3 = new GroupPackItemType();
//		type3.setType(1);
//		type3.setPackId(1);
//		
//		GroupPackItemType type4 = new GroupPackItemType();
//		type4.setType(1);
//		type4.setPackId(1);
//		
//		GroupPackItemType type5 = new GroupPackItemType();
//		type5.setType(1);
//		type5.setPackId(1);
//		
		packs.add(type1);
//		packs.add(type2);
//		packs.add(type3);
//		packs.add(type4);
//		packs.add(type5);
		
		request.setPacks(packs);
		
		
		BaseResponse<PlaceHolderResult> result = exporter.addPackInfo(dataUser2, request, apiOption2);
		System.out.println(result);
	}
	
	@Test
	public void testAddPackInfo2() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);
		
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499,499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();
		
		params.setServiceName("GroupConfigService");
		params.setMethodName("addPackInfo");
		
		AddPackInfoRequest request = new AddPackInfoRequest();
		List<GroupPackItemType> packs = new ArrayList<GroupPackItemType>();
		
		GroupPackItemType type1 = new GroupPackItemType();
		type1.setGroupId(244540l);
		type1.setType(1);
		type1.setPackId(1);
		
		GroupPackItemType type2 = new GroupPackItemType();
		type2.setType(1);
		type2.setPackId(1);
		
		GroupPackItemType type3 = new GroupPackItemType();
		type3.setType(1);
		type3.setPackId(1);
		
		GroupPackItemType type4 = new GroupPackItemType();
		type4.setType(1);
		type4.setPackId(1);
		
		GroupPackItemType type5 = new GroupPackItemType();
		type5.setType(1);
		type5.setPackId(1);
		
		packs.add(type1);
		packs.add(type2);
		packs.add(type3);
		packs.add(type4);
		packs.add(type5);
		
		request.setPacks(packs);
		
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
	public void testDeletePackInfo() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		
		DeletePackInfoRequest request = new DeletePackInfoRequest();
		List<GroupPackItemType> packs = new ArrayList<GroupPackItemType>();
		
		GroupPackItemType type1 = new GroupPackItemType();
		type1.setGroupId(244540l);
		type1.setType(1);
		type1.setPackId(1);
		
		GroupPackItemType type2 = new GroupPackItemType();
		type2.setType(1);
		type2.setPackId(1);
		
		GroupPackItemType type3 = new GroupPackItemType();
		type3.setType(1);
		type3.setPackId(1);
		
		GroupPackItemType type4 = new GroupPackItemType();
		type4.setType(1);
		type4.setPackId(1);
		
		GroupPackItemType type5 = new GroupPackItemType();
		type5.setType(1);
		type5.setPackId(1);
		
		packs.add(type1);
		packs.add(type2);
		packs.add(type3);
		packs.add(type4);
		packs.add(type5);
		
		request.setPacks(packs);
		
		
		BaseResponse<PlaceHolderResult> result = exporter.deletePackInfo(dataUser2, request, apiOption2);
		System.out.println(result);
	}
	
	@Test
	public void testDeletePackInfo2() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);
		
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499,499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();
		
		params.setServiceName("GroupConfigService");
		params.setMethodName("deletePackInfo");
		
		DeletePackInfoRequest request = new DeletePackInfoRequest();
		List<GroupPackItemType> packs = new ArrayList<GroupPackItemType>();
		
		GroupPackItemType type1 = new GroupPackItemType();
		type1.setGroupId(244540l);
		type1.setType(1);
		type1.setPackId(1);
		
		GroupPackItemType type2 = new GroupPackItemType();
		type2.setType(1);
		type2.setPackId(1);
		
		GroupPackItemType type3 = new GroupPackItemType();
		type3.setType(1);
		type3.setPackId(1);
		
		GroupPackItemType type4 = new GroupPackItemType();
		type4.setType(1);
		type4.setPackId(1);
		
		GroupPackItemType type5 = new GroupPackItemType();
		type5.setType(1);
		type5.setPackId(1);
		
		packs.add(type1);
		packs.add(type2);
		packs.add(type3);
		packs.add(type4);
		packs.add(type5);
		
		request.setPacks(packs);
		
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
