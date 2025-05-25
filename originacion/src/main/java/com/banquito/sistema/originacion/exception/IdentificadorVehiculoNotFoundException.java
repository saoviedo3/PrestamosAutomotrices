package com.banquito.sistema.originacion.exception;

public class IdentificadorVehiculoNotFoundException extends RuntimeException {
    private final Integer errorCode;

    public IdentificadorVehiculoNotFoundException(Integer id) {
        super("IdentificadorVehiculo not found with id " + id);
        this.errorCode = 1002;
    }

    @Override
    public String getMessage() {
        return "Error code: " + this.errorCode + ", message: " + super.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
