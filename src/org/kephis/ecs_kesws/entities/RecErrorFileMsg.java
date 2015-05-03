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
@Table(name = "REC_ERROR_FILE_MSG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecErrorFileMsg.findAll", query = "SELECT r FROM RecErrorFileMsg r"),
    @NamedQuery(name = "RecErrorFileMsg.findByRecErrorMsgId", query = "SELECT r FROM RecErrorFileMsg r WHERE r.recErrorMsgId = :recErrorMsgId"),
    @NamedQuery(name = "RecErrorFileMsg.findByRecErrorMsgTime", query = "SELECT r FROM RecErrorFileMsg r WHERE r.recErrorMsgTime = :recErrorMsgTime"),
    @NamedQuery(name = "RecErrorFileMsg.findByFileName", query = "SELECT r FROM RecErrorFileMsg r WHERE r.fileName = :fileName"),
    @NamedQuery(name = "RecErrorFileMsg.findByFilePath", query = "SELECT r FROM RecErrorFileMsg r WHERE r.filePath = :filePath"),
    @NamedQuery(name = "RecErrorFileMsg.findByEcsResCdFileMsgEcsResCdFileMsgId", query = "SELECT r FROM RecErrorFileMsg r WHERE r.ecsResCdFileMsgEcsResCdFileMsgId = :ecsResCdFileMsgEcsResCdFileMsgId")})
public class RecErrorFileMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "REC_ERROR_MSG_ID")
    private Integer recErrorMsgId;
    @Basic(optional = false)
    @Column(name = "REC_ERROR_MSG_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recErrorMsgTime;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "FILE_PATH")
    private String filePath;
    @Basic(optional = false)
    @Column(name = "ECS_RES_CD_FILE_MSG_ECS_RES_CD_FILE_MSG_ID")
    private int ecsResCdFileMsgEcsResCdFileMsgId;
    @JoinColumn(name = "RES_CD_FILE_MSG_RES_CD_FILE_ID", referencedColumnName = "RES_CD_FILE_ID")
    @ManyToOne(optional = true)
    private ResCdFileMsg resCdFileMsgResCdFileId;
    @JoinColumn(name = "MESSAGE_TYPES_MESSAGE_TYPE_ID", referencedColumnName = "MESSAGE_TYPE_ID")
    @ManyToOne(optional = false)
    private MessageTypes messageTypesMessageTypeId;

    public RecErrorFileMsg() {
    }

    public RecErrorFileMsg(Integer recErrorMsgId) {
        this.recErrorMsgId = recErrorMsgId;
    }

    public RecErrorFileMsg(Integer recErrorMsgId, Date recErrorMsgTime, int ecsResCdFileMsgEcsResCdFileMsgId) {
        this.recErrorMsgId = recErrorMsgId;
        this.recErrorMsgTime = recErrorMsgTime;
        this.ecsResCdFileMsgEcsResCdFileMsgId = ecsResCdFileMsgEcsResCdFileMsgId;
    }

    public Integer getRecErrorMsgId() {
        return recErrorMsgId;
    }

    public void setRecErrorMsgId(Integer recErrorMsgId) {
        this.recErrorMsgId = recErrorMsgId;
    }

    public Date getRecErrorMsgTime() {
        return recErrorMsgTime;
    }

    public void setRecErrorMsgTime(Date recErrorMsgTime) {
        this.recErrorMsgTime = recErrorMsgTime;
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

    public int getEcsResCdFileMsgEcsResCdFileMsgId() {
        return ecsResCdFileMsgEcsResCdFileMsgId;
    }

    public void setEcsResCdFileMsgEcsResCdFileMsgId(int ecsResCdFileMsgEcsResCdFileMsgId) {
        this.ecsResCdFileMsgEcsResCdFileMsgId = ecsResCdFileMsgEcsResCdFileMsgId;
    }

    public ResCdFileMsg getResCdFileMsgResCdFileId() {
        return resCdFileMsgResCdFileId;
    }

    public void setResCdFileMsgResCdFileId(ResCdFileMsg resCdFileMsgResCdFileId) {
        this.resCdFileMsgResCdFileId = resCdFileMsgResCdFileId;
    }

    public MessageTypes getMessageTypesMessageTypeId() {
        return messageTypesMessageTypeId;
    }

    public void setMessageTypesMessageTypeId(MessageTypes messageTypesMessageTypeId) {
        this.messageTypesMessageTypeId = messageTypesMessageTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recErrorMsgId != null ? recErrorMsgId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecErrorFileMsg)) {
            return false;
        }
        RecErrorFileMsg other = (RecErrorFileMsg) object;
        if ((this.recErrorMsgId == null && other.recErrorMsgId != null) || (this.recErrorMsgId != null && !this.recErrorMsgId.equals(other.recErrorMsgId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.RecErrorFileMsg[ recErrorMsgId=" + recErrorMsgId + " ]";
    }
    
}
