package com.baidu.beidou.api.external.fc.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: FCUnitType  <br>
 * Function: FC推广单元
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Apr 9, 2012
 */
public class FCUnitType  implements Serializable{
	
	private static final long serialVersionUID = 5337279224211L;

	private long unitId;
	
	private String unitName;

	public long getUnitId() {
		return unitId;
	}

	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("unitId",unitId)
		.append("unitName", unitName)
        .toString();
	}
	
}
