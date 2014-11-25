package org.dmb.trueprice.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;


/**
 *
 * @author Work.In.Progress
 */
public final class RestrictionFilter implements Filter {
                                                
    boolean isConnected ;
    boolean isWelcomePage ;    
    boolean sessionIsNotEmpty ;
    boolean isMemberPage ;
    
    boolean isRessourcePage ;
    boolean isPublicRessource ;
    boolean isPrivateRessource ;
    boolean isOwnedRessource ;
    
    boolean isApplicationRessource ;
    
    boolean isPulicPage ;    
    
//    FilterChain chain     ;
//    HttpServletRequest request ;
//    HttpServletResponse response ;    
    
    public static String URL_ACCES_ADMIN     = "URL_ACCES_ADMIN";    
    
    public static final String ACCES_ADMIN     = "/admin";
    public static final String ACCES_ACCEUIL     = "/";
    public static final String ACCES_PUBLIC     = "/accesPublic.jsp";
//    public static final String ACCES_RESTREINT  = "/members/";
    public static final String ACCES_CONNEXION  = "/connexion";
    public static final String ACCES_RECOVER  = "/recover";
    public static final String ACCES_GRAPH  = "/graph";
//    public static final String ACCES_GRAPH_CONTAINER  = "/graph/container";
//    public static final String ACCES_GRAPH_QUERY  = "/graph/query";
    public static final String ACCES_DECONNEXION  = "/deconnexion";
    public static final String ACCES_INSCRIPTION  = "/inscription";
    public static final String ACCES_PRODUITS  = "/produits";
//    public static final String ACCES_PRODUITS_LIST  = "/produits/list";
//    public static final String ACCES_PRODUITS_ADD  = "/produits/add";
//    public static final String ACCES_PRODUITS_MANAGE  = "/produits/manage";
    public static final String ACCES_DASHBOARD  = "/dashboard";
    public static final String ACCES_LISTES  = "/listes";

    public static final String ACCES_XML  = "/xml";                 // RESSOURCE
    public static final String ACCES_XML_PUBLIC  = "/xml/public";   // RESSOURCE + PUBLIC
    public static final String ACCES_XML_MEMBERS  = "/xml/members"; // RESSOURCE + PRIVATE
    public static final String ACCES_XML_RECOVER  = "/xml/recoveredMembers"; // RESSOURCE + APP
    
    
    public static final String ACCES_SYNC  = "/sync"; // SYNC base URI
    
    public static final String ACCES_REST  = "/rest"; // REST base URI
    public static final String ACCES_REST_LISTES  = "/listes"; // REST + LISTES
    
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ATT_SSL_SESSION_ID = "sslSessionId";
    
//    MyLogger lg = new MyLogger(this.getClass().getSimpleName());                
//    Logger logE = lg.getLoggerEJB();
             private static final Logger log 

            = InitContextListener.getLogger(RestrictionFilter.class) ;
    
    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        
        System.out.println("\t >>>\t Restriction filter --> init(FilterConfig config)");
        
//        config = 
                InitContextListener.getFilterConfig(config);

        
        /*  Filter CONFIG  */
        FilterConfig fltCfg = config;
                                
//            System.out.println("\t >>>\t SHOW INIT PARAMETERS of FILTER CONFIG");
//            Enumeration<String> eParam2 = fltCfg.getInitParameterNames();            
//            while (eParam2.hasMoreElements()) {
//                String paramName = eParam2.nextElement();
//                String paramValue = fltCfg.getInitParameter(paramName).toString();
//                
//                if (paramName.equals(URL_ACCES_ADMIN)
//                        || paramValue.equals("/admin")) {
//                    
//                    System.out.println("init param FltCfg [" 
//                            + (paramName == null ? "???" : paramName) 
//                            + "] > " 
//                            + (paramValue == null ? "???" : paramValue)
//                    );                    
//                    
//                }
//            }        
//        
//        /*  Servlet CONTEXT */        
//        ServletContext srvCtx = config.getServletContext();
//                    
//            System.out.println("\t >>>\t servlet context name IS > " + srvCtx.getServletContextName());
//            
//            System.out.println("\t >>>\t SHOW INIT PARAMETERS of SERVLET CONTEXT");
//            Enumeration<String> eParam = srvCtx.getInitParameterNames();            
//            while (eParam.hasMoreElements()) {
//                String paramName = eParam.nextElement();
//                String paramValue = srvCtx.getInitParameter(paramName).toString();
//                
//                if (paramName.equals(URL_ACCES_ADMIN)
//                        || paramValue.equals("/admin")) {
//                    
//                    System.out.println("init param srvCtxt [" 
//                            + (paramName == null ? "???" : paramName) 
//                            + "] > " 
//                            + (paramValue == null ? "???" : paramValue)
//                    );                    
//                    
//                }
//            }
//        
//            System.out.println("\n\t >>>\t SHOW ATTRIBUTES of SERVLET CONTEXT");
//            Enumeration<String> e = srvCtx.getAttributeNames();
//            while (e.hasMoreElements()) {
//                String attName = e.nextElement();
//                String attValue = srvCtx.getAttribute(attName).toString();
//                
//                if (attName.equals(URL_ACCES_ADMIN)
//                        || attValue.equals("/admin")) {
//                    
//                    System.out.println("attribute srvCtxt [" 
//                            + (attName == null ? "???" : attName) 
//                            + "] > " 
//                            + (attValue == null ? "???" : attValue)
//                    );                    
//                    
//                }
//            }
//
//        
            

