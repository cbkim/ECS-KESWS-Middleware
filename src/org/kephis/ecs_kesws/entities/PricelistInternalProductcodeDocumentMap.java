/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kim
 */
@Entity
@Table(name = "PRICELIST_INTERNAL_PRODUCTCODE_DOCUMENT_MAP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PricelistInternalProductcodeDocumentMap.findAll", query = "SELECT p FROM PricelistInternalProductcodeDocumentMap p"),
    @NamedQuery(name = "PricelistInternalProductcodeDocumentMap.findByPricelistIPCMAPID", query = "SELECT p FROM PricelistInternalProductcodeDocumentMap p WHERE p.pricelistIPCMAPID = :pricelistIPCMAPID"),
    @NamedQuery(name = "PricelistInternalProductcodeDocumentMap.findByIPCIDRef", query = "SELECT p FROM PricelistInternalProductcodeDocumentMap p WHERE p.iNTERNALPRODUCTCODESIPCIDRef = :iNTERNALPRODUCTCODESIPCIDRef"),
    @NamedQuery(name = "PricelistInternalProductcodeDocumentMap.findByDocumentIDRef", query = "SELECT p FROM PricelistInternalProductcodeDocumentMap p WHERE p.documentIDRef = :documentIDRef")})
public class PricelistInternalProductcodeDocumentMap implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Pricelist_IPC_MAP_ID")
    private Integer pricelistIPCMAPID;
    @Column(name = "Document_ID_Ref")
    private String documentIDRef;
    @OneToMany(mappedBy = "pRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID",fetch = FetchType.EAGER)
    private Collection<PaymentInfoLog> paymentInfoLogCollection;
    @JoinColumn(name = "PRICELIST_Price_ID_Ref", referencedColumnName = "Price_ID")
    @ManyToOne(optional = false)
    private Pricelist pRICELISTPriceIDRef;
    @JoinColumn(name = "INTERNAL_PRODUCTCODES_IPC_ID_Ref", referencedColumnName = "IPC_ID")
    @ManyToOne(optional = false)
    private InternalProductcodes iNTERNALPRODUCTCODESIPCIDRef;
    @JoinColumn(name = "DOCUMENT_TYPES_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EcsDocumentTypes dOCUMENTTYPESid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef",fetch = FetchType.EAGER)
    private Collection<CdFileDetails> cdFileDetailsCollection;

    public PricelistInternalProductcodeDocumentMap() {
    }

    public PricelistInternalProductcodeDocumentMap(Integer pricelistIPCMAPID) {
        this.pricelistIPCMAPID = pricelistIPCMAPID;
    }

    public Integer getPricelistIPCMAPID() {
        return pricelistIPCMAPID;
    }

    public void setPricelistIPCMAPID(Integer pricelistIPCMAPID) {
        this.pricelistIPCMAPID = pricelistIPCMAPID;
    }

    public String getDocumentIDRef() {
        return documentIDRef;
    }

    public void setDocumentIDRef(String documentIDRef) {
        this.documentIDRef = documentIDRef;
    }

    @XmlTransient
    public Collection<PaymentInfoLog> getPaymentInfoLogCollection() {
        return paymentInfoLogCollection;
    }

    public void setPaymentInfoLogCollection(Collection<PaymentInfoLog> paymentInfoLogCollection) {
        this.paymentInfoLogCollection = paymentInfoLogCollection;
    }

    public Pricelist getPRICELISTPriceIDRef() {
        return pRICELISTPriceIDRef;
    }

    public void setPRICELISTPriceIDRef(Pricelist pRICELISTPriceIDRef) {
        this.pRICELISTPriceIDRef = pRICELISTPriceIDRef;
    }

    public InternalProductcodes getINTERNALPRODUCTCODESIPCIDRef() {
        return iNTERNALPRODUCTCODESIPCIDRef;
    }

    public void setINTERNALPRODUCTCODESIPCIDRef(InternalProductcodes iNTERNALPRODUCTCODESIPCIDRef) {
        this.iNTERNALPRODUCTCODESIPCIDRef = iNTERNALPRODUCTCODESIPCIDRef;
    }

    public EcsDocumentTypes getDOCUMENTTYPESid() {
        return dOCUMENTTYPESid;
    }

    public void setDOCUMENTTYPESid(EcsDocumentTypes dOCUMENTTYPESid) {
        this.dOCUMENTTYPESid = dOCUMENTTYPESid;
    }

    @XmlTransient
    public Collection<CdFileDetails> getCdFileDetailsCollection() {
        return cdFileDetailsCollection;
    }

    public void setCdFileDetailsCollection(Collection<CdFileDetails> cdFileDetailsCollection) {
        this.cdFileDetailsCollection = cdFileDetailsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pricelistIPCMAPID != null ? pricelistIPCMAPID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PricelistInternalProductcodeDocumentMap)) {
            return false;
        }
        PricelistInternalProductcodeDocumentMap other = (PricelistInternalProductcodeDocumentMap) object;
        if ((this.pricelistIPCMAPID == null && other.pricelistIPCMAPID != null) || (this.pricelistIPCMAPID != null && !this.pricelistIPCMAPID.equals(other.pricelistIPCMAPID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.PricelistInternalProductcodeDocumentMap[ pricelistIPCMAPID=" + pricelistIPCMAPID + " ]";
    }
    
}
