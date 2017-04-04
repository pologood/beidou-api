package com.baidu.beidou.api.external.site.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.accountfile.output.CSVWriter;
import com.baidu.beidou.api.external.accountfile.service.impl.AccountFileMgrImpl;
import com.baidu.beidou.api.external.site.constant.SiteFileConstant;
import com.baidu.beidou.api.external.site.service.SiteFileMgr;
import com.baidu.beidou.api.external.site.vo.SiteVo;
import com.baidu.beidou.api.external.util.Zipper;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.vo.BDSiteInfo;
import com.baidu.beidou.util.MD5;

/**
 * 
 * 刷新联盟站点的服务类，在服务启动以及每天定时调用更新数据
 *
 * @author <a href="mailto:zhangxu04@baidu.com">Zhang Xu</a>
 * @version 2013-7-22 下午5:14:55
 */
public class SiteFileMgrImpl implements SiteFileMgr {

	private static final Log log = LogFactory.getLog(AccountFileMgrImpl.class);

	private static FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMdd");
	
	public void refresh() {
		// 置生成文件进行中
		SiteFileConstant.IS_GENERATE_GOING = true;
		
		try {
			// 生成FILEID与保存路径
			String siteFileId = MD5.getMd5(dateFormat.format(new Date())); 
			String tmpSiteFilePath = SiteFileConstant.DOWNLOAD_FILE_SAVEPATH + File.separator + siteFileId;
			String siteFilePath = SiteFileConstant.DOWNLOAD_FILE_SAVEPATH + File.separator + siteFileId + SiteFileConstant.SITEFILE_ZIP_SUFFIX;
			
			List<String[]> siteVoStringList = new ArrayList<String[]>();
			List<BDSiteInfo> siteList = UnionSiteCache.siteInfoCache.getSiteInfoList();
			for (BDSiteInfo bdSiteInfo : siteList) {
				SiteVo siteVo = new SiteVo();
				siteVo.setSiteUrl(bdSiteInfo.getSiteurl());
				siteVo.setSiteName(bdSiteInfo.getSitename());
				siteVo.setFirstTradeId(bdSiteInfo.getFirsttradeid());
				siteVo.setSecondTradeId(bdSiteInfo.getSecondtradeid());
				siteVo.setWuliaoType(bdSiteInfo.getSuporttype());
				siteVo.setDisplayType(bdSiteInfo.getDisplayType());
				siteVo.setAdSize(bdSiteInfo.isMain() ? SiteFileConstant.SITE_SRCH_GT_THRESHOLD : SiteFileConstant.SITE_SRCH_LE_THRESHOLD);
				siteVo.setAdThruput(bdSiteInfo.getSizethruput());
				siteVo.setSrchPv(bdSiteInfo.getThruputtype());
				siteVo.setUv(bdSiteInfo.getIpLevel());
				siteVo.setSiteSource(bdSiteInfo.getSiteSource());
				siteVoStringList.add(siteVo.toStringArray());
			}
			
			// 写入文件
			CSVWriter.getInstance().write(siteVoStringList, SiteFileConstant.SITE_FILE_HEADER, tmpSiteFilePath);
		
			// 压缩文件
			Zipper zipper = new Zipper();
			zipper.zipFile(siteFilePath, tmpSiteFilePath);
			
			// 获取文件MD5
			String siteFileMd5 = MD5.getFileMD5(new File(siteFilePath));
			
			// 全局FILEID与MD5更新
			SiteFileConstant.SITE_FILE_ID = siteFileId;
			SiteFileConstant.SITE_FILE_MD5 = siteFileMd5;
			
			log.info("Site file global constant id = " + siteFileId);
			
		} catch (Exception e) {
			log.error("导出API网站下载文件失败 " + e.getMessage(), e);
		} finally {
			SiteFileConstant.IS_GENERATE_GOING = false;
		}
		
	}

}
