 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.kephis.ecs_kesws.entities.EcsResCdFileMsg;
import org.kephis.ecs_kesws.entities.MessageTypes;
import org.kephis.ecs_kesws.entities.RecCdFileMsg;
import org.kephis.ecs_kesws.entities.RecErrorFileMsg;
import org.kephis.ecs_kesws.entities.ResCdFileMsg;
import org.kephis.ecs_kesws.entities.controllers.EcsEntitiesControllerCaller;
import org.kephis.ecs_kesws.entities.controllers.EcsKeswsEntitiesControllerCaller;
import org.kephis.ecs_kesws.utilities.FileProcessor;
import org.kephis.ecs_kesws.utilities.UtilityClass;
import org.kephis.ecs_kesws.xml.ApplicationConfigurationXMLMapper;
import org.kephis.ecs_kesws.xml.XmlFileMapper;
import org.kephis.ecs_kesws.xml.parser.ECSConsignmentDoc;
import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0.ConsignmentDocument;
import org.kephis.ecs_kesws.xml.parser.i.ogerrorres.v_1_1.ErrorResponse;
import org.kephis.ecs_kesws.xml.validator.XmlFileValidator;

/**
 *
 * @author kim
 */
class IncomingMessageProcessor implements Runnable {

    
    private int senario = 0;
    
    public IncomingMessageProcessor() {
        
    }

    /**
     * The constructor takes the scenario selected and calls
     * ......
     */
    public IncomingMessageProcessor(int scenario) {
        this.senario = scenario;
        this.run();
    }

