/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.utils.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;
import org.dmb.trueprice.objects.RecoveredMember;


/**
 *
 * @author Guitch
 */
public abstract class JaxbTraductor {
    
    private static final Logger log 
    = InitContextListener.getLogger( JaxbTraductor.class) ;
    
    public static void marshall (RecoveredMember mb, String fileFullPath)  {
    
        
        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(RecoveredMember.class);
            
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();            
            
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            
            marshaller.marshal(mb,new File(fileFullPath)); //NOI18N
            
            OutputStream os = new FileOutputStream(new File(fileFullPath));

            marshaller.marshal(mb, os);

            os.flush();
            os.close();

        } catch (JAXBException ex) {
            log.error("Could not write XML file because > " + ex.getMessage());
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            log.error("Could not find XML file because > " + ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            log.error("Could not write XML file because > " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public static RecoveredMember unmarshall (String fileFullPath) throws Exception {
    
        
        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(RecoveredMember.class);
            
            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            
            InputStream is = new FileInputStream(new File(fileFullPath));
            
            Object newInstance = unmarshaller.unmarshal(is); //NOI18N
            
            return (RecoveredMember)  newInstance;
            
        } catch (JAXBException ex) {
//            log.error("Could not read XML file because > " + ex.getMessage());
//            ex.printStackTrace();
            throw new Exception("File unparsable.");
        } catch (FileNotFoundException ex) {
//            log.error("Could not find XML file because > " + ex.getMessage());
//            ex.printStackTrace();
            throw new Exception("File not found.");
        } catch ( NullPointerException ex) {
//            log.error("Could not write XML file because > " + ex.getMessage());
//            ex.printStackTrace();
            throw new Exception("IO Error.");
        }
        
//        return null ;
    }
    
}
