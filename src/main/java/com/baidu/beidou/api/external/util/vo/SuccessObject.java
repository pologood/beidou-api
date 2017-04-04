package com.baidu.beidou.api.external.util.vo;

/**
 * ClassName: SuccessObject
 * Function: 当请求无需返回参数时，需设置该对象返回给dr-api
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2012-1-9
 */
public class SuccessObject {
	private String response = "placeholder";
	private static SuccessObject data;
	
	public static SuccessObject getInstance() {
		if (data == null) {
			synchronized (SuccessObject.class) {
				if (data == null) {
					data = new SuccessObject();
				}
			}
		}
		
		return data;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		return true;
	}
}
