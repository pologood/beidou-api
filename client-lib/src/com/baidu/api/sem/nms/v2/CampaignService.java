package com.baidu.api.sem.nms.v2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.9
 * Wed Jan 11 13:51:43 CST 2012
 * Generated source version: 2.2.9
 * 
 */
 
@WebService(targetNamespace = "https://api.baidu.com/sem/nms/v2", name = "CampaignService")
@XmlSeeAlso({com.baidu.api.sem.common.v2.ObjectFactory.class, ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface CampaignService {

    @WebResult(name = "getCampaignIdResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/CampaignService/getCampaignId")
    public GetCampaignIdResponse getCampaignId(
        @WebParam(partName = "parameters", name = "getCampaignIdRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        GetCampaignIdRequest parameters
    );

    @WebResult(name = "getCampaignResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/CampaignService/getCampaign")
    public GetCampaignResponse getCampaign(
        @WebParam(partName = "parameters", name = "getCampaignRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        GetCampaignRequest parameters
    );

    @WebResult(name = "updateCampaignResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/CampaignService/updateCampaign")
    public UpdateCampaignResponse updateCampaign(
        @WebParam(partName = "parameters", name = "updateCampaignRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        UpdateCampaignRequest parameters
    );

    @WebResult(name = "getCampaignByCampaignIdResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/CampaignService/getCampaignByCampaignId")
    public GetCampaignByCampaignIdResponse getCampaignByCampaignId(
        @WebParam(partName = "parameters", name = "getCampaignByCampaignIdRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        GetCampaignByCampaignIdRequest parameters
    );

    @WebResult(name = "addCampaignResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/CampaignService/addCampaign")
    public AddCampaignResponse addCampaign(
        @WebParam(partName = "parameters", name = "addCampaignRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        AddCampaignRequest parameters
    );

}
