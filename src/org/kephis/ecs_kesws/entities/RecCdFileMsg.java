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
@Table(name = "REC_CD_FILE_MSG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecCdFileMsg.findAll", query = "SELECT r FROM RecCdFileMsg r"),
    @NamedQuery(name = "RecCdFileMsg.findByFileName", query = "SELECT r FROM RecCdFileMsg r WHERE r.fileName = :fileName"),
    @NamedQuery(name = "RecCdFileMsg.findByReceivedDateTime", query = "SELECT r FROM RecCdFileMsg r WHERE r.receivedDateTime = :receivedDateTime"),
    @NamedQuery(name = "RecCdFileMsg.findByIsFileXMLValid", query = "SELECT r FROM RecCdFileMsg r WHERE r.isFileXMLValid = :isFileXMLValid"),
    @NamedQuery(name = "RecCdFileMsg.findByIsFileXMLValidForTransaction", query = "SELECT r FROM RecCdFileMsg r WHERE r.isFileXMLValidForTransaction = :isFileXMLValidForTransaction"),
    @NamedQuery(name = "RecCdFileMsg.findByRECCDFileID", query = "SELECT r FROM RecCdFileMsg r WHERE r.rECCDFileID = :rECCDFileID"),
    @NamedQuery(name = "RecCdFileMsg.findByInvoiceNO", query = "SELECT r FROM RecCdFileMsg r WHERE r.invoiceNO = :invoiceNO"),
    @NamedQuery(name = "RecCdFileMsg.findByDateTimeSubmittedToECS", query = "SELECT r FROM RecCdFileMsg r WHERE r.dateTimeSubmittedToECS = :dateTimeSubmittedToECS"),
    @NamedQuery(name = "RecCdFileMsg.findByDateTimePlanned", query = "SELECT r FROM RecCdFileMsg r WHERE r.dateTimePlanned = :dateTimePlanned"),
    @NamedQuery(name = "RecCdFileMsg.findByDateTimeInspected", query = "SELECT r FROM RecCdFileMsg r WHERE r.dateTimeInspected = :dateTimeInspected"),
    @NamedQuery(name = "RecCdFileMsg.findByDateTimeCertIssued", query = "SELECT r FROM RecCdFileMsg r WHERE r.dateTimeCertIssued = :dateTimeCertIssued"),
    @NamedQuery(name = "RecCdFileMsg.findByInspectionStatus", query = "SELECT r FROM RecCdFileMsg r WHERE r.inspectionStatus = :inspectionStatus"),
    @NamedQuery(name = "RecCdFileMsg.findByECSCONSIGNEMENTIDRef", query = "SELECT r FROM RecCdFileMsg r WHERE r.eCSCONSIGNEMENTIDRef = :eCSCONSIGNEMENTIDRef"),
    @NamedQuery(name = "RecCdFileMsg.findByFilePath", query = "SELECT r FROM RecCdFileMsg r WHERE r.filePath = :filePath"),
    @NamedQuery(name = "RecCdFileMsg.findByIspaymentreq", query = "SELECT r FROM RecCdFileMsg r WHERE r.ispaymentreq = :ispaymentreq")})
public class RecCdFileMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "File_Name")
    private String fileName;
    @Basic(optional = false)
    @Column(name = "Received_Date_Time")
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
    @OneToMany(mappedBy = "recCdFileMsgRecCdFileId")
    private Collection<ResCdFileMsg> resCdFileMsgCollection;
    @JoinColumn(name = "MESSAGE_TYPES_MESSAGE_TYPE_ID", referencedColumnName = "MESSAGE_TYPE_ID")
    @ManyToOne(optional = false)
    private MessageTypes messageTypesMessageTypeId;
    @OneToMany(mappedBy = "recCdFileMsgRecCdFileId")
    private Collection<TransactionLogs> transactionLogsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rECCDFILEMSGRECCDFILEIDRef")
    private Collection<CdFileDetails> cdFileDetailsCollection;

    public RecCdFileMsg() {
    }

    public RecCdFileMsg(Integer rECCDFileID) {
        this.rECCDFileID = rECCDFileID;
    }

    public RecCdFileMsg(Integer rECCDFileID, String fileName, Date receivedDateTime, int isFileXMLValid, int isFileXMLValidForTransaction) {
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
        if (!(object instanceof RecCdFileMsg)) {
            return false;
        }
        RecCdFileMsg other = (RecCdFileMsg) object;
        if ((this.rECCDFileID == null && other.rECCDFileID != null) || (this.rECCDFileID != null && !this.rECCDFileID.equals(other.rECCDFileID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.RecCdFileMsg[ rECCDFileID=" + rECCDFileID + " ]";
    }
    
}
