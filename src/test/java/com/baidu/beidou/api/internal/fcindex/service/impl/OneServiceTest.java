package com.baidu.beidou.api.internal.fcindex.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.fcindex.service.OneService;
import com.baidu.beidou.api.internal.fcindex.service.OneServiceConst;
import com.baidu.beidou.api.internal.fcindex.vo.ApiTaxStatVo;
import com.baidu.beidou.api.internal.fcindex.vo.BatchStatVo;
import com.baidu.beidou.api.internal.fcindex.vo.BudgetVo;
import com.baidu.beidou.api.internal.fcindex.vo.StatVo;
import com.baidu.beidou.api.internal.fcindex.vo.StatVo.Stat;
import com.baidu.beidou.test.common.BasicTestCaseLegacy;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.ReportHourViewItem;

/**
 * OneServiceTest
 * 
 * @author work
 * 
 */
public class OneServiceTest extends BasicTestCaseLegacy {

    private static final SimpleDateFormat _format = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    private OneService one = null;

    @Before
    public void setUp() {
          super.prepareMemcachedStub();
    }

    @Test
    public void testGetBeidouTaxData() {
        ApiTaxStatVo vo = one.getBeidouTaxData(480787L, "2013-09-9", "2013-09-15", 1);
        assertEquals("", vo.getErrmsg());
        assertTrue(vo.getResult().size() > 0);
    }

    @Test
    public void testGetAllBeidouTaxData() {
        Set<Long> userIds = new HashSet<Long>();
        userIds.add(3L);
        userIds.add(5L);
        userIds.add(64L);
        // userIds.add(480786L);
        ApiTaxStatVo vo = one.getAllBeidouTaxData(userIds, "2014-08-05", "2014-08-20");
        System.out.println(vo.getResult());
        assertEquals("", vo.getErrmsg());
        assertTrue(vo.getResult().size() > 0);
    }

    @Test
    public void testGetBeidouData() {
        StatVo vo = one.getBeidouData(1176815L, "2012-12-12", "2012-12-21", 0);
        assertEquals("", vo.getErrmsg());
        assertTrue(vo.getResult().size() > 0);
    }

    @Test
    public void testGetBeidouDataOverYear() {
        StatVo vo = one.getBeidouData(8L, "2012-12-28", "2013-01-03", 0);
        assertEquals("", vo.getErrmsg());
        assertTrue(vo.getResult().size() > 0);
    }

    @Test
    public void testGetBeidouDataOverYearByDay() {
        StatVo vo = one.getBeidouData(18L, "2012-12-01", "2013-01-03", 0);
        assertEquals("", vo.getErrmsg());
        assertTrue(vo.getResult().size() > 0);
    }

    @Test
    public void testGetBeidouDataBatchOverYear() {
        Set<Long> userids = new HashSet<Long>();
        userids.add(3080266L);
        userids.add(5406826L);
        userids.add(783434L);
        BatchStatVo vo = one.getAllBeidouData(userids, "2012-12-28", "2013-01-03");
        assertEquals("", vo.getErrmsg());
        assertTrue(vo.getResult().size() > 0);
    }

    @Test
    public void testIsOverRange() throws ParseException {
        assertFalse(OneServiceImpl.isOverRange(_format.parse("2012-12-27"), _format.parse("2012-12-29"), 4));

        assertFalse(OneServiceImpl.isOverRange(_format.parse("2012-12-28"), _format.parse("2013-01-03"),
                OneServiceConst.TimeRange.SINGLE));
        assertFalse(OneServiceImpl.isOverRange(_format.parse("2012-12-28"), _format.parse("2012-12-29"),
                OneServiceConst.TimeRange.SINGLE));
        assertTrue(OneServiceImpl.isOverRange(_format.parse("2012-09-28"), _format.parse("2013-01-03"),
                OneServiceConst.TimeRange.SINGLE));
        assertTrue(OneServiceImpl.isOverRange(_format.parse("2012-09-12"), _format.parse("2012-12-21"),
                OneServiceConst.TimeRange.SINGLE));
        assertTrue(OneServiceImpl.isOverRange(_format.parse("2012-03-12"), _format.parse("2013-01-01"),
                OneServiceConst.TimeRange.SINGLE));

        assertFalse(OneServiceImpl.isOverRange(_format.parse("2012-12-28"), _format.parse("2013-01-03"),
                OneServiceConst.TimeRange.BATCH));
        assertFalse(OneServiceImpl.isOverRange(_format.parse("2012-12-28"), _format.parse("2013-01-03"),
                OneServiceConst.TimeRange.BATCH));
        assertTrue(OneServiceImpl.isOverRange(_format.parse("2012-11-28"), _format.parse("2013-01-03"),
                OneServiceConst.TimeRange.BATCH));
        assertTrue(OneServiceImpl.isOverRange(_format.parse("2012-11-15"), _format.parse("2012-12-17"),
                OneServiceConst.TimeRange.BATCH));
        assertTrue(OneServiceImpl.isOverRange(_format.parse("2012-01-15"), _format.parse("2013-01-01"),
                OneServiceConst.TimeRange.BATCH));
    }

