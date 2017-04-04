package com.baidu.beidou.api.external.cprounit.vo;

import java.io.Serializable;

/**
 * ClassName: ImageInfo
 * Function: 图片相关信息的实体类，用于传给前端处理
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-15
 */
public class ImageInfo implements Serializable {
	private static final long serialVersionUID = -4021034356194730111L;

	private int success;
	
	private int width;
	
	private int height;

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}

