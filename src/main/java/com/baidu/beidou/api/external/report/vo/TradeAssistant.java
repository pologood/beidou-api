package com.baidu.beidou.api.external.report.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.unbiz.soma.module.reportmodule.report.vo.StatInfo;


public class TradeAssistant  {
	private int selfTradeId;
	
	private TradeStatViewItem selfTrade;
	private Map<Integer, TradeStatViewItem> childTrades = null;
	
	private boolean isFirstTrade;
	
	private boolean selfFiltered;
	
	public TradeAssistant(int selfTradeId, boolean isFirstTrade){
		this.selfTradeId = selfTradeId;
		this.isFirstTrade = isFirstTrade;
	}

	
	public StatInfo getSumData(){
		if(selfTrade != null){
			return selfTrade;
		}else{
			StatInfo info = new StatInfo();
			if(childTrades != null){
				for(TradeStatViewItem vo : childTrades.values()){
					info.setSrchs(vo.getSrchs() + info.getSrchs());
					info.setClks(vo.getClks() + info.getClks());
					info.setCost(vo.getCost() + info.getCost());
				}
			}
			return info;
		}
	}
	
	@Override
	public int hashCode(){
		return selfTradeId;
	}

	public void addSecondTrade(TradeStatViewItem second){
		if(childTrades == null){
			childTrades = new HashMap<Integer, TradeStatViewItem>();
		}
		if(second != null && second.getSecondTradeId() != 0){
			childTrades.put(second.getSecondTradeId(), second);
		}
	}
	
	public TradeStatViewItem getSecondTrade(int secondTradeId){
		if(childTrades == null){
			return null;
		}
		return childTrades.get(secondTradeId);
	}

	public TradeStatViewItem getSelfTrade() {
		return selfTrade;
	}

	public void setSelfTrade(TradeStatViewItem firstTrade) {
		this.selfTrade = firstTrade;
	}
	
	public List<TradeStatViewItem> getTradeViewItem(){
		if(this.isAllFiltered()){
			return new ArrayList<TradeStatViewItem>(0);
		}
		int b = childTrades == null ? 0 : childTrades.size();
		List<TradeStatViewItem> result = new ArrayList<TradeStatViewItem>(1 + b);
		result.add(selfTrade);		
		if(b != 0){
			result.addAll(childTrades.values());
		}
		return result;
	}
	
	/**
	 * 获取有统计数据的TradeStatViewItem
	 * @return
	 */
	public List<TradeStatViewItem> getTradeViewItemWithData(){
		if(this.isAllFiltered()){
			return null;
		}
		List<TradeStatViewItem> result = new ArrayList<TradeStatViewItem>();
		if(selfTrade.isWithData()){
			result.add(selfTrade);		
		}
		if(childTrades!=null){
			for(Integer tradeId :childTrades.keySet()){
				TradeStatViewItem child = childTrades.get(tradeId);
				if(child.isWithData()){
					result.add(child);		
				}
			}
		}
		return result;
	}
	
//	public void ensureStatFields(){
//		if(selfTrade != null){
//			if(selfTrade.isHasFillStatInfo()){
//				selfTrade.generateExtentionFields();
//			}else{
//				selfTrade.fillZeroStat();
//			}
//		}
//		if(childTrades != null){
//			for(TradeStatViewItem second : childTrades.values()){
//				if(second.isHasFillStatInfo()){
//					second.generateExtentionFields();
//				}else{
//					second.fillZeroStat();
//				}
//			}
//		}
//	}

	public boolean isAllFiltered() {
		return this.selfFiltered;
	}

	public boolean isFirstTrade() {
		return isFirstTrade;
	}

	public int getSelfTradeId() {
		return selfTradeId;
	}
}