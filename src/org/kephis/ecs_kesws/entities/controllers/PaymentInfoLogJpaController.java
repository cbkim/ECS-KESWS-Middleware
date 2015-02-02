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
import org.kephis.ecs_kesws.entities.PaymentInfoLog;
import org.kephis.ecs_kesws.entities.RecPaymentFileMsg;
import org.kephis.ecs_kesws.entities.ResCdFileMsg;
import org.kephis.ecs_kesws.entities.PricelistInternalProductcodeDocumentMap;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author kim
 */
public class PaymentInfoLogJpaController implements Serializable {

    public PaymentInfoLogJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PaymentInfoLog paymentInfoLog) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecPaymentFileMsg recPaymentFileId = paymentInfoLog.getRecPaymentFileId();
            if (recPaymentFileId != null) {
                recPaymentFileId = em.getReference(recPaymentFileId.getClass(), recPaymentFileId.getReceivedPaymentMsgId());
                paymentInfoLog.setRecPaymentFileId(recPaymentFileId);
            }
            ResCdFileMsg resCdFileMsgResCdFileId = paymentInfoLog.getResCdFileMsgResCdFileId();
            if (resCdFileMsgResCdFileId != null) {
                resCdFileMsgResCdFileId = em.getReference(resCdFileMsgResCdFileId.getClass(), resCdFileMsgResCdFileId.getResCdFileId());
                paymentInfoLog.setResCdFileMsgResCdFileId(resCdFileMsgResCdFileId);
            }
            PricelistInternalProductcodeDocumentMap PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID = paymentInfoLog.getPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID();
            if (PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID != null) {
                PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID = em.getReference(PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID.getClass(), PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID.getPricelistIPCMAPID());
                paymentInfoLog.setPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID(PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID);
            }
            em.persist(paymentInfoLog);
            if (recPaymentFileId != null) {
                recPaymentFileId.getPaymentInfoLogCollection().add(paymentInfoLog);
                recPaymentFileId = em.merge(recPaymentFileId);
            }
            if (resCdFileMsgResCdFileId != null) {
                resCdFileMsgResCdFileId.getPaymentInfoLogCollection().add(paymentInfoLog);
                resCdFileMsgResCdFileId = em.merge(resCdFileMsgResCdFileId);
            }
            if (PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID != null) {
                PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID.getPaymentInfoLogCollection().add(paymentInfoLog);
                PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID = em.merge(PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PaymentInfoLog paymentInfoLog) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PaymentInfoLog persistentPaymentInfoLog = em.find(PaymentInfoLog.class, paymentInfoLog.getPayementMsgId());
            RecPaymentFileMsg recPaymentFileIdOld = persistentPaymentInfoLog.getRecPaymentFileId();
            RecPaymentFileMsg recPaymentFileIdNew = paymentInfoLog.getRecPaymentFileId();
            ResCdFileMsg resCdFileMsgResCdFileIdOld = persistentPaymentInfoLog.getResCdFileMsgResCdFileId();
            ResCdFileMsg resCdFileMsgResCdFileIdNew = paymentInfoLog.getResCdFileMsgResCdFileId();
            PricelistInternalProductcodeDocumentMap PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOld = persistentPaymentInfoLog.getPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID();
            PricelistInternalProductcodeDocumentMap PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDNew = paymentInfoLog.getPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID();
            if (recPaymentFileIdNew != null) {
                recPaymentFileIdNew = em.getReference(recPaymentFileIdNew.getClass(), recPaymentFileIdNew.getReceivedPaymentMsgId());
                paymentInfoLog.setRecPaymentFileId(recPaymentFileIdNew);
            }
            if (resCdFileMsgResCdFileIdNew != null) {
                resCdFileMsgResCdFileIdNew = em.getReference(resCdFileMsgResCdFileIdNew.getClass(), resCdFileMsgResCdFileIdNew.getResCdFileId());
                paymentInfoLog.setResCdFileMsgResCdFileId(resCdFileMsgResCdFileIdNew);
            }
            if (PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDNew != null) {
                PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDNew = em.getReference(PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDNew.getClass(), PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDNew.getPricelistIPCMAPID());
                paymentInfoLog.setPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID(PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDNew);
            }
            paymentInfoLog = em.merge(paymentInfoLog);
            if (recPaymentFileIdOld != null && !recPaymentFileIdOld.equals(recPaymentFileIdNew)) {
                recPaymentFileIdOld.getPaymentInfoLogCollection().remove(paymentInfoLog);
                recPaymentFileIdOld = em.merge(recPaymentFileIdOld);
            }
            if (recPaymentFileIdNew != null && !recPaymentFileIdNew.equals(recPaymentFileIdOld)) {
                recPaymentFileIdNew.getPaymentInfoLogCollection().add(paymentInfoLog);
                recPaymentFileIdNew = em.merge(recPaymentFileIdNew);
            }
            if (resCdFileMsgResCdFileIdOld != null && !resCdFileMsgResCdFileIdOld.equals(resCdFileMsgResCdFileIdNew)) {
                resCdFileMsgResCdFileIdOld.getPaymentInfoLogCollection().remove(paymentInfoLog);
                resCdFileMsgResCdFileIdOld = em.merge(resCdFileMsgResCdFileIdOld);
            }
            if (resCdFileMsgResCdFileIdNew != null && !resCdFileMsgResCdFileIdNew.equals(resCdFileMsgResCdFileIdOld)) {
                resCdFileMsgResCdFileIdNew.getPaymentInfoLogCollection().add(paymentInfoLog);
                resCdFileMsgResCdFileIdNew = em.merge(resCdFileMsgResCdFileIdNew);
            }
            if (PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOld != null && !PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOld.equals(PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDNew)) {
                PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOld.getPaymentInfoLogCollection().remove(paymentInfoLog);
                PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOld = em.merge(PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOld);
            }
            if (PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDNew != null && !PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDNew.equals(PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOld)) {
                PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDNew.getPaymentInfoLogCollection().add(paymentInfoLog);
                PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDNew = em.merge(PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = paymentInfoLog.getPayementMsgId();
                if (findPaymentInfoLog(id) == null) {
                    throw new NonexistentEntityException("The paymentInfoLog with id " + id + " no longer exists.");
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
            PaymentInfoLog paymentInfoLog;
            try {
                paymentInfoLog = em.getReference(PaymentInfoLog.class, id);
                paymentInfoLog.getPayementMsgId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paymentInfoLog with id " + id + " no longer exists.", enfe);
            }
            RecPaymentFileMsg recPaymentFileId = paymentInfoLog.getRecPaymentFileId();
            if (recPaymentFileId != null) {
                recPaymentFileId.getPaymentInfoLogCollection().remove(paymentInfoLog);
                recPaymentFileId = em.merge(recPaymentFileId);
            }
            ResCdFileMsg resCdFileMsgResCdFileId = paymentInfoLog.getResCdFileMsgResCdFileId();
            if (resCdFileMsgResCdFileId != null) {
                resCdFileMsgResCdFileId.getPaymentInfoLogCollection().remove(paymentInfoLog);
                resCdFileMsgResCdFileId = em.merge(resCdFileMsgResCdFileId);
            }
            PricelistInternalProductcodeDocumentMap PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID = paymentInfoLog.getPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID();
            if (PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID != null) {
                PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID.getPaymentInfoLogCollection().remove(paymentInfoLog);
                PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID = em.merge(PRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID);
            }
            em.remove(paymentInfoLog);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PaymentInfoLog> findPaymentInfoLogEntities() {
        return findPaymentInfoLogEntities(true, -1, -1);
    }

    public List<PaymentInfoLog> findPaymentInfoLogEntities(int maxResults, int firstResult) {
        return findPaymentInfoLogEntities(false, maxResults, firstResult);
    }

    private List<PaymentInfoLog> findPaymentInfoLogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PaymentInfoLog.class));
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

    public PaymentInfoLog findPaymentInfoLog(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PaymentInfoLog.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaymentInfoLogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PaymentInfoLog> rt = cq.from(PaymentInfoLog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
