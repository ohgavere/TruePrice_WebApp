/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.CategoryJpaController;
import org.dmb.trueprice.controllers.ListesInfoJpaController;
import org.dmb.trueprice.controllers.ListesJpaController;
import org.dmb.trueprice.controllers.ListesResultJpaController;
import org.dmb.trueprice.controllers.ListesStatsJpaController;
import org.dmb.trueprice.controllers.ProduitInfoJpaController;
import org.dmb.trueprice.controllers.ProduitListeJpaController;
import org.dmb.trueprice.controllers.ProduitStatsJpaController;
import org.dmb.trueprice.controllers.QttDetailJpaController;
import org.dmb.trueprice.controllers.SubcategoryJpaController;
import org.dmb.trueprice.entities.Liste;
import org.dmb.trueprice.entities.ListeInfo;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.entities.ProduitInfo;
import org.dmb.trueprice.entities.Subcategory;
import org.dmb.trueprice.handlers.CategoryHandler;
import org.dmb.trueprice.handlers.ListeHandler;
import org.dmb.trueprice.handlers.ProductGeneriqHandler;
import org.dmb.trueprice.handlers.QttHandler;
import org.dmb.trueprice.handlers.SubCategoryHandler;
import org.dmb.trueprice.objects.AvailableList;
import org.dmb.trueprice.objects.ListeDetailFrontend;
import org.dmb.trueprice.objects.ListeFrontend;
import org.dmb.trueprice.objects.ListeInfoFrontend;
import org.dmb.trueprice.objects.StatsGlobalResponse;
import org.dmb.trueprice.objects.SyncGetterRequest;
import org.dmb.trueprice.objects.SyncInitResponse;
import org.dmb.trueprice.utils.internal.GsonConverter;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;

/**
 *
 * @author Guitch
 */
@WebServlet ( name = "StatsService", 
        urlPatterns = {
            "/stats",       // 'Page d'acceuil' des stats dans le dashboard
            
            "/stats/lst",   // 'Page d'acceuil' pour une liste  -> ?lst_id=''
            "/stats/pdt",   // 'Page d'acceuil' pour un produit -> ?pdt_id=''
            
            "/stats/lst/result",    // 'Resultat d'une liste'   -> ?lst_id=''&result_id=''
            "/stats/pdt/result",    // 'Resultat d'un produit'  -> ?pdt_id=''&result_id=''
            
            "/stats/lst/stats",     // 'Stats d'une liste'      -> ?lst_id=''&stats_id=''
            "/stats/pdt/stats"      // 'Stats d'un produit'     -> ?pdt_id=''&stats_id=''
        })
public class Stats_servlet extends HttpServlet {
     
    private static final Logger log 
        = InitContextListener.getLogger(Stats_servlet.class) ;
        
/**
 *  PRODUITS    - Generic  + custom
 *              - Info
 *              - Stats
 *              - Result
 */        
    @EJB 
    private  ProduitListeJpaController pdtBaseCtl ;
    private static ProductGeneriqHandler pdtGenForm ;
//    private static ProductCustomHandler pdtCustForm ;
    
    @EJB 
    private  ProduitInfoJpaController pdtInfoCtl ;
//    private static ProduitInfoHandler pdtInfoForm ;
    
    @EJB 
    private  ProduitStatsJpaController pdtStatsCtl ;    
//    private static ProduitStatsHandler pdtStatsForm ;    
        
/**
 *  LISTES  - Base
 *          - Info
 *          - Stats
 *          - Result
 */        
    @EJB 
    private  ListesJpaController listBaseCtl ;
    private static ListeHandler listBaseForm ;
    
    @EJB 
    private  ListesInfoJpaController listInfoCtl ;
//    private static ListeInfoHandler listInfoForm ;
    
    @EJB 
    private  ListesStatsJpaController listStatsCtl ;    
//    private static ListeStatsHandler listStatsForm ;
    
    @EJB 
    private  ListesResultJpaController listResultCtl ;    
//    private static ListeResultHandler listResultForm ;
    
/**
 *  OTHER EJBs
 */       
    @EJB
    private CategoryJpaController ctgCtl ;
    private CategoryHandler ctgForm;
    
