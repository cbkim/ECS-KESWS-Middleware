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
 * <p>Java class for DocumentExchangeDetailsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocumentExchangeDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ReceivingPartyDetails" type="{MDA_Common_Types}ReceivingPartyDetailsType"/>
 *         &lt;element name="NotifyPartyDetails" type="{MDA_Common_Types}NotifyPartyDetailsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentExchangeDetailsType", propOrder = {
    "receivingPartyDetails",
    "notifyPartyDetails"
})
public class DocumentExchangeDetailsType {

    @XmlElement(name = "ReceivingPartyDetails", required = true)
    protected ReceivingPartyDetailsType receivingPartyDetails;
    @XmlElement(name = "NotifyPartyDetails")
    protected NotifyPartyDetailsType notifyPartyDetails;

    /**
     * Gets the value of the receivingPartyDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ReceivingPartyDetailsType }
     *     
     */
    public ReceivingPartyDetailsType getReceivingPartyDetails() {
        return receivingPartyDetails;
    }

    /**
     * Sets the value of the receivingPartyDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReceivingPartyDetailsType }
     *     
     */
    public void setReceivingPartyDetails(ReceivingPartyDetailsType value) {
        this.receivingPartyDetails = value;
    }

    /**
     * Gets the value of the notifyPartyDetails property.
     * 
     * @return
     *     possible object is
     *     {@link NotifyPartyDetailsType }
     *     
     */
    public NotifyPartyDetailsType getNotifyPartyDetails() {
        return notifyPartyDetails;
    }

    /**
     * Sets the value of the notifyPartyDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link NotifyPartyDetailsType }
     *     
     */
    public void setNotifyPartyDetails(NotifyPartyDetailsType value) {
        this.notifyPartyDetails = value;
    }

}
