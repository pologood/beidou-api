package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: PackItemType  <br>
 * Function: 推广组受众组合配置类
 *
 * @author zhangxu
 * @date Aug 31, 2012
 */
public class PackItemType implements Serializable {
	
	private static final long serialVersionUID = 134975394354721L;

	/**
	 * 	1：兴趣组合
	 *  2：人群组合
	 *  3：关键词组合
	 *  4：高级组合
	 */
	private int type; //受众组合类型 
	
	private int packId; //受众组合id

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPackId() {
		return packId;
	}

	public void setPackId(int packId) {
		this.packId = packId;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("type",type)
		.append("packId",packId)
        .toString();
	}
	
}
