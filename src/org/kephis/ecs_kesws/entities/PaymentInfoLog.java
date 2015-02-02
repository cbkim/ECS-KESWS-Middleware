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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kim
 */
@Entity
@Table(name = "PAYMENT_INFO_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaymentInfoLog.findAll", query = "SELECT p FROM PaymentInfoLog p"),
    @NamedQuery(name = "PaymentInfoLog.findByPayementMsgId", query = "SELECT p FROM PaymentInfoLog p WHERE p.payementMsgId = :payementMsgId"),
    @NamedQuery(name = "PaymentInfoLog.findByAmountReq", query = "SELECT p FROM PaymentInfoLog p WHERE p.amountReq = :amountReq"),
    @NamedQuery(name = "PaymentInfoLog.findByAmountPaid", query = "SELECT p FROM PaymentInfoLog p WHERE p.amountPaid = :amountPaid"),
    @NamedQuery(name = "PaymentInfoLog.findByCurrency", query = "SELECT p FROM PaymentInfoLog p WHERE p.currency = :currency"),
    @NamedQuery(name = "PaymentInfoLog.findByRevenueCode", query = "SELECT p FROM PaymentInfoLog p WHERE p.revenueCode = :revenueCode"),
    @NamedQuery(name = "PaymentInfoLog.findByIsECSPaymentMsg", query = "SELECT p FROM PaymentInfoLog p WHERE p.isECSPaymentMsg = :isECSPaymentMsg"),
    @NamedQuery(name = "PaymentInfoLog.findByEcsCsgId", query = "SELECT p FROM PaymentInfoLog p WHERE p.ecsCsgId = :ecsCsgId")})
public class PaymentInfoLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PAYEMENT_MSG_ID")
    private Integer payementMsgId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AMOUNT_REQ")
    private Double amountReq;
    @Column(name = "AMOUNT_PAID")
    private Double amountPaid;
    @Column(name = "CURRENCY")
    private String currency;
    @Column(name = "REVENUE_CODE")
    private String revenueCode;
    @Column(name = "isECS_Payment_Msg")
    private Integer isECSPaymentMsg;
    @Column(name = "ECS_CSG_ID")
    private Integer ecsCsgId;
    @JoinColumn(name = "REC_PAYMENT_FILE_ID", referencedColumnName = "RECEIVED_PAYMENT_MSG_ID")
    @ManyToOne
    private RecPaymentFileMsg recPaymentFileId;
    @JoinColumn(name = "RES_CD_FILE_MSG_RES_CD_FILE_ID", referencedColumnName = "RES_CD_FILE_ID")
    @ManyToOne
    private ResCdFileMsg resCdFileMsgResCdFileId;
    @JoinColumn(name = "PRICELIST_INTERNAL_PRODUCTCODE_DOCUMENT_MAP_Pricelist_IPC_MAP_ID", referencedColumnName = "Pricelist_IPC_MAP_ID")
    @ManyToOne
    private PricelistInternalProductcodeDocumentMap pRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID;

    public PaymentInfoLog() {
    }

    public PaymentInfoLog(Integer payementMsgId) {
        this.payementMsgId = payementMsgId;
    }

    public Integer getPayementMsgId() {
        return payementMsgId;
    }

    public void setPayementMsgId(Integer payementMsgId) {
        this.payementMsgId = payementMsgId;
    }

    public Double getAmountReq() {
        return amountReq;
    }

    public void setAmountReq(Double amountReq) {
        this.amountReq = amountReq;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRevenueCode() {
        return revenueCode;
    }

    public void setRevenueCode(String revenueCode) {
        this.revenueCode = revenueCode;
    }

    public Integer getIsECSPaymentMsg() {
        return isECSPaymentMsg;
    }

    public void setIsECSPaymentMsg(Integer isECSPaymentMsg) {
        this.isECSPaymentMsg = isECSPaymentMsg;
    }

    public Integer getEcsCsgId() {
        return ecsCsgId;
    }

    public void setEcsCsgId(Integer ecsCsgId) {
        this.ecsCsgId = ecsCsgId;
    }

    public RecPaymentFileMsg getRecPaymentFileId() {
        return recPaymentFileId;
    }

    public void setRecPaymentFileId(RecPaymentFileMsg recPaymentFileId) {
        this.recPaymentFileId = recPaymentFileId;
    }

    public ResCdFileMsg getResCdFileMsgResCdFileId() {
        return resCdFileMsgResCdFileId;
    }

    public void setResCdFileMsgResCdFileId(ResCdFileMsg resCdFileMsgResCdFileId) {
        this.resCdFileMsgResCdFileId = resCdFileMsgResCdFileId;
    }

    public PricelistInternalProductcodeDocumentMap getPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID() {
        return pRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID;
    }

    public void setPRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID(PricelistInternalProductcodeDocumentMap pRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID) {
        this.pRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID = pRICELISTINTERNALPRODUCTCODEDOCUMENTMAPPricelistIPCMAPID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (payementMsgId != null ? payementMsgId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentInfoLog)) {
            return false;
        }
        PaymentInfoLog other = (PaymentInfoLog) object;
        if ((this.payementMsgId == null && other.payementMsgId != null) || (this.payementMsgId != null && !this.payementMsgId.equals(other.payementMsgId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.PaymentInfoLog[ payementMsgId=" + payementMsgId + " ]";
    }
    
}
