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
 * <p>Java class for ApprovalHistoryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApprovalHistoryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StageNo" type="{MDA_Common_Types}StageNoType" minOccurs="0"/>
 *         &lt;element name="StepCode" type="{MDA_Common_Types}StepCodeType" minOccurs="0"/>
 *         &lt;element name="MDACode" type="{MDA_Common_Types}MDACodeType" minOccurs="0"/>
 *         &lt;element name="RoleCode" type="{MDA_Common_Types}RoleCodeType" minOccurs="0"/>
 *         &lt;element name="Status" type="{MDA_Common_Types}StatusType" minOccurs="0"/>
 *         &lt;element name="UserId" type="{MDA_Common_Types}UserIDType" minOccurs="0"/>
 *         &lt;element name="UpdatedDate" type="{MDA_Common_Types}yyyyMMddHHssOrEmptyType" minOccurs="0"/>
 *         &lt;element name="PremiseInspection" type="{MDA_Common_Types}PremiseInspectionType" minOccurs="0"/>
 *         &lt;element name="ExaminationRequired" type="{MDA_Common_Types}ExaminationRequiredType" minOccurs="0"/>
 *         &lt;element name="TechnicalRejection" type="{MDA_Common_Types}TechnicalRejectionType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApprovalHistoryType", propOrder = {
    "stageNo",
    "stepCode",
    "mdaCode",
    "roleCode",
    "status",
    "userId",
    "updatedDate",
    "premiseInspection",
    "examinationRequired",
    "technicalRejection"
})
public class ApprovalHistoryType {

    @XmlElement(name = "StageNo")
    protected String stageNo;
    @XmlElement(name = "StepCode")
    protected String stepCode;
    @XmlElement(name = "MDACode")
    protected String mdaCode;
    @XmlElement(name = "RoleCode")
    protected String roleCode;
    @XmlElement(name = "Status")
    protected String status;
    @XmlElement(name = "UserId")
    protected String userId;
    @XmlElement(name = "UpdatedDate")
    protected String updatedDate;
    @XmlElement(name = "PremiseInspection")
    protected String premiseInspection;
    @XmlElement(name = "ExaminationRequired")
    protected String examinationRequired;
    @XmlElement(name = "TechnicalRejection")
    protected String technicalRejection;

    /**
     * Gets the value of the stageNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStageNo() {
        return stageNo;
    }

    /**
     * Sets the value of the stageNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStageNo(String value) {
        this.stageNo = value;
    }

    /**
     * Gets the value of the stepCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStepCode() {
        return stepCode;
    }

    /**
     * Sets the value of the stepCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStepCode(String value) {
        this.stepCode = value;
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
     * Gets the value of the roleCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * Sets the value of the roleCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoleCode(String value) {
        this.roleCode = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
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
     * Gets the value of the premiseInspection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPremiseInspection() {
        return premiseInspection;
    }

    /**
     * Sets the value of the premiseInspection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPremiseInspection(String value) {
        this.premiseInspection = value;
    }

    /**
     * Gets the value of the examinationRequired property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExaminationRequired() {
        return examinationRequired;
    }

    /**
     * Sets the value of the examinationRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExaminationRequired(String value) {
        this.examinationRequired = value;
    }

    /**
     * Gets the value of the technicalRejection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTechnicalRejection() {
        return technicalRejection;
    }

    /**
     * Sets the value of the technicalRejection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTechnicalRejection(String value) {
        this.technicalRejection = value;
    }

}
