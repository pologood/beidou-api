package com.baidu.beidou.api.external.tool.service;

import java.io.Serializable;

import com.baidu.beidou.api.external.report.service.GenericStatService;
import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;
/**
 * 
 * ClassName: GenericToolStatService
 * Function: 报告服务接口
 *
 * @author caichao
 * @date 2013-09-12
 */
public interface GenericToolStatService<T extends Serializable> extends GenericStatService<T> {
   public int querySumBudget(ApiReportQueryParameter request);
}
