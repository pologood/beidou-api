package com.baidu.beidou.api.external.report.output;

import java.io.IOException;

import com.baidu.beidou.api.external.report.vo.AbstractApiReportCsvVo;

/**
 * 
 * InterfaceName: ApiReportWriter <br>
 * Function: 报告写入接口
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 5, 2012
 */
public interface ApiReportWriter {

    public void write(AbstractApiReportCsvVo<?> report, String file) throws IOException;

}
