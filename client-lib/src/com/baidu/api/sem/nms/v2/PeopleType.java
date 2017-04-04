
package com.baidu.api.sem.nms.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.cxf.jaxb.JAXBToStringBuilder;
import org.apache.cxf.jaxb.JAXBToStringStyle;


/**
 * <p>Java class for PeopleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PeopleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pid" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="aliveDays" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="cookieNum" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PeopleType", propOrder = {
    "pid",
    "name",
    "aliveDays",
    "cookieNum"
})
public class PeopleType {

    protected long pid;
    @XmlElement(required = true)
    protected String name;
    protected int aliveDays;
    protected long cookieNum;

    /**
     * Gets the value of the pid property.
     * 
     */
    public long getPid() {
        return pid;
    }

    /**
     * Sets the value of the pid property.
     * 
     */
    public void setPid(long value) {
        this.pid = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

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
     * Gets the value of the cookieNum property.
     * 
     */
    public long getCookieNum() {
        return cookieNum;
    }

    /**
     * Sets the value of the cookieNum property.
     * 
     */
    public void setCookieNum(long value) {
        this.cookieNum = value;
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
