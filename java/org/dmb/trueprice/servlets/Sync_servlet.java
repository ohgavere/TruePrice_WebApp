/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.servlets;

import java.io.File;
import java.io.FileReader;
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
import org.dmb.trueprice.controllers.EnseigneJpaController;
import org.dmb.trueprice.controllers.ListesInfoJpaController;
import org.dmb.trueprice.controllers.ListesJpaController;
import org.dmb.trueprice.controllers.ListesResultJpaController;
import org.dmb.trueprice.controllers.ListesStatsJpaController;
import org.dmb.trueprice.controllers.ProduitInfoJpaController;
import org.dmb.trueprice.controllers.ProduitListeJpaController;
import org.dmb.trueprice.controllers.ProduitStatsJpaController;
import org.dmb.trueprice.controllers.QttDetailJpaController;
import org.dmb.trueprice.controllers.SubcategoryJpaController;
import org.dmb.trueprice.entities.Category;
import org.dmb.trueprice.entities.Liste;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.entities.QttDetail;
import org.dmb.trueprice.entities.Subcategory;
import org.dmb.trueprice.handlers.CategoryHandler;
import org.dmb.trueprice.handlers.EnseigneHandler;
import org.dmb.trueprice.handlers.ListeHandler;
import org.dmb.trueprice.handlers.ListeInfoHandler;
import org.dmb.trueprice.handlers.ListeResultHandler;
import org.dmb.trueprice.handlers.ListeStatsHandler;
import org.dmb.trueprice.handlers.ProductGeneriqHandler;
import org.dmb.trueprice.handlers.ProduitInfoHandler;
import org.dmb.trueprice.handlers.ProduitStatsHandler;
import org.dmb.trueprice.handlers.QttHandler;
import org.dmb.trueprice.handlers.SubCategoryHandler;
import org.dmb.trueprice.objects.AvailableList;
import org.dmb.trueprice.objects.ListHeader;
import org.dmb.trueprice.objects.ListeDetailFrontend;
import org.dmb.trueprice.objects.ListeFrontend;
import org.dmb.trueprice.objects.ProduitFrontend;
import org.dmb.trueprice.objects.SyncGetterRequest;
import org.dmb.trueprice.objects.SyncGetterResponse;
import org.dmb.trueprice.objects.SyncInitRequest;
import org.dmb.trueprice.objects.SyncInitResponse;
import org.dmb.trueprice.utils.internal.FileUtils;
import org.dmb.trueprice.utils.internal.GsonConverter;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;

/**
 *
 * @author Guitch
 */
@WebServlet ( name = "SyncService", 
        urlPatterns = {
            "/sync",
            "/sync/getter",
            "/sync/setter"
//            "/sync/icons"
        })
public class Sync_servlet extends HttpServlet {
     
    private static final Logger log 
        = InitContextListener.getLogger( Sync_servlet.class) ;
        
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
    private static ProduitInfoHandler pdtInfoForm ;
    
    @EJB 
    private  ProduitStatsJpaController pdtStatsCtl ;    
    private static ProduitStatsHandler pdtStatsForm ;    
        
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
    private static ListeInfoHandler listInfoForm ;
    
    @EJB 
    private  ListesStatsJpaController listStatsCtl ;    
    private static ListeStatsHandler listStatsForm ;
    
    @EJB 
    private  ListesResultJpaController listResultCtl ;    
    private static ListeResultHandler listResultForm ;
    
/**
 *  OTHER EJBs
 */       
    @EJB
    private CategoryJpaController ctgCtl ;
    private CategoryHandler ctgForm;
    
    @EJB
    private SubcategoryJpaController sctgCtl ;    
    private SubCategoryHandler sctgForm;
    
    @EJB 
    private  EnseigneJpaController esgnCtl ;    
    private  EnseigneHandler esgnForm ;    
    
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
    
    private String SyncInitResponseFilename = "SyncInitResponseFilename" ;
    
