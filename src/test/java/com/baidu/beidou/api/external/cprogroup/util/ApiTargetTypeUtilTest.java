package com.baidu.beidou.api.external.cprogroup.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class ApiTargetTypeUtilTest {

	@Before
	public void beforeTest() {
		
	}
	
	@Test
	public void testIsValidTargetType() {
		assertThat(ApiTargetTypeUtil.isValidTargetType(0), is(false));
		assertThat(ApiTargetTypeUtil.isValidTargetType(1), is(true));
		assertThat(ApiTargetTypeUtil.isValidTargetType(2), is(true));
		assertThat(ApiTargetTypeUtil.isValidTargetType(3), is(true));
		assertThat(ApiTargetTypeUtil.isValidTargetType(4), is(true));
		assertThat(ApiTargetTypeUtil.isValidTargetType(5), is(false));
	}
	
	
	@Test
	public void testToApiGroupTargetType() {
		//public static final int API_TARGET_TYPE_RT = 1;
		//public static final int API_TARGET_TYPE_KT = 2;
		//public static final int API_TARGET_TYPE_NONE = 3;
		//public static final int API_TARGET_TYPE_VT = 4;
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(0), is(3)); // PT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(1), is(2)); // CT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(2), is(2)); // QT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(3), is(2)); // CT QT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(4), is(2)); // HCT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(5), is(2)); // CT HCT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(6), is(2)); // QT HCT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(7), is(2)); // QT CT HCT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(8), is(1)); // RT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(16), is(4)); // VT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(40), is(1)); // RT + IT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(48), is(4)); // VT + IT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(35), is(2)); // CT + QT + IT
		assertThat(ApiTargetTypeUtil.toApiGroupTargetType(32), is(3)); // PT + IT

	}
	
	@Test
	public void testIsValidKtTargetType() {
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(0), is(false)); // PT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(1), is(true)); // CT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(2), is(true)); // QT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(3), is(true)); // CT QT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(4), is(true)); // HCT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(5), is(true)); // CT HCT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(6), is(true)); // QT HCT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(7), is(true)); // QT CT HCT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(8), is(false)); // RT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(16), is(false)); // VT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(40), is(false)); // RT + IT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(48), is(false)); // VT + IT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(35), is(true)); // CT + QT + IT
		assertThat(ApiTargetTypeUtil.isValidKtTargetType(32), is(false)); // PT + IT
	}
	
	
	@Test
	public void testToApiGroupKtTargetType() {
		//public static final int GROUP_TARGET_TYPE_CT=1;//主题词定向（关键词定向的一种）
		//public static final int GROUP_TARGET_TYPE_QT=2;//搜索定向（关键词定向的一种）
		//public static final int GROUP_TARGET_TYPE_HCT=4;//历史主题词定向（关键词定向的一种）
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(0), is(0)); // PT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(1), is(1)); // CT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(2), is(2)); // QT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(3), is(3)); // CT QT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(4), is(4)); // HCT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(5), is(5)); // CT HCT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(6), is(6)); // QT HCT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(7), is(7)); // QT CT HCT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(32), is(0)); // PT + IT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(33), is(1)); // CT + IT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(34), is(2)); // QT + IT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(35), is(3)); // CT QT + IT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(36), is(4)); // HCT + IT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(37), is(5)); // CT HCT + IT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(38), is(6)); // QT HCT + IT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(39), is(7)); // QT CT HCT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(8), is(0)); // RT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(16), is(0)); // VT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(40), is(0)); // RT + IT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(48), is(0)); // VT + IT
		assertThat(ApiTargetTypeUtil.toApiKtTargetType(32), is(0)); // PT + IT

	}
	
	
}
