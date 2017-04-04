package com.baidu.beidou.api.external.report.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.baidu.beidou.api.external.report.constant.ReportWebConstants;

/**
 * ClassName: InterestShownReportCsvVo Function: 用于写入文件报告的受众组合VO
 *
 * @author genglei
 * @version beidou-api 3 plus
 * @date 2012-9-25
 */
public class InterestShownReportCsvVo extends AbstractApiReportCsvVo<InterestStatViewItem> {

    private static final long serialVersionUID = -4137724449359650425L;

    // id only
    protected String[] idOnlyHeader = ReportWebConstants.REPORT_HEADER_IDONLY
            .get(ReportWebConstants.REPORT_TYPE.INTEREST_SHOWN);

    // 非id only
    protected String[] notIdOnlyHeader = ReportWebConstants.REPORT_HEADER_NOT_IDONLY
            .get(ReportWebConstants.REPORT_TYPE.INTEREST_SHOWN);

    public InterestShownReportCsvVo(boolean idOnly, int performanceData) {
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
        for (InterestStatViewItem item : details) {
            if (idOnly) {
                detailStringArray = new String[idOnlyHeader.length];
                int i = 0;
                detailStringArray[i++] = item.getDay();
                detailStringArray[i++] = String.valueOf(item.getUserId());
                detailStringArray[i++] = String.valueOf(item.getPlanId());
                detailStringArray[i++] = String.valueOf(item.getGroupId());
                detailStringArray[i++] = String.valueOf(item.getGpId());
                detailStringArray[i++] = String.valueOf(item.getInterestId());
                detailStringArray[i++] = String.valueOf(item.getInterestType());
                detailStringArray[i++] = item.getPackName();
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
                detailStringArray[i++] = String.valueOf(item.getGpId());
                detailStringArray[i++] = item.getPackName();
                detailStringArray[i++] = String.valueOf(item.getInterestId());
                detailStringArray[i++] = item.getInterestName();
                detailStringArray[i++] = String.valueOf(item.getInterestType());

                appendPerformanceData(detailStringArray, i, item);
            }
            result.add(detailStringArray);
        }
        return result;
    }

}
