package com.baidu.api.sem.nms.v2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.9
 * Thu Jan 12 16:44:29 CST 2012
 * Generated source version: 2.2.9
 * 
 */
 
@WebService(targetNamespace = "https://api.baidu.com/sem/nms/v2", name = "AdService")
@XmlSeeAlso({com.baidu.api.sem.common.v2.ObjectFactory.class, ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface AdService {

    @WebResult(name = "addAdResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/AdService/addAd")
    public AddAdResponse addAd(
        @WebParam(partName = "parameters", name = "addAdRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        AddAdRequest parameters
    );

    @WebResult(name = "getAdByGroupIdResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/AdService/getAdByGroupId")
    public GetAdByGroupIdResponse getAdByGroupId(
        @WebParam(partName = "parameters", name = "getAdByGroupIdRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        GetAdByGroupIdRequest parameters
    );

    @WebResult(name = "updateAdResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/AdService/updateAd")
    public UpdateAdResponse updateAd(
        @WebParam(partName = "parameters", name = "updateAdRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        UpdateAdRequest parameters
    );

    @WebResult(name = "deleteAdResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/AdService/deleteAd")
    public DeleteAdResponse deleteAd(
        @WebParam(partName = "parameters", name = "deleteAdRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        DeleteAdRequest parameters
    );

    @WebResult(name = "getAdIdByGroupIdResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/AdService/getAdIdByGroupId")
    public GetAdIdByGroupIdResponse getAdIdByGroupId(
        @WebParam(partName = "parameters", name = "getAdIdByGroupIdRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        GetAdIdByGroupIdRequest parameters
    );

    @WebResult(name = "copyAdResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/AdService/copyAd")
    public CopyAdResponse copyAd(
        @WebParam(partName = "parameters", name = "copyAdRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        CopyAdRequest parameters
    );

    @WebResult(name = "replaceAdResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/AdService/replaceAd")
    public ReplaceAdResponse replaceAd(
        @WebParam(partName = "parameters", name = "replaceAdRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        ReplaceAdRequest parameters
    );

    @WebResult(name = "setAdStatusResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/AdService/setAdStatus")
    public SetAdStatusResponse setAdStatus(
        @WebParam(partName = "parameters", name = "setAdStatusRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        SetAdStatusRequest parameters
    );

    @WebResult(name = "getAdByAdIdResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/AdService/getAdByAdId")
    public GetAdByAdIdResponse getAdByAdId(
        @WebParam(partName = "parameters", name = "getAdByAdIdRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        GetAdByAdIdRequest parameters
    );
}
