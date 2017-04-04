package com.baidu.beidou.api.external.interest.vo;

/**
 * 
 * ClassName: CustomInterestType  <br>
 * Function: 兴趣组合
 *
 * @author zhangxu
 * @date May 30, 2012
 */
public class CustomInterestType {

	private int customItId; //兴趣组合id	
	
	private String customItName; //兴趣组合名称	
	
	private CustomInterestCollectionType[] customItCollections; //兴趣组合中各个集合的详细配置信息

	public CustomInterestType() {

	}
	
	public CustomInterestType(int customItId, String customItName) {
		this.customItId = customItId;
		this.customItName = customItName;
	}
	
	public int getCustomItId() {
		return customItId;
	}

	public void setCustomItId(int customItId) {
		this.customItId = customItId;
	}

	public String getCustomItName() {
		return customItName;
	}

	public void setCustomItName(String customItName) {
		this.customItName = customItName;
	}

	public CustomInterestCollectionType[] getCustomItCollections() {
		return customItCollections;
	}

	public void setCustomItCollections(
			CustomInterestCollectionType[] customItCollections) {
		this.customItCollections = customItCollections;
	}
	
	

}
