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
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.CategoryJpaController;
import org.dmb.trueprice.controllers.ListesJpaController;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.controllers.ProduitListeJpaController;
import org.dmb.trueprice.controllers.SubcategoryJpaController;
import org.dmb.trueprice.entities.Liste;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.entities.Subcategory;
import org.dmb.trueprice.handlers.CategoryHandler;
import org.dmb.trueprice.handlers.ListeHandler;
import org.dmb.trueprice.handlers.SubCategoryHandler;
import org.dmb.trueprice.objects.ListeFrontend;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;

@WebServlet (   urlPatterns = {"/dashboard", "/dashboard/*"} )
public class Dashboard_servlet extends HttpServlet {

    
    @EJB 
    private  MembreJpaController userCtl ;
    @EJB 
    private  CategoryJpaController ctgCtl ;
    @EJB 
    private  SubcategoryJpaController sctgCtl ;    
    @EJB 
    private  ListesJpaController lstCtl ;    
    @EJB 
    private  ProduitListeJpaController pdtCtl ;    
    
    private static CategoryHandler ctgForm ;
    private static SubCategoryHandler sctgForm ;
    private static ListeHandler lstForm ;
    
    
    private static HashMap<String, String> shortListCategories = new HashMap<String, String>();
    private static HashMap<String, String> shortListSubCategories = new HashMap<String, String>();    
    private static ArrayList<Subcategory> listSubCategories = new ArrayList<Subcategory>() ;    
    
    private static ArrayList<Liste> listMemberListes = new ArrayList<Liste>() ;        
    private static ArrayList<ListeFrontend> listDisplayMemberListes = new ArrayList<ListeFrontend>() ;        
    private static HashMap<String, String> shortListMemberLists = new HashMap<String, String>();    
    
    public static final String ATT_SESSION_USER = "sessionUtilisateur";    

    private static final String VIEW_DASHBOARD = "/WEB-INF/dashboard.jsp";

    private static final String att_ShortListCatg = "slistCatg";    
    private static final String att_ListSCatg = "listSCatg";
    private static final String att_ListMbList = "listMbListes";
    private static final String att_ShortListMbList = "slistMbListes";
    private static final String att_ListDisplayedMbList = "listDsplMbListes";
    
    
    private static final String att_iconDataPath = "iconDataPath";    
    private String iconDataPath = "";    
    private Boolean iconPathIsOK = false;    
    
        private static final Logger log 
            = InitContextListener.getLogger(Dashboard_servlet.class) ;
    
        
//    @EJB
//    private PasswordJpaController pwdManager ;

//    @EJB
//    private MembreJpaController userManager ;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); //To change body of generated methods, choose Tools | Templates.
        
        iconDataPath = InitContextListener.getEnvEntryValue(att_iconDataPath);   
        
        ctgForm = new  CategoryHandler(ctgCtl);
        sctgForm  = new SubCategoryHandler(sctgCtl, ctgCtl);      
        lstForm = new ListeHandler(lstCtl);
        
    }
        
    private ArrayList<ListeFrontend> getPdtObjectsByMemberList(ArrayList<Liste> listMembersListes) {
    
        ArrayList<ListeFrontend> returned = new ArrayList<ListeFrontend>();
        
        for (Liste liste : listMembersListes) {
            
            ArrayList<Produit> pdtObjList = new ArrayList<Produit>();
            
            if (liste.getLstProduits() != null ) {
                
                for (String pdtId : liste.getLstProduits().split("_")) {
    
                    Produit pdt = null ;
                    
                    if (pdtId.length() > 0) {
                        pdt = pdtCtl.findProduitListe(Long.valueOf(pdtId));
                    }
                    
                    if (pdt != null) {
                        pdtObjList.add(pdt);
                    } else {
                        log.info("Could not find Produit with ID [" + pdtId 
                            + "] for liste U " + liste.getLstUser() 
                            + " || L " + liste.getLstId()
                        );
                    }

                }            
            }
            
            // Chaque liste reconstruite pour display avec produits Objects
            returned.add(new ListeFrontend(liste, pdtObjList));
            
            
        }
        
        return  returned;
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        
            // Ajoute l'url d'accès aux icones
            request.setAttribute(att_iconDataPath,
                // ternaire : build Path if 1st request
                (iconPathIsOK ? iconDataPath
//              : getIconDataPath(request) )
                : buildIconURL(request) )
            );        
            
            getCategoriesIdentifiers();
            request.setAttribute(att_ShortListCatg, shortListCategories);
            
            getSubCategories();
            request.setAttribute(att_ListSCatg, listSubCategories);        
            
            
            
            Membre mb = (Membre) ServletUtils.getSessionAttrObject(request.getSession(), ATT_SESSION_USER);
            
            getMemberListesIdentifiers(mb.getMbId());
            request.setAttribute(att_ShortListMbList, shortListMemberLists);
            
            getMemberListes(mb.getMbId());
            request.setAttribute(att_ListMbList, listMemberListes);
            
            listDisplayMemberListes = getPdtObjectsByMemberList(listMemberListes);
            request.setAttribute(att_ListDisplayedMbList, listDisplayMemberListes);
            
            
        
        /* Affichage de la page du dashboard */
        this.getServletContext().getRequestDispatcher(VIEW_DASHBOARD).forward(request, response);
    }

