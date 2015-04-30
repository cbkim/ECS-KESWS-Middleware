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
@Table(name = "condocheaderview")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcsConDocHeader.findAll", query = "SELECT e FROM EcsConDocHeader e"),
    @NamedQuery(name = "EcsConDocHeader.findById", query = "SELECT e FROM EcsConDocHeader e WHERE e.id = :id"),
    @NamedQuery(name = "EcsConDocHeader.findByConsignmentId", query = "SELECT e FROM EcsConDocHeader e WHERE e.consignmentId = :consignmentId"),
    @NamedQuery(name = "EcsConDocHeader.findByDocumentNumber", query = "SELECT e FROM EcsConDocHeader e WHERE e.documentNumber = :documentNumber"),
    @NamedQuery(name = "EcsConDocHeader.findBySenderID", query = "SELECT e FROM EcsConDocHeader e WHERE e.senderID = :senderID"),
    @NamedQuery(name = "EcsConDocHeader.findByConsignmentDocDetails", query = "SELECT e FROM EcsConDocHeader e WHERE e.consignmentDocDetails = :consignmentDocDetails"),
    @NamedQuery(name = "EcsConDocHeader.findByStrCDStandard", query = "SELECT e FROM EcsConDocHeader e WHERE e.strCDStandard = :strCDStandard"),
    @NamedQuery(name = "EcsConDocHeader.findByServiceProviderApplicationCode", query = "SELECT e FROM EcsConDocHeader e WHERE e.serviceProviderApplicationCode = :serviceProviderApplicationCode"),
    @NamedQuery(name = "EcsConDocHeader.findByServiceProviderName", query = "SELECT e FROM EcsConDocHeader e WHERE e.serviceProviderName = :serviceProviderName"),
    @NamedQuery(name = "EcsConDocHeader.findByServiceProviderTIN", query = "SELECT e FROM EcsConDocHeader e WHERE e.serviceProviderTIN = :serviceProviderTIN"),
    @NamedQuery(name = "EcsConDocHeader.findByServiceProviderPhysicalAddress", query = "SELECT e FROM EcsConDocHeader e WHERE e.serviceProviderPhysicalAddress = :serviceProviderPhysicalAddress"),
    @NamedQuery(name = "EcsConDocHeader.findByServiceProviderPhyCountry", query = "SELECT e FROM EcsConDocHeader e WHERE e.serviceProviderPhyCountry = :serviceProviderPhyCountry"),
    @NamedQuery(name = "EcsConDocHeader.findByApplicationDate", query = "SELECT e FROM EcsConDocHeader e WHERE e.applicationDate = :applicationDate"),
    @NamedQuery(name = "EcsConDocHeader.findByUpdatedDate", query = "SELECT e FROM EcsConDocHeader e WHERE e.updatedDate = :updatedDate"),
    @NamedQuery(name = "EcsConDocHeader.findByUCRNo", query = "SELECT e FROM EcsConDocHeader e WHERE e.uCRNo = :uCRNo"),
    @NamedQuery(name = "EcsConDocHeader.findByEndCDStandard", query = "SELECT e FROM EcsConDocHeader e WHERE e.endCDStandard = :endCDStandard"),
    @NamedQuery(name = "EcsConDocHeader.findByStrCDImporter", query = "SELECT e FROM EcsConDocHeader e WHERE e.strCDImporter = :strCDImporter"),
    @NamedQuery(name = "EcsConDocHeader.findByCDImporterName", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDImporterName = :cDImporterName"),
    @NamedQuery(name = "EcsConDocHeader.findByCDImporterTIN", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDImporterTIN = :cDImporterTIN"),
    @NamedQuery(name = "EcsConDocHeader.findByCDImporterPhysicalAddress", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDImporterPhysicalAddress = :cDImporterPhysicalAddress"),
    @NamedQuery(name = "EcsConDocHeader.findByCDImporterPhyCountry", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDImporterPhyCountry = :cDImporterPhyCountry"),
    @NamedQuery(name = "EcsConDocHeader.findByCDImporterPostalAddress", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDImporterPostalAddress = :cDImporterPostalAddress"),
    @NamedQuery(name = "EcsConDocHeader.findByCDImporterPosCountry", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDImporterPosCountry = :cDImporterPosCountry"),
    @NamedQuery(name = "EcsConDocHeader.findByCDImporterTeleFax", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDImporterTeleFax = :cDImporterTeleFax"),
    @NamedQuery(name = "EcsConDocHeader.findByCDImporterEmail", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDImporterEmail = :cDImporterEmail"),
    @NamedQuery(name = "EcsConDocHeader.findByCDImporterSectorofActivity", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDImporterSectorofActivity = :cDImporterSectorofActivity"),
    @NamedQuery(name = "EcsConDocHeader.findByCDImporterWarehouseCode", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDImporterWarehouseCode = :cDImporterWarehouseCode"),
    @NamedQuery(name = "EcsConDocHeader.findByCDImporterWarehouseLocation", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDImporterWarehouseLocation = :cDImporterWarehouseLocation"),
    @NamedQuery(name = "EcsConDocHeader.findByEndCDImporter", query = "SELECT e FROM EcsConDocHeader e WHERE e.endCDImporter = :endCDImporter"),
    @NamedQuery(name = "EcsConDocHeader.findByStrCDConsignee", query = "SELECT e FROM EcsConDocHeader e WHERE e.strCDConsignee = :strCDConsignee"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsigneName", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsigneName = :cDConsigneName"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsigneeTIN", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsigneeTIN = :cDConsigneeTIN"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsigneEPhysicalAddress", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsigneEPhysicalAddress = :cDConsigneEPhysicalAddress"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsigneePhyCountry", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsigneePhyCountry = :cDConsigneePhyCountry"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsigneePostalAddress", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsigneePostalAddress = :cDConsigneePostalAddress"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsigneePosCountry", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsigneePosCountry = :cDConsigneePosCountry"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsigneeTeleFax", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsigneeTeleFax = :cDConsigneeTeleFax"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsigneeEmail", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsigneeEmail = :cDConsigneeEmail"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsigneeSectorofActivity", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsigneeSectorofActivity = :cDConsigneeSectorofActivity"),
    @NamedQuery(name = "EcsConDocHeader.findByCCDConsigneeWarehouseCode", query = "SELECT e FROM EcsConDocHeader e WHERE e.cCDConsigneeWarehouseCode = :cCDConsigneeWarehouseCode"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsigneeWarehouseLocation", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsigneeWarehouseLocation = :cDConsigneeWarehouseLocation"),
    @NamedQuery(name = "EcsConDocHeader.findByEndCDConsignee", query = "SELECT e FROM EcsConDocHeader e WHERE e.endCDConsignee = :endCDConsignee"),
    @NamedQuery(name = "EcsConDocHeader.findByStrCDExporter", query = "SELECT e FROM EcsConDocHeader e WHERE e.strCDExporter = :strCDExporter"),
    @NamedQuery(name = "EcsConDocHeader.findByCDExporterName", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDExporterName = :cDExporterName"),
    @NamedQuery(name = "EcsConDocHeader.findByCDExporterTIN", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDExporterTIN = :cDExporterTIN"),
    @NamedQuery(name = "EcsConDocHeader.findByCDExporterPhysicalAddress", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDExporterPhysicalAddress = :cDExporterPhysicalAddress"),
    @NamedQuery(name = "EcsConDocHeader.findByCDExporterPhyCountry", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDExporterPhyCountry = :cDExporterPhyCountry"),
    @NamedQuery(name = "EcsConDocHeader.findByCDExporterPostalAddress", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDExporterPostalAddress = :cDExporterPostalAddress"),
    @NamedQuery(name = "EcsConDocHeader.findByCDExporterPosCountry", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDExporterPosCountry = :cDExporterPosCountry"),
    @NamedQuery(name = "EcsConDocHeader.findByCDExporterTeleFax", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDExporterTeleFax = :cDExporterTeleFax"),
    @NamedQuery(name = "EcsConDocHeader.findByCDExporterEmail", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDExporterEmail = :cDExporterEmail"),
    @NamedQuery(name = "EcsConDocHeader.findByCDExporterSectorofActivity", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDExporterSectorofActivity = :cDExporterSectorofActivity"),
    @NamedQuery(name = "EcsConDocHeader.findByCDExporterWarehouseCode", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDExporterWarehouseCode = :cDExporterWarehouseCode"),
    @NamedQuery(name = "EcsConDocHeader.findByCDExporterWarehouseLocation", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDExporterWarehouseLocation = :cDExporterWarehouseLocation"),
    @NamedQuery(name = "EcsConDocHeader.findByEndCDExporter", query = "SELECT e FROM EcsConDocHeader e WHERE e.endCDExporter = :endCDExporter"),
    @NamedQuery(name = "EcsConDocHeader.findByStrCDConsignor", query = "SELECT e FROM EcsConDocHeader e WHERE e.strCDConsignor = :strCDConsignor"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsignorName", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsignorName = :cDConsignorName"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsignorTIN", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsignorTIN = :cDConsignorTIN"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsignorPhysicalAddress", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsignorPhysicalAddress = :cDConsignorPhysicalAddress"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsignorPhyCountry", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsignorPhyCountry = :cDConsignorPhyCountry"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsignorPostalAddress", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsignorPostalAddress = :cDConsignorPostalAddress"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsignorPosCountry", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsignorPosCountry = :cDConsignorPosCountry"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsignorTeleFax", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsignorTeleFax = :cDConsignorTeleFax"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsignorEmail", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsignorEmail = :cDConsignorEmail"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsignorSectorofActivity", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsignorSectorofActivity = :cDConsignorSectorofActivity"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsignorWarehouseCode", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsignorWarehouseCode = :cDConsignorWarehouseCode"),
    @NamedQuery(name = "EcsConDocHeader.findByCDConsignorWarehouseLocation", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDConsignorWarehouseLocation = :cDConsignorWarehouseLocation"),
    @NamedQuery(name = "EcsConDocHeader.findByEndCDConsignor", query = "SELECT e FROM EcsConDocHeader e WHERE e.endCDConsignor = :endCDConsignor"),
    @NamedQuery(name = "EcsConDocHeader.findByStrCDTransport", query = "SELECT e FROM EcsConDocHeader e WHERE e.strCDTransport = :strCDTransport"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportModeOfTransport", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportModeOfTransport = :cDTransportModeOfTransport"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportModeOfTransportDesc", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportModeOfTransportDesc = :cDTransportModeOfTransportDesc"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportPortOfArrival", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportPortOfArrival = :cDTransportPortOfArrival"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportPortOfArrivalDesc", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportPortOfArrivalDesc = :cDTransportPortOfArrivalDesc"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportPortOfDeparture", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportPortOfDeparture = :cDTransportPortOfDeparture"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportPortOfDepartureDesc", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportPortOfDepartureDesc = :cDTransportPortOfDepartureDesc"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportVesselName", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportVesselName = :cDTransportVesselName"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportVoyageNo", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportVoyageNo = :cDTransportVoyageNo"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportShipmentDate", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportShipmentDate = :cDTransportShipmentDate"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportCarrier", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportCarrier = :cDTransportCarrier"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportManifestNo", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportManifestNo = :cDTransportManifestNo"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportBLAWB", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportBLAWB = :cDTransportBLAWB"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportMarksAndNumbers", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportMarksAndNumbers = :cDTransportMarksAndNumbers"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportCustomsOffice", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportCustomsOffice = :cDTransportCustomsOffice"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportCustomsOfficeDesc", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportCustomsOfficeDesc = :cDTransportCustomsOfficeDesc"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportFreightStation", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportFreightStation = :cDTransportFreightStation"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportFreightStationDesc", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportFreightStationDesc = :cDTransportFreightStationDesc"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportCargoTypeIndicator", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportCargoTypeIndicator = :cDTransportCargoTypeIndicator"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportInlandTransportCo", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportInlandTransportCo = :cDTransportInlandTransportCo"),
    @NamedQuery(name = "EcsConDocHeader.findByCDTransportInlandTransportCoRefNo", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDTransportInlandTransportCoRefNo = :cDTransportInlandTransportCoRefNo"),
    @NamedQuery(name = "EcsConDocHeader.findByEndCDTransport", query = "SELECT e FROM EcsConDocHeader e WHERE e.endCDTransport = :endCDTransport"),
    @NamedQuery(name = "EcsConDocHeader.findByStrPGAHeaderFields", query = "SELECT e FROM EcsConDocHeader e WHERE e.strPGAHeaderFields = :strPGAHeaderFields"),
    @NamedQuery(name = "EcsConDocHeader.findByPGAHeaderFieldsCollectionOffice", query = "SELECT e FROM EcsConDocHeader e WHERE e.pGAHeaderFieldsCollectionOffice = :pGAHeaderFieldsCollectionOffice"),
    @NamedQuery(name = "EcsConDocHeader.findByPGAHeaderFieldsPreferredInspectionDate", query = "SELECT e FROM EcsConDocHeader e WHERE e.pGAHeaderFieldsPreferredInspectionDate = :pGAHeaderFieldsPreferredInspectionDate"),
    @NamedQuery(name = "EcsConDocHeader.findByEndPGAHeaderFields", query = "SELECT e FROM EcsConDocHeader e WHERE e.endPGAHeaderFields = :endPGAHeaderFields"),
    @NamedQuery(name = "EcsConDocHeader.findByStrCDHeaderOne", query = "SELECT e FROM EcsConDocHeader e WHERE e.strCDHeaderOne = :strCDHeaderOne"),
    @NamedQuery(name = "EcsConDocHeader.findByCDHeaderOneInvoiceDate", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDHeaderOneInvoiceDate = :cDHeaderOneInvoiceDate"),
    @NamedQuery(name = "EcsConDocHeader.findByCDHeaderOneInvoiceNumber", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDHeaderOneInvoiceNumber = :cDHeaderOneInvoiceNumber"),
    @NamedQuery(name = "EcsConDocHeader.findByCDHeaderOneCountryOfSupply", query = "SELECT e FROM EcsConDocHeader e WHERE e.cDHeaderOneCountryOfSupply = :cDHeaderOneCountryOfSupply"),
    @NamedQuery(name = "EcsConDocHeader.findByEndCDHeaderOne", query = "SELECT e FROM EcsConDocHeader e WHERE e.endCDHeaderOne = :endCDHeaderOne"),
    @NamedQuery(name = "EcsConDocHeader.findByTotalConsignmentForBilling", query = "SELECT e FROM EcsConDocHeader e WHERE e.totalConsignmentForBilling = :totalConsignmentForBilling")})
public class EcsConDocHeader implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "consignment_Id")
    @Id
    private String consignmentId;
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
    @Column(name = "Total_Consignment_For_Billing")
    private String totalConsignmentForBilling;

    public EcsConDocHeader() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(String consignmentId) {
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

    public String getTotalConsignmentForBilling() {
        return totalConsignmentForBilling;
    }

    public void setTotalConsignmentForBilling(String totalConsignmentForBilling) {
        this.totalConsignmentForBilling = totalConsignmentForBilling;
    }
    
}
