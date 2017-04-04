package com.baidu.beidou.api.external.kr.facade;

import java.util.List;
import java.util.Map;

import com.baidu.beidou.api.external.kr.vo.KrInfoVo;
import com.baidu.beidou.api.external.kr.vo.KrInfoVo4Related;
import com.mongodb.MongoException;

public interface KrKeywordFacade {
	public static final long WORD_RECOMM = 1L; // 关键词推荐
	public static final long URL_RECOMM = 2L; // URL推荐
	public static final long RELATED_RECOMM = 3L; // 相关种子词推荐
	public static final String CMKR_PROVIDER_BEIDOU = "beidou_web";
	public static final String CMKR_PROVIDER_BEIDOU_API = "beidou_api";
	public static final long REQUIRED_UV_ONLY_NORMAL = 1L;// 只标准UV
	public static final long REQUIRED_UV_ONLY_ADVANCED = 2L;// 只高级UV
	public static final long REQUIRED_UV_BOTH = 3L;// 标准和高级UV
	
	/**
	 * 下载CTKR推荐结果
	 * 
	 * @param userId
	 * @param groupId
	 * @param krquery
	 * @param reglist
	 * @param allRegion
	 * @param isTmp
	 * @return
	 * @throws MongoException
	 * @throws Exception
	 */
	public List<KrInfoVo> getRecommWord(Integer userId, Integer groupId, String krquery, List<Integer> reglist, Integer allRegion, boolean isTmp, Integer targeting, Integer ktAliveDays) throws MongoException, Exception;
	
	/**
	 * 获取“种子词对应的相关推荐关键词”
	 * 
	 * @param userId
	 * @param groupId
	 * @param krquery
	 * @param reglist
	 * @param secondReg
	 * @param allRegion
	 * @param targeting
	 * @param ktAliveDays
	 * @return
	 * @throws Exception
	 *             2012-10-17 created by kanghongwei
	 */
	public List<KrInfoVo4Related> getRecommWord4Related(Integer userId, Integer groupId, String krquery, List<Integer> reglist, Integer allRegion, Integer targeting, Integer ktAliveDays) throws Exception;

}
