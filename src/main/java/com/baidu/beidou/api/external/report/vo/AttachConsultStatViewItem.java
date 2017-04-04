package com.baidu.beidou.api.external.report.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * ClassName: AttachConsultStatViewItem <br>
 * Function: 附加信息-咨询 statViewItem
 *
 * @author wangxujin
 */
public class AttachConsultStatViewItem extends AbstractStatViewItem implements Serializable {

    private static final long serialVersionUID = 3740457736427445440L;

    public AttachConsultStatViewItem() {
        super();
    }

    private int userId;

    private int planId;

    private int groupId;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("day", day)
                .append("userid", userId)
                .append("username", userName)
                .append("planid", planId)
                .append("planname", planName)
                .append("groupid", groupId)
                .append("groupname", groupName)
                .append("srchs", srchs)
                .append("clks", clks)
                .append("cost", cost)
                .append("ctr", ctr)
                .append("acp", acp)
                .append("cpm", cpm)
                .toString();
    }
}
