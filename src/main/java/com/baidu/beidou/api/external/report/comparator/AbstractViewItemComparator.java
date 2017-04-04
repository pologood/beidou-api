package com.baidu.beidou.api.external.report.comparator;

import java.util.Comparator;
import com.baidu.beidou.api.external.report.vo.AbstractStatViewItem;

/**
 * 
 * ClassName: ApiReportViewItemComparator  <br>
 * Function: 时间比较器,用于从doris查询出的天粒度数据到排序
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 10, 2012
 */
public class AbstractViewItemComparator implements Comparator<AbstractStatViewItem>{
	
	public int compare(AbstractStatViewItem a, AbstractStatViewItem b){
		String dateA = a.getDay();
		String dateB = b.getDay();
		if(dateA == null || dateB == null){
			return 0;
		}
		return dateA.compareTo(dateB);
	}
	
}
