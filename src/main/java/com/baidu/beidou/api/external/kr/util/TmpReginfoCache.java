package com.baidu.beidou.api.external.kr.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类用于在星计划中，兼容旧的历史记录。如高级地域，删除掉的二级地域等
 */
public class TmpReginfoCache {
	private static Map<Integer, Integer[]> regIdInfoMap;
	private static Map<Integer, String> regIdNameMap;
	
	public static Integer[] get(Integer regId){
		if(regIdInfoMap == null){
			init();
		}
		return regIdInfoMap.get(regId);
	}
	
	public static String getName(Integer regId){
		if(regIdNameMap == null){
			init();
		}
		return regIdNameMap.get(regId);
	}
	
	public static void init(){
		regIdInfoMap = new HashMap<Integer, Integer[]>();
		regIdNameMap = new HashMap<Integer, String>();
		
		cacheDeleteSecondRegion(regIdInfoMap, regIdNameMap);
		cacheLianTong(regIdInfoMap, regIdNameMap);
		cacheDianXin(regIdInfoMap, regIdNameMap);
		cacheWangba(regIdInfoMap, regIdNameMap);
		cacheXiaoYuan(regIdInfoMap, regIdNameMap);
	}
	
	/**
	 * 缓存删除掉的二级地域
	 * 
	 * @param regInfoMap
	 */
	private static void cacheDeleteSecondRegion(Map<Integer, Integer[]> regInfoMap, Map<Integer, String> regIdNameMap){
		/**
		 * 对于一级地域，[0]=0，[1]=分类标识（省级地域/高级地域）
		 * 对于二级地域，[0]=一级地域ID，[1]=分类标识（省级地域/高级地域）
		 */
		
		/**
firstregid	secondregid	regtype	regname

2	829	1	南汇区
3	762	1	大港区
3	764	1	汉沽区
3	775	1	塘沽区
		 */
		
		Integer[] regInfo;
		int firstId;
		
		// 添加一级地域
		firstId = 2;
		regInfo = new Integer[2];
		regInfo[0]=0;
		regInfo[1]=1;
		regInfoMap.put(firstId, regInfo);
		regIdNameMap.put(firstId, "上海市");
		
		firstId = 3;
		regInfo = new Integer[2];
		regInfo[0]=0;
		regInfo[1]=1;
		regInfoMap.put(firstId, regInfo);
		regIdNameMap.put(firstId, "天津市");
		
		// 添加二级地域
		regInfo = new Integer[2];
		regInfo[0]=2;
		regInfo[1]=1;
		regInfoMap.put(829, regInfo);
		regIdNameMap.put(829, "南汇区");
		
		regInfo = new Integer[2];
		regInfo[0]=3;
		regInfo[1]=1;
		regInfoMap.put(762, regInfo);
		regIdNameMap.put(762, "大港区");
		
		regInfo = new Integer[2];
		regInfo[0]=3;
		regInfo[1]=1;
		regInfoMap.put(764, regInfo);
		regIdNameMap.put(764, "汉沽区");
		
		regInfo = new Integer[2];
		regInfo[0]=3;
		regInfo[1]=1;
		regInfoMap.put(775, regInfo);
		regIdNameMap.put(775, "塘沽区");
	}
	
	/**
	 * 缓存删除掉的中国联通
	 * 
	 * @param regInfoMap
	 */
	private static void cacheLianTong(Map<Integer, Integer[]> regInfoMap, Map<Integer, String> regIdNameMap){
		/**
		 * 对于一级地域，[0]=0，[1]=分类标识（省级地域/高级地域）
		 * 对于二级地域，[0]=一级地域ID，[1]=分类标识（省级地域/高级地域）
		 */
		
		Integer[] regInfo;
		int firstId = 35;
		
		// 添加一级地域
		regInfo = new Integer[2];
		regInfo[0]=0;
		regInfo[1]=2;
		regInfoMap.put(firstId, regInfo);
		regIdNameMap.put(firstId, "中国联通");
		
		// 添加二级地域
		String[][] idNameArr = new String[][]{
				{"632","安徽省"},
				{"633","北京市"},
				{"634","福建省"},
				{"635","甘肃省"},
				{"636","广东省"},
				{"637","广西省"},
				{"638","贵州省"},
				{"639","海南省"},
				{"640","河北省"},
				{"641","河南省"},
				{"642","黑龙江省"},
				{"643","湖北省"},
				{"644","湖南省"},
				{"645","吉林省"},
				{"650","江苏省"},
				{"651","江西省"},
				{"652","辽宁省"},
				{"653","内蒙古省"},
				{"654","宁夏省"},
				{"655","青海省"},
				{"656","山东省"},
				{"657","山西省"},
				{"658","上海市"},
				{"659","四川省"},
				{"660","天津市"},
				{"661","西藏省"},
				{"662","新疆省"},
				{"663","云南省"},
				{"664","浙江省"},
				{"665","重庆市"},
				{"682","陕西省"}
		};
		
		for(String[] arr : idNameArr){
			Integer id = new Integer(arr[0]);
			String name = arr[1];
			
			regInfo = new Integer[2];
			regInfo[0]=firstId;
			regInfo[1]=2;
			regInfoMap.put(id, regInfo);
			
			regIdNameMap.put(id, name);
		}
	}
	
