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
@Table(name = "produit_stats", catalog = "TruePrice", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProduitStats.findAll", query = "SELECT p FROM ProduitStats p"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsId", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsId = :pdtStatsId"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsUser", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsUser = :pdtStatsUser"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsProduit", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsProduit = :pdtStatsProduit"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsMinus4", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsMinus4 = :pdtStatsMinus4"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsMinus3", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsMinus3 = :pdtStatsMinus3"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsMinus2", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsMinus2 = :pdtStatsMinus2"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsMinus1", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsMinus1 = :pdtStatsMinus1"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsCenter", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsCenter = :pdtStatsCenter"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsPlus1", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsPlus1 = :pdtStatsPlus1"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsPlus2", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsPlus2 = :pdtStatsPlus2"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsPlus3", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsPlus3 = :pdtStatsPlus3"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsPlus4", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsPlus4 = :pdtStatsPlus4"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsFreqMost", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsFreqMost = :pdtStatsFreqMost"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsFreqMinus1", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsFreqMinus1 = :pdtStatsFreqMinus1"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsFreqMinus2", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsFreqMinus2 = :pdtStatsFreqMinus2"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsAvgMinus1", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsAvgMinus1 = :pdtStatsAvgMinus1"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsAvgMinus2", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsAvgMinus2 = :pdtStatsAvgMinus2"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsSumMinus1", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsSumMinus1 = :pdtStatsSumMinus1"),
    @NamedQuery(name = "ProduitStats.findByPdtStatsSumMinus2", query = "SELECT p FROM ProduitStats p WHERE p.pdtStatsSumMinus2 = :pdtStatsSumMinus2")})
