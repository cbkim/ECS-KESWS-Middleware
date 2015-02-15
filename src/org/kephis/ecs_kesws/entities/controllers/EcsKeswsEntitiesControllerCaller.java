/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.entities.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.kephis.ecs_kesws.entities.CdFileDetails;
import org.kephis.ecs_kesws.entities.EcsConDoc;
import org.kephis.ecs_kesws.entities.EcsDocumentTypes;
import org.kephis.ecs_kesws.entities.InternalProductcodes;
import org.kephis.ecs_kesws.entities.LogTypes;
import org.kephis.ecs_kesws.entities.MessageTypes;
import org.kephis.ecs_kesws.entities.Pricelist;
import org.kephis.ecs_kesws.entities.PricelistInternalProductcodeDocumentMap;
import org.kephis.ecs_kesws.entities.RecCdFileMsg;
import org.kephis.ecs_kesws.entities.RecErrorFileMsg;
import org.kephis.ecs_kesws.entities.ResCdFileMsg;
import org.kephis.ecs_kesws.entities.EcsResCdFileMsg;
import org.kephis.ecs_kesws.entities.TransactionLogs;
import org.kephis.ecs_kesws.utilities.UtilityClass;
import org.kephis.ecs_kesws.entities.controllers.exceptions.IllegalOrphanException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author kim
 */
public class EcsKeswsEntitiesControllerCaller {

    EntityManagerFactory emf = null;
    EntityManager em = null;

    public EcsKeswsEntitiesControllerCaller() {

        //  Properties p = new Properties(System.getProperties());
        // p.put("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        //p.put("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "OFF");
        // or any other System.setProperties(p);
        // org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger("org.hibernate");
        // java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.WARNING);
        //or whatever level you need
        emf = Persistence.createEntityManagerFactory("ECS-KESWS-MiddlewarePU");

    }

    public ResCdFileMsg resCDFileMsg(RecCdFileMsg recCdFileMsg, int messageTypeId, String filePath) {
        ResCdFileMsg resCdFileMsg = null;
        MessageTypesJpaController messagetypeContr = new MessageTypesJpaController(emf);
        MessageTypes messageType = messagetypeContr.findMessageTypes(messageTypeId);
        ResCdFileMsgJpaController resCDFileMsgContr = new ResCdFileMsgJpaController(emf);
        int isResMessageSent = 0;
        if (!(recCdFileMsg == null)) {
            isResMessageSent = recCdFileMsg.getResCdFileMsgCollection().size();
        }
        if (isResMessageSent > 0) {

            for (Iterator<ResCdFileMsg> iterator1 = recCdFileMsg.getResCdFileMsgCollection().iterator(); iterator1.hasNext();) {
                resCdFileMsg = iterator1.next();
                if (resCdFileMsg.getMessageTypesMessageTypeId().getMessageTypeId() == 1) {
                    return resCdFileMsg;
                }
                if (resCdFileMsg.getMessageTypesMessageTypeId().getMessageTypeId() == 2) {
                    return resCdFileMsg;
                }
            }

        } else {
            resCdFileMsg = new ResCdFileMsg();
            resCdFileMsg.setRecCdFileMsgRecCdFileId(recCdFileMsg);
            resCdFileMsg.setMessageTypesMessageTypeId(messageType);
            resCdFileMsg.setFilePath(filePath);
            createResCdFileMsg(resCdFileMsg, recCdFileMsg);
            return recCdFileMsg.getResCdFileMsgCollection().iterator().next();
        }
        return resCdFileMsg;

    }

