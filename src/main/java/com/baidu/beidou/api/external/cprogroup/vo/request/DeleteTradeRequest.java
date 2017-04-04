package com.baidu.beidou.api.external.cprogroup.vo.request;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.cprogroup.vo.GroupTradeType;
import com.baidu.beidou.api.external.util.constant.ApiConstant;
import com.baidu.beidou.api.external.util.request.ApiRequest;

/**
 * ClassName: DeleteTradeRequest
 * Function: 删除投放行业请求
 *
 * @author Baidu API Team
 * @date 2012-4-1
 */
public class DeleteTradeRequest implements Serializable, ApiRequest {
	
	private static final long serialVersionUID = -2480618360404113412L;
	
	private GroupTradeType[] trades;	// 投放行业设置数组
	
	public int getDataSize() {
		int result = ApiConstant.REQUEST_DEFAULT_DATA_SIZE;
		
		if (!ArrayUtils.isEmpty(trades)) {
			result = trades.length;
		}
		
		return result;
	}

	public GroupTradeType[] getTrades() {
		return trades;
	}

	public void setTrades(GroupTradeType[] trades) {
		this.trades = trades;
	}
}
