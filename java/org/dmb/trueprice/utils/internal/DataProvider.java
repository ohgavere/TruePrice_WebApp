/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.utils.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.CategoryJpaController;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.controllers.SubcategoryJpaController;
import org.dmb.trueprice.entities.Liste;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.entities.Subcategory;
import org.dmb.trueprice.handlers.CategoryHandler;
import org.dmb.trueprice.handlers.SubCategoryHandler;

/**
 *
 * @author Guitch
 */
public class DataProvider {
    
//    private int temoin = RandomUtils.nextInt(10);
    private static int          temoinStatic        = RandomUtils.nextInt(10);
    private static final int    temoinStaticFinal   = RandomUtils.nextInt(10);

    private static final Logger log 
        = InitContextListener.getLogger( DataProvider.class.getSimpleName() + printTemoins()) ;        
    
    // Handlers for Entities events/operations management
//    private static CategoryHandler      cForm ;
//    private static SubCategoryHandler   scForm ;
    
    // Objects/Entities Lists
    private static ArrayList<Subcategory>   listSubCategories   = new ArrayList<Subcategory>() ;
    private static ArrayList<Produit>  listProducts        = new ArrayList<Produit>();
    private static ArrayList<Liste>  listMemberListes        = new ArrayList<Liste>();
    
    // Identifiers Maps
    private static HashMap<String, String>  shortListCategories     = new HashMap<String, String>();
    private static HashMap<String, String>  shortListSubCategories  = new HashMap<String, String>();    
    private static HashMap<String, String> shortListMemberLists = new HashMap<String, String>(); 

    ////////////////////////////////////////////////////////////////////////
    
    public DataProvider() {
        log.info("\tData Provider is INITIALIZING\t Loader is >> "
                + this.getClass().getClassLoader().getClass().getName() );
//        log.info(printTemoins());
    }
    
    public static String printTemoins(){
//        log.info(
        return
//            "\nTemoins  ==> \t"
//                + temoin +
            "|S " + temoinStatic 
            + " |SF "+ temoinStaticFinal
//        )
        ;    
    }
    
    @Override
    protected void finalize() throws Throwable {
        log.info("\n\tGonna kill me > " + printTemoins() + "\t Loader is >> " 
                + this.getClass().getClassLoader().getClass().getName() + "\n");
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
    }    
    
    ////////////////////////////////////////////////////////////////////////
    
    /*  GET & SET pairs for list of Objects/Entities    */
    
    // Subcategories
    public static ArrayList<Subcategory>    getListSubCategories (int actualSize) {
//        log.info(printTemoins());
        if (listSubCategories == null || listSubCategories.isEmpty() || 
//           (shortListSubCategories.size() != cgCount && cgCount != 0 )) {
                (listSubCategories.size() != actualSize && actualSize != 0 )) {
            log.info("You may retrieve \tSubCategory \tlist \tcause is null or has changed."
                    + "\t received size [" + actualSize + "]");
            
//            shortListSubCategories = cForm.getShortList();
            return null;
        }
        // Return the list, or null if : size = 0 || list = null || size != list.size()
        // So if return null, should be retrieved by caller and then shared
        return listSubCategories;        
    }
    public static void                      setListSubCategories(ArrayList<Subcategory> listSubCategories) {
//        log.info(printTemoins());
        log.info("Just got new \t[ list SubCategories]\t with size [" + listSubCategories.size() + "]");
        DataProvider.listSubCategories = listSubCategories;
    }
    
    //  Products
    public static ArrayList<Produit>    getListProducts (int actualSize) {
        
        if (listProducts == null || listProducts.isEmpty() || 
                
                (listProducts.size() != actualSize && actualSize != 0 )) {
            log.info("You may retrieve \tProducts \tlist \tcause is null "
                    + "or has changed.\t Received size [" + actualSize + "]");
            
//            shortListSubCategories = cForm.getShortList();
            return null;
        }
        // Return the list, or null if : size = 0 || list = null || size != list.size()
        // So if return null, should be retrieved by caller and then shared
        return listProducts;       
    }
    public static void                  setListProducts (ArrayList<Produit> listProducts) {
        log.info("Just got new \t[ list Products]\t with size [" + listProducts.size() + "]");
        DataProvider.listProducts = listProducts;
    }    
    
