package com.baidu.beidou.api.external.kr.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.baidu.beidou.api.external.kr.vo.KrInfoVo;
import com.baidu.beidou.pack.bo.WordPackKeyword;

/**
 * 
 * @author Zhu Zhenxing
 * 
 */
public final class KrFilterHelper {
    private KrFilterHelper() {
    }

    /**
     * remove existing keywords from KrInfoVo list
     * 
     * @param words
     * @param keywords
     * @return
     */
    public static List<KrInfoVo> filter(List<KrInfoVo> words, List<WordPackKeyword> keywords) {
        if (null != keywords && !keywords.isEmpty()) {
            Map<String, KrInfoVo> voMap = new HashMap<String, KrInfoVo>();
            for (KrInfoVo w : words) {
                voMap.put(w.getKw(), w);
            }
            Map<String, String> km = new HashMap<String, String>();
            for (WordPackKeyword k : keywords) {
                km.put(k.getKeyword(), k.getKeyword());
            }
            List<KrInfoVo> rtnVos = new ArrayList<KrInfoVo>();
            Set<String> keySet = km.keySet();
            for (String k : keySet) {
                voMap.remove(k);
            }
            rtnVos.addAll(voMap.values());
            return rtnVos;
        }

        return words;
    }

    public static List<KrInfoVo> truncate(List<KrInfoVo> krInfos, int limit) {
        if (krInfos.size() > limit) {
            return krInfos.subList(0, limit);
        }
        return krInfos;
    }
}
