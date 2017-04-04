package com.baidu.beidou.api.external.util.interceptor;


import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.cprogroup.bo.CproGroup;
import com.baidu.beidou.cprogroup.bo.CproGroupInfo;
import com.baidu.beidou.cprogroup.service.CproGroupMgr;
import com.baidu.beidou.cprogroup.service.CproGroupVTMgr;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.util.memcache.BeidouCacheInstance;

/**
 * 
 * ClassName: CacheInterceptor  <br>
 * Function: 缓存拦截器
 *
 * @author zhangxu
 * @date Jul 11, 2012
 */
public class CacheInterceptor implements MethodInterceptor {
	
	private static Log log = LogFactory.getLog(CacheInterceptor.class);
	
	private static int GLOBAL_CACHE_EXPIRE_TIME = 60;
	
	public static final String PLAN_KEY = "C_PLAN_";
	public static final String GROUP_KEY = "C_GROUP_";
	public static final String ACCOUNTFILE_KEY = "C_AF_";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = invocation.proceed();
		Class clazz = invocation.getMethod().getDeclaringClass();
		String method = invocation.getMethod().getName();
		if (clazz.equals(CproGroupMgr.class)) {
			if (method.equals("addCproGroup")) {
				if (result != null) {
					CproGroup group = (CproGroup)(invocation.getArguments()[1]);
					String id = getId(group, CproGroup.class, "getGroupId");
					putCache(GROUP_KEY + id, group);
				}
			} else if (method.equals("modCproGroup")) {
				if (result != null) {
					CproGroup group = (CproGroup)(invocation.getArguments()[1]);
					String id = getId(group, CproGroup.class, "getGroupId");
					putCache(GROUP_KEY + id, group);
				}
			} else if (method.equals("modCproGroupInfo")) {
				if (result != null) {
					CproGroupInfo groupInfo = (CproGroupInfo)(invocation.getArguments()[1]);
					String id = getId(groupInfo, CproGroupInfo.class, "getGroupId");
					CproGroup group = getCache(String.valueOf(GROUP_KEY + id));
					if (group != null) {
						group.setGroupInfo(groupInfo);
						updateCache(GROUP_KEY + id, group);
					}
				}
			} else if (method.equals("modSiteTradeInfo")) {
				if (result != null) {
					CproGroupInfo groupInfo = (CproGroupInfo)(invocation.getArguments()[1]);
					String id = getId(groupInfo, CproGroupInfo.class, "getGroupId");
					CproGroup group = getCache(String.valueOf(GROUP_KEY + id));
					if (group != null) {
						group.setGroupInfo(groupInfo);
						updateCache(GROUP_KEY + id, group);
					}
				}
			} else if (method.equals("findCproGroupById")) {
				Integer groupId = (Integer)(invocation.getArguments()[0]);
				CproGroup group = getCache(String.valueOf(GROUP_KEY + groupId));
				if (result == null && group != null) {
					result = group;
					log.info("Cache avoid DB master-slave delay for group=[" + groupId + "]");
				} else if(result != null && group != null){
					copyGroup((CproGroup)result, group);
				}
			} else if (method.equals("findWithInfoById")) {
				Integer groupId = (Integer)(invocation.getArguments()[0]);
				CproGroup group = getCache(String.valueOf(GROUP_KEY + groupId));
				if (result == null && group != null) {
					result = group;
					log.info("Cache avoid DB master-slave delay for group=[" + groupId + "]");
				} else if(result != null && group != null){
					copyGroup((CproGroup)result, group);
				}
			} else if (method.equals("findUserIdByGroupIds")) {
				if (isEmpty(result)) {
					List<Integer> groupIds = (List<Integer>)(invocation.getArguments()[0]);
					List<Integer> userIds = new ArrayList<Integer>();
					for (Integer groupId : groupIds) {
						CproGroup group = getCache(String.valueOf(GROUP_KEY + groupId));
						if (group != null) {
							userIds.add(group.getUserId());
						}
					}
					result = userIds;
					log.info("Cache avoid DB master-slave delay for findUserIdByGroupIds");
				}
			}
		} else if (clazz.equals(CproPlanMgr.class)) {
			if (method.equals("addCproPlan")) {
				if (result != null) {
					CproPlan plan = (CproPlan)(invocation.getArguments()[1]);
					String id = getId(plan, CproPlan.class, "getPlanId");
					putCache(PLAN_KEY + id, plan);
				}
			} else if (method.equals("modCproPlan")) {
				if (result != null) {
					CproPlan plan = (CproPlan)(invocation.getArguments()[1]);
					String id = getId(plan, CproPlan.class, "getPlanId");
					putCache(PLAN_KEY + id, plan);
				}
			} else if (method.equals("findCproPlanById")) {
				Integer planId = (Integer)(invocation.getArguments()[0]);
				CproPlan plan = getCache(String.valueOf(PLAN_KEY + planId));
				if (result == null && plan != null) {
					result = plan;
					log.info("Cache avoid DB master-slave delay for plan=[" + planId + "]");
				} else if(result != null && plan != null){
					copyPlan((CproPlan)result, plan);
				}
			} else if (method.equals("findUserIdByPlanIds")) {
				if (isEmpty(result)) {
					List<Integer> planIds = (List<Integer>)(invocation.getArguments()[0]);
					List<Integer> userIds = new ArrayList<Integer>();
					for (Integer planId : planIds) {
						CproPlan plan = getCache(String.valueOf(PLAN_KEY + planId));
						if (plan != null) {
							userIds.add(plan.getUserId());
						}
					}
					result = userIds;
					log.info("Cache avoid DB master-slave delay for findUserIdByPlanIds");
				}
			} 
		} else if (clazz.equals(CproGroupVTMgr.class)) {
			if (method.equals("modTargetTypeToVT")) {
				Integer targetType = (Integer)(invocation.getArguments()[0]);
				Integer id = (Integer)(invocation.getArguments()[3]);
				CproGroup group = getCache(String.valueOf(GROUP_KEY + id));
				if (group != null) {
					group.setTargetType(targetType);
					updateCache(GROUP_KEY + id, group);
				}
			}
		} 
		
