package com.baidu.beidou.api.internal.fcindex.vo;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.baidu.beidou.stat.vo.TaxStatVo;

public class ApiTaxStatVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	List<TaxStatVo> result = Collections.emptyList();
	
	public ApiTaxStatVo(int code, String err){
		this.code = code;
		this.errmsg = err;
	}
	
	ApiTaxStatVo(){
	}
	
	public List<TaxStatVo> getResult() {
		return result;
	}
	public void setResult(List<TaxStatVo> result) {
		this.result = result;
	}
	
}
