package com.baidu.beidou.api.external.report.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.report.constant.ReportWebConstants;

/**
 * 
 * ClassName: CtReportCsvVo <br>
 * Function: 用于写入文件报告的主题词VO
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 5, 2012
 */
public class CtReportCsvVo extends AbstractApiReportCsvVo<CtStatViewItem> {

    private static final long serialVersionUID = -1035255603639830367L;

    // id only
    protected String[] idOnlyHeader = ReportWebConstants.REPORT_HEADER_IDONLY
            .get(ReportWebConstants.REPORT_TYPE.KEYWORD_NORMAL);

    // 非id only
    protected String[] notIdOnlyHeader = ReportWebConstants.REPORT_HEADER_NOT_IDONLY
            .get(ReportWebConstants.REPORT_TYPE.KEYWORD_NORMAL);

    public CtReportCsvVo(boolean idOnly, int performanceData) {
        this.idOnly = idOnly;
        this.performanceData = performanceData;
        String[] tmpIdOnlyHeader = idOnlyHeader.clone();
        String[] tmpNotIdOnlyHeader = notIdOnlyHeader.clone();
        for (int i = 15; i >= 0; i--) {
            if (((performanceData >> i) & 1) != 1) {
                tmpIdOnlyHeader = (String[]) ArrayUtils.remove(tmpIdOnlyHeader, tmpIdOnlyHeader.length - (i + 1));
                tmpNotIdOnlyHeader =
                        (String[]) ArrayUtils.remove(tmpNotIdOnlyHeader, tmpNotIdOnlyHeader.length - (i + 1));
            }
        }
        this.idOnlyHeader = tmpIdOnlyHeader;
        this.notIdOnlyHeader = tmpNotIdOnlyHeader;
    }

    /**
     * 获取下载列表列表头
     *
     * @return 列表头
     */
    public String[] getCsvReportHeader() {
        if (this.idOnly) {
            return this.idOnlyHeader;
        } else {
            return this.notIdOnlyHeader;
        }
    }

    /**
     * 获取下载列表具体信息
     *
     * @return 列表详情信息
     */
    public List<String[]> getCsvReportDetail() {
        List<String[]> result = new ArrayList<String[]>(details.size());

        String[] detailStringArray;
        for (CtStatViewItem item : details) {
            if (idOnly) {
                detailStringArray = new String[idOnlyHeader.length];
                int i = 0;
                detailStringArray[i++] = item.getDay();
                detailStringArray[i++] = String.valueOf(item.getUserid());
                detailStringArray[i++] = String.valueOf(item.getPlanid());
                detailStringArray[i++] = String.valueOf(item.getGroupid());
                detailStringArray[i++] = String.valueOf(item.getKeywordId());
                appendPerformanceData(detailStringArray, i, item);
            } else {
                detailStringArray = new String[notIdOnlyHeader.length];
                int i = 0;
                detailStringArray[i++] = item.getDay();
                detailStringArray[i++] = String.valueOf(item.getUserid());
                detailStringArray[i++] = item.getUsername();
                detailStringArray[i++] = String.valueOf(item.getPlanid());
                detailStringArray[i++] = item.getPlanname();
                detailStringArray[i++] = String.valueOf(item.getGroupid());
                detailStringArray[i++] = item.getGroupname();
                detailStringArray[i++] = String.valueOf(item.getKeywordId());
                detailStringArray[i++] = item.getKeyword();
                appendPerformanceData(detailStringArray, i, item);
            }
            result.add(detailStringArray);
        }
        return result;
    }

}
