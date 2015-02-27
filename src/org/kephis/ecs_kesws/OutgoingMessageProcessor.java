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

/**
 *
 * @author kim
 */
class OutgoingMessageProcessor implements Runnable {

    private volatile boolean stop = false;// used to stop thread
    private static Lock lock = new ReentrantLock();//locking mechanism to have just one thread run

    public OutgoingMessageProcessor() {
    }

    @Override
    public void run() {
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
                            scenario3FileProcessor(fileprocessor, applicationConfigurationXMLMapper);
                            scenario3CDApprovalMesg(fileprocessor, applicationConfigurationXMLMapper);
                            //scenario3CDApprovalMesg(fileprocessor, applicationConfigurationXMLMapper,);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void getMessages(FileProcessor fileprocessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try {
            fileprocessor.retrieveMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), applicationConfigurationXMLMapper.getInboxFolder(), true);
        } catch (Exception e) {
            e.printStackTrace(); //TODO send notification 
        }

    }

    void sendErrorMessage(String receivedFileName, String invalid_XML_File) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * send approval IO and CO Messages
     *
     * @param fileprocessor
     * @param applicationConfigurationXMLMapper
     */
    private void scenario1(FileProcessor fileprocessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {

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
            java.lang.Thread.sleep(120000);
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
    private void scenario2(FileProcessor fileprocessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {

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
                            if ((recCdFileMsgConsignmentId != 0)) {
                                // && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file4, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate4 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment)) {

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
                            if ((recCdFileMsgConsignmentId != 0)) {
                                //   && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file3, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate3 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment)) {
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

    public void scenario3FileProcessor(FileProcessor fileProcessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {
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
                    String file = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_SUB_CD-" + RefrenceNo + "-B-" + msgyear + SeqNumber + ".xml";
                    String fileName = "OG_SUB_CD-" + RefrenceNo + "-B-" + msgyear + SeqNumber + ".xml";
                    File cdResFile = new File(file);
                    System.out.println("itemCounter " + cdResFile.getAbsoluteFile());
                    ecsResCdFileMsg = ecsKeswsEntitiesController.createEcsResCdFileMsg(fileName, 5, SubmittedConsignmentId);
                    if (ecsResCdFileMsg != null) {

                        for (Iterator<EcsConDoc> iterator1 = ecsConDocDetails.iterator(); iterator1.hasNext();) {
                            EcsConDoc ecsConDocDetail = iterator1.next();
                            ecsResCdFileMsg.setInvoiceNO(ecsConDocDetail.getInvoiceNr());
                            keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().setCommonRefNumber("" + ecsConDocDetail.getDocumentNumber());
                            keswsConsignmentDocumentObj.getDocumentHeader().getDocumentReference().setCommonRefNumber("" + RefrenceNo + "01");
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getServiceProvider().setApplicationCode("" + ecsConDocDetail.getExporterFirmName().substring(0, 3));
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getServiceProvider().setName("" + ecsConDocDetail.getExporterFirmName());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().getServiceProvider().setPhyCountry("" + ecsConDocDetail.getExporterCntCode());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setDocumentCode("" + docType);
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setProcessCode("KEEXPProc");// TO DO // CHANGE ON GO LIVE
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setApplicationDate("" + util.convertDatetoStringyyyMMdd(ecsConDocDetail.getConsignementApplicationDate()));
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setUpdatedDate("" + util.convertDatetoStringyyyMMddhhss(ecsConDocDetail.getConsignementApplicationDate()));
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setApprovalDate("" + util.convertDatetoStringyyyMMdd(ecsConDocDetail.getConsignementApplicationDate()));
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setFinalApprovalDate("" + util.convertDatetoStringyyyMMddhhss(ecsConDocDetail.getConsignementApplicationDate()));
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setApplicationRefNo("" + RefrenceNo);
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDStandard().setUCRNumber("" + ecsConDocDetail.getUCRNo());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setName("" + ecsConDocDetail.getImporterFirm());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setPhysicalAddress(ecsConDocDetail.getExporterPhysicalAddress());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setPostalAddress(ecsConDocDetail.getImporterPhysicalAddress2() + ecsConDocDetail.getImporterPostalCode());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDImporter().setPosCountry(ecsConDocDetail.getImporterCountryIso());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setName(ecsConDocDetail.getImporterFirm());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setPhysicalAddress(ecsConDocDetail.getImporterPhysicalAddress());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setPostalAddress(ecsConDocDetail.getImporterPhysicalAddress2() + ecsConDocDetail.getImporterPostalCode());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().setPosCountry(ecsConDocDetail.getImporterCountryIso());

                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setName(ecsConDocDetail.getExporterFirmName());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setTIN(ecsConDocDetail.getExporterPin());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setPhyCountry(ecsConDocDetail.getExporterCntCode());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setPostalAddress(ecsConDocDetail.getExporterPostNumber());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setPosCountry(ecsConDocDetail.getExporterCntCode());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setTeleFax("");
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setWarehouseCode(ecsConDocDetail.getExporterCntCode());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDExporter().setWarehouseLocation(ecsConDocDetail.getExporterCntCode());

                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setName(ecsConDocDetail.getExporterFirmName());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setTIN(ecsConDocDetail.getExporterPin());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setPhyCountry(ecsConDocDetail.getExporterCntCode());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setPostalAddress(ecsConDocDetail.getExporterPostNumber());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setPosCountry(ecsConDocDetail.getExporterCntCode());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setTeleFax("");
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setWarehouseCode(ecsConDocDetail.getExporterCntCode());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDConsignor().setWarehouseLocation(ecsConDocDetail.getExporterCntCode());

                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setModeOfTransport(ecsConDocDetail.getTransMode().substring(0, 1));
                            if (ecsConDocDetail.getTransMode() == "Air transport") {
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setModeOfTransport("Air");
                            }
                            if (ecsConDocDetail.getTransMode() == "Sea transport") {
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setModeOfTransport("Sea");
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setVesselName(ecsConDocDetail.getVesselName());
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setVoyageNo(ecsConDocDetail.getShippingOrder());
                            }
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setPortOfArrival(ecsConDocDetail.getPortOfArrival());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setPortOfArrivalDesc(ecsConDocDetail.getPortOfArrivalDesc());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setPortOfDeparture(ecsConDocDetail.getPortOfDeparture());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setPortOfDepartureDesc(ecsConDocDetail.getPortOfDepartureDesc());
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setCustomsOffice("");
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDTransport().setCustomsOfficeDesc("");
                            if (ecsConDocDetail.getCollectionOfficeId() != null) {
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getPGAHeaderFields().setCollectionOffice("" + ecsConDocDetail.getCollectionOfficeId().toString());
                            }
                            keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getPGAHeaderFields().setPreferredInspectionDate(util.convertDatetoStringddMMyyyhhmmss(ecsConDocDetail.getShipmentDate()));// TO Update

                            List<CdFileDetails> cdFileList = new LinkedList<CdFileDetails>();

                            if (!ecsConDocDetailItemId.equals(ecsConDocDetail.getId())) {
                                itemCounter = itemCounter + 1;
                                System.out.println("itemCounter " + ecsConDocDetail.getId());
                                ecsConDocDetailItemId = ecsConDocDetail.getId();
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDHeaderOne().setFOBFCY(BigDecimal.valueOf(itemCounter.doubleValue()));
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDHeaderOne().setCIFFCY(BigDecimal.valueOf(itemCounter.doubleValue()));
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDHeaderOne().setFOBNCY(BigDecimal.valueOf(itemCounter.doubleValue()));
                                keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDHeaderOne().setCIFNCY(BigDecimal.valueOf(itemCounter.doubleValue()));
                                // change item one details appropriately 
                                org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetailsType.ProductDetailsType.ItemDetails itemDetail = keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDProductDetails().getItemDetails().get(0);
                                if (itemCounter == 1) {
                                    itemDetail.setItemCount(itemCounter.toString());
                                    itemDetail.getCDProduct1().setItemDescription(ecsConDocDetail.getItemCommonName());
                                    //HSCODE LOOK UP IF NULL
                                    itemDetail.getCDProduct1().setItemHSCode(ecsConDocDetail.getHscode());
                                    itemDetail.getCDProduct1().setHSDescription(ecsConDocDetail.getHscodeDesc());
                                    itemDetail.getCDProduct1().setInternalProductNo(ecsConDocDetail.getItemInternalProductCode());
                                    itemDetail.getCDProduct1().setProductClassCode(ecsConDocDetail.getKeswsItemClass());
                                    itemDetail.getCDProduct1().setProductClassDescription(ecsConDocDetail.getKeswsItemClassDesc());
                                    itemDetail.getCDProduct1().getQuantity().setQty(BigDecimal.valueOf(ecsConDocDetail.getItemUnitQuantity()));
                                    //UNIT QUANTITY LOOK UP IF NULL
                                    itemDetail.getCDProduct1().getQuantity().setUnitOfQty(ecsConDocDetail.getItemUnit());
                                    itemDetail.getCDProduct1().getQuantity().setUnitOfQtyDesc(ecsConDocDetail.getKeswsUnitName());
                                    //UNIT Package LOOK UP IF NULL
                                    itemDetail.getCDProduct1().setPackageQty(BigDecimal.valueOf(ecsConDocDetail.getItemNumberOfPackages()));
                                    itemDetail.getCDProduct1().setPackageType(ecsConDocDetail.getKeswsPackageCode());
                                    itemDetail.getCDProduct1().setPackageTypeDesc(ecsConDocDetail.getKeswsPackageCode());
                                    //UNIT Weight mapping 
                                    itemDetail.getCDProduct1().setItemNetWeight(BigDecimal.valueOf(ecsConDocDetail.getItemUnitQuantity()));
                                    itemDetail.getCDProduct1().setItemGrossWeight(BigDecimal.valueOf(ecsConDocDetail.getItemSupUnitQuantity()));

                                    keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDProductDetails().getItemDetails().remove(itemCounter);
                                    keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDProductDetails().getItemDetails().add(itemDetail);

                                    InternalProductcodes IPCObj = null;
                                    Double weight = (Double) ecsConDocDetail.getItemUnitQuantity().doubleValue();

                                    if (!ecsKeswsEntitiesController.internalProductCodesExist(ecsConDocDetail.getItemInternalProductCode())) {
                                        IPCObj = new InternalProductcodes();
                                        Date date = new java.sql.Date(2010, 10, 10);
                                        IPCObj.setAggregateIPCCodeLevel(0);
                                        IPCObj.setInternalProductCode(ecsConDocDetail.getItemInternalProductCode());
                                        IPCObj.setHscode(ecsConDocDetail.getHscode());
                                        IPCObj.setHscodeDesc("SYSTEM GENERATED " + ecsConDocDetail.getHscodeDesc() + ":" + ecsConDocDetail.getItemCommonName());
                                        IPCObj.setDateDeactivated(date);
                                        ecsKeswsEntitiesController.createInternalProductcode(IPCObj);
                                        ecsKeswsEntitiesController.logInfo2(file, "CREATED IPC ENTRY ON ECSKESWSDB");

                                    }

                                    IPCObj = ecsKeswsEntitiesController.getInternalProductcodes(ecsConDocDetail.getItemInternalProductCode());
                                    ecsKeswsEntitiesController.updateCreateInternalProductcodePriceDocMappings(IPCObj);
                                    CdFileDetails cdFileDetails = ecsKeswsEntitiesController.recCDFileMsgDetails(ecsResCdFileMsg, IPCObj, weight);
                                    cdFileList.add(cdFileDetails);
                                }
                                if (itemCounter > 1) {
                                    // create additional fields
                                    itemDetail.setItemCount(itemCounter.toString());
                                    itemDetail.getCDProduct1().setItemNo(itemCounter);
                                    keswsConsignmentDocumentObj.getDocumentDetails().getConsignmentDocDetails().getCDProductDetails().getItemDetails().add(itemDetail);

                                }
                                keswsConsignmentDocumentObj.getDocumentSummary().setIssuedDateTime(docType);
                            }

                            ecsResCdFileMsg.setCdFileDetailsCollection(cdFileList);
                        }

                        resObjm.marshal(keswsConsignmentDocumentObj, cdResFile);
                        String[] attachments = new String[]{"", ""};
                        String attachment = "";
                        XmlFileValidator xmlvalidator = new XmlFileValidator();
                        boolean isvalidXmlFile = xmlvalidator.isValidOgCdResOFile(applicationConfigurationXMLMapper.getInboxFolder() + resTemplateFile);
                        boolean isvalidXmlFileforResponse = xmlvalidator.isValidOgCdResOFileForResponse(keswsConsignmentDocumentObj);
                        if (isvalidXmlFile && isvalidXmlFileforResponse) {

                            ecsKeswsEntitiesController.OgUpdateResCd1Msg(ecsResCdFileMsg);
                            if ((SubmittedConsignmentId != 0) && (fileProcessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file, attachments, "OG_SUB_CD-" + RefrenceNo + "-B-" + msgyear + SeqNumber + ".xml", RefrenceNo, "OG_SUB_CD", attachment))) {
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
    private void scenario3CDApprovalMesg(FileProcessor fileprocessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {

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
                        keswsConsignmentDocumentObj = (org.kephis.ecs_kesws.xml.parser.o.ogcdres.v_1_1.ConsignmentDocument) um.unmarshal(new FileReader(applicationConfigurationXMLMapper.getProcessingFolder() + fileName));

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
                                Amount = (float) (Amount + next.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef().getPRICELISTPriceIDRef().getChargeKshs());
                                ChargeDescription = next.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef().getPRICELISTPriceIDRef().getChargeDescription().trim();
                                ServiceType = next.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef().getDocumentIDRef() + ServiceType;

                            }
                        }
                        if (ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("SUBMITTED")
                                && (recCdFileMsgConsignmentId != 0)) {
                            String additionaldetails = submittedTime;
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
                                msgdate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                                resObjDocHeader.setMsgdate(msgdate);
                                resObjDocHeader.setMsgFunc(BigInteger.valueOf(9));
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
                                String file = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate + ".xml";
                                String fileN = "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate + ".xml";
                                resObjm.marshal(resObj, new File(file));
                                String[] attachments = new String[]{"", ""};
                                String attachment = "";
                                if ((recCdFileMsgConsignmentId != 0) && (fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment))) {
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

                        if (ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, 0).contains("PENDING")) {
                           
                            String additionaldetails = submittedTime; 
                        }
                        /**
                         * *
                         * TO DO this to be replaced with database trigger hence
                         * check status on OG_CD_REC Table
                         *
                         */
                        if (ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, 0).contains("PLANNED")) {
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
                                String file2 = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate2 + ".xml";
                                String fileN = "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate2 + ".xml";
                                resObjm2.marshal(resObj2, new File(file2));
                                String[] attachments = new String[]{"", ""};
                                String attachment = "";
                                if ((recCdFileMsgConsignmentId != 0) && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file2, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate2 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment)) {
                                    filesinQue.add(file2);
                                    String sourceDir = applicationConfigurationXMLMapper.getOutboxFolder();
                                    String destDir = applicationConfigurationXMLMapper.getOutboxArchiveFolder()
                                            + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                            + File.separator + util.getCurrentDay() + File.separator + "cd_res" + File.separator;
                                    fileprocessor.moveXmlFileProcessed(sourceDir, destDir, fileN);
                                    ResCdFileMsg resCdFileMsg = ecsKeswsEntitiesControllerCaller.resCDFileMsg(recCdFileMsg, 2, file2, fileN);
                                    ecsKeswsEntitiesControllerCaller.logInfo2(fileN, " SENT IO REJECT STATUS RESPONSE MESSAGE");
                                }
                            }
                        }
// to be changed to issued

                        if (ecsEntitiesControllerCaller.getECSconsignmentStatus(InvoiceNumber, recCdFileMsgConsignmentId).contains("ISSUED")
                                && (recCdFileMsgConsignmentId != 0)) {
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
                                String file3 = applicationConfigurationXMLMapper.getOutboxFolder() + "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate3 + ".xml";
                                String fileN = "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate3 + ".xml";
                                resObjm3.marshal(resObj3, new File(file3));
                                String[] attachments = new String[]{"", ""};
                                String attachment = "";
                                if ((recCdFileMsgConsignmentId != 0) && fileprocessor.submitMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), file3, attachments, "OG_CD_RES-" + InvoiceNumber.substring(0, InvoiceNumber.length() - 2) + "-1-" + "B-" + msgdate3 + ".xml", InvoiceNumber.substring(0, InvoiceNumber.length() - 2), "OG_CD_RES", attachment)) {
                                    filesinQue.add(file3);
                                    String sourceDir = applicationConfigurationXMLMapper.getOutboxFolder();
                                    String destDir = applicationConfigurationXMLMapper.getOutboxArchiveFolder()
                                            + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                            + File.separator + util.getCurrentDay() + File.separator + "cd_res" + File.separator;
                                    fileprocessor.moveXmlFileProcessed(sourceDir, destDir, fileN);
                                    ResCdFileMsg resCdFileMsg = ecsKeswsEntitiesControllerCaller.resCDFileMsg(recCdFileMsg, 2, file3, fileN);
                                    ecsKeswsEntitiesControllerCaller.logInfo2(fileN, " SENT IO PASS STATUS RESPONSE MESSAGE");
                                }

                            }
                            try {
//
                                //  System.out.println("INVOICEING CLIENT"+ recCdFileMsgConsignmentId+ ClientCustomerId+ Amount+ "Pending Accpac Invoice No"+ InvoiceNumber+ ChargeDescription+ServiceType);
                                ecsEntitiesControllerCaller.InvoiceConsignmentonECS(recCdFileMsgConsignmentId, ClientCustomerId, Amount, "Pending Accpac Invoice No", InvoiceNumber, ChargeDescription, ServiceType, ecsKeswsEntitiesControllerCaller, 2, "");
                                ecsKeswsEntitiesControllerCaller.logInfo2(fileName, "INVOICED CLIENT WITH PIN " + ClientPin.toUpperCase() + " FOR CONSIGNMENT ID " + recCdFileMsgConsignmentId + " ON ECS KES " + Amount + " FOR " + ChargeDescription.toUpperCase());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (ecsEntitiesControllerCaller.CreateClientinACCPAC(ClientPin, ClientName)) {
                                    ecsEntitiesControllerCaller.InvoiceConsignmentonAccpac(recCdFileMsgConsignmentId, ClientPin, Amount, "Pending Accpac Invoice No", InvoiceNumber, ChargeDescription, ecsKeswsEntitiesControllerCaller);
                                    ecsKeswsEntitiesControllerCaller.logInfo2(fileName, "INVOICED CLIENT WITH PIN " + ClientPin.toUpperCase() + " FOR CONSIGNMENT ID " + recCdFileMsgConsignmentId + " ON ACCPAC KES " + Amount + " FOR " + ChargeDescription.toUpperCase());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
                 * query database for consignements received and response
                 * messages not sent by checking on saved files in processing
                 * box and get application refrence query ecs database based on
                 * refrence id create response message object create response
                 * message file and save to out box send file remove files from
                 * processing box determine if response message is generated by
                 * the system check outbox if so send response messsage to KESWS
                 * if not query response message and create outbox if
                 * successfull else repeat process\ INSERT INTO
                 * `ecshscodepc`.`transaction_logs` (`ID`,
                 * `RELATEDTRANSACTIONID`, `TRANSACTIONTYPE`,
                 * `TRANSACTIONDETAILS`, `RECEIVESTATUS`, `RECEIVETIME`,
                 * `PROCESSSTATUS`, `PROCESSTIME`, `RESPONSESTATUS`,
                 * `REPONSETIME`, `ACHIVESTATUS`) VALUES (NULL, '0', '1',
                 * 'RECEIPT', '1', NULL, NULL, NULL, NULL, NULL, NULL);
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
                java.lang.Thread.sleep(120000);
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

    private void scenario3CDApprovalMesgandprePayment(FileProcessor fileprocessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper, int paymentoption) {

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
            java.lang.Thread.sleep(1200);
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
}
