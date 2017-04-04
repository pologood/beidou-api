package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: PriceType  <br>
 * Function: 推广组相关出价类
 *
 * @author zhangxu
 * @date Aug 31, 2012
 */
public class PriceType implements Serializable {
	
	private static final long serialVersionUID = 34915324344781L;

	/**
	 * 出价类型	必填
	 * 1：兴趣点或者兴趣组合
	 * 2：受众组合（包括关键词组合、兴趣组合、到访人群组合、高级组合）
	 */
	private int type; 
	
	private long groupId;
	
	/**
	 * 	当type=2时为受众组合id。当type=1时为兴趣id。
	 */
	private int id1;
	
	/**
	 * 	保留属性，未启用
	 */
	private int id2 = -1;
	
	/**
	 * 当type=2时，表示受众组合类型
	 */
	private int packType = -1;
	
	/**
	 * 是否为自定义兴趣，当type=1时请参考此值。
	 */
	private boolean isCustomInterest = false;
	
	/**
	 * 出价，单位为分。特殊处理：传入-1，表示不更新出价
	 */
	private int price;
	
	public PriceType() {
		
	}
	
	public PriceType(int type, long groupId, int id1, int id2, boolean isCustomInterest, int price) {
		super();
		this.type = type;
		this.groupId = groupId;
		this.id1 = id1;
		this.id2 = id2;
		this.isCustomInterest = isCustomInterest;
		this.price = price;
	}

	public PriceType(int type, long groupId, int id1, boolean isCustomInterest, int price) {
		super();
		this.type = type;
		this.groupId = groupId;
		this.id1 = id1;
		this.isCustomInterest = isCustomInterest;
		this.price = price;
	}
	
	public PriceType(int type, long groupId, int id1, int id2, int price) {
		super();
		this.type = type;
		this.groupId = groupId;
		this.id1 = id1;
		this.id2 = id2;
		this.price = price;
	}

	public PriceType(int type, long groupId, int id1, int price) {
		super();
		this.type = type;
		this.groupId = groupId;
		this.id1 = id1;
		this.price = price;
	}
	
	public PriceType(int type, long groupId, int id1, int id2, int packType, int price) {
		super();
		this.type = type;
		this.groupId = groupId;
		this.id1 = id1;
		this.id2 = id2;
		this.packType = packType;
		this.price = price;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public int getId1() {
		return id1;
	}

	public void setId1(int id1) {
		this.id1 = id1;
	}

	public int getId2() {
		return id2;
	}

	public void setId2(int id2) {
		this.id2 = id2;
	}

	public int getPackType() {
		return packType;
	}

	public void setPackType(int packType) {
		this.packType = packType;
	}

	public boolean isCustomInterest() {
		return isCustomInterest;
	}

	public void setCustomInterest(boolean isCustomInterest) {
		this.isCustomInterest = isCustomInterest;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String toString(){
		if (id2 != 0) {
			return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("type",type)
			.append("groupId",groupId)
			.append("id1",id1)
			.append("packType",packType)
			.append("id2",id2)
			.append("isCustomInterest",isCustomInterest)
			.append("price",price)
	        .toString();
		}
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("type",type)
		.append("groupId",groupId)
		.append("id1",id1)
		.append("packType",packType)
		.append("isCustomInterest",isCustomInterest)
		.append("price",price)
        .toString();
	}

}
