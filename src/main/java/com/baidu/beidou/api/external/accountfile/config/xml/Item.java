package com.baidu.beidou.api.external.accountfile.config.xml;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: Item  <br>
 * Function: 账户信息的item
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 28, 2012
 */
public class Item {
	
	private int id;
	
	private String name;
	
	private String filename;
	
	private String handlerBean;
	
	private String header;

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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getHandlerBean() {
		return handlerBean;
	}

	public void setHandlerBean(String handlerBean) {
		this.handlerBean = handlerBean;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id", id)
		.append("name", name)
		.append("filename", filename)
		.append("handlerBean", handlerBean)
		.append("header", header)
        .toString();
	}


}