public class ProduitStats implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pdt_stats_id")
    private Long pdtStatsId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pdt_stats_user")
    private long pdtStatsUser;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pdt_stats_produit")
    private long pdtStatsProduit;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_minus_4")
    private String pdtStatsMinus4;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_minus_3")
    private String pdtStatsMinus3;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_minus_2")
    private String pdtStatsMinus2;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_minus_1")
    private String pdtStatsMinus1;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_center")
    private String pdtStatsCenter;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_plus_1")
    private String pdtStatsPlus1;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_plus_2")
    private String pdtStatsPlus2;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_plus_3")
    private String pdtStatsPlus3;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_plus_4")
    private String pdtStatsPlus4;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_freq_most")
    private String pdtStatsFreqMost;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_freq_minus_1")
    private String pdtStatsFreqMinus1;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_freq_minus_2")
    private String pdtStatsFreqMinus2;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_avg_minus_1")
    private String pdtStatsAvgMinus1;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_avg_minus_2")
    private String pdtStatsAvgMinus2;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_sum_minus_1")
    private String pdtStatsSumMinus1;
    @Size(max = 2147483647)
    @Column(name = "pdt_stats_sum_minus_2")
    private String pdtStatsSumMinus2;

    public ProduitStats() {
    }

    public ProduitStats(Long pdtStatsId) {
        this.pdtStatsId = pdtStatsId;
    }

    public ProduitStats(Long pdtStatsId, long pdtStatsUser, long pdtStatsProduit) {
        this.pdtStatsId = pdtStatsId;
        this.pdtStatsUser = pdtStatsUser;
        this.pdtStatsProduit = pdtStatsProduit;
    }

    public Long getPdtStatsId() {
        return pdtStatsId;
    }

    public void setPdtStatsId(Long pdtStatsId) {
        this.pdtStatsId = pdtStatsId;
    }

    public long getPdtStatsUser() {
        return pdtStatsUser;
    }

    public void setPdtStatsUser(long pdtStatsUser) {
        this.pdtStatsUser = pdtStatsUser;
    }

    public long getPdtStatsProduit() {
        return pdtStatsProduit;
    }

    public void setPdtStatsProduit(long pdtStatsProduit) {
        this.pdtStatsProduit = pdtStatsProduit;
    }

    public String getPdtStatsMinus4() {
        return pdtStatsMinus4;
    }

    public void setPdtStatsMinus4(String pdtStatsMinus4) {
        this.pdtStatsMinus4 = pdtStatsMinus4;
    }

    public String getPdtStatsMinus3() {
        return pdtStatsMinus3;
    }

    public void setPdtStatsMinus3(String pdtStatsMinus3) {
        this.pdtStatsMinus3 = pdtStatsMinus3;
    }

    public String getPdtStatsMinus2() {
        return pdtStatsMinus2;
    }

    public void setPdtStatsMinus2(String pdtStatsMinus2) {
        this.pdtStatsMinus2 = pdtStatsMinus2;
    }

    public String getPdtStatsMinus1() {
        return pdtStatsMinus1;
    }

    public void setPdtStatsMinus1(String pdtStatsMinus1) {
        this.pdtStatsMinus1 = pdtStatsMinus1;
    }

    public String getPdtStatsCenter() {
        return pdtStatsCenter;
    }

    public void setPdtStatsCenter(String pdtStatsCenter) {
        this.pdtStatsCenter = pdtStatsCenter;
    }

    public String getPdtStatsPlus1() {
        return pdtStatsPlus1;
    }

    public void setPdtStatsPlus1(String pdtStatsPlus1) {
        this.pdtStatsPlus1 = pdtStatsPlus1;
    }

    public String getPdtStatsPlus2() {
        return pdtStatsPlus2;
    }

    public void setPdtStatsPlus2(String pdtStatsPlus2) {
        this.pdtStatsPlus2 = pdtStatsPlus2;
    }

    public String getPdtStatsPlus3() {
        return pdtStatsPlus3;
    }

    public void setPdtStatsPlus3(String pdtStatsPlus3) {
        this.pdtStatsPlus3 = pdtStatsPlus3;
    }

    public String getPdtStatsPlus4() {
        return pdtStatsPlus4;
    }

    public void setPdtStatsPlus4(String pdtStatsPlus4) {
        this.pdtStatsPlus4 = pdtStatsPlus4;
    }

    public String getPdtStatsFreqMost() {
        return pdtStatsFreqMost;
    }

    public void setPdtStatsFreqMost(String pdtStatsFreqMost) {
        this.pdtStatsFreqMost = pdtStatsFreqMost;
    }

    public String getPdtStatsFreqMinus1() {
        return pdtStatsFreqMinus1;
    }

    public void setPdtStatsFreqMinus1(String pdtStatsFreqMinus1) {
        this.pdtStatsFreqMinus1 = pdtStatsFreqMinus1;
    }

    public String getPdtStatsFreqMinus2() {
        return pdtStatsFreqMinus2;
    }

    public void setPdtStatsFreqMinus2(String pdtStatsFreqMinus2) {
        this.pdtStatsFreqMinus2 = pdtStatsFreqMinus2;
    }

    public String getPdtStatsAvgMinus1() {
        return pdtStatsAvgMinus1;
    }

    public void setPdtStatsAvgMinus1(String pdtStatsAvgMinus1) {
        this.pdtStatsAvgMinus1 = pdtStatsAvgMinus1;
    }

    public String getPdtStatsAvgMinus2() {
        return pdtStatsAvgMinus2;
    }

    public void setPdtStatsAvgMinus2(String pdtStatsAvgMinus2) {
        this.pdtStatsAvgMinus2 = pdtStatsAvgMinus2;
    }

    public String getPdtStatsSumMinus1() {
        return pdtStatsSumMinus1;
    }

    public void setPdtStatsSumMinus1(String pdtStatsSumMinus1) {
        this.pdtStatsSumMinus1 = pdtStatsSumMinus1;
    }

    public String getPdtStatsSumMinus2() {
        return pdtStatsSumMinus2;
    }

    public void setPdtStatsSumMinus2(String pdtStatsSumMinus2) {
        this.pdtStatsSumMinus2 = pdtStatsSumMinus2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pdtStatsId != null ? pdtStatsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProduitStats)) {
            return false;
        }
        ProduitStats other = (ProduitStats) object;
        if ((this.pdtStatsId == null && other.pdtStatsId != null) || (this.pdtStatsId != null && !this.pdtStatsId.equals(other.pdtStatsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.ProduitStats[ pdtStatsId=" + pdtStatsId + " ]";
    }
    
}
