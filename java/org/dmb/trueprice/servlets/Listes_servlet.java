/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.EnseigneJpaController;
import org.dmb.trueprice.controllers.ListesJpaController;
import org.dmb.trueprice.controllers.MembreJpaController;
import org.dmb.trueprice.controllers.ProduitListeJpaController;
import org.dmb.trueprice.entities.Enseigne;
import org.dmb.trueprice.entities.Liste;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.handlers.EnseigneHandler;
import org.dmb.trueprice.handlers.ListeHandler;
import org.dmb.trueprice.objects.ListeFrontend;
import static org.dmb.trueprice.servlets.Dashboard_servlet.ATT_SESSION_USER;
import org.dmb.trueprice.utils.internal.InitContextListener;
import org.dmb.trueprice.utils.internal.ServletUtils;

/**
 *
 * @author Guitch
 */
@WebServlet ( name = "Listes", 
        urlPatterns = {
            "/listes", //
            "/listes/add",      //  Ajouter une nouvelle liste
            "/listes/synchronize",      //  Envoyer une liste sur le telephone
            "/listes/download",      //  Telecharger une liste en xml
            "/listes/share",      //  Partager une liste via ...
            "/listes/insert",   // Retirer un produit d'une liste
            "/listes/remove",   // Retirer un produit d'une liste
            "/listes/delete"    // Supprimer une liste
        })

public class Listes_servlet extends HttpServlet {
    
    private static final Logger log 
        = InitContextListener.getLogger( Listes_servlet.class) ;
    
    @EJB 
    private  ListesJpaController lstCtl ;
    private static ListeHandler lstForm ;    
    
    @EJB 
    private  ProduitListeJpaController pdtCtl ;
    @EJB 
    private  MembreJpaController userCtl ;
    @EJB 
    private  EnseigneJpaController esgnCtl ;
//    private static EnseigneHandler esgnForm ;      
    
    private static final String URL_LISTES      =   "/listes" ;
    private static final String URL_ADD         =   "/add" ;
    private static final String URL_INSERT         =   "/insert" ;
    private static final String URL_REMOVE         =   "/remove" ;

    private static final String VIEW_LISTE_ADD = "/WEB-INF/liste-add.jsp" ;
    private static final String VIEW_LISTE_EDIT = "/WEB-INF/liste-edit.jsp" ;
    private static final String VIEW_LISTES_SUMMARY = "/WEB-INF/subview/listes/liste-summary.jsp" ;
    
    private static final String att_Form = "form";    
    
    private static final String ATT_SESSION_USER = "sessionUtilisateur";
//    public static final String ATT_SESSION_USER_PSEUDO = "sessionUtilisateur_PSEUDO";
    
    private static final String att_Liste = "liste";    
    
    private static final String att_ListMbList = "listMbListes";    
    private static ArrayList<Liste> listMemberListes = new ArrayList<Liste>() ; 
    
    private static final String att_ListDisplayedMbList = "listDsplMbListes";
    private static ArrayList<ListeFrontend> listDisplayMemberListes = new ArrayList<ListeFrontend>() ; 
    
    private static final String ATT_ESGN_LABELS = "esgnLabels" ;
    private static ArrayList<String> listEsgnLabels = new ArrayList<String>();

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
                
        lstForm = new ListeHandler(lstCtl);
//        esgnForm = new EnseigneHandler(esgnCtl);
        
