/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.handlers;

import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.ListesInfoJpaController;
import org.dmb.trueprice.controllers.ListesJpaController;
import org.dmb.trueprice.controllers.ListesResultJpaController;
import org.dmb.trueprice.controllers.ListesStatsJpaController;
import org.dmb.trueprice.controllers.ProduitInfoJpaController;
import org.dmb.trueprice.controllers.ProduitListeJpaController;
import org.dmb.trueprice.controllers.ProduitStatsJpaController;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guitch
 */
public class ListeResultHandler extends BasicHandler {
    
    private static final Logger log = InitContextListener.getLogger(ListeResultHandler.class) ;    
    
    private ListesInfoJpaController infoManager ;
    private ListesResultJpaController resultManager ;
    private ListesStatsJpaController statsManager ;
    private ListesJpaController baseManager ;

    public ListeResultHandler(ListesInfoJpaController infoManager, ListesResultJpaController resultManager, ListesStatsJpaController statsManager, ListesJpaController baseManager) {
        this.infoManager = infoManager;
        this.resultManager = resultManager;
        this.statsManager = statsManager;
        this.baseManager = baseManager;
    }



    
    
    
    
    
    
/*  /////////////////////////  */    
/* REDEFINED FROM BASICHANDLER */
/*  /////////////////////////  */    
    
    /**
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     * @param champ
     * @param message 
     */
    @Override
    public void setErreur( String champ, String message ) {
        log.warn("\n==========\tINSERTING EXCEPTION for {Liste Result Handler} to [" + champ + "] because > " + message);
        super.setErreur(champ, message);
    }
    
    
}
