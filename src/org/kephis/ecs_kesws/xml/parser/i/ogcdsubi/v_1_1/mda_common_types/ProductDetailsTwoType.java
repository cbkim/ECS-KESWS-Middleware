//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.01.29 at 05:59:02 PM EAT 
//


package org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProductDetailsTwoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProductDetailsTwoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EndUserDetails" type="{MDA_Common_Types}EndUserDetailsType" minOccurs="0"/>
 *         &lt;element name="RiskClassification" type="{MDA_Common_Types}RiskClassificationType" minOccurs="0"/>
 *         &lt;element name="RiskDetails" type="{MDA_Common_Types}RiskDetailsType" minOccurs="0"/>
 *         &lt;element name="SafetyClassification" type="{MDA_Common_Types}RiskClassificationType" minOccurs="0"/>
 *         &lt;element name="SafetyDetails" type="{MDA_Common_Types}RiskDetailsType" minOccurs="0"/>
 *         &lt;element name="RiskSafetyRemarks" type="{MDA_Common_Types}RiskSafetyRemarksType" minOccurs="0"/>
 *         &lt;element name="SamplingRequirement" type="{MDA_Common_Types}SamplingRequirementType" minOccurs="0"/>
 *         &lt;element name="SamplingResults" type="{MDA_Common_Types}SamplingResultsType" minOccurs="0"/>
 *         &lt;element name="ApplicantRemarks" type="{MDA_Common_Types}RemarksType" minOccurs="0"/>
 *         &lt;element name="MDARemarks" type="{MDA_Common_Types}RemarksType" minOccurs="0"/>
 *         &lt;element name="CustomsRemarks" type="{MDA_Common_Types}RemarksType" minOccurs="0"/>
 *         &lt;element name="MDAItemApprovalFlag" type="{MDA_Common_Types}MDAItemApprovalFlagType" minOccurs="0"/>
 *         &lt;element name="Attachments" type="{MDA_Common_Types}AttachmentsType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductDetailsTwoType", propOrder = {
    "endUserDetails",
    "riskClassification",
    "riskDetails",
    "safetyClassification",
    "safetyDetails",
    "riskSafetyRemarks",
    "samplingRequirement",
    "samplingResults",
    "applicantRemarks",
    "mdaRemarks",
    "customsRemarks",
    "mdaItemApprovalFlag",
    "attachments"
})
public class ProductDetailsTwoType {

    @XmlElement(name = "EndUserDetails")
    protected EndUserDetailsType endUserDetails;
    @XmlElement(name = "RiskClassification")
    protected String riskClassification;
    @XmlElement(name = "RiskDetails")
    protected String riskDetails;
    @XmlElement(name = "SafetyClassification")
    protected String safetyClassification;
    @XmlElement(name = "SafetyDetails")
    protected String safetyDetails;
    @XmlElement(name = "RiskSafetyRemarks")
    protected String riskSafetyRemarks;
    @XmlElement(name = "SamplingRequirement")
    protected String samplingRequirement;
    @XmlElement(name = "SamplingResults")
    protected String samplingResults;
    @XmlElement(name = "ApplicantRemarks")
    protected String applicantRemarks;
    @XmlElement(name = "MDARemarks")
    protected String mdaRemarks;
    @XmlElement(name = "CustomsRemarks")
    protected String customsRemarks;
    @XmlElement(name = "MDAItemApprovalFlag")
    protected String mdaItemApprovalFlag;
    @XmlElement(name = "Attachments")
    protected List<AttachmentsType> attachments;

    /**
     * Gets the value of the endUserDetails property.
     * 
     * @return
     *     possible object is
     *     {@link EndUserDetailsType }
     *     
     */
    public EndUserDetailsType getEndUserDetails() {
        return endUserDetails;
    }

    /**
     * Sets the value of the endUserDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndUserDetailsType }
     *     
     */
    public void setEndUserDetails(EndUserDetailsType value) {
        this.endUserDetails = value;
    }

    /**
     * Gets the value of the riskClassification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiskClassification() {
        return riskClassification;
    }

    /**
     * Sets the value of the riskClassification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiskClassification(String value) {
        this.riskClassification = value;
    }

    /**
     * Gets the value of the riskDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiskDetails() {
        return riskDetails;
    }

    /**
     * Sets the value of the riskDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiskDetails(String value) {
        this.riskDetails = value;
    }

    /**
     * Gets the value of the safetyClassification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSafetyClassification() {
        return safetyClassification;
    }

    /**
     * Sets the value of the safetyClassification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSafetyClassification(String value) {
        this.safetyClassification = value;
    }

    /**
     * Gets the value of the safetyDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSafetyDetails() {
        return safetyDetails;
    }

    /**
     * Sets the value of the safetyDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSafetyDetails(String value) {
        this.safetyDetails = value;
    }

    /**
     * Gets the value of the riskSafetyRemarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiskSafetyRemarks() {
        return riskSafetyRemarks;
    }

    /**
     * Sets the value of the riskSafetyRemarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiskSafetyRemarks(String value) {
        this.riskSafetyRemarks = value;
    }

    /**
     * Gets the value of the samplingRequirement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSamplingRequirement() {
        return samplingRequirement;
    }

    /**
     * Sets the value of the samplingRequirement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSamplingRequirement(String value) {
        this.samplingRequirement = value;
    }

    /**
     * Gets the value of the samplingResults property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSamplingResults() {
        return samplingResults;
    }

    /**
     * Sets the value of the samplingResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSamplingResults(String value) {
        this.samplingResults = value;
    }

    /**
     * Gets the value of the applicantRemarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicantRemarks() {
        return applicantRemarks;
    }

    /**
     * Sets the value of the applicantRemarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicantRemarks(String value) {
        this.applicantRemarks = value;
    }

    /**
     * Gets the value of the mdaRemarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDARemarks() {
        return mdaRemarks;
    }

    /**
     * Sets the value of the mdaRemarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDARemarks(String value) {
        this.mdaRemarks = value;
    }

    /**
     * Gets the value of the customsRemarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomsRemarks() {
        return customsRemarks;
    }

    /**
     * Sets the value of the customsRemarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomsRemarks(String value) {
        this.customsRemarks = value;
    }

    /**
     * Gets the value of the mdaItemApprovalFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDAItemApprovalFlag() {
        return mdaItemApprovalFlag;
    }

    /**
     * Sets the value of the mdaItemApprovalFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDAItemApprovalFlag(String value) {
        this.mdaItemApprovalFlag = value;
    }

    /**
     * Gets the value of the attachments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attachments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttachments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttachmentsType }
     * 
     * 
     */
    public List<AttachmentsType> getAttachments() {
        if (attachments == null) {
            attachments = new ArrayList<AttachmentsType>();
        }
        return this.attachments;
    }

}
