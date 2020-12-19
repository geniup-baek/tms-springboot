package com.example.tms.error;

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 2405125384205060039L;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }
   
}
