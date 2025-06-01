package com.banquito.sistema.originacion.exception;

public class VehiculoNotFoundException extends RuntimeException {

    private final Integer errorCode = 404;

    public VehiculoNotFoundException(Long id) {
        super("Vehiculo no encontrado con id=" + id);
    }

    @Override
    public String getMessage() {
        return "Error code: " + this.errorCode + ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
