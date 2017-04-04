package com.baidu.beidou.api.external.kr.vo;

import java.util.List;
import java.util.Map;

/**
 * 此类用于进行参数收集，收集用户选择的地域
 * firstRegions收集一级地域ID
 * secondRegionMap的Key为一级地域ID，value为二级地域ID列表
 * @author hejinggen
 *
 */
public class RegionSelectionVo {

	private List<Integer> firstRegions;
	private Map<Integer,List<Integer>> secondRegionMap;
	/**
	 * @return the firstRegions
	 */
	public List<Integer> getFirstRegions() {
		return firstRegions;
	}
	/**
	 * @param firstRegions the firstRegions to set
	 */
	public void setFirstRegions(List<Integer> firstRegions) {
		this.firstRegions = firstRegions;
	}
	/**
	 * @return the secondRegionMap
	 */
	public Map<Integer, List<Integer>> getSecondRegionMap() {
		return secondRegionMap;
	}
	/**
	 * @param secondRegionMap the secondRegionMap to set
	 */
	public void setSecondRegionMap(Map<Integer, List<Integer>> secondRegionMap) {
		this.secondRegionMap = secondRegionMap;
	}
	
}
