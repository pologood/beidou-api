package com.baidu.beidou.api.external.report.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: AccountStatViewItem <br>
 * Function: 从doris查询出来的账户数据VO
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 6, 2012
 */
public class AccountStatViewItem extends AbstractStatViewItem implements Serializable {

    private static final long serialVersionUID = 3024228077959244219L;

    public AccountStatViewItem() {
        super();
    }

    private int userid;

    private String username;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AccountStatViewItem))
            return false;
        final AccountStatViewItem other = (AccountStatViewItem) obj;
        // if (getUserid() != other.getUserid())
        // return false;
        // if (!getUsername().equals(other.getUsername()))
        // return false;
        if (!getDay().equals(other.getDay()))
            return false;
        return true;
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(day)
        // .append(userid)
        // .append(username)
                .toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("day", day).append("userid", userid)
                .append("username", username).append("srchs", srchs).append("clks", clks).append("cost", cost)
                .append("ctr", ctr).append("acp", acp).append("cpm", cpm).append("srchuv", srchuv)
                .append("clkuv", clkuv).append("srsur", srsur).append("cusur", cusur).append("cocur", cocur)
                .append("arrivalRate", arrivalRate).append("hopRate", hopRate).append("avgResTime", avgResTime)
                .append("directTrans", directTrans).append("indirectTrans", indirectTrans).toString();
    }

}
