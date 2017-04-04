package com.baidu.beidou.api.internal.user.exporter;


public interface AccountService {

	/**
	 * 提供给财务中心使用，当beidou账户余额由零调整为非零时，调整balancestat字段为1，上线广告传输。
	 * 当财务中心金额转账到零时，将调整balancestat字段为0，下线广告传输。
	 * 日志中记录下操作时间，以备查询使用
	 * @param userId 用户ID，整型
	 * @param balanceStat 状态值，1为有钱，上线；0没有钱，下线
	 * @return 结果状态码：0 执行成功；-2 参数非法;-100 找不到对应的用户；-1 系统报错，需重试
	 * 下午03:27:44
	 */
	public int modUserBalanceStat(Integer userId, Integer balanceStat);
}