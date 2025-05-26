package com.banquito.sistema.originacion.exception;

public class DocumentoAdjuntoNotFoundException extends RuntimeException {
    private final int errorCode;

    public DocumentoAdjuntoNotFoundException(Long id) {
        super("DocumentoAdjunto no encontrado con ID: " + id);
        this.errorCode = 1006;
    }

    public DocumentoAdjuntoNotFoundException(String identificador) {
        super("DocumentoAdjunto no encontrado con identificador: " + identificador);
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