    @Test
    public void testMixRange() {
        StatVo vo = one.getBeidouData(1176815L, "2012-12-12", "2012-12-01", 1);
        assertEquals(OneServiceConst.ErrorCode.DATE_RANGE_MIX, vo.getErrmsg());
    }

    @Test
    public void testGetBeidouDataByDay() {
        StatVo vo = one.getBeidouData(1176815L, "2012-12-12", "2012-12-21", 1);
        assertEquals("", vo.getErrmsg());
        assertTrue(vo.getResult().size() > 0);
    }

    @Test
    public void testGetBeidouDataByDayFillZero() {
        StatVo vo = one.getBeidouData(1L, "2012-12-19", "2012-12-21", 1);
        assertEquals("", vo.getErrmsg());
        assertTrue(vo.getResult().size() > 0);
        List<StatVo.Stat> stat50 = vo.getResult().get("5_0");
        assertEquals("2012-12-19", stat50.get(0).getDate());
    }

    @Test
    public void testFillZero() {
        List<StatVo.Stat> stat50 = OneServiceImpl.fillZero("2012-12-19", "2012-12-21");
        assertEquals(3, stat50.size());
        assertEquals(0, stat50.get(0).getClks());
        assertEquals(0, stat50.get(0).getPaysum(), 0);
        assertEquals(0, stat50.get(0).getShows());
    }

    @Test
    public void testFillZeroOverYear() {
        List<StatVo.Stat> stat50 = OneServiceImpl.fillZero("2012-12-29", "2013-01-02");
        assertEquals(5, stat50.size());
        assertEquals(0, stat50.get(0).getClks());
        assertEquals(0, stat50.get(0).getPaysum(), 0);
        assertEquals(0, stat50.get(0).getShows());
    }

    @Test
    public void testFillSpaceByZero() {
        List<StatVo.Stat> statData = new ArrayList<StatVo.Stat>();
        StatVo.Stat s1 = new StatVo.Stat("2012-12-10", 123, 123, 123);
        StatVo.Stat s2 = new StatVo.Stat("2012-12-13", 234, 234, 234);
        statData.add(s1);
        statData.add(s2);
        // should fill 2012-12-11, 2012-12-12
        List<StatVo.Stat> stat50 = OneServiceImpl.fillZero(statData, "2012-12-10", "2012-12-13");
        assertEquals(4, stat50.size());
    }

    @Test
    public void testGetBeidouBasicInfo() {
        BudgetVo budget = one.getBeidouBasicInfo(1L);
        assertNotNull(budget);
    }

    @Test
    public void testGetBeidouBasicInfoZero() {
        BudgetVo budget = one.getBeidouBasicInfo(1L);
        assertNotNull(budget);
        BudgetVo.Budget b = budget.getResult().get(OneServiceConst.SysCode.CPRO);
        assertEquals(0, b.getBudget(), 0);
    }

    @Test
    public void testGetBeidouDataBatch() {
        Set<Long> userids = new HashSet<Long>();
        // userids.add(3080266L);
        // userids.add(5406826L);
        // userids.add(783434L);
        userids.add(18L);
        userids.add(19L);
        BatchStatVo vo = one.getAllBeidouData(userids, "2012-12-15", "2012-12-17");
        assertEquals("", vo.getErrmsg());
        assertTrue(vo.getResult().size() > 0);
    }

