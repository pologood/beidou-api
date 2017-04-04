package com.baidu.beidou.api.external.report.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * ClassName: PackStatViewItem Function: 受众组合报表数据项
 *
 * @author genglei
 * @version beidou-api 3 plus
 * @date 2012-9-21
 */
public class PackStatViewItem extends AbstractStatViewItem {

    private static final long serialVersionUID = -57793738175560699L;

    private int userId;

    private int planId;

    private int groupId;

    /**
     * 关联关系ID，gpId唯一指定了某一推广组与某一受众组合的关系
     */
    private Long gpId;

    /**
     * 受众组合名称
     */
    private String packName;

    public PackStatViewItem() {
        super();
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("day", day).append("userId", userId)
                .append("userName", userName).append("planId", planId).append("planName", planName)
                .append("groupId", groupId).append("groupName", groupName).append("gpId", gpId)
                .append("packName", packName).append("srchs", srchs).append("clks", clks).append("cost", cost)
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

    public Long getGpId() {
        return gpId;
    }

    public void setGpId(Long gpId) {
        this.gpId = gpId;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }
}
