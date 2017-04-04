package com.baidu.beidou.api.external.util.interceptor;

import static com.baidu.beidou.api.external.util.constant.ApiConstant.BAN_LEVEL1_KEY;
import static com.baidu.beidou.api.external.util.constant.ApiConstant.BAN_LEVEL2_KEY;
import static com.baidu.beidou.api.external.util.constant.ApiConstant.BAN_LEVEL3_KEY;
import static com.baidu.beidou.api.external.util.constant.ApiConstant.BAN_VALUE;
import static com.baidu.beidou.api.external.util.constant.ApiConstant.PRISON_LEVEL1_USERKEY;
import static com.baidu.beidou.api.external.util.constant.ApiConstant.PRISON_LEVEL2_USERKEY;
import static com.baidu.beidou.api.external.util.constant.ApiConstant.PRISON_LEVEL3_USERKEY;

import java.util.Date;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.util.ApiResultBeanUtils;
import com.baidu.beidou.api.external.util.DRAPIMountAPIBeanUtils;
import com.baidu.beidou.api.external.util.cache.PrisonCacheInstance;
import com.baidu.beidou.api.external.util.constant.PositionConstant;
import com.baidu.beidou.api.external.util.error.GlobalErrorCode;
import com.baidu.beidou.api.external.util.request.ApiRequest;
import com.baidu.beidou.util.memcache.BeidouCacheInstance;

/**
 * 
 * ClassName: PrisonInterceptor  <p>
 * Function: 封禁控制拦截器 <p>
 * 工作原理：每次请求Request构造一个Date类型的对象，push到一个集中处理的定长队列中（java queue实现），根据pop出的Date与 <br>
 * push进去的Date的时间间隔来判断是否有超过定长时间内的访问阈值限制，队列分为3个级别，level1默认是60秒内访问超过60次，level2是 <br>
 * 60秒内访问超过120次，level3是60秒内访问超过240次，依次的需要封禁让其不允许访问API服务一定时长，level1禁止访问180s，level2禁止访问 <br>
 * 1800秒，level3禁止访问3600s。
 *
 * @author zhangxu
 * @date Jun 19, 2012
 */
public class PrisonInterceptor implements MethodInterceptor {
	
	private static final Log LOG = LogFactory.getLog(PrisonInterceptor.class);
	
	private static final Log prisonLog = LogFactory.getLog("prisonLog");
	
	private static boolean PRISON_ENABLE = true;
	
	private static int PRISON_LEVEL1_REJECT_TIME = 180;
	private static int PRISON_LEVEL2_REJECT_TIME = 1800;
	private static int PRISON_LEVEL3_REJECT_TIME = 3600;
	
	private static int PRISON_LEVEL1_THRESHOLD = 60;
	private static int PRISON_LEVEL2_THRESHOLD = 120;
	private static int PRISON_LEVEL3_THRESHOLD = 240;
	
	private static int PRISON_PERIOD = 60;
	
	private static int EXPIRE = 3600;
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if(PRISON_ENABLE){
			// 获取调用接口方法名称
			String funtionName = invocation.getMethod().getName();
			
			Object[] params = invocation.getArguments();
			
			Object obj = (Object) params[0];
			int[] typeAndUser = DRAPIMountAPIBeanUtils.getTypeUserInfo(obj);
			int type = typeAndUser[0];
			int opUser = typeAndUser[1];
			int userId = typeAndUser[2];
			
			ApiRequest request = (ApiRequest)params[1];
			if(request == null){
				Object object = invocation.proceed();
				return object;
			}

			String key1 = PRISON_LEVEL1_USERKEY + userId;
			String key2 = PRISON_LEVEL2_USERKEY + userId;
			String key3 = PRISON_LEVEL3_USERKEY + userId;
			String ban1 = BAN_LEVEL1_KEY + userId;
			String ban2 = BAN_LEVEL2_KEY + userId;
			String ban3 = BAN_LEVEL3_KEY + userId;
			
			// 业务逻辑处理前：判断是否到达封禁阈值
			try {
				long start = System.currentTimeMillis();
				
				Date now = new Date();
				
				if(testPrison(now, key3, PRISON_LEVEL3_THRESHOLD)){
					LOG.info("PIRSON LEVEL3 FOUND. Opuser=[" + opUser + "] userId=[" + userId + "] function=[" + funtionName + "]");
					setPrison(ban3, PRISON_LEVEL3_REJECT_TIME);
				} else if(testPrison(now, key2, PRISON_LEVEL2_THRESHOLD)){
					LOG.info("PIRSON LEVEL2 FOUND. Opuser=[" + opUser + "] userId=[" + userId + "] function=[" + funtionName + "]");
					setPrison(ban2, PRISON_LEVEL2_REJECT_TIME);
				} else if(testPrison(now, key1, PRISON_LEVEL1_THRESHOLD)){
					LOG.info("PIRSON LEVEL1 FOUND. Opuser=[" + opUser + "] userId=[" + userId + "] function=[" + funtionName + "]");
					setPrison(ban1, PRISON_LEVEL1_REJECT_TIME);
				} 
				
				boolean hasPrison = false;
				if(hasPrison(ban3)){
					hasPrison = true;
					
				} else if(hasPrison(ban2)){
					hasPrison = true;
					
				} else if(hasPrison(ban1)){
					hasPrison = true;
					
				} 
				
				long end = System.currentTimeMillis();
				prisonLog.info("PrisonInterceptor using " + (end-start) + " millseconds");
				
				if(hasPrison){
					return ApiResultBeanUtils.addApiError(type, null,
							GlobalErrorCode.TOO_FREQUENTLY.getValue(),
							GlobalErrorCode.TOO_FREQUENTLY.getMessage(),
							PositionConstant.NOPARAM, null);
				}
				
			} catch (Exception e) {
				LOG.error("封禁策略拦截器执行失败 " + e.getMessage(),e);
			}
		}
		
