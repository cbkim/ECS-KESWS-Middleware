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
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for StandardDetailsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StandardDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceProvider" type="{MDA_Common_Types}ServiceProviderType"/>
 *         &lt;element name="ApplicationTypeCode" type="{MDA_Common_Types}ApplicationTypeCodeType"/>
 *         &lt;element name="ApplicationTypeDescription" type="{MDA_Common_Types}ApplicationTypeDescriptionType" minOccurs="0"/>
 *         &lt;element name="DocumentTypeCode" type="{MDA_Common_Types}DocumentTypeCodeType" minOccurs="0"/>
 *         &lt;element name="CMSDocumentTypeCode" type="{MDA_Common_Types}CMSDocumentTypeCodeType" minOccurs="0"/>
 *         &lt;element name="DocumentTypeDescription" type="{MDA_Common_Types}DocumentTypeDescriptionType" minOccurs="0"/>
 *         &lt;element name="ConsignmentTypeCode" type="{MDA_Common_Types}ConsignmentTypeCodeType"/>
 *         &lt;element name="ConsignmentTypeDescription" type="{MDA_Common_Types}ConsignmentTypeDescriptionType" minOccurs="0"/>
 *         &lt;element name="MDACode" type="{MDA_Common_Types}MDACodeType"/>
 *         &lt;element name="MDADescription" type="{MDA_Common_Types}MDADescriptionType" minOccurs="0"/>
 *         &lt;element name="DocumentCode" type="{MDA_Common_Types}DocumentCodeType"/>
 *         &lt;element name="DocumentDescription" type="{MDA_Common_Types}DocumentDescriptionType" minOccurs="0"/>
 *         &lt;element name="ProcessCode" type="{MDA_Common_Types}ProcessCodeType"/>
 *         &lt;element name="ProcessDescription" type="{MDA_Common_Types}ProcessDescriptionType" minOccurs="0"/>
 *         &lt;element name="ApplicationDate" type="{MDA_Common_Types}DateType"/>
 *         &lt;element name="UpdatedDate" type="{MDA_Common_Types}yyyyMMddHHssOrEmptyType" minOccurs="0"/>
 *         &lt;element name="ExpiryDate" type="{MDA_Common_Types}DateType" minOccurs="0"/>
 *         &lt;element name="AmendedDate" type="{MDA_Common_Types}DateType" minOccurs="0"/>
 *         &lt;element name="ApprovalStatus" type="{MDA_Common_Types}ApprovalStatusType" minOccurs="0"/>
 *         &lt;element name="ApprovalDate" type="{MDA_Common_Types}DateType" minOccurs="0"/>
 *         &lt;element name="FinalApprovalDate" type="{MDA_Common_Types}yyyyMMddHHssOrEmptyType" minOccurs="0"/>
 *         &lt;element name="UsedStatus" type="{MDA_Common_Types}UsedStatusType" minOccurs="0"/>
 *         &lt;element name="UsedDate" type="{MDA_Common_Types}DateType" minOccurs="0"/>
 *         &lt;element name="ApplicationRefNo" type="{MDA_Common_Types}ApplicationRefNoType"/>
 *         &lt;element name="VersionNo" type="{MDA_Common_Types}VersionNoType"/>
 *         &lt;element name="UCRNumber" type="{MDA_Common_Types}UCRNoType"/>
 *         &lt;element name="ReferencedPermitExemptionNo" type="{MDA_Common_Types}ReferencedPermitExemptionNoType" minOccurs="0"/>
 *         &lt;element name="ReferencedPermitExemptionVersionNo" type="{MDA_Common_Types}VersionNoType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StandardDetailsType", propOrder = {
    "serviceProvider",
    "applicationTypeCode",
    "applicationTypeDescription",
    "documentTypeCode",
    "cmsDocumentTypeCode",
    "documentTypeDescription",
    "consignmentTypeCode",
    "consignmentTypeDescription",
    "mdaCode",
    "mdaDescription",
    "documentCode",
    "documentDescription",
    "processCode",
    "processDescription",
    "applicationDate",
    "updatedDate",
    "expiryDate",
    "amendedDate",
    "approvalStatus",
    "approvalDate",
    "finalApprovalDate",
    "usedStatus",
    "usedDate",
    "applicationRefNo",
    "versionNo",
    "ucrNumber",
    "referencedPermitExemptionNo",
    "referencedPermitExemptionVersionNo"
})
public class StandardDetailsType {

