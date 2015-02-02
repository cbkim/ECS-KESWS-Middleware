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
 * <p>Java class for DocumentSummaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocumentSummaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IssuedDateTime" type="{MDA_Common_Types}TimeStampType"/>
 *         &lt;element name="SummaryPageUrl" type="{MDA_Common_Types}SummaryPageUrlType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentSummaryType", propOrder = {
    "issuedDateTime",
    "summaryPageUrl"
})
public class DocumentSummaryType {

    @XmlElement(name = "IssuedDateTime", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String issuedDateTime;
    @XmlElement(name = "SummaryPageUrl", required = true)
    protected String summaryPageUrl;

    /**
     * Gets the value of the issuedDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuedDateTime() {
        return issuedDateTime;
    }

    /**
     * Sets the value of the issuedDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuedDateTime(String value) {
        this.issuedDateTime = value;
    }

    /**
     * Gets the value of the summaryPageUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSummaryPageUrl() {
        return summaryPageUrl;
    }

    /**
     * Sets the value of the summaryPageUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSummaryPageUrl(String value) {
        this.summaryPageUrl = value;
    }

}
