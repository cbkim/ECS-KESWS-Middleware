/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
    @NamedQuery(name = "EcsConDoc.findBySenderID", query = "SELECT e FROM EcsConDoc e WHERE e.senderID = :senderID"),
    @NamedQuery(name = "EcsConDoc.findByConsignmentDocDetails", query = "SELECT e FROM EcsConDoc e WHERE e.consignmentDocDetails = :consignmentDocDetails"),
    @NamedQuery(name = "EcsConDoc.findByStrCDStandard", query = "SELECT e FROM EcsConDoc e WHERE e.strCDStandard = :strCDStandard"),
    @NamedQuery(name = "EcsConDoc.findByServiceProviderApplicationCode", query = "SELECT e FROM EcsConDoc e WHERE e.serviceProviderApplicationCode = :serviceProviderApplicationCode"),
    @NamedQuery(name = "EcsConDoc.findByServiceProviderName", query = "SELECT e FROM EcsConDoc e WHERE e.serviceProviderName = :serviceProviderName"),
    @NamedQuery(name = "EcsConDoc.findByServiceProviderTIN", query = "SELECT e FROM EcsConDoc e WHERE e.serviceProviderTIN = :serviceProviderTIN"),
    @NamedQuery(name = "EcsConDoc.findByServiceProviderPhysicalAddress", query = "SELECT e FROM EcsConDoc e WHERE e.serviceProviderPhysicalAddress = :serviceProviderPhysicalAddress"),
    @NamedQuery(name = "EcsConDoc.findByServiceProviderPhyCountry", query = "SELECT e FROM EcsConDoc e WHERE e.serviceProviderPhyCountry = :serviceProviderPhyCountry"),
    @NamedQuery(name = "EcsConDoc.findByApplicationDate", query = "SELECT e FROM EcsConDoc e WHERE e.applicationDate = :applicationDate"),
    @NamedQuery(name = "EcsConDoc.findByUpdatedDate", query = "SELECT e FROM EcsConDoc e WHERE e.updatedDate = :updatedDate"),
    @NamedQuery(name = "EcsConDoc.findByUCRNo", query = "SELECT e FROM EcsConDoc e WHERE e.uCRNo = :uCRNo"),
    @NamedQuery(name = "EcsConDoc.findByEndCDStandard", query = "SELECT e FROM EcsConDoc e WHERE e.endCDStandard = :endCDStandard"),
    @NamedQuery(name = "EcsConDoc.findByStrCDImporter", query = "SELECT e FROM EcsConDoc e WHERE e.strCDImporter = :strCDImporter"),
    @NamedQuery(name = "EcsConDoc.findByCDImporterName", query = "SELECT e FROM EcsConDoc e WHERE e.cDImporterName = :cDImporterName"),
    @NamedQuery(name = "EcsConDoc.findByCDImporterTIN", query = "SELECT e FROM EcsConDoc e WHERE e.cDImporterTIN = :cDImporterTIN"),
    @NamedQuery(name = "EcsConDoc.findByCDImporterPhysicalAddress", query = "SELECT e FROM EcsConDoc e WHERE e.cDImporterPhysicalAddress = :cDImporterPhysicalAddress"),
    @NamedQuery(name = "EcsConDoc.findByCDImporterPhyCountry", query = "SELECT e FROM EcsConDoc e WHERE e.cDImporterPhyCountry = :cDImporterPhyCountry"),
    @NamedQuery(name = "EcsConDoc.findByCDImporterPostalAddress", query = "SELECT e FROM EcsConDoc e WHERE e.cDImporterPostalAddress = :cDImporterPostalAddress"),
    @NamedQuery(name = "EcsConDoc.findByCDImporterPosCountry", query = "SELECT e FROM EcsConDoc e WHERE e.cDImporterPosCountry = :cDImporterPosCountry"),
    @NamedQuery(name = "EcsConDoc.findByCDImporterTeleFax", query = "SELECT e FROM EcsConDoc e WHERE e.cDImporterTeleFax = :cDImporterTeleFax"),
    @NamedQuery(name = "EcsConDoc.findByCDImporterEmail", query = "SELECT e FROM EcsConDoc e WHERE e.cDImporterEmail = :cDImporterEmail"),
    @NamedQuery(name = "EcsConDoc.findByCDImporterSectorofActivity", query = "SELECT e FROM EcsConDoc e WHERE e.cDImporterSectorofActivity = :cDImporterSectorofActivity"),
    @NamedQuery(name = "EcsConDoc.findByCDImporterWarehouseCode", query = "SELECT e FROM EcsConDoc e WHERE e.cDImporterWarehouseCode = :cDImporterWarehouseCode"),
    @NamedQuery(name = "EcsConDoc.findByCDImporterWarehouseLocation", query = "SELECT e FROM EcsConDoc e WHERE e.cDImporterWarehouseLocation = :cDImporterWarehouseLocation"),
    @NamedQuery(name = "EcsConDoc.findByEndCDImporter", query = "SELECT e FROM EcsConDoc e WHERE e.endCDImporter = :endCDImporter"),
    @NamedQuery(name = "EcsConDoc.findByStrCDConsignee", query = "SELECT e FROM EcsConDoc e WHERE e.strCDConsignee = :strCDConsignee"),
    @NamedQuery(name = "EcsConDoc.findByCDConsigneName", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsigneName = :cDConsigneName"),
    @NamedQuery(name = "EcsConDoc.findByCDConsigneeTIN", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsigneeTIN = :cDConsigneeTIN"),
    @NamedQuery(name = "EcsConDoc.findByCDConsigneEPhysicalAddress", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsigneEPhysicalAddress = :cDConsigneEPhysicalAddress"),
    @NamedQuery(name = "EcsConDoc.findByCDConsigneePhyCountry", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsigneePhyCountry = :cDConsigneePhyCountry"),
    @NamedQuery(name = "EcsConDoc.findByCDConsigneePostalAddress", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsigneePostalAddress = :cDConsigneePostalAddress"),
    @NamedQuery(name = "EcsConDoc.findByCDConsigneePosCountry", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsigneePosCountry = :cDConsigneePosCountry"),
    @NamedQuery(name = "EcsConDoc.findByCDConsigneeTeleFax", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsigneeTeleFax = :cDConsigneeTeleFax"),
    @NamedQuery(name = "EcsConDoc.findByCDConsigneeEmail", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsigneeEmail = :cDConsigneeEmail"),
    @NamedQuery(name = "EcsConDoc.findByCDConsigneeSectorofActivity", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsigneeSectorofActivity = :cDConsigneeSectorofActivity"),
    @NamedQuery(name = "EcsConDoc.findByCCDConsigneeWarehouseCode", query = "SELECT e FROM EcsConDoc e WHERE e.cCDConsigneeWarehouseCode = :cCDConsigneeWarehouseCode"),
    @NamedQuery(name = "EcsConDoc.findByCDConsigneeWarehouseLocation", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsigneeWarehouseLocation = :cDConsigneeWarehouseLocation"),
    @NamedQuery(name = "EcsConDoc.findByEndCDConsignee", query = "SELECT e FROM EcsConDoc e WHERE e.endCDConsignee = :endCDConsignee"),
    @NamedQuery(name = "EcsConDoc.findByStrCDExporter", query = "SELECT e FROM EcsConDoc e WHERE e.strCDExporter = :strCDExporter"),
    @NamedQuery(name = "EcsConDoc.findByCDExporterName", query = "SELECT e FROM EcsConDoc e WHERE e.cDExporterName = :cDExporterName"),
    @NamedQuery(name = "EcsConDoc.findByCDExporterTIN", query = "SELECT e FROM EcsConDoc e WHERE e.cDExporterTIN = :cDExporterTIN"),
    @NamedQuery(name = "EcsConDoc.findByCDExporterPhysicalAddress", query = "SELECT e FROM EcsConDoc e WHERE e.cDExporterPhysicalAddress = :cDExporterPhysicalAddress"),
    @NamedQuery(name = "EcsConDoc.findByCDExporterPhyCountry", query = "SELECT e FROM EcsConDoc e WHERE e.cDExporterPhyCountry = :cDExporterPhyCountry"),
    @NamedQuery(name = "EcsConDoc.findByCDExporterPostalAddress", query = "SELECT e FROM EcsConDoc e WHERE e.cDExporterPostalAddress = :cDExporterPostalAddress"),
    @NamedQuery(name = "EcsConDoc.findByCDExporterPosCountry", query = "SELECT e FROM EcsConDoc e WHERE e.cDExporterPosCountry = :cDExporterPosCountry"),
    @NamedQuery(name = "EcsConDoc.findByCDExporterTeleFax", query = "SELECT e FROM EcsConDoc e WHERE e.cDExporterTeleFax = :cDExporterTeleFax"),
    @NamedQuery(name = "EcsConDoc.findByCDExporterEmail", query = "SELECT e FROM EcsConDoc e WHERE e.cDExporterEmail = :cDExporterEmail"),
    @NamedQuery(name = "EcsConDoc.findByCDExporterSectorofActivity", query = "SELECT e FROM EcsConDoc e WHERE e.cDExporterSectorofActivity = :cDExporterSectorofActivity"),
    @NamedQuery(name = "EcsConDoc.findByCDExporterWarehouseCode", query = "SELECT e FROM EcsConDoc e WHERE e.cDExporterWarehouseCode = :cDExporterWarehouseCode"),
    @NamedQuery(name = "EcsConDoc.findByCDExporterWarehouseLocation", query = "SELECT e FROM EcsConDoc e WHERE e.cDExporterWarehouseLocation = :cDExporterWarehouseLocation"),
    @NamedQuery(name = "EcsConDoc.findByEndCDExporter", query = "SELECT e FROM EcsConDoc e WHERE e.endCDExporter = :endCDExporter"),
    @NamedQuery(name = "EcsConDoc.findByStrCDConsignor", query = "SELECT e FROM EcsConDoc e WHERE e.strCDConsignor = :strCDConsignor"),
    @NamedQuery(name = "EcsConDoc.findByCDConsignorName", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsignorName = :cDConsignorName"),
    @NamedQuery(name = "EcsConDoc.findByCDConsignorTIN", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsignorTIN = :cDConsignorTIN"),
    @NamedQuery(name = "EcsConDoc.findByCDConsignorPhysicalAddress", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsignorPhysicalAddress = :cDConsignorPhysicalAddress"),
    @NamedQuery(name = "EcsConDoc.findByCDConsignorPhyCountry", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsignorPhyCountry = :cDConsignorPhyCountry"),
    @NamedQuery(name = "EcsConDoc.findByCDConsignorPostalAddress", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsignorPostalAddress = :cDConsignorPostalAddress"),
    @NamedQuery(name = "EcsConDoc.findByCDConsignorPosCountry", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsignorPosCountry = :cDConsignorPosCountry"),
    @NamedQuery(name = "EcsConDoc.findByCDConsignorTeleFax", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsignorTeleFax = :cDConsignorTeleFax"),
    @NamedQuery(name = "EcsConDoc.findByCDConsignorEmail", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsignorEmail = :cDConsignorEmail"),
    @NamedQuery(name = "EcsConDoc.findByCDConsignorSectorofActivity", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsignorSectorofActivity = :cDConsignorSectorofActivity"),
    @NamedQuery(name = "EcsConDoc.findByCDConsignorWarehouseCode", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsignorWarehouseCode = :cDConsignorWarehouseCode"),
    @NamedQuery(name = "EcsConDoc.findByCDConsignorWarehouseLocation", query = "SELECT e FROM EcsConDoc e WHERE e.cDConsignorWarehouseLocation = :cDConsignorWarehouseLocation"),
    @NamedQuery(name = "EcsConDoc.findByEndCDConsignor", query = "SELECT e FROM EcsConDoc e WHERE e.endCDConsignor = :endCDConsignor"),
    @NamedQuery(name = "EcsConDoc.findByStrCDTransport", query = "SELECT e FROM EcsConDoc e WHERE e.strCDTransport = :strCDTransport"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportModeOfTransport", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportModeOfTransport = :cDTransportModeOfTransport"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportModeOfTransportDesc", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportModeOfTransportDesc = :cDTransportModeOfTransportDesc"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportPortOfArrival", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportPortOfArrival = :cDTransportPortOfArrival"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportPortOfArrivalDesc", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportPortOfArrivalDesc = :cDTransportPortOfArrivalDesc"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportPortOfDeparture", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportPortOfDeparture = :cDTransportPortOfDeparture"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportPortOfDepartureDesc", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportPortOfDepartureDesc = :cDTransportPortOfDepartureDesc"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportVesselName", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportVesselName = :cDTransportVesselName"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportVoyageNo", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportVoyageNo = :cDTransportVoyageNo"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportShipmentDate", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportShipmentDate = :cDTransportShipmentDate"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportCarrier", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportCarrier = :cDTransportCarrier"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportManifestNo", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportManifestNo = :cDTransportManifestNo"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportBLAWB", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportBLAWB = :cDTransportBLAWB"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportMarksAndNumbers", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportMarksAndNumbers = :cDTransportMarksAndNumbers"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportCustomsOffice", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportCustomsOffice = :cDTransportCustomsOffice"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportCustomsOfficeDesc", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportCustomsOfficeDesc = :cDTransportCustomsOfficeDesc"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportFreightStation", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportFreightStation = :cDTransportFreightStation"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportFreightStationDesc", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportFreightStationDesc = :cDTransportFreightStationDesc"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportCargoTypeIndicator", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportCargoTypeIndicator = :cDTransportCargoTypeIndicator"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportInlandTransportCo", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportInlandTransportCo = :cDTransportInlandTransportCo"),
    @NamedQuery(name = "EcsConDoc.findByCDTransportInlandTransportCoRefNo", query = "SELECT e FROM EcsConDoc e WHERE e.cDTransportInlandTransportCoRefNo = :cDTransportInlandTransportCoRefNo"),
    @NamedQuery(name = "EcsConDoc.findByEndCDTransport", query = "SELECT e FROM EcsConDoc e WHERE e.endCDTransport = :endCDTransport"),
    @NamedQuery(name = "EcsConDoc.findByStrPGAHeaderFields", query = "SELECT e FROM EcsConDoc e WHERE e.strPGAHeaderFields = :strPGAHeaderFields"),
    @NamedQuery(name = "EcsConDoc.findByPGAHeaderFieldsCollectionOffice", query = "SELECT e FROM EcsConDoc e WHERE e.pGAHeaderFieldsCollectionOffice = :pGAHeaderFieldsCollectionOffice"),
    @NamedQuery(name = "EcsConDoc.findByPGAHeaderFieldsPreferredInspectionDate", query = "SELECT e FROM EcsConDoc e WHERE e.pGAHeaderFieldsPreferredInspectionDate = :pGAHeaderFieldsPreferredInspectionDate"),
    @NamedQuery(name = "EcsConDoc.findByEndPGAHeaderFields", query = "SELECT e FROM EcsConDoc e WHERE e.endPGAHeaderFields = :endPGAHeaderFields"),
    @NamedQuery(name = "EcsConDoc.findByStrCDHeaderOne", query = "SELECT e FROM EcsConDoc e WHERE e.strCDHeaderOne = :strCDHeaderOne"),
    @NamedQuery(name = "EcsConDoc.findByCDHeaderOneInvoiceDate", query = "SELECT e FROM EcsConDoc e WHERE e.cDHeaderOneInvoiceDate = :cDHeaderOneInvoiceDate"),
    @NamedQuery(name = "EcsConDoc.findByCDHeaderOneInvoiceNumber", query = "SELECT e FROM EcsConDoc e WHERE e.cDHeaderOneInvoiceNumber = :cDHeaderOneInvoiceNumber"),
    @NamedQuery(name = "EcsConDoc.findByCDHeaderOneCountryOfSupply", query = "SELECT e FROM EcsConDoc e WHERE e.cDHeaderOneCountryOfSupply = :cDHeaderOneCountryOfSupply"),
    @NamedQuery(name = "EcsConDoc.findByEndCDHeaderOne", query = "SELECT e FROM EcsConDoc e WHERE e.endCDHeaderOne = :endCDHeaderOne"),
    @NamedQuery(name = "EcsConDoc.findByStrCDProductDetails", query = "SELECT e FROM EcsConDoc e WHERE e.strCDProductDetails = :strCDProductDetails"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1ItemDescription", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1ItemDescription = :cDProduct1ItemDescription"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1ItemHSCode", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1ItemHSCode = :cDProduct1ItemHSCode"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1HSDescription", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1HSDescription = :cDProduct1HSDescription"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1InternalProductNo", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1InternalProductNo = :cDProduct1InternalProductNo"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1QuantityQty", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1QuantityQty = :cDProduct1QuantityQty"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1QuantityUnitOfQty", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1QuantityUnitOfQty = :cDProduct1QuantityUnitOfQty"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1QuantityUnitOfQtyDesc", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1QuantityUnitOfQtyDesc = :cDProduct1QuantityUnitOfQtyDesc"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1SupplementryQuantityQty", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1SupplementryQuantityQty = :cDProduct1SupplementryQuantityQty"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1SupplementryQuantityUnitOfQty", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1SupplementryQuantityUnitOfQty = :cDProduct1SupplementryQuantityUnitOfQty"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1PackageTypeDesc", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1PackageTypeDesc = :cDProduct1PackageTypeDesc"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1PackageQty", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1PackageQty = :cDProduct1PackageQty"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1PackageType", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1PackageType = :cDProduct1PackageType"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1ItemNetWeight", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1ItemNetWeight = :cDProduct1ItemNetWeight"),
    @NamedQuery(name = "EcsConDoc.findByCDProduct1ItemGrossWeight", query = "SELECT e FROM EcsConDoc e WHERE e.cDProduct1ItemGrossWeight = :cDProduct1ItemGrossWeight"),
    @NamedQuery(name = "EcsConDoc.findByEndCDProductDetails", query = "SELECT e FROM EcsConDoc e WHERE e.endCDProductDetails = :endCDProductDetails")})
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
    @Column(name = "SenderID")
    private String senderID;
    @Column(name = "ConsignmentDocDetails")
    private String consignmentDocDetails;
    @Column(name = "StrCDStandard")
    private String strCDStandard;
    @Column(name = "ServiceProvider_ApplicationCode")
    private String serviceProviderApplicationCode;
    @Column(name = "ServiceProvider_Name")
    private String serviceProviderName;
    @Basic(optional = false)
    @Column(name = "ServiceProvider_TIN")
    private String serviceProviderTIN;
    @Column(name = "ServiceProvider_PhysicalAddress")
    private String serviceProviderPhysicalAddress;
    @Basic(optional = false)
    @Column(name = "ServiceProvider_PhyCountry")
    private String serviceProviderPhyCountry;
    @Column(name = "ApplicationDate")
    private String applicationDate;
    @Column(name = "UpdatedDate")
    private String updatedDate;
    @Column(name = "UCRNo")
    private String uCRNo;
    @Column(name = "EndCDStandard")
    private String endCDStandard;
    @Column(name = "StrCDImporter")
    private String strCDImporter;
    @Basic(optional = false)
    @Column(name = "CDImporter_Name")
    private String cDImporterName;
    @Column(name = "CDImporter_TIN")
    private String cDImporterTIN;
    @Basic(optional = false)
    @Column(name = "CDImporter_PhysicalAddress")
    private String cDImporterPhysicalAddress;
    @Column(name = "CDImporter_PhyCountry")
    private String cDImporterPhyCountry;
    @Basic(optional = false)
    @Column(name = "CDImporter_PostalAddress")
    private String cDImporterPostalAddress;
    @Column(name = "CDImporter_PosCountry")
    private String cDImporterPosCountry;
    @Column(name = "CDImporter_TeleFax")
    private String cDImporterTeleFax;
    @Column(name = "CDImporter_Email")
    private String cDImporterEmail;
    @Column(name = "CDImporter_SectorofActivity")
    private String cDImporterSectorofActivity;
    @Column(name = "CDImporter_WarehouseCode")
    private String cDImporterWarehouseCode;
    @Column(name = "CDImporter_WarehouseLocation")
    private String cDImporterWarehouseLocation;
    @Column(name = "EndCDImporter")
    private String endCDImporter;
    @Column(name = "StrCDConsignee")
    private String strCDConsignee;
    @Basic(optional = false)
    @Column(name = "CDConsigne_Name")
    private String cDConsigneName;
    @Column(name = "CDConsignee_TIN")
    private String cDConsigneeTIN;
    @Basic(optional = false)
    @Column(name = "CDConsigneE_PhysicalAddress")
    private String cDConsigneEPhysicalAddress;
    @Column(name = "CDConsignee_PhyCountry")
    private String cDConsigneePhyCountry;
    @Basic(optional = false)
    @Column(name = "CDConsignee_PostalAddress")
    private String cDConsigneePostalAddress;
    @Column(name = "CDConsignee_PosCountry")
    private String cDConsigneePosCountry;
    @Column(name = "CDConsignee_TeleFax")
    private String cDConsigneeTeleFax;
    @Column(name = "CDConsignee_Email")
    private String cDConsigneeEmail;
    @Column(name = "CDConsignee_SectorofActivity")
    private String cDConsigneeSectorofActivity;
    @Column(name = "CCDConsignee_WarehouseCode")
    private String cCDConsigneeWarehouseCode;
    @Column(name = "CDConsignee_WarehouseLocation")
    private String cDConsigneeWarehouseLocation;
    @Column(name = "EndCDConsignee")
    private String endCDConsignee;
    @Column(name = "StrCDExporter")
    private String strCDExporter;
    @Basic(optional = false)
    @Column(name = "CDExporter_Name")
    private String cDExporterName;
    @Basic(optional = false)
    @Column(name = "CDExporter_TIN")
    private String cDExporterTIN;
    @Column(name = "CDExporter_PhysicalAddress")
    private String cDExporterPhysicalAddress;
    @Basic(optional = false)
    @Column(name = "CDExporter_PhyCountry")
    private String cDExporterPhyCountry;
    @Column(name = "CDExporter_PostalAddress")
    private String cDExporterPostalAddress;
    @Basic(optional = false)
    @Column(name = "CDExporter_PosCountry")
    private String cDExporterPosCountry;
    @Basic(optional = false)
    @Column(name = "CDExporter_TeleFax")
    private String cDExporterTeleFax;
    @Basic(optional = false)
    @Column(name = "CDExporter_Email")
    private String cDExporterEmail;
    @Column(name = "CDExporter_SectorofActivity")
    private String cDExporterSectorofActivity;
    @Column(name = "CDExporter_WarehouseCode")
    private String cDExporterWarehouseCode;
    @Column(name = "CDExporter_WarehouseLocation")
    private String cDExporterWarehouseLocation;
    @Column(name = "EndCDExporter")
    private String endCDExporter;
    @Column(name = "StrCDConsignor")
    private String strCDConsignor;
    @Basic(optional = false)
    @Column(name = "CDConsignor_Name")
    private String cDConsignorName;
    @Basic(optional = false)
    @Column(name = "CDConsignor_TIN")
    private String cDConsignorTIN;
    @Column(name = "CDConsignor_PhysicalAddress")
    private String cDConsignorPhysicalAddress;
    @Basic(optional = false)
    @Column(name = "CDConsignor_PhyCountry")
    private String cDConsignorPhyCountry;
    @Column(name = "CDConsignor_PostalAddress")
    private String cDConsignorPostalAddress;
    @Basic(optional = false)
    @Column(name = "CDConsignor_PosCountry")
    private String cDConsignorPosCountry;
    @Basic(optional = false)
    @Column(name = "CDConsignor_TeleFax")
    private String cDConsignorTeleFax;
    @Basic(optional = false)
    @Column(name = "CDConsignor_Email")
    private String cDConsignorEmail;
    @Column(name = "CDConsignor_SectorofActivity")
    private String cDConsignorSectorofActivity;
    @Column(name = "CDConsignor_WarehouseCode")
    private String cDConsignorWarehouseCode;
    @Column(name = "CDConsignor_WarehouseLocation")
    private String cDConsignorWarehouseLocation;
    @Column(name = "EndCDConsignor")
    private String endCDConsignor;
    @Column(name = "StrCDTransport")
    private String strCDTransport;
    @Basic(optional = false)
    @Column(name = "CDTransport_ModeOfTransport")
    private String cDTransportModeOfTransport;
    @Basic(optional = false)
    @Column(name = "CDTransport_ModeOfTransportDesc")
    private String cDTransportModeOfTransportDesc;
    @Column(name = "CDTransport_PortOfArrival")
    private String cDTransportPortOfArrival;
    @Column(name = "CDTransport_PortOfArrivalDesc")
    private String cDTransportPortOfArrivalDesc;
    @Basic(optional = false)
    @Column(name = "CDTransport_PortOfDeparture")
    private String cDTransportPortOfDeparture;
    @Column(name = "CDTransport_PortOfDepartureDesc")
    private String cDTransportPortOfDepartureDesc;
    @Column(name = "CDTransport_VesselName")
    private String cDTransportVesselName;
    @Column(name = "CDTransport_VoyageNo")
    private String cDTransportVoyageNo;
    @Column(name = "CDTransport_ShipmentDate")
    private String cDTransportShipmentDate;
    @Column(name = "CDTransport_Carrier")
    private String cDTransportCarrier;
    @Column(name = "CDTransport_ManifestNo")
    private String cDTransportManifestNo;
    @Column(name = "CDTransport_BLAWB")
    private String cDTransportBLAWB;
    @Column(name = "CDTransport_MarksAndNumbers")
    private String cDTransportMarksAndNumbers;
    @Column(name = "CDTransport_CustomsOffice")
    private String cDTransportCustomsOffice;
    @Column(name = "CDTransport_CustomsOfficeDesc")
    private String cDTransportCustomsOfficeDesc;
    @Column(name = "CDTransport_FreightStation")
    private String cDTransportFreightStation;
    @Column(name = "CDTransport_FreightStationDesc")
    private String cDTransportFreightStationDesc;
    @Column(name = "CDTransport_CargoTypeIndicator")
    private String cDTransportCargoTypeIndicator;
    @Column(name = "CDTransport_InlandTransportCo")
    private String cDTransportInlandTransportCo;
    @Column(name = "CDTransport_InlandTransportCoRefNo")
    private String cDTransportInlandTransportCoRefNo;
    @Column(name = "EndCDTransport")
    private String endCDTransport;
    @Column(name = "StrPGAHeaderFields")
    private String strPGAHeaderFields;
    @Column(name = "PGAHeaderFields_CollectionOffice")
    private BigInteger pGAHeaderFieldsCollectionOffice;
    @Column(name = "PGAHeaderFields_PreferredInspectionDate")
    private String pGAHeaderFieldsPreferredInspectionDate;
    @Column(name = "EndPGAHeaderFields")
    private String endPGAHeaderFields;
    @Column(name = "StrCDHeaderOne")
    private String strCDHeaderOne;
    @Column(name = "CDHeaderOne_InvoiceDate")
    private String cDHeaderOneInvoiceDate;
    @Basic(optional = false)
    @Column(name = "CDHeaderOne_InvoiceNumber")
    private String cDHeaderOneInvoiceNumber;
    @Column(name = "CDHeaderOne_CountryOfSupply")
    private String cDHeaderOneCountryOfSupply;
    @Column(name = "EndCDHeaderOne")
    private String endCDHeaderOne;
    @Column(name = "StrCDProductDetails")
    private String strCDProductDetails;
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
    @Column(name = "CDProduct1_SupplementryQuantity_UnitOfQty")
    private String cDProduct1SupplementryQuantityUnitOfQty;
    @Basic(optional = false)
    @Column(name = "CDProduct1_PackageTypeDesc")
    private String cDProduct1PackageTypeDesc;
    @Basic(optional = false)
    @Column(name = "CDProduct1_PackageQty")
    private int cDProduct1PackageQty;
    @Column(name = "CDProduct1_PackageType")
    private String cDProduct1PackageType;
    @Column(name = "CDProduct1_ItemNetWeight")
    private Double cDProduct1ItemNetWeight;
    @Column(name = "CDProduct1_ItemGrossWeight")
    private Double cDProduct1ItemGrossWeight;
    @Column(name = "EndCDProductDetails")
    private String endCDProductDetails;

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

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getConsignmentDocDetails() {
        return consignmentDocDetails;
    }

    public void setConsignmentDocDetails(String consignmentDocDetails) {
        this.consignmentDocDetails = consignmentDocDetails;
    }

    public String getStrCDStandard() {
        return strCDStandard;
    }

    public void setStrCDStandard(String strCDStandard) {
        this.strCDStandard = strCDStandard;
    }

    public String getServiceProviderApplicationCode() {
        return serviceProviderApplicationCode;
    }

    public void setServiceProviderApplicationCode(String serviceProviderApplicationCode) {
        this.serviceProviderApplicationCode = serviceProviderApplicationCode;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getServiceProviderTIN() {
        return serviceProviderTIN;
    }

    public void setServiceProviderTIN(String serviceProviderTIN) {
        this.serviceProviderTIN = serviceProviderTIN;
    }

    public String getServiceProviderPhysicalAddress() {
        return serviceProviderPhysicalAddress;
    }

    public void setServiceProviderPhysicalAddress(String serviceProviderPhysicalAddress) {
        this.serviceProviderPhysicalAddress = serviceProviderPhysicalAddress;
    }

    public String getServiceProviderPhyCountry() {
        return serviceProviderPhyCountry;
    }

    public void setServiceProviderPhyCountry(String serviceProviderPhyCountry) {
        this.serviceProviderPhyCountry = serviceProviderPhyCountry;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUCRNo() {
        return uCRNo;
    }

    public void setUCRNo(String uCRNo) {
        this.uCRNo = uCRNo;
    }

    public String getEndCDStandard() {
        return endCDStandard;
    }

    public void setEndCDStandard(String endCDStandard) {
        this.endCDStandard = endCDStandard;
    }

    public String getStrCDImporter() {
        return strCDImporter;
    }

    public void setStrCDImporter(String strCDImporter) {
        this.strCDImporter = strCDImporter;
    }

    public String getCDImporterName() {
        return cDImporterName;
    }

    public void setCDImporterName(String cDImporterName) {
        this.cDImporterName = cDImporterName;
    }

    public String getCDImporterTIN() {
        return cDImporterTIN;
    }

    public void setCDImporterTIN(String cDImporterTIN) {
        this.cDImporterTIN = cDImporterTIN;
    }

    public String getCDImporterPhysicalAddress() {
        return cDImporterPhysicalAddress;
    }

    public void setCDImporterPhysicalAddress(String cDImporterPhysicalAddress) {
        this.cDImporterPhysicalAddress = cDImporterPhysicalAddress;
    }

    public String getCDImporterPhyCountry() {
        return cDImporterPhyCountry;
    }

    public void setCDImporterPhyCountry(String cDImporterPhyCountry) {
        this.cDImporterPhyCountry = cDImporterPhyCountry;
    }

    public String getCDImporterPostalAddress() {
        return cDImporterPostalAddress;
    }

    public void setCDImporterPostalAddress(String cDImporterPostalAddress) {
        this.cDImporterPostalAddress = cDImporterPostalAddress;
    }

    public String getCDImporterPosCountry() {
        return cDImporterPosCountry;
    }

    public void setCDImporterPosCountry(String cDImporterPosCountry) {
        this.cDImporterPosCountry = cDImporterPosCountry;
    }

    public String getCDImporterTeleFax() {
        return cDImporterTeleFax;
    }

    public void setCDImporterTeleFax(String cDImporterTeleFax) {
        this.cDImporterTeleFax = cDImporterTeleFax;
    }

    public String getCDImporterEmail() {
        return cDImporterEmail;
    }

    public void setCDImporterEmail(String cDImporterEmail) {
        this.cDImporterEmail = cDImporterEmail;
    }

    public String getCDImporterSectorofActivity() {
        return cDImporterSectorofActivity;
    }

    public void setCDImporterSectorofActivity(String cDImporterSectorofActivity) {
        this.cDImporterSectorofActivity = cDImporterSectorofActivity;
    }

    public String getCDImporterWarehouseCode() {
        return cDImporterWarehouseCode;
    }

    public void setCDImporterWarehouseCode(String cDImporterWarehouseCode) {
        this.cDImporterWarehouseCode = cDImporterWarehouseCode;
    }

    public String getCDImporterWarehouseLocation() {
        return cDImporterWarehouseLocation;
    }

    public void setCDImporterWarehouseLocation(String cDImporterWarehouseLocation) {
        this.cDImporterWarehouseLocation = cDImporterWarehouseLocation;
    }

    public String getEndCDImporter() {
        return endCDImporter;
    }

    public void setEndCDImporter(String endCDImporter) {
        this.endCDImporter = endCDImporter;
    }

    public String getStrCDConsignee() {
        return strCDConsignee;
    }

    public void setStrCDConsignee(String strCDConsignee) {
        this.strCDConsignee = strCDConsignee;
    }

    public String getCDConsigneName() {
        return cDConsigneName;
    }

    public void setCDConsigneName(String cDConsigneName) {
        this.cDConsigneName = cDConsigneName;
    }

    public String getCDConsigneeTIN() {
        return cDConsigneeTIN;
    }

    public void setCDConsigneeTIN(String cDConsigneeTIN) {
        this.cDConsigneeTIN = cDConsigneeTIN;
    }

    public String getCDConsigneEPhysicalAddress() {
        return cDConsigneEPhysicalAddress;
    }

    public void setCDConsigneEPhysicalAddress(String cDConsigneEPhysicalAddress) {
        this.cDConsigneEPhysicalAddress = cDConsigneEPhysicalAddress;
    }

    public String getCDConsigneePhyCountry() {
        return cDConsigneePhyCountry;
    }

    public void setCDConsigneePhyCountry(String cDConsigneePhyCountry) {
        this.cDConsigneePhyCountry = cDConsigneePhyCountry;
    }

    public String getCDConsigneePostalAddress() {
        return cDConsigneePostalAddress;
    }

    public void setCDConsigneePostalAddress(String cDConsigneePostalAddress) {
        this.cDConsigneePostalAddress = cDConsigneePostalAddress;
    }

    public String getCDConsigneePosCountry() {
        return cDConsigneePosCountry;
    }

    public void setCDConsigneePosCountry(String cDConsigneePosCountry) {
        this.cDConsigneePosCountry = cDConsigneePosCountry;
    }

    public String getCDConsigneeTeleFax() {
        return cDConsigneeTeleFax;
    }

    public void setCDConsigneeTeleFax(String cDConsigneeTeleFax) {
        this.cDConsigneeTeleFax = cDConsigneeTeleFax;
    }

    public String getCDConsigneeEmail() {
        return cDConsigneeEmail;
    }

    public void setCDConsigneeEmail(String cDConsigneeEmail) {
        this.cDConsigneeEmail = cDConsigneeEmail;
    }

    public String getCDConsigneeSectorofActivity() {
        return cDConsigneeSectorofActivity;
    }

    public void setCDConsigneeSectorofActivity(String cDConsigneeSectorofActivity) {
        this.cDConsigneeSectorofActivity = cDConsigneeSectorofActivity;
    }

    public String getCCDConsigneeWarehouseCode() {
        return cCDConsigneeWarehouseCode;
    }

    public void setCCDConsigneeWarehouseCode(String cCDConsigneeWarehouseCode) {
        this.cCDConsigneeWarehouseCode = cCDConsigneeWarehouseCode;
    }

    public String getCDConsigneeWarehouseLocation() {
        return cDConsigneeWarehouseLocation;
    }

    public void setCDConsigneeWarehouseLocation(String cDConsigneeWarehouseLocation) {
        this.cDConsigneeWarehouseLocation = cDConsigneeWarehouseLocation;
    }

    public String getEndCDConsignee() {
        return endCDConsignee;
    }

    public void setEndCDConsignee(String endCDConsignee) {
        this.endCDConsignee = endCDConsignee;
    }

    public String getStrCDExporter() {
        return strCDExporter;
    }

    public void setStrCDExporter(String strCDExporter) {
        this.strCDExporter = strCDExporter;
    }

    public String getCDExporterName() {
        return cDExporterName;
    }

    public void setCDExporterName(String cDExporterName) {
        this.cDExporterName = cDExporterName;
    }

    public String getCDExporterTIN() {
        return cDExporterTIN;
    }

    public void setCDExporterTIN(String cDExporterTIN) {
        this.cDExporterTIN = cDExporterTIN;
    }

    public String getCDExporterPhysicalAddress() {
        return cDExporterPhysicalAddress;
    }

    public void setCDExporterPhysicalAddress(String cDExporterPhysicalAddress) {
        this.cDExporterPhysicalAddress = cDExporterPhysicalAddress;
    }

    public String getCDExporterPhyCountry() {
        return cDExporterPhyCountry;
    }

    public void setCDExporterPhyCountry(String cDExporterPhyCountry) {
        this.cDExporterPhyCountry = cDExporterPhyCountry;
    }

    public String getCDExporterPostalAddress() {
        return cDExporterPostalAddress;
    }

    public void setCDExporterPostalAddress(String cDExporterPostalAddress) {
        this.cDExporterPostalAddress = cDExporterPostalAddress;
    }

    public String getCDExporterPosCountry() {
        return cDExporterPosCountry;
    }

    public void setCDExporterPosCountry(String cDExporterPosCountry) {
        this.cDExporterPosCountry = cDExporterPosCountry;
    }

    public String getCDExporterTeleFax() {
        return cDExporterTeleFax;
    }

    public void setCDExporterTeleFax(String cDExporterTeleFax) {
        this.cDExporterTeleFax = cDExporterTeleFax;
    }

    public String getCDExporterEmail() {
        return cDExporterEmail;
    }

    public void setCDExporterEmail(String cDExporterEmail) {
        this.cDExporterEmail = cDExporterEmail;
    }

    public String getCDExporterSectorofActivity() {
        return cDExporterSectorofActivity;
    }

    public void setCDExporterSectorofActivity(String cDExporterSectorofActivity) {
        this.cDExporterSectorofActivity = cDExporterSectorofActivity;
    }

    public String getCDExporterWarehouseCode() {
        return cDExporterWarehouseCode;
    }

    public void setCDExporterWarehouseCode(String cDExporterWarehouseCode) {
        this.cDExporterWarehouseCode = cDExporterWarehouseCode;
    }

    public String getCDExporterWarehouseLocation() {
        return cDExporterWarehouseLocation;
    }

    public void setCDExporterWarehouseLocation(String cDExporterWarehouseLocation) {
        this.cDExporterWarehouseLocation = cDExporterWarehouseLocation;
    }

    public String getEndCDExporter() {
        return endCDExporter;
    }

    public void setEndCDExporter(String endCDExporter) {
        this.endCDExporter = endCDExporter;
    }

    public String getStrCDConsignor() {
        return strCDConsignor;
    }

    public void setStrCDConsignor(String strCDConsignor) {
        this.strCDConsignor = strCDConsignor;
    }

    public String getCDConsignorName() {
        return cDConsignorName;
    }

    public void setCDConsignorName(String cDConsignorName) {
        this.cDConsignorName = cDConsignorName;
    }

    public String getCDConsignorTIN() {
        return cDConsignorTIN;
    }

    public void setCDConsignorTIN(String cDConsignorTIN) {
        this.cDConsignorTIN = cDConsignorTIN;
    }

    public String getCDConsignorPhysicalAddress() {
        return cDConsignorPhysicalAddress;
    }

    public void setCDConsignorPhysicalAddress(String cDConsignorPhysicalAddress) {
        this.cDConsignorPhysicalAddress = cDConsignorPhysicalAddress;
    }

    public String getCDConsignorPhyCountry() {
        return cDConsignorPhyCountry;
    }

    public void setCDConsignorPhyCountry(String cDConsignorPhyCountry) {
        this.cDConsignorPhyCountry = cDConsignorPhyCountry;
    }

    public String getCDConsignorPostalAddress() {
        return cDConsignorPostalAddress;
    }

    public void setCDConsignorPostalAddress(String cDConsignorPostalAddress) {
        this.cDConsignorPostalAddress = cDConsignorPostalAddress;
    }

    public String getCDConsignorPosCountry() {
        return cDConsignorPosCountry;
    }

    public void setCDConsignorPosCountry(String cDConsignorPosCountry) {
        this.cDConsignorPosCountry = cDConsignorPosCountry;
    }

    public String getCDConsignorTeleFax() {
        return cDConsignorTeleFax;
    }

    public void setCDConsignorTeleFax(String cDConsignorTeleFax) {
        this.cDConsignorTeleFax = cDConsignorTeleFax;
    }

    public String getCDConsignorEmail() {
        return cDConsignorEmail;
    }

    public void setCDConsignorEmail(String cDConsignorEmail) {
        this.cDConsignorEmail = cDConsignorEmail;
    }

    public String getCDConsignorSectorofActivity() {
        return cDConsignorSectorofActivity;
    }

    public void setCDConsignorSectorofActivity(String cDConsignorSectorofActivity) {
        this.cDConsignorSectorofActivity = cDConsignorSectorofActivity;
    }

    public String getCDConsignorWarehouseCode() {
        return cDConsignorWarehouseCode;
    }

    public void setCDConsignorWarehouseCode(String cDConsignorWarehouseCode) {
        this.cDConsignorWarehouseCode = cDConsignorWarehouseCode;
    }

    public String getCDConsignorWarehouseLocation() {
        return cDConsignorWarehouseLocation;
    }

    public void setCDConsignorWarehouseLocation(String cDConsignorWarehouseLocation) {
        this.cDConsignorWarehouseLocation = cDConsignorWarehouseLocation;
    }

    public String getEndCDConsignor() {
        return endCDConsignor;
    }

    public void setEndCDConsignor(String endCDConsignor) {
        this.endCDConsignor = endCDConsignor;
    }

    public String getStrCDTransport() {
        return strCDTransport;
    }

    public void setStrCDTransport(String strCDTransport) {
        this.strCDTransport = strCDTransport;
    }

    public String getCDTransportModeOfTransport() {
        return cDTransportModeOfTransport;
    }

    public void setCDTransportModeOfTransport(String cDTransportModeOfTransport) {
        this.cDTransportModeOfTransport = cDTransportModeOfTransport;
    }

    public String getCDTransportModeOfTransportDesc() {
        return cDTransportModeOfTransportDesc;
    }

    public void setCDTransportModeOfTransportDesc(String cDTransportModeOfTransportDesc) {
        this.cDTransportModeOfTransportDesc = cDTransportModeOfTransportDesc;
    }

    public String getCDTransportPortOfArrival() {
        return cDTransportPortOfArrival;
    }

    public void setCDTransportPortOfArrival(String cDTransportPortOfArrival) {
        this.cDTransportPortOfArrival = cDTransportPortOfArrival;
    }

    public String getCDTransportPortOfArrivalDesc() {
        return cDTransportPortOfArrivalDesc;
    }

    public void setCDTransportPortOfArrivalDesc(String cDTransportPortOfArrivalDesc) {
        this.cDTransportPortOfArrivalDesc = cDTransportPortOfArrivalDesc;
    }

    public String getCDTransportPortOfDeparture() {
        return cDTransportPortOfDeparture;
    }

    public void setCDTransportPortOfDeparture(String cDTransportPortOfDeparture) {
        this.cDTransportPortOfDeparture = cDTransportPortOfDeparture;
    }

    public String getCDTransportPortOfDepartureDesc() {
        return cDTransportPortOfDepartureDesc;
    }

    public void setCDTransportPortOfDepartureDesc(String cDTransportPortOfDepartureDesc) {
        this.cDTransportPortOfDepartureDesc = cDTransportPortOfDepartureDesc;
    }

    public String getCDTransportVesselName() {
        return cDTransportVesselName;
    }

    public void setCDTransportVesselName(String cDTransportVesselName) {
        this.cDTransportVesselName = cDTransportVesselName;
    }

    public String getCDTransportVoyageNo() {
        return cDTransportVoyageNo;
    }

    public void setCDTransportVoyageNo(String cDTransportVoyageNo) {
        this.cDTransportVoyageNo = cDTransportVoyageNo;
    }

    public String getCDTransportShipmentDate() {
        return cDTransportShipmentDate;
    }

    public void setCDTransportShipmentDate(String cDTransportShipmentDate) {
        this.cDTransportShipmentDate = cDTransportShipmentDate;
    }

    public String getCDTransportCarrier() {
        return cDTransportCarrier;
    }

    public void setCDTransportCarrier(String cDTransportCarrier) {
        this.cDTransportCarrier = cDTransportCarrier;
    }

    public String getCDTransportManifestNo() {
        return cDTransportManifestNo;
    }

    public void setCDTransportManifestNo(String cDTransportManifestNo) {
        this.cDTransportManifestNo = cDTransportManifestNo;
    }

    public String getCDTransportBLAWB() {
        return cDTransportBLAWB;
    }

    public void setCDTransportBLAWB(String cDTransportBLAWB) {
        this.cDTransportBLAWB = cDTransportBLAWB;
    }

    public String getCDTransportMarksAndNumbers() {
        return cDTransportMarksAndNumbers;
    }

    public void setCDTransportMarksAndNumbers(String cDTransportMarksAndNumbers) {
        this.cDTransportMarksAndNumbers = cDTransportMarksAndNumbers;
    }

    public String getCDTransportCustomsOffice() {
        return cDTransportCustomsOffice;
    }

    public void setCDTransportCustomsOffice(String cDTransportCustomsOffice) {
        this.cDTransportCustomsOffice = cDTransportCustomsOffice;
    }

    public String getCDTransportCustomsOfficeDesc() {
        return cDTransportCustomsOfficeDesc;
    }

    public void setCDTransportCustomsOfficeDesc(String cDTransportCustomsOfficeDesc) {
        this.cDTransportCustomsOfficeDesc = cDTransportCustomsOfficeDesc;
    }

    public String getCDTransportFreightStation() {
        return cDTransportFreightStation;
    }

    public void setCDTransportFreightStation(String cDTransportFreightStation) {
        this.cDTransportFreightStation = cDTransportFreightStation;
    }

    public String getCDTransportFreightStationDesc() {
        return cDTransportFreightStationDesc;
    }

    public void setCDTransportFreightStationDesc(String cDTransportFreightStationDesc) {
        this.cDTransportFreightStationDesc = cDTransportFreightStationDesc;
    }

    public String getCDTransportCargoTypeIndicator() {
        return cDTransportCargoTypeIndicator;
    }

    public void setCDTransportCargoTypeIndicator(String cDTransportCargoTypeIndicator) {
        this.cDTransportCargoTypeIndicator = cDTransportCargoTypeIndicator;
    }

    public String getCDTransportInlandTransportCo() {
        return cDTransportInlandTransportCo;
    }

    public void setCDTransportInlandTransportCo(String cDTransportInlandTransportCo) {
        this.cDTransportInlandTransportCo = cDTransportInlandTransportCo;
    }

    public String getCDTransportInlandTransportCoRefNo() {
        return cDTransportInlandTransportCoRefNo;
    }

    public void setCDTransportInlandTransportCoRefNo(String cDTransportInlandTransportCoRefNo) {
        this.cDTransportInlandTransportCoRefNo = cDTransportInlandTransportCoRefNo;
    }

    public String getEndCDTransport() {
        return endCDTransport;
    }

    public void setEndCDTransport(String endCDTransport) {
        this.endCDTransport = endCDTransport;
    }

    public String getStrPGAHeaderFields() {
        return strPGAHeaderFields;
    }

    public void setStrPGAHeaderFields(String strPGAHeaderFields) {
        this.strPGAHeaderFields = strPGAHeaderFields;
    }

    public BigInteger getPGAHeaderFieldsCollectionOffice() {
        return pGAHeaderFieldsCollectionOffice;
    }

    public void setPGAHeaderFieldsCollectionOffice(BigInteger pGAHeaderFieldsCollectionOffice) {
        this.pGAHeaderFieldsCollectionOffice = pGAHeaderFieldsCollectionOffice;
    }

    public String getPGAHeaderFieldsPreferredInspectionDate() {
        return pGAHeaderFieldsPreferredInspectionDate;
    }

    public void setPGAHeaderFieldsPreferredInspectionDate(String pGAHeaderFieldsPreferredInspectionDate) {
        this.pGAHeaderFieldsPreferredInspectionDate = pGAHeaderFieldsPreferredInspectionDate;
    }

    public String getEndPGAHeaderFields() {
        return endPGAHeaderFields;
    }

    public void setEndPGAHeaderFields(String endPGAHeaderFields) {
        this.endPGAHeaderFields = endPGAHeaderFields;
    }

    public String getStrCDHeaderOne() {
        return strCDHeaderOne;
    }

    public void setStrCDHeaderOne(String strCDHeaderOne) {
        this.strCDHeaderOne = strCDHeaderOne;
    }

    public String getCDHeaderOneInvoiceDate() {
        return cDHeaderOneInvoiceDate;
    }

    public void setCDHeaderOneInvoiceDate(String cDHeaderOneInvoiceDate) {
        this.cDHeaderOneInvoiceDate = cDHeaderOneInvoiceDate;
    }

    public String getCDHeaderOneInvoiceNumber() {
        return cDHeaderOneInvoiceNumber;
    }

    public void setCDHeaderOneInvoiceNumber(String cDHeaderOneInvoiceNumber) {
        this.cDHeaderOneInvoiceNumber = cDHeaderOneInvoiceNumber;
    }

    public String getCDHeaderOneCountryOfSupply() {
        return cDHeaderOneCountryOfSupply;
    }

    public void setCDHeaderOneCountryOfSupply(String cDHeaderOneCountryOfSupply) {
        this.cDHeaderOneCountryOfSupply = cDHeaderOneCountryOfSupply;
    }

    public String getEndCDHeaderOne() {
        return endCDHeaderOne;
    }

    public void setEndCDHeaderOne(String endCDHeaderOne) {
        this.endCDHeaderOne = endCDHeaderOne;
    }

    public String getStrCDProductDetails() {
        return strCDProductDetails;
    }

    public void setStrCDProductDetails(String strCDProductDetails) {
        this.strCDProductDetails = strCDProductDetails;
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

    public Double getCDProduct1ItemNetWeight() {
        return cDProduct1ItemNetWeight;
    }

    public void setCDProduct1ItemNetWeight(Double cDProduct1ItemNetWeight) {
        this.cDProduct1ItemNetWeight = cDProduct1ItemNetWeight;
    }

    public Double getCDProduct1ItemGrossWeight() {
        return cDProduct1ItemGrossWeight;
    }

    public void setCDProduct1ItemGrossWeight(Double cDProduct1ItemGrossWeight) {
        this.cDProduct1ItemGrossWeight = cDProduct1ItemGrossWeight;
    }

    public String getEndCDProductDetails() {
        return endCDProductDetails;
    }

    public void setEndCDProductDetails(String endCDProductDetails) {
        this.endCDProductDetails = endCDProductDetails;
    }
    
}
