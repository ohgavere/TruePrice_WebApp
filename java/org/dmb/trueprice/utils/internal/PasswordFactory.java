/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.utils.internal;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.apache.log4j.Logger;
import org.dmb.trueprice.entities.Password;

/**
 *
 * @author Guiitch
 */
public class PasswordFactory extends PasswordHash {
    
     private static final Logger log 
            = InitContextListener.getLogger( PasswordFactory.class) ;

     
     
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
            
            
//            log.debug("First Hash >" + createHash(humanValue));
             
            String hash = createHash(humanValue);
             
//            log.debug("second hash >"+ hash);
             
             
//            hash = createHash(humanValue);
             
//            log.debug("3rd hash >"+ hash);
             
             
//            hash = createHash(humanValue);
             
//            log.debug("4th hash >"+ hash);
             
             return new Password(hash);
             
         } catch (NoSuchAlgorithmException ex) {
             log.warn(" No message affected to this exception. Cause : " + ex.getMessage());
         } catch (InvalidKeySpecException ex) {
             log.warn(" No message affected to this exception. Cause : " + ex.getMessage());
         }
 
 return null;
 
}


public boolean validPassword (String password, String hashValue) throws InvalidKeySpecException, NoSuchAlgorithmException {
    
    if (validatePassword(password, hashValue)) {
        log.debug("Return TRUE");
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
//            log.debug("input value > " + humanValue);
//          
//            
////          char[] password = {'t', 'h', 'e', ' ', 'p', 'a', 's', 's'};
//            
//            byte[] password = StringUtils.getBytesUsAscii(humanValue);
//            log.debug("input bytes > " + humanValue);
//            
//            // le salt est choisis aléatoirement et le nombre d'itérations par défaut vaut 10
//            PBEKeySpec keySpec = new PBEKeySpec(StringUtils.newStringUtf8(password).toCharArray());
//
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
//            SecretKey secretKey = keyFactory.generateSecret(keySpec);
//            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//            
//            log.debug("Try secretKey toString [" + secretKey.toString() + "]");
//            
////            byte[] input = plaintext.getBytes();
////            byte[] ciphertext = cipher.doFinal(input);
//            
//            
//            byte[] ciphertext = cipher.doFinal(password);
//            log.debug("ciphertext final [" + new BigInteger( ciphertext) + "]" );
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
//            log.debug("\n\tPassword factory available content : "
//                    + "Params > algo =[" + algo + "]"
//                    + "\tparamsToString=[" + paramsStr + "]"
//                    + "\nSecretKey > key =[" + key + "]"
//                    + "\tkeyAlgo =[" + keyAlgo + "]"
//                    + "\tkeyFormat =[" + keyFormat + "]"
//                    + "\tkeyEncoded =[" + keyEncoded + "]"
//                    + "\n\n"
//            );
//            
//            log.debug("Need to record this : " 
//                    + "KeySpec iter count > ["+ keySpec.getIterationCount() + "]"
//                    + "KeySpec salt > [" + keySpec.getSalt() + "]"
//                    );
//            
//            // récupère les paramètres, comme le salt et le nombre d'itérations
//            cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
//            byte[] plain = cipher.doFinal(ciphertext);
//            String plaintext2 = new String(plain);
//            log.debug("plaintext2 =  " + plaintext2);
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
//            log.debug("input value > " + humanValue);
//          
//            
////          char[] password = {'t', 'h', 'e', ' ', 'p', 'a', 's', 's'};
//            
//            byte[] password = StringUtils.getBytesUsAscii(humanValue);
//            log.debug("input bytes > " + humanValue);
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
//            log.debug("plaintext2 =  " + plaintext2);
//            
//            log.debug("Try secretKey toString [" + secretKey.toString() + "]");
//            
////            byte[] input = plaintext.getBytes();
////            byte[] ciphertext = cipher.doFinal(input);
//            
//            
//            
//            log.debug("ciphertext final [" + new BigInteger( ciphertext) + "]" );
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
//            log.debug("\n\tPassword factory available content : "
//                    + "Params > algo =[" + algo + "]"
//                    + "\tparamsToString=[" + paramsStr + "]"
//                    + "\nSecretKey > key =[" + key + "]"
//                    + "\tkeyAlgo =[" + keyAlgo + "]"
//                    + "\tkeyFormat =[" + keyFormat + "]"
//                    + "\tkeyEncoded =[" + keyEncoded + "]"
//                    + "\n\n"
//            );
//            
//            log.debug("Need to record this : " 
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
