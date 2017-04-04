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
import com.baidu.beidou.api.external.cprogroup.vo.ExcludeKeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeKeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.KeywordType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeKeywordRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.SetExcludeKeywordRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.common.util.JsonUtils;
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
 * 单测
 * 
 * @author work
 * 
 */
@Ignore
public class GroupConfigServiceTest_ExcludeKeyword extends ApiBaseRPCTest<GroupConfigService> {

	@Test
	public void testGetExcludeKeyword() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		GetExcludeKeywordRequest request = new GetExcludeKeywordRequest();
		request.setGroupIds(new long[]{1l});
		long start = System.currentTimeMillis();
		BaseResponse<ExcludeKeywordType> result = exporter.getExcludeKeyword(dataUser2, request, apiOption2);
		long end = System.currentTimeMillis();
		System.out.println("Using " + (end - start) + "ms");
		System.out.println(JsonUtils.toJson(request));
		System.out.println(result);
	}
	
	@Test
	public void testGetExcludeKeyword2() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);
		
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499,499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();
		
		params.setServiceName("GroupConfigService");
		params.setMethodName("getExcludeKeyword");
		GetExcludeKeywordRequest request = new GetExcludeKeywordRequest();
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
		ExcludeKeywordType[] result1 = gson.fromJson(result.getData()[0].getResponseData(), ExcludeKeywordType[].class);
		
		System.out.println(result1[0]);
	}
	
	@Test
	public void testSetExcludeKeyword() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType excludeKeyword = new ExcludeKeywordType();
		excludeKeyword.setGroupId(132977);
		List<KeywordType> excludeKeywords = new ArrayList<KeywordType>();
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("总监");
		kt1.setPattern(0);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("经理");
		kt2.setPattern(1);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("董事长");
		kt3.setPattern(0);
		excludeKeywords.add(kt1);
		excludeKeywords.add(kt2);
		excludeKeywords.add(kt3);
		excludeKeyword.setExcludeKeywords(excludeKeywords);
		
		List<Integer> keywordPackIds = new ArrayList<Integer>();
		keywordPackIds.add(1);
		keywordPackIds.add(5);
