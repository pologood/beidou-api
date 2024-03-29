
/*
 * 
 */

package com.baidu.api.sem.nms.v2;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.9
 * Wed Jan 11 13:51:43 CST 2012
 * Generated source version: 2.2.9
 * 
 */


@WebServiceClient(name = "CampaignService", 
                  wsdlLocation = "https://api.baidu.com/sem/nms/v2/CampaignService?wsdl",
                  targetNamespace = "https://api.baidu.com/sem/nms/v2") 
public class CampaignService_Service extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("https://api.baidu.com/sem/nms/v2", "CampaignService");
    public final static QName CampaignService = new QName("https://api.baidu.com/sem/nms/v2", "CampaignService");
    static {
        URL url = null;
        try {
            url = new URL("https://api.baidu.com/sem/nms/v2/CampaignService?wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from https://api.baidu.com/sem/nms/v2/CampaignService?wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public CampaignService_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public CampaignService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CampaignService_Service() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     * 
     * @return
     *     returns CampaignService
     */
    @WebEndpoint(name = "CampaignService")
    public CampaignService getCampaignService() {
        return super.getPort(CampaignService, CampaignService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CampaignService
     */
    @WebEndpoint(name = "CampaignService")
    public CampaignService getCampaignService(WebServiceFeature... features) {
        return super.getPort(CampaignService, CampaignService.class, features);
    }

}
