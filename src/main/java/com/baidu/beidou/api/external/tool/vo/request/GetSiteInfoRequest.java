package com.baidu.beidou.api.external.tool.vo.request;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetSiteInfoRequest  <br>
 * Function: 获取网站行业等冗余信息请求头
 *
 * @author zhangxu
 * @date Aug 20, 2012
 */
public class GetSiteInfoRequest implements ApiRequest{

	private String[] sites;

	public String[] getSites() {
		return sites;
	}

	public void setSites(String[] sites) {
		this.sites = sites;
	}
	
	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(sites)) {
			result = sites.length;
		}
		
		return result;
	}
}
