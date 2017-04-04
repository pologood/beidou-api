package com.baidu.beidou.api.external.util.vo;

import java.io.Serializable;

/**
 * ClassName: ApiError
 * Function: api处理出错信息
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public class ApiError implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 错误码
	 */
	private int code;
	/**
	 * 错误描述
	 */
	private String message;
	/**
	 * 导致该错误的ognl表达式，表达式以wsdl中的元素名为索引
	 */
	private String position;

	/**
	 * 导致错误的输入
	 */
	private String content;

	public ApiError() {
		super();
	}

	public ApiError(int code, String message, String position, String content) {
		super();
		this.code = code;
		this.message = message;
		this.position = position;
		this.content = content;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
