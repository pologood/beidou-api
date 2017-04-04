package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * ClassName: GroupType
 * Function: 推广组信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class GroupType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long groupId; // 在新增接口中，没有意义；升级接口中，必须有
	private long campaignId; // 新增接口中，必须含有campaignId
	private String groupName; //新增的推广组名
	private int price; //点击价格, 单位为分
	private Integer status; //状态，0：生效；1: 搁置；2：删除。对新增接口，只可以设置生效或搁置.
	private int excludeGender;

	private Integer type; //推广组类型，bit位表示，可复选，1：固定；2：悬浮；4：贴片；8：高级组合投放设置（这里比较特殊，这个值是targetType）

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	public int getExcludeGender() {
		return excludeGender;
	}

	public void setExcludeGender(int excludeGender) {
		this.excludeGender = excludeGender;
	}
	
	

}
