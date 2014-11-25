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
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.controllers.PasswordJpaController;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.entities.Password;
import org.dmb.trueprice.objects.RecoveredMember;
import org.dmb.trueprice.utils.internal.FileUtils;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.JaxbTraductor;
import org.dmb.trueprice.utils.internal.RecoverMailer;
import org.dmb.trueprice.utils.internal.ServletUtils;

public final class RecoverHandler {

    // STEP 1 : Demande de recover 
        //==> generer MDP + envoi passphrase par mail + write data to XML.
    private static final String CHAMP_MAIL_RECOVER = "recoverMail";
    private static final String CHAMP_PASS = "motdepasse";
    private static final String CHAMP_RECORD = "record";
        
    // STEP 2 : Reception du lien recu par mail
    public static final String MAIL_RECOVERED = "recoveredMail"; // Champ venant de l'email
    public static final String MAIL_SECURE_LINK = "securedLink"; // Champ venant de l'email
    public static final String MAIL_CHECKSUM = "checkSum"; // Champ venant de l'email
    
    // STEP 3 : Set new MDP
    private static final String CHAMP_EMAIL = "email";
    private static final String CHAMP_PSEUDO = "pseudo";    
//    private static final String CHAMP_PASS = "motdepasse";
    private static final String CHAMP_CONF = "confirmation";
    
    // OTHERS
    private static final Timestamp DATE = new Timestamp(System.currentTimeMillis());
    
    private final Path att_init_XmlFolder = Paths.get("c:" + File.separator + "TFE" + File.separator + "xml"  + File.separator + "recoveredMembers");
    
// Bouton pour acceder a la fenetre modale
    private static final String modalLauncher = 
            "<a href=\"#myModal\" role=\"button\" class=\"btn\" data-toggle=\"modal\">"
            + "<strong>  Account Recover </strong></a><br>" ;
    

    private RecoveredMember recoveredMember ;


