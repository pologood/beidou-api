package com.baidu.beidou.api.internal.audit.util;

import com.baidu.beidou.api.internal.audit.constant.QueryConstant;

/**
 * 标注工具类
 * 
 * @author work
 * @version 1.0.0
 */
public class UnitTagUtil {

    /**
     * 根据所有tagValue生成tagMask的值
     * 
     * @param confidenceLevel 置信度
     * @param beautyLevel 美观度
     * @param cheatLevel 欺诈度
     * @param vulgarLevel 低俗度
     * @param dangerLevel 高危度
     * @return TagMask
     */
    public static Integer getTagMask(Integer confidenceLevel, Integer beautyLevel, Integer cheatLevel,
            Integer vulgarLevel, Integer dangerLevel) {
		String tagMaskString = "";
		String strTemp = "0000";
		String strTemp2 = strTemp.substring(0, QueryConstant.eachTagLength);
		
		String tagValueString = Integer.toBinaryString(dangerLevel);
		tagValueString = strTemp2.substring(0, strTemp2.length() - tagValueString.length()) + tagValueString;
		tagMaskString += tagValueString;
		
		tagValueString = Integer.toBinaryString(vulgarLevel);
		tagValueString = strTemp2.substring(0, strTemp2.length() - tagValueString.length()) + tagValueString;
		tagMaskString += tagValueString;
		
		tagValueString = Integer.toBinaryString(cheatLevel);
		tagValueString = strTemp2.substring(0, strTemp2.length() - tagValueString.length()) + tagValueString;
		tagMaskString += tagValueString;
		
		tagValueString = Integer.toBinaryString(beautyLevel);
		tagValueString = strTemp2.substring(0, strTemp2.length() - tagValueString.length()) + tagValueString;
		tagMaskString += tagValueString;
		
		tagValueString = Integer.toBinaryString(confidenceLevel);
		tagValueString = strTemp2.substring(0, strTemp2.length() - tagValueString.length()) + tagValueString;
		tagMaskString += tagValueString;
		
		return new Integer(Integer.parseInt(tagMaskString, 2));
    }
	
    /**
     * 根据tagType以及新值tagValue更新tag的值
     * @param oldTagMask 旧的tagMask值  
     * @param tagType   tag类型
     * @param tagValue  tag值
     * @return newTagMask
     */
	public static Integer getNewTagMask(Integer oldTagMask, Integer tagType, Integer tagValue) {
        String strTemp = "00000000000000000000000000000000";
        
        // tag的总长度为QueryConstant.eachTagLength * QueryConstant.tagNum位二进制数
        String oldTagString = Integer.toBinaryString(oldTagMask);        
        String strTemp1 = strTemp.substring(0, QueryConstant.eachTagLength * QueryConstant.tagNum);
        oldTagString = strTemp1.substring(0, strTemp1.length() - oldTagString.length()) + oldTagString;
        
        // 每一个tagValue的长度为QueryConstant.eachTagLength位二进制数
        String tagValueString = Integer.toBinaryString(tagValue);
        String strTemp2 = strTemp.substring(0, QueryConstant.eachTagLength);
        tagValueString = strTemp2.substring(0, strTemp2.length() - tagValueString.length()) + tagValueString;
        
        // 转为char数组以便替换
        char[] oldTagCharArray = oldTagString.toCharArray();
        char[] tagValueCharArray = tagValueString.toCharArray();
        
        for (int i = (QueryConstant.tagNum - tagType) * QueryConstant.eachTagLength, j = 0;
                i < (QueryConstant.tagNum - tagType)
                * QueryConstant.eachTagLength + QueryConstant.eachTagLength; i++, j++) {
            oldTagCharArray[i] = tagValueCharArray[j];
        }
        
        int newTag = Integer.parseInt(String.valueOf(oldTagCharArray), 2);
        
        return newTag;
    }
    
    /**
     * 根据tagType获取对应的tagValue
     * @param tag  tag值
     * @param tagType  tag类型
     * @return tagValue
     */
    public static Integer getTagValueByType(Integer tag, Integer tagType) {
        tag = tag >> QueryConstant.eachTagLength * (tagType - 1);
        int tagValue = 0;
        for (int i = 0; i < QueryConstant.eachTagLength; i++) {
            tagValue = (int) ((1 & tag) * Math.pow(2, i) + tagValue);
            tag = tag >> 1;
        }
        return tagValue;
    }
    
    /**
     * 解析tag获取创意的详细tagValue
     * @param tag tag值
     * @return UnitTag  UnitTag类
     */
    public static UnitTag getUnitTag(Integer tag) {
        UnitTag unitTag = new UnitTag();
        unitTag.setConfidenceLevel(getTagValueByType(tag, QueryConstant.QueryModifyTag.confidenceLevel));
        unitTag.setBeautyLevel(getTagValueByType(tag, QueryConstant.QueryModifyTag.beautyLevel));        
        unitTag.setCheatLevel(getTagValueByType(tag, QueryConstant.QueryModifyTag.cheatLevel));
        unitTag.setVulgarLevel(getTagValueByType(tag, QueryConstant.QueryModifyTag.vulgarLevel));
        unitTag.setDangerLevel(getTagValueByType(tag, QueryConstant.QueryModifyTag.dangerLevel));
        return unitTag;
    }
}
