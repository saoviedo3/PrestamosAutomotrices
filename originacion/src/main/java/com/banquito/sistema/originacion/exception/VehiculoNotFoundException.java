package com.banquito.sistema.originacion.exception;

public class VehiculoNotFoundException extends RuntimeException {
    private final Integer errorCode;

    public VehiculoNotFoundException(Long id) {
        super("Vehiculo not found with id " + id);
        this.errorCode = 1001;
    }

    @Override
    public String getMessage() {
        return "Error code: " + this.errorCode + ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}