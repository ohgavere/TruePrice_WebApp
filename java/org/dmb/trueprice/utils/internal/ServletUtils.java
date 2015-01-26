/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dmb.trueprice.utils.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.entities.Password;
import org.dmb.trueprice.objects.AvailableList;
import org.dmb.trueprice.objects.ListHeader;
import org.dmb.trueprice.objects.SyncInitResponse;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * @author Guiitch
 */
public abstract class ServletUtils {

    private static final Logger log = InitContextListener.getLogger( ServletUtils.class) ;    
    
    private static final String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";
    
    private static String SyncInitResponseFilename = "SyncInitResponseFilename";

            
    static {
    
        SyncInitResponseFilename = InitContextListener.getEnvEntryValue(SyncInitResponseFilename);
        
    }
         
         
    /**
     * Méthode utilitaire gérant la récupération de la valeur d'un cookie donné
     * depuis la requête HTTP.
     *
     * @param request
     * @param cookieName
     * @return null if no value in field
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie != null && cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Méthode utilitaire gérant la création d'un cookie et son ajout à la
     * réponse HTTP
     *
     * @param response
     * @param nom
     * @param valeur
     * @param maxAge
     */
    public static void setCookie(HttpServletResponse response, String nom, String valeur, int maxAge) {
        Cookie cookie = new Cookie(nom, valeur);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    
    
    

    /**
     * Méthode utilitaire qui retourne null ou le contenu du champ.
     *
     * @param request
     * @param field
     * @return null if no value in field named (nomChamp)
     */
    public static String getRequestAttrValue(HttpServletRequest request, String field) {
        String valeur = request.getParameter(field);
        if (valeur == null || valeur.trim().length() == 0) {
            return null;
        } else {
            return valeur;
        }
    }

    /**
     * Méthode utilitaire qui retourne null ou le contenu du champ.
     *
     * @param request
     * @param field
     * @return null if no value in field named (nomChamp)
     */
    public static Long getRequestAttrLongValue(HttpServletRequest request, String field) {
        String valeur = request.getParameter(field);
        if (valeur == null || valeur.trim().length() == 0) {
            return null;
        } else {
            try {
                return Long.parseLong(valeur);
            } catch (Exception e) {
                return null;                
            }
        }
    }
    
    
    

    /**
     * Méthode utilitaire qui retourne null ou le contenu du champ.
     *
     * @param session
     * @param field
     * @return null if no value in field named (nomChamp)
     */
    public static String getSessionAttrValue(HttpSession session, String field) {
        String valeur = session.getAttribute(field).toString();
        if (valeur == null || valeur.trim().length() == 0) {
            return null;
        } else {
            return valeur;
        }
    }
    
    public static Object getSessionAttrObject(HttpSession session, String field) {
        Object valeur = session.getAttribute(field);

            return valeur;

    }

    /**
     * Méthode utilitaire qui retourne un Membre si sa session est ouverte.
     *
     * @param session
     * @param field
     * @return
     */
//    public static Membre getUserFromSession(HttpSession session, String field) {
//        Object value = session.getAttribute(field);
//        try {
//            if (value == null || !(value instanceof Membre) || !value.getClass().getDeclaredField("pseudo").toString().isEmpty()) {
//                return null;
//            } else {
//                return (Membre) value;
//            }
//        } catch (NoSuchFieldException ex) {
//            log.warn(" Le champ ne semble pas etre disponible : Cause : " + ex.getMessage());
//        } catch (SecurityException ex) {
//            log.warn(" Can't find user Cause : " + ex.getMessage());
//        }
//        return null;
//    }
    
    
    

    /**
     * Valide l'adresse email saisie.
     *
     * @param email
     * @return
     * @throws Exception
     */
    public static boolean validEmailSyntax(String email) throws Exception {
        if (email == null || !email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")) {
            log.debug("Email does not match regex [" + email + "]");
            throw new Exception("Merci de saisir une adresse mail valide.");
        } else {
            log.debug("Regex matched");
            return true;
        }

    }
    
    /**
     * 
     * @param pseudo
     * @throws Exception 
     */
    public static boolean validPseudoSyntax (String pseudo) throws Exception {
        if (pseudo != null && pseudo.length() < 3) {
            throw new Exception("Le pseudo doit contenir au moins 3 caractères.");
        } else {
            return true;
        }
    }
    
    /**
     *
     * @param password The password to check
     * @param membre The user to check
     * @return True if m.getMbPassid() == p.getId()
     * @throws Exception
     */
    public static boolean validPasswordAssignment(Password password, Membre membre) throws Exception {
        if (membre == null || password == null || membre.getMbPassid() != password.getMbPassid()) {
            log.debug("Mem Pass ID [" + membre.getMbPassid() + "] < == ? > [" + password.getMbPassid()+ "]");
            throw new Exception("Wrong password");
        } else {
            return true;
        }

    }

    /**
     * Valide le mot de passe saisi.
     *
     * @param password
     * @param confirmation
     * @return
     * @throws Exception
     */
    public static boolean validPasswordSyntax(String password, String confirmation) throws Exception {
        if (password != null && confirmation != null) {
            // Longueur minimale
            if (password.length() < 6) {
                throw new Exception("Le mot de passe doit contenir au moins 3 caractères.");
            } // Longueur OK
            else {
                // Les password sont identiques ?
                if (!password.equals(confirmation)) {
                    throw new Exception("Les mots de passe sont différents, veuillez recommencer.");
                } else {
                    // Password is OK
                    return true;
                }
            }
        } else {
            throw new Exception("Merci de saisir et confirmer votre mot de passe.");
        }
    }
    
    
    
    
    /**
     * Renvoie la date actuelle formattee
     * @return La date actuelle formattee
     */
    public static String getFormattedDateNow(){
        DateTime dt = new DateTime();
        /* Formatage de la date et conversion en texte */
        DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT_DATE);
        String dateDerniereConnexion = dt.toString(formatter);    
        return dateDerniereConnexion;
    }
    
