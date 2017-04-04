package com.baidu.beidou.api.external.cprogroup.error;

import java.util.List;

import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.ErrorParam;
import com.baidu.beidou.api.external.util.vo.ApiErrorPosition;
import com.baidu.beidou.api.external.util.vo.ApiResult;

/**
 * ClassName: GroupErrorCode Function: 推广组相关errorcode
 * 
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-22
 */
public enum GroupErrorCode {

	PROPETRY_INVALID(2001, "Invalid property"), // 属性验证不通过
	MAX_ALL_NUMBER(2002, "The number of groups in the same campaign exceeds the limit"), // 同一推广计划下的推广组个数超过限制
	MAX_EFFECTIVE_NUMBER(2003, "The number of non-deleted groups in the same campaign exceeds the limit"), // 同一推广计划下的非删除推广组个数超过限制
	COMMIT_FAIL(2004, "Failed to submit"), // 提交错误
	PARAMETER_ERROR(2005, "Invalid input parameters"), // 输入参数错误
	NAME_SPECIAL(2006, "The group name only includes the support of English letters, Arabic numerals, Chinese characters, hyphens and  underscores"), // 推广组名称仅支持字母、数字、汉字、中划线及下划线
	STATE_INVALID(2007, "Invalid status for the group"), // 推广组状态错误
	PRICE_INVALID(2008, "Invalid price for the group"), // 推广组价格错误
	NOT_FOUND(2009, "The group does not exist"), // 推广组不存在
	GROUP_STATE_ERROR(2010, "Invalid operating status for the group"), // 推广组操作状态不合法
	GROUP_CTREATION_ERROR(2011, "Failed to add the group"), // 推广组添加失败
	NAME_REPEAT(2012, "The group name in the same campaign can not be repeated"), // 同一推广计划下的推广组名称不能重复
	NAME_EMPTY(2013, "The group name is null"), // 推广组名称不能为空
	LENGTH_NAME_MAX(2014, "The length of the group name exceeds the limit"), // 推广组名称超过限制
	PRICE_NOT_REASONABLE(2015, "The group bid can not exceed the budget of campaign it belongs to"), // 推广组出价价格不得大于该推广组所属推广计划的每日预算值
	NO_FILM_PRIVELEGE(2016, "Have no authorization to submit groups with the Pre-loading"), // 没有提交贴片推广组的权限
	TYPE_CANT_CHANGE(2017, "The displaytype of the targeted group is invalid"), // 推广组展现类型无效
	NO_FILM_MOD_PRIVELEGE(2018, "Have no authorization to modify the group with the Pre-loading"), // 没有修改贴片推广组的权限
	KEYWORD_NOT_FOUND(2019, "The keyword does not exist"), // 主题词不存在
	TOOMANY_QUERY_GROUP(2020, "The number of query groups exceeds the limit"), // 查询的推广组数量过大
	TOOMANY_ADD_GROUP(2021, "The number of added groups exceeds the limit"), // 添加的推广组数量过大	
	GROUP_GENDERINFO_WRONG(2022, "The group exclude gender is wrong"), // 添加的推广组排除性别错误
	TOOMANY_MOD_GROUP(2023, "The number of modified groups exceeds the limit"), // 修改的推广组数量过大
	WORD_NOT_FOUND(2024, "The keyword does not exist"), // 主题词不存在，atom中不存在
	GROUP_UPDATE_ERROR(2025, "Failed to update the group"), // 推广组更新失败
	GROUP_TYPE_CAN_NOT_UPDATE(2026, "Group type can not be updated"), // 推广组类型不能更新
	GROUP_UPDATE_OUTOFPROTECTION(2027, "Failed to update this group,it has been deleted and out of proection period") // 不能被恢复,已经超出保护期
	;

	private int value = 0;
	private String message = null;

	private GroupErrorCode(int value, String message) {
		this.value = value;
		this.message = message;
	}

	public int getValue() {
		return value;
	}

	public String getMessage() {
		return message;
	}
	
    /**
     * 返回Error响应
     * 
     * @param result 响应对象
     * @param params 错误参数
     * @param content 错误信息
     * @return 包含错误信息的响应对象
     */
    public ApiResult<Object> getErrorResponse(ApiResult<Object> result, List<ErrorParam> params, String content) {
        ApiErrorPosition apiPosition = new ApiErrorPosition(PositionConstant.PARAM);
        for (ErrorParam param : params) {
            if (param.getPosition() != null) {
                apiPosition.addParam(param.getParamName(), param.getPosition().intValue());
            } else {
                apiPosition.addParam(param.getParamName());
            }
        }

        result = ApiResultBeanUtils.addApiError(result, value, message, apiPosition.getPosition(), content);
        return result;
    }
}
