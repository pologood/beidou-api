package com.baidu.beidou.api.internal.dmp.vo;

import java.util.Date;

/**
 * 接口返回bean
 * 
 * @author caichao
 * 
 */
public class PlanType {
    private Integer planId;
    private String name;
    private int planState;
    private Date addTime;

    public PlanType() {
    }

    public PlanType(Integer planId, String name, int planState, Date addTime) {
        this.planId = planId;
        this.name = name;
        this.planState = planState;
        this.addTime = addTime;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlanState() {
        return planState;
    }

    public void setPlanState(int planState) {
        this.planState = planState;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
