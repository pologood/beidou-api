package com.baidu.beidou.api.external.report.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * ClassName: AttachPhoneStatViewItem <br>
 * Function: 附件信息-电话 statViewItem
 *
 * @author wangxujin
 */
public class AttachPhoneStatViewItem extends AbstractStatViewItem implements Serializable {

    private static final long serialVersionUID = 4773036101756740589L;

    public AttachPhoneStatViewItem() {
        super();
    }

    private int userId;

    private int planId;

    private int groupId;

    private long phoneId;

    private String phone;

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

    public long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(long phoneId) {
        this.phoneId = phoneId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
                .append("phoneId", phoneId)
                .append("phone", phone)
                .append("srchs", srchs)
                .append("clks", clks)
                .append("cost", cost)
                .append("ctr", ctr)
                .append("acp", acp)
                .append("cpm", cpm)
                .toString();
    }
}
