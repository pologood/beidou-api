package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.KtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.RtItemType;
import com.baidu.beidou.api.external.cprogroup.vo.TargetInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.VtItemType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: SetTargetInfoRequest
 * Function: 设置定向信息请求
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class SetTargetInfoRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -2350023528930402333L;
	
	private TargetInfoType targetInfo;	// 投放方式及配置信息
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (targetInfo != null) {
			result = 0;
			KtItemType ktItem = targetInfo.getKtItem();
			RtItemType rtItem = targetInfo.getRtItem();
			VtItemType vtItem = targetInfo.getVtItem();
			
			if (ktItem != null && !ArrayUtils.isEmpty(ktItem.getKtWordList())) {
				result += ktItem.getKtWordList().length;
			}
			if (rtItem != null && CollectionUtils.isNotEmpty(rtItem.getRtRelationList())) {
				result += rtItem.getRtRelationList().size();
			}
			if (vtItem != null) {
				if(!ArrayUtils.isEmpty(vtItem.getRelatedPeopleIds())) {
					result += vtItem.getRelatedPeopleIds().length;
				}
				if(!ArrayUtils.isEmpty(vtItem.getUnRelatePeopleIds())) {
					result += vtItem.getUnRelatePeopleIds().length;
				}
			}
		}
		
		return result;
	}

	public TargetInfoType getTargetInfo() {
		return targetInfo;
	}

	public void setTargetInfo(TargetInfoType targetInfo) {
		this.targetInfo = targetInfo;
	}
}
