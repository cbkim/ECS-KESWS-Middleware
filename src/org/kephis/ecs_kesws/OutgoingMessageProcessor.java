/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.kephis.ecs_kesws.entities.CdFileDetails;
import org.kephis.ecs_kesws.entities.EcsResCdFileMsg;
import org.kephis.ecs_kesws.entities.RecCdFileMsg;
import org.kephis.ecs_kesws.entities.ResCdFileMsg;
import org.kephis.ecs_kesws.entities.EcsConDocHeader;
import org.kephis.ecs_kesws.entities.EcsConDocItems;
import org.kephis.ecs_kesws.entities.controllers.DBDAO;
import org.kephis.ecs_kesws.entities.controllers.EcsEntitiesControllerCaller;
import org.kephis.ecs_kesws.utilities.FileProcessor;
import org.kephis.ecs_kesws.utilities.UtilityClass;
import org.kephis.ecs_kesws.xml.ApplicationConfigurationXMLMapper;
import org.kephis.ecs_kesws.xml.parser.ECSConsignmentDoc;
import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0.ConsignmentDocument;
import org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_0.ConsignmentApprovalStatus;
import org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_0.ContainerType;
import org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_0.DocumentHeaderType;
import org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_0.DocumentDetailsType;
import org.kephis.ecs_kesws.xml.validator.XmlFileValidator;
import org.kephis.ecs_kesws.entities.InternalProductcodes;
import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.ProductDetailsOneType;
import org.kephis.ecs_kesws.entities.RecErrorFileMsg;
import org.kephis.ecs_kesws.entities.controllers.EcsKeswsEntitiesControllerCaller;

/**
 *
 * @author kim
 */
class OutgoingMessageProcessor { //implements Runnable {

    private static boolean stop = false;// used to stop thread
    private static Lock lock = new ReentrantLock();//locking mechanism to have just one thread run

    public OutgoingMessageProcessor() {

    }

