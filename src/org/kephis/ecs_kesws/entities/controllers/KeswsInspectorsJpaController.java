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
import org.kephis.ecs_kesws.entities.KeswsInspectors;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.PreexistingEntityException;

/**
 *
 * @author kim
 */
public class KeswsInspectorsJpaController implements Serializable {

    public KeswsInspectorsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(KeswsInspectors keswsInspectors) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(keswsInspectors);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findKeswsInspectors(keswsInspectors.getKeswsInspectorsId()) != null) {
                throw new PreexistingEntityException("KeswsInspectors " + keswsInspectors + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(KeswsInspectors keswsInspectors) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            keswsInspectors = em.merge(keswsInspectors);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = keswsInspectors.getKeswsInspectorsId();
                if (findKeswsInspectors(id) == null) {
                    throw new NonexistentEntityException("The keswsInspectors with id " + id + " no longer exists.");
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
            KeswsInspectors keswsInspectors;
            try {
                keswsInspectors = em.getReference(KeswsInspectors.class, id);
                keswsInspectors.getKeswsInspectorsId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The keswsInspectors with id " + id + " no longer exists.", enfe);
            }
            em.remove(keswsInspectors);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<KeswsInspectors> findKeswsInspectorsEntities() {
        return findKeswsInspectorsEntities(true, -1, -1);
    }

    public List<KeswsInspectors> findKeswsInspectorsEntities(int maxResults, int firstResult) {
        return findKeswsInspectorsEntities(false, maxResults, firstResult);
    }

    private List<KeswsInspectors> findKeswsInspectorsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(KeswsInspectors.class));
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

    public KeswsInspectors findKeswsInspectors(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(KeswsInspectors.class, id);
        } finally {
            em.close();
        }
    }

    public int getKeswsInspectorsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<KeswsInspectors> rt = cq.from(KeswsInspectors.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
