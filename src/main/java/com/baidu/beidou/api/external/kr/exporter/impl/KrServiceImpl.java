package com.baidu.beidou.api.external.kr.exporter.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;

import com.baidu.beidou.api.external.cprogroup.util.ApiTargetTypeUtil;
import com.baidu.beidou.api.external.kr.constant.KrConstant;
import com.baidu.beidou.api.external.kr.error.KrErrorCode;
import com.baidu.beidou.api.external.kr.exporter.KrService;
import com.baidu.beidou.api.external.kr.facade.KeyRecommNVFacade;
import com.baidu.beidou.api.external.kr.facade.KrKeywordFacade;
import com.baidu.beidou.api.external.kr.util.BizConf;
import com.baidu.beidou.api.external.kr.util.KrFilterHelper;
import com.baidu.beidou.api.external.kr.util.UrlUtils;
import com.baidu.beidou.api.external.kr.util.UrlUtils.QUERYTYPE;
import com.baidu.beidou.api.external.kr.vo.KRResponse;
import com.baidu.beidou.api.external.kr.vo.KRResultType;
import com.baidu.beidou.api.external.kr.vo.KRSuggestResultType;
import com.baidu.beidou.api.external.kr.vo.KrInfoVo;
import com.baidu.beidou.api.external.kr.vo.KrInfoVo4Related;
import com.baidu.beidou.api.external.kr.vo.RecommWordVo;
import com.baidu.beidou.api.external.kr.vo.RelatedWordVo;
import com.baidu.beidou.api.external.kr.vo.request.GetKRBySeedRequest;
import com.baidu.beidou.api.external.kr.vo.request.GetKRSuggestBySeedRequest;
import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.api.external.util.vo.PaymentResult;
import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.constant.UnionSiteCache;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.pack.bo.WordPackKeyword;
import com.baidu.beidou.pack.service.WordPackKeywordMgr;
import com.baidu.beidou.util.BeanMapperProxy;
import com.baidu.beidou.util.StringUtils;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.fengchao.sun.base.BaseResponse;
import com.baidu.fengchao.sun.base.BaseResponseOptions;
import com.baidu.fengchao.tools.annotation.RPCMethod;
import com.baidu.fengchao.tools.annotation.RPCService;
import com.baidu.fengchao.tools.conf.ReturnType;

/**
 * 
 * ClassName: KrService  <br>
 * Function: 关键词推荐
 *
 * @author zhangxu
 * @date Aug 14, 2012
 */
@RPCService(serviceName = "KrService")
public class KrServiceImpl implements KrService {
	
	private static final Log log = LogFactory.getLog(KrServiceImpl.class);
	
	private CproGroupMgr cproGroupMgr;
	
	private KrKeywordFacade krKeywordFacade;
	
	private KeyRecommNVFacade keyRecommNVFacade; 
    
    private WordPackKeywordMgr packKeywordMgr = null;

    /**
	 * 获取推荐关键词
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param param 调用参数
	 * @param options 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return BaseResponse 
	 */
	@RPCMethod(methodName = "getKRBySeed", returnType = ReturnType.ARRAY)
	public BaseResponse<KRResultType> getKRBySeed(BaseRequestUser user, GetKRBySeedRequest param, BaseRequestOptions options) {
		
		BaseResponse<KRResultType> response = new BaseResponse<KRResultType>();
		BaseResponseOptions option = new BaseResponseOptions();
		option.setTotal(1);
		response.setOptions(option);
		
		response = DRAPIMountAPIBeanUtils.validateUserAndParam(response, user, param);
		if (CollectionUtils.isNotEmpty(response.getErrors())) {
			return response;
		}
		
		//int opUser = DRAPIMountAPIBeanUtils.getUser(user)[0];
		int userId = DRAPIMountAPIBeanUtils.getUser(user)[1];
		
		String queryKw = param.getSeed();
		long groupid = param.getGroupId();
		int[] regList = param.getRegionList();
		int targeting = param.getTargetType();
		int ktAliveDays = param.getAliveDays();
		//int[] packIds = param.getPackIds();
		
		// 判断推广组id是否合法，如果没有传入groupId，则不过滤已购词
		Integer groupId = 0;
		if (groupid != 0l) {
			groupId = new Long(groupid).intValue();
			CproGroup group = cproGroupMgr.findCproGroupById(groupId);
			if (group == null || group.getUserId() != userId) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(KrConstant.GROUPID);
				response = DRAPIMountAPIBeanUtils.addApiError(response,
						GlobalErrorCode.UNAUTHORIZATION.getValue(),
						GlobalErrorCode.UNAUTHORIZATION.getMessage(),
						apiPosition.getPosition(), null);
				return response;
			}
		}
		
