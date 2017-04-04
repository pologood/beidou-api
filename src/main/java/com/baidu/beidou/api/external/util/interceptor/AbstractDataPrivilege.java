package com.baidu.beidou.api.external.util.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ClassName: AbstractDataPrivilege
 * Function: TODO ADD FUNCTION
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public abstract class AbstractDataPrivilege implements MethodInterceptor {
	private static final Log LOG = LogFactory.getLog(AbstractDataPrivilege.class);
	protected String[] ognlExp = null;
	protected String[] positionExp = null; // /
											// 没有权限的表达式的位置，如果为ognlExp表达式对应的值为数组，则位置后还需要加上序号
	protected String[] idExp = null; // /
										// 前有权限的表达式的位置后缀，如果为ognlExp表达式对应的值为数组，则为positionExp[index]+"["+i+"]"+idExp[index]

	/**
	 * @return the ognlExp
	 */
	public String[] getOgnlExp() {
		return ognlExp;
	}

	/**
	 * @param ognlExp
	 *            the ognlExp to set
	 */
	public final void setOgnlExp(String[] ognlExp) {
		this.ognlExp = ognlExp;
		if (ognlExp != null) {
			this.positionExp = new String[ognlExp.length];
			this.idExp = new String[ognlExp.length];

			int index = 0;
			// 设置表达式的position
			for (String exp : ognlExp) {
				int occur = exp.indexOf(".{");
				if (occur >= 0) {
					// 包含xxx.{id},对应的position为xx[index].id, positionExp = xxx,
					// idExp = .id
					int endOccur = exp.indexOf('}');
					if (occur > 0 && endOccur > occur + 2) {
						this.positionExp[index] = exp.substring(0, occur);
						this.idExp[index] = exp.substring(occur + 2, endOccur);
					} else {
						// 异常情况
						LOG.error("无法处理的OGNL表达式：" + exp);
					}
				} else {
					this.positionExp[index] = exp;
				}
				++index;
			}
		}
	}

}
