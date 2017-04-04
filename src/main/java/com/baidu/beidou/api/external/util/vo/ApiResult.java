package com.baidu.beidou.api.external.util.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONUtil;

import com.baidu.beidou.api.external.util.constant.ApiConstant;

/**
 * ClassName: ApiResult
 * Function: api处理出错信息，整个错误集合
 *
 * @version 2.0.0
 * @since cpweb357
 * @since TODO
 * @date 2011-12-20
 */
public class ApiResult<T> implements Serializable {
	
	private static final Log LOG = LogFactory.getLog(ApiResult.class);

	private static final long serialVersionUID = 1L;

	private List<ApiError> errors;

	private List<T> data;

	private Map<String, Integer> options;

	/**
	 * @return the errors
	 */
	public List<ApiError> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(List<ApiError> errors) {
		this.errors = errors;
	}

	/**
	 * @return the data
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<T> data) {
		this.data = data;
	}

	/**
	 * 获取配额
	 */
	public PaymentResult getPayment() {
		if(this.options == null){
			options = new HashMap<String, Integer>();
			options.put(ApiConstant.PAYMENT_TOTAL, 0);
			options.put(ApiConstant.PAYMENT_SUCCESS, 0);
		}
		PaymentResult paymentResult = new PaymentResult();
		paymentResult.setRef(this.options);
		return paymentResult;
	}

	/**
	 * 必须在方法结束后再调用该方法设置配额
	 */
	public void setPayment(PaymentResult payment) {
		this.options = payment.getRef();
	}
	
	public Map<String, Integer> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Integer> options) {
		this.options = options;
	}
	
	public boolean hasErrors() {
		if (CollectionUtils.isNotEmpty(this.errors)) {
			return true;
		}
		
		return false;
	}

	public String toString(){
		try{
			StringBuffer sb = new StringBuffer();
			sb.append(ApiConstant.TOSTRING_DATA + JSONUtil.serialize(data)).append("\t");
			sb.append(ApiConstant.TOSTRING_ERRORS + JSONUtil.serialize(errors)).append("\t");
			sb.append(ApiConstant.TOSTRING_PAYMENT + JSONUtil.serialize(options));
			return sb.toString();
		} catch (Exception e){
			LOG.error("toString failed of ApiResult",e);
			return "";
		}
	}

}
