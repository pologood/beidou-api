package com.baidu.beidou.api.external.report.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class InterestStatViewItem extends AbstractStatViewItem {

    private static final long serialVersionUID = 4578389666041020020L;

    private int userId;

    private int planId;

    private int groupId;

    private int interestId;

    private String interestName;

    /**
     * 兴趣类型：1是一级兴趣（或抽象人群属性），2是二级兴趣，3是兴趣组合
     */
    private int interestType;

    /**
     * 所属受众组合id，没有则为0
     */
    private long gpId = 0L;
    /**
     * 受众组合名称
     */
    private String packName;

    public InterestStatViewItem() {
        super();
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("day", day).append("userId", userId)
                .append("userName", userName).append("planId", planId).append("planName", planName)
                .append("groupId", groupId).append("groupName", groupName).append("gpId", gpId)
                .append("packName", packName).append("interestId", interestId).append("interestName", interestName)
                .append("interestType", interestType).append("srchs", srchs).append("clks", clks).append("cost", cost)
                .append("ctr", ctr).append("acp", acp).append("cpm", cpm).append("srchuv", srchuv)
                .append("clkuv", clkuv).append("srsur", srsur).append("cusur", cusur).append("cocur", cocur)
                .append("arrivalRate", arrivalRate).append("hopRate", hopRate).append("avgResTime", avgResTime)
                .append("directTrans", directTrans).append("indirectTrans", indirectTrans).toString();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getInterestId() {
        return interestId;
    }

    public void setInterestId(int interestId) {
        this.interestId = interestId;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public int getInterestType() {
        return interestType;
    }

    public void setInterestType(int interestType) {
        this.interestType = interestType;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public long getGpId() {
        return gpId;
    }

    public void setGpId(Long gpId) {
        if (gpId == null || gpId <= 0) {
            return;
        } else {
            this.gpId = gpId;
        }
    }
}
