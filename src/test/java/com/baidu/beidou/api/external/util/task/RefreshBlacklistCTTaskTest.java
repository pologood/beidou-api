package com.baidu.beidou.api.external.util.task;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Ignore;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.baidu.ctclient.ITaskUsingErrorCode;
@Ignore
public class RefreshBlacklistCTTaskTest  extends TestCase {
    
    public void testReloadBlacklist(){
        ClassPathResource cpr = new ClassPathResource("refreshBlacklistCache-hessianclient.xml");
        BeanFactory bf = new XmlBeanFactory(cpr);
        ITaskUsingErrorCode task = (ITaskUsingErrorCode)bf.getBean("refreshBlacklistService");
        Assert.assertTrue(task.execute());
    }

}
