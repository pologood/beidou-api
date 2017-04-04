package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupAttachInfoType;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class DeleteAttachInfoRequest implements ApiRequest{
	private List<GroupAttachInfoType> attachInfos;

	@Override
	public int getDataSize() {
		if (CollectionUtils.isEmpty(attachInfos)) {
			return 0;
		}
		return attachInfos.size();
	}

	public List<GroupAttachInfoType> getAttachInfos() {
		return attachInfos;
	}

	public void setAttachInfos(List<GroupAttachInfoType> attachInfos) {
		this.attachInfos = attachInfos;
	}
	
}
