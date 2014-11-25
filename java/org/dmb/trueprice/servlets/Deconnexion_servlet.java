/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.servlets;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.dmb.trueprice.utils.internal.InitContextListener;

@WebServlet (   urlPatterns = {"/deconnexion"} )
public class Deconnexion_servlet extends HttpServlet {
//    public static final String URL_REDIRECTION = "accesPublic.jsp";


            private static final Logger log 
            = InitContextListener.getLogger( Deconnexion_servlet.class) ;
    
    @Override
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Récupération et destruction de la session en cours */
        HttpSession session = request.getSession();
        
        Enumeration<String> attrs = session.getAttributeNames();
        do {
            String elem = attrs.nextElement() ;
            session.removeAttribute(elem);
             
            log.debug(
                    "[" + log.getClass() + "]"
                    + "Removed attr from Session : " + elem 
            );
            
        } while (attrs.hasMoreElements()) ;
        
        
            
        
        
        session.invalidate();

        /* Redirection vers l'acceuil */
        response.sendRedirect(this.getServletContext().getContextPath()  );
    }
}