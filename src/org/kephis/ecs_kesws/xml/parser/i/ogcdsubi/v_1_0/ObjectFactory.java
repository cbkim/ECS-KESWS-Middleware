//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.13 at 08:13:34 AM EAT 
//


package org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProductDetailsType }
     * 
     */
    public ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType createProductDetailsType() {
        return new ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType();
    }

    /**
     * Create an instance of {@link DocumentDetailsType }
     * 
     */
    public ConsignmentDocument.DocumentDetailsType createDocumentDetailsType() {
        return new ConsignmentDocument.DocumentDetailsType();
    }

    /**
     * Create an instance of {@link ConsignmentDocument }
     * 
     */
    public ConsignmentDocument createConsignmentDocument() {
        return new ConsignmentDocument();
    }

    /**
     * Create an instance of {@link KEPHISHeaderFieldsType }
     * 
     */
    public KEPHISHeaderFieldsType createKEPHISHeaderFieldsType() {
        return new KEPHISHeaderFieldsType();
    }

    /**
     * Create an instance of {@link ItemCommodityDetailsType }
     * 
     */
    public ItemCommodityDetailsType createItemCommodityDetailsType() {
        return new ItemCommodityDetailsType();
    }

    /**
     * Create an instance of {@link ApprovalDetailsType }
     * 
     */
    public ApprovalDetailsType createApprovalDetailsType() {
        return new ApprovalDetailsType();
    }

    /**
     * Create an instance of {@link ProductDetailsType.ItemDetails }
     * 
     */
    public ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails createProductDetailsTypeItemDetails() {
        return new ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails();
    }

    /**
     * Create an instance of {@link DocumentDetailsType.ConsignmentDocDetails }
     * 
     */
    public ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails createDocumentDetailsTypeConsignmentDocDetails() {
        return new ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails();
    }

}
