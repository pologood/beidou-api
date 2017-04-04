package com.baidu.beidou.api.external.cproplan2.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.beidou.cproplan.constant.CproPlanConstant;
import com.baidu.beidou.cproplan.util.CproPlanDeviceOSUtil;
import com.baidu.beidou.util.BinaryUtils;

/**
 * 
 * 推广计划-“设备、操作系统”和前端的交互帮助类
 * 
 * @author kanghongwei
 * @fileName DeviceOSTreeUtil.java
 * @dateTime 2013-6-11 下午4:02:59
 */

public class DeviceOsUtil {

	public static int isAllDevice(long deviceOperaSystem) {
		List<Long> binaryList = BinaryUtils.long2BinaryLongList(deviceOperaSystem);

		long partingValue = (long) Math.pow(2.0, CproPlanConstant.DEVICE_OS_PARTING_VALUE * 1.0);

		List<Long> deviceIdList = getLesserList(binaryList, partingValue);

		long deviceIdSumValue = CproPlanDeviceOSUtil.orOperation(deviceIdList);
		if (deviceIdSumValue != 0) {
			// “设备”自选
			return 0;
		}
		return 1;
	}

	public static void main(String[] args) {
		long deviceOperaSystem = 8388607;
		System.out.println("设备" + isAllDevice(deviceOperaSystem));
		System.out.println("设备" + getDeviceValue(deviceOperaSystem));

		System.out.println("操作系统" + isAllOs(deviceOperaSystem));
		System.out.println("操作系统" + getOSValue(deviceOperaSystem));

	}

	public static long getDeviceValue(long deviceOperaSystem) {
		List<Long> binaryList = BinaryUtils.long2BinaryLongList(deviceOperaSystem);

		long partingValue = (long) Math.pow(2.0, CproPlanConstant.DEVICE_OS_PARTING_VALUE * 1.0);

		List<Long> deviceIdList = getLesserList(binaryList, partingValue);

		return CproPlanDeviceOSUtil.orOperation(deviceIdList);
	}

	public static int isAllOs(long deviceOperaSystem) {
		List<Long> binaryList = BinaryUtils.long2BinaryLongList(deviceOperaSystem);

		long partingValue = (long) Math.pow(2.0, CproPlanConstant.DEVICE_OS_PARTING_VALUE * 1.0);

		List<Long> osIdList = getBiggerList(binaryList, partingValue);

		long osIdSumValue = CproPlanDeviceOSUtil.orOperation(osIdList);
		if (osIdSumValue != 0) {
			// “操作系统”自选
			return 0;
		}
		return 1;
	}

	public static List<Long> getOSValue(long deviceOperaSystem) {
		List<Long> binaryList = BinaryUtils.long2BinaryLongList(deviceOperaSystem);

		long partingValue = (long) Math.pow(2.0, CproPlanConstant.DEVICE_OS_PARTING_VALUE * 1.0);

		return getBiggerList(binaryList, partingValue);
	}

	private static List<Long> getLesserList(List<Long> list, long partingValue) {
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}
		List<Long> result = new ArrayList<Long>(list.size());
		for (long value : list) {
			if (value < partingValue) {
				result.add(value);
			}
		}
		return result;
	}

	private static List<Long> getBiggerList(List<Long> list, long partingValue) {
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}
		List<Long> result = new ArrayList<Long>(list.size());
		for (long value : list) {
			if (value >= partingValue) {
				result.add(value);
			}
		}
		return result;
	}
}
