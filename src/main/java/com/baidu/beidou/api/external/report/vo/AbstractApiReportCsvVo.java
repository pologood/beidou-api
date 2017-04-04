package com.baidu.beidou.api.external.report.vo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 
 * ClassName: AbstractApiReportCsvVo <br>
 * Function: 用于写入文件报告的VO基类
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 5, 2012
 */
public abstract class AbstractApiReportCsvVo<T extends AbstractStatViewItem> implements Serializable {

    private static final long serialVersionUID = 1108084878251617145L;

    protected int performanceData;

    protected List<T> details;

    protected DecimalFormat df6 = new DecimalFormat("#.######");// 四位小数
    protected DecimalFormat df4 = new DecimalFormat("#.####");// 四位小数
    protected DecimalFormat df2 = new DecimalFormat("#.##");// 两位小数

    protected boolean idOnly;

    /**
     * 获取下载列表列表头
     *
     * @return 列表头
     */
    public abstract String[] getCsvReportHeader();

    /**
     * 获取下载列表具体信息
     *
     * @return 列表详情信息
     */
    public abstract List<String[]> getCsvReportDetail();

    public void appendPerformanceData(String[] detailStringArray, int i, AbstractStatViewItem item) {
        if (((performanceData >> 15) & 1) == 1) {
            detailStringArray[i++] = String.valueOf(item.getSrchs());
        }
        if (((performanceData >> 14) & 1) == 1) {
            detailStringArray[i++] = String.valueOf(item.getClks());
        }
        if (((performanceData >> 13) & 1) == 1) {
            detailStringArray[i++] = df2.format(item.getCost());
        }
        if (((performanceData >> 12) & 1) == 1) {
            detailStringArray[i++] = df6.format(item.getCtr());
        }
        if (((performanceData >> 11) & 1) == 1) {
            detailStringArray[i++] = df2.format(item.getCpm());
        }
        if (((performanceData >> 10) & 1) == 1) {
            detailStringArray[i++] = df2.format(item.getAcp());
        }
        if (((performanceData >> 9) & 1) == 1) {
            detailStringArray[i++] = String.valueOf(item.getSrchuv());
        }
        if (((performanceData >> 8) & 1) == 1) {
            detailStringArray[i++] = String.valueOf(item.getClkuv());
        }
        if (((performanceData >> 7) & 1) == 1) {
            detailStringArray[i++] = df2.format(item.getSrsur());
        }
        if (((performanceData >> 6) & 1) == 1) {
            detailStringArray[i++] = df6.format(item.getCusur());
        }
        if (((performanceData >> 5) & 1) == 1) {
            detailStringArray[i++] = df4.format(item.getCocur());
        }
        if (((performanceData >> 4) & 1) == 1) {
            detailStringArray[i++] = df6.format(item.getArrivalRate());
        }
        if (((performanceData >> 3) & 1) == 1) {
            detailStringArray[i++] = df6.format(item.getHopRate());
        }
        if (((performanceData >> 2) & 1) == 1) {
            detailStringArray[i++] = df2.format(item.getAvgResTime());
        }
        if (((performanceData >> 1) & 1) == 1) {
            detailStringArray[i++] = String.valueOf(item.getDirectTrans());
        }
        if (((performanceData >> 0) & 1) == 1) {
            detailStringArray[i++] = String.valueOf(item.getIndirectTrans());
        }
    }

    public boolean isIdOnly() {
        return idOnly;
    }

    public void setIdOnly(boolean idOnly) {
        this.idOnly = idOnly;
    }

    public void setDetails(List<T> details) {
        this.details = details;
    }

    public int getPerformanceData() {
        return performanceData;
    }

    public void setPerformanceData(int performanceData) {
        this.performanceData = performanceData;
    }

}
