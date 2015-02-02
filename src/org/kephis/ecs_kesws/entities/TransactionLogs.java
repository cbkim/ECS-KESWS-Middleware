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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "TRANSACTION_LOGS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransactionLogs.findAll", query = "SELECT t FROM TransactionLogs t"),
    @NamedQuery(name = "TransactionLogs.findByLogID", query = "SELECT t FROM TransactionLogs t WHERE t.logID = :logID"),
    @NamedQuery(name = "TransactionLogs.findByDateTimeLogged", query = "SELECT t FROM TransactionLogs t WHERE t.dateTimeLogged = :dateTimeLogged")})
public class TransactionLogs implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Log_ID")
    private Integer logID;
    @Basic(optional = false)
    @Lob
    @Column(name = "Log_Details")
    private String logDetails;
    @Basic(optional = false)
    @Column(name = "Date_Time_Logged")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeLogged;
    @JoinColumn(name = "RES_CD_FILE_MSG_RES_CD_FILE_ID", referencedColumnName = "RES_CD_FILE_ID")
    @ManyToOne
    private ResCdFileMsg resCdFileMsgResCdFileId;
    @JoinColumn(name = "REC_PAYMENT_MSG_RECEIVED_PAYMENT_MSG_ID", referencedColumnName = "RECEIVED_PAYMENT_MSG_ID")
    @ManyToOne
    private RecPaymentFileMsg recPaymentMsgReceivedPaymentMsgId;
    @JoinColumn(name = "REC_CD_FILE_MSG_REC_CD_FILE_ID", referencedColumnName = "REC_CD_File_ID")
    @ManyToOne
    private RecCdFileMsg recCdFileMsgRecCdFileId;
    @JoinColumn(name = "LOG_TYPES_LOG_ID_LEVEL", referencedColumnName = "LOG_ID_LEVEL")
    @ManyToOne(optional = false)
    private LogTypes logTypesLogIdLevel;
    @JoinColumn(name = "ECS_RES_CD_FILE_MSG_REC_CD_File_ID", referencedColumnName = "REC_CD_File_ID")
    @ManyToOne
    private EcsResCdFileMsg eCSRESCDFILEMSGRECCDFileID;

    public TransactionLogs() {
    }

    public TransactionLogs(Integer logID) {
        this.logID = logID;
    }

    public TransactionLogs(Integer logID, String logDetails, Date dateTimeLogged) {
        this.logID = logID;
        this.logDetails = logDetails;
        this.dateTimeLogged = dateTimeLogged;
    }

    public Integer getLogID() {
        return logID;
    }

    public void setLogID(Integer logID) {
        this.logID = logID;
    }

    public String getLogDetails() {
        return logDetails;
    }

    public void setLogDetails(String logDetails) {
        this.logDetails = logDetails;
    }

    public Date getDateTimeLogged() {
        return dateTimeLogged;
    }

    public void setDateTimeLogged(Date dateTimeLogged) {
        this.dateTimeLogged = dateTimeLogged;
    }

    public ResCdFileMsg getResCdFileMsgResCdFileId() {
        return resCdFileMsgResCdFileId;
    }

    public void setResCdFileMsgResCdFileId(ResCdFileMsg resCdFileMsgResCdFileId) {
        this.resCdFileMsgResCdFileId = resCdFileMsgResCdFileId;
    }

    public RecPaymentFileMsg getRecPaymentMsgReceivedPaymentMsgId() {
        return recPaymentMsgReceivedPaymentMsgId;
    }

    public void setRecPaymentMsgReceivedPaymentMsgId(RecPaymentFileMsg recPaymentMsgReceivedPaymentMsgId) {
        this.recPaymentMsgReceivedPaymentMsgId = recPaymentMsgReceivedPaymentMsgId;
    }

    public RecCdFileMsg getRecCdFileMsgRecCdFileId() {
        return recCdFileMsgRecCdFileId;
    }

    public void setRecCdFileMsgRecCdFileId(RecCdFileMsg recCdFileMsgRecCdFileId) {
        this.recCdFileMsgRecCdFileId = recCdFileMsgRecCdFileId;
    }

    public LogTypes getLogTypesLogIdLevel() {
        return logTypesLogIdLevel;
    }

    public void setLogTypesLogIdLevel(LogTypes logTypesLogIdLevel) {
        this.logTypesLogIdLevel = logTypesLogIdLevel;
    }

    public EcsResCdFileMsg getECSRESCDFILEMSGRECCDFileID() {
        return eCSRESCDFILEMSGRECCDFileID;
    }

    public void setECSRESCDFILEMSGRECCDFileID(EcsResCdFileMsg eCSRESCDFILEMSGRECCDFileID) {
        this.eCSRESCDFILEMSGRECCDFileID = eCSRESCDFILEMSGRECCDFileID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logID != null ? logID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionLogs)) {
            return false;
        }
        TransactionLogs other = (TransactionLogs) object;
        if ((this.logID == null && other.logID != null) || (this.logID != null && !this.logID.equals(other.logID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.TransactionLogs[ logID=" + logID + " ]";
    }
    
}
