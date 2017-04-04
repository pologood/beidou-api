package com.baidu.beidou.api.internal.user.vo;

/**
 * ClassName: UserServResult <br/>
 * Function: UserService结果
 * 
 * @author zhangxu04
 */
public class UserServResult<T> {

    public static final int OK = 0;

    public static final int RPC_FAIL = -1;

    /**
     * 数据
     */
    private T data;

    /**
     * 错误码
     */
    private int errCode = OK;

    /**
     * 错误消息
     */
    private String errMsg;

    /**
     * 
     * Creates a new instance of UserServResult.
     *
     */
    private UserServResult() {

    }

    /**
     * build empty result
     * 
     * @return
     */
    public static <T> UserServResult<T> create() {
        return new UserServResult<T>();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

}
