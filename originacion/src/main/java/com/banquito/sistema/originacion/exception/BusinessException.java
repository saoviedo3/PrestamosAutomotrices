package com.banquito.sistema.originacion.exception;

public class BusinessException extends RuntimeException {

    private final String message;
    private final String operation;

    public BusinessException(String message, String operation) {
        super();
        this.message = message;
        this.operation = operation;
    }

    @Override
    public String getMessage() {
        return "Error en la operación: " + this.operation + ". " + this.message;
    }
} 