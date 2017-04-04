package com.baidu.beidou.api.internal.fcindex.vo;

import java.io.Serializable;

public abstract class BaseVo implements Serializable {
    
    private static final long serialVersionUID = -7180748770717254743L;

    public BaseVo() {
    }
    
    public BaseVo(int code, String err) {
        this.code = code;
        this.errmsg = err;
    }

    int code = 1;

    String errmsg = "";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

}
