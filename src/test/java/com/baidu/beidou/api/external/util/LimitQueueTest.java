package com.baidu.beidou.api.external.util;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class LimitQueueTest {

	@Test
	public void testQueue() {
		int size = 5;
		LimitQueue<String> lqueue = new LimitQueue<String>(5);

		//-----------------------------
		// 1-2-3-4-5-6-7  <--
		//-----------------------------
		lqueue.offer("1");
		lqueue.offer("2");
		lqueue.offer("3");
		lqueue.offer("4");
		lqueue.offer("5");
		assertThat(lqueue.size(), is(size));
		lqueue.offer("6");
		lqueue.offer("7");
		assertThat(lqueue.size(), is(size));
		assertThat(lqueue.poll(), is("3"));
		assertThat(lqueue.size(), is(size-1));
		assertThat(lqueue.poll(), is("4"));
		assertThat(lqueue.size(), is(size-2));
		lqueue.offer("8");
		assertThat(lqueue.poll(), is("5"));
		assertThat(lqueue.size(), is(size-2));
		for (String string : lqueue.getQueue()) {
			System.out.println(string);
		}
	}

}
