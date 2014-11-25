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
@Table(name = "listes_stats", catalog = "TruePrice", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListesStats.findAll", query = "SELECT l FROM ListesStats l"),
    @NamedQuery(name = "ListesStats.findByListesStatsId", query = "SELECT l FROM ListesStats l WHERE l.listesStatsId = :listesStatsId"),
    @NamedQuery(name = "ListesStats.findByListesStatsListe", query = "SELECT l FROM ListesStats l WHERE l.listesStatsListe = :listesStatsListe"),
    @NamedQuery(name = "ListesStats.findByListesStatsUser", query = "SELECT l FROM ListesStats l WHERE l.listesStatsUser = :listesStatsUser"),
    @NamedQuery(name = "ListesStats.findByListesStatsDownMinus3", query = "SELECT l FROM ListesStats l WHERE l.listesStatsDownMinus3 = :listesStatsDownMinus3"),
    @NamedQuery(name = "ListesStats.findByListesStatsDownMinus2", query = "SELECT l FROM ListesStats l WHERE l.listesStatsDownMinus2 = :listesStatsDownMinus2"),
    @NamedQuery(name = "ListesStats.findByListesStatsDownMinus1", query = "SELECT l FROM ListesStats l WHERE l.listesStatsDownMinus1 = :listesStatsDownMinus1"),
    @NamedQuery(name = "ListesStats.findByListesStatsDownCenter", query = "SELECT l FROM ListesStats l WHERE l.listesStatsDownCenter = :listesStatsDownCenter"),
    @NamedQuery(name = "ListesStats.findByListesStatsDownPlus1", query = "SELECT l FROM ListesStats l WHERE l.listesStatsDownPlus1 = :listesStatsDownPlus1"),
    @NamedQuery(name = "ListesStats.findByListesStatsDownPlus2", query = "SELECT l FROM ListesStats l WHERE l.listesStatsDownPlus2 = :listesStatsDownPlus2"),
    @NamedQuery(name = "ListesStats.findByListesStatsDownPlus3", query = "SELECT l FROM ListesStats l WHERE l.listesStatsDownPlus3 = :listesStatsDownPlus3"),
    @NamedQuery(name = "ListesStats.findByListesStatsUpPlus3", query = "SELECT l FROM ListesStats l WHERE l.listesStatsUpPlus3 = :listesStatsUpPlus3"),
    @NamedQuery(name = "ListesStats.findByListesStatsUpPlus2", query = "SELECT l FROM ListesStats l WHERE l.listesStatsUpPlus2 = :listesStatsUpPlus2"),
    @NamedQuery(name = "ListesStats.findByListesStatsUpPlus1", query = "SELECT l FROM ListesStats l WHERE l.listesStatsUpPlus1 = :listesStatsUpPlus1"),
    @NamedQuery(name = "ListesStats.findByListesStatsUpCenter", query = "SELECT l FROM ListesStats l WHERE l.listesStatsUpCenter = :listesStatsUpCenter"),
    @NamedQuery(name = "ListesStats.findByListesStatsUpMinus1", query = "SELECT l FROM ListesStats l WHERE l.listesStatsUpMinus1 = :listesStatsUpMinus1"),
    @NamedQuery(name = "ListesStats.findByListesStatsUpMinus2", query = "SELECT l FROM ListesStats l WHERE l.listesStatsUpMinus2 = :listesStatsUpMinus2"),
    @NamedQuery(name = "ListesStats.findByListesStatsUpMinus3", query = "SELECT l FROM ListesStats l WHERE l.listesStatsUpMinus3 = :listesStatsUpMinus3"),
    @NamedQuery(name = "ListesStats.findByListesStatsEqualsMinus3", query = "SELECT l FROM ListesStats l WHERE l.listesStatsEqualsMinus3 = :listesStatsEqualsMinus3"),
    @NamedQuery(name = "ListesStats.findByListesStatsEqualsMinus2", query = "SELECT l FROM ListesStats l WHERE l.listesStatsEqualsMinus2 = :listesStatsEqualsMinus2"),
    @NamedQuery(name = "ListesStats.findByListesStatsEqualsMinus1", query = "SELECT l FROM ListesStats l WHERE l.listesStatsEqualsMinus1 = :listesStatsEqualsMinus1"),
    @NamedQuery(name = "ListesStats.findByListesStatsEqualsCenter", query = "SELECT l FROM ListesStats l WHERE l.listesStatsEqualsCenter = :listesStatsEqualsCenter"),
    @NamedQuery(name = "ListesStats.findByListesStatsEqualsPlus1", query = "SELECT l FROM ListesStats l WHERE l.listesStatsEqualsPlus1 = :listesStatsEqualsPlus1"),
    @NamedQuery(name = "ListesStats.findByListesStatsEqualsPlus2", query = "SELECT l FROM ListesStats l WHERE l.listesStatsEqualsPlus2 = :listesStatsEqualsPlus2"),
    @NamedQuery(name = "ListesStats.findByListesStatsEqualsPlus3", query = "SELECT l FROM ListesStats l WHERE l.listesStatsEqualsPlus3 = :listesStatsEqualsPlus3"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcNbMinus3", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcNbMinus3 = :listesStatsRdcNbMinus3"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcNbMinus2", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcNbMinus2 = :listesStatsRdcNbMinus2"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcNbMinus1", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcNbMinus1 = :listesStatsRdcNbMinus1"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcNbCenter", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcNbCenter = :listesStatsRdcNbCenter"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcNbPlus1", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcNbPlus1 = :listesStatsRdcNbPlus1"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcNbPlus2", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcNbPlus2 = :listesStatsRdcNbPlus2"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcNbPlus3", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcNbPlus3 = :listesStatsRdcNbPlus3"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcValueMinus3", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcValueMinus3 = :listesStatsRdcValueMinus3"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcValueMinus2", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcValueMinus2 = :listesStatsRdcValueMinus2"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcValueMinus1", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcValueMinus1 = :listesStatsRdcValueMinus1"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcValueCenter", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcValueCenter = :listesStatsRdcValueCenter"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcValuePlus1", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcValuePlus1 = :listesStatsRdcValuePlus1"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcValuePlus2", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcValuePlus2 = :listesStatsRdcValuePlus2"),
    @NamedQuery(name = "ListesStats.findByListesStatsRdcValuePlus3", query = "SELECT l FROM ListesStats l WHERE l.listesStatsRdcValuePlus3 = :listesStatsRdcValuePlus3")})
