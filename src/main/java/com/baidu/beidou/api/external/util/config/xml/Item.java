package com.baidu.beidou.api.external.util.config.xml;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: Item  <br>
 * Function: 各个function操作数据量阈值配置
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 28, 2012
 */
public class Item {
	
	private int id;
	
	private String name;
	
	private int threshold;
	
	
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



	public int getThreshold() {
		return threshold;
	}



	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}



	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id", id)
		.append("name", name)
		.append("threshold", threshold)
        .toString();
	}


}