    // URLs
    private static final String URL_SYNC = "/sync" ;
    private static final String URL_SYNC_GETTER = "/getter" ;
    private static final String URL_SYNC_SETTER = "/setter" ;
//    private static final String URL_SYNC_ICON = "/icons" ;
       
    // Request & Session attributes
    public static final String ATT_DATA = "data";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    
    public static final String ATT_DETAIL_FRONTEND_LISTE = "DetailFrontendListes";
    public static final String ATT_MAP_PDT_ICON = "iconMap";
    
    // Servlet attributes for working
    private static HashMap<Long, String>   mapProduitIcon  ;
    
    private String currentUserMail = "" ;
    public String getCurrentUserMail() {return currentUserMail;}
    
/**
 * @param config
 * @throws ServletException 
 */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); //To change body of generated methods, choose Tools | Templates.
        
        iconDataPath  = InitContextListener.getEnvEntryValue(att_iconDataPath);    
        xmlMemberDataFolder = InitContextListener.getEnvEntryValue(xmlMemberDataFolder);    
                
        pdtGenForm      = new ProductGeneriqHandler(pdtBaseCtl,iconDataPath , sctgCtl, ctgCtl);
        pdtInfoForm     = new ProduitInfoHandler(pdtBaseCtl, pdtStatsCtl, pdtInfoCtl);
        pdtStatsForm    = new ProduitStatsHandler(pdtBaseCtl, pdtStatsCtl, pdtInfoCtl);
//        pdtResult
        
        listBaseForm    = new ListeHandler(listBaseCtl);
        listInfoForm    = new ListeInfoHandler(listInfoCtl, listResultCtl, listStatsCtl, listBaseCtl);
        listResultForm  = new ListeResultHandler(listInfoCtl, listResultCtl, listStatsCtl, listBaseCtl);
        listStatsForm   = new ListeStatsHandler(listInfoCtl, listResultCtl, listStatsCtl, listBaseCtl);
        
        ctgForm     = new CategoryHandler(ctgCtl);
        sctgForm    = new SubCategoryHandler(sctgCtl, ctgCtl);
        esgnForm    = new EnseigneHandler(esgnCtl);
        qttForm    = new QttHandler(pdtBaseCtl, qttCtl);
        