        lstForm     = new ListeHandler ( lstCtl, pdtCtl, userCtl, esgnCtl);
    }
      
    
    
    

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          
        // L'action/page demandee
        String action = findAction(request.getRequestURI());
        
        log.info("Action found is [" + action + "]");

        // Recuperer le membre
        Membre mb = (Membre) ServletUtils.getSessionAttrObject(request.getSession(), ATT_SESSION_USER);

        // Recupere les listes de la DB
        /* listMemberListes = */ getMemberListes(mb.getMbId());
        
        // Les transformer en frontend liste
            // Remplit aussi listeEsgnLabels
        listDisplayMemberListes = getFrontendListEsgn(listMemberListes);
        
        // Forward et attributs selon l'action
        switch (action) {
            
            case URL_ADD :
                
                request.setAttribute(ATT_ESGN_LABELS, listEsgnLabels);
                
                /* Affichage de la page d'ajout de liste */
                this.getServletContext().getRequestDispatcher(VIEW_LISTE_ADD).forward(request, response);
            break;
                
            case URL_REMOVE :
                
                lstForm.removeProduct(request);
                
                request.setAttribute(att_Form,lstForm);
                
                /* Affichage de la page d'ajout de liste */
                this.getServletContext().getRequestDispatcher(VIEW_LISTE_EDIT).forward(request, response);
                
            break;
                
            // Liste summary est coupe par default pour rester la page par defaut d'un appel sur cette servlet
            case URL_LISTES :                
            default :
                
//                request.setAttribute(att_ListMbList, listMemberListes);
                request.setAttribute(att_ListDisplayedMbList, listDisplayMemberListes);                            
                
                /* Affichage de la page d'ajout de liste */
                this.getServletContext().getRequestDispatcher(
                        VIEW_LISTES_SUMMARY).forward(request, response);
                
            break;
        }
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String requestedUri = request.getRequestURI() ;
        
        requestedUri = requestedUri.substring(request.getContextPath().length());
        
//        log.info("Requested URI : [" + requestedUri + "]");
        
//        HttpSession session = request.getSession(false);
        
        Liste lst = new Liste();
        
        switch (requestedUri) {
            
            case URL_LISTES + URL_ADD :
                    // Imperativement creer l'enseigne en premier

                    // Ensuite creer la liste avec ou sans l'enseigne selon le resultat de la creation de l'enseigne

                    lst = lstForm.createListes(request);
                    request.setAttribute(att_Form, lstForm);     
                    request.setAttribute(att_Liste, lst);

                    /* Affichage de la page d'ajout de liste */
                    this.getServletContext().getRequestDispatcher(
                        VIEW_LISTE_ADD).forward(request, response);
                    
                break;
                
            case URL_LISTES + URL_INSERT :
                
                    lstForm.insertProduct(request);
                    request.setAttribute(att_Form, lstForm);     
//                    request.setAttribute(att_Liste, lst);                

                    /* Affichage de la page d'ajout de liste */
                    this.getServletContext().getRequestDispatcher(
                        VIEW_LISTE_EDIT).forward(request, response);
                    
                break;
                
            default:
                break;
        }
        
        
        
//        if (requestedUri.startsWith((URL_LISTES + URL_ADD))) {
        
            // Imperativement creer l'enseigne en premier
            
            
            
            // Ensuite creer la liste avec ou sans l'enseigne selon le resultat de la creation de l'enseigne
            
//            lst = lstForm.createListes(request);
//            request.setAttribute(att_Form, lstForm);
            
//        }
        
//        else {
//            /* Affichage de la page d'ajout de liste */
//            this.getServletContext().getRequestDispatcher(
//                    VIEW_LISTE_ADD).forward(request, response);
//        }
        
//        request.setAttribute(att_Liste, lst);
//        
//        ListeFrontend frontListe = new ListeFrontend(lst, esgnCtl.findEnseigne(lst.getLstEnseigne()).getEsgnVille());
//        
//        request.setAttribute(att_Liste, frontListe);
        
        
//        request.setAttribute(att_Form, lstForm);

