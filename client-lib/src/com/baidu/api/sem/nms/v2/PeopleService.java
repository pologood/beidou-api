package com.baidu.api.sem.nms.v2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.9
 * Mon Dec 26 20:00:19 CST 2011
 * Generated source version: 2.2.9
 * 
 */
 
@WebService(targetNamespace = "https://api.baidu.com/sem/nms/v2", name = "PeopleService")
@XmlSeeAlso({com.baidu.api.sem.common.v2.ObjectFactory.class, ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface PeopleService {

    @WebResult(name = "getPeopleResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/PeopleService/getPeople")
    public GetPeopleResponse getPeople(
        @WebParam(partName = "parameters", name = "getPeopleRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        GetPeopleRequest parameters
    );

    @WebResult(name = "getAllPeopleResponse", targetNamespace = "https://api.baidu.com/sem/nms/v2", partName = "parameters")
    @WebMethod(action = "https://api.baidu.com/sem/nms/v2/PeopleService/getAllPeople")
    public GetAllPeopleResponse getAllPeople(
        @WebParam(partName = "parameters", name = "getAllPeopleRequest", targetNamespace = "https://api.baidu.com/sem/nms/v2")
        GetAllPeopleRequest parameters
    );
}
