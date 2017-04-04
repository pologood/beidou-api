package com.baidu.beidou.api.external.report.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class InterestAssistant {
	/**
	 * 一级兴趣或者兴趣组合ID
	 */
	private int selfInterestId;
	
	/**
	 * 一级兴趣或者兴趣组合对象
	 */
	private InterestStatViewItem selfInterest;
	
	/**
	 * 如果是一级兴趣，此为其下的二级兴趣列表
	 */
	private List<InterestStatViewItem> childInterests = null;
	
	public InterestAssistant(int selfInterestId) {
		this.selfInterestId = selfInterestId;
	}

	public void addSecondInterest(InterestStatViewItem second) {
		if (childInterests == null) {
			childInterests = new ArrayList<InterestStatViewItem>();
		}
		if (second != null && second.getInterestId() != 0) {
			childInterests.add(second);
		}
	}

	public InterestStatViewItem getSecondInterest(int secondInterestId) {
		if (childInterests == null) {
			return null;
		}
		return childInterests.get(secondInterestId);
	}

	/**
	 * 获得全部兴趣VO，一节兴趣点和兴趣组合放在List的第一位置
	 * 
	 * @return
	 */
	public List<InterestStatViewItem> getInterestViewItemList() {
		int b = childInterests == null ? 0 : childInterests.size();
		List<InterestStatViewItem> result = new ArrayList<InterestStatViewItem>(1 + b);
		result.add(selfInterest);
		if (!CollectionUtils.isEmpty(childInterests)) {
			if (b != 0) {
				result.addAll(childInterests);
			}
		}
		return result;
	}

	/**
	 * 计算统计数据和扩展数据，有子节点的计算统计数据和，没有子节点的即本身数据
	 */
	public void ensureStatFields() {
		if (childInterests != null) {
			for (InterestStatViewItem second : childInterests) {
				second.generateExtentionFields();
				selfInterest.setSrchs(second.getSrchs()	+ selfInterest.getSrchs());
				selfInterest.setCost(second.getCost() + selfInterest.getCost());
				selfInterest.setClks(second.getClks() + selfInterest.getClks());

				selfInterest.setArrivalCnt(second.getArrivalCnt() + selfInterest.getArrivalCnt());
				selfInterest.setHopCnt(second.getHopCnt() + selfInterest.getHopCnt());
				selfInterest.setResTime(second.getResTime() + selfInterest.getResTime());
				
				selfInterest.setHolmesClks(second.getHolmesClks() + selfInterest.getHolmesClks());
				selfInterest.setEffectArrCnt(second.getEffectArrCnt() + selfInterest.getEffectArrCnt());
			}
		}
		selfInterest.generateExtentionFields();
	}

	/** ******************getters and setters********************** */
	public InterestStatViewItem getSelfInterest() {
		return selfInterest;
	}

	public void setSelfInterest(InterestStatViewItem firstInterest) {
		this.selfInterest = firstInterest;
	}
	
	public int getSelfInterestId() {
		return selfInterestId;
	}

	public void setSelfInterestId(int selfInterestId) {
		this.selfInterestId = selfInterestId;
	}
}
