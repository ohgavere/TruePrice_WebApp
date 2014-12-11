/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.servlets;

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
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.CategoryJpaController;
import org.dmb.trueprice.controllers.ListesJpaController;
import org.dmb.trueprice.controllers.ProduitListeJpaController;
import org.dmb.trueprice.controllers.SubcategoryJpaController;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.entities.Subcategory;
import org.dmb.trueprice.handlers.CategoryHandler;
import org.dmb.trueprice.handlers.ListeHandler;
import org.dmb.trueprice.handlers.ProductGeneriqHandler;
import org.dmb.trueprice.handlers.SubCategoryHandler;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;


/**
 *
 * @author Guiitch
 */
@WebServlet (   
        urlPatterns = {
            "/produits",
            "/produits/view", 
            "/produits/stats",
//            "/produits/insert",
            "/produits/customize"
//                ,
//            "/produits/add", "/produits/manage", 
//            "/produits/search", "/produits/remove",
//            "/produits/share"
        } )

public class Produits_servlet extends HttpServlet {

    @EJB
    private ProduitListeJpaController pdtCtl ;
        
    private static final Logger log = InitContextListener.getLogger(Produits_servlet.class) ;
    
    public static final String VIEW_PRODUITS = "/WEB-INF/produits.jsp";
    public static final String VIEW_PRODUITS_GEN = "/WEB-INF/info-produit.jsp";

    private static final String URL_PRODUITS_PUBLIC = "/produits" ;
    private static final String URL_PRODUITS_DETAIL = "/view" ;
    private static final String URL_PRODUITS_SORT = "/sort" ;

    public static final String ATT_SESSION_USER = "sessionUtilisateur";    
    
    private static final String att_Pdt = "Pdt";
    private static final String att_detail_PdtId = "pdt_id";
    private static final String att_refCatgName = "CatgName";
    private static final String att_refSCatgName = "SCatgName";
    
    private static final String att_ListPdt = "listPdt";
//    private static ArrayList<ProduitListe> listProducts ;   
    
    private static final String att_ShortListCatg = "slistCatg";
//    private static HashMap<String, String> shortListCategories ;
    
    private static final String att_ShortListMbListes = "lstLabels";
//    private static HashMap<String, String> shortListCategories ;
    
    private static final String att_ListSCatg = "listSCatg";
//    private static ArrayList<Subcategory> listSubCategories ;
    
//    private static final String att_ShortListSCatg = "slistSCatg";    
//    private static HashMap<String, String> shortListSubCategories ; //=  new HashMap<String, String>();
    
    private static final String att_iconDataPath = "iconDataPath";    
    private String iconDataPath = "";
    private Boolean iconPathIsOK = false;

    
    private static ProductGeneriqHandler pdtGenForm ;    
    private static CategoryHandler ctgForm ;    
    private static SubCategoryHandler sctgForm ;       
    private static ListeHandler lstForm ;       
    

    
    @EJB
    private CategoryJpaController ctgCtl ;
    
    @EJB
    private SubcategoryJpaController sctgCtl ;
    
    @EJB
    private ListesJpaController lstCtl ;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); //To change body of generated methods, choose Tools | Templates.
        
