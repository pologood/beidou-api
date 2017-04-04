package com.baidu.beidou.api.external.kr.vo.request;

import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * 
 * ClassName: GetKRSuggestBySeedRequest  <br>
 * Function: 推荐相关关键词请求
 *
 * @author zhangxu
 * @date Aug 15, 2012
 */
public class GetKRSuggestBySeedRequest implements ApiRequest{

	private String seed;
	
	private long groupId;
	
	private int[] packIds;

	private int[] regionList;
	
	private int targetType;
	
	private int aliveDays;
	
	private String reserved;
	
	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public int[] getRegionList() {
		return regionList;
	}

	public void setRegionList(int[] regionList) {
		this.regionList = regionList;
	}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public int getAliveDays() {
		return aliveDays;
	}

	public void setAliveDays(int aliveDays) {
		this.aliveDays = aliveDays;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public int[] getPackIds() {
		return packIds;
	}

	public void setPackIds(int[] packIds) {
		this.packIds = packIds;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	/**
	 * getDataSize: 获取请求数据量大小
	 * @version ApiRequest
	 * @author genglei01
	 * @date 2012-4-20
	 */
	public int getDataSize(){
		return ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
	}
	

}