        System.out.println("\n\t >>>\t TRY to get envEntryValues");
        
        System.out.println("\n Get " + URL_ACCES_ADMIN + " value > " + 
            InitContextListener.getEnvEntryValue(URL_ACCES_ADMIN)
        );
        
        URL_ACCES_ADMIN = InitContextListener.getEnvEntryValue(URL_ACCES_ADMIN);
        System.out.println("\n New value " + URL_ACCES_ADMIN + " value > " );
        
        System.out.println("\t >>>\t Restriction filter --> init(FilterConfig config) --> DONE.");
    }
    
    private String getUrlRedirectHttps(HttpServletRequest request){
//            log.info("\t>>>\t not secured, redirecting...");
            
            String fullUrl = "https://" 
                    + request.getServerName()
                    + ":" + request.getServerPort()
                    + request.getContextPath() 
//                    + request.getServletPath()
                    ;            
            log.info("Url redirect target ==>" + fullUrl);
            
                    if (request.getPathInfo() != null) {                        
                        log.info("Url path info added in url for redirect==> " + request.getPathInfo());                        
                        fullUrl += request.getPathInfo();
                    }
                    
            fullUrl += "/";
                    
            log.info("Url redirect target FINAL ==>" + fullUrl);            
            
//            response.sendRedirect(fullUrl);      
            return fullUrl;
    }
    
    private void manageConnectedUser(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
    
        // Check if redirection or specific                
        if (isWelcomePage ) {
//            log.info("Connected & welcome page. go to /dashboard)");
//                    response.sendRedirect( request.getContextPath() + ACCES_RESTREINT );       
            response.sendRedirect( request.getContextPath() + ACCES_DASHBOARD );       
        } else 

        if (isPulicPage) {
//            log.info("Connected, MEMBER page. doFilter()");
            chain.doFilter(request,response);
            
        } else if (isMemberPage) {
            
            if (isRessourcePage) {
                
                if (isOwnedRessource) {
                    chain.doFilter(request, response);
                } else {
                    log.info("You can't access this ressource[" + request.getPathInfo() + "]");     
                    
                    response.addHeader("Refresh", "5; " + request.getContextPath() +  ACCES_ACCEUIL);
                    
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Sorry, you can't access this resource. You will be redirected in few seconds ...");
                }
                
            } else {
                chain.doFilter(request, response);
            }
            
        }

        // Other checks ...

        else {
            log.error("No way found ...");
        }    
        
    }
    private void manageUnconnectedUser (HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{

        // Check if public page                                                 
        if (isPulicPage) {
//            log.info("Not connected, public page. doFilter()");
            chain.doFilter( request, response );
        } 
        else {
            if (isMemberPage) {
                log.info("Not connected, MEMBER page. redirect to connexion");
                response.sendRedirect( request.getContextPath() + ACCES_CONNEXION);
            }
        }
      
    }
    
    private void initVariables(HttpServletRequest request, HttpServletResponse response, HttpSession session, String requestedPath) {
    //            log.info("\t>>>\t secured, continue.");    
        
        isConnected = isUserConnected(request, null);

    //            boolean isWelcomePage ;
        
        

        if ((requestedPath.startsWith( "/") 
                & requestedPath.length()==1 
                & requestedPath.endsWith("/"))
            ) {
                isWelcomePage = true ;
    //                    log.info("\t>>>\t it is, continue.");
        } else {
                isWelcomePage = false;
    //                    log.info("\t>>>\t it is, continue.");     
                
        }
        
//        log.info("\n\t == Working for path : " + requestedPath);
        
        
        
    //            boolean sessionIsNotEmpty ;

        try {
            ServletUtils.getSessionAttrValue (session,ATT_SESSION_USER) ;
            sessionIsNotEmpty = true ;
//                    log.info("Session is not empty.");
        } catch (NullPointerException e) {
            sessionIsNotEmpty = false;
//                    log.info("session is empty.");
        }



    //            boolean isPulicPage ;

            if (isWelcomePage) {
                isPulicPage = true ;
            } else {
                isPulicPage = isPublicPage(request, requestedPath);
            }

    //            boolean isMemberPage ;

            if (isPulicPage) {
                isMemberPage = false ;
            } else {
                isMemberPage = isMemberPage(request, requestedPath);
            }
            
            isRessourcePage = isRessource(request, requestedPath);

            
            if (isRessourcePage) {
                
                /*
                    INIT RESSOURCES FLAGS
                */
                if (isPulicPage) {  // Ex. ACCES_XML_PUBLIC
                    isPublicRessource = true ;
                    isPrivateRessource = false ;
                    isApplicationRessource = false ;
                    isOwnedRessource = false ;
                } else if (isMemberPage) {  // Ex. ACCES_XML_MEMBERS
                    isPublicRessource = false ;
                    isPrivateRessource = true ;
                    isApplicationRessource = false ;                
                    isOwnedRessource = isUserOwnedRessource(requestedPath, session);
                } else if (isApplicationRessource(request, requestedPath)){
                    // Ex. ACCES_XML_RECOVER
                    isPublicRessource = false ;
                    isPrivateRessource = false ;
                    isApplicationRessource = true;                   
                }
                
            } else {
                isPrivateRessource = false ;    
                isOwnedRessource = false ;
                isPublicRessource = false ;
                isApplicationRessource = false ;                                    
            }
            

    }
    
    /**
     *
     * @param req
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {        

        
        /* Cast des objets request et response */
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
//        this.chain = chain ;

        
        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession(false);

        
        /* Non-filtrage des ressources statiques */
        // => Supprimer le nom de l'application de la requete.
        // ici, /TruePrice
        String requestedPath = 
                request.getRequestURI().substring( 
                    request.getContextPath().length() 
                );
                        
        
//        log.info("Check if request is secured...");
        
        /* On verifie que l'on est bien en HTtps sur port 443 */
        if (!isSecureRequest(request)){
            
            log.info("FILTERING REQUEST ==> Request is not Secured > send redirect");
            response.sendRedirect(getUrlRedirectHttps(request));
        }
        
        else {
            
            initVariables(request, response, session, requestedPath);
            
//            log.info("User is a member connected ? > " + isConnected
//                    + " >> Sure ? > " + sessionIsNotEmpty);
            
//            log.info("Requested page is welcome page ? > " + isWelcomePage);
//            log.info("Requested page is public page ? > " + isPulicPage);
//            log.info("Requested page is MEMBER page ? > " + isMemberPage);
//            
//            log.info("Requested page is Ressource page ? > " + isRessourcePage);
//            
//            log.info("Requested page is Public Ressource page ? > " + isPublicRessource);
//            
//            log.info("Requested page is Private Ressource page ? > " + isPrivateRessource);
//            log.info("Requested page is Owned Private Ressource page ? > " + isOwnedRessource);            
//            
//            log.info("Requested page is Application Ressource page ? > " + isApplicationRessource);
            
            
            if (isApplicationRessource) {
                
                response.sendRedirect( request.getContextPath() + ACCES_ACCEUIL);                
                
            } // Si pas connecté ou session vide
            else if (!isConnected | !sessionIsNotEmpty) {
                
                 manageUnconnectedUser(request, response, chain);
                
            } else {
                
                manageConnectedUser(request, response, chain);
            }
            
        }

    }
        
    @Override
    public void destroy() {
        
    }

    private boolean isApplicationRessource (HttpServletRequest request, String requestedUri) {
        if (requestedUri.startsWith(ACCES_XML_RECOVER)) { 
            return true ;
        }
        return false ;
    }
    private boolean isRessource (HttpServletRequest request, String requestedUri) {
    
        if (requestedUri.startsWith(ACCES_XML)
                // Inutiles : startsWith !
//            | requestedUri.startsWith(ACCES_XML_PUBLIC)
//            | requestedUri.startsWith(ACCES_XML_MEMBERS)
//            | requestedUri.startsWith(ACCES_XML_APPLICATION)
            ) {
            return true ;
        }
        return false;
    }
    private boolean isUserConnected (HttpServletRequest request, HttpSession session ) {
        
     
            try {
                request.getSession().getAttribute(ATT_SESSION_USER);
//                log.info("\t>>>\t Connected.");
//                connected = true;
                return true;
            } catch (Exception e) {
//                log.info("\t>>>\t NOT connected ... ");
                return false;
            }            
    }
    private boolean isUserOwnedRessource ( String requestedUri, HttpSession session ) {
        
        try {
            Membre mb = (Membre) session.getAttribute(ATT_SESSION_USER);
            if (requestedUri.contains(mb.getMbMail())) {
                return true;
            }
        } catch (Exception e) {
            log.warn("could not certify it's user owned ressource page !");
        }            
     
        return false;
    }
    private boolean isMemberPage (HttpServletRequest request, String requestedUri) {
        
        if ( requestedUri.startsWith(ACCES_DECONNEXION)
            | requestedUri.startsWith(ACCES_GRAPH)
            | requestedUri.startsWith(ACCES_PRODUITS)
            | requestedUri.startsWith(ACCES_DASHBOARD)
            | requestedUri.startsWith(ACCES_LISTES)
            | requestedUri.startsWith(ACCES_ADMIN)
            | requestedUri.startsWith(ACCES_XML_MEMBERS)
            | requestedUri.startsWith(ACCES_SYNC)                
        ) {     return true;    } 
        
        return false;
    }
    private boolean isPublicPage (HttpServletRequest request, String requestedUri) {
        
        if (    requestedUri.startsWith( "/css") 
                    | requestedUri.startsWith("/js")
                    | requestedUri.startsWith("/img")
                    | requestedUri.startsWith("/engine2")
                    | requestedUri.startsWith(ACCES_INSCRIPTION)
                    | requestedUri.startsWith(ACCES_CONNEXION)
                    | requestedUri.startsWith(ACCES_PUBLIC) 
                    | requestedUri.equals(ACCES_ACCEUIL) 
                    | requestedUri.equals(ACCES_RECOVER) 
                    | requestedUri.startsWith(ACCES_XML_PUBLIC) 
                    | requestedUri.startsWith(ACCES_REST) 
//                    | requestedUri.startsWith(ACCES_SYNC) 
                   // | ((chemin.startsWith("/")) & (chemin.endsWith("/")))
                    ) { return true;} 
        return false;
    }
    private boolean isSecureRequest (HttpServletRequest request) {
        
//        if (request.isSecure()) {
            
//            log.info("FILTERING REQUEST ==> Request is Secure() ? >     " + request.isSecure());
            
            String scheme = "";
            scheme = request.getScheme();
            
//            log.info("FILTERING REQUEST ==>     Scheme          is      [" + scheme + "]");
            
            String sslSessionId = "";
            sslSessionId = ServletUtils.getRequestAttrValue(request, ATT_SSL_SESSION_ID) ;
            

            
            
            
            if (sslSessionId != null) {
            
//                log.info("FILTERING REQUEST ==>     ssl Session ID  is      [" + ( sslSessionId == "" ? "Value not found ...." : sslSessionId) + "]");                
                
            } else {
//                log.info("FILTERING REQUEST ==>     ssl Session ID  is  NULL !!");            
            }
            
//            String serverPort = "" ;
//            serverPort = Integer.toString(request.getServerPort());
            Integer serverPort = request.getServerPort() ;
            
//            if (serverPort != null) {
            
//                log.info("FILTERING REQUEST ==>     Server port     is      [" + serverPort + "]");                
                
//            }            
            
            
            String fullUrl = request.getRequestURL().toString() ;
            
//            if (serverPort != null) {
            
//                log.info("FILTERING REQUEST ==>     Request URL     is      [" + ( fullUrl == "" ? "Value not found ...." :  fullUrl) + "]");                
                
//            }            
            
                
                
                
//                    request.getRemoteUser() != null && 
                if ( ! scheme.equals("https")) {
                    
//                    log.info("Scheme is not HTTPS : [" + scheme.toString() +"] > return false; ");
                    
                    return false;
                } else if ( serverPort.compareTo(443) != 0 && serverPort.compareTo(8443) != 0 ) {
                    
                    log.info("Server port is not 443 || 8443 > " + serverPort  
                        + " -> compare to 8443 == [" + serverPort.compareTo(8443) 
                        + "]  >>> return false ; " );
                    
                    return false;
                } else {
                    
                    return true;
                }                      
                            
//        } else {

//            log.info("FILTERING REQUEST ==> Request is Secure() ? >     FALSE .... ");
            
//            return false;
//        }
    }
    
    
    
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {        
//    
//            /*
//                PAGES AUTORISEES
//            */
//            if (    requestedPath.startsWith( "/css") 
//                    | requestedPath.startsWith("/js")
//                    | requestedPath.startsWith("/img")
//                    | requestedPath.startsWith("/engine2")
//                    | requestedPath.startsWith(ACCES_INSCRIPTION)
//                    | requestedPath.startsWith(ACCES_CONNEXION)
//                    | requestedPath.startsWith(ACCES_PUBLIC) 
//                    | requestedPath.equals(ACCES_ACCEUIL) 
//                   // | ((chemin.startsWith("/")) & (chemin.endsWith("/")))
//                    ) {
//
//
//
////                    log.warn(
////                            "{0}||Normal:Chemin Starts With : " + requestedPath
////                            , new Object[]
//    //                        {   lg.getLogTHead(),
////                                + requestedPath.toString()
//    //                        }
////                    );
//
//                chain.doFilter( request, response );
//
//                
//                
//            /*
//                WELCOME PAGE DISCONNECTED
//            */    
//            } else if (isWelcomePage) {
//                
//                if  (session == null 
////                        | request.getSession().getAttribute(ATT_SESSION_USER) == null) {
//                        | ServletUtils.getSessionAttrValue(session,ATT_SESSION_USER) == null) {
//                log.warn("{0}||Welcome Redirect DISCONNECTED : "
////                               + "{1}", new Object[]
////                            {   lg.getLogTHead(),
//                                + requestedPath.toString()
////                            }
//                );
//                response.sendRedirect( request.getContextPath() + ACCES_ACCEUIL );
//
//            /*
//                WELCOME PAGE CONNECTED
//            */    
//                } else if (session.getAttribute(ATT_SESSION_USER) != null ) {
//                log.warn("{0}Welcome Redirect CONNNECTED : " 
////                                + "{1}", new Object[]
////                            {   lg.getLogTHead(),
//                                + requestedPath.toString()
////                            }
//                );
//                response.sendRedirect( request.getContextPath() + ACCES_RESTREINT );       
//
//                }
//
//                
//                
//                
//            /*
//                OTHER PAGE CONNECTED
//            */
//            } else if (isConnected ) {
//                if ( requestedPath.startsWith(ACCES_RESTREINT) 
//                        | requestedPath.startsWith(ACCES_DECONNEXION)
//                        | requestedPath.startsWith(ACCES_GRAPH)
//                        | requestedPath.startsWith(ACCES_GRAPH_CONTAINER)
//                        | requestedPath.startsWith(ACCES_GRAPH_QUERY)
//                        | requestedPath.startsWith(ACCES_PRODUITS)
//                        | requestedPath.startsWith(ACCES_PRODUITS_ADD)
//                        | requestedPath.startsWith(ACCES_PRODUITS_LIST)
//                        | requestedPath.startsWith(ACCES_PRODUITS_MANAGE)
//                    ) {
//
//                    log.warn("{0}Normal CONNNECTED : "
////                                + "{1}", new Object[]
////                            {   lg.getLogTHead(),
//                                + requestedPath.toString()
////                            }
//                    );
//                    chain.doFilter(request, response);
//                }
//
//
//            /**
//             * Si l'objet utilisateur n'existe pas dans la session en cours, alors
//             * l'utilisateur n'est pas connecté.
//             */
//           // if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
//                /* Redirection vers la page publique */
//                //response.sendRedirect( request.getContextPath() + ACCES_PUBLIC );
//
//                /*
//                    Si on bloque l'application complète en modifiant la portée du filtre dans web.xml,
//                        alors il faut mettre un un forward pour rediriger la requete vers la page de connexion,
//                            sinon personne ne pourra jamais y accéder, car il faut pouvoir se connecter au moins 1 fois !
//                        /!> aux implications du FORWARD !
//                //request.getRequestDispatcher(ACCES_CONNEXION).forward(request, response)  ;
//                */
//
//                //chain.doFilter(request, res);
//
//             /*   
//
//                log.info("Chemin Forward Wdth : "+ request.getRequestURI());
//                request.getRequestDispatcher(ACCES_CONNEXION).forward(request, response)  ;
//
//            }
//
//            */   
//            /*
//                WHATEVER ELSE
//            !   DANGEROUS   !
//            */
//            } else {
//
//                log.warn("{0}OTHER : Chemin : "
////                                + "{1}", new Object[]
////                            {   lg.getLogHead(null),
//                                + requestedPath.toString()
////                            }
//                    );
//
////                response.sendError(403, "Vous n'avez pas l'accès, ou une ereur est survenue. Toutes nos excuses ...");
//                
//                response.sendRedirect( request.getContextPath() + ACCES_ACCEUIL );;       
//
//
//                //chain.doFilter( request, response );
//            }                    
//    }
//    
  
}
