package com.baidu.beidou.api.internal.tool.exporter;

import java.util.List;
import java.util.Map;

/**
 * ClassName:PreviewUrlService Function: 提供给Wm123的预览API接口
 * 
 * @author <a href="mailto:zengyunfeng@baidu.com">曾云峰</a>
 * @version
 * @since Cpweb-183
 * @Date 2010 2010-5-27 下午03:25:32
 * 
 * @see
 */

public interface PreviewUrlService {

	/**
	 *  findUrlBySiteId:
	 * 
	 * @param siteId
	 * @return
	 * @since cpweb-183,it's deprecated 
	 * 
	 * @deprecated
	 */
	public List<String> findUrlBySiteId(final int siteId,
			final int urlSizeCategoryTop);

	/**
	 * findSitePreviewUrlsGroupBySizeOrderBySrchBySiteId:
	 * 用于查询站点URL列表信息，依据主域的ID来查询
	 * 
	 * @param siteId siteId
	 * @param urlSizeCategoryTop TopN
	 * @return Map<Integer ,List<String>> URL按尺寸分组，每组选日检索量TOP-N，其中key=0表示是文件链接
	 * @since Cpweb-206 Wm123详情页改版
	 */
	public Map<Integer, List<String>> findSitePreviewUrlsGroupBySizeOrderBySrchBySiteId(
			final int siteId, final int urlSizeCategoryTop);
}
