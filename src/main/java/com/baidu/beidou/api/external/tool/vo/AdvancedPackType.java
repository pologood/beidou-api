package com.baidu.beidou.api.external.tool.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: AdvancedPackType  <br>
 * Function: 高级组合配置类
 *
 * @author zhangxu
 * @date Aug 25, 2012
 */
public class AdvancedPackType implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String name;
	
	private boolean isOptimized;
	
	private List<BasicPackType> basicPacks = new ArrayList<BasicPackType>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<BasicPackType> getBasicPacks() {
		return basicPacks;
	}

	public void setBasicPacks(List<BasicPackType> basicPacks) {
		this.basicPacks = basicPacks;
	}

	public boolean isOptimized() {
		return isOptimized;
	}

	public void setOptimized(boolean isOptimized) {
		this.isOptimized = isOptimized;
	}

	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id",id)
		.append("name", name)
		.append("isOptimized", isOptimized)
		.append("basicPacks", basicPacks)
        .toString();
	}
	
}
