package com.banquito.sistema.originacion.exception;

public class ConcesionarioNotFoundException extends RuntimeException {
        private final Integer errorCode;

    public ConcesionarioNotFoundException(Long id) {
        super("Concesionario not found with id " + id);
        this.errorCode = 1002;
    }

    @Override
    public String getMessage() {
        return "Error code: " + this.errorCode + ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