		// 处理请求，执行业务逻辑处理
		Object obj = invocation.proceed();
		
		return obj;
	}
	
	/**
	 * testPrison <br>
	 * @param now 现在时刻的Date
	 * @param key 队列的key
	 * @param threshold 队列的长度
	 * @return boolean 是否访问过于频繁
	 */
	private boolean testPrison(Date now, String key, int threshold) {
		PrisonCacheInstance.getCache(threshold).lpush(key, now);
		prisonLog.info("lpush " + key + "|" + now);
		int len = PrisonCacheInstance.getCache(threshold).len(key);
		if (len > threshold) {
			prisonLog.info("llen " + key + " > " + threshold);
			Date mostBeforeDate = PrisonCacheInstance.getCache(threshold).rpop(key);
			prisonLog.info("Visit diff time is " + (now.getTime() - mostBeforeDate.getTime()));
			if((now.getTime() - mostBeforeDate.getTime()) < PRISON_PERIOD * 1000){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * setPrison <br>
	 * @param key 置封禁访问的key
	 * @param expire 过期时间
	 * @return boolean 是否置封禁成功
	 */
	private void setPrison(String key, int expire) {
		prisonLog.info("setPrison key=" + key);
		BeidouCacheInstance.getInstance().memcacheRandomSet(key, BAN_VALUE, expire);
	}
	
	/**
	 * hasPrison <br>
	 * @param key 置封禁访问的key
	 * @return boolean 是否有被封禁访问
	 */
	private boolean hasPrison(String key) {
		if(BeidouCacheInstance.getInstance().memcacheRandomGet(key) == null){
			prisonLog.info("hasNOTPrison key=" + key);
			return false;
		}
		prisonLog.info("hasPrison key=" + key);
		return true;
	}

	public int getPRISON_LEVEL1_REJECT_TIME() {
		return PRISON_LEVEL1_REJECT_TIME;
	}

	public void setPRISON_LEVEL1_REJECT_TIME(int prison_level1_reject_time) {
		PRISON_LEVEL1_REJECT_TIME = prison_level1_reject_time;
	}

	public int getPRISON_LEVEL2_REJECT_TIME() {
		return PRISON_LEVEL2_REJECT_TIME;
	}

	public void setPRISON_LEVEL2_REJECT_TIME(int prison_level2_reject_time) {
		PRISON_LEVEL2_REJECT_TIME = prison_level2_reject_time;
	}

	public int getPRISON_LEVEL3_REJECT_TIME() {
		return PRISON_LEVEL3_REJECT_TIME;
	}

	public void setPRISON_LEVEL3_REJECT_TIME(int prison_level3_reject_time) {
		PRISON_LEVEL3_REJECT_TIME = prison_level3_reject_time;
	}

	public int getPRISON_LEVEL1_THRESHOLD() {
		return PRISON_LEVEL1_THRESHOLD;
	}

	public void setPRISON_LEVEL1_THRESHOLD(int prison_level1_threshold) {
		PRISON_LEVEL1_THRESHOLD = prison_level1_threshold;
	}

	public int getPRISON_LEVEL2_THRESHOLD() {
		return PRISON_LEVEL2_THRESHOLD;
	}

	public void setPRISON_LEVEL2_THRESHOLD(int prison_level2_threshold) {
		PRISON_LEVEL2_THRESHOLD = prison_level2_threshold;
	}

	public int getPRISON_LEVEL3_THRESHOLD() {
		return PRISON_LEVEL3_THRESHOLD;
	}

	public void setPRISON_LEVEL3_THRESHOLD(int prison_level3_threshold) {
		PRISON_LEVEL3_THRESHOLD = prison_level3_threshold;
	}

	public int getPRISON_PERIOD() {
		return PRISON_PERIOD;
	}

	public void setPRISON_PERIOD(int prison_period) {
		PRISON_PERIOD = prison_period;
	}

	public int getEXPIRE() {
		return EXPIRE;
	}

	public void setEXPIRE(int expire) {
		EXPIRE = expire;
	}

	public boolean isPRISON_ENABLE() {
		return PRISON_ENABLE;
	}

	public void setPRISON_ENABLE(boolean prison_enable) {
		PRISON_ENABLE = prison_enable;
	}
	
	
}
