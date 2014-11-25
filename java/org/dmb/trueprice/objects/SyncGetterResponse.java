/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.objects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Guitch
 */
public class SyncGetterResponse  {
    
    // Les resultats disponibles sur le device [liste_id, result_releaseDate]
    private ArrayList<ListeDetailFrontend> providedLists = new ArrayList<ListeDetailFrontend>();
    private HashMap<Long, String>   mapProduitIcon  ;
    
    public SyncGetterResponse (ArrayList<ListeDetailFrontend> askedLists, HashMap<Long, String>   mapProduitIcon) {
        this.providedLists = askedLists ;
        this.mapProduitIcon = mapProduitIcon ;
    }

    public ArrayList<ListeDetailFrontend> getProvidedLists() {
        return providedLists;
    }

    public HashMap<Long, String> getMapProduitIcon() {
        return mapProduitIcon;
    }
    
    
    
    
    @Override
    public String toString() {
        String str = "\n" ;
        
        str += "Got [" + providedLists.size() + "] lists to send =>" ;
        
        for (ListeDetailFrontend liste : providedLists) {
            str += "\n Member ask for download Liste \t ID:[" + liste.getLstId() + "]" ;
        }
        str += "\n Got map Produit ID <-> Icon ==> " ;
        
        for (long id : mapProduitIcon.keySet()) {
            str += "\n ID:[" + id + "] ICON[" + mapProduitIcon.get(id) +"]";
        }
        
        return str += "\n" ;
    }
    
    
    
    
}
