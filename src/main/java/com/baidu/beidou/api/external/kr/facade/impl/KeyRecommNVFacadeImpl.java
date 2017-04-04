package com.baidu.beidou.api.external.kr.facade.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.cprogroup.bo.CproKeyword;
import com.baidu.beidou.cprogroup.bo.TmpCproKeyword;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.dao.CproKTStatMGDao;
import com.baidu.beidou.cprogroup.dao.CproKTUserMGDao;
import com.baidu.beidou.api.external.kr.facade.KeyRecommNVFacade;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.service.CproKeywordMgr;
import com.baidu.beidou.api.external.kr.util.GroupSiteUtil;
import com.baidu.beidou.cprogroup.vo.KTWordStatVo;
import com.baidu.beidou.api.external.kr.vo.KrInfoVo;
import com.baidu.beidou.exception.BusinessException;
import com.baidu.beidou.api.external.kr.util.BizConf;
import com.baidu.beidou.api.external.kr.constant.ErrorCodeConstant;
import com.baidu.beidou.util.BeidouConfig;
import com.baidu.beidou.util.StringUtils;
import com.baidu.beidou.api.external.kr.util.UrlUtils;
import com.baidu.beidou.api.external.kr.util.UrlUtils.QUERYTYPE;
import com.baidu.beidou.util.atomdriver.AtomUtils;
import com.baidu.beidou.util.krdriver.bo.KrAttrElement;
import com.baidu.beidou.util.krdriver.bo.KrRegion;
import com.baidu.beidou.util.krdriver.bo.KrReqInfo;
import com.baidu.beidou.util.krdriver.bo.KrResInfo;
import com.baidu.beidou.util.krdriver.bo.KrResRecordInfo;
import com.baidu.beidou.util.krdriver.bo.KrResult;
import com.baidu.beidou.util.krdriver.bo.KrResultGroup;
import com.baidu.beidou.util.krdriver.bo.KrResultItem;
import com.baidu.beidou.util.krdriver.constant.KrConstant;
import com.baidu.beidou.util.krdriver.exception.KrPageException;
import com.baidu.beidou.util.krdriver.exception.KrWordException;
import com.baidu.beidou.util.krdriver.service.KrDriverService;
import com.baidu.beidou.util.krdriver.service.impl.KrDriverServiceImpl;
import com.baidu.beidou.util.redriver.bo.REResultItemBO;
import com.baidu.beidou.util.redriver.constant.REConstant;
import com.baidu.beidou.util.redriver.dto.KeywordQueryDTO;
import com.baidu.beidou.util.redriver.dto.KeywordTargetQueryDTO;
import com.baidu.beidou.util.redriver.dto.RERequestDTO;
import com.baidu.beidou.util.redriver.dto.RegionQueryDTO;
import com.baidu.beidou.util.redriver.exception.REWordException;
import com.baidu.beidou.util.redriver.service.REService;
import com.mongodb.MongoException;

public class KeyRecommNVFacadeImpl implements KeyRecommNVFacade {

	private static final String KR_PROVIDER = "beidou";
	private CproKeywordMgr cproKeywordMgr;
	private CproGroupMgr cproGroupMgr;
	private CproKTUserMGDao cproKTUserMGDao;
	private CproKTStatMGDao cproKTStatMGDao;
	private REService reService;
	
	private static final Log log = LogFactory
			.getLog(KeyRecommNVFacadeImpl.class);

