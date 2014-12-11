package org.dmb.trueprice.handlers;

/**
 *
 * @author Work.In.Progress
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.CategoryJpaController;
import org.dmb.trueprice.controllers.ProduitListeJpaController;
import org.dmb.trueprice.controllers.SubcategoryJpaController;
import org.dmb.trueprice.entities.Category;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.entities.Subcategory;
import org.dmb.trueprice.utils.external.MultipartMap;
import org.dmb.trueprice.utils.internal.FileUtils;
import org.dmb.trueprice.utils.internal.InitContextListener;

public final class ProductGeneriqHandler extends BasicHandler {
    private static final String att_label  = "label-produit";
    private static final String att_description   = "description-produit";
    private static final String att_icon   = "icon-produit";
    private static final String att_parent   = "parentC-produit";
    private static final String att_parentSub   = "parentSC-produit";
    private static final String att_parentType   = "parent-type-produit" ; // To identify wich type of parent it is (Catg or SubCatg)
    
    private static final String att_form_record   = "record";
    
//    private String              resultat;
//    private Map<String, String> erreurs      = new HashMap<String, String>();

    private static final Logger log 
            = InitContextListener.getLogger(ProductGeneriqHandler.class) ;
    
    private ProduitListeJpaController   pdtManager;
    private CategoryJpaController       ctgManager;
    private SubcategoryJpaController    sctgManager;    
    
    private MultipartMap map;
    private String dataFolder ;
    
    private static final ArrayList<String> generiqFields = new ArrayList<String> ();
    
    static {
        generiqFields.add(att_label);
        generiqFields.add(att_description);
        generiqFields.add(att_icon);
        generiqFields.add(att_parent);
        generiqFields.add(att_parentSub);
        generiqFields.add(att_parentType);
        
    }

    // The map is not used anymore because we use it for the same location each time,
    // so creating new instance only need the request as dataFolder hasn't changed.
    // Thus, we build it 
    public ProductGeneriqHandler (ProduitListeJpaController pdtManager, MultipartMap map, String dataFolder, 
             SubcategoryJpaController sctgManager, CategoryJpaController ctgManager) {
        this.pdtManager = pdtManager;
        this.sctgManager = sctgManager;
        this.ctgManager = ctgManager;
        this.map = map ;
        this.dataFolder = dataFolder;
    }        

    public ProductGeneriqHandler (ProduitListeJpaController pdtManager, String dataFolder, 
             SubcategoryJpaController sctgManager, CategoryJpaController ctgManager) {
        this.pdtManager = pdtManager;
        this.sctgManager = sctgManager;
        this.ctgManager = ctgManager;
        this.dataFolder = dataFolder;
    }        

//    public void buildMultipartMap(MultipartMap map) { // 1st try, but servlet don't need to handle that for each request
//    public void buildMultipartMap(HttpServletRequest multipartRequest) {  // 2nd try, but why should servlet do that ?
    // So we do it here each time we receive a new multipartRequest => createProductGeneriq()
    private void buildMultipartMap(MultipartMap map) {
        this.map = map;
    }
    
    public Produit createProductGeneriq( HttpServletRequest request) throws IOException, ServletException {
        
        buildMultipartMap(new MultipartMap(request, dataFolder));
        
        HashMap<String, String> fieldsValues = new HashMap<String, String>();
        Produit pdt = new Produit();
        
        String resultat = "";
//        String iconFullName = "";
        String iconLink = "";
        
//        int parentCount = 2 ;
       
//        for (String key : map.getParameterMap().keySet()) {
//            log.info("Key [" + key + "] has value [" + 
//                    map.getParameter(key) + "]");
//        }
        
        for (String attName : generiqFields) {

            String strLog = "Try to get Attr [" + attName + "] .. got => [" ;
            
//            String attValue = ServletUtils.getRequestAttrValue(request, attName);
            String attValue = map.getParameter(attName);
                
            strLog += " " + attValue + " ]";
            
            log.info(strLog) ;
                
            if ( attValue == null || "".equals(attValue)) {
                
                log.warn("REQ Attr [" + attName + "] has empty value  ");
                
                switch (attName) {
                    case att_label :
                        setErreur(att_label, "Le nom est indispensable.");
                        break;
                    case att_description :
                        setErreur(att_description, "La description doit contenir au moins 10 caractères.");
                        break;
//                    case att_parentSub:
//                    case att_parent:
//                        parentCount --;
//                        break;
                    case att_parentType :
                        setErreur(att_parentSub, "Le parent est indispensable.");
                        break;                        
                }
                
            } else if (attName.equals(att_description) && attValue.length()<10) {
                setErreur(att_description, "La description doit contenir au moins 10 caractères.");
            }
            // Finally, when the param is OK.
            else if (attName.equals(att_icon)){
                    
                    // Nom fichier + extension
                    iconLink = attValue;
                    
                    /*
                    * Antibug pour Internet Explorer, qui transmet pour une raison
                    * mystique le chemin du fichier local à la machine du client...
                    * 
                    * Ex : C:/dossier/sous-dossier/fichier.ext
                    * 
                    * On doit donc faire en sorte de ne sélectionner que le nom et
                    * l'extension du fichier, et de se débarrasser du superflu.
                    */
                    
                    iconLink = iconLink.substring( iconLink.lastIndexOf( '/' ) + 1 )
                        .substring( iconLink.lastIndexOf( '\\' ) + 1 );
                    
                    try {
                        
                        File f = map.getFile(attName);
                        
                        f = new File(dataFolder + File.separator + iconLink);
                        
                    } catch (Exception ex) {
                       log.error("An issue was thrown with file [" + iconLink + "] because > " + ex.getMessage());
                        setErreur(att_form_record, "Impossible d'enregistrer l'icone. Veuillez réessayer."
                                + "<br>" + ex.getMessage());
//                        ex.printStackTrace();
                    }                
            }
            
            fieldsValues.put(attName, attValue);                          
        }
        
        String givenId = "" ;
        int foundId = 0 ;
        String foundLabel = "" ;
        
        // Si les params sont OK, on essaye de retrouver la categorie parente mentionée
        if ( getErreurs().isEmpty()) {

            try {
                log.info("Parent is of type [" + fieldsValues.get(att_parentType) + "]");
                if (fieldsValues.get(att_parentType).equals("scatg")) {
                    
                     givenId = fieldsValues.get(att_parentSub) ;
                     
                    Subcategory sc = sctgManager.findSubcategory(Integer.valueOf(givenId));
                    foundId = sc.getSctgId();
                    foundLabel = sc.getSctgLabel();
                    
                    pdt.setPdtCategory((int) sc.getSctgParent());
//                    scatg.setSctgSubparent(BigInteger.valueOf(Long.valueOf(givenId)));
                    pdt.setPdtSubcategory(sc.getSctgId());
                    
                } else {
                    
                     givenId = fieldsValues.get(att_parent) ;
                     
                    Category c = ctgManager.findCategory(Integer.valueOf(givenId));
                    foundId = c.getCtgId();
                    foundLabel = c.getCtgLabel();
                    
                    pdt.setPdtCategory(foundId);
//                    pdt.setPdtCategory(c.getCtgId());
                    
                }
//                log.info("Parent is of type [" + fieldsValues.get(att_parentType) + "] with ID [" + String.valueOf(foundId) + "]") ;

            } catch (Exception e) {
                log.error("Could not get parent ID with value [" + givenId + "]"  + e.getMessage());
                e.printStackTrace();
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
            resultat = "Produit Validé. Essai d'enregistrement.";
            
            pdt.setPdtNom(fieldsValues.get(att_label));
            pdt.setPdtDescription(fieldsValues.get(att_description));
            pdt.setPdtLink(iconLink);
            
            // Pour tout produit generique, quoi qu'il arrive : 
            pdt.setPdtProperty("GENERIC");
            pdt.setPdtSubproperty("GENERIC");
            
            try  {
                pdtManager.create(pdt);
            }catch (Exception e) {
                log.error("Persist Produit Generiq [" + pdt.getPdtNom()
                        + "] has failed because {" +e.getMessage() + "}." );
                setErreur(att_form_record, "Echec serveur, veuillez vérifier vos paramètres ou réessayer plus tard.");
                e.printStackTrace();
            }
            
            /* Initialisation du résultat global de la validation. */
            if ( getErreurs().isEmpty() ) {
                resultat = "<strong>Produit enregistré</strong>.<br> ID [" 
                        + pdt.getPdtId()+ "] ["
                        + pdt.getPdtNom()+ "]" ;
            } else {
                resultat = "<strong>Échec de l'enregistrement.</strong>";
                // Si l'email est correct mais pas le mot de passe => recover
            }
            
        } else {
            resultat = "Mauvais paramètre(s).";

        }
        
        setResultat(resultat);
        
        FileUtils.deleteTempFiles(dataFolder.toString());
        
        if (!getErreurs().isEmpty()) {
            FileUtils.deleteFile(dataFolder + File.separator + iconLink);
        }


        
        return pdt;
    }


    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    @Override
    public void setErreur( String champ, String message ) {
        log.warn("\n==========\tINSERTING EXCEPTION for {Produit Generiq} to [" + champ + "] because > " + message);
        super.setErreur(champ, message);
    }

    public HashMap<String, String> getShortList () {
        List<Produit> found = pdtManager.findProduitListeEntities();
        if (found != null) {
            HashMap<String, String> shortList = new HashMap<>(found.size());
            for (Produit pdt : found) {
                shortList.put(String.valueOf(pdt.getPdtId()), pdt.getPdtNom()) ;
            }
            return shortList;
            
        } else {
            log.error("Did not found any Product.");
        }
        return null;
    }
    public List<Produit> getList () {
        List<Produit> found = pdtManager.findProduitListeEntities();
        if (found != null) {
            return found;            
        } else {
            log.error("Did not found any Product.");
           return null;
        }        
    }
    
    public int getCount () {
        return pdtManager.getProduitListeCount();
    }
    
}