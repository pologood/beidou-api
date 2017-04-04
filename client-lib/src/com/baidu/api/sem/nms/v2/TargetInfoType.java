
package com.baidu.api.sem.nms.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.apache.cxf.jaxb.JAXBToStringBuilder;
import org.apache.cxf.jaxb.JAXBToStringStyle;


/**
 * <p>Java class for TargetInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TargetInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="targetType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="groupId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="ctItem" type="{https://api.baidu.com/sem/nms/v2}CtItemType" minOccurs="0"/>
 *         &lt;element name="rtItem" type="{https://api.baidu.com/sem/nms/v2}RtItemType" minOccurs="0"/>
 *         &lt;element name="qtItem" type="{https://api.baidu.com/sem/nms/v2}QtItemType" minOccurs="0"/>
 *         &lt;element name="vtItem" type="{https://api.baidu.com/sem/nms/v2}VtItemType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetInfoType", propOrder = {
    "targetType",
    "groupId",
    "ctItem",
    "rtItem",
    "qtItem",
    "vtItem"
})
public class TargetInfoType {

    protected int targetType;
    protected long groupId;
    protected CtItemType ctItem;
    protected RtItemType rtItem;
    protected QtItemType qtItem;
    protected VtItemType vtItem;

    /**
     * Gets the value of the targetType property.
     * 
     */
    public int getTargetType() {
        return targetType;
    }

    /**
     * Sets the value of the targetType property.
     * 
     */
    public void setTargetType(int value) {
        this.targetType = value;
    }

    /**
     * Gets the value of the groupId property.
     * 
     */
    public long getGroupId() {
        return groupId;
    }

    /**
     * Sets the value of the groupId property.
     * 
     */
    public void setGroupId(long value) {
        this.groupId = value;
    }

    /**
     * Gets the value of the ctItem property.
     * 
     * @return
     *     possible object is
     *     {@link CtItemType }
     *     
     */
    public CtItemType getCtItem() {
        return ctItem;
    }

    /**
     * Sets the value of the ctItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link CtItemType }
     *     
     */
    public void setCtItem(CtItemType value) {
        this.ctItem = value;
    }

    /**
     * Gets the value of the rtItem property.
     * 
     * @return
     *     possible object is
     *     {@link RtItemType }
     *     
     */
    public RtItemType getRtItem() {
        return rtItem;
    }

    /**
     * Sets the value of the rtItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link RtItemType }
     *     
     */
    public void setRtItem(RtItemType value) {
        this.rtItem = value;
    }

    /**
     * Gets the value of the qtItem property.
     * 
     * @return
     *     possible object is
     *     {@link QtItemType }
     *     
     */
    public QtItemType getQtItem() {
        return qtItem;
    }

    /**
     * Sets the value of the qtItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link QtItemType }
     *     
     */
    public void setQtItem(QtItemType value) {
        this.qtItem = value;
    }

    /**
     * Gets the value of the vtItem property.
     * 
     * @return
     *     possible object is
     *     {@link VtItemType }
     *     
     */
    public VtItemType getVtItem() {
        return vtItem;
    }

    /**
     * Sets the value of the vtItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link VtItemType }
     *     
     */
    public void setVtItem(VtItemType value) {
        this.vtItem = value;
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
