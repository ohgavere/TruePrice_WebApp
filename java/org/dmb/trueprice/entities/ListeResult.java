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
@Table(name = "listes_result", catalog = "TruePrice", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListesResult.findAll", query = "SELECT l FROM ListesResult l"),
    @NamedQuery(name = "ListesResult.findByListesResultRdcValue", query = "SELECT l FROM ListesResult l WHERE l.listesResultRdcValue = :listesResultRdcValue"),
    @NamedQuery(name = "ListesResult.findByListesResultRdcValueLast", query = "SELECT l FROM ListesResult l WHERE l.listesResultRdcValueLast = :listesResultRdcValueLast"),
    @NamedQuery(name = "ListesResult.findByListesResultRdcValueBefore", query = "SELECT l FROM ListesResult l WHERE l.listesResultRdcValueBefore = :listesResultRdcValueBefore"),
    @NamedQuery(name = "ListesResult.findByListesResultRdcNb", query = "SELECT l FROM ListesResult l WHERE l.listesResultRdcNb = :listesResultRdcNb"),
    @NamedQuery(name = "ListesResult.findByListesResultRdcNbLast", query = "SELECT l FROM ListesResult l WHERE l.listesResultRdcNbLast = :listesResultRdcNbLast"),
    @NamedQuery(name = "ListesResult.findByListesResultRdcNbBefore", query = "SELECT l FROM ListesResult l WHERE l.listesResultRdcNbBefore = :listesResultRdcNbBefore"),
    @NamedQuery(name = "ListesResult.findByListesResultDown", query = "SELECT l FROM ListesResult l WHERE l.listesResultDown = :listesResultDown"),
    @NamedQuery(name = "ListesResult.findByListesResultDownLast", query = "SELECT l FROM ListesResult l WHERE l.listesResultDownLast = :listesResultDownLast"),
    @NamedQuery(name = "ListesResult.findByListesResultDownBefore", query = "SELECT l FROM ListesResult l WHERE l.listesResultDownBefore = :listesResultDownBefore"),
    @NamedQuery(name = "ListesResult.findByListesResultUp", query = "SELECT l FROM ListesResult l WHERE l.listesResultUp = :listesResultUp"),
    @NamedQuery(name = "ListesResult.findByListesResultUpLast", query = "SELECT l FROM ListesResult l WHERE l.listesResultUpLast = :listesResultUpLast"),
    @NamedQuery(name = "ListesResult.findByListesResultUpBefore", query = "SELECT l FROM ListesResult l WHERE l.listesResultUpBefore = :listesResultUpBefore"),
    @NamedQuery(name = "ListesResult.findByListesResultEquals", query = "SELECT l FROM ListesResult l WHERE l.listesResultEquals = :listesResultEquals"),
    @NamedQuery(name = "ListesResult.findByListesResultEqualsLast", query = "SELECT l FROM ListesResult l WHERE l.listesResultEqualsLast = :listesResultEqualsLast"),
    @NamedQuery(name = "ListesResult.findByListesResultEqualsBefore", query = "SELECT l FROM ListesResult l WHERE l.listesResultEqualsBefore = :listesResultEqualsBefore"),
    @NamedQuery(name = "ListesResult.findByListesResultId", query = "SELECT l FROM ListesResult l WHERE l.listesResultId = :listesResultId"),
    @NamedQuery(name = "ListesResult.findByListesResultListe", query = "SELECT l FROM ListesResult l WHERE l.listesResultListe = :listesResultListe")})
