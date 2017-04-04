package com.baidu.beidou.api.external.kr.facade.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.kr.constant.ErrorCodeConstant;
import com.baidu.beidou.api.external.kr.facade.KrKeywordFacade;
import com.baidu.beidou.api.external.kr.util.GroupSiteUtil;
import com.baidu.beidou.api.external.kr.vo.KrInfoVo;
import com.baidu.beidou.api.external.kr.vo.KrInfoVo4Related;
import com.baidu.beidou.cprogroup.bo.CproKeyword;
import com.baidu.beidou.cprogroup.bo.TmpCproKeyword;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.dao.CproKTStatMGDao;
import com.baidu.beidou.cprogroup.dao.CproKTUserMGDao;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.service.CproKeywordMgr;
import com.baidu.beidou.cprogroup.vo.KTWordStatVo;
import com.baidu.beidou.exception.BusinessException;
import com.baidu.beidou.util.StringUtils;
import com.baidu.beidou.util.krdriver.bo.KrResultItemBO;
import com.baidu.beidou.util.krdriver.constant.KrConstant;
import com.baidu.beidou.util.krdriver.dto.KrRequestDTO;
import com.baidu.beidou.util.krdriver.dto.KrSeedWordInfoDTO;
import com.baidu.beidou.util.krdriver.exception.KrOutOfRangeException;
import com.baidu.beidou.util.krdriver.exception.KrWordException;
import com.baidu.beidou.util.krdriver.service.KrService;
import com.mongodb.MongoException;

public class KrKeywordFacadeImpl implements KrKeywordFacade {

	
	private CproKeywordMgr cproKeywordMgr;
	private CproGroupMgr cproGroupMgr;
    private KrService<KrResultItemBO> krService;
	private CproKTUserMGDao cproKTUserMGDao;
	private CproKTStatMGDao cproKTStatMGDao;
    private static final Log log = LogFactory.getLog(KrKeywordFacadeImpl.class);

	/**
	 * action 层调用推荐KT词，返回KrInfoVo
	 */
	public List<KrInfoVo> getRecommWord(Integer userId, Integer groupId, String krquery,
			List<Integer> reglist, Integer allRegion,
			boolean isTmp, Integer targeting, Integer ktAliveDays) throws MongoException, Exception {

		//判断种子词长度,过长和过短都抛出异常
		if (krquery == null || "".equals(krquery.trim())) {
			throw new BusinessException(ErrorCodeConstant.ERRMSG_TOOL_KR_WORD_ISNULL);
		} else {
			if (!StringUtils.isFieldLengthOK(krquery.trim(),KrConstant.MAX_KEYWORD__REQ_LENGTH)) {
				throw new BusinessException(ErrorCodeConstant.ERRMSG_TOOL_KR_WORD_TOOLONG);
			}
		}

		boolean isAllReg = isAllRegion(reglist, allRegion);
		List<KrResultItemBO> resultItemBOs = null;
		try {
			KrRequestDTO krRequestDTO = combineKrRequestDTO(userId, krquery, isAllReg, reglist, targeting, ktAliveDays, WORD_RECOMM);
			krRequestDTO.setUnit_id(Long.valueOf(groupId));
			resultItemBOs = krService.getKrResultBo(krRequestDTO);
		} catch (KrOutOfRangeException krOutOfRangeException) {
			throw new BusinessException(ErrorCodeConstant.ERRMSG_TOOL_KR_WORD_TOOLONG);
		} catch(KrWordException krWordException){
			throw new BusinessException(ErrorCodeConstant.ERRMSG_TOOL_KR_SEEDTYPE_INNER_ERROR);
		}
		
		if(CollectionUtils.isEmpty(resultItemBOs)){
			log.info("[CMKR]cmkr return 0 word!");
			return new ArrayList<KrInfoVo>(0);
		}
		Set<Long> purchasedWordids=new HashSet<Long>();
		Set<Long> blackWordids=new HashSet<Long>();

		//查询已购词
		if(groupId!=null && groupId!=0){
			if(isTmp){
				List<TmpCproKeyword> tmpCproKeywords=cproKeywordMgr.getTmpKeywordByGroup(groupId.longValue());
				for (TmpCproKeyword keyword: tmpCproKeywords) {
					purchasedWordids.add(keyword.getWordId().longValue());
				}
			}else{
				List<CproKeyword> cproKeywords= cproKeywordMgr.getCproKeywordsByGroup(groupId, userId);
				for (CproKeyword keyword: cproKeywords) {
					purchasedWordids.add(keyword.getWordId().longValue());
				}
			}
		}

		//查询黑名单词和不相干词
		Map<Integer, Integer> blacklist = cproKeywordMgr.getKTBlackListByUserId(userId);
		for (Integer key : blacklist.keySet()) {
			if (blacklist.get(key) == CproGroupConstant.KT_WORD_QUALITY_DEGREE_1) {
				 blackWordids.add(key.longValue());
			}
		}
		
		
		//输出日志打印过滤原因使用
		Map<String, List<KrResultItemBO>> filtelog=new HashMap<String, List<KrResultItemBO>>();	
		filtelog.put("purchase", new ArrayList<KrResultItemBO>());
		filtelog.put("black", new ArrayList<KrResultItemBO>());
		filtelog.put("irregular", new ArrayList<KrResultItemBO>());

		//过滤并打印日志
		List<KrResultItemBO> filtedBo=new ArrayList<KrResultItemBO>();//过滤后的结果
		
		for(KrResultItemBO itemBO:resultItemBOs){
			
			if (purchasedWordids.contains(itemBO.getWordid())) {// 过滤已购词
				filtelog.get("purchase").add(itemBO);
				continue;
			} else if (blackWordids.contains(itemBO.getWordid())) {// 过滤黑名单和不相干词
				filtelog.get("black").add(itemBO);
				continue;
			} else if (StringUtils.hitKTBlackRules(itemBO.getWord())
					|| !StringUtils.isFieldLengthOK(itemBO.getWord(),
							KrConstant.MAX_KEYWORD_RES_LENGTH)) { // 过滤不规则词和cmkr返回超过40字节的推荐词
				filtelog.get("irregular").add(itemBO);
				continue;
			}
			
			filtedBo.add(itemBO);
		 }

		//打印被过滤词日志
		log.info("[KTKR]sid="+krService.getSid()+",filte words:"+filtelog);
		if(CollectionUtils.isEmpty(filtedBo)){
			log.info("[KTKR]Filter out all recommended word!");
			return new ArrayList<KrInfoVo>(0);
		}
		List<KrInfoVo> result=new ArrayList<KrInfoVo>();
				
		result=parse2Vo(filtedBo, reglist, isAllReg, targeting);

		log.info("[KTKR]sid=" + krService.getSid() + ",recommand words:" + result);
		return result;
	}

