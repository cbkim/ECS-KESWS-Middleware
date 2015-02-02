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
 * <p>Java class for HdrNonStandardDetailsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HdrNonStandardDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CountryOfConsignment" type="{MDA_Common_Types}CountryType" minOccurs="0"/>
 *         &lt;element name="CountryOfConsignmentDesc" type="{MDA_Common_Types}CountryDescType2" minOccurs="0"/>
 *         &lt;element name="CountryOfReceipt" type="{MDA_Common_Types}CountryType" minOccurs="0"/>
 *         &lt;element name="CountryOfReceiptDesc" type="{MDA_Common_Types}CountryDescType2" minOccurs="0"/>
 *         &lt;element name="Manufacturer" type="{MDA_Common_Types}ManufacturerType" minOccurs="0"/>
 *         &lt;element name="NameOfContactInGhana" type="{MDA_Common_Types}ContactType" minOccurs="0"/>
 *         &lt;element name="TelNoOfContactInGhana" type="{MDA_Common_Types}ContactType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HdrNonStandardDetailsType", propOrder = {
    "countryOfConsignment",
    "countryOfConsignmentDesc",
    "countryOfReceipt",
    "countryOfReceiptDesc",
    "manufacturer",
    "nameOfContactInGhana",
    "telNoOfContactInGhana"
})
public class HdrNonStandardDetailsType {

    @XmlElement(name = "CountryOfConsignment")
    protected String countryOfConsignment;
    @XmlElement(name = "CountryOfConsignmentDesc")
    protected String countryOfConsignmentDesc;
    @XmlElement(name = "CountryOfReceipt")
    protected String countryOfReceipt;
    @XmlElement(name = "CountryOfReceiptDesc")
    protected String countryOfReceiptDesc;
    @XmlElement(name = "Manufacturer")
    protected String manufacturer;
    @XmlElement(name = "NameOfContactInGhana")
    protected String nameOfContactInGhana;
    @XmlElement(name = "TelNoOfContactInGhana")
    protected String telNoOfContactInGhana;

    /**
     * Gets the value of the countryOfConsignment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryOfConsignment() {
        return countryOfConsignment;
    }

    /**
     * Sets the value of the countryOfConsignment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryOfConsignment(String value) {
        this.countryOfConsignment = value;
    }

    /**
     * Gets the value of the countryOfConsignmentDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryOfConsignmentDesc() {
        return countryOfConsignmentDesc;
    }

    /**
     * Sets the value of the countryOfConsignmentDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryOfConsignmentDesc(String value) {
        this.countryOfConsignmentDesc = value;
    }

    /**
     * Gets the value of the countryOfReceipt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryOfReceipt() {
        return countryOfReceipt;
    }

    /**
     * Sets the value of the countryOfReceipt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryOfReceipt(String value) {
        this.countryOfReceipt = value;
    }

    /**
     * Gets the value of the countryOfReceiptDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryOfReceiptDesc() {
        return countryOfReceiptDesc;
    }

    /**
     * Sets the value of the countryOfReceiptDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryOfReceiptDesc(String value) {
        this.countryOfReceiptDesc = value;
    }

    /**
     * Gets the value of the manufacturer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the value of the manufacturer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturer(String value) {
        this.manufacturer = value;
    }

    /**
     * Gets the value of the nameOfContactInGhana property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOfContactInGhana() {
        return nameOfContactInGhana;
    }

    /**
     * Sets the value of the nameOfContactInGhana property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOfContactInGhana(String value) {
        this.nameOfContactInGhana = value;
    }

    /**
     * Gets the value of the telNoOfContactInGhana property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelNoOfContactInGhana() {
        return telNoOfContactInGhana;
    }

    /**
     * Sets the value of the telNoOfContactInGhana property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelNoOfContactInGhana(String value) {
        this.telNoOfContactInGhana = value;
    }

}
