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
import javax.persistence.Lob;
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
@Table(name = "PRICELIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pricelist.findAll", query = "SELECT p FROM Pricelist p"),
    @NamedQuery(name = "Pricelist.findByPriceID", query = "SELECT p FROM Pricelist p WHERE p.priceID = :priceID"),
    @NamedQuery(name = "Pricelist.findByPriceCode", query = "SELECT p FROM Pricelist p WHERE p.priceCode = :priceCode"),
    @NamedQuery(name = "Pricelist.findByChargeKshs", query = "SELECT p FROM Pricelist p WHERE p.chargeKshs = :chargeKshs"),
    @NamedQuery(name = "Pricelist.findByNoOfEntries", query = "SELECT p FROM Pricelist p WHERE p.noOfEntries = :noOfEntries"),
    @NamedQuery(name = "Pricelist.findByMinWeight", query = "SELECT p FROM Pricelist p WHERE p.minWeight = :minWeight"),
    @NamedQuery(name = "Pricelist.findByMaxWeight", query = "SELECT p FROM Pricelist p WHERE p.maxWeight = :maxWeight"),
    @NamedQuery(name = "Pricelist.findByIsActive", query = "SELECT p FROM Pricelist p WHERE p.isActive = :isActive"),
    @NamedQuery(name = "Pricelist.findByDateCreated", query = "SELECT p FROM Pricelist p WHERE p.dateCreated = :dateCreated"),
    @NamedQuery(name = "Pricelist.findByDateDeactivated", query = "SELECT p FROM Pricelist p WHERE p.dateDeactivated = :dateDeactivated")})
public class Pricelist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Price_ID")
    private Integer priceID;
    @Basic(optional = false)
    @Column(name = "Price_Code")
    private String priceCode;
    @Basic(optional = false)
    @Column(name = "Charge_Kshs")
    private double chargeKshs;
    @Basic(optional = false)
    @Lob
    @Column(name = "Charge_Description")
    private String chargeDescription;
    @Column(name = "No_Of_Entries")
    private Integer noOfEntries;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Min_Weight")
    private Double minWeight;
    @Column(name = "Max_Weight")
    private Double maxWeight;
    @Column(name = "IsActive")
    private Integer isActive;
    @Basic(optional = false)
    @Column(name = "Date_Created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "Date_Deactivated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDeactivated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pRICELISTPriceIDRef")
    private Collection<PricelistInternalProductcodeDocumentMap> pricelistInternalProductcodeDocumentMapCollection;

    public Pricelist() {
    }

    public Pricelist(Integer priceID) {
        this.priceID = priceID;
    }

    public Pricelist(Integer priceID, String priceCode, double chargeKshs, String chargeDescription, Date dateCreated) {
        this.priceID = priceID;
        this.priceCode = priceCode;
        this.chargeKshs = chargeKshs;
        this.chargeDescription = chargeDescription;
        this.dateCreated = dateCreated;
    }

    public Integer getPriceID() {
        return priceID;
    }

    public void setPriceID(Integer priceID) {
        this.priceID = priceID;
    }

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public double getChargeKshs() {
        return chargeKshs;
    }

    public void setChargeKshs(double chargeKshs) {
        this.chargeKshs = chargeKshs;
    }

    public String getChargeDescription() {
        return chargeDescription;
    }

    public void setChargeDescription(String chargeDescription) {
        this.chargeDescription = chargeDescription;
    }

    public Integer getNoOfEntries() {
        return noOfEntries;
    }

    public void setNoOfEntries(Integer noOfEntries) {
        this.noOfEntries = noOfEntries;
    }

    public Double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Double minWeight) {
        this.minWeight = minWeight;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
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
        hash += (priceID != null ? priceID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pricelist)) {
            return false;
        }
        Pricelist other = (Pricelist) object;
        if ((this.priceID == null && other.priceID != null) || (this.priceID != null && !this.priceID.equals(other.priceID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.Pricelist[ priceID=" + priceID + " ]";
    }
    
}
