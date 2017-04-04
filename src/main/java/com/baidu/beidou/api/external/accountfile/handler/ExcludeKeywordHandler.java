package com.baidu.beidou.api.external.accountfile.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.beidou.api.external.accountfile.vo.AbstractVo;
import com.baidu.beidou.api.external.accountfile.vo.ExcludeKeywordVo;
import com.baidu.beidou.cprogroup.bo.WordExclude;
import com.baidu.beidou.cprogroup.bo.WordPackExclude;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.facade.WordExcludeFacade;

/**
 * 
 * ClassName: ExcludeKeywordHandler  <br>
 * Function: 排除关键词输出VO的handler
 *
 * @author zhangxu
 * @date Sep 19, 2012
 */
public class ExcludeKeywordHandler extends Handler {

	//private static final Log log = LogFactory.getLog(ExcludeKeywordHandler.class);

	private WordExcludeFacade wordExcludeFacade;

	/**
	 * 生成排除关键词VO对象列表 <br>
	 * 
	 * @param userId 用户id
	 * @param planIds 推广计划ids
	 * @param groupIds 推广组ids
	 * @return
	 * 
	 */
	public List<AbstractVo> getVo(int userId, List<Integer> planIds, List<Integer> groupIds) {
		List<AbstractVo> list = new ArrayList<AbstractVo>();

		List<WordExclude> wordExcludes = wordExcludeFacade.getWordExcludeList(userId, groupIds);
		List<WordPackExclude> wordPackExcludes = wordExcludeFacade.getWordPackExcludeList(userId, groupIds);
		
		Map<Integer, List<WordExclude>> wordExcludesMap = new HashMap<Integer, List<WordExclude>>();
		for (WordExclude wordExclude : wordExcludes) {
			Integer groupId = wordExclude.getGroupId();
			if (!wordExcludesMap.containsKey(groupId)) {
				wordExcludesMap.put(groupId, new ArrayList<WordExclude>());
			}
			wordExcludesMap.get(groupId).add(wordExclude);
		}
		
		Map<Integer, List<WordPackExclude>> wordPackExcludesMap = new HashMap<Integer, List<WordPackExclude>>();
		for (WordPackExclude wordPackExclude : wordPackExcludes) {
			Integer groupId = wordPackExclude.getGroupId();
			if (!wordPackExcludesMap.containsKey(groupId)) {
				wordPackExcludesMap.put(groupId, new ArrayList<WordPackExclude>());
			}
			wordPackExcludesMap.get(groupId).add(wordPackExclude);
		}
		
		
		for (Integer groupId : groupIds) {
			if (wordExcludesMap.containsKey(groupId)) {
				for (WordExclude wordExclude : wordExcludesMap.get(groupId)) {
					ExcludeKeywordVo excludeKeywordVo = new ExcludeKeywordVo();
					excludeKeywordVo.setGroupid(groupId);
					excludeKeywordVo.setType(CproGroupConstant.EXCLUDE_WORD);
					excludeKeywordVo.setKeyword(wordExclude.getKeyword());
					list.add(excludeKeywordVo);
				}
			}
			
			if (wordPackExcludesMap.containsKey(groupId)) {
				for (WordPackExclude wordPackExclude : wordPackExcludesMap.get(groupId)) {
					ExcludeKeywordVo excludeKeywordVo = new ExcludeKeywordVo();
					excludeKeywordVo.setGroupid(groupId);
					excludeKeywordVo.setType(CproGroupConstant.EXCLUDE_WORD_PACK);
					excludeKeywordVo.setKeywordPackId(wordPackExclude.getPackId());
					list.add(excludeKeywordVo);
				}
			}
		}

		return list;
	}

	public WordExcludeFacade getWordExcludeFacade() {
		return wordExcludeFacade;
	}

	public void setWordExcludeFacade(WordExcludeFacade wordExcludeFacade) {
		this.wordExcludeFacade = wordExcludeFacade;
	}


}
