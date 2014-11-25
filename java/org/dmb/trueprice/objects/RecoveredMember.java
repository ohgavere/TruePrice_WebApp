/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Guitch
 */
/**
 * <p>Java class for RecoveredMember complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RecoveredMember">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="oldId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="newId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="checkSum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="secureLink" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="recoveredMail" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecoveredMember", propOrder = {
    "oldId",
    "newId",
    "checkSum",
    "secureLink",
    "recoveredMail"
})
@XmlRootElement(name = "RecoveredMember")
public class RecoveredMember {

    @XmlElement(required = true)
    private String oldId;
    @XmlElement(required = true)
    private String newId;
    @XmlElement(required = true)
    private String checkSum;
    @XmlElement(required = true)
    private String secureLink;
    @XmlElement(required = true)
    private String recoveredMail;
    
    public RecoveredMember() {
    }

    public RecoveredMember(String oldId, String newId, String checkSum, String secureLink, String recoveredMail) {
        this.oldId = oldId;
        this.newId = newId;
        this.checkSum = checkSum;
        this.secureLink = secureLink;
        this.recoveredMail = recoveredMail;
    }
    
    
    

    /**
     * Setters & Getters to members used when process is complete.
     * In other words, the data supposed to be received by the member via 
     * mail and then returned via GET request to /recover?...
     */
    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public void setRecoveredMail(String recoveredMail) {
        this.recoveredMail = recoveredMail;
    }

    public void setSecureLink(String secureLink) {
        this.secureLink = secureLink;
    }

    public String getNewId() {
        return newId;
    }

    public String getOldId() {
        return oldId;
    }

    public String getRecoveredMail() {
        return recoveredMail;
    }

    public String getSecureLink() {
        return secureLink;
    }

    public String getCheckSum() {
        return checkSum;
    }
    
}
