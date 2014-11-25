package org.dmb.trueprice.handlers;

/**
 *
 * @author Work.In.Progress
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.EnseigneJpaController;
import org.dmb.trueprice.controllers.ListesJpaController;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.controllers.ProduitListeJpaController;
import org.dmb.trueprice.entities.Category;
import org.dmb.trueprice.entities.Enseigne;
import org.dmb.trueprice.entities.Liste;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.objects.AvailableList;
import org.dmb.trueprice.objects.ListHeader;
import org.dmb.trueprice.objects.SyncInitResponse;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;

public final class ListeHandler extends BasicHandler {
    
    // Request & Session attributes
    private static final String ATT_SESSION_USER = "sessionUtilisateur";    
    
    private static final String att_label           = "label-liste";
    private static final String att_descri          = "description-liste";
    private static final String att_user            = "user-liste";
    private static final String att_enseigne            = "enseigne-liste";
    private static final String att_color           = "color-liste";
    private static final String att_pdtList              = "pdtList-liste";
    
    
    private static final String att_insert_pdtId              = "pdt_id";
    private static final String att_insert_targetListe              = "target_liste";
    
    

    private static final String att_form_record     = "record";
    
//    private String              resultat;
//    private Map<String, String> erreurs      = new HashMap<String, String>();

    private static final Logger log 
            = InitContextListener.getLogger( ListeHandler.class) ;
    
    private ListesJpaController         lstCtl;
    private ProduitListeJpaController   pdtCtl;
    private MembreJpaController         mbCtl;
    private EnseigneJpaController       esgCtl;
    
    
    Pattern hex = Pattern.compile("^[0-9A-F]{3,6}$") ;
    
    private static ArrayList<String> neededFields = new ArrayList<String> ();
    
    static {
        neededFields.add(att_label);
        // Color MUST be checked before DESCRI 
        neededFields.add(att_color);
        neededFields.add(att_descri);
        neededFields.add(att_user);
        neededFields.add(att_pdtList);
        neededFields.add(att_enseigne);
    }
    
    

    public ListeHandler() {
    }   
    public ListeHandler(ListesJpaController lstCtl) {
        this.lstCtl = lstCtl;
    }

    public ListeHandler(ListesJpaController lstCtl, ProduitListeJpaController pdtCtl, MembreJpaController mbCtl, EnseigneJpaController esgCtl) {
        this.lstCtl = lstCtl;
        this.pdtCtl = pdtCtl;
        this.mbCtl = mbCtl;
        this.esgCtl = esgCtl;
    }

    
    
    public Liste createListes( HttpServletRequest request) {
        
        clearForm();
        
        HashMap<String, String> fieldsValues = new HashMap<String, String>();
        Liste lst = new Liste();             
        
        String resultat = "";   
        
        
        String attValue ;
        for (String attName : neededFields) {

            attValue = ServletUtils.getRequestAttrValue(request, attName);
                
            log.debug("Try to get Attr [" + attName + "]") ;
                
            // Si valeur vide
            if ( attValue == null || "".equals(attValue.trim())
                 || attValue.length() < 1
            ) {
                log.warn("REQ Attr [" + attName + "] has empty value.");
                
                switch (attName) {
                    case att_label :
                        setErreur(att_label, "Le label est indispensable.");
                        break;
                    case att_user :
                        setErreur(att_user, "Le proprietaire de la liste est indispensable.");
                        break;
                }
                
            } 
            
            //// Si la valeur n'est pas nulle -> verif specifiques
            
            //  Description
            else if (attName.equals(att_descri) ) {
                if (  attValue.length() > 1 ) {
                    if (attValue.length() < 5 ||  attValue.trim().length() < 5) {
                        setErreur(att_descri, "La description n'est pas valide. Minimum 5 caracteres.");                    
                    } else {
                        fieldsValues.put(att_descri, attValue);
                    }
                }
            } 
            
            //  Enseigne
            else if (attName.equals(att_enseigne) && attValue.length() < 3) {
                setErreur(att_enseigne, "L'enseigne doit contenir au moins 3 caractères.");
            } 
            
            //  Membre
            else if (attName.equals(att_user) && attValue.length() < 3) {
                
                setErreur(att_user, "Le proprietaire doit contenir au moins 3 caractères.");
            } 
            
            //  Couleur -> min length == 4 : FFF
            else if (attName.equals(att_color)) {
                
                String colorValue = attValue ;
                if ( colorValue != null         
//                        && ! colorValue.equals("")
//                    && colorValue.length() <= 6 &&   colorValue.length() >= 3
//                    && hex.matcher(colorValue).matches()
                ) {
                    if ("FFFFFF".equals(colorValue) ) {
                        
                    } else if (hex.matcher(colorValue).matches()) {
                        colorValue = "[lstCL#" + colorValue + "]" ;
                        fieldsValues.put(attName, colorValue);
                        log.info("Color accepted : " + colorValue);
                    }
                }
                // Si la couleur n'est pas correcte
                else {             
                    setErreur(att_color, "La couleur doit contenir au moins 3 caractères [0-9 A-F].");
                }
            }
            
            // Finally, for simple check params if they are OK.
            else {
                fieldsValues.put(attName, attValue);
            }
                                  
        }
        
        List<Long> pdtIdList = null ; 

        if ( ! getErreurs().isEmpty() ) {
            log.error("Error(s) before check values in DB  : " + getErreurs().size());
            
            for (String key : getErreurs().keySet()) {
                log.warn("Error on [" + key +"]" 
                    + " for [" + getErreurs().get(key) +"]"
                );
            }
        }
        /* Si les paramètres sont OK on persite la categorie */
        else {
            
            // Resultat intermediaire avant essai persistance
            resultat = "Listes Validée. Essai d'enregistrement...";
            
//            for (String key : fieldsValues.keySet()) {
//                Field f = FieldUtils.getField(Liste.class, key);
//                String value = fieldsValues.get(key);
//                
//                if (value != "") {
//                    try {
//                        FieldUtils.writeField(esg, key, value, true);
//                    } catch (IllegalAccessException | IllegalArgumentException ex) {
//                        log.error("Could not get field from enseigne [" + key + "] because > " + ex.getMessage());
//                    }
//                }
//            }
            // Does not work cause fields have different names in JSP
            
            
            /* Record inside Entity */
            // Label
            lst.setLstLabel(fieldsValues.get(att_label));


            
            // Couleur et description ensemble
            // Dabord la couleur
            String colorValue = fieldsValues.get(att_color) ;
            // Ensuite description
            String value = fieldsValues.get(att_descri) ;
            if (  value != null  &&  value.length() > 1 ) {
                // Si descri ok et couleur existe
                if (colorValue != null) {
                    // Ajouter la couleur au debut de la description
                    value = colorValue + value ;
                    log.info("Add Color to description : " 
                            + value.substring(0, 20) + " ... ");
                }
                /* Record inside Entity */
                // Ajoute la description avec ou sans couleur
                lst.setLstDescription(value);
                
            } 
            // Si la description est null mais qu'une couleur est donnee on la conserve
            else if (colorValue != null) {
                lst.setLstDescription(colorValue);
            }

            
            // User
            value = fieldsValues.get(att_user) ;
            if ( value != null && ! value.equals("")  ) {
                Membre mb = null;
                try {                    
                    mb = validationPseudo(value);
                    Long userId = mb.getMbId();
                    log.info("Found User ID [" + userId + "]"); 
                
                    /* Record inside Entity */
                    lst.setLstUser(userId);
                    
                } catch (Exception ex) {
                    setErreur(att_user, "Le proprietaire n'a pas pu etre identifie."
                            + " (" + ex.getMessage() + ")");
                    ex.printStackTrace();
                }
            }
                                                    

            // Enseigne
            value = fieldsValues.get(att_enseigne) ;
            if ( value != null && ! value.equals("")  ) {
                Enseigne esg = null;
                try {
                    esg = validationEnseigne(value);
                    Long esgId = esg.getEsgnId();
                    log.info("Found Enseigne ID [" + esgId + "]");   
                    
                    /* Record inside Entity */
                    lst.setLstEnseigne(esgId);
                    
                } catch (Exception ex) {
                    setErreur(att_enseigne, "L'enseigne n'a pas pu etre identifiee."
                            + " (" + ex.getMessage() + ")");
                }                
            }            
            
//            List<Long> pdtIdList ; 
            
            // Liste de produits
            value = fieldsValues.get(att_pdtList) ;
            if ( value != null && ! value.equals("")  ) {
                // On recupere la liste en tant que Long pour verifier qu'ils sont bien 'parsable'
                pdtIdList = getIDsAsLong(value);
                // On recupere une liste de Long (sans les mauvais le cas echeant)
                if (pdtIdList != null) {
                    
//                    List<Long> validIdList = new ArrayList<>();                    
//                    for (long currentId : IdList) {
//                        Produit pdt = validationProduit(currentId);
//                        if (pdt == null) {                            
//                            IdList.indexOf()
//                        } else {
//                            IdList.indexOf()
//                        }
//                    }
                    
                    
                    // On reimprime la liste finale 
                    String finalValue = getIDsAsString(pdtIdList, "_");
                    log.info("Received IDs :: " + pdtIdList.toString());   
                    log.info("Received IDs for record :: " + finalValue);   
                    
                    /* Record inside Entity */
                    lst.setLstProduits(finalValue);
                }
            }            
            
            
        } 
        
        
        
        if (getErreurs().isEmpty()) {
            
            
            log.info(
                "Gonna try to persist list [" + lst.getLstLabel() + "] "
                + "\n Description [" + lst.getLstDescription() + "]"
                + "\n Membre ID[" + lst.getLstUser()+ "]"
                + "\n Enseigne ID [" + lst.getLstEnseigne() + "]"
                + "\n Produits IDs [" + lst.getLstProduits() + "]"
            );
            
            try  {
                lstCtl.create(lst);
            }catch (Exception e) {
                log.error("Persist Listes [" + lst.getLstLabel()
                        + "] has failed because {" +e.getMessage() + "}." );
                setErreur(att_form_record, "Echec serveur, veuillez vérifier vos paramètres ou réessayer plus tard.");
                e.printStackTrace();
            }
            
            /* Initialisation du résultat global de la validation. */
            if ( getErreurs().isEmpty() ) {
                resultat = "<strong>Listes enregistrée</strong>. ID [" 
                        + lst.getLstId()+ "] ["
                        + lst.getLstLabel()+ "]." ;
                
                /**
                 * MISE A JOUR DE SyncInitResponse !!!
                 */
                updateSyncInitResponse(
                        
                        // Membre
                        ((Membre) ServletUtils.getSessionAttrObject
                            //Session
                            (request.getSession(false),ATT_SESSION_USER)
                            // mbMail
                        )    .getMbMail(),
                        // ID de la liste
                        Long.valueOf(Integer.toString(lst.getLstId() )),
                        // Label de la liste
                        lst.getLstLabel(),
                        // compte des produits
                        (pdtIdList == null ? 0 : pdtIdList.size())
                );
                
            } else {
                resultat += " . . . <strong>Échec de l'enregistrement.</strong>";
            }
            
        } else {
            resultat = "Mauvais paramètre(s).";
        }
        
        setResultat(resultat);


        return lst;
    }

    public void insertProduct (HttpServletRequest request) {
        
        clearForm();
        
        HashMap<String, String> fieldsValues = new HashMap<String, String>();
        
        
   
        Liste lst = new Liste();             
        
        String resultat = "";   
        
        
        String attValue ;
        for (String attName : new String[]{att_insert_pdtId,att_insert_targetListe}) {

            attValue = ServletUtils.getRequestAttrValue(request, attName);
                
            log.debug("Try to get Attr [" + attName + "]") ;
                
            // Si valeur vide
            if ( attValue == null || "".equals(attValue.trim())
                 || attValue.length() < 1
            ) {
                log.warn("REQ Attr [" + attName + "] has empty value.");
                
                switch (attName) {
                    case att_insert_pdtId :
                        setErreur(att_label, "Le produit a ajouter est indispensable.");
                        break;
                    case att_insert_targetListe :
                        setErreur(att_user, "La liste conernée est indispensable.");
                        break;
                }
                
            } 
            
            //// Si la valeur n'est pas nulle -> verif specifiques
            
            
            // Finally, for simple check params if they are OK.
            else {
                fieldsValues.put(attName, attValue);
            }
                                  
        }
        
        
        Produit pdt = null ;
        
        
        if ( ! getErreurs().isEmpty() ) {
            log.error("Error(s) before check values in DB  : " + getErreurs().size());
            
            for (String key : getErreurs().keySet()) {
                log.warn("Error on [" + key +"]" 
                    + " for [" + getErreurs().get(key) +"]"
                );
            }
        }
        
        /* Si les paramètres sont OK on persite la categorie */
        else {
            
            // Resultat intermediaire avant essai persistance
            resultat = "Parametres recus. Recherche en DB ... ";
            
            lst = lstCtl.findListes(Integer.parseInt(fieldsValues.get(att_insert_targetListe)));
            
            if (lst == null) { setErreur(att_insert_targetListe, "La liste demandée n'a pû être trouvée");}
            
            pdt = pdtCtl.findProduitListe(Long.valueOf(fieldsValues.get(att_insert_pdtId)));
            
            if (pdt == null) { setErreur(att_insert_pdtId, "Le produit choisi n'a pû être trouvé");}
            
        
        }
        
        
        if (getErreurs().isEmpty()) {
            
            
            log.info(
                "Gonna try to modify list [" + lst.getLstLabel() + "] "
                + "\n Description [" + lst.getLstDescription() + "]"
                + "\n Membre ID[" + lst.getLstUser()+ "]"
                + "\n Enseigne ID [" + lst.getLstEnseigne() + "]"
                + "\n Produits IDs [" + lst.getLstProduits() + "]"
            );
            
            
            String originalProductLine = lst.getLstProduits() ;
            
            log.info("Value before insert [" + originalProductLine + "]");
            
            List<Long> pdtLineIds = null ;
            
            if (originalProductLine != null && originalProductLine.length() > 0) {
                pdtLineIds = getIDsAsLong(originalProductLine) ;
            } else {
                log.info("Product line should be initialized");
                pdtLineIds = new ArrayList<Long>();
            }
            
            log.info("Array size before = " + pdtLineIds.size());
            
            pdtLineIds.add(pdt.getPdtId());
            
            log.info("Array size after = " + pdtLineIds.size());
            
            String finalProductLine = getIDsAsString(pdtLineIds, "_");
            
            log.info("Value after insert [" + finalProductLine + "]");
            
            lst.setLstProduits(finalProductLine);
            
            try  {
                lstCtl.edit(lst);
            }catch (Exception e) {
                log.error("Edit Liste [" + lst.getLstLabel()
                        + "] has failed because {" +e.getMessage() + "}." );
                setErreur(att_form_record, "Echec serveur, veuillez vérifier vos paramètres ou réessayer plus tard.");
                e.printStackTrace();
            }
            
            /* Initialisation du résultat global de la validation. */
            if ( getErreurs().isEmpty() ) {
                resultat = "<strong>Liste modifiée avec succès. Le produit a été ajouté.</strong>" ;
                
                /**
                 * MISE A JOUR DE SyncInitResponse !!!
                 */
                updateSyncInitResponse(
                        
                        // Membre
                        ((Membre) ServletUtils.getSessionAttrObject
                            //Session
                            (request.getSession(false),ATT_SESSION_USER)
                            // mbMail
                        )    .getMbMail(),
                        // ID de la liste
                        Long.valueOf(Integer.toString(lst.getLstId() )),
                        // Label de la liste
                        lst.getLstLabel(),
                        // compte des produits
                        pdtLineIds.size()
                );
                
            } else {
                resultat += " . . . <strong>Échec de l'ajout du produit.</strong>";
            }
            
        } else {
            resultat = "Mauvais paramètre(s).";
        }
        
        setResultat(resultat);

        
        
//        return false;
    }

    public void removeProduct (HttpServletRequest request) {
        
        clearForm();
        
        HashMap<String, String> fieldsValues = new HashMap<String, String>();
        
        
   
        Liste lst = new Liste();             
        
        String resultat = "";   
        
        
        String attValue ;
        for (String attName : new String[]{att_insert_pdtId,att_insert_targetListe}) {

            attValue = ServletUtils.getRequestAttrValue(request, attName);
                
            log.debug("Try to get Attr [" + attName + "]") ;
                
            // Si valeur vide
            if ( attValue == null || "".equals(attValue.trim())
                 || attValue.length() < 1
            ) {
                log.warn("REQ Attr [" + attName + "] has empty value.");
                
                switch (attName) {
                    case att_insert_pdtId :
                        setErreur(att_label, "Le produit a supprimer est indispensable.");
                        break;
                    case att_insert_targetListe :
                        setErreur(att_user, "La liste conernée est indispensable.");
                        break;
                }
                
            } 
            
            //// Si la valeur n'est pas nulle -> verif specifiques
            
            
            // Finally, for simple check params if they are OK.
            else {
                fieldsValues.put(attName, attValue);
            }
                                  
        }
        
        
        Produit pdt = null ;
        
        
        if ( ! getErreurs().isEmpty() ) {
            log.error("Error(s) before check values in DB  : " + getErreurs().size());
            
            for (String key : getErreurs().keySet()) {
                log.warn("Error on [" + key +"]" 
                    + " for [" + getErreurs().get(key) +"]"
                );
            }
        }
        
        /* Si les paramètres sont OK on persite la categorie */
        else {
            
            // Resultat intermediaire avant essai persistance
            resultat = "Parametres recus. Recherche en DB ... ";
            
            lst = lstCtl.findListes(Integer.parseInt(fieldsValues.get(att_insert_targetListe)));
            
            if (lst == null) { setErreur(att_insert_targetListe, "La liste demandée n'a pû être trouvée");}
            
            pdt = pdtCtl.findProduitListe(Long.valueOf(fieldsValues.get(att_insert_pdtId)));
            
            if (pdt == null) { setErreur(att_insert_pdtId, "Le produit choisi n'a pû être trouvé");}
            
        
        }
        
        
        if (getErreurs().isEmpty()) {
            
            
            log.info(
                "Gonna try to modify list [" + lst.getLstLabel() + "] "
//                + "\n Description [" + lst.getLstDescription() + "]"
//                + "\n Membre ID[" + lst.getLstUser()+ "]"
//                + "\n Enseigne ID [" + lst.getLstEnseigne() + "]"
//                + "\n Produits IDs [" + lst.getLstProduits() + "]"
            );
            
            
            String originalProductLine = lst.getLstProduits() ;
            
            log.info("Value before remove [" + originalProductLine + "]");
            
            List<Long> pdtLineIds = null ;
            
            if (originalProductLine != null) {
                pdtLineIds = getIDsAsLong(originalProductLine) ;
            } else {
                pdtLineIds = new ArrayList<Long>();
            }
            
            pdtLineIds.remove(pdt.getPdtId());
            
            String finalProductLine = getIDsAsString(pdtLineIds, "_");
            
            log.info("Value after remove [" + finalProductLine + "]");
            
            lst.setLstProduits(finalProductLine);
            
            try  {
                lstCtl.edit(lst);
            }catch (Exception e) {
                log.error("Edit Liste [" + lst.getLstLabel()
                        + "] has failed because {" +e.getMessage() + "}." );
                setErreur(att_form_record, "Echec serveur, veuillez vérifier vos paramètres ou réessayer plus tard.");
                e.printStackTrace();
            }
            
            /* Initialisation du résultat global de la validation. */
            if ( getErreurs().isEmpty() ) {
                resultat = "<strong>Liste modifiée avec succès. Le produit a été retiré de la liste.</strong>" ;
                
                /**
                 * MISE A JOUR DE SyncInitResponse !!!
                 */
                updateSyncInitResponse(
                        
                        // Membre
                        ((Membre) ServletUtils.getSessionAttrObject
                            //Session
                            (request.getSession(false),ATT_SESSION_USER)
                            // mbMail
                        )    .getMbMail(),
                        // ID de la liste
                        Long.valueOf(Integer.toString(lst.getLstId() )),
                        // Label de la liste
                        lst.getLstLabel(),
                        // compte des produits
                        pdtLineIds.size()
                );               
                
            } else {
                resultat += " . . . <strong>Échec de l'ajout du produit.</strong>";
            }
            
        } else {
            resultat = "Mauvais paramètre(s).";
        }
        
        setResultat(resultat);

        
        
//        return false;
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    @Override
    public void setErreur( String champ, String message ) {
        log.warn("\n==========\tINSERTING EXCEPTION for {Listes} to [" + champ + "] because > " + message);
        super.setErreur(champ, message);
    }

    private String getIDsAsString(List<Long> idList, String separator) {
        String inlineValue = "";
        // Imprimer la liste en une ligne
        for (long id : idList) {
            inlineValue += String.valueOf(id);
            inlineValue += separator;
        }
        
        // Supprimer le dernier separateur
//        inlineValue = inlineValue.substring(0, 
//            inlineValue.lastIndexOf(separator) -1
//        );
        
        // Supprimer le dernier separateur
        if (inlineValue.endsWith(separator)) {
            inlineValue = inlineValue.substring(0, inlineValue.length()-1);
        }
        
        return inlineValue;
    }
    private List<Long> getIDsAsLong(String inlineValue) {
        
        Boolean isList = false;

        // Dabord vérifier qu'il s'agit bien d'une LISTE de produits(IDs)
        String separator = "";
        if (inlineValue.contains("_")) {
            separator = "_";
            isList = true;
        } else if (inlineValue.contains("-")) {
            separator = "-";
            isList = true;
        } 

        List<Long> IdList = new ArrayList<Long>();
        String tempStrList = inlineValue;
        Long currentID ;

        if (isList) {
            String currentStrId = "";
            int endSubString = 0 ;

            do {
                // recupere l'index du 1er separateur
                endSubString = (tempStrList.contains(separator) ?
                    tempStrList.indexOf(separator) :
                        tempStrList.length()
                    );                        
                log.info("Work on ID list :: " + tempStrList 
                    + "Separator :: " + separator
                    + " at index " + endSubString
                    );
                // l'ID courant va jusqu'au premier separateur
                currentStrId = tempStrList.substring(0, endSubString);
                currentID = Long.valueOf(currentStrId);

                if (currentID != null) {
                    log.info("Got current ID :: " + currentID + " ");
                    // Enregistrer l'ID
                    IdList.add(currentID);
                    // Supprimer l'id courant
//                    tempStrList = tempStrList.replaceFirst(currentStrId, "");
                    tempStrList = tempStrList.substring(currentStrId.length());
                    
                    log.info(" Added ID [" + currentID.toString() + "]");
                }
                else {
                    log.info("Could not add/parse ID [" + currentStrId + "]");
                }
                // Supprimer le separateur
                if (tempStrList.contains(separator) 
//                        && 
//                    tempStrList.indexOf(separator) == 0
                ) {
                    tempStrList = tempStrList.replaceFirst(separator, "");
                }

                // Sinon c'est la fin de la liste
                else { isList = false ;}

            } while (isList);
        }
        // Si aucun separateur -> liste avec 1 produit ?
        else {
            if (inlineValue.length() > 1) {
                try {
                    currentID = Long.valueOf(inlineValue);
                    IdList.add(currentID);
                    log.info("List contains [1] product with ID [" + currentID.toString() + "]");
                } catch (NumberFormatException e) {
                    setErreur(att_pdtList, "L'unique produit de la liste "
                            + "n'a pu être ajouté.("+ e.getMessage() + ")");
                }
            }
        }
        
        return IdList;
    }
    private Membre validationPseudo(String pseudo) throws Exception {
        Membre m = null ;
        if (pseudo != null) {
            if (!ServletUtils.validPseudoSyntax(pseudo)) {
                throw new Exception("Merci de saisir un pseudo valide.");
            } else {
                try {
                    m = mbCtl.findByPseudo(pseudo);
                    if (m == null) {
                        throw new EntityNotFoundException("Ce pseudo n'existe pas.");
                    }
                } catch (NullPointerException | NoResultException e) {
                    log.warn("Pseudo [" + pseudo + "] not found.");
                }
            }
        }
        // Si le pseudo est null
        else {
            throw new Exception("Merci de saisir un pseudo.");
        }
        return m;
    }    
    private Enseigne validationEnseigne (String label) throws Exception {
        Enseigne esg = null ;
        if (label != null) {
            try {
                esg = esgCtl.findByLabel(label);
                if (esg == null) {
                    throw new EntityNotFoundException("Cet enseigne n'existe pas.");
                }
            } catch (NullPointerException | NoResultException e) {
                log.debug("Label [" + label + "] not found.");
            }
        }
        // Si le pseudo est null
        else {
            throw new Exception("Merci de saisir un label.");
        }
        return esg;
    }    
    
    public List<Liste> getList () {
        List<Liste> found = lstCtl.findListesEntities();
        if (found != null) {
            return found;            
        } else {
            log.error("Did not found any Member's Listes.");
           return null;
        }        
    }    
    public HashMap<String, String> getShortList () {
        List<Liste> found = lstCtl.findListesEntities();
        if (found != null) {
            HashMap<String, String> shortList = new HashMap<>(found.size());
            for (Liste l : found) {
                shortList.put(String.valueOf(l.getLstId()),l.getLstLabel()) ;
            }
            return shortList;
            
        } else {
            log.error("Did not found any Member Liste.");
        }
        return null;
    }    
        
    public int getCount () {
        return lstCtl.getListesCount();
    }
        

        
    public List<Liste> getListByMember (long MemberID) {
        return lstCtl.findByUser(MemberID);
    }        
    
    public HashMap<String, String> getShortListByMember (long MemberID) {
        List<Liste> found = lstCtl.findByUser(MemberID);
        if (found != null) {
            HashMap<String, String> shortList = new HashMap<>(found.size());
            for (Liste l : found) {
                shortList.put(String.valueOf(l.getLstId()),l.getLstLabel()) ;
            }
            return shortList;
            
        } else {
            log.error("Did not found any Member Liste.");
        }
        return null;
    }      
    
    
    private void updateSyncInitResponse (String mbMail, Long listeId, String listeLabel, int pdtCount){
        ServletUtils.addUpdateEntryToSyncInitResponse(mbMail, 
//            new ListHeader(listeId, listeLabel, ServletUtils.getFormattedDateNow(), pdtCount)        
            new AvailableList(listeId, listeLabel, ServletUtils.getFormattedDateNow(), pdtCount)        
        );
    }
    
}