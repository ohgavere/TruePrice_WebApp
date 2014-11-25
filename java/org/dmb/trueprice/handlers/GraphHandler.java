package org.dmb.trueprice.handlers;

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
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.utils.internal.InitContextListener;



@WebServlet (   urlPatterns = {"/graph/container","/grapg/query"} )
public class GraphHandler extends HttpServlet {
    
    
    
    public static final String PATH_GRAPH = "/graph";
    public static final String PATH_GRAPH_CONTAINER = "/graph/container";
    public static final String PATH_GRAPH_QUERY = "/graph/query";
    
    
    public static final String ATT_DENY_ERROR = "error";
    public static final String ATT_DENY_MESSAGE = "message";
    public static final String ATT_SESSION_USER_ID = "sessionUtilisateur";
    public static final String ATT_USER = "utilisateur";    
    private static final String FORM_ATT_EMAIL  = "email";
    private static final String FORM_ATT_NOM    = "nom";
    public static final String FORM_PARAM_CGV = "CGV";

    private static final String VIEW_ATT_USER = "utilisateur";
    private static final String VIEW_FORM = "form";
    private static final String VIEW_TARGET_DENIED = "/WEB-INF/accessDenied.jsp";
    private static final String VIEW_TARGET_GRAPH_OVERVIEW = "/WEB-INF/graphContainer.jsp";
    private static final String VIEW_TARGET_GRAPH_BRIEF = "/WEB-INF/graphContainer.jsp";
    private static final String VIEW_TARGET_GRAPH_CIRCLE = "/WEB-INF/graphContainer.jsp";
        private static final Logger log 
            = InitContextListener.getLogger( GraphHandler.class) ;
    
    
    //private Utilisateur user ;
        // Deviens : 
//    @EJB    
//    private UserManager userDao ;
//    @EJB
//    private PasswordManager pwdDao ;
    @EJB    
    private MembreJpaController userDao ;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                
        HttpSession session = request.getSession();
        
        if (session != null) {
            // Sur de l'avoir car filtré a l'entrée
            Membre user = (Membre) session.getAttribute(ATT_SESSION_USER_ID);
            if (user != null) {    
                // init graph
                // laucnh graph
                // show graph
                String next = request.getContextPath() ;
                // Si graph overview
                if (next.endsWith(PATH_GRAPH)) {
                    log.debug("GraphHandler -- Asking for graph overview");
                    this.getServletContext().getRequestDispatcher(VIEW_TARGET_GRAPH_OVERVIEW).forward(request, response);
                } else if (next.startsWith(PATH_GRAPH_CONTAINER)) {
                    log.debug("GraphHandler -- Asking for graph container");
String value = getValue(request, "type");
//                    switch (getValue(request, "type")) {
if (value.equals("brief")) {
//                        case "brief":
                            log.debug("GraphHandler -- Found graph type [" + getValue(request, "type") + "]");
                            this.getServletContext().getRequestDispatcher(VIEW_TARGET_GRAPH_BRIEF).forward(request, response);
//                            break;
//                        case "circle":
} else if (value.equals("circle")) {
                            log.debug("GraphHandler -- Found graph type [" + getValue(request, "type") + "]");
                            this.getServletContext().getRequestDispatcher(VIEW_TARGET_GRAPH_CIRCLE).forward(request, response);                            
//                            break;
//                        default:
} else {                            
                            log.debug("GraphHandler -- Did not find graph type. Redirect to overview.");
                            this.getServletContext().getRequestDispatcher(VIEW_TARGET_GRAPH_OVERVIEW).forward(request, response);
//                            break;
}                            
//                    }
                } else if (next.startsWith(PATH_GRAPH_QUERY)) {
                    this.getServletContext().getRequestDispatcher(VIEW_TARGET_GRAPH_BRIEF).forward(request, response);
                }
            
            
            } else {
                request.setAttribute(ATT_DENY_ERROR, "Can't find user shouldn't append ... ");
                request.setAttribute(ATT_DENY_MESSAGE, "Sorry ... We try to fix it ASAP ... =D");
                this.getServletContext().getRequestDispatcher(VIEW_TARGET_DENIED).forward(request, response);
            }
        }
         
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }
    

/*
 * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
 * sinon.
 */
private static String getValue( HttpServletRequest request, String nomChamp ) {
    String valeur = request.getParameter( nomChamp );
    if ( valeur == null || valeur.trim().length() == 0 ) {
        return null;
    } else {
        return valeur.trim();
    }
}    

    
    
}
