/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.objects;

import java.util.ArrayList;
import org.dmb.trueprice.utils.internal.ServletUtils;

/**
 *
 * @author Guitch
 */
public class SyncInitResponseFrontend extends SyncInitResponse {

    public SyncInitResponseFrontend(ArrayList<AvailableList> listes) {
        super(listes);
    }
    
    public String getTimeElapsed(int position){
        // Recuperer la date de modification
        String str = availableLists.get(position).getDate();
        // Calculer la periode ecoulée depuis cette date
        str = ServletUtils.getFormattedTimeElapsed(str);        
        return (str == null ? "" : str);
    }
    
//    @Override
//    public String toString() {
//        String str = "\n" ;
//        
//        str += "Got [" + availableLists.size() + "] lists ready to download =>" ;
//        
//        for (Long id : getListesIds()) {
//            str += "\n Liste \t ID:[" + id + "]" ;
//            str += "\t Date Modified:[" + getDateModified(id) + "]" ;
//            str += "\t ==> Period:[" + getTimeElapsed(id) + "]" ;
//        }
//        
//        return str += "\n" ;
//    }


    
    
    
}
