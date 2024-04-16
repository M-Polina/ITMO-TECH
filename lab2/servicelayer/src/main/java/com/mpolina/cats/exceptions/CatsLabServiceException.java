package com.mpolina.cats.exceptions;

public class CatsLabServiceException extends RuntimeException{
    public CatsLabServiceException() {
    }

    public CatsLabServiceException(String errorMessage) {

        super(errorMessage);
    }

    public CatsLabServiceException(Throwable cause)
    {
        super(cause);
    }

    public CatsLabServiceException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}