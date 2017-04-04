package com.baidu.beidou.api.external.cprogroup.facade;

import java.util.List;

import com.baidu.beidou.api.external.util.vo.IndexMapper;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.tool.vo.OptContent;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * 
 * ClassName: GroupExcludePeopleFacade  <br>
 * Function: 由于历史原因，排除人群的代码在北斗web中，因此这里存在一个特殊的facade
 *
 * @author zhangxu
 * @date Sep 4, 2012
 */
public interface GroupExcludePeopleFacade {
	
	/**
	 * 保存推广组排除人群设置
	 * @param response
	 * @param groupId
	 * @param excludePids 排除人群id列表
	 * @param indexMapper excludePids在实际前端传递过来的数组里的下标索引，需要提前构造好
	 * @param userId
	 * @param opUser
	 * @param checkLimit 是否验证总数超过限制
	 * @param exitWhenValidationFail 遇到验证错误时是否退出
	 * @param isOverride 是否覆盖原配置信息
	 * @param contents 
	 */
	public void addOrSetExcludePeople(BaseResponse<PlaceHolderResult> response, 
			Integer groupId,
			List<Long> excludePids,
			IndexMapper indexMapper,
			int userId,
			int opUser,
			boolean checkLimit,
			boolean exitWhenValidationFail,
			boolean isOverride,
			List<OptContent> contents);
	
	/**
	 * 删除推广组排除人群设置
	 * @param response
	 * @param groupId
	 * @param excludePids 排除人群id列表
	 * @param indexMapper excludePids在实际前端传递过来的数组里的下标索引，需要提前构造好
	 * @param userId
	 * @param opUser
	 * @param contents 
	 */
	public void deleteExcludePeople(BaseResponse<PlaceHolderResult> response, 
			Integer groupId,
			List<Long> excludePids,
			IndexMapper indexMapper,
			int userId,
			int opUser,
			List<OptContent> contents);
}
