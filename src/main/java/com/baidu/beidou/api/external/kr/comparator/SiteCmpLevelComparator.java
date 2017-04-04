package com.baidu.beidou.api.external.kr.comparator;

import java.util.Comparator;

import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.api.external.kr.vo.CmpLevelVO;

/**
 * @author zhuqian
 *
 */
public class SiteCmpLevelComparator implements Comparator<CmpLevelVO> {

	/**
	 * 要求CmpLevelVO中的cmpLevel, rateCmp, scoreCmp字段均不为Null，
	 * 否则会出现 NullPointerException.
	 */
	public int compare(CmpLevelVO o1, CmpLevelVO o2) {

		if(o1 == null) return 1;
		if(o2 == null) return -1;
		
		
		if(! o1.getCmpLevel().equals(o2.getCmpLevel())){
			return o1.getCmpLevel().compareTo(o2.getCmpLevel());
		}
		
		
		if(o1.getCmpLevel().equals(CproGroupConstant.CMP_LEVEL_HEATED)){
			return o1.getRateCmp().compareTo(o2.getRateCmp());
		}else{
			return o1.getScoreCmp().compareTo(o2.getScoreCmp());
		}
		
	}

}
