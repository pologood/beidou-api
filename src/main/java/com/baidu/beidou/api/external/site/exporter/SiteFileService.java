package com.baidu.beidou.api.external.site.exporter;

import com.baidu.beidou.api.external.site.vo.request.GetSiteFileIdRequest;
import com.baidu.beidou.api.external.site.vo.request.GetSiteFileStateRequest;
import com.baidu.beidou.api.external.site.vo.request.GetSiteFileUrlRequest;
import com.baidu.beidou.api.external.site.vo.response.GetSiteFileIdResponse;
import com.baidu.beidou.api.external.site.vo.response.GetSiteFileStateResponse;
import com.baidu.beidou.api.external.site.vo.response.GetSiteFileUrlResponse;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;

/**
 * 
 * ClassName: SiteFileService  <br>
 * Function: 获取站点数据
 *
 * @author <a href="mailto:zhangxu04@baidu.com">Zhang Xu</a>
 * @version 2013-7-22 下午2:12:18
 */
public interface SiteFileService {

	/**
	 * 
	 * 获取站点完整数据fileId
	 *
	 * @param user
	 * @param request
	 * @param apiOption
	 * @return
	 */
	public ApiResult<GetSiteFileIdResponse> getSiteFileId(DataPrivilege user,
			GetSiteFileIdRequest request, 
			ApiOption apiOption);
	
	/**
	 * 
	 * 查询fileId状态 
	 *
	 * @param user
	 * @param request
	 * @param apiOption
	 * @return
	 */
	public ApiResult<GetSiteFileStateResponse> getSiteFileState(DataPrivilege user,
			GetSiteFileStateRequest request, 
			ApiOption apiOption);
	
	/**
	 * 
	 * 获取站点fileId对应的下载url
	 *
	 * @param user
	 * @param request
	 * @param apiOption
	 * @return
	 */
	public ApiResult<GetSiteFileUrlResponse> getSiteFileUrl(DataPrivilege user,
			GetSiteFileUrlRequest request, 
			ApiOption apiOption);
	
}
