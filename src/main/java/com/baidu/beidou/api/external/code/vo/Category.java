package com.baidu.beidou.api.external.code.vo;

import java.io.Serializable;

/**
 * 
 * ClassName: Category
 * Function: 网盟推广网站行业分类
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 23, 2011
 */
public class Category implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int  categoryId;			//分类id
	
	private String name;		//分类名
	
	private int  parentId;		//上级分类id，0表示为一级分类
	
	/**
	 * @return the categoryId
	 */
	public int getCategoryId() {
		return categoryId;
	}
	
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
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
	 * @return the parentId
	 */
	public int getParentId() {
		return parentId;
	}
	
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

}
