package com.baidu.beidou.api.external.report.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: RegionStatViewItem <br>
 * Function: 从doris查询出来的性别数据VO
 *
 * @author zhangxu
 * @date Sep 25, 2012
 */
public class RegionStatViewItem extends AbstractStatViewItem implements Serializable {

    private static final long serialVersionUID = 1001029050130638071L;

    private int userId;

    private int planId;

    private int groupId;

    private int regionId;

    private String regionName;

    /** 地域类型（前端显示）：1是一级地域，2是二级地域 */
    private int type = 1;

    /** 如果是二级地域，此字段不为0，并且为其一级地域节点ID */
    private int firstRegionId;

    /**
     * 二级地域数
     */
    private int childCount;

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

    public int getFirstRegionId() {
        return firstRegionId;
    }

    public void setFirstRegionId(int firstRegionId) {
        this.firstRegionId = firstRegionId;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("day", day).append("userid", userId)
                .append("username", userName).append("planid", planId).append("planname", planName)
                .append("groupid", groupId).append("groupname", groupName).append("regionId", regionId)
                .append("regionName", regionName).append("type", type).append("childCount", childCount)
                .append("srchs", srchs).append("clks", clks).append("cost", cost).append("ctr", ctr).append("acp", acp)
                .append("cpm", cpm).append("srchuv", srchuv).append("clkuv", clkuv).append("srsur", srsur)
                .append("cusur", cusur).append("cocur", cocur).append("arrivalRate", arrivalRate)
                .append("hopRate", hopRate).append("avgResTime", avgResTime).append("directTrans", directTrans)
                .append("indirectTrans", indirectTrans).toString();
    }

}
