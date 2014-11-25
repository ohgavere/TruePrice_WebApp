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
@Table(name = "produit_info", catalog = "TruePrice", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProduitInfo.findAll", query = "SELECT p FROM ProduitInfo p"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoUser", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoUser = :pdtInfoUser"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoProduit", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoProduit = :pdtInfoProduit"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoLastInsert", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoLastInsert = :pdtInfoLastInsert"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoBeforeLast", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoBeforeLast = :pdtInfoBeforeLast"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoLastResult", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoLastResult = :pdtInfoLastResult"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoBeforeLastResult", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoBeforeLastResult = :pdtInfoBeforeLastResult"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoLastDate", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoLastDate = :pdtInfoLastDate"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoVeforeDate", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoVeforeDate = :pdtInfoVeforeDate"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoRdcRvd", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoRdcRvd = :pdtInfoRdcRvd"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoRdcFbq", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoRdcFbq = :pdtInfoRdcFbq"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoRdcOther", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoRdcOther = :pdtInfoRdcOther"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoMin", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoMin = :pdtInfoMin"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoMax", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoMax = :pdtInfoMax"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoComment", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoComment = :pdtInfoComment"),
    @NamedQuery(name = "ProduitInfo.findByPdtInfoId", query = "SELECT p FROM ProduitInfo p WHERE p.pdtInfoId = :pdtInfoId")})
public class ProduitInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pdt_info_user")
    private long pdtInfoUser;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pdt_info_produit")
    private long pdtInfoProduit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "pdt_info_last_insert")
    private String pdtInfoLastInsert;
    @Size(max = 2147483647)
    @Column(name = "pdt_info_before_last")
    private String pdtInfoBeforeLast;
    @Size(max = 2147483647)
    @Column(name = "pdt_info_last_result")
    private String pdtInfoLastResult;
    @Size(max = 2147483647)
    @Column(name = "pdt_info_before_last_result")
    private String pdtInfoBeforeLastResult;
    @Column(name = "pdt_info_last_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pdtInfoLastDate;
    @Column(name = "pdt_info_vefore_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pdtInfoVeforeDate;
    @Size(max = 2147483647)
    @Column(name = "pdt_info_rdc_rvd")
    private String pdtInfoRdcRvd;
    @Size(max = 2147483647)
    @Column(name = "pdt_info_rdc_fbq")
    private String pdtInfoRdcFbq;
    @Size(max = 2147483647)
    @Column(name = "pdt_info_rdc_other")
    private String pdtInfoRdcOther;
    @Column(name = "pdt_info_min")
    private Short pdtInfoMin;
    @Column(name = "pdt_info_max")
    private Short pdtInfoMax;
    @Size(max = 2147483647)
    @Column(name = "pdt_info_comment")
    private String pdtInfoComment;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pdt_info_id")
    private Long pdtInfoId;

    public ProduitInfo() {
    }

    public ProduitInfo(Long pdtInfoId) {
        this.pdtInfoId = pdtInfoId;
    }

    public ProduitInfo(Long pdtInfoId, long pdtInfoUser, long pdtInfoProduit, String pdtInfoLastInsert) {
        this.pdtInfoId = pdtInfoId;
        this.pdtInfoUser = pdtInfoUser;
        this.pdtInfoProduit = pdtInfoProduit;
        this.pdtInfoLastInsert = pdtInfoLastInsert;
    }

    public long getPdtInfoUser() {
        return pdtInfoUser;
    }

    public void setPdtInfoUser(long pdtInfoUser) {
        this.pdtInfoUser = pdtInfoUser;
    }

    public long getPdtInfoProduit() {
        return pdtInfoProduit;
    }

    public void setPdtInfoProduit(long pdtInfoProduit) {
        this.pdtInfoProduit = pdtInfoProduit;
    }

    public String getPdtInfoLastInsert() {
        return pdtInfoLastInsert;
    }

    public void setPdtInfoLastInsert(String pdtInfoLastInsert) {
        this.pdtInfoLastInsert = pdtInfoLastInsert;
    }

    public String getPdtInfoBeforeLast() {
        return pdtInfoBeforeLast;
    }

    public void setPdtInfoBeforeLast(String pdtInfoBeforeLast) {
        this.pdtInfoBeforeLast = pdtInfoBeforeLast;
    }

    public String getPdtInfoLastResult() {
        return pdtInfoLastResult;
    }

    public void setPdtInfoLastResult(String pdtInfoLastResult) {
        this.pdtInfoLastResult = pdtInfoLastResult;
    }

    public String getPdtInfoBeforeLastResult() {
        return pdtInfoBeforeLastResult;
    }

    public void setPdtInfoBeforeLastResult(String pdtInfoBeforeLastResult) {
        this.pdtInfoBeforeLastResult = pdtInfoBeforeLastResult;
    }

    public Date getPdtInfoLastDate() {
        return pdtInfoLastDate;
    }

    public void setPdtInfoLastDate(Date pdtInfoLastDate) {
        this.pdtInfoLastDate = pdtInfoLastDate;
    }

    public Date getPdtInfoVeforeDate() {
        return pdtInfoVeforeDate;
    }

    public void setPdtInfoVeforeDate(Date pdtInfoVeforeDate) {
        this.pdtInfoVeforeDate = pdtInfoVeforeDate;
    }

    public String getPdtInfoRdcRvd() {
        return pdtInfoRdcRvd;
    }

    public void setPdtInfoRdcRvd(String pdtInfoRdcRvd) {
        this.pdtInfoRdcRvd = pdtInfoRdcRvd;
    }

    public String getPdtInfoRdcFbq() {
        return pdtInfoRdcFbq;
    }

    public void setPdtInfoRdcFbq(String pdtInfoRdcFbq) {
        this.pdtInfoRdcFbq = pdtInfoRdcFbq;
    }

    public String getPdtInfoRdcOther() {
        return pdtInfoRdcOther;
    }

    public void setPdtInfoRdcOther(String pdtInfoRdcOther) {
        this.pdtInfoRdcOther = pdtInfoRdcOther;
    }

    public Short getPdtInfoMin() {
        return pdtInfoMin;
    }

    public void setPdtInfoMin(Short pdtInfoMin) {
        this.pdtInfoMin = pdtInfoMin;
    }

    public Short getPdtInfoMax() {
        return pdtInfoMax;
    }

    public void setPdtInfoMax(Short pdtInfoMax) {
        this.pdtInfoMax = pdtInfoMax;
    }

    public String getPdtInfoComment() {
        return pdtInfoComment;
    }

    public void setPdtInfoComment(String pdtInfoComment) {
        this.pdtInfoComment = pdtInfoComment;
    }

    public Long getPdtInfoId() {
        return pdtInfoId;
    }

    public void setPdtInfoId(Long pdtInfoId) {
        this.pdtInfoId = pdtInfoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pdtInfoId != null ? pdtInfoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProduitInfo)) {
            return false;
        }
        ProduitInfo other = (ProduitInfo) object;
        if ((this.pdtInfoId == null && other.pdtInfoId != null) || (this.pdtInfoId != null && !this.pdtInfoId.equals(other.pdtInfoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.ProduitInfo[ pdtInfoId=" + pdtInfoId + " ]";
    }
    
}
