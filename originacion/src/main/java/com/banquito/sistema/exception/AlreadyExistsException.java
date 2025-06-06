package com.banquito.sistema.exception;

public class AlreadyExistsException extends RuntimeException {

    private final Integer errorCode = 1004; // Código para “entidad ya existe”
    private final String entityName;

    public AlreadyExistsException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return "Error code: " + errorCode
             + ", entity: " + entityName
             + ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getEntityName() {
        return entityName;
    }
}
