package com.banquito.sistema.originacion.exception;

public class ClienteProspectoNotFoundException extends RuntimeException {
    private final String identificador;
    private final String tipoIdentificador;

    public ClienteProspectoNotFoundException(String identificador, String tipoIdentificador) {
        super(String.format("Cliente prospecto no encontrado con %s: %s", tipoIdentificador, identificador));
        this.identificador = identificador;
        this.tipoIdentificador = tipoIdentificador;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getTipoIdentificador() {
        return tipoIdentificador;
    }
} 