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
    @NamedQuery(name = "EcsConDoc.findByExporterSenderName", query = "SELECT e FROM EcsConDoc e WHERE e.exporterSenderName = :exporterSenderName"),
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
    @NamedQuery(name = "EcsConDoc.findByTotalConsignmentWeight", query = "SELECT e FROM EcsConDoc e WHERE e.totalConsignmentWeight = :totalConsignmentWeight"),
    @NamedQuery(name = "EcsConDoc.findByCollectionOfficeId", query = "SELECT e FROM EcsConDoc e WHERE e.collectionOfficeId = :collectionOfficeId"),
    @NamedQuery(name = "EcsConDoc.findByPortOfArrival", query = "SELECT e FROM EcsConDoc e WHERE e.portOfArrival = :portOfArrival"),
    @NamedQuery(name = "EcsConDoc.findByPortOfArrivalDesc", query = "SELECT e FROM EcsConDoc e WHERE e.portOfArrivalDesc = :portOfArrivalDesc"),
    @NamedQuery(name = "EcsConDoc.findByPortOfDeparture", query = "SELECT e FROM EcsConDoc e WHERE e.portOfDeparture = :portOfDeparture"),
    @NamedQuery(name = "EcsConDoc.findByPortOfDepartureDesc", query = "SELECT e FROM EcsConDoc e WHERE e.portOfDepartureDesc = :portOfDepartureDesc"),
    @NamedQuery(name = "EcsConDoc.findByExporterFirmNameCityCode", query = "SELECT e FROM EcsConDoc e WHERE e.exporterFirmNameCityCode = :exporterFirmNameCityCode"),
    @NamedQuery(name = "EcsConDoc.findByExporterFirmName", query = "SELECT e FROM EcsConDoc e WHERE e.exporterFirmName = :exporterFirmName"),
    @NamedQuery(name = "EcsConDoc.findByExporterCountry", query = "SELECT e FROM EcsConDoc e WHERE e.exporterCountry = :exporterCountry"),
    @NamedQuery(name = "EcsConDoc.findByExporterCity", query = "SELECT e FROM EcsConDoc e WHERE e.exporterCity = :exporterCity"),
    @NamedQuery(name = "EcsConDoc.findByExporterCntCode", query = "SELECT e FROM EcsConDoc e WHERE e.exporterCntCode = :exporterCntCode"),
    @NamedQuery(name = "EcsConDoc.findByExporterPostNumber", query = "SELECT e FROM EcsConDoc e WHERE e.exporterPostNumber = :exporterPostNumber"),
    @NamedQuery(name = "EcsConDoc.findByExporterPin", query = "SELECT e FROM EcsConDoc e WHERE e.exporterPin = :exporterPin"),
    @NamedQuery(name = "EcsConDoc.findByExporterTel", query = "SELECT e FROM EcsConDoc e WHERE e.exporterTel = :exporterTel"),
    @NamedQuery(name = "EcsConDoc.findByExporterFax", query = "SELECT e FROM EcsConDoc e WHERE e.exporterFax = :exporterFax"),
    @NamedQuery(name = "EcsConDoc.findByExporterMail", query = "SELECT e FROM EcsConDoc e WHERE e.exporterMail = :exporterMail"),
    @NamedQuery(name = "EcsConDoc.findByExporterWareHouseCode", query = "SELECT e FROM EcsConDoc e WHERE e.exporterWareHouseCode = :exporterWareHouseCode"),
    @NamedQuery(name = "EcsConDoc.findByExporterPhysicalAddress", query = "SELECT e FROM EcsConDoc e WHERE e.exporterPhysicalAddress = :exporterPhysicalAddress"),
    @NamedQuery(name = "EcsConDoc.findByImporterFirm", query = "SELECT e FROM EcsConDoc e WHERE e.importerFirm = :importerFirm"),
    @NamedQuery(name = "EcsConDoc.findByImporterPhysicalAddress", query = "SELECT e FROM EcsConDoc e WHERE e.importerPhysicalAddress = :importerPhysicalAddress"),
    @NamedQuery(name = "EcsConDoc.findByImporterPhysicalAddress2", query = "SELECT e FROM EcsConDoc e WHERE e.importerPhysicalAddress2 = :importerPhysicalAddress2"),
    @NamedQuery(name = "EcsConDoc.findByImporterPostalCode", query = "SELECT e FROM EcsConDoc e WHERE e.importerPostalCode = :importerPostalCode"),
    @NamedQuery(name = "EcsConDoc.findByImporterCountryIso", query = "SELECT e FROM EcsConDoc e WHERE e.importerCountryIso = :importerCountryIso"),
    @NamedQuery(name = "EcsConDoc.findByImporterCountry", query = "SELECT e FROM EcsConDoc e WHERE e.importerCountry = :importerCountry"),
    @NamedQuery(name = "EcsConDoc.findByImporterCity", query = "SELECT e FROM EcsConDoc e WHERE e.importerCity = :importerCity"),
    @NamedQuery(name = "EcsConDoc.findByFreightStation", query = "SELECT e FROM EcsConDoc e WHERE e.freightStation = :freightStation"),
    @NamedQuery(name = "EcsConDoc.findByPreferedInspectionDate", query = "SELECT e FROM EcsConDoc e WHERE e.preferedInspectionDate = :preferedInspectionDate"),
    @NamedQuery(name = "EcsConDoc.findByHscode", query = "SELECT e FROM EcsConDoc e WHERE e.hscode = :hscode"),
    @NamedQuery(name = "EcsConDoc.findByHscodeDesc", query = "SELECT e FROM EcsConDoc e WHERE e.hscodeDesc = :hscodeDesc"),
    @NamedQuery(name = "EcsConDoc.findByHscode2", query = "SELECT e FROM EcsConDoc e WHERE e.hscode2 = :hscode2"),
    @NamedQuery(name = "EcsConDoc.findByHscodeDesc2", query = "SELECT e FROM EcsConDoc e WHERE e.hscodeDesc2 = :hscodeDesc2"),
    @NamedQuery(name = "EcsConDoc.findByItemUnitQuantity", query = "SELECT e FROM EcsConDoc e WHERE e.itemUnitQuantity = :itemUnitQuantity"),
    @NamedQuery(name = "EcsConDoc.findByItemUnit", query = "SELECT e FROM EcsConDoc e WHERE e.itemUnit = :itemUnit"),
    @NamedQuery(name = "EcsConDoc.findByItemSupUnitQuantity", query = "SELECT e FROM EcsConDoc e WHERE e.itemSupUnitQuantity = :itemSupUnitQuantity"),
    @NamedQuery(name = "EcsConDoc.findByItemUnit2", query = "SELECT e FROM EcsConDoc e WHERE e.itemUnit2 = :itemUnit2"),
    @NamedQuery(name = "EcsConDoc.findByKeswsUnitCode", query = "SELECT e FROM EcsConDoc e WHERE e.keswsUnitCode = :keswsUnitCode"),
    @NamedQuery(name = "EcsConDoc.findByKeswsUnitName", query = "SELECT e FROM EcsConDoc e WHERE e.keswsUnitName = :keswsUnitName"),
    @NamedQuery(name = "EcsConDoc.findByKeswsUnitCode2", query = "SELECT e FROM EcsConDoc e WHERE e.keswsUnitCode2 = :keswsUnitCode2"),
    @NamedQuery(name = "EcsConDoc.findByKeswsUnitName2", query = "SELECT e FROM EcsConDoc e WHERE e.keswsUnitName2 = :keswsUnitName2"),
    @NamedQuery(name = "EcsConDoc.findByItemCommodityForm", query = "SELECT e FROM EcsConDoc e WHERE e.itemCommodityForm = :itemCommodityForm"),
    @NamedQuery(name = "EcsConDoc.findByItemPackaging", query = "SELECT e FROM EcsConDoc e WHERE e.itemPackaging = :itemPackaging"),
    @NamedQuery(name = "EcsConDoc.findByItemNumberOfPackages", query = "SELECT e FROM EcsConDoc e WHERE e.itemNumberOfPackages = :itemNumberOfPackages"),
    @NamedQuery(name = "EcsConDoc.findByKeswsPackageCode", query = "SELECT e FROM EcsConDoc e WHERE e.keswsPackageCode = :keswsPackageCode"),
    @NamedQuery(name = "EcsConDoc.findByKeswsPackageName", query = "SELECT e FROM EcsConDoc e WHERE e.keswsPackageName = :keswsPackageName"),
    @NamedQuery(name = "EcsConDoc.findByItemCommonName", query = "SELECT e FROM EcsConDoc e WHERE e.itemCommonName = :itemCommonName"),
    @NamedQuery(name = "EcsConDoc.findByItemBotanicalName", query = "SELECT e FROM EcsConDoc e WHERE e.itemBotanicalName = :itemBotanicalName"),
    @NamedQuery(name = "EcsConDoc.findByItemInternalProductCode", query = "SELECT e FROM EcsConDoc e WHERE e.itemInternalProductCode = :itemInternalProductCode"),
    @NamedQuery(name = "EcsConDoc.findByTransMode", query = "SELECT e FROM EcsConDoc e WHERE e.transMode = :transMode"),
    @NamedQuery(name = "EcsConDoc.findByKeswsItemClass", query = "SELECT e FROM EcsConDoc e WHERE e.keswsItemClass = :keswsItemClass"),
    @NamedQuery(name = "EcsConDoc.findByKeswsItemClassDesc", query = "SELECT e FROM EcsConDoc e WHERE e.keswsItemClassDesc = :keswsItemClassDesc"),
    @NamedQuery(name = "EcsConDoc.findByAdditionalField1", query = "SELECT e FROM EcsConDoc e WHERE e.additionalField1 = :additionalField1"),
    @NamedQuery(name = "EcsConDoc.findByAdditionalField2", query = "SELECT e FROM EcsConDoc e WHERE e.additionalField2 = :additionalField2")})
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
    @Column(name = "exporter_sender_name")
    private String exporterSenderName;
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
    @Basic(optional = false)
    @Column(name = "TotalConsignmentWeight")
    private float totalConsignmentWeight;
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
    @Column(name = "exporter_firm_name_city_code")
    private String exporterFirmNameCityCode;
    @Basic(optional = false)
    @Column(name = "exporter_firm_name")
    private String exporterFirmName;
    @Column(name = "exporter_country")
    private String exporterCountry;
    @Column(name = "exporter_city")
    private String exporterCity;
    @Basic(optional = false)
    @Column(name = "exporter_cnt_code")
    private String exporterCntCode;
    @Basic(optional = false)
    @Column(name = "exporter_post_number")
    private String exporterPostNumber;
    @Basic(optional = false)
    @Column(name = "exporter_pin")
    private String exporterPin;
    @Basic(optional = false)
    @Column(name = "exporter_tel")
    private String exporterTel;
    @Basic(optional = false)
    @Column(name = "exporter_fax")
    private String exporterFax;
    @Basic(optional = false)
    @Column(name = "exporter_mail")
    private String exporterMail;
    @Basic(optional = false)
    @Column(name = "exporter_ware_house_code")
    private String exporterWareHouseCode;
    @Column(name = "exporter_physical_address")
    private String exporterPhysicalAddress;
    @Basic(optional = false)
    @Column(name = "importer_firm")
    private String importerFirm;
    @Basic(optional = false)
    @Column(name = "importer_physical_address")
    private String importerPhysicalAddress;
    @Basic(optional = false)
    @Column(name = "importer_physical_address_2")
    private String importerPhysicalAddress2;
    @Basic(optional = false)
    @Column(name = "importer_postal_code")
    private String importerPostalCode;
    @Column(name = "importer_country_iso")
    private String importerCountryIso;
    @Column(name = "importer_country")
    private String importerCountry;
    @Column(name = "importer_city")
    private String importerCity;
    @Column(name = "FreightStation")
    private String freightStation;
    @Column(name = "prefered_inspection_date")
    @Temporal(TemporalType.DATE)
    private Date preferedInspectionDate;
    @Column(name = "HSCODE")
    private String hscode;
    @Column(name = "HSCODE_DESC")
    private String hscodeDesc;
    @Column(name = "HSCODE_2")
    private String hscode2;
    @Column(name = "HSCODE_DESC_2")
    private String hscodeDesc2;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "item_unit_quantity")
    private Float itemUnitQuantity;
    @Column(name = "item_unit")
    private String itemUnit;
    @Column(name = "item_sup_unit_quantity")
    private Float itemSupUnitQuantity;
    @Column(name = "item_unit_2")
    private String itemUnit2;
    @Column(name = "kesws_unit_code")
    private String keswsUnitCode;
    @Column(name = "kesws_unit_name")
    private String keswsUnitName;
    @Column(name = "kesws_unit_code_2")
    private String keswsUnitCode2;
    @Column(name = "kesws_unit_name_2")
    private String keswsUnitName2;
    @Basic(optional = false)
    @Column(name = "item_commodity_form")
    private String itemCommodityForm;
    @Basic(optional = false)
    @Column(name = "item_packaging")
    private String itemPackaging;
    @Basic(optional = false)
    @Column(name = "item_number_of_packages")
    private int itemNumberOfPackages;
    @Column(name = "kesws_package_code")
    private String keswsPackageCode;
    @Column(name = "kesws_package_name")
    private String keswsPackageName;
    @Basic(optional = false)
    @Column(name = "item_common_name")
    private String itemCommonName;
    @Basic(optional = false)
    @Column(name = "item_botanical_name")
    private String itemBotanicalName;
    @Column(name = "Item_InternalProductCode")
    private String itemInternalProductCode;
    @Column(name = "trans_mode")
    private String transMode;
    @Column(name = "kesws_item_class")
    private String keswsItemClass;
    @Column(name = "kesws_item_class_desc")
    private String keswsItemClassDesc;
    @Basic(optional = false)
    @Column(name = "additional_field_1")
    private String additionalField1;
    @Column(name = "additional_field_2")
    private String additionalField2;

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

    public String getExporterSenderName() {
        return exporterSenderName;
    }

    public void setExporterSenderName(String exporterSenderName) {
        this.exporterSenderName = exporterSenderName;
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

    public float getTotalConsignmentWeight() {
        return totalConsignmentWeight;
    }

    public void setTotalConsignmentWeight(float totalConsignmentWeight) {
        this.totalConsignmentWeight = totalConsignmentWeight;
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

    public String getExporterFirmNameCityCode() {
        return exporterFirmNameCityCode;
    }

    public void setExporterFirmNameCityCode(String exporterFirmNameCityCode) {
        this.exporterFirmNameCityCode = exporterFirmNameCityCode;
    }

    public String getExporterFirmName() {
        return exporterFirmName;
    }

    public void setExporterFirmName(String exporterFirmName) {
        this.exporterFirmName = exporterFirmName;
    }

    public String getExporterCountry() {
        return exporterCountry;
    }

    public void setExporterCountry(String exporterCountry) {
        this.exporterCountry = exporterCountry;
    }

    public String getExporterCity() {
        return exporterCity;
    }

    public void setExporterCity(String exporterCity) {
        this.exporterCity = exporterCity;
    }

    public String getExporterCntCode() {
        return exporterCntCode;
    }

    public void setExporterCntCode(String exporterCntCode) {
        this.exporterCntCode = exporterCntCode;
    }

    public String getExporterPostNumber() {
        return exporterPostNumber;
    }

    public void setExporterPostNumber(String exporterPostNumber) {
        this.exporterPostNumber = exporterPostNumber;
    }

    public String getExporterPin() {
        return exporterPin;
    }

    public void setExporterPin(String exporterPin) {
        this.exporterPin = exporterPin;
    }

    public String getExporterTel() {
        return exporterTel;
    }

    public void setExporterTel(String exporterTel) {
        this.exporterTel = exporterTel;
    }

    public String getExporterFax() {
        return exporterFax;
    }

    public void setExporterFax(String exporterFax) {
        this.exporterFax = exporterFax;
    }

    public String getExporterMail() {
        return exporterMail;
    }

    public void setExporterMail(String exporterMail) {
        this.exporterMail = exporterMail;
    }

    public String getExporterWareHouseCode() {
        return exporterWareHouseCode;
    }

    public void setExporterWareHouseCode(String exporterWareHouseCode) {
        this.exporterWareHouseCode = exporterWareHouseCode;
    }

    public String getExporterPhysicalAddress() {
        return exporterPhysicalAddress;
    }

    public void setExporterPhysicalAddress(String exporterPhysicalAddress) {
        this.exporterPhysicalAddress = exporterPhysicalAddress;
    }

    public String getImporterFirm() {
        return importerFirm;
    }

    public void setImporterFirm(String importerFirm) {
        this.importerFirm = importerFirm;
    }

    public String getImporterPhysicalAddress() {
        return importerPhysicalAddress;
    }

    public void setImporterPhysicalAddress(String importerPhysicalAddress) {
        this.importerPhysicalAddress = importerPhysicalAddress;
    }

    public String getImporterPhysicalAddress2() {
        return importerPhysicalAddress2;
    }

    public void setImporterPhysicalAddress2(String importerPhysicalAddress2) {
        this.importerPhysicalAddress2 = importerPhysicalAddress2;
    }

    public String getImporterPostalCode() {
        return importerPostalCode;
    }

    public void setImporterPostalCode(String importerPostalCode) {
        this.importerPostalCode = importerPostalCode;
    }

    public String getImporterCountryIso() {
        return importerCountryIso;
    }

    public void setImporterCountryIso(String importerCountryIso) {
        this.importerCountryIso = importerCountryIso;
    }

    public String getImporterCountry() {
        return importerCountry;
    }

    public void setImporterCountry(String importerCountry) {
        this.importerCountry = importerCountry;
    }

    public String getImporterCity() {
        return importerCity;
    }

    public void setImporterCity(String importerCity) {
        this.importerCity = importerCity;
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

    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    public String getHscodeDesc() {
        return hscodeDesc;
    }

    public void setHscodeDesc(String hscodeDesc) {
        this.hscodeDesc = hscodeDesc;
    }

    public String getHscode2() {
        return hscode2;
    }

    public void setHscode2(String hscode2) {
        this.hscode2 = hscode2;
    }

    public String getHscodeDesc2() {
        return hscodeDesc2;
    }

    public void setHscodeDesc2(String hscodeDesc2) {
        this.hscodeDesc2 = hscodeDesc2;
    }

    public Float getItemUnitQuantity() {
        return itemUnitQuantity;
    }

    public void setItemUnitQuantity(Float itemUnitQuantity) {
        this.itemUnitQuantity = itemUnitQuantity;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public Float getItemSupUnitQuantity() {
        return itemSupUnitQuantity;
    }

    public void setItemSupUnitQuantity(Float itemSupUnitQuantity) {
        this.itemSupUnitQuantity = itemSupUnitQuantity;
    }

    public String getItemUnit2() {
        return itemUnit2;
    }

    public void setItemUnit2(String itemUnit2) {
        this.itemUnit2 = itemUnit2;
    }

    public String getKeswsUnitCode() {
        return keswsUnitCode;
    }

    public void setKeswsUnitCode(String keswsUnitCode) {
        this.keswsUnitCode = keswsUnitCode;
    }

    public String getKeswsUnitName() {
        return keswsUnitName;
    }

    public void setKeswsUnitName(String keswsUnitName) {
        this.keswsUnitName = keswsUnitName;
    }

    public String getKeswsUnitCode2() {
        return keswsUnitCode2;
    }

    public void setKeswsUnitCode2(String keswsUnitCode2) {
        this.keswsUnitCode2 = keswsUnitCode2;
    }

    public String getKeswsUnitName2() {
        return keswsUnitName2;
    }

    public void setKeswsUnitName2(String keswsUnitName2) {
        this.keswsUnitName2 = keswsUnitName2;
    }

    public String getItemCommodityForm() {
        return itemCommodityForm;
    }

    public void setItemCommodityForm(String itemCommodityForm) {
        this.itemCommodityForm = itemCommodityForm;
    }

    public String getItemPackaging() {
        return itemPackaging;
    }

    public void setItemPackaging(String itemPackaging) {
        this.itemPackaging = itemPackaging;
    }

    public int getItemNumberOfPackages() {
        return itemNumberOfPackages;
    }

    public void setItemNumberOfPackages(int itemNumberOfPackages) {
        this.itemNumberOfPackages = itemNumberOfPackages;
    }

    public String getKeswsPackageCode() {
        return keswsPackageCode;
    }

    public void setKeswsPackageCode(String keswsPackageCode) {
        this.keswsPackageCode = keswsPackageCode;
    }

    public String getKeswsPackageName() {
        return keswsPackageName;
    }

    public void setKeswsPackageName(String keswsPackageName) {
        this.keswsPackageName = keswsPackageName;
    }

    public String getItemCommonName() {
        return itemCommonName;
    }

    public void setItemCommonName(String itemCommonName) {
        this.itemCommonName = itemCommonName;
    }

    public String getItemBotanicalName() {
        return itemBotanicalName;
    }

    public void setItemBotanicalName(String itemBotanicalName) {
        this.itemBotanicalName = itemBotanicalName;
    }

    public String getItemInternalProductCode() {
        return itemInternalProductCode;
    }

    public void setItemInternalProductCode(String itemInternalProductCode) {
        this.itemInternalProductCode = itemInternalProductCode;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public String getKeswsItemClass() {
        return keswsItemClass;
    }

    public void setKeswsItemClass(String keswsItemClass) {
        this.keswsItemClass = keswsItemClass;
    }

    public String getKeswsItemClassDesc() {
        return keswsItemClassDesc;
    }

    public void setKeswsItemClassDesc(String keswsItemClassDesc) {
        this.keswsItemClassDesc = keswsItemClassDesc;
    }

    public String getAdditionalField1() {
        return additionalField1;
    }

    public void setAdditionalField1(String additionalField1) {
        this.additionalField1 = additionalField1;
    }

    public String getAdditionalField2() {
        return additionalField2;
    }

    public void setAdditionalField2(String additionalField2) {
        this.additionalField2 = additionalField2;
    }
    
}
