/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.controllers;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.exceptions.NonexistentEntityException;
import org.dmb.trueprice.entities.Password;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.PasswordFactory;

@Singleton
public class PasswordJpaController implements Serializable {
    
    private static final PasswordFactory encrypter = new PasswordFactory();
    
        private static final Logger log 
            = InitContextListener.getLogger( PasswordJpaController.class) ;
    
//    private static final String PARAM_VALUE           = "value";
//    private static final String JPQL_SELECT_BY_VALUE =
//            "SELECT p FROM Password p WHERE p." + PARAM_VALUE + "=:" + PARAM_VALUE ;
    

        
        
    // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "TruePrice_PersistenceUnit" )
    private EntityManager       entManager;
    
    
    
    public boolean simulateEncryption (Password clearPass, Password hashedPass) throws Exception {
        
        try {
            boolean t = encrypter.validPassword(clearPass.getPwdValue(), hashedPass.getPwdValue());
            log.debug("Got resopnse : " + t);
            return t;
        } catch (InvalidKeySpecException ex) {
            log.warn("ERROR >> Password >> InvalidKeySpecException [" + ex.getMessage() + "]");
            throw new Exception("InvalidKeySpecException [" + ex.getMessage() + "]");
        } catch (NoSuchAlgorithmException ex) {
            log.warn("ERROR >> Password >> NoSuchAlgorithmException [" + ex.getMessage() + "]");
            throw new Exception("NoSuchAlgorithmException [" + ex.getMessage() + "]");
        }
//        return false;
    }
    
    // Enregistrement d'un nouvel utilisateur
    public Password create( Password pass ) /*throws Exception /* throws DAOException */ {
        
        Password pTest = new Password();
        try {
            log.debug("Password before encyprted verif : " + pass.toString());
            
             pTest = encrypter.createPassword(pass.getPwdValue());
            
            log.debug("Password after encyprted verif : " + pTest.toString());
            
            pTest.setPwdDtcreation(new Timestamp(System.currentTimeMillis()));
            
            log.debug("launch persist");
            entManager.persist( pTest );
            log.debug("Password persisted "
//                    + "[ID=" + pass.getID() + ", "
//                    + "[Value=" + pass.getPwdValue()+ "]"
            );
            
//            return null;
             return findByValue(pTest.getPwdValue());
            
        } catch ( EntityExistsException e ) {
            //TODO: Remove value é return only the ID
            log.warn("Password already exist, returning entity ID & value ");
            return findByValue(pTest.getPwdValue());
        } 
//         catch ( Exception e ) {   
//            log.error("ERROR => Creating password > " + e.getMessage());
//            
////            throw new Exception( e.getMessage()  );
//            e.printStackTrace();
//            return null;
//        }
       
    }

    /*
     * Trouver un Utilisateur par son ID
     */
    public Password findById (Long id) {
        Password pass = null;   
        try {
            pass = entManager.find(Password.class, id);
        } catch (Exception e) {
            log.warn("Error creating password >[" + e.getMessage() + "]");
        }
        return pass;
    }
    
    /*
     * Trouver un Password par sa valeur 
     */
    public Password findByValue( String value ) /* throws DAOException */ {
        Password p = null;
        Query requete = entManager.createNamedQuery("Password.findByPwdValue" );
        requete.setParameter( "pwdValue", value );
        try {
            p = (Password) requete.getSingleResult();
        } catch ( NoResultException e ) {
            e.printStackTrace();
            log.debug("No Password found with value [" + value + "].");
        } 
//        catch ( Exception e ) {
//            throw new DAOException( e );
//            e.printStackTrace();
//        }
        
        return p;
    }    
   
    
        public void destroy(Integer id) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Password pass;
            try {
                pass = entManager.getReference(Password.class, id);
                pass.getMbPassid();
                entManager.remove(pass);
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The password with id " + id + " no longer exists.", enfe);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("The password with id " + id + " no longer exists.", e);
            }
           
//            em.remove(category);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

        
    public void flush() {
    
        try {
            entManager.flush();
        } catch (PersistenceException e) {
            log.error("Could not flush persistence context >> " + e.getMessage());
            e.printStackTrace();
        }
        
    }        
        
}