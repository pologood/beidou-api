package com.baidu.beidou.api.internal.audit.util;

/**
 * 标注类
 * @author work
 * @version 1.0.0
 */
public class UnitTag {
    private int confidenceLevel;    // 1 准确度
    private int beautyLevel;    // 2 美观度
    private int cheatLevel;    // 3 欺诈
    private int vulgarLevel;    // 4 低俗  
    private int dangerLevel;    // 5   高危
    
    /**
     * @return the confidenceLevel
     */
    public int getConfidenceLevel() {
        return confidenceLevel;
    }
    /**
     * @param confidenceLevel the confidenceLevel to set
     */
    public void setConfidenceLevel(int confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
    /**
     * @return the beautyLevel
     */
    public int getBeautyLevel() {
        return beautyLevel;
    }
    /**
     * @param beautyLevel the beautyLevel to set
     */
    public void setBeautyLevel(int beautyLevel) {
        this.beautyLevel = beautyLevel;
    }
    /**
     * @return the cheatLevel
     */
    public int getCheatLevel() {
        return cheatLevel;
    }
    /**
     * @param cheatLevel the cheatLevel to set
     */
    public void setCheatLevel(int cheatLevel) {
        this.cheatLevel = cheatLevel;
    }
    /**
     * @return the vulgarLevel
     */
    public int getVulgarLevel() {
        return vulgarLevel;
    }
    /**
     * @param vulgarLevel the vulgarLevel to set
     */
    public void setVulgarLevel(int vulgarLevel) {
        this.vulgarLevel = vulgarLevel;
    }
    /**
     * 
     * @return the dangerLevel
     */
    public int getDangerLevel() {
        return dangerLevel;
    }
    /**
     * 
     * @param dangerLevel the dangerLevel to set
     */
    public void setDangerLevel(int dangerLevel) {
        this.dangerLevel = dangerLevel;
    }
}
