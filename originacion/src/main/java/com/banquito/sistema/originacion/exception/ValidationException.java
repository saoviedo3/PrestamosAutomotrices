package com.banquito.sistema.originacion.exception;

public class ValidationException extends RuntimeException {

    private final String field;
    private final String value;

    public ValidationException(String field, String value) {
        super();
        this.field = field;
        this.value = value;
    }

    @Override
    public String getMessage() {
        return "Error de validación en el campo: " + this.field + " con el valor: " + this.value;
    }
} 