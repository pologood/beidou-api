
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
 *         &lt;element name="targetInfo" type="{https://api.baidu.com/sem/nms/v2}TargetInfoType"/>
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
    "targetInfo"
})
@XmlRootElement(name = "setTargetInfoRequest")
public class SetTargetInfoRequest {

    @XmlElement(required = true)
    protected TargetInfoType targetInfo;

    /**
     * Gets the value of the targetInfo property.
     * 
     * @return
     *     possible object is
     *     {@link TargetInfoType }
     *     
     */
    public TargetInfoType getTargetInfo() {
        return targetInfo;
    }

    /**
     * Sets the value of the targetInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetInfoType }
     *     
     */
    public void setTargetInfo(TargetInfoType value) {
        this.targetInfo = value;
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
