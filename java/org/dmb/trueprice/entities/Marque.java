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
    @NamedQuery(name = "Marque.findAll", query = "SELECT m FROM Marque m"),
    @NamedQuery(name = "Marque.findByMrqId", query = "SELECT m FROM Marque m WHERE m.mrqId = :mrqId"),
    @NamedQuery(name = "Marque.findByMrqLabel", query = "SELECT m FROM Marque m WHERE m.mrqLabel = :mrqLabel"),
    @NamedQuery(name = "Marque.findByMrqDescription", query = "SELECT m FROM Marque m WHERE m.mrqDescription = :mrqDescription")})
public class Marque implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mrq_id")
    private Integer mrqId;
    @Size(max = 2147483647)
    @Column(name = "mrq_label")
    private String mrqLabel;
    @Size(max = 2147483647)
    @Column(name = "mrq_description")
    private String mrqDescription;

    public Marque() {
    }

    public Marque(Integer mrqId) {
        this.mrqId = mrqId;
    }

    public Integer getMrqId() {
        return mrqId;
    }

    public void setMrqId(Integer mrqId) {
        this.mrqId = mrqId;
    }

    public String getMrqLabel() {
        return mrqLabel;
    }

    public void setMrqLabel(String mrqLabel) {
        this.mrqLabel = mrqLabel;
    }

    public String getMrqDescription() {
        return mrqDescription;
    }

    public void setMrqDescription(String mrqDescription) {
        this.mrqDescription = mrqDescription;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mrqId != null ? mrqId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Marque)) {
            return false;
        }
        Marque other = (Marque) object;
        if ((this.mrqId == null && other.mrqId != null) || (this.mrqId != null && !this.mrqId.equals(other.mrqId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.Marque[ mrqId=" + mrqId + " ]";
    }
    
}
