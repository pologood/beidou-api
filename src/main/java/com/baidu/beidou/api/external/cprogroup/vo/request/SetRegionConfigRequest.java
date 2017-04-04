package com.baidu.beidou.api.external.cprogroup.vo.request;

import com.baidu.beidou.api.external.cprogroup.vo.RegionConfigType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

import java.io.Serializable;

/**
 * ClassName: SetRegionConfigRequest
 * Function: 设置投放地域请求
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class SetRegionConfigRequest implements Serializable, ApiRequest {
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

	private static final long serialVersionUID = -2860402259661726643L;
	
	private RegionConfigType regionConfig;	// 广组投放地域
	
	public int getDataSize() {
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}

	public RegionConfigType getRegionConfig() {
		return regionConfig;
	}

	public void setRegionConfig(RegionConfigType regionConfig) {
		this.regionConfig = regionConfig;
	}
}
