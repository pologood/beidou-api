package com.baidu.beidou.api.external.util;

import java.io.File;

import org.junit.Ignore;

import com.baidu.beidou.api.external.DarwinApiHelper;
import com.baidu.beidou.api.external.util.vo.ApiOption;
import com.baidu.beidou.api.external.util.vo.DataPrivilege;
import com.baidu.fengchao.sun.base.BaseRequestOptions;
import com.baidu.fengchao.sun.base.BaseRequestUser;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
@Ignore
public class ApiBaseRPCTest<T> {

	protected static DataPrivilege dataUser = null;
	protected static ApiOption apiOption = DarwinApiHelper.getApiOptions();
	
	protected static BaseRequestUser dataUser2 = DarwinApiHelper.getBaseRequestUser(8, 8);
	protected static BaseRequestOptions apiOption2 = DarwinApiHelper.getBaseRequestOptions();

	static {
		dataUser = DarwinApiHelper.getDataPrivilege(ApiExternalConstant.OPUSER, ApiExternalConstant.DATAUSER);
		dataUser2 = DarwinApiHelper.getBaseRequestUser(ApiExternalConstant.OPUSER, ApiExternalConstant.DATAUSER);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected T getServiceProxy(Class c, String serviceUrl) throws Exception {
		System.out.println("URL: " + serviceUrl);
		System.out.println("opuser: " + dataUser.getOpUser());
		System.out.println("datauser: " + dataUser.getDataUser());
		McpackRpcProxy proxy = new McpackRpcProxy(serviceUrl, "UTF-8", new ExceptionHandler());
		T exporter = (T) ProxyFactory.createProxy(c, proxy);
		return exporter;
	}
	
	public static String getImageFilePath(String fileName) {
		return new File("").getAbsolutePath() + "\\src\\test\\java\\com\\baidu\\beidou\\api\\external\\images\\" + fileName;
	}

}
