/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dmb.trueprice.handlers;

/**
 *
 * @author Work.In.Progress
 */
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.controllers.PasswordJpaController;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.entities.Password;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;

public final class InscriptionHandler {

    private static final String CHAMP_EMAIL = "email";
    private static final String CHAMP_PASS = "motdepasse";
    private static final String CHAMP_CONF = "confirmation";
    private static final String CHAMP_NOM = "nom";
    private static final String CHAMP_PRENOM = "prenom";
    private static final String CHAMP_PSEUDO = "pseudo";
    private static final String CHAMP_RECOVER = "recover";
    private static final Timestamp date = new Timestamp(System.currentTimeMillis());
    
    
// Bouton pour acceder a la fenetre modale
    private static final String modalLauncher = 
            "<a href=\"#myModal\" role=\"button\" class=\"btn\" data-toggle=\"modal\">"
            + "<strong>  Account Recover </strong></a><br>" ;
    
// Composition de la fenetre modale
    private static final String modalHeader = 
            "<div id=\"myModal\" class=\"modal hide fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">" +
//            "<div class=\"modal hide fade\">\n" +
            "   <div class=\"modal-header\" style=\"padding: 10px 10px;border-bottom: 3px solid #eee;float: left;max-width: 100%; min-width: 460px;\">" +
            "    <button type=\"button\" class=\"close\" style=\"height: 30px;width: 30px;font-size: xx-large;float: right;right: 10px;position: absolute;\"" +
            " data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
            "    <h3>Réinitialiser un mot de passe</h3>" +
            "  </div>" ;
    
    
    private static String modalData = "";
    private static String modalBody = 
            "<br><div class=\"modal-body\">" +
            "   <form method=\"get\" action=\"recover\" class=\"recover\" id=\"recoverForm\">" +
            "       <fieldset class=\"recover\" id=\"recoverFieldset\">" +
            "           <label for=\"mail\" class=\"recover\">@ Email<span class=\"requis\">*</span></label>" +
            "           <br>" +
            "       <input type=\"email\" id=\"recoverMail\" class=\"recover\" name=\"mail\"" +
            "value=\"" + modalData + "\" size=\"20\" maxlength=\"60\" />" +
            "       </fieldset>" +
            "   </form>" +
            "</div>" ;
    
    // Bas de la fentere modale
    // Contiens les boutons d'actions possibles
        private static final String modalFooter =
            "  <div class=\"modal-footer\">" +
            "    <a href=\"#\" class=\"btn\" data-dismiss=\"modal\" aria-hidden=\"true\" " +
            ">Annuler et réessayer</a>" +
            "    <a href=\"<c:url value=\"/recover\"/>\" class=\"btn btn-primary\" style=\"float:right;\">Envoyer</a>" +
            "  </div>" +
            "</div>" 
        ;


    private static final Logger log
            = InitContextListener.getLogger( InscriptionHandler.class);

