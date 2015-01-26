/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.objects;

import java.util.ArrayList;
import org.dmb.trueprice.entities.ProduitInfo;

/**
 *
 * @author Guitch
 */
public class StatsGlobalResponse {
    
//    protected HashMap<Long , String> listsInfos =  new HashMap<Long, String>();
    protected ArrayList<ListeInfoFrontend> listsInfos =  new ArrayList<ListeInfoFrontend>();
    protected ArrayList<ProduitInfo> produitsInfos =  new ArrayList<ProduitInfo>();

    public StatsGlobalResponse() {
    }
    
    
    
//    public SyncInitResponse (HashMap<Long, String> availableListes) {
    public StatsGlobalResponse (ArrayList<ListeInfoFrontend> ListInfoFrontend, ArrayList<ProduitInfo> produitsInfos) {
        this.listsInfos = ListInfoFrontend ;
        this.produitsInfos = produitsInfos ;
    }
    
//    public String getDateModified(Long listeId) { 
//        return listsInfos.get(listeId);
//    }   
//    public Set<Long> getListesIds() {
//        return listsInfos.keySet();
//    }
//    public Collection<String> getTimeElapsedAll() {
//        return listsInfos.values();
//    }

//    @Override
//    public String toString() {
//        String str = "\n" ;
//        
//        str += "Got [" + listsInfos.size() + "] lists ready to download =>" ;
//        
//        for (Long id : getListesIds()) {
//            str += "\n Liste \t ID:[" + id + "]" ;
//            str += "\t Date Modified:[" + getDateModified(id) + "]" ;
//        }
//        
//        return str += "\n" ;
//    }

    public ArrayList<ListeInfoFrontend> getListsInfos() {
        return listsInfos;
    }

    public ArrayList<ProduitInfo> getProduitsInfos() {
        return produitsInfos;
    }

    public void setListsInfos(ArrayList<ListeInfoFrontend> listsInfos) {
        this.listsInfos = listsInfos;
    }

    public void setProduitsInfos(ArrayList<ProduitInfo> produitsInfos) {
        this.produitsInfos = produitsInfos;
    }
    
    
    
    
    
    
    
    
}