	public List<KrInfoVo> getRecommWord(Integer userId, Long groupId,String krquery, List<Integer> regList,
			Integer AllRegion ,Integer ktAliveDays, boolean isTmp, Integer targeting)
			throws MongoException, Exception {
		//如果没有得到ktAliveDays的值或者值不正确，则使用默认7天
		if(ktAliveDays==null ||!ArrayUtils.contains(CproGroupConstant.GROUP_KT_ALIVEDAYS,ktAliveDays)){
			ktAliveDays=7;
		   log.warn("Not get right ktAliveDays ,ktAliveDays=7");
		}
		boolean isallreg = isAllRegion(regList,AllRegion);
		List<KrRegion> regionlist = new ArrayList<KrRegion>(0);
		if (!isallreg) {
			regionlist = getKrRegionList(regList);
		}
		KrResult krResult = null;
		Long luserId = null;
		if(userId!=null)
			luserId = userId.longValue();
		try {
			log.info("begin fetch recommand word from fckr");
			QUERYTYPE type = UrlUtils.getKrQueryType(krquery);
			switch (type) {
			case JUSTURL:
				krResult = getAllWordsFromKRPage(luserId, 0l,
						krquery, 1l, regionlist, null, 0l, 0l, null, null,
						groupId, isTmp);
				krResult.setActualQueryType(type.getType());
				break;
			case JUSTWORD:
				krResult = getAllWordsFromKRGW(luserId, 0l, krquery,
						1l, regionlist, null, 0l, 0l, null, null, groupId,
						isTmp);
				krResult.setActualQueryType(type.getType());
				break;
			case URLANDWORD:
				krResult = getAllWordsFromKRPage(luserId, 0l,
						krquery, 1l, regionlist, null, 0l, 0l, null, null,
						groupId, isTmp);
				krResult.setActualQueryType(type.getType());
				break;
			default:
				break;
			}
		} catch (BusinessException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception ex) {
			log.error("keyRecommNVFacade.getRecommWord  Exception cause:"
					+ ex.getMessage(), ex);
			throw ex;
		} catch (Error error) {
			log.error("keyRecommNVFacade.getRecommWord  Error cause:"
					+ error.getMessage(), error);
		}
		log.info("end fetch recommand word from fckr");

		if (krResult != null) {
			List<KrInfoVo> ret = parseKrResult2Vo(userId, krResult, regList , ktAliveDays, isallreg, targeting);
			log.info("Get recommand word for userid:" + userId + ", result:" + ret);
			return ret;
		}
		return new ArrayList<KrInfoVo>(0);
	}

