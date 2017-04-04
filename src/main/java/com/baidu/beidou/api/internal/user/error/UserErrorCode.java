package com.baidu.beidou.api.internal.user.error;

/**
 * 用户的错误码
 *
 * @author zengyunfeng
 */
public enum UserErrorCode {
    NO_USER(-100, "用户不存在"),
    PARAM_ERROR(-200, "参数错误");

    private int value = 0;
    private String message = null;

    private UserErrorCode(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
