
package com.baidu.api.sem.nms.v2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.apache.cxf.jaxb.JAXBToStringBuilder;
import org.apache.cxf.jaxb.JAXBToStringStyle;


/**
 * <p>Java class for TradeSitePriceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TradeSitePriceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="groupId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="tradePriceList" type="{https://api.baidu.com/sem/nms/v2}TradePriceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sitePriceList" type="{https://api.baidu.com/sem/nms/v2}SitePriceType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TradeSitePriceType", propOrder = {
    "groupId",
    "tradePriceList",
    "sitePriceList"
})
public class TradeSitePriceType {

    protected long groupId;
    protected List<TradePriceType> tradePriceList;
    protected List<SitePriceType> sitePriceList;

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
     * Gets the value of the tradePriceList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tradePriceList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTradePriceList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TradePriceType }
     * 
     * 
     */
    public List<TradePriceType> getTradePriceList() {
        if (tradePriceList == null) {
            tradePriceList = new ArrayList<TradePriceType>();
        }
        return this.tradePriceList;
    }

    /**
     * Gets the value of the sitePriceList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sitePriceList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSitePriceList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SitePriceType }
     * 
     * 
     */
    public List<SitePriceType> getSitePriceList() {
        if (sitePriceList == null) {
            sitePriceList = new ArrayList<SitePriceType>();
        }
        return this.sitePriceList;
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