    public void createResCdFileMsg(ResCdFileMsg resCdFileMsg, RecCdFileMsg recCdFileMsg) {

        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecCdFileMsg recCdFileMsgRecCdFileId = recCdFileMsg;
            if (recCdFileMsgRecCdFileId != null) {
                resCdFileMsg.setRecCdFileMsgRecCdFileId(recCdFileMsgRecCdFileId);
            }
            MessageTypes messageTypesMessageTypeId = resCdFileMsg.getMessageTypesMessageTypeId();
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId = em.getReference(messageTypesMessageTypeId.getClass(), messageTypesMessageTypeId.getMessageTypeId());
                resCdFileMsg.setMessageTypesMessageTypeId(messageTypesMessageTypeId);
            }
            em.persist(resCdFileMsg);
            if (recCdFileMsgRecCdFileId != null) {
                recCdFileMsgRecCdFileId.getResCdFileMsgCollection().add(resCdFileMsg);
                recCdFileMsgRecCdFileId = em.merge(recCdFileMsgRecCdFileId);
            }
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId.getResCdFileMsgCollection().add(resCdFileMsg);
                messageTypesMessageTypeId = em.merge(messageTypesMessageTypeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public RecCdFileMsg OgSubCd1Msg(String receivedFileName, int message_type) {

        RecCdFileMsg recCdFileMsg = new RecCdFileMsg();

        MessageTypesJpaController messagetypeContr = new MessageTypesJpaController(emf);
        MessageTypes messageType = messagetypeContr.findMessageTypes(message_type);
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        recCdFileMsg.setFileName(receivedFileName);
        recCdFileMsg.setMessageTypesMessageTypeId(messageType);
        if (findRecCdFileMsgbyFileName(receivedFileName) == null) {
            try {
                recCDFileMsgContr.create(recCdFileMsg);
                recCdFileMsg = findRecCdFileMsgbyFileName(receivedFileName);
                return recCdFileMsg;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }

    public RecErrorFileMsg OgErrorResMsg(String receivedFilePath, int message_type, ResCdFileMsg resCdFileMsg, EcsResCdFileMsg ecsResCDFileMsg) {

        RecErrorFileMsg recErrorMsg = new RecErrorFileMsg();
        UtilityClass utilityclass = new UtilityClass();
        MessageTypesJpaController messagetypeContr = new MessageTypesJpaController(emf);
        MessageTypes messageType = messagetypeContr.findMessageTypes(message_type);
        RecErrorFileMsgJpaController recErrorFileMsgContr = new RecErrorFileMsgJpaController(emf);
        recErrorMsg.setFilePath(receivedFilePath);
        recErrorMsg.setResCdFileMsgResCdFileId(resCdFileMsg);
        recErrorMsg.setRecErrorMsgTime(utilityclass.getCurrentDate());

        if (findRecErrorMsgbyFilePath(receivedFilePath) == null) {
            try {
                recErrorFileMsgContr.create(recErrorMsg);
                recErrorMsg = findRecErrorMsgbyFilePath(receivedFilePath);
                return recErrorMsg;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }

    public CdFileDetails recCDFileMsgDetails(RecCdFileMsg CDFile, InternalProductcodes internalProductCode, Double Weight) {
        CdFileDetails cdFileDetails = new CdFileDetails();
        Double itemweight = 0.00;
        itemweight = itemweight + Weight;
        RecCdFileMsgJpaController recCdFileMsgController = new RecCdFileMsgJpaController(emf);

        if (internalProductCode.getAggregateIPCCodeLevel() == 0) {
            CdFileDetailsJpaController cdFileDetailsController = new CdFileDetailsJpaController(emf);
            if (recCdFileMsgController.findRecCdFileMsg(CDFile.getRECCDFileID()).getCdFileDetailsCollection().size() != 0) {
                for (Iterator iterator1 = CDFile.getCdFileDetailsCollection().iterator(); iterator1.hasNext();) {
                    cdFileDetails = (CdFileDetails) iterator1.next();
                    itemweight = itemweight + cdFileDetails.getWeight();
                    cdFileDetails.setWeight(itemweight);
                    PricelistInternalProductcodeDocumentMapJpaController plipcdoc = new PricelistInternalProductcodeDocumentMapJpaController(emf);
                    PricelistInternalProductcodeDocumentMap defaultdocprice = plipcdoc.findPricelistInternalProductcodeDocumentMap(0);
                    for (Iterator<PricelistInternalProductcodeDocumentMap> iterator = findPricelistIPCDocMapEntitiesbyIPC(internalProductCode).iterator(); iterator.hasNext();) {
                        PricelistInternalProductcodeDocumentMap next = (PricelistInternalProductcodeDocumentMap) iterator.next();
                        PricelistJpaController pricelistController = new PricelistJpaController(emf);
                        Pricelist pricelist = pricelistController.findPricelist(next.getPRICELISTPriceIDRef().getPriceID());
                        if ((pricelist.getMaxWeight() >= cdFileDetails.getWeight()) && (cdFileDetails.getWeight() <= pricelist.getMinWeight())) {
                            Double temp_max_weight = 0.00;
                            Double temp_min_weight = 0.00;
                            System.err.println(" temp_max_weight " + temp_max_weight);
                            System.err.println(" temp_min_weight " + temp_min_weight);
                            int pricelistid1 = 0;
                            int pricelistid2 = 0;
                            //set default to 0
                            if (temp_max_weight <= pricelist.getMaxWeight()) {
                                temp_max_weight = pricelist.getMaxWeight();
                                pricelistid1 = pricelist.getPriceID();
                                defaultdocprice = next;

                                System.err.println(" temp_max_weight_1 " + temp_max_weight);
                            }
                            if (temp_min_weight >= pricelist.getMinWeight()) {
                                temp_min_weight = pricelist.getMinWeight();
                                pricelistid2 = pricelist.getPriceID();

                                System.err.println(" temp_min_weight-1 " + temp_min_weight);
                            }
                            if (pricelistid2 == pricelistid1) {
                                cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);
                            } else {
                                cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);
                            }

                        }
                        cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);

                    }

                }
                return cdFileDetails;

            } else {

                cdFileDetails.setIpcIdCode(internalProductCode.getInternalProductCode());
                cdFileDetails.setAggregateIPCCodeLevel(internalProductCode.getAggregateIPCCodeLevel());
                cdFileDetails.setRECCDFILEMSGRECCDFILEIDRef(CDFile);

                cdFileDetails.setWeight(itemweight);
                PricelistInternalProductcodeDocumentMapJpaController plipcdoc = new PricelistInternalProductcodeDocumentMapJpaController(emf);
                PricelistInternalProductcodeDocumentMap defaultdocprice = plipcdoc.findPricelistInternalProductcodeDocumentMap(0);
                for (Iterator<PricelistInternalProductcodeDocumentMap> iterator = findPricelistIPCDocMapEntitiesbyIPC(internalProductCode).iterator(); iterator.hasNext();) {
                    PricelistInternalProductcodeDocumentMap next = (PricelistInternalProductcodeDocumentMap) iterator.next();
                    PricelistJpaController pricelistController = new PricelistJpaController(emf);
                    Pricelist pricelist = pricelistController.findPricelist(next.getPRICELISTPriceIDRef().getPriceID());
                    if ((pricelist.getMaxWeight() >= cdFileDetails.getWeight()) && (cdFileDetails.getWeight() <= pricelist.getMinWeight())) {
                        Double temp_max_weight = 0.00;
                        Double temp_min_weight = 0.00;
                        int pricelistid1 = 0;
                        int pricelistid2 = 0;
                        if (temp_max_weight <= pricelist.getMaxWeight()) {
                            temp_max_weight = pricelist.getMaxWeight();
                            pricelistid1 = pricelist.getPriceID();
                            defaultdocprice = next;
                        }
                        if (temp_min_weight >= pricelist.getMinWeight()) {
                            temp_min_weight = pricelist.getMinWeight();
                            pricelistid2 = pricelist.getPriceID();
                        }
                        if (pricelistid2 == pricelistid1) {
                            cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);
                        } else {
                            cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);
                        }

                    }
                }
                cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);
                cdFileDetails.setECSRESCDFILEMSGRECCDFileID(null);
                createCdFileDetails(cdFileDetails);
                for (Iterator<CdFileDetails> iterator = CDFile.getCdFileDetailsCollection().iterator(); iterator.hasNext();) {
                    cdFileDetails = iterator.next();
                }
                return cdFileDetails;
            }
            /**
             * } else if (true) { //aggregate level is 11 aggregate on commodity
             * category // if non is founs } else if (true) { //aggregate level
             * is 111 aggregate on commodity type // if non is founs } else if
             * (true) { //aggregate level is 11 aggregate on commodity variety
             * // if non is founs }
             *
             */

        }
        return cdFileDetails;
    }

    public CdFileDetails recCDFileMsgDetails(EcsResCdFileMsg CDFile, InternalProductcodes internalProductCode, Double Weight) {
        CdFileDetails cdFileDetails = new CdFileDetails();
        Double itemweight = 0.00;
        itemweight = itemweight + Weight;
        EcsResCdFileMsgJpaController recCdFileMsgController = new EcsResCdFileMsgJpaController(emf);

        if (internalProductCode.getAggregateIPCCodeLevel() == 0) {
            CdFileDetailsJpaController cdFileDetailsController = new CdFileDetailsJpaController(emf);
            if ((recCdFileMsgController.findEcsResCdFileMsg(CDFile.getRECCDFileID()) == null) || (recCdFileMsgController.findEcsResCdFileMsg(CDFile.getRECCDFileID()).getCdFileDetailsCollection().size() != 0)) {
                for (Iterator iterator1 = CDFile.getCdFileDetailsCollection().iterator(); iterator1.hasNext();) {
                    cdFileDetails = (CdFileDetails) iterator1.next();
                    itemweight = itemweight + cdFileDetails.getWeight();
                    cdFileDetails.setWeight(itemweight);
                    PricelistInternalProductcodeDocumentMapJpaController plipcdoc = new PricelistInternalProductcodeDocumentMapJpaController(emf);
                    PricelistInternalProductcodeDocumentMap defaultdocprice = plipcdoc.findPricelistInternalProductcodeDocumentMap(0);
                    for (Iterator<PricelistInternalProductcodeDocumentMap> iterator = findPricelistIPCDocMapEntitiesbyIPC(internalProductCode).iterator(); iterator.hasNext();) {
                        PricelistInternalProductcodeDocumentMap next = (PricelistInternalProductcodeDocumentMap) iterator.next();
                        PricelistJpaController pricelistController = new PricelistJpaController(emf);
                        Pricelist pricelist = pricelistController.findPricelist(next.getPRICELISTPriceIDRef().getPriceID());
                        if ((pricelist.getMaxWeight() >= cdFileDetails.getWeight()) && (cdFileDetails.getWeight() <= pricelist.getMinWeight())) {
                            Double temp_max_weight = 0.00;
                            Double temp_min_weight = 0.00;
                            System.err.println(" temp_max_weight " + temp_max_weight);
                            System.err.println(" temp_min_weight " + temp_min_weight);
                            int pricelistid1 = 0;
                            int pricelistid2 = 0;
                            //set default to 0
                            if (temp_max_weight <= pricelist.getMaxWeight()) {
                                temp_max_weight = pricelist.getMaxWeight();
                                pricelistid1 = pricelist.getPriceID();
                                defaultdocprice = next;

                                System.err.println(" temp_max_weight_1 " + temp_max_weight);
                            }
                            if (temp_min_weight >= pricelist.getMinWeight()) {
                                temp_min_weight = pricelist.getMinWeight();
                                pricelistid2 = pricelist.getPriceID();

                                System.err.println(" temp_min_weight-1 " + temp_min_weight);
                            }
                            if (pricelistid2 == pricelistid1) {
                                cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);
                            } else {
                                cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);
                            }

                        }
                        cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);

                    }

                }
                return cdFileDetails;

            } else {

                cdFileDetails.setIpcIdCode(internalProductCode.getInternalProductCode());
                cdFileDetails.setAggregateIPCCodeLevel(internalProductCode.getAggregateIPCCodeLevel());
                cdFileDetails.setECSRESCDFILEMSGRECCDFileID(CDFile);
                cdFileDetails.setWeight(itemweight);
                PricelistInternalProductcodeDocumentMapJpaController plipcdoc = new PricelistInternalProductcodeDocumentMapJpaController(emf);
                PricelistInternalProductcodeDocumentMap defaultdocprice = plipcdoc.findPricelistInternalProductcodeDocumentMap(0);
                for (Iterator<PricelistInternalProductcodeDocumentMap> iterator = findPricelistIPCDocMapEntitiesbyIPC(internalProductCode).iterator(); iterator.hasNext();) {
                    PricelistInternalProductcodeDocumentMap next = (PricelistInternalProductcodeDocumentMap) iterator.next();
                    PricelistJpaController pricelistController = new PricelistJpaController(emf);
                    Pricelist pricelist = pricelistController.findPricelist(next.getPRICELISTPriceIDRef().getPriceID());
                    if ((pricelist.getMaxWeight() >= cdFileDetails.getWeight()) && (cdFileDetails.getWeight() <= pricelist.getMinWeight())) {
                        Double temp_max_weight = 0.00;
                        Double temp_min_weight = 0.00;
                        int pricelistid1 = 0;
                        int pricelistid2 = 0;
                        if (temp_max_weight <= pricelist.getMaxWeight()) {
                            temp_max_weight = pricelist.getMaxWeight();
                            pricelistid1 = pricelist.getPriceID();
                            defaultdocprice = next;
                        }
                        if (temp_min_weight >= pricelist.getMinWeight()) {
                            temp_min_weight = pricelist.getMinWeight();
                            pricelistid2 = pricelist.getPriceID();
                        }
                        if (pricelistid2 == pricelistid1) {
                            cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);
                        } else {
                            cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);
                        }

                    }
                }
                cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);
                cdFileDetailsController.create(cdFileDetails);
                for (Iterator<CdFileDetails> iterator = CDFile.getCdFileDetailsCollection().iterator(); iterator.hasNext();) {
                    cdFileDetails = iterator.next();
                }
                return cdFileDetails;
            }
            /**
             * } else if (true) { //aggregate level is 11 aggregate on commodity
             * category // if non is founs } else if (true) { //aggregate level
             * is 111 aggregate on commodity type // if non is founs } else if
             * (true) { //aggregate level is 11 aggregate on commodity variety
             * // if non is founs }
             *
             */

        }
        return cdFileDetails;
    }

    public RecCdFileMsg findRecCdFileMsgbyFileName(String FileName) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            RecCdFileMsg recCdFileMsg = null;
            Query findRecCdFileMsgbyFileName = em.createNamedQuery("RecCdFileMsg.findByFileName");
            findRecCdFileMsgbyFileName.setParameter("fileName", FileName);
            List results = findRecCdFileMsgbyFileName.getResultList();
            if (!results.isEmpty()) {
                recCdFileMsg = (RecCdFileMsg) results.get(0);
            }
            return recCdFileMsg;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public EcsResCdFileMsg findECSResCdFileMsgbyFileName(String fileName) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            EcsResCdFileMsg resCdFileMsg = null;
            Query findRecCdFileMsgbyFileName = em.createNamedQuery("EcsResCdFileMsg.findByFileName");
            findRecCdFileMsgbyFileName.setParameter("fileName", fileName);
            List results = findRecCdFileMsgbyFileName.getResultList();
            if (!results.isEmpty()) {
                resCdFileMsg = (EcsResCdFileMsg) results.get(0);
            }
            return resCdFileMsg;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public RecErrorFileMsg findRecErrorMsgbyFilePath(String FilePath) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            RecErrorFileMsg recErrorMsg = null;
            Query findRecErrorMsgbyFilePath = em.createNamedQuery("RecErrorMsg.findByFilePath");
            findRecErrorMsgbyFilePath.setParameter("filePath", FilePath);
            List results = findRecErrorMsgbyFilePath.getResultList();
            if (!results.isEmpty()) {
                recErrorMsg = (RecErrorFileMsg) results.get(0);
            }
            return recErrorMsg;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editRecCdFileMsg(RecCdFileMsg recCdFileMsg) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(em.getEntityManagerFactory());
            recCDFileMsgContr.edit(recCdFileMsg);

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EcsKeswsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EcsKeswsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void logInfo(String receivedFileName, String info) {
        TransactionLogsJpaController transactionLogsContr = new TransactionLogsJpaController(emf);
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        TransactionLogs transactionLog = new TransactionLogs();
        LogTypes logtype = new LogTypes();
        Integer log_level = 1;
        logtype.setLogIdLevel(log_level);
        transactionLog.setLogDetails(info);
        transactionLog.setLogTypesLogIdLevel(logtype);
        transactionLog.setRecCdFileMsgRecCdFileId(findRecCdFileMsgbyFileName(receivedFileName));
        transactionLogsContr.create(transactionLog);

    }

    public void logInfo2(String responseFileName, String info) {
        TransactionLogsJpaController transactionLogsContr = new TransactionLogsJpaController(emf);
        EcsResCdFileMsgJpaController recCDFileMsgContr = new EcsResCdFileMsgJpaController(emf);
        TransactionLogs transactionLog = new TransactionLogs();
        LogTypes logtype = new LogTypes();
        Integer log_level = 1;
        logtype.setLogIdLevel(log_level);
        transactionLog.setLogDetails(info);
        transactionLog.setLogTypesLogIdLevel(logtype);
        transactionLog.setECSRESCDFILEMSGRECCDFileID(findECSResCdFileMsgbyFileName(responseFileName));
        transactionLogsContr.create(transactionLog);

    }

    public void logError(String receivedFileName, String error) {
        TransactionLogsJpaController transactionLogsContr = new TransactionLogsJpaController(emf);
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        TransactionLogs transactionLog = new TransactionLogs();
        LogTypes logtype = new LogTypes();
        Integer log_level = 3;
        logtype.setLogIdLevel(log_level);
        transactionLog.setLogDetails(error);
        transactionLog.setLogTypesLogIdLevel(logtype);
        transactionLog.setRecCdFileMsgRecCdFileId(findRecCdFileMsgbyFileName(receivedFileName));
        transactionLogsContr.create(transactionLog);

    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void CloseEmf() {
        emf.close();
    }

    public void logSevere(String receivedFileName, String errorDetails) {
        TransactionLogsJpaController transactionLogsContr = new TransactionLogsJpaController(emf);
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        TransactionLogs transactionLog = new TransactionLogs();
        LogTypes logtype = new LogTypes();
        Integer log_level = 1;
        logtype.setLogIdLevel(log_level);
        transactionLog.setLogDetails(errorDetails);
        transactionLog.setLogTypesLogIdLevel(logtype);
        if (findRecCdFileMsgbyFileName(receivedFileName) != null) {
            transactionLog.setRecCdFileMsgRecCdFileId(findRecCdFileMsgbyFileName(receivedFileName));
            transactionLogsContr.create(transactionLog);
        }

    }

    public void setRecCDFileMsgisValid(RecCdFileMsg recCdFileMsg, int status) {

        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        if (recCDFileMsgContr.findRecCdFileMsg(recCdFileMsg.getRECCDFileID()) != null) {
            recCdFileMsg = recCDFileMsgContr.findRecCdFileMsg(recCdFileMsg.getRECCDFileID());
            recCdFileMsg.setIsFileXMLValid(status);
            try {
                updateRecFileMsg(recCdFileMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //  return resCdFileMsg;
    }

    public void updateRecCDFileMsg(RecCdFileMsg recCdFileMsg) {
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        if (recCDFileMsgContr.findRecCdFileMsg(recCdFileMsg.getRECCDFileID()) != null) {
            try {
                updateRecFileMsg(recCdFileMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createCDFileDetails(RecCdFileMsg recCdFileMsg, CdFileDetails cdFileDetails) {
        CdFileDetailsJpaController cdFileContr = new CdFileDetailsJpaController(emf);
        if (cdFileContr.findCdFileDetails(cdFileDetails.getId()) == null) {
            cdFileDetails.setRECCDFILEMSGRECCDFILEIDRef(recCdFileMsg);
            cdFileContr.create(cdFileDetails);
        }

    }

    public ResCdFileMsg resCDFileMsg(RecCdFileMsg recCdFileMsg, int messageTypeId) {
        ResCdFileMsg resCdFileMsg = null;
        MessageTypesJpaController messagetypeContr = new MessageTypesJpaController(emf);
        MessageTypes messageType = messagetypeContr.findMessageTypes(messageTypeId);
        ResCdFileMsgJpaController resCDFileMsgContr = new ResCdFileMsgJpaController(emf);
        int isResMessageSent = 0;
        if (!(recCdFileMsg == null)) {
            isResMessageSent = recCdFileMsg.getResCdFileMsgCollection().size();
        }
        if (isResMessageSent > 0) {

            for (Iterator<ResCdFileMsg> iterator1 = recCdFileMsg.getResCdFileMsgCollection().iterator(); iterator1.hasNext();) {
                resCdFileMsg = iterator1.next();
                if (resCdFileMsg.getMessageTypesMessageTypeId().getMessageTypeId() == 1) {
                    return resCdFileMsg;
                }
                if (resCdFileMsg.getMessageTypesMessageTypeId().getMessageTypeId() == 2) {
                    return resCdFileMsg;
                }
            }

        } else {
            resCdFileMsg = new ResCdFileMsg();
            resCdFileMsg.setRecCdFileMsgRecCdFileId(recCdFileMsg);
            resCdFileMsg.setMessageTypesMessageTypeId(messageType);
///            resCDFileMsgContr.createResCdFileMsg(resCdFileMsg,resCdFileMsg);
            return recCdFileMsg.getResCdFileMsgCollection().iterator().next();
        }
        return resCdFileMsg;

    }

    public RecCdFileMsg getRecCDFileMsgbyFileName(String filename) {

        if (findRecCdFileMsgbyFileName(filename) == null) {
            return null;
        } else {
            return findRecCdFileMsgbyFileName(filename);
        }

    }

    public void updateRecFileMsg(RecCdFileMsg recCdFileMsg) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            recCdFileMsg = em.merge(recCdFileMsg);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recCdFileMsg.getRECCDFileID();
                if (recCDFileMsgContr.findRecCdFileMsg(id) == null) {
                    throw new NonexistentEntityException("The recCdFileMsg with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PricelistInternalProductcodeDocumentMap> findPricelistIPCDocMapEntitiesbyIPC(InternalProductcodes ipc) {
        List<PricelistInternalProductcodeDocumentMap> plipcdocmap = new ArrayList<PricelistInternalProductcodeDocumentMap>();
        PricelistInternalProductcodeDocumentMapJpaController pljpac = new PricelistInternalProductcodeDocumentMapJpaController(emf);
        EntityManager em = null;

        try {
            em = getEntityManager();
            Query PlQuery = em.createNamedQuery("PricelistInternalProductcodeDocumentMap.findByIPCIDRef");

            PlQuery.setParameter("iNTERNALPRODUCTCODESIPCIDRef", ipc);

            List results = PlQuery.getResultList();
            if (!results.isEmpty()) {
                // ignores multiple results
                for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                    //Object next = iterator.next();
                    PricelistInternalProductcodeDocumentMap plipcdocmp = (PricelistInternalProductcodeDocumentMap) iterator.next();
                    System.out.println("Price id" + plipcdocmp.getPRICELISTPriceIDRef().getChargeDescription());
                    if (iterator.hasNext()) {
                        plipcdocmap.add((PricelistInternalProductcodeDocumentMap) iterator.next());
                    }

                }
            }
            return plipcdocmap;
        } catch (Exception ex) {
            Logger.getLogger(EcsKeswsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
            return (List<PricelistInternalProductcodeDocumentMap>) pljpac.findPricelistInternalProductcodeDocumentMap(1);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public boolean internalProductCodesExist(String internalProductNo) {
        boolean ipcExist = false;
        InternalProductcodesJpaController ipcController = new InternalProductcodesJpaController(emf);
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query IPCExistQuery = em.createNamedQuery("InternalProductcodes.findByInternalProductCode");
            IPCExistQuery.setParameter("internalProductCode", internalProductNo);
            List results = IPCExistQuery.getResultList();
            if (!results.isEmpty()) {
                // ignores multiple results
                ipcExist = true;
            }
            return ipcExist;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public InternalProductcodes getInternalProductcodes(String internalProductNo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            InternalProductcodes internalProductcodes = null;
            Query IPCQuery = em.createNamedQuery("InternalProductcodes.findByInternalProductCode");
            IPCQuery.setParameter("internalProductCode", internalProductNo);
            List results = IPCQuery.getResultList();
            if (!results.isEmpty()) {
                // ignores multiple results
                internalProductcodes = (InternalProductcodes) results.get(0);

            }

            return internalProductcodes;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void createInternalProductcode(InternalProductcodes IPCObj) {
        InternalProductcodesJpaController ipjpc = new InternalProductcodesJpaController(emf);
        try {
            ipjpc.create(IPCObj);
        } catch (Exception ex) {
            Logger.getLogger(EcsKeswsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateCreateInternalProductcodePriceDocMappings(InternalProductcodes IPCObj) {
        //get default mappping on existing ipc if none create mappings and update ipc
        InternalProductcodesJpaController ipjpc = new InternalProductcodesJpaController(emf);
        if (findPricelistIPCDocMapEntitiesbyIPC(IPCObj).isEmpty()) {
            List<EcsDocumentTypes> docType = new ArrayList<EcsDocumentTypes>();
            EcsDocumentTypesJpaController ecsDocumentTypesJpaController = new EcsDocumentTypesJpaController(emf);
            docType = ecsDocumentTypesJpaController.findEcsDocumentTypesEntities();
            for (Iterator<EcsDocumentTypes> iterator = docType.iterator(); iterator.hasNext();) {
                EcsDocumentTypes ecsDocumentTypes = iterator.next();
                PricelistJpaController plc = new PricelistJpaController(emf);
                List<Pricelist> pl = plc.findPricelistEntities();
                for (Iterator<Pricelist> iterator1 = pl.iterator(); iterator1.hasNext();) {
                    Pricelist pricelist = iterator1.next();
                    if (pricelist.getChargeDescription().contains(ecsDocumentTypes.getDocumentName())) {
                        PricelistInternalProductcodeDocumentMap plIpcDocmapping = new PricelistInternalProductcodeDocumentMap();
                        PricelistInternalProductcodeDocumentMapJpaController plIpcDocMapper = new PricelistInternalProductcodeDocumentMapJpaController(emf);
                        plIpcDocmapping.setDOCUMENTTYPESid(ecsDocumentTypes);
                        plIpcDocmapping.setPRICELISTPriceIDRef(pricelist);
                        plIpcDocmapping.setINTERNALPRODUCTCODESIPCIDRef(IPCObj);
                        plIpcDocmapping.setDocumentIDRef("AUTO GENERATED");
                        //Phytosanitary Certificate (Commercial Agricultural commondities)
                        if (!IPCObj.getCommodityCategory().contains("Vegatable") && (!IPCObj.getCommodityCategory().contains("Flower"))
                                && (!IPCObj.getCommodityCategory().contains("Fruit")) && (pricelist.getPriceID() < 6) && (15 > pricelist.getPriceID())) {
                            plIpcDocMapper.create(plIpcDocmapping);
                        } //Phytosanitary Certificate for Fresh Produce excluding fruits and veg
                        else if ((IPCObj.getCommodityCategory().contains("Vegatable")
                                || IPCObj.getCommodityCategory().contains("Fruit")) && (pricelist.getPriceID() >= 10) && (pricelist.getPriceID() <= 15)) {
                            plIpcDocMapper.create(plIpcDocmapping);
                        } //Phytosanitary Certificate for Fresh Produce  fruits and veg
                        else if (IPCObj.getCommodityCategory().contains("Flower") && (pricelist.getPriceID() >= 6) && (pricelist.getPriceID() <= 9)) {
                            plIpcDocMapper.create(plIpcDocmapping);
                        } else {
                            plIpcDocMapper.create(plIpcDocmapping);
                        }
                    }
                }
            }
        }
        // select from price list based on the document 
        // deafult document type for ecs is phyto
        //if price mapping for coc exist add coc cost

        //check if coc is required using ipc
// use the IPC to compare commodity category type and form  to compare
        /**
         * PricelistInternalProductcodeDocumentMapJpaController
         * plIpcDocController = new
         * PricelistInternalProductcodeDocumentMapJpaController(emf);
         * Collection<PricelistInternalProductcodeDocumentMap>
         * PricelistInternalProductcodeDocumentMapList = new
         * ArrayList<PricelistInternalProductcodeDocumentMap>();
         * PricelistInternalProductcodeDocumentMapList.add(plIpcDocController.findPricelistInternalProductcodeDocumentMap(0));
         * IPCObj.setPricelistInternalProductcodeDocumentMapCollection(PricelistInternalProductcodeDocumentMapList);
         * try { ipjpc.edit(IPCObj); } catch (NonexistentEntityException ex) {
         * Logger.getLogger(EcsKeswsEntitiesControllerCaller.class.getName()).log(Level.SEVERE,
         * null, ex); } catch (Exception ex) {
         * Logger.getLogger(EcsKeswsEntitiesControllerCaller.class.getName()).log(Level.SEVERE,
         * null, ex); }*
         */
    }

    public void editECSResCDFileMsg(EcsResCdFileMsg ecsResCdFileMsg) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EcsResCdFileMsg persistentEcsResCdFileMsg = em.find(EcsResCdFileMsg.class, ecsResCdFileMsg.getRECCDFileID());
            MessageTypes messageTypesMessageTypeIdOld = persistentEcsResCdFileMsg.getMessageTypesMessageTypeId();
            MessageTypes messageTypesMessageTypeIdNew = ecsResCdFileMsg.getMessageTypesMessageTypeId();
            Collection<ResCdFileMsg> resCdFileMsgCollectionOld = persistentEcsResCdFileMsg.getResCdFileMsgCollection();
            Collection<ResCdFileMsg> resCdFileMsgCollectionNew = ecsResCdFileMsg.getResCdFileMsgCollection();
            Collection<TransactionLogs> transactionLogsCollectionOld = persistentEcsResCdFileMsg.getTransactionLogsCollection();
            Collection<TransactionLogs> transactionLogsCollectionNew = ecsResCdFileMsg.getTransactionLogsCollection();
            Collection<CdFileDetails> cdFileDetailsCollectionOld = persistentEcsResCdFileMsg.getCdFileDetailsCollection();
            Collection<CdFileDetails> cdFileDetailsCollectionNew = ecsResCdFileMsg.getCdFileDetailsCollection();
            List<String> illegalOrphanMessages = null;
            for (CdFileDetails cdFileDetailsCollectionOldCdFileDetails : cdFileDetailsCollectionOld) {
                if (!cdFileDetailsCollectionNew.contains(cdFileDetailsCollectionOldCdFileDetails)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CdFileDetails " + cdFileDetailsCollectionOldCdFileDetails + " since its ECSRESCDFILEMSGRECCDFileID field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (messageTypesMessageTypeIdNew != null) {
                messageTypesMessageTypeIdNew = em.getReference(messageTypesMessageTypeIdNew.getClass(), messageTypesMessageTypeIdNew.getMessageTypeId());
                ecsResCdFileMsg.setMessageTypesMessageTypeId(messageTypesMessageTypeIdNew);
            }
            Collection<ResCdFileMsg> attachedResCdFileMsgCollectionNew = new ArrayList<ResCdFileMsg>();
            for (ResCdFileMsg resCdFileMsgCollectionNewResCdFileMsgToAttach : resCdFileMsgCollectionNew) {
                resCdFileMsgCollectionNewResCdFileMsgToAttach = em.getReference(resCdFileMsgCollectionNewResCdFileMsgToAttach.getClass(), resCdFileMsgCollectionNewResCdFileMsgToAttach.getResCdFileId());
                attachedResCdFileMsgCollectionNew.add(resCdFileMsgCollectionNewResCdFileMsgToAttach);
            }
            resCdFileMsgCollectionNew = attachedResCdFileMsgCollectionNew;
            ecsResCdFileMsg.setResCdFileMsgCollection(resCdFileMsgCollectionNew);
            Collection<CdFileDetails> attachedCdFileDetailsCollectionNew = new ArrayList<CdFileDetails>();
            for (CdFileDetails cdFileDetailsCollectionNewCdFileDetailsToAttach : cdFileDetailsCollectionNew) {
                cdFileDetailsCollectionNewCdFileDetailsToAttach = em.getReference(cdFileDetailsCollectionNewCdFileDetailsToAttach.getClass(), cdFileDetailsCollectionNewCdFileDetailsToAttach.getId());
                attachedCdFileDetailsCollectionNew.add(cdFileDetailsCollectionNewCdFileDetailsToAttach);
            }
            cdFileDetailsCollectionNew = attachedCdFileDetailsCollectionNew;
            ecsResCdFileMsg.setCdFileDetailsCollection(cdFileDetailsCollectionNew);
            ecsResCdFileMsg = em.merge(ecsResCdFileMsg);
            if (messageTypesMessageTypeIdOld != null && !messageTypesMessageTypeIdOld.equals(messageTypesMessageTypeIdNew)) {
                messageTypesMessageTypeIdOld.getEcsResCdFileMsgCollection().remove(ecsResCdFileMsg);
                messageTypesMessageTypeIdOld = em.merge(messageTypesMessageTypeIdOld);
            }
            if (messageTypesMessageTypeIdNew != null && !messageTypesMessageTypeIdNew.equals(messageTypesMessageTypeIdOld)) {
                messageTypesMessageTypeIdNew.getEcsResCdFileMsgCollection().add(ecsResCdFileMsg);
                messageTypesMessageTypeIdNew = em.merge(messageTypesMessageTypeIdNew);
            }
            for (ResCdFileMsg resCdFileMsgCollectionOldResCdFileMsg : resCdFileMsgCollectionOld) {
                if (!resCdFileMsgCollectionNew.contains(resCdFileMsgCollectionOldResCdFileMsg)) {
                    resCdFileMsgCollectionOldResCdFileMsg.setECSRESCDFILEMSGRECCDFileID(null);
                    resCdFileMsgCollectionOldResCdFileMsg = em.merge(resCdFileMsgCollectionOldResCdFileMsg);
                }
            }
            for (ResCdFileMsg resCdFileMsgCollectionNewResCdFileMsg : resCdFileMsgCollectionNew) {
                if (!resCdFileMsgCollectionOld.contains(resCdFileMsgCollectionNewResCdFileMsg)) {
                    EcsResCdFileMsg oldECSRESCDFILEMSGRECCDFileIDOfResCdFileMsgCollectionNewResCdFileMsg = resCdFileMsgCollectionNewResCdFileMsg.getECSRESCDFILEMSGRECCDFileID();
                    resCdFileMsgCollectionNewResCdFileMsg.setECSRESCDFILEMSGRECCDFileID(ecsResCdFileMsg);
                    resCdFileMsgCollectionNewResCdFileMsg = em.merge(resCdFileMsgCollectionNewResCdFileMsg);
                    if (oldECSRESCDFILEMSGRECCDFileIDOfResCdFileMsgCollectionNewResCdFileMsg != null && !oldECSRESCDFILEMSGRECCDFileIDOfResCdFileMsgCollectionNewResCdFileMsg.equals(ecsResCdFileMsg)) {
                        oldECSRESCDFILEMSGRECCDFileIDOfResCdFileMsgCollectionNewResCdFileMsg.getResCdFileMsgCollection().remove(resCdFileMsgCollectionNewResCdFileMsg);
                        oldECSRESCDFILEMSGRECCDFileIDOfResCdFileMsgCollectionNewResCdFileMsg = em.merge(oldECSRESCDFILEMSGRECCDFileIDOfResCdFileMsgCollectionNewResCdFileMsg);
                    }
                }
            }
            for (CdFileDetails cdFileDetailsCollectionNewCdFileDetails : cdFileDetailsCollectionNew) {
                if (!cdFileDetailsCollectionOld.contains(cdFileDetailsCollectionNewCdFileDetails)) {
                    EcsResCdFileMsg oldECSRESCDFILEMSGRECCDFileIDOfCdFileDetailsCollectionNewCdFileDetails = cdFileDetailsCollectionNewCdFileDetails.getECSRESCDFILEMSGRECCDFileID();
                    cdFileDetailsCollectionNewCdFileDetails.setECSRESCDFILEMSGRECCDFileID(ecsResCdFileMsg);
                    cdFileDetailsCollectionNewCdFileDetails = em.merge(cdFileDetailsCollectionNewCdFileDetails);
                    if (oldECSRESCDFILEMSGRECCDFileIDOfCdFileDetailsCollectionNewCdFileDetails != null && !oldECSRESCDFILEMSGRECCDFileIDOfCdFileDetailsCollectionNewCdFileDetails.equals(ecsResCdFileMsg)) {
                        oldECSRESCDFILEMSGRECCDFileIDOfCdFileDetailsCollectionNewCdFileDetails.getCdFileDetailsCollection().remove(cdFileDetailsCollectionNewCdFileDetails);
                        oldECSRESCDFILEMSGRECCDFileIDOfCdFileDetailsCollectionNewCdFileDetails = em.merge(oldECSRESCDFILEMSGRECCDFileIDOfCdFileDetailsCollectionNewCdFileDetails);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ecsResCdFileMsg.getRECCDFileID();
                if (findEcsResCdFileMsg(id) == null) {
                    throw new NonexistentEntityException("The ecsResCdFileMsg with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public EcsResCdFileMsg findEcsResCdFileMsg(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EcsResCdFileMsg.class, id);
        } finally {
            em.close();
        }
    }

    public EcsResCdFileMsg OgUpdateResCd1Msg(EcsResCdFileMsg ecsResCdFileMsg) {

        EcsResCdFileMsgJpaController ecsResCDFileMsgContr = new EcsResCdFileMsgJpaController(emf);

        if (findECSResCdFileMsgbyFileName(ecsResCdFileMsg.getFileName()) != null) {
            try {
                editECSResCDFileMsg(ecsResCdFileMsg);
                ecsResCdFileMsg = findECSResCdFileMsgbyFileName(ecsResCdFileMsg.getFileName());
                return ecsResCdFileMsg;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public List<EcsConDoc> findEcsConDocByConsignmentId(Integer SubmittedConsignmentId) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            List<EcsConDoc> resEcsConDoc = new ArrayList<EcsConDoc>();
            Query findResEcsConDoc = em.createNamedQuery("EcsConDoc.findByConsignmentId");
            findResEcsConDoc.setParameter("consignmentId", SubmittedConsignmentId);

            List results = findResEcsConDoc.getResultList();
            if (!results.isEmpty()) {

                for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                    resEcsConDoc.add((EcsConDoc) iterator.next());

                }

            }
            return resEcsConDoc;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void createCdFileDetails(CdFileDetails cdFileDetails) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecCdFileMsg RECCDFILEMSGRECCDFILEIDRef = cdFileDetails.getRECCDFILEMSGRECCDFILEIDRef();
            if (RECCDFILEMSGRECCDFILEIDRef != null) {
                RECCDFILEMSGRECCDFILEIDRef = em.getReference(RECCDFILEMSGRECCDFILEIDRef.getClass(), RECCDFILEMSGRECCDFILEIDRef.getRECCDFileID());
                cdFileDetails.setRECCDFILEMSGRECCDFILEIDRef(RECCDFILEMSGRECCDFILEIDRef);
            }
            PricelistInternalProductcodeDocumentMap PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef = cdFileDetails.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef();
            if (PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef != null) {
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef = em.getReference(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef.getClass(), PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef.getPricelistIPCMAPID());
                cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef);
            }
            EcsResCdFileMsg ECSRESCDFILEMSGRECCDFileID = cdFileDetails.getECSRESCDFILEMSGRECCDFileID();
            if (ECSRESCDFILEMSGRECCDFileID != null) {
                ECSRESCDFILEMSGRECCDFileID = em.getReference(ECSRESCDFILEMSGRECCDFileID.getClass(), ECSRESCDFILEMSGRECCDFileID.getRECCDFileID());
                cdFileDetails.setECSRESCDFILEMSGRECCDFileID(ECSRESCDFILEMSGRECCDFileID);
            }
            em.persist(cdFileDetails);
            if (RECCDFILEMSGRECCDFILEIDRef != null) {
                RECCDFILEMSGRECCDFILEIDRef.getCdFileDetailsCollection().add(cdFileDetails);
                RECCDFILEMSGRECCDFILEIDRef = em.merge(RECCDFILEMSGRECCDFILEIDRef);
            }
            if (PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef != null) {
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef.getCdFileDetailsCollection().add(cdFileDetails);
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef = em.merge(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef);
            }

            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public EcsResCdFileMsg createEcsResCdFileMsg(String receivedFileName, int message_type, int ConsignmentId) {

        EcsResCdFileMsg recCdFileMsg = new EcsResCdFileMsg();

        MessageTypesJpaController messagetypeContr = new MessageTypesJpaController(emf);
        MessageTypes messageType = messagetypeContr.findMessageTypes(message_type);
        EcsResCdFileMsgJpaController recCDFileMsgContr = new EcsResCdFileMsgJpaController(emf);
        recCdFileMsg.setFileName(receivedFileName);
        recCdFileMsg.setECSCONSIGNEMENTIDRef(ConsignmentId);
        recCdFileMsg.setMessageTypesMessageTypeId(messageType);
        if (findECSResCdFileMsgbyFileName(receivedFileName) == null) {
            try {
                recCDFileMsgContr.create(recCdFileMsg);
                recCdFileMsg = findECSResCdFileMsgbyFileName(receivedFileName);
                return recCdFileMsg;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }

}
