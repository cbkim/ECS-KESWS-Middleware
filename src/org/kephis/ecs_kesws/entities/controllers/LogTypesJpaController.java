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
import org.kephis.ecs_kesws.entities.TransactionLogs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.kephis.ecs_kesws.entities.LogTypes;
import org.kephis.ecs_kesws.entities.controllers.exceptions.IllegalOrphanException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.PreexistingEntityException;

/**
 *
 * @author kim
 */
public class LogTypesJpaController implements Serializable {

    public LogTypesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LogTypes logTypes) throws PreexistingEntityException, Exception {
        if (logTypes.getTransactionLogsCollection() == null) {
            logTypes.setTransactionLogsCollection(new ArrayList<TransactionLogs>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<TransactionLogs> attachedTransactionLogsCollection = new ArrayList<TransactionLogs>();
            for (TransactionLogs transactionLogsCollectionTransactionLogsToAttach : logTypes.getTransactionLogsCollection()) {
                transactionLogsCollectionTransactionLogsToAttach = em.getReference(transactionLogsCollectionTransactionLogsToAttach.getClass(), transactionLogsCollectionTransactionLogsToAttach.getLogID());
                attachedTransactionLogsCollection.add(transactionLogsCollectionTransactionLogsToAttach);
            }
            logTypes.setTransactionLogsCollection(attachedTransactionLogsCollection);
            em.persist(logTypes);
            for (TransactionLogs transactionLogsCollectionTransactionLogs : logTypes.getTransactionLogsCollection()) {
                LogTypes oldLogTypesLogIdLevelOfTransactionLogsCollectionTransactionLogs = transactionLogsCollectionTransactionLogs.getLogTypesLogIdLevel();
                transactionLogsCollectionTransactionLogs.setLogTypesLogIdLevel(logTypes);
                transactionLogsCollectionTransactionLogs = em.merge(transactionLogsCollectionTransactionLogs);
                if (oldLogTypesLogIdLevelOfTransactionLogsCollectionTransactionLogs != null) {
                    oldLogTypesLogIdLevelOfTransactionLogsCollectionTransactionLogs.getTransactionLogsCollection().remove(transactionLogsCollectionTransactionLogs);
                    oldLogTypesLogIdLevelOfTransactionLogsCollectionTransactionLogs = em.merge(oldLogTypesLogIdLevelOfTransactionLogsCollectionTransactionLogs);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLogTypes(logTypes.getLogIdLevel()) != null) {
                throw new PreexistingEntityException("LogTypes " + logTypes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LogTypes logTypes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LogTypes persistentLogTypes = em.find(LogTypes.class, logTypes.getLogIdLevel());
            Collection<TransactionLogs> transactionLogsCollectionOld = persistentLogTypes.getTransactionLogsCollection();
            Collection<TransactionLogs> transactionLogsCollectionNew = logTypes.getTransactionLogsCollection();
            List<String> illegalOrphanMessages = null;
            for (TransactionLogs transactionLogsCollectionOldTransactionLogs : transactionLogsCollectionOld) {
                if (!transactionLogsCollectionNew.contains(transactionLogsCollectionOldTransactionLogs)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TransactionLogs " + transactionLogsCollectionOldTransactionLogs + " since its logTypesLogIdLevel field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<TransactionLogs> attachedTransactionLogsCollectionNew = new ArrayList<TransactionLogs>();
            for (TransactionLogs transactionLogsCollectionNewTransactionLogsToAttach : transactionLogsCollectionNew) {
                transactionLogsCollectionNewTransactionLogsToAttach = em.getReference(transactionLogsCollectionNewTransactionLogsToAttach.getClass(), transactionLogsCollectionNewTransactionLogsToAttach.getLogID());
                attachedTransactionLogsCollectionNew.add(transactionLogsCollectionNewTransactionLogsToAttach);
            }
            transactionLogsCollectionNew = attachedTransactionLogsCollectionNew;
            logTypes.setTransactionLogsCollection(transactionLogsCollectionNew);
            logTypes = em.merge(logTypes);
            for (TransactionLogs transactionLogsCollectionNewTransactionLogs : transactionLogsCollectionNew) {
                if (!transactionLogsCollectionOld.contains(transactionLogsCollectionNewTransactionLogs)) {
                    LogTypes oldLogTypesLogIdLevelOfTransactionLogsCollectionNewTransactionLogs = transactionLogsCollectionNewTransactionLogs.getLogTypesLogIdLevel();
                    transactionLogsCollectionNewTransactionLogs.setLogTypesLogIdLevel(logTypes);
                    transactionLogsCollectionNewTransactionLogs = em.merge(transactionLogsCollectionNewTransactionLogs);
                    if (oldLogTypesLogIdLevelOfTransactionLogsCollectionNewTransactionLogs != null && !oldLogTypesLogIdLevelOfTransactionLogsCollectionNewTransactionLogs.equals(logTypes)) {
                        oldLogTypesLogIdLevelOfTransactionLogsCollectionNewTransactionLogs.getTransactionLogsCollection().remove(transactionLogsCollectionNewTransactionLogs);
                        oldLogTypesLogIdLevelOfTransactionLogsCollectionNewTransactionLogs = em.merge(oldLogTypesLogIdLevelOfTransactionLogsCollectionNewTransactionLogs);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = logTypes.getLogIdLevel();
                if (findLogTypes(id) == null) {
                    throw new NonexistentEntityException("The logTypes with id " + id + " no longer exists.");
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
            LogTypes logTypes;
            try {
                logTypes = em.getReference(LogTypes.class, id);
                logTypes.getLogIdLevel();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The logTypes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TransactionLogs> transactionLogsCollectionOrphanCheck = logTypes.getTransactionLogsCollection();
            for (TransactionLogs transactionLogsCollectionOrphanCheckTransactionLogs : transactionLogsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This LogTypes (" + logTypes + ") cannot be destroyed since the TransactionLogs " + transactionLogsCollectionOrphanCheckTransactionLogs + " in its transactionLogsCollection field has a non-nullable logTypesLogIdLevel field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(logTypes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LogTypes> findLogTypesEntities() {
        return findLogTypesEntities(true, -1, -1);
    }

    public List<LogTypes> findLogTypesEntities(int maxResults, int firstResult) {
        return findLogTypesEntities(false, maxResults, firstResult);
    }

    private List<LogTypes> findLogTypesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LogTypes.class));
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

    public LogTypes findLogTypes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LogTypes.class, id);
        } finally {
            em.close();
        }
    }

    public int getLogTypesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LogTypes> rt = cq.from(LogTypes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
