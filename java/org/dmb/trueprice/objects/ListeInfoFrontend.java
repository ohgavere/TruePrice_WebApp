/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dmb.trueprice.objects;

import org.dmb.trueprice.entities.ListeInfo;

/**
 *
 * @author Guitch
 */
public class ListeInfoFrontend extends ListeInfo {

    private String dateUpdated ;
    private String listeLabel ;
    private int pdtCount ;

    public ListeInfoFrontend(ListeInfo lst) {
        setAttributesFromListeInfo(lst);
    }
    
    private void setAttributesFromListeInfo (ListeInfo lst) {
        setListesInfoId(lst.getListesInfoId());
        setListesInfoListe(lst.getListesInfoListe());
        setListesInfoUser(lst.getListesInfoUser());
        setListesInfoLastUp(lst.getListesInfoLastUp());
        setListesInfoLastDown(lst.getListesInfoLastDown());
        setListesInfoLastEquals(lst.getListesInfoLastEquals());
        setListesInfoLastRdcNb(lst.getListesInfoLastRdcNb());
        setListesInfoLastRdcValue(lst.getListesInfoLastRdcValue());
    }
    
    public ListeInfoFrontend(String dateUpdated, String listeLabel, int pdtCount) {
        this.dateUpdated = dateUpdated;
        this.listeLabel = listeLabel;
        this.pdtCount = pdtCount;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public void setListeLabel(String listeLabel) {
        this.listeLabel = listeLabel;
    }

    public void setPdtCount(int pdtCount) {
        this.pdtCount = pdtCount;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public String getListeLabel() {
        return listeLabel;
    }

    public int getPdtCount() {
        return pdtCount;
    }
    
    
    
    
    
}