    private static final Logger log
            = InitContextListener.getLogger( RecoverHandler.class);

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
    public RecoverHandler(MembreJpaController utilisateurDao, PasswordJpaController pwdDao) {
        this.utilisateurDao = utilisateurDao;
        this.pwdDao = pwdDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    /**
     * Creer le mdp temporaire, le fichier XML 
     * @param request
     * @return 
     */
    public Membre prepareRecover (HttpServletRequest request) {
        
        /* Récupération des champs du formulaire */
        String email = ServletUtils.getRequestAttrValue( request, CHAMP_MAIL_RECOVER );
//        String motDePasse = ServletUtils.getRequestAttrValue( request, CHAMP_PASS );
        String motDePasse = "";

//        Utilisateur utilisateur = new Utilisateur();
        Membre userFinal = null ;

        /* Validation du champ email. */
        try {
            ServletUtils.validEmailSyntax( email );
            userFinal = new Membre();
            userFinal.setMbMail(email);
        } catch ( Exception e ) {
            setErreur( CHAMP_MAIL_RECOVER, e.getMessage() );
        }
        
//        utilisateur.setMAIL(email );
         
        try {
//            Utilisateur uVerif  = userManager.findByMail(utilisateur.getMbMail());
            Membre uVerif = utilisateurDao.findByMail(email);
            if (uVerif == null) {   
                throw new Exception("Cette adresse ne semble pas être enregistrée <a href=\"../connexion\">S'enregistrer</a>");   
//            } else if (uVerif.getId() != userFinal.getId() ) {   
//                throw new Exception("The user is wrong or is not a member");   
            } else {
                userFinal = uVerif;
            }
        } catch ( Exception e ) {
            setErreur( CHAMP_MAIL_RECOVER, e.getMessage() );
        }
        
        Password passFinal = null;
        
        long oldId = 0 ;
        long newId = 0 ;
        String tempKey = "";
        
        /* Validation du champ mot de passe. */
        try {
            
            // On génère un mot de passe aléatoire, censé être retourné par l'utilisateur via mail.
            tempKey = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(512));
            
            log.info("Generated Temporary passphrase [" + tempKey + "]");
            // Pour ne pas réécrire une méthode, on envoye 2* le meme MDP
            ServletUtils.validPasswordSyntax(tempKey,tempKey);
            
            passFinal = new Password(tempKey);
            
            passFinal = pwdDao.create(passFinal);
            
            oldId = userFinal.getMbPassid();
            newId = passFinal.getMbPassid();
            
            log.info("Need to reset password with ID [" + oldId + "] for member [" 
                    + userFinal.getMbPseudo()+ " <=> " + userFinal.getMbMail() 
                    + "]\n New password is [" + passFinal.getMbPassid()+ " <=> " 
                    + passFinal.getPwdValue()+ "]\n We must send this by mail > ["
                    + tempKey +"]"
            );
//            
//            Password pVerif = pwdDao.findById(userFinal.getMbPassid());
//            
//            log.debug("Compare pass  with Pass ID from Membre [" + 
//                    userFinal.getMbPassid()
//                    + " >> \n >> \t[" + passFinal.getID() +"][" 
//                    + passFinal.getValue() + "] \n >> \t[" 
//                    + pVerif.getID() +"][" + pVerif.getValue() + "]"
//            );
//            // Est-ce le bon miot de passe ?
//            if (pwdDao.simulateEncryption(passFinal, pVerif)) {
//                passFinal = pVerif;
//                userFinal.setMbPassid(passFinal.getID());
//                // L'assignement est effectif ?
//                if (ServletUtils.validPasswordAssignment(passFinal, userFinal) ) {
//                    log.info("Acces granted for User [" + userFinal.getMbMail());
//                    log.debug("Received pass >> [" + passFinal.getID() +"][" + passFinal.getValue() + "]");
//                } else {
//                    throw new Exception("Wrong User or Password") ;
//                }
//            } else {
//                throw new Exception("Wrong User or Password") ;
//            }
                    
        } catch ( Exception e ) {
            setErreur( CHAMP_MAIL_RECOVER, e.getMessage() );
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


        if ( erreurs.isEmpty() ) {
            
            log.info("The recover is done with no errors. Start create entity Recovered an then marshall it ... ");
            
            recoveredMember = new RecoveredMember (
                String.valueOf(oldId),  // new Pass ID
                String.valueOf(newId),  // old Pass ID
                String.valueOf(newId + oldId),  // checksum value
                tempKey,                 // the random generated passphrase
                userFinal.getMbMail()  //.replace("@", "-")   // the Member's mail
            );
            
            log.info("Entity created. Create file on disk.");
            
            String filename = recoveredMember.getRecoveredMail();
            filename += "-" + recoveredMember.getCheckSum() ;
            filename += ".xml" ;
            
            try {
                            
                String fileFullPath = att_init_XmlFolder  + File.separator + filename ;

                FileUtils.createFile(att_init_XmlFolder.toString(), filename);
                
                JaxbTraductor.marshall(recoveredMember, fileFullPath);
                
                RecoverMailer.sendMail(
                        recoveredMember.getSecureLink(),
                        recoveredMember.getRecoveredMail(),
                        Integer.valueOf(recoveredMember.getCheckSum())
                );
                
                
            } catch (IOException ex) {
                setErreur(CHAMP_RECORD, "Impossible de creer le fichier de sauvegarde. > " + ex.getMessage());
                ex.printStackTrace();
            } catch (Exception ex) {
                setErreur(CHAMP_RECORD, "Impossible d'ecrire le fichier de sauvegarde. > " + ex.getMessage());
                ex.printStackTrace();
            }
            
        }
            
        /* Initialisation du résultat global de la validation. */
        if ( erreurs.isEmpty() ) {
            resultat = "Un email vous a été envoyé.<br>"
                    + "N'oubliez pas de vérifier votre dossier Spam "
                    + "si le mail n'apparaît pas dans une dizaine de minutes.";
        } else {
            resultat = "Une erreur est survenue, veuillez réessayer.";
        }

        return userFinal;
    }

    /**
     * Recuperer les infos du formulaire envoye a partir du mail, 
     * verifier et si ok proposer new mdp.
     * @param request
     * @return 
     */
    public Membre processMail (HttpServletRequest request) {
        
        /* Récupération des champs du formulaire */
        String email = ServletUtils.getRequestAttrValue( request, MAIL_RECOVERED );
//        String motDePasse = ServletUtils.getRequestAttrValue( request, CHAMP_PASS );
        String checkSum = ServletUtils.getRequestAttrValue(request, MAIL_CHECKSUM);
        String secureLink = ServletUtils.getRequestAttrValue(request, MAIL_SECURE_LINK);

//        Utilisateur utilisateur = new Utilisateur();
        Membre userFinal = null ;

        /* Validation du champ email. */
        try {
            ServletUtils.validEmailSyntax( email );
            userFinal = new Membre();
            userFinal.setMbMail(email);
        } catch ( Exception e ) {
            setErreur( MAIL_RECOVERED, e.getMessage() );
        }
        
//        utilisateur.setMAIL(email );
         
        try {
//            Utilisateur uVerif  = userManager.findByMail(utilisateur.getMbMail());
            Membre uVerif = utilisateurDao.findByMail(email);
            if (uVerif == null) {   
                throw new Exception("Cette adresse email ne semble pas être enregistrée <a href=\"../connexion\">S'enregistrer</a>");   
//            } else if (uVerif.getId() != userFinal.getId() ) {   
//                throw new Exception("The user is wrong or is not a member");   
            } else {
                userFinal = uVerif;
            }
        } catch ( Exception e ) {
            setErreur( CHAMP_MAIL_RECOVER, e.getMessage() );
        }
        
        /*
        Vérification de la PRESENCE du checksum
        */
        try {
            checkSum = ServletUtils.getRequestAttrValue(request, MAIL_CHECKSUM);
            
//            Integer.parseInt(checkSum);
            
        } catch (Exception e) {
            setErreur(MAIL_CHECKSUM, "Bad parameter value.");
            log.warn("Error with " + MAIL_CHECKSUM.toString()
                    + " because > " + e.getMessage()
            );
            
        }
        /*
        Vérification de la PRESENCE du secureLink
        */
        try {
            secureLink = ServletUtils.getRequestAttrValue(request, MAIL_SECURE_LINK);
        } catch (Exception e) {
            setErreur(MAIL_SECURE_LINK, "Bad parameter value.");
            log.warn("Error with " + MAIL_SECURE_LINK.toString()
                    + " because > " + e.getMessage()
            );
        }
        
        
        log.info("Got all params. Get file & unmarshall.");
        
        RecoveredMember fileMember = null;

        String filename = email;
        filename += "-" + checkSum ;
        filename += ".xml" ;
            
            try {
                            
                String fileFullPath = att_init_XmlFolder  + File.separator + filename ;

//                FileUtils.createFile(att_init_XmlFolder.toString(), filename);
                
                log.info("Search data on file : " + fileFullPath);
                
                fileMember = JaxbTraductor.unmarshall(fileFullPath);
                
            } catch (Exception ex) {
                setErreur(CHAMP_RECORD, "Impossible de lire le fichier de sauvegarde. > " + ex.getMessage());
                ex.printStackTrace();
            }


   
        if (erreurs.isEmpty()) {            
        
        log.info("Read file OK. Verif given secureLink + checksum + consistency check on passwords and time elapsed.");
        log.debug("Got values from file : "
                + "\n mail = " + fileMember.getRecoveredMail()
                + "\n old ID = " + fileMember.getOldId()
                + "\n new ID = " + fileMember.getNewId()
                + "\n checksum = " + fileMember.getCheckSum()
                + "\n secure Link = " + fileMember.getSecureLink()
        );
        
        Password oldPass ;
        Password newPass = null  ;   
        
        /* Validation des champs de sécurité. */
        try {            
            
                      

            
            oldPass = pwdDao.findById(Long.valueOf(fileMember.getOldId()));
            newPass = pwdDao.findById(Long.valueOf(fileMember.getNewId()));
            
            if (oldPass != null ) {
                if (newPass != null) {
                  log.info("Passwords exists. Need to change Member's password ID  from [" + oldPass.getMbPassid() + "] for member [" 
                        + userFinal.getMbPseudo()+ " <=> " + userFinal.getMbMail() 
                        + "]\n New password is [" + oldPass.getMbPassid()+ "]."
                    );
                } else {
                    log.error("Could not find password with ID [" + fileMember.getNewId() + "]");
                    setErreur (MAIL_CHECKSUM , "Could not find password with ID [" + fileMember.getNewId() + "]");
                }
            } else {
                log.error("Could not find password with ID [" + fileMember.getOldId() + "]");
                setErreur (MAIL_CHECKSUM , "Could not find password with ID [" + fileMember.getOldId() + "]");
            }
            
            /*
                VERIFICATION SECURE LINK is the right passphrase
            */
            // on vérifie que le secure link venant du mail 
            // est bien la passphrase correspondant au newPass généré AVANT l'envoi du mail.
            if (pwdDao.simulateEncryption( 
                    new Password(secureLink) ,  // la passphrase (== le MDP en clair)
                    newPass                     // la valeur stockee (== le MDP crypté)
            )) {
                
                // C'est le bon mot de passe on update le membre avec l'ID du newPass
                log.info("Given secureLink is the right one, continue by showing view to set a new Password.");
                
            } else  {
                throw  new Exception("Given secureLink is NOT the right one, cancel recover + delete the generated password.");
            }
            
            /*
                VERIFICATION CHECKSUM == oldPassID + newPassID 
                oldPassID + newPassID  =
                    oldId + newId from xml file
                OR  
                    oldId from DB Member's PassId
                    + 
                    newId from DB Pass found with secureLink Request
            */
            
            Long oldID = Long.parseLong(fileMember.getOldId());
            Long newID = Long.parseLong(fileMember.getNewId());
            Long sum = Long.parseLong(fileMember.getCheckSum());
            
            // On recupere en LONG valeurs d'ID (vieux et new) + somme
            if (    // Verifie que le contenu du fichier est valable 
                    (sum.compareTo(
                            newID + oldID
                    ) == 0) 
                    // Verifie que les passwords trouves dans la db donne bien le meme resultat
                    && (sum.compareTo(
                            oldPass.getMbPassid() + newPass.getMbPassid()
                    ) == 0)
                ) {
                
                // C'est le bon mot de passe on update le membre avec l'ID du newPass
                log.info("Given checkSum is the right one, continue by showing view to set a new Password ");
            } else  {
                throw  new Exception("Given checkSum is NOT the right one. Cancel recover + delete old password.");
            }
                    
        } catch ( Exception e ) {
            e.printStackTrace();
            setErreur(CHAMP_RECORD, "Bad parameter value.");
            log.warn("Error with " + MAIL_CHECKSUM.toString()
                    + " because > " + e.getMessage()
            );
            setErreur(MAIL_CHECKSUM, "Bad parameter value.");
            log.warn("Error with " + MAIL_CHECKSUM.toString()
                    + " because > " + e.getMessage()
            );
            setErreur(MAIL_SECURE_LINK, "Bad parameter value.");
            log.warn("Error with " + MAIL_SECURE_LINK.toString()
                    + " because > " + e.getMessage()
            ); 
//            if (newPass.getMbPassid() != 0) {
//                try {
//                    removePassword(newPass.getMbPassid());
//                } catch (NonexistentEntityException | NullPointerException ex) {
//                    log.error("Could not delete generated passwaord because > " + ex.getMessage());
//                    ex.printStackTrace();
//                }
//            }
                
        }
        
       } 
            

        
        /**
         * Now rename File withour checkSum for next step.
         */
        if ( erreurs.isEmpty() ) {
            
            log.info("The recover is done with no errors. Start create entity Recovered an then marshall it ... ");
            
//            RecoveredMember fileMember = null;

            filename = userFinal.getMbMail();
            filename += "-" + checkSum ;
            filename += ".xml" ;    
            
            String newName = userFinal.getMbMail() + ".xml" ;

            
            try {
                            
                String fileFullPath = att_init_XmlFolder  + File.separator + filename ;

                FileUtils.renameFile(fileFullPath, fileFullPath.replace(filename, newName));
                
                log.info("File successfully renamed to [" + newName + "]");
                
            } catch (IOException ex) {
                setErreur(CHAMP_RECORD, "Impossible de creer le fichier de sauvegarde. > " + ex.getMessage());
                ex.printStackTrace();
            } catch (Exception ex) {
                setErreur(CHAMP_RECORD, "Impossible d'ecrire le fichier de sauvegarde. > " + ex.getMessage());
                ex.printStackTrace();
            }
            
        }        
        
        
        /* Initialisation du résultat global de la validation. */
        if ( erreurs.isEmpty() ) {
            resultat = "Message envoyé.";
                    
        } else {
            resultat = "Une erreur est survenue, veuillez réessayer.";
            userFinal = null;
            
        }

        return userFinal;
    }

    public boolean proceedRecover (HttpServletRequest request) {
    
        String email = ServletUtils.getRequestAttrValue(request, CHAMP_EMAIL);
        String motDePasse = ServletUtils.getRequestAttrValue(request, CHAMP_PASS);
        String confirmation = ServletUtils.getRequestAttrValue(request, CHAMP_CONF);

        Membre utilisateur = new Membre();

        /* Validation du champ email. */
        try {
            ServletUtils.validEmailSyntax( email );
            utilisateur = new Membre();
            utilisateur.setMbMail(email);
        } catch ( Exception e ) {
            setErreur( MAIL_RECOVERED, e.getMessage() );
        }
        
//        utilisateur.setMAIL(email );
         
        try {
//            Utilisateur uVerif  = userManager.findByMail(utilisateur.getMbMail());
            Membre uVerif = utilisateurDao.findByMail(email);
            if (uVerif == null) {   
                throw new Exception("Cette adresse email ne semble pas être enregistrée <a href=\"../connexion\">S'enregistrer</a>");   
//            } else if (uVerif.getId() != userFinal.getId() ) {   
//                throw new Exception("The user is wrong or is not a member");   
            } else {
                utilisateur = uVerif;
            }
        } catch ( Exception e ) {
            setErreur( CHAMP_MAIL_RECOVER, e.getMessage() );
        }
                
        
        // VALID PASSWORD
        Password pwd = new Password();
        try {
            ServletUtils.validPasswordSyntax(motDePasse, confirmation);
//        pwd = new Password(confirmation);
            pwd.setPwdValue(confirmation);

            log.info("Password Value accepted > "
                    + "Value=[" + pwd.getPwdValue()+ "]"
            );
        } catch (Exception e) {
            setErreur(CHAMP_PASS, e.getMessage());
            setErreur(CHAMP_CONF, null);
            log.error(e.getMessage());
        }

        log.info("Find all params. Get file & unmarshall.");
        
        RecoveredMember fileMember = null;

        String filename = utilisateur.getMbMail();
//        filename += "-" + utilisateur. c
        filename += ".xml" ;
            
            try {
                            
                String fileFullPath = att_init_XmlFolder  + File.separator + filename ;
                
                log.info("Search data on file : " + fileFullPath);
                
                fileMember = JaxbTraductor.unmarshall(fileFullPath);
                
            } catch (Exception ex) {
                setErreur(CHAMP_RECORD, "Impossible de lire le fichier de sauvegarde. > " + ex.getMessage());
                ex.printStackTrace();
            }


   
        if (erreurs.isEmpty()) {            
        
        log.info("Read file OK. Verif given secureLink + checksum + consistency check on passwords and time elapsed.");
        log.debug("Got values from file : "
                + "\n mail = " + fileMember.getRecoveredMail()
                + "\n old ID = " + fileMember.getOldId()
                + "\n new ID = " + fileMember.getNewId()
                + "\n checksum = " + fileMember.getCheckSum()
                + "\n secure Link = " + fileMember.getSecureLink()
        );
                          
        
        // IF no error => Create Password, Update Member's password's ID
        if (erreurs.isEmpty()) {
            
            Long oldKey = utilisateur.getMbPassid();
            Password oldPass = pwdDao.findById(oldKey);            
            
            Long tempKey = Long.valueOf(fileMember.getNewId());
            Password tempPass = pwdDao.findById(oldKey);            
        
        /**
         * Create new Password
         */

            try {
                pwd = pwdDao.create(pwd);
                log.info("Created new user's password :\n" + pwd.toString());
            } catch (Exception e) {
                log.error("Could not persist new password because > " + e.getMessage());
                setErreur(CHAMP_RECORD, "Une erreur interne est survenue, veuillez réessayer.");
            }
            
            if (pwd.getMbPassid() == null) {
                log.info("ID was null, try to find it.");
                pwd = pwdDao.findByValue(pwd.getPwdValue());
            }
            if (pwd.getMbPassid() != null) {
                
                Long newKey = pwd.getMbPassid();
                
                // First Edit user to be able to remove oldKey
                utilisateur.setMbPassid(newKey);
                
                try {
                    log.info("update Member Pass ID");
                    utilisateurDao.edit(utilisateur);
                } catch (Exception e) {
                    log.error("Could not update member with new password  ID because > " + e.getMessage());
                    setErreur(CHAMP_RECORD, "Une erreur interne est survenue, veuillez réessayer.");
                }  
                
                // remove old Password
                try {
                    log.info("delete old password with key [" + Integer.valueOf(oldPass.getMbPassid().toString()) + "]");
                    pwdDao.destroy(Integer.valueOf(oldPass.getMbPassid().toString())); // == oldKey
                } catch (Exception e) {
                    log.error("Could not delete old password with ID [" + oldKey + "] because > " + e.getMessage());
                    setErreur(CHAMP_RECORD, "Une erreur interne est survenue, veuillez réessayer.");
                }  
                
                pwdDao.flush();
                
                // remove temp Password
                try {
                    log.info("delete temp password with key [" + Integer.valueOf(tempPass.getMbPassid().toString()) + "]");
                    pwdDao.destroy(Integer.valueOf(tempPass.getMbPassid().toString())); //== tempkey
                } catch (Exception e) {
                    log.error("Could not delete temp password with ID [" + tempKey + "] because > " + e.getMessage());
                    setErreur(CHAMP_RECORD, "Une erreur interne est survenue, veuillez réessayer.");
                    e.printStackTrace();
                }  
                
                // delete XML file
                try {
                    String fileFullPath = att_init_XmlFolder  + File.separator + filename ;
                    log.info("delete XML with name [" +  fileFullPath + "]");
                    FileUtils.deleteFile(fileFullPath);
                } catch (Exception e) {
                    log.error("Could not XML file name [" + filename + "] because > " + e.getMessage());
                    setErreur(CHAMP_RECORD,"Une erreur interne est survenue, veuillez réessayer.");
                }

            } else {
                log.error("ID is still null .....................");
            }
            
        } else {
            log.error("Error append, redirect to recover with error message");
        }

        
        
        
        if (erreurs.isEmpty()) {
            resultat = "Succès de l'inscription.";
        } else {
            resultat = "Échec de l'inscription.";
        }        
        
       
    } 
        return false;
}    
    
    private boolean validationEmail(String email) throws Exception {
        if (email != null) {
            if (!ServletUtils.validEmailSyntax(email)) {
                throw new Exception("Merci de saisir une adresse mail valide.");
            } else {
                try {
                    Membre m = utilisateurDao.findByMail(email);
                    if (m != null) {
                        
                    }
                } catch (NoResultException e) {
                    log.info("Email [" + email + "] not yet registered. Continue process ...");
                    return true;
                }
            }
        } else {
            throw new Exception("Merci de saisir une adresse mail.");
        }
        return false;
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setErreur(String champ, String message) {
        erreurs.put(champ, message);
    }


//    private void removePassword (Long passId) throws NonexistentEntityException {
//                pwdDao.destroy(Integer.valueOf(passId.toString()));
//}

    
    
}
