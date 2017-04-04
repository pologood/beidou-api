package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.InterestInfoType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: SetInterestInfoRequest  <br>
 * Function: 设置推广组兴趣请求头
 *
 * @author zhangxu
 * @date Jun 5, 2012
 */
public class SetInterestInfoRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 31560132116421380L;

	private InterestInfoType interestInfo;

	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (interestInfo != null) {
			int[] itIds = interestInfo.getInterestIds();
			int[] exceptItIds = interestInfo.getExceptInterestIds();
			if (!ArrayUtils.isEmpty(itIds)) {
				result = itIds.length;
			}
			if (!ArrayUtils.isEmpty(exceptItIds)) {
				result += exceptItIds.length;
			}
		}
		
		return result;
	}
	
	public InterestInfoType getInterestInfo() {
		return interestInfo;
	}

	public void setInterestInfo(InterestInfoType interestInfo) {
		this.interestInfo = interestInfo;
	}
	
}
