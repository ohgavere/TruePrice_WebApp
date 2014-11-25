package org.dmb.trueprice.handlers;

/**
 *
 * @author Work.In.Progress
 */

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.EnseigneJpaController;
import org.dmb.trueprice.entities.Enseigne;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;

public final class EnseigneHandler extends BasicHandler {
    private static final String att_label  = "label-enseigne";
    private static final String att_description   = "description-enseigne";
    private static final String att_ville   = "ville";
    private static final String att_adresse   = "adresse";
    private static final String att_cp   = "cp";
    private static final String att_tel   = "tel";
    private static final String att_form_record   = "record";
    
//    private String              resultat;
//    private Map<String, String> erreurs      = new HashMap<String, String>();

    private static final Logger log 
            = InitContextListener.getLogger( EnseigneHandler.class) ;
    
    private EnseigneJpaController      esgManager;
    
    private static ArrayList<String> neededFields = new ArrayList<String> ();
    
    static {
        neededFields.add(att_label);
        neededFields.add(att_description);
        neededFields.add(att_ville);
        neededFields.add(att_adresse);
        neededFields.add(att_cp);
        neededFields.add(att_tel);
    }

    public EnseigneHandler(EnseigneJpaController esgManager) {
        this.esgManager = esgManager;
    }        

    public Enseigne createEnseigne( HttpServletRequest request) {
        
        HashMap<String, String> fieldsValues = new HashMap<String, String>();
        Enseigne esg = new Enseigne();
        
        String resultat = "";       
        for (String attName : neededFields) {

            String attValue = ServletUtils.getRequestAttrValue(request, attName);
                
            log.debug("Try to get Attr [" + attName + "]") ;
                
            if ( attValue == null || "".equals(attValue)) {
                
                log.warn("REQ Attr [" + attName + "] has empty value.");
                
                switch (attName) {
                    case att_label :
                        setErreur(att_label, "Le label est indispensable.");
                        break;
                    case att_ville :
                        setErreur(att_ville, "La ville où se trouve l'enseigne est indispensable.");
                        break;
                }
                
            } else if (attName.equals(att_description) && attValue.length()<10) {
                setErreur(att_description, "La description doit contenir au moins 10 caractères.");
            } else if (attName.equals(att_ville) && attValue.length() < 3) {
                setErreur(att_description, "La ville doit contenir au moins 3 caractères.");
            }
            // Finally, when all params are OK.
            else {
                fieldsValues.put(attName, attValue);
            }
                                  
        }
        
        

        /* Si les paramètres sont OK on persite la categorie */
        if ( getErreurs().isEmpty() ) {
            
            // Resultat intermediaire avant essai persistance
            resultat = "Enseigne Validée. Essai d'enregistrement...";
            
//            for (String key : fieldsValues.keySet()) {
//                Field f = FieldUtils.getField(Enseigne.class, key);
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
            
            esg.setEsgnLabel(fieldsValues.get(att_label));
            esg.setEsgnVille(fieldsValues.get(att_ville));
            
            String value = fieldsValues.get(att_adresse) ;
            if ( value != null && ! value.equals("") ) {
                esg.setEsgnAdresse(value);
            }
            value = fieldsValues.get(att_description) ;
            if ( value != null && ! value.equals("")  ) {
                esg.setEsgnDescription(value);
            }
            value = fieldsValues.get(att_cp) ;
            if ( value != null && ! value.equals("")  ) {
                esg.setEsgnCp(value);
            }
            value = fieldsValues.get(att_tel) ;
            if ( value != null && ! value.equals("")  ) {
                esg.setEsgnTel(value);
            }
            
            
            try  {
                esgManager.create(esg);
            }catch (Exception e) {
                log.error("Persist Enseigne [" + esg.getEsgnLabel()
                        + "] has failed because {" +e.getMessage() + "}." );
                setErreur(att_form_record, "Echec serveur, veuillez vérifier vos paramètres ou réessayer plus tard.");
                e.printStackTrace();
            }
            
            /* Initialisation du résultat global de la validation. */
            if ( getErreurs().isEmpty() ) {
                resultat = "<strong>Enseigne enregistrée</strong>. ID [" 
                        + esg.getEsgnId()+ "] ["
                        + esg.getEsgnLabel()+ "] ["
                        + esg.getEsgnVille()+ "]." ;
            } else {
                resultat += " . . . <strong>Échec de l'enregistrement.</strong>";
                // Si l'email est correct mais pas le mot de passe => recover
            }
            
        } else {
            resultat = "Mauvais paramètre(s).";
            // Si l'email est correct mais pas le mot de passe => recover
        }
        
        setResultat(resultat);


        return esg;
    }


    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    @Override
    public void setErreur( String champ, String message ) {
        log.warn("\n==========\tINSERTING EXCEPTION for {Enseigne} to [" + champ + "] because > " + message);
        super.setErreur(champ, message);
    }

    
}