package com.baidu.beidou.api.external.util.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * ClassName: PlaceHolderResult  <br>
 * Function: API调用add/delete/set等类型接口时返回空result的类
 *
 * @author zhangxu
 * @date Aug 29, 2012
 */
public class PlaceHolderResult implements Serializable{

	private static final long serialVersionUID = 1129843732412354L;

	private int placeholder = 1;
	
	public static PlaceHolderResult[] createResult() {
		PlaceHolderResult[] result = new PlaceHolderResult[1];
		PlaceHolderResult r = new PlaceHolderResult();
		result[0] = r;
		return result;
	}

	public int getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(int placeholder) {
		this.placeholder = placeholder;
	}
	
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("placeholder",placeholder)
        .toString();
	}
	
}