public class ListeStats implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "listes_stats_id")
    private Integer listesStatsId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "listes_stats_liste")
    private long listesStatsListe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "listes_stats_user")
    private long listesStatsUser;
    @Column(name = "listes_stats_down_minus_3")
    private Short listesStatsDownMinus3;
    @Column(name = "listes_stats_down_minus_2")
    private Short listesStatsDownMinus2;
    @Column(name = "listes_stats_down_minus_1")
    private Short listesStatsDownMinus1;
    @Column(name = "listes_stats_down_center")
    private Short listesStatsDownCenter;
    @Column(name = "listes_stats_down_plus_1")
    private Short listesStatsDownPlus1;
    @Column(name = "listes_stats_down_plus_2")
    private Short listesStatsDownPlus2;
    @Column(name = "listes_stats_down_plus_3")
    private Short listesStatsDownPlus3;
    @Column(name = "listes_stats_up_plus_3")
    private Short listesStatsUpPlus3;
    @Column(name = "listes_stats_up_plus_2")
    private Short listesStatsUpPlus2;
    @Column(name = "listes_stats_up_plus_1")
    private Short listesStatsUpPlus1;
    @Column(name = "listes_stats_up_center")
    private Short listesStatsUpCenter;
    @Column(name = "listes_stats_up_minus_1")
    private Short listesStatsUpMinus1;
    @Column(name = "listes_stats_up_minus_2")
    private Short listesStatsUpMinus2;
    @Column(name = "listes_stats_up_minus_3")
    private Short listesStatsUpMinus3;
    @Column(name = "listes_stats_equals_minus_3")
    private Short listesStatsEqualsMinus3;
    @Column(name = "listes_stats_equals_minus_2")
    private Short listesStatsEqualsMinus2;
    @Column(name = "listes_stats_equals_minus_1")
    private Short listesStatsEqualsMinus1;
    @Column(name = "listes_stats_equals_center")
    private Short listesStatsEqualsCenter;
    @Column(name = "listes_stats_equals_plus_1")
    private Short listesStatsEqualsPlus1;
    @Column(name = "listes_stats_equals_plus_2")
    private Short listesStatsEqualsPlus2;
    @Column(name = "listes_stats_equals_plus_3")
    private Short listesStatsEqualsPlus3;
    @Column(name = "listes_stats_rdc_nb_minus_3")
    private Short listesStatsRdcNbMinus3;
    @Column(name = "listes_stats_rdc_nb_minus_2")
    private Short listesStatsRdcNbMinus2;
    @Column(name = "listes_stats_rdc_nb_minus_1")
    private Short listesStatsRdcNbMinus1;
    @Column(name = "listes_stats_rdc_nb_center")
    private Short listesStatsRdcNbCenter;
    @Column(name = "listes_stats_rdc_nb_plus_1")
    private Short listesStatsRdcNbPlus1;
    @Column(name = "listes_stats_rdc_nb_plus_2")
    private Short listesStatsRdcNbPlus2;
    @Column(name = "listes_stats_rdc_nb_plus_3")
    private Short listesStatsRdcNbPlus3;
    @Column(name = "listes_stats_rdc_value_minus_3")
    private Short listesStatsRdcValueMinus3;
    @Column(name = "listes_stats_rdc_value_minus_2")
    private Short listesStatsRdcValueMinus2;
    @Column(name = "listes_stats_rdc_value_minus_1")
    private Short listesStatsRdcValueMinus1;
    @Column(name = "listes_stats_rdc_value_center")
    private Short listesStatsRdcValueCenter;
    @Column(name = "listes_stats_rdc_value_plus_1")
    private Short listesStatsRdcValuePlus1;
    @Column(name = "listes_stats_rdc_value_plus_2")
    private Short listesStatsRdcValuePlus2;
    @Column(name = "listes_stats_rdc_value_plus_3")
    private Short listesStatsRdcValuePlus3;

    public ListeStats() {
    }

    public ListeStats(Integer listesStatsId) {
        this.listesStatsId = listesStatsId;
    }

    public ListeStats(Integer listesStatsId, long listesStatsListe, long listesStatsUser) {
        this.listesStatsId = listesStatsId;
        this.listesStatsListe = listesStatsListe;
        this.listesStatsUser = listesStatsUser;
    }

    public Integer getListesStatsId() {
        return listesStatsId;
    }

    public void setListesStatsId(Integer listesStatsId) {
        this.listesStatsId = listesStatsId;
    }

    public long getListesStatsListe() {
        return listesStatsListe;
    }

    public void setListesStatsListe(long listesStatsListe) {
        this.listesStatsListe = listesStatsListe;
    }

    public long getListesStatsUser() {
        return listesStatsUser;
    }

    public void setListesStatsUser(long listesStatsUser) {
        this.listesStatsUser = listesStatsUser;
    }

    public Short getListesStatsDownMinus3() {
        return listesStatsDownMinus3;
    }

    public void setListesStatsDownMinus3(Short listesStatsDownMinus3) {
        this.listesStatsDownMinus3 = listesStatsDownMinus3;
    }

    public Short getListesStatsDownMinus2() {
        return listesStatsDownMinus2;
    }

    public void setListesStatsDownMinus2(Short listesStatsDownMinus2) {
        this.listesStatsDownMinus2 = listesStatsDownMinus2;
    }

    public Short getListesStatsDownMinus1() {
        return listesStatsDownMinus1;
    }

    public void setListesStatsDownMinus1(Short listesStatsDownMinus1) {
        this.listesStatsDownMinus1 = listesStatsDownMinus1;
    }

    public Short getListesStatsDownCenter() {
        return listesStatsDownCenter;
    }

    public void setListesStatsDownCenter(Short listesStatsDownCenter) {
        this.listesStatsDownCenter = listesStatsDownCenter;
    }

    public Short getListesStatsDownPlus1() {
        return listesStatsDownPlus1;
    }

    public void setListesStatsDownPlus1(Short listesStatsDownPlus1) {
        this.listesStatsDownPlus1 = listesStatsDownPlus1;
    }

    public Short getListesStatsDownPlus2() {
        return listesStatsDownPlus2;
    }

    public void setListesStatsDownPlus2(Short listesStatsDownPlus2) {
        this.listesStatsDownPlus2 = listesStatsDownPlus2;
    }

    public Short getListesStatsDownPlus3() {
        return listesStatsDownPlus3;
    }

    public void setListesStatsDownPlus3(Short listesStatsDownPlus3) {
        this.listesStatsDownPlus3 = listesStatsDownPlus3;
    }

    public Short getListesStatsUpPlus3() {
        return listesStatsUpPlus3;
    }

    public void setListesStatsUpPlus3(Short listesStatsUpPlus3) {
        this.listesStatsUpPlus3 = listesStatsUpPlus3;
    }

    public Short getListesStatsUpPlus2() {
        return listesStatsUpPlus2;
    }

    public void setListesStatsUpPlus2(Short listesStatsUpPlus2) {
        this.listesStatsUpPlus2 = listesStatsUpPlus2;
    }

    public Short getListesStatsUpPlus1() {
        return listesStatsUpPlus1;
    }

    public void setListesStatsUpPlus1(Short listesStatsUpPlus1) {
        this.listesStatsUpPlus1 = listesStatsUpPlus1;
    }

    public Short getListesStatsUpCenter() {
        return listesStatsUpCenter;
    }

    public void setListesStatsUpCenter(Short listesStatsUpCenter) {
        this.listesStatsUpCenter = listesStatsUpCenter;
    }

    public Short getListesStatsUpMinus1() {
        return listesStatsUpMinus1;
    }

    public void setListesStatsUpMinus1(Short listesStatsUpMinus1) {
        this.listesStatsUpMinus1 = listesStatsUpMinus1;
    }

    public Short getListesStatsUpMinus2() {
        return listesStatsUpMinus2;
    }

    public void setListesStatsUpMinus2(Short listesStatsUpMinus2) {
        this.listesStatsUpMinus2 = listesStatsUpMinus2;
    }

    public Short getListesStatsUpMinus3() {
        return listesStatsUpMinus3;
    }

    public void setListesStatsUpMinus3(Short listesStatsUpMinus3) {
        this.listesStatsUpMinus3 = listesStatsUpMinus3;
    }

    public Short getListesStatsEqualsMinus3() {
        return listesStatsEqualsMinus3;
    }

    public void setListesStatsEqualsMinus3(Short listesStatsEqualsMinus3) {
        this.listesStatsEqualsMinus3 = listesStatsEqualsMinus3;
    }

    public Short getListesStatsEqualsMinus2() {
        return listesStatsEqualsMinus2;
    }

    public void setListesStatsEqualsMinus2(Short listesStatsEqualsMinus2) {
        this.listesStatsEqualsMinus2 = listesStatsEqualsMinus2;
    }

    public Short getListesStatsEqualsMinus1() {
        return listesStatsEqualsMinus1;
    }

    public void setListesStatsEqualsMinus1(Short listesStatsEqualsMinus1) {
        this.listesStatsEqualsMinus1 = listesStatsEqualsMinus1;
    }

    public Short getListesStatsEqualsCenter() {
        return listesStatsEqualsCenter;
    }

    public void setListesStatsEqualsCenter(Short listesStatsEqualsCenter) {
        this.listesStatsEqualsCenter = listesStatsEqualsCenter;
    }

    public Short getListesStatsEqualsPlus1() {
        return listesStatsEqualsPlus1;
    }

    public void setListesStatsEqualsPlus1(Short listesStatsEqualsPlus1) {
        this.listesStatsEqualsPlus1 = listesStatsEqualsPlus1;
    }

    public Short getListesStatsEqualsPlus2() {
        return listesStatsEqualsPlus2;
    }

    public void setListesStatsEqualsPlus2(Short listesStatsEqualsPlus2) {
        this.listesStatsEqualsPlus2 = listesStatsEqualsPlus2;
    }

    public Short getListesStatsEqualsPlus3() {
        return listesStatsEqualsPlus3;
    }

    public void setListesStatsEqualsPlus3(Short listesStatsEqualsPlus3) {
        this.listesStatsEqualsPlus3 = listesStatsEqualsPlus3;
    }

    public Short getListesStatsRdcNbMinus3() {
        return listesStatsRdcNbMinus3;
    }

    public void setListesStatsRdcNbMinus3(Short listesStatsRdcNbMinus3) {
        this.listesStatsRdcNbMinus3 = listesStatsRdcNbMinus3;
    }

    public Short getListesStatsRdcNbMinus2() {
        return listesStatsRdcNbMinus2;
    }

    public void setListesStatsRdcNbMinus2(Short listesStatsRdcNbMinus2) {
        this.listesStatsRdcNbMinus2 = listesStatsRdcNbMinus2;
    }

    public Short getListesStatsRdcNbMinus1() {
        return listesStatsRdcNbMinus1;
    }

    public void setListesStatsRdcNbMinus1(Short listesStatsRdcNbMinus1) {
        this.listesStatsRdcNbMinus1 = listesStatsRdcNbMinus1;
    }

    public Short getListesStatsRdcNbCenter() {
        return listesStatsRdcNbCenter;
    }

    public void setListesStatsRdcNbCenter(Short listesStatsRdcNbCenter) {
        this.listesStatsRdcNbCenter = listesStatsRdcNbCenter;
    }

    public Short getListesStatsRdcNbPlus1() {
        return listesStatsRdcNbPlus1;
    }

    public void setListesStatsRdcNbPlus1(Short listesStatsRdcNbPlus1) {
        this.listesStatsRdcNbPlus1 = listesStatsRdcNbPlus1;
    }

    public Short getListesStatsRdcNbPlus2() {
        return listesStatsRdcNbPlus2;
    }

    public void setListesStatsRdcNbPlus2(Short listesStatsRdcNbPlus2) {
        this.listesStatsRdcNbPlus2 = listesStatsRdcNbPlus2;
    }

    public Short getListesStatsRdcNbPlus3() {
        return listesStatsRdcNbPlus3;
    }

    public void setListesStatsRdcNbPlus3(Short listesStatsRdcNbPlus3) {
        this.listesStatsRdcNbPlus3 = listesStatsRdcNbPlus3;
    }

    public Short getListesStatsRdcValueMinus3() {
        return listesStatsRdcValueMinus3;
    }

    public void setListesStatsRdcValueMinus3(Short listesStatsRdcValueMinus3) {
        this.listesStatsRdcValueMinus3 = listesStatsRdcValueMinus3;
    }

    public Short getListesStatsRdcValueMinus2() {
        return listesStatsRdcValueMinus2;
    }

    public void setListesStatsRdcValueMinus2(Short listesStatsRdcValueMinus2) {
        this.listesStatsRdcValueMinus2 = listesStatsRdcValueMinus2;
    }

    public Short getListesStatsRdcValueMinus1() {
        return listesStatsRdcValueMinus1;
    }

    public void setListesStatsRdcValueMinus1(Short listesStatsRdcValueMinus1) {
        this.listesStatsRdcValueMinus1 = listesStatsRdcValueMinus1;
    }

    public Short getListesStatsRdcValueCenter() {
        return listesStatsRdcValueCenter;
    }

    public void setListesStatsRdcValueCenter(Short listesStatsRdcValueCenter) {
        this.listesStatsRdcValueCenter = listesStatsRdcValueCenter;
    }

    public Short getListesStatsRdcValuePlus1() {
        return listesStatsRdcValuePlus1;
    }

    public void setListesStatsRdcValuePlus1(Short listesStatsRdcValuePlus1) {
        this.listesStatsRdcValuePlus1 = listesStatsRdcValuePlus1;
    }

    public Short getListesStatsRdcValuePlus2() {
        return listesStatsRdcValuePlus2;
    }

    public void setListesStatsRdcValuePlus2(Short listesStatsRdcValuePlus2) {
        this.listesStatsRdcValuePlus2 = listesStatsRdcValuePlus2;
    }

    public Short getListesStatsRdcValuePlus3() {
        return listesStatsRdcValuePlus3;
    }

    public void setListesStatsRdcValuePlus3(Short listesStatsRdcValuePlus3) {
        this.listesStatsRdcValuePlus3 = listesStatsRdcValuePlus3;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (listesStatsId != null ? listesStatsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ListeStats)) {
            return false;
        }
        ListeStats other = (ListeStats) object;
        if ((this.listesStatsId == null && other.listesStatsId != null) || (this.listesStatsId != null && !this.listesStatsId.equals(other.listesStatsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.ListesStats[ listesStatsId=" + listesStatsId + " ]";
    }
    
}
