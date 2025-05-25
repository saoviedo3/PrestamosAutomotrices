package com.banquito.sistema.auditoria.exception;

public class AuditoriaNotFoundException extends RuntimeException {
    private final Integer errorCode;

    public AuditoriaNotFoundException(Long id) {
        super("Auditoria not found with id " + id);
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