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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "REC_PAYMENT_FILE_MSG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecPaymentFileMsg.findAll", query = "SELECT r FROM RecPaymentFileMsg r"),
    @NamedQuery(name = "RecPaymentFileMsg.findByReceivedPaymentMsgId", query = "SELECT r FROM RecPaymentFileMsg r WHERE r.receivedPaymentMsgId = :receivedPaymentMsgId"),
    @NamedQuery(name = "RecPaymentFileMsg.findByFileName", query = "SELECT r FROM RecPaymentFileMsg r WHERE r.fileName = :fileName"),
    @NamedQuery(name = "RecPaymentFileMsg.findByFilePath", query = "SELECT r FROM RecPaymentFileMsg r WHERE r.filePath = :filePath"),
    @NamedQuery(name = "RecPaymentFileMsg.findByTimeReceived", query = "SELECT r FROM RecPaymentFileMsg r WHERE r.timeReceived = :timeReceived"),
    @NamedQuery(name = "RecPaymentFileMsg.findByAmountPaid", query = "SELECT r FROM RecPaymentFileMsg r WHERE r.amountPaid = :amountPaid"),
    @NamedQuery(name = "RecPaymentFileMsg.findByDatePaid", query = "SELECT r FROM RecPaymentFileMsg r WHERE r.datePaid = :datePaid"),
    @NamedQuery(name = "RecPaymentFileMsg.findByModeOfPayment", query = "SELECT r FROM RecPaymentFileMsg r WHERE r.modeOfPayment = :modeOfPayment"),
    @NamedQuery(name = "RecPaymentFileMsg.findBySentPayementMsgPayementMsgId", query = "SELECT r FROM RecPaymentFileMsg r WHERE r.sentPayementMsgPayementMsgId = :sentPayementMsgPayementMsgId"),
    @NamedQuery(name = "RecPaymentFileMsg.findByPrepaymentAccount", query = "SELECT r FROM RecPaymentFileMsg r WHERE r.prepaymentAccount = :prepaymentAccount"),
    @NamedQuery(name = "RecPaymentFileMsg.findByTransactionNumber", query = "SELECT r FROM RecPaymentFileMsg r WHERE r.transactionNumber = :transactionNumber"),
    @NamedQuery(name = "RecPaymentFileMsg.findByMessageTypesMessageTypeId", query = "SELECT r FROM RecPaymentFileMsg r WHERE r.messageTypesMessageTypeId = :messageTypesMessageTypeId")})
public class RecPaymentFileMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "RECEIVED_PAYMENT_MSG_ID")
    private Integer receivedPaymentMsgId;
    @Basic(optional = false)
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "FILE_PATH")
    private String filePath;
    @Basic(optional = false)
    @Column(name = "TIME_RECEIVED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeReceived;
    @Column(name = "AMOUNT_PAID")
    private String amountPaid;
    @Column(name = "DATE_PAID")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaid;
    @Column(name = "MODE_OF_PAYMENT")
    private String modeOfPayment;
    @Basic(optional = false)
    @Column(name = "SENT_PAYEMENT_MSG_PAYEMENT_MSG_ID")
    private int sentPayementMsgPayementMsgId;
    @Column(name = "PREPAYMENT_ACCOUNT")
    private String prepaymentAccount;
    @Column(name = "TRANSACTION_NUMBER")
    private String transactionNumber;
    @Basic(optional = false)
    @Column(name = "MESSAGE_TYPES_MESSAGE_TYPE_ID")
    private int messageTypesMessageTypeId;
    @OneToMany(mappedBy = "recPaymentFileId")
    private Collection<PaymentInfoLog> paymentInfoLogCollection;
    @OneToMany(mappedBy = "recPaymentMsgReceivedPaymentMsgId")
    private Collection<TransactionLogs> transactionLogsCollection;

    public RecPaymentFileMsg() {
    }

    public RecPaymentFileMsg(Integer receivedPaymentMsgId) {
        this.receivedPaymentMsgId = receivedPaymentMsgId;
    }

    public RecPaymentFileMsg(Integer receivedPaymentMsgId, String fileName, Date timeReceived, int sentPayementMsgPayementMsgId, int messageTypesMessageTypeId) {
        this.receivedPaymentMsgId = receivedPaymentMsgId;
        this.fileName = fileName;
        this.timeReceived = timeReceived;
        this.sentPayementMsgPayementMsgId = sentPayementMsgPayementMsgId;
        this.messageTypesMessageTypeId = messageTypesMessageTypeId;
    }

    public Integer getReceivedPaymentMsgId() {
        return receivedPaymentMsgId;
    }

    public void setReceivedPaymentMsgId(Integer receivedPaymentMsgId) {
        this.receivedPaymentMsgId = receivedPaymentMsgId;
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

    public Date getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(Date timeReceived) {
        this.timeReceived = timeReceived;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public int getSentPayementMsgPayementMsgId() {
        return sentPayementMsgPayementMsgId;
    }

    public void setSentPayementMsgPayementMsgId(int sentPayementMsgPayementMsgId) {
        this.sentPayementMsgPayementMsgId = sentPayementMsgPayementMsgId;
    }

    public String getPrepaymentAccount() {
        return prepaymentAccount;
    }

    public void setPrepaymentAccount(String prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public int getMessageTypesMessageTypeId() {
        return messageTypesMessageTypeId;
    }

    public void setMessageTypesMessageTypeId(int messageTypesMessageTypeId) {
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
        hash += (receivedPaymentMsgId != null ? receivedPaymentMsgId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecPaymentFileMsg)) {
            return false;
        }
        RecPaymentFileMsg other = (RecPaymentFileMsg) object;
        if ((this.receivedPaymentMsgId == null && other.receivedPaymentMsgId != null) || (this.receivedPaymentMsgId != null && !this.receivedPaymentMsgId.equals(other.receivedPaymentMsgId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.RecPaymentFileMsg[ receivedPaymentMsgId=" + receivedPaymentMsgId + " ]";
    }
    
}
