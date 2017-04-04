
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
 *         &lt;element name="regionConfig" type="{https://api.baidu.com/sem/nms/v2}RegionConfigType"/>
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
    "regionConfig"
})
@XmlRootElement(name = "setRegionConfigRequest")
public class SetRegionConfigRequest {

    @XmlElement(required = true)
    protected RegionConfigType regionConfig;

    /**
     * Gets the value of the regionConfig property.
     * 
     * @return
     *     possible object is
     *     {@link RegionConfigType }
     *     
     */
    public RegionConfigType getRegionConfig() {
        return regionConfig;
    }

    /**
     * Sets the value of the regionConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegionConfigType }
     *     
     */
    public void setRegionConfig(RegionConfigType value) {
        this.regionConfig = value;
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