package com.baidu.beidou.api.external.accountfile.vo;


/**
 * 
 * ClassName: AdVo  <br>
 * Function: 创意数据 <br>
 * 
 * 属性包括：AdId,GroupId,CampaignId,状态,创意类型,标题,目标Url,显示Url,描述1,描述2,imageUrl,width,height
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class AdVo implements AbstractVo{
	
	private long adId; 
	
	private long groupId;
	
	private long planId;
	
	private int status; 
	
	private int type; // 类型：1：文字；2: 图片；3:flash
	
	private String title; // 创意标题
	
	private String displayUrl; // 显示url
	
	private String destinationUrl; // 点击url
	
	private String description1; //描述1
	
	private String description2; //描述2	

	private String imageUrl; 
	
	private int width; //图片的宽度
	
	private int height; //图片的高度
	
	private String appDisplayUrl; //app显示url;
	
	private String appDestinationUrl; //app点击url
	
	private int isSmart; 
	
	public String[] toStringArray(){
		String[] str = new String[16];
		str[0] = String.valueOf(this.getAdId());
		str[1] = String.valueOf(this.getGroupId());
		str[2] = String.valueOf(this.getPlanId());
		str[3] = String.valueOf(this.getStatus());
		str[4] = String.valueOf(this.getType());
		str[5] = String.valueOf(this.getTitle());
		str[6] = String.valueOf(this.getDestinationUrl());
		str[7] = String.valueOf(this.getDisplayUrl());
		str[8] = String.valueOf(this.getDescription1());
		str[9] = String.valueOf(this.getDescription2());
		str[10] = String.valueOf(this.getImageUrl());
		str[11] = String.valueOf(this.getWidth());
		str[12] = String.valueOf(this.getHeight());
		str[13] = String.valueOf(this.getAppDestinationUrl());
		str[14] = String.valueOf(this.getAppDisplayUrl());
		str[15] = String.valueOf(this.getIsSmart());
		return str;
	}

	public long getAdId() {
		return adId;
	}

	public void setAdId(long adId) {
		this.adId = adId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDisplayUrl() {
		return displayUrl;
	}

	public void setDisplayUrl(String displayUrl) {
		this.displayUrl = displayUrl;
	}

	public String getDestinationUrl() {
		return destinationUrl;
	}

	public void setDestinationUrl(String destinationUrl) {
		this.destinationUrl = destinationUrl;
	}

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

	public int getIsSmart() {
		return isSmart;
	}

	public void setIsSmart(int isSmart) {
		this.isSmart = isSmart;
	}
	
}
