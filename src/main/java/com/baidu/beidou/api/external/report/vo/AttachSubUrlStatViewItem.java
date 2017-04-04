package com.baidu.beidou.api.external.report.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * ClassName: AttachSubUrlStatViewItem <br>
 * Function: 附加信息-子链 statViewItem
 *
 * @author wangxujin
 */
public class AttachSubUrlStatViewItem extends AbstractStatViewItem implements Serializable {

    private static final long serialVersionUID = 3222591061844129647L;

    private int userId;

    private int planId;

    private int groupId;

    private String subUrlTitle; // 子链

    public AttachSubUrlStatViewItem() {
        super();
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

    public String getSubUrlTitle() {
        return subUrlTitle;
    }

    public void setSubUrlTitle(String subUrlTitle) {
        this.subUrlTitle = subUrlTitle;
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
                .append("suburltitle", subUrlTitle)
                .append("srchs", srchs)
                .append("clks", clks)
                .append("cost", cost)
                .append("ctr", ctr)
                .append("acp", acp)
                .append("cpm", cpm)
                .toString();
    }
}
