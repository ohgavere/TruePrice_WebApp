/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.handlers;

import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.ProduitInfoJpaController;
import org.dmb.trueprice.controllers.ProduitListeJpaController;
import org.dmb.trueprice.controllers.ProduitStatsJpaController;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guitch
 */
public class ProduitStatsHandler extends BasicHandler {
    
    private static final Logger log = InitContextListener.getLogger(ProduitStatsHandler.class) ;    
    
    private ProduitListeJpaController baseManager ;
    private ProduitStatsJpaController statsManager ;
    private ProduitInfoJpaController infoManager ;

    public ProduitStatsHandler(ProduitListeJpaController baseManager, ProduitStatsJpaController statsManager, ProduitInfoJpaController infoManager) {
        this.baseManager = baseManager;
        this.statsManager = statsManager;
        this.infoManager = infoManager;
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
        log.warn("\n==========\tINSERTING EXCEPTION for {Produit Stat Handler} to [" + champ + "] because > " + message);
        super.setErreur(champ, message);
    }
    
    
}
