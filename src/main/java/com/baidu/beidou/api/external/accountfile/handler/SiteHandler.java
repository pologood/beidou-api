package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.List;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.SiteVo;
import com.baidu.beidou.api.internal.util.converter.YuanToCentConverter;
import com.baidu.beidou.cprogroup.bo.GroupSitePrice;
import com.baidu.beidou.cprogroup.service.GroupSiteConfigMgr;

/**
 * 
 * ClassName: SiteHandler <br>
 * Function: 自选网站输出VO的handler
 * 
 * @author zhangxu
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class SiteHandler extends Handler {

	//private static final Log log = LogFactory.getLog(SiteHandler.class);

	private GroupSiteConfigMgr groupSiteConfigMgr;

	/**
	 * 生成自选网站VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();

		YuanToCentConverter conv = new YuanToCentConverter();
		for (Integer groupId : groupIds) {
			List<GroupSitePrice> sitePriceDBList = groupSiteConfigMgr
					.findAllSitePrice(userId, null, groupId);
			// List<GroupTradePrice> tradePriceDBList =
			// groupSiteConfigMgr.findAllTradePrice(userId, null, groupId);
			for (GroupSitePrice groupSitePrice : sitePriceDBList) {
				SiteVo siteVo = new SiteVo();
				siteVo.setGroupid(groupId);
				siteVo.setPlanid(groupSitePrice.getPlanid());
				siteVo.setPrice(conv.convertTo(groupSitePrice.getPrice()));
				siteVo.setSiteurl(groupSitePrice.getSiteurl());
				list.add(siteVo);
			}

		}


		return list;
	}

	public GroupSiteConfigMgr getGroupSiteConfigMgr() {
		return groupSiteConfigMgr;
	}

	public void setGroupSiteConfigMgr(GroupSiteConfigMgr groupSiteConfigMgr) {
		this.groupSiteConfigMgr = groupSiteConfigMgr;
	}


}
