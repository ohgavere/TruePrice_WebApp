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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.CategoryJpaController;
import org.dmb.trueprice.controllers.EnseigneJpaController;
import org.dmb.trueprice.controllers.ListesJpaController;
import org.dmb.trueprice.controllers.MarqueJpaController;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.controllers.ProduitListeJpaController;
import org.dmb.trueprice.controllers.SubcategoryJpaController;
import org.dmb.trueprice.entities.Category;
import org.dmb.trueprice.entities.Enseigne;
import org.dmb.trueprice.entities.Liste;
import org.dmb.trueprice.entities.Marque;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.entities.Subcategory;
import org.dmb.trueprice.handlers.AdminHandler;
import org.dmb.trueprice.handlers.BasicHandler;
import org.dmb.trueprice.handlers.CategoryHandler;
import org.dmb.trueprice.handlers.EnseigneHandler;
import org.dmb.trueprice.handlers.ListeHandler;
import org.dmb.trueprice.handlers.MarqueHandler;
import org.dmb.trueprice.handlers.ProductGeneriqHandler;
import org.dmb.trueprice.handlers.SubCategoryHandler;
import org.dmb.trueprice.utils.internal.InitContextListener;

@WebServlet (   urlPatterns = {"/admin/*"} )
//@MultipartConfig(location = "/upload"

@MultipartConfig(
//        location = "C:\\TFE\\icons", 
        location = "/home/gpc/trueprice/icons", 
        maxFileSize = 10485760L  // 10MB
) 
public class Admin_servlet extends HttpServlet {

    
    private static final String URL_ADMIN = "/admin" ;
    private static final String URL_CREATE = "/create" ;
    
    private static final String URL_ENSEIGNE = "/enseigne" ;
    private static final String URL_CATEGORIE = "/categorie" ;
    private static final String URL_MARQUE = "/marque" ;
    private static final String URL_SUBCATEGORIE = "/subcategorie" ;
    private static final String URL_PRODUITGENERIQUE = "/produitgen" ;
    
    
    private static final String URL_LISTE = "/liste" ;
    private static final String URL_PRODUITCUSTOM = "/produitcust" ;
    
//    private Path iconDataFolder = Paths.get("c:" + File.separator + "TFE" + File.separator + "icons");
    private String iconDataFolder = "iconDataFolder";
//    private Path absoluteIconDataFolder ;
//    private final String att_init_DataFolder = "att_init_DataFolder"  ;
    
    private static final String att_Categorie = "categorie";
//    private static final String att_ListCatg = "listCatg";
    private static final String att_ShortListCatg = "slistCatg";
    private static final String att_Subcategorie = "subcategorie";
    private static final String att_ListSCatg = "listSCatg";
//    private static final String att_ShortListSCatg = "slistSCatg";
    private static final String att_Enseigne = "enseigne";
    private static final String att_Marque = "marque";
    private static final String att_Produit = "produit";
    private static final String att_Liste = "liste";
    
    private static final String att_Form = "form";
    private static final String att_FormGlobal = "formAdmin";
//    private static final String Att_Form_Errors = "errors";
    
    private static final String ATT_SESSION_USER = "sessionUtilisateur";
    private static final String VIEW_ADMIN = "/WEB-INF/admin/form.jsp";
    private static final String VIEW_DASHBOARD = "/WEB-INF/dashboard.jsp";
    private static final String ATT_SESSION_ADMIN = "sessionAdmin";
    
//    private static HashMap<String, String> erreurs = new HashMap<String, String>();
    private static HashMap<String, String> shortListCategories = new HashMap<String, String>();
    private static HashMap<String, String> shortListSubCategories = new HashMap<String, String>();
    private static ArrayList<Subcategory> listSubCategories ;
    private static ArrayList<Category> listCategories = new ArrayList<Category>();
    
