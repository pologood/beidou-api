package com.baidu.beidou.api.external.util.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * ClassName: IndexMapper  <br>
 * Function: 添加/删除推广组设置记录映射到Map<GroupId, List<DATA>>中的DATA中的数据在原始请求中的下标缩印
 *
 * @author zhangxu
 * @date Aug 30, 2012
 */
public class IndexMapper implements Serializable{

	private static final long serialVersionUID = 1129243732419351L;
	
	private int len = 0;
	
	private Map<Integer, Integer> inGroup2ParamIndex = new HashMap<Integer, Integer>();

	public Map<Integer, Integer> getInGroup2ParamIndex() {
		return inGroup2ParamIndex;
	}

	public void setInGroup2ParamIndex(Map<Integer, Integer> inGroup2ParamIndex) {
		this.inGroup2ParamIndex = inGroup2ParamIndex;
	}
	
	public void setIndex(Integer paramIndex){
		inGroup2ParamIndex.put(len++, paramIndex);
	}
	
	public int getIndex(Integer inGroupIndex){
		return inGroup2ParamIndex.get(inGroupIndex);
	}
	
}
