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
import org.kephis.ecs_kesws.entities.ResCdFileMsg;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.kephis.ecs_kesws.entities.TransactionLogs;
import org.kephis.ecs_kesws.entities.CdFileDetails;
import org.kephis.ecs_kesws.entities.RecCdFileMsg;
import org.kephis.ecs_kesws.entities.controllers.exceptions.IllegalOrphanException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author kim
 */
public class RecCdFileMsgJpaController implements Serializable {

    public RecCdFileMsgJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RecCdFileMsg recCdFileMsg) {
        if (recCdFileMsg.getResCdFileMsgCollection() == null) {
            recCdFileMsg.setResCdFileMsgCollection(new ArrayList<ResCdFileMsg>());
        }
        if (recCdFileMsg.getTransactionLogsCollection() == null) {
            recCdFileMsg.setTransactionLogsCollection(new ArrayList<TransactionLogs>());
        }
        if (recCdFileMsg.getCdFileDetailsCollection() == null) {
            recCdFileMsg.setCdFileDetailsCollection(new ArrayList<CdFileDetails>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MessageTypes messageTypesMessageTypeId = recCdFileMsg.getMessageTypesMessageTypeId();
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId = em.getReference(messageTypesMessageTypeId.getClass(), messageTypesMessageTypeId.getMessageTypeId());
                recCdFileMsg.setMessageTypesMessageTypeId(messageTypesMessageTypeId);
            }
            Collection<ResCdFileMsg> attachedResCdFileMsgCollection = new ArrayList<ResCdFileMsg>();
            for (ResCdFileMsg resCdFileMsgCollectionResCdFileMsgToAttach : recCdFileMsg.getResCdFileMsgCollection()) {
                resCdFileMsgCollectionResCdFileMsgToAttach = em.getReference(resCdFileMsgCollectionResCdFileMsgToAttach.getClass(), resCdFileMsgCollectionResCdFileMsgToAttach.getResCdFileId());
                attachedResCdFileMsgCollection.add(resCdFileMsgCollectionResCdFileMsgToAttach);
            }
            recCdFileMsg.setResCdFileMsgCollection(attachedResCdFileMsgCollection);
            Collection<TransactionLogs> attachedTransactionLogsCollection = new ArrayList<TransactionLogs>();
            for (TransactionLogs transactionLogsCollectionTransactionLogsToAttach : recCdFileMsg.getTransactionLogsCollection()) {
                transactionLogsCollectionTransactionLogsToAttach = em.getReference(transactionLogsCollectionTransactionLogsToAttach.getClass(), transactionLogsCollectionTransactionLogsToAttach.getLogID());
                attachedTransactionLogsCollection.add(transactionLogsCollectionTransactionLogsToAttach);
            }
            recCdFileMsg.setTransactionLogsCollection(attachedTransactionLogsCollection);
            Collection<CdFileDetails> attachedCdFileDetailsCollection = new ArrayList<CdFileDetails>();
            for (CdFileDetails cdFileDetailsCollectionCdFileDetailsToAttach : recCdFileMsg.getCdFileDetailsCollection()) {
                cdFileDetailsCollectionCdFileDetailsToAttach = em.getReference(cdFileDetailsCollectionCdFileDetailsToAttach.getClass(), cdFileDetailsCollectionCdFileDetailsToAttach.getId());
                attachedCdFileDetailsCollection.add(cdFileDetailsCollectionCdFileDetailsToAttach);
            }
            recCdFileMsg.setCdFileDetailsCollection(attachedCdFileDetailsCollection);
            em.persist(recCdFileMsg);
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId.getRecCdFileMsgCollection().add(recCdFileMsg);
                messageTypesMessageTypeId = em.merge(messageTypesMessageTypeId);
            }
            for (ResCdFileMsg resCdFileMsgCollectionResCdFileMsg : recCdFileMsg.getResCdFileMsgCollection()) {
                RecCdFileMsg oldRecCdFileMsgRecCdFileIdOfResCdFileMsgCollectionResCdFileMsg = resCdFileMsgCollectionResCdFileMsg.getRecCdFileMsgRecCdFileId();
                resCdFileMsgCollectionResCdFileMsg.setRecCdFileMsgRecCdFileId(recCdFileMsg);
                resCdFileMsgCollectionResCdFileMsg = em.merge(resCdFileMsgCollectionResCdFileMsg);
                if (oldRecCdFileMsgRecCdFileIdOfResCdFileMsgCollectionResCdFileMsg != null) {
                    oldRecCdFileMsgRecCdFileIdOfResCdFileMsgCollectionResCdFileMsg.getResCdFileMsgCollection().remove(resCdFileMsgCollectionResCdFileMsg);
                    oldRecCdFileMsgRecCdFileIdOfResCdFileMsgCollectionResCdFileMsg = em.merge(oldRecCdFileMsgRecCdFileIdOfResCdFileMsgCollectionResCdFileMsg);
                }
            }
            for (TransactionLogs transactionLogsCollectionTransactionLogs : recCdFileMsg.getTransactionLogsCollection()) {
                RecCdFileMsg oldRecCdFileMsgRecCdFileIdOfTransactionLogsCollectionTransactionLogs = transactionLogsCollectionTransactionLogs.getRecCdFileMsgRecCdFileId();
                transactionLogsCollectionTransactionLogs.setRecCdFileMsgRecCdFileId(recCdFileMsg);
                transactionLogsCollectionTransactionLogs = em.merge(transactionLogsCollectionTransactionLogs);
                if (oldRecCdFileMsgRecCdFileIdOfTransactionLogsCollectionTransactionLogs != null) {
                    oldRecCdFileMsgRecCdFileIdOfTransactionLogsCollectionTransactionLogs.getTransactionLogsCollection().remove(transactionLogsCollectionTransactionLogs);
                    oldRecCdFileMsgRecCdFileIdOfTransactionLogsCollectionTransactionLogs = em.merge(oldRecCdFileMsgRecCdFileIdOfTransactionLogsCollectionTransactionLogs);
                }
            }
            for (CdFileDetails cdFileDetailsCollectionCdFileDetails : recCdFileMsg.getCdFileDetailsCollection()) {
                RecCdFileMsg oldRECCDFILEMSGRECCDFILEIDRefOfCdFileDetailsCollectionCdFileDetails = cdFileDetailsCollectionCdFileDetails.getRECCDFILEMSGRECCDFILEIDRef();
                cdFileDetailsCollectionCdFileDetails.setRECCDFILEMSGRECCDFILEIDRef(recCdFileMsg);
                cdFileDetailsCollectionCdFileDetails = em.merge(cdFileDetailsCollectionCdFileDetails);
                if (oldRECCDFILEMSGRECCDFILEIDRefOfCdFileDetailsCollectionCdFileDetails != null) {
                    oldRECCDFILEMSGRECCDFILEIDRefOfCdFileDetailsCollectionCdFileDetails.getCdFileDetailsCollection().remove(cdFileDetailsCollectionCdFileDetails);
                    oldRECCDFILEMSGRECCDFILEIDRefOfCdFileDetailsCollectionCdFileDetails = em.merge(oldRECCDFILEMSGRECCDFILEIDRefOfCdFileDetailsCollectionCdFileDetails);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RecCdFileMsg recCdFileMsg) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecCdFileMsg persistentRecCdFileMsg = em.find(RecCdFileMsg.class, recCdFileMsg.getRECCDFileID());
            MessageTypes messageTypesMessageTypeIdOld = persistentRecCdFileMsg.getMessageTypesMessageTypeId();
            MessageTypes messageTypesMessageTypeIdNew = recCdFileMsg.getMessageTypesMessageTypeId();
            Collection<ResCdFileMsg> resCdFileMsgCollectionOld = persistentRecCdFileMsg.getResCdFileMsgCollection();
            Collection<ResCdFileMsg> resCdFileMsgCollectionNew = recCdFileMsg.getResCdFileMsgCollection();
            Collection<TransactionLogs> transactionLogsCollectionOld = persistentRecCdFileMsg.getTransactionLogsCollection();
            Collection<TransactionLogs> transactionLogsCollectionNew = recCdFileMsg.getTransactionLogsCollection();
            Collection<CdFileDetails> cdFileDetailsCollectionOld = persistentRecCdFileMsg.getCdFileDetailsCollection();
            Collection<CdFileDetails> cdFileDetailsCollectionNew = recCdFileMsg.getCdFileDetailsCollection();
            List<String> illegalOrphanMessages = null;
            for (CdFileDetails cdFileDetailsCollectionOldCdFileDetails : cdFileDetailsCollectionOld) {
                if (!cdFileDetailsCollectionNew.contains(cdFileDetailsCollectionOldCdFileDetails)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CdFileDetails " + cdFileDetailsCollectionOldCdFileDetails + " since its RECCDFILEMSGRECCDFILEIDRef field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (messageTypesMessageTypeIdNew != null) {
                messageTypesMessageTypeIdNew = em.getReference(messageTypesMessageTypeIdNew.getClass(), messageTypesMessageTypeIdNew.getMessageTypeId());
                recCdFileMsg.setMessageTypesMessageTypeId(messageTypesMessageTypeIdNew);
            }
            Collection<ResCdFileMsg> attachedResCdFileMsgCollectionNew = new ArrayList<ResCdFileMsg>();
            for (ResCdFileMsg resCdFileMsgCollectionNewResCdFileMsgToAttach : resCdFileMsgCollectionNew) {
                resCdFileMsgCollectionNewResCdFileMsgToAttach = em.getReference(resCdFileMsgCollectionNewResCdFileMsgToAttach.getClass(), resCdFileMsgCollectionNewResCdFileMsgToAttach.getResCdFileId());
                attachedResCdFileMsgCollectionNew.add(resCdFileMsgCollectionNewResCdFileMsgToAttach);
            }
            resCdFileMsgCollectionNew = attachedResCdFileMsgCollectionNew;
            recCdFileMsg.setResCdFileMsgCollection(resCdFileMsgCollectionNew);
            Collection<TransactionLogs> attachedTransactionLogsCollectionNew = new ArrayList<TransactionLogs>();
            for (TransactionLogs transactionLogsCollectionNewTransactionLogsToAttach : transactionLogsCollectionNew) {
                transactionLogsCollectionNewTransactionLogsToAttach = em.getReference(transactionLogsCollectionNewTransactionLogsToAttach.getClass(), transactionLogsCollectionNewTransactionLogsToAttach.getLogID());
                attachedTransactionLogsCollectionNew.add(transactionLogsCollectionNewTransactionLogsToAttach);
            }
            transactionLogsCollectionNew = attachedTransactionLogsCollectionNew;
            recCdFileMsg.setTransactionLogsCollection(transactionLogsCollectionNew);
            Collection<CdFileDetails> attachedCdFileDetailsCollectionNew = new ArrayList<CdFileDetails>();
            for (CdFileDetails cdFileDetailsCollectionNewCdFileDetailsToAttach : cdFileDetailsCollectionNew) {
                cdFileDetailsCollectionNewCdFileDetailsToAttach = em.getReference(cdFileDetailsCollectionNewCdFileDetailsToAttach.getClass(), cdFileDetailsCollectionNewCdFileDetailsToAttach.getId());
                attachedCdFileDetailsCollectionNew.add(cdFileDetailsCollectionNewCdFileDetailsToAttach);
            }
            cdFileDetailsCollectionNew = attachedCdFileDetailsCollectionNew;
            recCdFileMsg.setCdFileDetailsCollection(cdFileDetailsCollectionNew);
            recCdFileMsg = em.merge(recCdFileMsg);
            if (messageTypesMessageTypeIdOld != null && !messageTypesMessageTypeIdOld.equals(messageTypesMessageTypeIdNew)) {
                messageTypesMessageTypeIdOld.getRecCdFileMsgCollection().remove(recCdFileMsg);
                messageTypesMessageTypeIdOld = em.merge(messageTypesMessageTypeIdOld);
            }
            if (messageTypesMessageTypeIdNew != null && !messageTypesMessageTypeIdNew.equals(messageTypesMessageTypeIdOld)) {
                messageTypesMessageTypeIdNew.getRecCdFileMsgCollection().add(recCdFileMsg);
                messageTypesMessageTypeIdNew = em.merge(messageTypesMessageTypeIdNew);
            }
            for (ResCdFileMsg resCdFileMsgCollectionOldResCdFileMsg : resCdFileMsgCollectionOld) {
                if (!resCdFileMsgCollectionNew.contains(resCdFileMsgCollectionOldResCdFileMsg)) {
                    resCdFileMsgCollectionOldResCdFileMsg.setRecCdFileMsgRecCdFileId(null);
                    resCdFileMsgCollectionOldResCdFileMsg = em.merge(resCdFileMsgCollectionOldResCdFileMsg);
                }
            }
            for (ResCdFileMsg resCdFileMsgCollectionNewResCdFileMsg : resCdFileMsgCollectionNew) {
                if (!resCdFileMsgCollectionOld.contains(resCdFileMsgCollectionNewResCdFileMsg)) {
                    RecCdFileMsg oldRecCdFileMsgRecCdFileIdOfResCdFileMsgCollectionNewResCdFileMsg = resCdFileMsgCollectionNewResCdFileMsg.getRecCdFileMsgRecCdFileId();
                    resCdFileMsgCollectionNewResCdFileMsg.setRecCdFileMsgRecCdFileId(recCdFileMsg);
                    resCdFileMsgCollectionNewResCdFileMsg = em.merge(resCdFileMsgCollectionNewResCdFileMsg);
                    if (oldRecCdFileMsgRecCdFileIdOfResCdFileMsgCollectionNewResCdFileMsg != null && !oldRecCdFileMsgRecCdFileIdOfResCdFileMsgCollectionNewResCdFileMsg.equals(recCdFileMsg)) {
                        oldRecCdFileMsgRecCdFileIdOfResCdFileMsgCollectionNewResCdFileMsg.getResCdFileMsgCollection().remove(resCdFileMsgCollectionNewResCdFileMsg);
                        oldRecCdFileMsgRecCdFileIdOfResCdFileMsgCollectionNewResCdFileMsg = em.merge(oldRecCdFileMsgRecCdFileIdOfResCdFileMsgCollectionNewResCdFileMsg);
                    }
                }
            }
            for (TransactionLogs transactionLogsCollectionOldTransactionLogs : transactionLogsCollectionOld) {
                if (!transactionLogsCollectionNew.contains(transactionLogsCollectionOldTransactionLogs)) {
                    transactionLogsCollectionOldTransactionLogs.setRecCdFileMsgRecCdFileId(null);
                    transactionLogsCollectionOldTransactionLogs = em.merge(transactionLogsCollectionOldTransactionLogs);
                }
            }
            for (TransactionLogs transactionLogsCollectionNewTransactionLogs : transactionLogsCollectionNew) {
                if (!transactionLogsCollectionOld.contains(transactionLogsCollectionNewTransactionLogs)) {
                    RecCdFileMsg oldRecCdFileMsgRecCdFileIdOfTransactionLogsCollectionNewTransactionLogs = transactionLogsCollectionNewTransactionLogs.getRecCdFileMsgRecCdFileId();
                    transactionLogsCollectionNewTransactionLogs.setRecCdFileMsgRecCdFileId(recCdFileMsg);
                    transactionLogsCollectionNewTransactionLogs = em.merge(transactionLogsCollectionNewTransactionLogs);
                    if (oldRecCdFileMsgRecCdFileIdOfTransactionLogsCollectionNewTransactionLogs != null && !oldRecCdFileMsgRecCdFileIdOfTransactionLogsCollectionNewTransactionLogs.equals(recCdFileMsg)) {
                        oldRecCdFileMsgRecCdFileIdOfTransactionLogsCollectionNewTransactionLogs.getTransactionLogsCollection().remove(transactionLogsCollectionNewTransactionLogs);
                        oldRecCdFileMsgRecCdFileIdOfTransactionLogsCollectionNewTransactionLogs = em.merge(oldRecCdFileMsgRecCdFileIdOfTransactionLogsCollectionNewTransactionLogs);
                    }
                }
            }
            for (CdFileDetails cdFileDetailsCollectionNewCdFileDetails : cdFileDetailsCollectionNew) {
                if (!cdFileDetailsCollectionOld.contains(cdFileDetailsCollectionNewCdFileDetails)) {
                    RecCdFileMsg oldRECCDFILEMSGRECCDFILEIDRefOfCdFileDetailsCollectionNewCdFileDetails = cdFileDetailsCollectionNewCdFileDetails.getRECCDFILEMSGRECCDFILEIDRef();
                    cdFileDetailsCollectionNewCdFileDetails.setRECCDFILEMSGRECCDFILEIDRef(recCdFileMsg);
                    cdFileDetailsCollectionNewCdFileDetails = em.merge(cdFileDetailsCollectionNewCdFileDetails);
                    if (oldRECCDFILEMSGRECCDFILEIDRefOfCdFileDetailsCollectionNewCdFileDetails != null && !oldRECCDFILEMSGRECCDFILEIDRefOfCdFileDetailsCollectionNewCdFileDetails.equals(recCdFileMsg)) {
                        oldRECCDFILEMSGRECCDFILEIDRefOfCdFileDetailsCollectionNewCdFileDetails.getCdFileDetailsCollection().remove(cdFileDetailsCollectionNewCdFileDetails);
                        oldRECCDFILEMSGRECCDFILEIDRefOfCdFileDetailsCollectionNewCdFileDetails = em.merge(oldRECCDFILEMSGRECCDFILEIDRefOfCdFileDetailsCollectionNewCdFileDetails);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recCdFileMsg.getRECCDFileID();
                if (findRecCdFileMsg(id) == null) {
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecCdFileMsg recCdFileMsg;
            try {
                recCdFileMsg = em.getReference(RecCdFileMsg.class, id);
                recCdFileMsg.getRECCDFileID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recCdFileMsg with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CdFileDetails> cdFileDetailsCollectionOrphanCheck = recCdFileMsg.getCdFileDetailsCollection();
            for (CdFileDetails cdFileDetailsCollectionOrphanCheckCdFileDetails : cdFileDetailsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RecCdFileMsg (" + recCdFileMsg + ") cannot be destroyed since the CdFileDetails " + cdFileDetailsCollectionOrphanCheckCdFileDetails + " in its cdFileDetailsCollection field has a non-nullable RECCDFILEMSGRECCDFILEIDRef field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            MessageTypes messageTypesMessageTypeId = recCdFileMsg.getMessageTypesMessageTypeId();
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId.getRecCdFileMsgCollection().remove(recCdFileMsg);
                messageTypesMessageTypeId = em.merge(messageTypesMessageTypeId);
            }
            Collection<ResCdFileMsg> resCdFileMsgCollection = recCdFileMsg.getResCdFileMsgCollection();
            for (ResCdFileMsg resCdFileMsgCollectionResCdFileMsg : resCdFileMsgCollection) {
                resCdFileMsgCollectionResCdFileMsg.setRecCdFileMsgRecCdFileId(null);
                resCdFileMsgCollectionResCdFileMsg = em.merge(resCdFileMsgCollectionResCdFileMsg);
            }
            Collection<TransactionLogs> transactionLogsCollection = recCdFileMsg.getTransactionLogsCollection();
            for (TransactionLogs transactionLogsCollectionTransactionLogs : transactionLogsCollection) {
                transactionLogsCollectionTransactionLogs.setRecCdFileMsgRecCdFileId(null);
                transactionLogsCollectionTransactionLogs = em.merge(transactionLogsCollectionTransactionLogs);
            }
            em.remove(recCdFileMsg);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RecCdFileMsg> findRecCdFileMsgEntities() {
        return findRecCdFileMsgEntities(true, -1, -1);
    }

    public List<RecCdFileMsg> findRecCdFileMsgEntities(int maxResults, int firstResult) {
        return findRecCdFileMsgEntities(false, maxResults, firstResult);
    }

    private List<RecCdFileMsg> findRecCdFileMsgEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RecCdFileMsg.class));
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

    public RecCdFileMsg findRecCdFileMsg(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RecCdFileMsg.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecCdFileMsgCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RecCdFileMsg> rt = cq.from(RecCdFileMsg.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
