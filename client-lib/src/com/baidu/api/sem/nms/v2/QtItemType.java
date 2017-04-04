
package com.baidu.api.sem.nms.v2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.cxf.jaxb.JAXBToStringBuilder;
import org.apache.cxf.jaxb.JAXBToStringStyle;


/**
 * <p>Java class for QtItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QtItemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="aliveDays" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="qtWordList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QtItemType", propOrder = {
    "aliveDays",
    "qtWordList"
})
public class QtItemType {

    protected int aliveDays;
    @XmlElement(required = true)
    protected List<String> qtWordList;

    /**
     * Gets the value of the aliveDays property.
     * 
     */
    public int getAliveDays() {
        return aliveDays;
    }

    /**
     * Sets the value of the aliveDays property.
     * 
     */
    public void setAliveDays(int value) {
        this.aliveDays = value;
    }

    /**
     * Gets the value of the qtWordList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the qtWordList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQtWordList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getQtWordList() {
        if (qtWordList == null) {
            qtWordList = new ArrayList<String>();
        }
        return this.qtWordList;
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
