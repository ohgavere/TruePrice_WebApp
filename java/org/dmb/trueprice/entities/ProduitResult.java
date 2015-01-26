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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Guitch
 */
@Entity
@Table(name = "produit_result")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProduitResult.findAll", query = "SELECT p FROM ProduitResult p"),
    @NamedQuery(name = "ProduitResult.findByPdtResultId", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultId = :pdtResultId"),
    @NamedQuery(name = "ProduitResult.findByPdtResultProduit", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultProduit = :pdtResultProduit"),
    @NamedQuery(name = "ProduitResult.findByPdtResultUser", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultUser = :pdtResultUser"),
    @NamedQuery(name = "ProduitResult.findByPdtResultQttValue", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultQttValue = :pdtResultQttValue"),
    @NamedQuery(name = "ProduitResult.findByPdtResultPriceSale", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultPriceSale = :pdtResultPriceSale"),
    @NamedQuery(name = "ProduitResult.findByPdtResultPriceMeasure", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultPriceMeasure = :pdtResultPriceMeasure"),
    @NamedQuery(name = "ProduitResult.findByPdtResultRdcType", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultRdcType = :pdtResultRdcType"),
    @NamedQuery(name = "ProduitResult.findByPdtResultRdcOfferer", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultRdcOfferer = :pdtResultRdcOfferer"),
    @NamedQuery(name = "ProduitResult.findByPdtResultRdcMinimumUnits", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultRdcMinimumUnits = :pdtResultRdcMinimumUnits"),
    @NamedQuery(name = "ProduitResult.findByPdtResultRdcCashback", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultRdcCashback = :pdtResultRdcCashback"),
    @NamedQuery(name = "ProduitResult.findByPdtResultRdcUnitsOffered", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultRdcUnitsOffered = :pdtResultRdcUnitsOffered"),
    @NamedQuery(name = "ProduitResult.findByPdtResultRdcValueOffered", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultRdcValueOffered = :pdtResultRdcValueOffered"),
    @NamedQuery(name = "ProduitResult.findByPdtResultRdcWholesaleUnits", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultRdcWholesaleUnits = :pdtResultRdcWholesaleUnits"),
    @NamedQuery(name = "ProduitResult.findByPdtResultRdcWholesalePrice", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultRdcWholesalePrice = :pdtResultRdcWholesalePrice"),
    @NamedQuery(name = "ProduitResult.findByPdtResultRdcWholesaleStep", query = "SELECT p FROM ProduitResult p WHERE p.pdtResultRdcWholesaleStep = :pdtResultRdcWholesaleStep")})
public class ProduitResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "pdt_result_id")
    private Integer pdtResultId;
    @Column(name = "pdt_result_produit")
    private Integer pdtResultProduit;
    @Column(name = "pdt_result_user")
    private Integer pdtResultUser;
    @Size(max = 2147483647)
    @Column(name = "pdt_result_qtt_value")
    private String pdtResultQttValue;
    @Size(max = 2147483647)
    @Column(name = "pdt_result_price_sale")
    private String pdtResultPriceSale;
    @Size(max = 2147483647)
    @Column(name = "pdt_result_price_measure")
    private String pdtResultPriceMeasure;
    @Size(max = 2147483647)
    @Column(name = "pdt_result_rdc_type")
    private String pdtResultRdcType;
    @Size(max = 2147483647)
    @Column(name = "pdt_result_rdc_offerer")
    private String pdtResultRdcOfferer;
    @Size(max = 2147483647)
    @Column(name = "pdt_result_rdc_minimum_units")
    private String pdtResultRdcMinimumUnits;
    @Size(max = 2147483647)
    @Column(name = "pdt_result_rdc_cashback")
    private String pdtResultRdcCashback;
    @Size(max = 2147483647)
    @Column(name = "pdt_result_rdc_units_offered")
    private String pdtResultRdcUnitsOffered;
    @Size(max = 2147483647)
    @Column(name = "pdt_result_rdc_value_offered")
    private String pdtResultRdcValueOffered;
    @Size(max = 2147483647)
    @Column(name = "pdt_result_rdc_wholesale_units")
    private String pdtResultRdcWholesaleUnits;
    @Size(max = 2147483647)
    @Column(name = "pdt_result_rdc_wholesale_price")
    private String pdtResultRdcWholesalePrice;
    @Size(max = 2147483647)
    @Column(name = "pdt_result_rdc_wholesale_step")
    private String pdtResultRdcWholesaleStep;

    public ProduitResult() {
    }

    public ProduitResult(Integer pdtResultId) {
        this.pdtResultId = pdtResultId;
    }

    public Integer getPdtResultId() {
        return pdtResultId;
    }

    public void setPdtResultId(Integer pdtResultId) {
        this.pdtResultId = pdtResultId;
    }

    public Integer getPdtResultProduit() {
        return pdtResultProduit;
    }

    public void setPdtResultProduit(Integer pdtResultProduit) {
        this.pdtResultProduit = pdtResultProduit;
    }

    public Integer getPdtResultUser() {
        return pdtResultUser;
    }

    public void setPdtResultUser(Integer pdtResultUser) {
        this.pdtResultUser = pdtResultUser;
    }

    public String getPdtResultQttValue() {
        return pdtResultQttValue;
    }

    public void setPdtResultQttValue(String pdtResultQttValue) {
        this.pdtResultQttValue = pdtResultQttValue;
    }

    public String getPdtResultPriceSale() {
        return pdtResultPriceSale;
    }

    public void setPdtResultPriceSale(String pdtResultPriceSale) {
        this.pdtResultPriceSale = pdtResultPriceSale;
    }

    public String getPdtResultPriceMeasure() {
        return pdtResultPriceMeasure;
    }

    public void setPdtResultPriceMeasure(String pdtResultPriceMeasure) {
        this.pdtResultPriceMeasure = pdtResultPriceMeasure;
    }

    public String getPdtResultRdcType() {
        return pdtResultRdcType;
    }

    public void setPdtResultRdcType(String pdtResultRdcType) {
        this.pdtResultRdcType = pdtResultRdcType;
    }

    public String getPdtResultRdcOfferer() {
        return pdtResultRdcOfferer;
    }

    public void setPdtResultRdcOfferer(String pdtResultRdcOfferer) {
        this.pdtResultRdcOfferer = pdtResultRdcOfferer;
    }

    public String getPdtResultRdcMinimumUnits() {
        return pdtResultRdcMinimumUnits;
    }

    public void setPdtResultRdcMinimumUnits(String pdtResultRdcMinimumUnits) {
        this.pdtResultRdcMinimumUnits = pdtResultRdcMinimumUnits;
    }

    public String getPdtResultRdcCashback() {
        return pdtResultRdcCashback;
    }

    public void setPdtResultRdcCashback(String pdtResultRdcCashback) {
        this.pdtResultRdcCashback = pdtResultRdcCashback;
    }

    public String getPdtResultRdcUnitsOffered() {
        return pdtResultRdcUnitsOffered;
    }

    public void setPdtResultRdcUnitsOffered(String pdtResultRdcUnitsOffered) {
        this.pdtResultRdcUnitsOffered = pdtResultRdcUnitsOffered;
    }

    public String getPdtResultRdcValueOffered() {
        return pdtResultRdcValueOffered;
    }

    public void setPdtResultRdcValueOffered(String pdtResultRdcValueOffered) {
        this.pdtResultRdcValueOffered = pdtResultRdcValueOffered;
    }

    public String getPdtResultRdcWholesaleUnits() {
        return pdtResultRdcWholesaleUnits;
    }

    public void setPdtResultRdcWholesaleUnits(String pdtResultRdcWholesaleUnits) {
        this.pdtResultRdcWholesaleUnits = pdtResultRdcWholesaleUnits;
    }

    public String getPdtResultRdcWholesalePrice() {
        return pdtResultRdcWholesalePrice;
    }

    public void setPdtResultRdcWholesalePrice(String pdtResultRdcWholesalePrice) {
        this.pdtResultRdcWholesalePrice = pdtResultRdcWholesalePrice;
    }

    public String getPdtResultRdcWholesaleStep() {
        return pdtResultRdcWholesaleStep;
    }

    public void setPdtResultRdcWholesaleStep(String pdtResultRdcWholesaleStep) {
        this.pdtResultRdcWholesaleStep = pdtResultRdcWholesaleStep;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pdtResultId != null ? pdtResultId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProduitResult)) {
            return false;
        }
        ProduitResult other = (ProduitResult) object;
        if ((this.pdtResultId == null && other.pdtResultId != null) || (this.pdtResultId != null && !this.pdtResultId.equals(other.pdtResultId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.ProduitResult[ pdtResultId=" + pdtResultId + " ]";
    }
    
}
