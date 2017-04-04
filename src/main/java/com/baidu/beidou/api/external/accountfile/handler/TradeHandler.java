package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.TradeVo;
import com.baidu.beidou.api.internal.util.converter.YuanToCentConverter;
import com.baidu.beidou.cprogroup.bo.GroupTradePrice;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.service.GroupSiteConfigMgr;

/**
 * 
 * ClassName: TradeHandler <br>
 * Function: 自选行业输出VO的handler
 * 
 * @author zhangxu
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class TradeHandler extends Handler {

	//private static final Log log = LogFactory.getLog(TradeHandler.class);

	private GroupSiteConfigMgr groupSiteConfigMgr;

	/**
	 * 生成行业网站VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();

		Map<Integer, String> tradeInfoMap = UnionSiteCache.adCategCache
				.getAdTradeInfo();

		YuanToCentConverter conv = new YuanToCentConverter();
		for (Integer groupId : groupIds) {
			List<GroupTradePrice> tradePriceDBList = groupSiteConfigMgr
					.findAllTradePrice(userId, null, groupId);
			for (GroupTradePrice groupTradePrice : tradePriceDBList) {
				TradeVo tradeVo = new TradeVo();
				tradeVo.setGroupid(groupId);
				tradeVo.setTradeid(groupTradePrice.getTradeid());
				tradeVo.setPlanid(groupTradePrice.getPlanid());
				tradeVo.setPrice(conv.convertTo(groupTradePrice.getPrice()));
				tradeVo.setTradename(tradeInfoMap.get(groupTradePrice
						.getTradeid()));
				list.add(tradeVo);
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
