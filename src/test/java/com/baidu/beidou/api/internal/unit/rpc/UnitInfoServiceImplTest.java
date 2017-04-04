package com.baidu.beidou.api.internal.unit.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.audit.constant.ResponseStatus;
import com.baidu.beidou.api.internal.unit.exporter.UnitInfoService;
import com.baidu.beidou.api.internal.unit.vo.MediaResult;
import com.baidu.beidou.api.internal.unit.vo.UBMCTextParam;
import com.baidu.beidou.api.internal.unit.vo.UnitParam;
import com.baidu.beidou.api.internal.unit.vo.UnitPreResult;
import com.baidu.beidou.api.internal.unit.vo.UnitResult;
import com.baidu.beidou.cprounit.vo.UnitInfoView;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
import com.google.common.collect.Lists;

/**
 * UnitInfoServiceImplTest
 * 
 * @author work
 * 
 */
@Ignore
public class UnitInfoServiceImplTest {

	@Test
	public void testGetUnitAuditList() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://10.94.21.70:8080/api/api/UnitInfoService", "UTF-8", new ExceptionHandler());
		//McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.54.65:8080/api/api/UnitInfoService", "UTF-8", new ExceptionHandler());
		
		UnitInfoService exporter = ProxyFactory.createProxy(UnitInfoService.class, proxy);
		
		Long mcId = 379067817L;
		Integer versionId = 1;
		
		UnitResult<String> result = exporter.getUbmcTmpUrl(mcId, versionId);
		
		if (result != null && result.getStatus() == ResponseStatus.SUCCESS) {
			System.out.println(result.getData().get(0));
		}
	}
	
	
	@Test
	public void testGetUBMCUrlByMcIdAndMcVersionId() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://10.94.21.70:8080/api/api/UnitInfoService", "UTF-8", new ExceptionHandler());
		//McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.54.65:8080/api/api/UnitInfoService", "UTF-8", new ExceptionHandler());
		
		UnitInfoService exporter = ProxyFactory.createProxy(UnitInfoService.class, proxy);
		
		Long mcId = 379067817L;
		Integer versionId = 1;
		
		UBMCTextParam param = new UBMCTextParam();
		param.setCreativeId(1);
		param.setMcId(mcId);
		param.setMcVersionId(versionId);
		List<UBMCTextParam> paramList = new ArrayList<UBMCTextParam>();
		paramList.add(param);
		Map<Long, String> result = exporter.getUBMCUrlByMcIdAndMcVersionId(paramList);
		
		if (result != null) {
			System.out.println(result.get(1L));
		}
	}
	
	@Test
	public void testGetHtmlSnippetForSmartUnit() throws Exception{
		
		McpackRpcProxy proxy = new McpackRpcProxy(
				"http://cq01-testing-ecom6314.cq01.baidu.com:8020/api/UnitInfoService", "UTF-8",
				new ExceptionHandler());
		UnitInfoService exporter = ProxyFactory.createProxy(UnitInfoService.class, proxy);
		Long unitId = 119700112L;
		Integer userId = 19;
		Integer flowTag = 1;
		UnitResult<UnitPreResult> result = exporter.getHtmlSnippetForSmartUnit(unitId, userId, flowTag);
		System.out.println("status=" + result.getStatus());
		List<UnitPreResult> list = result.getData();
		for (UnitPreResult unitPreResult : list) {
			System.out.println(unitPreResult.getHtmlSnippet());
			List<String> targetUrls = unitPreResult.getTargetUrls();
			for (String string : targetUrls) {
				System.out.println(string);
			}
		}
	}
	

    /**
     * Function: 
     * 
     * @author genglei01
     * @throws Exception 
     */
//  @Test
    public void testUploadMedia() throws Exception {
        
        McpackRpcProxy proxy = new McpackRpcProxy("http://10.94.21.70:8080/api/api/UnitInfoService", 
                "UTF-8", new ExceptionHandler());
        UnitInfoService exporter = ProxyFactory.createProxy(UnitInfoService.class, proxy);
        
        String desc = "";
        byte[] data = null;
        UnitResult<MediaResult> result = exporter.uploadMedia(desc, data);
        System.out.println("status=" + result.getStatus());
        List<MediaResult> list = result.getData();
        if (CollectionUtils.isNotEmpty(list)) {
            MediaResult mediaResult = list.get(0);
            System.out.println(desc + "\t" + mediaResult.getMcId() + "\t" 
                    + mediaResult.getVersionId() + "\t" + mediaResult.getMediaId());
        }
    }

	@Test
	public void testGetUnitList() {
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/api/UnitInfoService",
				"UTF-8", new ExceptionHandler());
		UnitInfoService exporter = ProxyFactory.createProxy(UnitInfoService.class, proxy);
		List<UnitParam> params = Lists.newArrayList();
		params.add(new UnitParam(480787, 206596848L));
		params.add(new UnitParam(480787, 206596856L));
		params.add(new UnitParam(5, 206596560L));
		params.add(new UnitParam(480787, 206596864L));
		params.add(new UnitParam(5, 206596864L)); // userid is used only for dbrouting
		params.add(new UnitParam(5, 206596544L));
		params.add(new UnitParam(480787, 206596872L));
		UnitResult<UnitInfoView> unitList = exporter.getUnitList(params);
		for (UnitInfoView viewItem : unitList.getData()) {
			System.out.println(viewItem);
		}
	}

}
