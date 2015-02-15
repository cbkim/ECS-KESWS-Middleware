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
import org.kephis.ecs_kesws.entities.EcsConDoc;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.PreexistingEntityException;

/**
 *
 * @author kim
 */
public class EcsConDocJpaController implements Serializable {

    public EcsConDocJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EcsConDoc ecsConDoc) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ecsConDoc);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEcsConDoc(ecsConDoc.getId()) != null) {
                throw new PreexistingEntityException("EcsConDoc " + ecsConDoc + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EcsConDoc ecsConDoc) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ecsConDoc = em.merge(ecsConDoc);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = ecsConDoc.getId();
                if (findEcsConDoc(id) == null) {
                    throw new NonexistentEntityException("The ecsConDoc with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EcsConDoc ecsConDoc;
            try {
                ecsConDoc = em.getReference(EcsConDoc.class, id);
                ecsConDoc.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ecsConDoc with id " + id + " no longer exists.", enfe);
            }
            em.remove(ecsConDoc);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EcsConDoc> findEcsConDocEntities() {
        return findEcsConDocEntities(true, -1, -1);
    }

    public List<EcsConDoc> findEcsConDocEntities(int maxResults, int firstResult) {
        return findEcsConDocEntities(false, maxResults, firstResult);
    }

    private List<EcsConDoc> findEcsConDocEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EcsConDoc.class));
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

    public EcsConDoc findEcsConDoc(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EcsConDoc.class, id);
        } finally {
            em.close();
        }
    }

    public int getEcsConDocCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EcsConDoc> rt = cq.from(EcsConDoc.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
