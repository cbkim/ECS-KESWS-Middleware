/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kim
 */
@Entity
@Table(name = "KESWS_REF_DATA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KeswsRefData.findAll", query = "SELECT k FROM KeswsRefData k"),
    @NamedQuery(name = "KeswsRefData.findById", query = "SELECT k FROM KeswsRefData k WHERE k.id = :id"),
    @NamedQuery(name = "KeswsRefData.findByKeswsRefDataDesc", query = "SELECT k FROM KeswsRefData k WHERE k.keswsRefDataDesc = :keswsRefDataDesc"),
    @NamedQuery(name = "KeswsRefData.findByKeswsRefDataValue", query = "SELECT k FROM KeswsRefData k WHERE k.keswsRefDataValue = :keswsRefDataValue"),
    @NamedQuery(name = "KeswsRefData.findByEcsRefId", query = "SELECT k FROM KeswsRefData k WHERE k.ecsRefId = :ecsRefId")})
public class KeswsRefData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "KESWS_REF_DATA_DESC")
    private String keswsRefDataDesc;
    @Basic(optional = false)
    @Column(name = "KESWS_REF_DATA_VALUE")
    private String keswsRefDataValue;
    @Column(name = "ECS_REF_ID")
    private String ecsRefId;
    @JoinColumn(name = "KESWS_REF_DATA_CATEGORY_ID", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private KeswsRefDataCategory keswsRefDataCategoryId;

    public KeswsRefData() {
    }

    public KeswsRefData(Integer id) {
        this.id = id;
    }

    public KeswsRefData(Integer id, String keswsRefDataValue) {
        this.id = id;
        this.keswsRefDataValue = keswsRefDataValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeswsRefDataDesc() {
        return keswsRefDataDesc;
    }

    public void setKeswsRefDataDesc(String keswsRefDataDesc) {
        this.keswsRefDataDesc = keswsRefDataDesc;
    }

    public String getKeswsRefDataValue() {
        return keswsRefDataValue;
    }

    public void setKeswsRefDataValue(String keswsRefDataValue) {
        this.keswsRefDataValue = keswsRefDataValue;
    }

    public String getEcsRefId() {
        return ecsRefId;
    }

    public void setEcsRefId(String ecsRefId) {
        this.ecsRefId = ecsRefId;
    }

    public KeswsRefDataCategory getKeswsRefDataCategoryId() {
        return keswsRefDataCategoryId;
    }

    public void setKeswsRefDataCategoryId(KeswsRefDataCategory keswsRefDataCategoryId) {
        this.keswsRefDataCategoryId = keswsRefDataCategoryId;
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
        if (!(object instanceof KeswsRefData)) {
            return false;
        }
        KeswsRefData other = (KeswsRefData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.KeswsRefData[ id=" + id + " ]";
    }
    
}
