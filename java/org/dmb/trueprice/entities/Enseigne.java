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
@Table(catalog = "TruePrice", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Enseigne.findAll", query = "SELECT e FROM Enseigne e"),
    @NamedQuery(name = "Enseigne.findByEsgnId", query = "SELECT e FROM Enseigne e WHERE e.esgnId = :esgnId"),
    @NamedQuery(name = "Enseigne.findByEsgnLabel", query = "SELECT e FROM Enseigne e WHERE e.esgnLabel = :esgnLabel"),
    @NamedQuery(name = "Enseigne.findByEsgnDescription", query = "SELECT e FROM Enseigne e WHERE e.esgnDescription = :esgnDescription"),
    @NamedQuery(name = "Enseigne.findByEsgnAdresse", query = "SELECT e FROM Enseigne e WHERE e.esgnAdresse = :esgnAdresse"),
    @NamedQuery(name = "Enseigne.findByEsgnVille", query = "SELECT e FROM Enseigne e WHERE e.esgnVille = :esgnVille"),
    @NamedQuery(name = "Enseigne.findByEsgnCp", query = "SELECT e FROM Enseigne e WHERE e.esgnCp = :esgnCp"),
    @NamedQuery(name = "Enseigne.findByEsgnTel", query = "SELECT e FROM Enseigne e WHERE e.esgnTel = :esgnTel")})
public class Enseigne implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "esgn_id")
    private Long esgnId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "esgn_label")
    private String esgnLabel;
    @Size(max = 2147483647)
    @Column(name = "esgn_description")
    private String esgnDescription;
    @Size(max = 2147483647)
    @Column(name = "esgn_adresse")
    private String esgnAdresse;
    @Size(max = 2147483647)
    @Column(name = "esgn_ville")
    private String esgnVille;
    @Size(max = 2147483647)
    @Column(name = "esgn_cp")
    private String esgnCp;
    @Size(max = 2147483647)
    @Column(name = "esgn_tel")
    private String esgnTel;

    public Enseigne() {
    }

    public Enseigne(Long esgnId) {
        this.esgnId = esgnId;
    }

    public Enseigne(Long esgnId, String esgnLabel) {
        this.esgnId = esgnId;
        this.esgnLabel = esgnLabel;
    }

    public Long getEsgnId() {
        return esgnId;
    }

    public void setEsgnId(Long esgnId) {
        this.esgnId = esgnId;
    }

    public String getEsgnLabel() {
        return esgnLabel;
    }

    public void setEsgnLabel(String esgnLabel) {
        this.esgnLabel = esgnLabel;
    }

    public String getEsgnDescription() {
        return esgnDescription;
    }

    public void setEsgnDescription(String esgnDescription) {
        this.esgnDescription = esgnDescription;
    }

    public String getEsgnAdresse() {
        return esgnAdresse;
    }

    public void setEsgnAdresse(String esgnAdresse) {
        this.esgnAdresse = esgnAdresse;
    }

    public String getEsgnVille() {
        return esgnVille;
    }

    public void setEsgnVille(String esgnVille) {
        this.esgnVille = esgnVille;
    }

    public String getEsgnCp() {
        return esgnCp;
    }

    public void setEsgnCp(String esgnCp) {
        this.esgnCp = esgnCp;
    }

    public String getEsgnTel() {
        return esgnTel;
    }

    public void setEsgnTel(String esgnTel) {
        this.esgnTel = esgnTel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (esgnId != null ? esgnId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Enseigne)) {
            return false;
        }
        Enseigne other = (Enseigne) object;
        if ((this.esgnId == null && other.esgnId != null) || (this.esgnId != null && !this.esgnId.equals(other.esgnId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.Enseigne[ esgnId=" + esgnId + " ]";
    }
    
}
