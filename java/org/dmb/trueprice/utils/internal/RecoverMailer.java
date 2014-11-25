/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.utils.internal;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Guitch
 */
public abstract class RecoverMailer {
    
    private static final String FORM_PARAM_MAIL_RECOVERED = "recoveredMail"; // Champ venant de l'email
    private static final String MAIL_SECURE_LINK = "securedLink"; // Champ venant de l'email
    private static final String MAIL_CHECKSUM = "checkSum"; // Champ venant de l'email
        
//    private final static String FROM = "demarbre.guillaume@gmail.com"; //From email address
    private final static String FROM = "trueprice.officiel@gmail.com"; //From email address
    private static String TO =  "";//"demarbre.guillaume@gmail.com"; // To email Address
    private final static String SUBJECT = "My first Email sent using Java Mail API =) So p^roud ... :P !";
    private final static String CONTENT = "Hello, This is email is the test email sent from Kodehelp.com's Java Mail API example."
            + "And bla bla bla bla  .... \n\n"
            + "And here is the link you should clic to recover your True|$|Price account by setting a new password.\n\n"
            ;

    private static String link = "";
 
    private final static String SMTP_HOST = "smtp.gmail.com";
    private  final static String SMTP_PORT = "587" ; // "465"; // because of google needs (see above) https://support.google.com/mail/answer/13287?hl=en
    private final static String EMAIL_PROTOCOL = "smtp";
        
    public static void sendMail(String encrytedValue, String targetMail, int checksum) {
        
        link = "<a href=\"http://localhost:8080/TruePrice/recover?"
                + FORM_PARAM_MAIL_RECOVERED + "=" + targetMail + "&"
                + MAIL_SECURE_LINK + "=" + encrytedValue + "&"
                + MAIL_CHECKSUM + "=" + String.valueOf(checksum)
                + "\"><u>Reinitialiser mon mot de passe</u></a>" ;

 
        //Properties is used to store the email configurations such as mail protocol, SMTP host, SMTP port and many others
        Properties prop = new Properties();
        prop.put("mail.smtp.host", SMTP_HOST);
        prop.put("mail.smtp.port", SMTP_PORT);
        
        prop.put("mail.smtp.auth", "true");

//        prop.put("mail.smtp.ssl.enable","true");
        prop.put("mail.debug", "true");
        prop.put("mail.transport.protocol", EMAIL_PROTOCOL);
        
                prop.put("mail.smtp.user", FROM);
//        prop.put("mail.smtp.password", "NTherchies0127050");
//                prop.put("mail.user", FROM);
//        prop.put("mail.password", "NTherchies0127050");
        prop.put("mail.smtp.password", "ohgavereA2B");
        
        
        /*
            com.sun.mail.smtp.SMTPSendFailedException: 530 5.7.0 Must issue a STARTTLS command first. ed6sm6117146wib.9 - gsmtp 

            Elle est due au fait que votre serveur SMTP utilise une connexion sécurisée. 
            C'est notamment le cas de smtp.gmail.com. 
            Le problème se résout très simplement en ajoutant cette propriété :        
        */
        prop.put("mail.smtp.starttls.enable", "true");
 
//        PasswordAuthentication auth = new PasswordAuthentication(FROM, "NTherchies0127050") ;
//        authen = new Authenticator( );
        
        
//        Session session = Session.getDefaultInstance(prop, null);
        
            Session session = Session.getInstance(prop,
		  new javax.mail.Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(FROM, "NTherchies0127050");
//				return new PasswordAuthentication(FROM, "suxvtvtvkphgykfx");
				return new PasswordAuthentication(FROM, "ohgavereA2B");
			}
		  });        
        
        Transport tp;
        
        try {
            tp = session.getTransport(EMAIL_PROTOCOL);

            TO = targetMail;

 
            try {
                InternetAddress fromAddress = new InternetAddress(FROM);
    //            InternetAddress toAddress = new InternetAddress(TO);
                Address toAddress = new InternetAddress(TO);


                Message message = new MimeMessage(session);
                message.setFrom(fromAddress);
//                message.setRecipient(Message.RecipientType.TO, toAddress);
                
                message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(TO));
                
                message.setSubject(SUBJECT);
                message.setText(CONTENT + "\n\n" + link);

                tp.connect(SMTP_HOST,  FROM, "ohgavereA2B" );

//                Transport.send(message, "demo@kodehelp .com", "password");
                tp.sendMessage(message, new Address[] {toAddress});
                
//                tp.send(message);
                
                tp.close();

            } catch(Exception ex){
                ex.printStackTrace();
            
            } finally { 
                try { 
                    if (tp != null) { 
                        tp.close(); 
                    } 
                } catch (MessagingException e) { 
                    e.printStackTrace(); 
                }
            } 
            
            
            
        } catch (NoSuchProviderException ex) {
            ex.printStackTrace();
        }
        

        
    }
    
    
    
}