//
//    @Override
//    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        /* Préparation de l'objet formulaire */
//        ConnexionHandler conManager = new ConnexionHandler(userManager, pwdManager);
//
//        /* Traitement de la requête et récupération du bean en résultant */
//        Membre user = conManager.connecterUtilisateur(request);
//
//        /* Récupération de la session depuis la requête */
//        HttpSession session = request.getSession(true);
//
//        /**
//         * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
//         * Utilisateur à la session, sinon suppression du bean de la session.
//         */
//        if (conManager.getErreurs().isEmpty()) {
//            log.debug("Setting attribute User in Session Scope");
//            session.setAttribute(ATT_SESSION_USER_ID, user);
//            log.debug("Done =)");
//        } else {
//            session.setAttribute(ATT_SESSION_USER_ID, null);
//        }
//
//        /* Si et seulement si la case du formulaire est cochée */
//        if (request.getParameter(FORM_PARAM_MEMORY) != null) {
//            /* Récupération de la date courante */
//            DateTime dt = new DateTime();
//            /* Formatage de la date et conversion en texte */
//            DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT_DATE);
//            String dateDerniereConnexion = dt.toString(formatter);
//            /* Création du cookie, et ajout à la réponse HTTP */
//            ServletUtils.setCookie(response, COOKIE_DERNIERE_CONNEXION, dateDerniereConnexion, COOKIE_MAX_AGE);
//        } else {
//            /* Demande de suppression du cookie du navigateur */
//            ServletUtils.setCookie(response, COOKIE_DERNIERE_CONNEXION, "", 0);
//        }
//        /* Stockage du formulaire et du bean dans l'objet request */
//        request.setAttribute(ATT_FORM, conManager);
//        request.setAttribute(ATT_USER, user);
//
//        this.getServletContext().getRequestDispatcher(VIEW_DASHBOARD).forward(request, response);
//    }
    

    
    private HashMap<String, String> getCategoriesIdentifiers() {

//        if (cForm == null) {
//            cForm = new CategoryHandler(userCtl, ctgCtl);
//        }
//        int cgCount = cForm.getCount() ;
        
        int cgCount = 0;
        // la premiere fois il enverra 0
        // ensuite la taille de la map
        if (shortListCategories != null) {
            cgCount = shortListCategories.size();
        }
        
        // Envoie 0 ett recupere map vide non nulle
        // Ensuite envoie sa taille actuelle, mais ne sera jamais differente car pas obtenue de la db
        shortListCategories = InitContextListener.getShortListCategories(cgCount);
        
        // on ne fait rien d'autre ici car on ne possede pas le jpaContrler ici
        
        if (shortListCategories == null
//        if (shortListCategories.isEmpty() 
                || 
           (shortListCategories.size() != cgCount && cgCount != 0 )
        ) {
            log.info("Retrieve Category short list cause has changed.");
            
//            shortListCategories = new HashMap<String, String>();
//            shortListCategories.put("#ERROR", "Sorry, error.The list is not yet initialized.");
            
//            shortListCategories.putAll(ctgForm.getShortList());
            
            shortListCategories = ctgForm.getShortList();
            
            InitContextListener.setShortListCategories(shortListCategories);
        }
//        

        return shortListCategories;
//        return DataProvider.getShortListCategories();
        
    }    
    
    private List<Subcategory> getSubCategories() {
        
        int cgCount = 0;
        if (listSubCategories != null) {
            cgCount = listSubCategories.size();
        }
        
        
        // We try to get an existing list (valid if same size)
        listSubCategories = InitContextListener.getListSubCategories(cgCount);
        
        // if not existing, we have to retrieve it;
        if (    listSubCategories == null 
            || (listSubCategories.size() != cgCount && cgCount != 0 )
        ) {
            log.info("Retrieve SubCategory list cause has changed.");
            
            listSubCategories = new ArrayList<Subcategory>();
            
            listSubCategories.addAll(sctgForm.getList());
            
            // If we get the up-to-date list, don't forget to share it !
            InitContextListener.setListSubCategories(listSubCategories);
            
        }
        return listSubCategories;
//        return DataProvider.getListSubCategories();
    }

    
    private List<Liste> getMemberListes(long memberID) {
        
//        if (sctgForm == null) {
//            sctgForm = new SubCategoryHandler( sctgCtl, ctgCtl);  
//        }
//        int cgCount = sctgForm.getCount() ;
//        int cgCount = listSubCategories.size() ;
        
        
//        int lstCount = 0;
//        if (listMemberListes != null) {
////            lstCount = listMemberListes.size();
//            lstCount = lstForm.getCount();
//        }
        
        
        
        // We try to get an existing list (valid if same size)
//        listMemberListes = InitContextListener.getListMemberListes(lstCount);
        
        // if not existing, we have to retrieve it;
        // As initCtxtLitnr returns null, change the test
//        if (    listSubCategories.isEmpty() 
//        if (    listSubCategories == null 
        
        
        
//        if (    listMemberListes == null 
//                // Not needed anymore cause handled by initContextListener
//            || (listMemberListes.size() != lstCount && lstCount != 0 )
//        ) {
            log.info("Retrieve Member's List list cause has changed.");
            
            listMemberListes = new ArrayList<Liste>();
            
//            listSubCategories.removeAll(listSubCategories);
//            listMemberListes.addAll(lstForm.getList());
            listMemberListes.addAll(lstForm.getListByMember(memberID)); 
            
            // If we get the up-to-date list, don't forget to share it !
//            InitContextListener.setListmemberListes(listMemberListes);
            log.info("Got member liste list with size [" + listMemberListes.size() + "]");
//        }
        return listMemberListes;
//        return DataProvider.getListSubCategories();
    }

    
    private HashMap<String, String> getMemberListesIdentifiers(long memberID) {

//        if (cForm == null) {
//            cForm = new CategoryHandler(userCtl, ctgCtl);
//        }
//        int cgCount = cForm.getCount() ;
        
//        int listeCount = 0;
//        // la premiere fois il enverra 0
//        // ensuite la taille de la map
//        if (shortListMemberLists != null) {
////            listeCount = shortListMemberLists.size();            
//            listeCount = lstForm.getCount();            
//        }
        
        // Envoie 0 ett recupere map vide non nulle
        // Ensuite envoie sa taille actuelle, mais ne sera jamais differente car pas obtenue de la db
//        shortListMemberLists = InitContextListener.getShortListMemberListes(listeCount);
        
        // on ne fait rien d'autre ici car on ne possede pas le jpaContrler ici
        
//        if (shortListMemberLists == null
////        if (shortListCategories.isEmpty() 
//                || 
//           (shortListMemberLists.size() != listeCount && listeCount != 0 )
//        ) {
            log.info("Retrieve MemberListes short list cause has changed.");
            
//            shortListCategories = new HashMap<String, String>();
//            shortListCategories.put("#ERROR", "Sorry, error.The list is not yet initialized.");
            
//            shortListCategories.putAll(ctgForm.getShortList());
            
            shortListMemberLists = lstForm.getShortListByMember(memberID);
            
            log.info("Got member liste short list with size [" + shortListMemberLists.size() + "]");
            
//            InitContextListener.setShortListCategories(shortListMemberLists);
//        }
//        

        return shortListMemberLists;
//        return DataProvider.getShortListCategories();
        
    }    
    
    
    /**
     * Permet de construire iconDataPath lors de la 1e requete
     * (iconDataPath initial récupéré dans init(config) ne contiens
     * que le chemin relatif d'accès au dossier des images sur le serveur)
     * @param request 
     */
//    private void buildIconURL(ServletRequest request) {       
    private String buildIconURL(HttpServletRequest request) {       
                        
        String srvName = request.getServerName();
        String srvPort = Integer.toString(request.getServerPort());
            
        log.debug("[srvName : srvPort] >> [" 
                + srvName + " : " + srvPort + "]" );                        
//        
        log.debug("Icon Data Path is >> " + iconDataPath );   
        if ( ! iconDataPath.contains
                (srvName)  ) {
//            log.debug("Adding real path [/] >> " + realPath );   
            iconDataPath = "https://" 
                + srvName + ":" + srvPort+ iconDataPath;
            
        }
        log.info("Finally, Icon Data Path is >> " + iconDataPath );   
        
        iconPathIsOK = true;
        
         return iconDataPath;
    }    
         
}
