package com.banquito.sistema.exception;

public class DeleteEntityException extends RuntimeException {

    private final Integer errorCode = 1005; // código para “error al eliminar entidad”
    private final String entityName;

    public DeleteEntityException(String entityName, String message) {
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
