
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
 *         &lt;element name="excludeSite" type="{https://api.baidu.com/sem/nms/v2}ExcludeSiteType"/>
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
    "excludeSite"
})
@XmlRootElement(name = "setExcludeSiteRequest")
public class SetExcludeSiteRequest {

    @XmlElement(required = true)
    protected ExcludeSiteType excludeSite;

    /**
     * Gets the value of the excludeSite property.
     * 
     * @return
     *     possible object is
     *     {@link ExcludeSiteType }
     *     
     */
    public ExcludeSiteType getExcludeSite() {
        return excludeSite;
    }

    /**
     * Sets the value of the excludeSite property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExcludeSiteType }
     *     
     */
    public void setExcludeSite(ExcludeSiteType value) {
        this.excludeSite = value;
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
