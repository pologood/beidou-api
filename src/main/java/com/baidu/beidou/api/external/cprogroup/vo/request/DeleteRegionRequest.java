package com.baidu.beidou.api.external.cprogroup.vo.request;

import com.baidu.beidou.api.external.cprogroup.vo.GroupRegionType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;
import org.apache.commons.lang.ArrayUtils;

import java.io.Serializable;

/**
 * ClassName: DeleteRegionRequest
 * Function: 删除投放地域请求
 *
 * @author Baidu API Team
 * @date 2012-4-1
 */
public class DeleteRegionRequest implements Serializable, ApiRequest {
    private String version;
	
	private static final long serialVersionUID = 6513339513810163120L;
	
	private GroupRegionType[] regions;	// 推广地域设置数组
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(regions)) {
			result = regions.length;
		}
		
		return result;
	}

	public GroupRegionType[] getRegions() {
		return regions;
	}

	public void setRegions(GroupRegionType[] regions) {
		this.regions = regions;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
