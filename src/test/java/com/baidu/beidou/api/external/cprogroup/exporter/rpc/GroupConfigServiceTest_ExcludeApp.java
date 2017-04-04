package com.baidu.beidou.api.external.cprogroup.exporter.rpc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.vo.GroupExcludeAppType;
import com.baidu.beidou.api.external.cprogroup.vo.request.AddExcludeAppRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteExcludeAppRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetExcludeAppRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.ApiResult;

/**
 * app信息单测
 * 
 * @author work
 */
@Ignore
public class GroupConfigServiceTest_ExcludeApp extends ApiBaseRPCTest<GroupConfigService> {

	@Test
	public void testGetExcludeApp() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		GetExcludeAppRequest request = new GetExcludeAppRequest();
		request.setGroupIds(new long[]{175l,190l});
		long start = System.currentTimeMillis();
		ApiResult<GroupExcludeAppType> result = exporter.getExcludeApp(dataUser, request, apiOption);
		long end = System.currentTimeMillis();
		System.out.println("Using " + (end - start) + "ms");
		System.out.println(result);
	}
	
	
	
	@Test
	public void testAddExcludeApp() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		
		AddExcludeAppRequest request = new AddExcludeAppRequest();
		List<GroupExcludeAppType> excludeApps = new ArrayList<GroupExcludeAppType>();
		
		GroupExcludeAppType type1 = new GroupExcludeAppType();
		type1.setGroupId(6774506);
		List<Long> excludeAppids1 = new ArrayList<Long>();
		excludeAppids1.add(466082l);
		excludeAppids1.add(100000028l);
		excludeAppids1.add(100000046l);
		excludeAppids1.add(100000051l);
		excludeAppids1.add(100000057l);
		excludeAppids1.add(1768417668l);
		//excludeAppids1.add(615960619l);
		type1.setExcludeApp(excludeAppids1);
		
//		GroupExcludeAppType type2 = new GroupExcludeAppType();
//		type2.setGroupId(190);
//		List<Long> excludeAppids2 = new ArrayList<Long>();
//		excludeAppids2.add(2901873225l);
		//excludeAppids2.add(1768417668l);
//		type2.setExcludeApp(excludeAppids2);
		
		
		excludeApps.add(type1);
//		excludeApps.add(type2);

		request.setExcludeApps(excludeApps);
		
		long start = System.currentTimeMillis();
		ApiResult<Object> result = exporter.addExcludeApp(dataUser, request, apiOption);
		System.out.println(System.currentTimeMillis() - start);
		System.out.println(result);
	}
	
	
	
	@Test
	public void testDeleteExcludeApp() throws Exception{
		GroupConfigService exporter = getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
		
		DeleteExcludeAppRequest request = new DeleteExcludeAppRequest();
		List<GroupExcludeAppType> excludeApps = new ArrayList<GroupExcludeAppType>();
		
		GroupExcludeAppType type1 = new GroupExcludeAppType();
		type1.setGroupId(228);
		List<Long> excludeAppids1 = new ArrayList<Long>();
		excludeAppids1.add(2901873225l);
		//excludeAppids1.add(615960619l);
		type1.setExcludeApp(excludeAppids1);
		
		GroupExcludeAppType type2 = new GroupExcludeAppType();
		type2.setGroupId(2288);
		List<Long> excludeAppids2 = new ArrayList<Long>();
		excludeAppids2.add(2901873225l);
		//excludeAppids2.add(1768417668l);
		type2.setExcludeApp(excludeAppids2);
		
		
		
		excludeApps.add(type1);
		excludeApps.add(type2);

		request.setExcludeApps(excludeApps);
		
		
		ApiResult<Object> result = exporter.deleteExcludeApp(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	
}
