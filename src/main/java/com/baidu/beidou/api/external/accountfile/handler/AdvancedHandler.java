package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.AdvancedVo;
import com.baidu.beidou.cprogroup.bo.GroupPack;
import com.baidu.beidou.cprogroup.service.GroupPackMgr;

/**
 * 
 * ClassName: AdvancedHandler  <br>
 * Function: 高级组合投放输出VO的handler
 *
 * @author zhangxu
 * @date Sep 19, 2012
 */
public class AdvancedHandler extends Handler {

	//private static final Log log = LogFactory.getLog(AdvancedHandler.class);

	private GroupPackMgr groupPackMgr;

	/**
	 * 生成高级组合投放VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();

		Map<Integer, List<GroupPack>> groupPackMap = new HashMap<Integer, List<GroupPack>>();
		List<GroupPack> boList = groupPackMgr.getGroupPacksByGroupIdList(groupIds);
		for (GroupPack groupPack : boList) {
			Integer groupId = groupPack.getGroupId();
			if (!groupPackMap.containsKey(groupId)) {
				groupPackMap.put(groupId, new ArrayList<GroupPack>());
			}
			groupPackMap.get(groupId).add(groupPack);
		}
		
		for (Integer groupId : groupIds) {
			if (groupPackMap.containsKey(groupId)) {
				for (GroupPack groupPack : groupPackMap.get(groupId)) {
					AdvancedVo advancedVo = new AdvancedVo();
					advancedVo.setGroupid(groupId);
					advancedVo.setPlanid(groupPack.getPlanId());
					advancedVo.setPackPrice(groupPack.getPrice());
					advancedVo.setPackType(groupPack.getPackType());
					advancedVo.setPackId(groupPack.getPackId());
					list.add(advancedVo);
				}
			}
		}

		return list;
	}
	
	public GroupPackMgr getGroupPackMgr() {
		return groupPackMgr;
	}

	public void setGroupPackMgr(GroupPackMgr groupPackMgr) {
		this.groupPackMgr = groupPackMgr;
	}

}
