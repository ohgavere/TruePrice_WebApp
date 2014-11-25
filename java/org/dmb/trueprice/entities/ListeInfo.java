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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Guiitch
 */
@Entity
@Table(name = "listes_info", catalog = "TruePrice", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListesInfo.findAll", query = "SELECT l FROM ListesInfo l"),
    @NamedQuery(name = "ListesInfo.findByListesInfoId", query = "SELECT l FROM ListesInfo l WHERE l.listesInfoId = :listesInfoId"),
    @NamedQuery(name = "ListesInfo.findByListesInfoListe", query = "SELECT l FROM ListesInfo l WHERE l.listesInfoListe = :listesInfoListe"),
    @NamedQuery(name = "ListesInfo.findByListesInfoUser", query = "SELECT l FROM ListesInfo l WHERE l.listesInfoUser = :listesInfoUser"),
    @NamedQuery(name = "ListesInfo.findByListesInfoLastDown", query = "SELECT l FROM ListesInfo l WHERE l.listesInfoLastDown = :listesInfoLastDown"),
    @NamedQuery(name = "ListesInfo.findByListesInfoLastUp", query = "SELECT l FROM ListesInfo l WHERE l.listesInfoLastUp = :listesInfoLastUp"),
    @NamedQuery(name = "ListesInfo.findByListesInfoLastEquals", query = "SELECT l FROM ListesInfo l WHERE l.listesInfoLastEquals = :listesInfoLastEquals"),
    @NamedQuery(name = "ListesInfo.findByListesInfoLastRdcNb", query = "SELECT l FROM ListesInfo l WHERE l.listesInfoLastRdcNb = :listesInfoLastRdcNb"),
    @NamedQuery(name = "ListesInfo.findByListesInfoLastRdcValue", query = "SELECT l FROM ListesInfo l WHERE l.listesInfoLastRdcValue = :listesInfoLastRdcValue")})
public class ListeInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "listes_info_id")
    private Integer listesInfoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "listes_info_liste")
    private long listesInfoListe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "listes_info_user")
    private long listesInfoUser;
    @Column(name = "listes_info_last_down")
    private Short listesInfoLastDown;
    @Column(name = "listes_info_last_up")
    private Short listesInfoLastUp;
    @Column(name = "listes_info_last_equals")
    private Short listesInfoLastEquals;
    @Column(name = "listes_info_last_rdc_nb")
    private Short listesInfoLastRdcNb;
    @Column(name = "listes_info_last_rdc_value")
    private Short listesInfoLastRdcValue;

    public ListeInfo() {
    }

    public ListeInfo(Integer listesInfoId) {
        this.listesInfoId = listesInfoId;
    }

    public ListeInfo(Integer listesInfoId, long listesInfoListe, long listesInfoUser) {
        this.listesInfoId = listesInfoId;
        this.listesInfoListe = listesInfoListe;
        this.listesInfoUser = listesInfoUser;
    }

    public Integer getListesInfoId() {
        return listesInfoId;
    }

    public void setListesInfoId(Integer listesInfoId) {
        this.listesInfoId = listesInfoId;
    }

    public long getListesInfoListe() {
        return listesInfoListe;
    }

    public void setListesInfoListe(long listesInfoListe) {
        this.listesInfoListe = listesInfoListe;
    }

    public long getListesInfoUser() {
        return listesInfoUser;
    }

    public void setListesInfoUser(long listesInfoUser) {
        this.listesInfoUser = listesInfoUser;
    }

    public Short getListesInfoLastDown() {
        return listesInfoLastDown;
    }

    public void setListesInfoLastDown(Short listesInfoLastDown) {
        this.listesInfoLastDown = listesInfoLastDown;
    }

    public Short getListesInfoLastUp() {
        return listesInfoLastUp;
    }

    public void setListesInfoLastUp(Short listesInfoLastUp) {
        this.listesInfoLastUp = listesInfoLastUp;
    }

    public Short getListesInfoLastEquals() {
        return listesInfoLastEquals;
    }

    public void setListesInfoLastEquals(Short listesInfoLastEquals) {
        this.listesInfoLastEquals = listesInfoLastEquals;
    }

    public Short getListesInfoLastRdcNb() {
        return listesInfoLastRdcNb;
    }

    public void setListesInfoLastRdcNb(Short listesInfoLastRdcNb) {
        this.listesInfoLastRdcNb = listesInfoLastRdcNb;
    }

    public Short getListesInfoLastRdcValue() {
        return listesInfoLastRdcValue;
    }

    public void setListesInfoLastRdcValue(Short listesInfoLastRdcValue) {
        this.listesInfoLastRdcValue = listesInfoLastRdcValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (listesInfoId != null ? listesInfoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ListeInfo)) {
            return false;
        }
        ListeInfo other = (ListeInfo) object;
        if ((this.listesInfoId == null && other.listesInfoId != null) || (this.listesInfoId != null && !this.listesInfoId.equals(other.listesInfoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.ListesInfo[ listesInfoId=" + listesInfoId + " ]";
    }
    
}