    //  Products
    public static ArrayList<Liste>    getListMemberListes (int actualSize) {
        
        if (listMemberListes == null || listMemberListes.isEmpty() || 
                
                (listMemberListes.size() != actualSize && actualSize != 0 )) {
            log.info("You may retrieve \tMember's Listes \tlist \tcause is null "
                    + "or has changed.\t Received size [" + actualSize + "]");
            
//            shortListSubCategories = cForm.getShortList();
            return null;
        }
        // Return the list, or null if : size = 0 || list = null || size != list.size()
        // So if return null, should be retrieved by caller and then shared
        return listMemberListes;       
    }
    public static void                  setListMemberListes (ArrayList<Liste> listMemberListes) {
        log.info("Just got new \t[ list Member's Listes]\t with size [" + listMemberListes.size() + "]");
        DataProvider.listMemberListes = listMemberListes;
    }    
    ////////////////////////////////////////////////////////////////////////
    
    /*  GET & SET pairs for Identifiers Maps    */
    /*  An Identifier is a pair of ID - LABEL from an Object/Entity    */
    
    //  Categories
    public HashMap<String, String>  getShortListCategories(int actualSize) {
//        log.info(printTemoins());
        if (shortListCategories == null || shortListCategories.isEmpty() || 
           (shortListCategories.size() != actualSize && actualSize != 0 )) {
            log.info("You may retrieve \tCategory \tshort list \tcause has changed."
                    + "\t received size [" + actualSize + "]");
            
            return null;
        }
        return shortListCategories;
    }
    public static void              setShortListCategories(HashMap<String, String> shortListCategories) {
//        log.info(printTemoins());
        
        
        log.info( "Just got new \t[short List Categories]\t with size [" + shortListCategories.size() + "]");
        DataProvider.shortListCategories = shortListCategories;
    }    
    
    // SubCategories
    public static HashMap<String, String>   getShortListSubCategories(int actualSize) {
//        log.info(printTemoins());
        //        if (scForm == null) {
//            scForm = new SubCategoryHandler(userCtl, sctgCtl, ctgCtl);  
//        }
//        int cgCount = scForm.getCount() ;
        if (shortListSubCategories == null || shortListSubCategories.isEmpty() || 
//           (shortListSubCategories.size() != cgCount && cgCount != 0 )) {
                (shortListSubCategories.size() != actualSize && actualSize != 0 )) {
            log.info("You may retrieve \tSubCategory \tshort list \tcause is null or has changed."
                    + "\t received size [" + actualSize + "]");
            
//            shortListSubCategories = cForm.getShortList();
            return null;
        }
        // Return the list, or null if : size = 0 || list = null || size != list.size()
        // So if return null, should be retrieved by caller and then shared
        return shortListSubCategories;
    }
    public static void                      setShortListSubCategories(HashMap<String, String> shortListSubCategories) {
         log.info("Just got new \t[ short list SubCategories]\t with size [" + shortListSubCategories.size() + "]");
        DataProvider.shortListSubCategories = shortListSubCategories;
        String outList = "";
        for (String key : shortListSubCategories.keySet()) {
            outList += "\n ID  [ " + key + " ]  Value  [ "
                    + shortListSubCategories.get(key) + " ]" ;                        
        }
        log.info("\t Print short SCATG list >>" + outList + "");        
    }
        
    
    // Member's Listes
    public static HashMap<String, String>   getShortListMemberListes(int actualSize) {
//        log.info(printTemoins());
        //        if (scForm == null) {
//            scForm = new SubCategoryHandler(userCtl, sctgCtl, ctgCtl);  
//        }
//        int cgCount = scForm.getCount() ;
        if (shortListMemberLists == null || shortListMemberLists.isEmpty() || 
//           (shortListSubCategories.size() != cgCount && cgCount != 0 )) {
                (shortListMemberLists.size() != actualSize && actualSize != 0 )) {
            log.info("You may retrieve \tMemberListes \tshort list \tcause is null or has changed."
                    + "\t received size [" + actualSize + "]");
            
//            shortListSubCategories = cForm.getShortList();
            return null;
        }
        // Return the list, or null if : size = 0 || list = null || size != list.size()
        // So if return null, should be retrieved by caller and then shared
        return shortListMemberLists;
    }
    public static void                      setShortListMemberListes(HashMap<String, String> shortListMemberLists) {
         log.info("Just got new \t[ short list MemberListes]\t with size [" + shortListMemberLists.size() + "]");
        DataProvider.shortListMemberLists = shortListMemberLists;
        String outList = "";
        for (String key : shortListMemberLists.keySet()) {
            outList += "\n ID  [ " + key + " ]  Value  [ "
                    + shortListMemberLists.get(key) + " ]" ;                        
        }
        log.info("\t Print short mbList list >>" + outList + "");        
    }
        
    
    
       
    


    
    



    

}
