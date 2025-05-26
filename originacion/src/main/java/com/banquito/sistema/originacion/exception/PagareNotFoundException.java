package com.banquito.sistema.originacion.exception;

public class PagareNotFoundException extends RuntimeException {
    private final Integer errorCode;

    public PagareNotFoundException(Long id) {
        super("Pagare not found with id " + id);
        this.errorCode = 1004;
    }

    @Override
    public String getMessage() {
        return "Error code: " + this.errorCode + ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
} 