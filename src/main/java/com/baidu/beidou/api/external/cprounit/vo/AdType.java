package com.baidu.beidou.api.external.cprounit.vo;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class AdType extends Ad implements Serializable {
	
	private static final long serialVersionUID = 5003492623091799333L;
	
	private long groupId; // 向指定的推广组添加创意 
	private long localId; // 本地id，由应用方提供，以作标识
	private String description1; //描述1
	private String description2; //描述2	

	private byte[] imageData; //原始图片数据，基本64位编码字符串。图片的大小<=50K,尺寸为固定的18种。
	private String imageUrl; //添加成功后，可以使用该图片网址查看图片，只读。
	private int width; //图片的宽度，只读。
	private int height; //图片的高度，只读。

	public String getDescription1() {
		return description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public String getDescription2() {
		return description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	@JsonIgnore
	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}
}