    public void run() {
        FileProcessor fileprocessor = new FileProcessor();

        ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper = new ApplicationConfigurationXMLMapper();
        try {
               fileprocessor.retrieveMessage(applicationConfigurationXMLMapper.getMHXUserProfileFilePath(), applicationConfigurationXMLMapper.getSenderId(), applicationConfigurationXMLMapper.getInboxFolder(), true);
        } catch (Exception e) {
            e.printStackTrace(); //TODO send notification 
        }
        fileprocessor.readInboxXmlFilesForProcessor(applicationConfigurationXMLMapper.getInboxFolder());
        final List<String> inboxfiles = fileprocessor.getInboxFilesForProcessing();
        while (inboxfiles.size() >= 1) {
            String fileName = (String) inboxfiles.get(inboxfiles.size() - 1);
            try {
                // switch intergration based on selected model
             
                switch (senario) {
                    case 1:
                        scenario1FileProcessor(fileName, fileprocessor, applicationConfigurationXMLMapper);
                        break;
                    case 2:
                        scenario2FileProcessor(fileName, fileprocessor, applicationConfigurationXMLMapper);
                        break;
                    case 3:
                        //scenario1FileProcessor(fileName,fileprocessor, applicationConfigurationXMLMapper);
                         
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            inboxfiles.remove(fileName);
            /**
             * File file = new
             * File(applicationConfigurationXMLMapper.getInboxFolder() +
             * fileName); if (file.delete()) { //
             * System.out.println(file.getName() + " is deleted!"); break; }
             * else { // System.out.println("Delete operation is failed."); } *
             */
        }

    }

    /**
     * this method process OGA_SUB_CD document using scenario 1 Message flow
     * from Kentrade to ECS , payment option is post payed received OGA
     *
     * @param recievedFileType
     * @param applicationConfigurationXMLMapper
     */
    public void scenario1FileProcessor(String receivedFileName, FileProcessor fileProcessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {
        EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController = new EcsKeswsEntitiesControllerCaller();
        EcsEntitiesControllerCaller ecsEntitiesControllerCaller = new EcsEntitiesControllerCaller(applicationConfigurationXMLMapper);
        OutgoingMessageProcessor outGoingMessage;
        
            if (receivedFileName.contains(applicationConfigurationXMLMapper.getFilesTypestoReceive().get(0).toString())) {

            RecCdFileMsg recCdFileMsg = ecsKeswsEntitiesController.OgSubCd1Msg(receivedFileName, 1);
            try {
                // confirm the received file is unique and has been created 
                if (recCdFileMsg != null) {
                    JAXBContext context = null;
                    Unmarshaller um = null;
                    ConsignmentDocument keswsConsignmentDocumentObj = new ConsignmentDocument();
                    ECSConsignmentDoc ecsConsignmentDocumentObj = new ECSConsignmentDoc();
                    UtilityClass util = new UtilityClass();
                    context = JAXBContext.newInstance(ConsignmentDocument.class);
                    um = context.createUnmarshaller();
                    keswsConsignmentDocumentObj = (ConsignmentDocument) um.unmarshal(new FileReader(applicationConfigurationXMLMapper.getInboxFolder() + receivedFileName));
                    XmlFileValidator xmlvalidator = new XmlFileValidator();
                    boolean isvalidXmlFile = xmlvalidator.isValidOgCdSubIFile(applicationConfigurationXMLMapper.getInboxFolder() + receivedFileName);
                    boolean isvalidXmlFileforProcessing = xmlvalidator.isValidOgCdSubIFileForProcessing(keswsConsignmentDocumentObj);
                    if (!isvalidXmlFile) {
                        outGoingMessage = new OutgoingMessageProcessor();
                        outGoingMessage.sendErrorMessage(receivedFileName, "Invalid XML File");
                        ecsKeswsEntitiesController.logError(receivedFileName, xmlvalidator.getErrorDetails());
                    }
                    if (!isvalidXmlFileforProcessing) {
                        outGoingMessage = new OutgoingMessageProcessor();
                        outGoingMessage.sendErrorMessage(receivedFileName, "Invalid XML File for Processing");
                        ecsKeswsEntitiesController.logError(receivedFileName, xmlvalidator.getErrorDetails());
                    }
                    if (isvalidXmlFile && isvalidXmlFileforProcessing) {
                        
                        recCdFileMsg.setIsFileXMLValid(1);
                        recCdFileMsg.setIsFileXMLValidForTransaction(1);
                        
                        XmlFileMapper mapper = new XmlFileMapper();
                        int ecsClientId = ecsEntitiesControllerCaller.mapKESWSClientPintoECSClientId(keswsConsignmentDocumentObj);
                        ecsConsignmentDocumentObj = mapper.getxmlMappedECSConsignmentDoc(keswsConsignmentDocumentObj, ecsConsignmentDocumentObj);
                        ecsConsignmentDocumentObj = ecsEntitiesControllerCaller.getDatabaseMappings(keswsConsignmentDocumentObj, ecsConsignmentDocumentObj, ecsClientId);
                        ecsConsignmentDocumentObj = ecsEntitiesControllerCaller.createDatabaseMappings(keswsConsignmentDocumentObj, ecsConsignmentDocumentObj, ecsKeswsEntitiesController, ecsClientId);
                        if (ecsEntitiesControllerCaller.ecsTransactionCommit(recCdFileMsg, ecsKeswsEntitiesController, keswsConsignmentDocumentObj, ecsConsignmentDocumentObj, ecsClientId) == false) {
                            ecsEntitiesControllerCaller.ecsTransactionRollBack(receivedFileName);
                            //copy file to inboxfile archives ERROR/Year/Month/Day 
                            String sourceDir = applicationConfigurationXMLMapper.getInboxFolder();
                            String destDir = applicationConfigurationXMLMapper.getInboxArchiveFolder()
                                    + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                    + File.separator + util.getCurrentDay() + File.separator + "error" + File.separator;
                            fileProcessor.moveXmlFileProcessed(sourceDir, destDir, receivedFileName);
                            recCdFileMsg.setFilePath(destDir + receivedFileName);
                            //Send error message to ecs sys admin

                        } else {
                            //copy file to inboxfile archives OG_SUB_CD/Year/Month/Day 
                            String sourceDir = applicationConfigurationXMLMapper.getInboxFolder();
                            String destDir = applicationConfigurationXMLMapper.getInboxArchiveFolder()
                                    + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                    + File.separator + util.getCurrentDay() + File.separator + "inbox" + File.separator;
                            fileProcessor.moveXmlFileProcessed(sourceDir, destDir, receivedFileName);
                            recCdFileMsg.setFilePath(destDir + receivedFileName);
                            //update file path of received file on 
                        }
                        ecsKeswsEntitiesController.updateRecCDFileMsg(recCdFileMsg);
                    }
                } else {
                    //log duplicate file received 
                    ecsKeswsEntitiesController.logError(receivedFileName, "DUPLICATE FILE RECEIVED");
                    //copy file to inboxfile archives ERROR/Year/Month/Day
                    UtilityClass util = new UtilityClass();
                    String sourceDir = applicationConfigurationXMLMapper.getInboxFolder();
                    String destDir = applicationConfigurationXMLMapper.getInboxArchiveFolder()
                            + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                            + File.separator + util.getCurrentDay() + File.separator + "error" + File.separator;
                    fileProcessor.moveXmlFileProcessed(sourceDir, destDir, receivedFileName);
                    //Send error message to kesws duplicate file 
                }
            } catch (Exception e) {
                //copy file to inboxfile archives ERROR/Year/Month/Day
                UtilityClass util = new UtilityClass();
                String sourceDir = applicationConfigurationXMLMapper.getInboxFolder();
                String destDir = applicationConfigurationXMLMapper.getInboxArchiveFolder()
                        + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                        + File.separator + util.getCurrentDay() + File.separator + "error" + File.separator;
                fileProcessor.moveXmlFileProcessed(sourceDir, destDir, receivedFileName);
                fileProcessor.moveXmlFileProcessed(sourceDir, destDir, receivedFileName);
                //log to database and file

                ecsKeswsEntitiesController.logError(receivedFileName, "ERROR " + e.getMessage());
                e.printStackTrace();
            }

        }
        ecsKeswsEntitiesController.CloseEmf();
    }

    /**
     * this method process OGA_SUB_CD document using scenario 2 Message flow
     * from Kentrade to ECS , payment option is pre paid via NPG will process
     * incoming payment message in addition to cd message
     *
     * @param recievedFileType
     * @param applicationConfigurationXMLMapper
     */
    public void scenario2FileProcessor(String receivedFileName, FileProcessor fileProcessor, ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {

        scenario1FileProcessor(receivedFileName, fileProcessor, applicationConfigurationXMLMapper);

        if (receivedFileName.contains(applicationConfigurationXMLMapper.getFilesTypestoReceive().get(0).toString())) {
            OutgoingMessageProcessor outGoingMessage;
            EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController = new EcsKeswsEntitiesControllerCaller();
            EcsEntitiesControllerCaller ecsEntitiesControllerCaller = new EcsEntitiesControllerCaller(applicationConfigurationXMLMapper);

            RecCdFileMsg recCdFileMsg = ecsKeswsEntitiesController.OgSubCd1Msg(receivedFileName, 1);
            try {
                // confirm the received file is unique and has been created 
                if (recCdFileMsg != null) {
                    JAXBContext context = null;
                    Unmarshaller um = null;
                    ConsignmentDocument keswsConsignmentDocumentObj = new ConsignmentDocument();
                    ECSConsignmentDoc ecsConsignmentDocumentObj = new ECSConsignmentDoc();
                    UtilityClass util = new UtilityClass();
                    context = JAXBContext.newInstance(ConsignmentDocument.class);
                    um = context.createUnmarshaller();
                    keswsConsignmentDocumentObj = (ConsignmentDocument) um.unmarshal(new FileReader(applicationConfigurationXMLMapper.getInboxFolder() + receivedFileName));
                    XmlFileValidator xmlvalidator = new XmlFileValidator();
                    boolean isvalidXmlFile = xmlvalidator.isValidOgCdSubIFile(applicationConfigurationXMLMapper.getInboxFolder() + receivedFileName);
                    boolean isvalidXmlFileforProcessing = xmlvalidator.isValidOgCdSubIFileForProcessing(keswsConsignmentDocumentObj);
                    if (!isvalidXmlFile) {
                        outGoingMessage = new OutgoingMessageProcessor();
                        outGoingMessage.sendErrorMessage(receivedFileName, "Invalid XML File");
                        ecsKeswsEntitiesController.logError(receivedFileName, xmlvalidator.getErrorDetails());
                    }
                    if (!isvalidXmlFileforProcessing) {
                        outGoingMessage = new OutgoingMessageProcessor();
                        outGoingMessage.sendErrorMessage(receivedFileName, "Invalid XML File for Processing");
                        ecsKeswsEntitiesController.logError(receivedFileName, xmlvalidator.getErrorDetails());
                    }
                    if (isvalidXmlFile && isvalidXmlFileforProcessing) {
                        recCdFileMsg.setIsFileXMLValid(1);
                        recCdFileMsg.setIsFileXMLValidForTransaction(1);
                        XmlFileMapper mapper = new XmlFileMapper();
                        int ecsClientId = ecsEntitiesControllerCaller.mapKESWSClientPintoECSClientId(keswsConsignmentDocumentObj);
                        ecsConsignmentDocumentObj = mapper.getxmlMappedECSConsignmentDoc(keswsConsignmentDocumentObj, ecsConsignmentDocumentObj);
                        ecsConsignmentDocumentObj = ecsEntitiesControllerCaller.getDatabaseMappings(keswsConsignmentDocumentObj, ecsConsignmentDocumentObj, ecsClientId);
                        ecsConsignmentDocumentObj = ecsEntitiesControllerCaller.createDatabaseMappings(keswsConsignmentDocumentObj, ecsConsignmentDocumentObj, ecsKeswsEntitiesController, ecsClientId);
                        if (ecsEntitiesControllerCaller.ecsTransactionCommit(recCdFileMsg, ecsKeswsEntitiesController, keswsConsignmentDocumentObj, ecsConsignmentDocumentObj, ecsClientId) == false) {
                            ecsEntitiesControllerCaller.ecsTransactionRollBack(receivedFileName);
                            //copy file to inboxfile archives ERROR/Year/Month/Day 
                            String sourceDir = applicationConfigurationXMLMapper.getInboxFolder();
                            String destDir = applicationConfigurationXMLMapper.getInboxArchiveFolder()
                                    + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                    + File.separator + util.getCurrentDay() + File.separator + "error" + File.separator;
                            fileProcessor.moveXmlFileProcessed(sourceDir, destDir, receivedFileName);
                            recCdFileMsg.setFilePath(destDir + receivedFileName);
                            //Send error message to ecs sys admin

                        } else {
                            //copy file to inboxfile archives OG_SUB_CD/Year/Month/Day 
                            String sourceDir = applicationConfigurationXMLMapper.getInboxFolder();
                            String destDir = applicationConfigurationXMLMapper.getInboxArchiveFolder()
                                    + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                    + File.separator + util.getCurrentDay() + File.separator + "inbox" + File.separator;
                            fileProcessor.moveXmlFileProcessed(sourceDir, destDir, receivedFileName);
                            recCdFileMsg.setFilePath(destDir + receivedFileName);
                            //update file path of received file on 
                        }
                        ecsKeswsEntitiesController.updateRecCDFileMsg(recCdFileMsg);
                    }
                } else {
                    //log duplicate file received 
                    ecsKeswsEntitiesController.logError(receivedFileName, "DUPLICATE FILE RECEIVED");
                    //copy file to inboxfile archives ERROR/Year/Month/Day
                    UtilityClass util = new UtilityClass();
                    String sourceDir = applicationConfigurationXMLMapper.getInboxFolder();
                    String destDir = applicationConfigurationXMLMapper.getInboxArchiveFolder()
                            + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                            + File.separator + util.getCurrentDay() + File.separator + "error" + File.separator;
                    fileProcessor.moveXmlFileProcessed(sourceDir, destDir, receivedFileName);
                    //Send error message to kesws duplicate file 
                }
            } catch (Exception e) {
                //copy file to inboxfile archives ERROR/Year/Month/Day
                UtilityClass util = new UtilityClass();
                String sourceDir = applicationConfigurationXMLMapper.getInboxFolder();
                String destDir = applicationConfigurationXMLMapper.getInboxArchiveFolder()
                        + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                        + File.separator + util.getCurrentDay() + File.separator + "error" + File.separator;
                fileProcessor.moveXmlFileProcessed(sourceDir, destDir, receivedFileName);
                fileProcessor.moveXmlFileProcessed(sourceDir, destDir, receivedFileName);
                //log to database and file

                ecsKeswsEntitiesController.logError(receivedFileName, "ERROR " + e.getMessage());
                e.printStackTrace();
            }
            ecsKeswsEntitiesController.CloseEmf();
        }
    }
    public void errorFileMessageProcessor(){
    /***
            if (receivedFileName.contains(applicationConfigurationXMLMapper.getFilesTypestoReceive().get(1).toString())) {
                RecErrorFileMsg recErroMsg = ecsKeswsEntitiesController.OgErrorResMsg(receivedFileName, 2, null, null);
                try {
                    // confirm the received file is unique and has been created 
                    if (recErroMsg != null) {
                        
                    JAXBContext context = null;
                    Unmarshaller um = null;
                    ErrorResponse errorResponse = new ErrorResponse();
         
                    UtilityClass util = new UtilityClass();
                    context = JAXBContext.newInstance(ErrorResponse.class);
                    um = context.createUnmarshaller();
                    
                    errorResponse = (ErrorResponse) um.unmarshal(new FileReader(applicationConfigurationXMLMapper.getInboxFolder() + receivedFileName));
                    XmlFileValidator xmlvalidator = new XmlFileValidator();
                    boolean isvalidXmlFile = true;
        
                    boolean isvalidXmlFileforProcessing = true;
                            
                    if (!isvalidXmlFile) {
                        outGoingMessage = new OutgoingMessageProcessor();
                        outGoingMessage.sendErrorMessage(receivedFileName, "Invalid XML File");
                        ecsKeswsEntitiesController.logError(receivedFileName, xmlvalidator.getErrorDetails());
                    }
                    if (!isvalidXmlFileforProcessing) {
                        outGoingMessage = new OutgoingMessageProcessor();
                        outGoingMessage.sendErrorMessage(receivedFileName, "Invalid XML File for Processing");
                        ecsKeswsEntitiesController.logError(receivedFileName, xmlvalidator.getErrorDetails());
                    }
                    if (isvalidXmlFile && isvalidXmlFileforProcessing) {
                        
                        
                        RecCdFileMsg recCdFileMsg = ecsKeswsEntitiesController.findRecCdFileMsgbyFileName(receivedFileName);
                        if (recCdFileMsg != null) {
                            Collection<ResCdFileMsg> resCdFileMsgCollection = recCdFileMsg.getResCdFileMsgCollection();
                            ResCdFileMsg resCdFileMsg=new ResCdFileMsg();
                            for (Iterator<ResCdFileMsg> it = resCdFileMsgCollection.iterator(); it.hasNext();) {
                                 resCdFileMsg = it.next();
                                }
                            
                            recErroMsg.setResCdFileMsgResCdFileId(resCdFileMsg);
                            recErroMsg.setRecErrorMsgTime(new Date());
                            recErroMsg.setFileName(receivedFileName);
                    
                    //copy file to inboxfile archives ERROR/Year/Month/Day
                            String sourceDir = applicationConfigurationXMLMapper.getInboxFolder();
                            String destDir = applicationConfigurationXMLMapper.getInboxArchiveFolder()
                                    + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                                    + File.separator + util.getCurrentDay() + File.separator + "inbox" + File.separator;
                            fileProcessor.moveXmlFileProcessed(sourceDir, destDir, receivedFileName);
                            //Send error message to kesws duplicate file 
                            recErroMsg.setFilePath(destDir + receivedFileName);
                            recErroMsg.setMessageTypesMessageTypeId(new MessageTypes(6));

                        }
                       
                        
                        //for cd files sent.
                    //    EcsResCdFileMsg  ecsResCdFileMsg = ecsKeswsEntitiesController.findResCdFileMsgbyFileName(receivedFileName);
                        
                                          
                        
                       //TODO
                        
                        
                    }
                } else {
                    //log duplicate file received 
                    ecsKeswsEntitiesController.logError(receivedFileName, "DUPLICATE FILE RECEIVED");
                    //copy file to inboxfile archives ERROR/Year/Month/Day
                    UtilityClass util = new UtilityClass();
                    String sourceDir = applicationConfigurationXMLMapper.getInboxFolder();
                    String destDir = applicationConfigurationXMLMapper.getInboxArchiveFolder()
                            + util.getCurrentYear() + File.separator + util.getCurrentMonth()
                            + File.separator + util.getCurrentDay() + File.separator + "error" + File.separator;
                    fileProcessor.moveXmlFileProcessed(sourceDir, destDir, receivedFileName);
                    //Send error message to kesws duplicate file 
                    
                   
                    } 
                } catch (Exception ex) {
                }

                //RecErrorMsg recErroMsg = new RecErrorMsg();
               
            }
         * ***/}



}