    private static      AdminHandler             globalForm ;
    private static      CategoryHandler          ctgForm ;
    private static      SubCategoryHandler       sctgForm ;
    private static      EnseigneHandler          ensForm ;
    private static      MarqueHandler            mrqForm ;
    private static      ListeHandler             lstForm;
    private static      ProductGeneriqHandler    pdtGenForm ;

//    private MultipartMap map;
    
    @EJB
    private MembreJpaController userCtl ;
    @EJB 
    private CategoryJpaController ctgCtl ;
    @EJB 
    private SubcategoryJpaController sctgCtl ;
    @EJB 
    private EnseigneJpaController esgCtl ;
    @EJB 
    private ProduitListeJpaController pdtCtl ;
    @EJB
    private MarqueJpaController mrqCtl;
    @EJB
    private ListesJpaController lstCtl;
    


    private static final Logger log = InitContextListener.getLogger(Admin_servlet.class) ;
    
    private static HashMap <String, ArrayList<String>> supposedFields = new HashMap<String, ArrayList<String>> ();

    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
//        InitContextListener.getServletConfig(this.getClass().getSimpleName(), this);        
        
        
        
//        super.init(config); //To change body of generated methods, choose Tools | Templates.
                
        // Si il est à sa valeur par défaut (donc avant d'etre strictement static ...),
        // alors on le modifie pour aller chercher le vrai chemin.
        // Ainsi on ne stock qu'une variable en mémoire
//        if (iconDataFolder == null) {
            /*
             * Lecture du paramètre 'chemin' passé à la servlet via la déclaration
             * dans le web.xml
             */            
//            att_init_DataFolder = this.getServletConfig().getInitParameter( att_init_DataFolder );
//            att_init_DataFolder = config.getInitParameter( "chemin" );
            
            
            iconDataFolder = 
//                    Paths.get(
                InitContextListener.getEnvEntryValue(iconDataFolder)            
//            )
            ;

            log.debug("Config Data Folder >> [" + iconDataFolder + "]");
            
            log.debug("Build absolute path : ");
                                   
//            
//            String rootB = Admin_servlet.class.getClassLoader().getParent().toString();
//            
//            log.debug("Try root full path >> [classLoader.Parent] > " + rootB );
//            
//            String rootFull = rootB + iconDataFolder ;
//            
//            log.debug("Try add root full path >> [classLoader.Parent] + iconDataFolder > " + rootFull );
//            
//            log.debug("Try to get path >> ");
//            
//            try {
//                Paths.get(rootFull);
//                iconDataFolder = rootFull;
//                log.debug("Success.");
//            } catch (Exception e) {
//                log.debug("\nFAILED...\n" + e.getMessage());
//                e.printStackTrace();
//            }
            
            
            
            log.info("Final icon Data Folder >> " + iconDataFolder
//                    + " & " + rootFull
            );
            
//        }

