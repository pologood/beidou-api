package com.baidu.beidou.test;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Ignore;

@Ignore
public class BaseJMockTest {

	protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
	
    @After
    public void verifyMockObjects(){
    	context.assertIsSatisfied();
    }
	
}
