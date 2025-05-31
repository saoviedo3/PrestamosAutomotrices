package com.banquito.sistema.exception;

public class CreateEntityException extends RuntimeException {

    private final Integer errorCode = 1001; // Código para “error al crear entidad”
    private final String entityName;

    public CreateEntityException(String entityName, String message) {
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
