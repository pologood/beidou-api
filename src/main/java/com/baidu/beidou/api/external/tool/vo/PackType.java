package com.baidu.beidou.api.external.tool.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: PackType  <br>
 * Function: 受众组合id和类型类
 *
 * @author zhangxu
 * @date Aug 20, 2012
 */
public class PackType implements Serializable{
	
	private static final long serialVersionUID = 13322792842591L;

	private int id;
	
	private int type;
		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id",id)
		.append("type", type)
        .toString();
	}
	
}
