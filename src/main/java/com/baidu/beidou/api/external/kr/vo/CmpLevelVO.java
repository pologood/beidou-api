package com.baidu.beidou.api.external.kr.vo;

import java.io.Serializable;

import com.baidu.beidou.cprogroup.constant.CproGroupConstant;

/**
 * @author zhuqian
 *
 */
public class CmpLevelVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -660721661650169277L;
	
	private Integer id;
	private Integer cmpLevel; 	//网站热度级别
	private String cmpName; 	//网站热度文字描述
	private Integer cmpSize;  	//[0,100]间的整数，用于显示网站热度条
	private Double rateCmp;
	private Double scoreCmp;
	
	public CmpLevelVO(Integer id, Integer cmpLevel){
		this.id = id;
		this.cmpLevel = cmpLevel;
		this.cmpName = CproGroupConstant.getCmpNameByLevel(cmpLevel);
		this.cmpSize = CproGroupConstant.getDefaultCmpSizeByLevel(cmpLevel);
	}
	
	public CmpLevelVO(Integer id, Integer cmpLevel, Integer cmpSize){
		this.id = id;
		this.cmpLevel = cmpLevel;
		this.cmpName = CproGroupConstant.getCmpNameByLevel(cmpLevel);
		this.cmpSize = cmpSize;
	}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the cmpLevel
	 */
	public Integer getCmpLevel() {
		return cmpLevel;
	}
	/**
	 * @param cmpLevel the cmpLevel to set
	 */
	public void setCmpLevel(Integer cmpLevel) {
		this.cmpLevel = cmpLevel;
	}
	/**
	 * @return the cmpSize
	 */
	public Integer getCmpSize() {
		return cmpSize;
	}
	/**
	 * @param cmpSize the cmpSize to set
	 */
	public void setCmpSize(Integer cmpSize) {
		this.cmpSize = cmpSize;
	}

	/**
	 * @return the cmpName
	 */
	public String getCmpName() {
		return cmpName;
	}

	/**
	 * @param cmpName the cmpName to set
	 */
	public void setCmpName(String cmpName) {
		this.cmpName = cmpName;
	}

	/**
	 * @return the rateCmp
	 */
	public Double getRateCmp() {
		return rateCmp;
	}

	/**
	 * @param rateCmp the rateCmp to set
	 */
	public void setRateCmp(Double rateCmp) {
		this.rateCmp = rateCmp;
	}

	/**
	 * @return the scoreCmp
	 */
	public Double getScoreCmp() {
		return scoreCmp;
	}

	/**
	 * @param scoreCmp the scoreCmp to set
	 */
	public void setScoreCmp(Double scoreCmp) {
		this.scoreCmp = scoreCmp;
	}
	
}
