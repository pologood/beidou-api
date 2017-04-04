package com.baidu.beidou.api.external.code.exporter.rpc;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.code.exporter.CodeService;
import com.baidu.beidou.api.external.code.vo.Category;
import com.baidu.beidou.api.external.code.vo.Region;
import com.baidu.beidou.api.external.code.vo.request.GetAllCategoryRequest;
import com.baidu.beidou.api.external.code.vo.request.GetAllRegionRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.ApiResult;
@Ignore
public class CodeServiceTest extends ApiBaseRPCTest<CodeService> {

	@Test
	public void testGetAllCatagory() throws Exception{
		CodeService exporter = getServiceProxy(CodeService.class, ApiExternalConstant.CODE_SERVICE_URL);
		GetAllCategoryRequest request = new GetAllCategoryRequest();
		ApiResult<Category> result = exporter.getAllCategory(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	public void testGetAllRegion() throws Exception{
		CodeService exporter = getServiceProxy(CodeService.class, ApiExternalConstant.CODE_SERVICE_URL);
		GetAllRegionRequest request = new GetAllRegionRequest();
		request.setVersion("1.1");
		ApiResult<Region> result = exporter.getAllRegion(dataUser, request, apiOption);
		System.out.println(result);
	}
	
}
