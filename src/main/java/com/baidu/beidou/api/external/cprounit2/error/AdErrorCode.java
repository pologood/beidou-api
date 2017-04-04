package com.baidu.beidou.api.external.cprounit2.error;

/**
 * ClassName: AdErrorCode
 * Function: unit层级错误代码
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-15
 */
public enum AdErrorCode {
	NO_UNIT(3000, "The creative does not exist"), // 创意不存在
	TOOMANY_NUM(3001, "The batch size of the creatives can not be larger than 100"), // 批量数量过多，最多100条
	TOOMANY_TOTAL_NUM(3002, "The number of the creatives in the same group exceeds the limit"), // 推广组下创意个数超过限制
	WRONG_USER(3003, "The creative does not belong to the user"), // 创意不属于该用户
	NO_GROUP(3004, "The group does not exist"), // 推广组不存在
	NOT_BINDING(3005, "The destination URL is not consistent with the registered website"), // 点击链接与网站域名不一致
	WRONG_TYPE(3006, "Wrong type for the material"), // 物料类型错误
	AKA_ERROR(3007, "Failure of auto audit"), // 自动校验失败
	IMAGE_ERROR(3008, "Data error for the image"), // 图片数据错误
	SHOWURL_NOT_BINDING(3009, "The show URL is not consistent with the registered website"), // 显示链接与网站域名不一致
	ICON_SIZE_ERROR(3010, "The size of icon data is not 60*60"), // 上传Icon尺寸异常

	NO_TITLE(3011, "The creative title can not be null"), // 标题不可为空
	TITLE_TOOLONG(3012, "The creative title is overlength"), // 标题过长，请控制在26个字符或13个汉字以内
	TITLE_ERROR(3013, "The creative title includes invalid characters"), // 标题包含以下非法字符：“<”、“>”、“{”、“}”
	IMAGE_TITLE_ERROR(3014, "The length of image name exceed limit"), // 图片名称过长，最多26个字符或13个汉字
	
	NO_SHOWURL(3021, "The display URL can not be null"), // 显示链接不可为空
	SHOWURL_TOOLONG(3022, "The display URL is overlength"), // 显示链接过长，请控制在35个字符以内
	SHOWURL_ERROR(3023, "The display URL includes invalid characters"), // 显示链接包含以下非法字符：“,”、“<”、“>”、“{”、“}”、“\"”、“'”、多字节字符
	SHOWURL_FORMAT(3024, "The display URL can not include \"http://\""), // 显示链接格式错误[输入的url中包含了http://]
	SHOWURL_FULLWIDTH_ERROR(3025, "The display URL can not include fullwidth characters"), // 显示链接不能包含全角字符

	NO_TARGETURL(3031, "The creative destination URL can not be null"), // 点击链接不可为空
	TARGETURL_TOOLONG(3032, "The creative destination URL is overlength"), // 点击链接过长，请控制在1024个字符以内
	TARGETURL_ERROR(3033, "The creative destination URL includes invalid characters"), // 点击链接包含以下非法字符：“,”、“<”、“>”
	TARGETURL_FORMAT(3034, "The creative destination URL must include \"http://\""), // 点击链接格式错误[输入的url没有http://]

	DESCRIPTION_TOOSHORT(3041, "The creative description1 is too short"), // 所输入的描述过短，请输入不少于8个字符或4个汉字
	DESCRIPTION_TOOLONG(3042, "The creative description1 is overlength"), // 所输入的描述过长，请控制在36个字符或18个汉字以内
	DESCRIPTION_ERROR(3043, "The creative description1 includes invalid characters"), // 描述中包含以下非法字符：“<”、“>”、“{”、“}”
	DESCRIPTION_NONE(3044, "The creative description1 can not be null"), // 描述1不可为空

