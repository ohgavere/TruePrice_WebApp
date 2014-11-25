/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.entities;

import org.dmb.trueprice.entities.*;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Guiitch
 */
@Entity
@Table(name = "qtt_detail", catalog = "TruePrice", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QttDetail.findAll", query = "SELECT q FROM QttDetail q"),
    @NamedQuery(name = "QttDetail.findByQttId", query = "SELECT q FROM QttDetail q WHERE q.qttId = :qttId"),
    @NamedQuery(name = "QttDetail.findByQttMesure", query = "SELECT q FROM QttDetail q WHERE q.qttMesure = :qttMesure"),
    @NamedQuery(name = "QttDetail.findByQttQuantite", query = "SELECT q FROM QttDetail q WHERE q.qttQuantite = :qttQuantite")})
public class QttDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "qtt_id")
    private Integer qttId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "qtt_mesure")
    private String qttMesure;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "qtt_quantite")
    private String qttQuantite;

    public QttDetail() {
    }

    public QttDetail(Integer qttId) {
        this.qttId = qttId;
    }

    public QttDetail(Integer qttId, String qttMesure, String qttQuantite) {
        this.qttId = qttId;
        this.qttMesure = qttMesure;
        this.qttQuantite = qttQuantite;
    }

    public Integer getQttId() {
        return qttId;
    }

    public void setQttId(Integer qttId) {
        this.qttId = qttId;
    }

    public String getQttMesure() {
        return qttMesure;
    }

    public void setQttMesure(String qttMesure) {
        this.qttMesure = qttMesure;
    }

    public String getQttQuantite() {
        return qttQuantite;
    }

    public void setQttQuantite(String qttQuantite) {
        this.qttQuantite = qttQuantite;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (qttId != null ? qttId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QttDetail)) {
            return false;
        }
        QttDetail other = (QttDetail) object;
        if ((this.qttId == null && other.qttId != null) || (this.qttId != null && !this.qttId.equals(other.qttId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.QttDetail[ qttId=" + qttId + " ]";
    }
    
}
