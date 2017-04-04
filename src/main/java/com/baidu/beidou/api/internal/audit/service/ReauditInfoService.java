package com.baidu.beidou.api.internal.audit.service;

import java.util.List;

import com.baidu.beidou.api.internal.audit.vo.ReauditUserInfo;
import com.baidu.beidou.api.internal.audit.vo.request.QueryBase;

public interface ReauditInfoService {
	
	public int findReauditUserList(QueryBase query, List<ReauditUserInfo> result);

}
