package com.baidu.beidou.api.external.report.output.impl;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.Ostermiller.util.ExcelCSVPrinter;
import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.baidu.beidou.api.external.report.output.ApiReportWriter;
import com.baidu.beidou.api.external.report.service.impl.ApiReportMgrImpl;
import com.baidu.beidou.api.external.report.vo.AbstractApiReportCsvVo;

/**
 * 
 * ClassName: ApiReportCSVWriter <br>
 * Function: 报告writer
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 5, 2012
 */
public class ApiReportCSVWriter implements ApiReportWriter {

    private static final Log log = LogFactory.getLog(ApiReportMgrImpl.class);

    private static final String encoding = ReportWebConstants.REPORT_CSV_ENCODING;

    private ApiReportCSVWriter() {
        super();
    }

    private static final ApiReportWriter writer = new ApiReportCSVWriter();

    public static ApiReportWriter getInstance() {
        return writer;
    }

    public void write(AbstractApiReportCsvVo<?> report, String file) throws IOException {
        if (report == null) {
            return;
        }
        ExcelCSVPrinter printer = null;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
            printer = new ExcelCSVPrinter(writer);
            printer.changeDelimiter(ReportWebConstants.REPORT_CSV_SEPERATOR); // 设置csv文件的分隔符
            // 输出表头
            printer.writeln(report.getCsvReportHeader());

            // 输出详情
            for (String[] detail : report.getCsvReportDetail()) {
                printer.writeln(detail);
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (printer != null) {
                    printer.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
