/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.utils.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import javax.naming.NamingException;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.ApplicationPath;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//import Logger;
import org.apache.log4j.Logger;
import org.dmb.trueprice.entities.Liste;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.entities.Subcategory;
import org.dmb.trueprice.filter.RestrictionFilter;
import org.dmb.trueprice.handlers.SubCategoryHandler;
import static org.dmb.trueprice.utils.internal.DataProvider.printTemoins;

/**
 *
 * @author Guitch
 */
public class InitContextListener implements ServletContextListener {
    
    private static String xmlMemberDataFolder = "xmlMemberDataFolder"; 

    public InitContextListener() {
        log.info("\n\n\tContext Listener is INITIALIZING\t Loader is >> " 
                + this.getClass().getClassLoader().getClass().getName() + "\n");
        log.info(printTemoins());    
        
        xmlMemberDataFolder = getEnvEntryValue(xmlMemberDataFolder);
    }
    
    private static int temoinStatic = RandomUtils.nextInt(10);
    private static final int temoinStaticFinal = RandomUtils.nextInt(10);

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
        
    private static DataProvider provider = new DataProvider() ;
    
    private static Properties prop ;        
        
    private static  HashMap <String , ServletContext > servletsContext = new HashMap<String, ServletContext>();

//    public static Logger getLog() {
//        return log;
//    }
    
    public static String getXmlMemberFullPath(String mbMail) {
        return xmlMemberDataFolder + File.separator + mbMail;
    }    
    
    private static final Logger log = getLogger(InitContextListener.class.getSimpleName() + printTemoins());
    
    
    // Donnees necessaires dans differents servlets. 
    // Le premier servlet à les demander devra les retrouver lui même.
    // Ensuite il le partage avec l'initContextListner pour etre diffusé facilement
    

    private static ArrayList<Produit> listProducts = new ArrayList<Produit>() ;   
    
    private static HashMap<String, String> shortListCategories = new HashMap<String, String>();
    
    private static ArrayList<Subcategory> listSubCategories = new ArrayList<Subcategory>() ;   
    
    
    
    //      //      //      //      //      //      //      //      //      //
    
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        
        log.warn("\n\t >>> \t INITIALIZING APPLICATION ....");
        
//        sce.getServletConfig().gets
        
        
        ServletContext sctxt = sce.getServletContext();
        