		return result;
	}
	
	public static void copyGroup(CproGroup to, CproGroup from){
		try {
			CproGroupInfo info = ((CproGroup)to).getGroupInfo();
			BeanUtils.copyProperties(to, from);
			BeanUtils.copyProperties(info, from.getGroupInfo());
			to.setGroupInfo(info);
		} catch (Exception e) {
			log.error("Errro to copy cprogroup bean");
		}
	}
	
	public static void copyPlan(CproPlan to, CproPlan from){
		try {
			BeanUtils.copyProperties(to, from);
		} catch (Exception e) {
			log.error("Errro to copy cproplan bean");
		}
	}


	public static <T extends Serializable> void putCache(String key, T t){
		BeidouCacheInstance.getInstance().memcacheRandomSet(key, t, GLOBAL_CACHE_EXPIRE_TIME);
		log.info("Add Key=["+ key + "] in cache");
	}
	
	public static <T extends Serializable> void updateCache(String key, T t){
		Object cacheObj = BeidouCacheInstance.getInstance().memcacheRandomGet(key);
		if(cacheObj != null && (cacheObj instanceof Serializable)) {
			BeidouCacheInstance.getInstance().memcacheRandomSet(key, t, GLOBAL_CACHE_EXPIRE_TIME);
			log.info("Update Key=["+ key + "] in cache");
		}else{
			log.info("Update Key=["+ key + "] NOT FOUND in cache");
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T getCache(String key){
		T t = (T)(BeidouCacheInstance.getInstance().memcacheRandomGet(key));
		if (t != null) {
			log.info("Hit Key=["+ key + "] in cache");
			return t;
		} else {
			log.info("NOT Hit Key=["+ key + "] in cache");
			return null;
		}
	}
	
	public static <T extends Serializable> void deleteCache(String key){
		BeidouCacheInstance.getInstance().memcacheRandomDelete(key);
		log.info("Del Key=["+ key + "] in cache");
	}
	
	public final static <T extends Serializable> String getId(T t, Class<T> genericClass, String getMethodName) {
		try {
			Method method = genericClass.getMethod(getMethodName);
			return String.valueOf((method.invoke(t)));
		} catch (IllegalAccessException e) {
			throw new RuntimeException("method is unaccessable");
		} catch (InvocationTargetException e) {
			throw new RuntimeException("error generic class of map's key");
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("no method called " + getMethodName);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static boolean isEmpty(Object result){
		return (result == null || (result instanceof Collection && CollectionUtils.isEmpty((Collection)result)));
	}

	public int getGLOBAL_CACHE_EXPIRE_TIME() {
		return GLOBAL_CACHE_EXPIRE_TIME;
	}

	public void setGLOBAL_CACHE_EXPIRE_TIME(int global_cache_expire_time) {
		GLOBAL_CACHE_EXPIRE_TIME = global_cache_expire_time;
	}
	
}
