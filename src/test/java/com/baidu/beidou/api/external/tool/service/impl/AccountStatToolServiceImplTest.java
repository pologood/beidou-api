package com.baidu.beidou.api.external.tool.service.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.report.vo.AccountStatViewItem;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.api.external.tool.service.AccountStatToolServiceImpl;
import com.baidu.beidou.api.external.tool.service.vo.AbstractReportData;
import com.baidu.beidou.api.external.tool.service.vo.ReportDataFactory;
import com.baidu.beidou.api.external.tool.vo.response.GetOneReportResponse;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.user.bo.User;
import com.baidu.beidou.user.service.UserMgr;
import com.baidu.beidou.util.DateUtils;

@Ignore
public class AccountStatToolServiceImplTest extends StatToolServiceImplTestSupport {
    @Resource
    private AccountStatToolServiceImpl accountStatToolService;
    private static List<Map<String, Object>> mockDrisData;

    private UserMgr userMgr;
    private CproPlanMgr planMgr;

    @BeforeClass
    public static void contextInitialized() {
        mockDrisData =
                getMockDorisData("src/test/java/com/baidu/beidou/api/external/tool/service/impl/AccountStatToolServiceImplTest.data");
    }

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Before
    public void beforeEach() {
        userMgr = context.mock(UserMgr.class);
        accountStatToolService.setUserMgr(userMgr);
        planMgr = context.mock(CproPlanMgr.class);
        accountStatToolService.setCproPlanMgr(planMgr);
        // statMgr = context.mock(StatService2.class);
        // accountStatToolService.setStatMgr(statMgr);
    }

    @After
    public void afterEach() {
        context.assertIsSatisfied();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testQueryStat() {
        try {
            ApiReportQueryParameter request = new ApiReportQueryParameter();
            Date startDate = DateUtils.strToDate("20111225");
            Date endDate = DateUtils.strToDate("20111231");
            request.setUserid(19);
            request.setStartDate(startDate);
            request.setEndDate(endDate);
            request.setReportType(1);
            context.checking(new Expectations() {
                {
                    User user = getUser(19, "测试用户名123");
                    allowing(userMgr).findUserBySFid(with(any(Integer.class)));
                    will(returnValue(user));
                    Integer sumBudget = 1141;
                    allowing(planMgr).findEffectivePlanBudgetByUserId(with(any(Integer.class)));
                    will(returnValue(sumBudget));
                    // Date startDate = DateUtils.strToDate("20111228");
                    // Date endDate = DateUtils.strToDate("20111231");
                    // allowing(statMgr).queryAUserData(with(any(Integer.class)), with(any(Date.class)),
                    // with(any(Date.class)), with(any(Integer.class)), with(any(Integer.class)));
                    will(returnValue(mockDrisData));
                }
            });

            List<AccountStatViewItem> result = null;
            Integer sumBudget = 0;
            try {
                result = accountStatToolService.queryStat(request);
                sumBudget = accountStatToolService.querySumBudget(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(sumBudget);
            for (AccountStatViewItem item : result) {
                System.out.println(item);
            }
            GetOneReportResponse data = new GetOneReportResponse();
            // 使用返回数据封装api对外数据
            @SuppressWarnings("rawtypes")
            List result1 = result;
            AbstractReportData report = ReportDataFactory.getReportData(request.getReportType());
            data = report.fillData(result1);
            data.setBudget(sumBudget);

            System.out.println(data);
            assertThat(result.size(), is(7));
            assertThat(result.get(0).getCost(), is(0d));
            assertThat(result.get(1).getCost(), is(0d));
            assertThat(result.get(2).getCost(), is(0d));
            assertThat(result.get(3).getCost(), not(0d));
            assertThat(result.get(4).getCost(), is(0d));
            assertThat(result.get(5).getCost(), is(0d));
            assertThat(result.get(6).getCost(), not(0d));

            assertThat(sumBudget, is(1141));
            /**
             * "todate": "2011-12-28 23:00:00", "ctr": 0.192484, "fromdate": "2011-12-28 00:00:00", "clks": 589, "cost":
             * 30600, "acp": 0.51952462, "cpm": 100, "srchs": 3060
             */
            AccountStatViewItem item = result.get(3);
            assertThat(item.getDay(), is("20111228"));
            assertThat(item.getUserid(), is(19));
            assertThat(item.getUsername(), is("测试用户名123"));
            assertThat(item.getSrchs(), is(3060l));
            assertThat(item.getClks(), is(589l));
            assertThat(item.getCost(), is(306.0));
            // assertThat(item.getCtr(), is(new BigDecimal(0.193018)));
            // assertThat(item.getCpm(), is(new BigDecimal(100.000000)));
            // assertThat(item.getAcp(), is(new BigDecimal(0.518085)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