//        log = InitContextListener.getLogger(Produits_servlet.class);
        
        iconDataPath = InitContextListener.getEnvEntryValue(att_iconDataPath);   
        
        pdtGenForm = new  ProductGeneriqHandler(pdtCtl, iconDataPath, sctgCtl, ctgCtl);
        ctgForm = new  CategoryHandler(ctgCtl);
        sctgForm  = new SubCategoryHandler(sctgCtl, ctgCtl);
        lstForm = new ListeHandler(lstCtl);
        
        getProductsListe();
        getCategoriesIdentifiers();
        getSubCategories();
        getSubCategoriesIdentifiers();
        
    }
    
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods.">
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String action = findAction(request.getRequestURI());
        
        HttpSession session = request.getSession(false);
        
        Membre mb = null ;
        
        if (session != null) {
//            mb = ServletUtils.getUserFromSession(session, ATT_SESSION_USER);
            mb = (Membre) ServletUtils.getSessionAttrObject(session, ATT_SESSION_USER);
        }
            
        // Le faire pour chaque requete !!
        // Ajoute l'url d'accès aux icones
        request.setAttribute(att_iconDataPath,
            // ternaire : build Path if 1st request
            (iconPathIsOK ? iconDataPath
//              : getIconDataPath(request) )
            : buildIconURL(request) )
        );    
            
            
        log.info("Received final action = " + action);
        
        if (action != null && action!= "") {
           
            
            switch (action) {
                
                case URL_PRODUITS_DETAIL :
                    
                      Long askedID = ServletUtils.getRequestAttrLongValue(request,att_detail_PdtId);
                      Produit prdt = null ;
                      
                      try {
                          
                          log.info("Gonna search to produitListe with ID [" + askedID + "]");
                           prdt = pdtCtl.findProduitListe(askedID);
                          
                          log.info("Found ? > [" + prdt.getPdtNom() + "]");
                          request.setAttribute(att_Pdt, prdt);
                          
                          if (prdt != null) {
                                                            
                              long refCatgId = prdt.getPdtCategory();
                              long refSCatgId = prdt.getPdtSubcategory();
                              
                              log.info("Product is ok, need Catg & SCatg names "
                                      + "\t|C " + refCatgId + "\t|SC " + refSCatgId
                                  );
                              
                              
//                              String refCatgName = InitContextListener.getShortListCategories(0).get(Long.toString(refCatgId));
//                              String refCatgName = "";
//                              switch (Long.toString(refCatgId)) { 
//                                  case "0":
//                                  case "GENERIC":
//                                  case "NONE":
//                                  case "NULL":
//                                  case "":
//                                      refCatgName = "";
//                                  default:
//                                    refCatgName = InitContextListener.getShortListCategories(0).get(Long.toString(refCatgId)); 
//                                  break;
//                              }
//                              log.info("Got Catg Name : " + refCatgName);


//                              String refSCatgName = InitContextListener.getSCatgNameWithID(refSCatgId);
//                              String refSCatgName = "";
//                              
//                              switch (Long.toString(refSCatgId)) { 
//                                  case "0":
//                                  case "GENERIC":
//                                  case "NONE":
//                                  case "NULL":
//                                  case "":
//                                      refSCatgName = "";
//                                  default:
//                                    refSCatgName = InitContextListener.getShortListSubCategories(0).get(Long.toString(refSCatgId)); 
//                                  break;
//                              }
//                              
//                              log.info("Got SCatg Name : " + refSCatgName);
                              
//                              request.setAttribute(att_refCatgName, refCatgName);
//                              request.setAttribute(att_refSCatgName, refSCatgName);
                              
                              request.setAttribute(att_refCatgName, getCatgNameWithID(prdt.getPdtCategory()));
                              request.setAttribute(att_refSCatgName, getSCatgNameWithID(prdt.getPdtSubcategory()));
                              
                              if (mb != null) {
                                  
                                  HashMap<String,String> lstLabels = lstForm.getShortListByMember(mb.getMbId());
                                  
                                  if (lstLabels != null ) {
                                        log.info("Got short Mb listes with size [" + lstLabels.size() + "]");
                                        request.setAttribute(att_ShortListMbListes, lstLabels);
                                  }
                              }
                              
                              
                              this.getServletContext().getRequestDispatcher(VIEW_PRODUITS_GEN).forward(request, response);
                          }
                                                    
                      } catch (Exception e) {
                          log.error("Asked product ID [" + askedID + "] could not be found >> " + e.getMessage());
                          
                          // Si le produit a été trouvé, mais une erreur inattendue est survenue
                          if (prdt != null) { e.printStackTrace();  }

                          // Si produit pas trouvé ou autre erreur, redirection vers page des produits avec message d'erreur
                          
                          // mon message d'erreur ...
                          
//                          this.getServletContext().getRequestDispatcher(VIEW_PRODUITS).forward(request, response);
                          response.sendRedirect(request.getContextPath() + URL_PRODUITS_PUBLIC);

                      }
//                    getProduct();
//                    request.setAttribute(att_PdtGenDetail, getProduct(       ));  
                   

                break;
                    
                default:                    
            
//                    // Ajoute l'url d'accès aux icones
//                    request.setAttribute(att_iconDataPath,
//                            // ternaire : build Path if 1st request
//                            (iconPathIsOK ? iconDataPath
////                                : getIconDataPath(request) )
//                                : buildIconURL(request) )
//                            );                    
                    
//                    getProductsListe();
//                    request.setAttribute(att_ListPdt, listProducts);                    
                    request.setAttribute(att_ListPdt, getProductsListe());                    
                    
//                    getCategoriesIdentifiers();
//                    request.setAttribute(att_ShortListCatg, shortListCategories);                    
                    request.setAttribute(att_ShortListCatg, getCategoriesIdentifiers());                    
                    
                    
//                    getSubCategories();
//                    request.setAttribute(att_ListSCatg, listSubCategories);                    
                    request.setAttribute(att_ListSCatg, getSubCategories());                    
                    
                    
//                    try {
                        // Called here cause user is SUPPOSED
                            //to go first to default page of this servlet to acces
                            // a detailed view of product, wich needs short SCatg list
//                        getSubCategoriesIdentifiers();
//                        String outList = "";
//                        for (String key : shortListSubCategories.keySet()) {
////                        for (String key : InitContextListener.getShortListSubCategories(2).keySet()) {
//                            outList += "\n ID  [ " + key + " ]  Value  [ "+ shortListSubCategories.get(key) + " ]" ;                        
//                        }
//                        log.info("\t Print short SCATG list >>" + outList + "");
                        
//                        request.setAttribute(att_ShortListSCatg, shortListSubCategories);
                        
//                    } catch (Exception e) {
//                        log.error("Could not get short SCATG list >> " + e.getMessage());
//                        e.printStackTrace();
//                    }
                    
                    this.getServletContext().getRequestDispatcher(VIEW_PRODUITS).forward(request, response);
                    break;
            }
            
        } else {
                  
            request.setAttribute(att_ListPdt, getProductsListe());                    
                
            request.setAttribute(att_ShortListCatg, getCategoriesIdentifiers());                    
                                       
            request.setAttribute(att_ListSCatg, getSubCategories());                    
            
            this.getServletContext().getRequestDispatcher(VIEW_PRODUITS).forward(request, response);
        }
    }

   
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {               
        this.getServletContext().getRequestDispatcher(VIEW_PRODUITS).forward(request, response);
    }

    // </editor-fold>

    
    private String findAction(String requestURI) {
        String action = "" ;

//        log.debug("Received path > " + requestURI);
        
        if (requestURI.contentEquals(URL_PRODUITS_PUBLIC)
//                || 
//                contextPath.contentEquals(URL_PRODUITS_PUBLIC + URL_CREATE)
        ) {
            log.debug("Try to go to /admin only ?  > " + requestURI);
            return "";
        } else {

            try {
                // Recuperer de /produits jusqu a la fin
                requestURI = requestURI.substring(requestURI.indexOf(URL_PRODUITS_PUBLIC 
    //                    + URL_CREATE
                ));  
                
//        log.debug("After substring  > " + requestURI);
            
            if (requestURI.isEmpty()) {
                return "";
            } else {
                
                switch (requestURI) {
                    case URL_PRODUITS_PUBLIC
                        +   URL_PRODUITS_DETAIL:
                        action = URL_PRODUITS_DETAIL;
                        break;
//                    case URL_CATEGORIE:
//                        action = URL_CATEGORIE;
//                        break;
//                    case URL_SUBCATEGORIE:
//                        action = URL_SUBCATEGORIE;
//                        break;
//                    case URL_PRODUITGENERIQUE:
//                        action = URL_PRODUITGENERIQUE;
//                        break;
//                    case URL_PRODUITCUSTOM:
//                        action = URL_PRODUITCUSTOM;
//                        break;
                    default:
                        action = "Tried {" + URL_PRODUITS_PUBLIC
//                                + URL_PRODUITS_VIEW 
                                + " ... ?} with [" + requestURI +"]." ;
                        break;
                }
                
            }
            
            return action;
                
            } catch (Exception e) {
                log.error("could not get supposed url /produits > " + requestURI);
                return "";
            }
            
            
            
        } 
                
    }

    private HashMap<String, String> getCategoriesIdentifiers() {

         HashMap<String, String> shortListCategories ;
        
//        if (cForm == null) {
//            cForm = new CategoryHandler(userCtl, ctgCtl);
//        }
//        int cgCount = cForm.getCount() ;
        
        int cgCount = 0;
        // la premiere fois il enverra 0
        // ensuite la taille de la map
//        if (shortListCategories != null) {
//            cgCount = shortListCategories.size();
//        }
        if (ctgForm != null) {
            cgCount = ctgForm.getCount() ;
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
        
        ArrayList<Subcategory> listSubCategories ; //= new ArrayList<Subcategory>() ;
        
//        if (sctgForm == null) {
//            sctgForm = new SubCategoryHandler( sctgCtl, ctgCtl);  
//        }
//        int cgCount = sctgForm.getCount() ;
//        int cgCount = listSubCategories.size() ;
        int cgCount = 0;
//        if (listSubCategories != null) {
//            cgCount = listSubCategories.size();
//        }
        if (sctgForm != null) {
            cgCount = sctgForm.getCount() ;
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
    
    private HashMap<String, String> getSubCategoriesIdentifiers() {
        
        HashMap<String, String> shortListSubCategories =  new HashMap<String, String>();

//        if ( sctgForm == null) {
//            sctgForm = new SubCategoryHandler(sctgCtl,ctgCtl);
//        }
//        int cgCount = cForm.getCount() ;
        
        int cgCount = 0;
        // la premiere fois il enverra 0
        // ensuite la taille de la map
        if (shortListSubCategories != null) {
            cgCount = shortListSubCategories.size();
        }
        
        // Envoie 0 ett recupere map vide non nulle
        // Ensuite envoie sa taille actuelle, mais ne sera jamais differente car pas obtenue de la db
        shortListSubCategories = InitContextListener.getShortListSubCategories(cgCount);
        
        
        if (shortListSubCategories == null
//        if (shortListCategories.isEmpty() 
                || 
           (shortListSubCategories.size() != cgCount && cgCount != 0 )
        ) {
            
            log.info("Retrieve SubCategory short list cause has changed.");
            
//            shortListCategories = new HashMap<String, String>();
//            shortListCategories.put("#ERROR", "Sorry, error.The list is not yet initialized.");
            
            shortListSubCategories = sctgForm.getShortList();
            
            InitContextListener.setShortListSubCategories(shortListSubCategories);
            
        }
//        

        
        return shortListSubCategories;
//        return DataProvider.getShortListCategories();
        
    }    
    
    private  List<Produit> getProductsListe() {
        
         ArrayList<Produit> tmpListe ;
        
        // In servlet Produits_servlet, we have a productManager, 
        // so we can retrieve list and share it next.
        int cgCount = 0 ;
        if (pdtGenForm != null) {
            cgCount = pdtGenForm.getCount();
        }
        // On laisse l'appel vers la db, cela permet de verifier de temps en temps
//        = pdtGenForm.getCount() ;
        
//        listProducts = InitContextListener.getListProducts(cgCount);
        tmpListe = InitContextListener.getListProducts(cgCount);
        
//        if (listProducts == null ||
        if (tmpListe == null ||
//        if (listProducts.isEmpty() || 
//           (listProducts.size() != cgCount && cgCount != 0 )
           (tmpListe.size() != cgCount && cgCount != 0 )
        ) {
            log.info("Retrieve Products list cause has changed.");
            
//            listProducts = new ArrayList<ProduitListe>() ;
            tmpListe = new ArrayList<Produit>() ;
            
//            listProducts.removeAll(listProducts);
            
//           listProducts.addAll(pdtGenForm.getList());
            tmpListe.addAll(pdtGenForm.getList());
        
//        InitContextListener.setListProducts(listProducts);
        InitContextListener.setListProducts(tmpListe);
        
        }

        
        return tmpListe;
//        return listProducts;
//        return DataProvider.getListSubCategories();
    }      
       

  
//    //
//    /**
//     * Vérifie que IconDataPath a déjà été construit, 
//     * sinon le construit puis le retourne.
//     * @return 
//     */
//    private Path getIconDataPath (ServletRequest request) {
//        
////        if (iconDataPath.contains("https://")           // Verifie qu'il contient [https://]
////                &   iconDataPath.lastIndexOf("/")> 7    // Verifie qu'il contiens le nom du serveur
////                // -> dans [https://] le dernier / est à l'index 6, si déjà construit, contiens au moins 1 / plus loin                
////                ) {
////            return Paths.get(iconDataPath);
////        } else {        
//            buildIconURL(request);
//            return Paths.get(iconDataPath);
////        }                
//    }

    private String getCatgNameWithID(long catgID) {
        String refCatgName = "";
        switch (Long.toString(catgID)) { 
          
            case "0":
            case "GENERIC":
            case "NONE":
            case "NULL":
            case "":
                refCatgName = "";
            break;
              
            default:
                refCatgName = InitContextListener.getShortListCategories(0).get(Long.toString(catgID)); 
            break;
        }
        log.info("Got Catg Name : " + refCatgName);
        return refCatgName;
    }
    private String getSCatgNameWithID(long scatgID) {
          String refSCatgName = "";

          switch (Long.toString(scatgID)) { 
              
              case "0":
              case "GENERIC":
              case "NONE":
              case "NULL":
              case "":
                  refSCatgName = "";
              break;
                  
              default:
                refSCatgName = InitContextListener.getShortListSubCategories(0).get(Long.toString(scatgID)); 
              break;
          }

          log.info("Got SCatg Name : " + refSCatgName);
          
          return refSCatgName;
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


//        String srvPath = this.getServletContext().getServerInfo();
            
//        log.debug("Try srvCtxt real path [/img] >> " + srvPath );                        
        
//        
//        String realPath = this.getServletContext().getRealPath("/img");
//            
//        log.debug("Try srvCtxt real path [/img] >> " + realPath );                        
        
//        String out = this.getServletContext().getResourcePaths("/").toString();
//            
//        log.debug(" PRINT srvCtxt ressources paths List for [/] >> \n\n" 
//                + out + "\n\n" );         
        
                
//        log.debug("Icon Data Folder is >> " + iconDataPath );   
//        if ( ! iconDataPath.contains
//                (realPath)  ) {
//            log.debug("Adding real path [/] >> " + realPath );   
//            iconDataPath = realPath + iconDataPath;
//            
//        }
//        log.debug("Finally, Icon Data Folder is >> " + iconDataPath );   
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
