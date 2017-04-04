package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupPackItemType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

public class DeletePackInfoRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = 405211001263570697L;
	
	private List<GroupPackItemType> packs;

	public List<GroupPackItemType> getPacks() {
		return packs;
	}

	public void setPacks(List<GroupPackItemType> packs) {
		this.packs = packs;
	}
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!CollectionUtils.isEmpty(packs)) {
			result = packs.size();
		}
		
		return result;
	}

}
