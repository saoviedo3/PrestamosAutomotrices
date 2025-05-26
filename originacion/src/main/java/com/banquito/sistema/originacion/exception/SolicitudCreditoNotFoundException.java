package com.banquito.sistema.originacion.exception;

public class SolicitudCreditoNotFoundException extends RuntimeException {
    private final int errorCode;

    public SolicitudCreditoNotFoundException(Long id) {
        super("Solicitud de crédito no encontrada con ID: " + id);
        this.errorCode = 1006;
    }

    public SolicitudCreditoNotFoundException(String identificador) {
        super("Solicitud de crédito no encontrada con identificador: " + identificador);
        this.errorCode = 1006;
    }

    @Override
    public String getMessage() {
        return String.format("[%d] %s", errorCode, super.getMessage());
    }

    public int getErrorCode() {
        return errorCode;
    }
} 