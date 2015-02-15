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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ECS_RES_CD_FILE_MSG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcsResCdFileMsg.findAll", query = "SELECT e FROM EcsResCdFileMsg e"),
    @NamedQuery(name = "EcsResCdFileMsg.findByFileName", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.fileName = :fileName"),
    @NamedQuery(name = "EcsResCdFileMsg.findByReceivedDateTime", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.receivedDateTime = :receivedDateTime"),
    @NamedQuery(name = "EcsResCdFileMsg.findByIsFileXMLValid", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.isFileXMLValid = :isFileXMLValid"),
    @NamedQuery(name = "EcsResCdFileMsg.findByIsFileXMLValidForTransaction", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.isFileXMLValidForTransaction = :isFileXMLValidForTransaction"),
    @NamedQuery(name = "EcsResCdFileMsg.findByRECCDFileID", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.rECCDFileID = :rECCDFileID"),
    @NamedQuery(name = "EcsResCdFileMsg.findByInvoiceNO", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.invoiceNO = :invoiceNO"),
    @NamedQuery(name = "EcsResCdFileMsg.findByDateTimeSubmittedToECS", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.dateTimeSubmittedToECS = :dateTimeSubmittedToECS"),
    @NamedQuery(name = "EcsResCdFileMsg.findByDateTimePlanned", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.dateTimePlanned = :dateTimePlanned"),
    @NamedQuery(name = "EcsResCdFileMsg.findByDateTimeInspected", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.dateTimeInspected = :dateTimeInspected"),
    @NamedQuery(name = "EcsResCdFileMsg.findByDateTimeCertIssued", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.dateTimeCertIssued = :dateTimeCertIssued"),
    @NamedQuery(name = "EcsResCdFileMsg.findByInspectionStatus", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.inspectionStatus = :inspectionStatus"),
    @NamedQuery(name = "EcsResCdFileMsg.findByECSCONSIGNEMENTIDRef", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.eCSCONSIGNEMENTIDRef = :eCSCONSIGNEMENTIDRef"),
    @NamedQuery(name = "EcsResCdFileMsg.findByFilePath", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.filePath = :filePath"),
    @NamedQuery(name = "EcsResCdFileMsg.findByIspaymentreq", query = "SELECT e FROM EcsResCdFileMsg e WHERE e.ispaymentreq = :ispaymentreq")})
public class EcsResCdFileMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "File_Name")
    private String fileName;
    @Basic(optional = false)
    @Column(name = "Received_Date_Time", insertable = false, updatable = false) 
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedDateTime;
    @Basic(optional = false)
    @Column(name = "isFile_XML_Valid")
    private int isFileXMLValid;
    @Basic(optional = false)
    @Column(name = "isFile_XML_Valid_For_Transaction")
    private int isFileXMLValidForTransaction;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "REC_CD_File_ID")
    private Integer rECCDFileID;
    @Column(name = "Invoice_NO")
    private String invoiceNO;
    @Column(name = "Date_Time_Submitted_To_ECS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeSubmittedToECS;
    @Column(name = "Date_Time_Planned")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimePlanned;
    @Column(name = "Date_Time_Inspected")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeInspected;
    @Column(name = "Date_Time_Cert_Issued")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeCertIssued;
    @Column(name = "InspectionStatus")
    private String inspectionStatus;
    @Column(name = "ECS_CONSIGNEMENT_ID_Ref")
    private Integer eCSCONSIGNEMENTIDRef;
    @Column(name = "FILE_PATH")
    private String filePath;
    @Column(name = "ISPAYMENTREQ")
    private Integer ispaymentreq;
    @OneToMany(mappedBy = "eCSRESCDFILEMSGRECCDFileID",fetch = FetchType.EAGER)
    private Collection<ResCdFileMsg> resCdFileMsgCollection;
    @JoinColumn(name = "MESSAGE_TYPES_MESSAGE_TYPE_ID", referencedColumnName = "MESSAGE_TYPE_ID")
    @ManyToOne(optional = false)
    private MessageTypes messageTypesMessageTypeId;
    @OneToMany(mappedBy = "eCSRESCDFILEMSGRECCDFileID")
    private Collection<TransactionLogs> transactionLogsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eCSRESCDFILEMSGRECCDFileID",fetch = FetchType.EAGER)
    private Collection<CdFileDetails> cdFileDetailsCollection;

    public EcsResCdFileMsg() {
    }

    public EcsResCdFileMsg(Integer rECCDFileID) {
        this.rECCDFileID = rECCDFileID;
    }

    public EcsResCdFileMsg(Integer rECCDFileID, String fileName, Date receivedDateTime, int isFileXMLValid, int isFileXMLValidForTransaction) {
        this.rECCDFileID = rECCDFileID;
        this.fileName = fileName;
        this.receivedDateTime = receivedDateTime;
        this.isFileXMLValid = isFileXMLValid;
        this.isFileXMLValidForTransaction = isFileXMLValidForTransaction;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getReceivedDateTime() {
        return receivedDateTime;
    }

    public void setReceivedDateTime(Date receivedDateTime) {
        this.receivedDateTime = receivedDateTime;
    }

    public int getIsFileXMLValid() {
        return isFileXMLValid;
    }

    public void setIsFileXMLValid(int isFileXMLValid) {
        this.isFileXMLValid = isFileXMLValid;
    }

    public int getIsFileXMLValidForTransaction() {
        return isFileXMLValidForTransaction;
    }

    public void setIsFileXMLValidForTransaction(int isFileXMLValidForTransaction) {
        this.isFileXMLValidForTransaction = isFileXMLValidForTransaction;
    }

    public Integer getRECCDFileID() {
        return rECCDFileID;
    }

    public void setRECCDFileID(Integer rECCDFileID) {
        this.rECCDFileID = rECCDFileID;
    }

    public String getInvoiceNO() {
        return invoiceNO;
    }

    public void setInvoiceNO(String invoiceNO) {
        this.invoiceNO = invoiceNO;
    }

    public Date getDateTimeSubmittedToECS() {
        return dateTimeSubmittedToECS;
    }

    public void setDateTimeSubmittedToECS(Date dateTimeSubmittedToECS) {
        this.dateTimeSubmittedToECS = dateTimeSubmittedToECS;
    }

    public Date getDateTimePlanned() {
        return dateTimePlanned;
    }

    public void setDateTimePlanned(Date dateTimePlanned) {
        this.dateTimePlanned = dateTimePlanned;
    }

    public Date getDateTimeInspected() {
        return dateTimeInspected;
    }

    public void setDateTimeInspected(Date dateTimeInspected) {
        this.dateTimeInspected = dateTimeInspected;
    }

    public Date getDateTimeCertIssued() {
        return dateTimeCertIssued;
    }

    public void setDateTimeCertIssued(Date dateTimeCertIssued) {
        this.dateTimeCertIssued = dateTimeCertIssued;
    }

    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(String inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }

    public Integer getECSCONSIGNEMENTIDRef() {
        return eCSCONSIGNEMENTIDRef;
    }

    public void setECSCONSIGNEMENTIDRef(Integer eCSCONSIGNEMENTIDRef) {
        this.eCSCONSIGNEMENTIDRef = eCSCONSIGNEMENTIDRef;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getIspaymentreq() {
        return ispaymentreq;
    }

    public void setIspaymentreq(Integer ispaymentreq) {
        this.ispaymentreq = ispaymentreq;
    }

    @XmlTransient
    public Collection<ResCdFileMsg> getResCdFileMsgCollection() {
        return resCdFileMsgCollection;
    }

    public void setResCdFileMsgCollection(Collection<ResCdFileMsg> resCdFileMsgCollection) {
        this.resCdFileMsgCollection = resCdFileMsgCollection;
    }

    public MessageTypes getMessageTypesMessageTypeId() {
        return messageTypesMessageTypeId;
    }

    public void setMessageTypesMessageTypeId(MessageTypes messageTypesMessageTypeId) {
        this.messageTypesMessageTypeId = messageTypesMessageTypeId;
    }

    @XmlTransient
    public Collection<TransactionLogs> getTransactionLogsCollection() {
        return transactionLogsCollection;
    }

    public void setTransactionLogsCollection(Collection<TransactionLogs> transactionLogsCollection) {
        this.transactionLogsCollection = transactionLogsCollection;
    }

    @XmlTransient
    public Collection<CdFileDetails> getCdFileDetailsCollection() {
        return cdFileDetailsCollection;
    }

    public void setCdFileDetailsCollection(Collection<CdFileDetails> cdFileDetailsCollection) {
        this.cdFileDetailsCollection = cdFileDetailsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rECCDFileID != null ? rECCDFileID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcsResCdFileMsg)) {
            return false;
        }
        EcsResCdFileMsg other = (EcsResCdFileMsg) object;
        if ((this.rECCDFileID == null && other.rECCDFileID != null) || (this.rECCDFileID != null && !this.rECCDFileID.equals(other.rECCDFileID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.EcsResCdFileMsg[ rECCDFileID=" + rECCDFileID + " ]";
    }
    
}
