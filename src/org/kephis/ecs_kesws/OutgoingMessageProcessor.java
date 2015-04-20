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
import org.kephis.ecs_kesws.entities.controllers.DBDAO;
import org.kephis.ecs_kesws.entities.controllers.EcsEntitiesControllerCaller;
import org.kephis.ecs_kesws.entities.controllers.EcsKeswsEntitiesControllerCaller;
import org.kephis.ecs_kesws.utilities.FileProcessor;
import org.kephis.ecs_kesws.utilities.UtilityClass;
import org.kephis.ecs_kesws.xml.ApplicationConfigurationXMLMapper;
import org.kephis.ecs_kesws.xml.parser.ECSConsignmentDoc;
import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0.ConsignmentDocument;
import org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_0.ConsignmentApprovalStatus;
import org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_0.ContainerType;
import org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_0.DocumentDetailsType;
import org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_0.DocumentHeaderType;
import org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_0.DocumentDetailsType;
import org.kephis.ecs_kesws.xml.validator.XmlFileValidator;
import org.kephis.ecs_kesws.entities.EcsConDoc;
import org.kephis.ecs_kesws.entities.InternalProductcodes;
import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_1.mda_common_types.ProductDetailsOneType;

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
                        case 1: {
                            scenario1(fileprocessor, applicationConfigurationXMLMapper);
                        }
                        case 2: {
                            scenario2(fileprocessor, applicationConfigurationXMLMapper);
                        }
                        case 3: {
                            getMessages(fileprocessor, applicationConfigurationXMLMapper);
                            System.gc();
                            scenario3FileProcessor(fileprocessor, applicationConfigurationXMLMapper);
                            scenario3CDApprovalMesg(fileprocessor, applicationConfigurationXMLMapper);
                            System.gc();
                        // scenario3FileProcessorTester(fileprocessor, applicationConfigurationXMLMapper);
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
            fileprocessor.retrieveMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), applicationConfigurationXMLMapper.getInboxFolder(), true);
            //  get error messages 

        } catch (Exception e) {
            e.printStackTrace(); //TODO send notification 
        }

    }

    static void sendErrorMessage(String receivedFileName, String invalid_XML_File) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * send approval IO and CO Messages
     *
     * @param fileprocessor
     * @param applicationConfigurationXMLMapper
     */
    private static void scenario1(FileProcessor fileprocessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {

        DBDAO dbao_omp = new DBDAO();
        DBDAO dbao_omp2 = new DBDAO();
        
        
        
        List<String> filesinQue = new ArrayList<String>();
        fileprocessor.readFilesBeingProcessed(applicationConfigurationXMLMapper.getProcessingFolder());
        List<String> filespendingprocesser = fileprocessor.getFilesbeingProcessed();
        for (Iterator<String> iterator = filespendingprocesser.iterator(); iterator.hasNext();) {
            String fileName = (String) iterator.next();
            String deleteFile = "";
            if (fileName.contains(applicationConfigurationXMLMapper.getFilesTypestoReceive().get(0).toString())) {
                JAXBContext context = null;
                try {
                    context = JAXBContext.newInstance(ConsignmentDocument.class);
                    Unmarshaller um = null;
                    um = context.createUnmarshaller();
                    ConsignmentDocument keswsConsignmentDocumentObj = null;
                    keswsConsignmentDocumentObj = (ConsignmentDocument) um.unmarshal(new FileReader(applicationConfigurationXMLMapper.getProcessingFolder() + fileName));
                    String InvoiceNumber = keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().getCommonRefNumber();
                    Double versionNumber = Double.parseDouble(keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getVersionNo());
                    String submittedTime = keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getUpdatedDate();
                    // System.out.println(" s t"+keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getUpdatedDate());
                    // Query file creation time if more than 48 hours delete the file 
                    Date date = new Date(new File(applicationConfigurationXMLMapper.getProcessingFolder() + fileName).lastModified());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    String fileCreatedOnDate = sdf.format(date);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                    Date curdate = new Date();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    String curDate = sdf1.format(curdate);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));;
                    Date d1 = sdf1.parse(fileCreatedOnDate);
                    Date d2 = sdf1.parse(curDate);
                    //System.out.println("File  date " + d1);
                    //System.out.println("Current date " + d2);
                    //System.out.println("FILE: " + fileName + " INVOICE NUMBER: " + InvoiceNumber + " Version Number: " + versionNumber + " FILE CREATION DATE: " + fileCreatedOnDate + " CURRENT DATE: " + curDate);
                    //in milliseconds
                    long diff = d2.getTime() - d1.getTime();
                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    //System.out.print(diffDays + " days ");

                    int recCdFileMsgConsignmentId = 0;
                    RecCdFileMsg recCdFileMsg = dbao_omp2.getRecCDFileMsgbyFileName(fileName);
                    int isFirstMessageSent = 0;
                    if (!(recCdFileMsg == null)) {
                        recCdFileMsgConsignmentId = recCdFileMsg.getECSCONSIGNEMENTIDRef();
                        isFirstMessageSent = recCdFileMsg.getResCdFileMsgCollection().size();
                    }

                    /**
                     *
                     * for (Iterator<ResCdFileMsg> iterator1 =
                     * recCdFileMsg.getResCdFileMsgCollection().iterator();
                     * iterator1.hasNext();) { ResCdFileMsg resCdFileMsg =
                     * iterator1.next();
                     *
                     * }
                     *
                     */
                    //send first message if there is no previous message sent --SUBMITTED--
                    if (dbao_omp.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("SUBMITTED")
                            && (recCdFileMsgConsignmentId != 0)) {
                        //inspection status 0 
                        String additionaldetails = submittedTime;

                        //   ecsKeswsEntitiesControllerCaller.trackTransactionDetails("SUBMITTEDINSPECTIONSTATUS", 0, fileName, additionaldetails);
                        if (isFirstMessageSent == 0) {
                            ConsignmentApprovalStatus resObj = new ConsignmentApprovalStatus();
                            DocumentHeaderType resObjDocHeader = new DocumentHeaderType();
                            DocumentDetailsType resObjDocDetails = new DocumentDetailsType();
                            ContainerType resObjconType = new ContainerType();
                            resObjDocHeader.setMsgid("OG_CD_RES");//OGA_CD_RES
                            resObjDocHeader.setRefno(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
                            String msgdate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                            resObjDocHeader.setMsgdate(msgdate);
                            resObjDocHeader.setMsgFunc(BigInteger.valueOf(9));
                            resObjDocHeader.setReceiver("KESWS");
                            resObjDocHeader.setSender("KEPHIS");
                            resObjDocHeader.setVersion(versionNumber.toString());

                            resObjDocDetails.setConsignmentRefnumber(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
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
                            resObjDocDetails.setPgaRemarks("submitted for inspection " + dbao_omp.getECSFinalconsignmentInspectionResult(InvoiceNumber) + "");
                            resObjDocDetails.setConditionalRemarks("submitted for inspection " + dbao_omp.getECSconsignmentInspectionFindings(InvoiceNumber));
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
                            String file = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate + ".xml";
                            resObjm.marshal(resObj, new File(file));
                            String[] attachments = new String[]{"", ""};
                            String attachment = "";
                            if ((recCdFileMsgConsignmentId != 0) && (fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment))) {
                                filesinQue.add(file);
//                                ResCdFileMsg resCdFileMsg = dbao_omp2.resCDFileMsg(recCdFileMsg, 2);
                            }
                        }
                    }
                    /**
                     * *
                     * TO DO this to be replaced with database trigger hence
                     * check status on OG_CD_REC Table
                     *
                     */

                    /**
                     * *
                     * TO DO this to be replaced with database trigger hence
                     * check status on OG_CD_REC Table
                     *
                     */
                    if (dbao_omp.getECSconsignmentStatus(InvoiceNumber, 0).contains("PENDING")) {
                        //inspection status 1
                        String additionaldetails = submittedTime;
                        dbao_omp.trackTransactionDetails("PENDINGINSPECTIONSTATUS", 1, fileName, additionaldetails);
                    }
                    /**
                     * *
                     * TO DO this to be replaced with database trigger hence
                     * check status on OG_CD_REC Table
                     *
                     */
                    if (dbao_omp.getECSconsignmentStatus(InvoiceNumber, 0).contains("PLANNED")) {
                        //inspection status 0
                        String additionaldetails = submittedTime;
                        //  ecsKeswsEntitiesControllerCaller.trackTransactionDetails("PLANNEDINSPECTIONSTATUS", 0, fileName, additionaldetails);
                    }
                    if (dbao_omp.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("REJECTED")
                            && (recCdFileMsgConsignmentId != 0)) {
                        //inspection status 0 
                        String additionaldetails = submittedTime;

                        //   ecsKeswsEntitiesControllerCaller.trackTransactionDetails("SUBMITTEDINSPECTIONSTATUS", 0, fileName, additionaldetails);
                        if (isFirstMessageSent == 1) {
                            ConsignmentApprovalStatus resObj2 = new ConsignmentApprovalStatus();
                            DocumentHeaderType resObjDocHeader2 = new DocumentHeaderType();
                            DocumentDetailsType resObjDocDetails2 = new DocumentDetailsType();
                            ContainerType resObjconType2 = new ContainerType();
                            resObjDocHeader2.setMsgid("OG_CD_RES");//OGA_CD_RES
                            resObjDocHeader2.setRefno(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
                            String msgdate2 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                            resObjDocHeader2.setMsgdate(msgdate2);
                            resObjDocHeader2.setMsgFunc(BigInteger.valueOf(9));
                            resObjDocHeader2.setReceiver("KESWS");
                            resObjDocHeader2.setSender("KEPHIS");
                            resObjDocHeader2.setVersion(versionNumber.toString());

                            resObjDocDetails2.setConsignmentRefnumber(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
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
                            resObjDocDetails2.setPgaRemarks("Rejected" + dbao_omp.getECSFinalconsignmentInspectionResult(InvoiceNumber));
                            resObjDocDetails2.setConditionalRemarks("Rejected" + dbao_omp.getECSconsignmentInspectionFindings(InvoiceNumber));
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
                            String file2 = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate2 + ".xml";
                            resObjm2.marshal(resObj2, new File(file2));
                            String[] attachments = new String[]{"", ""};
                            String attachment = "";
//  if(true){
                            if ((recCdFileMsgConsignmentId != 0) && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file2, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate2 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment)) {
                                filesinQue.add(file2);
//                                ResCdFileMsg resCdFileMsg = dbao_omp2.resCDFileMsg(recCdFileMsg, 2);
                            }
                        }
                    }
// to be changed to issued
                    if (dbao_omp.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("PLANNED")
                            && (recCdFileMsgConsignmentId != 0)) {
                        //inspection status 0 
                        String additionaldetails = submittedTime;

                        //   ecsKeswsEntitiesControllerCaller.trackTransactionDetails("SUBMITTEDINSPECTIONSTATUS", 0, fileName, additionaldetails);
                        if (isFirstMessageSent == 1) {
                            ConsignmentApprovalStatus resObj3 = new ConsignmentApprovalStatus();
                            DocumentHeaderType resObjDocHeader3 = new DocumentHeaderType();
                            DocumentDetailsType resObjDocDetails3 = new DocumentDetailsType();
                            ContainerType resObjconType3 = new ContainerType();
                            resObjDocHeader3.setMsgid("OG_CD_RES");//OGA_CD_RES
                            resObjDocHeader3.setRefno(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
                            String msgdate3 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                            resObjDocHeader3.setMsgdate(msgdate3);
                            resObjDocHeader3.setMsgFunc(BigInteger.valueOf(9));
                            resObjDocHeader3.setReceiver("KESWS");
                            resObjDocHeader3.setSender("KEPHIS");
                            resObjDocHeader3.setVersion(versionNumber.toString());

                            resObjDocDetails3.setConsignmentRefnumber(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
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
                            resObjDocDetails3.setPgaRemarks("Passed" + dbao_omp.getECSFinalconsignmentInspectionResult(InvoiceNumber));
                            resObjDocDetails3.setConditionalRemarks("Passed" + dbao_omp.getECSconsignmentInspectionFindings(InvoiceNumber));
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
                            String file3 = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate3 + ".xml";
                            resObjm3.marshal(resObj3, new File(file3));
                            String[] attachments = new String[]{"", ""};
                            String attachment = "";

                            // if(true){
                            if ((recCdFileMsgConsignmentId != 0)
                                    && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file3, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate3 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment)) {
                                filesinQue.add(file3);
                                //        ResCdFileMsg resCdFileMsg = dbao_omp2.resCDFileMsg(recCdFileMsg, 2);
                            }

                        }

                        if (diffMinutes > 30) {
                            //email inspector
                            //   ECSKESWSFileLogger.mailnotification("Kindly check the consignment " + InvoiceNumber + " on ecs it has been pending for 1/2 hour");

                        }
                        if (diffHours > 1) {
                            //email it support
                            //   ECSKESWSFileLogger.mailnotification("Kindly check the consignment " + InvoiceNumber + " on ecs it has been pending for 1 hour and will be deleted in one hours time");

                        }

                        if (diffHours > 2) {
                            //delete
                            deleteFile = fileName;

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
//                                        ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
                }

            }

            /**
             * 
             * query database for consignements received and response messages
             * not sent by checking on saved files in processing box and get
             * application refrence query ecs database based on refrence id
             * create response message object create response message file and
             * save to out box send file remove files from processing box
             * determine if response message is generated by the system check
             * outbox if so send response messsage to KESWS if not query
             * response message and create outbox if successfull else repeat
             * process\ INSERT INTO `ecshscodepc`.`transaction_logs` (`ID`,
             * `RELATEDTRANSACTIONID`, `TRANSACTIONTYPE`, `TRANSACTIONDETAILS`,
             * `RECEIVESTATUS`, `RECEIVETIME`, `PROCESSSTATUS`, `PROCESSTIME`,
             * `RESPONSESTATUS`, `REPONSETIME`, `ACHIVESTATUS`) VALUES (NULL,
             * '0', '1', 'RECEIPT', '1', NULL, NULL, NULL, NULL, NULL, NULL);
             *
             */
            //delete certificate 
            File file = new File(applicationConfigurationXMLMapper.getProcessingFolder() + deleteFile);
            filespendingprocesser.remove(deleteFile);

            if (file.delete()) {
                //System.out.println(file.getName() + " is deleted!");
                break;
            } else {
                //System.out.println("Delete operation is failed.");
            }

        }

        try {
            // java.lang.Thread.sleep(120000);
            for (Iterator<String> iterator = filesinQue.iterator(); iterator.hasNext();) {
                String next = iterator.next();
                File f = new File(next);
                if (f.delete()) {
                    //System.out.println(file.getName() + " is deleted!");
                    break;
                } else {
                    //System.out.println("Delete operation is failed.");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Send approval IO and CO messages with payment details
     *
     * @param fileprocessor
     * @param applicationConfigurationXMLMapper
     */
    private static void scenario2(FileProcessor fileprocessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {

        EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesControllerCaller = new EcsKeswsEntitiesControllerCaller();
        EcsEntitiesControllerCaller ecsEntitiesControllerCaller = new EcsEntitiesControllerCaller();

        List<String> filesinQue = new ArrayList<String>();
        fileprocessor.readFilesBeingProcessed(applicationConfigurationXMLMapper.getProcessingFolder());
        List<String> filespendingprocesser = fileprocessor.getFilesbeingProcessed();
        for (Iterator<String> iterator = filespendingprocesser.iterator(); iterator.hasNext();) {
            String fileName = (String) iterator.next();
            String deleteFile = "";
            if (fileName.contains(applicationConfigurationXMLMapper.getFilesTypestoReceive().get(0).toString())) {
                JAXBContext context = null;
                try {
                    context = JAXBContext.newInstance(ConsignmentDocument.class);
                    Unmarshaller um = null;
                    um = context.createUnmarshaller();
                    ConsignmentDocument keswsConsignmentDocumentObj = null;
                    keswsConsignmentDocumentObj = (ConsignmentDocument) um.unmarshal(new FileReader(applicationConfigurationXMLMapper.getProcessingFolder() + fileName));
                    String InvoiceNumber = keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().getCommonRefNumber();
                    Double versionNumber = Double.parseDouble(keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getVersionNo());
                    String submittedTime = keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getUpdatedDate();
                    // System.out.println(" s t"+keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getUpdatedDate());
                    // Query file creation time if more than 48 hours delete the file 
                    UtilityClass util = new UtilityClass();
                    Date date = new Date(new File(applicationConfigurationXMLMapper.getProcessingFolder() + fileName).lastModified());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    String fileCreatedOnDate = sdf.format(date);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                    Date curdate = new Date();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    String curDate = sdf1.format(curdate);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));;
                    Date d1 = sdf1.parse(fileCreatedOnDate);
                    Date d2 = sdf1.parse(curDate);
                    //System.out.println("File  date " + d1);
                    //System.out.println("Current date " + d2);
                    //System.out.println("FILE: " + fileName + " INVOICE NUMBER: " + InvoiceNumber + " Version Number: " + versionNumber + " FILE CREATION DATE: " + fileCreatedOnDate + " CURRENT DATE: " + curDate);
                    //in milliseconds
                    long diff = d2.getTime() - d1.getTime();
                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    //System.out.print(diffDays + " days ");

                    int recCdFileMsgConsignmentId = 0;
                    RecCdFileMsg recCdFileMsg = ecsKeswsEntitiesControllerCaller.getRecCDFileMsgbyFileName(fileName);
                    int isFirstMessageSent = 0;
                    if (!(recCdFileMsg == null)) {
                        recCdFileMsgConsignmentId = recCdFileMsg.getECSCONSIGNEMENTIDRef();
                        isFirstMessageSent = recCdFileMsg.getResCdFileMsgCollection().size();
                        System.err.println(recCdFileMsgConsignmentId + " First messsage set to 0" + isFirstMessageSent);

                    }

                    // payment pending option 
                    if (ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("SUBMITTED") && (recCdFileMsgConsignmentId != 0)) {
                        //inspection status 0
                        String additionaldetails = submittedTime;
                        if (isFirstMessageSent == 0) {
                            ConsignmentApprovalStatus resObj4 = new ConsignmentApprovalStatus();
                            DocumentHeaderType resObjDocHeader4 = new DocumentHeaderType();
                            DocumentDetailsType resObjDocDetails4 = new DocumentDetailsType();
                            ContainerType resObjconType4 = new ContainerType();
                            resObjDocHeader4.setMsgid("OG_CD_RES");//OGA_CD_RES
                            resObjDocHeader4.setRefno(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
                            String msgdate4 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                            resObjDocHeader4.setMsgdate(msgdate4);
                            resObjDocHeader4.setMsgFunc(BigInteger.valueOf(9));
                            resObjDocHeader4.setReceiver("KESWS");
                            resObjDocHeader4.setSender("KEPHIS");
                            resObjDocHeader4.setVersion(versionNumber.toString());

                            resObjDocDetails4.setConsignmentRefnumber(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
                            resObjDocDetails4.setVerNo("1");
                            resObjDocDetails4.setCertificateNo("");
                            resObjDocDetails4.setPermitNo("");
                            resObjDocDetails4.setStatus("PP");
                            resObjDocDetails4.setRoleCode("CO");
                            resObjDocDetails4.setVoInd("N");
                            resObjDocDetails4.setIoInd("Y");
                            resObjDocDetails4.setInspectionType("PI");

                            resObjconType4.setContNumber("");
                            resObjDocDetails4.setContDetails(resObjconType4);
                            resObjDocDetails4.setUserId("kephisco1");
                            resObjDocDetails4.setPgaRemarks("Pending payment" + ecsEntitiesControllerCaller.getECSFinalconsignmentInspectionResult(recCdFileMsgConsignmentId));
                            resObjDocDetails4.setConditionalRemarks("Pending payment" + ecsEntitiesControllerCaller.getECSconsignmentInspectionFindings(recCdFileMsgConsignmentId));
                            resObjDocDetails4.setOgaQueries("");
                            resObjDocDetails4.setQueriesResponse("");
                            resObjDocDetails4.setAmount(new BigDecimal(1200));
                            resObjDocDetails4.setCurrency("KSH");
                            resObjDocDetails4.setRevenueCode("1001");
                            resObjDocDetails4.setExpiryDate("20151231240000");
                            resObjDocDetails4.setPgaRiskAssessmentLane("1");
                            resObjDocDetails4.setAssessedBy("kephisco1");
                            resObjDocDetails4.setAssessedDate(msgdate4);
                            resObjDocDetails4.setAssessedRemarks("");
                            resObj4.setDocumentDetails(resObjDocDetails4);
                            resObj4.setDocumentHeader(resObjDocHeader4);
                            JAXBContext contextresObj = JAXBContext.newInstance(ConsignmentApprovalStatus.class);
                            Marshaller resObjm4 = contextresObj.createMarshaller();
                            resObjm4.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                            final String file4 = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate4 + ".xml";
                            File file = new File(file4);
                            resObjm4.marshal(resObj4, file);
                            String[] attachments = new String[]{"", ""};
                            String attachment = "";

                            // if(true){
                            if ((recCdFileMsgConsignmentId != 0) && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file4, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate4 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment)) {

                                String sourceDir = applicationConfigurationXMLMapper.getOutboxFolder();
                                String destDir = applicationConfigurationXMLMapper.getOutboxArchiveFolder()
                                        + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                        + File.separator + util.getCurrentDay() + File.separator + "outbox" + File.separator;
                                fileprocessor.moveXmlFileProcessed(sourceDir, destDir, file4.substring(applicationConfigurationXMLMapper.getOutboxFolder().length(), file4.length()));
                                String desfilepath = destDir + file4.substring(applicationConfigurationXMLMapper.getOutboxFolder().length(), file4.length());
                                ResCdFileMsg resCdFileMsg = ecsKeswsEntitiesControllerCaller.resCDFileMsg(recCdFileMsg, 2, desfilepath);
                                if (file.delete()) {
                                    System.out.println("Deleted");
                                } else {
                                }
                            }

                        }

                    }
                    /**
                     * *
                     * TO DO this to be replaced with database trigger hence
                     * check status on OG_CD_REC Table
                     *
                     */
                    if (ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, 0).contains("PENDING")) {
                        //inspection status 1
                        String additionaldetails = submittedTime;
                        //   ecsKeswsEntitiesControllerCaller.trackTransactionDetails("PENDINGINSPECTIONSTATUS", 1, fileName, additionaldetails);
                    }
                    /**
                     * *
                     * TO DO this to be replaced with database trigger hence
                     * check status on OG_CD_REC Table /** * TO DO this to be
                     * replaced with database trigger hence check status on
                     * OG_CD_REC Table
                     *
                     */

                    if (ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("REJECTED")
                            && (recCdFileMsgConsignmentId != 0)) {
                        //inspection status 0 
                        String additionaldetails = submittedTime;
                        System.err.println("isFirstMessageSent 2" + isFirstMessageSent);
                        //   ecsKeswsEntitiesControllerCaller.trackTransactionDetails("SUBMITTEDINSPECTIONSTATUS", 0, fileName, additionaldetails);
                        if (isFirstMessageSent == 1) {
                            ConsignmentApprovalStatus resObj2 = new ConsignmentApprovalStatus();
                            DocumentHeaderType resObjDocHeader2 = new DocumentHeaderType();
                            DocumentDetailsType resObjDocDetails2 = new DocumentDetailsType();
                            ContainerType resObjconType2 = new ContainerType();
                            resObjDocHeader2.setMsgid("OG_CD_RES");//OGA_CD_RES
                            resObjDocHeader2.setRefno(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
                            String msgdate2 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                            resObjDocHeader2.setMsgdate(msgdate2);
                            resObjDocHeader2.setMsgFunc(BigInteger.valueOf(9));
                            resObjDocHeader2.setReceiver("KESWS");
                            resObjDocHeader2.setSender("KEPHIS");
                            resObjDocHeader2.setVersion(versionNumber.toString());

                            resObjDocDetails2.setConsignmentRefnumber(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
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
                            resObjDocDetails2.setPgaRemarks("Rejected" + ecsEntitiesControllerCaller.getECSFinalconsignmentInspectionResult(recCdFileMsgConsignmentId));
                            resObjDocDetails2.setConditionalRemarks("Rejected" + ecsEntitiesControllerCaller.getECSconsignmentInspectionFindings(recCdFileMsgConsignmentId));
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
                            final String file2 = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate2 + ".xml";
                            resObjm2.marshal(resObj2, new File(file2));
                            String[] attachments = new String[]{"", ""};
                            String attachment = "";
//  if(true){
                            if ((recCdFileMsgConsignmentId != 0) && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file2, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate2 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment)) {
                                System.err.println(recCdFileMsgConsignmentId + " First messsage " + file2 + " set to 1" + isFirstMessageSent);
                                filesinQue.add(file2);
                                File f = new File(applicationConfigurationXMLMapper.getOutboxFolder() + file2);
                                if (f.delete()) {
                                } else {
                                }
                                String sourceDir = applicationConfigurationXMLMapper.getOutboxFolder();
                                String destDir = applicationConfigurationXMLMapper.getOutboxArchiveFolder() + File.separator
                                        + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                        + File.separator + util.getCurrentDay() + File.separator + "outbox" + File.separator;
                                fileprocessor.moveXmlFileProcessed(sourceDir, destDir, file2);
                                recCdFileMsg.setFilePath(destDir + file2);
//                                ResCdFileMsg resCdFileMsg = ecsKeswsEntitiesControllerCaller.resCDFileMsg(recCdFileMsg, 2);

                            }
                        }
                    }

                    if (ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("PLANNED")
                            && (recCdFileMsgConsignmentId != 0)) {
                        //inspection status 0
                        String additionaldetails = submittedTime;

                        System.err.println("isFirstMessageSent 3 planned " + isFirstMessageSent);
                        //   ecsKeswsEntitiesControllerCaller.trackTransactionDetails("SUBMITTEDINSPECTIONSTATUS", 0, fileName, additionaldetails);
                        if (isFirstMessageSent == 1) {
                            ConsignmentApprovalStatus resObj3 = new ConsignmentApprovalStatus();
                            DocumentHeaderType resObjDocHeader3 = new DocumentHeaderType();
                            DocumentDetailsType resObjDocDetails3 = new DocumentDetailsType();
                            ContainerType resObjconType3 = new ContainerType();
                            resObjDocHeader3.setMsgid("OG_CD_RES");//OGA_CD_RES
                            resObjDocHeader3.setRefno(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
                            String msgdate3 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                            resObjDocHeader3.setMsgdate(msgdate3);
                            resObjDocHeader3.setMsgFunc(BigInteger.valueOf(9));
                            resObjDocHeader3.setReceiver("KESWS");
                            resObjDocHeader3.setSender("KEPHIS");
                            resObjDocHeader3.setVersion(versionNumber.toString());

                            resObjDocDetails3.setConsignmentRefnumber(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
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
                            resObjDocDetails3.setPgaRemarks("Passed" + ecsEntitiesControllerCaller.getECSFinalconsignmentInspectionResult(recCdFileMsgConsignmentId));
                            resObjDocDetails3.setConditionalRemarks("Passed" + ecsEntitiesControllerCaller.getECSconsignmentInspectionFindings(recCdFileMsgConsignmentId));
                            resObjDocDetails3.setOgaQueries("");
                            resObjDocDetails3.setQueriesResponse("");
                            resObjDocDetails3.setAmount(BigDecimal.ZERO);
                            resObjDocDetails3.setCurrency("KES");
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
                            final String file3 = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate3 + ".xml";
                            resObjm3.marshal(resObj3, new File(file3));
                            String[] attachments = new String[]{"", ""};
                            String attachment = "";
                            // if(true){
                            if ((recCdFileMsgConsignmentId != 0) && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file3, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate3 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment)) {
                                filesinQue.add(file3);
                                File f = new File(applicationConfigurationXMLMapper.getOutboxFolder() + file3);
                                if (f.delete()) {
                                } else {
                                }
                                String sourceDir = applicationConfigurationXMLMapper.getOutboxFolder();
                                String destDir = applicationConfigurationXMLMapper.getOutboxArchiveFolder() + File.separator
                                        + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                        + File.separator + util.getCurrentDay() + File.separator + "outbox" + File.separator;
                                fileprocessor.moveXmlFileProcessed(sourceDir, destDir, file3);
                                recCdFileMsg.setFilePath(destDir + file3);
//                                ResCdFileMsg resCdFileMsg = ecsKeswsEntitiesControllerCaller.resCDFileMsg(recCdFileMsg, 2);

                            }

                        }

                        if (diffMinutes > 30) {
                            //email inspector
                            //   ECSKESWSFileLogger.mailnotification("Kindly check the consignment " + InvoiceNumber + " on ecs it has been pending for 1/2 hour");

                        }
                        if (diffHours > 1) {
                            //email it support
                            //   ECSKESWSFileLogger.mailnotification("Kindly check the consignment " + InvoiceNumber + " on ecs it has been pending for 1 hour and will be deleted in one hours time");

                        }

                        if (diffHours > 2) {
                            //delete
                            deleteFile = fileName;

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
//                                        ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
                }

            }

            /**
             * *
             * query database for consignements received and response messages
             * not sent by checking on saved files in processing box and get
             * application refrence query ecs database based on refrence id
             * create response message object create response message file and
             * save to out box send file remove files from processing box
             * determine if response message is generated by the system check
             * outbox if so send response messsage to KESWS if not query
             * response message and create outbox if successfull else repeat
             * process\ INSERT INTO `ecshscodepc`.`transaction_logs` (`ID`,
             * `RELATEDTRANSACTIONID`, `TRANSACTIONTYPE`, `TRANSACTIONDETAILS`,
             * `RECEIVESTATUS`, `RECEIVETIME`, `PROCESSSTATUS`, `PROCESSTIME`,
             * `RESPONSESTATUS`, `REPONSETIME`, `ACHIVESTATUS`) VALUES (NULL,
             * '0', '1', 'RECEIPT', '1', NULL, NULL, NULL, NULL, NULL, NULL);
             *
             */
            //delete certificate 
            File file = new File(applicationConfigurationXMLMapper.getProcessingFolder() + deleteFile);
            filespendingprocesser.remove(deleteFile);

            if (file.delete()) {
                //System.out.println(file.getName() + " is deleted!");
                break;
            } else {
                //System.out.println("Delete operation is failed.");
            }

        }

        try {
            for (Iterator<String> iterator = filesinQue.iterator(); iterator.hasNext();) {
                String next = iterator.next();
                File f = new File(applicationConfigurationXMLMapper.getOutboxFolder() + next);
                if (f.delete()) {
                    System.out.println(f.getAbsolutePath() + " is deleted!");

                } else {
                    System.out.println("Delete operation is failed.");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void scenario3FileProcessor(FileProcessor fileProcessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {
        EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController = new EcsKeswsEntitiesControllerCaller();
        EcsEntitiesControllerCaller ecsEntitiesController = new EcsEntitiesControllerCaller(applicationConfigurationXMLMapper);
        OutgoingMessageProcessor outGoingMessage;
        List<Integer> SubmittedConsignmentIds = new ArrayList<Integer>();

        // Query submitted consignement id 
        // check if consignement id is not on response table
        // if not on response table create object to send back
        //send file 
        //update ecsResCdfileMsg
        // query
        SubmittedConsignmentIds = ecsEntitiesController.getSubmittedConsignementIds();

        for (Iterator<Integer> iterator = SubmittedConsignmentIds.iterator(); iterator.hasNext();) {
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
                    System.out.println("File path " + cdResFile.getAbsoluteFile());
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
            }
        }

        ecsKeswsEntitiesController.CloseEmf();
    }

    public static void scenario3FileProcessorTester(FileProcessor fileProcessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {
        EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController = new EcsKeswsEntitiesControllerCaller();
        EcsEntitiesControllerCaller ecsEntitiesController = new EcsEntitiesControllerCaller(applicationConfigurationXMLMapper);
        OutgoingMessageProcessor outGoingMessage;
        List<Integer> SubmittedConsignmentIds = new ArrayList<Integer>();

        // Query submitted consignement id 
        // check if consignement id is not on response table
        // if not on response table create object to send back
        //send file 
        //update ecsResCdfileMsg
        // query
        SubmittedConsignmentIds = ecsEntitiesController.getSubmittedConsignementIds();

        for (Iterator<Integer> iterator = SubmittedConsignmentIds.iterator(); iterator.hasNext();) {
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
                    ECSConsignmentDoc ecsConsignmentDocumentObj = new ECSConsignmentDoc();
                    UtilityClass util = new UtilityClass();
                    context = JAXBContext.newInstance(org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.class);
                    um = context.createUnmarshaller();
                    keswsConsignmentDocumentObj = (org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument) um.unmarshal(new FileReader(resTemplateFile));
                    List<EcsConDoc> ecsConDocDetails = ecsKeswsEntitiesController.findEcsConDocByConsignmentId(SubmittedConsignmentId);
                    String ecsConDocDetailItemId = "";
                    Integer itemCounter = 0;
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
                    System.out.println("itemCounter " + cdResFile.getAbsoluteFile());
                    ecsResCdFileMsg = ecsKeswsEntitiesController.createEcsResCdFileMsg(fileName, 5, SubmittedConsignmentId);
                    if (ecsResCdFileMsg != null) {

                        resObjm.marshal(keswsConsignmentDocumentObj, cdResFile);
                        String[] attachments = new String[]{"", ""};
                        String attachment = "";
                        XmlFileValidator xmlvalidator = new XmlFileValidator();
                        boolean isvalidXmlFile = xmlvalidator.isValidOgCdResOFile(applicationConfigurationXMLMapper.getInboxFolder() + resTemplateFile);
                        boolean isvalidXmlFileforResponse = xmlvalidator.isValidOgCdResOFileForResponse(keswsConsignmentDocumentObj);
                        if (isvalidXmlFile && isvalidXmlFileforResponse) {

                            ecsKeswsEntitiesController.OgUpdateResCd1Msg(ecsResCdFileMsg);
                            if ((SubmittedConsignmentId != 0) && (fileProcessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file, attachments, "OG_SUB_CD-" + RefrenceNo + "-1-" + "B-" + msgyear + SeqNumber + ".xml", RefrenceNo, "OG_SUB_CD", attachment))) {
                                ecsKeswsEntitiesController.logInfo2(fileName, "SUBMITTED " + fileName);
                                ecsKeswsEntitiesController.OgUpdateResCd1Msg(ecsResCdFileMsg);
                            }
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

        //DBDAO dbao_omp = new DBDAO();
        List<String> filesinQue = new ArrayList<String>();
        UtilityClass util = new UtilityClass();
        fileprocessor.readFilesBeingProcessed(applicationConfigurationXMLMapper.getProcessingFolder());
        List<String> filespendingprocesser = fileprocessor.getFilesbeingProcessed();
        try {
            for (Iterator<String> iterator = filespendingprocesser.iterator(); iterator.hasNext();) {
                String fileName = (String) iterator.next();

                String deleteFile = "";
                if (fileName.contains(applicationConfigurationXMLMapper.getFilesTypestoReceive().get(0).toString())) {
                    JAXBContext context = null;

                    try {
                        context = JAXBContext.newInstance(org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.class);
                        Unmarshaller um = null;
                        um = context.createUnmarshaller();
                        org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument keswsConsignmentDocumentObj = null;
                        //  try{
                        keswsConsignmentDocumentObj = (org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument) um.unmarshal(new FileReader(applicationConfigurationXMLMapper.getProcessingFolder() + fileName));
                        //   }
                        //   catch(Exception e){
                        //      System.out.println(applicationConfigurationXMLMapper.getProcessingFolder() + fileName);
                        //  e.printStackTrace();
                        //  System.exit(0);
                        //   }
                        String InvoiceNumber = keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().getCommonRefNumber();
                        System.out.println("(InvoiceNumber)" + InvoiceNumber);
                        Double versionNumber = Double.parseDouble(keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getVersionNo());
                        String submittedTime = keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getUpdatedDate();
                        Date date = new Date(new File(applicationConfigurationXMLMapper.getProcessingFolder() + fileName).lastModified());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                        String fileCreatedOnDate = sdf.format(date);
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                        Date curdate = new Date();
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                        String curDate = sdf1.format(curdate);
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));;
                        Date d1 = sdf1.parse(fileCreatedOnDate);
                        Date d2 = sdf1.parse(curDate);
                        //System.out.println("File  date " + d1);
                        //System.out.println("Current date " + d2);
                        //System.out.println("FILE: " + fileName + " INVOICE NUMBER: " + InvoiceNumber + " Version Number: " + versionNumber + " FILE CREATION DATE: " + fileCreatedOnDate + " CURRENT DATE: " + curDate);
                        //in milliseconds
                        long diff = d2.getTime() - d1.getTime();
                        long diffSeconds = diff / 1000 % 60;
                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000) % 24;
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                        //System.out.print(diffDays + " days ");

                        int recCdFileMsgConsignmentId = 0;
                        EcsResCdFileMsg recCdFileMsg = ecsKeswsEntitiesControllerCaller.findECSResCdFileMsgbyFileName(fileName);
                        int isFirstMessageSent = 0;
                        if (!(recCdFileMsg == null)) {
                            recCdFileMsgConsignmentId = recCdFileMsg.getECSCONSIGNEMENTIDRef();
                            isFirstMessageSent = recCdFileMsg.getResCdFileMsgCollection().size();
                        }
                        EcsResCdFileMsg ecsResCdFileMsg = ecsKeswsEntitiesControllerCaller.findECSResCdFileMsgbyConsignmentId(recCdFileMsgConsignmentId);
                        int clientId = ecsEntitiesControllerCaller.getClientId(keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getTIN());
                        String ClientPin = keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getTIN();
                        String ClientName = keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getName();
                        String ClientCustomerId = ecsEntitiesControllerCaller.getClientCustomerId(ClientPin);
                        float Amount = 1000;
                        String ChargeDescription = "Phytosanitary Certificate (Commercial Agricultural commondities)".toUpperCase();
                        String ServiceType = "Default price".toUpperCase();
                        if (ecsResCdFileMsg != null) {
                            for (Iterator<CdFileDetails> iterator1 = ecsResCdFileMsg.getCdFileDetailsCollection().iterator(); iterator1.hasNext();) {
                                CdFileDetails next = iterator1.next();
                                Amount = (float) (next.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef().getPRICELISTPriceIDRef().getChargeKshs());
                                ChargeDescription = next.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef().getPRICELISTPriceIDRef().getChargeDescription().trim();
                                ServiceType = next.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef().getDocumentIDRef() + ServiceType;

                            }
                        }
                        // select all on submitted table not on payment log messages
                        //System.out.println("select all on submitted table not on payment log messages");
                        //System.out.println(" STATUS:  "+ ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId));
                        
                        if (ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("PENDING")
                                && (recCdFileMsgConsignmentId == 0)) {
                            //System.out.println("Inside recCdFileMsgConsignmentId == 0");
                            try {
                                ecsEntitiesControllerCaller.InvoiceConsignmentonECS(recCdFileMsgConsignmentId, ClientCustomerId, Amount, "Pending Accpac Invoice No", InvoiceNumber, ChargeDescription, ServiceType, ecsKeswsEntitiesControllerCaller, 1, "");
                                ecsKeswsEntitiesControllerCaller.logInfo2(fileName, " INVOICED CLIENT WITH PIN " + ClientPin.toUpperCase() + " FOR CONSIGNMENT ID " + recCdFileMsgConsignmentId + " ON ECS KES " + Amount + " FOR " + ChargeDescription.toUpperCase());

                                
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                               // System.out.println("Trying to creat client pin");
                                if (ecsEntitiesControllerCaller.CreateClientinACCPAC(ClientPin, ClientName)) {
                                   // System.out.println("JUST CREATED A CLIENT + " +ClientPin + " "+ ClientName);
                                   // System.out.println("Inside recCdFileMsgConsignmentId == 0");
                                    ecsEntitiesControllerCaller.InvoiceConsignmentonAccpac(recCdFileMsgConsignmentId, ClientPin, Amount, "Pending Accpac Invoice No", InvoiceNumber, ChargeDescription, ecsKeswsEntitiesControllerCaller);
                                    ecsKeswsEntitiesControllerCaller.logInfo2(fileName, " INVOICED CLIENT WITH PIN " + ClientPin.toUpperCase() + " FOR CONSIGNMENT ID " + recCdFileMsgConsignmentId + " ON ACCPAC KES " + Amount + " FOR " + ChargeDescription.toUpperCase());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("PENDING")
                                && (recCdFileMsgConsignmentId != 0)) {
                            String additionaldetails = submittedTime;
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

                        if (ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("REJECTED")
                                && (recCdFileMsgConsignmentId != 0)) {
                            //inspection status 0 
                            String additionaldetails = submittedTime;

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

                        if (ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("PENDING")
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
                File file = new File(applicationConfigurationXMLMapper.getProcessingFolder() + deleteFile);
                filespendingprocesser.remove(deleteFile);

                if (file.delete()) {
                    //System.out.println(file.getName() + " is deleted!");
                    break;
                } else {
                    //System.out.println("Delete operation is failed.");
                }

            }

            try {
                for (Iterator<String> iterator = filesinQue.iterator(); iterator.hasNext();) {
                    String next = iterator.next();
                    File f = new File(next);
                    if (f.delete()) {
                        System.out.println(f.getName() + " is deleted!");
                        break;
                    } else {
                        System.out.println("Delete operation is failed.");
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {

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
                        case 1: {
                            scenario1(fileprocessor, applicationConfigurationXMLMapper);
                        }
                        case 2: {
                            scenario2(fileprocessor, applicationConfigurationXMLMapper);
                        }
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

    private void scenario3CDApprovalMesgandprePayment(FileProcessor fileprocessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper, int paymentoption) {

        DBDAO dbao_omp = new DBDAO();
        DBDAO dbao_omp2 = new DBDAO();
        //List<String> filesinQue = new ArrayList<String>();
        //fileprocessor.readFilesBeingProcessed(applicationConfigurationXMLMapper.getProcessingFolder());
        ///List<String> filespendingprocesser = fileprocessor.getFilesbeingProcessed();
        
        List<String> filespendingprocesser = null; 
        //******************************************************************************************************************************
        //************************************REPLACE THIS SECTION WITH EXISTING DB CONNECTION *****************************************
        Connection conn = null;
        try {
      // This will load the MySQL driver, each DB has its own driver
      Class.forName("com.mysql.jdbc.Driver");
      // Setup the connection with the DB
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecs_kesws?" +
              "user=ecsapps&password=ecsapps");
    
            if (conn != null) {
                System.out.println("Connection created");
                CallableStatement cs = conn.prepareCall("{call CDKE_ID()}");
                ResultSet rs = cs.executeQuery();

                while (rs.next()) {
                    String documentNo = rs.getString("CDKE_ID");
                    filespendingprocesser.add(documentNo);
                }
            }
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        //******************************************************************************************************************************
        
        for (Iterator<String> iterator = filespendingprocesser.iterator(); iterator.hasNext();) {
            String InvoiceNumber =  (String) iterator.next();
            String deleteFile = "";
            if (true) {
                //JAXBContext context = null;
                try {
                    //context = JAXBContext.newInstance(ConsignmentDocument.class);
                   // Unmarshaller um = null;
                   // um = context.createUnmarshaller();
                    //ConsignmentDocument keswsConsignmentDocumentObj = null;
                    //keswsConsignmentDocumentObj = (ConsignmentDocument) um.unmarshal(new FileReader(applicationConfigurationXMLMapper.getProcessingFolder() + fileName));
                    //String InvoiceNumber = keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().getCommonRefNumber();
                    //Double versionNumber = Double.parseDouble(keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getVersionNo());
                    //String submittedTime = keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getUpdatedDate();
                    
                    Double versionNumber = 1.1;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    Date date = new Date();
                    String submittedTime = sdf.format(date);
                    // System.out.println(" s t"+keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getUpdatedDate());
                    // Query file creation time if more than 48 hours delete the file 
                    //Date date = new Date(new File(applicationConfigurationXMLMapper.getProcessingFolder() + fileName).lastModified());
                    
                    String fileCreatedOnDate = sdf.format(date);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                    Date curdate = new Date();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    String curDate = sdf1.format(curdate);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));;
                    Date d1 = sdf1.parse(fileCreatedOnDate);
                    Date d2 = sdf1.parse(curDate);
                    //System.out.println("File  date " + d1);
                    //System.out.println("Current date " + d2);
                    //System.out.println("FILE: " + fileName + " INVOICE NUMBER: " + InvoiceNumber + " Version Number: " + versionNumber + " FILE CREATION DATE: " + fileCreatedOnDate + " CURRENT DATE: " + curDate);
                    //in milliseconds
                    long diff = d2.getTime() - d1.getTime();
                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    //System.out.print(diffDays + " days ");

                    int recCdFileMsgConsignmentId = 0;
//                    RecCdFileMsg recCdFileMsg = dbao_omp2.getRecCDFileMsgbyFileName(fileName);
                   int isFirstMessageSent = 0;
//                    if (!(recCdFileMsg == null)) {
//                        recCdFileMsgConsignmentId = recCdFileMsg.getECSCONSIGNEMENTIDRef();
//                        isFirstMessageSent = recCdFileMsg.getResCdFileMsgCollection().size();
//                    }
                    
                                   
                         recCdFileMsgConsignmentId = recCdFileMsgConsignmentId = Integer.parseInt(InvoiceNumber.substring(17, InvoiceNumber.length()));
                        
                    /**
                     *
                     * for (Iterator<ResCdFileMsg> iterator1 =
                     * recCdFileMsg.getResCdFileMsgCollection().iterator();
                     * iterator1.hasNext();) { ResCdFileMsg resCdFileMsg =
                     * iterator1.next();
                     *
                     * }
                     *
                     */
                    //send first message if there is no previous message sent --SUBMITTED--
                    if (dbao_omp.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("PLANNED")
                            && (recCdFileMsgConsignmentId != 0)) {
                        //inspection status 0 
                        String additionaldetails = submittedTime;

                        //   ecsKeswsEntitiesControllerCaller.trackTransactionDetails("SUBMITTEDINSPECTIONSTATUS", 0, fileName, additionaldetails);
                        if (isFirstMessageSent == 0) {
                            ConsignmentApprovalStatus resObj = new ConsignmentApprovalStatus();
                            DocumentHeaderType resObjDocHeader = new DocumentHeaderType();
                            DocumentDetailsType resObjDocDetails = new DocumentDetailsType();
                            ContainerType resObjconType = new ContainerType();
                            resObjDocHeader.setMsgid("OG_CD_RES");//OGA_CD_RES
                            resObjDocHeader.setRefno(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
                            String msgdate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                            resObjDocHeader.setMsgdate(msgdate);
                            resObjDocHeader.setMsgFunc(BigInteger.valueOf(9));
                            resObjDocHeader.setReceiver("KESWS");
                            resObjDocHeader.setSender("KEPHIS");
                            resObjDocHeader.setVersion(versionNumber.toString());

                            resObjDocDetails.setConsignmentRefnumber(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
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
                            resObjDocDetails.setPgaRemarks("submitted for inspection " + dbao_omp.getECSFinalconsignmentInspectionResult(InvoiceNumber) + "");
                            resObjDocDetails.setConditionalRemarks("submitted for inspection " + dbao_omp.getECSconsignmentInspectionFindings(InvoiceNumber));
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
                            String file = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate + ".xml";
                            resObjm.marshal(resObj, new File(file));
                            String[] attachments = new String[]{"", ""};
                            String attachment = "";
                            if ((recCdFileMsgConsignmentId != 0) && (fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment))) {
                                //filesinQue.add(file);
                                //ResCdFileMsg resCdFileMsg = dbao_omp2.resCDFileMsg(recCdFileMsg, 2);
                            }
                        }
                    }
                    
                    /**
                     * *
                     * TO DO this to be replaced with database trigger hence
                     * check status on OG_CD_REC Table
                     *
                     */

                    /**
                     * *
                     * TO DO this to be replaced with database trigger hence
                     * check status on OG_CD_REC Table
                     *
                     */
                    if (dbao_omp.getECSconsignmentStatus(InvoiceNumber, 0).contains("PENDING")) {
                        //inspection status 1
                        //String additionaldetails = submittedTime;
                       dbao_omp.trackTransactionDetails("PENDINGINSPECTIONSTATUS", 1, InvoiceNumber, submittedTime);
                    }
                    /**
                     * *
                     * TO DO this to be replaced with database trigger hence
                     * check status on OG_CD_REC Table
                     *
                     */
                    if (dbao_omp.getECSconsignmentStatus(InvoiceNumber, 0).contains("PLANNED")) {
                        //inspection status 0
                        String additionaldetails = submittedTime;
                        //  ecsKeswsEntitiesControllerCaller.trackTransactionDetails("PLANNEDINSPECTIONSTATUS", 0, fileName, additionaldetails);
                    }
                    if (dbao_omp.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("REJECTED")
                            && (recCdFileMsgConsignmentId != 0)) {
                        //inspection status 0 
                        String additionaldetails = submittedTime;

                        //   ecsKeswsEntitiesControllerCaller.trackTransactionDetails("SUBMITTEDINSPECTIONSTATUS", 0, fileName, additionaldetails);
                        if (isFirstMessageSent == 1) {
                            ConsignmentApprovalStatus resObj2 = new ConsignmentApprovalStatus();
                            DocumentHeaderType resObjDocHeader2 = new DocumentHeaderType();
                            DocumentDetailsType resObjDocDetails2 = new DocumentDetailsType();
                            ContainerType resObjconType2 = new ContainerType();
                            resObjDocHeader2.setMsgid("OG_CD_RES");//OGA_CD_RES
                            resObjDocHeader2.setRefno(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
                            String msgdate2 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                            resObjDocHeader2.setMsgdate(msgdate2);
                            resObjDocHeader2.setMsgFunc(BigInteger.valueOf(9));
                            resObjDocHeader2.setReceiver("KESWS");
                            resObjDocHeader2.setSender("KEPHIS");
                            resObjDocHeader2.setVersion(versionNumber.toString());

                            resObjDocDetails2.setConsignmentRefnumber(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
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
                            resObjDocDetails2.setPgaRemarks("Rejected" + dbao_omp.getECSFinalconsignmentInspectionResult(InvoiceNumber));
                            resObjDocDetails2.setConditionalRemarks("Rejected" + dbao_omp.getECSconsignmentInspectionFindings(InvoiceNumber));
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
                            String file2 = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate2 + ".xml";
                            resObjm2.marshal(resObj2, new File(file2));
                            String[] attachments = new String[]{"", ""};
                            String attachment = "";
//  if(true){
                            if ((recCdFileMsgConsignmentId != 0) && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file2, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate2 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment)) {
                               // filesinQue.add(file2);
//                                ResCdFileMsg resCdFileMsg = dbao_omp2.resCDFileMsg(recCdFileMsg, 2);
                            }
                        }
                    }
// to be changed to issued
                    if (dbao_omp.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("ISSUED")
                            && (recCdFileMsgConsignmentId != 0)) {
                        //inspection status 0 
                        String additionaldetails = submittedTime;

                        //   ecsKeswsEntitiesControllerCaller.trackTransactionDetails("SUBMITTEDINSPECTIONSTATUS", 0, fileName, additionaldetails);
                        if (isFirstMessageSent == 1) {
                            ConsignmentApprovalStatus resObj3 = new ConsignmentApprovalStatus();
                            DocumentHeaderType resObjDocHeader3 = new DocumentHeaderType();
                            DocumentDetailsType resObjDocDetails3 = new DocumentDetailsType();
                            ContainerType resObjconType3 = new ContainerType();
                            resObjDocHeader3.setMsgid("OG_CD_RES");//OGA_CD_RES
                            resObjDocHeader3.setRefno(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
                            String msgdate3 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                            resObjDocHeader3.setMsgdate(msgdate3);
                            resObjDocHeader3.setMsgFunc(BigInteger.valueOf(9));
                            resObjDocHeader3.setReceiver("KESWS");
                            resObjDocHeader3.setSender("KEPHIS");
                            resObjDocHeader3.setVersion(versionNumber.toString());

                            resObjDocDetails3.setConsignmentRefnumber(InvoiceNumber.substring(0, InvoiceNumber.length() - 2));
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
                            resObjDocDetails3.setPgaRemarks("Passed" + dbao_omp.getECSFinalconsignmentInspectionResult(InvoiceNumber));
                            resObjDocDetails3.setConditionalRemarks("Passed" + dbao_omp.getECSconsignmentInspectionFindings(InvoiceNumber));
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
                            String file3 = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate3 + ".xml";
                            resObjm3.marshal(resObj3, new File(file3));
                            String[] attachments = new String[]{"", ""};
                            String attachment = "";

                            // if(true){
//                            if ((recCdFileMsgConsignmentId != 0)
//                                    && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file3, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate3 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment)) {
//                                filesinQue.add(file3);
//                                //        ResCdFileMsg resCdFileMsg = dbao_omp2.resCDFileMsg(recCdFileMsg, 2);
//                            }

                        }

                        if (diffMinutes > 30) {
                            //email inspector
                            //   ECSKESWSFileLogger.mailnotification("Kindly check the consignment " + InvoiceNumber + " on ecs it has been pending for 1/2 hour");

                        }
                        if (diffHours > 1) {
                            //email it support
                            //   ECSKESWSFileLogger.mailnotification("Kindly check the consignment " + InvoiceNumber + " on ecs it has been pending for 1 hour and will be deleted in one hours time");

                        }

                        if (diffHours > 2) {
                            //delete
                            //deleteFile = fileName;

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
//                                        ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
                }

    }
            File file = new File(applicationConfigurationXMLMapper.getProcessingFolder() + deleteFile);
            filespendingprocesser.remove(deleteFile);

            if (file.delete()) {
                //System.out.println(file.getName() + " is deleted!");
                break;
            } else {
                //System.out.println("Delete operation is failed.");
            }

        }

        try {

//            for (Iterator<String> iterator = filesinQue.iterator(); iterator.hasNext();) {
//                String next = iterator.next();
//                File f = new File(next);
//                if (f.delete()) {
//                    //System.out.println(file.getName() + " is deleted!");
//                    break;
//                } else {
//                    //System.out.println("Delete operation is failed.");
//                }
//            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
