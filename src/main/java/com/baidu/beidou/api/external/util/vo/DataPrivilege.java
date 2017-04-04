package com.baidu.beidou.api.external.util.vo;

import java.io.Serializable;

/**
 * ClassName: DataPrivilege
 * Function: 用户请求，包含操作用户以及被操作用户
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public class DataPrivilege implements Serializable {

	private static final long serialVersionUID = 1L;
	private int opUser; // 操作人的id, 不可为空；
	private int dataUser; // 被操作人的id，不可为空.

	/**
	 * @return the opUser
	 */
	public int getOpUser() {
		return opUser;
	}

	/**
	 * @param opUser
	 *            the opUser to set
	 */
	public void setOpUser(int opUser) {
		this.opUser = opUser;
	}

	/**
	 * @return the dataUser
	 */
	public int getDataUser() {
		return dataUser;
	}

	/**
	 * @param dataUser
	 *            the dataUser to set
	 */
	public void setDataUser(int dataUser) {
		this.dataUser = dataUser;
	}

	/**
	 * toString
	 * 
	 * @return String
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@").append(
				Integer.toHexString(hashCode())).append(" [");
		buffer.append("opUser").append("='").append(getOpUser()).append("' ");
		buffer.append("dataUser").append("='").append(getDataUser()).append(
				"' ");
		buffer.append("]");

		return buffer.toString();
	}

}