    @XmlElement(name = "ServiceProvider", required = true)
    protected ServiceProviderType serviceProvider;
    @XmlElement(name = "ApplicationTypeCode", required = true)
    protected String applicationTypeCode;
    @XmlElement(name = "ApplicationTypeDescription")
    protected String applicationTypeDescription;
    @XmlElement(name = "DocumentTypeCode")
    protected String documentTypeCode;
    @XmlElement(name = "CMSDocumentTypeCode")
    protected String cmsDocumentTypeCode;
    @XmlElement(name = "DocumentTypeDescription")
    protected String documentTypeDescription;
    @XmlElement(name = "ConsignmentTypeCode", required = true)
    protected String consignmentTypeCode;
    @XmlElement(name = "ConsignmentTypeDescription")
    protected String consignmentTypeDescription;
    @XmlElement(name = "MDACode", required = true)
    protected String mdaCode;
    @XmlElement(name = "MDADescription")
    protected String mdaDescription;
    @XmlElement(name = "DocumentCode", required = true)
    protected String documentCode;
    @XmlElement(name = "DocumentDescription")
    protected String documentDescription;
    @XmlElement(name = "ProcessCode", required = true)
    protected String processCode;
    @XmlElement(name = "ProcessDescription")
    protected String processDescription;
    @XmlElement(name = "ApplicationDate", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String applicationDate;
    @XmlElement(name = "UpdatedDate")
    protected String updatedDate;
    @XmlElement(name = "ExpiryDate")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String expiryDate;
    @XmlElement(name = "AmendedDate")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String amendedDate;
    @XmlElement(name = "ApprovalStatus")
    protected String approvalStatus;
    @XmlElement(name = "ApprovalDate")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String approvalDate;
    @XmlElement(name = "FinalApprovalDate")
    protected String finalApprovalDate;
    @XmlElement(name = "UsedStatus")
    protected String usedStatus;
    @XmlElement(name = "UsedDate")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String usedDate;
    @XmlElement(name = "ApplicationRefNo", required = true)
    protected String applicationRefNo;
    @XmlElement(name = "VersionNo", required = true)
    protected String versionNo;
    @XmlElement(name = "UCRNumber", required = true)
    protected String ucrNumber;
    @XmlElement(name = "ReferencedPermitExemptionNo")
    protected String referencedPermitExemptionNo;
    @XmlElement(name = "ReferencedPermitExemptionVersionNo")
    protected String referencedPermitExemptionVersionNo;

    /**
     * Gets the value of the serviceProvider property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceProviderType }
     *     
     */
    public ServiceProviderType getServiceProvider() {
        return serviceProvider;
    }

    /**
     * Sets the value of the serviceProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceProviderType }
     *     
     */
    public void setServiceProvider(ServiceProviderType value) {
        this.serviceProvider = value;
    }

    /**
     * Gets the value of the applicationTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationTypeCode() {
        return applicationTypeCode;
    }

    /**
     * Sets the value of the applicationTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationTypeCode(String value) {
        this.applicationTypeCode = value;
    }

    /**
     * Gets the value of the applicationTypeDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationTypeDescription() {
        return applicationTypeDescription;
    }

    /**
     * Sets the value of the applicationTypeDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationTypeDescription(String value) {
        this.applicationTypeDescription = value;
    }

    /**
     * Gets the value of the documentTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    /**
     * Sets the value of the documentTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentTypeCode(String value) {
        this.documentTypeCode = value;
    }

    /**
     * Gets the value of the cmsDocumentTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCMSDocumentTypeCode() {
        return cmsDocumentTypeCode;
    }

    /**
     * Sets the value of the cmsDocumentTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCMSDocumentTypeCode(String value) {
        this.cmsDocumentTypeCode = value;
    }

    /**
     * Gets the value of the documentTypeDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentTypeDescription() {
        return documentTypeDescription;
    }

    /**
     * Sets the value of the documentTypeDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentTypeDescription(String value) {
        this.documentTypeDescription = value;
    }

    /**
     * Gets the value of the consignmentTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsignmentTypeCode() {
        return consignmentTypeCode;
    }

    /**
     * Sets the value of the consignmentTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsignmentTypeCode(String value) {
        this.consignmentTypeCode = value;
    }

    /**
     * Gets the value of the consignmentTypeDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsignmentTypeDescription() {
        return consignmentTypeDescription;
    }

    /**
     * Sets the value of the consignmentTypeDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsignmentTypeDescription(String value) {
        this.consignmentTypeDescription = value;
    }

    /**
     * Gets the value of the mdaCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDACode() {
        return mdaCode;
    }

    /**
     * Sets the value of the mdaCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDACode(String value) {
        this.mdaCode = value;
    }

    /**
     * Gets the value of the mdaDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDADescription() {
        return mdaDescription;
    }

    /**
     * Sets the value of the mdaDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDADescription(String value) {
        this.mdaDescription = value;
    }

    /**
     * Gets the value of the documentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentCode() {
        return documentCode;
    }

    /**
     * Sets the value of the documentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentCode(String value) {
        this.documentCode = value;
    }

    /**
     * Gets the value of the documentDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentDescription() {
        return documentDescription;
    }

    /**
     * Sets the value of the documentDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentDescription(String value) {
        this.documentDescription = value;
    }

    /**
     * Gets the value of the processCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessCode() {
        return processCode;
    }

    /**
     * Sets the value of the processCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessCode(String value) {
        this.processCode = value;
    }

    /**
     * Gets the value of the processDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessDescription() {
        return processDescription;
    }

    /**
     * Sets the value of the processDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessDescription(String value) {
        this.processDescription = value;
    }

    /**
     * Gets the value of the applicationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationDate() {
        return applicationDate;
    }

    /**
     * Sets the value of the applicationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationDate(String value) {
        this.applicationDate = value;
    }

    /**
     * Gets the value of the updatedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdatedDate() {
        return updatedDate;
    }

    /**
     * Sets the value of the updatedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdatedDate(String value) {
        this.updatedDate = value;
    }

    /**
     * Gets the value of the expiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the value of the expiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpiryDate(String value) {
        this.expiryDate = value;
    }

    /**
     * Gets the value of the amendedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmendedDate() {
        return amendedDate;
    }

    /**
     * Sets the value of the amendedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmendedDate(String value) {
        this.amendedDate = value;
    }

    /**
     * Gets the value of the approvalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * Sets the value of the approvalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApprovalStatus(String value) {
        this.approvalStatus = value;
    }

    /**
     * Gets the value of the approvalDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApprovalDate() {
        return approvalDate;
    }

    /**
     * Sets the value of the approvalDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApprovalDate(String value) {
        this.approvalDate = value;
    }

    /**
     * Gets the value of the finalApprovalDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinalApprovalDate() {
        return finalApprovalDate;
    }

    /**
     * Sets the value of the finalApprovalDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinalApprovalDate(String value) {
        this.finalApprovalDate = value;
    }

    /**
     * Gets the value of the usedStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsedStatus() {
        return usedStatus;
    }

    /**
     * Sets the value of the usedStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsedStatus(String value) {
        this.usedStatus = value;
    }

    /**
     * Gets the value of the usedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsedDate() {
        return usedDate;
    }

    /**
     * Sets the value of the usedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsedDate(String value) {
        this.usedDate = value;
    }

    /**
     * Gets the value of the applicationRefNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationRefNo() {
        return applicationRefNo;
    }

    /**
     * Sets the value of the applicationRefNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationRefNo(String value) {
        this.applicationRefNo = value;
    }

    /**
     * Gets the value of the versionNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionNo() {
        return versionNo;
    }

    /**
     * Sets the value of the versionNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionNo(String value) {
        this.versionNo = value;
    }

    /**
     * Gets the value of the ucrNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUCRNumber() {
        return ucrNumber;
    }

    /**
     * Sets the value of the ucrNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUCRNumber(String value) {
        this.ucrNumber = value;
    }

    /**
     * Gets the value of the referencedPermitExemptionNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferencedPermitExemptionNo() {
        return referencedPermitExemptionNo;
    }

    /**
     * Sets the value of the referencedPermitExemptionNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferencedPermitExemptionNo(String value) {
        this.referencedPermitExemptionNo = value;
    }

    /**
     * Gets the value of the referencedPermitExemptionVersionNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferencedPermitExemptionVersionNo() {
        return referencedPermitExemptionVersionNo;
    }

    /**
     * Sets the value of the referencedPermitExemptionVersionNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferencedPermitExemptionVersionNo(String value) {
        this.referencedPermitExemptionVersionNo = value;
    }

}
