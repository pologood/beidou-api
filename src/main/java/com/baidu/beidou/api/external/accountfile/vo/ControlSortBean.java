package com.baidu.beidou.api.external.accountfile.vo;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


/**
 * 对于数据量比较大层级的文件，将拆分成多个任务并行处理
 * 此类用于控制同样请求参数请求多次得到数据顺序保持一致
 * 对于数据量小不需要拆分的文件，只会对应一个bean
 * 每个人物线程对应一个ControlSortBean对象
 * @author caichao
 *
 */
public class ControlSortBean {
	
	//例如有100个groupid 首先会对groupid排序， 拆分为10组，第一组对应的key为1,第二组key为2...
	//最终合并文件时根据key大小,一次性有序的写入数据到文件，从而保证多次请求数据顺序一致
	//byte[] 保存每个线程处理后的数据
	private Map<Integer,byte[]> dataMap;
	
	public ControlSortBean() {
	}
	
	public void addData(Integer blockId,byte[] byteArray) {
		if (dataMap == null) {
			dataMap = Collections.synchronizedMap(new TreeMap<Integer,byte[]>());
			dataMap.put(blockId, byteArray);
			return;
		}
		dataMap.put(blockId, byteArray);
	}

	public Map<Integer, byte[]> getDataMap() {
		return dataMap;
	}

}