        globalForm  = new AdminHandler          ();
        pdtGenForm  = new ProductGeneriqHandler (pdtCtl, iconDataFolder + File.separator + "generic", sctgCtl, ctgCtl);
        ctgForm     = new CategoryHandler       (ctgCtl);
        sctgForm    = new SubCategoryHandler    (sctgCtl, ctgCtl);
        ensForm     = new EnseigneHandler       (esgCtl);
        mrqForm     = new MarqueHandler         (mrqCtl);
        lstForm     = new ListeHandler          ( lstCtl, pdtCtl, userCtl, esgCtl);
        
//        getProductsListe();
        getCategoriesIdentifiers();
        getSubCategories();
//        getSubCategoriesIdentifiers();


            
    }
    
    

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setGlobalError( String champ, String message ) {
        log.warn("==========\tINSERTING EXCEPTION > " + message);
//        if (adminForm.erreurs.isEmpty()) {
            globalForm.setErreur( champ, message );
//        }
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Si il est à sa valeur par défaut (donc avant d'etre strictement static ...),
        // alors on le modifie pour aller chercher le vrai chemin.
        // Ainsi on ne stock qu'une variable en mémoire
//        if (att_init_DataFolder.equals("chemin")) {
//            /*
//             * Lecture du paramètre 'chemin' passé à la servlet via la déclaration
//             * dans le web.xml
//             */            
//            att_init_DataFolder = this.getServletConfig().getInitParameter( att_init_DataFolder );   
//        }        
        
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute(ATT_SESSION_USER) != null   && session.getAttribute(ATT_SESSION_ADMIN) != null) { 
            
            getCategoriesIdentifiers();
            request.setAttribute(att_ShortListCatg, shortListCategories);
            
//            getShortListSubCategories();
//            request.setAttribute(att_ShortListSCatg, shortListSubCategories);
            
            getSubCategories();
            request.setAttribute(att_ListSCatg, listSubCategories);
            
            /* Affichage de la page d'administration */
            this.getServletContext().getRequestDispatcher(VIEW_ADMIN).forward(request, response);
        } else {    
            /* Affichage de la page du dashboard */
            this.getServletContext().getRequestDispatcher(VIEW_DASHBOARD).forward(request, response);
        }
        
    }
    
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute(ATT_SESSION_USER) != null   && session.getAttribute(ATT_SESSION_ADMIN) != null) {       
            
            
            // 1. clear the old form
            log.debug("Admin : doPost() -> Session is not null, gonna clear session data from last POST ...");
            
            // Si on recoit un nouveau formulaire, on nettoye les anciennes données avant tout.
            clearFormsAndResults(session);
            
            log.debug("\nClear data Done.\n");
            
            
            
            String resultat = "";
//            String action = ServletUtils.getRequestAttrValue(request, "action");
            String action = findAction(request.getRequestURI());
            
//            if (request.getHeader("Content-type").contains("multipart/form-data")) {
//            
//                map = new MultipartMap(request, this);
//                action = map.getParameter("action");
//            } else {
////                action = request.getContextPath();
//                action = findAction(request.getRequestURI());
//            }
            
            log.info("Got action type [" + action + "]");

            
            if ( action == null || action.equals("") ) {
                log.error("Could not determine what you asked to do : [" + action + "] from [" + request.getRequestURI() + "]");
                setGlobalError("action","There is no action defined. Given : [" + action + "] from [" + request.getRequestURI() + "]");
            } else {
                
                resultat = "La creation de " ;
                
                switch (action) {
                    case URL_CATEGORIE :
                        resultat += "la catégorie";
                            Category ctg = ctgForm.createCategory(request);
                            session.setAttribute(att_Categorie, ctg);
                            session.setAttribute(att_Form, ctgForm);
                        break;
                    case URL_SUBCATEGORIE :
                        resultat += "la sous-categorie";
                            Subcategory sctg = sctgForm.createSubCategory(request);
                            session.setAttribute(att_Subcategorie, sctg);
                            session.setAttribute(att_Form, sctgForm);
                        break;
                    case URL_ENSEIGNE :
                        resultat += "l'enseigne";
                            Enseigne esg = ensForm.createEnseigne(request);
                            session.setAttribute(att_Enseigne, esg);
                            session.setAttribute(att_Form, ensForm);
                        break;
                        
                    case URL_PRODUITGENERIQUE :
                        resultat = "La creation d'un produit";
//                            Path dataFolder = Paths.get(absoluteIconDataFolder + File.separator + "generic");
//                            absoluteIconDataFolder = Paths.get(iconDataFolder + File.separator + "generic");
//                            log.debug(" Got path > " + dataFolder.toString());
//                            log.debug(" Got path absolute ? > " + dataFolder.isAbsolute());
//                            log.debug(" Got path absolute ? > " + Files.isDirectory(dataFolder));

    //                        MultipartMap map = new MultipartMap(request, att_init_DataFolder);
//                             map = new MultipartMap(request,absoluteIconDataFolder.toString() );
//                            pdtGenForm = new ProductGeneriqHandler(pdtCtl, map,
//    //                                getIconDataPath()
//                                    dataFolder.toString()
//                                    , sctgCtl, ctgCtl);
//                             pdtGenForm.buildMultipartMap(map);
//                             pdtGenForm.buildMultipartMap(new MultipartMap(request,absoluteIconDataFolder.toString()));
                             
                            Produit pdt = pdtGenForm.createProductGeneriq(request);

                            session.setAttribute(att_Produit, pdt);
                            session.setAttribute(att_Form, pdtGenForm);                        
                                                
                        break; 
                    case URL_PRODUITCUSTOM :
                        resultat = "La creation d'un produit <em>custom<em> ";
                            log.error("Action is not yet supproted. Sorry ... >> " + action);
                            setGlobalError("action","Action is not yet supproted. Sorry ... >> " + action);
                        break;
                    case URL_MARQUE :
                        resultat += "la marque";
//                            log.error("Action is not yet supproted. Sorry ... >> " + action);
//                            setGlobalError("action","Action is not yet supproted. Sorry ... >> " + action);
                        
                        Marque mrq = mrqForm.createMarque(request);
                        
                        session.setAttribute(att_Marque, mrq);
                        session.setAttribute(att_Form, mrqForm);
                        
                        break;
                    case URL_LISTE :
                        resultat += "la liste";
//                            log.error("Action is not yet supproted. Sorry ... >> " + action);
//                            setGlobalError("action","Action is not yet supproted. Sorry ... >> " + action);
                        
                        Liste lst = lstForm.createListes(request);
                        
                        session.setAttribute(att_Liste, lst);
                        session.setAttribute(att_Form, lstForm);
                        
                        break;
                    default :
                            log.error("Could not determine what you asked to do : [" + action + "]");
                            setGlobalError("action","There is no action defined. Given : [" + action + "]");
                        break;
                }
            }
            
            /*  
                Resultat du formulaire global (pour l'action courante)
            */
            BasicHandler mainHDL = (BasicHandler)globalForm;
//            log.debug("main action Handler ? > "
//                    + ( mainHDL == null ? " not found ... " : mainHDL.toString() )
//            );
            BasicHandler baseHDL = (BasicHandler)session.getAttribute(att_Form);
//            log.debug("base action Handler ? > "
//                    + ( baseHDL == null ? " not found ... " : baseHDL.toString() )
//            );
            
////            String responsable = "";
//            try {
//                
//                Map<String, String> mainErrMap = baseHDL.getErreurs();
////                responsable = "main global form";responsable = "specific form";
//                Map<String, String> baseErrMap = baseHDL.getErreurs();
////                responsable = "";
//            } catch (NullPointerException npe) {
////                log.debug("At least one was empty : [ " 
////                        + responsable
////                        + " ]\n >> " + npe.getMessage());
//            }
            
            // Determiner le resultat de l'action globale.
            if (mainHDL == null) {
                resultat += " a <strong><em>echoue</em></strong> ... (no AdminForm found)";
            } else if ( mainHDL.getErreurs().isEmpty()){ 
                if (action.contentEquals(URL_MARQUE) | action.contentEquals(URL_PRODUITCUSTOM)) {
                    // l'action a reussi +- car pas de marque ou de produit custom pour linstant
                    resultat += " a <strong><em>reussi</em></strong>.";
                } else {
                    if (baseHDL == null) {
                        log.warn("base HDL is null");
                        resultat += " a <strong><em>echoue</em></strong> ... (no specForm found)";
                    } else if (baseHDL.getErreurs().isEmpty()) {
                        resultat += " a reussi.";
                    } else {
                        //l'action a echoue
                        resultat += " a <strong><em>echoue</em></strong> ... .";
                    }
                }
                
            } else {
                //l'action a echoue
                resultat += " a <strong><em>echoue</em></strong> ... .";
            }
            
            // Enregistrer le resultat
            log.debug("Admin main action result is > " + resultat);            
            globalForm.setResultat(resultat);            
            log.debug("Admin main action HANDLER result is > " + globalForm.getResultat());
            
            // Le placer en session 
            session.setAttribute(att_FormGlobal, globalForm);
            
            // Recuperer les (sous-)categories pour les mettre en session
//            getCategoriesIdentifiers();
//            request.setAttribute(att_ShortListCatg, shortListCategories);
//            
//            getSubCategories();
//            request.setAttribute(att_ListSCatg, listSubCategories);
            
            // Rediriger vers ICI (ce servlet) pour revenir en doGet()
            response.sendRedirect(request.getContextPath() + URL_ADMIN);
            
        } else {    
            /* Affichage de la page du dashboard */
            this.getServletContext().getRequestDispatcher(VIEW_DASHBOARD).forward(request, response);
        }
        
    }
    
    /**
     * Permet de supprimer les resultats et erreurs des formulaires precedents.
     * Pour cela, cette méthode n'est exécutée que lors du POST d'un nouveau formulaire.
     * Les données du 'formAdmin' (formulaire global) sont obligatoirement nettoyées.
     * Les données du 'form' (formulaire spécififque) sont cherchées et supprimées le cas échéant.
     * @param session 
     */
    private void clearFormsAndResults (HttpSession session) {
        
//        log.info("Gonna get session attrs again & print them to debug");
//        Enumeration<String> attrs = session.getAttributeNames();
//        do {
//            String elem = attrs.nextElement() ;
//            log.info("\n\t\t Got attr [" + elem + "]\t "
//                    + "& value [" + session.getAttribute(elem)+ "]"
//            ); 
//        } while (attrs.hasMoreElements()) ;     
        
        // No need to remove it, cause we add it in a few millisecs ...
        // (clean only when receive a new POST request)
//        session.removeAttribute(att_FormGlobal);
        globalForm.clearForm();
        
        // but last used form should be cleared for the next time it would be called
        BasicHandler lastUsedForm = (BasicHandler) session.getAttribute(att_Form);
        if (lastUsedForm != null ) {
            lastUsedForm.clearForm();
            // And then we can remove it because it could be an other form this time -> 
            session.removeAttribute(att_Form);
        }

        
         Enumeration<String> attrs = session.getAttributeNames();
        do {
            String elem = attrs.nextElement() ;
            
            if (elem.contentEquals(att_Categorie)
                | elem.contentEquals(att_Subcategorie)
                | elem.contentEquals(att_Enseigne)
                | elem.contentEquals(att_Produit)
                | elem.contentEquals(att_Marque)
                | elem.contentEquals(att_Liste)
            ) {
            
                session.removeAttribute(elem);
             
                log.info("Removed attr from Session : " + elem );
            }
            
        } while (attrs.hasMoreElements()) ;        
        
//        log.info("Gonna get session attrs again & print them to debug");
//        attrs = session.getAttributeNames();
//        do {
//            String elem = attrs.nextElement() ;
//            log.info("\n\t Got attr [" + elem + "] "
//                    + "& value [" + session.getAttribute(elem)+ "]"
//            ); 
//        } while (attrs.hasMoreElements()) ;        
    }
    
    private String findAction(String contextpath) {
        String action = "" ;

//        log.debug("Received path > " + contextpath);
        
        if (contextpath.contentEquals(URL_ADMIN) || 
                contextpath.contentEquals(URL_ADMIN + URL_CREATE)
        ) {
//            log.debug("Try to go to /admin only ?  > " + contextpath);
            return "";
        } else {
            
            // On conserve [/admin/create*]
            contextpath = contextpath.substring(
                    contextpath.indexOf(URL_ADMIN + URL_CREATE)
            );
            
//            log.debug("After substring  1 > " + contextpath);

            //on retire [/admin/create] et on garde [*]
            contextpath = contextpath.substring(
                    (URL_ADMIN + URL_CREATE).length(),
                    contextpath.length()
            );
            
//            log.debug("After substring 2 > " + contextpath);
            
            if (contextpath.isEmpty()
                    | contextpath.contentEquals("")                    
                    ) {
                return "";
            } else {
                
                switch (contextpath) {
                    case URL_ENSEIGNE:
                        action = URL_ENSEIGNE;
                        break;
                    case URL_MARQUE:
                        action = URL_MARQUE;
                        break;
                    case URL_CATEGORIE:
                        action = URL_CATEGORIE;
                        break;
                    case URL_SUBCATEGORIE:
                        action = URL_SUBCATEGORIE;
                        break;
                    case URL_PRODUITGENERIQUE:
                        action = URL_PRODUITGENERIQUE;
                        break;
                    case URL_PRODUITCUSTOM:
                        action = URL_PRODUITCUSTOM;
                        break;
                    case URL_LISTE:
                        action = URL_LISTE;
                        break;
                    default:
                        action = "Tried {" + URL_ADMIN + URL_CREATE + "} with [" + contextpath +"]." ;
                        break;
                }
                
            }
            
        } 
        
        return action;
        
    }
    
    private HashMap<String, String> getCategoriesIdentifiers() {
//        if (cForm == null) {
//            cForm = new CategoryHandler( ctgCtl);
//        }
        int cgCount = 0;
        // la premiere fois il enverra 0
        // ensuite la taille de la map
        if (shortListCategories != null) {
            cgCount = shortListCategories.size();
        }
        
        // Envoie 0 si existe pas encore et recupere null si existe pas encore ou modifiee
        shortListCategories = InitContextListener.getShortListCategories(cgCount);
        
        if (shortListCategories == null || 
           (shortListCategories.size() != cgCount && cgCount != 0 )) {
            log.info("Retrieve short Category list cause has changed.");
            
            shortListCategories = ctgForm.getShortList();
            
            InitContextListener.setShortListCategories(shortListCategories);
        }
        
        
        
        return shortListCategories;
//        return DataProvider.getShortListCategories();
        
    }

