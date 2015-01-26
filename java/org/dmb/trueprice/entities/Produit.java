/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.entities;


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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Guiitch
 */
@Entity (name = "ProduitListe")
@Table(name = "produit_liste", catalog = "TruePrice", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProduitListe.findAll", query = "SELECT p FROM ProduitListe p"),
    @NamedQuery(name = "ProduitListe.findByPdtId", query = "SELECT p FROM ProduitListe p WHERE p.pdtId = :pdtId"),
    @NamedQuery(name = "ProduitListe.findByPdtNom", query = "SELECT p FROM ProduitListe p WHERE p.pdtNom = :pdtNom"),
    @NamedQuery(name = "ProduitListe.findByPdtDescription", query = "SELECT p FROM ProduitListe p WHERE p.pdtDescription = :pdtDescription"),
    @NamedQuery(name = "ProduitListe.findByPdtTvaTaux", query = "SELECT p FROM ProduitListe p WHERE p.pdtTvaTaux = :pdtTvaTaux"),
    @NamedQuery(name = "ProduitListe.findByPdtLink", query = "SELECT p FROM ProduitListe p WHERE p.pdtLink = :pdtLink"),
    @NamedQuery(name = "ProduitListe.findByPdtDtcreation", query = "SELECT p FROM ProduitListe p WHERE p.pdtDtcreation = :pdtDtcreation"),
    @NamedQuery(name = "ProduitListe.findByPdtPeriodPrefStart", query = "SELECT p FROM ProduitListe p WHERE p.pdtPeriodPrefStart = :pdtPeriodPrefStart"),
    @NamedQuery(name = "ProduitListe.findByPdtPeriodPrefStop", query = "SELECT p FROM ProduitListe p WHERE p.pdtPeriodPrefStop = :pdtPeriodPrefStop"),
    @NamedQuery(name = "ProduitListe.findByPdtProperty", query = "SELECT p FROM ProduitListe p WHERE p.pdtProperty = :pdtProperty"),
    @NamedQuery(name = "ProduitListe.findByPdtSubproperty", query = "SELECT p FROM ProduitListe p WHERE p.pdtSubproperty = :pdtSubproperty"),
    @NamedQuery(name = "ProduitListe.findByPdtCategory", query = "SELECT p FROM ProduitListe p WHERE p.pdtCategory = :pdtCategory"),
    @NamedQuery(name = "ProduitListe.findByPdtSubcategory", query = "SELECT p FROM ProduitListe p WHERE p.pdtSubcategory = :pdtSubcategory"),
    @NamedQuery(name = "ProduitListe.findByPdtEnsigne", query = "SELECT p FROM ProduitListe p WHERE p.pdtEnsigne = :pdtEnsigne"),
    @NamedQuery(name = "ProduitListe.findByPdtMarque", query = "SELECT p FROM ProduitListe p WHERE p.pdtMarque = :pdtMarque"),
    @NamedQuery(name = "ProduitListe.findByPdtQtt", query = "SELECT p FROM ProduitListe p WHERE p.pdtQtt = :pdtQtt")
})
public class Produit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pdt_id")
    private Long pdtId;
    @Size(max = 2147483647)
    @Column(name = "pdt_nom")
    private String pdtNom;
    @Size(max = 2147483647)
    @Column(name = "pdt_description")
    private String pdtDescription;
    @Size(max = 2147483647)
    @Column(name = "pdt_tva_taux")
    private String pdtTvaTaux;
    @Size(max = 2147483647)
    @Column(name = "pdt_link")
    private String pdtLink;
    @Column(name = "pdt_dtcreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pdtDtcreation;
    @Column(name = "pdt_period_pref_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pdtPeriodPrefStart;
    @Column(name = "pdt_period_pref_stop")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pdtPeriodPrefStop;
    @Size(max = 2147483647)
    @Column(name = "pdt_property")
    private String pdtProperty;
    @Size(max = 2147483647)
    @Column(name = "pdt_subproperty")
    private String pdtSubproperty;
    @Basic(optional = false)
    @Column(name = "pdt_category")
    private int pdtCategory;
    @Basic(optional = false)
    @Column(name = "pdt_subcategory")
    private int pdtSubcategory;
    @Column(name = "pdt_ensigne")
    private long pdtEnsigne;
    @Column(name = "pdt_marque")
    private long pdtMarque;
    @Column(name = "pdt_qtt")
    private long pdtQtt;

    public Produit() {
    }

    public Produit(Long pdtId) {
        this.pdtId = pdtId;
    }

    public Produit(Long pdtId, int pdtCategory, int pdtSubcategory) {
        this.pdtId = pdtId;
        this.pdtCategory = pdtCategory;
        this.pdtSubcategory = pdtSubcategory;
    }

    public Long getPdtId() {
        return pdtId;
    }

    public void setPdtId(Long pdtId) {
        this.pdtId = pdtId;
    }

    public String getPdtNom() {
        return pdtNom;
    }

    public void setPdtNom(String pdtNom) {
        this.pdtNom = pdtNom;
    }

    public String getPdtDescription() {
        return pdtDescription;
    }

    public void setPdtDescription(String pdtDescription) {
        this.pdtDescription = pdtDescription;
    }

    public String getPdtTvaTaux() {
        return pdtTvaTaux;
    }

    public void setPdtTvaTaux(String pdtTvaTaux) {
        this.pdtTvaTaux = pdtTvaTaux;
    }

    public String getPdtLink() {
        return pdtLink;
    }

    public void setPdtLink(String pdtLink) {
        this.pdtLink = pdtLink;
    }

    public Date getPdtDtcreation() {
        return pdtDtcreation;
    }

    public void setPdtDtcreation(Date pdtDtcreation) {
        this.pdtDtcreation = pdtDtcreation;
    }

    public Date getPdtPeriodPrefStart() {
        return pdtPeriodPrefStart;
    }

    public void setPdtPeriodPrefStart(Date pdtPeriodPrefStart) {
        this.pdtPeriodPrefStart = pdtPeriodPrefStart;
    }

    public Date getPdtPeriodPrefStop() {
        return pdtPeriodPrefStop;
    }

    public void setPdtPeriodPrefStop(Date pdtPeriodPrefStop) {
        this.pdtPeriodPrefStop = pdtPeriodPrefStop;
    }

    public String getPdtProperty() {
        return pdtProperty;
    }

    public void setPdtProperty(String pdtProperty) {
        this.pdtProperty = pdtProperty;
    }

    public String getPdtSubproperty() {
        return pdtSubproperty;
    }

    public void setPdtSubproperty(String pdtSubproperty) {
        this.pdtSubproperty = pdtSubproperty;
    }

    public int getPdtCategory() {
        return pdtCategory;
    }

    public void setPdtCategory(int pdtCategory) {
        this.pdtCategory = pdtCategory;
    }

    public int getPdtSubcategory() {
        return pdtSubcategory;
    }

    public void setPdtSubcategory(int pdtSubcategory) {
        this.pdtSubcategory = pdtSubcategory;
    }

    public long getPdtEnsigne() {
        return pdtEnsigne;
    }

    public void setPdtEnsigne(long pdtEnsigne) {
        this.pdtEnsigne = pdtEnsigne;
    }

    public long getPdtMarque() {
        return pdtMarque;
    }

    public void setPdtMarque(long pdtMarque) {
        this.pdtMarque = pdtMarque;
    }

    public long getPdtQtt() {
        return pdtQtt;
    }

    public void setPdtQtt(long pdtQtt) {
        this.pdtQtt = pdtQtt;
    }
    
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pdtId != null ? pdtId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produit)) {
            return false;
        }
        Produit other = (Produit) object;
        if ((this.pdtId == null && other.pdtId != null) || (this.pdtId != null && !this.pdtId.equals(other.pdtId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.ProduitListe[ pdtId=" + pdtId + " ]";
    }
    
}
