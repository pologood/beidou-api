package com.baidu.beidou.api.external.cproplan2.error;

/**
 * ClassName: PlanErrorCode
 * Function: 推广计划层级错误代码
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-10
 */
public enum PlanErrorCode {
	NO_PLAN(1000, "The campaign does not exist"), // 推广计划不存在
	TOOMANY_NUM(1001, "The batch size exceeds the limit"), // 批量条数过多
	WRONG_USER(1002, "The campaign does not belong to the user"), // 推广计划不属于该用户
	LENGTH_PLAN_NAME(1003, "The length of the campaign name exceeds the limit"), // 推广计划名称长度超过限制
	REPEAT_PLAN_NAME(1004, "The campaign name can not be repeated"), // 推广计划名称不能重复
	MAX_PLAN_BUDGET(1005, "The campaign budget exceeds the limit"), // 推广计划预算超出限制
	WRONG_PLAN_STATUS(1006, "Invalid status for the campaign"), // 推广计划状态非法
	MAX_PLAN_EFFECTIVE_NUM(1007, "The number of non-deleted campaigns exceeds the limit"), // 非删除的推广计划总数超过限制
	WRONG_PLAN_STARTDATE_BEGIN(1008, "The campaign has already began"), // 推广计划已开始
	WRONG_PLAN_STARTDATE_BEFORE(1009, "The startTime can not be earlier than today"), // 推广计划开始时间不能在今日之前
	WRONG_PLAN_DATE_FORMAT(1010, "Invalid time format"), // 时间格式不合法
	WRONG_PLAN_ENDDATE_END(1011, "The campaign has already ended"), // 推广计划已结束
	WRONG_PLAN_ENDDATE_BEFORE_START(1012, "The startTime must be earlier than endTime"), // 推广计划结束时间不能在开始时间之前
	WRONG_PLAN_ENDDATE_BEFORE(1013, "The endTime can not be earlier than today"), // 推广计划结束时间不能在今日之前
	REASONABLE_PLAN_BUDGET(1014, "The campaign budget should not be less than the maximum bid of all its groups"), // 推广计划预算不得小于其下所有推广组的最高出价
	MAX_PLAN_NUM(1015, "The number of the campaigns exceeds the limit"), // 推广计划总数超过限制
	PLAN_NAME_EMPTY(1016, "The campaign name is null"), // 推广计划名称不能为空
	PLAN_NAME_SPECIAL(1017, "The campaign name only includes the support of English letters, Arabic numerals, Chinese characters, hyphens and  underscores"), // 推广计划名称仅支持字母、数字、汉字、中划线及下划线
	INVALID_SCHEDULE(1018, "Invalid format for the campaign schedule"), // 投放日程格式不合法
	TOOMANY_QUERY_PLAN(1019, "The number of query campaigns exceeds the limit"), // 查询的推广计划数量过大
	WRONG_MODSTATE_DELETE_NORMAL(1020, "Can not change campaign's state from delete to normal"), // 不能暂停处于删除状态的推广计划
	PLAN_CREATE_FAILED(1021, "Failed to add this campaign"), // 创建推广计划失败
	INVALID_TYPE(1022, "Invalid campaign type"), // 推广计划类型错误
	NON_WHITELIST_WIRELESS_USER_CANNOT_SET_WIRELESS_ONLY(1023, "The user has not privilege to set campaign type to wireless only"), // 推广计划类型不允许被设置为“仅无线”
	WIRELESSRATIO_CAN_ONLY_DEFAULT(1024, "The campaign wireless ratio can only be set to default"), // 推广计划移动设备出价比例只允许设置为默认值
	INVALID_WIRELESSRATIO(1025, "Invalid wireless ratio"), // 推广计划移动设备出价比例值非法
	INVALID_WIRELESS_DEVICE_OS_PARAM(1026, "Invalid wireless device or os parameter, please specify device and os parameter correctly"), // 推广计划移动设备操作系统未设置任何值
	INVALID_WIRELESS_DEVICE_MAPPING(1027, "Invalid wireless device or os mapping"), // 推广计划移动设备操作系统值映射组合错误
	INVALID_WIRELESS_DEVICE(1028, "Invalid wireless device"), // 推广计划移动设备错误
	INVALID_WIRELESS_OS(1029, "Invalid wireless os"), // 推广计划移动设备操作系统错误
	WIRELESS_CANNOT_SET_DEVICE_OS(1030, "The user has not privilege to set campaign device and os"), // 推广计划移动设备操作系统设置无权限
	ALL_TYPE_CAMPAIGN_CANNOT_SET_DEVICE_OS(1031, "Device and os settings cannot be configured due to campaign type is all"), // 因推广计划类型为所有功能，移动设备操作系统无法设置
	PLAN_UPDATE_OUTOFPROTECTION(1031, "Failed to update this campaign,it has been deleted and out of proection period") // 计划已经被删除而且超过了恢复期
	;

	private int value = 0;
	private String message = null;

	private PlanErrorCode(int value, String message) {
		this.value = value;
		this.message = message;
	}

	public int getValue() {
		return value;
	}

	public String getMessage() {
		return message;
	}

}