public class ListeResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "listes_result_rdc_value_")
    private Short listesResultRdcValue;
    @Column(name = "listes_result_rdc_value_last")
    private Short listesResultRdcValueLast;
    @Column(name = "listes_result_rdc_value_before")
    private Short listesResultRdcValueBefore;
    @Column(name = "listes_result_rdc_nb")
    private Short listesResultRdcNb;
    @Column(name = "listes_result_rdc_nb_last")
    private Short listesResultRdcNbLast;
    @Column(name = "listes_result_rdc_nb_before")
    private Short listesResultRdcNbBefore;
    @Column(name = "listes_result_down")
    private Short listesResultDown;
    @Column(name = "listes_result_down_last")
    private Short listesResultDownLast;
    @Column(name = "listes_result_down_before")
    private Short listesResultDownBefore;
    @Column(name = "listes_result_up")
    private Short listesResultUp;
    @Column(name = "listes_result_up_last")
    private Short listesResultUpLast;
    @Column(name = "listes_result_up_before")
    private Short listesResultUpBefore;
    @Column(name = "listes_result_equals")
    private Short listesResultEquals;
    @Column(name = "listes_result_equals_last")
    private Short listesResultEqualsLast;
    @Column(name = "listes_result_equals_before")
    private Short listesResultEqualsBefore;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "listes_result_id")
    private Long listesResultId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "listes_result_liste")
    private long listesResultListe;

    public ListeResult() {
    }

    public ListeResult(Long listesResultId) {
        this.listesResultId = listesResultId;
    }

    public ListeResult(Long listesResultId, long listesResultListe) {
        this.listesResultId = listesResultId;
        this.listesResultListe = listesResultListe;
    }

    public Short getListesResultRdcValue() {
        return listesResultRdcValue;
    }

    public void setListesResultRdcValue(Short listesResultRdcValue) {
        this.listesResultRdcValue = listesResultRdcValue;
    }

    public Short getListesResultRdcValueLast() {
        return listesResultRdcValueLast;
    }

    public void setListesResultRdcValueLast(Short listesResultRdcValueLast) {
        this.listesResultRdcValueLast = listesResultRdcValueLast;
    }

    public Short getListesResultRdcValueBefore() {
        return listesResultRdcValueBefore;
    }

    public void setListesResultRdcValueBefore(Short listesResultRdcValueBefore) {
        this.listesResultRdcValueBefore = listesResultRdcValueBefore;
    }

    public Short getListesResultRdcNb() {
        return listesResultRdcNb;
    }

    public void setListesResultRdcNb(Short listesResultRdcNb) {
        this.listesResultRdcNb = listesResultRdcNb;
    }

    public Short getListesResultRdcNbLast() {
        return listesResultRdcNbLast;
    }

    public void setListesResultRdcNbLast(Short listesResultRdcNbLast) {
        this.listesResultRdcNbLast = listesResultRdcNbLast;
    }

    public Short getListesResultRdcNbBefore() {
        return listesResultRdcNbBefore;
    }

    public void setListesResultRdcNbBefore(Short listesResultRdcNbBefore) {
        this.listesResultRdcNbBefore = listesResultRdcNbBefore;
    }

    public Short getListesResultDown() {
        return listesResultDown;
    }

    public void setListesResultDown(Short listesResultDown) {
        this.listesResultDown = listesResultDown;
    }

    public Short getListesResultDownLast() {
        return listesResultDownLast;
    }

    public void setListesResultDownLast(Short listesResultDownLast) {
        this.listesResultDownLast = listesResultDownLast;
    }

    public Short getListesResultDownBefore() {
        return listesResultDownBefore;
    }

    public void setListesResultDownBefore(Short listesResultDownBefore) {
        this.listesResultDownBefore = listesResultDownBefore;
    }

    public Short getListesResultUp() {
        return listesResultUp;
    }

    public void setListesResultUp(Short listesResultUp) {
        this.listesResultUp = listesResultUp;
    }

    public Short getListesResultUpLast() {
        return listesResultUpLast;
    }

    public void setListesResultUpLast(Short listesResultUpLast) {
        this.listesResultUpLast = listesResultUpLast;
    }

    public Short getListesResultUpBefore() {
        return listesResultUpBefore;
    }

    public void setListesResultUpBefore(Short listesResultUpBefore) {
        this.listesResultUpBefore = listesResultUpBefore;
    }

    public Short getListesResultEquals() {
        return listesResultEquals;
    }

    public void setListesResultEquals(Short listesResultEquals) {
        this.listesResultEquals = listesResultEquals;
    }

    public Short getListesResultEqualsLast() {
        return listesResultEqualsLast;
    }

    public void setListesResultEqualsLast(Short listesResultEqualsLast) {
        this.listesResultEqualsLast = listesResultEqualsLast;
    }

    public Short getListesResultEqualsBefore() {
        return listesResultEqualsBefore;
    }

    public void setListesResultEqualsBefore(Short listesResultEqualsBefore) {
        this.listesResultEqualsBefore = listesResultEqualsBefore;
    }

    public Long getListesResultId() {
        return listesResultId;
    }

    public void setListesResultId(Long listesResultId) {
        this.listesResultId = listesResultId;
    }

    public long getListesResultListe() {
        return listesResultListe;
    }

    public void setListesResultListe(long listesResultListe) {
        this.listesResultListe = listesResultListe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (listesResultId != null ? listesResultId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ListeResult)) {
            return false;
        }
        ListeResult other = (ListeResult) object;
        if ((this.listesResultId == null && other.listesResultId != null) || (this.listesResultId != null && !this.listesResultId.equals(other.listesResultId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.ListesResult[ listesResultId=" + listesResultId + " ]";
    }
    
}
