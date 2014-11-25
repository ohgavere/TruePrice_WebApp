/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.entities;

import org.dmb.trueprice.entities.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Guiitch
 */
@Entity
@Table(name = "membre")//, catalog = "TruePrice", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Membre.findAll", query = "SELECT m FROM Membre m"),
    @NamedQuery(name = "Membre.findByMbMail", query = "SELECT m FROM Membre m WHERE m.mbMail = :mbMail"),
    @NamedQuery(name = "Membre.findByMbPseudo", query = "SELECT m FROM Membre m WHERE m.mbPseudo = :mbPseudo"),
    @NamedQuery(name = "Membre.findByMbId", query = "SELECT m FROM Membre m WHERE m.mbId = :mbId"),
    @NamedQuery(name = "Membre.findByMbDtcreation", query = "SELECT m FROM Membre m WHERE m.mbDtcreation = :mbDtcreation"),
    @NamedQuery(name = "Membre.findByMbPassid", query = "SELECT m FROM Membre m WHERE m.mbPassid = :mbPassid"),
    @NamedQuery(name = "Membre.findByMbNom", query = "SELECT m FROM Membre m WHERE m.mbNom = :mbNom"),
    @NamedQuery(name = "Membre.findByMbPrenom", query = "SELECT m FROM Membre m WHERE m.mbPrenom = :mbPrenom")
})
public class Membre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Size(max = 2147483647)
    @Column(name = "mb_mail")
    private String mbMail;
    @Size(max = 2147483647)
    @Column(name = "mb_pseudo")
    private String mbPseudo;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mb_id")
    private Long mbId;
    @Column(name = "mb_dtcreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mbDtcreation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mb_passid")
    private long mbPassid;
    @Size(max = 2147483647)
    @Column(name = "mb_nom")
    private String mbNom;
    @Size(max = 2147483647)
    @Column(name = "mb_prenom")
    private String mbPrenom;

    public Membre() {
    }

    public Membre(Long mbId) {
        this.mbId = mbId;
    }

    public Membre(Long mbId, long mbPassid) {
        this.mbId = mbId;
        this.mbPassid = mbPassid;
    }

    public String getMbMail() {
        return mbMail;
    }

    public void setMbMail(String mbMail) {
        this.mbMail = mbMail;
    }

    public String getMbPseudo() {
        return mbPseudo;
    }

    public void setMbPseudo(String mbPseudo) {
        this.mbPseudo = mbPseudo;
    }

    public Long getMbId() {
        return mbId;
    }

    public void setMbId(Long mbId) {
        this.mbId = mbId;
    }

    public Date getMbDtcreation() {
        return mbDtcreation;
    }

    public void setMbDtcreation(Date mbDtcreation) {
        this.mbDtcreation = mbDtcreation;
    }

    public long getMbPassid() {
        return mbPassid;
    }

    public void setMbPassid(long mbPassid) {
        this.mbPassid = mbPassid;
    }

    public String getMbNom() {
        return mbNom;
    }

    public void setMbNom(String mbNom) {
        this.mbNom = mbNom;
    }

    public String getMbPrenom() {
        return mbPrenom;
    }

    public void setMbPrenom(String mbPrenom) {
        this.mbPrenom = mbPrenom;
    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (mbId != null ? mbId.hashCode() : 0);
//        return hash;
//    }

//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof Membre)) {
//            return false;
//        }
//        Membre other = (Membre) object;
//        if ((this.mbId == null && other.mbId != null) || (this.mbId != null && !this.mbId.equals(other.mbId))) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.Membre[ mbId=" + mbId + " ]";
    }
    
}
