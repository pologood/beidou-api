
package com.baidu.api.sem.nms.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.apache.cxf.jaxb.JAXBToStringBuilder;
import org.apache.cxf.jaxb.JAXBToStringStyle;


/**
 * <p>Java class for RtRelationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RtRelationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="relationType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fcPlanId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="fcUnitId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="fcPlanName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fcUnitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RtRelationType", propOrder = {
    "relationType",
    "fcPlanId",
    "fcUnitId",
    "fcPlanName",
    "fcUnitName"
})
public class RtRelationType {

    protected int relationType;
    protected long fcPlanId;
    protected Long fcUnitId;
    protected String fcPlanName;
    protected String fcUnitName;

    /**
     * Gets the value of the relationType property.
     * 
     */
    public int getRelationType() {
        return relationType;
    }

    /**
     * Sets the value of the relationType property.
     * 
     */
    public void setRelationType(int value) {
        this.relationType = value;
    }

    /**
     * Gets the value of the fcPlanId property.
     * 
     */
    public long getFcPlanId() {
        return fcPlanId;
    }

    /**
     * Sets the value of the fcPlanId property.
     * 
     */
    public void setFcPlanId(long value) {
        this.fcPlanId = value;
    }

    /**
     * Gets the value of the fcUnitId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFcUnitId() {
        return fcUnitId;
    }

    /**
     * Sets the value of the fcUnitId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFcUnitId(Long value) {
        this.fcUnitId = value;
    }

    /**
     * Gets the value of the fcPlanName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFcPlanName() {
        return fcPlanName;
    }

    /**
     * Sets the value of the fcPlanName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFcPlanName(String value) {
        this.fcPlanName = value;
    }

    /**
     * Gets the value of the fcUnitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFcUnitName() {
        return fcUnitName;
    }

    /**
     * Sets the value of the fcUnitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFcUnitName(String value) {
        this.fcUnitName = value;
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
