package com.baidu.beidou.api.external;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.baidu.beidou.api.external.accountfile.dao.impl.ApiAccountFileTaskDaoTest;
import com.baidu.beidou.api.external.accountfile.exporter.impl.AccountFileServiceImplTest;
import com.baidu.beidou.api.external.accountfile.service.impl.ApiAccountFileTaskMgrTest;
import com.baidu.beidou.api.external.code.exporter.impl.CodeServiceImplTest;
import com.baidu.beidou.api.external.cprogroup.exporter.impl.GroupConfigServiceImplTest;
import com.baidu.beidou.api.external.cprogroup.exporter.impl.GroupConfigServiceImplTestExcludeApp;
import com.baidu.beidou.api.external.cprogroup.exporter.impl.GroupConfigServiceImplTestExcludeKeyword;
import com.baidu.beidou.api.external.cprogroup.exporter.impl.GroupConfigServiceImplTestExcludePeople;
import com.baidu.beidou.api.external.cprogroup.exporter.impl.GroupConfigServiceImplTestPackInfo;
import com.baidu.beidou.api.external.cprogroup.exporter.impl.GroupConfigServiceImplTestPrice;
import com.baidu.beidou.api.external.cprogroup.exporter.impl.GroupServiceImplTest;
import com.baidu.beidou.api.external.cprogroup.util.ApiTargetTypeUtilTest;
import com.baidu.beidou.api.external.cproplan.exporter.impl.CampaignServiceImplTest;
import com.baidu.beidou.api.external.cproplan2.exporter.impl.CampaignServiceImplTest2;
import com.baidu.beidou.api.external.cprounit.exporter.impl.AdServiceImplTest;
import com.baidu.beidou.api.external.cprounit2.exporter.impl.AdServiceImplTest2;
import com.baidu.beidou.api.external.fc.impl.FCServiceImplTest;
import com.baidu.beidou.api.external.interest.impl.InterestServiceImplTest;
import com.baidu.beidou.api.external.kr.exporter.impl.KrServiceImplTest;
import com.baidu.beidou.api.external.people.exporter.impl.PeopleServiceImplTest;
import com.baidu.beidou.api.external.tool.exporter.impl.ToolServiceImplTest;
import com.baidu.beidou.api.external.tool.exporter.impl.ToolServiceImplTest2;
import com.baidu.beidou.api.external.user.exporter.impl.PayCalculaterServiceImplTest;
import com.baidu.beidou.api.external.user.exporter.impl.UserAccountServiceImplTest;
import com.baidu.beidou.api.external.util.LimitQueueTest;
import com.baidu.beidou.api.integratetest.facade.impl.ApiReportFacadeImplTest;
import com.baidu.beidou.api.integratetest.service.impl.AccountStatServiceImplTest;
import com.baidu.beidou.api.integratetest.service.impl.ApiReportTaskMgrImplTest;

@RunWith(Suite.class)
@SuiteClasses({
	PeopleServiceImplTest.class,
	UserAccountServiceImplTest.class,
	PayCalculaterServiceImplTest.class,
	CodeServiceImplTest.class,
	AccountStatServiceImplTest.class,
	ApiReportTaskMgrImplTest.class,
	ApiReportFacadeImplTest.class,
	GroupConfigServiceImplTest.class,
	GroupServiceImplTest.class,
	CampaignServiceImplTest.class,
	CampaignServiceImplTest2.class,
	AdServiceImplTest.class,
	AdServiceImplTest2.class,
	ApiAccountFileTaskDaoTest.class,
	ApiAccountFileTaskMgrTest.class,
	AccountFileServiceImplTest.class,
	FCServiceImplTest.class,
	ApiTargetTypeUtilTest.class,
	InterestServiceImplTest.class,
	LimitQueueTest.class,
	KrServiceImplTest.class,
	ToolServiceImplTest.class,
	ToolServiceImplTest2.class,
	GroupConfigServiceImplTestExcludeKeyword.class,
	GroupConfigServiceImplTestExcludePeople.class,
	GroupConfigServiceImplTestPackInfo.class,
	GroupConfigServiceImplTestPrice.class,
	GroupConfigServiceImplTestExcludeApp.class
})
public class ExternalTestSuite {

}
