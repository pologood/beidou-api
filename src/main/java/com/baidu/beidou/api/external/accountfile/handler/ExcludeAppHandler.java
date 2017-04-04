package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.List;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.ExcludeAppVo;
import com.baidu.beidou.cprogroup.bo.AppExclude;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.service.AppExcludeMgr;

/**
 * 
 * ClassName: ExcludeAppHandler  <br>
 * Function: 排除移动应用输出VO的handler
 *
 */
public class ExcludeAppHandler extends Handler {

	private AppExcludeMgr appExcludeMgr;

	/**
	 * 生成排除移动应用VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();
		
		List<AppExclude> appExcludeList = appExcludeMgr.findAppExcludeByCondition(userId, groupIds, null);
		for (AppExclude appExclude : appExcludeList) {
			ExcludeAppVo excludeAppVo = new ExcludeAppVo();
			excludeAppVo.setGroupid(appExclude.getGroupId());
			String name = UnionSiteCache.appCache.getAppNameById(appExclude.getAppSid());
			if(name == null){
				continue;
			}
			excludeAppVo.setAppName(name);
			excludeAppVo.setAppId(appExclude.getAppSid());
			list.add(excludeAppVo);
		}

		return list;
	}

	public AppExcludeMgr getAppExcludeMgr() {
		return appExcludeMgr;
	}

	public void setAppExcludeMgr(AppExcludeMgr appExcludeMgr) {
		this.appExcludeMgr = appExcludeMgr;
	}

}
