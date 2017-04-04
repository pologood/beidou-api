package com.baidu.beidou.api.external.tool.vo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: BasicPackType  <br>
 * Function: 高级组合中的基础组合配置类
 *
 * @author zhangxu
 * @date Aug 25, 2012
 */
public class BasicPackType implements Serializable {
	
	private static final long serialVersionUID = 1L;

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
