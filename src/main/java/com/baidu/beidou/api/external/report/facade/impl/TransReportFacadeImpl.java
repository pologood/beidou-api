package com.baidu.beidou.api.external.report.facade.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.report.facade.TransReportFacade;
import com.baidu.beidou.tool.driver.vo.HmSite;
import com.baidu.beidou.tool.driver.vo.HmTrans;
import com.baidu.beidou.tool.service.HolmesMgr;
import com.baidu.beidou.util.BeidouCoreConstant;
import com.baidu.beidou.util.DateUtils;
import com.baidu.beidou.util.memcache.BeidouCacheInstance;

public class TransReportFacadeImpl implements TransReportFacade {
	
	private static final Log LOG = LogFactory.getLog(TransReportFacadeImpl.class);
	
	private HolmesMgr holmesMgr;

	private int transDataReadyTime = 14;
	
	private int signFlagMemcacheExpireTimeInMinute = 10;

	
	/* (non-Javadoc)
	 * @see com.baidu.beidou.report.facade.impl.TransReportFacade#needToFetchTransData(java.lang.Integer, java.util.Date, java.util.Date, boolean)
	 */
	public boolean needToFetchTransData(Integer userId, Date from, Date to,boolean forceGet) {
		
		boolean need = false;
		boolean signed = this.isTransToolSigned(userId, forceGet);
		if (signed) {
			Date now = new Date();
			Date today = DateUtils.getDateCeil(now).getTime();
			Date yestoday = DateUtils.getDateCeil(DateUtils.getPreviousDay(now)).getTime();

			need = from.before(yestoday) ;//查询的数据超过昨天， 则始终需要查询Doris
			if (!need) {//查询的时间在昨天今天以内
				Calendar readyTime = DateUtils.getCurDateCeil();
				readyTime.add(Calendar.HOUR_OF_DAY, transDataReadyTime);
				need = now.after(readyTime.getTime()) && from.before(today);
			}
		}
		return need;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.baidu.beidou.report.facade.impl.TransReportFacade#isTransToolSigned(java.lang.Integer, boolean)
	 */
	public boolean isTransToolSigned(Integer userId, boolean forceGet ){
		String key = BeidouCoreConstant.MEMCACHE_KEY_IS_TRANS_TOOL_SIGNED + String.valueOf(userId);
		
		boolean signed = false;//用户是否签订转化工具协议？
		
		if (!forceGet) {//非强制从holmes获取，则从缓存中获取
			Object obj = BeidouCacheInstance.getInstance().memcacheGet(key);
			if (obj != null) {
				signed = (Boolean)(obj);
			} else {
				forceGet = true;
			}
		}
		
		if (forceGet) {
			try {
				signed = holmesMgr.isContractSigned(userId);
			} catch (Exception e) {
				LOG.error("调用holmes API获取用户是否签约时发生错误", e);
			}
			BeidouCacheInstance.getInstance().memcacheSet(key, signed, 60*signFlagMemcacheExpireTimeInMinute);//十分钟失效
		}
		
		return signed;
	}


	public void setHolmesMgr(HolmesMgr holmesMgr) {
		this.holmesMgr = holmesMgr;
	}

	public void setTransDataReadyTime(int transDataReadyTime) {
		this.transDataReadyTime = transDataReadyTime;
	}


	public void setSignFlagMemcacheExpireTimeInMinute(
			int signFlagMemcacheExpireTimeInMinute) {
		this.signFlagMemcacheExpireTimeInMinute = signFlagMemcacheExpireTimeInMinute;
	}


	public String getTransName(Integer userId, Long siteId, Long transId) {
		if (siteId == null || transId == null || userId == null) {
			return null;
		}

		boolean isSiteIdValid = false;
		List<HmSite> siteList = this.holmesMgr.getSiteListForSelect(userId);//网站ID是与开放主域相匹配的网站
		if (CollectionUtils.isNotEmpty(siteList)) {
			for (HmSite site : siteList) {
				if (site.getSite_id().equals(siteId)) {
					isSiteIdValid = true;
				}
			}
		}
		
		if (!isSiteIdValid) {
			return null;//用户不存在这个siteId;
		} 
		
		String transName = null;
		List<HmTrans> list = this.holmesMgr.getPageTransListBySiteId(siteId);
		if (CollectionUtils.isNotEmpty(list)) {
			for (HmTrans trans : list) {
				if (trans.getTrans_id().equals(transId)) {
					transName = trans.getName();
					break;
				}
			}
		}

		return transName;
	}

	
	private TempSitesAndTrans getSitesAndTransByUserId(int userId, List<Long> transSiteIds, List<Long> transTargetIds){
		TempSitesAndTrans tsat = new TempSitesAndTrans();
		if(userId > 0){
			List<Long> siteIdList = null;
			List<Long> transIdList = null;
			
			//获取用户的站点列表ID信息
			if(CollectionUtils.isEmpty(transSiteIds)){//没有指定siteId，则需要获取所有的siteId
			   List<HmSite> siteList = holmesMgr.getSiteListByUserId(userId);
			   if(!CollectionUtils.isEmpty(siteList)){
				   siteIdList = new ArrayList<Long>();
				   for(HmSite hmSite : siteList){
					   siteIdList.add(hmSite.getSite_id());
				   }
			   }
			} else {
				siteIdList = transSiteIds;
			}
			tsat.setTransSiteIds(siteIdList);
			
			
			//获取站点对应的转化列表ID信息
			if(CollectionUtils.isNotEmpty(siteIdList) && CollectionUtils.isEmpty(transTargetIds)){//已经获取了siteId，但原没有指定转化ID，需要获取相应的转化ID
				transIdList = new ArrayList<Long>();
				for(Long hmSiteId : siteIdList){
					List<HmTrans> t_list = this.holmesMgr.getPageTransListBySiteId(hmSiteId);
					if (CollectionUtils.isNotEmpty(t_list)) {
						for (HmTrans trans : t_list) {
							transIdList.add(trans.getTrans_id());
						}
					}
				}
			} else {
				transIdList = transTargetIds;
			}
			
			
			tsat.setTransTargetIds(transIdList);
		}
		
		if (CollectionUtils.isEmpty(tsat.getTransSiteIds())
				||
			CollectionUtils.isEmpty(tsat.getTransTargetIds())
		) {
			LOG.warn("没有有效的站点ID和转化ID：siteIds=" + tsat.getTransSiteIds() + ",transIds=" + tsat.getTransTargetIds());
		}
		
		return tsat;
	}
	
	
	class TempSitesAndTrans{
		private List<Long> transSiteIds;
		private List<Long> transTargetIds;
		
		public List<Long> getTransSiteIds() {
			return transSiteIds;
		}
		public void setTransSiteIds(List<Long> transSiteIds) {
			this.transSiteIds = transSiteIds;
		}
		public List<Long> getTransTargetIds() {
			return transTargetIds;
		}
		public void setTransTargetIds(List<Long> transTargetIds) {
			this.transTargetIds = transTargetIds;
		}
	}


	
}