    private String resultat;
    private Map<String, String> erreurs = new HashMap <String, String>();

//    private UserManager      utilisateurDao;
    private PasswordJpaController pwdDao;
    private MembreJpaController utilisateurDao;
//    private Password pwd;

//    public InscriptionHandler( UserManager utilisateurDao, PasswordManager  pwdDao ) {
//        this.utilisateurDao = utilisateurDao;
//        this.pwdDao = pwdDao;
//    }
    public InscriptionHandler(MembreJpaController utilisateurDao, PasswordJpaController pwdDao) {
        this.utilisateurDao = utilisateurDao;
        this.pwdDao = pwdDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Membre inscrireUtilisateur(HttpServletRequest request) {
        
        String email = ServletUtils.getRequestAttrValue(request, CHAMP_EMAIL);
        String motDePasse = ServletUtils.getRequestAttrValue(request, CHAMP_PASS);
        String confirmation = ServletUtils.getRequestAttrValue(request, CHAMP_CONF);
        String nom = ServletUtils.getRequestAttrValue(request, CHAMP_NOM);
        String prenom = ServletUtils.getRequestAttrValue(request, CHAMP_PRENOM);
        String pseudo = ServletUtils.getRequestAttrValue(request, CHAMP_PSEUDO);

        Membre utilisateur = new Membre();

        // VALID EMAIL
        try {            
            validationEmail(email);
        } catch (Exception e) {
            log.debug("Email validation failed [" + email + "] > " + e.getMessage());
            log.debug(Arrays.toString(e.getStackTrace()));
            setErreur(CHAMP_EMAIL, e.getMessage());
            setErreur(CHAMP_RECOVER, "show");
        }
        utilisateur.setMbMail(email);

        // VALID PSEUDO
        try {
            validationPseudo(pseudo);
        } catch (Exception e) {
            log.debug("Pseudo validation failed > " + e.getMessage());
            setErreur(CHAMP_PSEUDO, e.getMessage());
        }
        utilisateur.setMbPseudo(pseudo);

        // VALID PRENOM 
        try {
            ServletUtils.validPseudoSyntax(prenom); 
        } catch (Exception e) {
            log.debug("Prenom validation failed > " + (e.getMessage()).replace("pseudo", "prenom"));
            setErreur(CHAMP_PRENOM, e.getMessage());
            log.debug(Arrays.toString(e.getStackTrace()));
        }        
            utilisateur.setMbPrenom(prenom);
        
        try {
            ServletUtils.validPseudoSyntax(nom); 
        } catch (Exception e) {
            log.debug("Nom validation failed > " + (e.getMessage()).replace("pseudo", "nom"));
            setErreur(CHAMP_NOM, e.getMessage());
            log.debug(Arrays.toString(e.getStackTrace()));
        }        
        // VALID NOM
            utilisateur.setMbNom(nom);
        
    
        // VALID PASSWORD
        Password pwd = new Password();
        try {
            ServletUtils.validPasswordSyntax(motDePasse, confirmation);
//        pwd = new Password(confirmation);
            pwd.setPwdValue(confirmation);

            log.debug("Password Value accepted > "
                    + "Value=[" + pwd.getPwdValue()+ "]"
            );
        } catch (Exception e) {
            setErreur(CHAMP_PASS, e.getMessage());
            setErreur(CHAMP_CONF, null);
            log.debug(Arrays.toString(e.getStackTrace()));
        }

        // IF no error => Create Password & Membre
        if (erreurs.isEmpty()) {

            // PERSIST Password
            pwd = pwdDao.create(pwd);
            log.info("Password persisted");

//        if (pwd == null) {
//            pwd = pwdDao.findByValue(confirmation);
//        }
//        try {
//            
//            pwd = null;
//            pwd = pwdDao.findByValue(confirmation);
//          
            // ASSIGN Password to Membre
            if (pwd.getMbPassid()!= null) {
//                log.debug("Password is not null =) > " + pwd.toString());
                utilisateur.setMbPassid(pwd.getMbPassid());
//                utilisateur.setPASS(pwd);
//                utilisateur.setPass(confirmation);
                log.info("Password assigned");
            }

//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error("Password assignment > " 
//                    + ex.getMessage() + "\n Cause of > "
//                    + ex.getCause().toString()
//            );
//            
//            setErreur( CHAMP_PASS, ex.getMessage() );
//            setErreur( CHAMP_CONF, null );
//        } 
            utilisateur.setMbDtcreation(
                    new Timestamp(System.currentTimeMillis()));

            log.warn(
                    "USER PERSIST >> START >>>");

            utilisateurDao.create(utilisateur);

            log.warn(
                    "USER PERSIST >> END >>>");

        } else {
            log.debug("Error append, redirect to inscription with error message");
        }

//    } catch ( Exception e ) {
//        log.debug(Arrays.toString(e.getStackTrace()));
//            log.error("Password creation > " 
//                    + e.getMessage() //+ "\n Cause of > "
//                    + e.getCause().toString()
//            );        
//        setErreur( CHAMP_PASS, e.getMessage() );
//        setErreur( CHAMP_CONF, null );
//    }
//    } catch (Exception e ) {
//            log.error("Password creation > "
//                + e.getMessage() //+ "\n Cause of > "
//        //                    + e.getCause().toString()
//        );
//        setErreur(CHAMP_PASS, e.getMessage());
//        setErreur(CHAMP_CONF, null);
//    }
        if (erreurs.isEmpty()) {
            resultat = "Succès de l'inscription.";
        } else {
            resultat = "Échec de l'inscription.";
        }

        return utilisateur;
    }


    private boolean validationEmail(String email) throws Exception {
        if (email != null) {
            if (!ServletUtils.validEmailSyntax(email)) {
                throw new Exception("Merci de saisir une adresse mail valide.");
            } else {
                try {
                    Membre m = utilisateurDao.findByMail(email);
                    if (m != null) {
                        // Ajouter l'erreur + lien vers fenetre modale pour 
                        // proposer un reset du password en donnant son mail
                        modalData = email;
                        throw new EntityExistsException(
                                "Cette adresse e-mail est déjà enregistrée.  "
                                + modalLauncher
//                                + modalHeader
//                                + modalBody
//                                + modalFooter
                        );
                    }
                } catch (NullPointerException | NoResultException e) {
                    log.debug("Email [" + email + "] not yet registered. Continue process ...");
                    return true;
                }
            }
        } else {
            throw new Exception("Merci de saisir une adresse mail.");
        }
        return false;
    }

    private boolean validationPseudo(String pseudo) throws Exception {
        if (pseudo != null) {
            if (!ServletUtils.validPseudoSyntax(pseudo)) {
                throw new Exception("Merci de saisir un pseudo valide.");
            } else {
                try {
                    Membre m = utilisateurDao.findByPseudo(pseudo);
                    if (m != null) {
                        // Ajouter l'erreur + lien vers fenetre modale pour 
                        // proposer un reset du password en donnant son mail
//                        modalData = pseudo;
                        throw new EntityExistsException(
                                "Ce pseudo est déjà enregistrée.  "
//                                + modalLauncher
//                                + modalHeader
//                                + modalBody
//                                + modalFooter
                        );
                    }
                } catch (NullPointerException | NoResultException e) {
                    log.debug("Pseudo [" + pseudo + "] not yet registered. Continue process ...");
                    return true;
                }
            }
        } else {
            throw new Exception("Merci de saisir un pseudo.");
        }
        return false;
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setErreur(String champ, String message) {
        erreurs.put(champ, message);
    }

}
