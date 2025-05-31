package com.banquito.sistema.originacion.exception;

public class IdentificadorVehiculoNotFoundException extends RuntimeException {

    private final Integer errorCode = 404;

    public IdentificadorVehiculoNotFoundException(Long id) {
        super("IdentificadorVehiculo no encontrado con id=" + id);
    }

    @Override
    public String getMessage() {
        return "Error code: " + errorCode + ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
