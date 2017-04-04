package com.baidu.beidou.api.external.cprounit2.vo;

import java.io.Serializable;

public class Ad implements Serializable {
	private static final long serialVersionUID = 1L;
	private long adId; // 在新增接口中，没有意义。
	private int status; // 状态：0：生效；1：搁置；3：审核中；4：审核拒绝。对新增没有意义
	private int type; // 类型：1：文字；2: 图片；3:flash
	private String title; // 创意标题
	private String displayUrl; // 显示url;
	private String destinationUrl; // 点击url

	/**
	 * @return the adId
	 */
	public long getAdId() {
		return adId;
	}

	/**
	 * @param adId
	 *            the adId to set
	 */
	public void setAdId(long adId) {
		this.adId = adId;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the showUrl
	 */
	public String getDisplayUrl() {
		return displayUrl;
	}

	/**
	 * @param showUrl
	 *            the showUrl to set
	 */
	public void setDisplayUrl(String showUrl) {
		this.displayUrl = showUrl;
	}

	/**
	 * @return the targetUrl
	 */
	public String getDestinationUrl() {
		return destinationUrl;
	}

	/**
	 * @param targetUrl
	 *            the targetUrl to set
	 */
	public void setDestinationUrl(String targetUrl) {
		this.destinationUrl = targetUrl;
	}

}
