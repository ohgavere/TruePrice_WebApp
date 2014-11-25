package org.dmb.trueprice.handlers;

/**
 *
 * @author Work.In.Progress
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.print.DocFlavor;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.CategoryJpaController;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.controllers.PasswordJpaController;
import org.dmb.trueprice.entities.Category;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.entities.Password;
import org.dmb.trueprice.servlets.Produits_servlet;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;

public final class CategoryHandler extends BasicHandler {
    private static final String att_label  = "label-catg";
    private static final String att_description   = "description-catg";
    private static final String att_form_record   = "record";
    
    private String              resultat;
//    private Map<String, String> erreurs      = new HashMap<String, String>();


    private static final Logger log = InitContextListener.getLogger(CategoryHandler.class) ;
    
//    private MembreJpaController      userManager;
    private CategoryJpaController      ctgManager;
    
    private static ArrayList<String> neededFields = new ArrayList<String> ();
    
    static {
        neededFields.add(att_label);
        neededFields.add(att_description);
        
        
    }

    public CategoryHandler(
//            MembreJpaController userManager, 
            CategoryJpaController ctgManager) {
//        log.warn("\n\n\t\t Initialise CategoryHandler"
//                + "\n\tIs userManager == null ? > " + (Boolean)(userManager == null)
//                + "\n\tIs ctgManager == null ? > " + (Boolean)(ctgManager == null)
//        );
//        this.userManager = userManager;
        this.ctgManager = ctgManager;
    }        

    public Category createCategory( HttpServletRequest request) {
        
        HashMap<String, String> fieldsValues = new HashMap<String, String>();
        Category catg = new Category();
       
        for (String attName : neededFields) {

            String attValue = ServletUtils.getRequestAttrValue(request, attName);
                
            log.debug("Try to get Attr [" + attName + "]") ;
                
            if ( attValue == null || "".equals(attValue)) {
                
                log.warn("REQ Attr [" + attName + "] has empty value  ");
                
                switch (attName) {
                    case att_label :
                        setErreur(att_label, "Le label est indispensable");
                        break;
                    case att_description :
                        setErreur(att_description, "La description doit contenir au moins 10 caractères");
                        break;
                }
                
            } else if (attName.equals(att_description) && attValue.length()<10) {
                setErreur(att_description, "La description doit contenir au moins 10 caractères");
            }
            // Finally, when all params are OK.
            else {
                fieldsValues.put(attName, attValue);
            }
                                  
        }
        
        

        /* Si les paramètres sont OK on persite la categorie */
        if ( getErreurs().isEmpty() ) {
            
            // Resultat intermediaire avant essai persistance
            resultat = "Categorie Validée. Essai d'enregistrement.";
            
            catg.setCtgLabel(fieldsValues.get(att_label));
            catg.setCtgDescription(fieldsValues.get(att_description));
            
            try  {
                ctgManager.create(catg);
            }catch (Exception e) {
                log.error("Persist Category [" + catg.getCtgLabel() 
                        + "] has failed because {" +e.getMessage() + "}." );
                setErreur(att_form_record, "Echec serveur, veuillez vérifier vos paramètres ou réessayer plus tard.");
                e.printStackTrace();
            }
            
            /* Initialisation du résultat global de la validation. */
            if ( getErreurs().isEmpty() ) {
                resultat = "<strong>Categorie enregistrée</strong>. ID [" 
                        + catg.getCtgId() + "] ["
                        + catg.getCtgLabel() + "]." ;
            } else {
                resultat = "<strong>Échec de l'enregistrement.</strong>";
                // Si l'email est correct mais pas le mot de passe => recover
            }
            
        } else {
            resultat = "Mauvais paramètre(s).";
            // Si l'email est correct mais pas le mot de passe => recover
        }
        


        setResultat(resultat);
        return catg;
    }


    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    @Override
    public void setErreur( String champ, String message ) {
        log.warn("\n==========\tINSERTING EXCEPTION for {Catgeory} to [" + champ + "] because > " + message);
//        if (erreurs.isEmpty()) {
//            erreurs.put( champ, message );
//        }        
//        if (getErreurs().isEmpty()) {
//            getErreurs().put( champ, message );
//        }
        super.setErreur(champ, message);
    }




    public HashMap<String, String> getShortList () {
        List<Category> found = ctgManager.findCategoryEntities() ;
        if (found != null) {
            HashMap<String, String> shortList = new HashMap<>(found.size());
            for (Category cg : found) {
                shortList.put(String.valueOf(cg.getCtgId()), cg.getCtgLabel()) ;
            }
            return shortList;
            
        } else {
            log.error("Did not found any Category.");
        }
        return null;
    }
    public List<Category> getList () {
        List<Category> found = ctgManager.findCategoryEntities() ;
        if (found != null) {
            return found;            
        } else {
            log.error("Did not found any Category.");
           return null;
        }        
    }
    
    public int getCount () {
        return ctgManager.getCategoryCount();
    }
    
}