	DESCRIPTION2_TOOSHORT(3051, "The creative description2 is too short"), // 所输入的描述过短，请输入不少于8个字符或4个汉字
	DESCRIPTION2_TOOLONG(3052, "The creative description2 is overlength"), // 所输入的描述过长，请控制在36个字符或18个汉字以内
	DESCRIPTION2_ERROR(3053, "The creative description2 includes invalid characters"), // 描述中包含以下非法字符：“<”、“>”、“{”、“}”
	DESCRIPTION2_NONE(3054, "The creative description2 can not be null"), // 描述2不可为空

	FILESRC_TOOBIG(3061, "The upload file size must be less than 55k"), // 文件大小不能超过55k
	FILESRC_WRONGSIZE(3062, "The specified size is not consistent with the actual size"), // 尺寸与物料实际尺寸不一致
	FILESRC_WRONGTYPE_PIC(3063, "Image formats only support jpg, gif"), // 文件类型错误，图片仅支持jpg、gif格式
	FILESRC_COLOR(3064, "Invalid color space for the image"), // 图片颜色空间错误
	FILESRC_SIZE(3065, "The material size is undesirable"), // 物料尺寸不符合要求
	FILESRC_WRONGTYPE_FLASH(3066, "Flash formats only supprot swf"), // 文件类型错误，flash仅支持swf格式

	FILESRC_WRONGTYPE_PIC_FILM(3067, "The Pre-loading only supports format jpg, swf"), // 文件类型错误，贴片创意仅支持gif、swf格式
	FILESRC_WRONGTYPE_GROUP(3068, "The Floating and the Pre-loading only supports images and Flash"), // 悬浮,贴片创意仅支持多媒体创意

	IMAGE_DATA_NONE(3071, "The image data can not be null"), // 物料内容不可为空
	IMAGE_WIDTH_NONE(3072, "The image width can not be null"), // 物料宽度不可为空
	IMAGE_HEIGHT_NONE(3073, "The image height can not be null"), // 物料高度不可为空

	URLSC(3110, "The URL includes special characters"), // URL中含有特殊字符
	URLEND(3111, "The URL includes invalid suffix"), // URL中含有非法后缀
	URLHEAD(3112, "The URL includes invalid prefix"), // URL中含有非法前缀

	TITI(3120, "The creative title includes invalid words or text"), // 标题触犯黑名单
	TITM(3121, "The creative title includes invalid trademarks"), // 标题触犯注册商标
	TITC(3122, "The creative title includes invalid competitive products"), // 标题触犯侵权词汇

	CORI(3130, "The creative title and descriptions include invalid words or text"), // 标题+描述触犯黑名单规则
	CORM(3131, "The creative title and descriptions include invalid trademarks"), // 标题+描述触犯注册商标
	CORC(3132, "The creative title and descriptions include competitive products"), // 标题+描述触犯侵权词汇

	DESC1I(3140, "The description1 includes invalid words or text"), // 描述1触犯黑名单规则
	DESC1M(3141, "The description1 includes invalid trademarks"), // 描述1触犯注册商标规则
	DESC1C(3142, "The description1 includes invalid competitive products"), // 描述1触犯侵权词汇
	DESC2I(3145, "The description2 includes invalid words or text"), // 描述2触犯黑名单规则
	DESC2M(3146, "The description2 includes invalid trademarks"), // 描述2触犯注册商标规则
	DESC2C(3147, "The description2 includes invalid competitive products"), // 描述2触犯侵权词汇

