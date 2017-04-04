package com.baidu.beidou.api.external.report.vo;

import java.io.Serializable;

/**
 * 
 * ClassName: ShownSiteStatViewItem <br>
 * Function: 从doris查询出来的有展现网站数据VO
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 6, 2012
 */
public class ShownSiteStatViewItem extends AbstractStatViewItem implements Serializable {

    private static final long serialVersionUID = 1001029090120630071L;

    private int userId;

    private int planId;

    private int groupId;

    private String siteUrl;
    // FIXME 装的下?
    private int siteId;

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

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

}
