package com.baidu.beidou.api.internal.tool.rpc;

import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.tool.exporter.PreviewUrlService;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
@Ignore
public class PreviewUrlServiceTest {

	@Test
	public void testGetPreviewUrl() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/api/PreviewUrlService", "UTF-8", new ExceptionHandler());
		//McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.93:8088/beidou-api/api/PreviewUrlService", "UTF-8", new ExceptionHandler());
		
		PreviewUrlService exporter = ProxyFactory.createProxy(PreviewUrlService.class, proxy);
		int siteId = 56397;
		Map<Integer, List<String>> result = exporter.findSitePreviewUrlsGroupBySizeOrderBySrchBySiteId(siteId, 6);
		System.out.println("count:" + result.size());
		for(Integer size: result.keySet()){
			List<String> urls = result.get(size);
			System.out.println("==size:" + size);
			System.out.println("==url:");
			for(String url: urls){
				System.out.println(url);
			}
			System.out.println("=========");
		}
	}
}
