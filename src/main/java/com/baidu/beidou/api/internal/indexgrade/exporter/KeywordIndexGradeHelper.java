/**
 * Copyright (C) 2015年10月27日 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.beidou.api.internal.indexgrade.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.baidu.beidou.api.internal.indexgrade.vo.KeywordState;
import com.baidu.beidou.cprogroup.bo.CproKeyword;
import com.baidu.unbiz.atom.util.CollectionUtils;

/**
 * 关键词索引分级的状态位处理工具
 * 
 * @author wangxiongjie
 * 
 */
public class KeywordIndexGradeHelper {

    /**
     * 根据grade和state(上下线状态)得到合并的state值
     * 
     * @param grade
     * @param state
     * @return
     */
    public static int getValueByGradeAndState(int grade, int state) {
        return (grade << 1) + state;
    }

    /**
     * 根据grade值，得到合并的state值，上下线状态根据默认的阈值直接得到
     * 
     * @param grade
     * @return
     */
    public static int getValueByGrade(int grade) {
        if (grade > ONLINETHRESHOLD) {
            return getValueByGradeAndState(grade, STATE_OFFLINE);
        }
        return getValueByGradeAndState(grade, STATE_ONLINE);
    }

    /**
     * 根据grade值判断是否应该上线
     * 
     * @param grade
     * @return
     */
    public static boolean gradeOnline(int grade) {
        if (grade > ONLINETHRESHOLD) {
            return false;
        }
        return true;
    }

    /**
     * 根据state值判断是否应该上线
     * 
     * @param state
     * @return
     */
    public static boolean stateOnline(int state) {
        if (state == STATE_ONLINE) {
            return true;
        }
        return false;
    }

    public static List<KeywordState> transferKeywordList(List<CproKeyword> keywordList) {
        if (CollectionUtils.isEmpty(keywordList)) {
            return Collections.emptyList();
        }
        List<KeywordState> res = new ArrayList<KeywordState>(keywordList.size());
        for (CproKeyword keyword : keywordList) {
            res.add(new KeywordState(keyword.getId(), keyword.getState()));
        }
        return res;
    }

    private static final int ONLINETHRESHOLD = 0; // 上线阈值，小于或者等于这个值，才有资格能够上线；

    private static final int STATE_ONLINE = 0;
    private static final int STATE_OFFLINE = 1;
}
