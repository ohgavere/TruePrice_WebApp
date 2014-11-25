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
    @NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c"),
    @NamedQuery(name = "Category.findByCtgId", query = "SELECT c FROM Category c WHERE c.ctgId = :ctgId"),
    @NamedQuery(name = "Category.findByCtgLabel", query = "SELECT c FROM Category c WHERE c.ctgLabel = :ctgLabel"),
    @NamedQuery(name = "Category.findByCtgDescription", query = "SELECT c FROM Category c WHERE c.ctgDescription = :ctgDescription")})
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ctg_id")
    private Integer ctgId;
    @Size(max = 2147483647)
    @Column(name = "ctg_label")
    private String ctgLabel;
    @Size(max = 2147483647)
    @Column(name = "ctg_description")
    private String ctgDescription;

    public Category() {
    }

    public Category(Integer ctgId) {
        this.ctgId = ctgId;
    }

    public Integer getCtgId() {
        return ctgId;
    }

    public void setCtgId(Integer ctgId) {
        this.ctgId = ctgId;
    }

    public String getCtgLabel() {
        return ctgLabel;
    }

    public void setCtgLabel(String ctgLabel) {
        this.ctgLabel = ctgLabel;
    }

    public String getCtgDescription() {
        return ctgDescription;
    }

    public void setCtgDescription(String ctgDescription) {
        this.ctgDescription = ctgDescription;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ctgId != null ? ctgId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.ctgId == null && other.ctgId != null) || (this.ctgId != null && !this.ctgId.equals(other.ctgId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.Category[ ctgId=" + ctgId + " ]";
    }
    
}
