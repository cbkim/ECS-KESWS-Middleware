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
@Table(name = "KESWS_REF_DATA_CATEGORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KeswsRefDataCategory.findAll", query = "SELECT k FROM KeswsRefDataCategory k"),
    @NamedQuery(name = "KeswsRefDataCategory.findById", query = "SELECT k FROM KeswsRefDataCategory k WHERE k.id = :id"),
    @NamedQuery(name = "KeswsRefDataCategory.findByKeswsRefDataCat", query = "SELECT k FROM KeswsRefDataCategory k WHERE k.keswsRefDataCat = :keswsRefDataCat")})
public class KeswsRefDataCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "KESWS_REF_DATA_CAT")
    private String keswsRefDataCat;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "keswsRefDataCategoryId")
    private Collection<KeswsRefData> keswsRefDataCollection;

    public KeswsRefDataCategory() {
    }

    public KeswsRefDataCategory(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeswsRefDataCat() {
        return keswsRefDataCat;
    }

    public void setKeswsRefDataCat(String keswsRefDataCat) {
        this.keswsRefDataCat = keswsRefDataCat;
    }

    @XmlTransient
    public Collection<KeswsRefData> getKeswsRefDataCollection() {
        return keswsRefDataCollection;
    }

    public void setKeswsRefDataCollection(Collection<KeswsRefData> keswsRefDataCollection) {
        this.keswsRefDataCollection = keswsRefDataCollection;
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
        if (!(object instanceof KeswsRefDataCategory)) {
            return false;
        }
        KeswsRefDataCategory other = (KeswsRefDataCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.KeswsRefDataCategory[ id=" + id + " ]";
    }
    
}
