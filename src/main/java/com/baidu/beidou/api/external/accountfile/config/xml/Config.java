package com.baidu.beidou.api.external.accountfile.config.xml;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * ClassName: Config  <br>
 * Function: 总配置项
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 28, 2012
 */
public class Config {
	
	/**
	 * 处理账户信息数据集合
	 */
	private List<Item> items = new ArrayList<Item>();

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public void addItem(Item item) {
		this.items.add(item);
	}

}
