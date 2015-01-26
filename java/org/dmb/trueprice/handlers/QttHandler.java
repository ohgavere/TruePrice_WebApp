/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.handlers;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.ProduitListeJpaController;
import org.dmb.trueprice.controllers.QttDetailJpaController;
import org.dmb.trueprice.entities.QttDetail;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guitch
 */
public class QttHandler extends BasicHandler {
    
    private static final Logger log = InitContextListener.getLogger(QttHandler.class) ;    
    
    private ProduitListeJpaController pdtManager ;
    private QttDetailJpaController qttManager ;

    public QttHandler(ProduitListeJpaController pdtManager, QttDetailJpaController qttManager) {
        this.pdtManager = pdtManager;
        this.qttManager = qttManager;
    }
    
    
    public QttDetail getQuantityDetailsById (Integer Id) {
    
        QttDetail qttReturned = null ;
        
        if (Id != null && Id > 0) {
        
            try {
                return qttManager.findQttDetail(Id);
            } catch (Exception e) {
                log.error("Could not find the QttDetail with id [" + Id + "]");
                e.printStackTrace();
            }
        
        }
    
        return qttReturned ;
    }
    
    public QttDetail createRandomQtt() {
    
        String [] mesures = {"Kg","g","L","piece"} ;
        String [] values = {"1,2","250","1,5","3"} ;
        
        int rd          = RandomUtils.nextInt(3);
        String mesure   = mesures[rd] ;
        String value    = values[rd];
        
        QttDetail qtt = new QttDetail();
        qtt.setQttMesure(mesure);
        qtt.setQttQuantite(value);
        
        return qtt;
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
        log.warn("\n==========\tINSERTING EXCEPTION for {QttDeatil Handler} to [" + champ + "] because > " + message);
        super.setErreur(champ, message);
    }
    
    
}
