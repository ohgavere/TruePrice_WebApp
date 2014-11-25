package org.dmb.trueprice.handlers;

/**
 *
 * @author Work.In.Progress
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.CategoryJpaController;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.controllers.SubcategoryJpaController;
import org.dmb.trueprice.entities.Category;
import org.dmb.trueprice.entities.Subcategory;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;

public final class SubCategoryHandler extends BasicHandler {
    private static final String att_label  = "label-scatg";
    private static final String att_description   = "description-scatg";
    private static final String att_parent   = "parentC-scatg" ; // == Category (as FIRST CHILD) 
    private static final String att_parentSub   = "parentSC-scatg" ; // == or Subcategory (Because one subcatg cane have an other subcatg as parent)
    private static final String att_parentType   = "parent-type-scatg" ; // To identify wich type of parent it is (Catg or SubCatg)
    private static final String att_form_record   = "record";
    
//    private String              resultat;
//    private Map<String, String> erreurs      = new HashMap<String, String>();

    private static final Logger log 
            = InitContextListener.getLogger( SubCategoryHandler.class) ;
    
    private CategoryJpaController      ctgManager;
    private SubcategoryJpaController      sctgManager;
    
    private static ArrayList<String> neededFields = new ArrayList<String> ();
    
    static {
        neededFields.add(att_label);
        neededFields.add(att_description);
        neededFields.add(att_parent);
        neededFields.add(att_parentSub);
        neededFields.add(att_parentType);

    }

    public SubCategoryHandler( SubcategoryJpaController sctgManager, CategoryJpaController ctgManager) {
//        this.userManager = userManager;
        this.sctgManager = sctgManager;
        this.ctgManager = ctgManager;
    }        

    public Subcategory createSubCategory( HttpServletRequest request) {
        
        HashMap<String, String> fieldsValues = new HashMap<String, String>();
        Subcategory scatg = new Subcategory();
        
        String resultat = "";
       
        for (String attName : neededFields) {

            String attValue = ServletUtils.getRequestAttrValue(request, attName);
                
            log.debug("Try to get Attr [" + attName + "]") ;
                
            if ( attValue == null || "".equals(attValue)) {
                
                log.warn("REQ Attr [" + attName + "] has empty value  ");
                
                switch (attName) {
                    case att_label :
                        setErreur(att_label, "Le label est indispensable.");
                        break;
                    case att_description :
                        setErreur(att_description, "La description est indispensable, et doit contenir au moins 10 caractères.");
                        break;
                    case att_parentType :
                        setErreur(att_parent, "Le parent est indispensable.");
                        break;
                }
                
            } else if (attName.equals(att_description) && attValue.length()<10) {
                setErreur(att_description, "La description doit contenir au moins 10 caractères");
            
            
                
            }// Finally, when a param is OK.
            else {
                fieldsValues.put(attName, attValue);
                log.debug("Attr [" + attName + "] accpted with value [" + attValue + "]");
            }
                                  
        }
        
        
        String givenId = "" ;
        int foundId = 0 ;
        String foundLabel = "" ;
        
        // Si les params sont OK, on essaye de retrouver la categorie parente mentionée
        if ( getErreurs().isEmpty()) {

            try {
                log.debug("Parent is of type [" + fieldsValues.get(att_parentType) + "]");
                if (fieldsValues.get(att_parentType).equals("scatg")) {
                    
                     givenId = fieldsValues.get(att_parentSub) ;
                     
                    Subcategory sc = sctgManager.findSubcategory(Integer.valueOf(givenId));
                    foundId = sc.getSctgId();
                    foundLabel = sc.getSctgLabel();
                    
                    scatg.setSctgParent(sc.getSctgParent());
//                    scatg.setSctgSubparent(BigInteger.valueOf(Long.valueOf(givenId)));
                    scatg.setSctgSubparent(Long.valueOf(sc.getSctgId()));
                    
                } else {
                    
                     givenId = fieldsValues.get(att_parent) ;
                     
                    Category c = ctgManager.findCategory(Integer.valueOf(givenId));
                    foundId = c.getCtgId();
                    foundLabel = c.getCtgLabel();
                    
                    scatg.setSctgParent(foundId);
                    
                }
                log.info("Parent is of type [" + fieldsValues.get(att_parentType) + "] with ID [" + String.valueOf(foundId) + "]") ;

            } catch (Exception e) {
                log.error("Could not get parent ID with value [" + givenId + "]"  + e.getMessage());
            } finally {
                if (foundId == 0) {
                    switch (fieldsValues.get(att_parentType)) {
                        case "scatg":
                            setErreur(att_parentSub, "La sous-categorie parente n'a pas pu etre trouvée.");
                        break;
                        case "catg":
                            setErreur(att_parent, "La categorie parente n'a pas pu etre trouvée.");
                        break;
                    }
                } else {
                    log.info("Found parent with ID [" + foundId + "] & Label [" + foundLabel + "]");
                }
            }
        }

        /* Si les paramètres sont OK on persite la categorie */
        if ( getErreurs().isEmpty() ) {
            
            // Resultat intermediaire avant essai persistance
            resultat = "Categorie Validée. Essai d'enregistrement.";
            
            scatg.setSctgLabel(fieldsValues.get(att_label));
            scatg.setSctgDescription(fieldsValues.get(att_description));
            
            
            try  {
                sctgManager.create(scatg);
            }catch (Exception e) {
                log.error("Persist Category [" + scatg.getSctgLabel()
                        + "] has failed because {" +e.getMessage() + "}." );
                setErreur(att_form_record, "Echec serveur, veuillez vérifier vos paramètres ou réessayer plus tard.");
                e.printStackTrace();
            }
            
            /* Initialisation du résultat global de la validation. */
            if ( getErreurs().isEmpty() ) {
                resultat = "<strong>Sous-Categorie enregistrée</strong>. ID [" 
                        + scatg.getSctgId() + "][" + scatg.getSctgLabel() + "].<br>"
                        + "Parent is of type [" + (fieldsValues.get(att_parentType).equals("catg") ?
                            "Categorie" : "Sous-categorie" )
                        + "] with ID [" + foundId + "][" + foundLabel + "].<br>" ;
            } else {
                resultat = "<strong>Échec de l'enregistrement.</strong>";
                // Si l'email est correct mais pas le mot de passe => recover
            }
            
        } else {
            resultat = "Mauvais paramètre(s).";
            // Si l'email est correct mais pas le mot de passe => recover
        }
        

        setResultat(resultat);

        return scatg;
    }


    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    @Override
    public void setErreur( String champ, String message ) {
        log.warn("\n==========\tINSERTING EXCEPTION for {SubCatgeory} to [" + champ + "] because > " + message);
        super.setErreur(champ, message);
    }


    public HashMap<String, String> getShortList () {
        List<Subcategory> found = sctgManager.findSubcategoryEntities();
        if (found != null) {
            HashMap<String, String> shortList = new HashMap<>(found.size());
            for (Subcategory scg : found) {
                shortList.put(String.valueOf(scg.getSctgId()), scg.getSctgLabel()) ;
            }
            return shortList;
            
        } else {
            log.error("Did not found any SubCategory.");
        }
        return null;
    }
    
    public List<Subcategory> getList () {
        List<Subcategory> found = sctgManager.findSubcategoryEntities();
        if (found != null) {
            return found;            
        } else {
            log.error("Did not found any Category.");
           return null;
        }        
    }    
    
    public int getCount () {
        return sctgManager.getSubcategoryCount();
    }
    
}