	/**
	 * 判断区域是否为全部区域
	 * 
	 * @param regions
	 * @return
	 */
	private boolean isAllRegion(List<Integer> regions, Integer allRegion) {
		if (allRegion == 1 || regions == null)
			return true;
		if (regions.size() == 0 || (regions.size() == 1 && regions.get(0) == 0))
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	private List<KrInfoVo> parseKrResult2Vo(Integer userId, KrResult krResult, List<Integer> regIds, 
			int ktAliveDays, boolean isallreg, Integer targeting) throws MongoException, Exception {
		if (krResult == null) {
			return new ArrayList<KrInfoVo>(0);
		}
		
		/**	去除从fengchao获取的黑马词标签 2012.05.25	
		//获取黑马词标签
		int newtag = -1;
		if(krResult.getAttrs() != null){
			for(int i = 0 ;i < krResult.getAttrs().length;i++){
				if(krResult.getAttrs()[i].getRsn2().equals("黑马"))
					newtag = i;
			}
		}
		**/

		List<KrInfoVo> ResArray = new ArrayList<KrInfoVo>();
		//获取已推荐词wordid list
		List<Long> wordidList = new ArrayList<Long>();
		if (krResult.getGroups() != null && !krResult.getGroups().isEmpty()) {
			for (KrResultGroup group : krResult.getGroups()) {
				for (KrResultItem krResultItem : group.getItems()) {
					if(krResultItem.getWordid() != null && krResultItem.getWordid() !=0){
						//返回非null非0才可以信任
						wordidList.add(krResultItem.getWordid());
					}
				}
			}
		}
		log.info("userid: " + userId + " begin fetch word usercount from mongodb");
		Map<Long, Integer> usercountMap = cproKTUserMGDao.getUsercountBywordid(wordidList);
		log.info("userid: " + userId + " end fetch word usercount from mongodb");		
		
		log.info("userid: " + userId + " begin fetch word stat from mongodb");
		Map<Long, KTWordStatVo> statInfoMap = cproKTStatMGDao.getStatInfoBywordid(wordidList, regIds, isallreg, targeting);
		log.info("userid: " + userId + " end fetch word stat from mongodb");
		
		log.info("userid: " + userId + " begin fetch uv info from re");
		Map<Long,REResultItemBO> uvMap = this.getUVinfoFromRE(userId, wordidList, regIds, targeting, ktAliveDays);
		log.info("userid: " + userId + " end fetch uv info from re");
		
		log.info("begin wapper word object");
		if (krResult.getGroups() != null && !krResult.getGroups().isEmpty()) {
			for (KrResultGroup group : krResult.getGroups()) {
				for (KrResultItem krResultItem : group.getItems()) {
					KrInfoVo item = new KrInfoVo();
					//字面和竞争激烈度
					item.setKw(krResultItem.getWord());	//推荐词字面
					item.setWordid(krResultItem.getWordid());
					item.setCompdg(getShowCompdg(usercountMap.get(krResultItem.getWordid())));//竞争激烈程度
					KTWordStatVo vo = statInfoMap.get(krResultItem.getWordid());
					//规则化的Cookies数和日均展现量
					if(vo == null){
						item.setAwgdscnt(5L);
						item.setAdvanceDscnt(5L);
					}else{
						item.setAwgdscnt(getShowPv(vo.getAvgAdview()));//日均展现量
						item.setAdvanceDscnt(getShowPv(vo.getAdvanceAdview()));//日均展现量（高级）
					}
					
					REResultItemBO uvbo = uvMap.get(krResultItem.getWordid());
					if(uvbo == null){
						item.setUv(5L);
						item.setAdvanceUv(5L);
					}else{
						item.setUv(getShowUv(new Long(uvbo.getBasicUV())));//覆盖人数
//						item.setAdvanceUv(getShowUv(new Long(uvbo.getAdvanceUV())));//覆盖人数（高级）
					}
					
					/**	去除从fengchao获取的黑马词提示及日均搜索量 2012.05.25		
					int dhwd = 0;
					for(Integer rsn1 : krResultItem.getWord_rsns()){
						if(rsn1 == newtag){
							dhwd = 1;
							break;
						}
					}
					item.setDhwd(dhwd);
				
					if(isallreg){
						//规则化的全国日均搜索量
						item.setAwgsrcnt(getShowPv(krResultItem.getPv()));
					}
					else{
						//规则化的给定地域日均搜索量
						item.setAwgsrcnt(getShowPv(krResultItem.getPv_zone()));
					}
					**/	
					ResArray.add(item);
				}
			}
		}
		log.info("end wapper word object");
		return ResArray;
	}

	public KrResult getAllWordsFromKRGW(Long userid, Long logid, String word,
			Long mtype, List<KrRegion> regionList, String negwords,
			Long rgfilter, Long pvmon, Long planid, Long unitid, Long groupid,boolean isTmp)
			throws BusinessException, KrWordException {
		if (word == null || "".equals(word.trim())) {
			throw new BusinessException(
					ErrorCodeConstant.ERRMSG_TOOL_KR_WORD_ISNULL);
		} else {
			if (!StringUtils.isFieldLengthOK(word.trim(),
					BizConf.TOOLS_KRGW_WORDLEN)) {
				throw new BusinessException(
						ErrorCodeConstant.ERRMSG_TOOL_KR_WORD_TOOLONG);
			}
		}
		KrReqInfo krgwReq = KrReqInfo.buildKrgwReqInfo(logid, getMtype(mtype),
				word, BizConf.KRGW_REQUEST_KWC_LOWER,
				BizConf.KRGW_REQUEST_KWC_UPPER, negwords,
				BizConf.KRGW_REQUEST_NUM, BizConf.KRGW_REQUEST_PV_LOWER, pvmon,
				BizConf.KRGW_REQUEST_PV_UPPER, rgfilter, regionList, userid,
				planid, unitid);

		log.info("krgw request: userid=" + userid + "&logid="
				+ krgwReq.getLogId() + "&word=" + word + "&mtype="
				+ krgwReq.getMatch_mode() + "&regions=" + regionList.toString()
				+ "&negwords=" + negwords + "&rgfilter=" + rgfilter + "&pvmon="
				+ pvmon + "&planid=" + planid + "&unitid=" + unitid);
		KrDriverService krdriver = KrDriverServiceImpl.getInstance("word");
		KrResInfo krResInfo = krdriver.getRecomWordsByKrgw(krgwReq, KR_PROVIDER); // throw
		// KrWordException
		// to
		// alarm
		// 增加判断KR是否全部封禁逻辑
		if (isKrForbid(krResInfo)) {
			throw new BusinessException(
					ErrorCodeConstant.ERRMSG_TOOL_KR_SERVER_FORBID);
		}
		// regionList为空时，是全部地域
		return getKrResult(krgwReq.getLogId(),userid, groupid, krResInfo,
				regionList.size() == 0, BizConf.KRGW_RESPONSE_NUM,isTmp);
	}

	public KrResult getAllWordsFromKRPage(Long userid, Long logid, String url,
			Long mtype, List<KrRegion> regionList, String negwords,
			Long rgfilter, Long pvmon, Long planid, Long unitid, Long groupid,
			boolean isTmp) throws BusinessException, KrPageException {
		//url不符合规范
		if (url == null || "".equals(url.trim())) {
			throw new BusinessException(
					ErrorCodeConstant.ERRMSG_TOOL_KR_URL_ISNULL);
		} else {
			if (!StringUtils.isFieldLengthOK(url.trim(),
					BizConf.TOOLS_KRPAGE_URLLEN)) {
				throw new BusinessException(
						ErrorCodeConstant.ERRMSG_TOOL_KR_URL_TOOLONG);
			}
		}
		//构造krpage请求
		KrReqInfo krpageReq = KrReqInfo.buildKrpageReqInfo(logid,
				getMtype(mtype), url, BizConf.KRGW_REQUEST_KWC_LOWER,
				BizConf.KRGW_REQUEST_KWC_UPPER, negwords,
				BizConf.KRGW_REQUEST_NUM, BizConf.KRGW_REQUEST_PV_LOWER, pvmon,
				BizConf.KRGW_REQUEST_PV_UPPER, rgfilter, regionList, userid,
				planid, unitid);

		log.info("krpage request: userid=" + userid + "&logid="
				+ krpageReq.getLogId() + "&url=" + url + "&mtype="
				+ krpageReq.getMatch_mode() + "&regions="
				+ regionList.toString() + "&negwords=" + negwords
				+ "&rgfilter=" + rgfilter + "&pv=mon" + pvmon);
		// 调用KR Driver，获得响应
		KrDriverService krdriver = KrDriverServiceImpl.getInstance("page");
		KrResInfo krResInfo = krdriver.getRecomWordsBykrpage(krpageReq);
		// 增加判断KR是否全部封禁逻辑
		if (isKrForbid(krResInfo)) {
			throw new BusinessException(
					ErrorCodeConstant.ERRMSG_TOOL_KR_SERVER_FORBID);
		}
		// regionList为空时，是全部地域
		return getKrResult(krpageReq.getLogId(),userid , groupid, krResInfo,
				regionList.size() == 0, BizConf.KRGW_RESPONSE_NUM, isTmp);
	}

	/**
	 * 从资源预估获取UV信息
	 * @param userId
	 * @param wordIdList
	 * @param wordIdWordMap
	 * @param regIdList
	 * @param targeting
	 * @param ktAliveDays
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Map<Long,REResultItemBO> getUVinfoFromRE(Integer userId, List<Long> wordIdList, 
			 List<Integer> regIdList, Integer targeting, Integer ktAliveDays){
		Map<Long,REResultItemBO> resultMap = new HashMap<Long, REResultItemBO>();
		if (wordIdList==null || ktAliveDays==null || regIdList==null || targeting==null) {
			return resultMap;
		}

		// 通过wordid获取word字面
		//		Set wordidSet = new HashSet (wordIdList);
		// 从阿童木获取关键词字面 FIXBUG
		Set<Long> wordIdSet = new HashSet<Long>(wordIdList);
		Map<Long, String> wordIdWordMap = AtomUtils.getWordById(wordIdSet);
		
		//构造请求参数
		RERequestDTO requestDTO = new RERequestDTO();
		List<KeywordQueryDTO> keywordQueryDTOList = new ArrayList<KeywordQueryDTO>(wordIdList.size());
		for (int i = 0; i < wordIdList.size(); i++) {
			KeywordQueryDTO tmp = new KeywordQueryDTO();
			tmp.setWordid(wordIdList.get(i));
			tmp.setWord(wordIdWordMap.get(wordIdList.get(i)));
			tmp.setPatternType(REConstant.PATTERN_TYPE);
			keywordQueryDTOList.add(tmp);
		}
		KeywordTargetQueryDTO keywordTargetQueryDTO = new KeywordTargetQueryDTO();
		keywordTargetQueryDTO.setAlivedays(ktAliveDays);
		keywordTargetQueryDTO.setTargetType(targeting);
		keywordTargetQueryDTO.setKeywordList(keywordQueryDTOList);
		
		RegionQueryDTO regionQueryDTO = new RegionQueryDTO();
		regionQueryDTO.setRegionList(regIdList);
		regionQueryDTO.setSelect(true);
		
		requestDTO.setKeywordTargetQuery(keywordTargetQueryDTO);
		requestDTO.setRegionQuery(regionQueryDTO);
		requestDTO.setSearch_id(userId + "-" + UUID.randomUUID());

		try {
			List<REResultItemBO> uvResultList = reService.getREResultBo(requestDTO);
			if(null != uvResultList){
				for(REResultItemBO tmpItemBO : uvResultList){
					resultMap.put(tmpItemBO.getWordId(), tmpItemBO);
				}
			}
		} catch (REWordException e) {
			log.error("[kr-api] getUVinfoFromRE failed " + e.getMessage(), e);
		}
		return resultMap;
	}
	
	/**
	 * pv字段的规则化
	 * 
	 * @param pv
	 * @return
	 */
	private Long getShowPv(Long pv) {
		if( pv == null)
			return 5l;
		if (pv < 5)
			return 5L;
		else if (pv <= 10)
			return 10L;
		else if (pv <= 999)
			return ((pv - 1) / 10 + 1) * 10;
		else if (pv <= 20000)
			return ((pv - 1) / 100 + 1) * 100;
		else
			return 20001L;
	}
	/**
	 * uv字段的规则化
	 * 
	 * @param uv
	 * @return
	 */
	private Long getShowUv(Long uv) {
		if( uv == null)
			return 5l;
		if (uv < 5)
			return 5L;
		else if (uv <= 10)
			return 10L;
		else if (uv <= 999)
			return ((uv - 1) / 10 + 1) * 10;
		else if (uv <= 20000)
			return ((uv - 1) / 100 + 1) * 100;
		else
			return 20001L;
	}
	/**
	 * usercount字段规则化
	 * 
	 * @param Qtc
	 * @return
	 */
	private Integer getShowCompdg(Integer usercount) {
		if (usercount == null || usercount < 1)
			return 0;
		else if (usercount == 1 || usercount == 2)
			return usercount;
		else if (usercount <= 5)
			return 3;
		else if (usercount <= 10)
			return 4;
		else if (usercount <= 20)
			return 5;
		else if (usercount <= 50)
			return 6;
		else
			return 7;
	}

	/**
	 * <p>
	 * matchtype字段正确性验证，错误时设置默认值为广泛
	 * </p>
	 * 
	 * @param mtype
	 * @return
	 */
	private Long getMtype(Long mtype) {
		Long ret = BizConf.KR_MATCH_TYPE_WIDE;
		if (mtype != null) {
			if (mtype.equals(BizConf.KR_MATCH_TYPE_WIDE)
					|| mtype.equals(BizConf.KR_MATCH_TYPE_WORD)
					|| mtype.equals(BizConf.KR_MATCH_TYPE_ACCU)) {
				ret = mtype;
			}
		}
		return ret + 1;
	}

	/**
	 * 
	 * @param logid
	 * @param userid
	 * @param krResInfo
	 * @param isAllRegion
	 * @param resnum
	 * @return
	 * @author wangyouliang
	 * @param groupid
	 */

	private KrResult getKrResult(Long logid, Long userid, Long groupid,
			KrResInfo krResInfo, boolean isAllRegion, Long resnum, boolean isTmp) {
		KrResult krResult = new KrResult();
		Integer gid = null;
		Integer uid = null;
		if(groupid!=null){
			gid = groupid.intValue();
		}
		if(userid!=null){
			uid = userid.intValue();
		}
		if (krResInfo != null && krResInfo.getRes_array().length != 0) {
			krResult.setLogid(krResInfo.getLogId());
			// 获取过滤wordid list
			Set<Long> listKrrWordid = new HashSet<Long>();
			// 获取cproKTKeyword已购词
			if (!isTmp && gid != null) {
				List<CproKeyword> keywords = cproKeywordMgr.getCproKeywordsByGroup(gid, uid);
				for (CproKeyword keyword: keywords) {
					listKrrWordid.add(keyword.getWordId().longValue());
				}
			} else if (isTmp && groupid != null){
				List<TmpCproKeyword> keywords = cproKeywordMgr.getTmpKeywordByGroup(groupid);
				for (TmpCproKeyword tempword: keywords) {
					listKrrWordid.add(tempword.getWordId().longValue());
				}
			}
			
			// 获取userid下黑名单词
			Map<Integer, Integer> blacklist = cproKeywordMgr.getKTBlackListByUserId(uid);
			for (Integer key : blacklist.keySet()) {
				if (blacklist.get(key) == CproGroupConstant.KT_WORD_QUALITY_DEGREE_1) {
					listKrrWordid.add(key.longValue());
				}
			}
			
			// 将krdriver返回的结果封装成KrResult。
			getKrResultFromKrResInfo(krResInfo, krResult, isAllRegion, listKrrWordid);
			// 过滤掉推词中没有使用到的属性，包扩分组属性和其他属性
			removeEmptyTag(krResult);
			// 截取推荐结果的数量(300L)
			krResult = truncateResult(krResult, resnum);
		}
		return krResult;
	}

	/**
	 * 判断KR返回结果是否为完全封禁
	 * 
	 * @param krResInfo
	 * @return
	 */
	private boolean isKrForbid(KrResInfo krResInfo) {
		return KrResInfo.KR_STATUS_FORBID == krResInfo.getKr_status()
				&& krResInfo.getRes_array().length < 1;
	}

	
	private void getKrResultFromKrResInfo(KrResInfo krResInfo, KrResult result,
			boolean isAllRegion, Set<Long> excludeWordids) {

		if (krResInfo == null || result == null) {
			return;
		}
		KrAttrElement krAttrElement[] = new KrAttrElement[krResInfo.getAttrs().length];
		// 把属性付给result
		for (int i = 0; i < krAttrElement.length; i++) {
			krAttrElement[i] = new KrAttrElement(krResInfo.getAttrs()[i]
					.getRsn2(), krResInfo.getAttrs()[i].getRsn_attr(),
					krResInfo.getAttrs()[i].getRsn2_rsn_popdesc(), krResInfo
							.getAttrs()[i].getRsn2_rsn_desc());
		}
		result.setAttrs(krAttrElement);
		// 给每个推词加上索引，给FE使用
		for (int i = 0; i < krResInfo.getRes_array().length; i++) {
			if (null == krResInfo.getRes_array()[i]) {
				break;
			}
			krResInfo.getRes_array()[i].setWordIndex(Long.valueOf(i));
		}
		String[] rsn1 = krResInfo.getRsn1_array();
		for (int i = 0; i < rsn1.length; i++) {
			result.getGroups().add(new KrResultGroup(i, rsn1[i]));
		}
		for (KrResRecordInfo krResRecord : krResInfo.getRes_array()) {
			if (null == krResRecord) {
				break;
			}
			Long wordid = krResRecord.getWordid();
			if (excludeWordids.contains(wordid) || 
					StringUtils.hitKTBlackRules(krResRecord.getWord()) ||
					!StringUtils.isFieldLengthOK(krResRecord.getWord(),KrConstant.MAX_KEYWORD_RES_LENGTH)//add by wangxiaokun since cpweb411	
					) {
				continue;
			}
			Long rsn1index = krResRecord.getRsn1_id();
			KrResultGroup group = result.getGroups().get(rsn1index.intValue());
			// 如果是全部地域，则前端应显示pv字段，否则前端应显示pv_zone字段
			Long pvToBeShow = isAllRegion ? krResRecord.getPv() : krResRecord
					.getPv_zone();
			// 去除pv小于等于0的项
			if (pvToBeShow == null || pvToBeShow <= 0) {
				log.warn("[QTKR]The pv value is 0,logid=" + result.getLogid()
						+ ",wordid=" + krResRecord.getWordid() + ",wordid="
						+ krResRecord.getWord());
			} else if (group != null) {
				group.getItems().add(
						new KrResultItem(krResRecord.getWord(), krResRecord
								.getWordid(), krResRecord.getKwc(), krResRecord
								.getPv(), krResRecord.getPv_ad(), krResRecord
								.getPv_show(), krResRecord.getPv_trend_month(),
								krResRecord.getPv_trend_value(), krResRecord
										.getPv_zone(), krResRecord
										.getPv_show_zone(), krResRecord
										.getRsn1_id(), krResRecord
										.getWord_rsns(), krResRecord
										.getWordIndex()));
			}
		}
		// 删除其下没有推词的一级理由分组
		Iterator<KrResultGroup> it = result.getGroups().iterator();
		while (it.hasNext()) {
			if (it.next().getItems().size() == 0) {
				it.remove();
			}
		}
	}

	// 在属性集合中删除没有使用到的属性
	private void removeEmptyTag(KrResult krResult) {
		// attrSet中存储的是推词中用到了那些属性下标(属性集合数组的下标)
		LinkedHashSet<Integer> attrSet = new LinkedHashSet<Integer>();
		// 组
		for (KrResultGroup group : krResult.getGroups()) {
			// 组中词
			for (KrResultItem krResultItem : group.getItems()) {
				// 词中属性
				attrSet.addAll(Arrays.asList(krResultItem.getWord_rsns()));
			}
		}
		ArrayList<Integer> attrList = new ArrayList<Integer>(attrSet);

		// 遍历推词的属性数组，把新的属性下标放入到推词的属性数组中
		for (KrResultGroup group : krResult.getGroups()) {
			for (KrResultItem krResultItem : group.getItems()) {
				ArrayList<Integer> word_rsns = new ArrayList<Integer>(Arrays
						.asList(krResultItem.getWord_rsns()));
				ArrayList<Integer> attrListTmp = new ArrayList<Integer>(
						attrList);
				/**
				 * attrSetTmp.retainAll(word_rsns):得到attrSetTmp中与word_rsns中公共的部分
				 * 如果推词中用到的属性下标和attrSetTmp中存储的下标相同，则把attrSetTmp的相应下标放入该推词的属性列表中
				 */
				ArrayList<Integer> temp = new ArrayList<Integer>();
				if (attrListTmp.retainAll(word_rsns)) {
					for (int i = 0; i < attrListTmp.size(); i++) {
						temp.add(attrList.indexOf(attrListTmp.get(i)));
					}
					krResultItem.setWord_rsns(temp.toArray(new Integer[temp
							.size()]));
				}
			}
		}
		// 建立一个新的属性集合，与推词中的属性id列表对应
		ArrayList<KrAttrElement> krAttrElement = new ArrayList<KrAttrElement>(
				attrList.size());
		Iterator<Integer> iter = attrList.iterator();
		while (iter.hasNext()) {
			Integer i = (Integer) iter.next();
			krAttrElement.add(krResult.getAttrs()[i]);
		}
		krResult.setAttrs(krAttrElement.toArray(new KrAttrElement[krAttrElement
				.size()]));
	}

	public static List<KrRegion> getKrRegionList(List<Integer> rgids) {
		List<KrRegion> res = new ArrayList<KrRegion>();

		// 转化为凤巢一级地域
		Collection<Integer> fcreglist =  BeidouConfig
				.transferBdRegionToSf(rgids);
		for (Integer reg : fcreglist) {
			res.add(new KrRegion(reg.longValue(), null));
		}
		return res;
	}

	/**
	 * <p>
	 * 截取推荐结果: 1. 预处理------向返回bo中的每一个一级理由组添加一项，并将该项从输入bo中删除 2.
	 * 算法第一步------计算每组应保留的数目,取整 3. 算法第二步------将取整后的差额补到各组 4.
	 * 算法第三步------将各组保留的前N项添加到结果bo中
	 * </p>
	 * 
	 * @param krResult
	 * @author zhouhuan
	 * @date Feb 26, 2010
	 * @version 1.1.0
	 */
	private KrResult truncateResult(KrResult krResult, Long count) {
		// return krResult;
		if (krResult == null)
			return null;
		// 截取目标数量
		int targetcount = count.intValue();
		// 原结果集数量
		int originSize = krResult.getTotalSize();
		if (originSize <= targetcount) {
			return krResult;
		}
		KrResult ret = new KrResult();
		ret.setLogid(krResult.getLogid());
		ret.setAttrs(krResult.getAttrs());
		// 1. 预处理------向返回bo中的每一个一级理由组添加一项，并将该项从输入bo中删除
		for (KrResultGroup group : krResult.getGroups()) {
			KrResultGroup retGroup = new KrResultGroup(group.getRsn1id(), group
					.getRsn1());
			retGroup.getItems().add(group.getItems().get(0));
			group.getItems().remove(0);
			ret.getGroups().add(retGroup);
			targetcount--;
		}
		// 删除处理后数量为0的组
		Iterator<KrResultGroup> it = krResult.getGroups().iterator();
		while (it.hasNext()) {
			if (it.next().getItems().size() == 0) {
				it.remove();
			}
		}

		// 对剩下的输入bo中的推荐项执行截取算法
		// 剩余总数
		originSize = krResult.getTotalSize();
		// 组数
		int groupCount = krResult.getGroups().size();
		// 每组个数
		int groupSizeArray[] = new int[groupCount];
		// 每组保留数量
		int remainSizeArray[] = new int[groupCount];
		// 取整后保留数量和
		int remainSum = 0;

		// 2. 算法第一步------计算每组应保留的数目,取整
		for (int i = 0; i < groupCount; i++) {
			groupSizeArray[i] = krResult.getGroups().get(i).getItems().size();
			remainSizeArray[i] = groupSizeArray[i] * targetcount / originSize;
			remainSum += remainSizeArray[i];
		}
		// 3. 算法第二部------将取整后的差额补到各组
		int gap = targetcount - remainSum;
		if (gap > 0 && gap < groupCount) {
			int[] addindex = getMinimalNIndex(remainSizeArray, gap);
			for (Integer i : addindex) {
				remainSizeArray[i]++;
			}
		}
		// 4. 算法第三步------将各组保留的前N项添加到结果bo中
		for (int i = 0; i < groupCount; i++) {
			KrResultGroup group = krResult.getGroups().get(i);
			addToGroup(ret, group, remainSizeArray[i]);
		}

		return ret;
	}

	/**
	 * 从array中取出最小的n个数的下标列表
	 * 
	 * @param array
	 * @param n
	 * @return
	 */
	private int[] getMinimalNIndex(int[] array, int n) {
		Integer[] indice = new Integer[array.length];
		int[] ret = new int[n];
		for (int i = 0; i < n; i++) {
			ret[i] = i;
		}
		for (int i = 0; i < array.length; i++) {
			indice[i] = i;
		}
		Arrays.sort(indice, new KRTruncateComparator(array));
		for (int i = 0; i < n; i++) {
			ret[i] = indice[i];
		}
		return ret;
	}

	private void addToGroup(KrResult ret, KrResultGroup inputgroup, int addcount) {
		for (KrResultGroup group : ret.getGroups()) {
			if (inputgroup.getRsn1id() == group.getRsn1id()) {
				if (addcount > 0) {
					if (addcount >= inputgroup.getItems().size()) {
						group.getItems().addAll(inputgroup.getItems());
					} else {
						group.getItems().addAll(
								inputgroup.getItems().subList(0, addcount));
					}
				}
			}
		}
	}

	public List<KrInfoVo> downloadRecommWord(Integer userId, Long groupId,
			String krquery, List<Integer> reglist, Integer isAllRegion,
			Integer qtAliveDays, boolean isTmp) throws MongoException,
			Exception {
		// TODO Auto-generated method stub
		return null;
	}

/******** getter and setter start ********/
	public CproKTUserMGDao getCproKTUserMGDao() {
		return cproKTUserMGDao;
	}

	public void setCproKTUserMGDao(CproKTUserMGDao cproKTUserMGDao) {
		this.cproKTUserMGDao = cproKTUserMGDao;
	}

	public CproKTStatMGDao getCproKTStatMGDao() {
		return cproKTStatMGDao;
	}

	public void setCproKTStatMGDao(CproKTStatMGDao cproKTStatMGDao) {
		this.cproKTStatMGDao = cproKTStatMGDao;
	}

	public CproGroupMgr getCproGroupMgr() {
		return cproGroupMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

	public CproKeywordMgr getCproKeywordMgr() {
		return cproKeywordMgr;
	}

	public void setCproKeywordMgr(CproKeywordMgr cproKeywordMgr) {
		this.cproKeywordMgr = cproKeywordMgr;
	}
	
	
	public REService getReService() {
		return reService;
	}

	public void setReService(REService reService) {
		this.reService = reService;
	}
	/******** getter and setter end ********/
}


class KRTruncateComparator implements Comparator<Integer> {

	private int array[];

	public KRTruncateComparator(int array[]) {
		this.array = array;
	}

	public int compare(Integer o1, Integer o2) {
		if (array[o1] > array[o2])
			return 1;
		else if (array[o1] == array[o2])
			return 0;
		else
			return -1;
	}
}
