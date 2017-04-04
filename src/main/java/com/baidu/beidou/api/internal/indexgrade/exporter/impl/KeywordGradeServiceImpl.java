/**
 * Copyright (C) 2015年10月27日 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.indexgrade.exporter.impl;

import java.util.List;

import com.baidu.beidou.api.internal.indexgrade.exporter.KeywordGradeService;
import com.baidu.beidou.api.internal.indexgrade.exporter.KeywordIndexGradeHelper;
import com.baidu.beidou.api.internal.indexgrade.vo.KeywordState;
import com.baidu.beidou.cprogroup.bo.CproKeyword;
import com.baidu.beidou.cprogroup.service.CproKeywordMgr;

/**
 * 关键词索引分级对外提供的操作关键词状态rpc实现
 * 
 * @author wangxiongjie
 * 
 */
public class KeywordGradeServiceImpl implements KeywordGradeService {

    private CproKeywordMgr cproKeywordMgr;

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.beidou.api.internal.indexgrade.exporter.KeywordGradeService#setKeywordGradeAndState (int, int,
     * java.util.List, int, int)
     */
    @Override
    public void setKeywordGradeAndState(int userId, int groupId, List<Long> keywordIds, int grade, int state) {

        cproKeywordMgr.updateKeywordGradeAndState(groupId, keywordIds,
                KeywordIndexGradeHelper.getValueByGradeAndState(grade, state));

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.beidou.api.internal.indexgrade.exporter.KeywordGradeService#setKeywordGrade (int, int,
     * java.util.List, int)
     */
    @Override
    public void setKeywordGrade(int userId, int groupId, List<Long> keywordIds, int grade) {

        cproKeywordMgr.updateKeywordGradeAndState(groupId, keywordIds, KeywordIndexGradeHelper.getValueByGrade(grade));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.beidou.api.internal.indexgrade.exporter.KeywordGradeService#setKeywordState(int, int,
     * java.util.List, int)
     */
    @Override
    public void setKeywordState(int userId, int groupId, List<Long> keywordIds, int state) {

        if (KeywordIndexGradeHelper.stateOnline(state)) {
            cproKeywordMgr.updateKeywordOnline(groupId, keywordIds);
        } else {
            cproKeywordMgr.updateKeywordOffline(groupId, keywordIds);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.beidou.api.internal.indexgrade.exporter.KeywordGradeService#getKeywordsGradeAndState(int,
     * java.util.List)
     */
    @Override
    public List<KeywordState> getKeywordsGradeAndState(int userId, List<Integer> groupIds) {

        List<CproKeyword> keywordList = cproKeywordMgr.findKeywordsByGroupIds(groupIds, userId);
        return KeywordIndexGradeHelper.transferKeywordList(keywordList);
    }

    public CproKeywordMgr getCproKeywordMgr() {
        return cproKeywordMgr;
    }

    public void setCproKeywordMgr(CproKeywordMgr cproKeywordMgr) {
        this.cproKeywordMgr = cproKeywordMgr;
    }

}
