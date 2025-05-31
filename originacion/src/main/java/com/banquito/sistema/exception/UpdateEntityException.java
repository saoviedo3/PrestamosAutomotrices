package com.banquito.sistema.exception;

public class UpdateEntityException extends RuntimeException {

    private final Integer errorCode = 1003; // código para “error al actualizar entidad”
    private final String entityName;

    public UpdateEntityException(String entityName, String message) {
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
