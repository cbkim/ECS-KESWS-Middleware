//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.13 at 08:13:34 AM EAT 
//



package org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0.mda_common_types;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HeaderOneType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HeaderOneType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ForeignCurrencyCode" type="{MDA_Common_Types}CurrencyCodeType" minOccurs="0"/>
 *         &lt;element name="ForexRate" type="{MDA_Common_Types}ForexRateType" minOccurs="0"/>
 *         &lt;element name="FOBFCY" type="{MDA_Common_Types}FCYType" minOccurs="0"/>
 *         &lt;element name="FreightFCY" type="{MDA_Common_Types}FCYType" minOccurs="0"/>
 *         &lt;element name="InsuranceFCY" type="{MDA_Common_Types}FCYType" minOccurs="0"/>
 *         &lt;element name="OtherChargesFCY" type="{MDA_Common_Types}FCYType" minOccurs="0"/>
 *         &lt;element name="CIFFCY" type="{MDA_Common_Types}FCYType" minOccurs="0"/>
 *         &lt;element name="FOBNCY" type="{MDA_Common_Types}NCYType" minOccurs="0"/>
 *         &lt;element name="FreightNCY" type="{MDA_Common_Types}NCYType" minOccurs="0"/>
 *         &lt;element name="InsuranceNCY" type="{MDA_Common_Types}NCYType" minOccurs="0"/>
 *         &lt;element name="OtherChargesNCY" type="{MDA_Common_Types}NCYType" minOccurs="0"/>
 *         &lt;element name="CIFNCY" type="{MDA_Common_Types}NCYType" minOccurs="0"/>
 *         &lt;element name="INCOTerms" type="{MDA_Common_Types}INCOTermsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HeaderOneType", propOrder = {
    "foreignCurrencyCode",
    "forexRate",
    "fobfcy",
    "freightFCY",
    "insuranceFCY",
    "otherChargesFCY",
    "ciffcy",
    "fobncy",
    "freightNCY",
    "insuranceNCY",
    "otherChargesNCY",
    "cifncy",
    "incoTerms"
})
public class HeaderOneType {

    @XmlElement(name = "ForeignCurrencyCode")
    protected String foreignCurrencyCode;
    @XmlElement(name = "ForexRate")
    protected BigDecimal forexRate;
    @XmlElement(name = "FOBFCY")
    protected BigDecimal fobfcy;
    @XmlElement(name = "FreightFCY")
    protected BigDecimal freightFCY;
    @XmlElement(name = "InsuranceFCY")
    protected BigDecimal insuranceFCY;
    @XmlElement(name = "OtherChargesFCY")
    protected BigDecimal otherChargesFCY;
    @XmlElement(name = "CIFFCY")
    protected BigDecimal ciffcy;
    @XmlElement(name = "FOBNCY")
    protected BigDecimal fobncy;
    @XmlElement(name = "FreightNCY")
    protected BigDecimal freightNCY;
    @XmlElement(name = "InsuranceNCY")
    protected BigDecimal insuranceNCY;
    @XmlElement(name = "OtherChargesNCY")
    protected BigDecimal otherChargesNCY;
    @XmlElement(name = "CIFNCY")
    protected BigDecimal cifncy;
    @XmlElement(name = "INCOTerms")
    protected String incoTerms;
  /**
   * @XmlElement(name = "COMESAType")
    protected String comesaType;
    @XmlElement(name = "InvoiceNoType")
    protected String invoiceNoType;
    **/
    /**
     * Gets the value of the foreignCurrencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignCurrencyCode() {
        return foreignCurrencyCode;
    }

    /**
     * Sets the value of the foreignCurrencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignCurrencyCode(String value) {
        this.foreignCurrencyCode = value;
    }

    /**
     * Gets the value of the forexRate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getForexRate() {
        return forexRate;
    }

    /**
     * Sets the value of the forexRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setForexRate(BigDecimal value) {
        this.forexRate = value;
    }

    /**
     * Gets the value of the fobfcy property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFOBFCY() {
        return fobfcy;
    }

    /**
     * Sets the value of the fobfcy property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFOBFCY(BigDecimal value) {
        this.fobfcy = value;
    }

    /**
     * Gets the value of the freightFCY property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFreightFCY() {
        return freightFCY;
    }

    /**
     * Sets the value of the freightFCY property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFreightFCY(BigDecimal value) {
        this.freightFCY = value;
    }

    /**
     * Gets the value of the insuranceFCY property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInsuranceFCY() {
        return insuranceFCY;
    }

    /**
     * Sets the value of the insuranceFCY property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInsuranceFCY(BigDecimal value) {
        this.insuranceFCY = value;
    }

    /**
     * Gets the value of the otherChargesFCY property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOtherChargesFCY() {
        return otherChargesFCY;
    }

    /**
     * Sets the value of the otherChargesFCY property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOtherChargesFCY(BigDecimal value) {
        this.otherChargesFCY = value;
    }

    /**
     * Gets the value of the ciffcy property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCIFFCY() {
        return ciffcy;
    }

    /**
     * Sets the value of the ciffcy property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCIFFCY(BigDecimal value) {
        this.ciffcy = value;
    }

    /**
     * Gets the value of the fobncy property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFOBNCY() {
        return fobncy;
    }

    /**
     * Sets the value of the fobncy property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFOBNCY(BigDecimal value) {
        this.fobncy = value;
    }

    /**
     * Gets the value of the freightNCY property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFreightNCY() {
        return freightNCY;
    }

    /**
     * Sets the value of the freightNCY property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFreightNCY(BigDecimal value) {
        this.freightNCY = value;
    }

    /**
     * Gets the value of the insuranceNCY property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInsuranceNCY() {
        return insuranceNCY;
    }

    /**
     * Sets the value of the insuranceNCY property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInsuranceNCY(BigDecimal value) {
        this.insuranceNCY = value;
    }

    /**
     * Gets the value of the otherChargesNCY property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOtherChargesNCY() {
        return otherChargesNCY;
    }

    /**
     * Sets the value of the otherChargesNCY property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOtherChargesNCY(BigDecimal value) {
        this.otherChargesNCY = value;
    }

    /**
     * Gets the value of the cifncy property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCIFNCY() {
        return cifncy;
    }

    /**
     * Sets the value of the cifncy property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCIFNCY(BigDecimal value) {
        this.cifncy = value;
    }

    /**
     * Gets the value of the incoTerms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINCOTerms() {
        return incoTerms;
    }

    /**
     * Sets the value of the incoTerms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINCOTerms(String value) {
        this.incoTerms = value;
    }

}
