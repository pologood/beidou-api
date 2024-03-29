
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
 * Thu Jan 12 16:44:29 CST 2012
 * Generated source version: 2.2.9
 * 
 */


@WebServiceClient(name = "AdService", 
                  wsdlLocation = "https://api.baidu.com/sem/nms/v2/AdService?wsdl",
                  targetNamespace = "https://api.baidu.com/sem/nms/v2") 
public class AdService_Service extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("https://api.baidu.com/sem/nms/v2", "AdService");
    public final static QName AdService = new QName("https://api.baidu.com/sem/nms/v2", "AdService");
    static {
        URL url = null;
        try {
            url = new URL("https://api.baidu.com/sem/nms/v2/AdService?wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from https://api.baidu.com/sem/nms/v2/AdService?wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public AdService_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public AdService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AdService_Service() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     * 
     * @return
     *     returns AdService
     */
    @WebEndpoint(name = "AdService")
    public AdService getAdService() {
        return super.getPort(AdService, AdService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AdService
     */
    @WebEndpoint(name = "AdService")
    public AdService getAdService(WebServiceFeature... features) {
        return super.getPort(AdService, AdService.class, features);
    }

}
