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
import org.kephis.ecs_kesws.entities.Pricelist;
import org.kephis.ecs_kesws.entities.controllers.exceptions.IllegalOrphanException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author kim
 */
public class PricelistJpaController implements Serializable {

    public PricelistJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pricelist pricelist) {
        if (pricelist.getPricelistInternalProductcodeDocumentMapCollection() == null) {
            pricelist.setPricelistInternalProductcodeDocumentMapCollection(new ArrayList<PricelistInternalProductcodeDocumentMap>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<PricelistInternalProductcodeDocumentMap> attachedPricelistInternalProductcodeDocumentMapCollection = new ArrayList<PricelistInternalProductcodeDocumentMap>();
            for (PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMapToAttach : pricelist.getPricelistInternalProductcodeDocumentMapCollection()) {
                pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMapToAttach = em.getReference(pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMapToAttach.getClass(), pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMapToAttach.getPricelistIPCMAPID());
                attachedPricelistInternalProductcodeDocumentMapCollection.add(pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMapToAttach);
            }
            pricelist.setPricelistInternalProductcodeDocumentMapCollection(attachedPricelistInternalProductcodeDocumentMapCollection);
            em.persist(pricelist);
            for (PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap : pricelist.getPricelistInternalProductcodeDocumentMapCollection()) {
                Pricelist oldPRICELISTPriceIDRefOfPricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap = pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap.getPRICELISTPriceIDRef();
                pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap.setPRICELISTPriceIDRef(pricelist);
                pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap = em.merge(pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap);
                if (oldPRICELISTPriceIDRefOfPricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap != null) {
                    oldPRICELISTPriceIDRefOfPricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap.getPricelistInternalProductcodeDocumentMapCollection().remove(pricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap);
                    oldPRICELISTPriceIDRefOfPricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap = em.merge(oldPRICELISTPriceIDRefOfPricelistInternalProductcodeDocumentMapCollectionPricelistInternalProductcodeDocumentMap);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pricelist pricelist) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pricelist persistentPricelist = em.find(Pricelist.class, pricelist.getPriceID());
            Collection<PricelistInternalProductcodeDocumentMap> pricelistInternalProductcodeDocumentMapCollectionOld = persistentPricelist.getPricelistInternalProductcodeDocumentMapCollection();
            Collection<PricelistInternalProductcodeDocumentMap> pricelistInternalProductcodeDocumentMapCollectionNew = pricelist.getPricelistInternalProductcodeDocumentMapCollection();
            List<String> illegalOrphanMessages = null;
            for (PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMapCollectionOldPricelistInternalProductcodeDocumentMap : pricelistInternalProductcodeDocumentMapCollectionOld) {
                if (!pricelistInternalProductcodeDocumentMapCollectionNew.contains(pricelistInternalProductcodeDocumentMapCollectionOldPricelistInternalProductcodeDocumentMap)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PricelistInternalProductcodeDocumentMap " + pricelistInternalProductcodeDocumentMapCollectionOldPricelistInternalProductcodeDocumentMap + " since its PRICELISTPriceIDRef field is not nullable.");
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
            pricelist.setPricelistInternalProductcodeDocumentMapCollection(pricelistInternalProductcodeDocumentMapCollectionNew);
            pricelist = em.merge(pricelist);
            for (PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap : pricelistInternalProductcodeDocumentMapCollectionNew) {
                if (!pricelistInternalProductcodeDocumentMapCollectionOld.contains(pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap)) {
                    Pricelist oldPRICELISTPriceIDRefOfPricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap = pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap.getPRICELISTPriceIDRef();
                    pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap.setPRICELISTPriceIDRef(pricelist);
                    pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap = em.merge(pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap);
                    if (oldPRICELISTPriceIDRefOfPricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap != null && !oldPRICELISTPriceIDRefOfPricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap.equals(pricelist)) {
                        oldPRICELISTPriceIDRefOfPricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap.getPricelistInternalProductcodeDocumentMapCollection().remove(pricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap);
                        oldPRICELISTPriceIDRefOfPricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap = em.merge(oldPRICELISTPriceIDRefOfPricelistInternalProductcodeDocumentMapCollectionNewPricelistInternalProductcodeDocumentMap);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pricelist.getPriceID();
                if (findPricelist(id) == null) {
                    throw new NonexistentEntityException("The pricelist with id " + id + " no longer exists.");
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
            Pricelist pricelist;
            try {
                pricelist = em.getReference(Pricelist.class, id);
                pricelist.getPriceID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pricelist with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PricelistInternalProductcodeDocumentMap> pricelistInternalProductcodeDocumentMapCollectionOrphanCheck = pricelist.getPricelistInternalProductcodeDocumentMapCollection();
            for (PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMapCollectionOrphanCheckPricelistInternalProductcodeDocumentMap : pricelistInternalProductcodeDocumentMapCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pricelist (" + pricelist + ") cannot be destroyed since the PricelistInternalProductcodeDocumentMap " + pricelistInternalProductcodeDocumentMapCollectionOrphanCheckPricelistInternalProductcodeDocumentMap + " in its pricelistInternalProductcodeDocumentMapCollection field has a non-nullable PRICELISTPriceIDRef field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pricelist);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pricelist> findPricelistEntities() {
        return findPricelistEntities(true, -1, -1);
    }

    public List<Pricelist> findPricelistEntities(int maxResults, int firstResult) {
        return findPricelistEntities(false, maxResults, firstResult);
    }

    private List<Pricelist> findPricelistEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pricelist.class));
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

    public Pricelist findPricelist(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pricelist.class, id);
        } finally {
            em.close();
        }
    }

    public int getPricelistCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pricelist> rt = cq.from(Pricelist.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
