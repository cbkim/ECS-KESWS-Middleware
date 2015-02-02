/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.entities.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.kephis.ecs_kesws.entities.MessageTypes;
import org.kephis.ecs_kesws.entities.RecCdFileMsg;
import org.kephis.ecs_kesws.entities.EcsResCdFileMsg;
import org.kephis.ecs_kesws.entities.RecErrorFileMsg;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.kephis.ecs_kesws.entities.PaymentInfoLog;
import org.kephis.ecs_kesws.entities.ResCdFileMsg;
import org.kephis.ecs_kesws.entities.TransactionLogs;
import org.kephis.ecs_kesws.entities.controllers.exceptions.IllegalOrphanException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author kim
 */
public class ResCdFileMsgJpaController implements Serializable {

    public ResCdFileMsgJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ResCdFileMsg resCdFileMsg) {
        if (resCdFileMsg.getRecErrorFileMsgCollection() == null) {
            resCdFileMsg.setRecErrorFileMsgCollection(new ArrayList<RecErrorFileMsg>());
        }
        if (resCdFileMsg.getPaymentInfoLogCollection() == null) {
            resCdFileMsg.setPaymentInfoLogCollection(new ArrayList<PaymentInfoLog>());
        }
        if (resCdFileMsg.getTransactionLogsCollection() == null) {
            resCdFileMsg.setTransactionLogsCollection(new ArrayList<TransactionLogs>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MessageTypes messageTypesMessageTypeId = resCdFileMsg.getMessageTypesMessageTypeId();
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId = em.getReference(messageTypesMessageTypeId.getClass(), messageTypesMessageTypeId.getMessageTypeId());
                resCdFileMsg.setMessageTypesMessageTypeId(messageTypesMessageTypeId);
            }
            RecCdFileMsg recCdFileMsgRecCdFileId = resCdFileMsg.getRecCdFileMsgRecCdFileId();
            if (recCdFileMsgRecCdFileId != null) {
                recCdFileMsgRecCdFileId = em.getReference(recCdFileMsgRecCdFileId.getClass(), recCdFileMsgRecCdFileId.getRECCDFileID());
                resCdFileMsg.setRecCdFileMsgRecCdFileId(recCdFileMsgRecCdFileId);
            }
            EcsResCdFileMsg ECSRESCDFILEMSGRECCDFileID = resCdFileMsg.getECSRESCDFILEMSGRECCDFileID();
            if (ECSRESCDFILEMSGRECCDFileID != null) {
                ECSRESCDFILEMSGRECCDFileID = em.getReference(ECSRESCDFILEMSGRECCDFileID.getClass(), ECSRESCDFILEMSGRECCDFileID.getRECCDFileID());
                resCdFileMsg.setECSRESCDFILEMSGRECCDFileID(ECSRESCDFILEMSGRECCDFileID);
            }
            Collection<RecErrorFileMsg> attachedRecErrorFileMsgCollection = new ArrayList<RecErrorFileMsg>();
            for (RecErrorFileMsg recErrorFileMsgCollectionRecErrorFileMsgToAttach : resCdFileMsg.getRecErrorFileMsgCollection()) {
                recErrorFileMsgCollectionRecErrorFileMsgToAttach = em.getReference(recErrorFileMsgCollectionRecErrorFileMsgToAttach.getClass(), recErrorFileMsgCollectionRecErrorFileMsgToAttach.getRecErrorMsgId());
                attachedRecErrorFileMsgCollection.add(recErrorFileMsgCollectionRecErrorFileMsgToAttach);
            }
            resCdFileMsg.setRecErrorFileMsgCollection(attachedRecErrorFileMsgCollection);
            Collection<PaymentInfoLog> attachedPaymentInfoLogCollection = new ArrayList<PaymentInfoLog>();
            for (PaymentInfoLog paymentInfoLogCollectionPaymentInfoLogToAttach : resCdFileMsg.getPaymentInfoLogCollection()) {
                paymentInfoLogCollectionPaymentInfoLogToAttach = em.getReference(paymentInfoLogCollectionPaymentInfoLogToAttach.getClass(), paymentInfoLogCollectionPaymentInfoLogToAttach.getPayementMsgId());
                attachedPaymentInfoLogCollection.add(paymentInfoLogCollectionPaymentInfoLogToAttach);
            }
            resCdFileMsg.setPaymentInfoLogCollection(attachedPaymentInfoLogCollection);
            Collection<TransactionLogs> attachedTransactionLogsCollection = new ArrayList<TransactionLogs>();
            for (TransactionLogs transactionLogsCollectionTransactionLogsToAttach : resCdFileMsg.getTransactionLogsCollection()) {
                transactionLogsCollectionTransactionLogsToAttach = em.getReference(transactionLogsCollectionTransactionLogsToAttach.getClass(), transactionLogsCollectionTransactionLogsToAttach.getLogID());
                attachedTransactionLogsCollection.add(transactionLogsCollectionTransactionLogsToAttach);
            }
            resCdFileMsg.setTransactionLogsCollection(attachedTransactionLogsCollection);
            em.persist(resCdFileMsg);
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId.getResCdFileMsgCollection().add(resCdFileMsg);
                messageTypesMessageTypeId = em.merge(messageTypesMessageTypeId);
            }
            if (recCdFileMsgRecCdFileId != null) {
                recCdFileMsgRecCdFileId.getResCdFileMsgCollection().add(resCdFileMsg);
                recCdFileMsgRecCdFileId = em.merge(recCdFileMsgRecCdFileId);
            }
            if (ECSRESCDFILEMSGRECCDFileID != null) {
                ECSRESCDFILEMSGRECCDFileID.getResCdFileMsgCollection().add(resCdFileMsg);
                ECSRESCDFILEMSGRECCDFileID = em.merge(ECSRESCDFILEMSGRECCDFileID);
            }
            for (RecErrorFileMsg recErrorFileMsgCollectionRecErrorFileMsg : resCdFileMsg.getRecErrorFileMsgCollection()) {
                ResCdFileMsg oldResCdFileMsgResCdFileIdOfRecErrorFileMsgCollectionRecErrorFileMsg = recErrorFileMsgCollectionRecErrorFileMsg.getResCdFileMsgResCdFileId();
                recErrorFileMsgCollectionRecErrorFileMsg.setResCdFileMsgResCdFileId(resCdFileMsg);
                recErrorFileMsgCollectionRecErrorFileMsg = em.merge(recErrorFileMsgCollectionRecErrorFileMsg);
                if (oldResCdFileMsgResCdFileIdOfRecErrorFileMsgCollectionRecErrorFileMsg != null) {
                    oldResCdFileMsgResCdFileIdOfRecErrorFileMsgCollectionRecErrorFileMsg.getRecErrorFileMsgCollection().remove(recErrorFileMsgCollectionRecErrorFileMsg);
                    oldResCdFileMsgResCdFileIdOfRecErrorFileMsgCollectionRecErrorFileMsg = em.merge(oldResCdFileMsgResCdFileIdOfRecErrorFileMsgCollectionRecErrorFileMsg);
                }
            }
            for (PaymentInfoLog paymentInfoLogCollectionPaymentInfoLog : resCdFileMsg.getPaymentInfoLogCollection()) {
                ResCdFileMsg oldResCdFileMsgResCdFileIdOfPaymentInfoLogCollectionPaymentInfoLog = paymentInfoLogCollectionPaymentInfoLog.getResCdFileMsgResCdFileId();
                paymentInfoLogCollectionPaymentInfoLog.setResCdFileMsgResCdFileId(resCdFileMsg);
                paymentInfoLogCollectionPaymentInfoLog = em.merge(paymentInfoLogCollectionPaymentInfoLog);
                if (oldResCdFileMsgResCdFileIdOfPaymentInfoLogCollectionPaymentInfoLog != null) {
                    oldResCdFileMsgResCdFileIdOfPaymentInfoLogCollectionPaymentInfoLog.getPaymentInfoLogCollection().remove(paymentInfoLogCollectionPaymentInfoLog);
                    oldResCdFileMsgResCdFileIdOfPaymentInfoLogCollectionPaymentInfoLog = em.merge(oldResCdFileMsgResCdFileIdOfPaymentInfoLogCollectionPaymentInfoLog);
                }
            }
            for (TransactionLogs transactionLogsCollectionTransactionLogs : resCdFileMsg.getTransactionLogsCollection()) {
                ResCdFileMsg oldResCdFileMsgResCdFileIdOfTransactionLogsCollectionTransactionLogs = transactionLogsCollectionTransactionLogs.getResCdFileMsgResCdFileId();
                transactionLogsCollectionTransactionLogs.setResCdFileMsgResCdFileId(resCdFileMsg);
                transactionLogsCollectionTransactionLogs = em.merge(transactionLogsCollectionTransactionLogs);
                if (oldResCdFileMsgResCdFileIdOfTransactionLogsCollectionTransactionLogs != null) {
                    oldResCdFileMsgResCdFileIdOfTransactionLogsCollectionTransactionLogs.getTransactionLogsCollection().remove(transactionLogsCollectionTransactionLogs);
                    oldResCdFileMsgResCdFileIdOfTransactionLogsCollectionTransactionLogs = em.merge(oldResCdFileMsgResCdFileIdOfTransactionLogsCollectionTransactionLogs);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ResCdFileMsg resCdFileMsg) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ResCdFileMsg persistentResCdFileMsg = em.find(ResCdFileMsg.class, resCdFileMsg.getResCdFileId());
            MessageTypes messageTypesMessageTypeIdOld = persistentResCdFileMsg.getMessageTypesMessageTypeId();
            MessageTypes messageTypesMessageTypeIdNew = resCdFileMsg.getMessageTypesMessageTypeId();
            RecCdFileMsg recCdFileMsgRecCdFileIdOld = persistentResCdFileMsg.getRecCdFileMsgRecCdFileId();
            RecCdFileMsg recCdFileMsgRecCdFileIdNew = resCdFileMsg.getRecCdFileMsgRecCdFileId();
            EcsResCdFileMsg ECSRESCDFILEMSGRECCDFileIDOld = persistentResCdFileMsg.getECSRESCDFILEMSGRECCDFileID();
            EcsResCdFileMsg ECSRESCDFILEMSGRECCDFileIDNew = resCdFileMsg.getECSRESCDFILEMSGRECCDFileID();
            Collection<RecErrorFileMsg> recErrorFileMsgCollectionOld = persistentResCdFileMsg.getRecErrorFileMsgCollection();
            Collection<RecErrorFileMsg> recErrorFileMsgCollectionNew = resCdFileMsg.getRecErrorFileMsgCollection();
            Collection<PaymentInfoLog> paymentInfoLogCollectionOld = persistentResCdFileMsg.getPaymentInfoLogCollection();
            Collection<PaymentInfoLog> paymentInfoLogCollectionNew = resCdFileMsg.getPaymentInfoLogCollection();
            Collection<TransactionLogs> transactionLogsCollectionOld = persistentResCdFileMsg.getTransactionLogsCollection();
            Collection<TransactionLogs> transactionLogsCollectionNew = resCdFileMsg.getTransactionLogsCollection();
            List<String> illegalOrphanMessages = null;
            for (RecErrorFileMsg recErrorFileMsgCollectionOldRecErrorFileMsg : recErrorFileMsgCollectionOld) {
                if (!recErrorFileMsgCollectionNew.contains(recErrorFileMsgCollectionOldRecErrorFileMsg)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecErrorFileMsg " + recErrorFileMsgCollectionOldRecErrorFileMsg + " since its resCdFileMsgResCdFileId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (messageTypesMessageTypeIdNew != null) {
                messageTypesMessageTypeIdNew = em.getReference(messageTypesMessageTypeIdNew.getClass(), messageTypesMessageTypeIdNew.getMessageTypeId());
                resCdFileMsg.setMessageTypesMessageTypeId(messageTypesMessageTypeIdNew);
            }
            if (recCdFileMsgRecCdFileIdNew != null) {
                recCdFileMsgRecCdFileIdNew = em.getReference(recCdFileMsgRecCdFileIdNew.getClass(), recCdFileMsgRecCdFileIdNew.getRECCDFileID());
                resCdFileMsg.setRecCdFileMsgRecCdFileId(recCdFileMsgRecCdFileIdNew);
            }
            if (ECSRESCDFILEMSGRECCDFileIDNew != null) {
                ECSRESCDFILEMSGRECCDFileIDNew = em.getReference(ECSRESCDFILEMSGRECCDFileIDNew.getClass(), ECSRESCDFILEMSGRECCDFileIDNew.getRECCDFileID());
                resCdFileMsg.setECSRESCDFILEMSGRECCDFileID(ECSRESCDFILEMSGRECCDFileIDNew);
            }
            Collection<RecErrorFileMsg> attachedRecErrorFileMsgCollectionNew = new ArrayList<RecErrorFileMsg>();
            for (RecErrorFileMsg recErrorFileMsgCollectionNewRecErrorFileMsgToAttach : recErrorFileMsgCollectionNew) {
                recErrorFileMsgCollectionNewRecErrorFileMsgToAttach = em.getReference(recErrorFileMsgCollectionNewRecErrorFileMsgToAttach.getClass(), recErrorFileMsgCollectionNewRecErrorFileMsgToAttach.getRecErrorMsgId());
                attachedRecErrorFileMsgCollectionNew.add(recErrorFileMsgCollectionNewRecErrorFileMsgToAttach);
            }
            recErrorFileMsgCollectionNew = attachedRecErrorFileMsgCollectionNew;
            resCdFileMsg.setRecErrorFileMsgCollection(recErrorFileMsgCollectionNew);
            Collection<PaymentInfoLog> attachedPaymentInfoLogCollectionNew = new ArrayList<PaymentInfoLog>();
            for (PaymentInfoLog paymentInfoLogCollectionNewPaymentInfoLogToAttach : paymentInfoLogCollectionNew) {
                paymentInfoLogCollectionNewPaymentInfoLogToAttach = em.getReference(paymentInfoLogCollectionNewPaymentInfoLogToAttach.getClass(), paymentInfoLogCollectionNewPaymentInfoLogToAttach.getPayementMsgId());
                attachedPaymentInfoLogCollectionNew.add(paymentInfoLogCollectionNewPaymentInfoLogToAttach);
            }
            paymentInfoLogCollectionNew = attachedPaymentInfoLogCollectionNew;
            resCdFileMsg.setPaymentInfoLogCollection(paymentInfoLogCollectionNew);
            Collection<TransactionLogs> attachedTransactionLogsCollectionNew = new ArrayList<TransactionLogs>();
            for (TransactionLogs transactionLogsCollectionNewTransactionLogsToAttach : transactionLogsCollectionNew) {
                transactionLogsCollectionNewTransactionLogsToAttach = em.getReference(transactionLogsCollectionNewTransactionLogsToAttach.getClass(), transactionLogsCollectionNewTransactionLogsToAttach.getLogID());
                attachedTransactionLogsCollectionNew.add(transactionLogsCollectionNewTransactionLogsToAttach);
            }
            transactionLogsCollectionNew = attachedTransactionLogsCollectionNew;
            resCdFileMsg.setTransactionLogsCollection(transactionLogsCollectionNew);
            resCdFileMsg = em.merge(resCdFileMsg);
            if (messageTypesMessageTypeIdOld != null && !messageTypesMessageTypeIdOld.equals(messageTypesMessageTypeIdNew)) {
                messageTypesMessageTypeIdOld.getResCdFileMsgCollection().remove(resCdFileMsg);
                messageTypesMessageTypeIdOld = em.merge(messageTypesMessageTypeIdOld);
            }
            if (messageTypesMessageTypeIdNew != null && !messageTypesMessageTypeIdNew.equals(messageTypesMessageTypeIdOld)) {
                messageTypesMessageTypeIdNew.getResCdFileMsgCollection().add(resCdFileMsg);
                messageTypesMessageTypeIdNew = em.merge(messageTypesMessageTypeIdNew);
            }
            if (recCdFileMsgRecCdFileIdOld != null && !recCdFileMsgRecCdFileIdOld.equals(recCdFileMsgRecCdFileIdNew)) {
                recCdFileMsgRecCdFileIdOld.getResCdFileMsgCollection().remove(resCdFileMsg);
                recCdFileMsgRecCdFileIdOld = em.merge(recCdFileMsgRecCdFileIdOld);
            }
            if (recCdFileMsgRecCdFileIdNew != null && !recCdFileMsgRecCdFileIdNew.equals(recCdFileMsgRecCdFileIdOld)) {
                recCdFileMsgRecCdFileIdNew.getResCdFileMsgCollection().add(resCdFileMsg);
                recCdFileMsgRecCdFileIdNew = em.merge(recCdFileMsgRecCdFileIdNew);
            }
            if (ECSRESCDFILEMSGRECCDFileIDOld != null && !ECSRESCDFILEMSGRECCDFileIDOld.equals(ECSRESCDFILEMSGRECCDFileIDNew)) {
                ECSRESCDFILEMSGRECCDFileIDOld.getResCdFileMsgCollection().remove(resCdFileMsg);
                ECSRESCDFILEMSGRECCDFileIDOld = em.merge(ECSRESCDFILEMSGRECCDFileIDOld);
            }
            if (ECSRESCDFILEMSGRECCDFileIDNew != null && !ECSRESCDFILEMSGRECCDFileIDNew.equals(ECSRESCDFILEMSGRECCDFileIDOld)) {
                ECSRESCDFILEMSGRECCDFileIDNew.getResCdFileMsgCollection().add(resCdFileMsg);
                ECSRESCDFILEMSGRECCDFileIDNew = em.merge(ECSRESCDFILEMSGRECCDFileIDNew);
            }
            for (RecErrorFileMsg recErrorFileMsgCollectionNewRecErrorFileMsg : recErrorFileMsgCollectionNew) {
                if (!recErrorFileMsgCollectionOld.contains(recErrorFileMsgCollectionNewRecErrorFileMsg)) {
                    ResCdFileMsg oldResCdFileMsgResCdFileIdOfRecErrorFileMsgCollectionNewRecErrorFileMsg = recErrorFileMsgCollectionNewRecErrorFileMsg.getResCdFileMsgResCdFileId();
                    recErrorFileMsgCollectionNewRecErrorFileMsg.setResCdFileMsgResCdFileId(resCdFileMsg);
                    recErrorFileMsgCollectionNewRecErrorFileMsg = em.merge(recErrorFileMsgCollectionNewRecErrorFileMsg);
                    if (oldResCdFileMsgResCdFileIdOfRecErrorFileMsgCollectionNewRecErrorFileMsg != null && !oldResCdFileMsgResCdFileIdOfRecErrorFileMsgCollectionNewRecErrorFileMsg.equals(resCdFileMsg)) {
                        oldResCdFileMsgResCdFileIdOfRecErrorFileMsgCollectionNewRecErrorFileMsg.getRecErrorFileMsgCollection().remove(recErrorFileMsgCollectionNewRecErrorFileMsg);
                        oldResCdFileMsgResCdFileIdOfRecErrorFileMsgCollectionNewRecErrorFileMsg = em.merge(oldResCdFileMsgResCdFileIdOfRecErrorFileMsgCollectionNewRecErrorFileMsg);
                    }
                }
            }
            for (PaymentInfoLog paymentInfoLogCollectionOldPaymentInfoLog : paymentInfoLogCollectionOld) {
                if (!paymentInfoLogCollectionNew.contains(paymentInfoLogCollectionOldPaymentInfoLog)) {
                    paymentInfoLogCollectionOldPaymentInfoLog.setResCdFileMsgResCdFileId(null);
                    paymentInfoLogCollectionOldPaymentInfoLog = em.merge(paymentInfoLogCollectionOldPaymentInfoLog);
                }
            }
            for (PaymentInfoLog paymentInfoLogCollectionNewPaymentInfoLog : paymentInfoLogCollectionNew) {
                if (!paymentInfoLogCollectionOld.contains(paymentInfoLogCollectionNewPaymentInfoLog)) {
                    ResCdFileMsg oldResCdFileMsgResCdFileIdOfPaymentInfoLogCollectionNewPaymentInfoLog = paymentInfoLogCollectionNewPaymentInfoLog.getResCdFileMsgResCdFileId();
                    paymentInfoLogCollectionNewPaymentInfoLog.setResCdFileMsgResCdFileId(resCdFileMsg);
                    paymentInfoLogCollectionNewPaymentInfoLog = em.merge(paymentInfoLogCollectionNewPaymentInfoLog);
                    if (oldResCdFileMsgResCdFileIdOfPaymentInfoLogCollectionNewPaymentInfoLog != null && !oldResCdFileMsgResCdFileIdOfPaymentInfoLogCollectionNewPaymentInfoLog.equals(resCdFileMsg)) {
                        oldResCdFileMsgResCdFileIdOfPaymentInfoLogCollectionNewPaymentInfoLog.getPaymentInfoLogCollection().remove(paymentInfoLogCollectionNewPaymentInfoLog);
                        oldResCdFileMsgResCdFileIdOfPaymentInfoLogCollectionNewPaymentInfoLog = em.merge(oldResCdFileMsgResCdFileIdOfPaymentInfoLogCollectionNewPaymentInfoLog);
                    }
                }
            }
            for (TransactionLogs transactionLogsCollectionOldTransactionLogs : transactionLogsCollectionOld) {
                if (!transactionLogsCollectionNew.contains(transactionLogsCollectionOldTransactionLogs)) {
                    transactionLogsCollectionOldTransactionLogs.setResCdFileMsgResCdFileId(null);
                    transactionLogsCollectionOldTransactionLogs = em.merge(transactionLogsCollectionOldTransactionLogs);
                }
            }
            for (TransactionLogs transactionLogsCollectionNewTransactionLogs : transactionLogsCollectionNew) {
                if (!transactionLogsCollectionOld.contains(transactionLogsCollectionNewTransactionLogs)) {
                    ResCdFileMsg oldResCdFileMsgResCdFileIdOfTransactionLogsCollectionNewTransactionLogs = transactionLogsCollectionNewTransactionLogs.getResCdFileMsgResCdFileId();
                    transactionLogsCollectionNewTransactionLogs.setResCdFileMsgResCdFileId(resCdFileMsg);
                    transactionLogsCollectionNewTransactionLogs = em.merge(transactionLogsCollectionNewTransactionLogs);
                    if (oldResCdFileMsgResCdFileIdOfTransactionLogsCollectionNewTransactionLogs != null && !oldResCdFileMsgResCdFileIdOfTransactionLogsCollectionNewTransactionLogs.equals(resCdFileMsg)) {
                        oldResCdFileMsgResCdFileIdOfTransactionLogsCollectionNewTransactionLogs.getTransactionLogsCollection().remove(transactionLogsCollectionNewTransactionLogs);
                        oldResCdFileMsgResCdFileIdOfTransactionLogsCollectionNewTransactionLogs = em.merge(oldResCdFileMsgResCdFileIdOfTransactionLogsCollectionNewTransactionLogs);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = resCdFileMsg.getResCdFileId();
                if (findResCdFileMsg(id) == null) {
                    throw new NonexistentEntityException("The resCdFileMsg with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ResCdFileMsg resCdFileMsg;
            try {
                resCdFileMsg = em.getReference(ResCdFileMsg.class, id);
                resCdFileMsg.getResCdFileId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The resCdFileMsg with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<RecErrorFileMsg> recErrorFileMsgCollectionOrphanCheck = resCdFileMsg.getRecErrorFileMsgCollection();
            for (RecErrorFileMsg recErrorFileMsgCollectionOrphanCheckRecErrorFileMsg : recErrorFileMsgCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ResCdFileMsg (" + resCdFileMsg + ") cannot be destroyed since the RecErrorFileMsg " + recErrorFileMsgCollectionOrphanCheckRecErrorFileMsg + " in its recErrorFileMsgCollection field has a non-nullable resCdFileMsgResCdFileId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            MessageTypes messageTypesMessageTypeId = resCdFileMsg.getMessageTypesMessageTypeId();
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId.getResCdFileMsgCollection().remove(resCdFileMsg);
                messageTypesMessageTypeId = em.merge(messageTypesMessageTypeId);
            }
            RecCdFileMsg recCdFileMsgRecCdFileId = resCdFileMsg.getRecCdFileMsgRecCdFileId();
            if (recCdFileMsgRecCdFileId != null) {
                recCdFileMsgRecCdFileId.getResCdFileMsgCollection().remove(resCdFileMsg);
                recCdFileMsgRecCdFileId = em.merge(recCdFileMsgRecCdFileId);
            }
            EcsResCdFileMsg ECSRESCDFILEMSGRECCDFileID = resCdFileMsg.getECSRESCDFILEMSGRECCDFileID();
            if (ECSRESCDFILEMSGRECCDFileID != null) {
                ECSRESCDFILEMSGRECCDFileID.getResCdFileMsgCollection().remove(resCdFileMsg);
                ECSRESCDFILEMSGRECCDFileID = em.merge(ECSRESCDFILEMSGRECCDFileID);
            }
            Collection<PaymentInfoLog> paymentInfoLogCollection = resCdFileMsg.getPaymentInfoLogCollection();
            for (PaymentInfoLog paymentInfoLogCollectionPaymentInfoLog : paymentInfoLogCollection) {
                paymentInfoLogCollectionPaymentInfoLog.setResCdFileMsgResCdFileId(null);
                paymentInfoLogCollectionPaymentInfoLog = em.merge(paymentInfoLogCollectionPaymentInfoLog);
            }
            Collection<TransactionLogs> transactionLogsCollection = resCdFileMsg.getTransactionLogsCollection();
            for (TransactionLogs transactionLogsCollectionTransactionLogs : transactionLogsCollection) {
                transactionLogsCollectionTransactionLogs.setResCdFileMsgResCdFileId(null);
                transactionLogsCollectionTransactionLogs = em.merge(transactionLogsCollectionTransactionLogs);
            }
            em.remove(resCdFileMsg);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ResCdFileMsg> findResCdFileMsgEntities() {
        return findResCdFileMsgEntities(true, -1, -1);
    }

    public List<ResCdFileMsg> findResCdFileMsgEntities(int maxResults, int firstResult) {
        return findResCdFileMsgEntities(false, maxResults, firstResult);
    }

    private List<ResCdFileMsg> findResCdFileMsgEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ResCdFileMsg.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ResCdFileMsg findResCdFileMsg(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ResCdFileMsg.class, id);
        } finally {
            em.close();
        }
    }

    public int getResCdFileMsgCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ResCdFileMsg> rt = cq.from(ResCdFileMsg.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
