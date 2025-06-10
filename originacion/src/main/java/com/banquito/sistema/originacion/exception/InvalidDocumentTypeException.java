package com.banquito.sistema.originacion.exception;

public class InvalidDocumentTypeException extends RuntimeException {

    private final Integer errorCode;

    public InvalidDocumentTypeException(String message) {
        super(message);
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