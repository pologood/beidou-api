package com.baidu.beidou.api.external.cprogroup.util;

import com.baidu.beidou.api.external.cprogroup.constant.GroupConstant;
import com.baidu.beidou.cprogroup.constant.CproGroupConstant;
import com.baidu.beidou.cprogroup.util.TargettypeUtil;

/**
 * 
 * ClassName: ApiGroupTypeUtil  <br>
 * Function: beidou-core中的TargetType值映射到API中的转换工具类
 *
 * @author zhangxu
 * @date May 30, 2012
 */
public class ApiTargetTypeUtil {
	
	//***********************************************************
	// TargetType - KT/RT/VT/None 静态工具方法
	//***********************************************************
	/**
	 * 是否是合法的类型
	 * @param targetType
	 * @return boolean 
	 */
	public static boolean isValidTargetType(int targetType){
		if (targetType != GroupConstant.API_TARGET_TYPE_RT
				&& targetType != GroupConstant.API_TARGET_TYPE_KT
				&& targetType != GroupConstant.API_TARGET_TYPE_NONE
				&& targetType != GroupConstant.API_TARGET_TYPE_VT) {
			return false;
		}
		return true;
	}

	/**
	 * 返回API对外定义的受众行为类型
	 * @param targetType
	 * @return int 
	 */
	public static int toApiGroupTargetType(int targetType){
		if (TargettypeUtil.isPack(targetType)) {
			return GroupConstant.API_TARGET_TYPE_ADVANCED;
		} else if (TargettypeUtil.hasKT(targetType)) {
			return GroupConstant.API_TARGET_TYPE_KT;
		} else if (TargettypeUtil.hasRT(targetType)) {
			return GroupConstant.API_TARGET_TYPE_RT;
		} else if (TargettypeUtil.hasVT(targetType)) {
			return GroupConstant.API_TARGET_TYPE_VT;
		} else if (TargettypeUtil.isAtRight(targetType)){
			return GroupConstant.API_TARGET_TYPE_ATRIGHT;
		} else {
			return GroupConstant.API_TARGET_TYPE_NONE;
		}
	}
	
	//***********************************************************
	// KT TargetType - QT/CT/HCT 静态工具方法
	//***********************************************************
	/**
	 * 默认定向方式为CT QT HCT全选
	 */
	public static final int DEFAUL_KT_TARGET_MASK = 
		CproGroupConstant.GROUP_TARGET_TYPE_CT + CproGroupConstant.GROUP_TARGET_TYPE_QT + CproGroupConstant.GROUP_TARGET_TYPE_HCT;

	/**
	 * 是否是合法的类型
	 * @param targetType
	 * @return boolean 
	 */
	public static boolean isValidKtTargetType(int targetType){
		if ( (targetType & (GroupConstant.API_KT_TARGET_TYPE_QT | 
				GroupConstant.API_KT_TARGET_TYPE_CT | 
				GroupConstant.API_KT_TARGET_TYPE_HCT)) == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 返回API对外定义的受众行为类型 <br>
	 * 从低位到高位依次是CT,QT,HCT，做位或运算
	 * @param targetType
	 * @return int 
	 */
	public static int toApiKtTargetType(int targetType){
		int ktTargetType = 0;
		if (!TargettypeUtil.hasKT(targetType)) {
			return 0;
		}
		if (TargettypeUtil.hasCT(targetType)) {
			ktTargetType |= 1;
		} 
		if (TargettypeUtil.hasQT(targetType)) {
			ktTargetType |= (1<<1);
		} 
		if (TargettypeUtil.hasHCT(targetType)) {
			ktTargetType |= (1<<2);
		} 
		return ktTargetType;
	}
	
	public static void main(String[] args) {
		System.out.println(ApiTargetTypeUtil.toApiGroupTargetType(0)); //PT
		System.out.println(ApiTargetTypeUtil.toApiGroupTargetType(1)); //CT
		System.out.println(ApiTargetTypeUtil.toApiGroupTargetType(7)); //QT CT HCT
		System.out.println(ApiTargetTypeUtil.toApiGroupTargetType(8)); //RT
		System.out.println(ApiTargetTypeUtil.toApiGroupTargetType(16)); //VT
		System.out.println(ApiTargetTypeUtil.toApiGroupTargetType(48)); //IT VT
		
		System.out.println();
		
		System.out.println(ApiTargetTypeUtil.toApiKtTargetType(0)); //PT
		System.out.println(ApiTargetTypeUtil.toApiKtTargetType(8)); //RT
		System.out.println(ApiTargetTypeUtil.toApiKtTargetType(16)); //VT
		System.out.println(ApiTargetTypeUtil.toApiKtTargetType(48)); //IT VT
		System.out.println(ApiTargetTypeUtil.toApiKtTargetType(1)); //CT
		System.out.println(ApiTargetTypeUtil.toApiKtTargetType(2)); //QT
		System.out.println(ApiTargetTypeUtil.toApiKtTargetType(3)); //CT QT
		System.out.println(ApiTargetTypeUtil.toApiKtTargetType(4)); //HCT
		System.out.println(ApiTargetTypeUtil.toApiKtTargetType(5)); //CT HCT
		System.out.println(ApiTargetTypeUtil.toApiKtTargetType(6)); //QT HCT
		System.out.println(ApiTargetTypeUtil.toApiKtTargetType(7)); //QT CT HCT
		
		System.out.println();
		
		System.out.println(ApiTargetTypeUtil.isValidKtTargetType(0)); //PT
		System.out.println(ApiTargetTypeUtil.isValidKtTargetType(8)); //RT
		System.out.println(ApiTargetTypeUtil.isValidKtTargetType(16)); //VT
		System.out.println(ApiTargetTypeUtil.isValidKtTargetType(48)); //IT VT
		System.out.println(ApiTargetTypeUtil.isValidKtTargetType(1)); //CT
		System.out.println(ApiTargetTypeUtil.isValidKtTargetType(2)); //QT
		System.out.println(ApiTargetTypeUtil.isValidKtTargetType(3)); //CT QT
		System.out.println(ApiTargetTypeUtil.isValidKtTargetType(4)); //HCT
		System.out.println(ApiTargetTypeUtil.isValidKtTargetType(5)); //CT HCT
		System.out.println(ApiTargetTypeUtil.isValidKtTargetType(6)); //QT HCT
		System.out.println(ApiTargetTypeUtil.isValidKtTargetType(7)); //QT CT HCT
		
	}
	
}
