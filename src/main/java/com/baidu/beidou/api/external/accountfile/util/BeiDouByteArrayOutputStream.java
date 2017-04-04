package com.baidu.beidou.api.external.accountfile.util;

import java.io.ByteArrayOutputStream;
/**
 * ByteArrayOutputStream的toByteArray方法创建一个新分配的 byte 数组，然后拷贝数据
 * 为提供性能直接返回，不需要拷贝
 * @author caichao
 */
public class BeiDouByteArrayOutputStream extends ByteArrayOutputStream{
	
	public BeiDouByteArrayOutputStream() {
		super();
	}
	
	public BeiDouByteArrayOutputStream(int size) {
		super(size);
	}
	
	/**
	 * 获取流内部buf 不需要copy一份新的
	 * @return 
	 * caichao
	 */
	public byte[] getInternalBuf() {
		return buf;
	}
}
