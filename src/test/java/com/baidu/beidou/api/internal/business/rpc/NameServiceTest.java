package com.baidu.beidou.api.internal.business.rpc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.internal.business.constant.NameConstant;
import com.baidu.beidou.api.internal.business.constant.Status;
import com.baidu.beidou.api.internal.business.exporter.NameService;
import com.baidu.beidou.api.internal.business.vo.KeywordInfo;
import com.baidu.beidou.api.internal.business.vo.KeywordResult;
import com.baidu.beidou.api.internal.business.vo.PlanInfo;
import com.baidu.beidou.api.internal.business.vo.PlanResult;
import com.baidu.beidou.api.internal.business.vo.UnitInfo;
import com.baidu.beidou.api.internal.business.vo.UnitResultOne;
import com.baidu.rpc.client.McpackRpcProxy;
import com.baidu.rpc.client.ProxyFactory;
import com.baidu.rpc.exception.ExceptionHandler;
@Ignore
public class NameServiceTest {

	@Test
	public void testGetPlanName() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/api/NameService", "UTF-8", new ExceptionHandler());
		//McpackRpcProxy proxy = new McpackRpcProxy("http://10.38.45.34:8080/api/NameService", "UTF-8", new ExceptionHandler());
		//McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.93:8088/api/NameService", "UTF-8", new ExceptionHandler());
		//McpackRpcProxy proxy = new McpackRpcProxy("http://10.23.244.60:8080/api/NameService", "UTF-8", new ExceptionHandler());
		
