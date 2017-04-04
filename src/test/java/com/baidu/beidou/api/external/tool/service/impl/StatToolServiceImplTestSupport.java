package com.baidu.beidou.api.external.tool.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.JSONUtil;
import org.junit.Ignore;

import com.baidu.beidou.report.ReportConstants;
import com.baidu.beidou.test.common.AbstractShardXdbTestCaseLegacy;
import com.baidu.beidou.user.bo.User;

/**
 * StatToolServiceImplTestSupport
 * 
 * @author work
 * 
 */
@Ignore
public class StatToolServiceImplTestSupport extends AbstractShardXdbTestCaseLegacy{
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getMockDorisData(String file){
		//2011-12-28T23:00:00
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> performanceData = null;
		List<String> list = readFileLines(file);
		StringBuffer sb = new StringBuffer();
		for(String str : list){
			sb.append(str);
		}
		try{
			//Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
			//performanceData = (List<Map<String, Object>>)new Gson().fromJson(sb.toString(), type);
			performanceData = (List<Map<String, Object>>)JSONUtil.deserialize(sb.toString());
			for(Map<String, Object> record : performanceData){
				// date
				String from = (String)(record.get(ReportConstants.FROMDATE));
				String to = (String)(record.get(ReportConstants.TODATE));
				Date fromDate = format.parse(from.toString());
				Date toDate = format.parse(to.toString());
				record.put(ReportConstants.FROMDATE, fromDate);
				record.put(ReportConstants.TODATE, toDate);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return performanceData;
	}

	public static List<String> readFileLines(String file){
		String path=file;	
		List<String> lines = new ArrayList<String>();
		
		try{
			if(!new File(path).exists()){
				System.err.println(file + " not exist!!!!!!!");
				return lines;
			}
			
			FileReader myFileReader=new FileReader(path);
			BufferedReader myBufferedReader=new BufferedReader(myFileReader);
			String line;
	
			while((line=myBufferedReader.readLine())!=null)
			{
				lines.add(line.trim());
			} 

		}catch(Exception e){
			System.err.println("read file lines fail "+path+" "+e.getMessage());
		}
			
		return lines;
	}
	
	/**
	 * 由于Storage仅返回时间粒度的起始时间，并且即使输入的起始时间为某时间粒度的中间点，也将返回该粒度的起始时间；<br>
	 * 需要根据起始时间定位所属的时间粒度分组，以便构造返回结果中的FROMDATE、TODATE<br>
	 * <br>
	 * 假设：<br>
	 * 1 dateParts不为空<br>
	 * 2 dateParts、f精确到天，没有小时、分、秒等尾巴
	 * 
	 * @param dateParts
	 * @param f
	 * @return
	 */
	protected Date[] locateTimeGroup(List<Date[]> dateParts, Date f){
		Date[] init = dateParts.get(0);
		Date[] tmp = new Date[]{init[0], init[1]};
		
		for(Date[] part : dateParts){
			if (f.compareTo(part[0]) == 0){
				return part;
			}else if (tmp[0].compareTo(part[0]) > 0){
				tmp[0] = part[0];
				tmp[1] = part[1];
			}
		}

		return tmp;
	}
	
	protected User getUser(int userid, String username){
		User result = new User();
		result.setUserid(userid);
		result.setUsername(username);
		return result;
}
}
