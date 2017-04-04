package com.baidu.beidou.api.internal.fcindex.service;

import java.util.List;
import java.util.Set;

import com.baidu.beidou.api.internal.fcindex.vo.ApiTaxStatVo;
import com.baidu.beidou.api.internal.fcindex.vo.BatchStatVo;
import com.baidu.beidou.api.internal.fcindex.vo.BudgetVo;
import com.baidu.beidou.api.internal.fcindex.vo.StatVo;
import com.baidu.unbiz.soma.module.reportmodule.olap.vo.ReportHourViewItem;

public interface OneService {
    /**
     * Date is in [startDate, endDate]
     * 
     * @param userid
     * @param startDate "yyyy-MM-dd"
     * @param endDate "yyyy-MM-dd"
     * @param isDay 0=all in one, 1=by day
     * @return
     */
    public StatVo getBeidouData(Long userid, String startDate, String endDate, int isDay);

    /**
     * 
     * @param userid
     * @return
     */
    public BudgetVo getBeidouBasicInfo(Long userid);

    /**
     * Date is in [startDate, endDate]
     * 
     * @param userid
     * @param startDate "yyyy-MM-dd"
     * @param endDate "yyyy-MM-dd"
     * @return
     */
    public BatchStatVo getAllBeidouData(Set<Long> userid, String startDate, String endDate);
    
    /**
     * 查询北斗分用户税费数据
     * @param userid
     * @param startDate
     * @param endDate
     * @param isByDay
     * @return
     * 2013-9-16 下午9:41:04 created by wangchongjie
     */
    public ApiTaxStatVo getBeidouTaxData(Long userid, String startDate, String endDate, int isByDay);
    
    /**
     * 查询北斗多用户税费数据
     * @param userid
     * @param startDate
     * @param endDate
     * @return
     * 2013-9-16 下午9:42:14 created by wangchongjie
     */
    public ApiTaxStatVo getAllBeidouTaxData(Set<Long> userid, String startDate, String endDate);

    /**
     * 查询小时粒度用户报表数据
     * @param userId 用户id
     * @param fromDate 起始日期
     * @param toDate 结束日期
     * @return 小时粒度用户报表数据
     */
    public List<ReportHourViewItem> getHourUserReport(Integer userId, String fromDate, String toDate);
}
