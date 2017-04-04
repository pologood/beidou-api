package com.baidu.beidou.api.internal.user.rpc;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import com.baidu.beidou.api.internal.user.exporter.UcEventReceiver;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
@Ignore
public class UcEventReceiverTest {

	public static int EVENT_ADD_USER = 1;
	public static int EVENT_MOD_SFSTATUS = 2;
	
	private static final String USERNAME_KEY = "username";
	private static final String USERSTATUS_KEY = "ustatus";
	private static final String USERLEVEL_KEY = "ulevel";
	
    /**Shifen状态迁移标志: 
     * 1：shifen状态为被拒绝(shifenstatus=4)时，设置为1；
     * 0：shifen状态为有效或者金额为0时，设置为0；Shifen状态为其他状态的更新时，该列不进行变化。
    **/
	
	/**
	 * @param token
	 *            本次操作token序列号
	 * @param eventType
	 *            事件类型:    1: 添加用户事件;  2: 用户的shifen状态通知; 
	 * @param userId
	 *            该事件的用户id
	 * @param info
	 *            事件元数据, 如用户uid
	 * @return
	 */
	@Test
	public void testNotifyUser() throws Exception{
		//McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/UcEventReceiver", "UTF-8", new ExceptionHandler());
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/api/UcEventReceiver", "UTF-8", new ExceptionHandler());
		
		UcEventReceiver exporter = ProxyFactory.createProxy(UcEventReceiver.class, proxy);
		long token = 12397845834l;
		int maxUserid = 3693854;
		Map<String, String> info = new HashMap<String, String>();
		info.put(USERNAME_KEY, "zhangxu004");
		info.put(USERSTATUS_KEY, "4");
		info.put(USERLEVEL_KEY, "10101");
		boolean result = exporter.notify(token, EVENT_MOD_SFSTATUS, maxUserid + 1, info);
		System.out.println("result:" + result);
		
	}
}