//        getProductsListe();
//        getCategoriesIdentifiers();
//        getSubCategories();
//        getSubCategoriesIdentifiers();
        
        
        
    }
    
    private void cleanGarbage() {
        // Give a new one each time to ensure it's not from an other member !!!
        setMapProduitIcon(new HashMap<Long, String>());
    }   

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        cleanGarbage() ;
        
        HttpServletRequest request = req ;
        HttpSession session = request.getSession(false);
        
        Membre mb = (Membre) ServletUtils.getSessionAttrObject(session, ATT_SESSION_USER);         
        
        // Ajoute l'url d'accès aux icones
        fullIconDataPath = buildIconURL(request);

        // L'action/page demandee
        String action = findAction(request.getRequestURI());
        
        switch (action) {
            case URL_SYNC :
                doSyncInit(request, resp, mb);
            break;
                
            case URL_SYNC_GETTER :
                doSyncGetter(request, resp, mb);               
            break;
                
            case URL_SYNC_SETTER :
                doSyncSetter(req, resp);
            break;
                
//            case URL_SYNC_ICON :
//                doSyncIcons(req, resp);
//            break;
        }
        
        
    }
        
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        cleanGarbage();
    }
    
    
    private String findAction(String requestURI) {
        
//        log.info("Received URI  = " +requestURI);
        
        requestURI = requestURI.substring(
            requestURI.indexOf(URL_SYNC)
        );
        
//        log.info("substr URI = " +requestURI);
        
        if (requestURI.contentEquals(URL_SYNC) ) {
            return URL_SYNC ;
        } else {
            
            if (requestURI.length() > URL_SYNC.length()) {
                
                requestURI = requestURI.substring(URL_SYNC.length());
                
                if (requestURI != null && requestURI.length() > 0) {
                    switch (requestURI) {
                        case URL_SYNC :
                            log.warn("Not supposed to ask this way : " + requestURI);
                            return URL_SYNC ;
                            
                        case URL_SYNC_GETTER :
                            return URL_SYNC_GETTER;                            
                            
                        case URL_SYNC_SETTER :
                            return URL_SYNC_SETTER;                            
                            
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
                    return URL_SYNC;
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

    
    
    private void doSyncInit (HttpServletRequest request, HttpServletResponse resp, Membre mb) throws IOException {
        
///////////
//        
//  READ REQUEST FOR SyncInitRequest
//        
        String data = ServletUtils.getRequestAttrValue(request, ATT_DATA);               

        SyncInitRequest initRequest = GsonConverter.fromJsonInitRequest(data);

        if (initRequest == null ) { 
            log.warn("It seems like no data was found ... BODY:[" 
                + request.getReader().readLine() + "]");
        
        } else {
            log.info(initRequest.toString());
            log.info("\n" + "Finished reading. Gonna try to get list modif Dates from filesystem" + "\n");
        }
        
///////////
//        
//  WRITE RESPONSE WITH SyncInitResponse
//          
        
        // Récupere le fichier dans le dossier du membre
//                File f = new File(getXmlMemberFullPath(mb) + File.separator + SyncInitResponseFilename ) ;
//                SyncInitResponse initresponse = GsonConverter.fromJsonInitResponse(new FileReader(f));

        SyncInitResponse initresponse = ServletUtils.getMemberInitResponse(mb.getMbMail());

        // Si SyncInitResponse n'existe pas, il faut le creer
        if (initresponse == null) {
            log.info("SyncInitResponse seems to be an empty. Gonna try to fill it in.");
            initresponse = createSyncInitResponse(mb);
            log.info("SyncInitResponse creation [ " + (initresponse == null ? "FAILED" : "SUCCESS") + " ]");
        }

        resp.setContentType("application/json"); 
        resp.getWriter().write(GsonConverter.toJson(initresponse));   
        
    }    
    
    private SyncInitResponse createSyncInitResponse(Membre mb) {
        
        SyncInitResponse initResponse ;

//        HashMap<Long , String> availableLists =  new HashMap<Long, String>();
        ArrayList<AvailableList> availableLists = new ArrayList<AvailableList>() ;
        
        List <Liste> backendListes = new ArrayList<Liste>();

        // 1.0 Recuperer les listes du membre        
        try {
            backendListes = listBaseForm.getListByMember(mb.getMbId());
        } catch (Exception e) {
            log.warn("Could not retrieve user Listes : [" + e.getMessage() + "]");
        }


        // 2.0 Construyire SyncInitResponse
        for (Liste liste : backendListes) {
//            availableLists.put(
            AvailableList avListe = new AvailableList(
                Long.valueOf(Integer.toString(liste.getLstId())) ,
                liste.getLstLabel(),
                ServletUtils.getFormattedDateNow()
            ) ;
            
            avListe.setPdtCount(getPdtCount(liste.getLstProduits()));
            
            availableLists.add(avListe);
        }

        initResponse = new SyncInitResponse(availableLists);

        log.info(initResponse.toString());

        // 3.0 Ecrire le fichier
        try {
            ServletUtils.writeMemberSyncInitResponse(initResponse, mb.getMbMail());
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Could not write member SyncInitResponse : [" + e.getMessage() + "]");
        }        
        
        return initResponse;
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
    
    
    private void doSyncGetter (HttpServletRequest request, HttpServletResponse resp, Membre mb) throws IOException{
        
///////////
//        
//  READ REQUEST FOR SyncGetterRequest
//        
        String data = ServletUtils.getRequestAttrValue(request, ATT_DATA);               

        SyncGetterRequest getterRequest ;
        try {
            getterRequest = GsonConverter.fromJsonGetterRequest(data);
        } catch (Exception e) {
            log.warn("SyncGetterRequest not parsed ... [" + e.getMessage() +"]");
            log.warn("\tGonna make sample for testing ...");
            
            HashSet<Long> sampleIDs = new HashSet<Long>();
            sampleIDs.add(new Long("7"));
            sampleIDs.add(new Long("2"));
            
            getterRequest = new SyncGetterRequest(sampleIDs);
            
            log.info("\tCreated sample ==> \n" + GsonConverter.toJson(getterRequest));
        }

        if (getterRequest == null ) { 
            log.warn("It seems like no data was found ... BODY:[" 
                + request.getReader().readLine() + "]");
        
        } else {
            log.info(getterRequest.toString());
            log.info("\n" + "Finished reading. Gonna try to get list modif Dates from filesystem" + "\n");
        }
        
///////////
//        
//  WRITE RESPONSE WITH SyncGetterResponse
//  
        
// 1.0      Recuperer les listes du membre
//  1.1 Recuperer les produits de chaque liste
    //  1.1.1   Recensser chaque (Sub)Categorie
    //  1.1.2   Recuperer les noms des images de chaque produit
    
// 2.1      Construire les produits frontend
// 2.2      Construire les listes frontend
        
// 3.0     Recuperer les infos globales des listes et produits
//  3.1 Recuperer les infos des listes
//  3.2 Recuperer les infos des produits
        
// 4.0      Construire la liste des illustrations
        
// 5.0      SEND ALL 
        
        //////////////////               
        
        // Toutes les listes du membre
        List      <Liste>                backendListes       = new ArrayList<Liste>();
        ArrayList <ListeFrontend>        frontendListes      = new ArrayList<ListeFrontend>();
        ArrayList <ListeDetailFrontend>  finalFrontendListes = new ArrayList<ListeDetailFrontend>();
        
//    Now It's done in cleanGarbage() to ensure it's not available from an other 
//    url before being reset by an other request here...
//        // Give a new one each time to ensure it's not from an other member !!!
//        setMapProduitIcon(new HashMap<Long, String>());
        
        
        //////////////////
        
// 1.0      Recuperer les listes du membre        
        try {
            backendListes = listBaseForm.getListByMember(mb.getMbId());
        } catch (Exception e) {
            log.warn("Could not retrieve user Listes : [" + e.getMessage() + "]");
        }
        
        if (backendListes != null) {
            
            List <Liste> tempBackendListes = new ArrayList<Liste>();
            Liste liste ;
//            int loopstatus = 0 ;
            
            for (int loopstatus = 0  ; loopstatus < backendListes.size() ; loopstatus++) {
                
                liste = backendListes.get(loopstatus);
                
                log.info("working for liste \tID:[" + liste.getLstId() + "]\t LABEL:[" + liste.getLstLabel() +"]");
                
//                if (liste.getLstId().toString() == Long.toString(listeId)) {
                if (getterRequest.getAskedLists().contains(Long.valueOf(liste.getLstId().toString()))) {
                    log.info("This liste was asked for download \tID:[" + liste.getLstId() + "]");
                    tempBackendListes.add(liste);
                } else {
                    log.info("This liste was not asked for download \tID:[" + liste.getLstId() + "] and will be REMOVED");
//                    finalFrontendListes.remove(liste);
                }
                
            }
            
            backendListes = tempBackendListes ;
        }
        
        
//  1.1 Recuperer les produits de chaque liste
        
        try {
            frontendListes = getPdtObjectsByMemberList(backendListes);
        } catch (Exception e) {
            log.warn("Error append retrieving products by listes : [" + e.getMessage() + "]");
        }      
        
// 2.0      Construire les produits frontend, listes frontend
//  2.1      Recuperer les noms des images de chaque produit
// 3.0      Recuperer les infos globales des listes et produits          
// 4.0      Construire la liste des illustrations        
        
        try {
            finalFrontendListes = buildListesDetailFrontend(frontendListes);            
        } catch (Exception e) {
            log.warn("Error append building detail frontend listes : [" + e.getMessage() + "]");
            e.printStackTrace();
        } 

// 5.0 Supprimer les resultats non voulus
        
//        if (getterRequest != null) {
//            
//            ListeDetailFrontend liste ;
////            int loopstatus = 0 ;
//            
//            for (int loopstatus = 0  ; loopstatus < finalFrontendListes.size() ; loopstatus++) {
//                
//                liste = finalFrontendListes.get(loopstatus);
//                
//                log.info("working for liste \tID:[" + liste.getLstId() + "]\t LABEL:[" + liste.getLstLabel() +"]");
//                
////                if (liste.getLstId().toString() == Long.toString(listeId)) {
//                if (getterRequest.getAskedLists().contains(Long.valueOf(liste.getLstId().toString()))) {
//                    log.info("This liste was asked for download \tID:[" + liste.getLstId() + "]");
//                } else {
//                    log.info("This liste was not asked for download \tID:[" + liste.getLstId() + "] and will be DELETED from SyncGetterResponse");
//                    finalFrontendListes.remove(liste);
//                }
//                
//            }
//        }
        
//        request.setAttribute(ATT_DETAIL_FRONTEND_LISTE, finalFrontendListes);
//        request.setAttribute(ATT_MAP_PDT_ICON, mapProduitIcon);
 
// 6.0 Convertir les DetailFrontendListes en JSON avec GSON
       
//        String jsonData = getJsonData(finalFrontendListes);        
        String jsonData ;
        SyncGetterResponse getterResponse = new SyncGetterResponse(finalFrontendListes,mapProduitIcon) ;
        
        jsonData = GsonConverter.toJson(getterResponse);
        
//        log.info(getterResponse);
                 
// 7.0 Envoyer le String JSON en attribut ou en "DIRECT" si faisable
        
//        request.setAttribute(ATT_DETAIL_FRONTEND_LISTE, finalFrontendListes);
//        request.setAttribute(ATT_MAP_PDT_ICON, mapProduitIcon);       
        
        
          
        resp.setContentType("application/json"); 
        resp.getWriter().write(jsonData);
                
    }
    
    private void doSyncSetter   (HttpServletRequest req, HttpServletResponse resp) {}
    
   
            
    private String getJsonData(ArrayList<ListeDetailFrontend> listes ) {
        
        String gloablJsonOutput = "";
//                "\n############### TESTING = Entity to JSON with GSON ###################\n" ;
        
        int count = 0 ;
        
        for ( ListeDetailFrontend liste : listes) {
        
//            gloablJsonOutput += "\n############### Entity [" + (++count) + "] ###################\n" ;
            
            gloablJsonOutput += GsonConverter.toJson(liste) + "\n" ;
            
        }    
        
        return gloablJsonOutput;
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
        
        String fullIconURL = "";
        
        log.info("[srvName : srvPort] >> [" 
                + srvName + " : " + srvPort + "]" );                        

        log.info("Icon Data Path is >> " + iconDataPath );   
        
        fullIconURL = "https://" + srvName + ":" + srvPort+ iconDataPath ;
        
        log.info("return fullIconUrl : >> " + fullIconURL);   
        
         return fullIconURL;
    }    
        
     
 private ArrayList<ListeFrontend> getPdtObjectsByMemberList(List<Liste> listMembersListes) {
    
        ArrayList<ListeFrontend> returned = new ArrayList<ListeFrontend>();
        
        for (Liste liste : listMembersListes) {
            
            ArrayList<Produit> pdtObjList = new ArrayList<Produit>();
            
            if (liste.getLstProduits() != null ) {
                
                for (String pdtId : liste.getLstProduits().split("_")) {
    
                    Produit pdt = null ;
                    
                    if (pdtId.length() > 0) {
                        pdt = pdtBaseCtl.findProduitListe(Long.valueOf(pdtId));
                    } else {
                        log.warn("ID not found : [" + pdtId + "]");
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
            
            // Si on a trouvé des objets
            if ( ! pdtObjList.isEmpty()) {
                // Chaque liste reconstruite pour display avec produits Objects
                returned.add(new ListeFrontend(liste, pdtObjList)); 
                log.info("Just added list U " + liste.getLstUser() 
                    + " || L " + liste.getLstId() + " for pdt Objects.");
            }
            // Si pas 'objet rouvé, la liste n'est pas ajoutée car inutilisable
            else {
                log.warn("The list U " + liste.getLstUser() 
                    + " || L " + liste.getLstId() 
                    + " => WAS REMOVED BECAUSE OF EMPTY NO PRODUCTS INSIDE"
                );
            }
            
            
        }
        
        return  returned;
    }    
 
//  private ArrayList<ListeDetailFrontend> getPdtFrontObjectsByMemberList(ArrayList<ProduitFrontend> listProduits) {
//      
//    ArrayList<ListeDetailFrontend> returned = new ArrayList<ListeDetailFrontend>();
//
//      
//    return returned ;
//  }
 
    private ArrayList <ListeDetailFrontend> buildListesDetailFrontend (ArrayList<ListeFrontend>  listes ){
        
        ArrayList <ListeDetailFrontend>  finalFrontendListes = new ArrayList<ListeDetailFrontend>(); ;
        
        ArrayList <ListeFrontend>  frontendListes = listes ;
        ArrayList<ProduitFrontend>  frontendProducts = null ;
        
        QttDetail   qttObj   = null ;
        Category    catgObj  = null ;
        Subcategory scatgObj = null ;        
        
        String esgnLabel = "" ;
        
        String strLog = "" ;
        
            for (ListeFrontend liste : frontendListes) {
                
                strLog += "\n\tStart build FrontendListe \tLABEL:[" + liste.getLstLabel() + "]";
                log.info("\tStart build FrontendListe \tLABEL:[" + liste.getLstLabel() + "]");
                
                try {
                    esgnLabel = liste.getEsgnLabel();
                    if (esgnLabel == null) {
                        esgnLabel = esgnCtl.findEnseigne(liste.getLstEnseigne()).getEsgnLabel();
                    }                    
                } catch (Exception e) {
//                    log.warn("The ESGN label could not be found.");
                    strLog += "\n\t\t[![The ESGN label could not be found for this liste.]!]";
                }
                
                frontendProducts = new ArrayList<ProduitFrontend>();
                
                for (Produit produit : liste.getPdtObjects()) {
                    
//                    str += "\n\t\tStart get Produit Data NOM:[" + produit.getPdtNom() + "]";
                    
                    Integer idQtt = Integer.valueOf(Long.toString(produit.getPdtQtt()));
//                    qttObj = qttForm.createRandomQtt();    
                    qttObj = qttForm.getQuantityDetailsById(idQtt);
//                    strLog += "\n got Qtt   :[" + qttObj.toString() + "]";
                    
                    catgObj = ctgCtl.findCategory(produit.getPdtCategory());
//                    str += "\n\t\t\t got Catg  :[" + catgObj.toString() + "]";
                    
                    scatgObj = sctgCtl.findSubcategory(produit.getPdtSubcategory());
                    
                    // ProduitFrontend constructor only need scatg label so give it empty is enough. 
                    // Only need to check it further in Android App.
                    if (scatgObj == null) { scatgObj = new Subcategory(-1); scatgObj.setSctgLabel("");}
                    
//                    str += "\n\t\t\t got SCatg :[" + scatgObj.toString() + "]";
                    
//                    // Test not needed because map replace value for an existing key !
////                    if ( ! mapProduitIcon.containsKey(produit.getPdtId())) {
//                        mapProduitIcon.put(produit.getPdtId(), produit.getPdtLink());
////                        str += "\n\t\t\t got PAIR [id / icon]:[" + produit.getPdtId() + " / " + produit.getPdtLink() + "]";                    
////                    }
                    
                    String buildedUrl = fullIconDataPath
                        + (("GENERIC".equals(produit.getPdtProperty())) ? 
                            "/generic/" 
                            : "/members/"+getCurrentUserMail()+"/" )
                        + produit.getPdtLink()
                    ;
                    
                    
                    
                    addIconToCurrentMap(produit.getPdtId(), buildedUrl);
                    
                    
                    frontendProducts.add(new ProduitFrontend(qttObj, produit, catgObj, scatgObj , "marque", "distributeur"));
                    
//                    strLog += "\n\t\tJust added ProduitFrontend \tLABEL:[" + produit.getPdtNom()+ "]\t"
                    log.info("Just added ProduitFrontend \tLABEL:[" + produit.getPdtNom()+ "]\n"
                        +  "\tCATG:[" + catgObj.getCtgLabel()+ "]"
                        +  "\tSCATG:[" + scatgObj.getSctgLabel()+ "]"
                        +  "\tUNIT:[" + qttObj.getQttMesure() + "]"
                        +  "\tVALUE:[" + qttObj.getQttQuantite() + "]"
                        +  "\tTVA:[" + produit.getPdtTvaTaux() + "]"
                        +  "\tIconURI:[" + buildedUrl + "]"
                    );
                }
                
                finalFrontendListes.add(new ListeDetailFrontend(liste, frontendProducts, esgnLabel));
                
                strLog += "\n\tJust added FinalFrontendListe \tLABEL:[" 
                    + liste.getLstLabel() + "]"
                    +  "\tESGN:[" + esgnLabel + "] <-> ESGN:[" + liste.getEsgnLabel() + "] \n\t-------------";
                
            }
                
            strLog += "\nJust FINISHED to build all FinalFrontendListe \tSIZE:[" + finalFrontendListes.size() + "]";
            
            log.info(strLog);
            
            return finalFrontendListes ;
    }
        
 
    private static void setMapProduitIcon(HashMap<Long, String> mapProduitIcon) {
        Sync_servlet.mapProduitIcon = mapProduitIcon;
    }

    private static HashMap<Long, String> getMapProduitIcon() {
        return mapProduitIcon;
    }

    private static void addIconToCurrentMap(Long id, String link){
        Sync_servlet.mapProduitIcon.put(id, link);
    }    
 
    
    
    
    
    
//    
//
//    private String getCatgNameWithID(long catgID) {
//        String refCatgName = "";
//        switch (Long.toString(catgID)) { 
//          
//            case "0":
//            case "GENERIC":
//            case "NONE":
//            case "NULL":
//            case "":
//                refCatgName = "";
//            break;
//              
//            default:
//                refCatgName = InitContextListener.getShortListCategories(0).get(Long.toString(catgID)); 
//            break;
//        }
//        log.info("Got Catg Name : " + refCatgName);
//        return refCatgName;
//    }
//    private String getSCatgNameWithID(long scatgID) {
//          String refSCatgName = "";
//
//          switch (Long.toString(scatgID)) { 
//              
//              case "0":
//              case "GENERIC":
//              case "NONE":
//              case "NULL":
//              case "":
//                  refSCatgName = "";
//              break;
//                  
//              default:
//                refSCatgName = InitContextListener.getShortListSubCategories(0).get(Long.toString(scatgID)); 
//              break;
//          }
//
//          log.info("Got SCatg Name : " + refSCatgName);
//          
//          return refSCatgName;
//    }    
 
}
