package com.baidu.beidou.api.internal.tool.exporter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baidu.beidou.api.internal.tool.exporter.PreviewUrlService;
import com.baidu.beidou.tool.service.UrlDetailInfoMgr;
import com.baidu.beidou.tool.vo.UrlDetailInfo;
/**
 * ClassName:PreviewUrlServiceImpl
 * Function: TODO ADD FUNCTION
 *
 * @author   <a href="mailto:zengyunfeng@baidu.com">曾云峰</a>
 * @version  
 * @since    TODO
 * @Date	 2010	2010-5-27		下午05:39:32
 *
 * @see 	 
 */

public class PreviewUrlServiceImpl implements PreviewUrlService {
	private UrlDetailInfoMgr urlMgr = null;
	
	public List<String> findUrlBySiteId(final int siteId, final int urlSizeCategoryTop) {
		List<String> result = new ArrayList<String>();
		if(siteId<1 || urlSizeCategoryTop<1){
			return result;
		}
		List<UrlDetailInfo> urlDetailInfo = urlMgr.findUrlDetailInfo(siteId, urlSizeCategoryTop);
		if(urlDetailInfo==null || urlDetailInfo.isEmpty()){
			return result;
		}
		for(UrlDetailInfo info : urlDetailInfo){
			result.add(info.getUrl());
		}
		return result;
	}

	/**
	 * urlMgr
	 *
	 * @return  the urlMgr
	 */
	
	public UrlDetailInfoMgr getUrlMgr() {
		return urlMgr;
	}

	/**
	 * urlMgr
	 *
	 * @param   urlMgr    the urlMgr to set
	 */
	
	public void setUrlMgr(UrlDetailInfoMgr urlMgr) {
		this.urlMgr = urlMgr;
	}

	public Map<Integer, List<String>> findSitePreviewUrlsGroupBySizeOrderBySrchBySiteId(
			int siteId, int urlSizeCategoryTop) {
		Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
		
		if (siteId < 1 || urlSizeCategoryTop < 1){
			return result;
		}
		
		Map<Integer ,List<UrlDetailInfo>> map = urlMgr.findTopNUrlDetailInfoGroupBySizeOrderBySrch(siteId, urlSizeCategoryTop);
		for (Map.Entry<Integer, List<UrlDetailInfo>> entry : map.entrySet()) {
			List<String> sizeUrls = new ArrayList<String>();
			for (UrlDetailInfo udi : entry.getValue()) {
				sizeUrls.add(udi.getUrl());
			}
			result.put(entry.getKey(), sizeUrls);
		}
		return result;
	}

}
