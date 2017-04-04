package com.baidu.beidou.api.external.report.service;

import java.io.Serializable;
import java.util.List;

import com.baidu.beidou.api.external.report.vo.ApiReportQueryParameter;

/**
 * 
 * InterfaceName: GenericStatService <br>
 * Function: 查询报告数据接口定义
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Dec 30, 2011
 */
public interface GenericStatService<T extends Serializable> {

    public List<T> queryStat(ApiReportQueryParameter request) throws Exception;

}
