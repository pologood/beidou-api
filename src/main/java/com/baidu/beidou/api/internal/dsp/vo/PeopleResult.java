package com.baidu.beidou.api.internal.dsp.vo;

import java.util.List;

public class PeopleResult extends DspPeopleResult{
	List<PeopleType> peopleList;

	public List<PeopleType> getPeopleList() {
		return peopleList;
	}

	public void setPeopleList(List<PeopleType> peopleList) {
		this.peopleList = peopleList;
	}
}