//        /* Affichage de la page d'ajout de liste */
//        this.getServletContext().getRequestDispatcher(
//                VIEW_LISTE_ADD).forward(request, response);
    }
    
    private String findAction(String requestURI) {
        
//        log.info("Received URI  = " +requestURI);
        
        requestURI = requestURI.substring(
            requestURI.indexOf(URL_LISTES)
        );
        
//        log.info("substr URI = " +requestURI);
        
        if (requestURI.contentEquals(URL_LISTES) ) {
            return URL_LISTES ;
        } else {
            
            if (requestURI.length() > URL_LISTES.length()) {
                
                requestURI = requestURI.substring(URL_LISTES.length());
                
                if (requestURI != null && requestURI.length() > 0) {
                    switch (requestURI) {
                        case URL_LISTES :
                            log.warn("Not supposed to ask this way : " + requestURI);
                            return URL_LISTES;                            
                        case URL_ADD:
                            return URL_ADD;                            
                        case URL_REMOVE:
                            return URL_REMOVE;                            
                        case "" :                            
                        default:
                            log.warn("Not supposed to ask this way : " + requestURI);
                            break;
                    }
                }
                // Ne devrais jamais arriver ici
                else {
                    return URL_LISTES;
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

    
    
    private List<Liste> getMemberListes(long memberID) {
        
//        if (sctgForm == null) {
//            sctgForm = new SubCategoryHandler( sctgCtl, ctgCtl);  
//        }
//        int cgCount = sctgForm.getCount() ;
//        int cgCount = listSubCategories.size() ;
        
        
//        int lstCount = 0;
//        if (listMemberListes != null) {
////            lstCount = listMemberListes.size();
//            lstCount = lstForm.getCount();
//        }
        
        
        
        // We try to get an existing list (valid if same size)
//        listMemberListes = InitContextListener.getListMemberListes(lstCount);
        
        // if not existing, we have to retrieve it;
        // As initCtxtLitnr returns null, change the test
//        if (    listSubCategories.isEmpty() 
//        if (    listSubCategories == null 
        
        
        
//        if (    listMemberListes == null 
//                // Not needed anymore cause handled by initContextListener
//            || (listMemberListes.size() != lstCount && lstCount != 0 )
//        ) {
            log.info("Retrieve Member's List list");
            
            listMemberListes = new ArrayList<Liste>();
            
//            listSubCategories.removeAll(listSubCategories);
//            listMemberListes.addAll(lstForm.getList());
            listMemberListes.addAll(lstForm.getListByMember(memberID)); 
            
            // If we get the up-to-date list, don't forget to share it !
//            InitContextListener.setListmemberListes(listMemberListes);
            log.info("Got member liste list with size [" + listMemberListes.size() + "]");
//        }
        return listMemberListes;
//        return DataProvider.getListSubCategories();
    }
    
    private ArrayList<String> getEsgnLabelsByMemberList(ArrayList<Liste> listMembersListes) {
        ArrayList<String> esgnLabels = new ArrayList<String>();
        
        for (Liste liste : listMembersListes) {
            
            String esgnLabel = esgnCtl.findEnseigne(liste.getLstEnseigne()).getEsgnLabel();
            
            if ( ! esgnLabels.contains(esgnLabel) ) { esgnLabels.add(esgnLabel); }
                        
        }        
        
        return esgnLabels;
    }
    
    private ArrayList<ListeFrontend> getFrontendListEsgn(ArrayList<Liste> listMembersListes) {
    
        ArrayList<ListeFrontend> returned = new ArrayList<ListeFrontend>();
            
            String finalStrVilleShort = null ;
            String finalStrEsgnFullLabel = null ;
        
            String esgnVilleShort = "" ;            
            String esgnLabel = "";
            String esgnVille = "";

            for (Liste liste : listMembersListes) {
            
            Enseigne esgn = esgnCtl.findEnseigne(liste.getLstEnseigne());            
            
            if (esgn != null ) {
                
                esgnLabel = esgn.getEsgnLabel();
                esgnVille = esgn.getEsgnVille();

                esgnVilleShort = "(";
                if (esgnVille.length() > 4) {
                    esgnVilleShort += esgnVille.substring(0, 4) + "." ;
                } else { esgnVilleShort += esgnVille ; }
                esgnVilleShort += ")" ;            
                
                finalStrVilleShort = esgnLabel + esgnVilleShort ;
                finalStrEsgnFullLabel = esgnLabel + " : " + esgnVille ;     
            
            } else {            
                finalStrVilleShort = "" ;
                finalStrEsgnFullLabel = "" ;     
            }
            
            // Chaque liste reconstruite pour display avec 
                // le label de l'enseigne au lieu de l'ID
                // et le nbre de produit (dans le constructeur)
            returned.add(new ListeFrontend(liste, finalStrVilleShort));
            
            // On en profite pour remplir la liste des enseignes
            if (!listEsgnLabels.contains(finalStrEsgnFullLabel)) {
                listEsgnLabels.add(finalStrEsgnFullLabel);
            }
                        
        }
        
        return  returned;
    }
        
}
