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
 * <p>Java class for AttachmentsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AttachmentsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AttachDocumentCode" type="{MDA_Common_Types}AttachDocumentCodeType" minOccurs="0"/>
 *         &lt;element name="AttachDocumentCodeDesc" type="{MDA_Common_Types}AttachDocumentCodeDescType" minOccurs="0"/>
 *         &lt;element name="AttachDocumentRefNo" type="{MDA_Common_Types}AttachDocumentRefNoType" minOccurs="0"/>
 *         &lt;element name="AttachDocumentInternalRefNo" type="{MDA_Common_Types}AttachDocumentInternalRefNoType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttachmentsType", propOrder = {
    "attachDocumentCode",
    "attachDocumentCodeDesc",
    "attachDocumentRefNo",
    "attachDocumentInternalRefNo"
})
public class AttachmentsType {

    @XmlElement(name = "AttachDocumentCode")
    protected String attachDocumentCode;
    @XmlElement(name = "AttachDocumentCodeDesc")
    protected String attachDocumentCodeDesc;
    @XmlElement(name = "AttachDocumentRefNo")
    protected String attachDocumentRefNo;
    @XmlElement(name = "AttachDocumentInternalRefNo")
    protected String attachDocumentInternalRefNo;

    /**
     * Gets the value of the attachDocumentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachDocumentCode() {
        return attachDocumentCode;
    }

    /**
     * Sets the value of the attachDocumentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachDocumentCode(String value) {
        this.attachDocumentCode = value;
    }

    /**
     * Gets the value of the attachDocumentCodeDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachDocumentCodeDesc() {
        return attachDocumentCodeDesc;
    }

    /**
     * Sets the value of the attachDocumentCodeDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachDocumentCodeDesc(String value) {
        this.attachDocumentCodeDesc = value;
    }

    /**
     * Gets the value of the attachDocumentRefNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachDocumentRefNo() {
        return attachDocumentRefNo;
    }

    /**
     * Sets the value of the attachDocumentRefNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachDocumentRefNo(String value) {
        this.attachDocumentRefNo = value;
    }

    /**
     * Gets the value of the attachDocumentInternalRefNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachDocumentInternalRefNo() {
        return attachDocumentInternalRefNo;
    }

    /**
     * Sets the value of the attachDocumentInternalRefNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachDocumentInternalRefNo(String value) {
        this.attachDocumentInternalRefNo = value;
    }

}
