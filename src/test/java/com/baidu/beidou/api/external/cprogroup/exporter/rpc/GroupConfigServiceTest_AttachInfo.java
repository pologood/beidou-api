package com.baidu.beidou.api.external.cprogroup.exporter.rpc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.baidu.beidou.api.external.cprogroup.exporter.GroupConfigService;
import com.baidu.beidou.api.external.cprogroup.vo.AttachInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.AttachSubUrlItemType;
import com.baidu.beidou.api.external.cprogroup.vo.GroupAttachInfoType;
import com.baidu.beidou.api.external.cprogroup.vo.request.DeleteAttachInfoRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.GetAttachInfosRequest;
import com.baidu.beidou.api.external.cprogroup.vo.request.UpdateAttachInfoRequest;
import com.baidu.beidou.api.external.util.ApiBaseRPCTest;
import com.baidu.beidou.api.external.util.ApiExternalConstant;
import com.baidu.beidou.api.external.util.vo.PlaceHolderResult;
import com.baidu.beidou.common.util.JsonUtils;
import com.baidu.fengchao.sun.base.BaseResponse;

/**
 * 附加信息单测
 * 
 * @author work
 */
public class GroupConfigServiceTest_AttachInfo extends ApiBaseRPCTest<GroupConfigService> {

    /**
     * 获取子链的test case
     * 
     * @throws Exception 访问api出现异常
     */
    @Ignore
    @Test
    public void testGetAttachInfos() throws Exception {
        GroupConfigService exporter =
                getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
        GetAttachInfosRequest request = new GetAttachInfosRequest();
        List<Integer> groupIds = new ArrayList<Integer>();
        groupIds.add(12001630);
        request.setGroupIds(groupIds);
        System.out.println(JsonUtils.toJson(request));
        long start = System.currentTimeMillis();
        BaseResponse<GroupAttachInfoType> result = exporter.getAttachInfos(dataUser2, request, apiOption2);
        long end = System.currentTimeMillis();
        System.out.println("Using " + (end - start) + "ms");
        GroupAttachInfoType[] response = result.getData();
        for (GroupAttachInfoType type : response) {
            System.out.println(type.getGroupId());
            List<AttachInfoType> infos = type.getAttachTypes();
            for (AttachInfoType info : infos) {
                String delimiter = " | ";
                System.out.println(info.getAttachType() + delimiter + info.getAttachMessage() + delimiter
                        + info.getAttachContent() + delimiter + info.getStatus() + delimiter
                        + info.getAttachSubUrls().toString());
            }
        }

    }

    /**
     * 测试删除子链的test case
     * 
     * @throws Exception 访问api出现异常
     */
    @Ignore
    @Test
    public void testDeleteAttachInfos() throws Exception {
        GroupConfigService exporter =
                getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
        DeleteAttachInfoRequest request = new DeleteAttachInfoRequest();
        List<GroupAttachInfoType> types = new ArrayList<GroupAttachInfoType>();
        List<AttachInfoType> type1 = new ArrayList<AttachInfoType>();

        type1.add(new AttachInfoType(1, "18134883345", 0, null, null));
        GroupAttachInfoType info1 = new GroupAttachInfoType();
        info1.setGroupId(13066763);
        info1.setAttachTypes(type1);

        types.add(info1);
        request.setAttachInfos(types);

        System.out.println(JsonUtils.toJson(request));
        long start = System.currentTimeMillis();
        BaseResponse<PlaceHolderResult> response = exporter.deleteAttachInfos(dataUser2, request, apiOption2);
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(response);
    }

    /**
     * 测试更新子链的test case
     * 
     * @throws Exception 访问api出现异常
     */
    @Test
    public void testUpdateAttachInfos() throws Exception {
        GroupConfigService exporter =
                getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
        UpdateAttachInfoRequest request = new UpdateAttachInfoRequest();

        List<GroupAttachInfoType> types = new ArrayList<GroupAttachInfoType>();
        List<AttachInfoType> type1 = new ArrayList<AttachInfoType>();
        List<AttachSubUrlItemType> attachSubUrls1 = new ArrayList<AttachSubUrlItemType>();
        AttachSubUrlItemType type11 = new AttachSubUrlItemType();
        type11.setAttachSubUrlTitle("子链1");

        String url1 = "http://wenhua.sh.cn";
        type11.setAttachSubUrlLink(url1);
        String wirelessUrl1 = "http://wenhua.sh.cn";
        type11.setAttachSubUrlWirelessLink(wirelessUrl1);
        AttachSubUrlItemType type12 = new AttachSubUrlItemType();
        type12.setAttachSubUrlTitle("子链2");
        String url2 = "http://wenhua.sh.cn";
        type12.setAttachSubUrlLink(url2);
        String wirelessUrl2 = "http://wenhua.sh.cn";
        type12.setAttachSubUrlWirelessLink(wirelessUrl2);

        attachSubUrls1.add(type11);
        attachSubUrls1.add(type12);
        type1.add(new AttachInfoType(512, "18134883345", 0, null, null)); // 参数＝null attachSubUrls1=null 测试

        GroupAttachInfoType info1 = new GroupAttachInfoType();
        info1.setGroupId(12002359);
        info1.setAttachTypes(type1);

        types.add(info1);

        // type1.add(new AttachInfoType(1, "13505165318", 0, null, null));

        request.setAttachInfos(types);

        System.out.println(JsonUtils.toJson(request));
        long start = System.currentTimeMillis();
        BaseResponse<PlaceHolderResult> response = exporter.updateAttachInfos(dataUser2, request, apiOption2);
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(response);
    }

    // @Test
    // public void testBrigeUser() throws Exception {
    // GroupConfigService exporter =
    // getServiceProxy(GroupConfigService.class, ApiExternalConstant.GROUPCONFIG_SERVICE_URL);
    // AttachInfoUserRequestType request = new AttachInfoUserRequestType();
    // BaseResponse<AttachInfoUserResponse> result = exporter.isBridgeUser(dataUser2, request, apiOption2);
    // System.out.println(result.getData()[0].getData().getSign());
    // }

}
