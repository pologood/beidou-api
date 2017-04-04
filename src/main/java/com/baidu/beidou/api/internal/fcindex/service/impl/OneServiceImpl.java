/**
 * 
 */
package com.baidu.beidou.api.internal.fcindex.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.internal.fcindex.OneConfigConst;
import com.baidu.beidou.api.internal.fcindex.service.OneService;
import com.baidu.beidou.api.internal.fcindex.service.OneServiceConst;
import com.baidu.beidou.api.internal.fcindex.vo.ApiTaxStatVo;
import com.baidu.beidou.api.internal.fcindex.vo.BatchStatVo;
import com.baidu.beidou.api.internal.fcindex.vo.BatchStatVo.BatchStat;
import com.baidu.beidou.api.internal.fcindex.vo.BudgetVo;
import com.baidu.beidou.api.internal.fcindex.vo.StatVo;
import com.baidu.beidou.api.internal.fcindex.vo.StatVo.Stat;
import com.baidu.beidou.cproplan.bo.CproPlan;
import com.baidu.beidou.cproplan.service.CproPlanMgr;
import com.baidu.beidou.olap.service.ExternalStatService;
import com.baidu.beidou.olap.vo.StatItem;
import com.baidu.beidou.olap.vo.TaxItem;
import com.baidu.beidou.stat.driver.exception.StorageServiceException;
import com.baidu.beidou.stat.vo.TaxStatVo;
import com.baidu.beidou.util.ThreadContext;
import com.baidu.beidou.util.memcache.BeidouCacheInstance;
import com.baidu.cpweb.soma.biz.syncreport.exporter.ReportService;
import com.baidu.unbiz.common.DateUtil;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.ReportHourViewItem;
import com.baidu.unbiz.soma.module.reportmodule.report.common.vo.QueryParam;
import com.baidu.unbiz.soma.module.reportmodule.report.constant.ReportConstants;

/**
 * @author zhuzhenxing@baidu.com
 * 
 */
public class OneServiceImpl implements OneService {

    private static final Log _log = LogFactory.getLog(OneServiceImpl.class);

    private CproPlanMgr cproPlanMgr = null;

    /**
     * 分小时报表最多查询天数
     */
    private static final int MAX_HOUR_REPORT_QUERY_DAYS = 100;

    @Resource(name = "externalStatServiceImpl")
    private ExternalStatService statService;

    private OneConfigConst configConst = null;

    @Resource
    private ReportService syncReportService;

    public void setCproPlanMgr(CproPlanMgr mgr) {
        this.cproPlanMgr = mgr;
    }

    public void setConfigConst(OneConfigConst cfgConst) {
        this.configConst = cfgConst;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.beidou.api.internal.fcindex.service.OneService#getBeidouData(java.lang.Long, java.lang.String,
     * java.lang.String, int)
     */
    public StatVo getBeidouData(Long userid, String startDate, String endDate, int isDay) {
        SimpleDateFormat format = getDateFormate();
        if (_log.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder("get beidou data by [ userid:");
            sb.append(userid).append(", from:").append(startDate).append(", to:");
            sb.append(endDate).append(", isDay:").append(isDay).append(", now:").append(format.format(new Date()))
                    .append(" ]");
            _log.info(sb.toString());
        }

        StatVo vo = new StatVo(OneServiceConst.StatusCode.SUCCESSFUL, "");
        Date from = new Date();
        Date to = new Date();
        try {
            from = format.parse(startDate);
            to = format.parse(endDate);
            if (from.getTime() > to.getTime()) {
                vo.setCode(OneServiceConst.StatusCode.FAILED);
                vo.setErrmsg(OneServiceConst.ErrorCode.DATE_RANGE_MIX);
            } else if (isOverRange(from, to, OneServiceConst.TimeRange.SINGLE)) {
                vo.setCode(OneServiceConst.StatusCode.FAILED);
                vo.setErrmsg(OneServiceConst.ErrorCode.DATE_RANGE_SINGLE);
            }
        } catch (ParseException e) {
            _log.info("wrong start date format, ", e);
            vo.setCode(OneServiceConst.StatusCode.FAILED);
            vo.setErrmsg(OneServiceConst.ErrorCode.DATE_FORMAT);
        }
        if (!(OneServiceConst.ByDay.NO == isDay || OneServiceConst.ByDay.YES == isDay)) {
            vo.setCode(OneServiceConst.StatusCode.FAILED);
            vo.setErrmsg(OneServiceConst.ErrorCode.BY_DAY);
        }

        if (OneServiceConst.StatusCode.FAILED == vo.getCode()) {
            if (_log.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder("date check failed, [ userid:");
                sb.append(userid).append(", from:").append(startDate).append(", to:");
                sb.append(endDate).append(", isDay:").append(isDay).append(", now:").append(format.format(new Date()))
                        .append(" ]").append(", return ").append(vo.toString());
                _log.info(sb.toString());
            }
            return vo;
        }

        String key = buildKey(userid, startDate, endDate);

        boolean shouldCache = shouldCache(startDate, endDate);
        // check cache
        if (shouldCache) {
            @SuppressWarnings("unchecked")
            List<StatItem> statData = (List<StatItem>) getFromCache(buildKey(userid, startDate, endDate));
            if (null != statData && !statData.isEmpty()) {
                vo = getBeidouData(statData, startDate, endDate, isDay);
                if (_log.isInfoEnabled()) {
                    StringBuilder sb = new StringBuilder("get beidou data from cache [ userid: ");
                    sb.append(userid).append(", from:").append(startDate).append(", to:");
                    sb.append(endDate).append(", isDay:").append(isDay).append(", now:")
                            .append(format.format(new Date())).append(" ], " + vo);
                    _log.info(sb.toString());
                }
                return vo;
            }
        } else {
            _log.info("wont cache key=[" + key + "]");
        }

        List<StatItem> statData = new ArrayList<StatItem>();
        try {
            if (isYesterday(startDate, endDate)) {
                statData = disposeYesterdayDataAndMore(userid, startDate, endDate);
            } else {
                statData = queryAndCache(userid, startDate, endDate);
            }
        } catch (StorageServiceException sse) {
            _log.warn(sse);
        } catch (ParseException pe) {
            _log.info(pe);
        }

        vo = getBeidouData(statData, startDate, endDate, isDay);
        if (_log.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder("get beidou data from Doris [ userid: ");
            sb.append(userid).append(", from:").append(startDate).append(", to:");
            sb.append(endDate).append(", isDay:").append(isDay).append(", now:").append(format.format(new Date()))
                    .append(" ], " + vo);
            _log.info(sb.toString());
        }
        return vo;
    }

