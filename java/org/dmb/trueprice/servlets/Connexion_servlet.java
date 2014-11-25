/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dmb.trueprice.servlets;

/**
 *
 * @author Work.In.Progress
 */
import java.io.IOException;
import java.util.ArrayList;
import javax.ejb.EJB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.openejb.server.httpd.util.HttpUtil;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.controllers.PasswordJpaController;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.handlers.ConnexionHandler;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

@WebServlet (   urlPatterns = {"/connexion"} )
public class Connexion_servlet extends HttpServlet {

    public static final String ATT_USER = "utilisateur";
    public static final String ATT_FORM = "form";
     private static final String ATT_MAIL  = "email";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
//    public static final String ATT_SESSION_USER_PSEUDO = "sessionUtilisateur_PSEUDO";
    public static final String ATT_SESSION_ADMIN = "sessionAdmin";
    public static final String VIEW_CONNEXION = "/WEB-INF/connexion.jsp";

    public static final String COOKIE_DERNIERE_CONNEXION = "derniereConnexion";
    public static final String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";
    public static final String ATT_INTERVALLE_CONNEXIONS = "intervalleConnexions";
    public static final String FORM_PARAM_MEMORY = "memoire";
    public static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 365; // 1 an

        private static final Logger log 
            = InitContextListener.getLogger( Connexion_servlet.class) ;
    
        
    @EJB
    private PasswordJpaController pwdManager ;
    @EJB
    private MembreJpaController  userManager ;
    
    /** 
     *  Liste des admin
     */
    private static final ArrayList<String> adminList = new ArrayList<String>();
    
    static {
        if (adminList.isEmpty()) {
            adminList.add("dedeur.alysson@gmail.com");
            adminList.add("demarbre.guillaume@gmail.com");
        }
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Tentative de récupération du cookie depuis la requête */
        String derniereConnexion = ServletUtils.getCookieValue(request, COOKIE_DERNIERE_CONNEXION);
        
        /* Si le cookie existe, alors calcul de la durée */
        if (derniereConnexion != null) {            
            String intervalleConnexions = ServletUtils.getFormattedTimeElapsed(derniereConnexion);
            request.setAttribute(ATT_INTERVALLE_CONNEXIONS, intervalleConnexions);
        }
        
        /* Affichage de la page de connexion */
        this.getServletContext().getRequestDispatcher(VIEW_CONNEXION).forward(request, response);
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        /* Préparation de l'objet formulaire */
        ConnexionHandler conManager = new ConnexionHandler(userManager, pwdManager);
        /* Traitement de la requête et récupération du bean en résultant */
        Membre user = conManager.connecterUtilisateur(request);

///////            
// LOGIN SUCCESS
        /**
         * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
         * Utilisateur à la session, sinon suppression du bean de la session.
         */
        if (conManager.getErreurs().isEmpty()) {
            
            /* Récupération de la session depuis la requête */
            HttpSession session = request.getSession(true);            
            
//            log.debug("Building user session");
            session.setAttribute(ATT_SESSION_USER, user);
//            session.setAttribute(ATT_SESSION_USER_PSEUDO, 
//                    StringEscapeUtils.escapeXml(
//                        user.getMbPseudo()
//                    )
//            );
//            log.debug("Added attr 'membre'");
            
            if (adminList.contains(user.getMbMail())) {
                // La valeur n'a pas d'importance, seule la présence de l'attribut est testée sur la jsp
                session.setAttribute(ATT_SESSION_ADMIN, "admin");    
                log.warn("User is Admin !");
            }            
//            log.debug("Session built");
            
//            session.setAttribute("testAttr", "Test Attr Value with whitespaces between words. Normal one so ... =P");
//            log.debug("Try to get Attr > " + session.getAttribute("testAttr"));
            
            /* Si et seulement si la case du formulaire est cochée ET login reussi */
            if (request.getParameter(FORM_PARAM_MEMORY) != null) {
                // Recupérer la date actuelle
                String dateDerniereConnexion = ServletUtils.getFormattedDateNow();
                /* Création du cookie, et ajout à la réponse HTTP */
                ServletUtils.setCookie(response, COOKIE_DERNIERE_CONNEXION, dateDerniereConnexion, COOKIE_MAX_AGE);
            } else {
                /* Demande de suppression du cookie du navigateur */
                ServletUtils.setCookie(response, COOKIE_DERNIERE_CONNEXION, "", 0);
            }
            
        }
        
///////            
// LOGIN FAILED
        else {
            
            String givenMail = ServletUtils.getRequestAttrValue(request, ATT_MAIL) ;
            
            if (  givenMail != null && ! givenMail.isEmpty()) {
                    user.setMbMail(givenMail);
            }
            
            request.setAttribute(ATT_USER, user);
            
///////            
// Add header to Android WEB-APP !!!
// Whitout this, android don't know login has failed !!!
//            
//            response.setStatus(200, "Authentication failed. Bad Value.");
            response.setHeader("TruePriceError", "403");
//            
///////
            
        }
        
        

        /* Stockage du formulaire et du bean dans l'objet request */
        request.setAttribute(ATT_FORM, conManager);
        request.setAttribute(ATT_USER, user);
        
        this.getServletContext().getRequestDispatcher(VIEW_CONNEXION).forward(request, response);
    }
}
