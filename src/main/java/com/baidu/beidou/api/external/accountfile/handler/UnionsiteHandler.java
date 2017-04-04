package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.List;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.UnionsiteVo;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.vo.BDSiteInfo;

/**
 * 
 * ClassName: UnionsiteHandler <br>
 * Function: 联盟有效站点输出VO的handler
 * 
 */
public class UnionsiteHandler extends Handler {

	//private static final Log log = LogFactory.getLog(AccountHandler.class);

	/**
	 * 生成联盟有效站点VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();

		List<BDSiteInfo> siteList = UnionSiteCache.siteInfoCache.getSiteInfoList();
		
		for (BDSiteInfo bdSiteInfo : siteList) {
			UnionsiteVo unionsiteVo = new UnionsiteVo();
			unionsiteVo.setSiteUrl(bdSiteInfo.getSiteurl());
			unionsiteVo.setSiteName(bdSiteInfo.getSitename());
			unionsiteVo.setFirstTradeId(bdSiteInfo.getFirsttradeid());
			unionsiteVo.setSecondTradeId(bdSiteInfo.getSecondtradeid());
			list.add(unionsiteVo);
		}

		return list;
	}

}
