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
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

/**
 *
 * @author Guiitch
 */
@Entity
@Table(catalog = "TruePrice", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Password.findAll", query = "SELECT p FROM Password p"),
    @NamedQuery(name = "Password.findByPwdId", query = "SELECT p FROM Password p WHERE p.pwdId = :pwdId"),
    @NamedQuery(name = "Password.findByPwdValue", query = "SELECT p FROM Password p WHERE p.pwdValue = :pwdValue"),
    @NamedQuery(name = "Password.findByPwdDtcreation", query = "SELECT p FROM Password p WHERE p.pwdDtcreation = :pwdDtcreation")})
public class Password implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pwd_id")
    private Long pwdId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pwd_value")
    private String pwdValue;
    @Column(name = "pwd_dtcreation")
    @Temporal(TemporalType.TIMESTAMP)
    @Converter (name = "dateTimeConverter"
            , converterClass = org.dmb.trueprice.utils.external.JodaDateTimeConverter.class    )
    @Convert (  value = "dateTimeConverter" )
    private Date pwdDtcreation;

    public Password() {
    }

    public Password(Long pwdId) {
        this.pwdId = pwdId;
    }

    public Password(Long pwdId, String pwdValue) {
        this.pwdId = pwdId;
        this.pwdValue = pwdValue;
    }

    public Password(Long pwdId, String pwdValue, Date pwdDtcreation) {
        this.pwdId = pwdId;
        this.pwdValue = pwdValue;
        this.pwdDtcreation = pwdDtcreation;
    }

    public Password(String pwdValue, Date pwdDtcreation) {
        this.pwdValue = pwdValue;
        this.pwdDtcreation = pwdDtcreation;
    }

    public Password(String pwdValue) {
        this.pwdValue = pwdValue;
    }

    public Long getMbPassid() {
        return pwdId;
    }

    public void setPwdId(Long pwdId) {
        this.pwdId = pwdId;
    }

    public String getPwdValue() {
        return pwdValue;
    }

    public void setPwdValue(String pwdValue) {
        this.pwdValue = pwdValue;
    }

    public Date getPwdDtcreation() {
        return pwdDtcreation;
    }

    public void setPwdDtcreation(Date pwdDtcreation) {
        this.pwdDtcreation = pwdDtcreation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pwdId != null ? pwdId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Password)) {
            return false;
        }
        Password other = (Password) object;
        if ((this.pwdId == null && other.pwdId != null) || (this.pwdId != null && !this.pwdId.equals(other.pwdId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.dmb.trueprice.entities.Password[ pwdId=" + pwdId + " ]";
    }
    
}
