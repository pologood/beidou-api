package com.baidu.beidou.api.external.kr.vo;

import java.util.List;
import java.util.Map;

/**
 * 此类用于进行参数收集，收集用户选择的行业
 * firstRegions收集一级行业ID
 * secondRegionMap的Key为一级地域ID，value为二级行业ID列表
 * @author hejinggen
 *
 */
public class TradeSelectionVo {

	private List<Integer> firstTrades;
	private Map<Integer,List<Integer>> secondTradeMap;
	/**
	 * @return the firstTrades
	 */
	public List<Integer> getFirstTrades() {
		return firstTrades;
	}
	/**
	 * @param firstTrades the firstTrades to set
	 */
	public void setFirstTrades(List<Integer> firstTrades) {
		this.firstTrades = firstTrades;
	}
	/**
	 * @return the secondTradeMap
	 */
	public Map<Integer, List<Integer>> getSecondTradeMap() {
		return secondTradeMap;
	}
	/**
	 * @param secondTradeMap the secondTradeMap to set
	 */
	public void setSecondTradeMap(Map<Integer, List<Integer>> secondTradeMap) {
		this.secondTradeMap = secondTradeMap;
	}	
	
}

