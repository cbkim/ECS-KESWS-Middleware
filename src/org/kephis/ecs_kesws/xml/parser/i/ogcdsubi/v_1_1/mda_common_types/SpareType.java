//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.01.29 at 05:59:02 PM EAT 
//


package org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SpareType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SpareType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SpareHeader" type="{MDA_Common_Types}SpareElementType" minOccurs="0"/>
 *         &lt;element name="SpareItem" type="{MDA_Common_Types}SpareElementType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpareType", propOrder = {
    "spareHeader",
    "spareItem"
})
public class SpareType {

    @XmlElement(name = "SpareHeader")
    protected String spareHeader;
    @XmlElement(name = "SpareItem")
    protected String spareItem;

    /**
     * Gets the value of the spareHeader property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpareHeader() {
        return spareHeader;
    }

    /**
     * Sets the value of the spareHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpareHeader(String value) {
        this.spareHeader = value;
    }

    /**
     * Gets the value of the spareItem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpareItem() {
        return spareItem;
    }

    /**
     * Sets the value of the spareItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpareItem(String value) {
        this.spareItem = value;
    }

}
