package com.baidu.beidou.api.external.util;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * ClassName: XmlUtil  <br>
 * Function: 解析betwixt xml文件的工具类
 *
 * @author zhangxu 
 * @since 2.0.1
 * @date Mar 28, 2012
 */
public final class XmlUtil {
	
	private static final Log LOG = LogFactory.getLog(XmlUtil.class);

	private XmlUtil(){}
	
	public static void toXml(Object object,OutputStream out){
		try {
			BeanWriter beanWriter = new BeanWriter(out,"UTF-8");
			beanWriter.enablePrettyPrint();
			beanWriter.setIndent("    ");
			beanWriter.setEndOfLine(System.getProperty("line.separator"));
			beanWriter.writeXmlDeclaration("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			beanWriter.getXMLIntrospector().setWrapCollectionsInElement(true);
			beanWriter.setWriteEmptyElements(false);
			beanWriter.write(object);
			beanWriter.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromXml(InputStream in,Class<T> clazz){
		BeanReader beanReader = new BeanReader();
		T object = null;
		try {
			beanReader.deregisterBeanClass(clazz);
			beanReader.clear();
			beanReader.registerBeanClass(clazz);
			beanReader.getXMLIntrospector().setWrapCollectionsInElement(true);
			object = (T) beanReader.parse(in); 
		} catch (Exception e) {
			LOG.error("error when parsing accountfile config file." + e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return object;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T fromXml(String filePath,Class<T> clazz){
		BeanReader beanReader = new BeanReader();
		T object = null;
		try {
			beanReader.deregisterBeanClass(clazz);
			beanReader.clear();
			beanReader.registerBeanClass(clazz);
			beanReader.getXMLIntrospector().setWrapCollectionsInElement(true);
			object = (T) beanReader.parse(new File(filePath));
		} catch (Exception e) {
			LOG.error("error when parsing accountfile config file." + e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return object;
	}

}