            log.debug("\t >>>\t SHOW servlets names list ");
//            log.debug("\t >>>\t SERVLET NAME IS > " + sctxt.getServletContextName());
            Map<String, ? extends ServletRegistration> eParam = sctxt.getServletRegistrations();
            for (String paramName : eParam.keySet()) {
//                String paramName = eParam.nextElement();
//                String paramValue = sctxt.getInitParameter(paramName).toString();
                
                if (paramName != null) {
                    String paramValue = eParam.get(paramName)
                            //on ajoute .getName car l'obejt dans la Map extends ServletRegistration
                            .getName();
                    log.debug("\t >> New servlet [" + ("".equals(paramName) ? "???" : paramName) 
//                    log.debug("\t >> New servlet [" + ("".equals(paramName) ? "???" : paramName) 
                            + "] > " + ("".equals(paramValue) ? "???" : paramValue)
                    );                
                    servletsContext.put(paramName, null);
                }

            }
        
//            log.debug("\t >>>\t SHOW INIT PARAMETERS of SERVLET CONTEXT");
//            log.debug("\t >>>\t SERVLET NAME IS > " + sctxt.getServletContextName());
//            Enumeration<String> eParam = sctxt.getInitParameterNames();
//            while (eParam.hasMoreElements()) {
    //            String paramName = eParam.nextElement();
    //            String paramValue = sctxt.getInitParameter(paramName).toString();
    //            log.debug("New init param [" + (paramName == null ? "???" : paramName) 
    //                    + "] > " + (paramValue == null ? "???" : paramValue)
//            );
//            }
//        
//            log.debug("\t >>>\t SHOW ATTRIBUTES of SERVLET CONTEXT");
//            Enumeration<String> e = sctxt.getAttributeNames();
//            while (e.hasMoreElements()) {
    //            String attName = e.nextElement();
    //            String attValue = sctxt.getAttribute(attName).toString();
    //            log.debug("New att [" + (attName == null ? "???" : attName) 
    //                    + "] > " + (attValue == null ? "???" : attValue)
    //            );
//              }


            

//    servletsContext = sctxt;
    
}    
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.warn("\n\t >>> \t DESTROYING APPLICATION ....\n\n");
        }
    
 
    public static void getServletConfig (String servletName, HttpServlet servlet) {
        log.warn("\n\t >>> \t Servlet [" + servletName + "] request for config \n");
        log.warn("\n\t >>> \t Can we get it's init param names ? [" 
//                 + servletsContext.getServletRegistration(servletName).getInitParameters().keySet().toString()  + "] "                
                 + servletsContext.containsKey(servletName)  + "] "                
                                 
        );
        if (servletsContext.containsKey(servletName)) {
                log.debug("\t >>>\t SHOW INIT PARAMS of SERVLET CONTEXT");
                ServletConfig config = servlet.getServletConfig();
                Enumeration<String> e ;
                try {
                    e = config.getInitParameterNames();
                    while (e.hasMoreElements()) {
                    String attName = e.nextElement();
                    String attValue = servlet.getInitParameter(attName).toString();
                    log.info("New init param [" + (attName == null ? "???" : attName) 
                            + "] > " + (attValue == null ? "???" : attValue)
                    );
                  }                    
                } catch (Exception ex) {
                    log.error("\t >>>\t        FAILURE       > " + ex.getMessage());
                    ex.printStackTrace();
                }
            
        }
    }
    
    public static FilterConfig getFilterConfig(FilterConfig config) {
        String filterName = config.getFilterName();
        log.warn("\n\t >>> \t Filter [" + filterName + "] request for config \n");
        switch (filterName) {
            case "" :
                log.warn("Can't determine wich filter wants config > " + filterName);
                break;
            case "RestrictionFilter" :
                log.info(filterName + " filter ask config");
                log.info("Add init param [" + RestrictionFilter.URL_ACCES_ADMIN + "]");
                config.getServletContext().setInitParameter(
                        RestrictionFilter.URL_ACCES_ADMIN,
                        getEnvEntryValue(RestrictionFilter.URL_ACCES_ADMIN)
                        );
                break;
            default :
                log.error("Could not match any filter. Who wants config ? > " + filterName);
                break;
        };
        return config;
    }
    
    private static void loadProperties () {
        
        prop = new Properties();
        
        InputStream input = null;
        
         try {
 
            String filename = "config.properties";
            input = InitContextListener.class.getClassLoader().getResourceAsStream(filename);
            if(input==null){
                    log.error("Unreachable properties file [" + filename + "]");
                    log.error("Tried classloader [" + InitContextListener.class.getClassLoader() + "]");
                return;
            }
 
            //load a properties file from class path, inside static method
            prop.load(input);
            
            printProperties();
            
//            testProperties12();
 
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    private static void printProperties(){
        log.info(" >> Printing properties  > > > ...");
        log.info(prop.toString());
        log.info(" >> Printing properties done  > > > ...");        
    }
    private static void testProperties12(){
        log.debug("\n\t\t >> p1 " + getEnvEntryValue("property1") );
        log.debug("\n\t\t >> p2 " + getEnvEntryValue("property2") );
        log.debug("\n\t\t >> p2bis " + getEnvEntryValue("property2bis") );
//        log.debug("\n\t\t >> p2bis escpaed " + getEnvEntryValue("property2bis") );
        log.debug("\n\t\t >> p3" + getEnvEntryValue("property3"));
        
    }
    
     public static String getEnvEntryValue(String envEntryName) {
        
        if (prop == null) {  loadProperties();  }
        
        String envEntryValue;
        
        try {
            
            envEntryValue = prop.getProperty(envEntryName);         
            
            if(envEntryValue == null) {
              throw new NamingException();
            }
            
        } catch (NamingException e) {
            
            throw new RuntimeException("cannot find env entry "+ envEntryName,e);
        }
        
        return envEntryValue;
    }
//    
//    /**
//     * check if we are in intranet version
//     * get into the environement the variable intranet
//     * if this variable is set to true , return this value 
//     */
//    public boolean isIntranet() {
////      return Boolean.parseBoolean(prop.getProperty("intranet"));
//        return Boolean.parseBoolean(getEnvEntryValue("intranet"));
//    }
//    
//    /**
//     * check if we are in internet version of poldoc
//     * the choose of not intranet is because the difference between isIntranet and isInternet is not enought to avoid fast reading mistake 
//     */
//    public boolean isNotIntranet() {
//      return ! isIntranet(); // smart <:O)
//    }
//    
//    /**
//     * check if we are in dev
//     * get into the environement the variable development
//     * if this variable is set to true , return this value 
//     */
//    public boolean isDev() {
//      String value = getEnvEntryValue("development");
//      
//      return Boolean.parseBoolean(value);
//    }
     
     
     public static Logger getLogger (Class clazz){
         String name = "";
         name = clazz.getName();
         if (name == "")  { name = clazz.getCanonicalName() ;}
         if (name == "")  { name = clazz.getSimpleName();}        
         return Logger.getLogger("T$p|" + name + "|");
     }     
     public static Logger getLogger (String name){
         return Logger.getLogger("T$p|" + name + "|");
     }     

     
     ///////////////////////////////////////////////////////////
     // GETTERS
     ///////////////////
        // Products
    public static ArrayList<Produit>   getListProducts           (int actualRealSize) {
//        if ( (actualRealSize != 0)
//            & (listProducts.size() != actualRealSize)) {
//            return null;
//        } else {
//            return listProducts;
//        }        
        return provider.getListProducts(actualRealSize);
    }
        // Member's Listes
    public static ArrayList<Liste>    getListMemberListes      (int actualRealSize) {

//        if ( (actualRealSize != 0)
//            & (listSubCategories.size() != actualRealSize)) {
//            return null;
//        } else {
//            return listSubCategories;
//        }
        return provider.getListMemberListes(actualRealSize);
    }    
    public static HashMap<String, String>   getShortListMemberListes    (int actualRealSize) {
//        if ( (actualRealSize != 0)
//            & (shortListCategories.size() != actualRealSize)) {
//            return null;
//        } else {
//            return shortListCategories;
//        }        
        return provider.getShortListMemberListes(actualRealSize);
    }
        // Categories
    public static HashMap<String, String>   getShortListCategories    (int actualRealSize) {
//        if ( (actualRealSize != 0)
//            & (shortListCategories.size() != actualRealSize)) {
//            return null;
//        } else {
//            return shortListCategories;
//        }        
        return provider.getShortListCategories(actualRealSize);
    }
        // SubCategories
    public static ArrayList<Subcategory>    getListSubCategories      (int actualRealSize) {

//        if ( (actualRealSize != 0)
//            & (listSubCategories.size() != actualRealSize)) {
//            return null;
//        } else {
//            return listSubCategories;
//        }
        return provider.getListSubCategories(actualRealSize);
    }
    public static HashMap<String, String>   getShortListSubCategories (int actualSize) {
    
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
        return provider.getShortListSubCategories(actualSize);
    }
    
    
    
    ///////////////////////////////////////////////////////////
    //  SETTERS
    ///////////////////
        // Products
    public static void setListProducts          (ArrayList<Produit>    listProducts) {
//        InitContextListener.listProducts = listProducts;
        log.info("Just got new [ list Products] with size [" + listProducts.size() + "]");        
        provider.setListProducts(listProducts);
    }    
        // Categories
    public static void setShortListCategories   (HashMap<String, String>    shortListCategories) {
        
        log.info("Just got new [ short List Categories] with size [" + shortListCategories.size() + "]");        
        
//        InitContextListener.shortListCategories = shortListCategories;
        provider.setShortListCategories(shortListCategories);
    }
        // SubCategories
    public static void setListSubCategories     (ArrayList<Subcategory>     listSubCategories) {        
        log.info("Just got new [ list SubCategories] with size [" + listSubCategories.size() + "]");
//        InitContextListener.listSubCategories = listSubCategories;
        provider.setListSubCategories(listSubCategories);
    }
    public static void setShortListSubCategories(HashMap<String, String>    shortListSubCategories) {
        log.info("Just got new [ short List SubCategories] with size [" + shortListSubCategories.size() + "]");        
        provider.setShortListSubCategories(shortListSubCategories);
    }
        // Member's Listes
    public static void setListmemberListes     (ArrayList<Liste>     listMemberListes) {        
        log.info("Just got new [ list Member's Listes] with size [" + listMemberListes.size() + "]");
//        InitContextListener.listSubCategories = listSubCategories;
        provider.setListMemberListes(listMemberListes);
    }    
    public static void setShortListMemberListes(HashMap<String, String>    shortMemberListes) {
        log.info("Just got new [ short List Member's Listes] with size [" + shortMemberListes.size() + "]");        
        provider.setShortListMemberListes(shortMemberListes);
    }





    public static String getSCatgNameWithID (long refID) {
        
        String name = "";
        log.info("Asking for SCatgName with refID [" + refID + "]");
        
//        ListIterator tempIter = listSubCategories.listIterator();
        
        // En envoyant 0 on est sûr d'obtenir la liste actuelle 
            //==> il faut etre sûr qu'elle soit remplie
        ListIterator tempIter = provider.getListSubCategories(0).listIterator();
        
        while (tempIter.hasNext() && name == "") {
            
            Subcategory tempSCatg = (Subcategory) tempIter.next();
            
            if (tempSCatg.getSctgId().toString().compareTo(Long.toString(refID)) >= 0) {
                
                log.debug("Found SCatg with id  [" + refID + " =?= " + tempSCatg.getSctgId() 
                        + "] and label [" + name + " =?= " + tempSCatg.getSctgLabel() + "]");
                name = tempSCatg.getSctgLabel();
                
            } 
//             Just for debugging (check the good one is not passed over)
            else {
                log.debug("Not this Catg >> id  [" + tempSCatg.getSctgId() + "]");
            }
        }        
        
        return name;
    }


        @Override
    protected void finalize() throws Throwable {
        log.info("Gonna kill me > " + printTemoins());
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
    } 
     
}

