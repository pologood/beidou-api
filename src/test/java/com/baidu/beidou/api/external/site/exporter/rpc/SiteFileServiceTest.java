package com.baidu.beidou.api.external.site.exporter.rpc;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.site.exporter.SiteFileService;
import com.baidu.beidou.api.external.site.vo.SiteFileRequestType;
import com.baidu.beidou.api.external.site.vo.request.GetSiteFileIdRequest;
import com.baidu.beidou.api.external.site.vo.request.GetSiteFileStateRequest;
import com.baidu.beidou.api.external.site.vo.request.GetSiteFileUrlRequest;
import com.baidu.beidou.api.external.site.vo.response.GetSiteFileIdResponse;
import com.baidu.beidou.api.external.site.vo.response.GetSiteFileStateResponse;
import com.baidu.beidou.api.external.site.vo.response.GetSiteFileUrlResponse;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.ApiResult;
@Ignore
public class SiteFileServiceTest extends ApiBaseRPCTest<SiteFileService> {

	@Test
	public void testGetSiteFileId() throws Exception{
		SiteFileService exporter = getServiceProxy(SiteFileService.class, ApiExternalConstant.SITEFILE_SERVICE_URL);
		GetSiteFileIdRequest request = new GetSiteFileIdRequest();
		SiteFileRequestType type = new SiteFileRequestType();
		request.setSiteFileRequestType(type);
		ApiResult<GetSiteFileIdResponse> result = exporter.getSiteFileId(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	public void testGetSiteFileState() throws Exception{
		SiteFileService exporter = getServiceProxy(SiteFileService.class, ApiExternalConstant.SITEFILE_SERVICE_URL);
		GetSiteFileStateRequest request = new GetSiteFileStateRequest();
		request.setFileId("f596ddc2414e86878f76853b8a3a11e0");
		ApiResult<GetSiteFileStateResponse> result = exporter.getSiteFileState(dataUser, request, apiOption);
		System.out.println(result);
	}
	
	@Test
	public void testGetSiteFileUrl() throws Exception{
		SiteFileService exporter = getServiceProxy(SiteFileService.class, ApiExternalConstant.SITEFILE_SERVICE_URL);
		GetSiteFileUrlRequest request = new GetSiteFileUrlRequest();
		request.setFileId("f596ddc2414e86878f76853b8a3a11e0");
		ApiResult<GetSiteFileUrlResponse> result = exporter.getSiteFileUrl(dataUser, request, apiOption);
		System.out.println(result);
	}
	
}