	TOOMANY_QUERYNUM(3201, "The search queries can not be more than 100"), // 查询条数过多，最多一百条
	TOOMANY_DELNUM(3202, "The batch size of delete operation can not exceed 100"), // 批量删除条数过多，最多一百条
	DEL_ERROR(3203, "It is not allowed to delete the creative when there is only one left"), // 当前推广组下只有一条创意，不能删除
	TOOMANY_CHGSTATUSNUM(3204, "The batch size of modify operation can not exceed 100"), // 批量修改状态过多，最多一百条
	RESUME_STATUS_ERROR(3205, "The status of the creative can not be modified as valid"), // 该创意状态不能改为生效
	STOP_STATUS_ERROR(3206, "The status of the creative can not be modified as pended"), // 该创意状态不能改为搁置
	MOD_STATUS_ERROR(3207, "The creative under auditing can not be modified"), // 处于审核状态中的创意不能进行修改
	MOD_ERROR(3208, "Exception occurs when modifying the creative"), // 修改创意时出现异常
	ADD_ICON_ERROR(3209, "Exception occurs when adding the icon"), // 新增icon时出现异常
	ADD_AD_ERROR(3210, "Exception occurs when adding the ads"), // 新增创意时出现异常
	REPLACE_AD_ERROR(3250, "Exception occurs when replacing the ads"), // 替换创意时出现异常
	TOOMANY_REPLACE_AD_ERROR(3251, "The batch size of replace operation can not exceed 100"), // 批量替换条数过多，最多一百条
	REPLACE_GROUP_TYPE_ERROR(3252, "The grouptypes of these two units are different when replacing the ads"), // 替换的创意要与目标推广组类型匹配
	REPLACE_AD_STATE_ERROR(3253, "The state of unit is not deleted when replacing the ads"), // 替换创意时创意不能为删除状态
	REPLACE_SRC_AD_PROMOTION_TYPE_ERROR(3254, "Can't find the source unit id's promotion type."), // 替换创意时“源创意”找不到“推广类型”
	REPLACE_TARGET_AD_PROMOTION_TYPE_ERROR(3255, "Can't find the target units id's promotion type."), // 替换创意时“被替换创意”找不到“推广类型”
	REPLACE_AD_PROMOTION_TYPE_ERROR(3256, "All of ads's promotion type must be the same."), // 替换创意时“源创意”和“被替换创意”的“推广类型”要一致
	COPY_AD_ERROR(3260, "Exception occurs when copying the ads"), // 复制创意时出现异常
	TOOMANY_COPY_AD_ERROR(3261, "The batch size of copy operation for ads can not exceed 100"), // 批量复制的创意条数过多，最多一百条
	TOOMANY_COPY_GROUP_ERROR(3262, "The batch size of copy operation for groups can not exceed 100"), // 批量复制的推广组条数过多，最多一百条
	COPY_GROUP_TYPE_ERROR(3263, "The grouptypes of unit and the targeted group are different when copying the ads"), // 复制的创意要与目标推广组类型匹配
	COPY_AD_STATE_ERROR(3264, "The state of unit is not deleted when copying the ads"), // 替换创意时创意不能为删除状态
	COPY_GROUP_DUP_ERROR(3265, "The targeted groups have duplicate"), // 目标推广组有重复
	COPY_GROUP_PROMOTION_TYPE_ERROR(3266, "Can't find the targeted groups's promotion type."), // 无法获取当前目标推广组的“推广类型”
	COPY_AD_PROMOTION_TYPE_ERROR(3267, "Can't find the ads's promotion type."), // 无法获取当前待拷贝的创意的“推广类型”
	COPY_GROUP_AD_PROMOTION_TYPE_ERROR(3268, "All of ads's promotion type and targeted groups's promotion type must be the same."), // 待拷贝的创意的“推广类型”和目标推广组的“推广类型”应该完全 一致
	APP_SHOWURL_TARGETURL_BOTH_NOT_NULL(3269, "App showurl and targeturl should be show in the same time"), // 移动showurl与targeturl必须同时出现
	
	APP_TARGETURL_NOT_BINDING(3270, "The app destination URL is not consistent with the registered website"), // 无线点击链接与网站域名不一致
	APP_SHOWURL_NOT_BINDING(3271, "The app show URL is not consistent with the registered website"), // 无线显示链接与网站域名不一致
	
	SMARTIDEA_GROUP_CANNOT_ADD_NORMAL_CREATIVE(3272, "The group of the creatives you are submitting is smart idea type, which does not support"), // 智能创意推广组不允许操作普通创意
	;

	private int value = 0;
	private String message = null;

	private AdErrorCode(int value, String message) {
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
