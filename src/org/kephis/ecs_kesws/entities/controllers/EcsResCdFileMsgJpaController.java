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
import org.kephis.ecs_kesws.entities.EcsResCdFileMsg;
import org.kephis.ecs_kesws.entities.controllers.exceptions.IllegalOrphanException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author kim
 */
public class EcsResCdFileMsgJpaController implements Serializable {

    public EcsResCdFileMsgJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EcsResCdFileMsg ecsResCdFileMsg) {
        if (ecsResCdFileMsg.getResCdFileMsgCollection() == null) {
            ecsResCdFileMsg.setResCdFileMsgCollection(new ArrayList<ResCdFileMsg>());
        }
        if (ecsResCdFileMsg.getTransactionLogsCollection() == null) {
            ecsResCdFileMsg.setTransactionLogsCollection(new ArrayList<TransactionLogs>());
        }
        if (ecsResCdFileMsg.getCdFileDetailsCollection() == null) {
            ecsResCdFileMsg.setCdFileDetailsCollection(new ArrayList<CdFileDetails>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MessageTypes messageTypesMessageTypeId = ecsResCdFileMsg.getMessageTypesMessageTypeId();
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId = em.getReference(messageTypesMessageTypeId.getClass(), messageTypesMessageTypeId.getMessageTypeId());
                ecsResCdFileMsg.setMessageTypesMessageTypeId(messageTypesMessageTypeId);
            }
            Collection<ResCdFileMsg> attachedResCdFileMsgCollection = new ArrayList<ResCdFileMsg>();
            for (ResCdFileMsg resCdFileMsgCollectionResCdFileMsgToAttach : ecsResCdFileMsg.getResCdFileMsgCollection()) {
                resCdFileMsgCollectionResCdFileMsgToAttach = em.getReference(resCdFileMsgCollectionResCdFileMsgToAttach.getClass(), resCdFileMsgCollectionResCdFileMsgToAttach.getResCdFileId());
                attachedResCdFileMsgCollection.add(resCdFileMsgCollectionResCdFileMsgToAttach);
            }
            ecsResCdFileMsg.setResCdFileMsgCollection(attachedResCdFileMsgCollection);
            Collection<TransactionLogs> attachedTransactionLogsCollection = new ArrayList<TransactionLogs>();
            for (TransactionLogs transactionLogsCollectionTransactionLogsToAttach : ecsResCdFileMsg.getTransactionLogsCollection()) {
                transactionLogsCollectionTransactionLogsToAttach = em.getReference(transactionLogsCollectionTransactionLogsToAttach.getClass(), transactionLogsCollectionTransactionLogsToAttach.getLogID());
                attachedTransactionLogsCollection.add(transactionLogsCollectionTransactionLogsToAttach);
            }
            ecsResCdFileMsg.setTransactionLogsCollection(attachedTransactionLogsCollection);
            Collection<CdFileDetails> attachedCdFileDetailsCollection = new ArrayList<CdFileDetails>();
            for (CdFileDetails cdFileDetailsCollectionCdFileDetailsToAttach : ecsResCdFileMsg.getCdFileDetailsCollection()) {
                cdFileDetailsCollectionCdFileDetailsToAttach = em.getReference(cdFileDetailsCollectionCdFileDetailsToAttach.getClass(), cdFileDetailsCollectionCdFileDetailsToAttach.getId());
                attachedCdFileDetailsCollection.add(cdFileDetailsCollectionCdFileDetailsToAttach);
            }
            ecsResCdFileMsg.setCdFileDetailsCollection(attachedCdFileDetailsCollection);
            em.persist(ecsResCdFileMsg);
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId.getEcsResCdFileMsgCollection().add(ecsResCdFileMsg);
                messageTypesMessageTypeId = em.merge(messageTypesMessageTypeId);
            }
            for (ResCdFileMsg resCdFileMsgCollectionResCdFileMsg : ecsResCdFileMsg.getResCdFileMsgCollection()) {
                EcsResCdFileMsg oldECSRESCDFILEMSGRECCDFileIDOfResCdFileMsgCollectionResCdFileMsg = resCdFileMsgCollectionResCdFileMsg.getECSRESCDFILEMSGRECCDFileID();
                resCdFileMsgCollectionResCdFileMsg.setECSRESCDFILEMSGRECCDFileID(ecsResCdFileMsg);
                resCdFileMsgCollectionResCdFileMsg = em.merge(resCdFileMsgCollectionResCdFileMsg);
                if (oldECSRESCDFILEMSGRECCDFileIDOfResCdFileMsgCollectionResCdFileMsg != null) {
                    oldECSRESCDFILEMSGRECCDFileIDOfResCdFileMsgCollectionResCdFileMsg.getResCdFileMsgCollection().remove(resCdFileMsgCollectionResCdFileMsg);
                    oldECSRESCDFILEMSGRECCDFileIDOfResCdFileMsgCollectionResCdFileMsg = em.merge(oldECSRESCDFILEMSGRECCDFileIDOfResCdFileMsgCollectionResCdFileMsg);
                }
            }
            for (TransactionLogs transactionLogsCollectionTransactionLogs : ecsResCdFileMsg.getTransactionLogsCollection()) {
                EcsResCdFileMsg oldECSRESCDFILEMSGRECCDFileIDOfTransactionLogsCollectionTransactionLogs = transactionLogsCollectionTransactionLogs.getECSRESCDFILEMSGRECCDFileID();
                transactionLogsCollectionTransactionLogs.setECSRESCDFILEMSGRECCDFileID(ecsResCdFileMsg);
                transactionLogsCollectionTransactionLogs = em.merge(transactionLogsCollectionTransactionLogs);
                if (oldECSRESCDFILEMSGRECCDFileIDOfTransactionLogsCollectionTransactionLogs != null) {
                    oldECSRESCDFILEMSGRECCDFileIDOfTransactionLogsCollectionTransactionLogs.getTransactionLogsCollection().remove(transactionLogsCollectionTransactionLogs);
                    oldECSRESCDFILEMSGRECCDFileIDOfTransactionLogsCollectionTransactionLogs = em.merge(oldECSRESCDFILEMSGRECCDFileIDOfTransactionLogsCollectionTransactionLogs);
                }
            }
            for (CdFileDetails cdFileDetailsCollectionCdFileDetails : ecsResCdFileMsg.getCdFileDetailsCollection()) {
                EcsResCdFileMsg oldECSRESCDFILEMSGRECCDFileIDOfCdFileDetailsCollectionCdFileDetails = cdFileDetailsCollectionCdFileDetails.getECSRESCDFILEMSGRECCDFileID();
                cdFileDetailsCollectionCdFileDetails.setECSRESCDFILEMSGRECCDFileID(ecsResCdFileMsg);
                cdFileDetailsCollectionCdFileDetails = em.merge(cdFileDetailsCollectionCdFileDetails);
                if (oldECSRESCDFILEMSGRECCDFileIDOfCdFileDetailsCollectionCdFileDetails != null) {
                    oldECSRESCDFILEMSGRECCDFileIDOfCdFileDetailsCollectionCdFileDetails.getCdFileDetailsCollection().remove(cdFileDetailsCollectionCdFileDetails);
                    oldECSRESCDFILEMSGRECCDFileIDOfCdFileDetailsCollectionCdFileDetails = em.merge(oldECSRESCDFILEMSGRECCDFileIDOfCdFileDetailsCollectionCdFileDetails);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EcsResCdFileMsg ecsResCdFileMsg) throws IllegalOrphanException, NonexistentEntityException, Exception {
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
            for (TransactionLogs transactionLogsCollectionOldTransactionLogs : transactionLogsCollectionOld) {
                if (!transactionLogsCollectionNew.contains(transactionLogsCollectionOldTransactionLogs)) {
                    transactionLogsCollectionOldTransactionLogs.setECSRESCDFILEMSGRECCDFileID(null);
                    transactionLogsCollectionOldTransactionLogs = em.merge(transactionLogsCollectionOldTransactionLogs);
                }
            }
            for (TransactionLogs transactionLogsCollectionNewTransactionLogs : transactionLogsCollectionNew) {
                if (!transactionLogsCollectionOld.contains(transactionLogsCollectionNewTransactionLogs)) {
                    EcsResCdFileMsg oldECSRESCDFILEMSGRECCDFileIDOfTransactionLogsCollectionNewTransactionLogs = transactionLogsCollectionNewTransactionLogs.getECSRESCDFILEMSGRECCDFileID();
                    transactionLogsCollectionNewTransactionLogs.setECSRESCDFILEMSGRECCDFileID(ecsResCdFileMsg);
                    transactionLogsCollectionNewTransactionLogs = em.merge(transactionLogsCollectionNewTransactionLogs);
                    if (oldECSRESCDFILEMSGRECCDFileIDOfTransactionLogsCollectionNewTransactionLogs != null && !oldECSRESCDFILEMSGRECCDFileIDOfTransactionLogsCollectionNewTransactionLogs.equals(ecsResCdFileMsg)) {
                        oldECSRESCDFILEMSGRECCDFileIDOfTransactionLogsCollectionNewTransactionLogs.getTransactionLogsCollection().remove(transactionLogsCollectionNewTransactionLogs);
                        oldECSRESCDFILEMSGRECCDFileIDOfTransactionLogsCollectionNewTransactionLogs = em.merge(oldECSRESCDFILEMSGRECCDFileIDOfTransactionLogsCollectionNewTransactionLogs);
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EcsResCdFileMsg ecsResCdFileMsg;
            try {
                ecsResCdFileMsg = em.getReference(EcsResCdFileMsg.class, id);
                ecsResCdFileMsg.getRECCDFileID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ecsResCdFileMsg with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CdFileDetails> cdFileDetailsCollectionOrphanCheck = ecsResCdFileMsg.getCdFileDetailsCollection();
            for (CdFileDetails cdFileDetailsCollectionOrphanCheckCdFileDetails : cdFileDetailsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EcsResCdFileMsg (" + ecsResCdFileMsg + ") cannot be destroyed since the CdFileDetails " + cdFileDetailsCollectionOrphanCheckCdFileDetails + " in its cdFileDetailsCollection field has a non-nullable ECSRESCDFILEMSGRECCDFileID field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            MessageTypes messageTypesMessageTypeId = ecsResCdFileMsg.getMessageTypesMessageTypeId();
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId.getEcsResCdFileMsgCollection().remove(ecsResCdFileMsg);
                messageTypesMessageTypeId = em.merge(messageTypesMessageTypeId);
            }
            Collection<ResCdFileMsg> resCdFileMsgCollection = ecsResCdFileMsg.getResCdFileMsgCollection();
            for (ResCdFileMsg resCdFileMsgCollectionResCdFileMsg : resCdFileMsgCollection) {
                resCdFileMsgCollectionResCdFileMsg.setECSRESCDFILEMSGRECCDFileID(null);
                resCdFileMsgCollectionResCdFileMsg = em.merge(resCdFileMsgCollectionResCdFileMsg);
            }
            Collection<TransactionLogs> transactionLogsCollection = ecsResCdFileMsg.getTransactionLogsCollection();
            for (TransactionLogs transactionLogsCollectionTransactionLogs : transactionLogsCollection) {
                transactionLogsCollectionTransactionLogs.setECSRESCDFILEMSGRECCDFileID(null);
                transactionLogsCollectionTransactionLogs = em.merge(transactionLogsCollectionTransactionLogs);
            }
            em.remove(ecsResCdFileMsg);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EcsResCdFileMsg> findEcsResCdFileMsgEntities() {
        return findEcsResCdFileMsgEntities(true, -1, -1);
    }

    public List<EcsResCdFileMsg> findEcsResCdFileMsgEntities(int maxResults, int firstResult) {
        return findEcsResCdFileMsgEntities(false, maxResults, firstResult);
    }

    private List<EcsResCdFileMsg> findEcsResCdFileMsgEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EcsResCdFileMsg.class));
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

    public EcsResCdFileMsg findEcsResCdFileMsg(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EcsResCdFileMsg.class, id);
        } finally {
            em.close();
        }
    }

    public int getEcsResCdFileMsgCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EcsResCdFileMsg> rt = cq.from(EcsResCdFileMsg.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
