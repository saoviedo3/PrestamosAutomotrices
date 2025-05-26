package com.banquito.sistema.originacion.exception;

public class TipoDocumentoNotFoundException extends RuntimeException {
    private final int errorCode;

    public TipoDocumentoNotFoundException(Long id) {
        super("TipoDocumento no encontrado con ID: " + id);
        this.errorCode = 1007;
    }

    public TipoDocumentoNotFoundException(String identificador) {
        super("TipoDocumento no encontrado con identificador: " + identificador);
        this.errorCode = 1007;
    }

    @Override
    public String getMessage() {
        return String.format("[%d] %s", errorCode, super.getMessage());
    }

    public int getErrorCode() {
        return errorCode;
    }
} 