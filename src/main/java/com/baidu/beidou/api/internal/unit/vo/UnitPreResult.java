package com.baidu.beidou.api.internal.unit.vo;

import java.util.List;
/**
 * 创意预览对象，包含预览html片段，targetUrl数组
 */
public class UnitPreResult {
	
	private String htmlSnippet;
	private List<String> targetUrls;
	
	
	public String getHtmlSnippet() {
		return htmlSnippet;
	}
	public void setHtmlSnippet(String htmlSnippet) {
		this.htmlSnippet = htmlSnippet;
	}
	public List<String> getTargetUrls() {
		return targetUrls;
	}
	public void setTargetUrls(List<String> targetUrls) {
		this.targetUrls = targetUrls;
	}
}
