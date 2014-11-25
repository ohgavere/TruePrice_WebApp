package org.dmb.trueprice.handlers;

/**
 *
 * @author Work.In.Progress
 */

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.MarqueJpaController;
import org.dmb.trueprice.entities.Marque;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;

public final class MarqueHandler extends BasicHandler {
    private static final String att_label  = "label-marque";
    private static final String att_description   = "description-marque";
//    private static final String att_ville   = "ville";
//    private static final String att_adresse   = "adresse";
//    private static final String att_cp   = "cp";
//    private static final String att_tel   = "tel";
    private static final String att_form_record   = "record";
    
//    private String              resultat;
//    private Map<String, String> erreurs      = new HashMap<String, String>();

    private static final Logger log 
            = InitContextListener.getLogger( MarqueHandler.class) ;
    
    private MarqueJpaController      mrqCtl;
    
    private static ArrayList<String> neededFields = new ArrayList<String> ();
    
    static {
        neededFields.add(att_label);
        neededFields.add(att_description);
//        neededFields.add(att_ville);
//        neededFields.add(att_adresse);
//        neededFields.add(att_cp);
//        neededFields.add(att_tel);
    }

    public MarqueHandler(MarqueJpaController esgManager) {
        this.mrqCtl = esgManager;
    }
    
    

    public Marque createMarque( HttpServletRequest request) {
        
        HashMap<String, String> fieldsValues = new HashMap<String, String>();
        Marque mrq = new Marque();
        
          
        for (String attName : neededFields) {

            String attValue = ServletUtils.getRequestAttrValue(request, attName);
                
            log.debug("Try to get Attr [" + attName + "]") ;
                
            if ( attValue == null || "".equals(attValue)) {
                
                log.warn("REQ Attr [" + attName + "] has empty value.");
                
                switch (attName) {
                    case att_label :
                        setErreur(att_label, "Le label est indispensable.");
                        break;
//                    case att_description :
//                        setErreur(att_description, "La ville où se trouve la marque est indispensable.");
//                        break;
                }
                
            } else if (attName.equals(att_description) && attValue.length()<10) {
                setErreur(att_description, "La description doit contenir au moins 10 caractères.");
//            } else if (attName.equals(att_ville) && attValue.length() < 3) {
//                setErreur(att_description, "La ville doit contenir au moins 3 caractères.");
            }
            // Finally, when all params are OK.
            else {
                fieldsValues.put(attName, attValue);
            }
                                  
        }
        
        String resultat = "";     

        /* Si les paramètres sont OK on persite la categorie */
        if ( getErreurs().isEmpty() ) {
            
            // Resultat intermediaire avant essai persistance
            resultat = "Marque Validée. Essai d'enregistrement...";
            
//            for (String key : fieldsValues.keySet()) {
//                Field f = FieldUtils.getField(Marque.class, key);
//                String value = fieldsValues.get(key);
//                
//                if (value != "") {
//                    try {
//                        FieldUtils.writeField(esg, key, value, true);
//                    } catch (IllegalAccessException | IllegalArgumentException ex) {
//                        log.error("Could not get field from marque [" + key + "] because > " + ex.getMessage());
//                    }
//                }
//            }
            // Does not work cause fields have different names in JSP
            
            
            
            mrq.setMrqLabel(fieldsValues.get(att_label));
//            esg.setMrqDescription(fieldsValues.get(att_description));
            
            String value = fieldsValues.get(att_description) ;
            if ( value != null && ! value.equals("") ) {
                mrq.setMrqDescription(value);
            }
            
            
            try  {
                mrqCtl.create(mrq);
            }catch (Exception e) {
                log.error("Persist Marque [" + mrq.getMrqLabel()
                        + "] has failed because {" +e.getMessage() + "}." );
                setErreur(att_form_record, "Echec serveur, veuillez vérifier vos paramètres ou réessayer plus tard.");
                e.printStackTrace();
            }
            
            /* Initialisation du résultat global de la validation. */
            if ( getErreurs().isEmpty() ) {
                resultat = "<strong>Marque enregistrée</strong>. ID [" 
                        + mrq.getMrqId()+ "]"
                        + " [" + mrq.getMrqLabel()+ "]<br>"
                        + "[" + mrq.getMrqDescription() + "]"
                         ;
            } else {
                resultat += " . . . <strong>Échec de l'enregistrement.</strong>";
            }
            
        } else {
            resultat = "Mauvais paramètre(s).";
            // Si l'email est correct mais pas le mot de passe => recover
        }
        
        setResultat(resultat);


        return mrq;
    }


    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    @Override
    public void setErreur( String champ, String message ) {
        log.warn("\n==========\tINSERTING EXCEPTION for {Marque} to [" + champ + "] because > " + message);
        super.setErreur(champ, message);
    }

    
}