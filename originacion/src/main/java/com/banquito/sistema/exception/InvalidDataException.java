package com.banquito.sistema.exception;

public class InvalidDataException extends RuntimeException {
    private final Integer errorCode = 1005; // código único para “datos inválidos”
    private final String entityName;

    public InvalidDataException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return "Error code: " + errorCode +
                ", entity: " + entityName +
                ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getEntityName() {
        return entityName;
    }
}