    @EJB
    private SubcategoryJpaController sctgCtl ;    
    private SubCategoryHandler sctgForm;
    
//    @EJB 
//    private  EnseigneJpaController esgnCtl ;    
//    private  EnseigneHandler esgnForm ;    
    
    @EJB 
    private  QttDetailJpaController qttCtl ;    
    private  QttHandler qttForm ;    
    
/**
 * STANDARD ATTRIBUTES
 */    
    
    // For initContextListener.getEnvEntryValue()
    private static final String att_iconDataPath = "iconDataPath";    
    private String iconDataPath = "";        
    private String fullIconDataPath = "";        
    
    private String xmlMemberDataFolder = "xmlMemberDataFolder"; 
    private String xmlMemberPath = "" ;
    
//    private String SyncInitResponseFilename = "SyncInitResponseFilename" ;
    
    // URLs
                            //  /!\     --> Utilité ? 
    private static final String URL_STATS = "/stats" ;                  // --> Tous les 'ListeInfo' et les 'ProduitInfo' d'un user
    
    private static final String URL_STATS_LISTES = "/lst" ;             //  --> Tous les 'ListeResult' + un 'ListeStats' d'une liste d'un user
    private static final String URL_STATS_PRODUITS = "/pdt" ;           //  --> Tous les 'ProduitResult' + un 'ProduitStats' d'un produit d'un user
    
    private static final String URL_STATS_LISTES_RESULT = "/lst/result" ;   // --> Un 'ListeResult' d'une liste d'un user
    private static final String URL_STATS_PRODUITS_RESULT = "/pdt/result" ; // --> Un 'ProduitResult' d'un produit d'un user
    
    private static final String URL_STATS_LISTES_STATS = "/lst/stats" ;     // --> Un 'ListeStats' d'une liste d'un user
    private static final String URL_STATS_PRODUITS_STATS = "/pdt/stats" ;   // --> Un 'ProduitStats' d'un produit d'un user
       
    // Request & Session attributes
    public static final String ATT_DATA = "data";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    
//    public static final String ATT_DETAIL_FRONTEND_LISTE = "DetailFrontendListes";
//    public static final String ATT_MAP_PDT_ICON = "iconMap";
    
    
    public static final String ATT_LISTE_ID = "lst_id";
    public static final String ATT_PRODUIT_ID = "pdt_id";
    public static final String ATT_RESULT_ID = "result_id";
    public static final String ATT_STATS_ID = "stats_id";
    
    
    // Servlet attributes for working
//    private static HashMap<Long, String>   mapProduitIcon  ;
    
//    private String currentUserMail = "" ;
//    public String getCurrentUserMail() {return currentUserMail;}
    
/**
 * @param config
 * @throws ServletException 
 */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); //To change body of generated methods, choose Tools | Templates.
        
        iconDataPath  = InitContextListener.getEnvEntryValue(att_iconDataPath);    
        xmlMemberDataFolder = InitContextListener.getEnvEntryValue(xmlMemberDataFolder);    
//                
        pdtGenForm      = new ProductGeneriqHandler(pdtBaseCtl,iconDataPath , sctgCtl, ctgCtl);
//        pdtInfoForm     = new ProduitInfoHandler(pdtBaseCtl, pdtStatsCtl, pdtInfoCtl);
//        pdtStatsForm    = new ProduitStatsHandler(pdtBaseCtl, pdtStatsCtl, pdtInfoCtl);
////        pdtResult
//        
        listBaseForm    = new ListeHandler(listBaseCtl);
//        listInfoForm    = new ListeInfoHandler(listInfoCtl, listResultCtl, listStatsCtl, listBaseCtl);
//        listResultForm  = new ListeResultHandler(listInfoCtl, listResultCtl, listStatsCtl, listBaseCtl);
//        listStatsForm   = new ListeStatsHandler(listInfoCtl, listResultCtl, listStatsCtl, listBaseCtl);
//        
        ctgForm     = new CategoryHandler(ctgCtl);
        sctgForm    = new SubCategoryHandler(sctgCtl, ctgCtl);
//        esgnForm    = new EnseigneHandler(esgnCtl);
//        qttForm    = new QttHandler(pdtBaseCtl, qttCtl);
        
