package com.banquito.sistema.originacion.service.exception;

public class CreditoException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String data;
    private final String entity;

    public CreditoException(String message) {
        super(message);
        this.data = null;
        this.entity = null;
    }

    public CreditoException(String data, String entity) {
        super();
        this.data = data;
        this.entity = entity;
    }

    public CreditoException(String message, Throwable cause) {
        super(message, cause);
        this.data = null;
        this.entity = null;
    }

    @Override
    public String getMessage() {
        if (data != null && entity != null) {
            return "No se encontró ninguna coincidencia para: " + this.entity + ", con el dato: " + this.data;
        } else {
            return super.getMessage();
        }
    }
} 