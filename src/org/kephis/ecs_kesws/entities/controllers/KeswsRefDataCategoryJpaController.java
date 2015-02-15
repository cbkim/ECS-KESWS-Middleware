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
import org.kephis.ecs_kesws.entities.KeswsRefData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.kephis.ecs_kesws.entities.KeswsRefDataCategory;
import org.kephis.ecs_kesws.entities.controllers.exceptions.IllegalOrphanException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.PreexistingEntityException;

/**
 *
 * @author kim
 */
public class KeswsRefDataCategoryJpaController implements Serializable {

    public KeswsRefDataCategoryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(KeswsRefDataCategory keswsRefDataCategory) throws PreexistingEntityException, Exception {
        if (keswsRefDataCategory.getKeswsRefDataCollection() == null) {
            keswsRefDataCategory.setKeswsRefDataCollection(new ArrayList<KeswsRefData>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<KeswsRefData> attachedKeswsRefDataCollection = new ArrayList<KeswsRefData>();
            for (KeswsRefData keswsRefDataCollectionKeswsRefDataToAttach : keswsRefDataCategory.getKeswsRefDataCollection()) {
                keswsRefDataCollectionKeswsRefDataToAttach = em.getReference(keswsRefDataCollectionKeswsRefDataToAttach.getClass(), keswsRefDataCollectionKeswsRefDataToAttach.getId());
                attachedKeswsRefDataCollection.add(keswsRefDataCollectionKeswsRefDataToAttach);
            }
            keswsRefDataCategory.setKeswsRefDataCollection(attachedKeswsRefDataCollection);
            em.persist(keswsRefDataCategory);
            for (KeswsRefData keswsRefDataCollectionKeswsRefData : keswsRefDataCategory.getKeswsRefDataCollection()) {
                KeswsRefDataCategory oldKeswsRefDataCategoryIdOfKeswsRefDataCollectionKeswsRefData = keswsRefDataCollectionKeswsRefData.getKeswsRefDataCategoryId();
                keswsRefDataCollectionKeswsRefData.setKeswsRefDataCategoryId(keswsRefDataCategory);
                keswsRefDataCollectionKeswsRefData = em.merge(keswsRefDataCollectionKeswsRefData);
                if (oldKeswsRefDataCategoryIdOfKeswsRefDataCollectionKeswsRefData != null) {
                    oldKeswsRefDataCategoryIdOfKeswsRefDataCollectionKeswsRefData.getKeswsRefDataCollection().remove(keswsRefDataCollectionKeswsRefData);
                    oldKeswsRefDataCategoryIdOfKeswsRefDataCollectionKeswsRefData = em.merge(oldKeswsRefDataCategoryIdOfKeswsRefDataCollectionKeswsRefData);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findKeswsRefDataCategory(keswsRefDataCategory.getId()) != null) {
                throw new PreexistingEntityException("KeswsRefDataCategory " + keswsRefDataCategory + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(KeswsRefDataCategory keswsRefDataCategory) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            KeswsRefDataCategory persistentKeswsRefDataCategory = em.find(KeswsRefDataCategory.class, keswsRefDataCategory.getId());
            Collection<KeswsRefData> keswsRefDataCollectionOld = persistentKeswsRefDataCategory.getKeswsRefDataCollection();
            Collection<KeswsRefData> keswsRefDataCollectionNew = keswsRefDataCategory.getKeswsRefDataCollection();
            List<String> illegalOrphanMessages = null;
            for (KeswsRefData keswsRefDataCollectionOldKeswsRefData : keswsRefDataCollectionOld) {
                if (!keswsRefDataCollectionNew.contains(keswsRefDataCollectionOldKeswsRefData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain KeswsRefData " + keswsRefDataCollectionOldKeswsRefData + " since its keswsRefDataCategoryId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<KeswsRefData> attachedKeswsRefDataCollectionNew = new ArrayList<KeswsRefData>();
            for (KeswsRefData keswsRefDataCollectionNewKeswsRefDataToAttach : keswsRefDataCollectionNew) {
                keswsRefDataCollectionNewKeswsRefDataToAttach = em.getReference(keswsRefDataCollectionNewKeswsRefDataToAttach.getClass(), keswsRefDataCollectionNewKeswsRefDataToAttach.getId());
                attachedKeswsRefDataCollectionNew.add(keswsRefDataCollectionNewKeswsRefDataToAttach);
            }
            keswsRefDataCollectionNew = attachedKeswsRefDataCollectionNew;
            keswsRefDataCategory.setKeswsRefDataCollection(keswsRefDataCollectionNew);
            keswsRefDataCategory = em.merge(keswsRefDataCategory);
            for (KeswsRefData keswsRefDataCollectionNewKeswsRefData : keswsRefDataCollectionNew) {
                if (!keswsRefDataCollectionOld.contains(keswsRefDataCollectionNewKeswsRefData)) {
                    KeswsRefDataCategory oldKeswsRefDataCategoryIdOfKeswsRefDataCollectionNewKeswsRefData = keswsRefDataCollectionNewKeswsRefData.getKeswsRefDataCategoryId();
                    keswsRefDataCollectionNewKeswsRefData.setKeswsRefDataCategoryId(keswsRefDataCategory);
                    keswsRefDataCollectionNewKeswsRefData = em.merge(keswsRefDataCollectionNewKeswsRefData);
                    if (oldKeswsRefDataCategoryIdOfKeswsRefDataCollectionNewKeswsRefData != null && !oldKeswsRefDataCategoryIdOfKeswsRefDataCollectionNewKeswsRefData.equals(keswsRefDataCategory)) {
                        oldKeswsRefDataCategoryIdOfKeswsRefDataCollectionNewKeswsRefData.getKeswsRefDataCollection().remove(keswsRefDataCollectionNewKeswsRefData);
                        oldKeswsRefDataCategoryIdOfKeswsRefDataCollectionNewKeswsRefData = em.merge(oldKeswsRefDataCategoryIdOfKeswsRefDataCollectionNewKeswsRefData);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = keswsRefDataCategory.getId();
                if (findKeswsRefDataCategory(id) == null) {
                    throw new NonexistentEntityException("The keswsRefDataCategory with id " + id + " no longer exists.");
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
            KeswsRefDataCategory keswsRefDataCategory;
            try {
                keswsRefDataCategory = em.getReference(KeswsRefDataCategory.class, id);
                keswsRefDataCategory.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The keswsRefDataCategory with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<KeswsRefData> keswsRefDataCollectionOrphanCheck = keswsRefDataCategory.getKeswsRefDataCollection();
            for (KeswsRefData keswsRefDataCollectionOrphanCheckKeswsRefData : keswsRefDataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This KeswsRefDataCategory (" + keswsRefDataCategory + ") cannot be destroyed since the KeswsRefData " + keswsRefDataCollectionOrphanCheckKeswsRefData + " in its keswsRefDataCollection field has a non-nullable keswsRefDataCategoryId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(keswsRefDataCategory);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<KeswsRefDataCategory> findKeswsRefDataCategoryEntities() {
        return findKeswsRefDataCategoryEntities(true, -1, -1);
    }

    public List<KeswsRefDataCategory> findKeswsRefDataCategoryEntities(int maxResults, int firstResult) {
        return findKeswsRefDataCategoryEntities(false, maxResults, firstResult);
    }

    private List<KeswsRefDataCategory> findKeswsRefDataCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(KeswsRefDataCategory.class));
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

    public KeswsRefDataCategory findKeswsRefDataCategory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(KeswsRefDataCategory.class, id);
        } finally {
            em.close();
        }
    }

    public int getKeswsRefDataCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<KeswsRefDataCategory> rt = cq.from(KeswsRefDataCategory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
