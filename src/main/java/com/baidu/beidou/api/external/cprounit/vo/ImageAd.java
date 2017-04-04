package com.baidu.beidou.api.external.cprounit.vo;

import java.io.Serializable;

public class ImageAd extends Ad implements Serializable {

	private static final long serialVersionUID = 1L;
	private String imageData; //原始图片数据，基本64位编码字符串。图片的大小<=50K,尺寸为固定的18种。
	private String imageUrl; //添加成功后，可以使用该图片网址查看图片，只读。
	private int width; //图片的宽度，只读。
	private int height; //图片的高度，只读。

	/**
	 * @return the imageData
	 */
	public String getImageData() {
		return imageData;
	}

	/**
	 * @param imageData the imageData to set
	 */
	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

}
