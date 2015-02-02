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
import org.kephis.ecs_kesws.entities.Pricelist;
import org.kephis.ecs_kesws.entities.InternalProductcodes;
import org.kephis.ecs_kesws.entities.EcsDocumentTypes;
import org.kephis.ecs_kesws.entities.PaymentInfoLog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.kephis.ecs_kesws.entities.CdFileDetails;
import org.kephis.ecs_kesws.entities.PricelistInternalProductcodeDocumentMap;
import org.kephis.ecs_kesws.entities.controllers.exceptions.IllegalOrphanException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author kim
 */
public class PricelistInternalProductcodeDocumentMapJpaController implements Serializable {

    public PricelistInternalProductcodeDocumentMapJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMap) {
        if (pricelistInternalProductcodeDocumentMap.getPaymentInfoLogCollection() == null) {
            pricelistInternalProductcodeDocumentMap.setPaymentInfoLogCollection(new ArrayList<PaymentInfoLog>());
        }
        if (pricelistInternalProductcodeDocumentMap.getCdFileDetailsCollection() == null) {
            pricelistInternalProductcodeDocumentMap.setCdFileDetailsCollection(new ArrayList<CdFileDetails>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pricelist PRICELISTPriceIDRef = pricelistInternalProductcodeDocumentMap.getPRICELISTPriceIDRef();
            if (PRICELISTPriceIDRef != null) {
                PRICELISTPriceIDRef = em.getReference(PRICELISTPriceIDRef.getClass(), PRICELISTPriceIDRef.getPriceID());
                pricelistInternalProductcodeDocumentMap.setPRICELISTPriceIDRef(PRICELISTPriceIDRef);
            }
            InternalProductcodes INTERNALPRODUCTCODESIPCIDRef = pricelistInternalProductcodeDocumentMap.getINTERNALPRODUCTCODESIPCIDRef();
            if (INTERNALPRODUCTCODESIPCIDRef != null) {
                INTERNALPRODUCTCODESIPCIDRef = em.getReference(INTERNALPRODUCTCODESIPCIDRef.getClass(), INTERNALPRODUCTCODESIPCIDRef.getIpcId());
                pricelistInternalProductcodeDocumentMap.setINTERNALPRODUCTCODESIPCIDRef(INTERNALPRODUCTCODESIPCIDRef);
            }
            EcsDocumentTypes DOCUMENTTYPESid = pricelistInternalProductcodeDocumentMap.getDOCUMENTTYPESid();
            if (DOCUMENTTYPESid != null) {
                DOCUMENTTYPESid = em.getReference(DOCUMENTTYPESid.getClass(), DOCUMENTTYPESid.getId());
                pricelistInternalProductcodeDocumentMap.setDOCUMENTTYPESid(DOCUMENTTYPESid);
            }
            Collection<PaymentInfoLog> attachedPaymentInfoLogCollection = new ArrayList<PaymentInfoLog>();
            for (PaymentInfoLog paymentInfoLogCollectionPaymentInfoLogToAttach : pricelistInternalProductcodeDocumentMap.getPaymentInfoLogCollection()) {
                paymentInfoLogCollectionPaymentInfoLogToAttach = em.getReference(paymentInfoLogCollectionPaymentInfoLogToAttach.getClass(), paymentInfoLogCollectionPaymentInfoLogToAttach.getPayementMsgId());
                attachedPaymentInfoLogCollection.add(paymentInfoLogCollectionPaymentInfoLogToAttach);
            }
            pricelistInternalProductcodeDocumentMap.setPaymentInfoLogCollection(attachedPaymentInfoLogCollection);
            Collection<CdFileDetails> attachedCdFileDetailsCollection = new ArrayList<CdFileDetails>();
            for (CdFileDetails cdFileDetailsCollectionCdFileDetailsToAttach : pricelistInternalProductcodeDocumentMap.getCdFileDetailsCollection()) {
                cdFileDetailsCollectionCdFileDetailsToAttach = em.getReference(cdFileDetailsCollectionCdFileDetailsToAttach.getClass(), cdFileDetailsCollectionCdFileDetailsToAttach.getId());
                attachedCdFileDetailsCollection.add(cdFileDetailsCollectionCdFileDetailsToAttach);
            }
            pricelistInternalProductcodeDocumentMap.setCdFileDetailsCollection(attachedCdFileDetailsCollection);
            em.persist(pricelistInternalProductcodeDocumentMap);
            if (PRICELISTPriceIDRef != null) {
                PRICELISTPriceIDRef.getPricelistInternalProductcodeDocumentMapCollection().add(pricelistInternalProductcodeDocumentMap);
                PRICELISTPriceIDRef = em.merge(PRICELISTPriceIDRef);
            }
            if (INTERNALPRODUCTCODESIPCIDRef != null) {
                INTERNALPRODUCTCODESIPCIDRef.getPricelistInternalProductcodeDocumentMapCollection().add(pricelistInternalProductcodeDocumentMap);
                INTERNALPRODUCTCODESIPCIDRef = em.merge(INTERNALPRODUCTCODESIPCIDRef);
            }
            if (DOCUMENTTYPESid != null) {
                DOCUMENTTYPESid.getPricelistInternalProductcodeDocumentMapCollection().add(pricelistInternalProductcodeDocumentMap);
                DOCUMENTTYPESid = em.merge(DOCUMENTTYPESid);
            }
            for (PaymentInfoLog paymentInfoLogCollectionPaymentInfoLog : pricelistInternalProductcodeDocumentMap.getPaymentInfoLogCollection()) {
                PricelistInternalProductcodeDocumentMap oldPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOfPaymentInfoLogCollectionPaymentInfoLog = paymentInfoLogCollectionPaymentInfoLog.getPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID();
                paymentInfoLogCollectionPaymentInfoLog.setPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID(pricelistInternalProductcodeDocumentMap);
                paymentInfoLogCollectionPaymentInfoLog = em.merge(paymentInfoLogCollectionPaymentInfoLog);
                if (oldPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOfPaymentInfoLogCollectionPaymentInfoLog != null) {
                    oldPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOfPaymentInfoLogCollectionPaymentInfoLog.getPaymentInfoLogCollection().remove(paymentInfoLogCollectionPaymentInfoLog);
                    oldPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOfPaymentInfoLogCollectionPaymentInfoLog = em.merge(oldPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOfPaymentInfoLogCollectionPaymentInfoLog);
                }
            }
            for (CdFileDetails cdFileDetailsCollectionCdFileDetails : pricelistInternalProductcodeDocumentMap.getCdFileDetailsCollection()) {
                PricelistInternalProductcodeDocumentMap oldPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOfCdFileDetailsCollectionCdFileDetails = cdFileDetailsCollectionCdFileDetails.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef();
                cdFileDetailsCollectionCdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(pricelistInternalProductcodeDocumentMap);
                cdFileDetailsCollectionCdFileDetails = em.merge(cdFileDetailsCollectionCdFileDetails);
                if (oldPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOfCdFileDetailsCollectionCdFileDetails != null) {
                    oldPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOfCdFileDetailsCollectionCdFileDetails.getCdFileDetailsCollection().remove(cdFileDetailsCollectionCdFileDetails);
                    oldPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOfCdFileDetailsCollectionCdFileDetails = em.merge(oldPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOfCdFileDetailsCollectionCdFileDetails);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMap) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PricelistInternalProductcodeDocumentMap persistentPricelistInternalProductcodeDocumentMap = em.find(PricelistInternalProductcodeDocumentMap.class, pricelistInternalProductcodeDocumentMap.getPricelistIPCMAPID());
            Pricelist PRICELISTPriceIDRefOld = persistentPricelistInternalProductcodeDocumentMap.getPRICELISTPriceIDRef();
            Pricelist PRICELISTPriceIDRefNew = pricelistInternalProductcodeDocumentMap.getPRICELISTPriceIDRef();
            InternalProductcodes INTERNALPRODUCTCODESIPCIDRefOld = persistentPricelistInternalProductcodeDocumentMap.getINTERNALPRODUCTCODESIPCIDRef();
            InternalProductcodes INTERNALPRODUCTCODESIPCIDRefNew = pricelistInternalProductcodeDocumentMap.getINTERNALPRODUCTCODESIPCIDRef();
            EcsDocumentTypes DOCUMENTTYPESidOld = persistentPricelistInternalProductcodeDocumentMap.getDOCUMENTTYPESid();
            EcsDocumentTypes DOCUMENTTYPESidNew = pricelistInternalProductcodeDocumentMap.getDOCUMENTTYPESid();
            Collection<PaymentInfoLog> paymentInfoLogCollectionOld = persistentPricelistInternalProductcodeDocumentMap.getPaymentInfoLogCollection();
            Collection<PaymentInfoLog> paymentInfoLogCollectionNew = pricelistInternalProductcodeDocumentMap.getPaymentInfoLogCollection();
            Collection<CdFileDetails> cdFileDetailsCollectionOld = persistentPricelistInternalProductcodeDocumentMap.getCdFileDetailsCollection();
            Collection<CdFileDetails> cdFileDetailsCollectionNew = pricelistInternalProductcodeDocumentMap.getCdFileDetailsCollection();
            List<String> illegalOrphanMessages = null;
            for (CdFileDetails cdFileDetailsCollectionOldCdFileDetails : cdFileDetailsCollectionOld) {
                if (!cdFileDetailsCollectionNew.contains(cdFileDetailsCollectionOldCdFileDetails)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CdFileDetails " + cdFileDetailsCollectionOldCdFileDetails + " since its PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (PRICELISTPriceIDRefNew != null) {
                PRICELISTPriceIDRefNew = em.getReference(PRICELISTPriceIDRefNew.getClass(), PRICELISTPriceIDRefNew.getPriceID());
                pricelistInternalProductcodeDocumentMap.setPRICELISTPriceIDRef(PRICELISTPriceIDRefNew);
            }
            if (INTERNALPRODUCTCODESIPCIDRefNew != null) {
                INTERNALPRODUCTCODESIPCIDRefNew = em.getReference(INTERNALPRODUCTCODESIPCIDRefNew.getClass(), INTERNALPRODUCTCODESIPCIDRefNew.getIpcId());
                pricelistInternalProductcodeDocumentMap.setINTERNALPRODUCTCODESIPCIDRef(INTERNALPRODUCTCODESIPCIDRefNew);
            }
            if (DOCUMENTTYPESidNew != null) {
                DOCUMENTTYPESidNew = em.getReference(DOCUMENTTYPESidNew.getClass(), DOCUMENTTYPESidNew.getId());
                pricelistInternalProductcodeDocumentMap.setDOCUMENTTYPESid(DOCUMENTTYPESidNew);
            }
            Collection<PaymentInfoLog> attachedPaymentInfoLogCollectionNew = new ArrayList<PaymentInfoLog>();
            for (PaymentInfoLog paymentInfoLogCollectionNewPaymentInfoLogToAttach : paymentInfoLogCollectionNew) {
                paymentInfoLogCollectionNewPaymentInfoLogToAttach = em.getReference(paymentInfoLogCollectionNewPaymentInfoLogToAttach.getClass(), paymentInfoLogCollectionNewPaymentInfoLogToAttach.getPayementMsgId());
                attachedPaymentInfoLogCollectionNew.add(paymentInfoLogCollectionNewPaymentInfoLogToAttach);
            }
            paymentInfoLogCollectionNew = attachedPaymentInfoLogCollectionNew;
            pricelistInternalProductcodeDocumentMap.setPaymentInfoLogCollection(paymentInfoLogCollectionNew);
            Collection<CdFileDetails> attachedCdFileDetailsCollectionNew = new ArrayList<CdFileDetails>();
            for (CdFileDetails cdFileDetailsCollectionNewCdFileDetailsToAttach : cdFileDetailsCollectionNew) {
                cdFileDetailsCollectionNewCdFileDetailsToAttach = em.getReference(cdFileDetailsCollectionNewCdFileDetailsToAttach.getClass(), cdFileDetailsCollectionNewCdFileDetailsToAttach.getId());
                attachedCdFileDetailsCollectionNew.add(cdFileDetailsCollectionNewCdFileDetailsToAttach);
            }
            cdFileDetailsCollectionNew = attachedCdFileDetailsCollectionNew;
            pricelistInternalProductcodeDocumentMap.setCdFileDetailsCollection(cdFileDetailsCollectionNew);
            pricelistInternalProductcodeDocumentMap = em.merge(pricelistInternalProductcodeDocumentMap);
            if (PRICELISTPriceIDRefOld != null && !PRICELISTPriceIDRefOld.equals(PRICELISTPriceIDRefNew)) {
                PRICELISTPriceIDRefOld.getPricelistInternalProductcodeDocumentMapCollection().remove(pricelistInternalProductcodeDocumentMap);
                PRICELISTPriceIDRefOld = em.merge(PRICELISTPriceIDRefOld);
            }
            if (PRICELISTPriceIDRefNew != null && !PRICELISTPriceIDRefNew.equals(PRICELISTPriceIDRefOld)) {
                PRICELISTPriceIDRefNew.getPricelistInternalProductcodeDocumentMapCollection().add(pricelistInternalProductcodeDocumentMap);
                PRICELISTPriceIDRefNew = em.merge(PRICELISTPriceIDRefNew);
            }
            if (INTERNALPRODUCTCODESIPCIDRefOld != null && !INTERNALPRODUCTCODESIPCIDRefOld.equals(INTERNALPRODUCTCODESIPCIDRefNew)) {
                INTERNALPRODUCTCODESIPCIDRefOld.getPricelistInternalProductcodeDocumentMapCollection().remove(pricelistInternalProductcodeDocumentMap);
                INTERNALPRODUCTCODESIPCIDRefOld = em.merge(INTERNALPRODUCTCODESIPCIDRefOld);
            }
            if (INTERNALPRODUCTCODESIPCIDRefNew != null && !INTERNALPRODUCTCODESIPCIDRefNew.equals(INTERNALPRODUCTCODESIPCIDRefOld)) {
                INTERNALPRODUCTCODESIPCIDRefNew.getPricelistInternalProductcodeDocumentMapCollection().add(pricelistInternalProductcodeDocumentMap);
                INTERNALPRODUCTCODESIPCIDRefNew = em.merge(INTERNALPRODUCTCODESIPCIDRefNew);
            }
            if (DOCUMENTTYPESidOld != null && !DOCUMENTTYPESidOld.equals(DOCUMENTTYPESidNew)) {
                DOCUMENTTYPESidOld.getPricelistInternalProductcodeDocumentMapCollection().remove(pricelistInternalProductcodeDocumentMap);
                DOCUMENTTYPESidOld = em.merge(DOCUMENTTYPESidOld);
            }
            if (DOCUMENTTYPESidNew != null && !DOCUMENTTYPESidNew.equals(DOCUMENTTYPESidOld)) {
                DOCUMENTTYPESidNew.getPricelistInternalProductcodeDocumentMapCollection().add(pricelistInternalProductcodeDocumentMap);
                DOCUMENTTYPESidNew = em.merge(DOCUMENTTYPESidNew);
            }
            for (PaymentInfoLog paymentInfoLogCollectionOldPaymentInfoLog : paymentInfoLogCollectionOld) {
                if (!paymentInfoLogCollectionNew.contains(paymentInfoLogCollectionOldPaymentInfoLog)) {
                    paymentInfoLogCollectionOldPaymentInfoLog.setPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID(null);
                    paymentInfoLogCollectionOldPaymentInfoLog = em.merge(paymentInfoLogCollectionOldPaymentInfoLog);
                }
            }
            for (PaymentInfoLog paymentInfoLogCollectionNewPaymentInfoLog : paymentInfoLogCollectionNew) {
                if (!paymentInfoLogCollectionOld.contains(paymentInfoLogCollectionNewPaymentInfoLog)) {
                    PricelistInternalProductcodeDocumentMap oldPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOfPaymentInfoLogCollectionNewPaymentInfoLog = paymentInfoLogCollectionNewPaymentInfoLog.getPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID();
                    paymentInfoLogCollectionNewPaymentInfoLog.setPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID(pricelistInternalProductcodeDocumentMap);
                    paymentInfoLogCollectionNewPaymentInfoLog = em.merge(paymentInfoLogCollectionNewPaymentInfoLog);
                    if (oldPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOfPaymentInfoLogCollectionNewPaymentInfoLog != null && !oldPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOfPaymentInfoLogCollectionNewPaymentInfoLog.equals(pricelistInternalProductcodeDocumentMap)) {
                        oldPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOfPaymentInfoLogCollectionNewPaymentInfoLog.getPaymentInfoLogCollection().remove(paymentInfoLogCollectionNewPaymentInfoLog);
                        oldPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOfPaymentInfoLogCollectionNewPaymentInfoLog = em.merge(oldPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPIDOfPaymentInfoLogCollectionNewPaymentInfoLog);
                    }
                }
            }
            for (CdFileDetails cdFileDetailsCollectionNewCdFileDetails : cdFileDetailsCollectionNew) {
                if (!cdFileDetailsCollectionOld.contains(cdFileDetailsCollectionNewCdFileDetails)) {
                    PricelistInternalProductcodeDocumentMap oldPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOfCdFileDetailsCollectionNewCdFileDetails = cdFileDetailsCollectionNewCdFileDetails.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef();
                    cdFileDetailsCollectionNewCdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(pricelistInternalProductcodeDocumentMap);
                    cdFileDetailsCollectionNewCdFileDetails = em.merge(cdFileDetailsCollectionNewCdFileDetails);
                    if (oldPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOfCdFileDetailsCollectionNewCdFileDetails != null && !oldPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOfCdFileDetailsCollectionNewCdFileDetails.equals(pricelistInternalProductcodeDocumentMap)) {
                        oldPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOfCdFileDetailsCollectionNewCdFileDetails.getCdFileDetailsCollection().remove(cdFileDetailsCollectionNewCdFileDetails);
                        oldPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOfCdFileDetailsCollectionNewCdFileDetails = em.merge(oldPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOfCdFileDetailsCollectionNewCdFileDetails);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pricelistInternalProductcodeDocumentMap.getPricelistIPCMAPID();
                if (findPricelistInternalProductcodeDocumentMap(id) == null) {
                    throw new NonexistentEntityException("The pricelistInternalProductcodeDocumentMap with id " + id + " no longer exists.");
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
            PricelistInternalProductcodeDocumentMap pricelistInternalProductcodeDocumentMap;
            try {
                pricelistInternalProductcodeDocumentMap = em.getReference(PricelistInternalProductcodeDocumentMap.class, id);
                pricelistInternalProductcodeDocumentMap.getPricelistIPCMAPID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pricelistInternalProductcodeDocumentMap with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CdFileDetails> cdFileDetailsCollectionOrphanCheck = pricelistInternalProductcodeDocumentMap.getCdFileDetailsCollection();
            for (CdFileDetails cdFileDetailsCollectionOrphanCheckCdFileDetails : cdFileDetailsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PricelistInternalProductcodeDocumentMap (" + pricelistInternalProductcodeDocumentMap + ") cannot be destroyed since the CdFileDetails " + cdFileDetailsCollectionOrphanCheckCdFileDetails + " in its cdFileDetailsCollection field has a non-nullable PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pricelist PRICELISTPriceIDRef = pricelistInternalProductcodeDocumentMap.getPRICELISTPriceIDRef();
            if (PRICELISTPriceIDRef != null) {
                PRICELISTPriceIDRef.getPricelistInternalProductcodeDocumentMapCollection().remove(pricelistInternalProductcodeDocumentMap);
                PRICELISTPriceIDRef = em.merge(PRICELISTPriceIDRef);
            }
            InternalProductcodes INTERNALPRODUCTCODESIPCIDRef = pricelistInternalProductcodeDocumentMap.getINTERNALPRODUCTCODESIPCIDRef();
            if (INTERNALPRODUCTCODESIPCIDRef != null) {
                INTERNALPRODUCTCODESIPCIDRef.getPricelistInternalProductcodeDocumentMapCollection().remove(pricelistInternalProductcodeDocumentMap);
                INTERNALPRODUCTCODESIPCIDRef = em.merge(INTERNALPRODUCTCODESIPCIDRef);
            }
            EcsDocumentTypes DOCUMENTTYPESid = pricelistInternalProductcodeDocumentMap.getDOCUMENTTYPESid();
            if (DOCUMENTTYPESid != null) {
                DOCUMENTTYPESid.getPricelistInternalProductcodeDocumentMapCollection().remove(pricelistInternalProductcodeDocumentMap);
                DOCUMENTTYPESid = em.merge(DOCUMENTTYPESid);
            }
            Collection<PaymentInfoLog> paymentInfoLogCollection = pricelistInternalProductcodeDocumentMap.getPaymentInfoLogCollection();
            for (PaymentInfoLog paymentInfoLogCollectionPaymentInfoLog : paymentInfoLogCollection) {
                paymentInfoLogCollectionPaymentInfoLog.setPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID(null);
                paymentInfoLogCollectionPaymentInfoLog = em.merge(paymentInfoLogCollectionPaymentInfoLog);
            }
            em.remove(pricelistInternalProductcodeDocumentMap);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PricelistInternalProductcodeDocumentMap> findPricelistInternalProductcodeDocumentMapEntities() {
        return findPricelistInternalProductcodeDocumentMapEntities(true, -1, -1);
    }

    public List<PricelistInternalProductcodeDocumentMap> findPricelistInternalProductcodeDocumentMapEntities(int maxResults, int firstResult) {
        return findPricelistInternalProductcodeDocumentMapEntities(false, maxResults, firstResult);
    }

    private List<PricelistInternalProductcodeDocumentMap> findPricelistInternalProductcodeDocumentMapEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PricelistInternalProductcodeDocumentMap.class));
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

    public PricelistInternalProductcodeDocumentMap findPricelistInternalProductcodeDocumentMap(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PricelistInternalProductcodeDocumentMap.class, id);
        } finally {
            em.close();
        }
    }

    public int getPricelistInternalProductcodeDocumentMapCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PricelistInternalProductcodeDocumentMap> rt = cq.from(PricelistInternalProductcodeDocumentMap.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
