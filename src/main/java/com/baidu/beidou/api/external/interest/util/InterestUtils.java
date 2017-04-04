package com.baidu.beidou.api.external.interest.util;

import java.util.List;

import com.baidu.beidou.api.external.interest.vo.CustomInterestCollectionType;
import com.baidu.beidou.api.external.interest.vo.InterestType;
import com.baidu.beidou.cprogroup.vo.InterestVo;

/**
 * 
 * ClassName: InterestUtils  <br>
 * Function: API专用兴趣工具类
 *
 * @author zhangxu
 * @date Jun 4, 2012
 */
public class InterestUtils {

	public static InterestType[] parseInterestVo2Type(List<InterestVo> interestVoList){
		if(interestVoList.size() == 0){
			return new InterestType[0];
		}
		InterestType[] result = new InterestType[interestVoList.size()];
		int index = 0;
		for (InterestVo vo : interestVoList) {
			result[index++] = new InterestType(vo.getId(), vo.getName(), vo.getParentId());
		}
		return result;
	}
	
	public static CustomInterestCollectionType[] parseItPackToItCollection(List<List<InterestVo>> itPack){
		if(itPack.size() == 0){
			return new CustomInterestCollectionType[0];
		}
		CustomInterestCollectionType[] collectionType = new CustomInterestCollectionType[itPack.size()];  //expList.size()可以看做兴趣包的数量
		int index = 0;
		for (List<InterestVo> list : itPack) {
			InterestType[] typeArray = InterestUtils.parseInterestVo2Type(list);
			collectionType[index] = new CustomInterestCollectionType();
			collectionType[index].setInterests(typeArray);
			index++;
		}
		return collectionType;
	}
	
}
