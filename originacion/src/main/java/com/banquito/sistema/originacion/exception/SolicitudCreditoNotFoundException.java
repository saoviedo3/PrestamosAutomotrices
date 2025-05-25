package com.banquito.sistema.originacion.exception;

public class SolicitudCreditoNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    private final String identificador;
    private final String tipoIdentificador;

    public SolicitudCreditoNotFoundException(String identificador, String tipoIdentificador) {
        super();
        this.identificador = identificador;
        this.tipoIdentificador = tipoIdentificador;
    }

    @Override
    public String getMessage() {
        return "No se encontró la solicitud de crédito con " + tipoIdentificador + ": " + identificador;
    }
} 