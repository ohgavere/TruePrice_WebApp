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
    @NamedQuery(name = "Subcategory.findAll", query = "SELECT s FROM Subcategory s"),
    @NamedQuery(name = "Subcategory.findBySctgId", query = "SELECT s FROM Subcategory s WHERE s.sctgId = :sctgId"),
    @NamedQuery(name = "Subcategory.findBySctgLabel", query = "SELECT s FROM Subcategory s WHERE s.sctgLabel = :sctgLabel"),
    @NamedQuery(name = "Subcategory.findBySctgDescription", query = "SELECT s FROM Subcategory s WHERE s.sctgDescription = :sctgDescription"),
    @NamedQuery(name = "Subcategory.findBySctgParent", query = "SELECT s FROM Subcategory s WHERE s.sctgParent = :sctgParent"),
    @NamedQuery(name = "Subcategory.findBySctgSubparent", query = "SELECT s FROM Subcategory s WHERE s.sctgSubparent = :sctgSubparent")})
public class Subcategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sctg_id")
    private Integer sctgId;
    @Size(max = 2147483647)
    @Column(name = "sctg_label")
    private String sctgLabel;
    @Size(max = 2147483647)
    @Column(name = "sctg_description")
    private String sctgDescription;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sctg_parent")
    private long sctgParent;
    @Column(name = "sctg_subparent")
    private long sctgSubparent;

    public Subcategory() {
    }

    public Subcategory(Integer sctgId) {
        this.sctgId = sctgId;
    }

    public Subcategory(Integer sctgId, long sctgParent) {
        this.sctgId = sctgId;
        this.sctgParent = sctgParent;
    }

    public Integer getSctgId() {
        return sctgId;
    }

    public void setSctgId(Integer sctgId) {
        this.sctgId = sctgId;
    }

    public String getSctgLabel() {
        return sctgLabel;
    }

    public void setSctgLabel(String sctgLabel) {
        this.sctgLabel = sctgLabel;
    }

    public String getSctgDescription() {
        return sctgDescription;
    }

    public void setSctgDescription(String sctgDescription) {
        this.sctgDescription = sctgDescription;
    }

    public long getSctgParent() {
        return sctgParent;
    }

    public void setSctgParent(long sctgParent) {
        this.sctgParent = sctgParent;
    }

    public long getSctgSubparent() {
        return sctgSubparent;
    }

    public void setSctgSubparent(long sctgSubparent) {
        this.sctgSubparent = sctgSubparent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sctgId != null ? sctgId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subcategory)) {
            return false;
        }
        Subcategory other = (Subcategory) object;
        if ((this.sctgId == null && other.sctgId != null) || (this.sctgId != null && !this.sctgId.equals(other.sctgId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.Subcategory[ sctgId=" + sctgId + " ]";
    }
    
}
