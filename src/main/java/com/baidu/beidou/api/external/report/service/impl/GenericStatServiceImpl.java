package com.baidu.beidou.api.external.report.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
import com.baidu.beidou.util.DateUtils;
import com.baidu.beidou.util.SessionHolder;
import com.baidu.unbiz.soma.module.reportmodule.report.common.vo.QueryParam;
import com.baidu.unbiz.soma.module.reportmodule.report.constant.ReportConstants;
import com.google.common.base.Preconditions;

/**
 * 
 * ClassName: GenericStatServiceImpl <br>
 * Function: 查询统计数据的泛型类，包含虚函数queryStat，所有报告需要重写 <br>
 * 
 * Note：该类包含来一些参数，因此为了避免线程安全问题，在spring中必须加入prototype多例配置。
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 30, 2011
 */
public abstract class GenericStatServiceImpl<T extends Serializable> extends StatServiceSupport implements
        GenericStatService<T> {
    
    private static final Log log = LogFactory.getLog(GenericStatServiceImpl.class);
    
    public abstract List<T> queryStat(ApiReportQueryParameter request) throws Exception;

    protected FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMdd");

    protected final String dataSizeTooLarge = this.getClass().getSimpleName() + " : request data size too large";
    //
    // protected final String emptyUserInfo = this.getClass().getSimpleName()
    // + " got empty user info result from db";

    int userId;

    Date from;

    Date to;

    List<Integer> planIds;

    List<Integer> groupIds;

    List<Long> unitIds;

    List<Integer> keywordIds;

    boolean idOnly;

    protected void initParameters(ApiReportQueryParameter request) {
        this.userId = request.getUserid();
        this.from = request.getStartDate();
        this.to = request.getEndDate();
        this.planIds = request.getPlanIds();
        this.groupIds = request.getGroupIds();
        this.unitIds = request.getUnitIds();
        this.idOnly = request.isIdOnly();
        SessionHolder.putUserId(userId);
    }

    /**
     * 计算size大小是否满足
     */
    protected boolean isSizeOk(Date from, Date to, Integer groupSize) {
        return DateUtils.getBetweenDate(from, to) * groupSize < size ? true : false;
    }
    
    /**
     * 为实时报表服务 <code>ReportService<code>构造请求对象
     * {@link com.baidu.unbiz.soma.module.reportmodule.skeleton.vo.QueryParam QueryParam}
     * 
     * @param apiQueryParam
     * @return QueryParam
     */
    public static QueryParam buildQueryParam(ApiReportQueryParameter apiQueryParam) {
        Preconditions.checkNotNull(apiQueryParam);
  
        QueryParam result = QueryParam.newInstance();
        result.setFrom(apiQueryParam.getStartDate());
        result.setTo(apiQueryParam.getEndDate());
        result.setUserId(apiQueryParam.getUserid());
        result.setPlanIds(apiQueryParam.getPlanIds());
        result.setGroupIds(apiQueryParam.getGroupIds());
        result.setUnitIds(apiQueryParam.getUnitIds());
        result.setTimeUnit(ReportConstants.TU_DAY);

        log.info("QueryParam=" + result);
        return result;
    }

}