    @Test
    public void testBuildStatRtnByDay() {
        List<StatVo.Stat> statData = new ArrayList<StatVo.Stat>();
        StatVo.Stat s1 = new StatVo.Stat("2012-12-12", 123, 123, 123);
        StatVo.Stat s2 = new StatVo.Stat("2012-12-13", 234, 234, 234);
        statData.add(s1);
        statData.add(s2);
        StatVo.Stat s3 = new StatVo.Stat("2012-12-12", 123, 123, 123);
        StatVo.Stat s4 = new StatVo.Stat("2012-12-13", 234, 234, 234);
        statData.add(s3);
        statData.add(s4);
        Map<String, List<StatVo.Stat>> mm = OneServiceImpl.buildStatRtn(statData, true);
        List<StatVo.Stat> stats5 = mm.get(OneServiceConst.SysCode.WM);
        assertEquals(2, stats5.size());

        List<StatVo.Stat> stats50 = mm.get(OneServiceConst.SysCode.CPRO);
        assertEquals(2, stats50.size());
        StatVo.Stat stat50 = stats50.get(0);
        if ("2012-12-12".equals(stat50.getDate())) {
            assertEquals(246, stat50.getShows());
            assertEquals(246, stat50.getClks());
            assertEquals(246.00, stat50.getPaysum(), 0);
        }
    }

    @Test
    public void testBuildStatRtn() {
        List<StatVo.Stat> statData = new ArrayList<StatVo.Stat>();
        StatVo.Stat s1 = new StatVo.Stat(123, 123, 123);
        StatVo.Stat s2 = new StatVo.Stat(234, 234, 234);
        statData.add(s1);
        statData.add(s2);
        Map<String, List<StatVo.Stat>> mm = OneServiceImpl.buildStatRtn(statData, false);
        List<StatVo.Stat> stats5 = mm.get(OneServiceConst.SysCode.WM);
        assertEquals(1, stats5.size());
        StatVo.Stat stat5 = stats5.get(0);
        assertEquals(357, stat5.getShows());
        assertEquals(357, stat5.getClks());
        assertEquals(357.00, stat5.getPaysum(), 0);
        assertEquals(2, mm.size());
        List<StatVo.Stat> stats50 = mm.get(OneServiceConst.SysCode.WM);
        assertEquals(1, stats50.size());
        StatVo.Stat stat50 = stats5.get(0);
        assertEquals(357, stat50.getShows());
        assertEquals(357, stat50.getClks());
        assertEquals(357.00, stat50.getPaysum(), 0);
    }

    @Test
    public void testGetBeidouDataErr92() {
        StatVo vo = one.getBeidouData(1176815L, "2012-09-12", "2012-12-21", 0);
        assertEquals(OneServiceConst.ErrorCode.DATE_RANGE_SINGLE, vo.getErrmsg());
    }

