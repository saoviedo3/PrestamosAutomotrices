package com.banquito.sistema.originacion.exception;

public class ClienteProspectoNotFoundException extends RuntimeException {

    private final Integer errorCode;

    public ClienteProspectoNotFoundException(Long id) {
        super("Cliente prospecto con ID " + id + " no encontrado");
        this.errorCode = 1005;
    }

    public ClienteProspectoNotFoundException(String message) {
        super(message);
        this.errorCode = 1005;
    }

    @Override
    public String getMessage() {
        return "Error code: " + this.errorCode + ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
} 