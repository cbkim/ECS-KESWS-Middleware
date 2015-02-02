/**
 *
 */
package org.kephis.ecs_kesws.xml;
 
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.kephis.ecs_kesws.entities.RecCdFileMsg;
import org.kephis.ecs_kesws.entities.controllers.EcsEntitiesControllerCaller;
import org.kephis.ecs_kesws.entities.controllers.EcsKeswsEntitiesControllerCaller;

 
 
import org.kephis.ecs_kesws.utilities.UtilityClass;
import org.kephis.ecs_kesws.xml.parser.ECSConsignmentDoc;
 
//import xmlparser.KESWSConsignmentDoc.DocumentDetails.ConsignmentDocDetails.CDProductDetails.ItemDetails;
 import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0.ConsignmentDocument;
 import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0.ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails;
import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0.ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.StandardDetailsTwoType.AttachmentsType;

/**
 * @author kim
 *
 */
public class XmlFileMapper {
  
    /**
     * This class does mapping from the source XML to the destination XML with
     * special considerations to translations
     * @param ecsEntitiesControllerCaller
     * @param ecsKeswsEntitiesControllerCaller
     */
    

    /**
    public ECSConsignmentDoc getMappedECSConsignmentDoc(
    final KESWSConsignmentDoc SourceDoc) {
    String p_inspectionDate = "";
    String marksandnumbers = "LABELLED";
    Integer shipmentdate = new Integer(utilclass.getCurrentTime().substring(0, 8));
    shipmentdate = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new java.util.Date()).toString());
    double totalconsignmrntweight = getTotalConsignementWeight(SourceDoc
    .getDocumentDetails().getConsignmentDocDetails()
    .getCDProductDetails().getItemDetails());
    try {
    p_inspectionDate = formatDateString2(SourceDoc.getDocumentDetails()
    .getConsignmentDocDetails().getKEPHISHeaderFields()
    .getPreferredInspectionDate());
    shipmentdate = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
    .getCDTransport().getShipmentDate();
    marksandnumbers = SourceDoc.getDocumentDetails()
    .getConsignmentDocDetails().getCDTransport()
    .getMarksAndNumbers();
    } catch (Exception e) {
    p_inspectionDate = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    shipmentdate = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new java.util.Date()).toString());
    }
    ECSConsignmentDoc desObj = new ECSConsignmentDoc();
    desObj.setInvoicenr(SourceDoc.getDocumentHeader()
    .getDocumentReference().getCommonRefNumber());
    desObj.setConsigneeID(mapKESWSClientConsigneeIdtoECSClientIdConsigneeId(SourceDoc));
    desObj.setCountryofdestination(mapKESWSConsignmentCODtoECSConsignmentCOD(SourceDoc));
    desObj.setPointofentry(mapCountryKESWSConsignmentPOEtoECSConsignmentPOE(SourceDoc));
    desObj.setMeansofconveyance(mapKESWSConsignmentMOCtoECSClientConsignmentMOC(SourceDoc));
    desObj.setInspectionlocation(mapKESWSConsignmentILtoECSClientConsignmentIL(SourceDoc));
    desObj.setDateofdeparture(formatDateString(shipmentdate));
    desObj.setTimeofdeparture(p_inspectionDate);
    desObj.setPreferredinspectiondate(p_inspectionDate);
    desObj.setPreferredinspectiontime("01:11");
    desObj.setDistinguishingmarks(marksandnumbers);
    desObj.setConsignementweight(totalconsignmrntweight);
    desObj.setAdditionalinformation(getAttachDocumentCEI(SourceDoc.getDocumentDetails()
    .getConsignmentDocDetails().getCDStandardTwo().getAttachments()));
    return desObj;
    }
     * **/
    public ECSConsignmentDoc getxmlMappedECSConsignmentDoc(ConsignmentDocument SourceDoc, ECSConsignmentDoc ecsConsDoc) {
        UtilityClass utilclass=new UtilityClass();
        String p_inspectionDate = "";
        String marksandnumbers = "LABELLED";
        String shipmentdate = new Integer(utilclass.getCurrentTime().substring(0, 8)).toString();
        shipmentdate = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date()).toString();
        String pInspectionTime="00:00";
        SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDProductDetails().getItemDetails();
        double totalconsignmrntweight = getTotalConsignementWeight(SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDProductDetails().getItemDetails());
        try {
            p_inspectionDate = utilclass.formatDateString2(SourceDoc.getDocumentDetails().getConsignmentDocDetails().getPGAHeaderFields().getPreferredInspectionDate().substring(0,7));
            pInspectionTime=SourceDoc.getDocumentDetails().getConsignmentDocDetails().getPGAHeaderFields().getPreferredInspectionDate().substring(8,9)+":"+SourceDoc.getDocumentDetails().getConsignmentDocDetails().getPGAHeaderFields().getPreferredInspectionDate().substring(10,11);
            shipmentdate = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDTransport().getShipmentDate();
            marksandnumbers = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDTransport().getMarksAndNumbers();
        } catch (Exception e) {
            p_inspectionDate = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            shipmentdate = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date()).toString();
        } 
        //Based on version 1.0
        ecsConsDoc.setInvoicenr(SourceDoc.getDocumentHeader().getDocumentReference().getCommonRefNumber());
        ecsConsDoc.setConsigneeID(0);//mapKESWSClientConsigneeIdtoECSClientIdConsigneeId(SourceDoc, ClientId)
        ecsConsDoc.setCountryofdestination("");//mapKESWSConsignmentCODtoECSConsignmentCOD(SourceDoc)
        ecsConsDoc.setPointofentry(SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDTransport().getPortOfArrivalDesc());
        ecsConsDoc.setMeansofconveyance(0);//mapKESWSConsignmentMOCtoECSClientConsignmentMOC(SourceDoc)
        ecsConsDoc.setInspectionlocation(0);//mapKESWSConsignmentILtoECSClientConsignmentIL(SourceDoc)
        ecsConsDoc.setDateofdeparture(utilclass.formatDateString(shipmentdate));
        ecsConsDoc.setTimeofdeparture(p_inspectionDate);
        ecsConsDoc.setPreferredinspectiondate(p_inspectionDate);
        ecsConsDoc.setPreferredinspectiontime(pInspectionTime);
        ecsConsDoc.setDistinguishingmarks(marksandnumbers);
        ecsConsDoc.setConsignementweight(totalconsignmrntweight);
        ecsConsDoc.setAdditionalinformation(getAttachDocumentCEI(SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDStandardTwo().getAttachments()));
         return ecsConsDoc;
    }
    private double getTotalConsignementWeight(List<ItemDetails> itemDetails) {
        double itemTotalWeight = 0;
        for (Iterator<ItemDetails> iterator = itemDetails.iterator(); iterator
                .hasNext();) {
            ItemDetails itemDetails2 = (ItemDetails) iterator.next();
            itemTotalWeight += itemDetails2.getCDProduct1().getItemNetWeight().longValue();
        }
        return itemTotalWeight;
    }
  

    private long getTotalNetConsignementWeight(List<ItemDetails> itemDetails) {
        long itemTotalWeight = 0;
        for (Iterator<ItemDetails> iterator = itemDetails.iterator(); iterator
                .hasNext();) {
            ItemDetails itemDetails2 = (ItemDetails) iterator.next();
            itemTotalWeight += itemDetails2.getCDProduct1().getItemNetWeight().longValue();
        }
        return itemTotalWeight;
    }

    private long getTotalGrossConsignementWeight(List<ItemDetails> itemDetails) {
        long itemTotalWeight = 0;
        for (Iterator<ItemDetails> iterator = itemDetails.iterator(); iterator
                .hasNext();) {
            ItemDetails itemDetails2 = (ItemDetails) iterator.next();
            itemTotalWeight += itemDetails2.getCDProduct1().getItemNetWeight().longValue();
        }
        return itemTotalWeight;
    }

    private String getAttachDocumentCEI(List<AttachmentsType> attachmentDetails) {
        String commercialInvoiceNr = " ";
        for (Iterator<AttachmentsType> iterator = attachmentDetails.iterator(); iterator
                .hasNext();) {
            AttachmentsType Attachments2 = (AttachmentsType) iterator.next();
            if (Attachments2.getAttachDocumentCode().contains("CEI")) {
                commercialInvoiceNr = Attachments2.getAttachDocumentRefNo();
            }
        }
        return commercialInvoiceNr;
    }
    
    
 
}
