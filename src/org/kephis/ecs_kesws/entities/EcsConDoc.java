/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kim
 */
@Entity
@Table(name = "condocview")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcsConDoc.findAll", query = "SELECT e FROM EcsConDoc e"),
    @NamedQuery(name = "EcsConDoc.findById", query = "SELECT e FROM EcsConDoc e WHERE e.id = :id"),
    @NamedQuery(name = "EcsConDoc.findByConsignmentId", query = "SELECT e FROM EcsConDoc e WHERE e.consignmentId = :consignmentId"),
    @NamedQuery(name = "EcsConDoc.findByDocumentNumber", query = "SELECT e FROM EcsConDoc e WHERE e.documentNumber = :documentNumber"),
    @NamedQuery(name = "EcsConDoc.findByStatus", query = "SELECT e FROM EcsConDoc e WHERE e.status = :status"),
    @NamedQuery(name = "EcsConDoc.findByConsignementApplicationDate", query = "SELECT e FROM EcsConDoc e WHERE e.consignementApplicationDate = :consignementApplicationDate"),
    @NamedQuery(name = "EcsConDoc.findByDocumentNr", query = "SELECT e FROM EcsConDoc e WHERE e.documentNr = :documentNr"),
    @NamedQuery(name = "EcsConDoc.findByInvoiceNr", query = "SELECT e FROM EcsConDoc e WHERE e.invoiceNr = :invoiceNr"),
    @NamedQuery(name = "EcsConDoc.findByVesselName", query = "SELECT e FROM EcsConDoc e WHERE e.vesselName = :vesselName"),
    @NamedQuery(name = "EcsConDoc.findByShippingOrder", query = "SELECT e FROM EcsConDoc e WHERE e.shippingOrder = :shippingOrder"),
    @NamedQuery(name = "EcsConDoc.findByUCRNo", query = "SELECT e FROM EcsConDoc e WHERE e.uCRNo = :uCRNo"),
    @NamedQuery(name = "EcsConDoc.findByShipmentDate", query = "SELECT e FROM EcsConDoc e WHERE e.shipmentDate = :shipmentDate"),
    @NamedQuery(name = "EcsConDoc.findByShipmentTime", query = "SELECT e FROM EcsConDoc e WHERE e.shipmentTime = :shipmentTime"),
    @NamedQuery(name = "EcsConDoc.findByMarksAndNumbers", query = "SELECT e FROM EcsConDoc e WHERE e.marksAndNumbers = :marksAndNumbers"),
    @NamedQuery(name = "EcsConDoc.findByCollectionOfficeId", query = "SELECT e FROM EcsConDoc e WHERE e.collectionOfficeId = :collectionOfficeId"),
    @NamedQuery(name = "EcsConDoc.findByPortOfArrival", query = "SELECT e FROM EcsConDoc e WHERE e.portOfArrival = :portOfArrival"),
    @NamedQuery(name = "EcsConDoc.findByPortOfArrivalDesc", query = "SELECT e FROM EcsConDoc e WHERE e.portOfArrivalDesc = :portOfArrivalDesc"),
    @NamedQuery(name = "EcsConDoc.findByPortOfDeparture", query = "SELECT e FROM EcsConDoc e WHERE e.portOfDeparture = :portOfDeparture"),
    @NamedQuery(name = "EcsConDoc.findByPortOfDepartureDesc", query = "SELECT e FROM EcsConDoc e WHERE e.portOfDepartureDesc = :portOfDepartureDesc"),
    @NamedQuery(name = "EcsConDoc.findByConsigneeCityCode", query = "SELECT e FROM EcsConDoc e WHERE e.consigneeCityCode = :consigneeCityCode"),
    @NamedQuery(name = "EcsConDoc.findByFirmName", query = "SELECT e FROM EcsConDoc e WHERE e.firmName = :firmName"),
    @NamedQuery(name = "EcsConDoc.findByClientCountry", query = "SELECT e FROM EcsConDoc e WHERE e.clientCountry = :clientCountry"),
    @NamedQuery(name = "EcsConDoc.findByClientCity", query = "SELECT e FROM EcsConDoc e WHERE e.clientCity = :clientCity"),
    @NamedQuery(name = "EcsConDoc.findByClientCntCode", query = "SELECT e FROM EcsConDoc e WHERE e.clientCntCode = :clientCntCode"),
    @NamedQuery(name = "EcsConDoc.findByClientPostNumber", query = "SELECT e FROM EcsConDoc e WHERE e.clientPostNumber = :clientPostNumber"),
    @NamedQuery(name = "EcsConDoc.findByClientPin", query = "SELECT e FROM EcsConDoc e WHERE e.clientPin = :clientPin"),
    @NamedQuery(name = "EcsConDoc.findByClientTel", query = "SELECT e FROM EcsConDoc e WHERE e.clientTel = :clientTel"),
    @NamedQuery(name = "EcsConDoc.findByClientFax", query = "SELECT e FROM EcsConDoc e WHERE e.clientFax = :clientFax"),
    @NamedQuery(name = "EcsConDoc.findByClientMail", query = "SELECT e FROM EcsConDoc e WHERE e.clientMail = :clientMail"),
    @NamedQuery(name = "EcsConDoc.findByClientWareHouseCode", query = "SELECT e FROM EcsConDoc e WHERE e.clientWareHouseCode = :clientWareHouseCode"),
    @NamedQuery(name = "EcsConDoc.findByConsigneeFirm", query = "SELECT e FROM EcsConDoc e WHERE e.consigneeFirm = :consigneeFirm"),
    @NamedQuery(name = "EcsConDoc.findByConsigneePhysicalAddress", query = "SELECT e FROM EcsConDoc e WHERE e.consigneePhysicalAddress = :consigneePhysicalAddress"),
    @NamedQuery(name = "EcsConDoc.findByConsigneePhysicalAddress2", query = "SELECT e FROM EcsConDoc e WHERE e.consigneePhysicalAddress2 = :consigneePhysicalAddress2"),
    @NamedQuery(name = "EcsConDoc.findByConsigneePostalCode", query = "SELECT e FROM EcsConDoc e WHERE e.consigneePostalCode = :consigneePostalCode"),
    @NamedQuery(name = "EcsConDoc.findByConsigneeCountryIso", query = "SELECT e FROM EcsConDoc e WHERE e.consigneeCountryIso = :consigneeCountryIso"),
    @NamedQuery(name = "EcsConDoc.findByConsigneeCountry", query = "SELECT e FROM EcsConDoc e WHERE e.consigneeCountry = :consigneeCountry"),
    @NamedQuery(name = "EcsConDoc.findByConsigneeCity", query = "SELECT e FROM EcsConDoc e WHERE e.consigneeCity = :consigneeCity"),
    @NamedQuery(name = "EcsConDoc.findByHscode", query = "SELECT e FROM EcsConDoc e WHERE e.hscode = :hscode"),
    @NamedQuery(name = "EcsConDoc.findByHscode2", query = "SELECT e FROM EcsConDoc e WHERE e.hscode2 = :hscode2"),
    @NamedQuery(name = "EcsConDoc.findByHscodeDesc", query = "SELECT e FROM EcsConDoc e WHERE e.hscodeDesc = :hscodeDesc"),
    @NamedQuery(name = "EcsConDoc.findByHscodeDesc2", query = "SELECT e FROM EcsConDoc e WHERE e.hscodeDesc2 = :hscodeDesc2"),
    @NamedQuery(name = "EcsConDoc.findByConsignmentUnitName", query = "SELECT e FROM EcsConDoc e WHERE e.consignmentUnitName = :consignmentUnitName"),
    @NamedQuery(name = "EcsConDoc.findByVaqNumberOfPackages", query = "SELECT e FROM EcsConDoc e WHERE e.vaqNumberOfPackages = :vaqNumberOfPackages"),
    @NamedQuery(name = "EcsConDoc.findByVaqPackagesWeight", query = "SELECT e FROM EcsConDoc e WHERE e.vaqPackagesWeight = :vaqPackagesWeight"),
    @NamedQuery(name = "EcsConDoc.findByFreightStation", query = "SELECT e FROM EcsConDoc e WHERE e.freightStation = :freightStation"),
    @NamedQuery(name = "EcsConDoc.findByPreferedInspectionDate", query = "SELECT e FROM EcsConDoc e WHERE e.preferedInspectionDate = :preferedInspectionDate"),
    @NamedQuery(name = "EcsConDoc.findByUnitCode", query = "SELECT e FROM EcsConDoc e WHERE e.unitCode = :unitCode"),
    @NamedQuery(name = "EcsConDoc.findByUnitName", query = "SELECT e FROM EcsConDoc e WHERE e.unitName = :unitName"),
    @NamedQuery(name = "EcsConDoc.findByUnitName2", query = "SELECT e FROM EcsConDoc e WHERE e.unitName2 = :unitName2"),
    @NamedQuery(name = "EcsConDoc.findByCommodityForm", query = "SELECT e FROM EcsConDoc e WHERE e.commodityForm = :commodityForm"),
    @NamedQuery(name = "EcsConDoc.findByPackaging", query = "SELECT e FROM EcsConDoc e WHERE e.packaging = :packaging"),
    @NamedQuery(name = "EcsConDoc.findByPackageCode", query = "SELECT e FROM EcsConDoc e WHERE e.packageCode = :packageCode"),
    @NamedQuery(name = "EcsConDoc.findByPackageName", query = "SELECT e FROM EcsConDoc e WHERE e.packageName = :packageName"),
    @NamedQuery(name = "EcsConDoc.findByCommonName", query = "SELECT e FROM EcsConDoc e WHERE e.commonName = :commonName"),
    @NamedQuery(name = "EcsConDoc.findByBotanicalName", query = "SELECT e FROM EcsConDoc e WHERE e.botanicalName = :botanicalName"),
    @NamedQuery(name = "EcsConDoc.findByInternalProductCode", query = "SELECT e FROM EcsConDoc e WHERE e.internalProductCode = :internalProductCode"),
    @NamedQuery(name = "EcsConDoc.findByClassCode", query = "SELECT e FROM EcsConDoc e WHERE e.classCode = :classCode"),
    @NamedQuery(name = "EcsConDoc.findByClassName", query = "SELECT e FROM EcsConDoc e WHERE e.className = :className"),
    @NamedQuery(name = "EcsConDoc.findByTransMode", query = "SELECT e FROM EcsConDoc e WHERE e.transMode = :transMode")})
