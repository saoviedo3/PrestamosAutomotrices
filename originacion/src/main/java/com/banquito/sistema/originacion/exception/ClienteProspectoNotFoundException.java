package com.banquito.sistema.originacion.exception;

public class ClienteProspectoNotFoundException extends RuntimeException {
    private final Integer errorCode;

    public ClienteProspectoNotFoundException(Long id) {
        super("ClienteProspecto not found with id " + id);
        this.errorCode = 1003;
    }

    public ClienteProspectoNotFoundException(String identificador) {
        super("ClienteProspecto not found with " + identificador);
        this.errorCode = 1003;
    }

    @Override
    public String getMessage() {
        return "Error code: " + this.errorCode + ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
} 