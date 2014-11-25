/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.utils.external;


import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import org.dmb.trueprice.entities.Password;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guiitch
 */
public class PasswordFactory_old extends PasswordHash_old {
    
     private static final Logger log 
            = InitContextListener.getLogger( PasswordFactory_old.class) ;

     
     
public Password createPassword (String humanValue) {
         
//             try
//             {
//                 // Print out 10 hashes
//                 for(int i = 0; i < 10; i++)
////                System.out.println(PasswordHash.createHash("p\r\nassw0Rd!"));
//                     System.out.println(createHash(humanValue));
//                 
//                 // Test password validation
//                 boolean failure = false;
//                 System.out.println("Running tests...");
//                 for(int i = 0; i < 100; i++)
//                 {
//                     String password = ""+i;
//                     String hash = createHash(password);
//                     String secondHash = createHash(password);
//                     if(hash.equals(secondHash)) {
//                         System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
//                         failure = true;
//                     }
//                     String wrongPassword = ""+(i+1);
//                     if(validatePassword(wrongPassword, hash)) {
//                         System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
//                         failure = true;
//                     }
//                     if(!validatePassword(password, hash)) {
//                         System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
//                         failure = true;
//                     }
//                 }
//                 if(failure)
//                     System.out.println("TESTS FAILED!");
//                 else
//                     System.out.println("TESTS PASSED!");
//             }
//             catch(Exception ex)
//             {
//                 System.out.println("ERROR: " + ex);
//             }

    
        try {
            
            
            log.info("First Hash >" + createHash(humanValue));
             
            String hash = createHash(humanValue);
             
            log.info("second hash >"+ hash);
             
             
            hash = createHash(humanValue);
             
            log.info("3rd hash >"+ hash);
             
             
            hash = createHash(humanValue);
             
            log.info("4th hash >"+ hash);
             
             return new Password(hash);
             
         } catch (NoSuchAlgorithmException ex) {
             log.warn(" No message affected to this exception. Cause : " + ex.getMessage());
         } catch (InvalidKeySpecException ex) {
             log.warn(" No message affected to this exception. Cause : " + ex.getMessage());
         }
 
 return null;
 
}


public boolean validPassword (String password, String hashValue) throws InvalidKeySpecException, NoSuchAlgorithmException {
    
    if (validatePassword(password, createHash(password))) {
        return true;
    } else {
        return false;
    }

}
     
     
     
//    public Password createPassword(String humanValue){
//    
//        try {
//            
////          String plaintext = "Vive les cours Java";
//            log.info("input value > " + humanValue);
//          
//            
////          char[] password = {'t', 'h', 'e', ' ', 'p', 'a', 's', 's'};
//            
//            byte[] password = StringUtils.getBytesUsAscii(humanValue);
//            log.info("input bytes > " + humanValue);
//            
//            // le salt est choisis aléatoirement et le nombre d'itérations par défaut vaut 10
//            PBEKeySpec keySpec = new PBEKeySpec(StringUtils.newStringUtf8(password).toCharArray());
//
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
//            SecretKey secretKey = keyFactory.generateSecret(keySpec);
//            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//            
//            log.info("Try secretKey toString [" + secretKey.toString() + "]");
//            
////            byte[] input = plaintext.getBytes();
////            byte[] ciphertext = cipher.doFinal(input);
//            
//            
//            byte[] ciphertext = cipher.doFinal(password);
//            log.info("ciphertext final [" + new BigInteger( ciphertext) + "]" );
//            // Password encrypted
//            
//            AlgorithmParameters params = cipher.getParameters();
//              
//            String algo = params.getAlgorithm();
//            String paramsStr = params.toString();            
//            String key = secretKey.toString();
//            String keyAlgo = secretKey.getAlgorithm();
//            String keyFormat = secretKey.getFormat();
//            String keyEncoded = StringUtils.newStringUsAscii(secretKey.getEncoded());
//            
//            log.info("\n\tPassword factory available content : "
//                    + "Params > algo =[" + algo + "]"
//                    + "\tparamsToString=[" + paramsStr + "]"
//                    + "\nSecretKey > key =[" + key + "]"
//                    + "\tkeyAlgo =[" + keyAlgo + "]"
//                    + "\tkeyFormat =[" + keyFormat + "]"
//                    + "\tkeyEncoded =[" + keyEncoded + "]"
//                    + "\n\n"
//            );
//            
//            log.info("Need to record this : " 
//                    + "KeySpec iter count > ["+ keySpec.getIterationCount() + "]"
//                    + "KeySpec salt > [" + keySpec.getSalt() + "]"
//                    );
//            
//            // récupère les paramètres, comme le salt et le nombre d'itérations
//            cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
//            byte[] plain = cipher.doFinal(ciphertext);
//            String plaintext2 = new String(plain);
//            log.info("plaintext2 =  " + plaintext2);
//            
//            
//          
//        
//            return new Password(plaintext2);
//            
//        }
//          
//        
//        catch (InvalidAlgorithmParameterException e) {
//            log.error(e.getMessage());
//          } catch (InvalidKeyException e) {
//              log.error(e.getMessage());
//         } catch (NoSuchAlgorithmException e) {
//             log.error(e.getMessage());
//         } catch (InvalidKeySpecException e) {
//             log.error(e.getMessage());
//         } catch (BadPaddingException e) {
//             log.error(e.getMessage());
//         } catch (IllegalBlockSizeException e) {
//             log.error(e.getMessage());
//         } catch (NoSuchPaddingException e) {
//             log.error(e.getMessage());
//         }
//         return null;
//        
//    }
//    
//    
//    public Password decryptPassword(String humanValue){
//    
//        try {
//            
////          String plaintext = "Vive les cours Java";
//            log.info("input value > " + humanValue);
//          
//            
////          char[] password = {'t', 'h', 'e', ' ', 'p', 'a', 's', 's'};
//            
//            byte[] password = StringUtils.getBytesUsAscii(humanValue);
//            log.info("input bytes > " + humanValue);
//            
//            // le salt est choisis aléatoirement et le nombre d'itérations par défaut vaut 10
//            PBEKeySpec keySpec = new PBEKeySpec(StringUtils.newStringUtf8(password).toCharArray());
//
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
//            SecretKey secretKey = keyFactory.generateSecret(keySpec);
//            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
//            
////            byte[] ciphertext = cipher.doFinal(password); 
//            
//            AlgorithmParameters params = cipher.getParameters();
//            
//            cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
////            byte[] plain = cipher.doFinal(ciphertext);
//            byte[] plain = cipher.doFinal(password);
//            String plaintext2 = new String(plain);
//            log.info("plaintext2 =  " + plaintext2);
//            
//            log.info("Try secretKey toString [" + secretKey.toString() + "]");
//            
////            byte[] input = plaintext.getBytes();
////            byte[] ciphertext = cipher.doFinal(input);
//            
//            
//            
//            log.info("ciphertext final [" + new BigInteger( ciphertext) + "]" );
//            // Password encrypted
//            
//           
//              
//            String algo = params.getAlgorithm();
//            String paramsStr = params.toString();            
//            String key = secretKey.toString();
//            String keyAlgo = secretKey.getAlgorithm();
//            String keyFormat = secretKey.getFormat();
//            String keyEncoded = StringUtils.newStringUsAscii(secretKey.getEncoded());
//            
//            log.info("\n\tPassword factory available content : "
//                    + "Params > algo =[" + algo + "]"
//                    + "\tparamsToString=[" + paramsStr + "]"
//                    + "\nSecretKey > key =[" + key + "]"
//                    + "\tkeyAlgo =[" + keyAlgo + "]"
//                    + "\tkeyFormat =[" + keyFormat + "]"
//                    + "\tkeyEncoded =[" + keyEncoded + "]"
//                    + "\n\n"
//            );
//            
//            log.info("Need to record this : " 
//                    + "KeySpec iter count > ["+ keySpec.getIterationCount() + "]"
//                    + "KeySpec salt > [" + keySpec.getSalt() + "]"
//                    );
//            
//            // récupère les paramètres, comme le salt et le nombre d'itérations
//
//            
//            
//          
//        
//            return new Password(plaintext2);
//            
//        }
//          
//        
//        catch (InvalidAlgorithmParameterException e) {
//            log.error(e.getMessage());
//          } catch (InvalidKeyException e) {
//              log.error(e.getMessage());
//         } catch (NoSuchAlgorithmException e) {
//             log.error(e.getMessage());
//         } catch (InvalidKeySpecException e) {
//             log.error(e.getMessage());
//         } catch (BadPaddingException e) {
//             log.error(e.getMessage());
//         } catch (IllegalBlockSizeException e) {
//             log.error(e.getMessage());
//         } catch (NoSuchPaddingException e) {
//             log.error(e.getMessage());
//         }
//         return null;
//        
//    }
    
}
