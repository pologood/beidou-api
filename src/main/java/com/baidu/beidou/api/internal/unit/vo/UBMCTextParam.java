/**
 * CreativeUBMCParam.java 
 */
package com.baidu.beidou.api.internal.unit.vo;

import java.io.Serializable;

/**
 *
 * @author lixukun
 * @date 2014-03-19
 */
public class UBMCTextParam implements Serializable {
	private static final long serialVersionUID = -7749588553801466750L;
	
	private long creativeId;
	private long mcId;
	private int mcVersionId;
	/**
	 * @return the creativeId
	 */
	public long getCreativeId() {
		return creativeId;
	}
	/**
	 * @param creativeId the creativeId to set
	 */
	public void setCreativeId(long creativeId) {
		this.creativeId = creativeId;
	}
	/**
	 * @return the mcId
	 */
	public long getMcId() {
		return mcId;
	}
	/**
	 * @param mcId the mcId to set
	 */
	public void setMcId(long mcId) {
		this.mcId = mcId;
	}
	/**
	 * @return the mcVersion
	 */
	public int getMcVersionId() {
		return mcVersionId;
	}
	/**
	 * @param mcVersion the mcVersion to set
	 */
	public void setMcVersionId(int mcVersionId) {
		this.mcVersionId = mcVersionId;
	}
	
}
