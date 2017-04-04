
package com.baidu.api.sem.nms.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.cxf.jaxb.JAXBToStringBuilder;
import org.apache.cxf.jaxb.JAXBToStringStyle;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="excludeIp" type="{https://api.baidu.com/sem/nms/v2}ExcludeIpType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "excludeIp"
})
@XmlRootElement(name = "setExcludeIpRequest")
public class SetExcludeIpRequest {

    @XmlElement(required = true)
    protected ExcludeIpType excludeIp;

    /**
     * Gets the value of the excludeIp property.
     * 
     * @return
     *     possible object is
     *     {@link ExcludeIpType }
     *     
     */
    public ExcludeIpType getExcludeIp() {
        return excludeIp;
    }

    /**
     * Sets the value of the excludeIp property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExcludeIpType }
     *     
     */
    public void setExcludeIp(ExcludeIpType value) {
        this.excludeIp = value;
    }

    /**
     * Generates a String representation of the contents of this type.
     * This is an extension method, produced by the 'ts' xjc plugin
     * 
     */
    @Override
    public String toString() {
        return JAXBToStringBuilder.valueOf(this, JAXBToStringStyle.SIMPLE_STYLE);
    }

}
