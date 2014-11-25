package org.dmb.trueprice.servlets;

/**
 *
 * @author Work.In.Progress
 */
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.controllers.PasswordJpaController;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.handlers.InscriptionHandler;
import org.dmb.trueprice.handlers.RecoverHandler;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;

@WebServlet (   urlPatterns = {"/inscription","/recover"} )
public class Inscription_servlet extends HttpServlet {
    
    private static final String FORM_ATT_EMAIL  = "email";
    private static final String FORM_ATT_NOM    = "nom";
    public static final String FORM_PARAM_CGV = "CGV";
    public static final String FORM_PARAM_MAIL_RECOVER = "recoverMail"; // Champ à partir du formulaire
    
    public static final String MAIL_RECOVERED = "recoveredMail"; // Champ venant de l'email
    public static final String MAIL_SECURE_LINK = "securedLink"; // Champ venant de l'email
    public static final String MAIL_CHECKSUM = "checkSum"; // Champ venant de l'email

    private static final String VIEW_ATT_USER = "utilisateur";
    private static final String VIEW_ATT_STEP = "step";
    private static final String VIEW_FORM = "form";
    private static final String VIEW_TARGET_INSCRIPTION = "/WEB-INF/inscription.jsp";
    private static final String VIEW_TARGET_RECOVER = "/WEB-INF/recover.jsp";
    private static final String VIEW_TARGET_SETNEWPASS = "/WEB-INF/recoverPassword.jsp";
        private static final Logger log 
            = InitContextListener.getLogger( Inscription_servlet.class) ;
    
    
    //private Utilisateur user ;
        // Deviens : 
//    @EJB    
//    private UserManager userDao ;
    @EJB
    private PasswordJpaController pwdDao ;
    @EJB    
    private MembreJpaController userDao ;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nom;
        String mail;
        
        try {
            // Si recover
           if (request.getRequestURI().contains("/recover")) {
               
               log.info("Entered in Recover Mode");
               
               // si viens du formulaire
               mail = ServletUtils.getRequestAttrValue(request,FORM_PARAM_MAIL_RECOVER);
               if (mail != null) {
                   
                    log.info("Mail needing recover found [" + mail + "]");

                    RecoverHandler reseter = new RecoverHandler(userDao, pwdDao);
                    Membre m = reseter.prepareRecover(request);

                    request.setAttribute(VIEW_ATT_USER, m);
    
               
               }
               // sinon 
               else {
                   
                   mail = ServletUtils.getRequestAttrValue(request, MAIL_RECOVERED);
                   // si viens du mail => vérifier puis autoriser new MDP
                   if (mail != null) {
                   
//                        String checksum  = ServletUtils.getRequestAttrValue(request, MAIL_CHECKSUM);

//                        String keyword  = ServletUtils.getRequestAttrValue(request, MAIL_SECURE_LINK);

                        log.info("Member's Mail verifying recover found [" + mail + "]");


                        RecoverHandler reseter = new RecoverHandler(userDao, pwdDao);
                        Membre m = reseter.processMail(request);

                        request.setAttribute(VIEW_ATT_USER, m);

                        // Si tout s'est bie passé => view set new password
                        if (m != null) {
                            request.setAttribute(VIEW_ATT_STEP, "step");
                        }                   
                        
                        
                   }
                   // alors viens du formulaire pour mettre un new MDP
                   else {
                       
                       // Deplace dans POST
                       
                   }
                   
               }
               
               this.getServletContext().getRequestDispatcher(VIEW_TARGET_RECOVER).forward(request, response);
               
           // Si inscription
           } else {
            
                    // Verif CGV is checked
                    if (request.getParameter(FORM_PARAM_CGV) == null) {

                    }
                    // Get mail & pseudo from incoming request
                    nom = ServletUtils.getRequestAttrValue(request, "pseudo");
                    mail = ServletUtils.getRequestAttrValue(request, "mailSignin");

                    // Transport mail & pseudo
                    request.setAttribute("nom", nom);
                    request.setAttribute("email", mail);

                    log.debug(" || Data transferred !...?? > Nom=" + nom + " mail=" + mail);

                    /* Affichage de la page d'inscription */
                    this.getServletContext().getRequestDispatcher(VIEW_TARGET_INSCRIPTION).forward(request, response);
            
           }
        } catch (Exception e) {
            log.error("Error transferring User data from JSP_Connexion to JSP_Inscription > " , e);
        } 
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
            // Si recover (set new MDP)
           if (request.getRequestURI().contains("/recover")) { 
               
               log.info("Received POST request to finalize recover");
               
                RecoverHandler reseter = new RecoverHandler(userDao, pwdDao);
                Boolean done = reseter.proceedRecover(request);
               
               
           } 
           // Sinon POST de l'inscription
           else {
               
                /* Préparation de l'objet formulaire */
                  //InscriptionForm form = new InscriptionHandler();
                      // Deviens :         
                  InscriptionHandler form = new InscriptionHandler(userDao, pwdDao);
          //        InscriptionHandler form = new InscriptionHandler(userDao);


                  /* Appel au traitement et à la validation de la requête, et récupération du bean en résultant */

          //        Utilisateur utilisateur = null;


                  Membre utilisateur = null;

                  try {
                      utilisateur = form.inscrireUtilisateur(request);
                  } catch ( NullPointerException e) {
                      e.printStackTrace();
                      log.error("Error retrieving User from InscriptionForm > " , e);
                  }


          //        UserManager managUser = new UserManager();
          //        managUser.create(utilisateur);


                  /* Stockage du formulaire et du bean dans l'objet request */
                  request.setAttribute(VIEW_FORM, form);
                  request.setAttribute(VIEW_ATT_USER, utilisateur);

                  this.getServletContext().getRequestDispatcher(VIEW_TARGET_INSCRIPTION).forward(request, response);              
           }        
        
     
    }
    
    

}