	private KrRequestDTO combineKrRequestDTO(Integer userId, String krquery, boolean isAllReg, List<Integer> reglist, Integer targeting, Integer ktAliveDays, Long krMode) {
		// 构造种子词数组
		KrSeedWordInfoDTO[] seed_word_array = { new KrSeedWordInfoDTO(krquery, null) };

		//List<Integer> allRegList = GroupSiteUtil.genRegionList(reglist, secondReg);

		Long[] zone_search_ids = getKrRegionArray(isAllReg, reglist);

		return new KrRequestDTO(0L, CMKR_PROVIDER_BEIDOU_API, krMode, targeting.longValue(), seed_word_array, userId.longValue(), zone_search_ids, REQUIRED_UV_ONLY_NORMAL, ktAliveDays.longValue());
	}

	/**

	 * 将过滤后cmkr返回的结果拼接成KrInfoVo
	 * 
	 * @param itemBOs
	 * @param firstRegionList
	 * @param isallreg
	 * @return
	 * @throws IOException
	 * @throws MongoException
	 */
	private List<KrInfoVo> parse2Vo(List<KrResultItemBO> itemBOs, List<Integer> reglist, boolean isallreg, Integer targeting) 
			throws MongoException, IOException, Exception {
		  List<KrInfoVo> resArray = new ArrayList<KrInfoVo>();

	      //对二级地域全选的情况下，按一级地域处理
			//List<Integer> reglist = GroupSiteUtil.genRegionList(firstRegionList, secondRegMap);
			//获取已推荐词的id集合，提供下面调用函数的参数
			List<Long> wordidlist = new ArrayList<Long>();
			for(KrResultItemBO itemBO:itemBOs){
				if(itemBO.getWordid()!=null && itemBO.getWordid()!=0){
					wordidlist.add(itemBO.getWordid());
				}
			}

			//从mongoDB中得到每个词的竞争激烈程度
			log.info("[KTKR]sid="+krService.getSid()+",fetch usercount from mongo begin.");
			Map<Long, Integer> usercountMap = cproKTUserMGDao.getUsercountBywordid(wordidlist);
			log.info("[KTKR]sid="+krService.getSid()+",fetch usercount from mongo end.");
			

			//从mongoDB中得到每个词的pv
			log.info("[KTKR]sid="+krService.getSid()+",fetch pv from mongo begin.");
			Map<Long, KTWordStatVo> statInfoMap = cproKTStatMGDao.getStatInfoBywordid(wordidlist, reglist, isallreg, targeting);
			log.info("[KTKR]sid="+krService.getSid()+",fetch pv from mongo end.");
			
			for (KrResultItemBO krItem : itemBOs) {
				KrInfoVo krInfoVo=new KrInfoVo();
				krInfoVo.setWordid(krItem.getWordid());
				krInfoVo.setKw(krItem.getWord());
				krInfoVo.setUv(getShowUv(krItem.getBasic_uv()));
				krInfoVo.setAdvanceUv(getShowUv(krItem.getAdvance_uv()));
				krInfoVo.setKwRoot(krItem.getKwRoot());
				krInfoVo.setReasonId(krItem.getReasonId());
				//竞争激烈程度
				krInfoVo.setCompdg(getShowCompdg(usercountMap.get(krItem.getWordid())));

				//日均展现量
				KTWordStatVo vo = statInfoMap.get(krItem.getWordid());
				if(vo == null){
					krInfoVo.setAwgdscnt(5L);
					krInfoVo.setAdvanceDscnt(5L);
				}else{	
					krInfoVo.setAwgdscnt(getShowPv(vo.getAvgAdview()));//日均展现量
					krInfoVo.setAdvanceDscnt(getShowPv(vo.getAdvanceAdview()));//日均展现量(高级)
				}
				resArray.add(krInfoVo);
			}
			
		return resArray;
	}

