/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.entities.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.kephis.ecs_kesws.entities.KeswsRefData;
import org.kephis.ecs_kesws.entities.KeswsRefDataCategory;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author kim
 */
public class KeswsRefDataJpaController implements Serializable {

    public KeswsRefDataJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(KeswsRefData keswsRefData) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            KeswsRefDataCategory keswsRefDataCategoryId = keswsRefData.getKeswsRefDataCategoryId();
            if (keswsRefDataCategoryId != null) {
                keswsRefDataCategoryId = em.getReference(keswsRefDataCategoryId.getClass(), keswsRefDataCategoryId.getId());
                keswsRefData.setKeswsRefDataCategoryId(keswsRefDataCategoryId);
            }
            em.persist(keswsRefData);
            if (keswsRefDataCategoryId != null) {
                keswsRefDataCategoryId.getKeswsRefDataCollection().add(keswsRefData);
                keswsRefDataCategoryId = em.merge(keswsRefDataCategoryId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(KeswsRefData keswsRefData) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            KeswsRefData persistentKeswsRefData = em.find(KeswsRefData.class, keswsRefData.getId());
            KeswsRefDataCategory keswsRefDataCategoryIdOld = persistentKeswsRefData.getKeswsRefDataCategoryId();
            KeswsRefDataCategory keswsRefDataCategoryIdNew = keswsRefData.getKeswsRefDataCategoryId();
            if (keswsRefDataCategoryIdNew != null) {
                keswsRefDataCategoryIdNew = em.getReference(keswsRefDataCategoryIdNew.getClass(), keswsRefDataCategoryIdNew.getId());
                keswsRefData.setKeswsRefDataCategoryId(keswsRefDataCategoryIdNew);
            }
            keswsRefData = em.merge(keswsRefData);
            if (keswsRefDataCategoryIdOld != null && !keswsRefDataCategoryIdOld.equals(keswsRefDataCategoryIdNew)) {
                keswsRefDataCategoryIdOld.getKeswsRefDataCollection().remove(keswsRefData);
                keswsRefDataCategoryIdOld = em.merge(keswsRefDataCategoryIdOld);
            }
            if (keswsRefDataCategoryIdNew != null && !keswsRefDataCategoryIdNew.equals(keswsRefDataCategoryIdOld)) {
                keswsRefDataCategoryIdNew.getKeswsRefDataCollection().add(keswsRefData);
                keswsRefDataCategoryIdNew = em.merge(keswsRefDataCategoryIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = keswsRefData.getId();
                if (findKeswsRefData(id) == null) {
                    throw new NonexistentEntityException("The keswsRefData with id " + id + " no longer exists.");
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
            KeswsRefData keswsRefData;
            try {
                keswsRefData = em.getReference(KeswsRefData.class, id);
                keswsRefData.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The keswsRefData with id " + id + " no longer exists.", enfe);
            }
            KeswsRefDataCategory keswsRefDataCategoryId = keswsRefData.getKeswsRefDataCategoryId();
            if (keswsRefDataCategoryId != null) {
                keswsRefDataCategoryId.getKeswsRefDataCollection().remove(keswsRefData);
                keswsRefDataCategoryId = em.merge(keswsRefDataCategoryId);
            }
            em.remove(keswsRefData);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<KeswsRefData> findKeswsRefDataEntities() {
        return findKeswsRefDataEntities(true, -1, -1);
    }

    public List<KeswsRefData> findKeswsRefDataEntities(int maxResults, int firstResult) {
        return findKeswsRefDataEntities(false, maxResults, firstResult);
    }

    private List<KeswsRefData> findKeswsRefDataEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(KeswsRefData.class));
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

    public KeswsRefData findKeswsRefData(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(KeswsRefData.class, id);
        } finally {
            em.close();
        }
    }

    public int getKeswsRefDataCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<KeswsRefData> rt = cq.from(KeswsRefData.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
