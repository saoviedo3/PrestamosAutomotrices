package com.banquito.sistema.exception;

import com.banquito.sistema.originacion.exception.IdentificadorVehiculoNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(IdentificadorVehiculoNotFoundException.class)
    public ResponseEntity<Map<String, String>> manejarNotFound(IdentificadorVehiculoNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Recurso no encontrado");
        error.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler({AlreadyExistsException.class, InvalidDataException.class})
    public ResponseEntity<Map<String, String>> manejarBadRequest(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error en la solicitud");
        error.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CreateEntityException.class)
    public ResponseEntity<Map<String, String>> manejarCreateError(CreateEntityException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error al crear el recurso");
        error.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarErroresGenerales(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno");
        error.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