//        getProductsListe();
//        getCategoriesIdentifiers();
//        getSubCategories();
//        getSubCategoriesIdentifiers();
        
        
        
    }
    
//    private void cleanGarbage() {
//        // Give a new one each time to ensure it's not from an other member !!!
//        setMapProduitIcon(new HashMap<Long, String>());
//    }   

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        cleanGarbage() ;
        
        HttpServletRequest request = req ;
        HttpSession session = request.getSession(false);
        
        Membre mb = (Membre) ServletUtils.getSessionAttrObject(session, ATT_SESSION_USER);         
        
        // Ajoute l'url d'accès aux icones
//        fullIconDataPath = buildIconURL(request);

        // L'action/page demandee
        String action = findAction(request.getRequestURI());
        
        log.debug("STATS action asked : /stats -> " + action);
        
        switch (action) {
            case URL_STATS :
                getGlobalStats(request, resp, mb);
            break;
                
            case URL_STATS_LISTES :
                getGlobalStatsForListe(request, resp, mb);               
            break;
                
            case URL_STATS_PRODUITS :
                getGlobalStatsForProduct(req, resp, mb);
            break;
                
//            case URL_SYNC_ICON :
//                doSyncIcons(req, resp);
//            break;
        }
        
        
    }
        
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        cleanGarbage();
    }
    
    
    private String findAction(String requestURI) {
        
//        log.info("Received URI  = " +requestURI);
        
        requestURI = requestURI.substring(
            requestURI.indexOf(URL_STATS)
        );
        
//        log.info("substr URI = " +requestURI);
        
        if (requestURI.contentEquals(URL_STATS) ) {
            return URL_STATS ;
        } else {
            
            if (requestURI.length() > URL_STATS.length()) {
                
                requestURI = requestURI.substring(URL_STATS.length());
                
                if (requestURI != null && requestURI.length() > 0) {
                    switch (requestURI) {
                        case URL_STATS :
                            return URL_STATS ;
                            
                        case URL_STATS_LISTES :
                            return URL_STATS_LISTES;                            
                            
                        case URL_STATS_PRODUITS :
                            return URL_STATS_PRODUITS;                            
                            
                        case URL_STATS_LISTES_RESULT :
                            return URL_STATS_LISTES_RESULT;                            
                            
                        case URL_STATS_PRODUITS_RESULT :
                            return URL_STATS_PRODUITS_RESULT;                            
                            
                        case URL_STATS_LISTES_STATS :
                            return URL_STATS_LISTES_STATS;                            
                            
                        case URL_STATS_PRODUITS_STATS :
                            return URL_STATS_PRODUITS_STATS;                            
                            
//                        case URL_SYNC_ICON :
//                            return URL_SYNC_ICON;                            
                            
                        case "" :                            
                        default:
                            log.warn("Not supposed to ask this way : " + requestURI);
                            break;
                    }
                }
                // Ne devrais jamais arriver ici
                else {
                    return URL_STATS;
                }
                
            }
            // Ne devrais jamais arriver ici
            else {
                log.warn("Not supposed to ask this way : " + requestURI);
                return "";
            }            
        }
        
        
        
        return "";
    }

    
    /**
     * Method called to display /stats
     * @param request
     * @param resp
     * @param mb
     * @throws IOException 
     */
    private void getGlobalStats (HttpServletRequest request, HttpServletResponse resp, Membre mb) throws IOException {
        
        StatsGlobalResponse globalStatsResp = new StatsGlobalResponse()  ;

        ArrayList<ListeInfoFrontend> listesInfos = new ArrayList<ListeInfoFrontend>();
        ArrayList<ProduitInfo> produitsInfos = new ArrayList<ProduitInfo>();
        
        long userId = mb.getMbId();

        
// 1.0 Les listesInfos + Listes (pour le label, ...)        


        try {
             
            listesInfos = getListeInfoFrontendByUser(userId);
            
        } catch (Exception e) {
            log.error("An error occured trying to get ListesInfo : " + e.getMessage());
            e.printStackTrace();
        }
        
        if (listesInfos != null) {
            
            globalStatsResp.setListsInfos(listesInfos);
        
// 2.0 Pour les ProduitInfo  
            try {
                
                for (ListeInfo l : listesInfos) {
                    produitsInfos.addAll(getProduitInfoFrontendByListe(l.getListesInfoListe()));
                    log.debug("Added ProduitsInfos from Liste ID [" + l.getListesInfoListe() + "]");
                }
                
                if (produitsInfos != null){
                    
                    globalStatsResp.setProduitsInfos(produitsInfos);
                    
                }

            } catch (Exception e) {
                log.error("An error occured trying to get ProduitsInfo : " + e.getMessage());
                e.printStackTrace();
            }            
            
        }
      
        
        

        resp.setContentType("application/json"); 
        resp.getWriter().write(GsonConverter.toJson(globalStatsResp));   
        
    }
    
    private void getGlobalStatsForListe (HttpServletRequest request, HttpServletResponse resp, Membre mb) throws IOException {
        
        StatsGlobalResponse globalStatsResp = new StatsGlobalResponse()  ;

        ArrayList<ListeInfoFrontend> listesInfos = new ArrayList<ListeInfoFrontend>();
        ArrayList<ProduitInfo> produitsInfos = new ArrayList<ProduitInfo>();
        
        long userId = mb.getMbId();

        
// 1.0 Les listesInfos + Listes (pour le label, ...)        


        try {
             
            listesInfos = getListeInfoFrontendByUser(userId);
            
        } catch (Exception e) {
            log.error("An error occured trying to get ListesInfo : " + e.getMessage());
            e.printStackTrace();
        }        
      
        
        
// 2.0 Pour les ProduitInfo        
        
        globalStatsResp.setListsInfos(listesInfos);
        
        
        

        resp.setContentType("application/json"); 
        resp.getWriter().write(GsonConverter.toJson(globalStatsResp));   
        
    }    
    
    private void getGlobalStatsForProduct (HttpServletRequest request, HttpServletResponse resp, Membre mb) throws IOException {
        
        StatsGlobalResponse globalStatsResp = new StatsGlobalResponse()  ;

        ArrayList<ListeInfoFrontend> listesInfos = new ArrayList<ListeInfoFrontend>();
        ArrayList<ProduitInfo> produitsInfos = new ArrayList<ProduitInfo>();
        
        long userId = mb.getMbId();

        
// 1.0 Les listesInfos + Listes (pour le label, ...)        


        try {
             
            listesInfos = getListeInfoFrontendByUser(userId);
            
        } catch (Exception e) {
            log.error("An error occured trying to get ListesInfo : " + e.getMessage());
            e.printStackTrace();
        }        
      
        
        
// 2.0 Pour les ProduitInfo        
        
        globalStatsResp.setListsInfos(listesInfos);
        
        
        

        resp.setContentType("application/json"); 
        resp.getWriter().write(GsonConverter.toJson(globalStatsResp));   
        
    }    
    
    private ArrayList<ListeInfoFrontend> getListeInfoFrontendByUser (long userId) {
    
        ArrayList<ListeInfoFrontend> listesInfos = new ArrayList<ListeInfoFrontend>();
        ArrayList<ListeInfo> basicInfos = new ArrayList<ListeInfo> ();
        ArrayList<Liste> basicListes = new ArrayList<Liste>();        
        
        basicInfos.addAll(listInfoCtl.findByUser(userId));

        if (basicInfos.isEmpty()) { 
            log.error("No listeInfo found for userID [" + userId + "]");
        } else {

            basicListes.addAll(listBaseCtl.findByUser(userId));


            for (ListeInfo basicInfo : basicInfos) {
    //            availableLists.put(
                long lstId = basicInfo.getListesInfoListe();

                Liste bListe = null ;

                for (Liste basicListe : basicListes) {
                    if (basicListe.getLstId() == lstId) { bListe = basicListe ;}
                    if (bListe != null) break;
                }

                ListeInfoFrontend listInfo = new ListeInfoFrontend(basicInfo);
//                        = (ListeInfoFrontend) basicInfo ;
//                    = new ListeInfoFrontend(
//                        bListe.getLstLabel(),
//                        ServletUtils.getFormattedDateNow(),
//                        getPdtCount(bListe.getLstProduits())
//                    ) ;

                // Should be extracted from USER file 'UserStatsHistory.json'
                listInfo.setDateUpdated(ServletUtils.getFormattedDateNow());

                listInfo.setListeLabel(bListe.getLstLabel());
                listInfo.setPdtCount(getPdtCount(bListe.getLstProduits()));

                listesInfos.add(listInfo);

                log.info("Added ListeInfo with ID[" + listInfo.getListesInfoId() + "]"
                    + " for Liste with ID[" + listInfo.getListesInfoListe() + "]"
                );
            }                  

        }        
        
        return listesInfos;
        
    }

    private ArrayList<ProduitInfo> getProduitInfoFrontendByListe (long listId) {
    
        /**
         * TO REBUILD FOR PRODUCT BY LISTE
         */
        
        ArrayList<ProduitInfo> pdtInfos = new ArrayList<>();
        
//        ArrayList<ListeInfoFrontend> listesInfos = new ArrayList<ListeInfoFrontend>();
//        ArrayList<ListeInfo> basicInfos = new ArrayList<ListeInfo> ();
//        ArrayList<Liste> basicListes = new ArrayList<Liste>();        
//        
//        basicInfos.addAll(listInfoCtl.findByUser(listId));
//
//        if (basicInfos.isEmpty()) { 
//            log.error("No listeInfo found for userID [" + listId + "]");
//        } else {
//
//            basicListes.addAll(listBaseCtl.findByUser(listId));
//
//
//            for (ListeInfo basicInfo : basicInfos) {
//    //            availableLists.put(
//                long lstId = basicInfo.getListesInfoListe();
//
//                Liste bListe = null ;
//
//                for (Liste basicListe : basicListes) {
//                    if (basicListe.getLstId() == lstId) { bListe = basicListe ;}
//                    if (bListe != null) break;
//                }
//
//                ListeInfoFrontend listInfo = new ListeInfoFrontend(basicInfo);
////                        = (ListeInfoFrontend) basicInfo ;
////                    = new ListeInfoFrontend(
////                        bListe.getLstLabel(),
////                        ServletUtils.getFormattedDateNow(),
////                        getPdtCount(bListe.getLstProduits())
////                    ) ;
//
//                // Should be extracted from USER file 'UserStatsHistory.json'
//                listInfo.setDateUpdated(ServletUtils.getFormattedDateNow());
//
//                listInfo.setListeLabel(bListe.getLstLabel());
//                listInfo.setPdtCount(getPdtCount(bListe.getLstProduits()));
//
//                listesInfos.add(listInfo);
//
//                log.info("Added ListeInfo with ID[" + listInfo.getListesInfoId() + "]"
//                    + " for Liste with ID[" + listInfo.getListesInfoListe() + "]"
//                );
//            }                  
//
//        }        
//        
//        return listesInfos;
        
        return  pdtInfos;
        
    }

    private int getPdtCount (String pdtLine) {
        
        int pdtCount ;
        
        // Si pas de produits
        if (pdtLine == null) {
            pdtCount = 0 ;
        } else if (pdtLine.length() == 0 ) {
            pdtCount = 0 ;
        } 
        // Si   AU MOINS  1 produit
        else {
            /* Le nbre de produit = nbre de separateurs + 1 */
            // Premier separateur '_'
            int countSeparator = pdtLine.length() - pdtLine.replace("_", "").length() ;
            // Au moins 1 produit ==> +1
            int countProduit = countSeparator + 1 ;
            
            // Si pas plus de  1 ...
            if (countProduit == 1) {
                
                // ... on verifie qu'il n'y pas non plus de 2e separateur '-'
                countSeparator = pdtLine.length() - pdtLine.replace("_", "").length() ;
            
                // Si == 1 aussi on est certain d'avoir 1 seul produit
                                 // Au moins 1 produit ==> +1
//                if ( countProduit < countSeparator + 1 ) {
                
                // Si plus que 1 ==> (countSeparator'-' > 0)
                if ( countSeparator + 1  > countProduit) {
                    // final count == countSeparator + 1
                } 
                
                else {
                    // final count == 1
                    countProduit = countSeparator + 1  ;
                }
            }
            pdtCount = countProduit ;
        }
        return pdtCount ;
    }
    

}
