/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.handlers;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Guitch
 */
public abstract class BasicHandler {
    
    private String              resultat    = "";
    private Map<String, String> erreurs     = new HashMap<String, String>();
    
//        private static final Logger log 
//            = InitContextListener.getLogger( "BasicHandler.class") ;
    // If need to log, get logger from initcontext
        
       public void clearForm(){
           clearErreurs();
           clearResultat();
       };    
    
    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    public void setErreur( String champ, String message ) {
//        log.warn("\n==========\tINSERTING EXCEPTION to [" + champ + "] > " + message);
//        if (erreurs.isEmpty()) {
            erreurs.put( champ, message );
//        }
    }

    protected void clearResultat(){ resultat = "";}
    
    protected void clearErreurs(){erreurs = new HashMap<String, String>();}
    
    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
//        log.warn("==========\tResultat add [" + resultat + "]");
        if ( ! this.resultat.isEmpty()) {
            this.resultat += "<br>" ;
        }
            this.resultat += resultat ;
//        log.warn("==========\tResultat NEW VALUE IS > [" + this.resultat + "]");
    }
    
    public Map<String, String> getErreurs() {
        return erreurs;
    }
    
}
