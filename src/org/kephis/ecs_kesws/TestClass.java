
package org.kephis.ecs_kesws;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.kephis.ecs_kesws.entities.CdFileDetails;
import org.kephis.ecs_kesws.entities.EcsConDoc;
import org.kephis.ecs_kesws.entities.EcsResCdFileMsg;
import org.kephis.ecs_kesws.entities.InternalProductcodes;
import org.kephis.ecs_kesws.entities.controllers.EcsEntitiesControllerCaller;
import org.kephis.ecs_kesws.entities.controllers.EcsKeswsEntitiesControllerCaller;
import org.kephis.ecs_kesws.utilities.FileProcessor;
import org.kephis.ecs_kesws.utilities.UtilityClass;
import org.kephis.ecs_kesws.xml.ApplicationConfigurationXMLMapper;
import org.kephis.ecs_kesws.xml.parser.ECSConsignmentDoc;
import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.ProductDetailsOneType;
import org.kephis.ecs_kesws.xml.validator.XmlFileValidator;

/**
 *
 * @author destiny
 */
public class TestClass {

    public TestClass() {
        
        FileProcessor fileProcessor = new FileProcessor();
        ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper = new ApplicationConfigurationXMLMapper();

        EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController = new EcsKeswsEntitiesControllerCaller();
        EcsEntitiesControllerCaller ecsEntitiesController = new EcsEntitiesControllerCaller(applicationConfigurationXMLMapper);
        OutgoingMessageProcessor outGoingMessage;
        List<Integer> submittedConsignmentIds = new ArrayList<Integer>();
        
        submittedConsignmentIds = ecsEntitiesController.getSubmittedConsignementIds();
        
        for (Iterator<Integer> iterator = submittedConsignmentIds.iterator(); iterator.hasNext();) {
            Integer SubmittedConsignmentId = 0;
            SubmittedConsignmentId = iterator.next();
            //System.out.println(ecsKeswsEntitiesController.findEcsConDocByConsignmentId(SubmittedConsignmentId).size());
            if (ecsKeswsEntitiesController.findEcsConDocByConsignmentId(SubmittedConsignmentId) != null) {
                String resTemplateFile = "/ecs_kesws/service/xml/OG_SUB_CD-FILE.xml";

                EcsResCdFileMsg ecsResCdFileMsg = new EcsResCdFileMsg();
                ecsResCdFileMsg.setECSCONSIGNEMENTIDRef(SubmittedConsignmentId);
                
                
                try {
                    JAXBContext context = null;
                    Unmarshaller um = null;
                    org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument keswsConsignmentDocumentObj = new org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument();
                    org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument keswsConsignmentDocumentResObj = new org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument();

                    ECSConsignmentDoc ecsConsignmentDocumentObj = new ECSConsignmentDoc();
                    UtilityClass util = new UtilityClass();
                    context = JAXBContext.newInstance(org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.class);
                    um = context.createUnmarshaller();
                    keswsConsignmentDocumentObj = (org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument) um.unmarshal(new FileReader(resTemplateFile));
                   
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    List<EcsConDoc> ecsConDocDetails = ecsKeswsEntitiesController.findEcsConDocByConsignmentId(SubmittedConsignmentId);
                    
                    
                    String ecsConDocDetailItemId = "";
                    Integer itemCounter = 1;
                    String msgyear = new SimpleDateFormat("yyyy").format(new Date());
                    String docType = "KEEXP";
                    String SeqNumber = util.RPad(SubmittedConsignmentId.toString(), 10, '0');
                    String RefrenceNo = "CD" + msgyear + "KEPHIS" + docType + SeqNumber;
                    
                    JAXBContext contextresObj = JAXBContext.newInstance(org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.class);
                    Marshaller resObjm = contextresObj.createMarshaller();
                    resObjm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                    
                    String file = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_SUB_CD-" + RefrenceNo + "-1-" + "B-" + msgyear + SeqNumber + ".xml";
                    String fileName = "OG_SUB_CD-" + RefrenceNo + "-1-" + "B-" + msgyear + SeqNumber + ".xml";
                    
                    File cdResFile = new File(file);
                    //System.out.println("File path " + cdResFile.getAbsoluteFile());
                    ecsResCdFileMsg = ecsKeswsEntitiesController.createEcsResCdFileMsg(fileName, 5, SubmittedConsignmentId);
                    
                    if (ecsResCdFileMsg != null) {

                        for (Iterator<EcsConDoc> iterator1 = ecsConDocDetails.iterator(); iterator1.hasNext();) {
                            EcsConDoc ecsConDocDetail = iterator1.next();
                            ecsResCdFileMsg.setInvoiceNO(ecsConDocDetail.getCDHeaderOneInvoiceNumber());
                            /*  <ConsignmentDocument> <DocumentHeader>  <DocumentReference><DocumentNumber> */
                            keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().setDocumentNumber(ecsConDocDetail.getDocumentNumber());
                            /* <ConsignmentDocument> <DocumentHeader> <DocumentReference> <CommonRefNumber> */
                            keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().setCommonRefNumber("" + RefrenceNo);
                            keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().setSenderID("" + ecsConDocDetail.getSenderID() + "");
                            //keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().setSenderID("conyangoexim");
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard> <avider>  <ApplicationCode>   */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getServiceProvider().setApplicationCode("" + ecsConDocDetail.getServiceProviderApplicationCode());
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard> <ServiceProvider>  <Name>   */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getServiceProvider().setName("" + ecsConDocDetail.getServiceProviderName());
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard> <ServiceProvider>  <TIN>   */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getServiceProvider().setTIN("" + ecsConDocDetail.getServiceProviderTIN());
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard> <ServiceProvider>  <PhyCountry>   */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getServiceProvider().setPhyCountry("" + ecsConDocDetail.getServiceProviderPhyCountry());
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   <DocumentCode>      --TO BE LOADED FROM CONFIG FILE*/
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setDocumentCode("" + docType);
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < ProcessCode>      --TO BE LOADED FROM CONFIG FILE*/
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setProcessCode("KEEXPProc");// TO DO // CHANGE ON GO LIVE
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < ApplicationDate> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setApplicationDate("" + ecsConDocDetail.getApplicationDate());
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < UpdatedDate> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setUpdatedDate("" + ecsConDocDetail.getUpdatedDate());
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < ApprovalDate> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setApprovalDate("" + ecsConDocDetail.getApplicationDate());
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < FinalApprovalDate> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setFinalApprovalDate("" + ecsConDocDetail.getPGAHeaderFieldsPreferredInspectionDate());
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < ApplicationRefNo> */  //<!-- REQ GENERATED-->
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setApplicationRefNo("" + RefrenceNo);
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < UCRNumber > */
                            if (ecsConDocDetail.getUCRNo() != null) {
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setUCRNumber("" + ecsConDocDetail.getUCRNo());

                            } else {
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setUCRNumber("" + "UCR201500020539");

                            }
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter>  <Name> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setName("" + ecsConDocDetail.getCDImporterName());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter>  <PhysicalAddress> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setPhysicalAddress(ecsConDocDetail.getCDImporterPhysicalAddress());

                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter>  <PostalAddress>*/
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setPostalAddress(ecsConDocDetail.getCDImporterPhysicalAddress() + ecsConDocDetail.getCDImporterPostalAddress());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter>   <PosCountry>*/
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setPosCountry(ecsConDocDetail.getCDImporterPosCountry());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter> <TeleFax> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setTeleFax(ecsConDocDetail.getCDImporterTeleFax());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter> <SectorofActivity> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setSectorofActivity(ecsConDocDetail.getCDImporterSectorofActivity());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter>   <WarehouseCode> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setWarehouseCode(ecsConDocDetail.getCDImporterWarehouseCode());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter>   <WarehouseLocation> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setWarehouseLocation(ecsConDocDetail.getCDImporterWarehouseLocation());

                            /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>  <Name>*/
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setName(ecsConDocDetail.getCDConsigneName());

                            /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>  <PhysicalAddress> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setPhysicalAddress(ecsConDocDetail.getCDConsigneEPhysicalAddress());
                            /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>  <PostalAddress> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setPostalAddress(ecsConDocDetail.getCDConsigneEPhysicalAddress() + ecsConDocDetail.getCDConsigneePostalAddress());
                            /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>   <PhyCountry> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setPosCountry(ecsConDocDetail.getCDConsigneePosCountry());

                            /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>   <TeleFax> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setTeleFax(ecsConDocDetail.getCDConsigneeTeleFax());
                            /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee> <Email>m</Email>*/
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setEmail("notavailable@kephis.co.ke");
                            /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>  <SectorofActivity>TRD</SectorofActivity>*/
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setSectorofActivity("TRD");
                            /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>  <WarehouseCode>AFRICOFF</WarehouseCode>*/
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setWarehouseCode(ecsConDocDetail.getCCDConsigneeWarehouseCode());
                            /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>  WarehouseLocation>MSA</WarehouseLocation>*/
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setWarehouseLocation(ecsConDocDetail.getCDConsigneeWarehouseLocation());

                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter>  <Name> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setName(ecsConDocDetail.getCDExporterName());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> TIN */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setTIN(ecsConDocDetail.getCDExporterTIN());
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDExporter>  PhysicalAddress */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setPhysicalAddress(ecsConDocDetail.getCDExporterPhysicalAddress());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> PhyCountry */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setPhyCountry(ecsConDocDetail.getCDExporterPhyCountry());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> PostalAddress */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setPostalAddress(ecsConDocDetail.getCDExporterPostalAddress());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> PosCountry */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setPosCountry(ecsConDocDetail.getCDExporterPosCountry());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> TeleFax */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setTeleFax(ecsConDocDetail.getCDExporterTeleFax());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> <WarehouseCode> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setWarehouseCode(ecsConDocDetail.getCDExporterWarehouseCode());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> <WarehouseLocation> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setWarehouseLocation(ecsConDocDetail.getCDExporterWarehouseLocation());

                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor>  <Name> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setName(ecsConDocDetail.getCDConsignorName());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> TIN */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setTIN(ecsConDocDetail.getCDConsignorTIN());
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDConsignor>  PhysicalAddress */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setPhysicalAddress(ecsConDocDetail.getCDConsignorPhysicalAddress());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> PhyCountry */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setPhyCountry(ecsConDocDetail.getCDConsignorPhyCountry());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> PostalAddress */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setPostalAddress(ecsConDocDetail.getCDConsignorPostalAddress());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> PosCountry */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setPosCountry(ecsConDocDetail.getCDConsignorPosCountry());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> TeleFax */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setTeleFax(ecsConDocDetail.getCDConsignorTeleFax());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> <WarehouseCode> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setWarehouseCode(ecsConDocDetail.getCDConsignorWarehouseCode());
                            /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> <WarehouseLocation> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setWarehouseLocation(ecsConDocDetail.getCDConsignorWarehouseLocation());

                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDTransport> ModeOfTranspor */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setModeOfTransport(ecsConDocDetail.getCDTransportModeOfTransport());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setVesselName(ecsConDocDetail.getCDTransportVesselName());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setVoyageNo(ecsConDocDetail.getCDTransportVoyageNo());

                            /*  <DocumentDetails> <ConsignmentDocDetails> <ModeOfTransportDesc>  <PortOfArrival>*/
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setModeOfTransportDesc(ecsConDocDetail.getCDTransportModeOfTransportDesc());

                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setPortOfArrival(ecsConDocDetail.getCDTransportPortOfArrival());
                            /*  <DocumentDetails> <ConsignmentDocDetails>   <PortOfArrivalDesc> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setPortOfArrivalDesc(ecsConDocDetail.getCDTransportPortOfArrivalDesc());
                            /*  <DocumentDetails> <ConsignmentDocDetails>   <PortOfDeparture> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setPortOfDeparture(ecsConDocDetail.getCDTransportPortOfDeparture());
                            /*  <DocumentDetails> <ConsignmentDocDetails>   <PortOfDepartureDesc> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setPortOfDepartureDesc(ecsConDocDetail.getCDTransportPortOfDepartureDesc());
                            /*  <DocumentDetails> <ConsignmentDocDetails>   <CustomsOffice>  */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setCustomsOffice(ecsConDocDetail.getCDTransportCustomsOffice());
                            /*  <DocumentDetails> <ConsignmentDocDetails>   CustomsOfficeDes */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setCustomsOfficeDesc(ecsConDocDetail.getCDTransportCustomsOfficeDesc());
                            /*  <DocumentDetails> <ConsignmentDocDetails>    VesselName */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setVesselName(ecsConDocDetail.getCDTransportVesselName());
                            /*  <DocumentDetails> <ConsignmentDocDetails>    VoyageNo */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setVoyageNo(ecsConDocDetail.getCDTransportVoyageNo());
                            /*  <DocumentDetails> <ConsignmentDocDetails>    ShipmentDate */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setShipmentDate(ecsConDocDetail.getCDTransportShipmentDate());
                            /*  <DocumentDetails> <ConsignmentDocDetails>    MarksAndNumbers */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setMarksAndNumbers(ecsConDocDetail.getCDTransportMarksAndNumbers());
                            /*  <DocumentDetails> <ConsignmentDocDetails>    FreightStation */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setFreightStation(ecsConDocDetail.getCDTransportFreightStation());
                            /*  <DocumentDetails> <ConsignmentDocDetails>    FreightStationDes */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setFreightStationDesc(ecsConDocDetail.getCDTransportFreightStationDesc());

                            /*  <DocumentDetails> <ConsignmentDocDetails>  <PGAHeaderFields>  <CollectionOffice> */
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getPGAHeaderFields().setCollectionOffice("" + ecsConDocDetail.getPGAHeaderFieldsCollectionOffice());

                            /*  <DocumentDetails> <ConsignmentDocDetails>  <PGAHeaderFields>  <PreferredInspectionDate>*/
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getPGAHeaderFields().setPreferredInspectionDate(ecsConDocDetail.getPGAHeaderFieldsPreferredInspectionDate());// TO Update

                            List<CdFileDetails> cdFileList = new LinkedList<CdFileDetails>();
                            if (!ecsConDocDetailItemId.equals(ecsConDocDetail.getId())) {
                                System.out.println(itemCounter + " itemCounter " + ecsConDocDetail.getId());
                                /*  <DocumentDetails> <ConsignmentDocDetails> <CDHeaderOne>  */
                                ecsConDocDetailItemId = ecsConDocDetail.getId();
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDHeaderOne().setInvoiceDate(ecsConDocDetail.getCDHeaderOneInvoiceDate());
                                /*  <DocumentDetails> <ConsignmentDocDetails> <CDHeaderOne>  */
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDHeaderOne().setInvoiceNumber(ecsConDocDetail.getCDHeaderOneInvoiceNumber());
                            //

                                if (itemCounter == 1) {
                                    org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.ProductDetailsOneType CProductDetailsOneType = new org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.ProductDetailsOneType();
                                     org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.ProductDetailsTwoType CProductDetailsTwoType = new org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.ProductDetailsTwoType();
                                    org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetailsType.ProductDetailsType.ItemDetails itemDetail = new org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetailsType.ProductDetailsType.ItemDetails();
                                    org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.QuantityType Quantity = new org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.QuantityType();
                                    itemDetail.setItemCount(itemCounter.toString());
                                    CProductDetailsOneType.setItemNo(itemCounter);
                                    CProductDetailsOneType.setItemDescription(ecsConDocDetail.getCDProduct1ItemDescription());
                                    CProductDetailsOneType.setItemHSCode(ecsConDocDetail.getCDProduct1ItemHSCode());
                                    CProductDetailsOneType.setHSDescription(ecsConDocDetail.getCDProduct1HSDescription());
                                    CProductDetailsOneType.setInternalProductNo(ecsConDocDetail.getCDProduct1InternalProductNo());
                                    CProductDetailsOneType.setProductClassCode("");
                                    CProductDetailsOneType.setProductClassDescription(""); 
                                    CProductDetailsOneType.setForeignCurrencyCode("KES");
                                    CProductDetailsOneType.setCountryOfOrigin("KE");
                                    CProductDetailsOneType.setCountryOfOriginDesc("Kenya");
                                    Quantity.setQty(BigDecimal.valueOf(ecsConDocDetail.getCDProduct1QuantityQty()));
                                    Quantity.setUnitOfQty(ecsConDocDetail.getCDProduct1QuantityUnitOfQty());
                                    Quantity.setUnitOfQtyDesc(ecsConDocDetail.getCDProduct1QuantityUnitOfQtyDesc());
                                    CProductDetailsOneType.setQuantity(Quantity);
                                    CProductDetailsOneType.setPackageQty(BigDecimal.valueOf(ecsConDocDetail.getCDProduct1PackageQty()));
                                    CProductDetailsOneType.setPackageType(ecsConDocDetail.getCDProduct1PackageType());
                                    CProductDetailsOneType.setPackageTypeDesc(ecsConDocDetail.getCDProduct1PackageTypeDesc());
                                    CProductDetailsOneType.setItemNetWeight(BigDecimal.valueOf(ecsConDocDetail.getCDProduct1ItemNetWeight()));  //conversion error may occur
                                    CProductDetailsOneType.setItemGrossWeight(BigDecimal.valueOf(ecsConDocDetail.getCDProduct1ItemGrossWeight()));//conversion error may occur
                                     /*  <DocumentDetails> <ConsignmentDocDetails> <ConsignmentDocDetails>  <CDProductDetails> */
                                    itemDetail.setCDProduct1(CProductDetailsOneType);
                                    CProductDetailsTwoType.setApplicantRemarks("REMARKS");
                                    itemDetail.setCDProduct2(CProductDetailsTwoType);
                                    org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetailsType.ProductDetailsType.ItemDetails newitemDetail = itemDetail;
                                    newitemDetail = itemDetail;
                                    keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDProductDetails().setItemDetails(itemCounter,newitemDetail);

                                }
                                if (itemCounter > 1) {
                                    org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.ProductDetailsOneType CProductDetailsOneType = new ProductDetailsOneType();
                                    org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.ProductDetailsTwoType CProductDetailsTwoType = new org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.ProductDetailsTwoType();
                                    org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetailsType.ProductDetailsType.ItemDetails itemDetail = new org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetailsType.ProductDetailsType.ItemDetails();
                                    org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.QuantityType Quantity = new org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.QuantityType();
                                    itemDetail.setItemCount(itemCounter.toString());
                                    CProductDetailsOneType.setItemNo(itemCounter);
                                    CProductDetailsOneType.setItemDescription(ecsConDocDetail.getCDProduct1ItemDescription());
                                    CProductDetailsOneType.setItemHSCode(ecsConDocDetail.getCDProduct1ItemHSCode());
                                    CProductDetailsOneType.setHSDescription(ecsConDocDetail.getCDProduct1HSDescription());
                                    CProductDetailsOneType.setInternalProductNo(ecsConDocDetail.getCDProduct1InternalProductNo());
                                    CProductDetailsOneType.setProductClassCode("");
                                    CProductDetailsOneType.setProductClassDescription("");
                                    CProductDetailsOneType.setForeignCurrencyCode("KES");
                                    CProductDetailsOneType.setCountryOfOrigin("KE");
                                    CProductDetailsOneType.setCountryOfOriginDesc("Kenya");
                                    Quantity.setQty(BigDecimal.valueOf(ecsConDocDetail.getCDProduct1QuantityQty()));
                                    Quantity.setUnitOfQty(ecsConDocDetail.getCDProduct1QuantityUnitOfQty());
                                    Quantity.setUnitOfQtyDesc(ecsConDocDetail.getCDProduct1QuantityUnitOfQtyDesc());
                                    CProductDetailsOneType.setQuantity(Quantity);
                                    CProductDetailsOneType.setPackageQty(BigDecimal.valueOf(ecsConDocDetail.getCDProduct1PackageQty()));
                                    CProductDetailsOneType.setPackageType(ecsConDocDetail.getCDProduct1PackageType());
                                    CProductDetailsOneType.setPackageTypeDesc(ecsConDocDetail.getCDProduct1PackageTypeDesc());
                                    CProductDetailsOneType.setItemNetWeight(BigDecimal.valueOf(ecsConDocDetail.getCDProduct1ItemNetWeight()));  //conversion error may occur
                                    CProductDetailsOneType.setItemGrossWeight(BigDecimal.valueOf(ecsConDocDetail.getCDProduct1ItemGrossWeight()));//conversion error may occur
                                     /*  <DocumentDetails> <ConsignmentDocDetails> <ConsignmentDocDetails>  <CDProductDetails> */
                                    itemDetail.setCDProduct1(CProductDetailsOneType);
                                     CProductDetailsTwoType.setApplicantRemarks("REMARKS");
                                    itemDetail.setCDProduct2(CProductDetailsTwoType);
                                    org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetailsType.ProductDetailsType.ItemDetails newitemDetail = itemDetail;
                                    newitemDetail = itemDetail;
                                    System.out.println("IPC details " + newitemDetail.getCDProduct1().getInternalProductNo());
                                    keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDProductDetails().setItemDetails(itemCounter,newitemDetail);

                                }
                               itemCounter = itemCounter + 1;
                                
                             
                                System.out.println(itemCounter + " ic value");

                                InternalProductcodes IPCObj = null;
                                Double weight = Double.valueOf(ecsConDocDetail.getTotalConsignmentForBilling());

                                if (!ecsKeswsEntitiesController.internalProductCodesExist(ecsConDocDetail.getCDProduct1InternalProductNo())) {
                                    IPCObj = new InternalProductcodes();
                                    Date date = new java.sql.Date(2010, 10, 10);
                                    IPCObj.setAggregateIPCCodeLevel(0);
                                    IPCObj.setInternalProductCode(ecsConDocDetail.getCDProduct1InternalProductNo());
                                    IPCObj.setHscode(ecsConDocDetail.getCDProduct1ItemHSCode());
                                    IPCObj.setHscodeDesc("SYSTEM GENERATED " + ecsConDocDetail.getCDProduct1HSDescription() + ":" + ecsConDocDetail.getCDProduct1ItemDescription());
                                    IPCObj.setDateDeactivated(date);
                                    ecsKeswsEntitiesController.createInternalProductcode(IPCObj);
                                    ecsKeswsEntitiesController.logInfo2(file, "CREATED IPC ENTRY ON ECSKESWSDB");
                                }
                                IPCObj = ecsKeswsEntitiesController.getInternalProductcodes(ecsConDocDetail.getCDProduct1InternalProductNo());
                                ecsKeswsEntitiesController.updateCreateInternalProductcodePriceDocMappings(ecsResCdFileMsg, IPCObj);
                                CdFileDetails cdFileDetails = ecsKeswsEntitiesController.recCDFileMsgDetails(ecsResCdFileMsg, IPCObj, weight);
                                cdFileList.add(cdFileDetails);

                            }
                            keswsConsignmentDocumentObj.getDocumentSummary().setIssuedDateTime(ecsConDocDetail.getPGAHeaderFieldsPreferredInspectionDate());

                            if (ecsConDocDetail.getUCRNo() != null) {
                                keswsConsignmentDocumentObj.getDocumentSummary().setSummaryPageUrl("https://trial.kenyatradenet.go.ke/keswsoga/IDFSummaryPage.mda?ucr=" + ecsConDocDetail.getUCRNo());
                            }
                            ecsResCdFileMsg.setCdFileDetailsCollection(cdFileList);
                        }
                         keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDProductDetails().getItemDetails().remove(0);
                               
                        keswsConsignmentDocumentResObj = keswsConsignmentDocumentObj;
                        // resObjm.marshal(keswsConsignmentDocumentResObj, System.out);
                        resObjm.marshal(keswsConsignmentDocumentResObj, cdResFile);
                        String[] attachments = new String[]{"", ""};
                        String attachment = "";
                        XmlFileValidator xmlvalidator = new XmlFileValidator();
                        boolean isvalidXmlFile = xmlvalidator.isValidOgCdResOFile(applicationConfigurationXMLMapper.getInboxFolder() + resTemplateFile);
                        boolean isvalidXmlFileforResponse = xmlvalidator.isValidOgCdResOFileForResponse(keswsConsignmentDocumentObj);
                        if (isvalidXmlFile && isvalidXmlFileforResponse) {

                            ecsKeswsEntitiesController.OgUpdateResCd1Msg(ecsResCdFileMsg);
                            if ((SubmittedConsignmentId != 0) && (fileProcessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file, attachments, "OG_SUB_CD-" + RefrenceNo + "-1-" + "B-" + msgyear + SeqNumber + ".xml", RefrenceNo, "OG_SUB_CD", attachment))) {
                                ecsKeswsEntitiesController.logInfo2(fileName, "SUBMITTED " + fileName + " AT " + util.getCurrentTime());
                                ecsKeswsEntitiesController.OgUpdateResCd1Msg(ecsResCdFileMsg);

                                //Move to process box and sourceDir
                                String processDir = applicationConfigurationXMLMapper.getProcessingFolder();
                                String sourceDir = applicationConfigurationXMLMapper.getOutboxFolder();
                                fileProcessor.moveXmlFileProcessed(sourceDir, processDir, cdResFile.getName());
                                String destDir = applicationConfigurationXMLMapper.getOutboxArchiveFolder()
                                        + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                        + File.separator + util.getCurrentDay() + File.separator + "cd" + File.separator;

                                fileProcessor.moveXmlFileProcessed(sourceDir, destDir, cdResFile.getName());

                                File deleteOuboxfile = new File(sourceDir + cdResFile.getName());

                                if (deleteOuboxfile.delete()) {
                                    //System.out.println(file.getName() + " is deleted!");
                                    break;
                                } else {
                                    //System.out.println("Delete operation is failed.");
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    //copy file to inboxfile archives ERROR/Year/Month/Day
                    e.printStackTrace();
                    ecsKeswsEntitiesController.logError(resTemplateFile, "ERROR " + e.getMessage());
                    e.printStackTrace();
                }
                
                
                
            }//end if 
            
        }//end For loop
        
        
    }
    
    
}
