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
import org.kephis.ecs_kesws.entities.RecErrorFileMsg;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.kephis.ecs_kesws.entities.ResCdFileMsg;
import org.kephis.ecs_kesws.entities.RecCdFileMsg;
import org.kephis.ecs_kesws.entities.EcsResCdFileMsg;
import org.kephis.ecs_kesws.entities.MessageTypes;
import org.kephis.ecs_kesws.entities.controllers.exceptions.IllegalOrphanException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.PreexistingEntityException;

/**
 *
 * @author kim
 */
public class MessageTypesJpaController implements Serializable {

    public MessageTypesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MessageTypes messageTypes) throws PreexistingEntityException, Exception {
        if (messageTypes.getRecErrorFileMsgCollection() == null) {
            messageTypes.setRecErrorFileMsgCollection(new ArrayList<RecErrorFileMsg>());
        }
        if (messageTypes.getResCdFileMsgCollection() == null) {
            messageTypes.setResCdFileMsgCollection(new ArrayList<ResCdFileMsg>());
        }
        if (messageTypes.getRecCdFileMsgCollection() == null) {
            messageTypes.setRecCdFileMsgCollection(new ArrayList<RecCdFileMsg>());
        }
        if (messageTypes.getEcsResCdFileMsgCollection() == null) {
            messageTypes.setEcsResCdFileMsgCollection(new ArrayList<EcsResCdFileMsg>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<RecErrorFileMsg> attachedRecErrorFileMsgCollection = new ArrayList<RecErrorFileMsg>();
            for (RecErrorFileMsg recErrorFileMsgCollectionRecErrorFileMsgToAttach : messageTypes.getRecErrorFileMsgCollection()) {
                recErrorFileMsgCollectionRecErrorFileMsgToAttach = em.getReference(recErrorFileMsgCollectionRecErrorFileMsgToAttach.getClass(), recErrorFileMsgCollectionRecErrorFileMsgToAttach.getRecErrorMsgId());
                attachedRecErrorFileMsgCollection.add(recErrorFileMsgCollectionRecErrorFileMsgToAttach);
            }
            messageTypes.setRecErrorFileMsgCollection(attachedRecErrorFileMsgCollection);
            Collection<ResCdFileMsg> attachedResCdFileMsgCollection = new ArrayList<ResCdFileMsg>();
            for (ResCdFileMsg resCdFileMsgCollectionResCdFileMsgToAttach : messageTypes.getResCdFileMsgCollection()) {
                resCdFileMsgCollectionResCdFileMsgToAttach = em.getReference(resCdFileMsgCollectionResCdFileMsgToAttach.getClass(), resCdFileMsgCollectionResCdFileMsgToAttach.getResCdFileId());
                attachedResCdFileMsgCollection.add(resCdFileMsgCollectionResCdFileMsgToAttach);
            }
            messageTypes.setResCdFileMsgCollection(attachedResCdFileMsgCollection);
            Collection<RecCdFileMsg> attachedRecCdFileMsgCollection = new ArrayList<RecCdFileMsg>();
            for (RecCdFileMsg recCdFileMsgCollectionRecCdFileMsgToAttach : messageTypes.getRecCdFileMsgCollection()) {
                recCdFileMsgCollectionRecCdFileMsgToAttach = em.getReference(recCdFileMsgCollectionRecCdFileMsgToAttach.getClass(), recCdFileMsgCollectionRecCdFileMsgToAttach.getRECCDFileID());
                attachedRecCdFileMsgCollection.add(recCdFileMsgCollectionRecCdFileMsgToAttach);
            }
            messageTypes.setRecCdFileMsgCollection(attachedRecCdFileMsgCollection);
            Collection<EcsResCdFileMsg> attachedEcsResCdFileMsgCollection = new ArrayList<EcsResCdFileMsg>();
            for (EcsResCdFileMsg ecsResCdFileMsgCollectionEcsResCdFileMsgToAttach : messageTypes.getEcsResCdFileMsgCollection()) {
                ecsResCdFileMsgCollectionEcsResCdFileMsgToAttach = em.getReference(ecsResCdFileMsgCollectionEcsResCdFileMsgToAttach.getClass(), ecsResCdFileMsgCollectionEcsResCdFileMsgToAttach.getRECCDFileID());
                attachedEcsResCdFileMsgCollection.add(ecsResCdFileMsgCollectionEcsResCdFileMsgToAttach);
            }
            messageTypes.setEcsResCdFileMsgCollection(attachedEcsResCdFileMsgCollection);
            em.persist(messageTypes);
            for (RecErrorFileMsg recErrorFileMsgCollectionRecErrorFileMsg : messageTypes.getRecErrorFileMsgCollection()) {
                MessageTypes oldMessageTypesMessageTypeIdOfRecErrorFileMsgCollectionRecErrorFileMsg = recErrorFileMsgCollectionRecErrorFileMsg.getMessageTypesMessageTypeId();
                recErrorFileMsgCollectionRecErrorFileMsg.setMessageTypesMessageTypeId(messageTypes);
                recErrorFileMsgCollectionRecErrorFileMsg = em.merge(recErrorFileMsgCollectionRecErrorFileMsg);
                if (oldMessageTypesMessageTypeIdOfRecErrorFileMsgCollectionRecErrorFileMsg != null) {
                    oldMessageTypesMessageTypeIdOfRecErrorFileMsgCollectionRecErrorFileMsg.getRecErrorFileMsgCollection().remove(recErrorFileMsgCollectionRecErrorFileMsg);
                    oldMessageTypesMessageTypeIdOfRecErrorFileMsgCollectionRecErrorFileMsg = em.merge(oldMessageTypesMessageTypeIdOfRecErrorFileMsgCollectionRecErrorFileMsg);
                }
            }
            for (ResCdFileMsg resCdFileMsgCollectionResCdFileMsg : messageTypes.getResCdFileMsgCollection()) {
                MessageTypes oldMessageTypesMessageTypeIdOfResCdFileMsgCollectionResCdFileMsg = resCdFileMsgCollectionResCdFileMsg.getMessageTypesMessageTypeId();
                resCdFileMsgCollectionResCdFileMsg.setMessageTypesMessageTypeId(messageTypes);
                resCdFileMsgCollectionResCdFileMsg = em.merge(resCdFileMsgCollectionResCdFileMsg);
                if (oldMessageTypesMessageTypeIdOfResCdFileMsgCollectionResCdFileMsg != null) {
                    oldMessageTypesMessageTypeIdOfResCdFileMsgCollectionResCdFileMsg.getResCdFileMsgCollection().remove(resCdFileMsgCollectionResCdFileMsg);
                    oldMessageTypesMessageTypeIdOfResCdFileMsgCollectionResCdFileMsg = em.merge(oldMessageTypesMessageTypeIdOfResCdFileMsgCollectionResCdFileMsg);
                }
            }
            for (RecCdFileMsg recCdFileMsgCollectionRecCdFileMsg : messageTypes.getRecCdFileMsgCollection()) {
                MessageTypes oldMessageTypesMessageTypeIdOfRecCdFileMsgCollectionRecCdFileMsg = recCdFileMsgCollectionRecCdFileMsg.getMessageTypesMessageTypeId();
                recCdFileMsgCollectionRecCdFileMsg.setMessageTypesMessageTypeId(messageTypes);
                recCdFileMsgCollectionRecCdFileMsg = em.merge(recCdFileMsgCollectionRecCdFileMsg);
                if (oldMessageTypesMessageTypeIdOfRecCdFileMsgCollectionRecCdFileMsg != null) {
                    oldMessageTypesMessageTypeIdOfRecCdFileMsgCollectionRecCdFileMsg.getRecCdFileMsgCollection().remove(recCdFileMsgCollectionRecCdFileMsg);
                    oldMessageTypesMessageTypeIdOfRecCdFileMsgCollectionRecCdFileMsg = em.merge(oldMessageTypesMessageTypeIdOfRecCdFileMsgCollectionRecCdFileMsg);
                }
            }
            for (EcsResCdFileMsg ecsResCdFileMsgCollectionEcsResCdFileMsg : messageTypes.getEcsResCdFileMsgCollection()) {
                MessageTypes oldMessageTypesMessageTypeIdOfEcsResCdFileMsgCollectionEcsResCdFileMsg = ecsResCdFileMsgCollectionEcsResCdFileMsg.getMessageTypesMessageTypeId();
                ecsResCdFileMsgCollectionEcsResCdFileMsg.setMessageTypesMessageTypeId(messageTypes);
                ecsResCdFileMsgCollectionEcsResCdFileMsg = em.merge(ecsResCdFileMsgCollectionEcsResCdFileMsg);
                if (oldMessageTypesMessageTypeIdOfEcsResCdFileMsgCollectionEcsResCdFileMsg != null) {
                    oldMessageTypesMessageTypeIdOfEcsResCdFileMsgCollectionEcsResCdFileMsg.getEcsResCdFileMsgCollection().remove(ecsResCdFileMsgCollectionEcsResCdFileMsg);
                    oldMessageTypesMessageTypeIdOfEcsResCdFileMsgCollectionEcsResCdFileMsg = em.merge(oldMessageTypesMessageTypeIdOfEcsResCdFileMsgCollectionEcsResCdFileMsg);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMessageTypes(messageTypes.getMessageTypeId()) != null) {
                throw new PreexistingEntityException("MessageTypes " + messageTypes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MessageTypes messageTypes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MessageTypes persistentMessageTypes = em.find(MessageTypes.class, messageTypes.getMessageTypeId());
            Collection<RecErrorFileMsg> recErrorFileMsgCollectionOld = persistentMessageTypes.getRecErrorFileMsgCollection();
            Collection<RecErrorFileMsg> recErrorFileMsgCollectionNew = messageTypes.getRecErrorFileMsgCollection();
            Collection<ResCdFileMsg> resCdFileMsgCollectionOld = persistentMessageTypes.getResCdFileMsgCollection();
            Collection<ResCdFileMsg> resCdFileMsgCollectionNew = messageTypes.getResCdFileMsgCollection();
            Collection<RecCdFileMsg> recCdFileMsgCollectionOld = persistentMessageTypes.getRecCdFileMsgCollection();
            Collection<RecCdFileMsg> recCdFileMsgCollectionNew = messageTypes.getRecCdFileMsgCollection();
            Collection<EcsResCdFileMsg> ecsResCdFileMsgCollectionOld = persistentMessageTypes.getEcsResCdFileMsgCollection();
            Collection<EcsResCdFileMsg> ecsResCdFileMsgCollectionNew = messageTypes.getEcsResCdFileMsgCollection();
            List<String> illegalOrphanMessages = null;
            for (RecErrorFileMsg recErrorFileMsgCollectionOldRecErrorFileMsg : recErrorFileMsgCollectionOld) {
                if (!recErrorFileMsgCollectionNew.contains(recErrorFileMsgCollectionOldRecErrorFileMsg)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecErrorFileMsg " + recErrorFileMsgCollectionOldRecErrorFileMsg + " since its messageTypesMessageTypeId field is not nullable.");
                }
            }
            for (ResCdFileMsg resCdFileMsgCollectionOldResCdFileMsg : resCdFileMsgCollectionOld) {
                if (!resCdFileMsgCollectionNew.contains(resCdFileMsgCollectionOldResCdFileMsg)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ResCdFileMsg " + resCdFileMsgCollectionOldResCdFileMsg + " since its messageTypesMessageTypeId field is not nullable.");
                }
            }
            for (RecCdFileMsg recCdFileMsgCollectionOldRecCdFileMsg : recCdFileMsgCollectionOld) {
                if (!recCdFileMsgCollectionNew.contains(recCdFileMsgCollectionOldRecCdFileMsg)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecCdFileMsg " + recCdFileMsgCollectionOldRecCdFileMsg + " since its messageTypesMessageTypeId field is not nullable.");
                }
            }
            for (EcsResCdFileMsg ecsResCdFileMsgCollectionOldEcsResCdFileMsg : ecsResCdFileMsgCollectionOld) {
                if (!ecsResCdFileMsgCollectionNew.contains(ecsResCdFileMsgCollectionOldEcsResCdFileMsg)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EcsResCdFileMsg " + ecsResCdFileMsgCollectionOldEcsResCdFileMsg + " since its messageTypesMessageTypeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<RecErrorFileMsg> attachedRecErrorFileMsgCollectionNew = new ArrayList<RecErrorFileMsg>();
            for (RecErrorFileMsg recErrorFileMsgCollectionNewRecErrorFileMsgToAttach : recErrorFileMsgCollectionNew) {
                recErrorFileMsgCollectionNewRecErrorFileMsgToAttach = em.getReference(recErrorFileMsgCollectionNewRecErrorFileMsgToAttach.getClass(), recErrorFileMsgCollectionNewRecErrorFileMsgToAttach.getRecErrorMsgId());
                attachedRecErrorFileMsgCollectionNew.add(recErrorFileMsgCollectionNewRecErrorFileMsgToAttach);
            }
            recErrorFileMsgCollectionNew = attachedRecErrorFileMsgCollectionNew;
            messageTypes.setRecErrorFileMsgCollection(recErrorFileMsgCollectionNew);
            Collection<ResCdFileMsg> attachedResCdFileMsgCollectionNew = new ArrayList<ResCdFileMsg>();
            for (ResCdFileMsg resCdFileMsgCollectionNewResCdFileMsgToAttach : resCdFileMsgCollectionNew) {
                resCdFileMsgCollectionNewResCdFileMsgToAttach = em.getReference(resCdFileMsgCollectionNewResCdFileMsgToAttach.getClass(), resCdFileMsgCollectionNewResCdFileMsgToAttach.getResCdFileId());
                attachedResCdFileMsgCollectionNew.add(resCdFileMsgCollectionNewResCdFileMsgToAttach);
            }
            resCdFileMsgCollectionNew = attachedResCdFileMsgCollectionNew;
            messageTypes.setResCdFileMsgCollection(resCdFileMsgCollectionNew);
            Collection<RecCdFileMsg> attachedRecCdFileMsgCollectionNew = new ArrayList<RecCdFileMsg>();
            for (RecCdFileMsg recCdFileMsgCollectionNewRecCdFileMsgToAttach : recCdFileMsgCollectionNew) {
                recCdFileMsgCollectionNewRecCdFileMsgToAttach = em.getReference(recCdFileMsgCollectionNewRecCdFileMsgToAttach.getClass(), recCdFileMsgCollectionNewRecCdFileMsgToAttach.getRECCDFileID());
                attachedRecCdFileMsgCollectionNew.add(recCdFileMsgCollectionNewRecCdFileMsgToAttach);
            }
            recCdFileMsgCollectionNew = attachedRecCdFileMsgCollectionNew;
            messageTypes.setRecCdFileMsgCollection(recCdFileMsgCollectionNew);
            Collection<EcsResCdFileMsg> attachedEcsResCdFileMsgCollectionNew = new ArrayList<EcsResCdFileMsg>();
            for (EcsResCdFileMsg ecsResCdFileMsgCollectionNewEcsResCdFileMsgToAttach : ecsResCdFileMsgCollectionNew) {
                ecsResCdFileMsgCollectionNewEcsResCdFileMsgToAttach = em.getReference(ecsResCdFileMsgCollectionNewEcsResCdFileMsgToAttach.getClass(), ecsResCdFileMsgCollectionNewEcsResCdFileMsgToAttach.getRECCDFileID());
                attachedEcsResCdFileMsgCollectionNew.add(ecsResCdFileMsgCollectionNewEcsResCdFileMsgToAttach);
            }
            ecsResCdFileMsgCollectionNew = attachedEcsResCdFileMsgCollectionNew;
            messageTypes.setEcsResCdFileMsgCollection(ecsResCdFileMsgCollectionNew);
            messageTypes = em.merge(messageTypes);
            for (RecErrorFileMsg recErrorFileMsgCollectionNewRecErrorFileMsg : recErrorFileMsgCollectionNew) {
                if (!recErrorFileMsgCollectionOld.contains(recErrorFileMsgCollectionNewRecErrorFileMsg)) {
                    MessageTypes oldMessageTypesMessageTypeIdOfRecErrorFileMsgCollectionNewRecErrorFileMsg = recErrorFileMsgCollectionNewRecErrorFileMsg.getMessageTypesMessageTypeId();
                    recErrorFileMsgCollectionNewRecErrorFileMsg.setMessageTypesMessageTypeId(messageTypes);
                    recErrorFileMsgCollectionNewRecErrorFileMsg = em.merge(recErrorFileMsgCollectionNewRecErrorFileMsg);
                    if (oldMessageTypesMessageTypeIdOfRecErrorFileMsgCollectionNewRecErrorFileMsg != null && !oldMessageTypesMessageTypeIdOfRecErrorFileMsgCollectionNewRecErrorFileMsg.equals(messageTypes)) {
                        oldMessageTypesMessageTypeIdOfRecErrorFileMsgCollectionNewRecErrorFileMsg.getRecErrorFileMsgCollection().remove(recErrorFileMsgCollectionNewRecErrorFileMsg);
                        oldMessageTypesMessageTypeIdOfRecErrorFileMsgCollectionNewRecErrorFileMsg = em.merge(oldMessageTypesMessageTypeIdOfRecErrorFileMsgCollectionNewRecErrorFileMsg);
                    }
                }
            }
            for (ResCdFileMsg resCdFileMsgCollectionNewResCdFileMsg : resCdFileMsgCollectionNew) {
                if (!resCdFileMsgCollectionOld.contains(resCdFileMsgCollectionNewResCdFileMsg)) {
                    MessageTypes oldMessageTypesMessageTypeIdOfResCdFileMsgCollectionNewResCdFileMsg = resCdFileMsgCollectionNewResCdFileMsg.getMessageTypesMessageTypeId();
                    resCdFileMsgCollectionNewResCdFileMsg.setMessageTypesMessageTypeId(messageTypes);
                    resCdFileMsgCollectionNewResCdFileMsg = em.merge(resCdFileMsgCollectionNewResCdFileMsg);
                    if (oldMessageTypesMessageTypeIdOfResCdFileMsgCollectionNewResCdFileMsg != null && !oldMessageTypesMessageTypeIdOfResCdFileMsgCollectionNewResCdFileMsg.equals(messageTypes)) {
                        oldMessageTypesMessageTypeIdOfResCdFileMsgCollectionNewResCdFileMsg.getResCdFileMsgCollection().remove(resCdFileMsgCollectionNewResCdFileMsg);
                        oldMessageTypesMessageTypeIdOfResCdFileMsgCollectionNewResCdFileMsg = em.merge(oldMessageTypesMessageTypeIdOfResCdFileMsgCollectionNewResCdFileMsg);
                    }
                }
            }
            for (RecCdFileMsg recCdFileMsgCollectionNewRecCdFileMsg : recCdFileMsgCollectionNew) {
                if (!recCdFileMsgCollectionOld.contains(recCdFileMsgCollectionNewRecCdFileMsg)) {
                    MessageTypes oldMessageTypesMessageTypeIdOfRecCdFileMsgCollectionNewRecCdFileMsg = recCdFileMsgCollectionNewRecCdFileMsg.getMessageTypesMessageTypeId();
                    recCdFileMsgCollectionNewRecCdFileMsg.setMessageTypesMessageTypeId(messageTypes);
                    recCdFileMsgCollectionNewRecCdFileMsg = em.merge(recCdFileMsgCollectionNewRecCdFileMsg);
                    if (oldMessageTypesMessageTypeIdOfRecCdFileMsgCollectionNewRecCdFileMsg != null && !oldMessageTypesMessageTypeIdOfRecCdFileMsgCollectionNewRecCdFileMsg.equals(messageTypes)) {
                        oldMessageTypesMessageTypeIdOfRecCdFileMsgCollectionNewRecCdFileMsg.getRecCdFileMsgCollection().remove(recCdFileMsgCollectionNewRecCdFileMsg);
                        oldMessageTypesMessageTypeIdOfRecCdFileMsgCollectionNewRecCdFileMsg = em.merge(oldMessageTypesMessageTypeIdOfRecCdFileMsgCollectionNewRecCdFileMsg);
                    }
                }
            }
            for (EcsResCdFileMsg ecsResCdFileMsgCollectionNewEcsResCdFileMsg : ecsResCdFileMsgCollectionNew) {
                if (!ecsResCdFileMsgCollectionOld.contains(ecsResCdFileMsgCollectionNewEcsResCdFileMsg)) {
                    MessageTypes oldMessageTypesMessageTypeIdOfEcsResCdFileMsgCollectionNewEcsResCdFileMsg = ecsResCdFileMsgCollectionNewEcsResCdFileMsg.getMessageTypesMessageTypeId();
                    ecsResCdFileMsgCollectionNewEcsResCdFileMsg.setMessageTypesMessageTypeId(messageTypes);
                    ecsResCdFileMsgCollectionNewEcsResCdFileMsg = em.merge(ecsResCdFileMsgCollectionNewEcsResCdFileMsg);
                    if (oldMessageTypesMessageTypeIdOfEcsResCdFileMsgCollectionNewEcsResCdFileMsg != null && !oldMessageTypesMessageTypeIdOfEcsResCdFileMsgCollectionNewEcsResCdFileMsg.equals(messageTypes)) {
                        oldMessageTypesMessageTypeIdOfEcsResCdFileMsgCollectionNewEcsResCdFileMsg.getEcsResCdFileMsgCollection().remove(ecsResCdFileMsgCollectionNewEcsResCdFileMsg);
                        oldMessageTypesMessageTypeIdOfEcsResCdFileMsgCollectionNewEcsResCdFileMsg = em.merge(oldMessageTypesMessageTypeIdOfEcsResCdFileMsgCollectionNewEcsResCdFileMsg);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = messageTypes.getMessageTypeId();
                if (findMessageTypes(id) == null) {
                    throw new NonexistentEntityException("The messageTypes with id " + id + " no longer exists.");
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
            MessageTypes messageTypes;
            try {
                messageTypes = em.getReference(MessageTypes.class, id);
                messageTypes.getMessageTypeId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The messageTypes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<RecErrorFileMsg> recErrorFileMsgCollectionOrphanCheck = messageTypes.getRecErrorFileMsgCollection();
            for (RecErrorFileMsg recErrorFileMsgCollectionOrphanCheckRecErrorFileMsg : recErrorFileMsgCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MessageTypes (" + messageTypes + ") cannot be destroyed since the RecErrorFileMsg " + recErrorFileMsgCollectionOrphanCheckRecErrorFileMsg + " in its recErrorFileMsgCollection field has a non-nullable messageTypesMessageTypeId field.");
            }
            Collection<ResCdFileMsg> resCdFileMsgCollectionOrphanCheck = messageTypes.getResCdFileMsgCollection();
            for (ResCdFileMsg resCdFileMsgCollectionOrphanCheckResCdFileMsg : resCdFileMsgCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MessageTypes (" + messageTypes + ") cannot be destroyed since the ResCdFileMsg " + resCdFileMsgCollectionOrphanCheckResCdFileMsg + " in its resCdFileMsgCollection field has a non-nullable messageTypesMessageTypeId field.");
            }
            Collection<RecCdFileMsg> recCdFileMsgCollectionOrphanCheck = messageTypes.getRecCdFileMsgCollection();
            for (RecCdFileMsg recCdFileMsgCollectionOrphanCheckRecCdFileMsg : recCdFileMsgCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MessageTypes (" + messageTypes + ") cannot be destroyed since the RecCdFileMsg " + recCdFileMsgCollectionOrphanCheckRecCdFileMsg + " in its recCdFileMsgCollection field has a non-nullable messageTypesMessageTypeId field.");
            }
            Collection<EcsResCdFileMsg> ecsResCdFileMsgCollectionOrphanCheck = messageTypes.getEcsResCdFileMsgCollection();
            for (EcsResCdFileMsg ecsResCdFileMsgCollectionOrphanCheckEcsResCdFileMsg : ecsResCdFileMsgCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MessageTypes (" + messageTypes + ") cannot be destroyed since the EcsResCdFileMsg " + ecsResCdFileMsgCollectionOrphanCheckEcsResCdFileMsg + " in its ecsResCdFileMsgCollection field has a non-nullable messageTypesMessageTypeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(messageTypes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MessageTypes> findMessageTypesEntities() {
        return findMessageTypesEntities(true, -1, -1);
    }

    public List<MessageTypes> findMessageTypesEntities(int maxResults, int firstResult) {
        return findMessageTypesEntities(false, maxResults, firstResult);
    }

    private List<MessageTypes> findMessageTypesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MessageTypes.class));
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

    public MessageTypes findMessageTypes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MessageTypes.class, id);
        } finally {
            em.close();
        }
    }

    public int getMessageTypesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MessageTypes> rt = cq.from(MessageTypes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
