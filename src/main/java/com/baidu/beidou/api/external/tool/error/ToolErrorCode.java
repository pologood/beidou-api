package com.baidu.beidou.api.external.tool.error;

/**
 * 
 * ClassName: ToolErrorCode  <br>
 * Function: 工具服务错误代码
 *
 * @author zhangxu
 * @date Aug 21, 2012
 */
public enum ToolErrorCode {
	
	NO_UNIT(500, "The creative does not exist"), // 创意不存在
	PARAM_EMPTY(501, "The id list is empty"), // 参数为空
	AD_TOOMANY_NUM(502, "The batch size of the creatives is too much"), // 批量数量过多
	WRONG_USER(503, "The creative does not belong to the user"), // 创意不属于该用户
	SITE_NOT_FOUND(504, "The website does not exsit"), //网站不存在
	SITE_TOOMANY_NUM(505, "The batch size of the sites is too much"), // 批量数量过多
	FCUNIT_TOOMANY_NUM(506, "The batch size of the unit id is too much"), // 批量数量过多
	IDS_TOOMANY_NUM(507, "The batch size of the id is too much"), // 批量数量过多
	FAIL_TO_GET_FC_KEYWORDS(508, "Failed to get fc keywords"), // 获取凤巢关键词失败
	GPID_NOT_EXIST(509, "Gpid not exit"), // 推广组-受众组合关联关系不存在
	;

	private int value = 0;
	private String message = null;

	private ToolErrorCode(int value, String message) {
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
