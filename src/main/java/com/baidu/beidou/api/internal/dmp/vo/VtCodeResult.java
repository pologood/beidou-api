package com.baidu.beidou.api.internal.dmp.vo;

import java.util.Date;

/**
 * VtCode对应Vo
 * 
 * @author wangyu45
 * 
 */
public class VtCodeResult {
    private Long jsid;
    private String name;
    private String sign;
    private Integer userId;
    private Date addTime;
    private Date modTime;
    private Integer addUser;
    private Integer modUser;
    private Integer isAllSite;
    
    public Long getJsid() {
        return jsid;
    }
    public void setJsid(Long jsid) {
        this.jsid = jsid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Date getAddTime() {
        return addTime;
    }
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    public Date getModTime() {
        return modTime;
    }
    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }
    public Integer getAddUser() {
        return addUser;
    }
    public void setAddUser(Integer addUser) {
        this.addUser = addUser;
    }
    public Integer getModUser() {
        return modUser;
    }
    public void setModUser(Integer modUser) {
        this.modUser = modUser;
    }
    public Integer getIsAllSite() {
        return isAllSite;
    }
    public void setIsAllSite(Integer isAllSite) {
        this.isAllSite = isAllSite;
    }
}
