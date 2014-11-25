package org.dmb.trueprice.handlers;

/**
 *
 * @author Work.In.Progress
 */


import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.controllers.PasswordJpaController;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.entities.Password;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;

public final class ConnexionHandler extends BasicHandler {
    private static final String CHAMP_EMAIL  = "email";
    private static final String CHAMP_PASS   = "motdepasse";
    private static final String CHAMP_RECOVER = "recover";
//    private String              resultat;
//    private Map<String, String> erreurs      = new HashMap<String, String>();

    private static final Logger log 
            = InitContextListener.getLogger( ConnexionHandler.class) ;
    
    // Bouton pour acceder a la fenetre modale
    private static final String modalLauncher = 
            "<a href=\"#myModal\" role=\"button\" class=\"btn\" data-toggle=\"modal\">"
            + "<strong>  Account Recover </strong></a><br>" ;
    
    
    private MembreJpaController      userManager;
    private PasswordJpaController      pwdManager;
    
    
    public ConnexionHandler( MembreJpaController userManager, PasswordJpaController  pwdManager ) {
        this.userManager = userManager ;
        this.pwdManager = pwdManager ;
    }

    public ConnexionHandler(MembreJpaController userManager) {
        this.userManager = userManager;
    }
    
    

    public Membre connecterUtilisateur( HttpServletRequest request ) {
        /* Récupération des champs du formulaire */
        String email = ServletUtils.getRequestAttrValue( request, CHAMP_EMAIL );
        String motDePasse = ServletUtils.getRequestAttrValue( request, CHAMP_PASS );

        String resultat = "";
        
//        Utilisateur utilisateur = new Utilisateur();
        Membre userFinal = new Membre();
//        userFinal

        /* Validation du champ email. */
        try {
            ServletUtils.validEmailSyntax( email );
            
            userFinal.setMbMail(email);
        } catch ( Exception e ) {
            log.error("ERROR : EmailSyntaxValidation > " + e.getMessage());
            setErreur( CHAMP_EMAIL, e.getMessage() );
//            log.error(Arrays.toString(e.getStackTrace()));
        }
        
//        utilisateur.setMAIL(email );
         
        try {
//            Utilisateur uVerif  = userManager.findByMail(utilisateur.getMbMail());
            Membre uVerif = userManager.findByMail(email);
            if (uVerif == null) {   
                throw new Exception("Cannot find user");   
//            } else if (uVerif.getId() != userFinal.getId() ) {   
//                throw new Exception("The user is wrong or is not a member");   
            } else {
                userFinal = uVerif;
            }
        } catch ( Exception e ) {   
            log.error("ERROR : FindUserByMail > " + e.getMessage());
            setErreur( CHAMP_PASS, e.getMessage() );
//            log.error(Arrays.toString(e.getStackTrace()));
        }
        
        Password passFinal = null;
        
        /* Validation du champ mot de passe. */
        try {
            // Pour ne pas réécrire une méthode, on envoye 2* le meme MDP
            ServletUtils.validPasswordSyntax(motDePasse,motDePasse);
            
            passFinal = new Password(motDePasse);
            
            Password pVerif = pwdManager.findById(userFinal.getMbPassid());
            
//            log.debug("Compare pass  with Pass ID from Membre [" + 
//                    userFinal.getMbPassid()
//                    + " >> \n >> \t[" + passFinal.getMbPassid()+"][" 
//                    + passFinal.getPwdValue()+ "] \n >> \t[" 
//                    + pVerif.getMbPassid()+"][" + pVerif.getPwdValue()+ "]"
//            );
            
            // Est-ce le bon mot de passe ?
            Boolean b = null ;
            b = pwdManager.simulateEncryption(passFinal, pVerif);
            
            
            if (b != null & b.booleanValue() == Boolean.TRUE) {
                passFinal = pVerif;
                userFinal.setMbPassid(passFinal.getMbPassid());
                log.debug("Try validate assignment ... ");
                
            log.debug("Compare pass  with Pass ID from Membre [" + 
                    userFinal.getMbPassid()
                    + " >> \n >> \t[" + passFinal.getMbPassid()+"][" 
                    + passFinal.getPwdValue()+ "] \n >> \t[" 
                    + pVerif.getPwdValue()+"][" + pVerif.getPwdValue()+ "]"
                );                
                
                // L'assignement est effectif ?
                if (ServletUtils.validPasswordAssignment(passFinal, userFinal) ) {
                    log.info("Acces granted for User [" + userFinal.getMbMail());
                    log.debug("Received pass >> [" + passFinal.getMbPassid()
                            +"][" + passFinal.getPwdValue()+ "]");
                } else {
                    throw new Exception("Wrong User or Password Assignment") ;
                }
            } else {
                log.debug("Got final response : " + b);
                throw new Exception("Wrong User or Password") ;
            }
                    
        } catch ( Exception e ) {
            log.info("ERROR : Password > " + e.getMessage());
            setErreur( CHAMP_PASS, e.getMessage() );
//            log.debug(Arrays.toString(e.getStackTrace()));
        }
        

//        try {
//            pVerif = pwdManager.findByValue(passFinal.getValue());
//            if (pVerif == null) {   
//                throw new Exception("Password does not exist");   
//            } else  {
//                passFinal = pVerif;
//            }
//        } catch ( Exception e ) {
//            setErreur( CHAMP_PASS, e.getMessage() );
//        }


        /* Initialisation du résultat global de la validation. */
        if ( getErreurs().isEmpty() ) {
            resultat = "Succès de la connexion.";
        } else {
            resultat = "Échec de la connexion.";
            // Si l'email est correct mais pas le mot de passe => recover
            if ( ! getErreurs().containsKey(CHAMP_EMAIL) && getErreurs().containsKey(CHAMP_PASS) ) {
                setErreur(CHAMP_RECOVER, "show");
                String oldValue = getErreurs().get(CHAMP_PASS);
                String newValue = oldValue + modalLauncher ;
//                setErreur(CHAMP_PASS, oldValue + "  " + modalLauncher);
                getErreurs().remove(CHAMP_PASS);
                setErreur(CHAMP_PASS, newValue);
            }
        }

        setResultat(resultat);
        
        return userFinal;
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