//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.01.29 at 05:59:02 PM EAT 
//


package org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for HeaderTwoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HeaderTwoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TermsOfPayment" type="{MDA_Common_Types}TermsOfPaymentType" minOccurs="0"/>
 *         &lt;element name="TermsOfPaymentDesc" type="{MDA_Common_Types}TermsOfPaymentDescType" minOccurs="0"/>
 *         &lt;element name="LocalBankCode" type="{MDA_Common_Types}LocalBankCodeType" minOccurs="0"/>
 *         &lt;element name="LocalBankDesc" type="{MDA_Common_Types}LocalBankCodeDescType" minOccurs="0"/>
 *         &lt;element name="ReceiptOfRemittance" type="{MDA_Common_Types}ReceiptOfRemittanceType" minOccurs="0"/>
 *         &lt;element name="RemittanceCurrency" type="{MDA_Common_Types}RemittanceCurrencyType" minOccurs="0"/>
 *         &lt;element name="RemittanceAmount" type="{MDA_Common_Types}RemittanceAmountType" minOccurs="0"/>
 *         &lt;element name="RemittanceDate" type="{MDA_Common_Types}DateType" minOccurs="0"/>
 *         &lt;element name="RemittanceReference" type="{MDA_Common_Types}RemittanceReferenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HeaderTwoType", propOrder = {
    "termsOfPayment",
    "termsOfPaymentDesc",
    "localBankCode",
    "localBankDesc",
    "receiptOfRemittance",
    "remittanceCurrency",
    "remittanceAmount",
    "remittanceDate",
    "remittanceReference"
})
public class HeaderTwoType {

    @XmlElement(name = "TermsOfPayment")
    protected String termsOfPayment;
    @XmlElement(name = "TermsOfPaymentDesc")
    protected String termsOfPaymentDesc;
    @XmlElement(name = "LocalBankCode")
    protected String localBankCode;
    @XmlElement(name = "LocalBankDesc")
    protected String localBankDesc;
    @XmlElement(name = "ReceiptOfRemittance")
    protected String receiptOfRemittance;
    @XmlElement(name = "RemittanceCurrency")
    protected String remittanceCurrency;
    @XmlElement(name = "RemittanceAmount")
    protected BigDecimal remittanceAmount;
    @XmlElement(name = "RemittanceDate")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String remittanceDate;
    @XmlElement(name = "RemittanceReference")
    protected String remittanceReference;

    /**
     * Gets the value of the termsOfPayment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTermsOfPayment() {
        return termsOfPayment;
    }

    /**
     * Sets the value of the termsOfPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTermsOfPayment(String value) {
        this.termsOfPayment = value;
    }

    /**
     * Gets the value of the termsOfPaymentDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTermsOfPaymentDesc() {
        return termsOfPaymentDesc;
    }

    /**
     * Sets the value of the termsOfPaymentDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTermsOfPaymentDesc(String value) {
        this.termsOfPaymentDesc = value;
    }

    /**
     * Gets the value of the localBankCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalBankCode() {
        return localBankCode;
    }

    /**
     * Sets the value of the localBankCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalBankCode(String value) {
        this.localBankCode = value;
    }

    /**
     * Gets the value of the localBankDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalBankDesc() {
        return localBankDesc;
    }

    /**
     * Sets the value of the localBankDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalBankDesc(String value) {
        this.localBankDesc = value;
    }

    /**
     * Gets the value of the receiptOfRemittance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiptOfRemittance() {
        return receiptOfRemittance;
    }

    /**
     * Sets the value of the receiptOfRemittance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiptOfRemittance(String value) {
        this.receiptOfRemittance = value;
    }

    /**
     * Gets the value of the remittanceCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemittanceCurrency() {
        return remittanceCurrency;
    }

    /**
     * Sets the value of the remittanceCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemittanceCurrency(String value) {
        this.remittanceCurrency = value;
    }

    /**
     * Gets the value of the remittanceAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRemittanceAmount() {
        return remittanceAmount;
    }

    /**
     * Sets the value of the remittanceAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRemittanceAmount(BigDecimal value) {
        this.remittanceAmount = value;
    }

    /**
     * Gets the value of the remittanceDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemittanceDate() {
        return remittanceDate;
    }

    /**
     * Sets the value of the remittanceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemittanceDate(String value) {
        this.remittanceDate = value;
    }

    /**
     * Gets the value of the remittanceReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemittanceReference() {
        return remittanceReference;
    }

    /**
     * Sets the value of the remittanceReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemittanceReference(String value) {
        this.remittanceReference = value;
    }

}
