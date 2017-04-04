package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import com.baidu.beidou.api.external.accountfile.constant.AccountFileWebConstants;
import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.GroupVo;
import com.baidu.beidou.api.external.cprogroup.util.ApiTargetTypeUtil;
import com.baidu.beidou.cprogroup.bo.AttachInfo;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.CproGroupInfo;
import com.baidu.beidou.cprogroup.bo.GroupIpFilter;
import com.baidu.beidou.cprogroup.bo.GroupSiteFilter;
import com.baidu.beidou.cprogroup.constant.AttachInfoConstant;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.service.AttachInfoMgr;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.service.GroupAttachInfoMgr;
import com.baidu.beidou.cprogroup.service.GroupSiteConfigMgr;
import com.baidu.beidou.cprogroup.util.TargettypeUtil;
import com.baidu.beidou.cprogroup.vo.BDSiteInfo;
import com.baidu.beidou.cprogroup.vo.BDSiteLiteInfo;

/**
 * 
 * ClassName: GroupHandler <br>
 * Function: 推广组文件输出VO的handler
 * 
 * @author zhangxu
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class GroupHandler extends Handler {

	private static final Log log = LogFactory.getLog(GroupHandler.class);

	private CproGroupMgr cproGroupMgr;

	private GroupSiteConfigMgr groupSiteConfigMgr;
	
	private AttachInfoMgr attachInfoMgr;
	    
	private GroupAttachInfoMgr groupAttachInfoMgr;
	
	/**
	 * 生成推广组VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();

		List<CproGroup> cproGroupList = new ArrayList<CproGroup>();

		// 根据planId查询出所有的Group，然后再后面的遍历中再过滤已删除的Group
		for (Integer planId : planIds) {
			List<CproGroup> incrementalGroups = cproGroupMgr
					.findCproGroupByPlanId(planId);
			cproGroupList.addAll(incrementalGroups);
		}

		// 查询Group config info 放在hashmap中
		Map<Integer, CproGroupInfo> groupInfoMap = new HashMap<Integer, CproGroupInfo>();
		for (Integer groupId : groupIds) {
			CproGroupInfo cproGroupInfo = cproGroupMgr
					.findCproGroupInfoById(groupId);
			if (cproGroupInfo != null) {
				groupInfoMap.put(groupId, cproGroupInfo);
			}
		}

		StringBuffer sb = null;

		// 查询过滤IP放在hashmap中
		Map<Integer, String> groupExculdeIpMap = new HashMap<Integer, String>();
		List<GroupIpFilter> ipFilters = null;
		GroupIpFilter ipFilter = null;
		for (Integer groupId : groupIds) {
			ipFilters = groupSiteConfigMgr.findIpFilterByGroupId(groupId);
			sb = new StringBuffer();
			for (int i = 0; i < ipFilters.size(); i++) {
				ipFilter = ipFilters.get(i);
				sb.append(ipFilter.getIp());
				if (i != ipFilters.size() - 1) {
					sb
							.append(AccountFileWebConstants.ACCOUNTFILE_DATA_SEPERATOR);
				}
			}
			groupExculdeIpMap.put(groupId, sb.toString());
		}

		// 查询过滤网站放放在hashmap中
		Map<Integer, String> groupExcludeSiteMap = new HashMap<Integer, String>();
		List<GroupSiteFilter> siteFilters = null;
		GroupSiteFilter siteFilter = null;
		for (Integer groupId : groupIds) {
			siteFilters = groupSiteConfigMgr
					.findSiteFilterByGroupId(groupId);
			sb = new StringBuffer();
			for (int i = 0; i < siteFilters.size(); i++) {
				siteFilter = siteFilters.get(i);
				sb.append(siteFilter.getSite());
				if (i != siteFilters.size() - 1) {
					sb
							.append(AccountFileWebConstants.ACCOUNTFILE_DATA_SEPERATOR);
				}
			}
			groupExcludeSiteMap.put(groupId, sb.toString());
		}
		
		// 查询出推广组附件信息
		List<AttachInfo> attachInfoList = attachInfoMgr.getAttachInfoByGroupIds(groupIds);
		Map<Integer,List<AttachInfo>> attachMap = new HashMap<Integer,List<AttachInfo>>(groupIds.size());
		
		for (AttachInfo info : attachInfoList) {
		    List<AttachInfo> groupAttachs = attachMap.get(info.getGroupId());
		    if (groupAttachs == null) {
		        groupAttachs = new ArrayList<AttachInfo>();
		        attachMap.put(info.getGroupId(), groupAttachs);
		    }
		    groupAttachs.add(info);
		}

		CproGroupInfo cproGroupInfo = null;
		if (cproGroupList != null) {
			for (CproGroup g : cproGroupList) {
				
				//临时过滤atleft推广组 add by caichao
				if (g.getTargetType() == 256) {
					continue;
				}
				
				// 不返回已删除的组
				if(g.getGroupState() == CproGroupConstant.GROUP_STATE_DELETE){
					continue;
				}
				
				// 由于推广组信息比较复杂，因此没有用dozzer mapping
				// 先设置推广组基本信息
				GroupVo groupVo = new GroupVo();
				groupVo.setCampaignId(g.getPlanId());
				groupVo.setGroupId(g.getGroupId());
				groupVo.setGroupName(g.getGroupName());
				groupVo.setPrice(g.getGroupInfo().getPrice());
				groupVo.setRegionList(g.getGroupInfo().getRegListStr());
				groupVo.setGenderInfo(g.getGroupInfo().getGenderInfo());
				if (g.getGroupInfo().getRegListStr() != null) {
					String[] regions = g.getGroupInfo().getRegListStr().split("\\|");
					List<Integer> regList = new ArrayList<Integer>(regions.length);
					try {
						for (int j = 0; j < regions.length; j++) {
							regList.add(Integer.parseInt(regions[j]));
						}
					} catch (Exception e) {
						log.error("Error to parse regions" + e.getMessage());
					}
					String regListStr = StringUtils.join(regList, AccountFileWebConstants.ACCOUNTFILE_DATA_SEPERATOR);
					groupVo.setRegionList(regListStr);
				}
				groupVo.setStatus(g.getGroupState());
				groupVo.setType(g.getGroupType());
				groupVo.setTargetType(ApiTargetTypeUtil.toApiGroupTargetType(g.getTargetType()));
				groupVo.setKtTargetType(ApiTargetTypeUtil.toApiKtTargetType(g.getTargetType()));
				groupVo.setIsITEnabled( (TargettypeUtil.hasIT(g.getTargetType())) ? 1 : 0  );
				groupVo.setIsSmart(g.getIsSmart());
				if(TargettypeUtil.hasKT(g.getTargetType())){
					groupVo.setAliveDays(g.getQtAliveDays());
				}
				
				// 再设置推广组设置信息

				if (groupInfoMap.containsKey(g.getGroupId())) {
					cproGroupInfo = groupInfoMap.get(g.getGroupId());
					groupInfoMap.get(g.getGroupId());
					groupVo.setIsAllSite(cproGroupInfo.getIsAllSite());
					groupVo.setIsAllRegion(cproGroupInfo.getIsAllRegion());
					groupVo.setSites(getSiteurlsFromSiteids(cproGroupInfo.getSiteListStr()));
					groupVo.setTrades(cproGroupInfo.getSiteTradeListStr());
				}

				// 设置过滤IP
				if (groupExculdeIpMap.containsKey(g.getGroupId())) {
					groupVo.setExculdeIps(groupExculdeIpMap.get(g
							.getGroupId()));
				}

				// 设置过滤网站
				if (groupExcludeSiteMap.containsKey(g.getGroupId())) {
					groupVo.setExcludSites(groupExcludeSiteMap.get(g
							.getGroupId()));
				}
				
				// 设置推广组附加信息
				setAttachInfoForGroup(g.getGroupId(),groupInfoMap,groupVo,attachMap);

				list.add(groupVo);
			}
		}


		return list;
	}
	
    /**
     * 对每个推广组校验并填充附件信息
     * 
     * @param groupId 推广组id
     * @param groupInfoMap 推广组对应的groupinfo信息
     * @param groupVo 返回vo对象
     * @param attachMap 推广组对应附加信息列表
     */
    private void setAttachInfoForGroup(Integer groupId, Map<Integer, CproGroupInfo> groupInfoMap, GroupVo groupVo,
            Map<Integer, List<AttachInfo>> attachMap) {
        CproGroupInfo groupInfo = groupInfoMap.get(groupId);
        if (groupInfo == null) {
            return;
        }

        if (groupAttachInfoMgr.validUbmcInfo(groupInfo.getAttachUbmcId(), groupInfo.getAttachUbmcVersionId())) {
            List<AttachInfo> groupAttachInfos = attachMap.get(groupId);
            fillAttachInfo(groupAttachInfos, groupVo, groupInfo.getAttachUbmcId(), groupInfo.getAttachUbmcVersionId());
        }
    }
	
	/**
	 * 填充每个推广组附加信息
	 * @param groupAttachInfos 推广组对应附加信息
	 * @param groupVo 返回Vo对象
	 * @param mcId 物料id
	 * @param versionId 版本id
	 */
	private void fillAttachInfo(List<AttachInfo> groupAttachInfos,GroupVo groupVo,Long mcId,Integer versionId) {
	    if (CollectionUtils.isEmpty(groupAttachInfos)) {
	        return ;
	    }
	    for (AttachInfo groupAttachInfo : groupAttachInfos) {
            if (groupAttachInfo.getAttachType() == AttachInfoConstant.ATTACH_INFO_PHONE) {
                groupVo.setPhone(groupAttachInfo.getAttachContent());
                groupVo.setPhoneStatus(groupAttachInfo.getState());
            }
            if (groupAttachInfo.getAttachType() == AttachInfoConstant.ATTACH_INFO_CONSULT) {
                groupVo.setConsult(1);
                groupVo.setConsultStatus(groupAttachInfo.getState());
            }
            if (groupAttachInfo.getAttachType() == AttachInfoConstant.ATTACH_INFO_MESSAGE) {
                groupVo.setMessagePhone(groupAttachInfo.getAttachContent());
                String message = groupAttachInfoMgr.getMessageFromUbmc(mcId, versionId);
                groupVo.setMessage(message);
                groupVo.setMessageStatus(groupAttachInfo.getState());
            }
        }
	    
	}

    /**
	 * 根据site的字符串返回其siteurl字符串 <br>
	 * 
	 * @param siteids 例如123|456|789
	 * @return String 例如youku.com|sina.com.cn|nihao123.com
	 * 
	 */
	private String getSiteurlsFromSiteids(String siteids){
		if(StringUtils.isEmpty(siteids)){
			return "";
		}
		String[] siteIdsArray = StringUtils.split(siteids, AccountFileWebConstants.GROUPINFO_SITE_TRADE_SEPERATOR);
		List<Integer> siteIds = new ArrayList<Integer>();
		for(String siteId: siteIdsArray){
			siteIds.add(Integer.parseInt(siteId));
		}
		
		BDSiteInfo info = null;
		StringBuffer sb = new StringBuffer();
		for(Integer siteId: siteIds){
			info = UnionSiteCache.siteInfoCache.getSiteInfoBySiteId(siteId);
			if(info == null){
				BDSiteLiteInfo liteInfo = UnionSiteCache.allSiteLiteCache.getSiteLiteInfo(siteId);
				if(liteInfo == null){
					log.warn("siteId=" + siteId + " cannot be found in UnionSiteCache");
					continue;
				}
				//重新构造BDSiteInfo对象，其中各种参数都是基元类型，不需要初始化和判空
				info = new BDSiteInfo();
				info.setSiteid(siteId);
				info.setSiteurl(liteInfo.getSiteUrl());
			}
			String siteUrl = info.getSiteurl();
			sb.append(siteUrl);
			sb.append(AccountFileWebConstants.ACCOUNTFILE_DATA_SEPERATOR);
		}
		
		if(sb.length() > 0){
			return sb.substring(0, sb.length() - 1);
		}
		return sb.toString();
	}

	public GroupSiteConfigMgr getGroupSiteConfigMgr() {
		return groupSiteConfigMgr;
	}

	public void setGroupSiteConfigMgr(GroupSiteConfigMgr groupSiteConfigMgr) {
		this.groupSiteConfigMgr = groupSiteConfigMgr;
	}

	public CproGroupMgr getCproGroupMgr() {
		return cproGroupMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

    public void setAttachInfoMgr(AttachInfoMgr attachInfoMgr) {
        this.attachInfoMgr = attachInfoMgr;
    }

    public void setGroupAttachInfoMgr(GroupAttachInfoMgr groupAttachInfoMgr) {
        this.groupAttachInfoMgr = groupAttachInfoMgr;
    }
}
