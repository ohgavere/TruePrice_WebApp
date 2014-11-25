/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FilenameUtils;
import org.dmb.trueprice.entities.Membre;
import static org.dmb.trueprice.filter.RestrictionFilter.ATT_SESSION_USER;
import org.dmb.trueprice.utils.internal.FileUtils;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guitch
 */
@WebServlet(name = "Download_servlet", urlPatterns = {"/xml/*"})
public class Download_servlet extends HttpServlet {

            
    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10 ko
    private int TAILLE_TAMPON = 1024;


    
    private String xmlDataFolder = "xmlDataFolder";   
    
    private String xmlMemberDataFolder = "xmlMemberDataFolder";   
    private String xmlMemberDataPath = "xmlMemberDataPath";   
    
    private String xmlPublicDataFolder = "xmlPublicDataFolder";   
    private String xmlPublicDataPath = "xmlPublicDataPath";   
    
    private String xmlRecoverDataFolder = "xmlRecoverDataFolder";   
    
    
    

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); //To change body of generated methods, choose Tools | Templates.
        
        
        this.xmlDataFolder = InitContextListener.getEnvEntryValue(xmlDataFolder);
        
        this.xmlMemberDataFolder = InitContextListener.getEnvEntryValue(xmlMemberDataFolder);
        this.xmlMemberDataPath = InitContextListener.getEnvEntryValue(xmlMemberDataPath);
        
        this.xmlPublicDataFolder = InitContextListener.getEnvEntryValue(xmlPublicDataFolder);
        this.xmlPublicDataPath = InitContextListener.getEnvEntryValue(xmlPublicDataPath);
        
        this.xmlRecoverDataFolder = InitContextListener.getEnvEntryValue(xmlRecoverDataFolder);

        
    }
    
    
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /* Récupération du chemin du fichier demandé au sein de l'URL de la requête */
        String fichierRequis = request.getPathInfo();
        
        /* Vérifie qu'un fichier a bien été fourni */
        if ( fichierRequis == null || "/".equals( fichierRequis ) ) {
            /* Si non, alors on envoie une erreur 404, qui signifie que la ressource demandée n'existe pas */
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        InitContextListener.getLogger(this.getClass())
            .info("File asked [" + fichierRequis + "]");      
        
        String parentFolder = xmlDataFolder ;
        
        if (fichierRequis.startsWith(xmlMemberDataPath)) { 
        
            HttpSession session = request.getSession(false);
            
            Membre mb = (Membre) session.getAttribute(ATT_SESSION_USER);
            
            parentFolder = xmlMemberDataFolder + File.separator + mb.getMbMail() ;

        }
        else if (fichierRequis.startsWith(xmlPublicDataPath)) { parentFolder = xmlPublicDataFolder ; }
        
        InitContextListener.getLogger(this.getClass())
            .info("Parent folder : [" + parentFolder + "]");      
        
        /* Décode le nom de fichier récupéré, susceptible de contenir des espaces et autres caractères spéciaux, et prépare l'objet File */
        fichierRequis = URLDecoder.decode( fichierRequis, "UTF-8");
                
        InitContextListener.getLogger(this.getClass())
            .info("Decoded File asked : [" + fichierRequis + "]");  
        
        /*  Ne retenir que le nom du fichier + extension */
        fichierRequis = FilenameUtils.getName(fichierRequis);
                                
        InitContextListener.getLogger(this.getClass())
            .info("Finally, file name is : [" + fichierRequis + "]");  
        
        File fichier = new File( parentFolder, fichierRequis );

        /* Vérifie que le fichier existe bien */
        if ( !fichier.exists() ) {            
        
        InitContextListener.getLogger(this.getClass())
            .info("Can't find : [" + fichierRequis + "] in folder [" + parentFolder + "]");             
            
            /* Si non, alors on envoie une erreur 404, qui signifie que la ressource demandée n'existe pas */
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }    
        
        /* Récupère le type du fichier */
        String type = getServletContext().getMimeType( fichier.getName() );

        /* Si le type de fichier est inconnu, alors on initialise un type par défaut */
        if ( type == null ) {
            type = "application/octet-stream";
        } else {
            InitContextListener.getLogger(this.getClass())
                .info(" Found MimeType for asked ressource [" + type +"]");
        }
        

        /* Initialise la réponse HTTP */
        response.reset();
        response.setBufferSize( DEFAULT_BUFFER_SIZE );
        response.setContentType( type );
        response.setHeader( "Content-Length", String.valueOf( fichier.length() ) );
        response.setHeader( "Content-Disposition", "attachment; filename=\"" + fichier.getName() + "\"" );  


        /* Prépare les flux */
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        try {
            /* Ouvre les flux */
            entree = new BufferedInputStream( new FileInputStream( fichier ), TAILLE_TAMPON );
            sortie = new BufferedOutputStream( response.getOutputStream(), TAILLE_TAMPON );

            /* ... */
            /* Lit le fichier et écrit son contenu dans la réponse HTTP */
            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur;
            while ( ( longueur= entree.read( tampon ) ) > 0 ) {
                sortie.write( tampon, 0, longueur );
            }
            
            
        } finally {
            try {
                sortie.close();
            } catch ( IOException ignore ) {
            }
            try {
                entree.close();
            } catch ( IOException ignore ) {
            }
        }
        
        InitContextListener.getLogger(this.getClass()).info("File sent [" + fichierRequis + "]");
        
    }

    
    
}
