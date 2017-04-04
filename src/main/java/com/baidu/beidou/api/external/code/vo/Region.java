package com.baidu.beidou.api.external.code.vo;

import java.io.Serializable;

/**
 * 
 * ClassName: Region
 * Function: 网盟推广网站地域
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 23, 2011
 */
public class Region implements Serializable{
	
	private static final long serialVersionUID = 10324712340L;
	
	private int regionId;		//地域ID
	
	private String name;		//地域名
	
	private int type;			//地域类型：1: 自然地域; 2: 中国电脑；3: 中国联通； 4：校园网  5：网吧
	
	private int parentId;		//上级地域id
	
	/**
	 * @return the regionId
	 */
	public int getRegionId() {
		return regionId;
	}
	
	/**
	 * @param regionId the regionId to set
	 */
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * @return the parented
	 */
	public int getParentId() {
		return parentId;
	}
	
	/**
	 * @param parented the parented to set
	 */
	public void setParentId(int parented) {
		this.parentId = parented;
	}

}
