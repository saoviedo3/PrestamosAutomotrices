package com.banquito.sistema.originacion.exception;

public class ContratoNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    private final String identificador;
    private final String tipoIdentificador;

    
    public ContratoNotFoundException(String identificador, String tipoIdentificador) {
        super();
        this.identificador = identificador;
        this.tipoIdentificador = tipoIdentificador;
    }

    @Override
    public String getMessage() {
        return "No se encontró el contrato con " + tipoIdentificador + ": " + identificador;
    }
} 