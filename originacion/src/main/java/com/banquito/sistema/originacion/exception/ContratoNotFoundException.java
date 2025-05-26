package com.banquito.sistema.originacion.exception;

public class ContratoNotFoundException extends RuntimeException {
    private final Integer errorCode;

    public ContratoNotFoundException(Long id) {
        super("Contrato not found with id " + id);
        this.errorCode = 1005;
    }

    public ContratoNotFoundException(String identificador) {
        super("Contrato not found with " + identificador);
        this.errorCode = 1005;
    }

    @Override
    public String getMessage() {
        return "Error code: " + this.errorCode + ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
} 