    static String buildKey(Long userid, String startDate, String endDate) {
        return new StringBuilder("-").append(userid).append("-").append(startDate).append("-").append(endDate)
                .toString();
    }

    static String buildTaxKey(Set<Long> userids, String startDate, String endDate, int isByDay) {
        return new StringBuilder("-").append(OneConfigConst.TaxCachePrefix).append(userids).append("-")
                .append(startDate).append("-").append(endDate).append("-").append(isByDay).toString();
    }

    List<StatItem> queryAndCache(Long userid, String startDate, String endDate) throws ParseException,
            StorageServiceException {
        SimpleDateFormat format = getDateFormate();

        Set<Long> userids = new HashSet<Long>();
        userids.add(userid);

        List<StatItem> statData;
        Date from = format.parse(startDate);
        Date to = format.parse(endDate);

        statData = statService.queryStatDataByFc(userids, from, to);
        if (null != statData && !statData.isEmpty()) {
            ArrayList<StatItem> cl = new ArrayList<StatItem>();
            cl.addAll(statData);
            putIntoCache(buildKey(userid, startDate, endDate), cl);
        }
        return statData;
    }

    @SuppressWarnings("unchecked")
    List<TaxItem> queryTaxAndCache(Set<Long> userids, String startDate, String endDate, int isByDay) {

        SimpleDateFormat format = getDateFormate();
        Date from = null;
        Date to = null;
        try {
            from = format.parse(startDate);
            to = format.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<TaxItem> statData = Collections.emptyList();
        if (this.shouldCacheTax(from, to)) {
            statData = (List<TaxItem>) getFromCache(buildTaxKey(userids, startDate, endDate, isByDay));
        }

        if (CollectionUtils.isEmpty(statData)) {
            statData =
                    statService.queryTaxStatDataByFc(userids, from, to, isByDay == OneServiceConst.ByDay.YES ? true
                            : false);
            if (CollectionUtils.isNotEmpty(statData) && this.shouldCacheTax(from, to)) {
                ArrayList<TaxItem> cacheData = new ArrayList<TaxItem>();
                cacheData.addAll(statData);
                putIntoCache(buildTaxKey(userids, startDate, endDate, isByDay), cacheData);
            }
        }

        return statData;
    }

    List<StatItem> disposeYesterdayDataAndMore(Long userid, String startDate, String endDate) throws ParseException,
            StorageServiceException {

        SimpleDateFormat format = getDateFormate();
        Calendar c1 = Calendar.getInstance();

        // 计算各个缓存的起始时间
        Date yesterday;
        Date yesterday_circle;
        Date week_start;
        Date week_end;
        Date week_circle_start;
        Date week_circle_end;

        c1.setTime(format.parse(endDate));
        yesterday = c1.getTime();

        c1.add(Calendar.DAY_OF_YEAR, -1);
        yesterday_circle = c1.getTime();

        c1.add(Calendar.DAY_OF_YEAR, -5);
        week_start = c1.getTime();

        week_end = yesterday;

        c1.add(Calendar.DAY_OF_YEAR, -7);
        week_circle_start = c1.getTime();

        c1.add(Calendar.DAY_OF_YEAR, 6);
        week_circle_end = c1.getTime();

        // 从doris中取出最近14天的数据
        List<StatItem> statData = Collections.emptyList();

        Set<Long> userids = new HashSet<Long>();
        userids.add(userid);

        statData = statService.queryStatDataByFc(userids, week_circle_start, week_end);

        // 解析4个时间段，并分别将4个时间段的数据缓存起来
        ArrayList<StatItem> yesterdayList = new ArrayList<StatItem>();
        ArrayList<StatItem> yesterdayCircleList = new ArrayList<StatItem>();
        ArrayList<StatItem> lastWeekList = new ArrayList<StatItem>();
        ArrayList<StatItem> lastWeekCircleList = new ArrayList<StatItem>();

        for (StatItem item : statData) {
            Date date = new Date(item.getUnixTime() * 1000L);
            if (date.compareTo(yesterday) == 0) {
                yesterdayList.add(item);
            }
            if (date.compareTo(yesterday_circle) == 0) {
                yesterdayCircleList.add(item);
            }
            if (date.compareTo(week_start) >= 0 && date.compareTo(week_end) <= 0) {
                lastWeekList.add(item);
            }
            if (date.compareTo(week_circle_start) >= 0 && date.compareTo(week_circle_end) <= 0) {
                lastWeekCircleList.add(item);
            }
        }

        putIntoCache(buildKey(userid, format.format(yesterday), format.format(yesterday)), yesterdayList);
        putIntoCache(buildKey(userid, format.format(yesterday_circle), format.format(yesterday_circle)),
                yesterdayCircleList);
        putIntoCache(buildKey(userid, format.format(week_start), format.format(week_end)), lastWeekList);
        putIntoCache(buildKey(userid, format.format(week_circle_start), format.format(week_circle_end)),
                lastWeekCircleList);

        // 返回今天的数据
        return yesterdayList;
    }

    StatVo getBeidouData(List<StatItem> statData, String startDate, String endDate, int isDay) {
        StatVo vo = new StatVo(OneServiceConst.StatusCode.SUCCESSFUL, "");
        if (null != statData && !statData.isEmpty()) {
            List<StatVo.Stat> stat50 = new ArrayList<StatVo.Stat>();
            for (StatItem data : statData) {
                Date d = new Date(data.getUnixTime() * 1000L);
                String dd = null != d ? getDateFormate().format(d) : "";
                Long srchs = data.getSrchs();
                Long clks = data.getClks();
                Long cost = (long) data.getCost(); // unit cent
                if (OneServiceConst.ByDay.YES == isDay) {
                    stat50.add(new StatVo.Stat(dd, srchs.longValue(), clks.longValue(), cent2yuan(cost.longValue())));
                } else { // checked before
                    stat50.add(new StatVo.Stat(srchs.longValue(), clks.longValue(), cent2yuan(cost.longValue())));
                }
            }
            fillZero(stat50, startDate, endDate);
            vo.setResult(buildStatRtn(stat50, OneServiceConst.ByDay.YES == isDay));
        } else { // no result fill with zero
            vo.setResult(buildStatRtn(fillZero(startDate, endDate), OneServiceConst.ByDay.YES == isDay));
        }
        return vo;
    }

    static boolean isYesterday(String startDate, String endDate) {
        if (!startDate.equals(endDate)) {
            return false;
        }
        return isYesterday(startDate, endDate, new Date());
    }

    static boolean isYesterday(String startDate, String endDate, Date today) {
        if (!startDate.equals(endDate)) {
            return false;
        }
        SimpleDateFormat format = getDateFormate();
        try {
            Date end = format.parse(endDate);
            Calendar ce = Calendar.getInstance();
            ce.setTime(end);
            Calendar cn = Calendar.getInstance();
            cn.setTime(today);
            if (ce.get(Calendar.YEAR) != cn.get(Calendar.YEAR)) { // 12-31
                if (ce.get(Calendar.YEAR) > cn.get(Calendar.YEAR)) {
                    return false;
                } else {
                    return Calendar.DECEMBER == ce.get(Calendar.MONTH) && 31 == ce.get(Calendar.DAY_OF_MONTH);
                }
            } else { // same year
                return ce.get(Calendar.DAY_OF_YEAR) == cn.get(Calendar.DAY_OF_YEAR) - 1;
            }
        } catch (ParseException e) {
            // ignore
        }
        return false;
    }

    static List<StatVo.Stat> fillZero(List<StatVo.Stat> stat50, String startDate, String endDate) {
        Map<String, StatVo.Stat> statMap = new HashMap<String, StatVo.Stat>();
        for (StatVo.Stat s : stat50) {
            statMap.put(s.getDate(), s);
        }

        List<StatVo.Stat> zeroStat = fillZero(startDate, endDate);
        for (StatVo.Stat s : zeroStat) {
            if (!statMap.containsKey(s.getDate())) {
                stat50.add(s);
            }
        }
        return stat50;
    }

    static List<StatVo.Stat> fillZero(String startDate, String endDate) {
        SimpleDateFormat format = getDateFormate();
        try {
            Date from = format.parse(startDate);
            Date to = format.parse(endDate);
            List<StatVo.Stat> stat50 = new ArrayList<StatVo.Stat>();
            Calendar df = Calendar.getInstance();
            df.setTime(from);
            Calendar dt = Calendar.getInstance();
            dt.setTime(to);
//            dt.add(Calendar.DAY_OF_YEAR, 1); // [start, end]
            stat50.add(new StatVo.Stat(startDate));
            if (df.get(Calendar.YEAR) == dt.get(Calendar.YEAR)) { // same year
                for (int i = 1, n = dt.get(Calendar.DAY_OF_YEAR) - df.get(Calendar.DAY_OF_YEAR) + 1; i < n; i++) {
                    df.add(Calendar.DAY_OF_YEAR, 1);
                    stat50.add(new StatVo.Stat(format.format(df.getTime())));
                }
            } else {
                Calendar lastDay = Calendar.getInstance();
                lastDay.set(Calendar.YEAR, df.get(Calendar.YEAR));
                lastDay.set(Calendar.MONTH, 11); // December
                lastDay.set(Calendar.DAY_OF_MONTH, 31); // 31
                // fill last year
                for (int i = 1, n = lastDay.get(Calendar.DAY_OF_YEAR) + 1 - df.get(Calendar.DAY_OF_YEAR); i < n; i++) {
                    df.add(Calendar.DAY_OF_YEAR, 1);
                    stat50.add(new StatVo.Stat(format.format(df.getTime())));
                }
                Calendar firstDay = Calendar.getInstance();
                firstDay.set(Calendar.YEAR, dt.get(Calendar.YEAR));
                firstDay.set(Calendar.MONTH, 0); // January
                firstDay.set(Calendar.DAY_OF_MONTH, 1); // 1
                
                stat50.add(new StatVo.Stat(format.format(firstDay.getTime())));
                
                for (int i = 1, n = dt.get(Calendar.DAY_OF_YEAR) - firstDay.get(Calendar.DAY_OF_YEAR) + 1; i < n; i++) {
                    firstDay.add(Calendar.DAY_OF_YEAR, 1);
                    stat50.add(new StatVo.Stat(format.format(firstDay.getTime())));
                }
            }
            return stat50;
        } catch (ParseException e) {
            _log.info("wrong date format, ", e);
        }
        return Collections.emptyList();
    }

    static Map<String, List<StatVo.Stat>> buildStatRtn(List<StatVo.Stat> statData, boolean byDay) {
        Map<String, List<StatVo.Stat>> rtn = new HashMap<String, List<StatVo.Stat>>();
        if (byDay) {
            Map<String, List<StatVo.Stat>> dayMap = new HashMap<String, List<StatVo.Stat>>();
            for (StatVo.Stat stat : statData) {
                String dd = stat.getDate();
                if (dayMap.containsKey(dd)) {
                    List<StatVo.Stat> list = dayMap.get(dd);
                    list.add(stat);
                } else {
                    List<StatVo.Stat> list = new ArrayList<StatVo.Stat>();
                    list.add(stat);
                    dayMap.put(dd, list);
                }
            }

            List<StatVo.Stat> sumStat = new ArrayList<StatVo.Stat>();
            for (Entry<String, List<StatVo.Stat>> es : dayMap.entrySet()) {
                List<StatVo.Stat> ss = es.getValue();
                long shows = 0;
                long clks = 0;
                double pay = 0.0d; // cost in stat
                for (StatVo.Stat stat : ss) {
                    shows += stat.getShows();
                    clks += stat.getClks();
                    pay += stat.getPaysum();
                }
                StatVo.Stat s = new StatVo.Stat(es.getKey(), shows, clks, roundOffTwo(pay));
                sumStat.add(s);
            }

            Collections.sort(sumStat, new Comparator<StatVo.Stat>() {
                public int compare(Stat o1, Stat o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
            rtn.put(OneServiceConst.SysCode.CPRO, sumStat);
            rtn.put(OneServiceConst.SysCode.WM, sumStat);
        } else {
            long shows = 0;
            long clks = 0;
            double pay = 0.0d; // cost in stat
            for (StatVo.Stat stat : statData) {
                shows += stat.getShows();
                clks += stat.getClks();
                pay += stat.getPaysum();
            }
            List<StatVo.Stat> stats = new ArrayList<StatVo.Stat>();
            StatVo.Stat s = new StatVo.Stat(shows, clks, roundOffTwo(pay));
            stats.add(s);
            rtn.put(OneServiceConst.SysCode.CPRO, stats);
            rtn.put(OneServiceConst.SysCode.WM, stats);
        }

        return rtn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.beidou.api.internal.fcindex.service.OneService#getBeidouBasicInfo(java.lang.Long)
     */
    public BudgetVo getBeidouBasicInfo(Long userid) {
        if (_log.isInfoEnabled()) {
            _log.info("get beidou effective budged by userid: " + userid);
        }
        try {
            BudgetVo vo = internalGetBeidouBasicInfo(userid);
            if (_log.isInfoEnabled()) {
                _log.info("get beidou effective budged by userid: " + userid + ", " + vo);
            }
            return vo;
        } catch (Exception e) { // for database exception
            try {
                Thread.sleep(100); // sleep 100m and retry
                try {
                    BudgetVo vo = internalGetBeidouBasicInfo(userid);
                    if (_log.isInfoEnabled()) {
                        _log.info("retry, get beidou effective budged by userid: " + userid + ", " + vo);
                    }
                    return vo;
                } catch (Exception ee) {
                    _log.info("retry, get beidou effective budged by userid: " + userid + ", error2 " + ee.getMessage());
                    return new BudgetVo(OneServiceConst.StatusCode.FAILED, ee.getMessage());
                }
            } catch (InterruptedException e1) {
                ; // ignore this
            }
            _log.info("get beidou effective budged by userid: " + userid + ", error1 " + e.getMessage());
            return new BudgetVo(OneServiceConst.StatusCode.FAILED, e.getMessage());
        }
    }

    BudgetVo internalGetBeidouBasicInfo(Long userid) {
        // for db-routing
        ThreadContext.putUserId(userid.intValue());
        List<CproPlan> plans = cproPlanMgr.findEffectiveByUserId(userid.intValue());
        if (plans == null || plans.isEmpty()) {
            BudgetVo vo = new BudgetVo(OneServiceConst.StatusCode.SUCCESSFUL, OneServiceConst.ErrorCode.NO_RESULT);
            Map<String, BudgetVo.Budget> bgtMap = new HashMap<String, BudgetVo.Budget>();
            BudgetVo.Budget budget = new BudgetVo.Budget(0);
            bgtMap.put(OneServiceConst.SysCode.CPRO, budget);
            vo.setResult(bgtMap);
            return vo;
        }

        int pb = 0;
        for (CproPlan p : plans) {
            pb += p.getBudget().intValue();
        }
        BudgetVo vo = new BudgetVo(OneServiceConst.StatusCode.SUCCESSFUL, "");
        Map<String, BudgetVo.Budget> bgtMap = new HashMap<String, BudgetVo.Budget>();
        BudgetVo.Budget budget = new BudgetVo.Budget(pb);
        bgtMap.put(OneServiceConst.SysCode.CPRO, budget);
        vo.setResult(bgtMap);
        return vo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baidu.beidou.api.internal.fcindex.service.OneService#getBeidouData(java.util.Set, java.lang.String,
     * java.lang.String)
     */
    public BatchStatVo getAllBeidouData(Set<Long> userids, String startDate, String endDate) {
        SimpleDateFormat format = getDateFormate();

        if (_log.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder("get beidou data by [ userids:");
            sb.append(userids).append(", from:").append(startDate).append(", to:");
            sb.append(endDate).append(", now:").append(format.format(new Date())).append(" ]");
            _log.info(sb.toString());
        }

        BatchStatVo vo = new BatchStatVo(OneServiceConst.StatusCode.SUCCESSFUL, "");
        Date from = new Date();
        Date to = new Date();
        try {
            from = format.parse(startDate);
            to = format.parse(endDate);
            if (from.getTime() > to.getTime()) {
                vo.setCode(OneServiceConst.StatusCode.FAILED);
                vo.setErrmsg(OneServiceConst.ErrorCode.DATE_RANGE_MIX);
            } else if (isOverRange(from, to, OneServiceConst.TimeRange.BATCH)) {
                vo.setCode(OneServiceConst.StatusCode.FAILED);
                vo.setErrmsg(OneServiceConst.ErrorCode.DATE_RANGE_BATCH);
            }
        } catch (ParseException e) {
            _log.info("wrong start date format, ", e);
            vo.setCode(OneServiceConst.StatusCode.FAILED);
            vo.setErrmsg(OneServiceConst.ErrorCode.DATE_FORMAT);
            return vo;
        }

        if (OneServiceConst.StatusCode.FAILED == vo.getCode()) {
            if (_log.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder("date check failed, [ userids:");
                sb.append(userids).append(", from:").append(startDate).append(", to:");
                sb.append(endDate).append(", now:").append(format.format(new Date())).append(" ]").append(", return ")
                        .append(vo.toString());
                _log.info(sb.toString());
            }
            return vo;
        }

        // check cache
        List<Long> uids = new ArrayList<Long>(userids.size());
        uids.addAll(userids);
        Collections.sort(uids);
        String key = DigestUtils.md5Hex(new StringBuilder().append(uids).append(startDate).append(endDate).toString());
        boolean shouldCache = shouldCache(startDate, endDate);
        if (shouldCache) {
            BatchStatVo voc = getFromCache(key);
            if (null != voc) {
                if (_log.isInfoEnabled()) {
                    StringBuilder sbl = new StringBuilder("get beidou data from cache [ userids:");
                    sbl.append(userids).append(", from:").append(startDate).append(", to:");
                    sbl.append(endDate).append(", now:").append(format.format(new Date())).append(" ], ").append(voc);
                    _log.info(sbl.toString());
                }
                return voc;
            }
        } else {
            _log.info("wont cache key=[" + key + "]");
        }

        boolean withoutException = true;
        List<StatItem> statData = new ArrayList<StatItem>();
        try {
            statData = statService.queryStatDataByFc(userids, from, to);
        } catch (StorageServiceException sse) {
            withoutException = false;
        }

        if (null != statData && !statData.isEmpty()) {
            vo.setResult(buildStatRtn(userids, statData));
        } else {
            List<BatchStat> statResult = new ArrayList<BatchStat>(userids.size());
            for (Long uid : userids) {
                BatchStat bs = new BatchStat(uid, 0, 0, 0);
                statResult.add(bs);
            }
            vo.setResult(statResult);
        }

        if (withoutException && shouldCache) {
            if (!vo.getResult().isEmpty()) {
                putIntoCache(key, vo);
            }
        }

        if (_log.isInfoEnabled()) {
            StringBuilder sbl = new StringBuilder("get beidou data from Doris [ userids:");
            sbl.append(userids).append(", from:").append(startDate).append(", to:");
            sbl.append(endDate).append(", now:").append(format.format(new Date())).append(" ] ").append(vo);
            _log.info(sbl.toString());
        }

        return vo;
    }

    static List<BatchStatVo.BatchStat> buildStatRtn(Set<Long> userids, List<StatItem> statData) {
        Map<Long, List<BatchStatVo.BatchStat>> voMap = new HashMap<Long, List<BatchStatVo.BatchStat>>();
        for (StatItem data : statData) {
            Long uid = data.getUserId().longValue();
            Long srchs = data.getSrchs();
            Long clks = data.getClks();
            Long cost = (long) data.getCost(); // unit cent
            List<BatchStatVo.BatchStat> stats = voMap.get(uid);
            if (null == stats) {
                stats = new ArrayList<BatchStatVo.BatchStat>();
                stats.add(new BatchStatVo.BatchStat(uid.longValue(), srchs.longValue(), clks.longValue(),
                        cent2yuan(cost)));
                voMap.put(uid, stats);
            } else {
                stats.add(new BatchStatVo.BatchStat(uid.longValue(), srchs.longValue(), clks.longValue(),
                        cent2yuan(cost)));
            }
        }
        List<BatchStatVo.BatchStat> voList = new ArrayList<BatchStatVo.BatchStat>();
        for (Entry<Long, List<BatchStatVo.BatchStat>> e : voMap.entrySet()) {
            List<BatchStatVo.BatchStat> stats = e.getValue();
            long srchs = 0, clks = 0;
            double cost = 0.0d;
            for (BatchStatVo.BatchStat stat : stats) {
                srchs += stat.getShows();
                clks += stat.getClks();
                cost += stat.getPaysum();
            }
            voList.add(new BatchStatVo.BatchStat(e.getKey().longValue(), srchs, clks, roundOffTwo(cost)));
        }
        for (Long uid : userids) {
            if (!voMap.containsKey(uid)) {
                voList.add(new BatchStatVo.BatchStat(uid, 0, 0, 0));
            }
        }
        return voList;
    }

    // keep ten hours cache
    private <T extends Serializable> void putIntoCache(String key, T t) {
        BeidouCacheInstance.getInstance().memcacheSetBig(key, t, configConst.getExpireTime());
        _log.info("put key=[" + key + "] in cache");
    }

    private <T extends Serializable> T getFromCache(String key) {
        @SuppressWarnings("unchecked")
        T t = (T) (BeidouCacheInstance.getInstance().memcacheGet(key));
        if (t != null) {
            _log.info("hit key=[" + key + "] in cache");
            return t;
        } else {
            _log.info("no hit key=[" + key + "] in cache");
            return null;
        }
    }

    boolean shouldCache(String startDate, String endDate) {
        if (this.configConst.isCacheEnable()) {
            return internalShouldCache(startDate, endDate);
        } else {
            return false;
        }
    }

    boolean shouldCacheTax(Date from, Date to) {
        if (this.configConst.isCacheEnable()) {
            if (includesToday(to) || includesToday(from)) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * yesterday, last seven days, last week, current month, last month,
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    static boolean internalShouldCache(String startDate, String endDate) {
        SimpleDateFormat format = getDateFormate();
        try {
            Date from = format.parse(startDate);
            Date to = format.parse(endDate);
            Calendar cf = Calendar.getInstance();
            cf.setTime(from);
            Calendar ct = Calendar.getInstance();
            ct.setTime(to);
            Calendar cc = Calendar.getInstance();

            int df = cf.get(Calendar.DAY_OF_YEAR);
            int dt = ct.get(Calendar.DAY_OF_YEAR);
            int dc = cc.get(Calendar.DAY_OF_YEAR);

            if (includesToday(to)) {
                return false;
            }

            // same year
            if (cf.get(Calendar.YEAR) == ct.get(Calendar.YEAR)) {
                if (df == dt) { // yesterday
                    if (1 == dc) { // today is the day one of a year
                        Calendar dc2 = Calendar.getInstance();
                        dc2.add(Calendar.DAY_OF_YEAR, -1);
                        if (df == dc2.get(Calendar.DAY_OF_YEAR)) { // 12-31
                            return true;
                        } else {
                            dc2.add(Calendar.DAY_OF_YEAR, -1); // the day before yesterday
                            if (df == dc2.get(Calendar.DAY_OF_YEAR)) { // 12-30
                                return true;
                            }
                        }
                    } else if (dt == dc - 1) {
                        return true;
                    } else { // the day before yesterday
                        // today is day 2 of the year, i.e., 12-31 vs. 01-02
                        Calendar dc2 = Calendar.getInstance();
                        dc2.add(Calendar.DAY_OF_YEAR, -2);
                        if (cf.get(Calendar.YEAR) != cc.get(Calendar.YEAR) && 2 == dc && df == dt
                                && df == dc2.get(Calendar.DAY_OF_YEAR)) {
                            return true;
                        } else if (dt == dc - 2) { // in the same year
                            return true;
                        }
                    }
                    return false;
                } else if (6 == dt - df && dt == dc - 1) { // last 7 days
                    return true;
                } else if ((cf.get(Calendar.YEAR) == cc.get(Calendar.YEAR)) // same year
                        && (6 == dt - df && dt == dc - 8)) { // last 14 ~ 8 days
                    return true;
                } else if ((cf.get(Calendar.YEAR) != cc.get(Calendar.YEAR)) // not the same year
                        && (6 == dt - df && (7 == 31 - ct.get(Calendar.DAY_OF_MONTH) + cc.get(Calendar.DAY_OF_MONTH)
                                - 1))) {
                    return true;
                } else if ((1 == cc.get(Calendar.WEEK_OF_YEAR) && 52 == cf.get(Calendar.WEEK_OF_YEAR)
                        && 52 == cf.get(Calendar.WEEK_OF_YEAR) && 8 == cf.get(Calendar.DAY_OF_WEEK)
                        + ct.get(Calendar.DAY_OF_WEEK))
                        || (cf.get(Calendar.DAY_OF_YEAR) == cc.get(Calendar.DAY_OF_YEAR) - 7)
                        && (ct.get(Calendar.DAY_OF_YEAR) == cc.get(Calendar.DAY_OF_YEAR) - 1)) { // last
                    // week
                    return true;
                } else if (cf.get(Calendar.MONTH) == cc.get(Calendar.MONTH)
                        && ct.get(Calendar.MONTH) == cc.get(Calendar.MONTH) && 1 == cf.get(Calendar.DAY_OF_MONTH)) { // current
                                                                                                                     // month
                    return true;
                } else { // last month
                    Calendar ca = Calendar.getInstance();
                    ca.set(Calendar.DAY_OF_MONTH, 1);
                    ca.set(Calendar.MONTH, cc.get(Calendar.MONTH));
                    ca.add(Calendar.DAY_OF_YEAR, -1);
                    if ((11 == cf.get(Calendar.MONTH) && 11 == cf.get(Calendar.MONTH) && 0 == cc.get(Calendar.MONTH)
                            && 1 == cf.get(Calendar.DAY_OF_MONTH) && 31 == ct.get(Calendar.DAY_OF_MONTH)) // January
                            || (ct.get(Calendar.MONTH) == cc.get(Calendar.MONTH) - 1
                                    && 1 == cf.get(Calendar.DAY_OF_MONTH) && ca.get(Calendar.DAY_OF_MONTH) == ct
                                    .get(Calendar.DAY_OF_MONTH))) {
                        return true;
                    }
                }
            } else {
                // yesterday
                if (11 == cf.get(Calendar.MONTH) && 31 == cf.get(Calendar.DAY_OF_MONTH)
                        && 31 == ct.get(Calendar.DAY_OF_MONTH) && 1 == cc.get(Calendar.DAY_OF_MONTH)) {
                    return true;
                } else if (11 == cf.get(Calendar.MONTH) && (7 == 31 - cf.get(Calendar.DAY_OF_MONTH) + dt + 1)) { // last
                                                                                                                 // 7
                    return true;
                } else if ((11 == cf.get(Calendar.MONTH))
                // start and end span 01-01
                        && (7 - 1 == 31 - cf.get(Calendar.DAY_OF_MONTH) + ct.get(Calendar.DAY_OF_MONTH))
                        // end is 7 days before current day, (2 to 10)
                        && (7 + 1 == cc.get(Calendar.DAY_OF_MONTH) - ct.get(Calendar.DAY_OF_MONTH))) { // last
                                                                                                       // 14
                                                                                                       // ~
                                                                                                       // 8
                    return true;
                } else if (1 == cf.get(Calendar.WEEK_OF_YEAR) && 2 == ct.get(Calendar.WEEK_OF_YEAR)) {
                    // last week
                    return true;
                } else {
                    // e.g., TODAY is 2013-01-02, FROM is 2012-12-12 and TO is 2013-01-01,
                    // not last month, not current month
                    // e.g., TODAY is 2013-01-03, FROM is 2012-12-01 and TO is 2012-12-31, it's
                    // previous scenario
                    return false;
                }
            }

            return false;
        } catch (ParseException e) {
            // ignore
            return false;
        }
    }

    static boolean isOverRange(Date start, Date end, int range) {

        Calendar df = Calendar.getInstance();
        df.setTime(start);
        Calendar dt = Calendar.getInstance();
        dt.setTime(end);
        dt.add(Calendar.DAY_OF_YEAR, 1); // [start, end]

        // same year
        if (df.get(Calendar.YEAR) == dt.get(Calendar.YEAR)) {
            int fromDate = df.get(Calendar.DAY_OF_YEAR);
            int toDate = dt.get(Calendar.DAY_OF_YEAR);
            // make it more clear!
            return toDate - fromDate > range;
        } else { // over year
            Calendar lastDay = Calendar.getInstance();
            lastDay.set(Calendar.YEAR, df.get(Calendar.YEAR));
            lastDay.set(Calendar.MONTH, 11); // December
            lastDay.set(Calendar.DAY_OF_MONTH, 31); // 31
            return lastDay.get(Calendar.DAY_OF_YEAR) - df.get(Calendar.DAY_OF_YEAR) + dt.get(Calendar.DAY_OF_YEAR) > range;
        }
    }

    static boolean includesToday(Date end) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(end);
        Calendar c2 = Calendar.getInstance();
        int y1 = c1.get(Calendar.YEAR);
        int y2 = c2.get(Calendar.YEAR);
        if (y1 > y2) {
            return true;
        } else if (y1 < y2) {
            return false;
        } else { // same year
            return c1.get(Calendar.DAY_OF_YEAR) >= c2.get(Calendar.DAY_OF_YEAR);
        }
    }

    static double cent2yuan(long cost) {
        return roundOffTwo(cost / 100.00d);
    }

    static double roundOffTwo(double cost) {
        return BigDecimal.valueOf(cost).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

//    static Date roundEndDate(Date to) {
//        Calendar c = Calendar.getInstance();
//        c.setTime(to);
//        c.add(Calendar.DAY_OF_YEAR, +1);
//        c.add(Calendar.MILLISECOND, -1);
//        return c.getTime();
//    }

    private static final SimpleDateFormat getDateFormate() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public ApiTaxStatVo getBeidouTaxData(Long userid, String startDate, String endDate, int isByDay) {
        ApiTaxStatVo apiRes = checkParamters(userid, startDate, endDate, isByDay);
        if (OneServiceConst.StatusCode.FAILED == apiRes.getCode()) {
            return apiRes;
        }
        SimpleDateFormat format = getDateFormate();
        Date from = null;
        Date to = null;
        try {
            from = format.parse(startDate);
            to = format.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Set<Long> userids = new HashSet<Long>();
        userids.add(userid);
        List<TaxItem> statData = this.queryTaxAndCache(userids, startDate, endDate, isByDay);

        List<TaxStatVo> result = new ArrayList<TaxStatVo>();
        this.fillTaxRecord(result, statData);
        if (isByDay == OneServiceConst.ByDay.YES) {
            result = this.fillNoDataRecord(from, to, result);
        } else {
            result = this.fixTimeField(result);
        }
        apiRes.setResult(result);
        return apiRes;
    }

    public ApiTaxStatVo getAllBeidouTaxData(Set<Long> userids, String startDate, String endDate) {
        ApiTaxStatVo apiRes = checkMultiUserParamters(userids, startDate, endDate);
        if (OneServiceConst.StatusCode.FAILED == apiRes.getCode()) {
            return apiRes;
        }

        List<TaxItem> statData = Collections.emptyList();
        statData = this.queryTaxAndCache(userids, startDate, endDate, OneServiceConst.ByDay.NO);
        List<TaxStatVo> result = new ArrayList<TaxStatVo>();
        this.fillTaxRecord(result, statData);
        this.fixTimeField(result);
        apiRes.setResult(result);
        return apiRes;
    }

    // 不区分时间粒度，则将date列置空
    private List<TaxStatVo> fixTimeField(List<TaxStatVo> infoData) {
        if (CollectionUtils.isNotEmpty(infoData)) {
            for (TaxStatVo item : infoData) {
                item.setDate("");
            }
        }
        return infoData;
    }

    // 补全无统计数据的日期的记录
    private List<TaxStatVo> fillNoDataRecord(Date from, Date to, List<TaxStatVo> infoData) {
        List<TaxStatVo> result = new ArrayList<TaxStatVo>();
        Map<String, TaxStatVo> statMap = new HashMap<String, TaxStatVo>();

        if (CollectionUtils.isNotEmpty(infoData)) {
            for (TaxStatVo item : infoData) {
                String dateStr = item.getDate();
                statMap.put(dateStr, item);
            }
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(from);

        // 天粒度处理,补全天数据
        while (!cal.getTime().after(to)) {
            SimpleDateFormat sd1 = getDateFormate();
            String dateStr = sd1.format(cal.getTime());
            TaxStatVo tmp = statMap.get(dateStr);
            if (null == tmp) {
                tmp = new TaxStatVo();
                tmp.setDate(dateStr);
            }
            result.add(tmp);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }

    private void fillTaxRecord(List<TaxStatVo> result, List<TaxItem> statData) {
        for (TaxItem record : statData) {
            TaxStatVo vo = new TaxStatVo();
            Long cost = (long) record.getCost(); // unit cent
            Long tax = (long) record.getTax(); // unit cent
            Date date = new Date(record.getUnixTime() * 1000L);
            String dateStr = (null != date ? getDateFormate().format(date) : "");
            Integer userId = record.getUserId();
            if (null != userId) {
                vo.setUserId(userId.longValue());
            }
            vo.setCost(cent2yuan(cost));
            vo.setTax(cent2yuan(tax));
            vo.setDate(dateStr);
            result.add(vo);
        }
    }

    private ApiTaxStatVo checkParamters(long userid, String startDate, String endDate, int isByDay) {
        ApiTaxStatVo vo = new ApiTaxStatVo(OneServiceConst.StatusCode.SUCCESSFUL, "");
        Date from = new Date();
        Date to = new Date();
        SimpleDateFormat format = getDateFormate();
        try {
            from = format.parse(startDate);
            to = format.parse(endDate);
            // if (includesToday(to)) { // not includes today
            // vo.setCode(OneServiceConst.StatusCode.FAILED);
            // vo.setErrmsg(OneServiceConst.ErrorCode.DATE_NO_TODAY);
            // } else
            if (from.getTime() > to.getTime()) {
                vo.setCode(OneServiceConst.StatusCode.FAILED);
                vo.setErrmsg(OneServiceConst.ErrorCode.DATE_RANGE_MIX);
            } else if (isOverRange(from, to, OneServiceConst.TimeRange.SINGLE)) {
                vo.setCode(OneServiceConst.StatusCode.FAILED);
                vo.setErrmsg(OneServiceConst.ErrorCode.DATE_RANGE_SINGLE);
            }
        } catch (ParseException e) {
            _log.info("wrong start date format, ", e);
            vo.setCode(OneServiceConst.StatusCode.FAILED);
            vo.setErrmsg(OneServiceConst.ErrorCode.DATE_FORMAT);
        }
        if (!(OneServiceConst.ByDay.NO == isByDay || OneServiceConst.ByDay.YES == isByDay)) {
            vo.setCode(OneServiceConst.StatusCode.FAILED);
            vo.setErrmsg(OneServiceConst.ErrorCode.BY_DAY);
        }

        if (OneServiceConst.StatusCode.FAILED == vo.getCode()) {
            if (_log.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder("date check failed, [ userid:");
                sb.append(userid).append(", from:").append(startDate).append(", to:");
                sb.append(endDate).append(", isByDay:").append(isByDay).append(", now:")
                        .append(format.format(new Date())).append(" ]").append(", return ").append(vo.toString());
                _log.info(sb.toString());
            }
        }
        return vo;
    }

    private ApiTaxStatVo checkMultiUserParamters(Set<Long> userids, String startDate, String endDate) {
        ApiTaxStatVo vo = new ApiTaxStatVo(OneServiceConst.StatusCode.SUCCESSFUL, "");
        Date from = new Date();
        Date to = new Date();
        SimpleDateFormat format = getDateFormate();
        try {
            from = format.parse(startDate);
            to = format.parse(endDate);
            // if (includesToday(to)) { // not includes today
            // vo.setCode(OneServiceConst.StatusCode.FAILED);
            // vo.setErrmsg(OneServiceConst.ErrorCode.DATE_NO_TODAY);
            // } else
            if (from.getTime() > to.getTime()) {
                vo.setCode(OneServiceConst.StatusCode.FAILED);
                vo.setErrmsg(OneServiceConst.ErrorCode.DATE_RANGE_MIX);
            } else if (isOverRange(from, to, OneServiceConst.TimeRange.BATCH)) {
                vo.setCode(OneServiceConst.StatusCode.FAILED);
                vo.setErrmsg(OneServiceConst.ErrorCode.DATE_RANGE_BATCH);
            }
        } catch (ParseException e) {
            _log.info("wrong start date format, ", e);
            vo.setCode(OneServiceConst.StatusCode.FAILED);
            vo.setErrmsg(OneServiceConst.ErrorCode.DATE_FORMAT);
        }
        if (userids.size() > OneServiceConst.MAX_USERIDS_NUM) {
            vo.setCode(OneServiceConst.StatusCode.FAILED);
            vo.setErrmsg(OneServiceConst.ErrorCode.USERIDS_EXCEED);
        }
        if (userids.size() == 0) {
            vo.setCode(OneServiceConst.StatusCode.FAILED);
            vo.setErrmsg(OneServiceConst.ErrorCode.USERIDS_EMPTY);
        }

        if (OneServiceConst.StatusCode.FAILED == vo.getCode()) {
            if (_log.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder("date check failed, [ userid:");
                sb.append(userids).append(", from:").append(startDate).append(", to:");
                sb.append(endDate).append(", now:").append(format.format(new Date())).append(" ]").append(", return ")
                        .append(vo.toString());
                _log.info(sb.toString());
            }
        }
        return vo;
    }

    /**
     * @see OneService#getHourUserReport(Integer, String, String)
     */
    @Override
    public List<ReportHourViewItem> getHourUserReport(Integer userId, String fromDate, String toDate) {
        QueryParam qp = new QueryParam();
        SimpleDateFormat format = getDateFormate();
        try {
            Date from = format.parse(fromDate);
            Date to = format.parse(toDate);
            if (from.after(to)) {
                _log.warn(String.format("from date=%s after to date=%s", fromDate, toDate));
                return null;
            }
            if (DateUtil.getNumberOfDaysBetween(from, to) > MAX_HOUR_REPORT_QUERY_DAYS) {
                _log.warn(String.format("from=%s to=%s days > %s", fromDate, toDate, MAX_HOUR_REPORT_QUERY_DAYS));
                return null;
            }
            qp.setFrom(from);
            qp.setTo(to);
            qp.setTimeUnit(ReportConstants.TU_HOUR);
            qp.setUserId(userId);
        } catch (ParseException e) {
            _log.warn("wrong date format", e);
            return null;
        }
        return syncReportService.queryHourUserReport(qp).getItemList();
    }

}
