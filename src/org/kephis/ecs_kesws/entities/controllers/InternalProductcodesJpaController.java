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
import org.kephis.ecs_kesws.entities.PricelistInternalProductcodeDocumentMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.kephis.ecs_kesws.entities.InternalProductcodes;
import org.kephis.ecs_kesws.entities.controllers.exceptions.IllegalOrphanException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author kim
 */
public class InternalProductcodesJpaController implements Serializable {

    public InternalProductcodesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InternalProductcodes internalProductcodes) {
        if (internalProductcodes.getPricelistInternalProductcodeDocumentMapCollection() == null) {
            internalProductcodes.setPricelistInternalProductcodeDocumentMapCollection(new ArrayList<PricelistInternalProductcodeDocumentMap>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<PricelistInternalProductcodeDocumentMap> attachedPricelistInternalProductcodeDocumentMapCollection = new ArrayList<PricelistInternalProductcodeDocumentMap>();
            for (PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMapToAttach : internalProductcodes.getPricelistInternalProductcodeDocumentMapCollection()) {
                pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMapToAttach = em.getReference(pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMapToAttach.getClass(), pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMapToAttach.getPricelistIPCMAPID());
                attachedPricelistInternalProductcodeDocumentMapCollection.add(pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMapToAttach);
            }
            internalProductcodes.setPricelistInternalProductcodeDocumentMapCollection(attachedPricelistInternalProductcodeDocumentMapCollection);
            em.persist(internalProductcodes);
            for (PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap : internalProductcodes.getPricelistInternalProductcodeDocumentMapCollection()) {
                InternalProductcodes oldINTERNALPRODUCTCODESIPCIDRefOfPricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap = pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap.getINTERNALPRODUCTCODESIPCIDRef();
                pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap.setINTERNALPRODUCTCODESIPCIDRef(internalProductcodes);
                pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap = em.merge(pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap);
                if (oldINTERNALPRODUCTCODESIPCIDRefOfPricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap != null) {
                    oldINTERNALPRODUCTCODESIPCIDRefOfPricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap.getPricelistInternalProductcodeDocumentMapCollection().remove(pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap);
                    oldINTERNALPRODUCTCODESIPCIDRefOfPricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap = em.merge(oldINTERNALPRODUCTCODESIPCIDRefOfPricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(InternalProductcodes internalProductcodes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InternalProductcodes persistentInternalProductcodes = em.find(InternalProductcodes.class, internalProductcodes.getIpcId());
            Collection<PricelistInternalProductcodeDocumentMap> pricelistInternalProductcodeDocumentMapCollectionOld = persistentInternalProductcodes.getPricelistInternalProductcodeDocumentMapCollection();
            Collection<PricelistInternalProductcodeDocumentMap> pricelistInternalProductcodeDocumentMapCollectionNew = internalProductcodes.getPricelistInternalProductcodeDocumentMapCollection();
            List<String> illegalOrphanMessages = null;
            for (PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMapCollectionOldPricelistInternalProductcodeDocumentMap : pricelistInternalProductcodeDocumentMapCollectionOld) {
                if (!pricelistInternalProductcodeDocumentMapCollectionNew.contains(pricelistInternalProductcodeDocumentMapCollectionOldPricelistInternalProductcodeDocumentMap)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PricelistInternalProductcodeDocumentMap " + pricelistInternalProductcodeDocumentMapCollectionOldPricelistInternalProductcodeDocumentMap + " since its INTERNALPRODUCTCODESIPCIDRef field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<PricelistInternalProductcodeDocumentMap> attachedPricelistInternalProductcodeDocumentMapCollectionNew = new ArrayList<PricelistInternalProductcodeDocumentMap>();
            for (PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMapToAttach : pricelistInternalProductcodeDocumentMapCollectionNew) {
                pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMapToAttach = em.getReference(pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMapToAttach.getClass(), pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMapToAttach.getPricelistIPCMAPID());
                attachedPricelistInternalProductcodeDocumentMapCollectionNew.add(pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMapToAttach);
            }
            pricelistInternalProductcodeDocumentMapCollectionNew = attachedPricelistInternalProductcodeDocumentMapCollectionNew;
            internalProductcodes.setPricelistInternalProductcodeDocumentMapCollection(pricelistInternalProductcodeDocumentMapCollectionNew);
            internalProductcodes = em.merge(internalProductcodes);
            for (PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap : pricelistInternalProductcodeDocumentMapCollectionNew) {
                if (!pricelistInternalProductcodeDocumentMapCollectionOld.contains(pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap)) {
                    InternalProductcodes oldINTERNALPRODUCTCODESIPCIDRefOfPricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap = pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap.getINTERNALPRODUCTCODESIPCIDRef();
                    pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap.setINTERNALPRODUCTCODESIPCIDRef(internalProductcodes);
                    pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap = em.merge(pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap);
                    if (oldINTERNALPRODUCTCODESIPCIDRefOfPricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap != null && !oldINTERNALPRODUCTCODESIPCIDRefOfPricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap.equals(internalProductcodes)) {
                        oldINTERNALPRODUCTCODESIPCIDRefOfPricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap.getPricelistInternalProductcodeDocumentMapCollection().remove(pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap);
                        oldINTERNALPRODUCTCODESIPCIDRefOfPricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap = em.merge(oldINTERNALPRODUCTCODESIPCIDRefOfPricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = internalProductcodes.getIpcId();
                if (findInternalProductcodes(id) == null) {
                    throw new NonexistentEntityException("The internalProductcodes with id " + id + " no longer exists.");
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
            InternalProductcodes internalProductcodes;
            try {
                internalProductcodes = em.getReference(InternalProductcodes.class, id);
                internalProductcodes.getIpcId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The internalProductcodes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PricelistInternalProductcodeDocumentMap> pricelistInternalProductcodeDocumentMapCollectionOrphanCheck = internalProductcodes.getPricelistInternalProductcodeDocumentMapCollection();
            for (PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMapCollectionOrphanCheckPricelistInternalProductcodeDocumentMap : pricelistInternalProductcodeDocumentMapCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This InternalProductcodes (" + internalProductcodes + ") cannot be destroyed since the PricelistInternalProductcodeDocumentMap " + pricelistInternalProductcodeDocumentMapCollectionOrphanCheckPricelistInternalProductcodeDocumentMap + " in its pricelistInternalProductcodeDocumentMapCollection field has a non-nullable INTERNALPRODUCTCODESIPCIDRef field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(internalProductcodes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InternalProductcodes> findInternalProductcodesEntities() {
        return findInternalProductcodesEntities(true, -1, -1);
    }

    public List<InternalProductcodes> findInternalProductcodesEntities(int maxResults, int firstResult) {
        return findInternalProductcodesEntities(false, maxResults, firstResult);
    }

    private List<InternalProductcodes> findInternalProductcodesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InternalProductcodes.class));
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

    public InternalProductcodes findInternalProductcodes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InternalProductcodes.class, id);
        } finally {
            em.close();
        }
    }

    public int getInternalProductcodesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InternalProductcodes> rt = cq.from(InternalProductcodes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
