/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.handlers;

import org.apache.log4j.Logger;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guitch
 */
public final class AdminHandler extends BasicHandler {
    
    private static final Logger log = InitContextListener.getLogger(AdminHandler.class) ;
    
//  private final class AdminHandler extends BasicHandler {
        
//        private String resultat = new String();
//        private HashMap<String, String> erreurs = new HashMap<String, String>();        
//        /*
//        * Ajoute un message correspondant au champ spécifié à la map des erreurs.
//        */
//        @Override
//       public void setErreur( String champ, String message ) {
//           log.warn("==========\tINSERTING EXCEPTION > " + message);
//           if (erreurs.isEmpty()) {erreurs.put( champ, message );}
//       }
//        /*
//        * Ajoute un message correspondant au champ spécifié à la map des erreurs.
//        */
//       private void setResultat( String message ) {
//           log.warn("==========\tINSERTING EXCEPTION > " + message);
//           if ( ! resultat.isEmpty()) { resultat += "<br>" ;}
//           resultat += "<br>"+message ;
//       }
//
//        @Override
//       public HashMap<String, String> getErreurs() {
//            return erreurs;
//        }
    
       @Override
       public void clearForm(){
           clearErreurs();
           clearResultat();
       };

        @Override
        protected void clearErreurs() {
            super.clearErreurs(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected void clearResultat() {
            super.clearResultat(); //To change body of generated methods, choose Tools | Templates.
        }
    

           /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    @Override
    public void setErreur( String champ, String message ) {
        log.warn("\n==========\tINSERTING EXCEPTION to [" + champ + "] > " + message);
//        if (erreurs.isEmpty()) {
//            erreurs.put( champ, message );
//        }
        super.setErreur(champ, message);
    }



    }