//    private HashMap<String, String> getSubCategoriesIdentifiers() {
//        if (scForm == null) {
//            scForm = new SubCategoryHandler(userCtl, sctgCtl, ctgCtl);  
//        }
//        int cgCount = scForm.getCount() ;
//        if (shortListSubCategories.isEmpty() || 
//           (shortListSubCategories.size() != cgCount && cgCount != 0 )) {
//            log.info("Retrieve SubCategory list cause has changed.");
//            
//            shortListSubCategories = cForm.getShortList();
//        }
//        return shortListSubCategories;
////        return DataProvider.getShortListSubCategories();
//    }
    
    private List<Subcategory> getSubCategories() {
        
//        if (scForm == null) {
//            scForm = new SubCategoryHandler( sctgCtl, ctgCtl);  
//        }
//        int cgCount = scForm.getCount() ;
        
        int cgCount = 0;
        if (listSubCategories != null) {
            cgCount = listSubCategories.size();
        }        
        
        // We try to get an existing list (valid if same size)
        listSubCategories = InitContextListener.getListSubCategories(cgCount);
        
        // if not existing, we have to retrieve it;
        // As initCtxtLitnr returns null, change the test
//        if (    listSubCategories.isEmpty() 
//        if (    listSubCategories == null 
        if (    listSubCategories == null 
                // Not needed anymore cause handled by initContextListener
            || (listSubCategories.size() != cgCount && cgCount != 0 )
        ) {
            log.info("Retrieve SubCategory list cause has changed.");
            
            listSubCategories = new ArrayList<Subcategory>();
            
//            listSubCategories.removeAll(listSubCategories);
            listSubCategories.addAll(sctgForm.getList());
            
            // If we get the up-to-date list, don't forget to share it !
            InitContextListener.setListSubCategories(listSubCategories);
            
        }
        return listSubCategories;
//        return DataProvider.getListSubCategories();
    }
    

  
    
}
