package com.baidu.beidou.api.external.cprounit2.vo;

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
	
	private String appDisplayUrl; // app显示url
	private String appDestinationUrl; // app点击url
	
	//冗余字段
	private Integer reserved1;
	private Integer reserved2;
	private Integer[] reserved3;
	private Long reserved4;
	private Long[] reserved5;
	private String reserved6;
	private String reserved7;

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

	public String getAppDisplayUrl() {
		return appDisplayUrl;
	}

	public void setAppDisplayUrl(String appDisplayUrl) {
		this.appDisplayUrl = appDisplayUrl;
	}

	public String getAppDestinationUrl() {
		return appDestinationUrl;
	}

	public void setAppDestinationUrl(String appDestinationUrl) {
		this.appDestinationUrl = appDestinationUrl;
	}

	public Integer getReserved1() {
		return reserved1;
	}

	public void setReserved1(Integer reserved1) {
		this.reserved1 = reserved1;
	}

	public Integer getReserved2() {
		return reserved2;
	}

	public void setReserved2(Integer reserved2) {
		this.reserved2 = reserved2;
	}

	public Integer[] getReserved3() {
		return reserved3;
	}

	public void setReserved3(Integer[] reserved3) {
		this.reserved3 = reserved3;
	}

	public Long getReserved4() {
		return reserved4;
	}

	public void setReserved4(Long reserved4) {
		this.reserved4 = reserved4;
	}

	public Long[] getReserved5() {
		return reserved5;
	}

	public void setReserved5(Long[] reserved5) {
		this.reserved5 = reserved5;
	}

	public String getReserved6() {
		return reserved6;
	}

	public void setReserved6(String reserved6) {
		this.reserved6 = reserved6;
	}

	public String getReserved7() {
		return reserved7;
	}

	public void setReserved7(String reserved7) {
		this.reserved7 = reserved7;
	}

}
