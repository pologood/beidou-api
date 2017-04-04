package com.baidu.beidou.api.internal.user.rpc;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.user.exporter.AccountService;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
@Ignore
public class AccountServiceTest {

	@Test
	/**
	 * 提供给财务中心使用，当beidou账户余额由零调整为非零时，调整balancestat字段为1，上线广告传输。
	 * 当财务中心金额转账到零时，将调整balancestat字段为0，下线广告传输。
	 * 日志中记录下操作时间，以备查询使用
	 * @param userId 用户ID，整型
	 * @param balanceStat 状态值，1为有钱，上线；0没有钱，下线
	 * @return 结果状态码：0 执行成功；-2 参数非法;-100 找不到对应的用户；-1 系统报错，需重试
	 * 下午03:27:44
	 */
	public void testGetPlanName() throws Exception{
		//McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/AccountService", "UTF-8", new ExceptionHandler());
		McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.93:8088/beidou-api/api/AccountService", "UTF-8", new ExceptionHandler());
		
		AccountService exporter = ProxyFactory.createProxy(AccountService.class, proxy);
		int result = exporter.modUserBalanceStat(19, 1);
		System.out.println("result:" + result);
		
	}
}
