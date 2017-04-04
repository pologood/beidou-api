package com.baidu.beidou.api.external.code.exporter;

import com.baidu.beidou.api.external.code.vo.Category;
import com.baidu.beidou.api.external.code.vo.Region;
import com.baidu.beidou.api.external.code.vo.request.GetAllCategoryRequest;
import com.baidu.beidou.api.external.code.vo.request.GetAllRegionRequest;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;

/**
 * 
 * ClassName: CodeService
 * Function: 查询网盟推广的网站行业分类代码与地域代码信息
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 23, 2011
 */
public interface CodeService {

	/**
	 * 获取所有的网站分类信息
	 * @param user
	 * @param reverseData
	 * @return
	 */
	public ApiResult<Category> getAllCategory(DataPrivilege user, GetAllCategoryRequest request, ApiOption apiOption);
	
	/**
	 * 获取所有的地域信息
	 * @param user
	 * @param reverseData
	 * @return
	 */
	public ApiResult<Region> getAllRegion(DataPrivilege user, GetAllRegionRequest request, ApiOption apiOption);
		
}