    //   @Override
    public static void main(String[] args) { //run() {
        boolean endthread = false;
        try {
            if (OutgoingMessageProcessor.lock.tryLock()) {
                FileProcessor fileprocessor = new FileProcessor();
                ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper = new ApplicationConfigurationXMLMapper();

                while (!stop && !endthread) {
                    int senario = 3;
                    switch (senario) {
                        case 3: {

                            scenario3FileProcessor(fileprocessor, applicationConfigurationXMLMapper);
                            scenario3CDApprovalMesg(fileprocessor, applicationConfigurationXMLMapper);
                            System.gc();
                            getMessages(fileprocessor, applicationConfigurationXMLMapper);
                            System.gc();
                            recErrorFileMsg(fileprocessor, applicationConfigurationXMLMapper);
                        }
                    }
                    System.gc();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    static void getMessages(FileProcessor fileprocessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {
        try {
            fileprocessor.retrieveMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(),
                    applicationConfigurationXMLMapper.getSenderId(), applicationConfigurationXMLMapper.getInboxFolder(), true);

        } catch (Exception e) {
            e.printStackTrace(); //TODO send notification 
        }

    }

    private static void recErrorFileMsg(FileProcessor fileprocessor,
            ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {

        EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesControllerCaller
                = new EcsKeswsEntitiesControllerCaller();

        List<String> filesinQue = new ArrayList<String>();

        fileprocessor.readFilesBeingProcessed(applicationConfigurationXMLMapper.getInboxFolder());
        List<String> filespendingprocesser = fileprocessor.getFilesbeingProcessed();

        for (Iterator<String> iterator = filespendingprocesser.iterator(); iterator.hasNext();) {
            String fileName = (String) iterator.next();
            String deleteFile = "";
            if (fileName.contains(applicationConfigurationXMLMapper.getFilesTypestoReceive().get(1).toString())) {
                //ERR_MSG-CD2015KEPHISKEEXP0000227755-1-F-20150428061513
                String docid = fileName.substring(25, 35);
                String refDocID = docid.substring(docid.lastIndexOf("0") + 1);

                String filePath = new File(applicationConfigurationXMLMapper.getInboxFolder() + fileName).getAbsolutePath();

                int cdFileMsgId = Integer.parseInt(refDocID);
                int messageType = 6;

                EcsResCdFileMsg ecsResCdFileMsg = ecsKeswsEntitiesControllerCaller.findECSResCdFileMsgbyConsignmentId(cdFileMsgId);

                RecErrorFileMsg recErrorFileMsg = ecsKeswsEntitiesControllerCaller.OgErrorResMsg(
                        filePath, messageType, ecsResCdFileMsg);

                //MOVE FILES TO ARCHIVE : 
                UtilityClass util = new UtilityClass();
                String destDir = applicationConfigurationXMLMapper.getArchiveFolder()
                        + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                        + File.separator + util.getCurrentDay() + File.separator;
                String sourceDir = applicationConfigurationXMLMapper.getInboxFolder();

                fileprocessor.moveXmlFileProcessed(sourceDir, destDir, fileName);

            }

        }
    }

    public static void scenario3FileProcessor(FileProcessor fileProcessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {
        EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController = new EcsKeswsEntitiesControllerCaller();
        EcsEntitiesControllerCaller ecsEntitiesController = new EcsEntitiesControllerCaller(applicationConfigurationXMLMapper);
        List<Integer> submittedConsignmentIds = new ArrayList<Integer>();
        submittedConsignmentIds = ecsEntitiesController.getSubmittedConsignementIds();
        EcsResCdFileMsg ecsResCdFileMsg = new EcsResCdFileMsg();// CDfile database entity mapper
        UtilityClass util = new UtilityClass();
        for (Iterator<Integer> iterator = submittedConsignmentIds.iterator(); iterator.hasNext();) {
            Integer SubmittedConsignmentId = 0;
            SubmittedConsignmentId = iterator.next();
            //Call view with consignment documents to be processed 
            if (ecsKeswsEntitiesController.findEcsConDocHeaderByConsignmentId(SubmittedConsignmentId) != null) {
                String resTemplateFile = "/ecs_kesws/service/xml/OG_SUB_CD-FILE.xml";
                ecsResCdFileMsg.setECSCONSIGNEMENTIDRef(SubmittedConsignmentId);
                try {
                    JAXBContext context = null;
                    Unmarshaller um = null;
                    org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument keswsConsignmentDocumentObj = new org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument();
                    org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument keswsConsignmentDocumentResObj = new org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument();
                    context = JAXBContext.newInstance(org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.class);
                    um = context.createUnmarshaller();
                    keswsConsignmentDocumentObj = (org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument) um.unmarshal(new FileReader(resTemplateFile));
                    EcsConDocHeader ecsConDocHeader = ecsKeswsEntitiesController.findEcsConDocHeaderByConsignmentId(SubmittedConsignmentId);
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
                    ecsResCdFileMsg = ecsKeswsEntitiesController.createEcsResCdFileMsg(fileName, 5, SubmittedConsignmentId);

                    if (ecsResCdFileMsg != null) {

                        ecsResCdFileMsg.setInvoiceNO(ecsConDocHeader.getCDHeaderOneInvoiceNumber());
                        /*  <ConsignmentDocument> <DocumentHeader>  <DocumentReference><DocumentNumber> */
                        keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().setDocumentNumber(ecsConDocHeader.getDocumentNumber());
                        /* <ConsignmentDocument> <DocumentHeader> <DocumentReference> <CommonRefNumber> */
                        keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().setCommonRefNumber("" + RefrenceNo);
                        keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().setSenderID("" + ecsConDocHeader.getSenderID() + "");
                        //keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().setSenderID("conyangoexim");
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard> <avider>  <ApplicationCode>   */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getServiceProvider().setApplicationCode("" + ecsConDocHeader.getServiceProviderApplicationCode());
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard> <ServiceProvider>  <Name>   */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getServiceProvider().setName("" + ecsConDocHeader.getServiceProviderName());
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard> <ServiceProvider>  <TIN>   */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getServiceProvider().setTIN("" + ecsConDocHeader.getServiceProviderTIN());
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard> <ServiceProvider>  <PhyCountry>   */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getServiceProvider().setPhyCountry("" + ecsConDocHeader.getServiceProviderPhyCountry());
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   <DocumentCode>      --TO BE LOADED FROM CONFIG FILE*/
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setDocumentCode("" + docType);
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < ProcessCode>      --TO BE LOADED FROM CONFIG FILE*/
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setProcessCode("KEEXPProc");// TO DO // CHANGE ON GO LIVE
                            /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < ApplicationDate> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setApplicationDate("" + ecsConDocHeader.getApplicationDate());
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < UpdatedDate> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setUpdatedDate("" + ecsConDocHeader.getUpdatedDate());
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < ApprovalDate> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setApprovalDate("" + ecsConDocHeader.getApplicationDate());
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < FinalApprovalDate> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setFinalApprovalDate("" + ecsConDocHeader.getPGAHeaderFieldsPreferredInspectionDate());
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < ApplicationRefNo> */  //<!-- REQ GENERATED-->
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setApplicationRefNo("" + RefrenceNo);
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDStandard>   < UCRNumber > */
                        if (ecsConDocHeader.getUCRNo() != null) {
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setUCRNumber("" + ecsConDocHeader.getUCRNo());

                        } else {
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setUCRNumber("" + "UCR201500020539");

                        }
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter>  <Name> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setName("" + ecsConDocHeader.getCDImporterName());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter>  <PhysicalAddress> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setPhysicalAddress(ecsConDocHeader.getCDImporterPhysicalAddress());

                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter>  <PostalAddress>*/
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setPostalAddress(ecsConDocHeader.getCDImporterPhysicalAddress() + ecsConDocHeader.getCDImporterPostalAddress());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter>   <PosCountry>*/
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setPosCountry(ecsConDocHeader.getCDImporterPosCountry());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter> <TeleFax> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setTeleFax(ecsConDocHeader.getCDImporterTeleFax());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter> <SectorofActivity> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setSectorofActivity(ecsConDocHeader.getCDImporterSectorofActivity());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter>   <WarehouseCode> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setWarehouseCode(ecsConDocHeader.getCDImporterWarehouseCode());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDImporter>   <WarehouseLocation> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setWarehouseLocation(ecsConDocHeader.getCDImporterWarehouseLocation());

                        /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>  <Name>*/
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setName(ecsConDocHeader.getCDConsigneName());

                        /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>  <PhysicalAddress> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setPhysicalAddress(ecsConDocHeader.getCDConsigneEPhysicalAddress());
                        /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>  <PostalAddress> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setPostalAddress(ecsConDocHeader.getCDConsigneEPhysicalAddress() + ecsConDocHeader.getCDConsigneePostalAddress());
                        /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>   <PhyCountry> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setPosCountry(ecsConDocHeader.getCDConsigneePosCountry());

                        /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>   <TeleFax> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setTeleFax(ecsConDocHeader.getCDConsigneeTeleFax());
                        /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee> <Email>m</Email>*/
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setEmail("notavailable@kephis.co.ke");
                        /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>  <SectorofActivity>TRD</SectorofActivity>*/
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setSectorofActivity("TRD");
                        /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>  <WarehouseCode>AFRICOFF</WarehouseCode>*/
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setWarehouseCode(ecsConDocHeader.getCCDConsigneeWarehouseCode());
                        /*  <DocumentDetails> <ConsignmentDocDetails>   <CDConsignee>  WarehouseLocation>MSA</WarehouseLocation>*/
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setWarehouseLocation(ecsConDocHeader.getCDConsigneeWarehouseLocation());

                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter>  <Name> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setName(ecsConDocHeader.getCDExporterName());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> TIN */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setTIN(ecsConDocHeader.getCDExporterTIN());
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDExporter>  PhysicalAddress */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setPhysicalAddress(ecsConDocHeader.getCDExporterPhysicalAddress());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> PhyCountry */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setPhyCountry(ecsConDocHeader.getCDExporterPhyCountry());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> PostalAddress */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setPostalAddress(ecsConDocHeader.getCDExporterPostalAddress());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> PosCountry */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setPosCountry(ecsConDocHeader.getCDExporterPosCountry());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> TeleFax */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setTeleFax(ecsConDocHeader.getCDExporterTeleFax());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> <WarehouseCode> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setWarehouseCode(ecsConDocHeader.getCDExporterWarehouseCode());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDExporter> <WarehouseLocation> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setWarehouseLocation(ecsConDocHeader.getCDExporterWarehouseLocation());

                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor>  <Name> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setName(ecsConDocHeader.getCDConsignorName());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> TIN */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setTIN(ecsConDocHeader.getCDConsignorTIN());
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDConsignor>  PhysicalAddress */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setPhysicalAddress(ecsConDocHeader.getCDConsignorPhysicalAddress());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> PhyCountry */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setPhyCountry(ecsConDocHeader.getCDConsignorPhyCountry());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> PostalAddress */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setPostalAddress(ecsConDocHeader.getCDConsignorPostalAddress());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> PosCountry */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setPosCountry(ecsConDocHeader.getCDConsignorPosCountry());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> TeleFax */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setTeleFax(ecsConDocHeader.getCDConsignorTeleFax());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> <WarehouseCode> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setWarehouseCode(ecsConDocHeader.getCDConsignorWarehouseCode());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDConsignor> <WarehouseLocation> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setWarehouseLocation(ecsConDocHeader.getCDConsignorWarehouseLocation());

                        /*  <DocumentDetails> <ConsignmentDocDetails>  <CDTransport> ModeOfTranspor */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setModeOfTransport(ecsConDocHeader.getCDTransportModeOfTransport());
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setVesselName(ecsConDocHeader.getCDTransportVesselName());
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setVoyageNo(ecsConDocHeader.getCDTransportVoyageNo());

                        /*  <DocumentDetails> <ConsignmentDocDetails> <ModeOfTransportDesc>  <PortOfArrival>*/
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setModeOfTransportDesc(ecsConDocHeader.getCDTransportModeOfTransportDesc());

                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setPortOfArrival(ecsConDocHeader.getCDTransportPortOfArrival());
                        /*  <DocumentDetails> <ConsignmentDocDetails>   <PortOfArrivalDesc> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setPortOfArrivalDesc(ecsConDocHeader.getCDTransportPortOfArrivalDesc());
                        /*  <DocumentDetails> <ConsignmentDocDetails>   <PortOfDeparture> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setPortOfDeparture(ecsConDocHeader.getCDTransportPortOfDeparture());
                        /*  <DocumentDetails> <ConsignmentDocDetails>   <PortOfDepartureDesc> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setPortOfDepartureDesc(ecsConDocHeader.getCDTransportPortOfDepartureDesc());
                        /*  <DocumentDetails> <ConsignmentDocDetails>   <CustomsOffice>  */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setCustomsOffice(ecsConDocHeader.getCDTransportCustomsOffice());
                        /*  <DocumentDetails> <ConsignmentDocDetails>   CustomsOfficeDes */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setCustomsOfficeDesc(ecsConDocHeader.getCDTransportCustomsOfficeDesc());
                        /*  <DocumentDetails> <ConsignmentDocDetails>    VesselName */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setVesselName(ecsConDocHeader.getCDTransportVesselName());
                        /*  <DocumentDetails> <ConsignmentDocDetails>    VoyageNo */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setVoyageNo(ecsConDocHeader.getCDTransportVoyageNo());
                        /*  <DocumentDetails> <ConsignmentDocDetails>    ShipmentDate */
                        /*  <DocumentDetails> <ConsignmentDocDetails>    MarksAndNumbers */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setMarksAndNumbers(ecsConDocHeader.getCDTransportMarksAndNumbers());
                        /*  <DocumentDetails> <ConsignmentDocDetails>    FreightStation */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setFreightStation(ecsConDocHeader.getCDTransportFreightStation());
                        /*  <DocumentDetails> <ConsignmentDocDetails>    FreightStationDes */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setFreightStationDesc(ecsConDocHeader.getCDTransportFreightStationDesc());
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <PGAHeaderFields>  <CollectionOffice> */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getPGAHeaderFields().setCollectionOffice("" + ecsConDocHeader.getPGAHeaderFieldsCollectionOffice());
                        /*  <DocumentDetails> <ConsignmentDocDetails>  <PGAHeaderFields>  <PreferredInspectionDate>*/
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getPGAHeaderFields().setPreferredInspectionDate(ecsConDocHeader.getPGAHeaderFieldsPreferredInspectionDate());// TO Update
                             /*  <DocumentDetails> <ConsignmentDocDetails> <CDHeaderOne>  */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDHeaderOne().setInvoiceDate(ecsConDocHeader.getCDHeaderOneInvoiceDate());
                        /*  <DocumentDetails> <ConsignmentDocDetails> <CDHeaderOne>  */
                        keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDHeaderOne().setInvoiceNumber(ecsConDocHeader.getCDHeaderOneInvoiceNumber());

                        if (Long.parseLong(ecsConDocHeader.getCDTransportShipmentDate()) - Long.parseLong(ecsConDocHeader.getPGAHeaderFieldsPreferredInspectionDate()) < 30000) {
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setShipmentDate((Long.parseLong(ecsConDocHeader.getCDTransportShipmentDate()) + 30000) + "");
                        } else {
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setShipmentDate((Long.parseLong(ecsConDocHeader.getCDTransportShipmentDate()) + 30000) + "");

                        }
                        keswsConsignmentDocumentObj.getDocumentSummary().setIssuedDateTime(ecsConDocHeader.getPGAHeaderFieldsPreferredInspectionDate());
                        if (ecsConDocHeader.getUCRNo() != null) {
                            keswsConsignmentDocumentObj.getDocumentSummary().setSummaryPageUrl("https://trial.kenyatradenet.go.ke/keswsoga/IDFSummaryPage.mda?ucr=" + ecsConDocHeader.getUCRNo());
                        }
                        List<EcsConDocItems> ecsConDocItems = ecsKeswsEntitiesController.findEcsConDocItemsByConsignmentId(SubmittedConsignmentId);
                        for (Iterator<EcsConDocItems> iterator1 = ecsConDocItems.iterator(); iterator1.hasNext();) {
                            EcsConDocItems ecsConDocDetail = iterator1.next();
                            List<CdFileDetails> cdFileList = new LinkedList<CdFileDetails>();
                            if (!ecsConDocDetailItemId.equals(ecsConDocDetail.getId())) {
                                ecsConDocDetailItemId = ecsConDocDetail.getId();
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
                                    CProductDetailsOneType.setItemNetWeight(BigDecimal.valueOf(Double.parseDouble(ecsConDocDetail.getCDProduct1ItemNetWeight())));  //conversion error may occur
                                    CProductDetailsOneType.setItemGrossWeight(BigDecimal.valueOf(Double.parseDouble(ecsConDocDetail.getCDProduct1ItemGrossWeight())));//conversion error may occur
                                     /*  <DocumentDetails> <ConsignmentDocDetails> <ConsignmentDocDetails>  <CDProductDetails> */
                                    itemDetail.setCDProduct1(CProductDetailsOneType);
                                    CProductDetailsTwoType.setApplicantRemarks("REMARKS");
                                    itemDetail.setCDProduct2(CProductDetailsTwoType);
                                    org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetailsType.ProductDetailsType.ItemDetails newitemDetail = itemDetail;
                                    newitemDetail = itemDetail;
                                    keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDProductDetails().setItemDetails(itemCounter, newitemDetail);
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
                                    CProductDetailsOneType.setItemNetWeight(BigDecimal.valueOf(Double.parseDouble(ecsConDocDetail.getCDProduct1ItemNetWeight())));  //conversion error may occur
                                    CProductDetailsOneType.setItemGrossWeight(BigDecimal.valueOf(Double.parseDouble(ecsConDocDetail.getCDProduct1ItemGrossWeight())));//conversion error may occur
                                    /*  <DocumentDetails> <ConsignmentDocDetails> <ConsignmentDocDetails>  <CDProductDetails> */
                                    itemDetail.setCDProduct1(CProductDetailsOneType);
                                    CProductDetailsTwoType.setApplicantRemarks("REMARKS");
                                    itemDetail.setCDProduct2(CProductDetailsTwoType);
                                    org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetailsType.ProductDetailsType.ItemDetails newitemDetail = itemDetail;
                                    newitemDetail = itemDetail;
                                    //System.out.println("IPC details " + newitemDetail.getCDProduct1().getInternalProductNo());
                                    keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDProductDetails().setItemDetails(itemCounter, newitemDetail);
                                }
                                if ((itemCounter == 1)) {
                                    InternalProductcodes IPCObj = null;
                                    Double weight = Double.valueOf(ecsConDocHeader.getTotalConsignmentForBilling());
                                    if (!ecsKeswsEntitiesController.internalProductCodesExist(ecsConDocDetail.getCDProduct1InternalProductNo())) {
                                        IPCObj = new InternalProductcodes();
                                        Date date = new java.sql.Date(Integer.parseInt(util.getCurrentYear()), Integer.parseInt(util.getCurrentMonth()), Integer.parseInt(util.getCurrentDay()));
                                        IPCObj.setAggregateIPCCodeLevel(0);
                                        IPCObj.setInternalProductCode(ecsConDocDetail.getCDProduct1InternalProductNo());
                                        IPCObj.setHscode(ecsConDocDetail.getCDProduct1ItemHSCode());
                                        IPCObj.setHscodeDesc("SYSTEM GENERATED " + ecsConDocDetail.getCDProduct1HSDescription() + " : " + ecsConDocDetail.getCDProduct1ItemDescription());
                                        IPCObj.setDateDeactivated(date);
                                        ecsKeswsEntitiesController.createInternalProductcode(IPCObj);
                                        ecsKeswsEntitiesController.logInfo2(file, "CREATED IPC ENTRY ON ECSKESWSDB");
                                    }
                                    IPCObj = ecsKeswsEntitiesController.getInternalProductcodes(ecsConDocDetail.getCDProduct1InternalProductNo());
                                    ecsKeswsEntitiesController.updateCreateInternalProductcodePriceDocMappings(ecsResCdFileMsg, IPCObj);
                                    CdFileDetails cdFileDetails = ecsKeswsEntitiesController.recCDFileMsgDetails(ecsResCdFileMsg, IPCObj, weight);
                                    cdFileList.add(cdFileDetails);
                                    ecsResCdFileMsg.setCdFileDetailsCollection(cdFileList);
                                }
                            }
                            itemCounter = itemCounter + 1;
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
                                //  String processDir = applicationConfigurationXMLMapper.getProcessingFolder();
                                String sourceDir = applicationConfigurationXMLMapper.getOutboxFolder();
                                // fileProcessor.moveXmlFileProcessed(sourceDir, processDir, cdResFile.getName());
                                String destDir = applicationConfigurationXMLMapper.getOutboxArchiveFolder()
                                        + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                        + File.separator + util.getCurrentDay() + File.separator + SubmittedConsignmentId.toString() + File.separator;

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
            }
        }

        ecsKeswsEntitiesController.CloseEmf();
    }

    /**
     * Send Consignment XML Send approval IO and CO Messages
     *
     * @param fileprocessor
     * @param applicationConfigurationXMLMapper
     */
    private static void scenario3CDApprovalMesg(FileProcessor fileprocessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {

        EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesControllerCaller = new EcsKeswsEntitiesControllerCaller();
        EcsEntitiesControllerCaller ecsEntitiesControllerCaller = new EcsEntitiesControllerCaller(applicationConfigurationXMLMapper);

        List<String> filesinQue = new ArrayList<String>();
        UtilityClass util = new UtilityClass();
        List<String> cdFileApprovalMessages = ecsEntitiesControllerCaller.getConsignmentsApprovalMesg();
        if (!cdFileApprovalMessages.isEmpty()) {
            try {
                for (Iterator<String> iterator = cdFileApprovalMessages.iterator(); iterator.hasNext();) {
                    String CdRefNo = (String) iterator.next();

                    try {
                        String InvoiceNumber = CdRefNo;
                        Double versionNumber = Double.parseDouble("1");
                        int recCdFileMsgConsignmentId = 0;
                        EcsResCdFileMsg recCdFileMsg = ecsKeswsEntitiesControllerCaller.findECSResCdFileMsgbyFileName(CdRefNo);
                        int isFirstMessageSent = 0;
                        if (!(recCdFileMsg == null)) {
                            recCdFileMsgConsignmentId = recCdFileMsg.getECSCONSIGNEMENTIDRef();
                            isFirstMessageSent = recCdFileMsg.getResCdFileMsgCollection().size();
                        }
                        EcsResCdFileMsg ecsResCdFileMsg = ecsKeswsEntitiesControllerCaller.findECSResCdFileMsgbyConsignmentId(recCdFileMsgConsignmentId);
                        int clientId = ecsEntitiesControllerCaller.getECSconsignmentClientId(recCdFileMsgConsignmentId);
                        int consignmentPkId = ecsEntitiesControllerCaller.getECSconsignmentPkId(recCdFileMsgConsignmentId);
                        String ClientPin = ecsEntitiesControllerCaller.getClientPin(clientId);
                        String ClientName = ecsEntitiesControllerCaller.getClientName(clientId);
                        String ClientCustomerId = ecsEntitiesControllerCaller.getClientCustomerId(ClientPin);
                        float Amount = 1000;
                        String ChargeDescription = "Phytosanitary Certificate (Commercial Agricultural commondities)".toUpperCase();
                        String ServiceType = "Default price ".toUpperCase();
                        if (ecsResCdFileMsg != null) {
                            for (Iterator<CdFileDetails> iterator1 = ecsResCdFileMsg.getCdFileDetailsCollection().iterator(); iterator1.hasNext();) {
                                CdFileDetails next = iterator1.next();
                                Amount = (float) (next.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef().getPRICELISTPriceIDRef().getChargeKshs());
                                ChargeDescription = next.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef().getPRICELISTPriceIDRef().getChargeDescription().trim();
                                ServiceType = next.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef().getDocumentIDRef() + ServiceType;

                            }
                        }
                        if (ecsEntitiesControllerCaller.getECSconsignmentStatus(consignmentPkId, "PENDING").contains("PENDING")
                                && (recCdFileMsgConsignmentId == 0)) {
                            try {
                                ecsEntitiesControllerCaller.InvoiceConsignmentonECS(consignmentPkId, ClientCustomerId, Amount, "Pending Accpac Invoice No", InvoiceNumber, ChargeDescription, ServiceType, ecsKeswsEntitiesControllerCaller, 1, "");
                                ecsKeswsEntitiesControllerCaller.logInfo2(CdRefNo, " INVOICED CLIENT WITH PIN " + ClientPin.toUpperCase() + " FOR CONSIGNMENT ID " + recCdFileMsgConsignmentId + " ON ECS KES " + Amount + " FOR " + ChargeDescription.toUpperCase());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                // System.out.println("Trying to creat client pin");
                                if (ecsEntitiesControllerCaller.CreateClientinACCPAC(ClientPin, ClientName)) {
                                    // System.out.println("JUST CREATED A CLIENT + " +ClientPin + " "+ ClientName);
                                    // System.out.println("Inside recCdFileMsgConsignmentId == 0");
                                    ecsEntitiesControllerCaller.InvoiceConsignmentonAccpac(recCdFileMsgConsignmentId, ClientPin, Amount, "Pending Accpac Invoice No", InvoiceNumber, ChargeDescription, ecsKeswsEntitiesControllerCaller);
                                    ecsKeswsEntitiesControllerCaller.logInfo2(CdRefNo, " INVOICED CLIENT WITH PIN " + ClientPin.toUpperCase() + " FOR CONSIGNMENT ID " + recCdFileMsgConsignmentId + " ON ACCPAC KES " + Amount + " FOR " + ChargeDescription.toUpperCase());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (ecsEntitiesControllerCaller.getECSconsignmentStatus(consignmentPkId, "PENDING").contains("PENDING")
                                && (recCdFileMsgConsignmentId != 0)) {
                            if (isFirstMessageSent == 0) {
                                ConsignmentApprovalStatus resObj = new ConsignmentApprovalStatus();
                                DocumentHeaderType resObjDocHeader = new DocumentHeaderType();
                                DocumentDetailsType resObjDocDetails = new DocumentDetailsType();
                                ContainerType resObjconType = new ContainerType();
                                resObjDocHeader.setMsgid("OG_CD_RES");//OGA_CD_RES
                                resObjDocHeader.setRefno(InvoiceNumber.substring(0, InvoiceNumber.length()));
                                String msgdate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                                resObjDocHeader.setMsgdate(msgdate);
                                resObjDocHeader.setMsgFunc(BigInteger.valueOf(9));
                                resObjDocHeader.setReceiver("KESWS");
                                resObjDocHeader.setSender("KEPHIS");
                                msgdate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                                resObjDocHeader.setMsgdate(msgdate);
                                resObjDocHeader.setMsgFunc(BigInteger.valueOf(9));
                                resObjDocHeader.setVersion(versionNumber.toString());
                                resObjDocDetails.setConsignmentRefnumber(InvoiceNumber.substring(0, InvoiceNumber.length()));
                                resObjDocDetails.setVerNo("1");
                                resObjDocDetails.setCertificateNo("");
                                resObjDocDetails.setPermitNo("");
                                resObjDocDetails.setStatus("AP");
                                resObjDocDetails.setRoleCode("CO");
                                resObjDocDetails.setVoInd("N");
                                resObjDocDetails.setIoInd("Y");
                                resObjDocDetails.setInspectionType("PI");

                                resObjconType.setContNumber("");
                                resObjDocDetails.setContDetails(resObjconType);
                                resObjDocDetails.setUserId("kephisco1");
                                resObjDocDetails.setPgaRemarks("submitted for inspection " + "");
                                resObjDocDetails.setConditionalRemarks("submitted for inspection ");
                                resObjDocDetails.setOgaQueries("");
                                resObjDocDetails.setQueriesResponse("");
                                resObjDocDetails.setAmount(BigDecimal.ZERO);
                                resObjDocDetails.setCurrency("KSH");
                                resObjDocDetails.setRevenueCode("");
                                resObjDocDetails.setExpiryDate("20151231240000");
                                resObjDocDetails.setPgaRiskAssessmentLane("1");
                                resObjDocDetails.setAssessedBy("kephisco1");
                                resObjDocDetails.setAssessedDate(msgdate);
                                resObjDocDetails.setAssessedRemarks("");
                                resObj.setDocumentDetails(resObjDocDetails);
                                resObj.setDocumentHeader(resObjDocHeader);
                                JAXBContext contextresObj = JAXBContext.newInstance(ConsignmentApprovalStatus.class);
                                Marshaller resObjm = contextresObj.createMarshaller();
                                resObjm.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                                String file = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length()) + "-1-" + "B-" + msgdate + ".xml";
                                String fileN = "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length()) + "-1-" + "B-" + msgdate + ".xml";
                                resObjm.marshal(resObj, new File(file));
                                String[] attachments = new String[]{"", ""};
                                String attachment = "";
                                if ((recCdFileMsgConsignmentId != 0) && (fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length()) + "-1-" + "B-" + msgdate + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length()), "OG_CD_RES", attachment))) {
                                    filesinQue.add(file);
                                    String sourceDir = applicationConfigurationXMLMapper.getOutboxFolder();
                                    String destDir = applicationConfigurationXMLMapper.getOutboxArchiveFolder()
                                            + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                            + File.separator + util.getCurrentDay() + File.separator + "cd_res" + File.separator;
                                    fileprocessor.moveXmlFileProcessed(sourceDir, destDir, fileN);

                                    ResCdFileMsg resCdFileMsg = ecsKeswsEntitiesControllerCaller.resCDFileMsg(recCdFileMsg, 2, file, fileN);
                                    ecsKeswsEntitiesControllerCaller.logInfo2(fileN, " SENT CO PASS STATUS RESPONSE MESSAGE");
                                }
                            }
                        }

                        if (ecsEntitiesControllerCaller.getECSconsignmentStatus(consignmentPkId, "REJECTED").contains("REJECTED")
                                && (recCdFileMsgConsignmentId != 0)) {

                            if (isFirstMessageSent == 1) {
                                ConsignmentApprovalStatus resObj2 = new ConsignmentApprovalStatus();
                                DocumentHeaderType resObjDocHeader2 = new DocumentHeaderType();
                                DocumentDetailsType resObjDocDetails2 = new DocumentDetailsType();
                                ContainerType resObjconType2 = new ContainerType();
                                resObjDocHeader2.setMsgid("OG_CD_RES");//OGA_CD_RES
                                resObjDocHeader2.setRefno(InvoiceNumber.substring(0, InvoiceNumber.length()));
                                String msgdate2 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                                resObjDocHeader2.setMsgdate(msgdate2);
                                resObjDocHeader2.setMsgFunc(BigInteger.valueOf(9));
                                resObjDocHeader2.setReceiver("KESWS");
                                resObjDocHeader2.setSender("KEPHIS");
                                resObjDocHeader2.setVersion(versionNumber.toString());

                                resObjDocDetails2.setConsignmentRefnumber(InvoiceNumber.substring(0, InvoiceNumber.length()));
                                resObjDocDetails2.setVerNo("1");
                                resObjDocDetails2.setCertificateNo("");
                                resObjDocDetails2.setPermitNo("");
                                resObjDocDetails2.setStatus("RJ");
                                resObjDocDetails2.setRoleCode("IO");
                                resObjDocDetails2.setVoInd("N");
                                resObjDocDetails2.setIoInd("N");
                                resObjDocDetails2.setInspectionType("FV");

                                resObjconType2.setContNumber("");
                                resObjDocDetails2.setContDetails(resObjconType2);
                                resObjDocDetails2.setUserId("kephisio1");
                                resObjDocDetails2.setPgaRemarks("Rejected");
                                resObjDocDetails2.setConditionalRemarks("Rejected");
                                resObjDocDetails2.setOgaQueries("");
                                resObjDocDetails2.setQueriesResponse("");
                                resObjDocDetails2.setAmount(BigDecimal.ZERO);
                                resObjDocDetails2.setCurrency("KSH");
                                resObjDocDetails2.setRevenueCode("");
                                resObjDocDetails2.setExpiryDate("20151231240000");
                                resObjDocDetails2.setPgaRiskAssessmentLane("1");
                                resObjDocDetails2.setAssessedBy("kephisio1");
                                resObjDocDetails2.setAssessedDate(msgdate2);
                                resObjDocDetails2.setAssessedRemarks("");
                                resObj2.setDocumentDetails(resObjDocDetails2);
                                resObj2.setDocumentHeader(resObjDocHeader2);
                                JAXBContext contextresObj = JAXBContext.newInstance(ConsignmentApprovalStatus.class);
                                Marshaller resObjm2 = contextresObj.createMarshaller();
                                resObjm2.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                                String file2 = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length()) + "-1-" + "B-" + msgdate2 + ".xml";
                                String fileN = "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length()) + "-1-" + "B-" + msgdate2 + ".xml";
                                resObjm2.marshal(resObj2, new File(file2));
                                String[] attachments = new String[]{"", ""};
                                String attachment = "";
                                if ((recCdFileMsgConsignmentId != 0) && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file2, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length()) + "-1-" + "B-" + msgdate2 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length()), "OG_CD_RES", attachment)) {
                                    filesinQue.add(file2);
                                    String sourceDir = applicationConfigurationXMLMapper.getOutboxFolder();
                                    String destDir = applicationConfigurationXMLMapper.getOutboxArchiveFolder()
                                            + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                            + File.separator + util.getCurrentDay() + File.separator + "cd_res" + File.separator;
                                    fileprocessor.moveXmlFileProcessed(sourceDir, destDir, fileN);
                                    ResCdFileMsg resCdFileMsg = ecsKeswsEntitiesControllerCaller.resCDFileMsg(recCdFileMsg, 3, file2, fileN);
                                    ecsKeswsEntitiesControllerCaller.logInfo2(fileN, " SENT IO REJECT STATUS RESPONSE MESSAGE");
                                }
                            }
                        }
// to be changed to issued 
                        if (ecsEntitiesControllerCaller.getECSconsignmentStatus(consignmentPkId, "PENDING").contains("PENDING")
                                && (recCdFileMsgConsignmentId != 0)) {
                            if (isFirstMessageSent == 1) {
                                ConsignmentApprovalStatus resObj3 = new ConsignmentApprovalStatus();
                                DocumentHeaderType resObjDocHeader3 = new DocumentHeaderType();
                                DocumentDetailsType resObjDocDetails3 = new DocumentDetailsType();
                                ContainerType resObjconType3 = new ContainerType();
                                resObjDocHeader3.setMsgid("OG_CD_RES");//OGA_CD_RES
                                resObjDocHeader3.setRefno(InvoiceNumber.substring(0, InvoiceNumber.length()));
                                String msgdate3 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                                resObjDocHeader3.setMsgdate(msgdate3);
                                resObjDocHeader3.setMsgFunc(BigInteger.valueOf(9));
                                resObjDocHeader3.setReceiver("KESWS");
                                resObjDocHeader3.setSender("KEPHIS");
                                resObjDocHeader3.setVersion(versionNumber.toString());
                                resObjDocDetails3.setConsignmentRefnumber(InvoiceNumber.substring(0, InvoiceNumber.length()));
                                resObjDocDetails3.setVerNo("1");
                                resObjDocDetails3.setCertificateNo("");
                                resObjDocDetails3.setPermitNo("");
                                resObjDocDetails3.setStatus("AP");
                                resObjDocDetails3.setRoleCode("IO");
                                resObjDocDetails3.setVoInd("N");
                                resObjDocDetails3.setIoInd("N");
                                resObjDocDetails3.setInspectionType("FV");
                                resObjconType3.setContNumber("");
                                resObjDocDetails3.setContDetails(resObjconType3);
                                resObjDocDetails3.setUserId("kephisio1");
                                resObjDocDetails3.setPgaRemarks("Passed");
                                resObjDocDetails3.setConditionalRemarks("Passed");
                                resObjDocDetails3.setOgaQueries("");
                                resObjDocDetails3.setQueriesResponse("");
                                resObjDocDetails3.setAmount(BigDecimal.ZERO);
                                resObjDocDetails3.setCurrency("KSH");
                                resObjDocDetails3.setRevenueCode("");
                                resObjDocDetails3.setExpiryDate("20151231240000");
                                resObjDocDetails3.setPgaRiskAssessmentLane("1");
                                resObjDocDetails3.setAssessedBy("kephisio1");
                                resObjDocDetails3.setAssessedDate(msgdate3);
                                resObjDocDetails3.setAssessedRemarks("");
                                resObj3.setDocumentDetails(resObjDocDetails3);
                                resObj3.setDocumentHeader(resObjDocHeader3);
                                JAXBContext contextresObj = JAXBContext.newInstance(ConsignmentApprovalStatus.class);
                                Marshaller resObjm3 = contextresObj.createMarshaller();
                                resObjm3.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                                String file3 = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length()) + "-1-" + "B-" + msgdate3 + ".xml";
                                String fileN = "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length()) + "-1-" + "B-" + msgdate3 + ".xml";
                                resObjm3.marshal(resObj3, new File(file3));
                                String[] attachments = new String[]{"", ""};
                                String attachment = "";
                                if ((recCdFileMsgConsignmentId != 0) && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file3, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length()) + "-1-" + "B-" + msgdate3 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length()), "OG_CD_RES", attachment)) {
                                    filesinQue.add(file3);
                                    String sourceDir = applicationConfigurationXMLMapper.getOutboxFolder();
                                    String destDir = applicationConfigurationXMLMapper.getOutboxArchiveFolder()
                                            + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                            + File.separator + util.getCurrentDay() + File.separator + "cd_res" + File.separator;
                                    fileprocessor.moveXmlFileProcessed(sourceDir, destDir, fileN);
                                    ResCdFileMsg resCdFileMsg = ecsKeswsEntitiesControllerCaller.resCDFileMsg(recCdFileMsg, 3, file3, fileN);
                                    ecsKeswsEntitiesControllerCaller.logInfo2(fileN, " SENT IO PASS STATUS RESPONSE MESSAGE");
                                }

                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } finally {

            }
        }
    }

    public OutgoingMessageProcessor(int senario) {
        boolean endthread = false;
        try {
            if (OutgoingMessageProcessor.lock.tryLock()) {
                FileProcessor fileprocessor = new FileProcessor();
                ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper = new ApplicationConfigurationXMLMapper();

                while (!stop && !endthread) {

                    switch (senario) {

                        case 3: {
                            getMessages(fileprocessor, applicationConfigurationXMLMapper);
                            scenario3FileProcessor(fileprocessor, applicationConfigurationXMLMapper);
                            scenario3CDApprovalMesg(fileprocessor, applicationConfigurationXMLMapper);
                            // scenario3FileProcessorTester(fileprocessor, applicationConfigurationXMLMapper);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        try {
            //Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
