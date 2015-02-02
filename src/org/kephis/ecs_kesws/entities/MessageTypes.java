/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kim
 */
@Entity
@Table(name = "MESSAGE_TYPES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MessageTypes.findAll", query = "SELECT m FROM MessageTypes m"),
    @NamedQuery(name = "MessageTypes.findByMessageTypeId", query = "SELECT m FROM MessageTypes m WHERE m.messageTypeId = :messageTypeId"),
    @NamedQuery(name = "MessageTypes.findByMessageName", query = "SELECT m FROM MessageTypes m WHERE m.messageName = :messageName"),
    @NamedQuery(name = "MessageTypes.findByMessageSource", query = "SELECT m FROM MessageTypes m WHERE m.messageSource = :messageSource"),
    @NamedQuery(name = "MessageTypes.findByMessageProcessReq", query = "SELECT m FROM MessageTypes m WHERE m.messageProcessReq = :messageProcessReq"),
    @NamedQuery(name = "MessageTypes.findByMessageDest", query = "SELECT m FROM MessageTypes m WHERE m.messageDest = :messageDest"),
    @NamedQuery(name = "MessageTypes.findByMessageTypesDesc", query = "SELECT m FROM MessageTypes m WHERE m.messageTypesDesc = :messageTypesDesc")})
public class MessageTypes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "MESSAGE_TYPE_ID")
    private Integer messageTypeId;
    @Column(name = "MESSAGE_NAME")
    private String messageName;
    @Column(name = "MESSAGE_SOURCE")
    private String messageSource;
    @Column(name = "MESSAGE_PROCESS_REQ")
    private String messageProcessReq;
    @Column(name = "MESSAGE_DEST")
    private String messageDest;
    @Column(name = "MESSAGE_TYPES_DESC")
    private String messageTypesDesc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "messageTypesMessageTypeId")
    private Collection<RecErrorFileMsg> recErrorFileMsgCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "messageTypesMessageTypeId")
    private Collection<ResCdFileMsg> resCdFileMsgCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "messageTypesMessageTypeId")
    private Collection<RecCdFileMsg> recCdFileMsgCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "messageTypesMessageTypeId")
    private Collection<EcsResCdFileMsg> ecsResCdFileMsgCollection;

    public MessageTypes() {
    }

    public MessageTypes(Integer messageTypeId) {
        this.messageTypeId = messageTypeId;
    }

    public Integer getMessageTypeId() {
        return messageTypeId;
    }

    public void setMessageTypeId(Integer messageTypeId) {
        this.messageTypeId = messageTypeId;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(String messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessageProcessReq() {
        return messageProcessReq;
    }

    public void setMessageProcessReq(String messageProcessReq) {
        this.messageProcessReq = messageProcessReq;
    }

    public String getMessageDest() {
        return messageDest;
    }

    public void setMessageDest(String messageDest) {
        this.messageDest = messageDest;
    }

    public String getMessageTypesDesc() {
        return messageTypesDesc;
    }

    public void setMessageTypesDesc(String messageTypesDesc) {
        this.messageTypesDesc = messageTypesDesc;
    }

    @XmlTransient
    public Collection<RecErrorFileMsg> getRecErrorFileMsgCollection() {
        return recErrorFileMsgCollection;
    }

    public void setRecErrorFileMsgCollection(Collection<RecErrorFileMsg> recErrorFileMsgCollection) {
        this.recErrorFileMsgCollection = recErrorFileMsgCollection;
    }

    @XmlTransient
    public Collection<ResCdFileMsg> getResCdFileMsgCollection() {
        return resCdFileMsgCollection;
    }

    public void setResCdFileMsgCollection(Collection<ResCdFileMsg> resCdFileMsgCollection) {
        this.resCdFileMsgCollection = resCdFileMsgCollection;
    }

    @XmlTransient
    public Collection<RecCdFileMsg> getRecCdFileMsgCollection() {
        return recCdFileMsgCollection;
    }

    public void setRecCdFileMsgCollection(Collection<RecCdFileMsg> recCdFileMsgCollection) {
        this.recCdFileMsgCollection = recCdFileMsgCollection;
    }

    @XmlTransient
    public Collection<EcsResCdFileMsg> getEcsResCdFileMsgCollection() {
        return ecsResCdFileMsgCollection;
    }

    public void setEcsResCdFileMsgCollection(Collection<EcsResCdFileMsg> ecsResCdFileMsgCollection) {
        this.ecsResCdFileMsgCollection = ecsResCdFileMsgCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (messageTypeId != null ? messageTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MessageTypes)) {
            return false;
        }
        MessageTypes other = (MessageTypes) object;
        if ((this.messageTypeId == null && other.messageTypeId != null) || (this.messageTypeId != null && !this.messageTypeId.equals(other.messageTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.MessageTypes[ messageTypeId=" + messageTypeId + " ]";
    }
    
}
