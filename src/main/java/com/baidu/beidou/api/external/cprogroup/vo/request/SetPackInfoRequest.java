package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.util.List;

import com.baidu.beidou.api.external.cprogroup.vo.PackInfoType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: SetPackInfoRequest  <br>
 * Function: 设置推广组受众组合请求
 *
 * @author zhangxu
 * @date Aug 31, 2012
 */
public class SetPackInfoRequest implements ApiRequest {

	private PackInfoType packInfo;

	public PackInfoType getPackInfo() {
		return packInfo;
	}

	public void setPackInfo(PackInfoType packInfo) {
		this.packInfo = packInfo;
	}
	
	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}
}
