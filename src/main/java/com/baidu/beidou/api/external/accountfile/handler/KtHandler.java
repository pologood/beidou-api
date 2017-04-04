package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.KtVo;
import com.baidu.beidou.cprogroup.bo.CproKeyword;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.service.CproKeywordMgr;
import com.baidu.beidou.util.BeanMapperProxy;

/**
 * 
 * ClassName: KtHandler <br>
 * Function: 关键词文件输出VO的handler
 * 
 * @author zhangxu
 * @since 2.0.1
 * @date Mar 31, 2012
 */
public class KtHandler extends Handler {

	//private static final Log log = LogFactory.getLog(KtHandler.class);

	private CproKeywordMgr cproKeywordMgr;

	/**
	 * 生成关键词VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();
		
		List<CproKeyword> groupKeyWordList = new ArrayList<CproKeyword>();

		for (Integer groupId : groupIds) {
			List<CproKeyword> keywords = cproKeywordMgr.findByGroupId(groupId, userId);
			groupKeyWordList.addAll(keywords);
		}

		// 获取关键词展现资格列表
		Map<Integer, Integer> ktBlackList = cproKeywordMgr.getKTBlackListByUserId(userId);

		if (groupKeyWordList != null) {
			Mapper mapper = BeanMapperProxy.getMapper();
			for (CproKeyword cproKeyword : groupKeyWordList) {
				KtVo ktVo = mapper.map(cproKeyword, KtVo.class);
				int quality = getKtWordQuality(ktBlackList, cproKeyword.getKeyword(), cproKeyword.getWordId().intValue());
//				if(ktVo.getQualityDg() == 0){
//					ktVo.setQualityDg(CproGroupConstant.KT_WORD_QUALITY_DEGREE_3);
//				} else {
//					ktVo.setQualityDg(quality);
//				}
				ktVo.setQualityDg(quality);
				list.add(ktVo);
			}
		}


		return list;
	}
	
	/**
	 * getKtWordQuality: 填充关键词展现资格
	 * @param keyword 关键词字面
	 * @param wordId atomId
	 * @version GroupConfigServiceImpl
	 * @author zhangxu
	 * @date 2012-4-1
	 */
	private int getKtWordQuality (Map<Integer, Integer> blackList, String keyword, Integer wordId) {
		if (blackList != null){
			Integer q = blackList.get(wordId);
			if (q != null) {
				return q;
			} else if (com.baidu.beidou.util.StringUtils.hitKTBlackRules(keyword)) {
				return CproGroupConstant.KT_WORD_QUALITY_DEGREE_1;
			}
		}
		
		return CproGroupConstant.KT_WORD_QUALITY_DEGREE_3;
	}

	public CproKeywordMgr getCproKeywordMgr() {
		return cproKeywordMgr;
	}

	public void setCproKeywordMgr(CproKeywordMgr cproKeywordMgr) {
		this.cproKeywordMgr = cproKeywordMgr;
	}

}
