/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kim
 */
@Entity
@Table(name = "INTERNAL_PRODUCTCODES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InternalProductcodes.findAll", query = "SELECT i FROM InternalProductcodes i"),
    @NamedQuery(name = "InternalProductcodes.findByIpcId", query = "SELECT i FROM InternalProductcodes i WHERE i.ipcId = :ipcId"),
    @NamedQuery(name = "InternalProductcodes.findByHscode", query = "SELECT i FROM InternalProductcodes i WHERE i.hscode = :hscode"),
    @NamedQuery(name = "InternalProductcodes.findByInternalProductCode", query = "SELECT i FROM InternalProductcodes i WHERE i.internalProductCode = :internalProductCode"),
    @NamedQuery(name = "InternalProductcodes.findByHscodeDesc", query = "SELECT i FROM InternalProductcodes i WHERE i.hscodeDesc = :hscodeDesc"),
    @NamedQuery(name = "InternalProductcodes.findByCommodityCategory", query = "SELECT i FROM InternalProductcodes i WHERE i.commodityCategory = :commodityCategory"),
    @NamedQuery(name = "InternalProductcodes.findByCommodityType", query = "SELECT i FROM InternalProductcodes i WHERE i.commodityType = :commodityType"),
    @NamedQuery(name = "InternalProductcodes.findByCommodityTechnicalName", query = "SELECT i FROM InternalProductcodes i WHERE i.commodityTechnicalName = :commodityTechnicalName"),
    @NamedQuery(name = "InternalProductcodes.findByCommodityVariety", query = "SELECT i FROM InternalProductcodes i WHERE i.commodityVariety = :commodityVariety"),
    @NamedQuery(name = "InternalProductcodes.findByCommodityForm", query = "SELECT i FROM InternalProductcodes i WHERE i.commodityForm = :commodityForm"),
    @NamedQuery(name = "InternalProductcodes.findByIsActiveIPC", query = "SELECT i FROM InternalProductcodes i WHERE i.isActiveIPC = :isActiveIPC"),
    @NamedQuery(name = "InternalProductcodes.findByAggregateIPCCodeLevel", query = "SELECT i FROM InternalProductcodes i WHERE i.aggregateIPCCodeLevel = :aggregateIPCCodeLevel"),
    @NamedQuery(name = "InternalProductcodes.findByDateCreated", query = "SELECT i FROM InternalProductcodes i WHERE i.dateCreated = :dateCreated"),
    @NamedQuery(name = "InternalProductcodes.findByDateDeactivated", query = "SELECT i FROM InternalProductcodes i WHERE i.dateDeactivated = :dateDeactivated")})
public class InternalProductcodes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IPC_ID")
    private Integer ipcId;
    @Column(name = "HSCODE")
    private String hscode;
    @Basic(optional = false)
    @Column(name = "Internal_Product_Code")
    private String internalProductCode;
    @Column(name = "HSCODE_DESC")
    private String hscodeDesc;
    @Column(name = "Commodity_Category")
    private String commodityCategory;
    @Column(name = "Commodity_Type")
    private String commodityType;
    @Column(name = "Commodity_Technical_Name")
    private String commodityTechnicalName;
    @Column(name = "Commodity_Variety")
    private String commodityVariety;
    @Column(name = "Commodity_Form")
    private String commodityForm;
    @Column(name = "isActive_IPC")
    private Integer isActiveIPC;
    @Column(name = "Aggregate_IPC_Code_Level")
    private Integer aggregateIPCCodeLevel;
    @Column(name = "Date_Created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "Date_Deactivated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDeactivated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iNTERNALPRODUCTCODESIPCIDRef")
    private Collection<PricelistInternalProductcodeDocumentMap> pricelistInternalProductcodeDocumentMapCollection;

    public InternalProductcodes() {
    }

    public InternalProductcodes(Integer ipcId) {
        this.ipcId = ipcId;
    }

    public InternalProductcodes(Integer ipcId, String internalProductCode) {
        this.ipcId = ipcId;
        this.internalProductCode = internalProductCode;
    }

    public Integer getIpcId() {
        return ipcId;
    }

    public void setIpcId(Integer ipcId) {
        this.ipcId = ipcId;
    }

    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    public String getInternalProductCode() {
        return internalProductCode;
    }

    public void setInternalProductCode(String internalProductCode) {
        this.internalProductCode = internalProductCode;
    }

    public String getHscodeDesc() {
        return hscodeDesc;
    }

    public void setHscodeDesc(String hscodeDesc) {
        this.hscodeDesc = hscodeDesc;
    }

    public String getCommodityCategory() {
        return commodityCategory;
    }

    public void setCommodityCategory(String commodityCategory) {
        this.commodityCategory = commodityCategory;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public String getCommodityTechnicalName() {
        return commodityTechnicalName;
    }

    public void setCommodityTechnicalName(String commodityTechnicalName) {
        this.commodityTechnicalName = commodityTechnicalName;
    }

    public String getCommodityVariety() {
        return commodityVariety;
    }

    public void setCommodityVariety(String commodityVariety) {
        this.commodityVariety = commodityVariety;
    }

    public String getCommodityForm() {
        return commodityForm;
    }

    public void setCommodityForm(String commodityForm) {
        this.commodityForm = commodityForm;
    }

    public Integer getIsActiveIPC() {
        return isActiveIPC;
    }

    public void setIsActiveIPC(Integer isActiveIPC) {
        this.isActiveIPC = isActiveIPC;
    }

    public Integer getAggregateIPCCodeLevel() {
        return aggregateIPCCodeLevel;
    }

    public void setAggregateIPCCodeLevel(Integer aggregateIPCCodeLevel) {
        this.aggregateIPCCodeLevel = aggregateIPCCodeLevel;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateDeactivated() {
        return dateDeactivated;
    }

    public void setDateDeactivated(Date dateDeactivated) {
        this.dateDeactivated = dateDeactivated;
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
        hash += (ipcId != null ? ipcId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InternalProductcodes)) {
            return false;
        }
        InternalProductcodes other = (InternalProductcodes) object;
        if ((this.ipcId == null && other.ipcId != null) || (this.ipcId != null && !this.ipcId.equals(other.ipcId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.InternalProductcodes[ ipcId=" + ipcId + " ]";
    }
    
}
