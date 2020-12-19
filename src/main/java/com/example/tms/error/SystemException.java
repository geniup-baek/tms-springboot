package com.example.tms.error;

public class SystemException extends RuntimeException{

    private static final long serialVersionUID = -2974712418841940448L;

    public SystemException() {
        super();
    }
    
    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }    
}
