package com.banquito.sistema.exception;

public class InvalidDataException extends RuntimeException {
    private final String entityName;
    private final String message;

    public InvalidDataException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
        this.message = message;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public String getMessage() {
        return String.format("Error en %s: %s", entityName, message);
    }
}