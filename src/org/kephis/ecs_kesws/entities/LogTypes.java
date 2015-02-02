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
@Table(name = "LOG_TYPES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogTypes.findAll", query = "SELECT l FROM LogTypes l"),
    @NamedQuery(name = "LogTypes.findByLogIdLevel", query = "SELECT l FROM LogTypes l WHERE l.logIdLevel = :logIdLevel"),
    @NamedQuery(name = "LogTypes.findByLogLevel", query = "SELECT l FROM LogTypes l WHERE l.logLevel = :logLevel")})
public class LogTypes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "LOG_ID_LEVEL")
    private Integer logIdLevel;
    @Column(name = "LOG_LEVEL")
    private String logLevel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "logTypesLogIdLevel")
    private Collection<TransactionLogs> transactionLogsCollection;

    public LogTypes() {
    }

    public LogTypes(Integer logIdLevel) {
        this.logIdLevel = logIdLevel;
    }

    public Integer getLogIdLevel() {
        return logIdLevel;
    }

    public void setLogIdLevel(Integer logIdLevel) {
        this.logIdLevel = logIdLevel;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
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
        hash += (logIdLevel != null ? logIdLevel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LogTypes)) {
            return false;
        }
        LogTypes other = (LogTypes) object;
        if ((this.logIdLevel == null && other.logIdLevel != null) || (this.logIdLevel != null && !this.logIdLevel.equals(other.logIdLevel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.kephis.ecs_kesws.entities.LogTypes[ logIdLevel=" + logIdLevel + " ]";
    }
    
}