//		excludeKeyword.setExcludeKeywordPackIds(keywordPackIds);
		
		request.setExcludeKeyword(excludeKeyword);
		
		BaseResponse<PlaceHolderResult> result = exporter.setExcludeKeyword(dataUser2, request, apiOption2);
		System.out.println(result);
	}
	
	@Test
	public void testSetExcludeKeyword2() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);
		
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499,499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();
		
		params.setServiceName("GroupConfigService");
		params.setMethodName("setExcludeKeyword");
		
		SetExcludeKeywordRequest request = new SetExcludeKeywordRequest();
		ExcludeKeywordType excludeKeyword = new ExcludeKeywordType();
		excludeKeyword.setGroupId(228l);
		List<KeywordType> excludeKeywords = new ArrayList<KeywordType>();
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("总监");
		kt1.setPattern(0);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("经理");
		kt2.setPattern(1);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("董事长");
		kt3.setPattern(0);
		excludeKeywords.add(kt1);
		excludeKeywords.add(kt2);
		excludeKeywords.add(kt3);
		excludeKeyword.setExcludeKeywords(excludeKeywords);
		
		List<Integer> keywordPackIds = new ArrayList<Integer>();
		keywordPackIds.add(1);
		keywordPackIds.add(2);
		excludeKeyword.setExcludeKeywordPackIds(keywordPackIds);
		
		request.setExcludeKeyword(excludeKeyword);
		
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
	public void testAddExcludeKeyword() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		
		AddExcludeKeywordRequest request = new AddExcludeKeywordRequest();
		List<GroupExcludeKeywordType> excludeKeywords = new ArrayList<GroupExcludeKeywordType>();
		
		GroupExcludeKeywordType type1 = new GroupExcludeKeywordType();
		type1.setGroupId(228l);
		type1.setType(0);
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("Ibm");
		kt1.setPattern(1);
		type1.setExcludeKeyword(kt1);
		
		GroupExcludeKeywordType type2 = new GroupExcludeKeywordType();
		type2.setGroupId(228l);
		type2.setType(0);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("HP");
		kt2.setPattern(1);
		type2.setExcludeKeyword(kt2);
		
		GroupExcludeKeywordType type3 = new GroupExcludeKeywordType();
		type3.setGroupId(228l);
		type3.setType(0);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("菠萝");
		kt3.setPattern(0);
		type3.setExcludeKeyword(kt3);
		
		GroupExcludeKeywordType type4 = new GroupExcludeKeywordType();
		type4.setGroupId(228l);
		type4.setType(1);
		type4.setExcludeKeywordPackId(2);
		
		GroupExcludeKeywordType type5 = new GroupExcludeKeywordType();
		type5.setGroupId(228l);
		type5.setType(1);
		type5.setExcludeKeywordPackId(5);
		
		excludeKeywords.add(type1);
		excludeKeywords.add(type2);
		excludeKeywords.add(type3);
		excludeKeywords.add(type4);
		excludeKeywords.add(type5);
		request.setExcludeKeywords(excludeKeywords);
		
		
		BaseResponse<PlaceHolderResult> result = exporter.addExcludeKeyword(dataUser2, request, apiOption2);
		System.out.println(result);
	}
	
	@Test
	public void testAddExcludeKeyword2() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);
		
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499,499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();
		
		params.setServiceName("GroupConfigService");
		params.setMethodName("addExcludeKeyword");
		
		AddExcludeKeywordRequest request = new AddExcludeKeywordRequest();
		List<GroupExcludeKeywordType> excludeKeywords = new ArrayList<GroupExcludeKeywordType>();
		
		GroupExcludeKeywordType type1 = new GroupExcludeKeywordType();
		type1.setGroupId(228l);
		type1.setType(0);
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("");
		kt1.setPattern(1);
		type1.setExcludeKeyword(kt1);
		
		GroupExcludeKeywordType type2 = new GroupExcludeKeywordType();
		type2.setGroupId(228l);
		type2.setType(0);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("HP");
		kt2.setPattern(1);
		type2.setExcludeKeyword(kt2);
		
		GroupExcludeKeywordType type3 = new GroupExcludeKeywordType();
		type3.setGroupId(228l);
		type3.setType(0);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("菠萝");
		kt3.setPattern(0);
		type3.setExcludeKeyword(kt3);
		
		GroupExcludeKeywordType type4 = new GroupExcludeKeywordType();
		type4.setGroupId(228l);
		type4.setType(1);
		type4.setExcludeKeywordPackId(9999);
		
		GroupExcludeKeywordType type5 = new GroupExcludeKeywordType();
		type5.setGroupId(228l);
		type5.setType(1);
		type5.setExcludeKeywordPackId(5);
		
		excludeKeywords.add(type1);
		excludeKeywords.add(type2);
		excludeKeywords.add(type3);
		excludeKeywords.add(type4);
		excludeKeywords.add(type5);
		request.setExcludeKeywords(excludeKeywords);
		
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
	public void testDeleteExcludeKeyword() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		
		DeleteExcludeKeywordRequest request = new DeleteExcludeKeywordRequest();
		List<GroupExcludeKeywordType> excludeKeywords = new ArrayList<GroupExcludeKeywordType>();
		
		GroupExcludeKeywordType type1 = new GroupExcludeKeywordType();
		type1.setGroupId(111l);
		type1.setType(0);
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("Ibm");
		kt1.setPattern(1);
		type1.setExcludeKeyword(kt1);
		
		GroupExcludeKeywordType type2 = new GroupExcludeKeywordType();
		type2.setGroupId(228l);
		type2.setType(0);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("HP");
		kt2.setPattern(1);
		type2.setExcludeKeyword(kt2);
		
		GroupExcludeKeywordType type3 = new GroupExcludeKeywordType();
		type3.setGroupId(228l);
		type3.setType(0);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("菠萝");
		kt3.setPattern(0);
		type3.setExcludeKeyword(kt3);
		
		GroupExcludeKeywordType type4 = new GroupExcludeKeywordType();
		type4.setGroupId(228l);
		type4.setType(1);
		type4.setExcludeKeywordPackId(2);
		
		GroupExcludeKeywordType type5 = new GroupExcludeKeywordType();
		type5.setGroupId(228l);
		type5.setType(1);
		type5.setExcludeKeywordPackId(5);
		
		excludeKeywords.add(type1);
		excludeKeywords.add(type2);
		excludeKeywords.add(type3);
		excludeKeywords.add(type4);
		excludeKeywords.add(type5);
		request.setExcludeKeywords(excludeKeywords);
		
		
		BaseResponse<PlaceHolderResult> result = exporter.deleteExcludeKeyword(dataUser2, request, apiOption2);
		System.out.println(result);
	}
	
	@Test
	public void testDeleteExcludeKeyword2() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8082/beidou-api/api/DRAPIMountAPI", "UTF-8", new ExceptionHandler());
		DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy(DRAPIMountAPI.class, proxy);
		//DRAPIMountAPI drAPIMountAPI = ProxyFactory.createProxy("aoMountAPI",DRAPIMountAPI.class);
		
		BaseRequestUser user = DarwinApiHelper.getBaseRequestUser(499,499);
		BaseRequestOptions options = DarwinApiHelper.getBaseRequestOptions();
		CommonRequest params = new CommonRequest();
		
		params.setServiceName("GroupConfigService");
		params.setMethodName("deleteExcludeKeyword");
		
		DeleteExcludeKeywordRequest request = new DeleteExcludeKeywordRequest();
		List<GroupExcludeKeywordType> excludeKeywords = new ArrayList<GroupExcludeKeywordType>();
		
		GroupExcludeKeywordType type1 = new GroupExcludeKeywordType();
		type1.setGroupId(1111l);
		type1.setType(0);
		KeywordType kt1 = new KeywordType();
		kt1.setKeyword("董事长");
		kt1.setPattern(1);
		type1.setExcludeKeyword(kt1);
		
		GroupExcludeKeywordType type2 = new GroupExcludeKeywordType();
		type2.setGroupId(228l);
		type2.setType(0);
		KeywordType kt2 = new KeywordType();
		kt2.setKeyword("HP");
		kt2.setPattern(1);
		type2.setExcludeKeyword(kt2);
		
		GroupExcludeKeywordType type3 = new GroupExcludeKeywordType();
		type3.setGroupId(228l);
		type3.setType(0);
		KeywordType kt3 = new KeywordType();
		kt3.setKeyword("菠萝");
		kt3.setPattern(0);
		type3.setExcludeKeyword(kt3);
		
		GroupExcludeKeywordType type4 = new GroupExcludeKeywordType();
		type4.setGroupId(228l);
		type4.setType(1);
		type4.setExcludeKeywordPackId(9999);
		
		GroupExcludeKeywordType type5 = new GroupExcludeKeywordType();
		type5.setGroupId(228l);
		type5.setType(1);
		type5.setExcludeKeywordPackId(1);
		
		excludeKeywords.add(type1);
		excludeKeywords.add(type2);
		excludeKeywords.add(type3);
		excludeKeywords.add(type4);
		excludeKeywords.add(type5);
		request.setExcludeKeywords(excludeKeywords);
		
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
