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
import org.kephis.ecs_kesws.entities.CdFileDetails;
import org.kephis.ecs_kesws.entities.EcsResCdFileMsg;
import org.kephis.ecs_kesws.entities.RecCdFileMsg;
import org.kephis.ecs_kesws.entities.PricelistInternalProductcodeDocumentMap;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author kim
 */
public class CdFileDetailsJpaController implements Serializable {

    public CdFileDetailsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CdFileDetails cdFileDetails) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EcsResCdFileMsg ECSRESCDFILEMSGRECCDFileID = cdFileDetails.getECSRESCDFILEMSGRECCDFileID();
            if (ECSRESCDFILEMSGRECCDFileID != null) {
                ECSRESCDFILEMSGRECCDFileID = em.getReference(ECSRESCDFILEMSGRECCDFileID.getClass(), ECSRESCDFILEMSGRECCDFileID.getRECCDFileID());
                cdFileDetails.setECSRESCDFILEMSGRECCDFileID(ECSRESCDFILEMSGRECCDFileID);
            }
            RecCdFileMsg RECCDFILEMSGRECCDFILEIDRef = cdFileDetails.getRECCDFILEMSGRECCDFILEIDRef();
            if (RECCDFILEMSGRECCDFILEIDRef != null) {
                RECCDFILEMSGRECCDFILEIDRef = em.getReference(RECCDFILEMSGRECCDFILEIDRef.getClass(), RECCDFILEMSGRECCDFILEIDRef.getRECCDFileID());
                cdFileDetails.setRECCDFILEMSGRECCDFILEIDRef(RECCDFILEMSGRECCDFILEIDRef);
            }
            PricelistInternalProductcodeDocumentMap PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef = cdFileDetails.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef();
            if (PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef != null) {
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef = em.getReference(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef.getClass(), PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef.getPricelistIPCMAPID());
                cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef);
            }
            em.persist(cdFileDetails);
            if (ECSRESCDFILEMSGRECCDFileID != null) {
                ECSRESCDFILEMSGRECCDFileID.getCdFileDetailsCollection().add(cdFileDetails);
                ECSRESCDFILEMSGRECCDFileID = em.merge(ECSRESCDFILEMSGRECCDFileID);
            }
            if (RECCDFILEMSGRECCDFILEIDRef != null) {
                RECCDFILEMSGRECCDFILEIDRef.getCdFileDetailsCollection().add(cdFileDetails);
                RECCDFILEMSGRECCDFILEIDRef = em.merge(RECCDFILEMSGRECCDFILEIDRef);
            }
            if (PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef != null) {
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef.getCdFileDetailsCollection().add(cdFileDetails);
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef = em.merge(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef);
            }
            em.getTransaction().commit();
        } 
         finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CdFileDetails cdFileDetails) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CdFileDetails persistentCdFileDetails = em.find(CdFileDetails.class, cdFileDetails.getId());
            EcsResCdFileMsg ECSRESCDFILEMSGRECCDFileIDOld = persistentCdFileDetails.getECSRESCDFILEMSGRECCDFileID();
            EcsResCdFileMsg ECSRESCDFILEMSGRECCDFileIDNew = cdFileDetails.getECSRESCDFILEMSGRECCDFileID();
            RecCdFileMsg RECCDFILEMSGRECCDFILEIDRefOld = persistentCdFileDetails.getRECCDFILEMSGRECCDFILEIDRef();
            RecCdFileMsg RECCDFILEMSGRECCDFILEIDRefNew = cdFileDetails.getRECCDFILEMSGRECCDFILEIDRef();
            PricelistInternalProductcodeDocumentMap PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOld = persistentCdFileDetails.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef();
            PricelistInternalProductcodeDocumentMap PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefNew = cdFileDetails.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef();
            if (ECSRESCDFILEMSGRECCDFileIDNew != null) {
                ECSRESCDFILEMSGRECCDFileIDNew = em.getReference(ECSRESCDFILEMSGRECCDFileIDNew.getClass(), ECSRESCDFILEMSGRECCDFileIDNew.getRECCDFileID());
                cdFileDetails.setECSRESCDFILEMSGRECCDFileID(ECSRESCDFILEMSGRECCDFileIDNew);
            }
            if (RECCDFILEMSGRECCDFILEIDRefNew != null) {
                RECCDFILEMSGRECCDFILEIDRefNew = em.getReference(RECCDFILEMSGRECCDFILEIDRefNew.getClass(), RECCDFILEMSGRECCDFILEIDRefNew.getRECCDFileID());
                cdFileDetails.setRECCDFILEMSGRECCDFILEIDRef(RECCDFILEMSGRECCDFILEIDRefNew);
            }
            if (PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefNew != null) {
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefNew = em.getReference(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefNew.getClass(), PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefNew.getPricelistIPCMAPID());
                cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefNew);
            }
            cdFileDetails = em.merge(cdFileDetails);
            if (ECSRESCDFILEMSGRECCDFileIDOld != null && !ECSRESCDFILEMSGRECCDFileIDOld.equals(ECSRESCDFILEMSGRECCDFileIDNew)) {
                ECSRESCDFILEMSGRECCDFileIDOld.getCdFileDetailsCollection().remove(cdFileDetails);
                ECSRESCDFILEMSGRECCDFileIDOld = em.merge(ECSRESCDFILEMSGRECCDFileIDOld);
            }
            if (ECSRESCDFILEMSGRECCDFileIDNew != null && !ECSRESCDFILEMSGRECCDFileIDNew.equals(ECSRESCDFILEMSGRECCDFileIDOld)) {
                ECSRESCDFILEMSGRECCDFileIDNew.getCdFileDetailsCollection().add(cdFileDetails);
                ECSRESCDFILEMSGRECCDFileIDNew = em.merge(ECSRESCDFILEMSGRECCDFileIDNew);
            }
            if (RECCDFILEMSGRECCDFILEIDRefOld != null && !RECCDFILEMSGRECCDFILEIDRefOld.equals(RECCDFILEMSGRECCDFILEIDRefNew)) {
                RECCDFILEMSGRECCDFILEIDRefOld.getCdFileDetailsCollection().remove(cdFileDetails);
                RECCDFILEMSGRECCDFILEIDRefOld = em.merge(RECCDFILEMSGRECCDFILEIDRefOld);
            }
            if (RECCDFILEMSGRECCDFILEIDRefNew != null && !RECCDFILEMSGRECCDFILEIDRefNew.equals(RECCDFILEMSGRECCDFILEIDRefOld)) {
                RECCDFILEMSGRECCDFILEIDRefNew.getCdFileDetailsCollection().add(cdFileDetails);
                RECCDFILEMSGRECCDFILEIDRefNew = em.merge(RECCDFILEMSGRECCDFILEIDRefNew);
            }
            if (PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOld != null && !PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOld.equals(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefNew)) {
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOld.getCdFileDetailsCollection().remove(cdFileDetails);
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOld = em.merge(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOld);
            }
            if (PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefNew != null && !PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefNew.equals(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefOld)) {
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefNew.getCdFileDetailsCollection().add(cdFileDetails);
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefNew = em.merge(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRefNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cdFileDetails.getId();
                if (findCdFileDetails(id) == null) {
                    throw new NonexistentEntityException("The cdFileDetails with id " + id + " no longer exists.");
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
            CdFileDetails cdFileDetails;
            try {
                cdFileDetails = em.getReference(CdFileDetails.class, id);
                cdFileDetails.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cdFileDetails with id " + id + " no longer exists.", enfe);
            }
            EcsResCdFileMsg ECSRESCDFILEMSGRECCDFileID = cdFileDetails.getECSRESCDFILEMSGRECCDFileID();
            if (ECSRESCDFILEMSGRECCDFileID != null) {
                ECSRESCDFILEMSGRECCDFileID.getCdFileDetailsCollection().remove(cdFileDetails);
                ECSRESCDFILEMSGRECCDFileID = em.merge(ECSRESCDFILEMSGRECCDFileID);
            }
            RecCdFileMsg RECCDFILEMSGRECCDFILEIDRef = cdFileDetails.getRECCDFILEMSGRECCDFILEIDRef();
            if (RECCDFILEMSGRECCDFILEIDRef != null) {
                RECCDFILEMSGRECCDFILEIDRef.getCdFileDetailsCollection().remove(cdFileDetails);
                RECCDFILEMSGRECCDFILEIDRef = em.merge(RECCDFILEMSGRECCDFILEIDRef);
            }
            PricelistInternalProductcodeDocumentMap PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef = cdFileDetails.getPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef();
            if (PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef != null) {
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef.getCdFileDetailsCollection().remove(cdFileDetails);
                PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef = em.merge(PRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef);
            }
            em.remove(cdFileDetails);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CdFileDetails> findCdFileDetailsEntities() {
        return findCdFileDetailsEntities(true, -1, -1);
    }

    public List<CdFileDetails> findCdFileDetailsEntities(int maxResults, int firstResult) {
        return findCdFileDetailsEntities(false, maxResults, firstResult);
    }

    private List<CdFileDetails> findCdFileDetailsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CdFileDetails.class));
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

    public CdFileDetails findCdFileDetails(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CdFileDetails.class, id);
        } finally {
            em.close();
        }
    }

    public int getCdFileDetailsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CdFileDetails> rt = cq.from(CdFileDetails.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
