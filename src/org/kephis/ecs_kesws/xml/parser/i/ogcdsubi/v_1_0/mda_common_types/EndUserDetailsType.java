//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.13 at 08:13:34 AM EAT 
//


package org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0.mda_common_types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EndUserDetailsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EndUserDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RegNo" type="{MDA_Common_Types}RegNoType" minOccurs="0"/>
 *         &lt;element name="Name" type="{MDA_Common_Types}NameType" minOccurs="0"/>
 *         &lt;element name="PhysicalAddress" type="{MDA_Common_Types}PhysicalAddressType" minOccurs="0"/>
 *         &lt;element name="TelFax" type="{MDA_Common_Types}TelephoneNumberType" minOccurs="0"/>
 *         &lt;element name="UseGeneralDescription" type="{MDA_Common_Types}UseGeneralDescriptionType" minOccurs="0"/>
 *         &lt;element name="UseDetails" type="{MDA_Common_Types}UseDetailsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EndUserDetailsType", propOrder = {
    "regNo",
    "name",
    "physicalAddress",
    "telFax",
    "useGeneralDescription",
    "useDetails"
})
public class EndUserDetailsType {

    @XmlElement(name = "RegNo")
    protected String regNo;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "PhysicalAddress")
    protected String physicalAddress;
    @XmlElement(name = "TelFax")
    protected String telFax;
    @XmlElement(name = "UseGeneralDescription")
    protected String useGeneralDescription;
    @XmlElement(name = "UseDetails")
    protected String useDetails;

    /**
     * Gets the value of the regNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegNo() {
        return regNo;
    }

    /**
     * Sets the value of the regNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegNo(String value) {
        this.regNo = value;
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
     * Gets the value of the physicalAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhysicalAddress() {
        return physicalAddress;
    }

    /**
     * Sets the value of the physicalAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhysicalAddress(String value) {
        this.physicalAddress = value;
    }

    /**
     * Gets the value of the telFax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelFax() {
        return telFax;
    }

    /**
     * Sets the value of the telFax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelFax(String value) {
        this.telFax = value;
    }

    /**
     * Gets the value of the useGeneralDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUseGeneralDescription() {
        return useGeneralDescription;
    }

    /**
     * Sets the value of the useGeneralDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUseGeneralDescription(String value) {
        this.useGeneralDescription = value;
    }

    /**
     * Gets the value of the useDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUseDetails() {
        return useDetails;
    }

    /**
     * Sets the value of the useDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUseDetails(String value) {
        this.useDetails = value;
    }

}
