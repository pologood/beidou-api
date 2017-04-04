
package com.baidu.api.sem.nms.v2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.apache.cxf.jaxb.JAXBToStringBuilder;
import org.apache.cxf.jaxb.JAXBToStringStyle;


/**
 * <p>Java class for RegionConfigType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegionConfigType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="groupId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="allRegion" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="regionList" type="{https://api.baidu.com/sem/nms/v2}RegionItemType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegionConfigType", propOrder = {
    "groupId",
    "allRegion",
    "regionList"
})
public class RegionConfigType {

    protected long groupId;
    protected boolean allRegion;
    protected List<RegionItemType> regionList;

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
     * Gets the value of the allRegion property.
     * 
     */
    public boolean isAllRegion() {
        return allRegion;
    }

    /**
     * Sets the value of the allRegion property.
     * 
     */
    public void setAllRegion(boolean value) {
        this.allRegion = value;
    }

    /**
     * Gets the value of the regionList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the regionList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegionList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegionItemType }
     * 
     * 
     */
    public List<RegionItemType> getRegionList() {
        if (regionList == null) {
            regionList = new ArrayList<RegionItemType>();
        }
        return this.regionList;
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
