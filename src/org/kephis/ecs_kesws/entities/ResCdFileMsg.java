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
@Table(name = "RES_CD_FILE_MSG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResCdFileMsg.findAll", query = "SELECT r FROM ResCdFileMsg r"),
    @NamedQuery(name = "ResCdFileMsg.findByResCdFileId", query = "SELECT r FROM ResCdFileMsg r WHERE r.resCdFileId = :resCdFileId"),
    @NamedQuery(name = "ResCdFileMsg.findByFileName", query = "SELECT r FROM ResCdFileMsg r WHERE r.fileName = :fileName"),
    @NamedQuery(name = "ResCdFileMsg.findByFilePath", query = "SELECT r FROM ResCdFileMsg r WHERE r.filePath = :filePath"),
    @NamedQuery(name = "ResCdFileMsg.findByOutboxFileItemTotalWeight", query = "SELECT r FROM ResCdFileMsg r WHERE r.outboxFileItemTotalWeight = :outboxFileItemTotalWeight"),
    @NamedQuery(name = "ResCdFileMsg.findByOutboxFileItemTotalCost", query = "SELECT r FROM ResCdFileMsg r WHERE r.outboxFileItemTotalCost = :outboxFileItemTotalCost"),
    @NamedQuery(name = "ResCdFileMsg.findByOutboxFileItemDetails", query = "SELECT r FROM ResCdFileMsg r WHERE r.outboxFileItemDetails = :outboxFileItemDetails"),
    @NamedQuery(name = "ResCdFileMsg.findByOutboxFileItemDetailInspectionType", query = "SELECT r FROM ResCdFileMsg r WHERE r.outboxFileItemDetailInspectionType = :outboxFileItemDetailInspectionType"),
    @NamedQuery(name = "ResCdFileMsg.findByOutboxFileItemInspectorId", query = "SELECT r FROM ResCdFileMsg r WHERE r.outboxFileItemInspectorId = :outboxFileItemInspectorId"),
    @NamedQuery(name = "ResCdFileMsg.findByComments", query = "SELECT r FROM ResCdFileMsg r WHERE r.comments = :comments"),
    @NamedQuery(name = "ResCdFileMsg.findByTimeSent", query = "SELECT r FROM ResCdFileMsg r WHERE r.timeSent = :timeSent"),
    @NamedQuery(name = "ResCdFileMsg.findBySendAck", query = "SELECT r FROM ResCdFileMsg r WHERE r.sendAck = :sendAck")})
