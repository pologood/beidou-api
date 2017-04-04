package com.baidu.beidou.api.external.report.vo;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.report.constant.ReportWebConstants;
import com.google.common.collect.Lists;

/**
 * ClassName: AttachConsultReportCsvVo <br>
 * Function: 附加信息-咨询 csv vo
 *
 * @author wangxujin
 */
public class AttachConsultReportCsvVo extends AbstractApiReportCsvVo<AttachConsultStatViewItem> {

    // id only
    protected String[] idOnlyHeader = ReportWebConstants.REPORT_HEADER_IDONLY
            .get(ReportWebConstants.REPORT_TYPE.ATTACH_CONSULT);

    // 非id only
    protected String[] notIdOnlyHeader = ReportWebConstants.REPORT_HEADER_NOT_IDONLY
            .get(ReportWebConstants.REPORT_TYPE.ATTACH_CONSULT);

    public AttachConsultReportCsvVo(boolean idOnly, int performanceData) {
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

    @Override
    public List<String[]> getCsvReportDetail() {
        List<String[]> result = Lists.newArrayListWithCapacity(details.size());

        String[] detailStringArray;
        for (AttachConsultStatViewItem item : details) {
            if (idOnly) {
                detailStringArray = new String[idOnlyHeader.length];
                int i = 0;
                detailStringArray[i++] = item.getDay();
                detailStringArray[i++] = String.valueOf(item.getUserId());
                detailStringArray[i++] = String.valueOf(item.getPlanId());
                detailStringArray[i++] = String.valueOf(item.getGroupId());
                appendPerformanceData(detailStringArray, i, item);
            } else {
                detailStringArray = new String[notIdOnlyHeader.length];
                int i = 0;
                detailStringArray[i++] = item.getDay();
                detailStringArray[i++] = String.valueOf(item.getUserId());
                detailStringArray[i++] = item.getUserName();
                detailStringArray[i++] = String.valueOf(item.getPlanId());
                detailStringArray[i++] = item.getPlanName();
                detailStringArray[i++] = String.valueOf(item.getGroupId());
                detailStringArray[i++] = item.getGroupName();
                appendPerformanceData(detailStringArray, i, item);
            }
            result.add(detailStringArray);
        }
        return result;
    }

    @Override
    public String[] getCsvReportHeader() {
        if (idOnly) {
            return idOnlyHeader;
        } else {
            return notIdOnlyHeader;
        }
    }
}