	/**
	 * 
	 * 缓存删除掉的电信
	 * 
	 * @param regInfoMap
	 */
	private static void cacheDianXin(Map<Integer, Integer[]> regInfoMap, Map<Integer, String> regIdNameMap){
		/**
		 * 对于一级地域，[0]=0，[1]=分类标识（省级地域/高级地域）
		 * 对于二级地域，[0]=一级地域ID，[1]=分类标识（省级地域/高级地域）
		 */
		
		Integer[] regInfo;
		int firstId = 36;
		
		// 添加一级地域
		regInfo = new Integer[2];
		regInfo[0]=0;
		regInfo[1]=2;
		regInfoMap.put(firstId, regInfo);
		regIdNameMap.put(firstId, "电信");
		
		// 添加二级地域
		String[][] idNameArr = new String[][]{
				{"666","安徽省"},
				{"667","北京市"},
				{"668","福建省"},
				{"669","甘肃省"},
				{"670","广东省"},
				{"671","广西省"},
				{"672","贵州省"},
				{"683","海南省"},
				{"684","河北省"},
				{"685","河南省"},
				{"686","黑龙江省"},
				{"687","湖北省"},
				{"688","湖南省"},
				{"689","吉林省"},
				{"690","江苏省"},
				{"691","江西省"},
				{"692","辽宁省"},
				{"693","内蒙古省"},
				{"694","宁夏省"},
				{"695","青海省"},
				{"696","山东省"},
				{"697","山西省"},
				{"698","陕西省"},
				{"699","上海市"},
				{"700","四川省"},
				{"703","天津市"},
				{"704","西藏省"},
				{"706","新疆省"},
				{"707","云南省"},
				{"708","浙江省"},
				{"709","重庆市"}
		};
		
		for(String[] arr : idNameArr){
			Integer id = new Integer(arr[0]);
			String name = arr[1];
			
			regInfo = new Integer[2];
			regInfo[0]=firstId;
			regInfo[1]=2;
			regInfoMap.put(id, regInfo);
			
			regIdNameMap.put(id, name);
		}
	}
	
	/**
	 * 缓存删除掉的网吧
	 * 
	 * @param regInfoMap
	 */
	private static void cacheWangba(Map<Integer, Integer[]> regInfoMap, Map<Integer, String> regIdNameMap){
		/**
		 * 对于一级地域，[0]=0，[1]=分类标识（省级地域/高级地域）
		 * 对于二级地域，[0]=一级地域ID，[1]=分类标识（省级地域/高级地域）
		 */
		Integer[] regInfo;
		int firstId = 37;
		
		// 添加一级地域
		regInfo = new Integer[2];
		regInfo[0]=0;
		regInfo[1]=2;
		regInfoMap.put(firstId, regInfo);
		regIdNameMap.put(firstId, "网吧");
		
		// 添加二级地域
		String[][] idNameArr = new String[][]{
				{"710","重庆"},
				{"711","浙江"},
				{"712","云南"},
				{"713","新疆"},
				{"714","香港"},
				{"715","西藏"},
				{"716","天津"},
				{"717","四川"},
				{"718","上海"},
				{"719","陕西"},
				{"720","山西"},
				{"721","山东"},
				{"722","青海"},
				{"723","宁夏"},
				{"724","内蒙"},
				{"725","辽宁"},
				{"726","江西"},
				{"727","江苏"},
				{"728","吉林"},
				{"729","湖南"},
				{"730","湖北"},
				{"731","黑龙江"},
				{"732","河南"},
				{"733","河北"},
				{"734","海南"},
				{"735","贵州"},
				{"736","广西"},
				{"737","甘肃"},
				{"738","福州"},
				{"739","福建"},
				{"740","北京"},
				{"741","安徽"},
				{"838","广东"}
		};
		for(String[] arr : idNameArr){
			Integer id = new Integer(arr[0]);
			String name = arr[1];
			
			regInfo = new Integer[2];
			regInfo[0]=firstId;
			regInfo[1]=2;
			regInfoMap.put(id, regInfo);
			
			regIdNameMap.put(id, name);
		}
	}
	
	/**
	 * 缓存删除掉的校园
	 * 
	 * @param regInfoMap
	 */
	private static void cacheXiaoYuan(Map<Integer, Integer[]> regInfoMap, Map<Integer, String> regIdNameMap){
		/**
		 * 对于一级地域，[0]=0，[1]=分类标识（省级地域/高级地域）
		 * 对于二级地域，[0]=一级地域ID，[1]=分类标识（省级地域/高级地域）
		 */
		
		Integer[] regInfo;
		int firstId = 38;
		
		// 添加一级地域
		regInfo = new Integer[2];
		regInfo[0]=0;
		regInfo[1]=2;
		regInfoMap.put(firstId, regInfo);
		regIdNameMap.put(firstId, "校园");
		
		// 添加二级地域
		String[][] idNameArr = new String[][]{
				{"839","安徽"},
				{"840","北京"},
				{"841","福建"},
				{"842","甘肃"},
				{"843","贵州"},
				{"844","河北"},
				{"845","河南"},
				{"846","黑龙江"},
				{"847","湖北"},
				{"848","湖南"},
				{"849","吉林"},
				{"850","江苏"},
				{"851","江西"},
				{"852","辽宁"},
				{"853","内蒙古"},
				{"854","宁夏"},
				{"855","青海"},
				{"856","山东"},
				{"857","山西"},
				{"858","陕西"},
				{"859","上海"},
				{"860","四川"},
				{"861","天津"},
				{"862","西藏"},
				{"863","新疆"},
				{"864","云南"},
				{"865","浙江"},
				{"866","重庆"},
				{"871","海南"},
				{"872","广东"},
				{"873","广西"}
		};
		for(String[] arr : idNameArr){
			Integer id = new Integer(arr[0]);
			String name = arr[1];
			
			regInfo = new Integer[2];
			regInfo[0]=firstId;
			regInfo[1]=2;
			regInfoMap.put(id, regInfo);
			
			regIdNameMap.put(id, name);
		}
	}
}
