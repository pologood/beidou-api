package com.baidu.beidou.api.external.refusereason.exporter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.ub.beidou.refusereason.bo.Reason;
import com.baidu.ub.beidou.refusereason.exporter.RefuseReasonService;

@Component
public class RefuseReasonServiceTest {

    @Autowired
    private RefuseReasonService refuseReasonService;

    @Test
    @Ignore
    public void testGetAllInMap() {
        Map<Integer, Reason> reasonMap = refuseReasonService.getAllInMap();
        Iterator<Map.Entry<Integer, Reason>> iterator = reasonMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Reason> entry = iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        assertThat(reasonMap.size(), is(36));
    }

}
