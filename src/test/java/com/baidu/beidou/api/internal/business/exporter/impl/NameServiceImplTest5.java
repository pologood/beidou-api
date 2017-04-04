package com.baidu.beidou.api.internal.business.exporter.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.baidu.beidou.api.internal.business.constant.Status;
import com.baidu.beidou.api.internal.business.exporter.NameService;
import com.baidu.beidou.api.internal.business.vo.UnitInfo;
import com.baidu.beidou.api.internal.business.vo.UnitResultOne;
import com.baidu.beidou.test.common.BasicTestCaseLegacy;

/**
 * NameServiceImplTest5
 * 
 * @author caichao
 * 
 */
public class NameServiceImplTest5 extends BasicTestCaseLegacy {

    private static int userId = 8;

    @Resource
    private NameService nameService;

    /**
     * inid data from file
     */
    @Before
    public void setUp() {
        super.prepareDataFromClasspathScript(new String[] 
                { "com/baidu/beidou/api/internal/business/exporter/impl/NameServiceImplTest_data.sql" });
    }

    /**
     * getUnitMaterialbyId（Integer userId，Integer unitId） test cases
     */
    @Test
    public void testGetUnitMaterialbyId() {
        long unitId = 10541L;
        UnitResultOne result = nameService.getOneUnitMaterialbyId(userId, unitId);
        UnitInfo unitInfo = result.getUnitid2UnitInfo();
        assertThat(result.getStatus(), is(Status.SUCCESS));
        assertThat(unitInfo, notNullValue());

        String title = unitInfo.getTitle();
        int type = unitInfo.getType();
        String url = unitInfo.getUrl();
        String desc1 = unitInfo.getDesc1();
        String desc2 = unitInfo.getDesc2();
        String showUrl = unitInfo.getShowUrl();
        String targetUrl = unitInfo.getTargetUrl();
        // int width = unitInfo.getWidth();
        // int height = unitInfo.getHeight();

        assertThat(title, is("正宗韩国焖锅 百年秘制"));
        assertThat(type, is(1));
        assertThat(url, nullValue());
        assertThat(desc1, is("u88汇集众多特色小吃"));
        assertThat(desc2, is("风味火锅,烧烤串吧等项"));
        assertThat(showUrl, is("www.u88.cn/"));
        assertThat(targetUrl, is("http://www.u88.cn/ItemClass/ItemClassList500.htm?friendlink=tgcany3"));
        // assertThat(height, is(60)); //type=1为文字广告，没有width和height属性
        // assertThat(width, is(60));
    }

    /**
     * testGetUnitMaterialbyId_ParamErrorNull
     */
    @Test
    public void testGetUnitMaterialbyIdParamErrorNull() {
        Long unitId = null;
        UnitResultOne result = nameService.getOneUnitMaterialbyId(userId, unitId);
        assertThat(result.getStatus(), is(Status.PARAM_ERROR));
        assertThat(result.getUnitid2UnitInfo(), nullValue());
    }

    /**
     * testGetUnitMaterialbyId_NotFind
     */
    @Test
    public void testGetUnitMaterialbyIdNotFind() {
        long unitId = 25478;
        UnitResultOne result = nameService.getOneUnitMaterialbyId(userId, unitId);
        assertThat(result.getStatus(), is(Status.PARAM_ERROR));
        assertThat(result.getUnitid2UnitInfo(), nullValue());
    }

    /**
     * testGetUnitMaterialbyId_UserIdNotMatch
     */
    @Test
    public void testGetUnitMaterialbyIdUserIdNotMatch() {
        long unitId = 1541395;
        UnitResultOne result = nameService.getOneUnitMaterialbyId(userId, unitId);
        assertThat(result.getStatus(), is(Status.PARAM_ERROR));
        assertThat(result.getUnitid2UnitInfo(), nullValue());
    }
}