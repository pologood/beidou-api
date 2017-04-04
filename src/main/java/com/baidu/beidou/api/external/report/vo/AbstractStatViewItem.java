package com.baidu.beidou.api.external.report.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.baidu.unbiz.soma.module.reportmodule.report.vo.StatInfo;


/**
 * 
 * ClassName: AbstractStatViewItem <br>
 * Function: 从doris查询出来的数据基类
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 6, 2012
 */
public class AbstractStatViewItem extends StatInfo {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2261005122609637463L;
    protected String day;

    protected String userName;

    protected String groupName;

    protected String planName;

    public AbstractStatViewItem() {
        super();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