public class EcsConDoc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    @Id
    private String id;
    @Basic(optional = false)
    @Column(name = "consignment_Id")
    private int consignmentId;
    @Column(name = "DocumentNumber")
    private String documentNumber;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "consignement_application_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date consignementApplicationDate;
    @Column(name = "document_nr")
    private String documentNr;
    @Basic(optional = false)
    @Column(name = "invoice_nr")
    private String invoiceNr;
    @Column(name = "vessel_name")
    private String vesselName;
    @Column(name = "shipping_order")
    private String shippingOrder;
    @Column(name = "UCRNo")
    private String uCRNo;
    @Basic(optional = false)
    @Column(name = "shipment_date")
    @Temporal(TemporalType.DATE)
    private Date shipmentDate;
    @Basic(optional = false)
    @Column(name = "shipment_time")
    @Temporal(TemporalType.TIME)
    private Date shipmentTime;
    @Basic(optional = false)
    @Column(name = "MarksAndNumbers")
    private String marksAndNumbers;
    @Column(name = "collection_office_id")
    private Integer collectionOfficeId;
    @Column(name = "PortOfArrival")
    private String portOfArrival;
    @Column(name = "PortOfArrivalDesc")
    private String portOfArrivalDesc;
    @Column(name = "PortOfDeparture")
    private String portOfDeparture;
    @Column(name = "PortOfDepartureDesc")
    private String portOfDepartureDesc;
    @Column(name = "consignee_city_code")
    private String consigneeCityCode;
    @Column(name = "firm_name")
    private String firmName;
    @Column(name = "client_country")
    private String clientCountry;
    @Column(name = "client_city")
    private String clientCity;
    @Column(name = "client_cnt_code")
    private String clientCntCode;
    @Column(name = "client_post_number")
    private String clientPostNumber;
    @Column(name = "client_pin")
    private String clientPin;
    @Column(name = "client_tel")
    private String clientTel;
    @Column(name = "client_fax")
    private String clientFax;
    @Column(name = "client_mail")
    private String clientMail;
    @Column(name = "client_ware_house_code")
    private String clientWareHouseCode;
    @Column(name = "consignee_firm")
    private String consigneeFirm;
    @Column(name = "consignee_physical_address")
    private String consigneePhysicalAddress;
    @Column(name = "consignee_physical_address_2")
    private String consigneePhysicalAddress2;
    @Column(name = "consignee_postal_code")
    private String consigneePostalCode;
    @Column(name = "consignee_country_iso")
    private String consigneeCountryIso;
    @Column(name = "consignee_country")
    private String consigneeCountry;
    @Column(name = "consignee_city")
    private String consigneeCity;
    @Column(name = "HSCODE")
    private String hscode;
    @Column(name = "HSCODE2")
    private String hscode2;
    @Column(name = "HSCODE_DESC")
    private String hscodeDesc;
    @Column(name = "HSCODE_DESC2")
    private String hscodeDesc2;
    @Column(name = "consignment_unit_name")
    private String consignmentUnitName;
    @Column(name = "vaq_number_of_packages")
    private Integer vaqNumberOfPackages;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "vaq_packages_weight")
    private Float vaqPackagesWeight;
    @Column(name = "FreightStation")
    private String freightStation;
    @Column(name = "prefered_inspection_date")
    @Temporal(TemporalType.DATE)
    private Date preferedInspectionDate;
    @Column(name = "unit_code")
    private String unitCode;
    @Column(name = "unit_name")
    private String unitName;
    @Column(name = "unit_name_2")
    private String unitName2;
    @Column(name = "commodity_form")
    private String commodityForm;
    @Column(name = "packaging")
    private String packaging;
    @Column(name = "package_code")
    private String packageCode;
    @Column(name = "package_name")
    private String packageName;
    @Column(name = "common_name")
    private String commonName;
    @Column(name = "botanical_name")
    private String botanicalName;
    @Column(name = "InternalProductCode")
    private String internalProductCode;
    @Column(name = "class_code")
    private String classCode;
    @Column(name = "class_name")
    private String className;
    @Column(name = "trans_mode")
    private String transMode;

    public EcsConDoc() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(int consignmentId) {
        this.consignmentId = consignmentId;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getConsignementApplicationDate() {
        return consignementApplicationDate;
    }

    public void setConsignementApplicationDate(Date consignementApplicationDate) {
        this.consignementApplicationDate = consignementApplicationDate;
    }

    public String getDocumentNr() {
        return documentNr;
    }

    public void setDocumentNr(String documentNr) {
        this.documentNr = documentNr;
    }

    public String getInvoiceNr() {
        return invoiceNr;
    }

    public void setInvoiceNr(String invoiceNr) {
        this.invoiceNr = invoiceNr;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(String shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public String getUCRNo() {
        return uCRNo;
    }

    public void setUCRNo(String uCRNo) {
        this.uCRNo = uCRNo;
    }

    public Date getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public Date getShipmentTime() {
        return shipmentTime;
    }

    public void setShipmentTime(Date shipmentTime) {
        this.shipmentTime = shipmentTime;
    }

    public String getMarksAndNumbers() {
        return marksAndNumbers;
    }

    public void setMarksAndNumbers(String marksAndNumbers) {
        this.marksAndNumbers = marksAndNumbers;
    }

    public Integer getCollectionOfficeId() {
        return collectionOfficeId;
    }

    public void setCollectionOfficeId(Integer collectionOfficeId) {
        this.collectionOfficeId = collectionOfficeId;
    }

    public String getPortOfArrival() {
        return portOfArrival;
    }

    public void setPortOfArrival(String portOfArrival) {
        this.portOfArrival = portOfArrival;
    }

    public String getPortOfArrivalDesc() {
        return portOfArrivalDesc;
    }

    public void setPortOfArrivalDesc(String portOfArrivalDesc) {
        this.portOfArrivalDesc = portOfArrivalDesc;
    }

    public String getPortOfDeparture() {
        return portOfDeparture;
    }

    public void setPortOfDeparture(String portOfDeparture) {
        this.portOfDeparture = portOfDeparture;
    }

    public String getPortOfDepartureDesc() {
        return portOfDepartureDesc;
    }

    public void setPortOfDepartureDesc(String portOfDepartureDesc) {
        this.portOfDepartureDesc = portOfDepartureDesc;
    }

    public String getConsigneeCityCode() {
        return consigneeCityCode;
    }

    public void setConsigneeCityCode(String consigneeCityCode) {
        this.consigneeCityCode = consigneeCityCode;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getClientCountry() {
        return clientCountry;
    }

    public void setClientCountry(String clientCountry) {
        this.clientCountry = clientCountry;
    }

    public String getClientCity() {
        return clientCity;
    }

    public void setClientCity(String clientCity) {
        this.clientCity = clientCity;
    }

    public String getClientCntCode() {
        return clientCntCode;
    }

    public void setClientCntCode(String clientCntCode) {
        this.clientCntCode = clientCntCode;
    }

    public String getClientPostNumber() {
        return clientPostNumber;
    }

    public void setClientPostNumber(String clientPostNumber) {
        this.clientPostNumber = clientPostNumber;
    }

    public String getClientPin() {
        return clientPin;
    }

    public void setClientPin(String clientPin) {
        this.clientPin = clientPin;
    }

    public String getClientTel() {
        return clientTel;
    }

    public void setClientTel(String clientTel) {
        this.clientTel = clientTel;
    }

    public String getClientFax() {
        return clientFax;
    }

    public void setClientFax(String clientFax) {
        this.clientFax = clientFax;
    }

    public String getClientMail() {
        return clientMail;
    }

    public void setClientMail(String clientMail) {
        this.clientMail = clientMail;
    }

    public String getClientWareHouseCode() {
        return clientWareHouseCode;
    }

    public void setClientWareHouseCode(String clientWareHouseCode) {
        this.clientWareHouseCode = clientWareHouseCode;
    }

    public String getConsigneeFirm() {
        return consigneeFirm;
    }

    public void setConsigneeFirm(String consigneeFirm) {
        this.consigneeFirm = consigneeFirm;
    }

    public String getConsigneePhysicalAddress() {
        return consigneePhysicalAddress;
    }

    public void setConsigneePhysicalAddress(String consigneePhysicalAddress) {
        this.consigneePhysicalAddress = consigneePhysicalAddress;
    }

    public String getConsigneePhysicalAddress2() {
        return consigneePhysicalAddress2;
    }

    public void setConsigneePhysicalAddress2(String consigneePhysicalAddress2) {
        this.consigneePhysicalAddress2 = consigneePhysicalAddress2;
    }

    public String getConsigneePostalCode() {
        return consigneePostalCode;
    }

    public void setConsigneePostalCode(String consigneePostalCode) {
        this.consigneePostalCode = consigneePostalCode;
    }

    public String getConsigneeCountryIso() {
        return consigneeCountryIso;
    }

    public void setConsigneeCountryIso(String consigneeCountryIso) {
        this.consigneeCountryIso = consigneeCountryIso;
    }

    public String getConsigneeCountry() {
        return consigneeCountry;
    }

    public void setConsigneeCountry(String consigneeCountry) {
        this.consigneeCountry = consigneeCountry;
    }

    public String getConsigneeCity() {
        return consigneeCity;
    }

    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
    }

    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    public String getHscode2() {
        return hscode2;
    }

    public void setHscode2(String hscode2) {
        this.hscode2 = hscode2;
    }

    public String getHscodeDesc() {
        return hscodeDesc;
    }

    public void setHscodeDesc(String hscodeDesc) {
        this.hscodeDesc = hscodeDesc;
    }

    public String getHscodeDesc2() {
        return hscodeDesc2;
    }

    public void setHscodeDesc2(String hscodeDesc2) {
        this.hscodeDesc2 = hscodeDesc2;
    }

    public String getConsignmentUnitName() {
        return consignmentUnitName;
    }

    public void setConsignmentUnitName(String consignmentUnitName) {
        this.consignmentUnitName = consignmentUnitName;
    }

    public Integer getVaqNumberOfPackages() {
        return vaqNumberOfPackages;
    }

    public void setVaqNumberOfPackages(Integer vaqNumberOfPackages) {
        this.vaqNumberOfPackages = vaqNumberOfPackages;
    }

    public Float getVaqPackagesWeight() {
        return vaqPackagesWeight;
    }

    public void setVaqPackagesWeight(Float vaqPackagesWeight) {
        this.vaqPackagesWeight = vaqPackagesWeight;
    }

    public String getFreightStation() {
        return freightStation;
    }

    public void setFreightStation(String freightStation) {
        this.freightStation = freightStation;
    }

    public Date getPreferedInspectionDate() {
        return preferedInspectionDate;
    }

    public void setPreferedInspectionDate(Date preferedInspectionDate) {
        this.preferedInspectionDate = preferedInspectionDate;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitName2() {
        return unitName2;
    }

    public void setUnitName2(String unitName2) {
        this.unitName2 = unitName2;
    }

    public String getCommodityForm() {
        return commodityForm;
    }

    public void setCommodityForm(String commodityForm) {
        this.commodityForm = commodityForm;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getBotanicalName() {
        return botanicalName;
    }

    public void setBotanicalName(String botanicalName) {
        this.botanicalName = botanicalName;
    }

    public String getInternalProductCode() {
        return internalProductCode;
    }

    public void setInternalProductCode(String internalProductCode) {
        this.internalProductCode = internalProductCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }
    
}
