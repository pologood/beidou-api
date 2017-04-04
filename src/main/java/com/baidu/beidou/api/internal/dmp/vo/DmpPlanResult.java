package com.baidu.beidou.api.internal.dmp.vo;

import java.util.List;

/**
 * 返回DMP 北斗计划列表
 * 
 * @author caichao
 * 
 */
public class DmpPlanResult {
    private int status;
    private String errorMsg;
    private List<PlanType> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<PlanType> getData() {
        return data;
    }

    public void setData(List<PlanType> data) {
        this.data = data;
    }
}
