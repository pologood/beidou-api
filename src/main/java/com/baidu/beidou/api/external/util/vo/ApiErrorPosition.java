package com.baidu.beidou.api.external.util.vo;

import com.baidu.beidou.api.external.util.constant.PositionConstant;

public class ApiErrorPosition {
	private StringBuffer sb = null;
	
	public ApiErrorPosition(String position) {
		sb = new StringBuffer(position);
	}
	
	public ApiErrorPosition addParam(String param) {
		if (param == null) {
			return this;
		}
		
		sb.append(PositionConstant.SPLIT)
			.append(param);
		return this;
	}
	
	public ApiErrorPosition addParam(String param, int pos) {
		if (param == null) {
			return this;
		}
		
		sb.append(PositionConstant.SPLIT)
			.append(param)
			.append("[")
			.append(pos)
			.append("]");
		return this;
	}
	
	public ApiErrorPosition addParam(String param, int pos, IndexMapper indexMapper) {
		if (param == null) {
			return this;
		}
		
		if (indexMapper != null) {
			int actualPos = indexMapper.getIndex(pos);
			pos = actualPos;
		}
		
		sb.append(PositionConstant.SPLIT)
			.append(param)
			.append("[")
			.append(pos)
			.append("]");
		return this;
	}
	
	public String getPosition() {
		return sb.toString();
	}
	
	public String combinePosition(ApiErrorPosition another) {
		return this.getPosition() + PositionConstant.SPLIT_COMBINE + another.getPosition();
	}
}
