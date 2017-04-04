package com.baidu.beidou.api.external.kr.facade;

import java.util.List;
import java.util.Map;

import com.baidu.beidou.api.external.kr.vo.KrInfoVo;
import com.mongodb.MongoException;

public interface KeyRecommNVFacade {
	
	public final static int PassiveRecomm = 0; //被动推荐
	public final static int WordRecomm = 1; //关键词推荐
	public final int URLRecomm = 2; //URL推荐

	
	/**
	 * 下载KR推荐结果
	 * @param userId
	 * @param groupId
	 * @param krquery
	 * @param reglist
	 * @param isAllRegion
	 * @param qtAliveDays
	 * @param isTmp
	 * @param targeting KT定向方式
	 * @return
	 * @throws MongoException
	 * @throws Exception
	 */
	public List<KrInfoVo> getRecommWord(Integer userId, Long groupId, String krquery, 
			List<Integer> reglist, Integer isAllRegion ,Integer qtAliveDays, boolean isTmp, Integer targeting) throws MongoException, Exception;
}
