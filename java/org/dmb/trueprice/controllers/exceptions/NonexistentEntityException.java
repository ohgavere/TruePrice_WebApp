package org.dmb.trueprice.controllers.exceptions;

import org.dmb.trueprice.controllers.exceptions.*;

public class NonexistentEntityException extends Exception {
    public NonexistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public NonexistentEntityException(String message) {
        super(message);
    }
}
