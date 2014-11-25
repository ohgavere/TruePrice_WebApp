/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.utils.internal;

import java.util.logging.Logger;
import org.apache.openejb.util.LogCategory;
import org.joda.time.format.ISODateTimeFormat;


/**
 *
 * @author Guiitch
 */
public class MyLogger {

    private Logger log = null;
    private org.apache.openejb.util.Logger logEJB = null;
            
    
    private String myLogHead = "" ;
    private String myLogID = "" ;
        
    private Logger logs = Logger.getLogger(MyLogger.class.getName());

    public MyLogger(String myLogIdentifier) {
        this.myLogID = "[T$p." + myLogIdentifier + "|" ;
    }

    
    
    public org.apache.openejb.util.Logger getLoggerEJB() {
        return logEJB = org.apache.openejb.util.Logger.getInstance (
                            LogCategory.OPENEJB,
                        "[T$p." +   this.getClass().getSimpleName()
                        +   "]"
        ) ;
    }

    public Logger getLogger() {
        return logs;
    }

    public String getMyLogHead() {
        return myLogHead;
    }

    /*
    * Send null to reset Header
    */
    public void setMyLogHead(String myLogHead) {
        if (myLogHead == null) {
            this.myLogHead = null;
        } else {
            this.myLogHead = myLogHead;
        }
    }

    public String getMyLogID() {
        return myLogID;
    }

    public void setMyLogID(String myLogID) {
        this.myLogID = myLogID;
    }
    
    
    
        
   public String getLogHead (String whatever)  {
//       try { 
            if (myLogHead == null | whatever == null) {    
            } else {
                myLogHead = "[T$p|" +
                        getMyLogID()
                        +  "||" + whatever + "]";
        }
//        } catch (Exception ex) {
//            Logger.getLogger("").log(Level.WARNING, "Cannot personnalize your log header.", ex);
//        } finally {     return myLogHead;   }
        return myLogHead;
   }
   public String getLogTHead () {
//       try {
           /*RestrictionFilter.class.getSimpleName()*/
           if (myLogHead == null ) {
                myLogHead = getMyLogID() 
                   + ISODateTimeFormat.hourMinuteSecond().print(System.currentTimeMillis())
                   + "]"
                   ;
                return myLogHead;
           } else {
               return getLogHead(Long.toString(System.currentTimeMillis()));
           }
//        } catch (Exception ex) {
//            Logger.getLogger(this.getClass().getSimpleName()).log(Level.WARNING, "Cannot set logger time header.", ex);
//            return "" ;
        }
        
   
    
}
