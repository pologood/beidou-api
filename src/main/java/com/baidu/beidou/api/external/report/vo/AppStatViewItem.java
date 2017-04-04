package com.baidu.beidou.api.external.report.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: DeviceStatViewItem <br>
 * Function: 从doris查询出来的有展现应用推广计划数据VO
 *
 * @author caichao
 * @version 2.0.0
 * @since cpweb670
 * @date 11 20, 2013
 */
public class AppStatViewItem extends AbstractStatViewItem implements Serializable {

    private static final long serialVersionUID = 3021223077159647189L;

    public AppStatViewItem() {
        super();
    }

    private int userId;

    private int planId;

    private int groupId;

    private Long appId;

    private String appName;

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("day", day).append("userid", userId)
                .append("username", userName).append("planid", planId).append("planname", planName)
                .append("groupid", groupId).append("groupname", groupName).append("appid", appId)
                .append("srchs", srchs).append("clks", clks).append("cost", cost).append("ctr", ctr).append("acp", acp)
                .append("cpm", cpm).append("srchuv", srchuv).append("clkuv", clkuv).append("srsur", srsur)
                .append("cusur", cusur).append("cocur", cocur).append("arrivalRate", arrivalRate)
                .append("hopRate", hopRate).append("avgResTime", avgResTime).append("directTrans", directTrans)
                .append("indirectTrans", indirectTrans).toString();
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

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}
