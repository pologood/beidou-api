package com.baidu.beidou.api.internal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.baidu.beidou.api.internal.business.exporter.impl.NameServiceImplTest;
import com.baidu.beidou.api.internal.business.exporter.impl.NameServiceImplTest2;
import com.baidu.beidou.api.internal.business.exporter.impl.NameServiceImplTest3;
import com.baidu.beidou.api.internal.business.exporter.impl.NameServiceImplTest4;
import com.baidu.beidou.api.internal.fcindex.service.SimpleUserInfoMgrImplTest;
import com.baidu.beidou.api.internal.holmes.exporter.HolmesPeopleServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
	NameServiceImplTest.class,
	NameServiceImplTest2.class,
	NameServiceImplTest3.class,
	NameServiceImplTest4.class,
	SimpleUserInfoMgrImplTest.class,
	HolmesPeopleServiceTest.class
})
public class InternalTestSuite {

}
