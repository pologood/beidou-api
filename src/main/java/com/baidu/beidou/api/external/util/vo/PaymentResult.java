package com.baidu.beidou.api.external.util.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONUtil;

import com.baidu.beidou.api.external.util.constant.ApiConstant;

/**
 * ClassName: PaymentResult <br>
 * Function: 处理结果总信息，包含请求中包含处理的总次数以及成功的次数 <br>
 * DR-API叫做quota
 * 
 * @author genglei
 * @version 
 * @since TODO
 * @date 2011-12-20
 */
public class PaymentResult implements Serializable {
	
	private static final Log LOG = LogFactory.getLog(PaymentResult.class);

	private static final long serialVersionUID = -2461698156464230577L;
	
	private Map<String, Integer> ref = new HashMap<String, Integer>();
	
	{
		ref.put(ApiConstant.PAYMENT_TOTAL, 0);
		ref.put(ApiConstant.PAYMENT_SUCCESS, 0);
	}

	public Integer getTotal() {
		return ref.get(ApiConstant.PAYMENT_TOTAL);
	}

	public void setTotal(Integer total) {
		ref.put(ApiConstant.PAYMENT_TOTAL, total);
	}
	
	public void increTotal(Integer by) {
		int total = ref.get(ApiConstant.PAYMENT_TOTAL);
		ref.put(ApiConstant.PAYMENT_TOTAL, total + by); 
	}
	
	public void descrTotal(Integer by) {
		int total = ref.get(ApiConstant.PAYMENT_TOTAL);
		ref.put(ApiConstant.PAYMENT_TOTAL, total - by); 
	}

	public Integer getSuccess() {
		return ref.get(ApiConstant.PAYMENT_SUCCESS);
	}

	public void setSuccess(Integer success) {
		ref.put(ApiConstant.PAYMENT_SUCCESS, success);
	}
	
	public void increSuccess(){
		int succ = ref.get(ApiConstant.PAYMENT_SUCCESS);
		ref.put(ApiConstant.PAYMENT_SUCCESS, succ + 1);
	}
	
	public void increSuccess(int by){
		int succ = ref.get(ApiConstant.PAYMENT_SUCCESS);
		ref.put(ApiConstant.PAYMENT_SUCCESS, succ + by);
	}

	public Map<String, Integer> getRef() {
		return ref;
	}

	public void setRef(Map<String, Integer> ref) {
		this.ref = ref;
	}
	
	public String toString(){
		if(ref!=null){
			try{
				return JSONUtil.serialize(ref);
			} catch (Exception e){
				LOG.error("toString failed of PaymentResult",e);
				return "";
			}
		} else {
			return "";
		}
		
	}

}
