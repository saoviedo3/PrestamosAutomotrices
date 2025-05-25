package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.Vehiculo;
import com.banquito.sistema.originacion.repository.VehiculoRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.InvalidDataException;
import com.banquito.sistema.originacion.exception.VehiculoNotFoundException;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> getAllVehiculos() {
        return vehiculoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vehiculo getVehiculoById(Long id) {
        return vehiculoRepository.findById(id)
            .orElseThrow(() -> new VehiculoNotFoundException(id));
    }

    @Transactional
    public Vehiculo createVehiculo(Vehiculo vehiculo) {

        // 1. Validaciones básicas de datos
        if (vehiculo.getMarca() == null || vehiculo.getMarca().isBlank()) {
            throw new InvalidDataException("Vehiculo", "La marca es obligatoria");
        }
        if (vehiculo.getModelo() == null || vehiculo.getModelo().isBlank()) {
            throw new InvalidDataException("Vehiculo", "El modelo es obligatorio");
        }
        if (vehiculo.getAnio() == null || vehiculo.getAnio() < 1886) {
            throw new InvalidDataException("Vehiculo", "El año no es válido");
        }
        if (vehiculo.getValor() == null || vehiculo.getValor() <= 0) {
            throw new InvalidDataException("Vehiculo", "El valor debe ser positivo");
        }

        // // 2. Validar existencia de concesionario (FK)
        // Long concesionarioId = vehiculo.getConcesionario().getId();
        // if (!concesionarioRepository.existsById(concesionarioId)) {
        //     throw new InvalidDataException(
        //         "Vehiculo",
        //         "No existe Concesionario con id " + concesionarioId
        //     );
        // }

        // 3. Evitar duplicados (ejemplo: mismo IdentificadorVehiculo)
        Long identId = vehiculo.getIdentificadorVehiculo().getId();
        if (vehiculoRepository.existsByIdentificadorVehiculoId(identId)) {
            throw new AlreadyExistsException(
                "Vehiculo",
                "Ya existe un Vehiculo con IdentificadorVehiculo id " + identId
            );
        }

        // 4. Persistencia y manejo de errores en BD
        try {
            return vehiculoRepository.save(vehiculo);
        } catch (DataAccessException ex) {
            throw new CreateEntityException(
                "Vehiculo",
                "Error al crear Vehiculo en la base de datos: " + ex.getMostSpecificCause().getMessage()
            );
        }
    }
}
