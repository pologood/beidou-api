package com.baidu.beidou.api.external.util.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.baidu.beidou.api.external.util.LimitQueue;

/**
 * 
 * ClassName: PrisonCache  <br>
 * Function: 封禁缓存
 *
 * @author zhangxu
 * @date Jun 24, 2012
 */
public class PrisonCache<E> {

	private int queueLen = 0;
	
	public PrisonCache(int queueLen){
		this.queueLen = queueLen;
	}
	
	private Map<String, LimitQueue<E>> PRISON_MAP = new ConcurrentHashMap<String, LimitQueue<E>>();

	/**
	 * 队头插入
	 * @param key   
	 * @param e
	 */
	public void lpush(String key, E e){
		LimitQueue<E> lqueue = PRISON_MAP.get(key);
		if (lqueue == null) {
			lqueue = new LimitQueue<E>(queueLen);
			PRISON_MAP.put(key, lqueue);
		}
		lqueue.offer(e);
	}
	
	/**
	 * 队尾退出
	 * @param key   
	 * @return E 
	 */
	public E rpop(String key){
		LimitQueue<E> lqueue = PRISON_MAP.get(key);
		if (lqueue == null) {
			return null;
		}
		return lqueue.poll();
	}

	/**
	 * 队列长度
	 * @param key   
	 * @return E 
	 */
	public int len(String key) {
		LimitQueue<E> lqueue = PRISON_MAP.get(key);
		if (lqueue == null) {
			return 0;
		}
		return lqueue.size();
	}
	
}
