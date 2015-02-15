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
import org.kephis.ecs_kesws.entities.ResCdFileMsg;
import org.kephis.ecs_kesws.entities.MessageTypes;
import org.kephis.ecs_kesws.entities.RecErrorFileMsg;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author kim
 */
public class RecErrorFileMsgJpaController implements Serializable {

    public RecErrorFileMsgJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RecErrorFileMsg recErrorFileMsg) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ResCdFileMsg resCdFileMsgResCdFileId = recErrorFileMsg.getResCdFileMsgResCdFileId();
            if (resCdFileMsgResCdFileId != null) {
                resCdFileMsgResCdFileId = em.getReference(resCdFileMsgResCdFileId.getClass(), resCdFileMsgResCdFileId.getResCdFileId());
                recErrorFileMsg.setResCdFileMsgResCdFileId(resCdFileMsgResCdFileId);
            }
            MessageTypes messageTypesMessageTypeId = recErrorFileMsg.getMessageTypesMessageTypeId();
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId = em.getReference(messageTypesMessageTypeId.getClass(), messageTypesMessageTypeId.getMessageTypeId());
                recErrorFileMsg.setMessageTypesMessageTypeId(messageTypesMessageTypeId);
            }
            em.persist(recErrorFileMsg);
            if (resCdFileMsgResCdFileId != null) {
                resCdFileMsgResCdFileId.getRecErrorFileMsgCollection().add(recErrorFileMsg);
                resCdFileMsgResCdFileId = em.merge(resCdFileMsgResCdFileId);
            }
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId.getRecErrorFileMsgCollection().add(recErrorFileMsg);
                messageTypesMessageTypeId = em.merge(messageTypesMessageTypeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RecErrorFileMsg recErrorFileMsg) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecErrorFileMsg persistentRecErrorFileMsg = em.find(RecErrorFileMsg.class, recErrorFileMsg.getRecErrorMsgId());
            ResCdFileMsg resCdFileMsgResCdFileIdOld = persistentRecErrorFileMsg.getResCdFileMsgResCdFileId();
            ResCdFileMsg resCdFileMsgResCdFileIdNew = recErrorFileMsg.getResCdFileMsgResCdFileId();
            MessageTypes messageTypesMessageTypeIdOld = persistentRecErrorFileMsg.getMessageTypesMessageTypeId();
            MessageTypes messageTypesMessageTypeIdNew = recErrorFileMsg.getMessageTypesMessageTypeId();
            if (resCdFileMsgResCdFileIdNew != null) {
                resCdFileMsgResCdFileIdNew = em.getReference(resCdFileMsgResCdFileIdNew.getClass(), resCdFileMsgResCdFileIdNew.getResCdFileId());
                recErrorFileMsg.setResCdFileMsgResCdFileId(resCdFileMsgResCdFileIdNew);
            }
            if (messageTypesMessageTypeIdNew != null) {
                messageTypesMessageTypeIdNew = em.getReference(messageTypesMessageTypeIdNew.getClass(), messageTypesMessageTypeIdNew.getMessageTypeId());
                recErrorFileMsg.setMessageTypesMessageTypeId(messageTypesMessageTypeIdNew);
            }
            recErrorFileMsg = em.merge(recErrorFileMsg);
            if (resCdFileMsgResCdFileIdOld != null && !resCdFileMsgResCdFileIdOld.equals(resCdFileMsgResCdFileIdNew)) {
                resCdFileMsgResCdFileIdOld.getRecErrorFileMsgCollection().remove(recErrorFileMsg);
                resCdFileMsgResCdFileIdOld = em.merge(resCdFileMsgResCdFileIdOld);
            }
            if (resCdFileMsgResCdFileIdNew != null && !resCdFileMsgResCdFileIdNew.equals(resCdFileMsgResCdFileIdOld)) {
                resCdFileMsgResCdFileIdNew.getRecErrorFileMsgCollection().add(recErrorFileMsg);
                resCdFileMsgResCdFileIdNew = em.merge(resCdFileMsgResCdFileIdNew);
            }
            if (messageTypesMessageTypeIdOld != null && !messageTypesMessageTypeIdOld.equals(messageTypesMessageTypeIdNew)) {
                messageTypesMessageTypeIdOld.getRecErrorFileMsgCollection().remove(recErrorFileMsg);
                messageTypesMessageTypeIdOld = em.merge(messageTypesMessageTypeIdOld);
            }
            if (messageTypesMessageTypeIdNew != null && !messageTypesMessageTypeIdNew.equals(messageTypesMessageTypeIdOld)) {
                messageTypesMessageTypeIdNew.getRecErrorFileMsgCollection().add(recErrorFileMsg);
                messageTypesMessageTypeIdNew = em.merge(messageTypesMessageTypeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recErrorFileMsg.getRecErrorMsgId();
                if (findRecErrorFileMsg(id) == null) {
                    throw new NonexistentEntityException("The recErrorFileMsg with id " + id + " no longer exists.");
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
            RecErrorFileMsg recErrorFileMsg;
            try {
                recErrorFileMsg = em.getReference(RecErrorFileMsg.class, id);
                recErrorFileMsg.getRecErrorMsgId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recErrorFileMsg with id " + id + " no longer exists.", enfe);
            }
            ResCdFileMsg resCdFileMsgResCdFileId = recErrorFileMsg.getResCdFileMsgResCdFileId();
            if (resCdFileMsgResCdFileId != null) {
                resCdFileMsgResCdFileId.getRecErrorFileMsgCollection().remove(recErrorFileMsg);
                resCdFileMsgResCdFileId = em.merge(resCdFileMsgResCdFileId);
            }
            MessageTypes messageTypesMessageTypeId = recErrorFileMsg.getMessageTypesMessageTypeId();
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId.getRecErrorFileMsgCollection().remove(recErrorFileMsg);
                messageTypesMessageTypeId = em.merge(messageTypesMessageTypeId);
            }
            em.remove(recErrorFileMsg);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RecErrorFileMsg> findRecErrorFileMsgEntities() {
        return findRecErrorFileMsgEntities(true, -1, -1);
    }

    public List<RecErrorFileMsg> findRecErrorFileMsgEntities(int maxResults, int firstResult) {
        return findRecErrorFileMsgEntities(false, maxResults, firstResult);
    }

    private List<RecErrorFileMsg> findRecErrorFileMsgEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RecErrorFileMsg.class));
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

    public RecErrorFileMsg findRecErrorFileMsg(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RecErrorFileMsg.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecErrorFileMsgCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RecErrorFileMsg> rt = cq.from(RecErrorFileMsg.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