public class ResCdFileMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "RES_CD_FILE_ID")
    private Integer resCdFileId;
    @Basic(optional = false)
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "FILE_PATH")
    private String filePath;
    @Column(name = "OUTBOX_FILE_ITEM_TOTAL_WEIGHT")
    private String outboxFileItemTotalWeight;
    @Column(name = "OUTBOX_FILE_ITEM_TOTAL_COST")
    private String outboxFileItemTotalCost;
    @Column(name = "OUTBOX_FILE_ITEM_DETAILS")
    private String outboxFileItemDetails;
    @Column(name = "OUTBOX_FILE_ITEM_DETAIL_INSPECTION_TYPE")
    private String outboxFileItemDetailInspectionType;
    @Column(name = "OUTBOX_FILE_ITEM_INSPECTOR_ID")
    private String outboxFileItemInspectorId;
    @Column(name = "COMMENTS")
    private String comments;
    @Column(name = "TIME_SENT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeSent;
    @Column(name = "SEND_ACK")
    private Integer sendAck;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "resCdFileMsgResCdFileId")
    private Collection<RecErrorFileMsg> recErrorFileMsgCollection;
    @JoinColumn(name = "REC_CD_FILE_MSG_REC_CD_FILE_ID", referencedColumnName = "REC_CD_File_ID")
    @ManyToOne
    private RecCdFileMsg recCdFileMsgRecCdFileId;
    @JoinColumn(name = "ECS_RES_CD_FILE_MSG_REC_CD_File_ID", referencedColumnName = "REC_CD_File_ID")
    @ManyToOne
    private EcsResCdFileMsg eCSRESCDFILEMSGRECCDFileID;
    @JoinColumn(name = "MESSAGE_TYPES_MESSAGE_TYPE_ID", referencedColumnName = "MESSAGE_TYPE_ID")
    @ManyToOne(optional = false)
    private MessageTypes messageTypesMessageTypeId;
    @OneToMany(mappedBy = "resCdFileMsgResCdFileId")
    private Collection<PaymentInfoLog> paymentInfoLogCollection;
    @OneToMany(mappedBy = "resCdFileMsgResCdFileId")
    private Collection<TransactionLogs> transactionLogsCollection;

    public ResCdFileMsg() {
    }

    public ResCdFileMsg(Integer resCdFileId) {
        this.resCdFileId = resCdFileId;
    }

    public ResCdFileMsg(Integer resCdFileId, String fileName) {
        this.resCdFileId = resCdFileId;
        this.fileName = fileName;
    }

    public Integer getResCdFileId() {
        return resCdFileId;
    }

    public void setResCdFileId(Integer resCdFileId) {
        this.resCdFileId = resCdFileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOutboxFileItemTotalWeight() {
        return outboxFileItemTotalWeight;
    }

    public void setOutboxFileItemTotalWeight(String outboxFileItemTotalWeight) {
        this.outboxFileItemTotalWeight = outboxFileItemTotalWeight;
    }

    public String getOutboxFileItemTotalCost() {
        return outboxFileItemTotalCost;
    }

    public void setOutboxFileItemTotalCost(String outboxFileItemTotalCost) {
        this.outboxFileItemTotalCost = outboxFileItemTotalCost;
    }

    public String getOutboxFileItemDetails() {
        return outboxFileItemDetails;
    }

    public void setOutboxFileItemDetails(String outboxFileItemDetails) {
        this.outboxFileItemDetails = outboxFileItemDetails;
    }

    public String getOutboxFileItemDetailInspectionType() {
        return outboxFileItemDetailInspectionType;
    }

    public void setOutboxFileItemDetailInspectionType(String outboxFileItemDetailInspectionType) {
        this.outboxFileItemDetailInspectionType = outboxFileItemDetailInspectionType;
    }

    public String getOutboxFileItemInspectorId() {
        return outboxFileItemInspectorId;
    }

    public void setOutboxFileItemInspectorId(String outboxFileItemInspectorId) {
        this.outboxFileItemInspectorId = outboxFileItemInspectorId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    public Integer getSendAck() {
        return sendAck;
    }

    public void setSendAck(Integer sendAck) {
        this.sendAck = sendAck;
    }

    @XmlTransient
    public Collection<RecErrorFileMsg> getRecErrorFileMsgCollection() {
        return recErrorFileMsgCollection;
    }

    public void setRecErrorFileMsgCollection(Collection<RecErrorFileMsg> recErrorFileMsgCollection) {
        this.recErrorFileMsgCollection = recErrorFileMsgCollection;
    }

    public RecCdFileMsg getRecCdFileMsgRecCdFileId() {
        return recCdFileMsgRecCdFileId;
    }

    public void setRecCdFileMsgRecCdFileId(RecCdFileMsg recCdFileMsgRecCdFileId) {
        this.recCdFileMsgRecCdFileId = recCdFileMsgRecCdFileId;
    }

    public EcsResCdFileMsg getECSRESCDFILEMSGRECCDFileID() {
        return eCSRESCDFILEMSGRECCDFileID;
    }

    public void setECSRESCDFILEMSGRECCDFileID(EcsResCdFileMsg eCSRESCDFILEMSGRECCDFileID) {
        this.eCSRESCDFILEMSGRECCDFileID = eCSRESCDFILEMSGRECCDFileID;
    }

    public MessageTypes getMessageTypesMessageTypeId() {
        return messageTypesMessageTypeId;
    }

    public void setMessageTypesMessageTypeId(MessageTypes messageTypesMessageTypeId) {
        this.messageTypesMessageTypeId = messageTypesMessageTypeId;
    }

    @XmlTransient
    public Collection<PaymentInfoLog> getPaymentInfoLogCollection() {
        return paymentInfoLogCollection;
    }

    public void setPaymentInfoLogCollection(Collection<PaymentInfoLog> paymentInfoLogCollection) {
        this.paymentInfoLogCollection = paymentInfoLogCollection;
    }

    @XmlTransient
    public Collection<TransactionLogs> getTransactionLogsCollection() {
        return transactionLogsCollection;
    }

    public void setTransactionLogsCollection(Collection<TransactionLogs> transactionLogsCollection) {
        this.transactionLogsCollection = transactionLogsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resCdFileId != null ? resCdFileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResCdFileMsg)) {
            return false;
        }
        ResCdFileMsg other = (ResCdFileMsg) object;
        if ((this.resCdFileId == null && other.resCdFileId != null) || (this.resCdFileId != null && !this.resCdFileId.equals(other.resCdFileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.ResCdFileMsg[ resCdFileId=" + resCdFileId + " ]";
    }
    
}
