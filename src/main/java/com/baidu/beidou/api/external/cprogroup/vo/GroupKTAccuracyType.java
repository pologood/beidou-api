package com.baidu.beidou.api.external.cprogroup.vo;

import java.io.Serializable;

/**
 * 优质流量API接口Model
 *
 * @author huangjinkun.
 * @date 16/2/19
 * @time 下午2:54
 */
public class GroupKTAccuracyType implements Serializable {


    private static final long serialVersionUID = 7841460413589772615L;

    private int groupId;
    private int ktaFlag;
    private int ktaBidRatio;

    /**
     * 优质流量Model
     */
    public GroupKTAccuracyType() {
        super();
    }

    /**
     * @param groupId     推广组ID
     * @param ktaFlag     优质流量开关标识位
     * @param ktaBidRatio 优质流量溢价倍数
     */
    public GroupKTAccuracyType(int groupId, int ktaFlag, int ktaBidRatio) {
        super();
        this.groupId = groupId;
        this.ktaFlag = ktaFlag;
        this.ktaBidRatio = ktaBidRatio;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getKtaFlag() {
        return ktaFlag;
    }

    public int getKtaBidRatio() {
        return ktaBidRatio;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setKtaFlag(int ktaFlag) {
        this.ktaFlag = ktaFlag;
    }

    public void setKtaBidRatio(int ktaBidRatio) {
        this.ktaBidRatio = ktaBidRatio;
    }
}
