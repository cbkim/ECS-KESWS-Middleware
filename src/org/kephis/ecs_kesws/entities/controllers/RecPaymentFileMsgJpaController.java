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
import org.kephis.ecs_kesws.entities.PaymentInfoLog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.kephis.ecs_kesws.entities.RecPaymentFileMsg;
import org.kephis.ecs_kesws.entities.TransactionLogs;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author kim
 */
public class RecPaymentFileMsgJpaController implements Serializable {

    public RecPaymentFileMsgJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RecPaymentFileMsg recPaymentFileMsg) {
        if (recPaymentFileMsg.getPaymentInfoLogCollection() == null) {
            recPaymentFileMsg.setPaymentInfoLogCollection(new ArrayList<PaymentInfoLog>());
        }
        if (recPaymentFileMsg.getTransactionLogsCollection() == null) {
            recPaymentFileMsg.setTransactionLogsCollection(new ArrayList<TransactionLogs>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<PaymentInfoLog> attachedPaymentInfoLogCollection = new ArrayList<PaymentInfoLog>();
            for (PaymentInfoLog paymentInfoLogCollectionPaymentInfoLogToAttach : recPaymentFileMsg.getPaymentInfoLogCollection()) {
                paymentInfoLogCollectionPaymentInfoLogToAttach = em.getReference(paymentInfoLogCollectionPaymentInfoLogToAttach.getClass(), paymentInfoLogCollectionPaymentInfoLogToAttach.getPayementMsgId());
                attachedPaymentInfoLogCollection.add(paymentInfoLogCollectionPaymentInfoLogToAttach);
            }
            recPaymentFileMsg.setPaymentInfoLogCollection(attachedPaymentInfoLogCollection);
            Collection<TransactionLogs> attachedTransactionLogsCollection = new ArrayList<TransactionLogs>();
            for (TransactionLogs transactionLogsCollectionTransactionLogsToAttach : recPaymentFileMsg.getTransactionLogsCollection()) {
                transactionLogsCollectionTransactionLogsToAttach = em.getReference(transactionLogsCollectionTransactionLogsToAttach.getClass(), transactionLogsCollectionTransactionLogsToAttach.getLogID());
                attachedTransactionLogsCollection.add(transactionLogsCollectionTransactionLogsToAttach);
            }
            recPaymentFileMsg.setTransactionLogsCollection(attachedTransactionLogsCollection);
            em.persist(recPaymentFileMsg);
            for (PaymentInfoLog paymentInfoLogCollectionPaymentInfoLog : recPaymentFileMsg.getPaymentInfoLogCollection()) {
                RecPaymentFileMsg oldRecPaymentFileIdOfPaymentInfoLogCollectionPaymentInfoLog = paymentInfoLogCollectionPaymentInfoLog.getRecPaymentFileId();
                paymentInfoLogCollectionPaymentInfoLog.setRecPaymentFileId(recPaymentFileMsg);
                paymentInfoLogCollectionPaymentInfoLog = em.merge(paymentInfoLogCollectionPaymentInfoLog);
                if (oldRecPaymentFileIdOfPaymentInfoLogCollectionPaymentInfoLog != null) {
                    oldRecPaymentFileIdOfPaymentInfoLogCollectionPaymentInfoLog.getPaymentInfoLogCollection().remove(paymentInfoLogCollectionPaymentInfoLog);
                    oldRecPaymentFileIdOfPaymentInfoLogCollectionPaymentInfoLog = em.merge(oldRecPaymentFileIdOfPaymentInfoLogCollectionPaymentInfoLog);
                }
            }
            for (TransactionLogs transactionLogsCollectionTransactionLogs : recPaymentFileMsg.getTransactionLogsCollection()) {
                RecPaymentFileMsg oldRecPaymentMsgReceivedPaymentMsgIdOfTransactionLogsCollectionTransactionLogs = transactionLogsCollectionTransactionLogs.getRecPaymentMsgReceivedPaymentMsgId();
                transactionLogsCollectionTransactionLogs.setRecPaymentMsgReceivedPaymentMsgId(recPaymentFileMsg);
                transactionLogsCollectionTransactionLogs = em.merge(transactionLogsCollectionTransactionLogs);
                if (oldRecPaymentMsgReceivedPaymentMsgIdOfTransactionLogsCollectionTransactionLogs != null) {
                    oldRecPaymentMsgReceivedPaymentMsgIdOfTransactionLogsCollectionTransactionLogs.getTransactionLogsCollection().remove(transactionLogsCollectionTransactionLogs);
                    oldRecPaymentMsgReceivedPaymentMsgIdOfTransactionLogsCollectionTransactionLogs = em.merge(oldRecPaymentMsgReceivedPaymentMsgIdOfTransactionLogsCollectionTransactionLogs);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RecPaymentFileMsg recPaymentFileMsg) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecPaymentFileMsg persistentRecPaymentFileMsg = em.find(RecPaymentFileMsg.class, recPaymentFileMsg.getReceivedPaymentMsgId());
            Collection<PaymentInfoLog> paymentInfoLogCollectionOld = persistentRecPaymentFileMsg.getPaymentInfoLogCollection();
            Collection<PaymentInfoLog> paymentInfoLogCollectionNew = recPaymentFileMsg.getPaymentInfoLogCollection();
            Collection<TransactionLogs> transactionLogsCollectionOld = persistentRecPaymentFileMsg.getTransactionLogsCollection();
            Collection<TransactionLogs> transactionLogsCollectionNew = recPaymentFileMsg.getTransactionLogsCollection();
            Collection<PaymentInfoLog> attachedPaymentInfoLogCollectionNew = new ArrayList<PaymentInfoLog>();
            for (PaymentInfoLog paymentInfoLogCollectionNewPaymentInfoLogToAttach : paymentInfoLogCollectionNew) {
                paymentInfoLogCollectionNewPaymentInfoLogToAttach = em.getReference(paymentInfoLogCollectionNewPaymentInfoLogToAttach.getClass(), paymentInfoLogCollectionNewPaymentInfoLogToAttach.getPayementMsgId());
                attachedPaymentInfoLogCollectionNew.add(paymentInfoLogCollectionNewPaymentInfoLogToAttach);
            }
            paymentInfoLogCollectionNew = attachedPaymentInfoLogCollectionNew;
            recPaymentFileMsg.setPaymentInfoLogCollection(paymentInfoLogCollectionNew);
            Collection<TransactionLogs> attachedTransactionLogsCollectionNew = new ArrayList<TransactionLogs>();
            for (TransactionLogs transactionLogsCollectionNewTransactionLogsToAttach : transactionLogsCollectionNew) {
                transactionLogsCollectionNewTransactionLogsToAttach = em.getReference(transactionLogsCollectionNewTransactionLogsToAttach.getClass(), transactionLogsCollectionNewTransactionLogsToAttach.getLogID());
                attachedTransactionLogsCollectionNew.add(transactionLogsCollectionNewTransactionLogsToAttach);
            }
            transactionLogsCollectionNew = attachedTransactionLogsCollectionNew;
            recPaymentFileMsg.setTransactionLogsCollection(transactionLogsCollectionNew);
            recPaymentFileMsg = em.merge(recPaymentFileMsg);
            for (PaymentInfoLog paymentInfoLogCollectionOldPaymentInfoLog : paymentInfoLogCollectionOld) {
                if (!paymentInfoLogCollectionNew.contains(paymentInfoLogCollectionOldPaymentInfoLog)) {
                    paymentInfoLogCollectionOldPaymentInfoLog.setRecPaymentFileId(null);
                    paymentInfoLogCollectionOldPaymentInfoLog = em.merge(paymentInfoLogCollectionOldPaymentInfoLog);
                }
            }
            for (PaymentInfoLog paymentInfoLogCollectionNewPaymentInfoLog : paymentInfoLogCollectionNew) {
                if (!paymentInfoLogCollectionOld.contains(paymentInfoLogCollectionNewPaymentInfoLog)) {
                    RecPaymentFileMsg oldRecPaymentFileIdOfPaymentInfoLogCollectionNewPaymentInfoLog = paymentInfoLogCollectionNewPaymentInfoLog.getRecPaymentFileId();
                    paymentInfoLogCollectionNewPaymentInfoLog.setRecPaymentFileId(recPaymentFileMsg);
                    paymentInfoLogCollectionNewPaymentInfoLog = em.merge(paymentInfoLogCollectionNewPaymentInfoLog);
                    if (oldRecPaymentFileIdOfPaymentInfoLogCollectionNewPaymentInfoLog != null && !oldRecPaymentFileIdOfPaymentInfoLogCollectionNewPaymentInfoLog.equals(recPaymentFileMsg)) {
                        oldRecPaymentFileIdOfPaymentInfoLogCollectionNewPaymentInfoLog.getPaymentInfoLogCollection().remove(paymentInfoLogCollectionNewPaymentInfoLog);
                        oldRecPaymentFileIdOfPaymentInfoLogCollectionNewPaymentInfoLog = em.merge(oldRecPaymentFileIdOfPaymentInfoLogCollectionNewPaymentInfoLog);
                    }
                }
            }
            for (TransactionLogs transactionLogsCollectionOldTransactionLogs : transactionLogsCollectionOld) {
                if (!transactionLogsCollectionNew.contains(transactionLogsCollectionOldTransactionLogs)) {
                    transactionLogsCollectionOldTransactionLogs.setRecPaymentMsgReceivedPaymentMsgId(null);
                    transactionLogsCollectionOldTransactionLogs = em.merge(transactionLogsCollectionOldTransactionLogs);
                }
            }
            for (TransactionLogs transactionLogsCollectionNewTransactionLogs : transactionLogsCollectionNew) {
                if (!transactionLogsCollectionOld.contains(transactionLogsCollectionNewTransactionLogs)) {
                    RecPaymentFileMsg oldRecPaymentMsgReceivedPaymentMsgIdOfTransactionLogsCollectionNewTransactionLogs = transactionLogsCollectionNewTransactionLogs.getRecPaymentMsgReceivedPaymentMsgId();
                    transactionLogsCollectionNewTransactionLogs.setRecPaymentMsgReceivedPaymentMsgId(recPaymentFileMsg);
                    transactionLogsCollectionNewTransactionLogs = em.merge(transactionLogsCollectionNewTransactionLogs);
                    if (oldRecPaymentMsgReceivedPaymentMsgIdOfTransactionLogsCollectionNewTransactionLogs != null && !oldRecPaymentMsgReceivedPaymentMsgIdOfTransactionLogsCollectionNewTransactionLogs.equals(recPaymentFileMsg)) {
                        oldRecPaymentMsgReceivedPaymentMsgIdOfTransactionLogsCollectionNewTransactionLogs.getTransactionLogsCollection().remove(transactionLogsCollectionNewTransactionLogs);
                        oldRecPaymentMsgReceivedPaymentMsgIdOfTransactionLogsCollectionNewTransactionLogs = em.merge(oldRecPaymentMsgReceivedPaymentMsgIdOfTransactionLogsCollectionNewTransactionLogs);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recPaymentFileMsg.getReceivedPaymentMsgId();
                if (findRecPaymentFileMsg(id) == null) {
                    throw new NonexistentEntityException("The recPaymentFileMsg with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecPaymentFileMsg recPaymentFileMsg;
            try {
                recPaymentFileMsg = em.getReference(RecPaymentFileMsg.class, id);
                recPaymentFileMsg.getReceivedPaymentMsgId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recPaymentFileMsg with id " + id + " no longer exists.", enfe);
            }
            Collection<PaymentInfoLog> paymentInfoLogCollection = recPaymentFileMsg.getPaymentInfoLogCollection();
            for (PaymentInfoLog paymentInfoLogCollectionPaymentInfoLog : paymentInfoLogCollection) {
                paymentInfoLogCollectionPaymentInfoLog.setRecPaymentFileId(null);
                paymentInfoLogCollectionPaymentInfoLog = em.merge(paymentInfoLogCollectionPaymentInfoLog);
            }
            Collection<TransactionLogs> transactionLogsCollection = recPaymentFileMsg.getTransactionLogsCollection();
            for (TransactionLogs transactionLogsCollectionTransactionLogs : transactionLogsCollection) {
                transactionLogsCollectionTransactionLogs.setRecPaymentMsgReceivedPaymentMsgId(null);
                transactionLogsCollectionTransactionLogs = em.merge(transactionLogsCollectionTransactionLogs);
            }
            em.remove(recPaymentFileMsg);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RecPaymentFileMsg> findRecPaymentFileMsgEntities() {
        return findRecPaymentFileMsgEntities(true, -1, -1);
    }

    public List<RecPaymentFileMsg> findRecPaymentFileMsgEntities(int maxResults, int firstResult) {
        return findRecPaymentFileMsgEntities(false, maxResults, firstResult);
    }

    private List<RecPaymentFileMsg> findRecPaymentFileMsgEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RecPaymentFileMsg.class));
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

    public RecPaymentFileMsg findRecPaymentFileMsg(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RecPaymentFileMsg.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecPaymentFileMsgCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RecPaymentFileMsg> rt = cq.from(RecPaymentFileMsg.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