    /**
     * Renvoie la période de temps ecoulée depuis <param>fromDate</param>
     * en un String formatte en FR
     * @param fromDate
     * @return la période de temps ecoulée formattee en FR
     */
    public static String getFormattedTimeElapsed (String fromDate) {
            /* Récupération de la date courante */
            DateTime dtCourante = new DateTime();
            /* Récupération de la date présente dans le cookie */
            DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT_DATE);
            DateTime dtDerniereConnexion = formatter.parseDateTime(fromDate);
            /* Calcul de la durée de l'intervalle */
            Period periode = new Period(dtDerniereConnexion, dtCourante);
            /* Formatage de la durée de l'intervalle */
            PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                    .appendYears().appendSuffix(" an ", " ans ")
                    .appendMonths().appendSuffix(" mois ")
                    .appendDays().appendSuffix(" jour ", " jours ")
                    .appendHours().appendSuffix(" heure ", " heures ")
                    .appendMinutes().appendSuffix(" minute ", "minutes ")
                    .appendSeparator(" et ")
                    .appendSeconds().appendSuffix(" seconde ", " secondes ")
                    .toFormatter();

            String intervalleConnexions = periodFormatter.print(periode); 
            
            return intervalleConnexions ;
    }
    
    
    public static void addUpdateEntryToSyncInitResponse (String userMail, AvailableList liste){
        try {
            
//            SyncInitResponse initResp = GsonConverter.fromJsonInitResponse(new FileReader(new File(getPath(userMail))));
            SyncInitResponse initResp = getMemberInitResponse(userMail);
            
//            initResp.getAvailableLists().put(listeId, date);
//            initResp.getAvailableLists().add(liste);
            
            Long idToRemove = null ;
            int listeIndexToRemove  = -1 ;
            
            for (AvailableList loopListe : initResp.getAvailableLists()) {
                if (Long.compare(loopListe.getListeId() , liste.getListeId()) == 0 ) {
//                    initResp.getAvailableLists().remove(loopListe);
                    idToRemove = loopListe.getListeId();
                    listeIndexToRemove = initResp.getAvailableLists().indexOf(loopListe);
                    break;
                }
            }
            
            String strLog = "";
            
            if (idToRemove != null && listeIndexToRemove >= 0) {
            
                log.error("addUpdateEntryToSyncInitResponse : " 
                    + "\n\t Remove : [" + idToRemove + "] / NbPdt: " 
                        + (listeIndexToRemove == -1 ? "[NOT FOUND !!]" : initResp.getAvailableLists().get(listeIndexToRemove).getPdtCount())
                    + "\n\t Add    : [" + liste.getListeId() + "] / NbPdt: " + liste.getPdtCount()
                );

                AvailableList removed = initResp.getAvailableLists().remove(listeIndexToRemove);

                if (removed == null) {
                    strLog = "The List supposed to be removed was not found. Try to replace dates... Old value:[" 
                        + initResp.getAvailableLists().get(listeIndexToRemove).getDate() + "]" ;
                    initResp.getAvailableLists().get(listeIndexToRemove).setDate(liste.getDate());
                }
            
            } else {
                initResp.getAvailableLists().add(liste);
                strLog = "The List was added. Gonna write changes ...";
            }

            try {
//            byte[] bytes = GsonConverter.toJsonTree(initResp).getBytes();
                byte[] bytes = GsonConverter.toJson(initResp).getBytes();
            
                FileUtils.writeFile(SyncInitResponseFilename, bytes ,InitContextListener.getXmlMemberFullPath(userMail));
            
                strLog += " \t ... OK." ;
                
                log.info(strLog) ;
                
            } catch (Exception e) {
                strLog += "... ... Failed ... " + e.getMessage() ;
                log.error(strLog);
                e.printStackTrace();
            }
            
//            if (strLog != "") { log.info(strLog) ; }
            
        } catch (Exception e) {
            log.error("Could not write SyncInitResponse : \n" + e.getMessage());
        }
    } 
    
    public static void writeMemberSyncInitResponse(SyncInitResponse initResponse, String mbMail) throws Exception {

        String jsonData = null ;
        String filename = null ;
        String folder = null ;
        
        try {
//            jsonData = GsonConverter.toJsonTree(initResponse) ;
            jsonData = GsonConverter.toJson(initResponse) ;
            filename = SyncInitResponseFilename ;
            folder = InitContextListener.getXmlMemberFullPath(mbMail) ;
            FileUtils.writeFile(filename, jsonData.getBytes(), folder);
        } catch (IOException e) {
            log.error("Could not write file \t[" 
                + folder + "]" 
                + "[" + filename + "]"
                + "\nCONTENT:[" + jsonData + "]"
            );
            e.printStackTrace();
            throw new Exception("error :" + e.getMessage());
        }
        
    }
    
    private static void createMemberSyncInitResponse(String mbMail) throws IOException{
        
        String fileFullName = "" ;
        fileFullName = InitContextListener.getXmlMemberFullPath(mbMail) + File.separator + SyncInitResponseFilename ;
//        byte[] bytes = GsonConverter.toJsonTree(initResponse).getBytes();
         
        FileUtils.createFile(InitContextListener.getXmlMemberFullPath(mbMail), SyncInitResponseFilename);
        
        
        
    }
    
         
    public static SyncInitResponse getMemberInitResponse(String mbMail) {
        
        SyncInitResponse initResp = null ;
        String fileFullName = "" ;
        fileFullName = InitContextListener.getXmlMemberFullPath(mbMail) + File.separator + SyncInitResponseFilename ;
        
        // On cree le fichier si il n'existe pas
        if (Files.exists(Paths.get(fileFullName)) == false) {  
            log.info("File does not exist, try to create it");
            
            try {                
//                FileUtils.createFile(InitContextListener.getXmlMemberFullPath(mbMail), SyncInitResponseFilename);                
                createMemberSyncInitResponse(mbMail);                
            } catch (IOException ex) {
                log.error("Can't create Member SyncInitResponse File in it's folder : fullName:[" 
                        + fileFullName + "]" + "\n" + ex.getMessage() );            
            }
        }
        
        // Ensuite on le recupere et on le donne, meme null
        try {
            initResp = GsonConverter.fromJsonInitResponse(new FileReader(new File(fileFullName)));
        } catch (FileNotFoundException ex) {
            log.error("Can't get Member SyncInitResponse instance from file : \nAbsolutePath:[" 
                    +  fileFullName + "]" + "\n" + ex.getMessage()
            );
        }
        
        
//        return fileFullName ;
        return initResp ;
        
    }     
}
