package com.baidu.beidou.api.external.cproplan.facade;

import java.util.List;

import com.baidu.beidou.api.external.cproplan.vo.CampaignType;
import com.baidu.beidou.api.external.util.vo.ApiError;
import com.baidu.beidou.api.external.util.vo.ApiResult;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.beidou.tool.vo.OptContent;

/**
 * ClassName: PlanFacade
 * Function: 推广计划层级添加和修改接口
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-10
 */
public interface PlanFacade {

	/**
	 * 修改推广计划
	 * 
	 * @param user
	 * @param plan
	 * @param plan的ognl表达式
	 * @return 成功返回传入的plan,失败返回null
	 */
	public ApiError updatePlan(final DataPrivilege user,
			final CampaignType plan, int index, List<OptContent> optContents);

	/**
	 * 
	 * @param user
	 * @param plan
	 *            [in/out] 如果planid<=0，表示新增计划，否则，只需要新增group
	 * @param group
	 *            [in/out] 如果groupid<=0，表示新增组，否则，只需要新增ad
	 * @param ad
	 *            [in/out]
	 * @param planPosition
	 *            plan的ognl表达式，<br>
	 *            group和ads的ognl表达式由于group，ad的函数没有地方可以设置，<br>
	 *            已经硬编码进函数为：PositionConstant.PARAM+AdGroupConstant.GROUP, <br>
	 *            PositionConstant.PARAM+AdConstant.ADS+"["+adIndex+"]"
	 * @return
	 */
	public ApiResult<CampaignType> addPlan(ApiResult<CampaignType> result,
			DataPrivilege user, CampaignType plan, int index, List<OptContent> optContents);

	/**
	 * 验证推广计划：<br>
	 * 1. 推广计划名称：不可为空，长度("stringgbklength")为1-20，<br>
	 * 2. 推广计划名称重复 <br>
	 * 3. 预算：[1-10000]; <br>
	 * 4. 推广计划个数未达到上限：100 <br>
	 * 5. 非删除的推广计划个数未达到上限：30 <br>
	 * 6. 推广计划，推广组不可为空，推广单元个数不能超过20个。 <br>
	 * 7. 推广计划状态为生效或者搁置。 <br>
	 * 11. 开始时间>=今日 <br>
	 * 12. 结束时间>=开始时间 <br>
	 * 
	 * @param userid
	 * @param plan
	 * @param positionPre
	 * @return
	 */
	public ApiError validateAddPlan(int userid, CampaignType plan, int index);

}