	public List<KrInfoVo4Related> getRecommWord4Related(Integer userId, Integer groupId, String krquery, List<Integer> reglist, Integer allRegion, Integer targeting, Integer ktAliveDays) throws Exception {
		if (org.apache.commons.lang.StringUtils.isEmpty(krquery)) {
			throw new BusinessException(ErrorCodeConstant.ERRMSG_TOOL_KR_WORD_ISNULL);
		}
		if (!StringUtils.isFieldLengthOK(krquery.trim(), KrConstant.MAX_KEYWORD__REQ_LENGTH)) {
			throw new BusinessException(ErrorCodeConstant.ERRMSG_TOOL_KR_WORD_TOOLONG);
		}

		List<KrInfoVo4Related> result = null;
		try {
			boolean isAllReg = isAllRegion(reglist, allRegion);
			KrRequestDTO krRequestDTO = combineKrRequestDTO(userId, krquery, isAllReg, reglist, targeting, ktAliveDays, RELATED_RECOMM);
			// “相关推荐”单独设置请求词个数
			krRequestDTO.setReq_num(KrConstant.DEFAULT_RELATE_RES_KEYWORD_NUM);
			// 设置“推广组ID”
			krRequestDTO.setUnit_id(Long.valueOf(groupId));
			List<KrResultItemBO> resultItemBOs = krService.getKrResultBo(krRequestDTO);
			if (CollectionUtils.isNotEmpty(resultItemBOs)) {
				result = new ArrayList<KrInfoVo4Related>(resultItemBOs.size());
				for (KrResultItemBO bo : resultItemBOs) {
					result.add(new KrInfoVo4Related(bo.getWordid(), bo.getWord()));
				}
			}
		} catch (KrOutOfRangeException krOutOfRangeException) {
			throw new BusinessException(ErrorCodeConstant.ERRMSG_TOOL_KR_WORD_TOOLONG);
		} catch (KrWordException krWordException) {
			throw new BusinessException(ErrorCodeConstant.ERRMSG_TOOL_KR_SEEDTYPE_INNER_ERROR);
		}

		if (CollectionUtils.isEmpty(result)) {
			log.info("[CMKR]cmkr recommond 4 related return 0 word!");
			return new ArrayList<KrInfoVo4Related>(0);
		}
		return result;
	}
	
	
	/**
	 * 将一级和二级地域列表转化为数组
	 * @param isallreg 
	 * @param regions
	 * @return Long[]
	 */
	private Long[] getKrRegionArray(boolean isallreg, List<Integer> regList) {
		if(isallreg){
			return new Long[0];
		}
		Integer[] array=(Integer[])regList.toArray(new Integer[0]);
		Long[] region_array=new Long[array.length];
		for (int i = 0; i < region_array.length; i++) {
			
			region_array[i]=array[i].longValue();
		}
		
		return region_array;
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
	 * @param usercount
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
	
	/******** getter and setter start ********/
	public KrService<KrResultItemBO> getKrService() {
		return krService;
	}
	public void setKrService(KrService<KrResultItemBO> krService) {
		this.krService = krService;
	}
    
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
	/******** get and setter end ********/
}
