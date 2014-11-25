/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.entities;

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
@Table(catalog = "TruePrice", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Liste.findAll", query = "SELECT l FROM Liste l"),
    @NamedQuery(name = "Liste.findByListesId", query = "SELECT l FROM Liste l WHERE l.lstId = :lstId"),
    @NamedQuery(name = "Liste.findByListesUser", query = "SELECT l FROM Liste l WHERE l.lstUser = :lstUser"),
    @NamedQuery(name = "Liste.findByListesLabel", query = "SELECT l FROM Liste l WHERE l.lstLabel = :lstLabel"),
    @NamedQuery(name = "Liste.findByListesDescription", query = "SELECT l FROM Liste l WHERE l.lstDescription = :lstDescription"),
    @NamedQuery(name = "Liste.findByListesEnseigne", query = "SELECT l FROM Liste l WHERE l.lstEnseigne = :lstEnseigne"),
    @NamedQuery(name = "Liste.findByListesProduits", query = "SELECT l FROM Liste l WHERE l.lstProduits = :lstProduits")})
public class Liste implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "listes_id")
    private Integer lstId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "listes_user")
    private long lstUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "listes_label")
    private String lstLabel;
    @Size(max = 2147483647)
    @Column(name = "listes_description")
    private String lstDescription;
    @Column(name = "listes_enseigne")
    private long lstEnseigne;
    @Size(max = 2147483647)
    @Column(name = "listes_produits")
    private String lstProduits;

    public Liste() {
    }

    public Liste(Integer listesId) {
        this.lstId = listesId;
    }

    public Liste(Integer listesId, long listesUser, String listesLabel) {
        this.lstId = listesId;
        this.lstUser = listesUser;
        this.lstLabel = listesLabel;
    }

    public Integer getLstId() {
        return lstId;
    }

    public void setLstId(Integer lstId) {
        this.lstId = lstId;
    }

    public long getLstUser() {
        return lstUser;
    }

    public void setLstUser(long lstUser) {
        this.lstUser = lstUser;
    }

    public String getLstLabel() {
        return lstLabel;
    }

    public void setLstLabel(String lstLabel) {
        this.lstLabel = lstLabel;
    }

    public String getLstDescription() {
        return lstDescription;
    }

    public void setLstDescription(String lstDescription) {
        this.lstDescription = lstDescription;
    }

    public long getLstEnseigne() {
        return lstEnseigne;
    }

    public void setLstEnseigne(long lstEnseigne) {
        this.lstEnseigne = lstEnseigne;
    }

    public String getLstProduits() {
        return lstProduits;
    }

    public void setLstProduits(String lstProduits) {
        this.lstProduits = lstProduits;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lstId != null ? lstId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Liste)) {
            return false;
        }
        Liste other = (Liste) object;
        if ((this.lstId == null && other.lstId != null) || (this.lstId != null && !this.lstId.equals(other.lstId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.Listes[ listesId=" + lstId + " ]";
    }
    
}
