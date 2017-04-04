package com.baidu.beidou.api.external.report.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * ClassName: AttachMessageStatViewItem <br>
 * Function: 附加信息-短息 statViewItem
 *
 * @author wangxujin
 */
public class AttachMessageStatViewItem extends AbstractStatViewItem implements Serializable {

    private static final long serialVersionUID = 8631620974436212737L;

    public AttachMessageStatViewItem() {
        super();
    }

    private int userId;

    private int planId;

    private int groupId;

    private long messageId;

    private String message;

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

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
                .append("messageId", messageId)
                .append("message", message)
                .append("srchs", srchs)
                .append("clks", clks)
                .append("cost", cost)
                .append("ctr", ctr)
                .append("acp", acp)
                .append("cpm", cpm)
                .toString();
    }
}
