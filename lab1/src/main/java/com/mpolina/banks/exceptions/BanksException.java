package com.mpolina.banks.exceptions;

/**
 * Исключение, выбрасываемое при ошибке на уровне архитектуры "Банков"
 */
public class BanksException extends RuntimeException{
    public BanksException() {
    }

    public BanksException(String errorMessage) {

        super(errorMessage);
    }

    public BanksException(Throwable cause)
    {
        super(cause);
    }

    public BanksException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
