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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DESTINY
 */
@Entity
@Table(name = "items_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemsView.findAll", query = "SELECT i FROM ItemsView i"),
    @NamedQuery(name = "ItemsView.findByCDProduct1ItemDescription", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1ItemDescription = :cDProduct1ItemDescription"),
    @NamedQuery(name = "ItemsView.findByCDProduct1ItemHSCode", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1ItemHSCode = :cDProduct1ItemHSCode"),
    @NamedQuery(name = "ItemsView.findByCDProduct1HSDescription", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1HSDescription = :cDProduct1HSDescription"),
    @NamedQuery(name = "ItemsView.findByCDProduct1InternalProductNo", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1InternalProductNo = :cDProduct1InternalProductNo"),
    @NamedQuery(name = "ItemsView.findByCDProduct1QuantityQty", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1QuantityQty = :cDProduct1QuantityQty"),
    @NamedQuery(name = "ItemsView.findByCDProduct1QuantityUnitOfQty", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1QuantityUnitOfQty = :cDProduct1QuantityUnitOfQty"),
    @NamedQuery(name = "ItemsView.findByCDProduct1QuantityUnitOfQtyDesc", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1QuantityUnitOfQtyDesc = :cDProduct1QuantityUnitOfQtyDesc"),
    @NamedQuery(name = "ItemsView.findByCDProduct1SupplementryQuantityQty", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1SupplementryQuantityQty = :cDProduct1SupplementryQuantityQty"),
    @NamedQuery(name = "ItemsView.findByCDProduct1SupplementryQuantityUnitOfQty", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1SupplementryQuantityUnitOfQty = :cDProduct1SupplementryQuantityUnitOfQty"),
    @NamedQuery(name = "ItemsView.findByCDProduct1PackageTypeDesc", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1PackageTypeDesc = :cDProduct1PackageTypeDesc"),
    @NamedQuery(name = "ItemsView.findByCDProduct1PackageQty", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1PackageQty = :cDProduct1PackageQty"),
    @NamedQuery(name = "ItemsView.findByCDProduct1PackageType", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1PackageType = :cDProduct1PackageType"),
    @NamedQuery(name = "ItemsView.findByCDProduct1ItemNetWeight", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1ItemNetWeight = :cDProduct1ItemNetWeight"),
    @NamedQuery(name = "ItemsView.findByCDProduct1ItemGrossWeight", query = "SELECT i FROM ItemsView i WHERE i.cDProduct1ItemGrossWeight = :cDProduct1ItemGrossWeight"),
    @NamedQuery(name = "ItemsView.findByEndCDProductDetails", query = "SELECT i FROM ItemsView i WHERE i.endCDProductDetails = :endCDProductDetails")})
public class ItemsView implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "CDProduct1_ItemDescription")
    private String cDProduct1ItemDescription;
    @Column(name = "CDProduct1_ItemHSCode")
    private String cDProduct1ItemHSCode;
    @Column(name = "CDProduct1_HSDescription")
    private String cDProduct1HSDescription;
    @Column(name = "CDProduct1_InternalProductNo")
    private String cDProduct1InternalProductNo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CDProduct1_Quantity_Qty")
    private Double cDProduct1QuantityQty;
    @Column(name = "CDProduct1_Quantity_UnitOfQty")
    private String cDProduct1QuantityUnitOfQty;
    @Column(name = "CDProduct1_Quantity_UnitOfQtyDesc")
    private String cDProduct1QuantityUnitOfQtyDesc;
    @Column(name = "CDProduct1_SupplementryQuantity_Qty")
    private Float cDProduct1SupplementryQuantityQty;
    @Basic(optional = false)
    @Column(name = "CDProduct1_SupplementryQuantity_UnitOfQty")
    private String cDProduct1SupplementryQuantityUnitOfQty;
    @Column(name = "CDProduct1_PackageTypeDesc")
    private String cDProduct1PackageTypeDesc;
    @Basic(optional = false)
    @Column(name = "CDProduct1_PackageQty")
    private int cDProduct1PackageQty;
    @Column(name = "CDProduct1_PackageType")
    private String cDProduct1PackageType;
    @Column(name = "CDProduct1_ItemNetWeight")
    private String cDProduct1ItemNetWeight;
    @Column(name = "CDProduct1_ItemGrossWeight")
    private String cDProduct1ItemGrossWeight;
    @Column(name = "EndCDProductDetails")
    private String endCDProductDetails;

    public ItemsView() {
    }

    public String getCDProduct1ItemDescription() {
        return cDProduct1ItemDescription;
    }

    public void setCDProduct1ItemDescription(String cDProduct1ItemDescription) {
        this.cDProduct1ItemDescription = cDProduct1ItemDescription;
    }

    public String getCDProduct1ItemHSCode() {
        return cDProduct1ItemHSCode;
    }

    public void setCDProduct1ItemHSCode(String cDProduct1ItemHSCode) {
        this.cDProduct1ItemHSCode = cDProduct1ItemHSCode;
    }

    public String getCDProduct1HSDescription() {
        return cDProduct1HSDescription;
    }

    public void setCDProduct1HSDescription(String cDProduct1HSDescription) {
        this.cDProduct1HSDescription = cDProduct1HSDescription;
    }

    public String getCDProduct1InternalProductNo() {
        return cDProduct1InternalProductNo;
    }

    public void setCDProduct1InternalProductNo(String cDProduct1InternalProductNo) {
        this.cDProduct1InternalProductNo = cDProduct1InternalProductNo;
    }

    public Double getCDProduct1QuantityQty() {
        return cDProduct1QuantityQty;
    }

    public void setCDProduct1QuantityQty(Double cDProduct1QuantityQty) {
        this.cDProduct1QuantityQty = cDProduct1QuantityQty;
    }

    public String getCDProduct1QuantityUnitOfQty() {
        return cDProduct1QuantityUnitOfQty;
    }

    public void setCDProduct1QuantityUnitOfQty(String cDProduct1QuantityUnitOfQty) {
        this.cDProduct1QuantityUnitOfQty = cDProduct1QuantityUnitOfQty;
    }

    public String getCDProduct1QuantityUnitOfQtyDesc() {
        return cDProduct1QuantityUnitOfQtyDesc;
    }

    public void setCDProduct1QuantityUnitOfQtyDesc(String cDProduct1QuantityUnitOfQtyDesc) {
        this.cDProduct1QuantityUnitOfQtyDesc = cDProduct1QuantityUnitOfQtyDesc;
    }

    public Float getCDProduct1SupplementryQuantityQty() {
        return cDProduct1SupplementryQuantityQty;
    }

    public void setCDProduct1SupplementryQuantityQty(Float cDProduct1SupplementryQuantityQty) {
        this.cDProduct1SupplementryQuantityQty = cDProduct1SupplementryQuantityQty;
    }

    public String getCDProduct1SupplementryQuantityUnitOfQty() {
        return cDProduct1SupplementryQuantityUnitOfQty;
    }

    public void setCDProduct1SupplementryQuantityUnitOfQty(String cDProduct1SupplementryQuantityUnitOfQty) {
        this.cDProduct1SupplementryQuantityUnitOfQty = cDProduct1SupplementryQuantityUnitOfQty;
    }

    public String getCDProduct1PackageTypeDesc() {
        return cDProduct1PackageTypeDesc;
    }

    public void setCDProduct1PackageTypeDesc(String cDProduct1PackageTypeDesc) {
        this.cDProduct1PackageTypeDesc = cDProduct1PackageTypeDesc;
    }

    public int getCDProduct1PackageQty() {
        return cDProduct1PackageQty;
    }

    public void setCDProduct1PackageQty(int cDProduct1PackageQty) {
        this.cDProduct1PackageQty = cDProduct1PackageQty;
    }

    public String getCDProduct1PackageType() {
        return cDProduct1PackageType;
    }

    public void setCDProduct1PackageType(String cDProduct1PackageType) {
        this.cDProduct1PackageType = cDProduct1PackageType;
    }

    public String getCDProduct1ItemNetWeight() {
        return cDProduct1ItemNetWeight;
    }

    public void setCDProduct1ItemNetWeight(String cDProduct1ItemNetWeight) {
        this.cDProduct1ItemNetWeight = cDProduct1ItemNetWeight;
    }

    public String getCDProduct1ItemGrossWeight() {
        return cDProduct1ItemGrossWeight;
    }

    public void setCDProduct1ItemGrossWeight(String cDProduct1ItemGrossWeight) {
        this.cDProduct1ItemGrossWeight = cDProduct1ItemGrossWeight;
    }

    public String getEndCDProductDetails() {
        return endCDProductDetails;
    }

    public void setEndCDProductDetails(String endCDProductDetails) {
        this.endCDProductDetails = endCDProductDetails;
    }
    
}