		NameService exporter = ProxyFactory.createProxy(NameService.class, proxy);
		List<Integer> planId = new ArrayList<Integer>();
		planId.add(754575);
		planId.add(770767);
		PlanResult planResult = exporter.getPlanNamebyId(19, planId);
		System.out.println("status:" + planResult.getStatus());
		Map<Integer, PlanInfo> planInfo = planResult.getPlanid2Name();
		for(Integer i : planInfo.keySet()){
			System.out.println("planid:" + i + " | planname:" + planInfo.get(i).getName() + " | isDeleted:" + planInfo.get(i).getIsDeleted());
		}
	}
	
	@Test
	public void testGetKeywordLiteral() throws Exception{
		//McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/NameService", "UTF-8", new ExceptionHandler());
		//McpackRpcProxy proxy = new McpackRpcProxy("http://10.38.45.34:8080/api/NameService", "UTF-8", new ExceptionHandler());
		//McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.93:8088/api/NameService", "UTF-8", new ExceptionHandler());
		McpackRpcProxy proxy = new McpackRpcProxy("http://10.23.244.60:8080/api/NameService", "UTF-8", new ExceptionHandler());
		
		NameService exporter = ProxyFactory.createProxy(NameService.class, proxy);
		List<Map<String, Long>> keywordIdAndAtomIdList = new ArrayList<Map<String, Long>>();
		String inputFileName = "./test/com/baidu/beidou/api/internal/business/rpc/keyword.txt";
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFileName));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] items = line.split("\t");
				
				Map<String, Long> map = new HashMap<String, Long>();
				map.put(NameConstant.KEYWORDID_CONST, Long.valueOf(items[0]));
				map.put(NameConstant.ATOMID_CONST, Long.valueOf(items[1]));
				keywordIdAndAtomIdList.add(map);
			}
			
		}  catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		KeywordResult result = exporter.getKeywordLiteral(keywordIdAndAtomIdList);
		System.out.println("status:" + result.getStatus());
		Map<Long, KeywordInfo> info = result.getKeywordid2Name();
		for(Long i : info.keySet()){
			System.out.println("atomid:" + i + " | literal:" 
					+ info.get(i).getLiteral() + " | isDeleted:" 
					+ info.get(i).getIsDeleted());
		}
	}
	
	@Test
	public void testGetKeywordLiteral2() throws Exception{
		//McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/beidou-api/api/NameService", "UTF-8", new ExceptionHandler());
		//McpackRpcProxy proxy = new McpackRpcProxy("http://10.38.45.34:8080/api/NameService", "UTF-8", new ExceptionHandler());
		McpackRpcProxy proxy = new McpackRpcProxy("http://10.81.31.95:8231/api/NameService", "UTF-8", new ExceptionHandler());
		//McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/api/NameService", "UTF-8", new ExceptionHandler());
		NameService exporter = ProxyFactory.createProxy(NameService.class, proxy);
		
		List<Long> wordIdList = new ArrayList<Long>();
		
		
		/**
		 * keywordid  kwd  wordid planid groupid userid ..
2386847535	123	577	1092595	3474333	499	0	0	NULL	NULL	832979	2013-02-27 12:35:05	832979	2013-02-27 12:35:05	0
2386847543	abc	327763	1092595	3474333	499	0	0	NULL	NULL	832979	2013-02-27 12:35:05	832979	2013-02-27 12:35:05	0
2386847551	test	25816	1092595	3474333	499	0	0	NULL	NULL	499	2013-02-27 12:49:59	499	2013-02-27 12:49:59	0

2837356721	你好	123501	108	228	499	0	0	NULL	NULL	499	2013-04-01 17:20:24	499	2013-04-01 17:20:24	0
2837356722	你好123	41652024	108	228	499	0	0	NULL	NULL	499	2013-04-01 17:21:17	499	2013-04-01 17:21:17	0
2837356723	rrrtt	805662963	108	228	499	0	0	NULL	NULL	499	2013-04-01 17:25:02	499	2013-04-01 17:25:02	0

2837676721	汽车	129882	1507167	4303015	499	0	0	NULL	NULL	499	2013-04-17 20:25:26	499	2013-04-17 20:25:26	0
2837676722	宝马	33908	1507167	4303015	499	0	0	NULL	NULL	499	2013-04-17 20:25:26	499	2013-04-17 20:25:26	0
2837676723	马德拉斯	39093095	1507167	4303015	499	0	0	NULL	NULL	499	2013-04-17 20:25:26	499	2013-04-17 20:25:26	0
2837676724	轿车	98370	1507167	4303015	499	0	0	NULL	NULL	499	2013-04-17 20:25:26	499	2013-04-17 20:25:26	0
2837676725	大众	49752	1507167	4303015	499	0	0	NULL	NULL	499	2013-04-17 20:25:26	499	2013-04-17 20:25:26	0
2837676726	马自达	299963	1507167	4303015	499	0	0	NULL	NULL	499	2013-04-17 20:25:26	499	2013-04-17 20:25:26	0
2837676727	速腾	2455323	1507167	4303015	499	0	0	NULL	NULL	499	2013-04-17 20:25:26	499	2013-04-17 20:25:26	0

		 */
		wordIdList.add(577l); 
		wordIdList.add(327763l);
		wordIdList.add(25816l);
		wordIdList.add(123501l);
		//wordIdList.add(129882l);
		//wordIdList.add(577l);
		KeywordResult result = exporter.getKeywordLiteral2(499, 4303015, wordIdList);
		
		System.out.println(result.getStatus());
		Map<Long, KeywordInfo> wordId2NameMap = result.getKeywordid2Name();
		for (Long key : wordId2NameMap.keySet()) {
			System.out.println("wordid="+key);
			System.out.println("literal="+wordId2NameMap.get(key).getLiteral());
			System.out.println("isdel="+(wordId2NameMap.get(key).getIsDeleted()==0?false:true));
			System.out.println("--------");
		}
		
	}
	
	//added by lvzichan
	@Test
	public void testGetOneUnitMaterialbyId() throws Exception{
		McpackRpcProxy proxy = new McpackRpcProxy("http://127.0.0.1:8080/api/NameService", "UTF-8", new ExceptionHandler());
		NameService exporter = ProxyFactory.createProxy(NameService.class, proxy);
		
		int userId = 8;
		long unitId = 40492488l;
		UnitResultOne result = exporter.getOneUnitMaterialbyId(userId, unitId);
		assertThat(result.getStatus(), is(Status.SUCCESS));
		UnitInfo unitInfo = result.getUnitid2UnitInfo();
		assertThat(unitInfo, notNullValue());
		
		assertThat(unitInfo.getTitle(), is("图文创意title-url含有通配符"));
		assertThat(unitInfo.getType(), is(5));
		assertThat(unitInfo.getUrl(), containsString("id=nW0snjDdPjTzrf&gp=403&time=nHnLnW6YrHRsn6.jpg"));
		assertThat(unitInfo.getDesc1(), is("图文创意desc11111"));
		assertThat(unitInfo.getDesc2(), is("图文创意desc22222"));
		assertThat(unitInfo.getShowUrl(), is("www.baidu.com?kwid"));
		assertThat(unitInfo.getTargetUrl(), is("http://www.baidu.com?kwid={keywordid}&creative={creative}&pl={placement}&kw={keyword}"));
		assertThat(unitInfo.getHeight(), is(60));
		assertThat(unitInfo.getWidth(), is(60));
//		System.out.println("getOneUnitMaterialbyId测试通过，result.status=" + result.getStatus());
	}
	
}
