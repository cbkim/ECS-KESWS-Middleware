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
import javax.persistence.Id;
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
@Table(name = "ECS_DOCUMENT_TYPES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcsDocumentTypes.findAll", query = "SELECT e FROM EcsDocumentTypes e"),
    @NamedQuery(name = "EcsDocumentTypes.findById", query = "SELECT e FROM EcsDocumentTypes e WHERE e.id = :id"),
    @NamedQuery(name = "EcsDocumentTypes.findByDocumentName", query = "SELECT e FROM EcsDocumentTypes e WHERE e.documentName = :documentName")})
public class EcsDocumentTypes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "Document_Name")
    private String documentName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dOCUMENTTYPESid")
    private Collection<PricelistInternalProductcodeDocumentMap> pricelistInternalProductcodeDocumentMapCollection;

    public EcsDocumentTypes() {
    }

    public EcsDocumentTypes(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @XmlTransient
    public Collection<PricelistInternalProductcodeDocumentMap> getPricelistInternalProductcodeDocumentMapCollection() {
        return pricelistInternalProductcodeDocumentMapCollection;
    }

    public void setPricelistInternalProductcodeDocumentMapCollection(Collection<PricelistInternalProductcodeDocumentMap> pricelistInternalProductcodeDocumentMapCollection) {
        this.pricelistInternalProductcodeDocumentMapCollection = pricelistInternalProductcodeDocumentMapCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcsDocumentTypes)) {
            return false;
        }
        EcsDocumentTypes other = (EcsDocumentTypes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.EcsDocumentTypes[ id=" + id + " ]";
    }
    
}