		// 种子关键词字面过滤
		if (!isKeywordValid(queryKw)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(KrConstant.SEED);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					KrErrorCode.SEED_WORD_INVALID.getValue(),
					KrErrorCode.SEED_WORD_INVALID.getMessage(),
					apiPosition.getPosition(), null);
			return response;
		}
		// 种子词去括号
		queryKw = removeKwBrackets(queryKw);
		
		// 地域配置是否合法
		int isAllRegion = 1;
		if (!ArrayUtils.isEmpty(regList)) {
			isAllRegion = 0;
			Map<Integer, String> regionMap = UnionSiteCache.regCache.getRegNameList();
			for (int i = 0; i < regList.length; i++) {
				int reg = regList[i];
				if (regionMap.get(reg) == null) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(KrConstant.REGION_LIST, i);
					response = DRAPIMountAPIBeanUtils.addApiError(response,
							KrErrorCode.REGION_NOT_FOUND.getValue(),
							KrErrorCode.REGION_NOT_FOUND.getMessage(),
							apiPosition.getPosition(), null);
					return response;
				}
			}
		}
		List<Integer> regionList = getRegList(isAllRegion, regList);
		
		// 有效期是否合法
		if (!Arrays.asList(CproGroupConstant.GROUP_KT_ALIVEDAYS).contains(ktAliveDays)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(KrConstant.ALIVE_DAYS);
			
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					KrErrorCode.KT_ALIVEDAYS_ERROR.getValue(),
					KrErrorCode.KT_ALIVEDAYS_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			return response;
		}
		
		// 关键词定向方式是否合法
		if (!ApiTargetTypeUtil.isValidKtTargetType(targeting)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(KrConstant.TARGET_TYPE);
			
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					KrErrorCode.KT_TARGETTYPE_INTERNAL_ERROR.getValue(),
					KrErrorCode.KT_TARGETTYPE_INTERNAL_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			return response;
		}
		
		/* 组合包是否合法 - 未作验证
		if (!ArrayUtils.isEmpty(packIds)) {
			//TODO
			for (Integer packId : packIds) {
				List<WordPackKeyword> wordPackKeyword = packKeywordMgr.list(packId);
				if (CollectionUtils.ise) {
					
				}
			}
		}
		List<Integer> wordPackIds = CollectionsUtil.tranformIntArrayToIntegerList(packIds);
		*/
		
		// 获取推荐关键词
		List<KrInfoVo> krVoList = new ArrayList<KrInfoVo>();
		QUERYTYPE type = UrlUtils.getKrQueryType(queryKw);
		try {
			if(type.equals(QUERYTYPE.JUSTWORD)){	
				//调用CMKR接口通过主题词获取主题词推荐词
				//若为批量操作则groupId为null，否则不为null
				krVoList = krKeywordFacade.getRecommWord(userId, groupId, queryKw, regionList, 
						isAllRegion, false, targeting, ktAliveDays);
			}else{
				Long groupID = null;
				if (groupId != null){
					groupID = groupId.longValue();
				}
				//调用FCKR接口通过主题词获取主题词推荐词
				//若为批量操作则groupId为null，否则不为null
				krVoList = keyRecommNVFacade.getRecommWord(userId,groupID, queryKw, regionList, 
						isAllRegion, ktAliveDays, false, targeting);
			}
			
			//过滤回收站词 - 未启用
            //List<KrInfoVo> filterRecycleResult = filterRecycleWord(krVoList, userId);
			
			// remove words belonging to WordPack(wpId) or CproGroup(gpId)
			// truncate to limit, 300 now
			List<KrInfoVo> filterdResult = KrFilterHelper.truncate(filterExistingKeywords(krVoList, null, null), (int) BizConf.KRGW_RESPONSE_NUM);

            //筛选包含及不包含词 - 未启用
			//List<KrInfoVo> filterInAndExResult = filterIncludeAndExcludeWord(filterdResult);
			
			// 转换返回结果
			List<KRResultType> result = new ArrayList<KRResultType>(filterdResult.size());
			Mapper mapper = BeanMapperProxy.getMapper();
			for(KrInfoVo vo : filterdResult){
				KRResultType krVo = mapper.map(vo, KRResultType.class);
				result.add(krVo);
			}

			response.getOptions().setSuccess(1);
			response.setData(result.toArray(new KRResultType[0]));
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = DRAPIMountAPIBeanUtils.addApiError(response,
					KrErrorCode.FAILED_TO_GET_KR.getValue(),
					KrErrorCode.FAILED_TO_GET_KR.getMessage(),
					PositionConstant.PARAM, null);
		}
		
		return response;
		
	}
	
	/**
	 * 关键词推荐同时，获取“相关推荐” 
	 * @param user 用户信息，包含操作者和被操作者id
	 * @param request
	 * @param apiOption 用户登陆信息，包含token、渠道、ip、日志id、dr-api来源服务器等
	 * @return ApiResult<KRSuggestResultType> 返回种子词推荐种子词
	 */
	public ApiResult<KRSuggestResultType> getKRSuggestBySeed(DataPrivilege user, GetKRSuggestBySeedRequest request, ApiOption apiOption) {
		
		ApiResult<KRSuggestResultType> result = new ApiResult<KRSuggestResultType>();
		PaymentResult pay = new PaymentResult();
		pay.setTotal(1);
		result.setPayment(pay);
		
		result = ApiResultBeanUtils.validateUserAndParam(result, user, request);
		if (result.hasErrors()) {
			return result;
		}
		
		//int opUser = user.getOpUser();
		int userId = user.getDataUser();
		
		String queryKw = request.getSeed();
		long groupid = request.getGroupId();
		int[] regList = request.getRegionList();
		int targeting = request.getTargetType();
		int ktAliveDays = request.getAliveDays();
		//int[] packIds = request.getPackIds();
		
		// 判断推广组id是否合法，如果没有传入groupId，则不过滤已购词
		Integer groupId = 0;
		if (groupid != 0l) {
			groupId = new Long(groupid).intValue();
			CproGroup group = cproGroupMgr.findCproGroupById(groupId);
			if (group == null || group.getUserId() != userId) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(KrConstant.GROUPID);
				result = ApiResultBeanUtils.addApiError(result,
						GlobalErrorCode.UNAUTHORIZATION.getValue(),
						GlobalErrorCode.UNAUTHORIZATION.getMessage(),
						apiPosition.getPosition(), null);
				return result;
			}
		}
		
		// 种子关键词字面过滤
		if (!isKeywordValid(queryKw)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(KrConstant.SEED);
			result = ApiResultBeanUtils.addApiError(result,
					KrErrorCode.SEED_WORD_INVALID.getValue(),
					KrErrorCode.SEED_WORD_INVALID.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		// 种子词去括号
		queryKw = removeKwBrackets(queryKw);
		
		// 地域配置是否合法
		int isAllRegion = 1;
		if (!ArrayUtils.isEmpty(regList)) {
			isAllRegion = 0;
			Map<Integer, String> regionMap = UnionSiteCache.regCache.getRegNameList();
			for (int i = 0; i < regList.length; i++) {
				int reg = regList[i];
				if (regionMap.get(reg) == null) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(KrConstant.REGION_LIST, i);
					result = ApiResultBeanUtils.addApiError(result,
							KrErrorCode.REGION_NOT_FOUND.getValue(),
							KrErrorCode.REGION_NOT_FOUND.getMessage(),
							apiPosition.getPosition(), null);
					return result;
				}
			}
		}
		List<Integer> regionList = getRegList(isAllRegion, regList);
		
		// 有效期是否合法
		if (!Arrays.asList(CproGroupConstant.GROUP_KT_ALIVEDAYS).contains(ktAliveDays)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(KrConstant.ALIVE_DAYS);
			
			result = ApiResultBeanUtils.addApiError(result,
					KrErrorCode.KT_ALIVEDAYS_ERROR.getValue(),
					KrErrorCode.KT_ALIVEDAYS_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 关键词定向方式是否合法
		if (!ApiTargetTypeUtil.isValidKtTargetType(targeting)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(KrConstant.TARGET_TYPE);
			
			result = ApiResultBeanUtils.addApiError(result,
					KrErrorCode.KT_TARGETTYPE_INTERNAL_ERROR.getValue(),
					KrErrorCode.KT_TARGETTYPE_INTERNAL_ERROR.getMessage(),
					apiPosition.getPosition(), null);
			return result;
		}
		
		// 获取相关推荐关键词
		List<KrInfoVo4Related> krVoList = new ArrayList<KrInfoVo4Related>();
		try {
			QUERYTYPE type = UrlUtils.getKrQueryType(queryKw);
			if (type.equals(QUERYTYPE.JUSTWORD)) {
				krVoList = krKeywordFacade.getRecommWord4Related(userId, groupId, queryKw, regionList, 
						isAllRegion, targeting, ktAliveDays);
			}

			// 转换返回结果
			Mapper mapper = BeanMapperProxy.getMapper();
			for(KrInfoVo4Related vo : krVoList){
				KRSuggestResultType krVo = mapper.map(vo, KRSuggestResultType.class);
				result = ApiResultBeanUtils.addApiResult(result, krVo);
			}
			pay.setSuccess(1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = ApiResultBeanUtils.addApiError(result,
					KrErrorCode.FAILED_TO_GET_KR.getValue(),
					KrErrorCode.FAILED_TO_GET_KR.getMessage(),
					PositionConstant.PARAM, null);
		}
		
		return result;
	}

	
	public ApiResult<KRResponse> getKRAndSuggestBySeed(DataPrivilege user, GetKRBySeedRequest request, ApiOption apiOption) {

		ApiResult<KRResponse> result = new ApiResult<KRResponse>();
		KRResponse krResponse = new KRResponse();

		PaymentResult pay = new PaymentResult();
		pay.setTotal(1);
		result.setPayment(pay);

		result = ApiResultBeanUtils.validateUserAndParam(result, user, request);
		if (result.hasErrors()) {
			return result;
		}

		//int opUser = user.getOpUser();
		int userId = user.getDataUser();

		String queryKw = request.getSeed();
		long groupid = request.getGroupId();
		int[] regList = request.getRegionList();
		int targeting = request.getTargetType();
		int ktAliveDays = request.getAliveDays();
		//int[] packIds = request.getPackIds();

		// 判断推广组id是否合法，如果没有传入groupId，则不过滤已购词
		Integer groupId = 0;
		if (groupid != 0l) {
			groupId = new Long(groupid).intValue();
			CproGroup group = cproGroupMgr.findCproGroupById(groupId);
			if (group == null || group.getUserId() != userId) {
				ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
				apiPosition.addParam(KrConstant.GROUPID);
				result = ApiResultBeanUtils.addApiError(result, GlobalErrorCode.UNAUTHORIZATION.getValue(), GlobalErrorCode.UNAUTHORIZATION.getMessage(), apiPosition.getPosition(), null);
				return result;
			}
		}

		// 种子关键词字面过滤
		if (!isKeywordValid(queryKw)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(KrConstant.SEED);
			result = ApiResultBeanUtils.addApiError(result, KrErrorCode.SEED_WORD_INVALID.getValue(), KrErrorCode.SEED_WORD_INVALID.getMessage(), apiPosition.getPosition(), null);
			return result;
		}
		// 种子词去括号
		queryKw = removeKwBrackets(queryKw);

		// 地域配置是否合法
		int isAllRegion = 1;
		if (!ArrayUtils.isEmpty(regList)) {
			isAllRegion = 0;
			Map<Integer, String> regionMap = UnionSiteCache.regCache.getRegNameList();
			for (int i = 0; i < regList.length; i++) {
				int reg = regList[i];
				if (regionMap.get(reg) == null) {
					ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
					apiPosition.addParam(KrConstant.REGION_LIST, i);
					result = ApiResultBeanUtils.addApiError(result, KrErrorCode.REGION_NOT_FOUND.getValue(), KrErrorCode.REGION_NOT_FOUND.getMessage(), apiPosition.getPosition(), null);
					return result;
				}
			}
		}
		List<Integer> regionList = getRegList(isAllRegion, regList);

		// 有效期是否合法
		if (!Arrays.asList(CproGroupConstant.GROUP_KT_ALIVEDAYS).contains(ktAliveDays)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(KrConstant.ALIVE_DAYS);

			result = ApiResultBeanUtils.addApiError(result, KrErrorCode.KT_ALIVEDAYS_ERROR.getValue(), KrErrorCode.KT_ALIVEDAYS_ERROR.getMessage(), apiPosition.getPosition(), null);
			return result;
		}

		// 关键词定向方式是否合法
		if (!ApiTargetTypeUtil.isValidKtTargetType(targeting)) {
			ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
			apiPosition.addParam(KrConstant.TARGET_TYPE);

			result = ApiResultBeanUtils.addApiError(result, KrErrorCode.KT_TARGETTYPE_INTERNAL_ERROR.getValue(), KrErrorCode.KT_TARGETTYPE_INTERNAL_ERROR.getMessage(), apiPosition.getPosition(), null);
			return result;
		}

		// 获取推荐关键词
		List<KrInfoVo> krVoList = new ArrayList<KrInfoVo>();
		QUERYTYPE type = UrlUtils.getKrQueryType(queryKw);
		try {
			if (type.equals(QUERYTYPE.JUSTWORD)) {
				//调用CMKR接口通过主题词获取主题词推荐词
				//若为批量操作则groupId为null，否则不为null
				krVoList = krKeywordFacade.getRecommWord(userId, groupId, queryKw, regionList, isAllRegion, false, targeting, ktAliveDays);
			} else {
				Long groupID = null;
				if (groupId != null) {
					groupID = groupId.longValue();
				}
				//调用FCKR接口通过主题词获取主题词推荐词
				//若为批量操作则groupId为null，否则不为null
				krVoList = keyRecommNVFacade.getRecommWord(userId, groupID, queryKw, regionList, isAllRegion, ktAliveDays, false, targeting);
			}

			//过滤回收站词 - 未启用
			//List<KrInfoVo> filterRecycleResult = filterRecycleWord(krVoList, userId);

			// remove words belonging to WordPack(wpId) or CproGroup(gpId)
			// truncate to limit, 300 now
			List<KrInfoVo> filterdResult = KrFilterHelper.truncate(filterExistingKeywords(krVoList, null, null), (int) BizConf.KRGW_RESPONSE_NUM);

			//筛选包含及不包含词 - 未启用
			//List<KrInfoVo> filterInAndExResult = filterIncludeAndExcludeWord(filterdResult);

			// 转换返回结果
			List<RecommWordVo> recommWordVos = new ArrayList<RecommWordVo>(filterdResult.size());
			Mapper mapper = BeanMapperProxy.getMapper();
			for (KrInfoVo vo : filterdResult) {
				RecommWordVo krVo = mapper.map(vo, RecommWordVo.class);
				recommWordVos.add(krVo);
			}
			krResponse.setRecommWordVos(recommWordVos.toArray(new RecommWordVo[recommWordVos.size()]));

			// 获取相关推荐关键词
			List<KrInfoVo4Related> krInfoVo4Relateds = new ArrayList<KrInfoVo4Related>();

			if (type.equals(QUERYTYPE.JUSTWORD)) {
				krInfoVo4Relateds = krKeywordFacade.getRecommWord4Related(userId, groupId, queryKw, regionList, isAllRegion, targeting, ktAliveDays);
			}

			// 转换返回结果
			List<RelatedWordVo> relatedWordVos = new ArrayList<RelatedWordVo>(krInfoVo4Relateds.size());
			for (KrInfoVo4Related vo : krInfoVo4Relateds) {
				RelatedWordVo relatedWordVo = mapper.map(vo, RelatedWordVo.class);
				relatedWordVos.add(relatedWordVo);
			}
			krResponse.setRelatedWordVos(relatedWordVos.toArray(new RelatedWordVo[relatedWordVos.size()]));
			result = ApiResultBeanUtils.addApiResult(result, krResponse);

			pay.setSuccess(1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = ApiResultBeanUtils.addApiError(result, KrErrorCode.FAILED_TO_GET_KR.getValue(), KrErrorCode.FAILED_TO_GET_KR.getMessage(), PositionConstant.PARAM, null);
		}

		return result;
	}

	/**
	 * 种子词去括号
	 * @param queryKw
	 * @return String
	 */
	private String removeKwBrackets(String queryKw){
		final String REG = "^\\{*(.*?)\\}*$";
		final Pattern pattern = Pattern.compile(REG);
		Matcher matcher = pattern.matcher(queryKw);
		if(matcher.find()){
			return matcher.group(1);
		}
		return queryKw;
	}
	

	private List<Integer> getRegList(int isAllRegion, int[] regList){
		List<Integer> result = new ArrayList<Integer>();
		if (isAllRegion == 0) {
			for (int reg: regList) {
				result.add(reg);
			}
		}
		return result;
	}
	
	private boolean isKeywordValid(String seedWord) {
		return !StringUtils.isEmpty(seedWord)
				&& com.baidu.beidou.util.StringUtils.isFieldLengthOK(seedWord,
						com.baidu.beidou.util.krdriver.constant.KrConstant.MAX_KEYWORD__REQ_LENGTH);
	}
	
	 /*
     * wordPackId and gpId will not be there in the same time
     */
    private List<KrInfoVo> filterExistingKeywords(List<KrInfoVo> words, List<Integer> wordPackIds,
                    Integer gpId) {
        if ((null != wordPackIds && null == gpId) || (null == wordPackIds && null != gpId)) {
            List<WordPackKeyword> keywords = Collections.emptyList();
            if (null != wordPackIds) {
            	for (Integer wordPackId : wordPackIds) {
            		keywords.addAll(packKeywordMgr.list(wordPackId));
				}
            } else if (null != gpId) {
                keywords = packKeywordMgr.listByGroupPackId(gpId);
            } else { // lol
                return words;
            }
            if (!keywords.isEmpty()) {
                return KrFilterHelper.filter(words, keywords);
            }
        }
        return words;
    }

	public CproGroupMgr getCproGroupMgr() {
		return cproGroupMgr;
	}

	public void setCproGroupMgr(CproGroupMgr cproGroupMgr) {
		this.cproGroupMgr = cproGroupMgr;
	}

	public KrKeywordFacade getKrKeywordFacade() {
		return krKeywordFacade;
	}

	public void setKrKeywordFacade(KrKeywordFacade krKeywordFacade) {
		this.krKeywordFacade = krKeywordFacade;
	}

	public KeyRecommNVFacade getKeyRecommNVFacade() {
		return keyRecommNVFacade;
	}

	public void setKeyRecommNVFacade(KeyRecommNVFacade keyRecommNVFacade) {
		this.keyRecommNVFacade = keyRecommNVFacade;
	}

	public WordPackKeywordMgr getPackKeywordMgr() {
		return packKeywordMgr;
	}

	public void setPackKeywordMgr(WordPackKeywordMgr packKeywordMgr) {
		this.packKeywordMgr = packKeywordMgr;
	}
	
	
}