    @Test
    @Ignore
    public void testGetBeidouDataErrToday() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -43);
        StatVo vo = one.getBeidouData(1176815L, _format.format(c.getTime()), _format.format(new Date()), 0);
        assertEquals(OneServiceConst.ErrorCode.DATE_NO_TODAY, vo.getErrmsg());
    }

    @Test
    public void testIncludesToday() throws ParseException {
        assertTrue(OneServiceImpl.includesToday(new Date()));
        assertFalse(OneServiceImpl.includesToday(_format.parse("2013-01-03")));
    }

    @Test
    public void testGetBeidouDataErrFormat() {
        StatVo vo = one.getBeidouData(1176815L, "2012/12/12", "2012/12/21", 0);
        assertEquals(OneServiceConst.ErrorCode.DATE_FORMAT, vo.getErrmsg());
    }

    @Test
    public void testGetBeidouDataErrByDay() {
        StatVo vo = one.getBeidouData(1176815L, "2012-12-12", "2012-12-21", 2);
        assertEquals(OneServiceConst.ErrorCode.BY_DAY, vo.getErrmsg());
    }

    @Test
    public void testGetBeidouDataBatchErr31() {
        Set<Long> userids = new HashSet<Long>();
        userids.add(3080266L);
        userids.add(5406826L);
        userids.add(783434L);
        BatchStatVo vo = one.getAllBeidouData(userids, "2012-11-15", "2012-12-17");
        assertEquals(OneServiceConst.ErrorCode.DATE_RANGE_BATCH, vo.getErrmsg());
    }

    @Test
    public void testShouldCacheYesterday() { // yesterday
        Calendar cc = Calendar.getInstance();
        cc.add(Calendar.DAY_OF_YEAR, -1);
        assertTrue(OneServiceImpl.internalShouldCache(_format.format(cc.getTime()), _format.format(cc.getTime())));
    }

    @Test
    public void testShouldCacheTheDayBeforeYesterday() { // the day before yesterday
        Calendar cc = Calendar.getInstance();
        cc.add(Calendar.DAY_OF_YEAR, -2);
        assertTrue(OneServiceImpl.internalShouldCache(_format.format(cc.getTime()), _format.format(cc.getTime())));
    }

    @Test
    public void testShouldCacheLast7() { // last seven days
        Calendar cf1 = Calendar.getInstance();
        cf1.add(Calendar.DAY_OF_YEAR, -7);
        Calendar ct1 = Calendar.getInstance();
        ct1.add(Calendar.DAY_OF_YEAR, -1);
        assertTrue(OneServiceImpl.internalShouldCache(_format.format(cf1.getTime()), _format.format(ct1.getTime())));
    }

    @Test
    public void testShouldCacheLast14() { // last seven days before a week
        Calendar cf1 = Calendar.getInstance();
        cf1.add(Calendar.DAY_OF_YEAR, -14);
        Calendar ct1 = Calendar.getInstance();
        ct1.add(Calendar.DAY_OF_YEAR, -8);
        assertTrue(OneServiceImpl.internalShouldCache(_format.format(cf1.getTime()), _format.format(ct1.getTime())));
    }

    @Test
    public void testShouldCacheLastWeek() { // last week
        Calendar cf2 = Calendar.getInstance();
        cf2.add(Calendar.WEEK_OF_YEAR, -1);
        Calendar ct2 = Calendar.getInstance();
        ct2.add(Calendar.DAY_OF_YEAR, -1);
        assertTrue(OneServiceImpl.internalShouldCache(_format.format(cf2.getTime()), _format.format(ct2.getTime())));
    }

    @Test
    public void testShouldCacheCurrMonth() { // current month
        Calendar cc = Calendar.getInstance();
        cc.add(Calendar.DAY_OF_YEAR, -1);
        Calendar cf3 = Calendar.getInstance();
        cf3.set(Calendar.MONTH, cc.get(Calendar.MONTH));
        cf3.set(Calendar.DAY_OF_MONTH, 1);
        assertTrue(OneServiceImpl.internalShouldCache(_format.format(cf3.getTime()), _format.format(cc.getTime())));
    }

    @Test
    public void testShouldCacheLastMonth() { // last month
        Calendar cf4 = Calendar.getInstance();
        cf4.add(Calendar.WEEK_OF_YEAR, -1);
        Calendar ct4 = Calendar.getInstance();
        ct4.add(Calendar.DAY_OF_YEAR, -1);
        assertTrue(OneServiceImpl.internalShouldCache(_format.format(cf4.getTime()), _format.format(ct4.getTime())));
    }

    @Test
    public void testGetBeidouDataBatchZero() {
        Set<Long> userids = new HashSet<Long>();
        userids.add(1L);
        userids.add(2L);
        userids.add(3L);
        BatchStatVo vo = one.getAllBeidouData(userids, "2013-01-03", "2013-01-03");
        assertTrue(vo.getResult().size() == 3);

        assertTrue(0 == vo.getResult().get(0).getClks());
        assertTrue(0 == vo.getResult().get(0).getShows());
        assertTrue(0 == vo.getResult().get(0).getPaysum());
    }

    @Test
    public void testGetBeidouDataBatchMix() {
        Set<Long> userids = new HashSet<Long>();
        userids.add(1L);
        userids.add(5406826L);
        userids.add(783434L);
        BatchStatVo vo = one.getAllBeidouData(userids, "2012-12-15", "2012-12-17");
        assertEquals("", vo.getErrmsg());
        assertTrue(vo.getResult().size() == 3);
    }

    @Test
    public void testIsYesterday() throws ParseException {
        Date now = _format.parse("2012-11-01");

        String star = "2012-10-30";
        String start = "2012-10-31";
        String end = "2012-10-31";

        assertFalse(OneServiceImpl.isYesterday(star, end, now));

        assertTrue(OneServiceImpl.isYesterday(start, end, now));

        start = "2012-12-12";
        end = "2012-12-12";
        now = _format.parse("2012-12-13");

        assertTrue(OneServiceImpl.isYesterday(start, end, now));
    }

    @Test
    public void testIsYesterdayDiffYear() throws ParseException {
        Date now = _format.parse("2013-01-01");

        String star = "2012-12-30";
        String start = "2012-12-31";
        String end = "2012-12-31";

        assertFalse(OneServiceImpl.isYesterday(star, end, now));
        assertTrue(OneServiceImpl.isYesterday(start, end, now));
    }

    @SuppressWarnings("unused")
    @Test
    public void testCacheMoreData() {
        Date today = new Date();
        Calendar c_1 = Calendar.getInstance();
        c_1.setTime(today);
        c_1.add(Calendar.DAY_OF_YEAR, -1);
        String from_1 = _format.format(c_1.getTime());
        String to_1 = _format.format(c_1.getTime());
        one.getBeidouData(18L, from_1, to_1, 0);

        c_1.add(Calendar.DAY_OF_YEAR, -1);
        from_1 = _format.format(c_1.getTime());
        to_1 = _format.format(c_1.getTime());
        one.getBeidouData(18L, from_1, to_1, 0);

        // get yesterday data, so 3 more data set will be cached at the same time
        // the day before yesterday
        Calendar c_2 = Calendar.getInstance();
        c_2.setTime(today);
        c_2.add(Calendar.DAY_OF_YEAR, -2);
        String from_2 = _format.format(c_2.getTime());
        String to_2 = _format.format(c_2.getTime());
        String key_2 = OneServiceImpl.buildKey(18L, from_2, to_2);
        // Object data_2 = (BeidouCacheInstance.getInstance().memcacheGet(key_2));
        // assertNotNull(data_2);

        // last 7 days
        Calendar c_7 = Calendar.getInstance();
        c_7.setTime(today);
        c_7.add(Calendar.DAY_OF_YEAR, -7);
        String key_7 = OneServiceImpl.buildKey(18L, _format.format(c_7.getTime()), to_1);
        // Object data_7 = (BeidouCacheInstance.getInstance().memcacheGet(key_7));
        // assertNotNull(data_7);

        // last 14 ~ 7 days
        Calendar c_14 = Calendar.getInstance();
        c_14.setTime(today);
        c_14.add(Calendar.DAY_OF_YEAR, -14);
        c_7.add(Calendar.DAY_OF_YEAR, -1);
        String key_14 = OneServiceImpl.buildKey(18L, _format.format(c_14.getTime()), _format.format(c_7.getTime()));
        // Object data_14 = (BeidouCacheInstance.getInstance().memcacheGet(key_14));
        // assertNotNull(data_14);
    }

    @Test
    public void disposeYesterdayDataAndMore() {
        Date today = new Date();
        Calendar c_1 = Calendar.getInstance();
        c_1.setTime(today);
        c_1.add(Calendar.DAY_OF_YEAR, -1);

        Calendar c_2 = Calendar.getInstance();
        c_2.setTime(today);
        c_2.add(Calendar.DAY_OF_YEAR, -2);

        String from_1 = _format.format(c_2.getTime());
        String to_1 = _format.format(c_1.getTime());
        StatVo vo;
        // vo = one.getBeidouData(18L, from_1, to_1, 0);
        vo = one.getBeidouData(18L, from_1, to_1, 1);

        for (Map.Entry<String, List<Stat>> entry : vo.getResult().entrySet()) {
            System.out.println("key is " + entry.getKey());
            List<Stat> list = entry.getValue();
            for (Stat s : list) {
                System.out.println("#" + s.getDate() + "#");
                System.out.println(s.getPaysum());
                System.out.println(s.getShows());
                System.out.println(s.getClks());
            }
        }
    }

    @Test
    @Ignore
    public void testGetHourUserReport() {
        List<ReportHourViewItem> viewItems = one.getHourUserReport(19, "2015-12-15", "2015-12-29");
        for (ReportHourViewItem item : viewItems) {
            System.out.println(item);
        }
        Assert.assertTrue(viewItems.size() > 0);

        viewItems = one.getHourUserReport(321, "2015-12-15", "2015-12-29");
        Assert.assertNull(viewItems);

        viewItems = one.getHourUserReport(19, "2015-12-15", "2015-12-19");
        for (ReportHourViewItem item : viewItems) {
            System.out.println(item);
        }
        Assert.assertTrue(viewItems.size() == 0);
    }

    /**
     * 释放memcache桩
     */
    @After
    public void release() {
        super.releaseMemcachedStub();
    }


}
