package org.dmb.trueprice.controllers.exceptions;

import org.dmb.trueprice.controllers.exceptions.*;

public class PreexistingEntityException extends Exception {
    public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public PreexistingEntityException(String message) {
        super(message);
    }
}
